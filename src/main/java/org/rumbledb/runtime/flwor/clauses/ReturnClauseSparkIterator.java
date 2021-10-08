/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.flwor.clauses;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.closures.ReturnFlatMapClosure;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReturnClauseSparkIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeTupleIterator child;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private RuntimeIterator expression;
    private Item nextResult;

    public ReturnClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator expression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(expression), executionMode, iteratorMetadata);
        this.child = child;
        this.expression = expression;
        setInputAndOutputTupleVariableDependencies();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );

            this.child.open(context);
            JavaRDD<Item> result = null;
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                               // from new tuple

                JavaRDD<Item> intermediateResult = this.expression.getRDD(dynamicContext);
                if (result == null) {
                    result = intermediateResult;
                } else {
                    result = result.union(intermediateResult);
                }
            }
            if (result == null) {
                return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
            }
            return result;
        }
        Dataset<Row> df = this.child.getDataFrame(context);
        StructType oldSchema = df.schema();
        List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
            oldSchema,
            this.expression.getVariableDependencies(),
            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null
        );
        return df.toJavaRDD().flatMap(new ReturnFlatMapClosure(expression, context, oldSchema, UDFcolumns));
    }

    private void setInputAndOutputTupleVariableDependencies() {
        Map<Name, VariableDependency> dependencies = this.expression.getVariableDependencies();
        Set<Name> allTupleNames = this.child.getOutputTupleVariableNames();
        Map<Name, VariableDependency> projection = new HashMap<>();
        for (Name n : dependencies.keySet()) {
            if (allTupleNames.contains(n)) {
                projection.put(n, dependencies.get(n));
            }
        }
        this.child.setInputAndOutputTupleVariableDependencies(projection);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            // context
            this.child.open(context);
            JSoundDataFrame result = null;
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                               // from new tuple

                JSoundDataFrame intermediateResult = this.expression.getDataFrame(dynamicContext);
                if (result == null) {
                    result = intermediateResult;
                } else {
                    result = result.union(intermediateResult);
                }
            }
            if (result == null) {
                return JSoundDataFrame.emptyDataFrame();
            }
            return result;
        }

        throw new OurBadException(
                "Unexpected application state: a dataframe was expected even though the return expression does not produce one.",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    @Override
    protected void openLocal() {
        this.child.open(this.currentDynamicContextForLocalExecution);
        this.tupleContext = new DynamicContext(this.currentDynamicContextForLocalExecution); // assign current context
        // as parent
        setNextResult();
    }

    private void setNextResult() {
        if (this.expression.isOpen()) {
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        while (this.child.hasNext()) {
            FlworTuple tuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                              // from new tuple

            this.expression.open(this.tupleContext);
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        // execution reaches here when there are no more results
        this.hasNext = false;
    }

    /**
     * expression has to be open prior to call.
     *
     * @return true if nextResult is set and hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (this.expression.hasNext()) { // if expression returns a value, set it as next
            this.nextResult = this.expression.next();
            this.hasNext = true;
            return true;
        } else { // if not, keep iterating
            this.expression.close();
            return false;
        }
    }

    @Override
    protected void closeLocal() {
        this.child.close();
        if (this.expression.isOpen()) {
            this.expression.close();
        }
    }

    @Override
    protected void resetLocal() {
        this.child.reset(this.currentDynamicContextForLocalExecution);
        if (this.expression.isOpen()) {
            this.expression.close();
        }
        this.tupleContext = new DynamicContext(this.currentDynamicContextForLocalExecution); // assign current context
        setNextResult();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.expression.getVariableDependencies());
        for (Name variable : this.child.getOutputTupleVariableNames()) {
            result.remove(variable);
        }
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | ");

        buffer.append("Variable dependencies: ");
        Map<Name, DynamicContext.VariableDependency> dependencies = getVariableDependencies();
        for (Name v : dependencies.keySet()) {
            buffer.append(v + "(" + dependencies.get(v) + ")" + " ");
        }
        buffer.append("\n");

        this.child.print(buffer, indent + 1);
        this.expression.print(buffer, indent + 1);
    }

    private void readObject(ObjectInputStream i) throws ClassNotFoundException, IOException {
        i.defaultReadObject();
        setInputAndOutputTupleVariableDependencies();
    }

    private void writeObject(ObjectOutputStream i) throws IOException {
        i.defaultWriteObject();
    }

}
