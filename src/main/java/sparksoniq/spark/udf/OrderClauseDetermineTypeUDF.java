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
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseAnnotatedChildIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderClauseDetermineTypeUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, List<String>> {
    private static final long serialVersionUID = 1L;
    private Map<String, DynamicContext.VariableDependency> dependencies;
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

        this.dependencies = new TreeMap<String, DynamicContext.VariableDependency>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            this.dependencies.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        this.columnNamesByType = columnNamesByType;

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }

    @Override
    public List<String> call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.deserializedParams.clear();
        this.context.removeAllVariables();
        this.result.clear();

        DataFrameUtils.deserializeWrappedParameters(
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

        DataFrameUtils.prepareDynamicContext(
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
                this.result.add("empty-sequence");
            } else if (this.nextItem.isNull()) {
                this.result.add("null");
            } else if (this.nextItem.isBoolean()) {
                this.result.add("boolean");
            } else if (this.nextItem.isString()) {
                this.result.add("string");
            } else if (this.nextItem.isInteger()) {
                this.result.add("integer");
            } else if (this.nextItem.isDouble()) {
                this.result.add("double");
            } else if (this.nextItem.isDecimal()) {
                this.result.add("decimal");
            } else if (this.nextItem.isYearMonthDuration()) {
                this.result.add("yearMonthDuration");
            } else if (this.nextItem.isDayTimeDuration()) {
                this.result.add("dayTimeDuration");
            } else if (this.nextItem.isDuration()) {
                this.result.add("duration");
            } else if (this.nextItem.isDateTime()) {
                this.result.add("dateTime");
            } else if (this.nextItem.isDate()) {
                this.result.add("date");
            } else if (this.nextItem.isTime()) {
                this.result.add("time");
            } else if (this.nextItem.isArray() || this.nextItem.isObject()) {
                throw new UnexpectedTypeException(
                        "Order by variable can not contain arrays or objects.",
                        expressionWithIterator.getIterator().getMetadata()
                );
            } else if (this.nextItem.isBinary()) {
                String itemType = ItemTypes.getItemTypeName(this.nextItem.getClass().getSimpleName());
                throw new UnexpectedTypeException(
                        "\""
                            + itemType
                            + "\": invalid type: can not compare for equality to type \""
                            + itemType
                            + "\"",
                        expressionWithIterator.getIterator().getMetadata()
                );
            } else {
                throw new OurBadException("Unexpected type found.");
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
        DataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }
}
