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

package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.closures.OrderByClauseSortClosure;
import sparksoniq.spark.closures.OrderByMapToPairClosure;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;
import sparksoniq.spark.udf.OrderClauseCreateColumnsUDF;
import sparksoniq.spark.udf.OrderClauseDetermineTypeUDF;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class OrderByClauseSparkIterator extends SparkRuntimeTupleIterator {
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    private List<FlworTuple> _localTupleResults;
    private int _resultIndex;

    public OrderByClauseSparkIterator(RuntimeTupleIterator child, List<OrderByClauseSparkIteratorExpression> expressions,
                                      boolean stable, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        this._expressions = expressions;
        this._isStable = stable;
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public boolean isDataFrame() {
        return _child.isDataFrame();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);

            if (_child.hasNext()) {
                this._hasNext = true;
            } else {
                this._hasNext = false;
            }
        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (_hasNext == true) {
            if (_localTupleResults == null) {
                _localTupleResults = new ArrayList<>();
                _resultIndex = 0;
                setAllLocalResults();
            }

            FlworTuple result = _localTupleResults.get(_resultIndex++);
            if (_resultIndex == _localTupleResults.size()) {
                this._hasNext = false;
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    /**
     * All local results need to be calculated for sorting/ordering to be performed.
     */
    private void setAllLocalResults() {
        TreeMap<FlworKey, List<FlworTuple>> keyValuePairs = mapExpressionsToOrderedPairs();
        // get only the values(ordered tuples) and save them in a list for next() calls
        keyValuePairs.forEach((key, valueList) -> valueList.forEach((value) -> _localTupleResults.add(value)));

        _child.close();
        if (_localTupleResults.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    /**
     * Evaluates expressions to atomics(error is thrown if not possible) which are used as keys for sorted TreeMap.
     * Requires _child iterator to be opened.
     *
     * @return Sorted TreeMap(ascending). key - atomics from expressions, value - input tuples
     */
    private TreeMap<FlworKey, List<FlworTuple>> mapExpressionsToOrderedPairs() {
        // tree map keeps the natural item order deduced from an implementation of Comparator
        // OrderByClauseSortClosure implements a comparator and provides the exact desired behavior for local execution as well
        TreeMap<FlworKey, List<FlworTuple>> keyValuePairs = new TreeMap<>(new OrderByClauseSortClosure(_expressions, true));

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(_currentDynamicContext);
        while (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>(); // results from the expressions will become a key
            for (OrderByClauseSparkIteratorExpression orderByExpression : _expressions) {
                tupleContext.removeAllVariables();                     // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple);        // assign new variables from new tuple

                boolean isFieldEmpty = true;
                RuntimeIterator expression = orderByExpression.getExpression();
                expression.open(tupleContext);
                while (expression.hasNext()) {
                    Item resultItem = expression.next();
                    if (resultItem != null && !resultItem.isAtomic())
                        throw new NonAtomicKeyException("Order by keys must be atomics",
                                orderByExpression.getIteratorMetadata().getExpressionMetadata());
                    isFieldEmpty = false;
                    results.add(resultItem);
                }
                // if empty ordering field is found, add a Java null as placeholder
                if (isFieldEmpty) {
                    results.add(null);
                }
                expression.close();
            }
            FlworKey key = new FlworKey(results);
            List<FlworTuple> values = keyValuePairs.get(key);   // all values for a single matching key are held in a list
            if (values == null) {
                values = new ArrayList<>();
                keyValuePairs.put(key, values);
            }
            values.add(inputTuple);
        }
        return keyValuePairs;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid orderby clause.");
        }

        //map to pairs - ArrayItem [sort keys] , tuples
        JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._child.getRDD(context)
                .mapToPair(new OrderByMapToPairClosure(this._expressions, _isStable));
        //sort by key
        keyTuplePair = keyTuplePair.sortByKey(new OrderByClauseSortClosure(this._expressions, _isStable));
        //map back to tuple RDD
        return keyTuplePair.map(tuple2 -> tuple2._2());
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid orderby clause.");
        }
        Dataset<Row> df = _child.getDataFrame(context);
        StructType inputSchema = df.schema();

        df.sparkSession().udf().register("determineOrderingDataType",
                new OrderClauseDetermineTypeUDF(_expressions, inputSchema),
                DataTypes.createArrayType(DataTypes.StringType));

        String udfSQL = DataFrameUtils.getSQL(inputSchema, -1, false);

        df.createOrReplaceTempView("input");
        df.sparkSession().table("input").cache();
        Dataset<Row> columnTypesDf = df.sparkSession().sql(
                String.format("select distinct(determineOrderingDataType(array(%s))) as `distinct-types` from input",
                        udfSQL)
        );
        Object columnTypesObject = columnTypesDf.collect();
        Row[] columnTypesOfRows = ((Row[]) columnTypesObject);

        // Every column represents an order by expression
        // Check that every column contains a matching atomic type in all rows (nulls and empty-sequences are allowed)
        Map<Integer, String> typesForAllColumns = new LinkedHashMap<>();
        for (Row columnTypesOfRow : columnTypesOfRows) {
            List columnsTypesOfRowAsList = columnTypesOfRow.getList(0);
            for (int columnIndex = 0; columnIndex < columnsTypesOfRowAsList.size(); columnIndex++) {
                String columnType = (String) columnsTypesOfRowAsList.get(columnIndex);

                if (!columnType.equals("empty-sequence") && !columnType.equals("null")) {
                    String currentColumnType = typesForAllColumns.get(columnIndex);
                    if (currentColumnType == null) {
                        typesForAllColumns.put(columnIndex, columnType);
                    } else if ((currentColumnType.equals("integer") || currentColumnType.equals("double") || currentColumnType.equals("decimal"))
                            && (columnType.equals("integer") || columnType.equals("double") || columnType.equals("decimal"))) {
                        // the numeric type calculation is identical to Item::getNumericResultType()
                        if (currentColumnType.equals("double") || columnType.equals("double")) {
                            typesForAllColumns.put(columnIndex, "double");
                        } else if (currentColumnType.equals("decimal") || columnType.equals("decimal")) {
                            typesForAllColumns.put(columnIndex, "decimal");
                        } else {
                            // do nothing, type is already set to integer
                        }
                    } else if (!currentColumnType.equals(columnType)) {
                        throw new UnexpectedTypeException("Order by variable must contain values of a single type.", getMetadata());
                        // TODO-can add tests with different types
                    }
                }
            }
        }


        List<StructField> typedFields = new ArrayList<>();  // Determine the return type for ordering UDF
        StringBuilder orderingSQL = new StringBuilder();    // Prepare the SQL statement for the order by query
        String appendedOrderingColumnsName = "ordering_columns";
        for (int columnIndex = 0; columnIndex < typesForAllColumns.size(); columnIndex++) {
            String columnTypeString = typesForAllColumns.get(columnIndex);
            String columnName;
            DataType columnType;

            // every expression contains an int column for null/empty check
            columnName = columnIndex + "-nullEmptyCheckField";
            typedFields.add(DataTypes.createStructField(columnName, DataTypes.IntegerType, false));

            // create fields for the given value types
            columnName = columnIndex + "-valueField";
            if (columnTypeString.equals("bool")) {
                columnType = DataTypes.BooleanType;
            } else if (columnTypeString.equals("string")) {
                columnType = DataTypes.StringType;
            } else if (columnTypeString.equals("integer")) {
                columnType = DataTypes.IntegerType;
            } else if (columnTypeString.equals("double")) {
                columnType = DataTypes.DoubleType;
            } else if (columnTypeString.equals("decimal")) {
                columnType = DataTypes.createDecimalType();
            } else {
                throw new SparksoniqRuntimeException("Unexpected ordering type found while determining UDF return type.");
            }
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));

            OrderByClauseSparkIteratorExpression expression = _expressions.get(columnIndex);
            // accessing the created ordering row as "`ordering_columns`.`0-nullEmptyCheckField` (desc)"
            // prepare sql for expression's 1st column
            orderingSQL.append("`");
            orderingSQL.append(appendedOrderingColumnsName);
            orderingSQL.append("`.`");
            orderingSQL.append(columnIndex);
            orderingSQL.append("-nullEmptyCheckField`");
            if (expression.isAscending()) {
                orderingSQL.append(", ");
            } else {
                orderingSQL.append(" desc, ");
            }

            // prepare sql for expression's 2nd column
            orderingSQL.append("`");
            orderingSQL.append(appendedOrderingColumnsName);
            orderingSQL.append("`.`");
            orderingSQL.append(columnIndex);
            orderingSQL.append("-valueField`");
            if (columnIndex != typesForAllColumns.size() - 1) {
                if (expression.isAscending()) {
                    orderingSQL.append(", ");
                } else {
                    orderingSQL.append(" desc, ");
                }
            } else {
                if (!expression.isAscending()) {
                    orderingSQL.append(" desc");
                }
            }
        }

        df.sparkSession().udf().register("createOrderingColumns",
                new OrderClauseCreateColumnsUDF(_expressions, inputSchema, typesForAllColumns),
                DataTypes.createStructType(typedFields));

        String selectSQL = DataFrameUtils.getSQL(inputSchema, -1, true);
        String projectSQL = selectSQL.substring(0, selectSQL.length() - 1);   // remove trailing comma
        udfSQL = projectSQL;

        return df.sparkSession().sql(
                String.format(
                        "select %s from (select %s createOrderingColumns(array(%s)) as `%s` from input order by %s)",
                        projectSQL, selectSQL, udfSQL, appendedOrderingColumnsName, orderingSQL
                )
        );
    }

    public Set<String> getVariableDependencies()
    {
        Set<String> result = new HashSet<String>();
        for(OrderByClauseSparkIteratorExpression iterator : _expressions)
        {
            result.addAll(iterator.getExpression().getVariableDependencies());
        }
        result.removeAll(_child.getVariablesBoundInCurrentFLWORExpression());
        result.addAll(_child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression()
    {
        Set<String> result = new HashSet<String>();
        result.addAll(_child.getVariablesBoundInCurrentFLWORExpression());
        return result;
    }
    
    public void print(StringBuffer buffer, int indent)
    {
        super.print(buffer, indent);
        for(OrderByClauseSparkIteratorExpression iterator : _expressions)
        {
            iterator.getExpression().print(buffer, indent+1);
        }
    }
}
