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

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.StructType;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

public class GroupClauseCreateColumnsUDF implements UDF1<Row, Row> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final DataFrameContext dataFrameContext;
    private final List<Name> groupingVariableNames;
    private final List<SequenceType> groupingSequenceTypes;

    private final List<Object> results;
    private final ExceptionMetadata metadata;

    // nulls, true, false and empty sequences have special grouping captured in the first grouping column.
    // The second column is used for strings, with a special value in the first column.
    // The third column is used for numbers (as a double), with a special value in the first column.
    private static final int emptySequenceGroupIndex = 1;
    private static final int nullGroupIndex = 2;
    private static final int booleanTrueGroupIndex = 3;
    private static final int booleanFalseGroupIndex = 4;
    private static final int stringGroupIndex = 5;
    private static final int doubleGroupIndex = 5;
    private static final int durationGroupIndex = 5;
    private static final int dateTimeGroupIndex = 5;

    public GroupClauseCreateColumnsUDF(
            List<GroupByClauseSparkIteratorExpression> groupingExpressions,
            DynamicContext context,
            StructType schema,
            List<FlworDataFrameColumn> columns,
            ExceptionMetadata metadata) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.groupingVariableNames = new ArrayList<>();
        this.groupingSequenceTypes = new ArrayList<>();
        for (GroupByClauseSparkIteratorExpression expression : groupingExpressions) {
            this.groupingVariableNames.add(expression.getVariableName());
            this.groupingSequenceTypes.add(expression.getSequenceType());
        }
        this.results = new ArrayList<>();
        this.metadata = metadata;
    }

    @Override
    public Row call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();

        for (int i = 0; i < this.groupingVariableNames.size(); i++) {
            Name groupingVariableName = this.groupingVariableNames.get(i);
            SequenceType declaredType = this.groupingSequenceTypes.get(i);
            List<Item> items = this.dataFrameContext
                    .getContext()
                    .getVariableValues()
                    .getLocalVariableValue(groupingVariableName, this.metadata);

            List<Item> atomizedGroupingKey = atomizeGroupingKey(items);

            validateGroupingKeySequenceType(declaredType, atomizedGroupingKey);

            if (atomizedGroupingKey.isEmpty()) {
                this.results.add(emptySequenceGroupIndex);
                this.results.add(null);
                this.results.add(null);
                this.results.add(null);
                continue;
            }

            Item nextItem = atomizedGroupingKey.get(0);
            this.createColumnsForItem(nextItem);
        }

        return RowFactory.create(this.results.toArray());
    }

    private List<Item> atomizeGroupingKey(List<Item> items) {
        List<Item> atomizedGroupingKey = new ArrayList<>();
        for (Item item : items) {
            try {
                atomizedGroupingKey.addAll(item.atomizedValue());
            } catch (CannotAtomizeException e) {
                throw new UnexpectedTypeException(
                        "Group by variable must atomize to a supported atomic value.", this.metadata);
            }
        }
        if (atomizedGroupingKey.size() > 1) {
            throw new UnexpectedTypeException(
                    "Keys in a group-by clause must atomize to at most one item.", this.metadata);
        }
        return atomizedGroupingKey;
    }

    private void validateGroupingKeySequenceType(SequenceType declaredType, List<Item> groupingKey) {
        if (declaredType == null) {
            return;
        }
        if (!declaredType.isResolved()) {
            declaredType.resolve(this.dataFrameContext.getContext(), this.metadata);
        }

        boolean validCardinality =
                switch (declaredType.getArity()) {
                    case Zero -> groupingKey.isEmpty();
                    case One -> groupingKey.size() == 1;
                    case OneOrZero -> groupingKey.size() <= 1;
                    case OneOrMore -> !groupingKey.isEmpty();
                    case ZeroOrMore -> true;
                };
        if (!validCardinality) {
            throw new UnexpectedTypeException(
                    "The grouping key has cardinality "
                            + groupingKey.size()
                            + ", but the expected type is "
                            + declaredType,
                    this.metadata);
        }
        for (Item item : groupingKey) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(declaredType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType() + " is not expected here. The expected type is " + declaredType,
                        this.metadata);
            }
        }
    }

    private void createColumnsForItem(Item nextItem) {
        if (nextItem.isNull()) {
            this.results.add(nullGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(null);
            return;
        } else if (nextItem.isBoolean()) {
            if (nextItem.getBooleanValue()) {
                this.results.add(booleanTrueGroupIndex);
            } else {
                this.results.add(booleanFalseGroupIndex);
            }
            this.results.add(null);
            this.results.add(null);
            this.results.add(null);
            return;
        } else if (nextItem.isString() || nextItem.isHexBinary() || nextItem.isBase64Binary()) {
            this.results.add(stringGroupIndex);
            this.results.add(nextItem.getStringValue());
            this.results.add(null);
            this.results.add(null);
            return;
        } else if (nextItem.isInteger()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
            return;
        } else if (nextItem.isDecimal()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
            return;
        } else if (nextItem.isDouble()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.getDoubleValue());
            this.results.add(null);
            return;
        } else if (nextItem.isFloat()) {
            this.results.add(doubleGroupIndex);
            this.results.add(null);
            this.results.add(nextItem.castToDoubleValue());
            this.results.add(null);
            return;
        } else if (nextItem.isDuration()) {
            this.results.add(durationGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(nextItem.getEpochMillis());
            return;
        } else if (nextItem.hasDateTime()) {
            this.results.add(dateTimeGroupIndex);
            this.results.add(null);
            this.results.add(null);
            this.results.add(nextItem.getEpochMillis());
            return;
        }
        throw new UnexpectedTypeException("Group by variable must atomize to a supported atomic value.", this.metadata);
    }
}
