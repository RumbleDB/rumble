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

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrame;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn.ColumnFormat;
import org.rumbledb.runtime.flwor.udfs.GenericLetClauseUDF;
import org.rumbledb.runtime.flwor.udfs.GroupClauseSerializeAggregateResultsUDF;
import org.rumbledb.runtime.flwor.udfs.HashUDF;
import org.rumbledb.runtime.flwor.udfs.ExpressionEvaluationUDF;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.navigation.PredicateIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

// import org.rumbledb.exceptions.ExceptionMetadata;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.math.BigDecimal;
import java.util.*;

public class LetClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private Name variableName; // for efficient use in local iteration
    private SequenceType sequenceType;
    private RuntimeIterator assignmentIterator;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple nextLocalTupleResult;

    public LetClauseSparkIterator(
            RuntimeTupleIterator child,
            Name variableName,
            SequenceType sequenceType,
            RuntimeIterator assignmentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(child, staticContext);
        this.variableName = variableName;
        this.sequenceType = sequenceType;
        this.assignmentIterator = assignmentIterator;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child == null || this.evaluationDepthLimit == 0) {
            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(null);
            this.tupleContext = this.currentDynamicContext;
        } else {
            this.child.open(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            setNextLocalTupleResult();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child == null || this.evaluationDepthLimit == 0) {
            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(null);
            this.tupleContext = this.currentDynamicContext;
        } else {
            this.child.reset(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            setNextLocalTupleResult();
        }
    }

    private void setNextLocalTupleResult() {
        // if starting clause: result is a single tuple -> no more tuples after the first next call
        if (this.child == null || this.evaluationDepthLimit == 0) {
            this.hasNext = false;
            return;
        }

        if (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata()); // assign new
                                                                                                   // variables from new
                                                                                                   // tuple
            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(inputTuple);
            this.hasNext = true;
        } else {
            this.child.close();
            this.hasNext = false;
        }
    }

    private FlworTuple generateTupleFromExpressionWithContext(FlworTuple inputTuple) {
        FlworTuple resultTuple;
        if (inputTuple == null) {
            resultTuple = new FlworTuple(this.getConfiguration());
        } else {
            resultTuple = new FlworTuple(inputTuple);
        }
        if (this.assignmentIterator.isDataFrame()) {
            JSoundDataFrame df = this.assignmentIterator.getDataFrame(this.tupleContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext);
            resultTuple.putValue(this.variableName, df);
        } else if (this.assignmentIterator.isRDDOrDataFrame()) {
            JavaRDD<Item> itemRDD = this.assignmentIterator.getRDD(this.tupleContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext);
            resultTuple.putValue(this.variableName, itemRDD);
        } else {
            List<Item> results = new ArrayList<>();
            this.assignmentIterator.open(this.tupleContext);
            while (this.assignmentIterator.hasNext()) {
                results.add(this.assignmentIterator.next());
            }
            this.assignmentIterator.close();
            resultTuple.putValue(this.variableName, results);
        }
        return resultTuple;
    }

    @Override
    public FlworTuple next() {
        if (this.hasNext) {
            FlworTuple result = this.nextLocalTupleResult; // save the result to be returned
            setNextLocalTupleResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    @Override
    public void close() {
        this.isOpen = false;
        if (this.child != null && this.evaluationDepthLimit != 0) {
            this.child.close();
        }
    }

    @Override
    public FlworDataFrame getDataFrame(
            DynamicContext context
    ) {
        if (this.child != null && this.evaluationDepthLimit != 0) {
            FlworDataFrame df = this.child.getDataFrame(context);

            if (!this.outputTupleProjection.containsKey(this.variableName)) {
                return df;
            }

            if (this.assignmentIterator.isRDDOrDataFrame()) {
                return getDataFrameAsJoin(context, this.outputTupleProjection, df);
            }

            Dataset<Row> result = bindLetVariableInDataFrame(
                df.getDataFrame(),
                this.variableName,
                this.sequenceType,
                this.assignmentIterator,
                context,
                (this.child == null || this.evaluationDepthLimit == 0)
                    ? Collections.emptyList()
                    : new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
                this.outputTupleProjection,
                false,
                getConfiguration()
            );

            return new FlworDataFrame(result);
        }
        throw new RuntimeException(
                "Unexpected program state reached. Initial let clauses are always locally executed."
        );
    }

    public FlworDataFrame getDataFrameAsJoin(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection,
            FlworDataFrame childDF
    ) {
        // We try to detect an equi-join.

        // Is this a predicate expression?
        if (!(this.assignmentIterator instanceof PredicateIterator)) {
            throw new JobWithinAJobException(
                    "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. Rumble is able to handle large scale left outer joins, but this requires the let expression to be a predicate expression, the left-hand-side of which is independent from the previous variables of the current FLWOR expression.",
                    getMetadata()
            );
        }

        RuntimeIterator sequenceIterator = ((PredicateIterator) this.assignmentIterator).sequenceIterator();
        RuntimeIterator predicateIterator = ((PredicateIterator) this.assignmentIterator).predicateIterator();

        // Is the left-hand-side of this predicate expression independent from input tuples?
        if (!isExpressionIndependentFromInputTuple(sequenceIterator, this.child)) {
            throw new JobWithinAJobException(
                    "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. Rumble attempted to detect a join but the left-hand-side of the predicate expression in this let clause depends on the previous variables of the current FLWOR expression. You can try again by making sure that such is not the case.",
                    getMetadata()
            );
        }

        // Is the predicate a comparison?
        if (!(predicateIterator instanceof ComparisonIterator)) {
            throw new JobWithinAJobException(
                    "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. We did detect a predicate expression, but the criterion inside the predicate is not a comparison.",
                    getMetadata()
            );

        }
        ComparisonIterator comparisonIterator = (ComparisonIterator) predicateIterator;
        // Is the predicate a value equality comparison?
        if (!comparisonIterator.isValueEquality()) {
            throw new JobWithinAJobException(
                    "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. We did detect a predicate expression, but the criterion inside the predicate is not a value equality comparison.",
                    getMetadata()
            );
        }

        // Is the equality comparing the left hand side of the predicate with the input tuple?
        RuntimeIterator leftHandSideOfJoinEqualityCriterion = comparisonIterator.getLeftIterator();
        RuntimeIterator rightHandSideOfJoinEqualityCriterion = comparisonIterator.getRightIterator();
        Set<Name> leftDependencies = new HashSet<>(
                leftHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
        );
        Set<Name> rightDependencies = new HashSet<>(
                rightHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
        );
        RuntimeIterator contextItemValueExpression = null;
        RuntimeIterator inputTupleValueExpression = null;
        if (leftDependencies.size() == 1 && leftDependencies.contains(Name.CONTEXT_ITEM)) {
            if (!rightDependencies.contains(Name.CONTEXT_ITEM)) {
                contextItemValueExpression = leftHandSideOfJoinEqualityCriterion;
                inputTupleValueExpression = rightHandSideOfJoinEqualityCriterion;
            } else {
                throw new JobWithinAJobException(
                        "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. We did detect a predicate expression, but the criterion inside the predicate is not comparing the left-hand-side of this predicate to the input tuple.",
                        getMetadata()
                );
            }
        }

        if (rightDependencies.size() == 1 && rightDependencies.contains(Name.CONTEXT_ITEM)) {
            if (!leftDependencies.contains(Name.CONTEXT_ITEM)) {
                contextItemValueExpression = rightHandSideOfJoinEqualityCriterion;
                inputTupleValueExpression = leftHandSideOfJoinEqualityCriterion;
            } else {
                throw new JobWithinAJobException(
                        "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. We did detect a predicate expression, but the criterion inside the predicate is not comparing the left-hand-side of this predicate to the input tuple.",
                        getMetadata()
                );
            }
        }
        if (inputTupleValueExpression == null) {
            throw new JobWithinAJobException(
                    "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. We did detect a predicate expression, but the criterion inside the predicate is not comparing the left-hand-side of this predicate to the input tuple.",
                    getMetadata()
            );
        }

        // Now we know we can execute the query as an equi-join.
        // First, we evaluate all input tuples.
        Dataset<Row> inputDF = this.child.getDataFrame(context).getDataFrame();

        // We resolve the dependencies of the predicate expression.
        // If the predicate depends on position() or last(), we are not able yet to support this.
        Map<Name, VariableDependency> predicateDependencies = predicateIterator.getVariableDependencies();
        if (predicateDependencies.containsKey(Name.CONTEXT_POSITION)) {
            throw new UnsupportedFeatureException(
                    "Rumble detected an equi-join, but does not support yet position() in the join predicate.",
                    getMetadata()
            );
        }
        if (predicateDependencies.containsKey(Name.CONTEXT_COUNT)) {
            throw new UnsupportedFeatureException(
                    "Rumble detected an equi-join, but does not support yet last() in the join predicate.",
                    getMetadata()
            );
        }

        // Now we execute the left-hand-side of the predicate, which is the right side of the join.
        // We need to manually adjust the context item with the dependency mode the parent projection.
        Map<Name, VariableDependency> sequenceDependencies = new HashMap<>();
        sequenceDependencies.put(Name.CONTEXT_ITEM, DynamicContext.VariableDependency.FULL);
        Dataset<Row> expressionDF = ForClauseSparkIterator.getDataFrameStartingClause(
            sequenceIterator,
            Name.CONTEXT_ITEM,
            null,
            false,
            context,
            sequenceDependencies
        ).getDataFrame();

        // We compute the hashes for both sides of the equality predicate.
        expressionDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
            expressionDF,
            Name.createVariableInNoNamespace(SparkSessionManager.rightHandSideHashColumnName),
            this.sequenceType,
            contextItemValueExpression,
            context,
            Collections.singletonList(Name.CONTEXT_ITEM),
            null,
            true,
            getConfiguration()
        );

        inputDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
            inputDF,
            Name.createVariableInNoNamespace(SparkSessionManager.leftHandSideHashColumnName),
            this.sequenceType,
            inputTupleValueExpression,
            context,
            (this.child == null || this.evaluationDepthLimit == 0)
                ? Collections.emptyList()
                : new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null,
            true,
            getConfiguration()
        );

        // We group the right-hand-side of the join by hash to prepare the left outer join.
        String hashedExpressionResults = FlworDataFrameUtils.createTempView(expressionDF);
        FlworDataFrameColumn variableNameAggregatedColumn = new FlworDataFrameColumn(
                this.variableName,
                FlworDataFrameColumn.ColumnFormat.NATIVE_SEQUENCE
        );
        boolean isBinary = FlworDataFrameUtils.isVariableAvailableAsSerializedSequence(
            expressionDF.schema(),
            Name.CONTEXT_ITEM
        );
        expressionDF = expressionDF.sparkSession()
            .sql(
                String.format(
                    "SELECT `%s`, collect_list(`%s`) AS %s FROM %s GROUP BY `%s`",
                    SparkSessionManager.rightHandSideHashColumnName,
                    Name.CONTEXT_ITEM.toString(),
                    variableNameAggregatedColumn,
                    hashedExpressionResults,
                    SparkSessionManager.rightHandSideHashColumnName
                )
            );


        // We serialize back all grouped items as sequences of items.
        if (isBinary) {
            String groupedResults = FlworDataFrameUtils.createTempView(expressionDF);
            expressionDF.sparkSession()
                .udf()
                .register(
                    "serializeArray",
                    new GroupClauseSerializeAggregateResultsUDF(),
                    DataTypes.BinaryType
                );
            FlworDataFrameColumn newVariableNameAggregatedColumn = new FlworDataFrameColumn(
                    this.variableName,
                    FlworDataFrameColumn.ColumnFormat.SERIALIZED_SEQUENCE
            );

            expressionDF = expressionDF.sparkSession()
                .sql(
                    String.format(
                        "SELECT `%s`, serializeArray(%s) AS %s FROM %s",
                        SparkSessionManager.rightHandSideHashColumnName,
                        variableNameAggregatedColumn,
                        newVariableNameAggregatedColumn,
                        groupedResults
                    )
                );
            variableNameAggregatedColumn = newVariableNameAggregatedColumn;
        }
        String groupedAndSerializedResults = FlworDataFrameUtils.createTempView(expressionDF);
        variableNameAggregatedColumn.setTableName(groupedAndSerializedResults);
        String inputTuples = FlworDataFrameUtils.createTempView(inputDF);

        // We gather the columns to select.
        // We need to project away the let clause variable because we re-create it.
        StructType inputSchema = inputDF.schema();
        List<Name> variableNamesToExclude = new ArrayList<>();
        variableNamesToExclude.add(this.variableName);
        Map<Name, VariableDependency> prefilterProjection = DynamicContext.copyVariableDependencies(parentProjection);
        DynamicContext.mergeVariableDependencies(prefilterProjection, predicateDependencies);
        prefilterProjection.put(this.variableName, prefilterProjection.get(Name.CONTEXT_ITEM));
        prefilterProjection.remove(Name.CONTEXT_ITEM);
        List<FlworDataFrameColumn> columnsToSelect = FlworDataFrameUtils.getColumns(
            inputSchema,
            prefilterProjection,
            null,
            variableNamesToExclude
        );
        String projectionVariables = FlworDataFrameUtils.getSQLColumnProjection(columnsToSelect, true);

        // Now we proceed with the left outer join.
        inputDF = inputDF.sparkSession()
            .sql(
                String.format(
                    "SELECT %s %s FROM %s LEFT OUTER JOIN %s ON `%s` = `%s`",
                    projectionVariables,
                    variableNameAggregatedColumn,
                    inputTuples,
                    groupedAndSerializedResults,
                    SparkSessionManager.rightHandSideHashColumnName,
                    SparkSessionManager.leftHandSideHashColumnName
                )
            );
        // We now post-filter on the predicate, by hash group.
        RuntimeIterator filteringPredicateIterator = new PredicateIterator(
                new VariableReferenceIterator(
                        this.variableName,
                        new RuntimeStaticContext(
                                getConfiguration(),
                                SequenceType.ITEM_STAR,
                                ExecutionMode.LOCAL,
                                getMetadata()
                        )
                ),
                predicateIterator,
                new RuntimeStaticContext(
                        getConfiguration(),
                        SequenceType.ITEM_STAR,
                        ExecutionMode.LOCAL,
                        getMetadata()
                )
        );
        inputDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
            inputDF,
            this.variableName,
            this.sequenceType,
            filteringPredicateIterator,
            context,
            new ArrayList<Name>(this.getOutputTupleVariableNames()),
            parentProjection,
            false,
            getConfiguration()
        );

        return new FlworDataFrame(inputDF);
    }

    public static boolean isExpressionIndependentFromInputTuple(
            RuntimeIterator sequenceIterator,
            RuntimeTupleIterator tupleIterator
    ) {
        // Check that the expression does not depend functionally on the input tuples
        Set<Name> intersection = new HashSet<>(
                sequenceIterator.getVariableDependencies().keySet()
        );
        intersection.retainAll(tupleIterator.getOutputTupleVariableNames());
        return intersection.isEmpty();
    }

    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.assignmentIterator.getVariableDependencies());
        if (this.child != null && this.evaluationDepthLimit != 0) {
            for (Name var : this.child.getOutputTupleVariableNames()) {
                result.remove(var);
            }
            result.putAll(this.child.getDynamicContextVariableDependencies());
        }
        return result;
    }

    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        if (this.child != null && this.evaluationDepthLimit != 0) {
            result.addAll(this.child.getOutputTupleVariableNames());
        }
        result.add(this.variableName);
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (int i = 0; i < indent + 1; ++i) {
            buffer.append("  ");
        }
        buffer.append("Variable ").append(this.variableName).append("\n");
        this.assignmentIterator.print(buffer, indent + 1);
    }

    public Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null || this.evaluationDepthLimit == 0) {
            return Collections.emptyMap();
        }

        // start with an empty projection.

        // copy over the projection needed by the parent clause.
        Map<Name, DynamicContext.VariableDependency> projection =
            new TreeMap<>(parentProjection);

        // remove the variable that this clause binds.
        projection.remove(this.variableName);

        // add the variable dependencies needed by this for clause's expression.
        Map<Name, DynamicContext.VariableDependency> exprDependency = this.assignmentIterator
            .getVariableDependencies();
        for (Name variable : exprDependency.keySet()) {
            if (projection.containsKey(variable)) {
                if (projection.get(variable) != exprDependency.get(variable)) {
                    if (
                        this.child != null
                            && this.evaluationDepthLimit != 0
                            && this.child.getOutputTupleVariableNames().contains(variable)
                    ) {
                        projection.put(variable, DynamicContext.VariableDependency.FULL);
                    }
                }
            } else {
                if (
                    this.child != null
                        && this.evaluationDepthLimit != 0
                        && this.child.getOutputTupleVariableNames().contains(variable)
                ) {
                    projection.put(variable, exprDependency.get(variable));
                }
            }
        }
        return projection;
    }

    /**
     * Extends a DataFrame with a new column obtained from the evaluation of an expression for each tuple.
     * 
     * @param dataFrame the DataFrame to extend
     * @param newVariableName the name of the new column (variable)
     * @param sequenceType the sequence type of the new bound item, not used in case of hash
     * @param newVariableExpression the expression to evaluate
     * @param context the context (in addition to each tuple) in which to evaluation the expression
     * @param variablesInInputTuple the name of the variables that can be found in the input tuple (as opposed to those
     *        in the context)
     * @param outputTupleVariableDependencies the dependencies to project to (possibly null to keep everything).
     * @param hash whether or not to compute single-item hashes rather than the actual serialized sequences of items.
     * @return the DataFrame with the new column
     */
    public static Dataset<Row> bindLetVariableInDataFrame(
            Dataset<Row> dataFrame,
            Name newVariableName,
            SequenceType sequenceType,
            RuntimeIterator newVariableExpression,
            DynamicContext context,
            List<Name> variablesInInputTuple,
            Map<Name, DynamicContext.VariableDependency> outputTupleVariableDependencies,
            boolean hash,
            RumbleRuntimeConfiguration conf
    ) {
        StructType inputSchema = dataFrame.schema();
        // inputSchema.printTreeString();

        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            inputSchema,
            outputTupleVariableDependencies,
            null,
            Collections.singletonList(newVariableName)
        );
        // for (String c : allColumns) {
        // System.out.println(c);
        // }

        // if we can (depending on the expression) use let natively without UDF

        if (!hash) {
            Dataset<Row> nativeQueryResult = null;
            if (conf.nativeExecution()) {
                nativeQueryResult = tryNativeQuery(
                    dataFrame,
                    newVariableName,
                    newVariableExpression,
                    allColumns,
                    inputSchema,
                    context
                );
            }

            if (nativeQueryResult != null) {
                return nativeQueryResult;
            }
        }

        // for (Name n : newVariableExpression.getVariableDependencies().keySet()) {
        // System.out.println(n.toString() + " -> " + newVariableExpression.getVariableDependencies().get(n));
        // }
        //
        // for (Name n : variablesInInputTuple) {
        // System.out.println(n.toString() + " in input");
        // }

        // was not possible, we use let udf
        List<FlworDataFrameColumn> UDFcolumns = FlworDataFrameUtils.getColumns(
            inputSchema,
            newVariableExpression.getVariableDependencies(),
            variablesInInputTuple,
            null
        );
        // for (String c : UDFcolumns) {
        // System.out.println("UDF " + c);
        // }

        boolean isNative = false;
        if (!hash) {
            isNative = registerLetClauseUDF(
                dataFrame,
                newVariableExpression,
                context,
                inputSchema,
                UDFcolumns,
                sequenceType
            );
        } else {
            dataFrame.sparkSession()
                .udf()
                .register(
                    "hashUDF",
                    new HashUDF(newVariableExpression, context, UDFcolumns),
                    DataTypes.LongType
                );
        }

        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);
        String UDFParameters = FlworDataFrameUtils.getUDFParametersFromColumns(UDFcolumns);

        String input = FlworDataFrameUtils.createTempView(dataFrame);

        FlworDataFrameColumn dfColumnSequence = new FlworDataFrameColumn(newVariableName, ColumnFormat.NATIVE_SEQUENCE);
        FlworDataFrameColumn dfColumnNative = new FlworDataFrameColumn(newVariableName, ColumnFormat.FULLY_NATIVE);

        if (!hash) {
            dataFrame = dataFrame.sparkSession()
                .sql(
                    String.format(
                        "select %s letClauseUDF(%s) as %s from %s",
                        selectSQL,
                        UDFParameters,
                        isNative ? dfColumnNative : dfColumnSequence,
                        input
                    )
                );


        } else {
            dataFrame = dataFrame.sparkSession()
                .sql(
                    String.format(
                        "select %s hashUDF(%s) as %s from %s",
                        selectSQL,
                        UDFParameters,
                        dfColumnNative,
                        input
                    )
                );
        }
        return dataFrame;
    }

    public static boolean registerLetClauseUDF(
            Dataset<Row> dataFrame,
            RuntimeIterator newVariableExpression,
            DynamicContext context,
            StructType inputSchema,
            List<FlworDataFrameColumn> UDFcolumns,
            SequenceType sequenceType
    ) {
        // for the moment we only consider native types with single arity (what about optional)
        if (
            sequenceType != null
                && !sequenceType.isEmptySequence()
                && sequenceType.getArity().equals(SequenceType.Arity.One)
        ) {
            ItemType itemType = sequenceType.getItemType();

            if (itemType.equals(BuiltinTypesCatalogue.stringItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new GenericLetClauseUDF<String>(
                                newVariableExpression,
                                context,
                                UDFcolumns,
                                "String"
                        ),
                        DataTypes.StringType
                    );
                return true;
            }

            if (itemType.equals(BuiltinTypesCatalogue.intItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new GenericLetClauseUDF<Integer>(
                                newVariableExpression,
                                context,
                                UDFcolumns,
                                "Integer"
                        ),
                        DataTypes.IntegerType
                    );
                return true;
            }

            if (itemType.equals(BuiltinTypesCatalogue.integerItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new GenericLetClauseUDF<Integer>(
                                newVariableExpression,
                                context,
                                UDFcolumns,
                                "BigDecimal"
                        ),
                        TypeMappings.decimalType
                    );
                return true;
            }

            if (itemType.equals(BuiltinTypesCatalogue.decimalItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new GenericLetClauseUDF<BigDecimal>(
                                newVariableExpression,
                                context,
                                UDFcolumns,
                                "BigDecimal"
                        ),
                        TypeMappings.decimalType
                    );
                return true;
            }

            if (itemType.equals(BuiltinTypesCatalogue.doubleItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "letClauseUDF",
                        new GenericLetClauseUDF<Double>(
                                newVariableExpression,
                                context,
                                UDFcolumns,
                                "Double"
                        ),
                        DataTypes.DoubleType
                    );
                return true;
            }
        }

        // if it is not one of the allowed sequence type we just return the default udf
        dataFrame.sparkSession()
            .udf()
            .register(
                "letClauseUDF",
                new ExpressionEvaluationUDF(newVariableExpression, context, UDFcolumns),
                DataTypes.createArrayType(DataTypes.BinaryType)
            );
        return false;
    }

    /**
     * Try to generate the native query for the let clause and run it, if successful return the resulting dataframe,
     * otherwise it returns null
     *
     * @param dataFrame input dataframe for the query
     * @param newVariableName name of the new bound variable
     * @param iterator let variable assignment expression iterator
     * @param allColumns other columns required in following clauses
     * @param inputSchema input schema of the dataframe
     * @param context current dynamic context of the dataframe
     * @return resulting dataframe of the let clause if successful, null otherwise
     */
    public static Dataset<Row> tryNativeQuery(
            Dataset<Row> dataFrame,
            Name newVariableName,
            RuntimeIterator iterator,
            List<FlworDataFrameColumn> allColumns,
            StructType inputSchema,
            DynamicContext context
    ) {
        String input = FlworDataFrameUtils.createTempView(dataFrame);
        NativeClauseContext letContext = new NativeClauseContext(FLWOR_CLAUSES.LET, inputSchema, context);
        letContext.setView(input);
        NativeClauseContext nativeQuery = iterator.generateNativeQuery(letContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);
        LogManager.getLogger("LetClauseSparkIterator")
            .info(
                "Rumble was able to optimize a let clause to a native SQL query: "
                    + String.format(
                        "select %s %s as `%s` from (%s)",
                        selectSQL,
                        nativeQuery.getResultingQuery(),
                        SequenceType.Arity.OneOrMore.isSubtypeOf(nativeQuery.getResultingType().getArity())
                            ? newVariableName + ".sequence"
                            : newVariableName,
                        nativeQuery.getView()
                    )
            );
        return dataFrame.sparkSession()
            .sql(
                String.format(
                    "select %s %s as `%s` from (%s)",
                    selectSQL,
                    nativeQuery.getResultingQuery(),
                    SequenceType.Arity.OneOrMore.isSubtypeOf(nativeQuery.getResultingType().getArity())
                        ? newVariableName + ".sequence"
                        : newVariableName,
                    nativeQuery.getView()
                )
            );
    }

    public boolean containsClause(FLWOR_CLAUSES kind) {
        if (kind == FLWOR_CLAUSES.LET) {
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
        if (this.child.isSparkJobNeeded()) {
            return true;
        }
        if (this.assignmentIterator.isSparkJobNeeded()) {
            return true;
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
        if (this.child != null) {
            nativeClauseContext = this.child.generateNativeQuery(nativeClauseContext);
            if (nativeClauseContext == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }
        } else if (nativeClauseContext.getView() == null) {
            return NativeClauseContext.NoNativeQuery;
        }
        nativeClauseContext.setClauseType(FLWOR_CLAUSES.LET);
        NativeClauseContext expressionContext = this.assignmentIterator.generateNativeQuery(nativeClauseContext);
        if (expressionContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        String variableName = nativeClauseContext.addVariable(this.variableName).toString();
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            (StructType) nativeClauseContext.getSchema(),
            null,
            null,
            null
        );
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(expressionContext.getResultingType().getArity())) {
            variableName = variableName + ".sequence";
        }
        String resultString = String.format(
            "select %s %s as `%s` from (%s)",
            FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
            expressionContext.getResultingQuery(),
            variableName,
            expressionContext.getView()
        );
        NativeClauseContext letClauseContext = new NativeClauseContext(nativeClauseContext);
        letClauseContext.setSchema(
            ((StructType) letClauseContext.getSchema()).add(
                variableName,
                TypeMappings.getDataFrameDataTypeFromItemType(expressionContext.getResultingType().getItemType())
            )
        );
        letClauseContext.setView(resultString);
        letClauseContext.setResultingQuery(null);
        letClauseContext.setResultingType(null);
        return letClauseContext;
    }
}
