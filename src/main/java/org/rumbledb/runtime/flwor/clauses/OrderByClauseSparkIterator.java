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

package org.rumbledb.runtime.flwor.clauses;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.flwor.udfs.OrderClauseCreateColumnsUDF;
import org.rumbledb.runtime.flwor.udfs.OrderClauseDetermineTypeUDF;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworKeyComparator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OrderByClauseSparkIterator extends RuntimeTupleIterator {

    private static final long serialVersionUID = 1L;
    private final boolean isStable;
    private final List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;
    private Map<String, DynamicContext.VariableDependency> dependencies;

    private List<FlworTuple> localTupleResults;
    private int resultIndex;

    public OrderByClauseSparkIterator(
            RuntimeTupleIterator child,
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            boolean stable,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.expressionsWithIterator = expressionsWithIterator;
        this.isStable = stable;
        this.dependencies = new TreeMap<>();
        for (OrderByClauseAnnotatedChildIterator e : this.expressionsWithIterator) {
            this.dependencies.putAll(e.getIterator().getVariableDependencies());
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child != null) {
            this.child.open(this.currentDynamicContext);

            this.hasNext = this.child.hasNext();
        } else {
            throw new OurBadException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (this.hasNext) {
            if (this.localTupleResults == null) {
                this.localTupleResults = new ArrayList<>();
                this.resultIndex = 0;
                setAllLocalResults();
            }

            FlworTuple result = this.localTupleResults.get(this.resultIndex++);
            if (this.resultIndex == this.localTupleResults.size()) {
                this.hasNext = false;
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
        keyValuePairs.forEach((key, valueList) -> this.localTupleResults.addAll(valueList));

        this.child.close();
        this.hasNext = this.localTupleResults.size() != 0;
    }

    /**
     * Evaluates expressions to atomics(error is thrown if not possible) which are used as keys for sorted TreeMap.
     * Requires child iterator to be opened.
     *
     * @return Sorted TreeMap(ascending). key - atomics from expressions, value - input tuples
     */
    private TreeMap<FlworKey, List<FlworTuple>> mapExpressionsToOrderedPairs() {
        // tree map keeps the natural item order deduced from an implementation of Comparator
        // OrderByClauseSortClosure implements a comparator and provides the exact desired behavior for local execution
        // as well
        TreeMap<FlworKey, List<FlworTuple>> keyValuePairs = new TreeMap<>(
                new FlworKeyComparator(this.expressionsWithIterator, true)
        );

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(this.currentDynamicContext);
        while (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();

            List<Item> results = new ArrayList<>(); // results from the expressions will become a key
            for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
                tupleContext.removeAllVariables(); // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple, getMetadata()); // assign new variables from new tuple

                boolean isFieldEmpty = true;
                RuntimeIterator iterator = expressionWithIterator.getIterator();
                iterator.open(tupleContext);
                while (iterator.hasNext()) {
                    Item resultItem = iterator.next();
                    if (resultItem != null) {
                        if (!resultItem.isAtomic()) {
                            throw new NonAtomicKeyException(
                                    "Order by keys must be atomics",
                                    expressionWithIterator.getIterator().getMetadata()
                            );
                        }
                        if (resultItem.isBinary()) {
                            String itemType = ItemTypes.getItemTypeName(resultItem.getClass().getSimpleName());
                            throw new UnexpectedTypeException(
                                    "\""
                                        + itemType
                                        + "\": invalid type: can not compare for equality to type \""
                                        + itemType
                                        + "\"",
                                    getMetadata()
                            );
                        }
                    }
                    isFieldEmpty = false;
                    results.add(resultItem);
                }
                // if empty ordering field is found, add a Java null as placeholder
                if (isFieldEmpty) {
                    results.add(null);
                }
                iterator.close();
            }
            FlworKey key = new FlworKey(results);
            List<FlworTuple> values = keyValuePairs.get(key); // all values for a single matching key are held in a list
            if (values == null) {
                values = new ArrayList<>();
                keyValuePairs.put(key, values);
            }
            values.add(inputTuple);
        }
        return keyValuePairs;
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid orderby clause.");
        }

        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            if (expressionWithIterator.getIterator().isRDD()) {
                throw new JobWithinAJobException(
                        "An order by clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }
        }

        Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));
        if (df.count() == 0) {
            return df;
        }
        StructType inputSchema = df.schema();

        List<String> allColumns = FlworDataFrameUtils.getColumnNames(inputSchema);
        Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            this.dependencies
        );

        df.sparkSession()
            .udf()
            .register(
                "determineOrderingDataType",
                new OrderClauseDetermineTypeUDF(this.expressionsWithIterator, context, UDFcolumnsByType),
                DataTypes.createArrayType(DataTypes.StringType)
            );


        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

        df.createOrReplaceTempView("input");
        df.sparkSession().table("input").cache();
        Dataset<Row> columnTypesDf = df.sparkSession()
            .sql(
                String.format(
                    "select distinct(determineOrderingDataType(%s)) as `distinct-types` from input",
                    UDFParameters
                )
            );
        Object columnTypesObject = columnTypesDf.collect();
        Row[] columnTypesOfRows = ((Row[]) columnTypesObject);

        // Every column represents an order by expression
        // Check that every column contains a matching atomic type in all rows (nulls and empty-sequences are allowed)
        Map<Integer, String> typesForAllColumns = new LinkedHashMap<>();
        for (Row columnTypesOfRow : columnTypesOfRows) {
            List<Object> columnsTypesOfRowAsList = columnTypesOfRow.getList(0);
            for (int columnIndex = 0; columnIndex < columnsTypesOfRowAsList.size(); columnIndex++) {
                String columnType = (String) columnsTypesOfRowAsList.get(columnIndex);

                if (!columnType.equals("empty-sequence") && !columnType.equals("null")) {
                    String currentColumnType = typesForAllColumns.get(columnIndex);
                    if (currentColumnType == null) {
                        typesForAllColumns.put(columnIndex, columnType);
                    } else if (
                        (currentColumnType.equals("integer")
                            || currentColumnType.equals("double")
                            || currentColumnType.equals("decimal"))
                            && (columnType.equals("integer")
                                || columnType.equals("double")
                                || columnType.equals("decimal"))
                    ) {
                        // the numeric type calculation is identical to Item::getNumericResultType()
                        if (currentColumnType.equals("double") || columnType.equals("double")) {
                            typesForAllColumns.put(columnIndex, "double");
                        } else if (currentColumnType.equals("decimal") || columnType.equals("decimal")) {
                            typesForAllColumns.put(columnIndex, "decimal");
                        } else {
                            // do nothing, type is already set to integer
                        }
                    } else if (
                        (currentColumnType.equals("dayTimeDuration")
                            || currentColumnType.equals("yearMonthDuration")
                            || currentColumnType.equals("duration"))
                            && (columnType.equals("dayTimeDuration")
                                || columnType.equals("yearMonthDuration")
                                || columnType.equals("duration"))
                    ) {
                        typesForAllColumns.put(columnIndex, "duration");
                    } else if (!currentColumnType.equals(columnType)) {
                        throw new UnexpectedTypeException(
                                "Order by variable must contain values of a single type.",
                                getMetadata()
                        );
                        // TODO-can add tests with different types
                    }
                }
            }
        }


        List<StructField> typedFields = new ArrayList<>(); // Determine the return type for ordering UDF
        StringBuilder orderingSQL = new StringBuilder(); // Prepare the SQL statement for the order by query
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
            switch (columnTypeString) {
                case "boolean":
                    columnType = DataTypes.BooleanType;
                    break;
                case "string":
                    columnType = DataTypes.StringType;
                    break;
                case "integer":
                    columnType = DataTypes.IntegerType;
                    break;
                case "double":
                    columnType = DataTypes.DoubleType;
                    break;
                case "decimal":
                    columnType = DataTypes.createDecimalType();
                    break;
                case "duration":
                case "yearMonthDuration":
                case "dayTimeDuration":
                case "dateTime":
                case "date":
                case "time":
                    columnType = DataTypes.LongType;
                    break;
                default:
                    throw new SparksoniqRuntimeException(
                            "Unexpected ordering type found while determining UDF return type."
                    );
            }
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));

            OrderByClauseAnnotatedChildIterator expressionWithIterator = this.expressionsWithIterator.get(columnIndex);
            // accessing the created ordering row as "`ordering_columns`.`0-nullEmptyCheckField` (desc)"
            // prepare sql for expression's 1st column
            orderingSQL.append("`");
            orderingSQL.append(appendedOrderingColumnsName);
            orderingSQL.append("`.`");
            orderingSQL.append(columnIndex);
            orderingSQL.append("-nullEmptyCheckField`");
            if (expressionWithIterator.isAscending()) {
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
                if (expressionWithIterator.isAscending()) {
                    orderingSQL.append(", ");
                } else {
                    orderingSQL.append(" desc, ");
                }
            } else {
                if (!expressionWithIterator.isAscending()) {
                    orderingSQL.append(" desc");
                }
            }
        }

        df.sparkSession()
            .udf()
            .register(
                "createOrderingColumns",
                new OrderClauseCreateColumnsUDF(
                        this.expressionsWithIterator,
                        context,
                        typesForAllColumns,
                        UDFcolumnsByType
                ),
                DataTypes.createStructType(typedFields)
            );

        String selectSQL = FlworDataFrameUtils.getSQL(allColumns, true);
        String projectSQL = selectSQL.substring(0, selectSQL.length() - 1); // remove trailing comma

        return df.sparkSession()
            .sql(
                String.format(
                    "select %s from (select %s createOrderingColumns(%s) as `%s` from input order by %s)",
                    projectSQL,
                    selectSQL,
                    UDFParameters,
                    appendedOrderingColumnsName,
                    orderingSQL
                )
            );
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            result.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        for (String var : this.child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(var);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        return new HashSet<>(this.child.getVariablesBoundInCurrentFLWORExpression());
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (OrderByClauseAnnotatedChildIterator iterator : this.expressionsWithIterator) {
            iterator.getIterator().print(buffer, indent + 1);
        }
    }

    public Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        // start with an empty projection.
        Map<String, DynamicContext.VariableDependency> projection =
            new TreeMap<>(parentProjection);

        // add the variable dependencies needed by this for clause's expression.
        for (OrderByClauseAnnotatedChildIterator iterator : this.expressionsWithIterator) {
            Map<String, DynamicContext.VariableDependency> exprDependency = iterator.getIterator()
                .getVariableDependencies();
            for (String variable : exprDependency.keySet()) {
                if (projection.containsKey(variable)) {
                    if (projection.get(variable) != exprDependency.get(variable)) {
                        projection.put(variable, DynamicContext.VariableDependency.FULL);
                    }
                } else {
                    projection.put(variable, exprDependency.get(variable));
                }
            }
        }
        return projection;
    }
}
