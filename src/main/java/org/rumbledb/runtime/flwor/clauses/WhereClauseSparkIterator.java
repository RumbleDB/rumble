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
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.udfs.WhereClauseUDF;

import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WhereClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator expression;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple nextLocalTupleResult;

    public WhereClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator whereExpression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.expression = whereExpression;
        this.expression.getVariableDependencies();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child != null) {
            this.child.open(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent

            setNextLocalTupleResult();

        } else {
            throw new OurBadException("Invalid where clause.");
        }
    }

    @Override
    public void close() {
        super.close();
        if (this.child != null) {
            this.child.close();
            this.tupleContext = null;
        } else {
            throw new OurBadException("Invalid where clause.");
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child != null) {
            this.child.reset(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent

            setNextLocalTupleResult();

        } else {
            throw new OurBadException("Invalid where clause.");
        }
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

    private void setNextLocalTupleResult() {
        // for each incoming tuple, evaluate the expression to a boolean.
        // forward if true, drop if false

        FlworTuple inputTuple;
        while (this.child.hasNext()) {
            // tuple received from child, used for tuple creation
            inputTuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata()); // assign new
                                                                                                   // variables from new
                                                                                                   // tuple

            boolean effectiveBooleanValue = this.expression.getEffectiveBooleanValue(this.tupleContext);
            if (effectiveBooleanValue) {
                this.nextLocalTupleResult = inputTuple;
                this.hasNext = true;
                return;
            }
        }

        // execution reaches here when there are no more results
        this.child.close();
        this.hasNext = false;
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid where clause.");
        }

        if (this.expression.isRDDOrDataFrame()) {
            throw new JobWithinAJobException(
                    "A where clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        Dataset<Row> dataFrameIfJoinPossible = getDataFrameIfJoinPossible(context);
        if(dataFrameIfJoinPossible != null)
        {
            return dataFrameIfJoinPossible;
        }

        Dataset<Row> df = this.child.getDataFrame(context);
        StructType inputSchema = df.schema();

        Dataset<Row> nativeQueryResult = tryNativeQuery(
            df,
            this.expression,
            inputSchema,
            context
        );
        if (nativeQueryResult != null) {
            return nativeQueryResult;
        }

        // was not possible, we use let udf
        List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            this.expression.getVariableDependencies(),
            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null
        );

        df.sparkSession()
            .udf()
            .register(
                "whereClauseUDF",
                new WhereClauseUDF(this.expression, context, inputSchema, UDFcolumns),
                DataTypes.BooleanType
            );

        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);

        df.createOrReplaceTempView("input");
        df = df.sparkSession()
            .sql(
                String.format(
                    "select * from input where whereClauseUDF(%s) = 'true'",
                    UDFParameters
                )
            );
        return df;
    }

    private Dataset<Row> getDataFrameIfJoinPossible(DynamicContext context) {
        int height = this.getHeight();
        // System.out.println("[DEBUG] Height of the where clause: " + height);
        for(int i = 1; i < height; ++i)
        {
            if(this.canSetEvaluationDepthLimit(i))
            {
                // System.out.println("[DEBUG] Depth " + i + " possible.");
                RuntimeTupleIterator otherChild = this.getSubtreeBeyondLimit(i);
                // System.out.println(otherChild.toString());
            } else {
                // System.out.println("[DEBUG] Depth " + i + " impossible.");
            }
        }
        if (
            this.child instanceof ForClauseSparkIterator
        ) {
            ForClauseSparkIterator forChild = (ForClauseSparkIterator) this.child;
            if (forChild.getChildIterator() != null) {
                if (
                    (!forChild.getAssignmentIterator().getHighestExecutionMode().equals(ExecutionMode.LOCAL))
                        &&
                        forChild.getChildIterator().getHighestExecutionMode().equals(ExecutionMode.DATAFRAME)
                ) {
                    RuntimeIterator sequenceIterator = forChild.getAssignmentIterator();
                    Name forVariable = forChild.getVariableName();

                    if (
                        LetClauseSparkIterator.isExpressionIndependentFromInputTuple(sequenceIterator, this.child)
                            && forChild.getPositionalVariableName() == null
                            && !forChild.isAllowingEmpty()
                    ) {
                        System.err.println(
                            "[INFO] Rumble detected a join predicate in the where clause."
                        );

                        // Next we prepare the data frame on the expression side.
                        Dataset<Row> expressionDF;

                        Map<Name, DynamicContext.VariableDependency> startingClauseDependencies = new HashMap<>();
                        startingClauseDependencies.put(forVariable, DynamicContext.VariableDependency.FULL);
                        expressionDF = ForClauseSparkIterator.getDataFrameStartingClause(
                            sequenceIterator,
                            forVariable,
                            null,
                            false,
                            context,
                            startingClauseDependencies
                        );

                        return JoinClauseSparkIterator.joinInputTupleWithSequenceOnPredicate(
                            context,
                            forChild.getChildIterator().getDataFrame(context),
                            expressionDF,
                            this.outputTupleProjection,
                            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
                            Collections.singletonList(forVariable),
                            this.expression,
                            false,
                            forVariable,
                            forVariable,
                            getMetadata()
                        );
                    }
                }
            }
        }
        return null;
    }

    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(
                this.expression.getVariableDependencies()
        );
        for (Name var : this.child.getOutputTupleVariableNames()) {
            result.remove(var);
        }
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    public Set<Name> getOutputTupleVariableNames() {
        return new HashSet<>(this.child.getOutputTupleVariableNames());
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        this.expression.print(buffer, indent + 1);
    }

    public Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // copy over the projection needed by the parent clause.
        Map<Name, DynamicContext.VariableDependency> projection = new TreeMap<>(parentProjection);

        // add the variable dependencies needed by this for clause's expression.
        Map<Name, DynamicContext.VariableDependency> exprDependency = this.expression
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
        return projection;
    }

    /**
     * Try to generate the native query for the let clause and run it, if successful return the resulting dataframe,
     * otherwise it returns null
     *
     * @param dataFrame input dataframe for the query
     * @param iterator where filtering expression iterator
     * @param inputSchema input schema of the dataframe
     * @param context current dynamic context of the dataframe
     * @return resulting dataframe of the let clause if successful, null otherwise
     */
    public static Dataset<Row> tryNativeQuery(
            Dataset<Row> dataFrame,
            RuntimeIterator iterator,
            StructType inputSchema,
            DynamicContext context
    ) {
        NativeClauseContext letContext = new NativeClauseContext(FLWOR_CLAUSES.WHERE, inputSchema, context);
        NativeClauseContext nativeQuery = iterator.generateNativeQuery(letContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        System.out.println(
            "[INFO] Rumble was able to optimize a where clause to a native SQL query: "
                + nativeQuery.getResultingQuery()
        );
        dataFrame.createOrReplaceTempView("input");
        return dataFrame.sparkSession()
            .sql(
                String.format(
                    "select * from input where %s",
                    nativeQuery.getResultingQuery()
                )
            );
    }
}
