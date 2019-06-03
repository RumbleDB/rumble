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
import sparksoniq.exceptions.InvalidGroupVariableException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.closures.GroupByLinearizeTupleClosure;
import sparksoniq.spark.closures.GroupByToPairMapClosure;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import sparksoniq.spark.udf.GroupClauseCreateColumnsUDF;
import sparksoniq.spark.udf.GroupClauseDetermineTypeUDF;
import sparksoniq.spark.udf.GroupClauseSerializeAggregateResultsUDF;
import sparksoniq.spark.udf.LetClauseUDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupByClauseSparkIterator extends SparkRuntimeTupleIterator {
    private final List<GroupByClauseSparkIteratorExpression> _expressions;
    private List<FlworTuple> _localTupleResults;
    private int _resultIndex;


    public GroupByClauseSparkIterator(RuntimeTupleIterator child, List<GroupByClauseSparkIteratorExpression> variables,
                                      IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        this._expressions = variables;
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
            throw new SparksoniqRuntimeException("Invalid groupby clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (_hasNext) {

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
     * All local results need to be calculated for grouping to be performed.
     */
    private void setAllLocalResults() {
        Map<FlworKey, List<FlworTuple>> keyTuplePairs = mapTuplesToPairs();
        keyTuplePairs.forEach((key, tupleList) -> linearizeTuples(tupleList));

        _child.close();
        if (_localTupleResults.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }


    private HashMap<FlworKey, List<FlworTuple>> mapTuplesToPairs() {
        HashMap<FlworKey, List<FlworTuple>> keyValuePairs = new HashMap<>();

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(_currentDynamicContext);
        while (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>();
            for (GroupByClauseSparkIteratorExpression expression : _expressions) {
                tupleContext.removeAllVariables();                     // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple);        // assign new variables from new tuple

                // if grouping on an expression
                RuntimeIterator groupVariableExpression = expression.getExpression();
                if (groupVariableExpression != null) {
                    if (inputTuple.contains(expression.getVariableReference().getVariableName())) {
                        throw new InvalidGroupVariableException("Group by variable redeclaration is illegal", expression.getIteratorMetadata());
                    }

                    List<Item> newVariableResults = new ArrayList<>();
                    groupVariableExpression.open(tupleContext);
                    while (groupVariableExpression.hasNext()) {
                        Item resultItem = groupVariableExpression.next();
                        if (!resultItem.isAtomic()) {
                            throw new NonAtomicKeyException("Group by keys must be atomics", expression.getIteratorMetadata().getExpressionMetadata());
                        }
                        newVariableResults.add(resultItem);
                    }
                    groupVariableExpression.close();

                    //if a new variable is declared inside the group by clause, insert value in tuple
                    inputTuple.putValue(expression.getVariableReference().getVariableName(), newVariableResults, false);
                    results.addAll(newVariableResults);

                } else { // if grouping on a variable reference
                    VariableReferenceIterator groupVariableReference = expression.getVariableReference();
                    if (!inputTuple.contains(groupVariableReference.getVariableName())) {
                        throw new InvalidGroupVariableException("Variable " + groupVariableReference.getVariableName() + " cannot be used in group clause", expression.getIteratorMetadata());
                    }

                    groupVariableReference.open(tupleContext);
                    while (groupVariableReference.hasNext()) {
                        results.add(groupVariableReference.next());
                    }
                    groupVariableReference.close();
                }
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

    private void linearizeTuples(List<FlworTuple> keyTuplePairs) {
        Iterator<FlworTuple> iterator = keyTuplePairs.iterator();
        FlworTuple oldFirstTuple = iterator.next();
        FlworTuple newTuple = new FlworTuple(oldFirstTuple.getKeys().size());
        for (String tupleVariable : oldFirstTuple.getKeys()) {
            iterator = keyTuplePairs.iterator();
            if (_expressions.stream().anyMatch(v -> v.getVariableReference().getVariableName().equals(tupleVariable)))
                newTuple.putValue(tupleVariable, oldFirstTuple.getValue(tupleVariable), false);
            else {
                List<Item> allValues = new ArrayList<>();
                while (iterator.hasNext())
                    allValues.addAll(iterator.next().getValue(tupleVariable));
                newTuple.putValue(tupleVariable, allValues, false);
            }
        }
        _localTupleResults.add(newTuple);
    }


    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        _rdd = this._child.getRDD(context);
        //map to pairs - ArrayItem [sort keys] , tuples
        JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._rdd
                .mapToPair(new GroupByToPairMapClosure(_expressions));
        //group by key
        JavaPairRDD<FlworKey, Iterable<FlworTuple>> groupedPair =
                keyTuplePair.groupByKey();
        //linearize iterable tuples into arrays
        this._rdd = groupedPair.map(new GroupByLinearizeTupleClosure(_expressions));
        return _rdd;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid groupby clause.");
        }
        Dataset<Row> df = _child.getDataFrame(context);
        StructType inputSchema;
        String[] columnNamesArray;
        List<String> columnNames;

        List<VariableReferenceIterator> variableAccessExpressions = new ArrayList<>();
        for (int expressionIndex = 0; expressionIndex < _expressions.size(); expressionIndex++) {
            GroupByClauseSparkIteratorExpression expression = _expressions.get(expressionIndex);
            inputSchema = df.schema();
            columnNamesArray = inputSchema.fieldNames();
            columnNames = Arrays.asList(columnNamesArray);

            if (expression.getExpression() != null) {
                // if a variable is defined in-place with groupby, execute a let on the variable
                variableAccessExpressions.add(expression.getVariableReference());
                String newVariableName = expression.getVariableReference().getVariableName();
                RuntimeIterator newVariableExpression = expression.getExpression();

                df.sparkSession().udf().register("letClauseUDF",
                        new LetClauseUDF(newVariableExpression, inputSchema), DataTypes.BinaryType);

                int duplicateVariableIndex = columnNames.indexOf(newVariableName);
                String selectSQL = DataFrameUtils.getSQL(inputSchema, duplicateVariableIndex, true);
                String udfSQL = DataFrameUtils.getSQL(inputSchema, -1, false);

                df.createOrReplaceTempView("input");
                df = df.sparkSession().sql(
                        String.format("select %s letClauseUDF(array(%s)) as `%s` from input",
                                selectSQL, udfSQL, newVariableName)
                );

            } else {
                if (!columnNames.contains(expression.getVariableReference().getVariableName())) {
                    throw new InvalidGroupVariableException(
                            "Variable " + expression.getVariableReference().getVariableName() + " cannot be used in group clause", expression.getIteratorMetadata()
                    );
                }
                variableAccessExpressions.add(expression.getVariableReference());
            }
        }

        // determine grouping data types after all variable introductions are completed
        inputSchema = df.schema();
        columnNamesArray = inputSchema.fieldNames();
        columnNames = Arrays.asList(columnNamesArray);

        df.createOrReplaceTempView("input");
        df.sparkSession().table("input").cache();
        df.sparkSession().udf().register("determineGroupingDataType",
                new GroupClauseDetermineTypeUDF(variableAccessExpressions, columnNames),
                DataTypes.createArrayType(DataTypes.StringType));

        String udfSQL = DataFrameUtils.getSQL(inputSchema, -1, false);

        Dataset<Row> columnTypesDf = df.sparkSession().sql(
                String.format("select distinct(determineGroupingDataType(array(%s))) as `distinct-types` from input",
                        udfSQL)
        );
        Object columnTypesObject = columnTypesDf.collect();
        Row[] columnTypesOfRows = ((Row[]) columnTypesObject);

        // Every column represents a group by expression
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
                        throw new UnexpectedTypeException("Group by variable must contain values of a single type.", getMetadata());
                    }
                }
            }
        }

        // Determine the return type for grouping UDF
        List<StructField> typedFields = new ArrayList<>();
        String appendedGroupingColumnsName = "grouping_columns";
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
                throw new SparksoniqRuntimeException("Unexpected grouping type found while determining UDF return type.");
            }
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));
        }

        df.sparkSession().udf().register("createGroupingColumns",
                new GroupClauseCreateColumnsUDF(variableAccessExpressions, columnNames, typesForAllColumns),
                DataTypes.createStructType(typedFields));

        String serializerUDFName = "serialize";
        df.sparkSession().udf().register(serializerUDFName,
                new GroupClauseSerializeAggregateResultsUDF(),
                DataTypes.BinaryType);

        String selectSQL = DataFrameUtils.getSQL(inputSchema, -1, true);
        udfSQL = DataFrameUtils.getSQL(inputSchema, -1, false);

        String createColumnsSQL = String.format(
                "select %s createGroupingColumns(array(%s)) as `%s` from input",
                selectSQL, udfSQL, appendedGroupingColumnsName
        );

        List<String> groupbyVariableNames = new ArrayList<>();
        for (VariableReferenceIterator variableAccessExpression : variableAccessExpressions) {
            groupbyVariableNames.add(variableAccessExpression.getVariableName());
        }
        String projectSQL = DataFrameUtils.getGroupbyProjectSQL(
                inputSchema,
                -1,
                false,
                serializerUDFName,
                groupbyVariableNames
        );

        return df.sparkSession().sql(
                String.format(
                        "select %s from (%s) group by `%s`",
                        projectSQL, createColumnsSQL, appendedGroupingColumnsName
                )
        );
    }
}
