/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.object;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ObjectKeysFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults; // queue that holds the results created by the current item in inspection
    private List<Item> _alreadyFoundKeys;

    public ObjectKeysFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this._iterator = arguments.get(0);
    }

    @Override
    public void openLocal() {
        this._alreadyFoundKeys = new ArrayList<>();
        this._nextResults = new LinkedList<>();

        if (this._iterator.isDataFrame()) {
            setResultsFromDF();
        } else {
            this._iterator.open(this._currentDynamicContextForLocalExecution);
            setResultsFromNextObjectItem();
        }
    }

    private void setResultsFromDF() {
        Dataset<Row> childDF = this._iterator.getDataFrame(this._currentDynamicContextForLocalExecution);
        String[] keys = childDF.schema().fieldNames();
        for (String key : keys) {
            this._nextResults.add(ItemFactory.getInstance().createStringItem(key));
        }
    }

    private void setResultsFromNextObjectItem() {
        while (this._iterator.hasNext()) {
            Item item = this._iterator.next();
            if (item.isObject()) { // ignore non-object items
                Item result;
                for (String key : item.getKeys()) {
                    result = ItemFactory.getInstance().createStringItem(key);
                    if (!this._alreadyFoundKeys.contains(result)) {
                        this._alreadyFoundKeys.add(result);
                        this._nextResults.add(result);
                    }
                }
                if (!this._nextResults.isEmpty()) {
                    break;
                }
            }
        }

        if (this._nextResults.isEmpty()) {
            this._hasNext = false;
            this._iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = this._nextResults.remove();
            if (this._nextResults.isEmpty()) {
                if (this._iterator.isDataFrame()) {
                    this._hasNext = false;
                } else {
                    setResultsFromNextObjectItem();
                }
            }
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this._alreadyFoundKeys = new ArrayList<>();
        this._nextResults = new LinkedList<>();

        if (this._iterator.isDataFrame()) {
            setResultsFromDF();
        } else {
            this._iterator.reset(this._currentDynamicContextForLocalExecution);
            setResultsFromNextObjectItem();
        }
    }

    @Override
    protected void closeLocal() {
        this._iterator.close();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this._iterator.getRDD(context);
        FlatMapFunction<Item, Item> transformation = new ObjectKeysClosure();
        return childRDD.flatMap(transformation).distinct();
    }
}
