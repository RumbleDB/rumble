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
import org.apache.spark.sql.api.java.UDF1;
import org.joda.time.Instant;
import org.rumbledb.api.Item;
import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupClauseCreateColumnsUDF implements UDF1<WrappedArray<byte[]>, Row> {

    private static final long serialVersionUID = 1L;
    private List<VariableReferenceIterator> _expressions;
    private List<String> _inputColumnNames;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _parentContext;
    private DynamicContext _context;
    private List<Object> _results;

    private transient Kryo _kryo;
    private transient Input _input;

    public GroupClauseCreateColumnsUDF(
            List<VariableReferenceIterator> expressions,
            DynamicContext context,
            List<String> inputColumnNames
    ) {
        _expressions = expressions;
        _inputColumnNames = inputColumnNames;

        _deserializedParams = new ArrayList<>();
        _parentContext = context;
        _context = new DynamicContext(_parentContext);
        _results = new ArrayList<>();

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public Row call(WrappedArray<byte[]> wrappedParameters) {
        _deserializedParams.clear();
        _results.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);

        for (VariableReferenceIterator expression : _expressions) {
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
            _context.removeAllVariables();
            for (int columnIndex = 0; columnIndex < _inputColumnNames.size(); columnIndex++) {
                _context.addVariableValue(_inputColumnNames.get(columnIndex), _deserializedParams.get(columnIndex));
            }

            // apply expression in the dynamic context
            expression.open(_context);
            boolean isEmptySequence = true;
            if (expression.hasNext()) {
                isEmptySequence = false;
                Item nextItem = expression.next();
                if (nextItem.isNull()) {
                    _results.add(nullGroupIndex);
                    _results.add(null);
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isBoolean()) {
                    if (nextItem.getBooleanValue()) {
                        _results.add(booleanTrueGroupIndex);
                    } else {
                        _results.add(booleanFalseGroupIndex);
                    }
                    _results.add(null);
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isString() || nextItem.isHexBinary() || nextItem.isBase64Binary()) {
                    _results.add(stringGroupIndex);
                    _results.add(nextItem.getStringValue());
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isInteger()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(nextItem.castToDoubleValue());
                    _results.add(null);
                } else if (nextItem.isDecimal()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(nextItem.castToDoubleValue());
                    _results.add(null);
                } else if (nextItem.isDouble()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(nextItem.getDoubleValue());
                    _results.add(null);
                } else if (nextItem.isDuration()) {
                    _results.add(durationGroupIndex);
                    _results.add(null);
                    _results.add(null);
                    _results.add(nextItem.getDurationValue().toDurationFrom(Instant.now()).getMillis());
                } else if (nextItem.hasDateTime()) {
                    _results.add(dateTimeGroupIndex);
                    _results.add(null);
                    _results.add(null);
                    _results.add(nextItem.getDateTimeValue().getMillis());
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
                _results.add(emptySequenceGroupIndex);
                _results.add(null);
                _results.add(null);
                _results.add(null);
            }
            expression.close();

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
