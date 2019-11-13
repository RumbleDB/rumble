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

package sparksoniq.spark.iterator.function;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.rumbledb.api.Item;

import sparksoniq.io.json.RowToItemMapper;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class ParquetFileFunctionIterator extends SparkFunctionCallIterator {

	private static final long serialVersionUID = 1L;

	public ParquetFileFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        long resultSize = this.getRDD(_currentDynamicContext).count();
        if (resultSize == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public boolean isDataFrame() {
        return true;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        RuntimeIterator urlIterator = this._children.get(0);
        urlIterator.open(context);
        Dataset<Row> rows = SparkSessionManager.getInstance().getOrCreateSession()
                .read().parquet(urlIterator.next().getStringValue());
        urlIterator.close();
        return rows;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        if (this._rdd == null) {
            Dataset<Row> rows;
            RuntimeIterator urlIterator = this._children.get(0);
            urlIterator.open(context);
        	rows = SparkSessionManager.getInstance().getOrCreateSession()
        	    .read().parquet(urlIterator.next().getStringValue());
            JavaRDD<Row> rowrdd = rows.javaRDD();
            _rdd = rowrdd.map(new RowToItemMapper(getMetadata()));
            urlIterator.close();
        }
        return _rdd;
    }

}
