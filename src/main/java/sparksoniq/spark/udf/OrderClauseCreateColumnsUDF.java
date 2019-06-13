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
import org.apache.spark.sql.types.StructType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderClauseCreateColumnsUDF implements UDF1<WrappedArray, Row> {
    private List<OrderByClauseSparkIteratorExpression> _expressions;
    private StructType _inputSchema;
    private Map _allColumnTypes;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private List<Object> _results;
    
    private transient Kryo _kryo;
    private transient Input _input;

    public OrderClauseCreateColumnsUDF(
            List<OrderByClauseSparkIteratorExpression> expressions,
            StructType inputSchema,
            Map allColumnTypes) {
        _expressions = expressions;
        _inputSchema = inputSchema;
        _allColumnTypes = allColumnTypes;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        _results = new ArrayList<>();
        
        _kryo = new Kryo();
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public Row call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _results.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);
        String[] columnNames = _inputSchema.fieldNames();

        for (int expressionIndex = 0; expressionIndex < _expressions.size(); expressionIndex++) {
            OrderByClauseSparkIteratorExpression expression = _expressions.get(expressionIndex);

            // nulls and empty sequences have special ordering captured in the first sorting column
            // if non-null, non-empty-sequence value is given, the second column is used to sort the input
            // indices are assigned to each value type for the first column
            int emptySequenceOrderIndex = 1;         // by default, empty sequence is taken as first(=least)
            int nullOrderIndex = 2;                  // null is the smallest value except empty sequence(default)
            int valueOrderIndex = 3;                 // values are larger than null and empty sequence(default)
            if (expression.getEmptyOrder() == OrderByClauseExpr.EMPTY_ORDER.LAST) {
                emptySequenceOrderIndex = 4;
            }

            // prepare dynamic context
            _context.removeAllVariables();
            for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
                _context.addVariableValue(columnNames[columnIndex], _deserializedParams.get(columnIndex));
            }

            // apply expression in the dynamic context
            expression.getExpression().open(_context);
            boolean isEmptySequence = true;
            while (expression.getExpression().hasNext()) {
                isEmptySequence = false;
                Item nextItem = expression.getExpression().next();
                if (nextItem instanceof NullItem) {
                    _results.add(nullOrderIndex);
                    _results.add(null);     // placeholder for valueColumn(2nd column)
                } else {
                    // any other atomic type
                    _results.add(valueOrderIndex);

                    // extract type information for the sorting column
                    String typeName = (String) _allColumnTypes.get(expressionIndex);

                    if (typeName.equals("bool")) {
                        _results.add(nextItem.getBooleanValue());
                    } else if (typeName.equals("string")) {
                        _results.add(nextItem.getStringValue());
                    } else if (typeName.equals("integer")) {
                        _results.add(Item.getNumericValue(nextItem, Integer.class));
                    } else if (typeName.equals("double")) {
                        _results.add(Item.getNumericValue(nextItem, Double.class));
                    } else if (typeName.equals("decimal")) {
                        _results.add(Item.getNumericValue(nextItem, BigDecimal.class));
                    } else {
                        throw new SparksoniqRuntimeException("Unexpected ordering type found while creating columns.");
                    }
                }
            }
            if (isEmptySequence) {
                _results.add(emptySequenceOrderIndex);
                _results.add(null);     // placeholder for valueColumn(2nd column)
            }
            expression.getExpression().close();

        }
        return RowFactory.create(_results.toArray());
    }
    
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        _kryo = new Kryo();
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }
}
