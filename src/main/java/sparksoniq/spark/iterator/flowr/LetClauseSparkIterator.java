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
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.spark.closures.LetClauseMapClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.ArrayList;
import java.util.List;

public class LetClauseSparkIterator extends FlowrClauseSparkIterator {

    private VariableReferenceIterator _variableReference;
    private String _variableName;           // for efficient use in local iteration
    private RuntimeIterator _expression;
    private DynamicContext _tupleContext;   // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;

    public LetClauseSparkIterator(RuntimeTupleIterator child, VariableReferenceIterator variableReference, RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(child, null, FLWOR_CLAUSES.LET, iteratorMetadata);
        this._children.add(variableReference);
        this._children.add(expression);
        _variableReference = variableReference;
        _expression = expression;
    }

    @Override
    public boolean isRDD() {
        if (this._child == null) {
            return false;
        } else {
            return _child.isRDD();
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
        // if first let clause, there are no more tuples
        if (this._child == null) {
            this._hasNext = false;
            return;
        }

        if (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();
            _tupleContext.removeAllVariables();             // clear the previous variables
            _tupleContext.setBindingsFromTuple(inputTuple);      // assign new variables from new tuple

            List<Item> result = new ArrayList<>();
            _expression.open(_tupleContext);
            while (_expression.hasNext())
                result.add(_expression.next());
            _expression.close();

            FlworTuple resultTuple = new FlworTuple();
            resultTuple.putValue(_variableName, result, true);
            _nextLocalTupleResult = resultTuple;
            this._hasNext = true;
        } else {
            _child.close();
            this._hasNext = false;
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // evaluate locally
        if (!isRDD()) {
            if (this._child != null) { //if it's not a start clause
                _tupleContext = new DynamicContext(_currentDynamicContext);     // assign current context as parent
                _child.open(_tupleContext);
                _variableName = _variableReference.getVariableName();

                setNextLocalTupleResult();

            } else {    //if it's a start clause, it returns only one tuple
                if(_expression.isRDD())
                    throw new SparksoniqRuntimeException("Initial let clauses don't support RDDs");
                    // materialize the initial RDD
                else {
                    _tupleContext = new DynamicContext(_currentDynamicContext);
                    List<Item> contents = new ArrayList<>();
                    _expression.open(_tupleContext);
                    while (_expression.hasNext())
                        contents.add(_expression.next());
                    _expression.close();
                    FlworTuple tuple  = new FlworTuple();
                    tuple.putValue(_variableReference.getVariableName(), contents, false);
                    _nextLocalTupleResult = tuple;
                }
            }
        }
    }



    @Override
    public JavaRDD<FlworTuple> getRDD() {
        if (this._rdd == null) {
            //if it's not a start clause
            if (this._child != null) {
                this._rdd = _child.getRDD();
                String variableName = _variableReference.getVariableName();
                this._rdd = this._rdd.map(new LetClauseMapClosure(variableName, _expression));
            } else {
                //if it's a start clause
                _rdd = this.getNewRDDFromExpression(_expression);
            }
        }
        return _rdd;
    }
    
    private JavaRDD<FlworTuple> getNewRDDFromExpression(RuntimeIterator expression) {
        JavaRDD<FlworTuple> rdd;
        if(expression.isRDD())
            throw new SparksoniqRuntimeException("Initial let clauses don't support RDDs");
        else {
            List<Item> contents = new ArrayList<>();
            expression.open(this._currentDynamicContext);
            while (expression.hasNext())
                contents.add(expression.next());
            expression.close();
            List<FlworTuple> tuples = new ArrayList<>();
            FlworTuple tuple  = new FlworTuple();
            tuple.putValue(_variableReference.getVariableName(), contents, false);
            tuples.add(tuple);
            rdd = SparkContextManager.getInstance().getContext().parallelize(tuples);
        }
        return rdd;
    }


    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
    }

    @Override
    public void close() {
        if (_child != null) {
            _child.close();
        }
    }

}
