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
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrame;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.udfs.WhereClauseUDF;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.*;

public class WhereClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator expression;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple nextLocalTupleResult;

    public WhereClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator whereExpression,
            RuntimeStaticContext staticContext
    ) {
        super(child, staticContext);
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
    public FlworDataFrame getDataFrame(
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

        FlworDataFrame dataFrameIfLimit = getDataFrameIfLimit(context);
        if (dataFrameIfLimit != null) {
            return dataFrameIfLimit;
        }

        FlworDataFrame dataFrameIfJoinPossible = getDataFrameIfJoinPossible(context);
        if (dataFrameIfJoinPossible != null) {
            return dataFrameIfJoinPossible;
        }

        FlworDataFrame df = this.child.getDataFrame(context);
        // StructType inputSchema = df.schema();

        FlworDataFrame nativeQueryResult = null;
        if (getConfiguration().nativeExecution()) {
            nativeQueryResult = tryNativeQuery(
                df,
                this.expression,
                context,
                this.getMetadata()
            );
        }
        if (nativeQueryResult != null) {
            return nativeQueryResult;
        }

        // was not possible, we use let udf
        List<FlworDataFrameColumn> UDFcolumns = df.getColumns(
            this.expression.getVariableDependencies(),
            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null
        );

        df.getUDFRegistration()
            .register(
                "whereClauseUDF",
                new WhereClauseUDF(this.expression, context, UDFcolumns),
                DataTypes.BooleanType
            );

        String UDFParameters = FlworDataFrameUtils.getUDFParametersFromColumns(UDFcolumns);

        String input = df.createTempView();
        return df.sql(
            String.format(
                "select * from %s where whereClauseUDF(%s) = 'true'",
                input,
                UDFParameters
            )
        );
    }

    private FlworDataFrame getDataFrameIfLimit(DynamicContext context) {
        if (!(this.child instanceof CountClauseSparkIterator)) {
            return null;
        }
        CountClauseSparkIterator countClauseIterator = (CountClauseSparkIterator) this.child;
        Name countVariable = countClauseIterator.getVariableName();
        if (!(this.expression instanceof ComparisonIterator)) {
            return null;
        }
        ComparisonIterator comparisonIterator = (ComparisonIterator) this.expression;
        if (
            !comparisonIterator.getComparisonOperator().equals(ComparisonExpression.ComparisonOperator.VC_LE)
                &&
                !comparisonIterator.getComparisonOperator().equals(ComparisonExpression.ComparisonOperator.GC_LE)
        ) {
            return null;
        }
        RuntimeIterator left = comparisonIterator.getLeftIterator();
        if (!(left instanceof VariableReferenceIterator)) {
            return null;
        }
        VariableReferenceIterator varRef = (VariableReferenceIterator) left;
        if (!varRef.getVariableName().equals(countVariable)) {
            return null;
        }
        RuntimeIterator right = comparisonIterator.getRightIterator();
        Set<Name> usedVariables = right.getVariableDependencies().keySet();
        List<Item> items = new ArrayList<>();
        Set<Name> tuples = countClauseIterator.getOutputTupleVariableNames();
        usedVariables.retainAll(tuples);
        if (!usedVariables.isEmpty()) {
            return null;
        }
        right.materializeNFirstItems(context, items, 2);
        if (items.size() != 1) {
            return null;
        }
        Item item = items.get(0);
        if (!item.isInteger()) {
            return null;
        }
        LogManager.getLogger("WhereClauseSparkIterator")
            .info(
                "Rumble detected a LIMIT in a count and where clause."
            );
        FlworDataFrame df = this.child.getChildIterator().getDataFrame(context);
        String input = df.createTempView();
        return df.sql(String.format("SELECT * FROM %s LIMIT %s", input, item.getStringValue()));
    }

    private FlworDataFrame getDataFrameIfJoinPossible(DynamicContext context) {
        if (this.evaluationDepthLimit >= 0) {
            return null;
        }

        int height = this.getHeight();
        int limit = -1;
        // System.err.println("[DEBUG] Height of the where clause: " + height);
        for (int i = 1; i < height; ++i) {
            if (!this.canSetEvaluationDepthLimit(i)) {
                // System.err.println("[DEBUG] Depth " + i + " impossible (not a starting let or for clause).");
                continue;
            }
            this.setEvaluationDepthLimit(i);
            if (this.containsClause(FLWOR_CLAUSES.GROUP_BY)) {
                // System.err.println("[DEBUG] Depth " + i + " does not work (because of a group by clause).");
                continue;
            }
            if (this.containsClause(FLWOR_CLAUSES.COUNT)) {
                // System.err.println("[DEBUG] Depth " + i + " does not work (because of a count clause).");
                continue;
            }
            RuntimeTupleIterator otherChild = this.getSubtreeBeyondLimit(i);
            if (!otherChild.getHighestExecutionMode().equals(ExecutionMode.DATAFRAME)) {
                // System.err.println(
                // "[DEBUG] Depth " + i + " does not work (because the left does not have a DataFrame execution)."
                // );
                continue;
            }
            Set<Name> leftNames = otherChild.getOutputTupleVariableNames();
            Map<Name, VariableDependency> rightDependencies = this.child.getDynamicContextVariableDependencies();
            Set<Name> rightNames = rightDependencies.keySet();
            rightNames.retainAll(leftNames);
            if (!rightNames.isEmpty()) {
                // System.err.println(
                // "[DEBUG] Depth "
                // + i
                // + " does not work (because of variable dependencies: "
                // + Arrays.toString(rightNames.toArray())
                // );
                continue;
            }
            rightNames = this.child.getOutputTupleVariableNames();
            rightNames.retainAll(leftNames);
            if (!rightNames.isEmpty()) {
                // System.err.println(
                // "[DEBUG] Depth "
                // + i
                // + " does not work (because of variable collisions: "
                // + Arrays.toString(rightNames.toArray())
                // );
                continue;
            }
            // System.err.println("[DEBUG] Depth " + i + " possible.");
            // System.err.println(otherChild.toString());
            limit = i;
        }
        this.setEvaluationDepthLimit(-1);
        if (limit == -1) {
            return null;
        }

        LogManager.getLogger("WhereClauseSparkIterator")
            .info("Rumble detected a join predicate in the where clause (limit=" + limit + " of " + height + ").");

        try {
            FlworDataFrame leftTuples = getSubtreeBeyondLimit(limit).getDataFrame(context);
            Set<Name> leftVariables = getSubtreeBeyondLimit(limit).getOutputTupleVariableNames();
            this.setEvaluationDepthLimit(limit);
            Map<Name, VariableDependency> temporaryInputProjection = new HashMap<>(this.inputTupleProjection);
            for (Name key : leftVariables) {
                temporaryInputProjection.remove(key);
            }
            this.child.setInputAndOutputTupleVariableDependencies(temporaryInputProjection);
            FlworDataFrame rightTuples = this.child.getDataFrame(context);
            this.child.setInputAndOutputTupleVariableDependencies(this.inputTupleProjection);

            Set<Name> rightVariables = this.child.getOutputTupleVariableNames();
            this.setEvaluationDepthLimit(-1);

            FlworDataFrame result = JoinClauseSparkIterator.joinInputTupleWithSequenceOnPredicate(
                context,
                leftTuples.getDataFrame(),
                rightTuples.getDataFrame(),
                this.outputTupleProjection,
                new ArrayList<Name>(leftVariables),
                new ArrayList<Name>(rightVariables),
                this.expression,
                false,
                null,
                getMetadata(),
                getConfiguration()
            );
            return result;
        } catch (Exception e) {
            LogManager.getLogger("WhereClauseSparkIterator")
                .warn(
                    "Join failed. Falling back to regular execution (nevertheless, please let us know!)."
                );

            this.setEvaluationDepthLimit(-1);
            this.child.setInputAndOutputTupleVariableDependencies(this.inputTupleProjection);
            return null;
        }

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
     * @param context current dynamic context of the dataframe
     * @param metadata
     * @return resulting dataframe of the let clause if successful, null otherwise
     */
    public static FlworDataFrame tryNativeQuery(
            FlworDataFrame dataFrame,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        StructType inputSchema = dataFrame.getDataFrame().schema();
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(inputSchema);
        String input = FlworDataFrameUtils.createTempView(dataFrame.getDataFrame());
        NativeClauseContext letContext = new NativeClauseContext(
                FLWOR_CLAUSES.WHERE,
                inputSchema,
                context
        );
        letContext.setView(input);
        NativeClauseContext nativeQuery = iterator.generateNativeQuery(letContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        if (!nativeQuery.getResultingType().getItemType().equals(BuiltinTypesCatalogue.booleanItem)) {
            throw new InvalidArgumentTypeException(
                    "Effective boolean value not defined for items of type "
                        +
                        nativeQuery.getResultingType().getItemType().toString(),
                    iterator.getMetadata()
            );
        }
        LogManager.getLogger("WhereClauseSparkIterator")
            .info(
                "Rumble was able to optimize a where clause to a native SQL query: "
                    + String.format(
                        "select %s from (%s) where %s",
                        FlworDataFrameUtils.getSQLColumnProjection(allColumns, false),
                        nativeQuery.getView(),
                        nativeQuery.getResultingQuery()
                    )
            );
        return new FlworDataFrame(
                dataFrame.getDataFrame()
                    .sparkSession()
                    .sql(
                        String.format(
                            "select %s from (%s) where %s",
                            FlworDataFrameUtils.getSQLColumnProjection(allColumns, false),
                            nativeQuery.getView(),
                            nativeQuery.getResultingQuery()
                        )
                    )
        );
    }

    public boolean containsClause(FLWOR_CLAUSES kind) {
        if (kind == FLWOR_CLAUSES.WHERE) {
            return true;
        }
        if (this.child == null) {
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
        if (this.expression.isSparkJobNeeded()) {
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
        NativeClauseContext childContext = this.child.generateNativeQuery(nativeClauseContext);
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        childContext.setClauseType(FLWOR_CLAUSES.WHERE);
        NativeClauseContext expressionContext = this.expression.generateNativeQuery(childContext);
        if (expressionContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!expressionContext.getResultingType().getItemType().equals(BuiltinTypesCatalogue.booleanItem)) {
            throw new InvalidArgumentTypeException(
                    "Effective boolean value not defined for items of type "
                        +
                        expressionContext.getResultingType().getItemType().toString(),
                    getMetadata()
            );
        }
        String conditionalColumnName = childContext.addVariable().toString();

        String resultString = String.format(
            "select *, %s as `%s` from (%s)",
            expressionContext.getResultingQuery(),
            conditionalColumnName,
            expressionContext.getView()
        );
        if (nativeClauseContext.getPositionalVariableName() != null && !childContext.isGrouped()) {
            resultString = String.format(
                "select * from (%s) where `%s` or (`%s` = 1)",
                resultString,
                conditionalColumnName,
                nativeClauseContext.getPositionalVariableName().getLocalName()
            );
        }
        childContext.setSchema(
            ((StructType) childContext.getSchema()).add(
                conditionalColumnName,
                DataTypes.BooleanType
            )
        );
        childContext.addConditionalColumn(conditionalColumnName);
        childContext.setView(resultString);
        return new NativeClauseContext(childContext, null, null);
    }
}
