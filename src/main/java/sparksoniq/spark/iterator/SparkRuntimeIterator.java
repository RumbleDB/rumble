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
 package sparksoniq.spark.iterator;

import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;

public abstract class SparkRuntimeIterator extends RuntimeIterator {

    protected SparkRuntimeIterator(List<RuntimeIterator> children, IteratorMetadata iteratorMetadata) {
        super(children, iteratorMetadata);
        this.parser = new JiqsItemParser();
    }

    @Override
    public boolean isRDD()
    {
        return true;
    }

    @Override
    public void reset(DynamicContext context){
        super.reset(context);
        result = null;
    }

    @Override
    public void close(){
        super.close();
        result = null;
    }

    @Override
    public Item next(){
        if(!this._isOpen)
            throw new IteratorFlowException("Runtime iterator is not open", getMetadata());

        if(result == null){
            currentResultIndex = 0;
            this._rdd = this.getRDD();
            if(SparkContextManager.LIMIT_COLLECT()) {
                result = _rdd.take(SparkContextManager.COLLECT_ITEM_LIMIT);
            }
            else
                result = _rdd.collect();
        }

        if(!(currentResultIndex <= result.size() - 1))
             throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.getClass().getSimpleName(),
                     getMetadata());
        if(currentResultIndex == result.size() - 1)
             this._hasNext = false;

        Item item = result.get(currentResultIndex);
        currentResultIndex++;
        return item;
    }

    protected JiqsItemParser parser;
    protected JavaRDD<Item> _rdd;
    protected List<Item> result = null;
    protected int currentResultIndex = 0;
}
