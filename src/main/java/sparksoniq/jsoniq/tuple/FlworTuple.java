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

import sparksoniq.exceptions.OurBadException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.io.json.RowToItemMapper;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
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
    private LinkedHashMap<String, Dataset<Row>> dfVariables;

    public FlworTuple() {
        localVariables = new LinkedHashMap<>(1, 1);
        rddVariables = new LinkedHashMap<>(1, 1);
        dfVariables = new LinkedHashMap<>(1, 1);
    }

    public FlworTuple(int nb) {
        localVariables = new LinkedHashMap<>(nb, 1);
        rddVariables = new LinkedHashMap<>(nb, 1);
        dfVariables = new LinkedHashMap<>(nb, 1);
    }

    /**
     * Deep copy constructor
     */
    public FlworTuple(FlworTuple toCopy) {
        localVariables = new LinkedHashMap<>(toCopy.localVariables.size(), 1);
        rddVariables = new LinkedHashMap<>(toCopy.rddVariables.size(), 1);
        dfVariables = new LinkedHashMap<>(toCopy.dfVariables.size(), 1);
        for (String key : toCopy.localVariables.keySet()) {
            this.putValue(key, toCopy.localVariables.get(key));
        }
        for (String key : toCopy.rddVariables.keySet()) {
            this.putValue(key, toCopy.rddVariables.get(key));
        }
        for (String key : toCopy.dfVariables.keySet()) {
            this.putValue(key, toCopy.dfVariables.get(key));
        }
    }

    public Set<String> getLocalKeys() {
        return localVariables.keySet();
    }

    public Set<String> getRDDKeys() {
        return rddVariables.keySet();
    }

    public Set<String> getDFKeys() {
        return dfVariables.keySet();
    }

    public boolean contains(String key) {
        return localVariables.containsKey(key)
            || rddVariables.containsKey(key)
            || dfVariables.containsKey(key);
    }

    public boolean isRDD(String key, IteratorMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return rddVariables.containsKey(key)
            || dfVariables.containsKey(key);
    }

    public boolean isDF(String key, IteratorMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return dfVariables.containsKey(key);
    }

    public List<Item> getLocalValue(String key, IteratorMetadata metadata) {
        if (localVariables.containsKey(key)) {
            return localVariables.get(key);
        }
        if (rddVariables.containsKey(key)) {
            JavaRDD<Item> rdd = this.getRDDValue(key, metadata);
            return SparkSessionManager.collectRDDwithLimit(rdd);
        }

        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JavaRDD<Item> getRDDValue(String key, IteratorMetadata metadata) {
        if (rddVariables.containsKey(key)) {
            return rddVariables.get(key);
        }
        if (dfVariables.containsKey(key)) {
            Dataset<Row> df = dfVariables.get(key);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata));
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public Dataset<Row> getDFValue(String key, IteratorMetadata metadata) {
        if (dfVariables.containsKey(key)) {
            return dfVariables.get(key);
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public void putValue(String key, Item value) {
        List<Item> itemList = new ArrayList<>(1);
        itemList.add(value);
        this.putValue(key, itemList);
    }

    public FlworTuple putValue(String key, List<Item> value) {
        rddVariables.remove(key);
        dfVariables.remove(key);
        localVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(String key, JavaRDD<Item> value) {
        localVariables.remove(key);
        dfVariables.remove(key);
        rddVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(String key, Dataset<Row> value) {
        localVariables.remove(key);
        rddVariables.remove(key);
        dfVariables.put(key, value);
        return this;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, localVariables);
        kryo.writeObject(output, rddVariables);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        localVariables = kryo.readObject(input, LinkedHashMap.class);
        rddVariables = kryo.readObject(input, LinkedHashMap.class);
    }
}
