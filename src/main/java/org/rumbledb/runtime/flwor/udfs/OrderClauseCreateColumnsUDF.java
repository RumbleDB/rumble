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
import org.apache.spark.sql.api.java.UDF1;
import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.items.NullItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderClauseCreateColumnsUDF implements UDF1<Row, Row> {

    private static final long serialVersionUID = 1L;
    private DataFrameContext dataFrameContext;
    private List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;

    private Map<Integer, Name> sortingKeyTypes;

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
            Map<Integer, Name> sortingKeyTypes,
            List<FlworDataFrameColumn> columns
    ) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.expressionsWithIterator = expressionsWithIterator;
        this.sortingKeyTypes = sortingKeyTypes;

        this.results = new ArrayList<>();
    }

    @Override
    public Row call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();

        for (int expressionIndex = 0; expressionIndex < this.expressionsWithIterator.size(); expressionIndex++) {
            OrderByClauseAnnotatedChildIterator expressionWithIterator = this.expressionsWithIterator.get(
                expressionIndex
            );

            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this.dataFrameContext.getContext());
            if (!iterator.hasNext()) {
                if (expressionWithIterator.getEmptyOrder() == OrderByClauseSortingKey.EMPTY_ORDER.GREATEST) {
                    this.results.add(emptySequenceOrderIndexLast);
                } else {
                    this.results.add(emptySequenceOrderIndexFirst);
                }
                this.results.add(null); // placeholder for valueColumn(2nd column)
                iterator.close();
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
            Name typeName = this.sortingKeyTypes.get(expressionIndex);
            try {
                if (typeName.equals(BuiltinTypesCatalogue.booleanItem.getName())) {
                    this.results.add(nextItem.getBooleanValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.stringItem.getName())) {
                    this.results.add(nextItem.getStringValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.integerItem.getName())) {
                    this.results.add(nextItem.castToDecimalValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.intItem.getName())) {
                    this.results.add(nextItem.castToIntValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.doubleItem.getName())) {
                    this.results.add(nextItem.castToDoubleValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.floatItem.getName())) {
                    this.results.add(nextItem.castToFloatValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.decimalItem.getName())) {
                    this.results.add(nextItem.castToDecimalValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.base64BinaryItem.getName())) {
                    this.results.add(nextItem.getBinaryValue());
                } else if (typeName.equals(BuiltinTypesCatalogue.hexBinaryItem.getName())) {
                    this.results.add(nextItem.getBinaryValue());
                } else if (
                    typeName.equals(BuiltinTypesCatalogue.durationItem.getName())
                        || typeName.equals(BuiltinTypesCatalogue.yearMonthDurationItem.getName())
                        || typeName.equals(BuiltinTypesCatalogue.dayTimeDurationItem.getName())
                ) {
                    this.results.add(
                        Duration.ofDays(
                            nextItem.getPeriodValue().toTotalMonths() * 30 + nextItem.getPeriodValue().getDays()
                        ).toMillis()
                    );
                } else if (
                    typeName.equals(BuiltinTypesCatalogue.dateTimeItem.getName())
                        || typeName.equals(BuiltinTypesCatalogue.dateItem.getName())
                        || typeName.equals(BuiltinTypesCatalogue.timeItem.getName())
                ) {
                    this.results.add(nextItem.getDateTimeValue().toInstant().toEpochMilli());
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
