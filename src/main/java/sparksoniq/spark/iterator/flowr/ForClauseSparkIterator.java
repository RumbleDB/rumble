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
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.spark.closures.ForClauseClosure;
import sparksoniq.spark.closures.InitialForClauseClosure;

import java.util.ArrayList;
import java.util.List;

public class ForClauseSparkIterator extends SparkRuntimeTupleIterator {

    private String _variableName;           // for efficient use in local iteration
    private RuntimeIterator _expression;
    private DynamicContext _tupleContext;   // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;
    private FlworTuple _inputTuple;     // tuple received from child, used for tuple creation

    public ForClauseSparkIterator(RuntimeTupleIterator child, VariableReferenceIterator variableReference,
                                  RuntimeIterator assignmentExpression, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _variableName = variableReference.getVariableName();
        _expression = assignmentExpression;
    }

    @Override
    public boolean isRDD() {
        return (_expression.isRDD() || (_child != null && _child.isRDD()));
    }

    @Override
    public FlworTuple next() {
        if(_hasNext == true){
            FlworTuple result = _nextLocalTupleResult;      // save the result to be returned
            // calculate and store the next result
            if (_child == null) {       // if it's the initial for clause, call the correct function
                initialFor_setNextLocalTupleResult();
            } else {
                setNextLocalTupleResult();
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        if (_expression.isOpen()) {
            if (_expression.hasNext()) {
                List<Item> values = new ArrayList<>();
                values.add(_expression.next());
                FlworTuple newTuple = new FlworTuple(_inputTuple, _variableName, values);
                _nextLocalTupleResult = newTuple;
                this._hasNext = true;
                return;
            } else {
                _expression.close();
            }
        }

        while (_child.hasNext()) {
            _inputTuple = _child.next();
            _tupleContext.removeAllVariables();             // clear the previous variables
            _tupleContext.setBindingsFromTuple(_inputTuple);      // assign new variables from new tuple

            _expression.open(_tupleContext);
            if (_expression.hasNext()) {     // if expression returns a value, set it as next
                List<Item> results = new ArrayList<>();
                results.add(_expression.next());
                FlworTuple newTuple = new FlworTuple(_inputTuple, _variableName, results);
                _nextLocalTupleResult = newTuple;
                this._hasNext = true;
                return;
            } else {
                _expression.close();
            }
        }

        // execution reaches here when there are no more results
        _child.close();
        this._hasNext = false;
    }

    private void initialFor_setNextLocalTupleResult() {
        if (_expression.hasNext()) {
            List<Item> results = new ArrayList<>();
            results.add(_expression.next());
            FlworTuple newTuple = new FlworTuple(_variableName, results);
            _nextLocalTupleResult = newTuple;
            this._hasNext = true;
        } else {
            _expression.close();
            this._hasNext = false;
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) { //if it's not a start clause
            _child.open(_currentDynamicContext);
            _tupleContext = new DynamicContext(_currentDynamicContext);     // assign current context as parent

            setNextLocalTupleResult();

        } else {    //if it's a start clause
            _expression.open(this._currentDynamicContext);

            initialFor_setNextLocalTupleResult();
        }
    }

    @Override
    public void close() {
        if (_child != null) {
            _child.close();
        }
    }


    @Override
    public JavaRDD<FlworTuple> getRDD() {
        if (this._rdd == null) {
            JavaRDD<Item> initialRdd = null;
            //if it's a start clause
            if (this._child == null) {
                initialRdd = this.getNewRDDFromExpression(_expression);
                this._rdd = initialRdd.map(new InitialForClauseClosure(_variableName));
            } else {
            //if it's not a start clause
                this._rdd = this._child.getRDD();
                this._rdd = this._rdd.flatMap(new ForClauseClosure(_expression, _variableName));
            }
        }
        return _rdd;
    }

    //used to generate inital RDD for start LET/FOR
    protected JavaRDD<Item> getNewRDDFromExpression(RuntimeIterator expression){
        JavaRDD<Item> rdd;
        if(expression.isRDD())
            rdd = expression.getRDD(_currentDynamicContext);
        else {
            List<Item> contents = new ArrayList<>();
            expression.open(this._currentDynamicContext);
            while (expression.hasNext())
                contents.add(expression.next());
            expression.close();
            rdd = SparkContextManager.getInstance().getContext().parallelize(contents);
        }
        return rdd;
    }
}
