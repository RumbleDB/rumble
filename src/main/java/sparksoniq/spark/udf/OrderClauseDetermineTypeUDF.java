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
import org.apache.spark.sql.types.StructType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderClauseDetermineTypeUDF implements UDF1<WrappedArray, List> {
    private List<OrderByClauseSparkIteratorExpression> _expressions;
    Set<String> _dependencies;
    List<String> _columnNames;
    private StructType _inputSchema;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private Item _nextItem;
    private List<String> result;
    
    private transient Kryo _kryo;
    private transient Input _input;

    public OrderClauseDetermineTypeUDF(
            List<OrderByClauseSparkIteratorExpression> expressions,
            StructType inputSchema,
            List<String> columnNames) {
        _expressions = expressions;
        _inputSchema = inputSchema;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        result = new ArrayList<>();
        
        _dependencies = new HashSet<String>();
        for (OrderByClauseSparkIteratorExpression expression : _expressions) {
            _dependencies.addAll(expression.getExpression().getVariableDependencies());
        }
        _columnNames = columnNames;
        
        _kryo = new Kryo();
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public List call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _context.removeAllVariables();
        result.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);

        DataFrameUtils.prepareDynamicContext(_context, _columnNames, _deserializedParams);

        for (OrderByClauseSparkIteratorExpression expression : _expressions) {
            // apply expression in the dynamic context
            expression.getExpression().open(_context);
            if (expression.getExpression().hasNext()) {
                _nextItem = expression.getExpression().next();
                if (expression.getExpression().hasNext()) {
                    throw new UnexpectedTypeException("Can not order by variables with sequences of multiple items.", expression.getIteratorMetadata());
                }
            }
            expression.getExpression().close();

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
                throw new UnexpectedTypeException("Order by variable can not contain arrays or objects.", expression.getIteratorMetadata());
            } else {
                throw new SparksoniqRuntimeException("Unexpected type found.");
            }
        }
        return result;
    }
    
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        _kryo = new Kryo();
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }
}
