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

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class FlworTuple implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private LinkedHashMap<Name, List<Item>> localVariables;
    private LinkedHashMap<Name, JavaRDD<Item>> rddVariables;
    private LinkedHashMap<Name, JSoundDataFrame> dataFrameVariables;
    private int initialCapacity = 1;
    private RumbleRuntimeConfiguration configuration;

    public FlworTuple() {
    }

    public FlworTuple(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
    }

    public FlworTuple(RumbleRuntimeConfiguration configuration, int nb) {
        this.initialCapacity = nb;
        this.configuration = configuration;
    }

    /**
     * Deep copy constructor
     */
    public FlworTuple(FlworTuple toCopy) {
        this.initialCapacity = toCopy.initialCapacity;
        if (toCopy.localVariables != null) {
            for (Name key : toCopy.localVariables.keySet()) {
                this.putValue(key, toCopy.localVariables.get(key));
            }
        }
        if (toCopy.rddVariables != null) {
            for (Name key : toCopy.rddVariables.keySet()) {
                this.putValue(key, toCopy.rddVariables.get(key));
            }
        }
        if (toCopy.dataFrameVariables != null) {
            for (Name key : toCopy.dataFrameVariables.keySet()) {
                this.putValue(key, toCopy.dataFrameVariables.get(key));
            }
        }
        this.configuration = toCopy.configuration;
    }

    private LinkedHashMap<Name, List<Item>> localVariables() {
        if (this.localVariables == null) {
            this.localVariables = new LinkedHashMap<>(this.initialCapacity, 1);
        }
        return this.localVariables;
    }

    private LinkedHashMap<Name, JavaRDD<Item>> rddVariables() {
        if (this.rddVariables == null) {
            this.rddVariables = new LinkedHashMap<>(this.initialCapacity, 1);
        }
        return this.rddVariables;
    }

    private LinkedHashMap<Name, JSoundDataFrame> dataFrameVariables() {
        if (this.dataFrameVariables == null) {
            this.dataFrameVariables = new LinkedHashMap<>(this.initialCapacity, 1);
        }
        return this.dataFrameVariables;
    }

    private static boolean has(LinkedHashMap<Name, ?> map, Name key) {
        return map != null && map.containsKey(key);
    }

    private static <V> Set<Name> keys(LinkedHashMap<Name, V> map) {
        return (map == null || map.isEmpty()) ? Collections.emptySet() : map.keySet();
    }

    public Set<Name> getLocalKeys() {
        return keys(this.localVariables);
    }

    public Set<Name> getRDDKeys() {
        return keys(this.rddVariables);
    }

    public Set<Name> getDataFrameKeys() {
        return keys(this.dataFrameVariables);
    }

    public boolean contains(Name key) {
        return has(this.localVariables, key)
            || has(this.rddVariables, key)
            || has(this.dataFrameVariables, key);
    }

    public boolean isRDD(Name key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return has(this.rddVariables, key)
            || has(this.dataFrameVariables, key);
    }

    public boolean isDataFrame(Name key, ExceptionMetadata metadata) {
        if (!contains(key)) {
            throw new OurBadException("Undeclared FLWOR variable", metadata);
        }
        return has(this.dataFrameVariables, key);
    }

    public List<Item> getLocalValue(Name key, ExceptionMetadata metadata) {
        if (has(this.localVariables, key)) {
            return this.localVariables.get(key);
        }
        if (has(this.rddVariables, key)) {
            JavaRDD<Item> rdd = this.getRDDValue(key, metadata);
            return HybridRuntimeIterator.collectRDDwithLimit(rdd, this.configuration, metadata);
        }

        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JavaRDD<Item> getRDDValue(Name key, ExceptionMetadata metadata) {
        if (has(this.rddVariables, key)) {
            return this.rddVariables.get(key);
        }
        if (has(this.dataFrameVariables, key)) {
            JSoundDataFrame df = this.dataFrameVariables.get(key);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata, df.getItemType()));
        }
        throw new OurBadException("Undeclared FLOWR variable", metadata);
    }

    public JSoundDataFrame getDataFrameValue(Name key, ExceptionMetadata metadata) {
        if (has(this.dataFrameVariables, key)) {
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
        remove(this.rddVariables, key);
        remove(this.dataFrameVariables, key);
        localVariables().put(key, value);
        return this;
    }

    public FlworTuple putValue(Name key, JavaRDD<Item> value) {
        remove(this.localVariables, key);
        remove(this.dataFrameVariables, key);
        rddVariables().put(key, value);
        return this;
    }

    public FlworTuple putValue(Name key, JSoundDataFrame value) {
        remove(this.localVariables, key);
        remove(this.rddVariables, key);
        dataFrameVariables().put(key, value);
        return this;
    }

    private static void remove(LinkedHashMap<Name, ?> map, Name key) {
        if (map != null) {
            map.remove(key);
        }
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Local:\n");
        for (Name s : getLocalKeys()) {
            sb.append("    ");
            sb.append(s);
        }
        sb.append("\n  RDD:\n");
        for (Name s : getRDDKeys()) {
            sb.append("    ");
            sb.append(s);
        }
        sb.append("\n  DataFrame:\n");
        for (Name s : getDataFrameKeys()) {
            sb.append("    ");
            sb.append(s);
        }
        return sb.toString();
    }
}
