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

package sparksoniq.spark.iterator.flowr;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidGroupVariableException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.OurBadException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import sparksoniq.spark.udf.GroupClauseCreateColumnsUDF;
import sparksoniq.spark.udf.GroupClauseSerializeAggregateResultsUDF;
import sparksoniq.spark.udf.LetClauseUDF;

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
    private Map<String, DynamicContext.VariableDependency> dependencies;

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
                    e.getVariableReference().getVariableName(),
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
                tupleContext.removeAllVariables(); // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple, getMetadata()); // assign new variables from new tuple

                // if grouping on an expression
                RuntimeIterator groupVariableExpression = expression.getExpression();
                if (groupVariableExpression != null) {
                    if (inputTuple.contains(expression.getVariableReference().getVariableName())) {
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
                    inputTuple.putValue(expression.getVariableReference().getVariableName(), newVariableResults);
                    results.addAll(newVariableResults);

                } else { // if grouping on a variable reference
                    VariableReferenceIterator groupVariableReference = expression.getVariableReference();
                    if (!inputTuple.contains(groupVariableReference.getVariableName())) {
                        throw new InvalidGroupVariableException(
                                "Variable "
                                    + groupVariableReference.getVariableName()
                                    + " cannot be used in group clause",
                                groupVariableReference.getMetadata()
                        );
                    }

                    groupVariableReference.open(tupleContext);
                    while (groupVariableReference.hasNext()) {
                        results.add(groupVariableReference.next());
                    }
                    groupVariableReference.close();
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
        for (String tupleVariable : oldFirstTuple.getLocalKeys()) {
            iterator = keyTuplePairs.iterator();
            if (
                this.groupingExpressions.stream()
                    .anyMatch(v -> v.getVariableReference().getVariableName().equals(tupleVariable))
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
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid groupby clause.");
        }

        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (expression.getExpression() != null && expression.getExpression().isRDD()) {
                throw new JobWithinAJobException(
                        "A group by clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }
        }

        Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));
        StructType inputSchema;
        String[] columnNamesArray;
        List<String> columnNames;

        List<VariableReferenceIterator> variableAccessExpressions = new ArrayList<>();
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            inputSchema = df.schema();
            columnNamesArray = inputSchema.fieldNames();
            columnNames = Arrays.asList(columnNamesArray);

            if (expression.getExpression() != null) {
                // if a variable is defined in-place with groupby, execute a let on the variable
                variableAccessExpressions.add(expression.getVariableReference());
                String newVariableName = expression.getVariableReference().getVariableName();
                RuntimeIterator newVariableExpression = expression.getExpression();
                int duplicateVariableIndex = columnNames.indexOf(newVariableName);

                List<String> allColumns = DataFrameUtils.getColumnNames(inputSchema, duplicateVariableIndex, null);
                Map<String, List<String>> UDFcolumnsByType = DataFrameUtils.getColumnNamesByType(
                    inputSchema,
                    -1,
                    this.dependencies
                );

                df.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new LetClauseUDF(newVariableExpression, context, UDFcolumnsByType),
                        DataTypes.BinaryType
                    );

                String selectSQL = DataFrameUtils.getSQL(allColumns, true);
                String UDFParameters = DataFrameUtils.getUDFParameters(UDFcolumnsByType);

                df.createOrReplaceTempView("input");
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "select %s letClauseUDF(%s) as `%s` from input",
                            selectSQL,
                            UDFParameters,
                            newVariableName
                        )
                    );


            } else {
                if (!columnNames.contains(expression.getVariableReference().getVariableName())) {
                    throw new InvalidGroupVariableException(
                            "Variable "
                                + expression.getVariableReference().getVariableName()
                                + " cannot be used in group clause",
                            expression.getVariableReference().getMetadata()
                    );
                }
                variableAccessExpressions.add(expression.getVariableReference());
            }
        }

        inputSchema = df.schema();
        Map<String, DynamicContext.VariableDependency> groupingVariables = new TreeMap<>();

        df.createOrReplaceTempView("input");

        // Determine the return type for grouping UDF
        List<StructField> typedFields = new ArrayList<>();
        String appendedGroupingColumnsName = "grouping_columns";
        for (int columnIndex = 0; columnIndex < this.groupingExpressions.size(); columnIndex++) {
            groupingVariables.put(
                this.groupingExpressions.get(columnIndex).getVariableReference().getVariableName(),
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

        List<String> allColumns = DataFrameUtils.getColumnNames(inputSchema);
        Map<String, List<String>> UDFcolumnsByType = DataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            groupingVariables
        );

        df.sparkSession()
            .udf()
            .register(
                "createGroupingColumns",
                new GroupClauseCreateColumnsUDF(variableAccessExpressions, context, UDFcolumnsByType),
                DataTypes.createStructType(typedFields)
            );

        String selectSQL = DataFrameUtils.getSQL(allColumns, true);

        String UDFParameters = DataFrameUtils.getUDFParameters(UDFcolumnsByType);

        String createColumnsSQL = String.format(
            "select %s createGroupingColumns(%s) as `%s` from input",
            selectSQL,
            UDFParameters,
            appendedGroupingColumnsName
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
            groupbyVariableNames,
            parentProjection,
            UDFcolumnsByType
        );

        return df.sparkSession()
            .sql(
                String.format(
                    "select %s from (%s) group by `%s`",
                    projectSQL,
                    createColumnsSQL,
                    appendedGroupingColumnsName
                )
            );
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            if (iterator.getExpression() != null) {
                result.putAll(iterator.getExpression().getVariableDependencies());
            } else {
                result.put(iterator.getVariableReference().getVariableName(), DynamicContext.VariableDependency.FULL);
            }
        }
        for (String var : this.child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(var);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        Set<String> result = new HashSet<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            result.add(iterator.getVariableReference().getVariableName());
        }
        result.addAll(this.child.getVariablesBoundInCurrentFLWORExpression());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            for (int i = 0; i < indent + 1; ++i) {
                buffer.append("  ");
            }
            buffer.append("Variable ").append(iterator.getVariableReference().getVariableName()).append("\n");
            if (iterator.getExpression() != null) {
                iterator.getExpression().print(buffer, indent + 1);
            }
        }
    }

    public Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        // copy over the projection needed by the parent clause.
        Map<String, DynamicContext.VariableDependency> projection = new TreeMap<>(parentProjection);

        // remove the variables that this clause binds.
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            projection.remove(iterator.getVariableReference().getVariableName());
        }

        // add the variable dependencies needed by this for clause's expression.
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            if (iterator.getExpression() == null) {
                String variable = iterator.getVariableReference().getVariableName();
                projection.put(variable, DynamicContext.VariableDependency.FULL);
                continue;
            }
            Map<String, DynamicContext.VariableDependency> exprDependency = iterator.getExpression()
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
