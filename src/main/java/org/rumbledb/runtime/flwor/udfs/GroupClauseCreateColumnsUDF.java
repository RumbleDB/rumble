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
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GroupClauseCreateColumnsUDF implements UDF1<Row, Row> {

    private static final long serialVersionUID = 1L;
    private DataFrameContext dataFrameContext;
    private List<Name> groupingVariableNames;

    private List<Object> results;
    private ExceptionMetadata metadata;

    // nulls, true, false and empty sequences have special grouping captured in the first grouping column.
    // The second column is used for strings, with a special value in the first column.
    // The third column is used for numbers (as a double), with a special value in the first column.
    private static int emptySequenceGroupIndex = 1;
    private static int nullGroupIndex = 2;
    private static int booleanTrueGroupIndex = 3;
    private static int booleanFalseGroupIndex = 4;
    private static int stringGroupIndex = 5;
    private static int doubleGroupIndex = 5;
    private static int durationGroupIndex = 5;
    private static int dateTimeGroupIndex = 5;

    public GroupClauseCreateColumnsUDF(
            List<Name> groupingVariableNames,
            DynamicContext context,
            StructType schema,
            List<FlworDataFrameColumn> columns,
            ExceptionMetadata metadata
    ) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.groupingVariableNames = groupingVariableNames;
        this.results = new ArrayList<>();
        this.metadata = metadata;
    }

    @Override
    public Row call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();

        for (Name groupingVariableName : this.groupingVariableNames) {
            List<Item> items = this.dataFrameContext.getContext()
                .getVariableValues()
                .getLocalVariableValue(
                    groupingVariableName,
                    this.metadata
                );

            if (items.size() > 1) {
                throw new UnexpectedTypeException(
                        "Can not group on variables with sequences of multiple items.",
                        this.metadata
                );
            }

            if (items.size() == 0) {
                this.results.add(emptySequenceGroupIndex);
                this.results.add(null);
                this.results.add(null);
                this.results.add(null);
                continue;
            }

            Item nextItem = items.get(0);
            this.createColumnsForItem(nextItem);
        }

        return RowFactory.create(this.results.toArray());
    }

    private void createColumnsForItem(Item nextItem) {
        if (nextItem.isNull()) {
            this.results.add(nullGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(null);
        } else if (nextItem.isBoolean()) {
            if (nextItem.getBooleanValue()) {
                this.results.add(booleanTrueGroupIndex);
            } else {
                this.results.add(booleanFalseGroupIndex);
            }
            this.results.add(null);
            this.results.add(null);
            this.results.add(null);
        } else if (nextItem.isString() || nextItem.isHexBinary() || nextItem.isBase64Binary()) {
            this.results.add(stringGroupIndex);
            this.results.add(nextItem.getStringValue());
            this.results.add(null);
            this.results.add(null);
        } else if (nextItem.isInteger()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
        } else if (nextItem.isDecimal()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
        } else if (nextItem.isDouble()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.getDoubleValue());
            this.results.add(null);
        } else if (nextItem.isFloat()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
        } else if (nextItem.isDuration()) {
            this.results.add(durationGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(
                Duration.ofDays(
                    nextItem.getPeriodValue().toTotalMonths() * 30 + nextItem.getPeriodValue().getDays()
                ).toMillis()
            );
        } else if (nextItem.hasDateTime()) {
            this.results.add(dateTimeGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(nextItem.getDateTimeValue().toInstant().toEpochMilli());
        } else {
            throw new UnexpectedTypeException(
                    "Group by variable can not contain arrays or objects.",
                    this.metadata
            );
        }
    }
}
