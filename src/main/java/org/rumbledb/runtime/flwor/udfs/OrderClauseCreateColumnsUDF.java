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
 * Authors: Stefan Irimescu, Can Berker Cikis, Ghislain Fourny
 *
 */

package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.api.java.UDF2;
import org.joda.time.Instant;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.items.NullItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.types.ItemType;
import scala.collection.mutable.WrappedArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private DataFrameContext dataFrameContext;
    private List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;

    private Map<Integer, String> sortingKeyTypes;

    private List<Object> results;

    // nulls and empty sequences have special ordering captured in the first sorting column
    // if non-null, non-empty-sequence value is given, the second column is used to sort the input
    // indices are assigned to each value type for the first column
    private static int emptySequenceOrderIndexFirst = 1; // by default, empty sequence is taken as first(=least)
    private static int emptySequenceOrderIndexLast = 4; // by default, empty sequence is taken as first(=least)
    private static int nullOrderIndex = 2; // null is the smallest value except empty sequence(default)
    private static int valueOrderIndex = 3; // values are larger than null and empty sequence(default)


    public OrderClauseCreateColumnsUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            Map<Integer, String> sortingKeyTypes,
            Map<String, List<String>> columnNamesByType
    ) {
        this.dataFrameContext = new DataFrameContext(context, columnNamesByType);
        this.expressionsWithIterator = expressionsWithIterator;
        this.sortingKeyTypes = sortingKeyTypes;

        this.results = new ArrayList<>();
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.dataFrameContext.setFromWrappedParameters(wrappedParameters, wrappedParametersLong);

        this.results.clear();

        for (int expressionIndex = 0; expressionIndex < this.expressionsWithIterator.size(); expressionIndex++) {
            OrderByClauseAnnotatedChildIterator expressionWithIterator = this.expressionsWithIterator.get(
                expressionIndex
            );

            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this.dataFrameContext.getContext());
            if (!iterator.hasNext()) {
                if (expressionWithIterator.getEmptyOrder() == OrderByClauseSortingKey.EMPTY_ORDER.LAST) {
                    this.results.add(emptySequenceOrderIndexLast);
                } else {
                    this.results.add(emptySequenceOrderIndexFirst);
                }
                this.results.add(null); // placeholder for valueColumn(2nd column)
                continue;
            }
            while (iterator.hasNext()) {
                Item nextItem = iterator.next();
                createColumnsForItem(nextItem, expressionIndex);
            }
            iterator.close();

        }
        return RowFactory.create(this.results.toArray());
    }



    private void createColumnsForItem(Item nextItem, int expressionIndex) {
        if (nextItem instanceof NullItem) {
            this.results.add(nullOrderIndex);
            this.results.add(null); // placeholder for valueColumn(2nd column)
        } else {
            // any other atomic type
            this.results.add(valueOrderIndex);

            // extract type information for the sorting column
            String typeName = this.sortingKeyTypes.get(expressionIndex);
            try {
                if (typeName.equals(ItemType.booleanItem.getName())) {
                    this.results.add(nextItem.getBooleanValue());
                } else if (typeName.equals(ItemType.stringItem.getName())) {
                    this.results.add(nextItem.getStringValue());
                } else if (typeName.equals(ItemType.integerItem.getName())) {
                    this.results.add(nextItem.castToIntValue());
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
}
