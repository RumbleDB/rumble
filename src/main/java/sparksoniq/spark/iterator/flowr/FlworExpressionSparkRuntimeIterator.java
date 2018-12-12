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
                                               RuntimeTupleIterator returnClause,
                                               IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        _clauses = new ArrayList<>();
        if(startClause.getClauseType() != FLWOR_CLAUSES.FOR && startClause.getClauseType() != FLWOR_CLAUSES.LET)
            throw new SparksoniqRuntimeException("FLOWR clauses must start with a for/let clause");
        this._returnClause = returnClause;
        this._clauses.add(startClause);
        this._clauses.addAll(contentClauses);
        this._clauses.add(_returnClause);

    }

    @Override public void open(DynamicContext context){
        if(this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._hasNext = true;
        if(context == null)
            this._currentDynamicContext = new DynamicContext();
        else
            this._currentDynamicContext = new DynamicContext(context);
        this._clauses.forEach(clause -> {
                clause.setDynamicContext(_currentDynamicContext);
        });

        currentResultIndex = 0;
        this._rdd = this.getRDD();
        if(SparkContextManager.LIMIT_COLLECT()) {
            result = _rdd.take(SparkContextManager.COLLECT_ITEM_LIMIT);
        }
        else {
            result = _rdd.collect();
        }

        if (result.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public Item next(){
        if(!this._isOpen) {
            throw new IteratorFlowException("Runtime iterator is not open", getMetadata());
        }

        if (this._hasNext) {
            if(currentResultIndex == result.size() - 1)
                this._hasNext = false;

            Item item = result.get(currentResultIndex);
            currentResultIndex++;
            return item;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.getClass().getSimpleName(),
                getMetadata());
    }

    @Override public void reset(DynamicContext context){
        this._hasNext = true;
        this._currentDynamicContext = context;
        this._children.forEach(c -> c.reset(context));
        this._clauses.forEach(clause -> {
            clause.setDynamicContext(_currentDynamicContext);
        });
    }

    @Override
    public JavaRDD<Item> getRDD() {
        return _returnClause.getItemRDD();
    }
}
