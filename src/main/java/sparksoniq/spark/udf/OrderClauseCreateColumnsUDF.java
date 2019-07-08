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
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderClauseCreateColumnsUDF implements UDF1<WrappedArray, Row> {
    private List<OrderByClauseSparkIteratorExpression> _expressions;
    Set<String> _dependencies;
    List<String> _columnNames;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private List<Object> _results;
    
    private transient Kryo _kryo;
    private transient Input _input;

    public OrderClauseCreateColumnsUDF(
            List<OrderByClauseSparkIteratorExpression> expressions,
            StructType inputSchema,
            List<String> columnNames) {
        _expressions = expressions;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        _results = new ArrayList<>();
        
        _dependencies = new HashSet<String>();
        for (OrderByClauseSparkIteratorExpression expression : _expressions) {
            _dependencies.addAll(expression.getExpression().getVariableDependencies());
        }
        _columnNames = columnNames;
        
        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public Row call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _context.removeAllVariables();
        _results.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);

        DataFrameUtils.prepareDynamicContext(_context, _columnNames, _deserializedParams);

        for (int expressionIndex = 0; expressionIndex < _expressions.size(); expressionIndex++) {
            OrderByClauseSparkIteratorExpression expression = _expressions.get(expressionIndex);

            // nulls, true, false and empty sequences have special grouping captured in the first grouping column.
            // The second column is used for strings, with a special value in the first column.
            // The third column is used for numbers (as a double), with a special value in the first column.
            int emptySequenceOrderIndex = 1; // by default, empty sequence is taken as first(=least)
            int nullGroupIndex = 2;
            int booleanTrueOrderIndex = 3;
            int booleanFalseOrderIndex = 4;
            int stringGroupIndex = 5;
            int doubleGroupIndex = 6;
            if (expression.getEmptyOrder() == OrderByClauseExpr.EMPTY_ORDER.LAST) {
                emptySequenceOrderIndex = 7;
            }

            // apply expression in the dynamic context
            expression.getExpression().open(_context);
            boolean isEmptySequence = true;
            while (expression.getExpression().hasNext()) {
                isEmptySequence = false;
                Item nextItem = expression.getExpression().next();
                if (nextItem.isNull()) {
                    _results.add(nullGroupIndex);
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isBoolean() ){
                    if(nextItem.getBooleanValue())
                    {
                        _results.add(booleanTrueOrderIndex);
                    } else {
                        _results.add(booleanFalseOrderIndex);
                    }                        
                    _results.add(null);
                    _results.add(null);
                } else if (nextItem.isString()) {
                    _results.add(stringGroupIndex);
                    _results.add(nextItem.getStringValue());
                    _results.add(null);
                } else if (nextItem.isInteger()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getIntegerValue()));
                } else if (nextItem.isDecimal()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getDecimalValue().doubleValue()));
                } else if (nextItem.isDouble()) {
                    _results.add(doubleGroupIndex);
                    _results.add(null);
                    _results.add(new Double(nextItem.getDoubleValue()));
                } else {
                    throw new UnexpectedTypeException("Order by value can not contain arrays or objects.", expression.getExpression().getMetadata());
                }
            }
            if (expression.getExpression().hasNext()) {
                throw new UnexpectedTypeException("Cannot order values with sequences of multiple items.", expression.getExpression().getMetadata());
            }
            if (isEmptySequence) {
                _results.add(emptySequenceOrderIndex);
                _results.add(null);
                _results.add(null);
            }
            expression.getExpression().close();

        }
        return RowFactory.create(_results.toArray());
    }
    
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }
}
