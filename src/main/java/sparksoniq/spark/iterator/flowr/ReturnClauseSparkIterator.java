/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.ReturnFlatMapClosure;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReturnClauseSparkIterator extends HybridRuntimeIterator {

    private JavaRDD<Item> itemRDD;
    private RuntimeTupleIterator _child;
    private DynamicContext _tupleContext;   // re-use same DynamicContext object for efficiency
    private RuntimeIterator _expression;
    private Item _nextLocalResult;

    public ReturnClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(Arrays.asList(expression), iteratorMetadata);
        _child = child;
        _expression = expression;
    }

    @Override
    protected boolean initIsRDD() {
        return _child.isRDD();
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {

        RuntimeIterator expression = this._children.get(0);
        itemRDD = this._child.getRDD(context).flatMap(new ReturnFlatMapClosure(expression));
        return itemRDD;
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (_hasNext == true) {
            Item result = _nextLocalResult;  // save the result to be returned
            setNextLocalResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    @Override
    protected void openLocal(DynamicContext context) {
        _child.open(context);
        _tupleContext = new DynamicContext(_currentDynamicContext);     // assign current context as parent
        setNextLocalResult();
    }

    private void setNextLocalResult() {
        if (_expression.isOpen()) {
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        while (_child.hasNext()) {
            FlworTuple tuple = _child.next();
            _tupleContext.removeAllVariables();             // clear the previous variables
            _tupleContext.setBindingsFromTuple(tuple);      // assign new variables from new tuple

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
     * @return true if _nextLocalResult is set and _hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (_expression.hasNext()) {        // if expression returns a value, set it as next
            _nextLocalResult = _expression.next();
            this._hasNext = true;
            return true;
        } else {    // if not, keep iterating
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
        _child.reset(_currentDynamicContext);
        _expression.close();
        setNextLocalResult();
    }

    public Set<String> getVariableDependencies()
    {
        Set<String> result = new HashSet<String>();
        result.addAll(_expression.getVariableDependencies());
        result.removeAll(_child.getVariablesBoundInCurrentFLWORExpression());
        result.addAll(_child.getVariableDependencies());
        return result;
    }
    
    public void print(StringBuffer buffer, int indent)
    {
        for (int i = 0; i < indent; ++i)
        {
            buffer.append("  ");
        }
        buffer.append(getClass().getName());
        buffer.append("\n");
        _child.print(buffer, indent + 1);
        _expression.print(buffer, indent + 1);
    }
}
