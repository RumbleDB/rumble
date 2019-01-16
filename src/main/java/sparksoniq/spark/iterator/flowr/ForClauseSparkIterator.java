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
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
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
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.ArrayList;
import java.util.List;

public class ForClauseSparkIterator extends SparkRuntimeTupleIterator {

    private String _variableName;           // for efficient use in local iteration
    private RuntimeIterator _expression;

    public ForClauseSparkIterator(RuntimeTupleIterator child, VariableReferenceIterator variableReference,
                                  RuntimeIterator assignmentExpression, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _variableName = variableReference.getVariableName();
        _expression = assignmentExpression;
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
