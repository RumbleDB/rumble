/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import sparksoniq.Main;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparkRuntimeException;
import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.io.json.RowToItemMapper;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public abstract class HybridRuntimeIterator extends RuntimeIterator {

    private static final long serialVersionUID = 1L;
    protected JiqsItemParser parser;
    protected List<Item> result = null;
    private int currentResultIndex = 0;
    private boolean isRDDInitialized = false;
    private boolean _isRDD;

    protected HybridRuntimeIterator(List<RuntimeIterator> children, IteratorMetadata iteratorMetadata) {
        super(children, iteratorMetadata);
        this.parser = new JiqsItemParser();
    }

    @Override
    public boolean isRDD() {
        if (!isRDDInitialized) {
            _isRDD = initIsRDD();
            isRDDInitialized = true;
        }
        return _isRDD;
    }

    @Override
    public boolean isDataFrame() {
        return false;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (!isRDD()) {
            openLocal();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (!_isRDD) {
            resetLocal(context);
            return;
        }
        result = null;
    }

    @Override
    public void close() {
        super.close();
        if (!_isRDD) {
            closeLocal();
            return;
        }
        result = null;
    }

    @Override
    public boolean hasNext() {
        if (!_isRDD) {
            return hasNextLocal();
        }
        if (result == null) {
            currentResultIndex = 0;
            JavaRDD<Item> rdd = this.getRDD(_currentDynamicContext);
            if (SparkSessionManager.LIMIT_COLLECT()) {
                result = rdd.take(SparkSessionManager.COLLECT_ITEM_LIMIT);
                if (result.size() == SparkSessionManager.COLLECT_ITEM_LIMIT) {
                    if (Main.terminal == null) {
                        System.out.println("Results have been truncated to:" + SparkSessionManager.COLLECT_ITEM_LIMIT
                                + " items. This value can be configured with the --result-size parameter at startup.\n");
                    } else {
                        Main.terminal.output("\nWarning: Results have been truncated to: " + SparkSessionManager.COLLECT_ITEM_LIMIT
                                + " items. This value can be configured with the --result-size parameter at startup.\n");
                    }
                }
            } else {
                result = rdd.collect();
            }
            _hasNext = !result.isEmpty();
        }
        return _hasNext;
    }

    @Override
    public Item next() {
        if (!_isRDD) {
            return nextLocal();
        }
        if (!this._isOpen)
            throw new IteratorFlowException("Runtime iterator is not open", getMetadata());

        if (!(currentResultIndex <= result.size() - 1))
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.getClass().getSimpleName(),
                    getMetadata());
        if (currentResultIndex == result.size() - 1)
            this._hasNext = false;

        Item item = result.get(currentResultIndex);
        currentResultIndex++;
        return item;
    }


    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {
        if (isDataFrame()) {
            Dataset<Row> df = this.getDataFrame(context);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(getMetadata()));
        } else {
            return getRDDAux(context);
        }
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        throw new SparkRuntimeException("DataFrames are not implemented for the iterator", getMetadata());
    }

    protected abstract JavaRDD<Item> getRDDAux(DynamicContext context);

    protected abstract boolean initIsRDD();

    protected abstract void openLocal();

    protected abstract void closeLocal();

    protected abstract void resetLocal(DynamicContext context);

    protected abstract boolean hasNextLocal();

    protected abstract Item nextLocal();
}
