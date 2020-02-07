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
import org.rumbledb.exceptions.UnexpectedTypeException;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupClauseCreateColumnsUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, Row> {

    private static final long serialVersionUID = 1L;
    private List<VariableReferenceIterator> _expressions;
    private Map<String, List<String>> _columnNamesByType;

    private List<List<Item>> _deserializedParams;
    private List<Item> _longParams;
    private DynamicContext _parentContext;
    private DynamicContext _context;
    private List<Object> _results;

    private transient Kryo _kryo;
    private transient Input _input;

    public GroupClauseCreateColumnsUDF(
            List<VariableReferenceIterator> expressions,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
        this._expressions = expressions;
        this._columnNamesByType = columnNamesByType;

        this._deserializedParams = new ArrayList<>();
        this._longParams = new ArrayList<>();
        this._parentContext = context;
        this._context = new DynamicContext(this._parentContext);
        this._results = new ArrayList<>();

        this._kryo = new Kryo();
        this._kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(this._kryo);
        this._input = new Input();
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        this._deserializedParams.clear();
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

        for (VariableReferenceIterator expression : this._expressions) {
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
            this._context.removeAllVariables();
            for (int columnIndex = 0; columnIndex < this._columnNamesByType.get("byte[]").size(); columnIndex++) {
                this._context.addVariableValue(
                    this._columnNamesByType.get("byte[]").get(columnIndex),
                    this._deserializedParams.get(columnIndex)
                );
            }
            for (int columnIndex = 0; columnIndex < this._columnNamesByType.get("Long").size(); columnIndex++) {
                this._context.addVariableCount(
                    this._columnNamesByType.get("Long").get(columnIndex),
                    this._longParams.get(columnIndex)
                );
            }

            // apply expression in the dynamic context
            expression.open(this._context);
            boolean isEmptySequence = true;
            if (expression.hasNext()) {
                isEmptySequence = false;
                Item nextItem = expression.next();
                if (nextItem.isNull()) {
                    this._results.add(nullGroupIndex);
                    this._results.add(null);
                    this._results.add(null);
                    this._results.add(null);
                } else if (nextItem.isBoolean()) {
                    if (nextItem.getBooleanValue()) {
                        this._results.add(booleanTrueGroupIndex);
                    } else {
                        this._results.add(booleanFalseGroupIndex);
                    }
                    this._results.add(null);
                    this._results.add(null);
                    this._results.add(null);
                } else if (nextItem.isString() || nextItem.isHexBinary() || nextItem.isBase64Binary()) {
                    this._results.add(stringGroupIndex);
                    this._results.add(nextItem.getStringValue());
                    this._results.add(null);
                    this._results.add(null);
                } else if (nextItem.isInteger()) {
                    this._results.add(doubleGroupIndex);
                    this._results.add(null);
                    this._results.add(nextItem.castToDoubleValue());
                    this._results.add(null);
                } else if (nextItem.isDecimal()) {
                    this._results.add(doubleGroupIndex);
                    this._results.add(null);
                    this._results.add(nextItem.castToDoubleValue());
                    this._results.add(null);
                } else if (nextItem.isDouble()) {
                    this._results.add(doubleGroupIndex);
                    this._results.add(null);
                    this._results.add(nextItem.getDoubleValue());
                    this._results.add(null);
                } else if (nextItem.isDuration()) {
                    this._results.add(durationGroupIndex);
                    this._results.add(null);
                    this._results.add(null);
                    this._results.add(nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis());
                } else if (nextItem.hasDateTime()) {
                    this._results.add(dateTimeGroupIndex);
                    this._results.add(null);
                    this._results.add(null);
                    this._results.add(nextItem.getDateTimeValue().getMillis());
                } else {
                    throw new UnexpectedTypeException(
                            "Group by variable can not contain arrays or objects.",
                            expression.getMetadata()
                    );
                }
            }
            if (expression.hasNext()) {
                throw new UnexpectedTypeException(
                        "Can not group on variables with sequences of multiple items.",
                        expression.getMetadata()
                );
            }
            if (isEmptySequence) {
                this._results.add(emptySequenceGroupIndex);
                this._results.add(null);
                this._results.add(null);
                this._results.add(null);
            }
            expression.close();

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
