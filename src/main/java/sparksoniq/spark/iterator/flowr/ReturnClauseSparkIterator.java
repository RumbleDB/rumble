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

package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.ReturnFlatMapClosure;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ReturnClauseSparkIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeTupleIterator _child;
    private DynamicContext _tupleContext; // re-use same DynamicContext object for efficiency
    private RuntimeIterator _expression;
    private Item _nextResult;

    public ReturnClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator expression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(expression), executionMode, iteratorMetadata);
        _child = child;
        _expression = expression;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator expression = this._children.get(0);
        if (expression.isRDD()) {
            throw new JobWithinAJobException(
                    "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata().getExceptionMetadata()
            );
        }


        Dataset<Row> df = this._child.getDataFrame(context, expression.getVariableDependencies());
        StructType oldSchema = df.schema();
        return df.javaRDD().flatMap(new ReturnFlatMapClosure(expression, context, oldSchema));
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (_hasNext) {
            Item result = _nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    @Override
    protected void openLocal() {
        _child.open(_currentDynamicContextForLocalExecution);
        _tupleContext = new DynamicContext(_currentDynamicContextForLocalExecution); // assign current context as parent
        setNextResult();
    }

    private void setNextResult() {
        if (_expression.isOpen()) {
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        while (_child.hasNext()) {
            FlworTuple tuple = _child.next();
            _tupleContext.removeAllVariables(); // clear the previous variables
            _tupleContext.setBindingsFromTuple(tuple, getMetadata()); // assign new variables from new tuple

            _expression.open(_tupleContext);
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        // execution reaches here when there are no more results
        _child.close();
        this._hasNext = false;
    }

    /**
     * _expression has to be open prior to call.
     *
     * @return true if _nextResult is set and _hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (_expression.hasNext()) { // if expression returns a value, set it as next
            _nextResult = _expression.next();
            this._hasNext = true;
            return true;
        } else { // if not, keep iterating
            _expression.close();
            return false;
        }
    }

    @Override
    protected void closeLocal() {
        _child.close();
        _expression.close();
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _child.reset(_currentDynamicContextForLocalExecution);
        _expression.close();
        setNextResult();
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<>(_expression.getVariableDependencies());
        for (String variable : _child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(variable);
        }
        result.putAll(_child.getVariableDependencies());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getName());
        buffer.append("\n");
        _child.print(buffer, indent + 1);
        _expression.print(buffer, indent + 1);
    }
}
