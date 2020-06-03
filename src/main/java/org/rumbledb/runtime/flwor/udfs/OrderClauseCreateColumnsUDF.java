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
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.api.java.UDF2;
import org.joda.time.Instant;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.module.FunctionOrVariableName;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.NullItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.types.ItemType;
import scala.collection.mutable.WrappedArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;
    private Map<FunctionOrVariableName, DynamicContext.VariableDependency> dependencies;

    private Map<String, List<String>> columnNamesByType;
    private Map<Integer, String> allColumnTypes;

    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext parentContext;
    private DynamicContext context;
    private List<Object> results;

    private transient Kryo kryo;
    private transient Input input;

    public OrderClauseCreateColumnsUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            Map<Integer, String> allColumnTypes,
            Map<String, List<String>> columnNamesByType
    ) {
        this.expressionsWithIterator = expressionsWithIterator;
        this.allColumnTypes = allColumnTypes;

        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();
        this.parentContext = context;
        this.context = new DynamicContext(this.parentContext);
        this.results = new ArrayList<>();

        this.dependencies = new TreeMap<>();
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
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
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

        for (int expressionIndex = 0; expressionIndex < this.expressionsWithIterator.size(); expressionIndex++) {
            OrderByClauseAnnotatedChildIterator expressionWithIterator = this.expressionsWithIterator.get(
                expressionIndex
            );

            // nulls and empty sequences have special ordering captured in the first sorting column
            // if non-null, non-empty-sequence value is given, the second column is used to sort the input
            // indices are assigned to each value type for the first column
            int emptySequenceOrderIndex = 1; // by default, empty sequence is taken as first(=least)
            int nullOrderIndex = 2; // null is the smallest value except empty sequence(default)
            int valueOrderIndex = 3; // values are larger than null and empty sequence(default)
            if (expressionWithIterator.getEmptyOrder() == OrderByClauseSortingKey.EMPTY_ORDER.LAST) {
                emptySequenceOrderIndex = 4;
            }


            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this.context);
            boolean isEmptySequence = true;
            while (iterator.hasNext()) {
                isEmptySequence = false;
                Item nextItem = iterator.next();
                if (nextItem instanceof NullItem) {
                    this.results.add(nullOrderIndex);
                    this.results.add(null); // placeholder for valueColumn(2nd column)
                } else {
                    // any other atomic type
                    this.results.add(valueOrderIndex);

                    // extract type information for the sorting column
                    String typeName = this.allColumnTypes.get(expressionIndex);
                    try {
                        if (typeName.equals(ItemType.booleanItem.getName())) {
                            this.results.add(nextItem.getBooleanValue());
                        } else if (typeName.equals(ItemType.stringItem.getName())) {
                            this.results.add(nextItem.getStringValue());
                        } else if (typeName.equals(ItemType.integerItem.getName())) {
                            this.results.add(nextItem.castToIntegerValue());
                        } else if (typeName.equals(ItemType.doubleItem.getName())) {
                            this.results.add(nextItem.castToDoubleValue());
                        } else if (typeName.equals(ItemType.decimalItem.getName())) {
                            this.results.add(nextItem.castToDecimalValue());
                        } else if (
                            typeName.equals(ItemType.durationItem.getName())
                                || typeName.equals(ItemType.yearMonthDurationItem.getName())
                                || typeName.equals(ItemType.dayTimeDurationItem.getName())
                        ) {
                            this.results.add(
                                nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis()
                            );
                        } else if (
                            typeName.equals(ItemType.dateTimeItem.getName())
                                || typeName.equals(ItemType.dateItem.getName())
                                || typeName.equals(ItemType.timeItem.getName())
                        ) {
                            this.results.add(nextItem.getDateTimeValue().getMillis());
                        } else {
                            throw new OurBadException(
                                    "Unexpected ordering type found while creating columns."
                            );
                        }
                    } catch (RuntimeException e) {
                        throw new OurBadException(
                                "Invalid sort key: cannot compare item of type "
                                    + typeName
                                    + " with item of type "
                                    + nextItem.getDynamicType().toString()
                                    + "."
                        );
                    }
                }
            }
            if (isEmptySequence) {
                this.results.add(emptySequenceOrderIndex);
                this.results.add(null); // placeholder for valueColumn(2nd column)
            }
            iterator.close();

        }
        return RowFactory.create(this.results.toArray());
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
