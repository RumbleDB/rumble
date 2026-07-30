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
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class FlworTuple implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private LinkedHashMap<Name, List<Item>> localVariables;
    private LinkedHashMap<Name, JavaRDD<Item>> rddVariables;
    private LinkedHashMap<Name, JSoundDataFrame> dataFrameVariables;
    private RumbleRuntimeConfiguration configuration;

    public FlworTuple() {
        this.localVariables = new LinkedHashMap<>(1, 1);
        this.rddVariables = new LinkedHashMap<>(1, 1);
        this.dataFrameVariables = new LinkedHashMap<>(1, 1);
    }

    public FlworTuple(RumbleRuntimeConfiguration configuration) {
        this.localVariables = new LinkedHashMap<>(1, 1);
        this.rddVariables = new LinkedHashMap<>(1, 1);
        this.dataFrameVariables = new LinkedHashMap<>(1, 1);
        this.configuration = configuration;
    }

    public FlworTuple(RumbleRuntimeConfiguration configuration, int nb) {
        this.localVariables = new LinkedHashMap<>(nb, 1);
        this.rddVariables = new LinkedHashMap<>(nb, 1);
        this.dataFrameVariables = new LinkedHashMap<>(nb, 1);
        this.configuration = configuration;
    }

    /**
     * Deep copy constructor
     */
    public FlworTuple(FlworTuple toCopy) {
        this.localVariables = new LinkedHashMap<>(toCopy.localVariables.size(), 1);
        this.rddVariables = new LinkedHashMap<>(toCopy.rddVariables.size(), 1);
        this.dataFrameVariables = new LinkedHashMap<>(toCopy.dataFrameVariables.size(), 1);
        for (Name key : toCopy.localVariables.keySet()) {
            this.putValue(key, toCopy.localVariables.get(key));
        }
        for (Name key : toCopy.rddVariables.keySet()) {
            this.putValue(key, toCopy.rddVariables.get(key));
        }
        for (Name key : toCopy.dataFrameVariables.keySet()) {
            this.putValue(key, toCopy.dataFrameVariables.get(key));
        }
        this.configuration = toCopy.configuration;
    }

    public Set<Name> getLocalKeys() {
        return this.localVariables.keySet();
    }

    public Set<Name> getRDDKeys() {
        return this.rddVariables.keySet();
    }

    public Set<Name> getDataFrameKeys() {
        return this.dataFrameVariables.keySet();
    }

    public boolean contains(Name key) {
        return this.localVariables.containsKey(key)
            || this.rddVariables.containsKey(key)
            || this.dataFrameVariables.containsKey(key);
    }

    public boolean isRDD(Name key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return this.rddVariables.containsKey(key)
            || this.dataFrameVariables.containsKey(key);
    }

    public boolean isDataFrame(Name key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return this.dataFrameVariables.containsKey(key);
    }

    public List<Item> getLocalValue(Name key, ExceptionMetadata metadata) {
        if (this.localVariables.containsKey(key)) {
            return this.localVariables.get(key);
        }
        if (this.rddVariables.containsKey(key)) {
            JavaRDD<Item> rdd = this.getRDDValue(key, metadata);
            return HybridRuntimeIterator.collectRDDwithLimit(rdd, this.configuration, metadata);
        }

        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JavaRDD<Item> getRDDValue(Name key, ExceptionMetadata metadata) {
        if (this.rddVariables.containsKey(key)) {
            return this.rddVariables.get(key);
        }
        if (this.dataFrameVariables.containsKey(key)) {
            JSoundDataFrame df = this.dataFrameVariables.get(key);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata, df.getItemType()));
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JSoundDataFrame getDataFrameValue(Name key, ExceptionMetadata metadata) {
        if (this.dataFrameVariables.containsKey(key)) {
            return this.dataFrameVariables.get(key);
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public void putValue(Name key, Item value) {
        List<Item> itemList = new ArrayList<>(1);
        itemList.add(value);
        this.putValue(key, itemList);
    }

    public FlworTuple putValue(Name key, List<Item> value) {
        this.rddVariables.remove(key);
        this.dataFrameVariables.remove(key);
        this.localVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(Name key, JavaRDD<Item> value) {
        this.localVariables.remove(key);
        this.dataFrameVariables.remove(key);
        this.rddVariables.put(key, value);
        return this;
    }

    public FlworTuple putValue(Name key, JSoundDataFrame value) {
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
        kryo.writeObject(output, this.configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.localVariables = kryo.readObject(input, LinkedHashMap.class);
        this.rddVariables = kryo.readObject(input, LinkedHashMap.class);
        this.dataFrameVariables = kryo.readObject(input, LinkedHashMap.class);
        this.configuration = kryo.readObject(input, RumbleRuntimeConfiguration.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Local:\n");
        for (Name s : this.localVariables.keySet()) {
            sb.append("    ");
            sb.append(s);
        }
        sb.append("\n  RDD:\n");
        for (Name s : this.rddVariables.keySet()) {
            sb.append("    ");
            sb.append(s);
        }
        sb.append("\n  DataFrame:\n");
        for (Name s : this.dataFrameVariables.keySet()) {
            sb.append("    ");
            sb.append(s);
        }
        return sb.toString();
    }
}
