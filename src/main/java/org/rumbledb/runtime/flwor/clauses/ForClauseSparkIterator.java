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
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.closures.ItemsToBinaryColumn;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;
import org.rumbledb.runtime.flwor.udfs.ForClauseUDF;
import org.rumbledb.runtime.flwor.udfs.IntegerSerializeUDF;
import org.rumbledb.runtime.flwor.udfs.WhereClauseUDF;
import org.rumbledb.runtime.operational.AndOperationIterator;
import org.rumbledb.runtime.operational.ComparisonOperationIterator;
import org.rumbledb.runtime.postfix.PredicateIterator;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ForClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;

    // Properties
    private Name variableName; // for efficient use in local iteration
    private Name positionalVariableName; // for efficient use in local iteration
    private RuntimeIterator assignmentIterator;
    private boolean allowingEmpty;
    private Map<Name, DynamicContext.VariableDependency> dependencies;
    private DataFrameContext dataFrameContext;

    // Computation state
    private transient DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private transient long position;
    private transient FlworTuple nextLocalTupleResult;
    private transient FlworTuple inputTuple; // tuple received from child, used for tuple creation
    private transient boolean isFirstItem;

    public ForClauseSparkIterator(
            RuntimeTupleIterator child,
            Name variableName,
            Name positionalVariableName,
            boolean allowingEmpty,
            RuntimeIterator assignmentIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.positionalVariableName = positionalVariableName;
        this.assignmentIterator = assignmentIterator;
        this.allowingEmpty = allowingEmpty;
        this.dependencies = this.assignmentIterator.getVariableDependencies();
        this.dataFrameContext = new DataFrameContext();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        if (this.child != null) { // if it's not a start clause
            this.child.open(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            this.position = 1;
            this.isFirstItem = true;
            setNextLocalTupleResult();
        } else { // if it's a start clause, get results using only the assignmentIterator
            this.assignmentIterator.open(this.currentDynamicContext);
            this.position = 1;
            this.isFirstItem = true;
            setResultFromExpression();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);

        if (this.child != null) { // if it's not a start clause
            this.child.reset(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            this.position = 1;
            this.isFirstItem = true;
            setNextLocalTupleResult();
        } else { // if it's a start clause, get results using only the assignmentIterator
            this.assignmentIterator.reset(this.currentDynamicContext);
            this.position = 1;
            this.isFirstItem = true;
            setResultFromExpression();
        }
    }

    @Override
    public FlworTuple next() {
        if (this.hasNext) {
            FlworTuple result = this.nextLocalTupleResult; // save the result to be returned
            // calculate and store the next result
            if (this.child == null) { // if it's the initial for clause, call the correct function
                setResultFromExpression();
            } else {
                setNextLocalTupleResult();
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        if (this.assignmentIterator.isOpen()) {
            if (setResultFromExpression()) {
                return;
            }
        }

        while (this.child.hasNext()) {
            this.inputTuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(this.inputTuple, getMetadata());
            this.assignmentIterator.open(this.tupleContext);
            this.position = 1;
            this.isFirstItem = true;
            if (setResultFromExpression()) {
                return;
            }
        }

        // execution reaches here when there are no more results
        this.hasNext = false;
        this.child.close();
    }

    /**
     * assignmentIterator has to be open prior to call.
     *
     * @return true if nextLocalTupleResult is set and hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (this.assignmentIterator.hasNext()) { // if expression returns a value, set it as next

            // Set the for item
            if (this.child == null) { // if initial for clause
                this.nextLocalTupleResult = new FlworTuple();
            } else {
                this.nextLocalTupleResult = new FlworTuple(this.inputTuple);
            }
            this.nextLocalTupleResult.putValue(this.variableName, this.assignmentIterator.next());

            // Set the position item (if any)
            if (this.positionalVariableName != null) {
                this.nextLocalTupleResult.putValue(
                    this.positionalVariableName,
                    ItemFactory.getInstance().createLongItem(this.position)
                );
                ++this.position;
            }

            this.hasNext = true;
            this.isFirstItem = false;
            return true;
        }

        this.assignmentIterator.close();

        // If an item was already output by this expression and there is no more, we are done.
        if (!this.isFirstItem || !this.allowingEmpty) {
            this.hasNext = false;
            return false;
        }

        // If nothing was output yet by this expression but we allow empty, we need to bind
        // the empty sequence.
        if (this.child == null) { // if initial for clause
            this.nextLocalTupleResult = new FlworTuple();
        } else {
            this.nextLocalTupleResult = new FlworTuple(this.inputTuple);
        }
        this.nextLocalTupleResult.putValue(this.variableName, Collections.emptyList());
        // Set the position item (if any)
        if (this.positionalVariableName != null) {
            this.nextLocalTupleResult.putValue(
                this.positionalVariableName,
                ItemFactory.getInstance().createLongItem(0)
            );
        }
        this.hasNext = true;
        this.isFirstItem = false;
        return true;
    }

    @Override
    public void close() {
        this.isOpen = false;
        if (this.child != null) {
            this.child.close();
        }
        this.assignmentIterator.close();
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // if it's a starting clause
        if (this.child == null) {
            return getDataFrameStartingClause(context, parentProjection);
        }

        if (this.child.isDataFrame()) {
            if (this.assignmentIterator.isRDD()) {
                return getDataFrameFromCartesianProduct(context, parentProjection);
            }

            return getDataFrameInParallel(context, parentProjection);
        }

        // if child is locally evaluated
        // assignmentIterator is definitely an RDD if execution flows here
        return getDataFrameFromUnion(context, parentProjection);
    }

    /**
     * 
     * Non-starting clause, the child clause (above in the syntax) is parallelizable, the expression as well, and the
     * expression does not depend on the input tuple.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromCartesianProduct(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // If the expression depends on this input tuple, we might still recognize an join.
        if (!LetClauseSparkIterator.isExpressionIndependentFromInputTuple(this.assignmentIterator, this.child)) {
            return getDataFrameFromJoin(context, parentProjection);
        }

        // Since no variable dependency to the current FLWOR expression exists for the expression
        // evaluate the DataFrame with the parent context and calculate the cartesian product
        Dataset<Row> expressionDF;
        expressionDF = getDataFrameStartingClause(context, parentProjection);

        Dataset<Row> inputDF = this.child.getDataFrame(context, getProjection(parentProjection));

        // Now we prepare the two views that we want to compute the Cartesian product of.
        String inputDFTableName = "input";
        String expressionDFTableName = "expression";
        inputDF.createOrReplaceTempView(inputDFTableName);
        expressionDF.createOrReplaceTempView(expressionDFTableName);

        // We gather the columns to select from the previous clause.
        // We need to project away the clause's variables from the previous clause.
        StructType inputSchema = inputDF.schema();
        int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames())
            .indexOf(this.variableName.toString());
        int duplicatePositionalVariableIndex = -1;
        if (this.positionalVariableName != null) {
            duplicatePositionalVariableIndex = Arrays.asList(inputSchema.fieldNames())
                .indexOf(this.positionalVariableName.toString());
        }
        List<String> columnsToSelect = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            duplicateVariableIndex,
            duplicatePositionalVariableIndex,
            parentProjection
        );

        // We add the one or two current clause variables to our projection.
        if (duplicateVariableIndex == -1) {
            columnsToSelect.add(this.variableName.toString());
        } else {
            columnsToSelect.add(expressionDFTableName + "`.`" + this.variableName);
        }
        if (this.positionalVariableName != null) {
            if (duplicatePositionalVariableIndex == -1) {
                columnsToSelect.add(this.positionalVariableName.toString());
            } else {
                columnsToSelect.add(expressionDFTableName + "`.`" + this.positionalVariableName);
            }
        }
        String projectionVariables = FlworDataFrameUtils.getListOfSQLVariables(columnsToSelect, false);

        // And return the Cartesian product with the desired projection.
        return inputDF.sparkSession()
            .sql(
                String.format(
                    "select %s from %s, %s",
                    projectionVariables,
                    inputDFTableName,
                    expressionDFTableName
                )
            );
    }

    /**
     * 
     * Non-starting clause, the child clause (above in the syntax) is parallelizable, the expression as well, and the
     * expression is a predicate whose lhs does not depend on the input tuple.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromJoin(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (!(this.assignmentIterator instanceof PredicateIterator)) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. A piece of advice: if you use a predicate expression in your for clause, like for $"
                        + this.variableName.toString()
                        + " in json-file(\"...\")[$$.id eq $other-flwor-variable.id], Rumble may be able to detect a join.",
                    getMetadata()
            );
        }
        RuntimeIterator sequenceIterator = ((PredicateIterator) this.assignmentIterator).sequenceIterator();
        RuntimeIterator predicateIterator = ((PredicateIterator) this.assignmentIterator).predicateIterator();

        // If the left hand side depends on the input tuple, we do not how to handle it.
        if (!LetClauseSparkIterator.isExpressionIndependentFromInputTuple(sequenceIterator, this.child)) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion. In our efforts to detect a join, we did recognize a predicate expression in the for clause, but the left-hand-side of the predicate expression depends on the previous variables of this FLWOR expression. You can fix this by making sure it does not.",
                    getMetadata()
            );
        }

        return joinInputTupleWithSequenceOnPredicate(
            context,
            this.child.getDataFrame(context, getProjection(parentProjection)),
            parentProjection,
            sequenceIterator,
            predicateIterator,
            this.allowingEmpty,
            this.variableName,
            this.positionalVariableName,
            Name.CONTEXT_ITEM,
            getMetadata()
        );
    }

    private static Dataset<Row> joinInputTupleWithSequenceOnPredicate(
            DynamicContext context,
            Dataset<Row> inputTuples,
            Map<Name, DynamicContext.VariableDependency> parentProjection,
            RuntimeIterator sequenceIterator,
            RuntimeIterator predicateIterator,
            boolean allowingEmpty,
            Name variableName,
            Name positionalVariableName,
            Name sequenceVariableName,
            ExceptionMetadata metadata
    ) {
        // Is this a join that we can optimize as an actual Spark join?
        boolean optimizableJoin = false;
        boolean contextItemToTheLeft = false;
        RuntimeIterator leftHandSideOfJoinEqualityCriterion = null;
        RuntimeIterator rightHandSideOfJoinEqualityCriterion = null;
        RuntimeIterator iterator = predicateIterator;
        while (iterator instanceof AndOperationIterator) {
            iterator = ((AndOperationIterator) iterator).getLeftIterator();
        }
        if (iterator instanceof ComparisonOperationIterator) {
            ComparisonOperationIterator comparisonIterator = (ComparisonOperationIterator) iterator;
            if (comparisonIterator.isValueEquality()) {
                leftHandSideOfJoinEqualityCriterion = comparisonIterator.getLeftIterator();
                rightHandSideOfJoinEqualityCriterion = comparisonIterator.getRightIterator();

                Set<Name> leftDependencies = new HashSet<>(
                        leftHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
                );
                Set<Name> rightDependencies = new HashSet<>(
                        rightHandSideOfJoinEqualityCriterion.getVariableDependencies().keySet()
                );
                if (leftDependencies.size() == 1 && leftDependencies.contains(sequenceVariableName)) {
                    if (!rightDependencies.contains(sequenceVariableName)) {
                        optimizableJoin = true;
                        contextItemToTheLeft = true;
                    }
                }
                if (rightDependencies.size() == 1 && rightDependencies.contains(sequenceVariableName)) {
                    if (!leftDependencies.contains(sequenceVariableName)) {
                        optimizableJoin = true;
                        contextItemToTheLeft = false;
                    }
                }
            }
        }

        if (allowingEmpty) {
            optimizableJoin = false;
        }

        // Since no variable dependency to the current FLWOR expression exists for the expression
        // evaluate the DataFrame with the parent context and calculate the cartesian product
        Dataset<Row> expressionDF;

        Map<Name, VariableDependency> predicateDependencies = predicateIterator.getVariableDependencies();
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && parentProjection.containsKey(variableName)) {
            predicateDependencies.put(Name.CONTEXT_ITEM, parentProjection.get(variableName));
        }

        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && predicateDependencies.containsKey(Name.CONTEXT_POSITION)) {
            optimizableJoin = false;
            expressionDF = getDataFrameStartingClause(
                sequenceIterator,
                sequenceVariableName,
                Name.CONTEXT_POSITION,
                false,
                context,
                predicateDependencies
            );
        } else {
            expressionDF = getDataFrameStartingClause(
                sequenceIterator,
                sequenceVariableName,
                null,
                false,
                context,
                predicateDependencies
            );
        }

        if (optimizableJoin) {
            System.out.println(
                "INFO: Rumble detected that it can optimize your query and make it faster with an equi-join."
            );
        }

        if (optimizableJoin) {
            // expressionDF.show();
            if (contextItemToTheLeft) {
                expressionDF = LetClauseSparkIterator.bindLetVariableInDataFrame(
                    expressionDF,
                    Name.createVariableInNoNamespace(SparkSessionManager.leftHashColumnName),
                    leftHandSideOfJoinEqualityCriterion,
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
            }
            // expressionDF.show();
        }

        String inputDFTableName = "inputTuples";
        String expressionDFTableName = "sequenceExpression";
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && predicateDependencies.containsKey(Name.CONTEXT_COUNT)) {
            expressionDF.sparkSession()
                .udf()
                .register(
                    "serializeIntegerIndex",
                    new IntegerSerializeUDF(),
                    DataTypes.BinaryType
                );
            long size = expressionDF.count();
            expressionDF.createOrReplaceTempView(expressionDFTableName);
            expressionDF = expressionDF.sparkSession()
                .sql(
                    String.format(
                        "SELECT *, serializeIntegerIndex(%s) AS `%s` FROM %s",
                        Long.toString(size),
                        Name.CONTEXT_COUNT.getLocalName(),
                        expressionDFTableName
                    )
                );
        }

        if (optimizableJoin) {
            // inputDF.show();
            if (contextItemToTheLeft) {
                inputTuples = LetClauseSparkIterator.bindLetVariableInDataFrame(
                    inputTuples,
                    Name.createVariableInNoNamespace(SparkSessionManager.rightHashColumnName),
                    rightHandSideOfJoinEqualityCriterion,
                    context,
                    null,
                    true
                );
            } else {
                inputTuples = LetClauseSparkIterator.bindLetVariableInDataFrame(
                    inputTuples,
                    Name.createVariableInNoNamespace(SparkSessionManager.rightHashColumnName),
                    leftHandSideOfJoinEqualityCriterion,
                    context,
                    null,
                    true
                );
            }
            // inputDF.show();
        }

        // Now we prepare the two views that we want to compute the Cartesian product of.
        inputTuples.createOrReplaceTempView(inputDFTableName);
        expressionDF.createOrReplaceTempView(expressionDFTableName);

        // We gather the columns to select from the previous clause.
        // We need to project away the clause's variables from the previous clause.
        StructType inputSchema = inputTuples.schema();
        int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames())
            .indexOf(variableName.toString());
        int duplicatePositionalVariableIndex = -1;
        if (positionalVariableName != null) {
            duplicatePositionalVariableIndex = Arrays.asList(inputSchema.fieldNames())
                .indexOf(positionalVariableName.toString());
        }
        List<String> columnsToSelect = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            duplicateVariableIndex,
            duplicatePositionalVariableIndex,
            parentProjection
        );

        // We don't support positional variables yet for large joins.
        if (positionalVariableName != null) {
            throw new UnsupportedFeatureException(
                    "Rumble detected a large-scale join, but we do not support positional variables yet for these joins.",
                    metadata
            );
        }
        String projectionVariables = FlworDataFrameUtils.getListOfSQLVariables(columnsToSelect, true);

        // We need to prepare the parameters fed into the predicate.
        Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            predicateDependencies
        );
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && !UDFcolumnsByType.containsKey("byte[]")) {
            UDFcolumnsByType.put("byte[]", new ArrayList<>());
        }
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && predicateDependencies.containsKey(Name.CONTEXT_ITEM)) {
            UDFcolumnsByType.get("byte[]").add(Name.CONTEXT_ITEM.getLocalName());
        }
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && predicateDependencies.containsKey(Name.CONTEXT_POSITION)) {
            UDFcolumnsByType.get("byte[]").add(Name.CONTEXT_POSITION.getLocalName());
        }
        if (sequenceVariableName.equals(Name.CONTEXT_ITEM) && predicateDependencies.containsKey(Name.CONTEXT_COUNT)) {
            UDFcolumnsByType.get("byte[]").add(Name.CONTEXT_COUNT.getLocalName());
        }

        // Now we need to register or join predicate as a UDF.
        inputTuples.sparkSession()
            .udf()
            .register(
                "joinUDF",
                new WhereClauseUDF(predicateIterator, context, UDFcolumnsByType),
                DataTypes.BooleanType
            );

        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

        // If we allow empty, we need a LEFT OUTER JOIN.
        if (allowingEmpty) {
            Dataset<Row> resultDF = inputTuples.sparkSession()
                .sql(
                    String.format(
                        "SELECT %s `%s`.`%s` AS `%s` FROM %s LEFT OUTER JOIN %s ON joinUDF(%s) = 'true'",
                        projectionVariables,
                        expressionDFTableName,
                        sequenceVariableName.getLocalName(),
                        variableName,
                        inputDFTableName,
                        expressionDFTableName,
                        UDFParameters
                    )
                );
            return resultDF;
        }

        if (optimizableJoin) {
            // Otherwise, it's a regular join.
            Dataset<Row> resultDF = inputTuples.sparkSession()
                .sql(
                    String.format(
                        "SELECT %s `%s`.`%s` AS `%s` FROM %s JOIN %s ON `%s` = `%s` WHERE joinUDF(%s) = 'true'",
                        projectionVariables,
                        expressionDFTableName,
                        sequenceVariableName.getLocalName(),
                        variableName,
                        inputDFTableName,
                        expressionDFTableName,
                        SparkSessionManager.leftHashColumnName,
                        SparkSessionManager.rightHashColumnName,
                        UDFParameters
                    )
                );
            return resultDF;
        }
        // Otherwise, it's a regular join.
        Dataset<Row> resultDF = inputTuples.sparkSession()
            .sql(
                String.format(
                    "SELECT %s `%s`.`%s` AS `%s` FROM %s JOIN %s ON joinUDF(%s) = 'true'",
                    projectionVariables,
                    expressionDFTableName,
                    sequenceVariableName.getLocalName(),
                    variableName,
                    inputDFTableName,
                    expressionDFTableName,
                    UDFParameters
                )
            );
        return resultDF;
    }

    /**
     * 
     * Non-starting clause, the child clause (above in the syntax) is local but the expression is parallelizable.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromUnion(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        Dataset<Row> df = null;
        this.child.open(context);
        StructType schema = null;
        while (this.child.hasNext()) {
            // We first compute the new tuple variable values
            this.inputTuple = this.child.next();
            this.tupleContext = new DynamicContext(context);
            // IMPORTANT: this must be a new context object every time
            // because of lazy evaluation.
            this.tupleContext.getVariableValues().setBindingsFromTuple(this.inputTuple, getMetadata()); // assign new
                                                                                                        // variables
                                                                                                        // from new

            Dataset<Row> lateralView = getDataFrameStartingClause(this.tupleContext, parentProjection);
            lateralView.createOrReplaceTempView("lateralView");

            // We then get the (singleton) input tuple as a data frame

            List<byte[]> serializedRowColumns = new ArrayList<>();
            for (Name columnName : this.inputTuple.getLocalKeys()) {
                serializedRowColumns.add(
                    FlworDataFrameUtils.serializeItemList(
                        this.inputTuple.getLocalValue(columnName, getMetadata()),
                        this.dataFrameContext.getKryo(),
                        this.dataFrameContext.getOutput()
                    )
                );
            }

            Row row = RowFactory.create(serializedRowColumns.toArray());

            JavaRDD<Row> inputTupleRDD = JavaSparkContext.fromSparkContext(
                lateralView.sparkSession()
                    .sparkContext()
            ).parallelize(Collections.singletonList(row));
            if (schema == null) {
                schema = generateSchema();
            }
            Dataset<Row> inputTupleDataFrame = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(inputTupleRDD, schema);
            inputTupleDataFrame.createOrReplaceTempView("inputTuple");

            // And we join.
            inputTupleDataFrame = inputTupleDataFrame.sparkSession()
                .sql("select * FROM inputTuple JOIN lateralView");

            if (df == null) {
                df = inputTupleDataFrame;
            } else {
                df = df.union(inputTupleDataFrame);
            }
        }
        this.child.close();
        return df;
    }

    /**
     * 
     * Non-starting clause and the child clause (above in the syntax) is parallelizable.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameInParallel(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {

        // the expression is locally evaluated
        Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));
        StructType inputSchema = df.schema();
        int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames()).indexOf(this.variableName.toString());
        int duplicatePositionalVariableIndex = -1;
        if (this.positionalVariableName != null) {
            duplicatePositionalVariableIndex = Arrays.asList(inputSchema.fieldNames())
                .indexOf(this.positionalVariableName.toString());
        }
        List<String> allColumns = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            duplicateVariableIndex,
            duplicatePositionalVariableIndex,
            null
        );
        Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            this.dependencies
        );

        df.sparkSession()
            .udf()
            .register(
                "forClauseUDF",
                new ForClauseUDF(this.assignmentIterator, context, UDFcolumnsByType),
                DataTypes.createArrayType(DataTypes.BinaryType)
            );

        String projectionVariables = FlworDataFrameUtils.getListOfSQLVariables(allColumns, true);
        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

        df.createOrReplaceTempView("input");
        if (this.positionalVariableName == null) {
            if (this.allowingEmpty) {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "select %s explode_outer(forClauseUDF(%s)) as `%s` from input",
                            projectionVariables,
                            UDFParameters,
                            this.variableName
                        )
                    );
            } else {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "select %s explode(forClauseUDF(%s)) as `%s` from input",
                            projectionVariables,
                            UDFParameters,
                            this.variableName
                        )
                    );
            }
        } else {
            df.sparkSession()
                .udf()
                .register(
                    "serializePositionIndex",
                    new IntegerSerializeUDF(),
                    DataTypes.BinaryType
                );

            if (this.allowingEmpty) {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "SELECT %s for_vars.`%s`, serializePositionIndex(IF(for_vars.`%s` IS NULL, 0, for_vars.`%s` + 1)) AS `%s` "
                                + "FROM input "
                                + "LATERAL VIEW OUTER posexplode(forClauseUDF(%s)) for_vars AS `%s`, `%s` ",
                            projectionVariables,
                            this.variableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            UDFParameters,
                            this.positionalVariableName,
                            this.variableName
                        )
                    );
            } else {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "SELECT %s for_vars.`%s`, serializePositionIndex(for_vars.`%s` + 1) AS `%s` "
                                + "FROM input "
                                + "LATERAL VIEW posexplode(forClauseUDF(%s)) for_vars AS `%s`, `%s` ",
                            projectionVariables,
                            this.variableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            UDFParameters,
                            this.positionalVariableName,
                            this.variableName
                        )
                    );
            }
        }
        return df;
    }

    private StructType generateSchema() {
        List<StructField> fields = new ArrayList<>();
        for (Name columnName : this.inputTuple.getLocalKeys()) {
            // all columns store items serialized to binary format
            StructField field = DataTypes.createStructField(columnName.toString(), DataTypes.BinaryType, true);
            fields.add(field);
        }
        return DataTypes.createStructType(fields);
    }

    /**
     * 
     * Starting clause and the expression is parallelizable.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameStartingClause(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        return getDataFrameStartingClause(
            this.assignmentIterator,
            this.variableName,
            this.positionalVariableName,
            this.allowingEmpty,
            context,
            parentProjection
        );
    }

    /**
     * 
     * Starting clause and the expression is parallelizable.
     * 
     * @param context the dynamic context.
     * @param parentProjection the desired project.
     * @return the resulting DataFrame.
     */
    public static Dataset<Row> getDataFrameStartingClause(
            RuntimeIterator iterator,
            Name variableName,
            Name positionalVariableName,
            boolean allowingEmpty,
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // create initial RDD from expression
        JavaRDD<Item> expressionRDD = iterator.getRDD(context);
        Dataset<Row> df = getDataFrameFromItemRDD(variableName, expressionRDD);
        if (positionalVariableName == null && !allowingEmpty) {
            return df;
        }
        if (positionalVariableName == null && allowingEmpty) {
            df.createOrReplaceTempView("input");
            df = df.sparkSession()
                .sql(
                    String.format(
                        "SELECT input.`%s` FROM VALUES(1) FULL OUTER JOIN input",
                        variableName
                    )
                );
            return df;
        }
        // Add column for positional variable, similar to count clause.
        Dataset<Row> dfWithIndex = CountClauseSparkIterator.addSerializedCountColumn(
            df,
            parentProjection,
            positionalVariableName
        );
        if (!allowingEmpty) {
            return dfWithIndex;
        }
        dfWithIndex.createOrReplaceTempView("inputWithIndex");
        dfWithIndex.sparkSession()
            .udf()
            .register(
                "serializeCountIndex",
                new IntegerSerializeUDF(),
                DataTypes.BinaryType
            );

        dfWithIndex = dfWithIndex.sparkSession()
            .sql(
                String.format(
                    "SELECT inputWithIndex.`%s`, IF(inputWithIndex.`%s` IS NULL, serializeCountIndex(0), inputWithIndex.`%s`) AS `%s` FROM VALUES(1) FULL OUTER JOIN inputWithIndex",
                    variableName,
                    positionalVariableName,
                    positionalVariableName,
                    positionalVariableName
                )
            );
        return dfWithIndex;
    }

    private static Dataset<Row> getDataFrameFromItemRDD(Name variableName, JavaRDD<Item> expressionRDD) {
        // define a schema
        List<StructField> fields = Collections.singletonList(
            DataTypes.createStructField(variableName.toString(), DataTypes.BinaryType, true)
        );
        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> rowRDD = expressionRDD.map(new ItemsToBinaryColumn());

        // apply the schema to row RDD
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
    }

    @Override
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

    @Override
    public Set<Name> getVariablesBoundInCurrentFLWORExpression() {
        Set<Name> result = new HashSet<>();
        if (this.child != null) {
            result.addAll(this.child.getVariablesBoundInCurrentFLWORExpression());
        }
        result.add(this.variableName);
        if (this.positionalVariableName != null) {
            result.add(this.positionalVariableName);
        }
        return result;
    }

    @Override
    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (int i = 0; i < indent + 1; ++i) {
            buffer.append("  ");
        }
        buffer.append("Variable ").append(this.variableName.toString()).append("\n");
        for (int i = 0; i < indent + 1; ++i) {
            buffer.append("  ");
        }
        if (this.positionalVariableName != null) {
            buffer.append("Positional variable ").append(this.positionalVariableName.toString()).append("\n");
        }
        this.assignmentIterator.print(buffer, indent + 1);
    }

    @Override
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

        // remove the variables that this for clause binds.
        projection.remove(this.variableName);
        if (this.positionalVariableName != null) {
            projection.remove(this.positionalVariableName);
        }

        // add the variable dependencies needed by this for clause's expression.
        Map<Name, DynamicContext.VariableDependency> exprDependency = this.assignmentIterator
            .getVariableDependencies();
        for (Name variable : exprDependency.keySet()) {
            if (projection.containsKey(variable)) {
                if (projection.get(variable) != exprDependency.get(variable)) {
                    // If the projection already needed a different kind of dependency, we fall back to the full
                    // sequence of items.
                    projection.put(variable, DynamicContext.VariableDependency.FULL);
                }
            } else {
                projection.put(variable, exprDependency.get(variable));
            }
        }
        return projection;
    }
}
