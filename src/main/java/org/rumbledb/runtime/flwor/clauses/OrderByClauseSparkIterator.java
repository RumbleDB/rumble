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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.flwor.udfs.OrderClauseCreateColumnsUDF;
import org.rumbledb.runtime.flwor.udfs.OrderClauseDetermineTypeUDF;
import org.rumbledb.types.AtomicItemType;

import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworKeyComparator;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.rumbledb.items.parsing.ItemParser.decimalType;

public class OrderByClauseSparkIterator extends RuntimeTupleIterator {

    public static final String StringFlagForEmptySequence = "empty-sequence";
    private static final long serialVersionUID = 1L;
    private final List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;
    private Map<Name, DynamicContext.VariableDependency> dependencies;

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
        this.dependencies = new TreeMap<>();
        for (OrderByClauseAnnotatedChildIterator e : this.expressionsWithIterator) {
            this.dependencies.putAll(e.getIterator().getVariableDependencies());
        }
        this.localTupleResults = new ArrayList<>();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child == null) {
            throw new OurBadException("Invalid order-by clause.");
        }
        this.child.open(this.currentDynamicContext);
        this.localTupleResults.clear();
        this.resultIndex = 0;
        this.hasNext = this.child.hasNext();
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child == null) {
            throw new OurBadException("Invalid order-by clause.");
        }
        this.child.reset(this.currentDynamicContext);
        this.localTupleResults.clear();
        this.resultIndex = 0;
        this.hasNext = this.child.hasNext();
    }

    @Override
    public void close() {
        super.close();
        if (this.child == null) {
            throw new OurBadException("Invalid order-by clause.");
        }
        this.child.close();
        this.localTupleResults.clear();
        this.resultIndex = 0;
    }

    @Override
    public FlworTuple next() {
        if (this.hasNext) {
            if (this.resultIndex == 0) {
                setAllLocalResults();
            }
            FlworTuple result = this.localTupleResults.get(this.resultIndex++);
            if (this.resultIndex == this.localTupleResults.size()) {
                this.hasNext = false;
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in order-by clause", getMetadata());
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
                new FlworKeyComparator(this.expressionsWithIterator)
        );

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(this.currentDynamicContext);
        while (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();

            List<Item> results = new ArrayList<>(); // results from the expressions will become a key
            for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
                tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
                tupleContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata()); // assign new
                                                                                                  // variables from new
                                                                                                  // tuple

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
                            String itemType = resultItem.getDynamicType().toString();
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
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid orderby clause.");
        }

        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            if (expressionWithIterator.getIterator().isRDDOrDataFrame()) {
                throw new JobWithinAJobException(
                        "An order by clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }
        }

        Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));
        StructType inputSchema = df.schema();

        List<String> allColumns = FlworDataFrameUtils.getColumnNames(inputSchema);
        List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            null,
            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null
        );

        Dataset<Row> nativeQueryResult = tryNativeQuery(
            df,
            this.expressionsWithIterator,
            allColumns,
            inputSchema,
            context
        );
        if (nativeQueryResult != null) {
            return nativeQueryResult;
        }

        // was not possible, we use order udf
        System.out.println("using UDF");

        df.sparkSession()
            .udf()
            .register(
                "determineOrderingDataType",
                new OrderClauseDetermineTypeUDF(this.expressionsWithIterator, context, inputSchema, UDFcolumns),
                DataTypes.createArrayType(DataTypes.StringType)
            );


        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);

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

        if (columnTypesOfRows.length == 0) {
            // The input is empty, so we output this empty DF again.
            return df;
        }

        // Every column represents an order by expression
        // Check that every column contains a matching atomic type in all rows (nulls and empty-sequences are allowed)
        Map<Integer, String> typesForAllColumns = new LinkedHashMap<>();
        for (Row columnTypesOfRow : columnTypesOfRows) {
            List<Object> columnsTypesOfRowAsList = columnTypesOfRow.getList(0);
            for (int columnIndex = 0; columnIndex < columnsTypesOfRowAsList.size(); columnIndex++) {
                String columnType = (String) columnsTypesOfRowAsList.get(columnIndex);

                if (
                    !columnType.equals(StringFlagForEmptySequence)
                        && !columnType.equals(AtomicItemType.nullItem.getName())
                ) {
                    String currentColumnType = typesForAllColumns.get(columnIndex);
                    if (currentColumnType == null) {
                        typesForAllColumns.put(columnIndex, columnType);
                    } else if (
                        (currentColumnType.equals(AtomicItemType.integerItem.getName())
                            || currentColumnType.equals(AtomicItemType.doubleItem.getName())
                            || currentColumnType.equals(AtomicItemType.decimalItem.getName()))
                            && (columnType.equals(AtomicItemType.integerItem.getName())
                                || columnType.equals(AtomicItemType.doubleItem.getName())
                                || columnType.equals(AtomicItemType.decimalItem.getName()))
                    ) {
                        // the numeric type calculation is identical to Item::getNumericResultType()
                        if (
                            currentColumnType.equals(AtomicItemType.doubleItem.getName())
                                || columnType.equals(AtomicItemType.doubleItem.getName())
                        ) {
                            typesForAllColumns.put(columnIndex, AtomicItemType.doubleItem.getName());
                        } else if (
                            currentColumnType.equals(AtomicItemType.decimalItem.getName())
                                || columnType.equals(AtomicItemType.decimalItem.getName())
                        ) {
                            typesForAllColumns.put(columnIndex, AtomicItemType.decimalItem.getName());
                        } else {
                            // do nothing, type is already set to integer
                        }
                    } else if (
                        (currentColumnType.equals(AtomicItemType.dayTimeDurationItem.getName())
                            || currentColumnType.equals(AtomicItemType.yearMonthDurationItem.getName())
                            || currentColumnType.equals(AtomicItemType.durationItem.getName()))
                            && (columnType.equals(AtomicItemType.dayTimeDurationItem.getName())
                                || columnType.equals(AtomicItemType.yearMonthDurationItem.getName())
                                || columnType.equals(AtomicItemType.durationItem.getName()))
                    ) {
                        typesForAllColumns.put(columnIndex, AtomicItemType.durationItem.getName());
                    } else if (!currentColumnType.equals(columnType)) {
                        throw new UnexpectedTypeException(
                                "Order by variable must contain values of a single type.",
                                getMetadata()
                        );
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
            if (columnTypeString.equals(AtomicItemType.booleanItem.getName())) {
                columnType = DataTypes.BooleanType;
            } else if (columnTypeString.equals(AtomicItemType.stringItem.getName())) {
                columnType = DataTypes.StringType;
            } else if (columnTypeString.equals(AtomicItemType.integerItem.getName())) {
                columnType = DataTypes.IntegerType;
            } else if (columnTypeString.equals(AtomicItemType.doubleItem.getName())) {
                columnType = DataTypes.DoubleType;
            } else if (columnTypeString.equals(AtomicItemType.decimalItem.getName())) {
                columnType = decimalType;
                // columnType = DataTypes.createDecimalType();
            } else if (
                columnTypeString.equals(AtomicItemType.durationItem.getName())
                    || columnTypeString.equals(AtomicItemType.yearMonthDurationItem.getName())
                    || columnTypeString.equals(AtomicItemType.dayTimeDurationItem.getName())
                    || columnTypeString.equals(AtomicItemType.dateTimeItem.getName())
                    || columnTypeString.equals(AtomicItemType.dateItem.getName())
                    || columnTypeString.equals(AtomicItemType.timeItem.getName())
            ) {
                columnType = DataTypes.LongType;
            } else {
                throw new RumbleException(
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
                        inputSchema,
                        typesForAllColumns,
                        UDFcolumns
                ),
                DataTypes.createStructType(typedFields)
            );

        String selectSQL = FlworDataFrameUtils.getSQLProjection(allColumns, true);
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

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            result.putAll(expressionWithIterator.getIterator().getVariableDependencies());
        }
        for (Name var : this.child.getOutputTupleVariableNames()) {
            result.remove(var);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public Set<Name> getOutputTupleVariableNames() {
        return new HashSet<>(this.child.getOutputTupleVariableNames());
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (OrderByClauseAnnotatedChildIterator iterator : this.expressionsWithIterator) {
            iterator.getIterator().print(buffer, indent + 1);
        }
    }

    public Map<Name, DynamicContext.VariableDependency> getProjection(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // start with an empty projection.
        Map<Name, DynamicContext.VariableDependency> projection =
            new TreeMap<>(parentProjection);

        // add the variable dependencies needed by this for clause's expression.
        for (OrderByClauseAnnotatedChildIterator iterator : this.expressionsWithIterator) {
            Map<Name, DynamicContext.VariableDependency> exprDependency = iterator.getIterator()
                .getVariableDependencies();
            for (Name variable : exprDependency.keySet()) {
                if (projection.containsKey(variable)) {
                    if (projection.get(variable) != exprDependency.get(variable)) {
                        if (this.child.getOutputTupleVariableNames().contains(variable)) {
                            projection.put(variable, DynamicContext.VariableDependency.FULL);
                        }
                    }
                } else {
                    if (this.child.getOutputTupleVariableNames().contains(variable)) {
                        projection.put(variable, exprDependency.get(variable));
                    }
                }
            }
        }
        return projection;
    }

    /**
     * Try to generate the native query for the order by clause and run it, if successful return the resulting
     * dataframe,
     * otherwise it returns null
     *
     * @param dataFrame input dataframe for the query
     * @param expressionsWithIterator list of ordering iterators
     * @param allColumns other columns required in following clauses
     * @param inputSchema input schema of the dataframe
     * @param context current dynamic context of the dataframe
     * @return resulting dataframe of the order by clause if successful, null otherwise
     */
    public static Dataset<Row> tryNativeQuery(
            Dataset<Row> dataFrame,
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            List<String> allColumns,
            StructType inputSchema,
            DynamicContext context
    ) {
        NativeClauseContext orderContext = new NativeClauseContext(FLWOR_CLAUSES.ORDER_BY, inputSchema, context);
        StringBuilder orderSql = new StringBuilder();
        String orderSeparator = "";
        NativeClauseContext nativeQuery;
        for (OrderByClauseAnnotatedChildIterator orderIterator : expressionsWithIterator) {
            nativeQuery = orderIterator.getIterator().generateNativeQuery(orderContext);
            if (nativeQuery == NativeClauseContext.NoNativeQuery) {
                return null;
            }
            orderSql.append(orderSeparator);
            orderSeparator = ", ";
            // special check to avoid ordering by an integer constant in an ordering clause
            // because of meaning mismatch between sparksql (where it is supposed to order by the i-th col)
            // and jsoniq (order by a costant, so no actual ordering is performed)
            if (nativeQuery.getResultingQuery().matches("\\s*-?\\s*\\d+\\s*")) {
                orderSql.append('"');
                orderSql.append(nativeQuery.getResultingQuery());
                orderSql.append('"');
            } else {
                orderSql.append(nativeQuery.getResultingQuery());
            }
            if (!orderIterator.isAscending()) {
                orderSql.append(" desc");
            }
        }

        System.out.println("native query returned: " + orderSql);
        String selectSQL = FlworDataFrameUtils.getSQLProjection(allColumns, false);
        dataFrame.createOrReplaceTempView("input");
        return dataFrame.sparkSession()
            .sql(
                String.format(
                    "select %s from input order by %s",
                    selectSQL,
                    orderSql
                )
            );
    }
}
