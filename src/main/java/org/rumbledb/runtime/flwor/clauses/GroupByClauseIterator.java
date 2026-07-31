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

import org.rumbledb.runtime.plan.DataFrameRuntimePlan;

import org.apache.log4j.LogManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.InvalidGroupVariableException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrame;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn.ColumnFormat;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.flwor.udfs.GroupClauseArrayMergeAggregateResultsUDF;
import org.rumbledb.runtime.flwor.udfs.GroupClauseCreateColumnsUDF;
import org.rumbledb.runtime.flwor.udfs.GroupClauseSerializeAggregateResultsUDF;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanDiagnostics;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GroupByClauseIterator extends TupleRuntimePlan implements DataFrameRuntimePlan<FlworTuple> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<GroupByClauseSparkIteratorExpression> groupingExpressions;
    private Map<Name, DynamicContext.VariableDependency> dependencies;

    public GroupByClauseIterator(
            TupleRuntimePlan child,
            List<GroupByClauseSparkIteratorExpression> groupingExpressions,
            RuntimeStaticContext staticContext
    ) {
        super(child, staticContext);
        this.groupingExpressions = groupingExpressions;
        this.dependencies = new TreeMap<>();
        for (GroupByClauseSparkIteratorExpression e : this.groupingExpressions) {
            if (e.getExpression() != null) {
                this.dependencies.putAll(
                    VariableDependencyRuntimePlan.get(e.getExpression())
                );
            } else {
                this.dependencies.put(
                    e.getVariableName(),
                    DynamicContext.VariableDependency.FULL
                );
            }
        }
    }

    @Override
    public Cursor<FlworTuple> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeLocalResults(context).iterator(), getMetadata());
    }

    private List<FlworTuple> computeLocalResults(DynamicContext context) {
        if (this.child == null) {
            throw new OurBadException("Invalid groupby clause.");
        }
        Map<FlworKey, List<FlworTuple>> tuplesByKey = new LinkedHashMap<>();
        DynamicContext tupleContext = new DynamicContext(context);
        try (Cursor<FlworTuple> childCursor = this.child.createNativeCursor(context)) {
            while (childCursor.hasNext()) {
                FlworTuple tuple = childCursor.next();
                List<Item> keys = new ArrayList<>();
                for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
                    tupleContext.getVariableValues().removeAllVariables();
                    tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata());
                    RuntimePlan<Item> keyExpression = expression
                        .getExpression();
                    if (keyExpression != null) {
                        if (tuple.contains(expression.getVariableName())) {
                            throw new InvalidGroupVariableException(
                                    "Group by variable redeclaration is illegal",
                                    getMetadata()
                            );
                        }
                        Item key;
                        try {
                            key = keyExpression.materializeAtMostOne(tupleContext);
                        } catch (MoreThanOneItemException e) {
                            throw new UnexpectedTypeException(
                                    "Keys in a group-by clause must be at most one item.",
                                    getMetadata()
                            );
                        }
                        List<Item> value = Collections.emptyList();
                        if (key != null) {
                            List<Item> atomized;
                            try {
                                atomized = key.atomizedValue();
                            } catch (CannotAtomizeException e) {
                                throw new UnexpectedTypeException(
                                        "Group by variable must atomize to a supported atomic value.",
                                        getMetadata()
                                );
                            }
                            if (atomized.size() > 1) {
                                throw new UnexpectedTypeException(
                                        "Keys in a group-by clause must atomize to at most one item.",
                                        getMetadata()
                                );
                            }
                            if (!atomized.isEmpty()) {
                                Item atomizedKey = atomized.get(0);
                                if (!atomizedKey.isAtomic()) {
                                    throw new UnexpectedTypeException(
                                            "Keys in a group-by clause must atomize to atomic values.",
                                            getMetadata()
                                    );
                                }
                                value = Collections.singletonList(atomizedKey);
                                keys.add(normalizeGroupingKey(atomizedKey, expression));
                            }
                        }
                        validateGroupingKeySequenceType(expression.getSequenceType(), value, tupleContext);
                        tuple.putValue(expression.getVariableName(), value);
                    } else {
                        Name variable = expression.getVariableName();
                        if (!tuple.contains(variable)) {
                            throw new InvalidGroupVariableException(
                                    "Variable " + variable + " cannot be used in group clause",
                                    getMetadata()
                            );
                        }
                        List<Item> atomized = new ArrayList<>();
                        for (
                            Item item : tupleContext.getVariableValues().getLocalVariableValue(variable, getMetadata())
                        ) {
                            try {
                                atomized.addAll(item.atomizedValue());
                            } catch (CannotAtomizeException e) {
                                throw new UnexpectedTypeException(
                                        "Group by variable must atomize to a supported atomic value.",
                                        getMetadata()
                                );
                            }
                        }
                        if (atomized.size() > 1) {
                            throw new UnexpectedTypeException(
                                    "Keys in a group-by clause must atomize to at most one item.",
                                    getMetadata()
                            );
                        }
                        validateGroupingKeySequenceType(expression.getSequenceType(), atomized, tupleContext);
                        tuple.putValue(variable, atomized);
                        if (atomized.size() == 1) {
                            keys.add(normalizeGroupingKey(atomized.get(0), expression));
                        }
                    }
                }
                tuplesByKey.computeIfAbsent(new FlworKey(keys), ignored -> new ArrayList<>()).add(tuple);
            }
        }
        List<FlworTuple> results = new ArrayList<>();
        tuplesByKey.values().forEach(group -> linearizeTuples(group, results));
        return results;
    }

    private Item normalizeGroupingKey(Item key, GroupByClauseSparkIteratorExpression expression) {
        return CollationSupport.normalizeItemForCollation(
            key,
            expression.getCollationURI() == null
                ? getRuntimeStaticContext().getDefaultCollation()
                : expression.getCollationURI(),
            getMetadata()
        );
    }

    private void validateGroupingKeySequenceType(
            SequenceType declaredType,
            List<Item> groupingKey,
            DynamicContext dynamicContext
    ) {
        if (declaredType == null) {
            return;
        }
        if (!declaredType.isResolved()) {
            declaredType.resolve(dynamicContext, getMetadata());
        }

        boolean validCardinality = switch (declaredType.getArity()) {
            case Zero -> groupingKey.isEmpty();
            case One -> groupingKey.size() == 1;
            case OneOrZero -> groupingKey.size() <= 1;
            case OneOrMore -> !groupingKey.isEmpty();
            case ZeroOrMore -> true;
        };
        if (!validCardinality) {
            throw new UnexpectedTypeException(
                    "The grouping key has cardinality "
                        + groupingKey.size()
                        + ", but the expected type is "
                        + declaredType,
                    getMetadata()
            );
        }
        for (Item item : groupingKey) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(declaredType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType() + " is not expected here. The expected type is " + declaredType,
                        getMetadata()
                );
            }
        }
    }

    /**
     * Iterate over all tuples to evaluate grouping
     */
    private void linearizeTuples(List<FlworTuple> keyTuplePairs, List<FlworTuple> output) {
        Iterator<FlworTuple> iterator = keyTuplePairs.iterator();
        FlworTuple oldFirstTuple = iterator.next();
        FlworTuple newTuple = new FlworTuple(
                this.getRuntimeStaticContext().getConfiguration(),
                oldFirstTuple.getLocalKeys().size()
        );

        // Iterate over local keys
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

        // Iterate over RDD keys
        for (Name tupleVariable : oldFirstTuple.getRDDKeys()) {
            iterator = keyTuplePairs.iterator();
            if (
                this.groupingExpressions.stream()
                    .anyMatch(v -> v.getVariableName().equals(tupleVariable))
            ) {
                newTuple.putValue(tupleVariable, oldFirstTuple.getRDDValue(tupleVariable, getMetadata()));
            } else {
                JavaRDD<Item> allValues = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .emptyRDD();
                while (iterator.hasNext()) {
                    allValues = allValues.union(iterator.next().getRDDValue(tupleVariable, getMetadata()));
                }
                newTuple.putValue(tupleVariable, allValues);
            }
        }

        // Iterate over DataFrame keys
        for (Name tupleVariable : oldFirstTuple.getDataFrameKeys()) {
            iterator = keyTuplePairs.iterator();
            if (
                this.groupingExpressions.stream()
                    .anyMatch(v -> v.getVariableName().equals(tupleVariable))
            ) {
                newTuple.putValue(tupleVariable, oldFirstTuple.getDataFrameValue(tupleVariable, getMetadata()));
            } else {
                HomogeneousItemDataFrame allValues = null;
                while (iterator.hasNext()) {
                    HomogeneousItemDataFrame df = iterator.next().getDataFrameValue(tupleVariable, getMetadata());
                    if (allValues == null) {
                        allValues = df;
                        continue;
                    }
                    allValues = allValues.union(df);
                }
                if (allValues == null) {
                    allValues = HomogeneousItemDataFrame.emptyDataFrame();
                }
                newTuple.putValue(tupleVariable, allValues);
            }
        }

        output.add(newTuple);
    }

    @Override
    public FlworDataFrame createNativeDataFrame(
            DynamicContext context
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid groupby clause.");
        }

        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (
                expression.getExpression() != null
                    && expression.getExpression().getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()
            ) {
                throw new JobWithinAJobException(
                        "A group by clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }
        }

        Dataset<Row> df = this.child.getDataFrame(context).getDataFrame();
        StructType inputSchema;
        // String[] columnNamesArray;
        // List<String> columnNames;

        List<Name> variableAccessNames = new ArrayList<>();
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            inputSchema = df.schema();
            // columnNamesArray = inputSchema.fieldNames();
            // columnNames = Arrays.asList(columnNamesArray);

            // TODO: consider add sequence type to group clause variable
            if (expression.getExpression() != null) {
                // if a variable is defined in-place with groupby, execute a let on the variable
                variableAccessNames.add(expression.getVariableName());
                df = LetClauseIterator.bindLetVariableInDataFrame(
                    df,
                    expression.getVariableName(),
                    null,
                    expression.getExpression(),
                    context,
                    new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
                    null,
                    false,
                    getConfiguration()
                );



            } else {
                if (!FlworDataFrameUtils.hasColumnForVariable(inputSchema, expression.getVariableName())) {
                    throw new InvalidGroupVariableException(
                            "Variable "
                                + expression.getVariableName()
                                + " cannot be used as a grouping key because it is not in the input tuple stream. It must be a variable from the same FLWOR expression).",
                            getMetadata()
                    );
                }
                variableAccessNames.add(expression.getVariableName());
            }
        }

        inputSchema = df.schema();

        String input = FlworDataFrameUtils.createTempView(df);

        Dataset<Row> nativeQueryResult = null;
        if (getConfiguration().nativeExecution()) {
            nativeQueryResult = tryNativeQuery(
                df,
                variableAccessNames,
                this.outputTupleProjection,
                inputSchema,
                context,
                input
            );
        }
        if (nativeQueryResult != null) {

            return new FlworDataFrame(nativeQueryResult);
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

        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(inputSchema);
        List<FlworDataFrameColumn> UDFcolumns = FlworDataFrameUtils.getColumns(
            inputSchema,
            groupingVariables
        );

        df.sparkSession()
            .udf()
            .register(
                "createGroupingColumns",
                new GroupClauseCreateColumnsUDF(
                        this.groupingExpressions,
                        context,
                        inputSchema,
                        UDFcolumns,
                        getMetadata()
                ),
                DataTypes.createStructType(typedFields)
            );

        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);

        String UDFParameters = FlworDataFrameUtils.getUDFParametersFromColumns(UDFcolumns);

        String createColumnsSQL = String.format(
            "select %s createGroupingColumns(%s) as `%s` from %s",
            selectSQL,
            UDFParameters,
            appendedGroupingColumnsName,
            input
        );

        StructType schemaType = df.schema();
        for (StructField sf : schemaType.fields()) {
            DataType dataType = sf.dataType();
            String name = sf.name();
            FlworDataFrameColumn dfColumn = new FlworDataFrameColumn(name, schemaType);
            if (dfColumn.isNativeSequence()) {
                int i = Math.abs(dataType.hashCode());
                df.sparkSession()
                    .udf()
                    .register(
                        "arraymerge" + i,
                        new GroupClauseArrayMergeAggregateResultsUDF(),
                        dataType
                    );
            }
        }

        String projectSQL = FlworDataFrameUtils.getGroupBySQLProjection(
            inputSchema,
            -1,
            false,
            serializerUDFName,
            variableAccessNames,
            this.outputTupleProjection
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
        return new FlworDataFrame(result);
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            if (iterator.getExpression() != null) {
                result.putAll(VariableDependencyRuntimePlan.get(iterator.getExpression()));
            } else {
                result.put(iterator.getVariableName(), DynamicContext.VariableDependency.FULL);
            }
        }
        for (Name var : this.child.getOutputTupleVariableNames()) {
            result.remove(var);
        }
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    @Override
    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            result.add(iterator.getVariableName());
        }
        result.addAll(this.child.getOutputTupleVariableNames());
        return result;
    }

    @Override
    public void print(StringBuilder buffer, int indent) {
        super.print(buffer, indent);
        for (GroupByClauseSparkIteratorExpression iterator : this.groupingExpressions) {
            for (int i = 0; i < indent + 1; ++i) {
                buffer.append("  ");
            }
            buffer.append("Variable ").append(iterator.getVariableName()).append("\n");
            if (iterator.getExpression() != null) {
                RuntimePlanDiagnostics.print(iterator.getExpression(), buffer, indent + 1);
            }
        }
    }

    @Override
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
            Map<Name, DynamicContext.VariableDependency> exprDependency =
                VariableDependencyRuntimePlan.get(iterator.getExpression());
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
            DynamicContext context,
            String input
    ) {
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (expression.getSequenceType() != null) {
                return null;
            }
        }

        StringBuilder groupByString = new StringBuilder();
        String sep = " ";
        for (Name groupingVar : groupingVariables) {
            if (!FlworDataFrameUtils.isVariableAvailableAsNativeItem(inputSchema, groupingVar)) {
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
            if (groupingVariables.contains(entry.getKey())) {
                // we are considering one of the grouping variables
                selectString.append(entry.getKey().toString());
                continue;
            }
            if (FlworDataFrameUtils.isVariableAvailableAsCountOnly(inputSchema, entry.getKey())) {
                // we are summing over a previous count
                selectString.append("sum(`");
                selectString.append(entry.getKey().toString());
                selectString.append(".count");
                selectString.append("`) as `");
                selectString.append(entry.getKey().toString());
                selectString.append(".count");
                selectString.append("`");
                continue;
            }
            if (entry.getValue() == DynamicContext.VariableDependency.COUNT) {
                if (FlworDataFrameUtils.isVariableAvailableAsNativeSequence(inputSchema, entry.getKey())) {
                    FlworDataFrameColumn dfColumnSequence = new FlworDataFrameColumn(
                            entry.getKey(),
                            ColumnFormat.NATIVE_SEQUENCE
                    );
                    FlworDataFrameColumn dfColumnCount = new FlworDataFrameColumn(entry.getKey(), ColumnFormat.COUNT);
                    selectString.append("sum(cardinality(");
                    selectString.append(dfColumnSequence);
                    selectString.append(")) as ");
                    selectString.append(dfColumnCount);
                    continue;
                }
                // we need a count
                selectString.append("count(`");
                selectString.append(entry.getKey().toString());
                selectString.append("`) as `");
                selectString.append(entry.getKey().toString());
                selectString.append(".count`");
                continue;
            }
            if (FlworDataFrameUtils.isVariableAvailableAsNativeSequence(inputSchema, entry.getKey())) {
                // we cannot merge arrays natively in Spark, strangely.
                return null;
            }
            // we collect all the values, if it is a binary object we just switch over to udf
            FlworDataFrameColumn dfColumnSequence = new FlworDataFrameColumn(
                    entry.getKey(),
                    ColumnFormat.NATIVE_SEQUENCE
            );
            String columnName = entry.getKey().toString();
            StructField field = inputSchema.fields()[inputSchema.fieldIndex(columnName)];
            if (field.dataType().equals(DataTypes.BinaryType)) {
                return null;
            }
            selectString.append("collect_list(");
            selectString.append(columnName);
            selectString.append(") as ");
            selectString.append(dfColumnSequence);
        }
        LogManager.getLogger("GroupByClauseSparkIterator")
            .info("Rumble was able to optimize a group by clause to a native SQL query.");
        return dataFrame.sparkSession()
            .sql(
                String.format(
                    "select %s from %s group by %s",
                    selectString,
                    input,
                    groupByString
                )
            );
    }

    @Override
    public boolean containsClause(FLWOR_CLAUSES kind) {
        if (kind == FLWOR_CLAUSES.GROUP_BY) {
            return true;
        }
        if (this.child == null || this.evaluationDepthLimit == 0) {
            return false;
        }
        return this.child.containsClause(kind);
    }

    /**
     * Says whether this expression evaluation triggers a Spark job.
     *
     * @return true if the execution triggers a Spark, false otherwise, null if undetermined yet.
     */
    @Override
    public boolean isSparkJobNeeded() {
        if (RuntimePlanDiagnostics.isSparkJobNeeded(this.child)) {
            return true;
        }
        for (GroupByClauseSparkIteratorExpression i : this.groupingExpressions) {
            if (i.getExpression() != null) {
                if (RuntimePlanDiagnostics.isSparkJobNeeded(i.getExpression())) {
                    return true;
                }
            }
        }
        switch (getHighestExecutionMode()) {
            case DATAFRAME:
                return true;
            case LOCAL:
                return false;
            case RDD:
                return true;
            case UNSET:
                return false;
            default:
                return false;
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (expression.getSequenceType() != null) {
                return NativeClauseContext.NoNativeQuery;
            }
        }
        List<FlworDataFrameColumn> dfColumns = FlworDataFrameUtils.getColumns(
            (StructType) nativeClauseContext.getSchema(),
            null,
            null,
            null
        );
        NativeClauseContext childContext = NativeQueryRuntimePlan.generate(
            this.child,
            nativeClauseContext
        );
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            (StructType) childContext.getSchema(),
            null,
            null,
            null
        );
        String view = childContext.getView();
        childContext.setView(null);
        // get all variables, get expressions for grouping
        Map<Name, String> bindingColumns = new HashMap<>();
        List<String> groupingVars = new ArrayList<>();
        groupingVars.add(nativeClauseContext.getRowIdField());
        for (GroupByClauseSparkIteratorExpression expression : this.groupingExpressions) {
            if (expression.getExpression() != null) {
                NativeClauseContext expressionContext = NativeQueryRuntimePlan.generate(
                    expression.getExpression(),
                    childContext
                );
                if (expressionContext == NativeClauseContext.NoNativeQuery) {
                    return NativeClauseContext.NoNativeQuery;
                }
                Name name = childContext.addVariable(expression.getVariableName());
                bindingColumns.put(name, expressionContext.getResultingQuery());
                childContext.setSchema(
                    ((StructType) childContext.getSchema()).add(
                        name.toString(),
                        TypeMappings.getDataFrameDataTypeFromItemType(
                            expressionContext.getResultingType().getItemType(),
                            this.getStaticContext()
                        )
                    )
                );
                groupingVars.add(name.toString());
            } else {
                Name name = childContext.getVariable(expression.getVariableName());
                if (
                    !FlworDataFrameUtils.hasColumnForVariable(
                        (StructType) childContext.getSchema(),
                        name
                    )
                ) {
                    throw new InvalidGroupVariableException(
                            "Variable "
                                + name
                                + " cannot be used as a grouping key because it is not in the input tuple stream. It must be a variable from the same FLWOR expression.",
                            getMetadata()
                    );
                }
                groupingVars.add(name.toString());
            }
        }
        // bind variables that have an expression
        if (!bindingColumns.isEmpty()) {
            view = String.format(
                "select %s %s from (%s)",
                FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
                bindingColumns.entrySet()
                    .stream()
                    .map(entry -> String.format("(%s) as `%s`", entry.getValue(), entry.getKey().toString()))
                    .collect(Collectors.joining(", ")),
                view
            );
            allColumns = FlworDataFrameUtils.getColumns(
                (StructType) childContext.getSchema(),
                null,
                null,
                null
            );
        }
        // create aggregation for each column
        List<String> selectionStrings = new ArrayList<>();
        String conditionString = childContext.getConditionalColumns()
            .stream()
            .map(name -> "`" + name + "`")
            .collect(Collectors.joining(" and "));
        StructType ungroupedSchema = (StructType) childContext.getSchema();
        StructType newSchema = (StructType) nativeClauseContext.getSchema();
        for (FlworDataFrameColumn column : allColumns) {
            // group by doesn't preserve ordering
            if (nativeClauseContext.getSortingColumns().containsKey(column.getColumnName())) {
                continue;
            }
            DataType type = ungroupedSchema.fields()[ungroupedSchema.fieldIndex(column.getColumnName())].dataType();
            if (groupingVars.contains(column.getVariableName().toString())) {
                // if it's a grouping variable, don't aggregate
                selectionStrings.add("`" + column.getColumnName() + "`");
                if (dfColumns.stream().noneMatch(dfColumn -> dfColumn.getColumnName().equals(column.getColumnName()))) {
                    newSchema = newSchema.add(column.getColumnName(), type);
                }
            } else if (
                dfColumns.stream().anyMatch(dfColumn -> dfColumn.getColumnName().equals(column.getColumnName()))
            ) {
                // if it's in the original dataframe, don't aggregate
                selectionStrings.add(
                    String.format("first(`%s`) as `%s`", column.getColumnName(), column.getColumnName())
                );
            } else {
                if (column.isCount()) {
                    selectionStrings.add(
                        String.format("sum(`%s`) as `%s`", column.getColumnName(), column.getColumnName())
                    );
                    newSchema = newSchema.add(column.getColumnName(), type);
                } else if (
                    this.outputTupleProjection.entrySet()
                        .stream()
                        .filter(
                            out -> nativeClauseContext.getVariable(out.getKey())
                                .toString()
                                .equals(column.getColumnName())
                        )
                        .map(entry -> entry.getValue() == DynamicContext.VariableDependency.COUNT)
                        .findFirst()
                        .orElse(false)
                ) {
                    FlworDataFrameColumn countColumn = new FlworDataFrameColumn(
                            column.getVariableName(),
                            ColumnFormat.COUNT
                    );
                    selectionStrings.add(
                        String.format("count(`%s`) as `%s`", column.getColumnName(), countColumn.getColumnName())
                    );
                    newSchema = newSchema.add(countColumn.getColumnName(), DataTypes.IntegerType);
                } else if (column.isNativeSequence()) {
                    // if it's a sequence, use flatten
                    selectionStrings.add(
                        String.format(
                            "flatten(collect_list(`%s`)) as `%s`",
                            column.getColumnName(),
                            column.getColumnName()
                        )
                    );
                    newSchema = newSchema.add(column.getColumnName(), type);
                } else {
                    if (!childContext.getConditionalColumns().contains(column.getColumnName())) {
                        String groupedColumnName = column.getColumnName() + ".sequence";
                        if (childContext.getConditionalColumns().size() > 0) {
                            selectionStrings.add(
                                String.format(
                                    "collect_list(if(%s, `%s`, null)) as `%s`",
                                    conditionString,
                                    column.getColumnName(),
                                    groupedColumnName
                                )
                            );
                        } else {
                            selectionStrings.add(
                                String.format(
                                    "collect_list(`%s`) as `%s`",
                                    column.getColumnName(),
                                    groupedColumnName
                                )
                            );
                        }
                        newSchema = newSchema.add(groupedColumnName, type);
                    }
                }
            }
        }
        childContext.clearConditionalColumns();
        childContext.clearSortingColumns();
        childContext.setGrouped(true);
        childContext.setSchema(newSchema);
        String groupingString = String.format(
            "select %s from (%s) group by %s",
            String.join(",", selectionStrings),
            view,
            groupingVars.stream().map(name -> "`" + name + "`").collect(Collectors.joining(","))
        );
        childContext.setView(groupingString);
        return new NativeClauseContext(childContext, null, null);
    }
}
