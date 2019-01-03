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
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.jsoniq.runtime.iterator.SparkRuntimeIterator;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.List;

public class FlworExpressionSparkRuntimeIterator extends SparkRuntimeIterator {
    private final ReturnClauseSparkIterator _returnClause;
    private List<FlowrClauseSparkIterator> _clauses;

    public FlworExpressionSparkRuntimeIterator(RuntimeTupleIterator startClause,
                                               List<RuntimeTupleIterator> contentClauses,
                                               RuntimeIterator returnClause,
                                               IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        if(((FlowrClauseSparkIterator)startClause).getClauseType() != FLWOR_CLAUSES.FOR
                && ((FlowrClauseSparkIterator)startClause).getClauseType() != FLWOR_CLAUSES.LET)
            throw new SparksoniqRuntimeException("FLOWR clauses must start with a for/let clause");
        this._returnClause = (ReturnClauseSparkIterator)returnClause;

    }

    @Override public void open(DynamicContext context){
        _returnClause.open(context);
    }

    @Override
    public Item next(){
        return _returnClause.next();
    }

    @Override public void reset(DynamicContext context){
        _returnClause.reset(context);
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        return _returnClause.getRDD(dynamicContext);
    }
}
