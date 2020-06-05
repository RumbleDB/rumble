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
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseSparkIterator;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import scala.collection.mutable.WrappedArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderClauseDetermineTypeUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, List<String>> {
    private static final long serialVersionUID = 1L;
    private Map<Name, DynamicContext.VariableDependency> dependencies;
    private Map<String, List<String>> columnNamesByType;
    private List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;
    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext parentContext;
    private DynamicContext context;
    private Item nextItem;
    private List<String> result;

    private transient Kryo kryo;
    private transient Input input;

    public OrderClauseDetermineTypeUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
        this.expressionsWithIterator = expressionsWithIterator;

        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();
        this.parentContext = context;
        this.context = new DynamicContext(this.parentContext);
        this.result = new ArrayList<>();

        this.dependencies = new TreeMap<Name, DynamicContext.VariableDependency>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            this.dependencies.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        this.columnNamesByType = columnNamesByType;

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }

    @Override
    public List<String> call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.deserializedParams.clear();
        this.context.removeAllVariables();
        this.result.clear();

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

        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this.context);
            if (iterator.hasNext()) {
                this.nextItem = iterator.next();
                if (iterator.hasNext()) {
                    throw new UnexpectedTypeException(
                            "Can not order by variables with sequences of multiple items.",
                            expressionWithIterator.getIterator().getMetadata()
                    );
                }
            }
            iterator.close();

            if (this.nextItem == null) {
                this.result.add(OrderByClauseSparkIterator.StringFlagForEmptySequence);
            } else if (this.nextItem.isArray() || this.nextItem.isObject()) {
                throw new UnexpectedTypeException(
                        "Order by variable can not contain arrays or objects.",
                        expressionWithIterator.getIterator().getMetadata()
                );
            } else if (this.nextItem.isBinary()) {
                String itemType = this.nextItem.getDynamicType().toString();
                throw new UnexpectedTypeException(
                        "\""
                            + itemType
                            + "\": invalid type: can not compare for equality to type \""
                            + itemType
                            + "\"",
                        expressionWithIterator.getIterator().getMetadata()
                );
            } else {
                this.result.add(this.nextItem.getDynamicType().getName());
            }
        }
        return this.result;
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
