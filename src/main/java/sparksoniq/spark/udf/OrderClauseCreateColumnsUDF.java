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
import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseExprWithIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private List<OrderByClauseExprWithIterator> _expressionsWithIterator;
    private Map<String, DynamicContext.VariableDependency> _dependencies;

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
            List<OrderByClauseExprWithIterator> expressionsWithIterator,
            DynamicContext context,
            Map<Integer, String> allColumnTypes,
            Map<String, List<String>> columnNamesByType
    ) {
        _expressionsWithIterator = expressionsWithIterator;
        _allColumnTypes = allColumnTypes;

        _deserializedParams = new ArrayList<>();
        _longParams = new ArrayList<>();
        _parentContext = context;
        _context = new DynamicContext(_parentContext);
        _results = new ArrayList<>();

        _dependencies = new TreeMap<>();
        for (OrderByClauseExprWithIterator expressionWithIterator : _expressionsWithIterator) {
            _dependencies.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        _columnNamesByType = columnNamesByType;

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        _deserializedParams.clear();
        _context.removeAllVariables();
        _results.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);

        // Long parameters correspond to pre-computed counts, when a materialization of the
        // actual sequence was avoided upfront.
        Object[] longParams = (Object[]) wrappedParametersLong.array();
        for (Object longParam : longParams) {
            Item count = ItemFactory.getInstance().createIntegerItem(((Long) longParam).intValue());
            _longParams.add(count);
        }

        DataFrameUtils.prepareDynamicContext(
            _context,
            _columnNamesByType.get("byte[]"),
            _columnNamesByType.get("Long"),
            _deserializedParams,
            _longParams
        );

        for (int expressionIndex = 0; expressionIndex < _expressionsWithIterator.size(); expressionIndex++) {
            OrderByClauseExprWithIterator expressionWithIterator = _expressionsWithIterator.get(expressionIndex);

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
            iterator.open(_context);
            boolean isEmptySequence = true;
            while (iterator.hasNext()) {
                isEmptySequence = false;
                Item nextItem = iterator.next();
                if (nextItem instanceof NullItem) {
                    _results.add(nullOrderIndex);
                    _results.add(null); // placeholder for valueColumn(2nd column)
                } else {
                    // any other atomic type
                    _results.add(valueOrderIndex);

                    // extract type information for the sorting column
                    String typeName = _allColumnTypes.get(expressionIndex);
                    try {
                        switch (typeName) {
                            case "boolean":
                                _results.add(nextItem.getBooleanValue());
                                break;
                            case "string":
                                _results.add(nextItem.getStringValue());
                                break;
                            case "integer":
                                _results.add(nextItem.castToIntegerValue());
                                break;
                            case "double":
                                _results.add(nextItem.castToDoubleValue());
                                break;
                            case "decimal":
                                _results.add(nextItem.castToDecimalValue());
                                break;
                            case "duration":
                            case "yearMonthDuration":
                            case "dayTimeDuration":
                                _results.add(nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis());
                                break;
                            case "dateTime":
                            case "date":
                            case "time":
                                _results.add(nextItem.getDateTimeValue().getMillis());
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
                _results.add(emptySequenceOrderIndex);
                _results.add(null); // placeholder for valueColumn(2nd column)
            }
            iterator.close();

        }
        return RowFactory.create(_results.toArray());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }
}
