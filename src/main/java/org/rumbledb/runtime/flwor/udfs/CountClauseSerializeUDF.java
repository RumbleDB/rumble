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

package org.rumbledb.runtime.flwor.udfs;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountClauseSerializeUDF implements UDF1<Long, byte[]> {


    private static final long serialVersionUID = 1L;

    private List<Item> nextResult;

    private transient Kryo kryo;
    private transient Output output;

    public CountClauseSerializeUDF() {
        this.nextResult = new ArrayList<>();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
    }

    @Override
    public byte[] call(Long countIndex) {
        this.nextResult.clear();
        this.nextResult.add(new IntegerItem(countIndex.intValue()));

        return FlworDataFrameUtils.serializeItemList(this.nextResult, this.kryo, this.output);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
    }
}
