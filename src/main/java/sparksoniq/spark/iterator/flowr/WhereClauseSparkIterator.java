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
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.spark.closures.WhereClauseClosure;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import org.apache.spark.api.java.JavaRDD;

public class WhereClauseSparkIterator extends FlowrClauseSparkIterator {
    public WhereClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator whereExpression, IteratorMetadata iteratorMetadata) {
        super(child, null, FLWOR_CLAUSES.WHERE, iteratorMetadata);
        this._children.add(whereExpression);
    }

    @Override
    public JavaRDD<FlworTuple> getRDD() {
        if (this._rdd == null) {
            RuntimeIterator expression = this._children.get(0);
            if (this._child != null) {
                this._rdd = _child.getRDD();
                this._rdd = this._rdd.filter(new WhereClauseClosure(expression));
            } else {
                throw new SparksoniqRuntimeException("Invalid where clause");
            }
        }
        return _rdd;
    }
}
