/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.api.java.UDF1;
import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupClauseCreateColumnsUDF implements UDF1<WrappedArray, Row> {
    private List<VariableReferenceIterator> _expressions;
    private List<String> _inputColumnNames;
    private Map _allColumnTypes;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private List<Object> _results;

    public GroupClauseCreateColumnsUDF(
            List<VariableReferenceIterator> expressions,
            List<String> inputColumnNames,
            Map allColumnTypes) {
        _expressions = expressions;
        _inputColumnNames = inputColumnNames;
        _allColumnTypes = allColumnTypes;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        _results = new ArrayList<>();
    }

    @Override
    public Row call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _results.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams);

        for (int expressionIndex = 0; expressionIndex < _expressions.size(); expressionIndex++) {
            VariableReferenceIterator expression = _expressions.get(expressionIndex);

            // nulls and empty sequences have special grouping captured in the first grouping column
            // if non-null, non-empty-sequence value is given, the second column is used to group the input
            // indices are assigned to each value type for the first column
            int emptySequenceGroupIndex = 1;
            int nullGroupIndex = 2;
            int trueGroupIndex = 3;
            int falseGroupIndex = 4;
            int stringGroupIndex = 5;
            int doubleGroupIndex = 5;

            // prepare dynamic context
            _context.removeAllVariables();
            for (int columnIndex = 0; columnIndex < _inputColumnNames.size(); columnIndex++) {
                _context.addVariableValue(_inputColumnNames.get(columnIndex), _deserializedParams.get(columnIndex));
            }

            // apply expression in the dynamic context
            expression.open(_context);
            boolean isEmptySequence = true;
            while (expression.hasNext()) {
                isEmptySequence = false;
                Item nextItem = expression.next();
                if (nextItem.isNull()) {
                    _results.add(nullGroupIndex);
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isBoolean() ){
                    if(nextItem.getBooleanValue())
                    {
                        _results.add(trueGroupIndex);
                    } else {
                        _results.add(falseGroupIndex);
                    }                        
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isString()) {
                    _results.add(stringGroupIndex);
                    _results.add(nextItem.getStringValue());
                    _results.add(null);
                } else if (nextItem.isInteger()) {
                    // any other atomic type
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getIntegerValue()));
                } else if (nextItem.isDecimal()) {
                    // any other atomic type
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getDecimalValue().doubleValue()));
                } else if (nextItem.isDouble()) {
                    // any other atomic type
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getDoubleValue()));
                } else {
                    throw new SparksoniqRuntimeException("Unexpected grouping type found while creating columns.");
                }
            }
            if (isEmptySequence) {
                _results.add(emptySequenceGroupIndex);
                _results.add(null);
                _results.add(null);
            }
            expression.close();

        }
        return RowFactory.create(_results.toArray());
    }
}
