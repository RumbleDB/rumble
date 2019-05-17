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

import org.apache.spark.sql.api.java.UDF1;
import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupClauseDetermineTypeUDF implements UDF1<WrappedArray, List> {
    private List<VariableReferenceIterator> _expressions;
    private List<String> _inputColumnNames;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private Item _nextItem;
    private List<String> result;

    public GroupClauseDetermineTypeUDF(
            List<VariableReferenceIterator> expressions,
            List<String> inputColumnNames) {
        _expressions = expressions;
        _inputColumnNames = inputColumnNames;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        result = new ArrayList<>();
    }

    @Override
    public List call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        result.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams);

        for (VariableReferenceIterator expression : _expressions) {
            // prepare dynamic context
            _context.removeAllVariables();
            for (int columnIndex = 0; columnIndex < _inputColumnNames.size(); columnIndex++) {
                _context.addVariableValue(_inputColumnNames.get(columnIndex), _deserializedParams.get(columnIndex));
            }

            // apply expression in the dynamic context
            expression.open(_context);
            if (expression.hasNext()) {
                _nextItem = expression.next();
                if (expression.hasNext()) {
                    throw new UnexpectedTypeException("Can not group on variables with sequences of multiple items.", expression.getMetadata());
                }
            }
            expression.close();

            if (_nextItem == null) {
                result.add("empty-sequence");
            } else if (_nextItem.isNull()) {
                result.add("null");
            } else if (_nextItem.isBoolean()) {
                result.add("bool");
            } else if (_nextItem.isString()) {
                result.add("string");
            } else if (_nextItem.isInteger()) {
                result.add("integer");
            } else if (_nextItem.isDouble()) {
                result.add("double");
            } else if (_nextItem.isDecimal()) {
                result.add("decimal");
            } else if (_nextItem.isArray() || _nextItem.isObject()) {
                throw new UnexpectedTypeException("Group by variable can not contain arrays or objects.", expression.getMetadata());
            } else {
                throw new SparksoniqRuntimeException("Unexpected type found.");
            }
        }
        return result;
    }
}