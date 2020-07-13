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
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import scala.collection.mutable.WrappedArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private List<Name> groupingVariableNames;
    private List<Name> serializedVariableNames;
    private List<Name> countedVariableNames;

    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext parentContext;
    private DynamicContext context;
    private List<Object> results;
    private ExceptionMetadata metadata;

    private transient Kryo kryo;
    private transient Input input;

    public GroupClauseCreateColumnsUDF(
            List<Name> groupingVariableNames,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType,
            ExceptionMetadata metadata
    ) {
        this.groupingVariableNames = groupingVariableNames;
        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();
        this.parentContext = context;
        this.context = new DynamicContext(this.parentContext);
        this.results = new ArrayList<>();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
        this.metadata = metadata;

        List<String> serializedColumNames = columnNamesByType.get("byte[]");
        this.serializedVariableNames = new ArrayList<>(serializedColumNames.size());
        for (String columnName : serializedColumNames) {
            this.serializedVariableNames.add(Name.createVariableInNoNamespace(columnName));
        }
        List<String> countedColumNames = columnNamesByType.get("Long");
        this.countedVariableNames = new ArrayList<>(countedColumNames.size());
        for (String columnName : countedColumNames) {
            this.countedVariableNames.add(Name.createVariableInNoNamespace(columnName));
        }
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this.deserializedParams.clear();
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

        for (Name groupingVariableName : this.groupingVariableNames) {
            // nulls, true, false and empty sequences have special grouping captured in the first grouping column.
            // The second column is used for strings, with a special value in the first column.
            // The third column is used for numbers (as a double), with a special value in the first column.
            int emptySequenceGroupIndex = 1;
            int nullGroupIndex = 2;
            int booleanTrueGroupIndex = 3;
            int booleanFalseGroupIndex = 4;
            int stringGroupIndex = 5;
            int doubleGroupIndex = 5;
            int durationGroupIndex = 5;
            int dateTimeGroupIndex = 5;

            // prepare dynamic context
            this.context.getVariableValues().removeAllVariables();
            int columnIndex = 0;
            for (Name serializedVariableName : this.serializedVariableNames) {
                this.context.getVariableValues()
                    .addVariableValue(
                        serializedVariableName,
                        this.deserializedParams.get(columnIndex)
                    );
                ++columnIndex;
            }
            columnIndex = 0;
            for (Name countedVariableName : this.countedVariableNames) {
                this.context.getVariableValues()
                    .addVariableCount(
                        countedVariableName,
                        this.longParams.get(columnIndex)
                    );
                ++columnIndex;
            }

            boolean isEmptySequence = true;
            List<Item> items = this.context.getVariableValues()
                .getLocalVariableValue(
                    groupingVariableName,
                    this.metadata
                );
            if (items.size() >= 1) {
                isEmptySequence = false;
                Item nextItem = items.get(0);
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
                } else if (nextItem.isDuration()) {
                    this.results.add(durationGroupIndex);
                    this.results.add(null);
                    this.results.add(null);
                    this.results.add(nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis());
                } else if (nextItem.hasDateTime()) {
                    this.results.add(dateTimeGroupIndex);
                    this.results.add(null);
                    this.results.add(null);
                    this.results.add(nextItem.getDateTimeValue().getMillis());
                } else {
                    throw new UnexpectedTypeException(
                            "Group by variable can not contain arrays or objects.",
                            this.metadata
                    );
                }
            }
            if (items.size() > 1) {
                throw new UnexpectedTypeException(
                        "Can not group on variables with sequences of multiple items.",
                        this.metadata
                );
            }
            if (isEmptySequence) {
                this.results.add(emptySequenceGroupIndex);
                this.results.add(null);
                this.results.add(null);
                this.results.add(null);
            }

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
