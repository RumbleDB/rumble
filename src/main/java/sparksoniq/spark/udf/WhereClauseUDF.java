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
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereClauseUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Boolean> {
    private static final long serialVersionUID = 1L;
    private RuntimeIterator _expression;
    private Map<String, DynamicContext.VariableDependency> _dependencies;

    private Map<String, List<String>> _columnNamesByType;

    private List<List<Item>> _deserializedParams;
    private List<Item> _longParams;
    private DynamicContext _context;
    private DynamicContext _parentContext;

    private transient Kryo _kryo;
    private transient Input _input;

    public WhereClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            StructType inputSchema,
            Map<String, List<String>> columnNamesByType
    ) {
        this._expression = expression;

        this._deserializedParams = new ArrayList<>();
        this._longParams = new ArrayList<>();
        this._parentContext = context;
        this._context = new DynamicContext(this._parentContext);

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();

        this._columnNamesByType = columnNamesByType;
        this._dependencies = this._expression.getVariableDependencies();

    }


    @Override
    public Boolean call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this._deserializedParams.clear();
        this._context.removeAllVariables();

        DataFrameUtils.deserializeWrappedParameters(
            wrappedParameters,
            this._deserializedParams,
            this._kryo,
            this._input
        );

        // Long parameters correspond to pre-computed counts, when a materialization of the
        // actual sequence was avoided upfront.
        Object[] longParams = (Object[]) wrappedParametersLong.array();
        for (Object longParam : longParams) {
            Item count = ItemFactory.getInstance().createIntegerItem(((Long) longParam).intValue());
            this._longParams.add(count);
        }

        DataFrameUtils.prepareDynamicContext(
            this._context,
            this._columnNamesByType.get("byte[]"),
            this._columnNamesByType.get("Long"),
            this._deserializedParams,
            this._longParams
        );

        // apply expression in the dynamic context
        this._expression.open(this._context);
        boolean result = RuntimeIterator.getEffectiveBooleanValue(this._expression);
        this._expression.close();
        return result;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();
    }
}
