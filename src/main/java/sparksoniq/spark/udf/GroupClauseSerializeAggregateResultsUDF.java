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

package sparksoniq.spark.udf;

import org.apache.spark.sql.api.java.UDF1;

import com.esotericsoftware.kryo.Kryo;

import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupClauseSerializeAggregateResultsUDF implements UDF1<WrappedArray, byte[]> {

    private List<Item> _nextResult;
    private List<List<Item>> _deserializedParams;
    
    private transient Kryo _kryo;

    public GroupClauseSerializeAggregateResultsUDF() {
        _nextResult = new ArrayList<>();
        _deserializedParams = new ArrayList<>();
        
        _kryo = new Kryo();
        _kryo.register(Item.class);
        _kryo.register(ArrayItem.class);
        _kryo.register(ObjectItem.class);
        _kryo.register(StringItem.class);
        _kryo.register(IntegerItem.class);
        _kryo.register(DoubleItem.class);
        _kryo.register(DecimalItem.class);
        _kryo.register(NullItem.class);
        _kryo.register(BooleanItem.class);
        _kryo.register(ArrayList.class);
    }

    @Override
    public byte[] call(WrappedArray wrappedParameters) {
        _nextResult.clear();
        _deserializedParams.clear();
        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo);

        for (List<Item> deserializedParam : _deserializedParams) {
            _nextResult.addAll(deserializedParam);
        }
        return DataFrameUtils.serializeItemList(_nextResult);
    }
    
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        _kryo = new Kryo();
        _kryo.register(Item.class);
        _kryo.register(ArrayItem.class);
        _kryo.register(ObjectItem.class);
        _kryo.register(StringItem.class);
        _kryo.register(IntegerItem.class);
        _kryo.register(DoubleItem.class);
        _kryo.register(DecimalItem.class);
        _kryo.register(NullItem.class);
        _kryo.register(BooleanItem.class);
        _kryo.register(ArrayList.class);
    }
}
