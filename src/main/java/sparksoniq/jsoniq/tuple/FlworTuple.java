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

package sparksoniq.jsoniq.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.RowToItemMapper;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class FlworTuple implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private LinkedHashMap<String, List<Item>> localVariables;
    private LinkedHashMap<String, JavaRDD<Item>> rddVariables;
    private LinkedHashMap<String, Dataset<Row>> dataFrameVariables;

    public FlworTuple() {
        this.localVariables = new LinkedHashMap<>(1, 1);
        this.rddVariables = new LinkedHashMap<>(1, 1);
        this.dataFrameVariables = new LinkedHashMap<>(1, 1);
    }

    public FlworTuple(int nb) {
        this.localVariables = new LinkedHashMap<>(nb, 1);
        this.rddVariables = new LinkedHashMap<>(nb, 1);
        this.dataFrameVariables = new LinkedHashMap<>(nb, 1);
    }

    /**
     * Deep copy constructor
     */
    public FlworTuple(FlworTuple toCopy) {
        this.localVariables = new LinkedHashMap<>(toCopy.localVariables.size(), 1);
        this.rddVariables = new LinkedHashMap<>(toCopy.rddVariables.size(), 1);
        this.dataFrameVariables = new LinkedHashMap<>(toCopy.dataFrameVariables.size(), 1);
        for (String key : toCopy.localVariables.keySet()) {
            this.putValue(key, toCopy.localVariables.get(key));
        }
        for (String key : toCopy.rddVariables.keySet()) {
            this.putValue(key, toCopy.rddVariables.get(key));
        }
        for (String key : toCopy.dataFrameVariables.keySet()) {
            this.putValue(key, toCopy.dataFrameVariables.get(key));
        }
    }

    public Set<String> getLocalKeys() {
        return this.localVariables.keySet();
    }

    public Set<String> getRDDKeys() {
        return this.rddVariables.keySet();
    }

    public Set<String> getDataFrameKeys() {
        return this.dataFrameVariables.keySet();
    }

    public boolean contains(String key) {
        return this.localVariables.containsKey(key)
            || this.rddVariables.containsKey(key)
            || this.dataFrameVariables.containsKey(key);
    }

    public boolean isRDD(String key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return this.rddVariables.containsKey(key)
            || this.dataFrameVariables.containsKey(key);
    }

    public boolean isDataFrame(String key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return this.dataFrameVariables.containsKey(key);
    }

    public List<Item> getLocalValue(String key, ExceptionMetadata metadata) {
        if (this.localVariables.containsKey(key)) {
            return this.localVariables.get(key);
        }
        if (this.rddVariables.containsKey(key)) {
            JavaRDD<Item> rdd = this.getRDDValue(key, metadata);
            return SparkSessionManager.collectRDDwithLimit(rdd, metadata);
        }

        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JavaRDD<Item> getRDDValue(String key, ExceptionMetadata metadata) {
        if (this.rddVariables.containsKey(key)) {
            return this.rddVariables.get(key);
        }
        if (this.dataFrameVariables.containsKey(key)) {
            Dataset<Row> df = this.dataFrameVariables.get(key);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata));
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public Dataset<Row> getDataFrameValue(String key, ExceptionMetadata metadata) {
        if (this.dataFrameVariables.containsKey(key)) {
            return this.dataFrameVariables.get(key);
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public void putValue(String key, Item value) {
        List<Item> itemList = new ArrayList<>(1);
        itemList.add(value);
        this.putValue(key, itemList);
    }

    public FlworTuple putValue(String key, List<Item> value) {
        this.rddVariables.remove(key);
        this.dataFrameVariables.remove(key);
        this.localVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(String key, JavaRDD<Item> value) {
        this.localVariables.remove(key);
        this.dataFrameVariables.remove(key);
        this.rddVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(String key, Dataset<Row> value) {
        this.localVariables.remove(key);
        this.rddVariables.remove(key);
        this.dataFrameVariables.put(key, value);
        return this;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.localVariables);
        kryo.writeObject(output, this.rddVariables);
        kryo.writeObject(output, this.dataFrameVariables);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.localVariables = kryo.readObject(input, LinkedHashMap.class);
        this.rddVariables = kryo.readObject(input, LinkedHashMap.class);
        this.dataFrameVariables = kryo.readObject(input, LinkedHashMap.class);
    }
}
