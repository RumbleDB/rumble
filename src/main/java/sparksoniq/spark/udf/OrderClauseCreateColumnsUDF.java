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
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.api.java.UDF2;
import org.joda.time.Instant;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.flowr.OrderByClauseExpr;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.NullItem;
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

public class OrderClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private List<OrderByClauseAnnotatedChildIterator> _expressionsWithIterator;
    private Map<String, DynamicContext.VariableDependency> dependencies;

    private Map<String, List<String>> _columnNamesByType;
    private Map<Integer, String> _allColumnTypes;

    private List<List<Item>> _deserializedParams;
    private List<Item> _longParams;
    private DynamicContext _parentContext;
    private DynamicContext _context;
    private List<Object> _results;

    private transient Kryo _kryo;
    private transient Input _input;

    public OrderClauseCreateColumnsUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            Map<Integer, String> allColumnTypes,
            Map<String, List<String>> columnNamesByType
    ) {
        this._expressionsWithIterator = expressionsWithIterator;
        this._allColumnTypes = allColumnTypes;

        this._deserializedParams = new ArrayList<>();
        this._longParams = new ArrayList<>();
        this._parentContext = context;
        this._context = new DynamicContext(this._parentContext);
        this._results = new ArrayList<>();

        this.dependencies = new TreeMap<>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this._expressionsWithIterator) {
            this.dependencies.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        this._columnNamesByType = columnNamesByType;

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this._deserializedParams.clear();
        this._context.removeAllVariables();
        this._results.clear();

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

        for (int expressionIndex = 0; expressionIndex < this._expressionsWithIterator.size(); expressionIndex++) {
            OrderByClauseAnnotatedChildIterator expressionWithIterator = this._expressionsWithIterator.get(
                expressionIndex
            );

            // nulls and empty sequences have special ordering captured in the first sorting column
            // if non-null, non-empty-sequence value is given, the second column is used to sort the input
            // indices are assigned to each value type for the first column
            int emptySequenceOrderIndex = 1; // by default, empty sequence is taken as first(=least)
            int nullOrderIndex = 2; // null is the smallest value except empty sequence(default)
            int valueOrderIndex = 3; // values are larger than null and empty sequence(default)
            if (expressionWithIterator.getEmptyOrder() == OrderByClauseExpr.EMPTY_ORDER.LAST) {
                emptySequenceOrderIndex = 4;
            }


            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this._context);
            boolean isEmptySequence = true;
            while (iterator.hasNext()) {
                isEmptySequence = false;
                Item nextItem = iterator.next();
                if (nextItem instanceof NullItem) {
                    this._results.add(nullOrderIndex);
                    this._results.add(null); // placeholder for valueColumn(2nd column)
                } else {
                    // any other atomic type
                    this._results.add(valueOrderIndex);

                    // extract type information for the sorting column
                    String typeName = this._allColumnTypes.get(expressionIndex);
                    try {
                        switch (typeName) {
                            case "boolean":
                                this._results.add(nextItem.getBooleanValue());
                                break;
                            case "string":
                                this._results.add(nextItem.getStringValue());
                                break;
                            case "integer":
                                this._results.add(nextItem.castToIntegerValue());
                                break;
                            case "double":
                                this._results.add(nextItem.castToDoubleValue());
                                break;
                            case "decimal":
                                this._results.add(nextItem.castToDecimalValue());
                                break;
                            case "duration":
                            case "yearMonthDuration":
                            case "dayTimeDuration":
                                this._results.add(
                                    nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis()
                                );
                                break;
                            case "dateTime":
                            case "date":
                            case "time":
                                this._results.add(nextItem.getDateTimeValue().getMillis());
                                break;
                            default:
                                throw new OurBadException(
                                        "Unexpected ordering type found while creating columns."
                                );
                        }
                    } catch (RuntimeException e) {
                        throw new OurBadException(
                                "Invalid sort key: cannot compare item of type "
                                    + typeName
                                    + " with item of type "
                                    + ItemTypes.getItemTypeName(nextItem.getClass().getSimpleName())
                                    + "."
                        );
                    }
                }
            }
            if (isEmptySequence) {
                this._results.add(emptySequenceOrderIndex);
                this._results.add(null); // placeholder for valueColumn(2nd column)
            }
            iterator.close();

        }
        return RowFactory.create(this._results.toArray());
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
