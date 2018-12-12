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

import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.spark.closures.LetClauseMapClosure;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.List;

public class LetClauseSparkIterator extends FlowrClauseSparkIterator {
    public LetClauseSparkIterator(VariableReferenceIterator variableReference, RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(null, FLWOR_CLAUSES.LET, iteratorMetadata);
        this._children.add(variableReference);
        this._children.add(expression);
    }
    
    @Override
    public JavaRDD<FlworTuple> getTupleRDD() {
        if (this._rdd == null) {
            VariableReferenceIterator variableReference = (VariableReferenceIterator)this._children.get(0);
            RuntimeIterator expression = this._children.get(1);
            //if it's not a start clause
            if (this._previousClause != null) {
                this._rdd = _previousClause.getTupleRDD();
                String variableName = variableReference.getVariableName();
                this._rdd = this._rdd.map(new LetClauseMapClosure(variableName, expression));
            } else {
                //if it's a start clause
                _rdd = this.getNewRDDFromExpression(expression);
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
            tuple.putValue(((VariableReferenceIterator)this._children.get(0)).getVariableName(), contents, false);
            tuples.add(tuple);
            rdd = SparkContextManager.getInstance().getContext().parallelize(tuples);
        }
        return rdd;
    }
}
