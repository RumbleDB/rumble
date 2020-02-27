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
 * Author: Stefan Irimescu
 *
 */

package org.rumbledb.runtime.flwor.closures;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForClauseLocalTupleToRowClosure implements Function<Item, Row> {

    private static final long serialVersionUID = 1L;

    private final FlworTuple inputTuple;
    private final ExceptionMetadata metadata;

    private transient Kryo kryo;
    private transient Output output;

    public ForClauseLocalTupleToRowClosure(FlworTuple inputTuple, ExceptionMetadata metadata) {
        this.inputTuple = inputTuple;
        this.metadata = metadata;
        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
    }

    @Override
    public Row call(Item item) {
        List<List<Item>> rowColumns = new ArrayList<>();
        this.inputTuple.getLocalKeys()
            .forEach(key -> rowColumns.add(this.inputTuple.getLocalValue(key, this.metadata)));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        rowColumns.add(itemList);

        List<byte[]> serializedRowColumns = new ArrayList<>();
        for (List<Item> column : rowColumns) {
            serializedRowColumns.add(DataFrameUtils.serializeItemList(column, this.kryo, this.output));
        }

        return RowFactory.create(serializedRowColumns.toArray());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
    }
}
