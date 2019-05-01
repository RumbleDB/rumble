/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.WhereClauseClosure;

public class WhereClauseSparkIterator extends SparkRuntimeTupleIterator {

    private RuntimeIterator _expression;
    private DynamicContext _tupleContext;   // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;
    private FlworTuple _inputTuple;     // tuple received from child, used for tuple creation

    public WhereClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator whereExpression, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _expression = whereExpression;
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);
            _tupleContext = new DynamicContext(_currentDynamicContext);     // assign current context as parent

            setNextLocalTupleResult();

        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if(_hasNext == true){
            FlworTuple result = _nextLocalTupleResult;      // save the result to be returned
            setNextLocalTupleResult();              // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        // for each incoming tuple, evaluate the expression to a boolean.
        // forward if true, drop if false

        while (_child.hasNext()) {
            _inputTuple = _child.next();
            _tupleContext.removeAllVariables();             // clear the previous variables
            _tupleContext.setBindingsFromTuple(_inputTuple);      // assign new variables from new tuple

            _expression.open(_tupleContext);
            boolean effectiveBooleanValue = RuntimeIterator.getEffectiveBooleanValue(_expression);
            _expression.close();
            if (effectiveBooleanValue) {
                _nextLocalTupleResult = _inputTuple;
                this._hasNext = true;
                return;
            }
        }

        // execution reaches here when there are no more results
        _child.close();
        this._hasNext = false;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._child != null) {
            this._rdd = _child.getRDD(context);
            this._rdd = this._rdd.filter(new WhereClauseClosure(_expression));
        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
        return _rdd;
    }
}
