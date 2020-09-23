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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.udfs.HashUDF;
import org.rumbledb.runtime.flwor.udfs.LetClauseUDF;
import org.rumbledb.runtime.operational.ComparisonOperationIterator;
import org.rumbledb.runtime.postfix.PredicateIterator;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LetClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private Name variableName; // for efficient use in local iteration
    private RuntimeIterator assignmentIterator;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple nextLocalTupleResult;

    public LetClauseSparkIterator(
            RuntimeTupleIterator child,
            Name variableName,
            RuntimeIterator assignmentIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.assignmentIterator = assignmentIterator;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child == null) {
            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(null, this.currentDynamicContext);
        } else {
            this.child.open(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            setNextLocalTupleResult();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child == null) {
            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(null, this.currentDynamicContext);
        } else {
            this.child.reset(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            setNextLocalTupleResult();
        }
    }

    private void setNextLocalTupleResult() {
        // if starting clause: result is a single tuple -> no more tuples after the first next call
        if (this.child == null) {
            this.hasNext = false;
            return;
        }

        if (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata()); // assign new
                                                                                                   // variables from new
                                                                                                   // tuple

            this.nextLocalTupleResult = generateTupleFromExpressionWithContext(inputTuple, this.tupleContext);
            this.hasNext = true;
        } else {
            this.child.close();
            this.hasNext = false;
        }
    }

    private FlworTuple generateTupleFromExpressionWithContext(FlworTuple inputTuple, DynamicContext context) {
        FlworTuple resultTuple;
        if (inputTuple == null) {
            resultTuple = new FlworTuple();
        } else {
            resultTuple = new FlworTuple(inputTuple);
        }
        if (this.assignmentIterator.isDataFrame()) {
            Dataset<Row> df = this.assignmentIterator.getDataFrame(context);
            resultTuple.putValue(this.variableName, df);
        } else if (this.assignmentIterator.isRDD()) {
            JavaRDD<Item> itemRDD = this.assignmentIterator.getRDD(context);
            resultTuple.putValue(this.variableName, itemRDD);
        } else {
            List<Item> results = new ArrayList<>();
            this.assignmentIterator.open(context);
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
        if (this.child != null) {
            this.child.close();
        }
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child != null) {
            Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));

            if (!parentProjection.containsKey(this.variableName)) {
                return df;
            }

            if (this.assignmentIterator.isRDD()) {
                getDataFrameAsJoin(context, parentProjection, df);
            }

            df = bindLetVariableInDataFrame(
                df,
                this.variableName,
                this.assignmentIterator,
                context,
                parentProjection,
                false
            );

            return df;
        }
        throw new RuntimeException(
                "Unexpected program state reached. Initial let clauses are always locally executed."
        );
    }

    public Dataset<Row> getDataFrameAsJoin(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection,
            Dataset<Row> childDF
    ) {
        if (!(this.assignmentIterator instanceof PredicateIterator)) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        RuntimeIterator sequenceIterator = ((PredicateIterator) this.assignmentIterator).sequenceIterator();
        RuntimeIterator predicateIterator = ((PredicateIterator) this.assignmentIterator).predicateIterator();

        // If it does, we cannot handle it.
        if (!isExpressionIndependentFromInputTuple(sequenceIterator, this.child)) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        // Is this a join that we can optimize as an actual Spark join?
        boolean optimizableJoin = false;
        boolean contextItemToTheLeft = false;
        RuntimeIterator leftHandSideOfJoinEqualityCriterion = null;
        RuntimeIterator rightHandSideOfJoinEqualityCriterion = null;
        if (predicateIterator instanceof ComparisonOperationIterator) {
            ComparisonOperationIterator comparisonIterator = (ComparisonOperationIterator) predicateIterator;
            if (comparisonIterator.isValueEquality()) {
                leftHandSideOfJoinEqualityCriterion = comparisonIterator.getLeftIterator();
                rightHandSideOfJoinEqualityCriterion = comparisonIterator.getRightIterator();

                Set<Name> leftDependencies = new HashSet<>(
                        leftHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
                );
                Set<Name> rightDependencies = new HashSet<>(
                        rightHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
                );
                if (leftDependencies.size() == 1 && leftDependencies.contains(Name.CONTEXT_ITEM)) {
                    if (!rightDependencies.contains(Name.CONTEXT_ITEM)) {
                        optimizableJoin = true;
                        contextItemToTheLeft = true;
                    }
                }
                if (rightDependencies.size() == 1 && rightDependencies.contains(Name.CONTEXT_ITEM)) {
                    if (!leftDependencies.contains(Name.CONTEXT_ITEM)) {
                        optimizableJoin = true;
                        contextItemToTheLeft = false;
                    }
                }
            }
        }

        if (!optimizableJoin) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        Dataset<Row> inputDF = this.child.getDataFrame(context, getProjection(parentProjection));
        inputDF.show();
        inputDF.printSchema();

        // Since no variable dependency to the current FLWOR expression exists for the expression
        // evaluate the DataFrame with the parent context and calculate the cartesian product
        Dataset<Row> expressionDF;

        Map<Name, VariableDependency> predicateDependencies = predicateIterator.getVariableDependencies();
        if (parentProjection.containsKey(this.variableName)) {
            predicateDependencies.put(Name.CONTEXT_ITEM, parentProjection.get(this.variableName));
        }

        if (predicateDependencies.containsKey(Name.CONTEXT_POSITION)) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        } else {
            expressionDF = ForClauseSparkIterator.getDataFrameStartingClause(
                sequenceIterator,
                Name.CONTEXT_ITEM,
                null,
                false,
                context,
                predicateDependencies
            );
        }

        System.out.println("Optimizable join detected!");
        if (contextItemToTheLeft) {
            System.out.println("To the left.");
        } else {
            System.out.println("To the right.");
        }
        expressionDF.show();
        expressionDF.printSchema();

        if (contextItemToTheLeft) {
            expressionDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
                expressionDF,
                Name.createVariableInNoNamespace(SparkSessionManager.leftHashColumnName),
                leftHandSideOfJoinEqualityCriterion,
                context,
                null,
                true
            );
            inputDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
                inputDF,
                Name.createVariableInNoNamespace(SparkSessionManager.rightHashColumnName),
                rightHandSideOfJoinEqualityCriterion,
                context,
                null,
                true
            );

        } else {
            expressionDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
                expressionDF,
                Name.createVariableInNoNamespace(SparkSessionManager.leftHashColumnName),
                rightHandSideOfJoinEqualityCriterion,
                context,
                null,
                true
            );
            inputDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
                inputDF,
                Name.createVariableInNoNamespace(SparkSessionManager.rightHashColumnName),
                leftHandSideOfJoinEqualityCriterion,
                context,
                null,
                true
            );

        }

        inputDF.show();
        inputDF.printSchema();
        expressionDF.show();
        expressionDF.printSchema();

        expressionDF.createOrReplaceTempView("hashedExpressionResults");

        if (contextItemToTheLeft) {
            expressionDF = expressionDF.sparkSession()
                .sql(
                    String.format(
                        "SELECT `%s`, collect_list(`%s`) AS `%s` FROM hashedExpressionResults GROUP BY `%s`",
                        SparkSessionManager.leftHashColumnName,
                        Name.CONTEXT_ITEM.toString(),
                        Name.CONTEXT_ITEM.toString(),
                        SparkSessionManager.leftHashColumnName
                    )
                );
        } else {
            expressionDF = expressionDF.sparkSession()
                .sql(
                    String.format(
                        "SELECT `%s`, collect_list(`%s`) AS `%s` FROM hashedExpressionResults GROUP BY `%s`",
                        SparkSessionManager.rightHashColumnName,
                        Name.CONTEXT_ITEM.toString(),
                        Name.CONTEXT_ITEM.toString(),
                        SparkSessionManager.leftHashColumnName
                    )
                );
        }

        inputDF.show();
        inputDF.printSchema();
        expressionDF.show();
        expressionDF.printSchema();

        return null;
    }
    
    public static boolean isExpressionIndependentFromInputTuple(
            RuntimeIterator sequenceIterator,
            RuntimeTupleIterator tupleIterator
    ) {
        // Check that the expression does not depend functionally on the input tuples
        Set<Name> intersection = new HashSet<>(
                sequenceIterator.getVariableDependencies().keySet()
        );
        intersection.retainAll(tupleIterator.getVariablesBoundInCurrentFLWORExpression());
        return intersection.isEmpty();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.assignmentIterator.getVariableDependencies());
        if (this.child != null) {
            for (Name var : this.child.getVariablesBoundInCurrentFLWORExpression()) {
                result.remove(var);
            }
            result.putAll(this.child.getVariableDependencies());
        }
        return result;
    }

    public Set<Name> getVariablesBoundInCurrentFLWORExpression() {
        Set<Name> result = new HashSet<>();
        if (this.child != null) {
            result.addAll(this.child.getVariablesBoundInCurrentFLWORExpression());
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

    public Map<Name, DynamicContext.VariableDependency> getProjection(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null) {
            return null;
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
                    projection.put(variable, DynamicContext.VariableDependency.FULL);
                }
            } else {
                projection.put(variable, exprDependency.get(variable));
            }
        }
        return projection;
    }

    /**
     * Extends a DataFrame with a new column obtained from the evaluation of an expression for each tuple.
     * 
     * @param df the DataFrame to extend
     * @param newVariableName the name of the new column (variable)
     * @param newVariableExpression the expression to evaluate
     * @param context the context (in addition to each tuple) in which to evaluation the expression
     * @param dependencies the dependencies to project to (possibly null to keep everything).
     * @param hash whether or not to compute single-item hashes rather than the actual serialized sequences of items.
     * @return
     */
    public static Dataset<Row> bindLetVariableInDataFrame(
            Dataset<Row> df,
            Name newVariableName,
            RuntimeIterator newVariableExpression,
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            boolean hash
    ) {
        StructType inputSchema = df.schema();
        List<String> columnNames = Arrays.asList(inputSchema.fieldNames());

        int duplicateVariableIndex = columnNames.indexOf(newVariableName.toString());

        List<String> allColumns = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            duplicateVariableIndex,
            dependencies
        );
        Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            newVariableExpression.getVariableDependencies()
        );

        if (!hash) {
            df.sparkSession()
                .udf()
                .register(
                    "letClauseUDF",
                    new LetClauseUDF(newVariableExpression, context, UDFcolumnsByType),
                    DataTypes.BinaryType
                );
        } else {
            df.sparkSession()
                .udf()
                .register(
                    "hashUDF",
                    new HashUDF(newVariableExpression, context, UDFcolumnsByType),
                    DataTypes.LongType
                );
        }

        String selectSQL = FlworDataFrameUtils.getListOfSQLVariables(allColumns, true);
        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

        df.createOrReplaceTempView("input");
        if (!hash) {
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
            df = df.sparkSession()
                .sql(
                    String.format(
                        "select %s hashUDF(%s) as `%s` from input",
                        selectSQL,
                        UDFParameters,
                        newVariableName
                    )
                );
        }
        return df;
    }
}
