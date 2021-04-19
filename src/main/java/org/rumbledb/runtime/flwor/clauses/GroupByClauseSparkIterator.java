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
import org.rumbledb.exceptions.InvalidGroupVariableException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.flwor.udfs.GroupClauseCreateColumnsUDF;
import org.rumbledb.runtime.flwor.udfs.GroupClauseSerializeAggregateResultsUDF;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupByClauseSparkIterator extends RuntimeTupleIterator {

    private static final long serialVersionUID = 1L;
    private final List<GroupByClauseSparkIteratorExpression> groupingExpressions;
    private List<FlworTuple> localTupleResults;
    private int resultIndex;
    private Map<Name, DynamicContext.VariableDependency> dependencies;

    public GroupByClauseSparkIterator(
            RuntimeTupleIterator child,
            List<GroupByClauseSparkIteratorExpression> groupingExpressions,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.groupingExpressions = groupingExpressions;
        this.dependencies = new TreeMap<>();
        for (GroupByClauseSparkIteratorExpression e : this.groupingExpressions) {
            if (e.getExpression() != null) {
                this.dependencies.putAll(e.getExpression().getVariableDependencies());
            } else {
                this.dependencies.put(
                    e.getVariableName(),
                    DynamicContext.VariableDependency.FULL
                );
            }
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child != null) {
            this.child.open(this.currentDynamicContext);
            this.hasNext = this.child.hasNext();
        } else {
            throw new OurBadException("Invalid groupby clause.");
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

    @Override
    public void close() {
        super.close();
        if (this.child != null) {
            this.child.close();
            this.localTupleResults = null;
        } else {
            throw new OurBadException("Invalid groupby clause.");
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child != null) {
            this.child.reset(this.currentDynamicContext);
            this.localTupleResults = null;
            this.hasNext = this.child.hasNext();
        } else {
            throw new OurBadException("Invalid groupby clause.");
        }
    }

    /**
     * All local results need to be calculated for grouping to be performed.
     */
    private void setAllLocalResults() {
        Map<FlworKey, List<FlworTuple>> keyTuplePairs = mapTuplesToPairs();
        keyTuplePairs.forEach((key, tupleList) -> linearizeTuples(tupleList));

        this.child.close();
        this.hasNext = this.localTupleResults.size() != 0;
    }


    private HashMap<FlworKey, List<FlworTuple>> mapTuplesToPairs() {
        HashMap<FlworKey, List<FlworTuple>> keyValuePairs = new HashMap<>();

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(this.currentDynamicContext);
        while (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();

            List<Item> results = new ArrayList<>();
            for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
                tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
                tupleContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata()); // assign new
                                                                                                  // variables from new
                                                                                                  // tuple

                // if grouping on an expression
                RuntimeIterator groupVariableExpression = expression.getExpression();
                if (groupVariableExpression != null) {
                    if (inputTuple.contains(expression.getVariableName())) {
                        throw new InvalidGroupVariableException(
                                "Group by variable redeclaration is illegal",
                                getMetadata()
                        );
                    }

                    List<Item> newVariableResults = new ArrayList<>();
                    groupVariableExpression.open(tupleContext);
                    while (groupVariableExpression.hasNext()) {
                        Item resultItem = groupVariableExpression.next();
                        if (!resultItem.isAtomic()) {
                            throw new NonAtomicKeyException(
                                    "Group by keys must be atomics",
                                    getMetadata()
                            );
                        }
                        newVariableResults.add(resultItem);
                    }
                    groupVariableExpression.close();

                    // if a new variable is declared inside the group by clause, insert value in tuple
                    inputTuple.putValue(expression.getVariableName(), newVariableResults);
                    results.addAll(newVariableResults);

                } else { // if grouping on a variable reference
                    Name groupVariableName = expression.getVariableName();
                    if (!inputTuple.contains(groupVariableName)) {
                        throw new InvalidGroupVariableException(
                                "Variable "
                                    + groupVariableName
                                    + " cannot be used in group clause",
                                this.getMetadata()
                        );
                    }

                    results.addAll(
                        tupleContext.getVariableValues()
                            .getLocalVariableValue(
                                groupVariableName,
                                getMetadata()
                            )
                    );
                }
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

    private void linearizeTuples(List<FlworTuple> keyTuplePairs) {
        Iterator<FlworTuple> iterator = keyTuplePairs.iterator();
        FlworTuple oldFirstTuple = iterator.next();
        FlworTuple newTuple = new FlworTuple(oldFirstTuple.getLocalKeys().size());
        for (Name tupleVariable : oldFirstTuple.getLocalKeys()) {
            iterator = keyTuplePairs.iterator();
            if (
                this.groupingExpressions.stream()
                    .anyMatch(v -> v.getVariableName().equals(tupleVariable))
            ) {
                newTuple.putValue(tupleVariable, oldFirstTuple.getLocalValue(tupleVariable, getMetadata()));
            } else {
                List<Item> allValues = new ArrayList<>();
                while (iterator.hasNext()) {
                    allValues.addAll(iterator.next().getLocalValue(tupleVariable, getMetadata()));
                }
                newTuple.putValue(tupleVariable, allValues);
            }
        }
        this.localTupleResults.add(newTuple);
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid groupby clause.");
        }

        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (expression.getExpression() != null && expression.getExpression().isRDDOrDataFrame()) {
                throw new JobWithinAJobException(
                        "A group by clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }
        }

        Dataset<Row> df = this.child.getDataFrame(context);
        StructType inputSchema;
        String[] columnNamesArray;
        List<String> columnNames;

        List<Name> variableAccessNames = new ArrayList<>();
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            inputSchema = df.schema();
            columnNamesArray = inputSchema.fieldNames();
            columnNames = Arrays.asList(columnNamesArray);

            // TODO: consider add sequence type to group clause variable
            if (expression.getExpression() != null) {
                // if a variable is defined in-place with groupby, execute a let on the variable
                variableAccessNames.add(expression.getVariableName());
                df = LetClauseSparkIterator.bindLetVariableInDataFrame(
                    df,
                    expression.getVariableName(),
                    null,
                    expression.getExpression(),
                    context,
                    new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
                    null,
                    false
                );


            } else {
                if (!columnNames.contains(expression.getVariableName().toString())) {
                    throw new InvalidGroupVariableException(
                            "Variable "
                                + expression.getVariableName()
                                + " cannot be used in group clause",
                            getMetadata()
                    );
                }
                variableAccessNames.add(expression.getVariableName());
            }
        }

        inputSchema = df.schema();

        df.createOrReplaceTempView("input");

        Dataset<Row> nativeQueryResult = tryNativeQuery(
            df,
            variableAccessNames,
            this.outputTupleProjection,
            inputSchema,
            context
        );
        if (nativeQueryResult != null) {
            return nativeQueryResult;
        }

        Map<Name, DynamicContext.VariableDependency> groupingVariables = new TreeMap<>();

        // Determine the return type for grouping UDF
        List<StructField> typedFields = new ArrayList<>();
        String appendedGroupingColumnsName = "grouping_columns";
        for (int columnIndex = 0; columnIndex < this.groupingExpressions.size(); columnIndex++) {
            groupingVariables.put(
                this.groupingExpressions.get(columnIndex).getVariableName(),
                DynamicContext.VariableDependency.FULL
            );
            // every expression contains an int column for null/empty/true/false/string/double check
            String columnName = columnIndex + "-nullEmptyBooleanCheckField";
            typedFields.add(DataTypes.createStructField(columnName, DataTypes.IntegerType, false));
            columnName = columnIndex + "-stringField";
            DataType columnType = DataTypes.StringType;
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));
            columnName = columnIndex + "-doubleField";
            columnType = DataTypes.DoubleType;
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));
            columnName = columnIndex + "-durationField";
            columnType = DataTypes.LongType;
            typedFields.add(DataTypes.createStructField(columnName, columnType, true));
        }

        String serializerUDFName = "serialize";
        df.sparkSession()
            .udf()
            .register(
                serializerUDFName,
                new GroupClauseSerializeAggregateResultsUDF(),
                DataTypes.BinaryType
            );

        List<String> allColumns = FlworDataFrameUtils.getColumnNames(inputSchema);
        List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            groupingVariables
        );

        df.sparkSession()
            .udf()
            .register(
                "createGroupingColumns",
                new GroupClauseCreateColumnsUDF(variableAccessNames, context, inputSchema, UDFcolumns, getMetadata()),
                DataTypes.createStructType(typedFields)
            );

        String selectSQL = FlworDataFrameUtils.getSQLProjection(allColumns, true);

        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);

        String createColumnsSQL = String.format(
            "select %s createGroupingColumns(%s) as `%s` from input",
            selectSQL,
            UDFParameters,
            appendedGroupingColumnsName
        );

        String projectSQL = FlworDataFrameUtils.getGroupBySQLProjection(
            inputSchema,
            -1,
            false,
            serializerUDFName,
            variableAccessNames,
            this.outputTupleProjection,
            UDFcolumns
        );

        Dataset<Row> result = df.sparkSession()
            .sql(
                String.format(
                    "select %s from (%s) group by `%s`",
                    projectSQL,
                    createColumnsSQL,
                    appendedGroupingColumnsName
                )
            );
        return result;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            if (iterator.getExpression() != null) {
                result.putAll(iterator.getExpression().getVariableDependencies());
            } else {
                result.put(iterator.getVariableName(), DynamicContext.VariableDependency.FULL);
            }
        }
        for (Name var : this.child.getOutputTupleVariableNames()) {
            result.remove(var);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            result.add(iterator.getVariableName());
        }
        result.addAll(this.child.getOutputTupleVariableNames());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            for (int i = 0; i < indent + 1; ++i) {
                buffer.append("  ");
            }
            buffer.append("Variable ").append(iterator.getVariableName()).append("\n");
            if (iterator.getExpression() != null) {
                iterator.getExpression().print(buffer, indent + 1);
            }
        }
    }

    public Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // copy over the projection needed by the parent clause.
        Map<Name, DynamicContext.VariableDependency> projection = new TreeMap<>(parentProjection);

        // remove the variables that this clause binds.
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            projection.remove(iterator.getVariableName());
        }

        // add the variable dependencies needed by this for clause's expression.
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            if (iterator.getExpression() == null) {
                Name variable = iterator.getVariableName();
                if (this.child.getOutputTupleVariableNames().contains(variable)) {
                    projection.put(variable, DynamicContext.VariableDependency.FULL);
                }
                continue;
            }
            Map<Name, DynamicContext.VariableDependency> exprDependency = iterator.getExpression()
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
     * Try to generate the native query for the group by clause and run it, if successful return the resulting
     * dataframe,
     * otherwise it returns null (expect `input` table to be already available)
     *
     * @param dataFrame input dataframe for the query
     * @param groupingVariables group by variables
     * @param dependencies dependencies to forward to the next clause (select variables)
     * @param inputSchema input schema of the dataframe
     * @param context current dynamic context of the dataframe
     * @return resulting dataframe of the group by clause if successful, null otherwise
     */
    private Dataset<Row> tryNativeQuery(
            Dataset<Row> dataFrame,
            List<Name> groupingVariables,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            StructType inputSchema,
            DynamicContext context
    ) {
        StringBuilder groupByString = new StringBuilder();
        String sep = " ";
        for (Name groupingVar : groupingVariables) {
            StructField field = inputSchema.fields()[inputSchema.fieldIndex(groupingVar.toString())];
            if (field.dataType().equals(DataTypes.BinaryType)) {
                // we got a non-native type for grouping, switch to udf version
                return null;
            }

            groupByString.append(sep);
            sep = ", ";
            groupByString.append(groupingVar.toString());
        }
        StringBuilder selectString = new StringBuilder();
        sep = " ";
        for (Map.Entry<Name, DynamicContext.VariableDependency> entry : dependencies.entrySet()) {
            selectString.append(sep);
            sep = ", ";
            if (FlworDataFrameUtils.isVariableCountOnly(inputSchema, entry.getKey())) {
                // we are summing over a previous count
                selectString.append("sum(`");
                selectString.append(entry.getKey().toString());
                selectString.append(".count");
                selectString.append("`) as `");
                selectString.append(entry.getKey().toString());
                selectString.append(".count");
                selectString.append("`");
            } else if (FlworDataFrameUtils.isVariableNativeSequence(inputSchema, entry.getKey())) {
                // we are summing over a previous count
                String columnName = entry.getKey().toString();
                selectString.append("collect_list(`");
                selectString.append(columnName);
                selectString.append("`) as `");
                selectString.append(columnName);
                selectString.append("`");
            } else if (entry.getValue() == DynamicContext.VariableDependency.COUNT) {
                // we need a count
                selectString.append("count(`");
                selectString.append(entry.getKey().toString());
                selectString.append("`) as `");
                selectString.append(entry.getKey().toString());
                selectString.append(".count`");
            } else if (groupingVariables.contains(entry.getKey())) {
                // we are considering one of the grouping variables
                selectString.append(entry.getKey().toString());
            } else {
                // we collect all the values, if it is a binary object we just switch over to udf
                String columnName = entry.getKey().toString();
                StructField field = inputSchema.fields()[inputSchema.fieldIndex(columnName)];
                if (field.dataType().equals(DataTypes.BinaryType)) {
                    return null;
                }
                selectString.append("collect_list(`");
                selectString.append(columnName);
                selectString.append("`) as `");
                selectString.append(columnName);
                selectString.append(".sequence`");
            }
        }
        System.out.println("[INFO] Rumble was able to optimize a let clause to a native SQL query: " + selectString);
        System.out.println("[INFO] group-by part: " + groupByString);
        return dataFrame.sparkSession()
            .sql(
                String.format(
                    "select %s from input group by %s",
                    selectString,
                    groupByString
                )
            );
    }
}
