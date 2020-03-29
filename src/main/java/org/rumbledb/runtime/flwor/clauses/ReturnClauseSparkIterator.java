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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.closures.ReturnFlatMapClosure;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.Collections;
import java.util.Map;
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
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDD()) {
            throw new JobWithinAJobException(
                    "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }


        Dataset<Row> df = this.child.getDataFrame(context, expression.getVariableDependencies());
        StructType oldSchema = df.schema();
        return df.javaRDD().flatMap(new ReturnFlatMapClosure(expression, context, oldSchema));
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
            this.tupleContext.removeAllVariables(); // clear the previous variables
            this.tupleContext.setBindingsFromTuple(tuple, getMetadata()); // assign new variables from new tuple

            this.expression.open(this.tupleContext);
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        // execution reaches here when there are no more results
        this.child.close();
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
        this.expression.close();
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.child.reset(this.currentDynamicContextForLocalExecution);
        this.expression.close();
        setNextResult();
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.expression.getVariableDependencies());
        for (String variable : this.child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(variable);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getName());
        buffer.append("\n");
        this.child.print(buffer, indent + 1);
        this.expression.print(buffer, indent + 1);
    }
}
