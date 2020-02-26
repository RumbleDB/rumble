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

package sparksoniq.spark.udf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.api.java.UDF2;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;

import scala.collection.mutable.WrappedArray;
import sparksoniq.semantics.DynamicContext;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForClauseUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, List<byte[]>> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<String, List<String>> columnNamesByType;
    private RuntimeIterator expression;
    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext context;
    private List<Item> nextResult;
    private List<byte[]> results;

    private transient Kryo kryo;
    private transient Output output;
    private transient Input input;

    public ForClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
        this.expression = expression;
        this.columnNamesByType = columnNamesByType;

        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();

        this.context = new DynamicContext(context);
        this.nextResult = new ArrayList<>();
        this.results = new ArrayList<>();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }


    @Override
    public List<byte[]> call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.deserializedParams.clear();
        this.context.removeAllVariables();
        this.results.clear();

        FlworDataFrameUtils.deserializeWrappedParameters(
            wrappedParameters,
            this.deserializedParams,
            this.kryo,
            this.input
        );

        // Long parameters correspond to pre-computed counts, when a materialization of the
        // actual sequence was avoided upfront.
        Object[] longParams = (Object[]) wrappedParametersLong.array();
        for (Object longParam : longParams) {
            Item count = ItemFactory.getInstance().createIntegerItem(((Long) longParam).intValue());
            this.longParams.add(count);
        }

        FlworDataFrameUtils.prepareDynamicContext(
            this.context,
            this.columnNamesByType.get("byte[]"),
            this.columnNamesByType.get("Long"),
            this.deserializedParams,
            this.longParams
        );

        // apply expression in the dynamic context
        this.expression.open(this.context);
        while (this.expression.hasNext()) {
            this.nextResult.clear();
            Item nextItem = this.expression.next();
            this.nextResult.add(nextItem);
            this.results.add(FlworDataFrameUtils.serializeItemList(this.nextResult, this.kryo, this.output));
        }
        this.expression.close();

        return this.results;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }
}
