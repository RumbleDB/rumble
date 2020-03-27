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
import com.esotericsoftware.kryo.io.Input;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import scala.collection.mutable.WrappedArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereClauseUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Boolean> {
    private static final long serialVersionUID = 1L;
    private RuntimeIterator expression;
    private Map<String, DynamicContext.VariableDependency> dependencies;

    private Map<String, List<String>> columnNamesByType;

    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext context;
    private DynamicContext parentContext;

    private transient Kryo kryo;
    private transient Input input;

    public WhereClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            StructType inputSchema,
            Map<String, List<String>> columnNamesByType
    ) {
        this.expression = expression;

        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();
        this.parentContext = context;
        this.context = new DynamicContext(this.parentContext);

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();

        this.columnNamesByType = columnNamesByType;
        this.dependencies = this.expression.getVariableDependencies();

    }


    @Override
    public Boolean call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.deserializedParams.clear();
        this.context.removeAllVariables();

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
        boolean result = RuntimeIterator.getEffectiveBooleanValue(this.expression);
        this.expression.close();
        return result;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }
}
