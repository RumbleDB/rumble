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
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;
import org.rumbledb.runtime.flwor.udfs.WhereClauseUDF;
import org.rumbledb.runtime.logics.AndOperationIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.ArrayRuntimeIterator;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class JoinClauseSparkIterator extends RuntimeTupleIterator {

    private static final long serialVersionUID = 1L;

    // Properties
    @SuppressWarnings("unused")
    private boolean isLeftOuterJoin;
    @SuppressWarnings("unused")
    private DataFrameContext dataFrameContext;

    // Computation state
    @SuppressWarnings("unused")
    private transient DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    @SuppressWarnings("unused")
    private transient long position;
    @SuppressWarnings("unused")
    private transient FlworTuple nextLocalTupleResult;
    @SuppressWarnings("unused")
    private transient FlworTuple inputTuple; // tuple received from child, used for tuple creation
    @SuppressWarnings("unused")
    private transient boolean isFirstItem;

    public JoinClauseSparkIterator(
            RuntimeTupleIterator leftChild,
            RuntimeTupleIterator rightChild,
            boolean isLeftOuterJoin,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(leftChild, executionMode, iteratorMetadata);
        this.isLeftOuterJoin = isLeftOuterJoin;
        this.dataFrameContext = new DataFrameContext();
    }

    /**
     * Joins two input tuples.
     * 
     * Warning: if the two tuples collide in their columns, unexpected things may happen for now.
     * 
     * @param context the dynamic context for the evaluation of the predicate expression.
     * @param leftInputTuple the left tuple.
     * @param rightInputTuple the right tuple.
     * @param outputTupleVariableDependencies the necessary and sufficient variable dependencies that the output tuple
     *        should contain.
     * @param variablesInLeftInputTuple a list of the variables in the left tuple.
     * @param variablesInRightInputTuple a list of the variables in the right tuple.
     * @param predicateIterator the predicate iterator.
     * @param isLeftOuterJoin true if it is a left outer join, false otherwise.
     * @param newRightSideVariableName the new name of the variable to rename the context item in the output (null if no
     *        rename).
     * @param metadata the metadata.
     * @return the joined tuple.
     */
    public static Dataset<Row> joinInputTupleWithSequenceOnPredicate(
            DynamicContext context,
            Dataset<Row> leftInputTuple,
            Dataset<Row> rightInputTuple,
            Map<Name, DynamicContext.VariableDependency> outputTupleVariableDependencies,
            List<Name> variablesInLeftInputTuple, // really needed?
            List<Name> variablesInRightInputTuple, // really needed?
            RuntimeIterator predicateIterator,
            boolean isLeftOuterJoin,
            Name newRightSideVariableName, // really needed?
            ExceptionMetadata metadata
    ) {
        Dataset<Row> result = tryNativeQueryStatically(
            context,
            leftInputTuple,
            rightInputTuple,
            outputTupleVariableDependencies,
            predicateIterator,
            isLeftOuterJoin,
            newRightSideVariableName,
            metadata
        );
        if (result != null) {
            return result;
        }
        String leftInputDFTableName = "leftInputTuples";
        String rightInputDFTableName = "rightInputTuples";

        // TODO project away from the left all variables from the right

        // Is this a join that we can optimize as an actual Spark join?
        List<RuntimeIterator> leftTupleSideEqualityCriteria = new ArrayList<>();
        List<RuntimeIterator> rightTupleSideEqualityCriteria = new ArrayList<>();

        boolean optimizableJoin = extractEqualityComparisonsForHashing(
            predicateIterator,
            leftTupleSideEqualityCriteria,
            rightTupleSideEqualityCriteria,
            variablesInLeftInputTuple,
            variablesInRightInputTuple
        );

        if (isLeftOuterJoin) {
            optimizableJoin = false;
        }

        // for (RuntimeIterator r : rightHandSideEqualityCriteria) {
        // StringBuffer sb = new StringBuffer();
        // r.print(sb, 2);
        // System.out.println(sb.toString());
        // }

        Map<Name, VariableDependency> predicateDependencies = predicateIterator.getVariableDependencies();
        if (
            newRightSideVariableName != null
                && outputTupleVariableDependencies.containsKey(newRightSideVariableName)
        ) {
            predicateDependencies.put(Name.CONTEXT_ITEM, outputTupleVariableDependencies.get(newRightSideVariableName));
        }

        // System.out.println("Old right side variable name : " + oldRightSideVariableName);
        // System.out.println("New right side variable name: " + newRightSideVariableName);
        // for (Name n : predicateDependencies.keySet()) {
        // System.out.println(n.toString() + " -> " + predicateDependencies.get(n));
        // }



        // If the join criterion uses the context count, then we need to add it to the expression side (it is a
        // constant).


        // for (Name n : variablesInRightInputTuple) {
        // System.out.println(n.toString() + " in expression side tuple.");
        // }

        if (optimizableJoin) {
            System.err.println(
                "[INFO] Rumble detected that it can optimize your query and make it faster with an equi-join."
            );
        }



        // Now we prepare the iterators for the two sides of the equality criterion.
        RuntimeIterator rightHandSideEqualityCriterion;
        RuntimeIterator leftHandSideEqualityCriterion;

        if (rightTupleSideEqualityCriteria.size() == 1) {
            rightHandSideEqualityCriterion = rightTupleSideEqualityCriteria.get(0);
        } else {
            rightHandSideEqualityCriterion = new ArrayRuntimeIterator(
                    new CommaExpressionIterator(
                            rightTupleSideEqualityCriteria,
                            ExecutionMode.LOCAL,
                            metadata
                    ),
                    ExecutionMode.LOCAL,
                    metadata
            );
        }
        if (leftTupleSideEqualityCriteria.size() == 1) {
            leftHandSideEqualityCriterion = leftTupleSideEqualityCriteria.get(0);
        } else {
            leftHandSideEqualityCriterion = new ArrayRuntimeIterator(
                    new CommaExpressionIterator(
                            leftTupleSideEqualityCriteria,
                            ExecutionMode.LOCAL,
                            metadata
                    ),
                    ExecutionMode.LOCAL,
                    metadata
            );
        }
        // leftInputTuple.show();
        // rightInputTuple.show();

        // And we extend the expression and input tuple views with the hashes.
        if (optimizableJoin) {
            rightInputTuple = LetClauseSparkIterator.bindLetVariableInDataFrame(
                rightInputTuple,
                Name.createVariableInNoNamespace(SparkSessionManager.rightHandSideHashColumnName),
                null,
                rightHandSideEqualityCriterion,
                context,
                variablesInRightInputTuple,
                null,
                true
            );
            // rightInputTuple.show();
            leftInputTuple = LetClauseSparkIterator.bindLetVariableInDataFrame(
                leftInputTuple,
                Name.createVariableInNoNamespace(SparkSessionManager.leftHandSideHashColumnName),
                null,
                leftHandSideEqualityCriterion,
                context,
                variablesInLeftInputTuple,
                null,
                true
            );
            // leftInputTuple.show();
        }



        // Now we prepare the two views that we want to compute the Cartesian product of.
        leftInputDFTableName = FlworDataFrameUtils.createTempView(leftInputTuple);
        rightInputDFTableName = FlworDataFrameUtils.createTempView(rightInputTuple);

        StructType leftSchema = leftInputTuple.schema();
        StructType rightSchema = rightInputTuple.schema();
        StructType jointSchema = FlworDataFrameUtils.schemaUnion(leftSchema, rightSchema);

        // We gather the columns to select from the previous clause.
        // We need to project away the clause's variables from the previous clause.
        // One variable gets renamed. We need to remove it from the projection.
        List<FlworDataFrameColumn> columnsToSelect = FlworDataFrameUtils.getColumns(
            jointSchema,
            outputTupleVariableDependencies,
            null,
            (newRightSideVariableName != null)
                ? Collections.singletonList(newRightSideVariableName)
                : Collections.emptyList()
        );
        String projectionVariables = FlworDataFrameUtils.getSQLColumnProjection(
            columnsToSelect,
            newRightSideVariableName != null
        );
        if (newRightSideVariableName != null) {
            projectionVariables += String.format(
                " `%s`.`%s` AS `%s`",
                rightInputDFTableName,
                Name.CONTEXT_ITEM.getLocalName(),
                newRightSideVariableName
            );
        }

        // We need to prepare the parameters fed into the predicate UDF.
        List<Name> variablesInJointTuple = new ArrayList<>();
        variablesInJointTuple.addAll(variablesInLeftInputTuple);
        variablesInJointTuple.addAll(variablesInRightInputTuple);
        List<String> joinCriterionUDFcolumns = FlworDataFrameUtils.getColumnNames(
            jointSchema,
            predicateIterator.getVariableDependencies(),
            variablesInJointTuple,
            null
        );

        // Now we need to register or join predicate as a UDF.
        leftInputTuple.sparkSession()
            .udf()
            .register(
                "joinUDF",
                new WhereClauseUDF(predicateIterator, context, jointSchema, joinCriterionUDFcolumns),
                DataTypes.BooleanType
            );

        String UDFParameters = FlworDataFrameUtils.getUDFParameters(joinCriterionUDFcolumns);

        // If we allow empty, we need a LEFT OUTER JOIN.
        if (isLeftOuterJoin) {
            Dataset<Row> resultDF = leftInputTuple.sparkSession()
                .sql(
                    String.format(
                        "SELECT %s FROM %s LEFT OUTER JOIN %s ON joinUDF(%s) = 'true'",
                        projectionVariables,
                        leftInputDFTableName,
                        rightInputDFTableName,
                        UDFParameters
                    )
                );
            return resultDF;
        }

        if (optimizableJoin) {
            // Otherwise, it's a regular join.
            Dataset<Row> resultDF = leftInputTuple.sparkSession()
                .sql(
                    String.format(
                        "SELECT %s FROM %s JOIN %s ON `%s` = `%s` WHERE joinUDF(%s) = 'true'",
                        projectionVariables,
                        leftInputDFTableName,
                        rightInputDFTableName,
                        SparkSessionManager.rightHandSideHashColumnName,
                        SparkSessionManager.leftHandSideHashColumnName,
                        UDFParameters
                    )
                );
            return resultDF;
        }
        // Otherwise, it's a regular join.
        Dataset<Row> resultDF = leftInputTuple.sparkSession()
            .sql(
                String.format(
                    "SELECT %s FROM %s JOIN %s ON joinUDF(%s) = 'true'",
                    projectionVariables,
                    leftInputDFTableName,
                    rightInputDFTableName,
                    UDFParameters
                )
            );
        return resultDF;
    }

    private static boolean extractEqualityComparisonsForHashing(
            RuntimeIterator predicateIterator,
            List<RuntimeIterator> leftTupleSideEqualityCriteria,
            List<RuntimeIterator> rightTupleSideEqualityCriteria,
            List<Name> leftTupleSideVariableNames,
            List<Name> rightTupleSideVariableNames
    ) {
        boolean optimizableJoin = false;
        Stack<RuntimeIterator> candidateIterators = new Stack<>();
        candidateIterators.push(predicateIterator);
        while (!candidateIterators.isEmpty()) {
            RuntimeIterator iterator = candidateIterators.pop();
            if (iterator instanceof AndOperationIterator) {
                AndOperationIterator andIterator = ((AndOperationIterator) iterator);
                candidateIterators.push(andIterator.getLeftIterator());
                candidateIterators.push(andIterator.getRightIterator());
            } else if (iterator instanceof ComparisonIterator) {
                ComparisonIterator comparisonIterator = (ComparisonIterator) iterator;
                if (comparisonIterator.isValueEquality()) {
                    RuntimeIterator lhs = comparisonIterator.getLeftIterator();
                    RuntimeIterator rhs = comparisonIterator.getRightIterator();

                    Set<Name> leftComparisonDependencies = new HashSet<>(
                            lhs.getVariableDependencies().keySet()
                    );
                    Set<Name> rightComparisonDependencies = new HashSet<>(
                            rhs.getVariableDependencies().keySet()
                    );
                    // TODO it would be nice to be more generic and also allow dependencies on the
                    // dynamic context on any side.
                    if (
                        leftTupleSideVariableNames.containsAll(leftComparisonDependencies)
                            && rightTupleSideVariableNames.containsAll(rightComparisonDependencies)
                    ) {
                        optimizableJoin = true;
                        leftTupleSideEqualityCriteria.add(lhs);
                        rightTupleSideEqualityCriteria.add(rhs);
                    }
                    if (
                        leftTupleSideVariableNames.containsAll(rightComparisonDependencies)
                            && rightTupleSideVariableNames.containsAll(leftComparisonDependencies)
                    ) {
                        optimizableJoin = true;
                        leftTupleSideEqualityCriteria.add(rhs);
                        rightTupleSideEqualityCriteria.add(lhs);
                    }
                }
            }
        }
        return optimizableJoin;
    }

    @Override
    public FlworTuple next() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Map<Name, VariableDependency> getInputTupleVariableDependencies(
            Map<Name, VariableDependency> parentProjection
    ) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean containsClause(FLWOR_CLAUSES kind) {
        return false;
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
    private static Dataset<Row> tryNativeQueryStatically(
            DynamicContext context,
            Dataset<Row> leftInputTuple,
            Dataset<Row> rightInputTuple,
            Map<Name, DynamicContext.VariableDependency> outputTupleVariableDependencies,
            RuntimeIterator predicateIterator,
            boolean isLeftOuterJoin,
            Name newRightSideVariableName, // really needed?
            ExceptionMetadata metadata
    ) {
        if (isLeftOuterJoin) {
            return null;
        }
        if (newRightSideVariableName != null) {
            return null;
        }
        StructType leftSchema = leftInputTuple.schema();
        StructType rightSchema = rightInputTuple.schema();
        StructType unionSchema = FlworDataFrameUtils.schemaUnion(leftSchema, rightSchema);
        NativeClauseContext nativeContext = new NativeClauseContext(FLWOR_CLAUSES.WHERE, unionSchema, context);
        NativeClauseContext nativeQuery = predicateIterator.generateNativeQuery(nativeContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        System.err.println(
            "[INFO] Rumble was able to optimize a join to a native SQL query."
        );
        String left = FlworDataFrameUtils.createTempView(leftInputTuple);
        String right = FlworDataFrameUtils.createTempView(rightInputTuple);
        List<FlworDataFrameColumn> columnsToSelect = FlworDataFrameUtils.getColumns(
            unionSchema,
            outputTupleVariableDependencies,
            null,
            Collections.emptyList()
        );
        String projectionVariables = FlworDataFrameUtils.getSQLColumnProjection(
            columnsToSelect,
            newRightSideVariableName != null
        );
        return leftInputTuple.sparkSession()
            .sql(
                String.format(
                    "SELECT %s FROM %s JOIN %s ON %s",
                    projectionVariables,
                    left,
                    right,
                    nativeQuery.getResultingQuery()
                )
            );
    }

    /**
     * Says whether this expression evaluation triggers a Spark job.
     *
     * @param visitorConfig the configuration of the visitor.
     * @return true if the execution triggers a Spark, false otherwise, null if undetermined yet.
     */
    @Override
    public boolean isSparkJobNeeded() {
        if (this.child.isSparkJobNeeded()) {
            return true;
        }
        switch (this.highestExecutionMode) {
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

}
