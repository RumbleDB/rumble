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
    private Map<String, DynamicContext.VariableDependency> _dependencies;
    private Map<String, List<String>> _columnNamesByType;
    private List<OrderByClauseAnnotatedChildIterator> _expressionsWithIterator;
    private List<List<Item>> _deserializedParams;
    private List<Item> _longParams;
    private DynamicContext _parentContext;
    private DynamicContext _context;
    private Item _nextItem;
    private List<String> result;

    private transient Kryo _kryo;
    private transient Input _input;

    public OrderClauseDetermineTypeUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
        this._expressionsWithIterator = expressionsWithIterator;

        this._deserializedParams = new ArrayList<>();
        this._longParams = new ArrayList<>();
        this._parentContext = context;
        this._context = new DynamicContext(this._parentContext);
        this.result = new ArrayList<>();

        this._dependencies = new TreeMap<String, DynamicContext.VariableDependency>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this._expressionsWithIterator) {
            this._dependencies.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        this._columnNamesByType = columnNamesByType;

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();
    }

    @Override
    public List<String> call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this._deserializedParams.clear();
        this._context.removeAllVariables();
        this.result.clear();

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

        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this._expressionsWithIterator) {
            // apply expression in the dynamic context
            RuntimeIterator iterator = expressionWithIterator.getIterator();
            iterator.open(this._context);
            if (iterator.hasNext()) {
                this._nextItem = iterator.next();
                if (iterator.hasNext()) {
                    throw new UnexpectedTypeException(
                            "Can not order by variables with sequences of multiple items.",
                            expressionWithIterator.getIterator().getMetadata()
                    );
                }
            }
            iterator.close();

            if (this._nextItem == null) {
                this.result.add("empty-sequence");
            } else if (this._nextItem.isNull()) {
                this.result.add("null");
            } else if (this._nextItem.isBoolean()) {
                this.result.add("boolean");
            } else if (this._nextItem.isString()) {
                this.result.add("string");
            } else if (this._nextItem.isInteger()) {
                this.result.add("integer");
            } else if (this._nextItem.isDouble()) {
                this.result.add("double");
            } else if (this._nextItem.isDecimal()) {
                this.result.add("decimal");
            } else if (this._nextItem.isYearMonthDuration()) {
                this.result.add("yearMonthDuration");
            } else if (this._nextItem.isDayTimeDuration()) {
                this.result.add("dayTimeDuration");
            } else if (this._nextItem.isDuration()) {
                this.result.add("duration");
            } else if (this._nextItem.isDateTime()) {
                this.result.add("dateTime");
            } else if (this._nextItem.isDate()) {
                this.result.add("date");
            } else if (this._nextItem.isTime()) {
                this.result.add("time");
            } else if (this._nextItem.isArray() || this._nextItem.isObject()) {
                throw new UnexpectedTypeException(
                        "Order by variable can not contain arrays or objects.",
                        expressionWithIterator.getIterator().getMetadata()
                );
            } else if (this._nextItem.isBinary()) {
                String itemType = ItemTypes.getItemTypeName(this._nextItem.getClass().getSimpleName());
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

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();
    }
}
