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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.spark.closures.ForClauseClosure;
import sparksoniq.spark.closures.InitialForClauseClosure;
import sparksoniq.spark.tuple.FlworTuple;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.List;

public class ForClauseSparkIterator extends FlowrClauseSparkIterator {

    public ForClauseSparkIterator(VariableReferenceIterator variableReference,
                                  RuntimeIterator assignmentExpression) {
        super(null, FLWOR_CLAUSES.FOR);
        this._children.add(variableReference);
        this._children.add(assignmentExpression);
    }


    @Override
    public JavaRDD<FlworTuple> getTupleRDD() {
        if (this._rdd == null) {
            String variableReference = ((VariableReferenceIterator)this._children.get(0)).getVariableName();
            RuntimeIterator assignmentExpression = this._children.get(1);
            JavaRDD<Item> initialRdd = null;
            //if it's a start clause
            if (this._previousClause == null) {
                initialRdd = this.getNewRDDFromExpression(assignmentExpression);
                this._rdd = initialRdd.map(new InitialForClauseClosure(variableReference));
            } else {
            //if it's not a start clause
                this._rdd = this._previousClause.getTupleRDD();
                this._rdd = this._rdd.flatMap(new ForClauseClosure(assignmentExpression, variableReference));
            }
        }
        return _rdd;
    }

    //used to generate inital RDD for start LET/FOR
    protected JavaRDD<Item> getNewRDDFromExpression(RuntimeIterator expression){
        JavaRDD<Item> rdd;
        if(expression.isRDD())
            rdd = expression.getRDD();
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
