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
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.closures.ItemsToBinaryColumn;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;
import org.rumbledb.runtime.flwor.udfs.ForClauseUDF;
import org.rumbledb.runtime.flwor.udfs.GenericForClauseUDF;
import org.rumbledb.runtime.flwor.udfs.IntegerSerializeUDF;
import org.rumbledb.runtime.navigation.PredicateIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
        this.assignmentIterator.getVariableDependencies();
        this.dataFrameContext = new DataFrameContext();
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public Name getPositionalVariableName() {
        return this.positionalVariableName;
    }

    public RuntimeIterator getAssignmentIterator() {
        return this.assignmentIterator;
    }

    public boolean isAllowingEmpty() {
        return this.allowingEmpty;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        if (this.child != null && this.evaluationDepthLimit != 0) { // if it's not a start clause
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

        if (this.child != null && this.evaluationDepthLimit != 0) { // if it's not a start clause
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
            if (this.child == null || this.evaluationDepthLimit == 0) { // if it's the initial for clause, call the
                                                                        // correct function
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
    }

    /**
     * assignmentIterator has to be open prior to call.
     *
     * @return true if nextLocalTupleResult is set and hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (this.assignmentIterator.hasNext()) { // if expression returns a value, set it as next

            // Set the for item
            if (this.child == null || this.evaluationDepthLimit == 0) { // if initial for clause
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

        // If an item was already output by this expression and there is no more, we are done.
        if (!this.isFirstItem || !this.allowingEmpty) {
            this.assignmentIterator.close();
            this.hasNext = false;
            return false;
        }

        // If nothing was output yet by this expression but we allow empty, we need to bind
        // the empty sequence.
        if (this.child == null || this.evaluationDepthLimit == 0) { // if initial for clause
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
        if (this.child != null && this.evaluationDepthLimit != 0) {
            this.child.close();
        }
        if (this.assignmentIterator.isOpen()) {
            this.assignmentIterator.close();
        }
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context
    ) {
        // if it's a starting clause
        if (this.child == null || this.evaluationDepthLimit == 0) {
            return getDataFrameStartingClause(context, this.outputTupleProjection);
        }

        if (this.child.isDataFrame()) {
            if (this.assignmentIterator.isRDDOrDataFrame()) {
                return getDataFrameFromCartesianProduct(context);
            }

            return getDataFrameInParallel(context);
        }

        // if child is locally evaluated
        // assignmentIterator is definitely an RDD if execution flows here
        return getDataFrameFromUnion(context);
    }

    /**
     * 
     * Non-starting clause, the child clause (above in the syntax) is parallelizable, the expression as well, and the
     * expression does not depend on the input tuple.
     * 
     * @param context the dynamic context.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromCartesianProduct(
            DynamicContext context
    ) {
        // If the expression depends on this input tuple, we might still recognize an join.
        if (!LetClauseSparkIterator.isExpressionIndependentFromInputTuple(this.assignmentIterator, this.child)) {
            return getDataFrameFromJoin(context);
        }

        // Since no variable dependency to the current FLWOR expression exists for the expression
        // evaluate the DataFrame with the parent context and calculate the cartesian product
        Dataset<Row> expressionDF;
        Map<Name, DynamicContext.VariableDependency> startingClauseDependencies = new HashMap<>();
        if (this.outputTupleProjection.containsKey(this.variableName)) {
            startingClauseDependencies.put(this.variableName, this.outputTupleProjection.get(this.variableName));
        }
        if (
            this.positionalVariableName != null
                && this.outputTupleProjection.containsKey(this.positionalVariableName)
        ) {
            startingClauseDependencies.put(
                this.positionalVariableName,
                this.outputTupleProjection.get(this.positionalVariableName)
            );
        }
        expressionDF = getDataFrameStartingClause(context, startingClauseDependencies);

        Dataset<Row> inputDF = this.child.getDataFrame(
            context
        );

        // Now we prepare the two views that we want to compute the Cartesian product of.
        String inputDFTableName = FlworDataFrameUtils.createTempView(inputDF);
        String expressionDFTableName = FlworDataFrameUtils.createTempView(expressionDF);

        // We gather the columns to select from the previous clause.
        // We need to project away the clause's variables from the previous clause.
        StructType inputSchema = inputDF.schema();
        List<Name> overridenVariables = new ArrayList<>();
        overridenVariables.add(this.variableName);
        if (this.positionalVariableName != null) {
            overridenVariables.add(this.positionalVariableName);
        }
        List<String> columnsToSelect = FlworDataFrameUtils.getColumnNames(
            inputSchema,
            this.outputTupleProjection,
            null,
            overridenVariables
        );

        // For the new variables, we need to disambiguate.
        columnsToSelect.add(expressionDFTableName + "`.`" + this.variableName.toString());
        if (this.positionalVariableName != null) {
            columnsToSelect.add(expressionDFTableName + "`.`" + this.positionalVariableName);
        }
        String projectionVariables = FlworDataFrameUtils.getSQLProjection(columnsToSelect, false);

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
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromJoin(
            DynamicContext context
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

        // We don't support positional variables yet for large joins.
        if (this.positionalVariableName != null) {
            throw new UnsupportedFeatureException(
                    "Rumble detected a large-scale join, but we do not support positional variables yet for these joins.",
                    getMetadata()
            );
        }

        String expressionDFTableName = "sequenceExpression";


        // Next we prepare the data frame on the expression side.
        Dataset<Row> expressionDF;


        Map<Name, VariableDependency> predicateDependencies = predicateIterator.getVariableDependencies();

        if (predicateDependencies.containsKey(Name.CONTEXT_POSITION)) {
            Map<Name, DynamicContext.VariableDependency> startingClauseDependencies = new HashMap<>();
            startingClauseDependencies.put(Name.CONTEXT_ITEM, DynamicContext.VariableDependency.FULL);
            startingClauseDependencies.put(Name.CONTEXT_POSITION, DynamicContext.VariableDependency.FULL);

            expressionDF = getDataFrameStartingClause(
                sequenceIterator,
                Name.CONTEXT_ITEM,
                Name.CONTEXT_POSITION,
                false,
                context,
                startingClauseDependencies
            );
        } else {
            Map<Name, DynamicContext.VariableDependency> startingClauseDependencies = new HashMap<>();
            startingClauseDependencies.put(Name.CONTEXT_ITEM, DynamicContext.VariableDependency.FULL);
            expressionDF = getDataFrameStartingClause(
                sequenceIterator,
                Name.CONTEXT_ITEM,
                null,
                false,
                context,
                startingClauseDependencies
            );
        }

        // If the join criterion uses the context count, then we need to add it to the expression side (it is a
        // constant).
        if (predicateDependencies.containsKey(Name.CONTEXT_COUNT)) {
            expressionDF.sparkSession()
                .udf()
                .register(
                    "serializeIntegerIndex",
                    new IntegerSerializeUDF(),
                    DataTypes.BinaryType
                );
            long size = expressionDF.count();
            expressionDFTableName = FlworDataFrameUtils.createTempView(expressionDF);
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

        List<Name> variablesInRightInputTuple = new ArrayList<>();
        variablesInRightInputTuple.add(Name.CONTEXT_ITEM);
        if (
            predicateDependencies.containsKey(Name.CONTEXT_POSITION)
        ) {
            variablesInRightInputTuple.add(Name.CONTEXT_POSITION);
        }
        if (
            predicateDependencies.containsKey(Name.CONTEXT_COUNT)
        ) {
            variablesInRightInputTuple.add(Name.CONTEXT_COUNT);
        }

        return JoinClauseSparkIterator.joinInputTupleWithSequenceOnPredicate(
            context,
            this.child.getDataFrame(context),
            expressionDF,
            this.outputTupleProjection,
            (this.child == null || this.evaluationDepthLimit == 0)
                ? Collections.emptyList()
                : new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            variablesInRightInputTuple,
            predicateIterator,
            this.allowingEmpty,
            this.variableName,
            getMetadata()
        );
    }

    /**
     * 
     * Non-starting clause, the child clause (above in the syntax) is local but the expression is parallelizable.
     * 
     * @param context the dynamic context.
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameFromUnion(
            DynamicContext context
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

            Map<Name, DynamicContext.VariableDependency> startingClauseDependencies = new HashMap<>();
            if (this.outputTupleProjection.containsKey(this.variableName)) {
                startingClauseDependencies.put(this.variableName, this.outputTupleProjection.get(this.variableName));
            }
            if (
                this.positionalVariableName != null
                    && this.outputTupleProjection.containsKey(this.positionalVariableName)
            ) {
                startingClauseDependencies.put(
                    this.positionalVariableName,
                    this.outputTupleProjection.get(this.positionalVariableName)
                );
            }
            Dataset<Row> lateralView = getDataFrameStartingClause(this.tupleContext, startingClauseDependencies);
            String lateralViewString = FlworDataFrameUtils.createTempView(lateralView);

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
            ).parallelize(Collections.singletonList(row), 1);
            if (schema == null) {
                schema = generateSchema();
            }
            Dataset<Row> inputTupleDataFrame = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(inputTupleRDD, schema);
            String inputTuple = FlworDataFrameUtils.createTempView(inputTupleDataFrame);

            // And we join.
            inputTupleDataFrame = inputTupleDataFrame.sparkSession()
                .sql(String.format("select * FROM %s JOIN %s", inputTuple, lateralViewString));

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
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameInParallel(
            DynamicContext context
    ) {

        // the expression is locally evaluated
        Dataset<Row> df = this.child.getDataFrame(context);
        StructType inputSchema = df.schema();
        List<Name> variableNamesToExclude = new ArrayList<>();
        variableNamesToExclude.add(this.variableName);
        if (this.positionalVariableName != null) {
            variableNamesToExclude.add(this.positionalVariableName);
        }
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            inputSchema,
            this.outputTupleProjection,
            null,
            variableNamesToExclude
        );

        Dataset<Row> nativeQueryResult = tryNativeQuery(
            df,
            this.variableName,
            this.positionalVariableName,
            this.allowingEmpty,
            this.assignmentIterator,
            allColumns,
            inputSchema,
            context
        );
        if (nativeQueryResult != null) {
            return nativeQueryResult;
        }

        List<String> UDFcolumns;
        if (this.child != null && this.evaluationDepthLimit != 0) {
            UDFcolumns = FlworDataFrameUtils.getColumnNames(
                inputSchema,
                this.assignmentIterator.getVariableDependencies(),
                new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
                null
            );
        } else {
            UDFcolumns = Collections.emptyList();
        }

        df.sparkSession()
            .udf()
            .register(
                "forClauseUDF",
                new ForClauseUDF(this.assignmentIterator, context, inputSchema, UDFcolumns),
                DataTypes.createArrayType(DataTypes.BinaryType)
            );

        String projectionVariables = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);
        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);

        String viewName = FlworDataFrameUtils.createTempView(df);
        if (this.positionalVariableName == null) {
            if (this.allowingEmpty) {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "select %s explode_outer(forClauseUDF(%s)) as `%s` from %s",
                            projectionVariables,
                            UDFParameters,
                            this.variableName,
                            viewName
                        )
                    );
            } else {
                df = df.sparkSession()
                    .sql(
                        String.format(
                            "select %s explode(forClauseUDF(%s)) as `%s` from %s",
                            projectionVariables,
                            UDFParameters,
                            this.variableName,
                            viewName
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
                                + "FROM %s "
                                + "LATERAL VIEW OUTER posexplode(forClauseUDF(%s)) for_vars AS `%s`, `%s` ",
                            projectionVariables,
                            this.variableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            viewName,
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
                                + "FROM %s "
                                + "LATERAL VIEW posexplode(forClauseUDF(%s)) for_vars AS `%s`, `%s` ",
                            projectionVariables,
                            this.variableName,
                            this.positionalVariableName,
                            this.positionalVariableName,
                            viewName,
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
     * @return the resulting DataFrame.
     */
    private Dataset<Row> getDataFrameStartingClause(
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> outputDependencies
    ) {
        return getDataFrameStartingClause(
            this.assignmentIterator,
            this.variableName,
            this.positionalVariableName,
            this.allowingEmpty,
            context,
            outputDependencies
        );
    }

    /**
     * 
     * Starting clause and the expression is parallelizable.
     * 
     * @param iterator the expression iterator
     * @param variableName the name of the for variable
     * @param positionalVariableName the name of the positional variable (or null if none)
     * @param allowingEmpty whether the allowing empty option is present
     * @param context the dynamic context.
     * @param outputDependencies the desired project.
     * @return the resulting DataFrame.
     */
    public static Dataset<Row> getDataFrameStartingClause(
            RuntimeIterator iterator,
            Name variableName,
            Name positionalVariableName,
            boolean allowingEmpty,
            DynamicContext context,
            Map<Name, DynamicContext.VariableDependency> outputDependencies
    ) {
        Dataset<Row> df = null;;
        if (iterator.isDataFrame()) {
            JSoundDataFrame rows = iterator.getDataFrame(context);

            String assignment = FlworDataFrameUtils.createTempView(rows.getDataFrame());
            if (rows.getItemType().isObjectItemType()) {
                String[] fields = rows.getDataFrame().schema().fieldNames();
                String columnNames = FlworDataFrameUtils.getSQLProjection(Arrays.asList(fields), false);
                df = rows.getDataFrame()
                    .sparkSession()
                    .sql(
                        String.format(
                            "SELECT struct(%s) AS `%s` FROM %s",
                            columnNames,
                            variableName,
                            assignment
                        )
                    );
            } else {
                df = rows.getDataFrame()
                    .sparkSession()
                    .sql(
                        String.format(
                            "SELECT `%s` AS `%s` FROM %s",
                            SparkSessionManager.atomicJSONiqItemColumnName,
                            variableName,
                            assignment
                        )
                    );
            }
        } else {
            // create initial RDD from expression
            JavaRDD<Item> expressionRDD = iterator.getRDD(context);
            df = getDataFrameFromItemRDD(variableName, expressionRDD);
        }
        if (positionalVariableName == null && !allowingEmpty) {
            return df;
        }
        if (positionalVariableName == null && allowingEmpty) {
            String viewName = FlworDataFrameUtils.createTempView(df);
            df = df.sparkSession()
                .sql(
                    String.format(
                        "SELECT %s.`%s` FROM VALUES(1) FULL OUTER JOIN %s",
                        viewName,
                        variableName,
                        viewName
                    )
                );
            return df;
        }
        // Add column for positional variable, similar to count clause.
        Dataset<Row> dfWithIndex = CountClauseSparkIterator.addSerializedCountColumn(
            df,
            outputDependencies,
            positionalVariableName
        );
        if (!allowingEmpty) {
            return dfWithIndex;
        }
        String inputWithIndex = FlworDataFrameUtils.createTempView(dfWithIndex);
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
                    "SELECT %s.`%s`, IF(%s.`%s` IS NULL, serializeCountIndex(0), %s.`%s`) AS `%s` FROM VALUES(1) FULL OUTER JOIN %s",
                    inputWithIndex,
                    variableName,
                    inputWithIndex,
                    positionalVariableName,
                    inputWithIndex,
                    positionalVariableName,
                    positionalVariableName,
                    inputWithIndex
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

    @Override
    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        if (this.child != null && this.evaluationDepthLimit != 0) {
            result.addAll(this.child.getOutputTupleVariableNames());
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
     * Try to generate the native query for the for clause and run it, if successful return the resulting dataframe,
     * otherwise it returns null
     *
     * @param dataFrame input dataframe for the query
     * @param newVariableName name of the new bound variable
     * @param positionalVariableName name of the positional variable (or null if absent)
     * @param allowingEmpty boolean signaling allowing empty flag in expression
     * @param iterator for variable assignment expression iterator
     * @param allColumns other columns required in following clauses
     * @param inputSchema input schema of the dataframe
     * @param context current dynamic context of the dataframe
     * @return resulting dataframe of the for clause if successful, null otherwise
     */
    public static Dataset<Row> tryNativeQuery(
            Dataset<Row> dataFrame,
            Name newVariableName,
            Name positionalVariableName,
            boolean allowingEmpty,
            RuntimeIterator iterator,
            List<FlworDataFrameColumn> allColumns,
            StructType inputSchema,
            DynamicContext context
    ) {
        NativeClauseContext forContext = new NativeClauseContext(FLWOR_CLAUSES.FOR, inputSchema, context);
        NativeClauseContext nativeQuery = iterator.generateNativeQuery(forContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        System.err.println(
            "[INFO] Rumble was able to optimize a for clause to a native SQL query."
        );
        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);
        String viewName = FlworDataFrameUtils.createTempView(dataFrame);

        // let's distinguish 4 cases
        if (positionalVariableName == null) {
            if (allowingEmpty) {
                List<String> lateralViewPart = nativeQuery.getLateralViewPart();
                if (lateralViewPart.size() == 0) {
                    // no array unboxing in the operation
                    // already covered in the generation and handled through UDF
                    // this branch should never happen in practice
                    return null;
                } else {
                    // we have at least an array unboxing operation
                    // col is the default name of explode

                    // to deal with allowing empty
                    // first we add an artificial unique id to the dataset and re-register the input table
                    String rowIdField = "idx-9384-3948-1272-4375";
                    dataFrame = dataFrame.sparkSession()
                        .sql("select *, monotonically_increasing_id() as `" + rowIdField + "` from " + viewName);
                    String viewName2 = FlworDataFrameUtils.createTempView(dataFrame);

                    // then we create the virtual exploded table as before
                    // but this time we store it and also get the index field
                    StringBuilder lateralViewString = new StringBuilder();
                    int arrIndex = 0;
                    for (String lateralView : lateralViewPart) {
                        ++arrIndex;
                        lateralViewString.append(" lateral view ");
                        lateralViewString.append(lateralView);
                        lateralViewString.append(" arr");
                        lateralViewString.append(arrIndex);
                    }
                    Dataset<Row> lateralViewDf = dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select `%s`, arr%d.col%s as `%s` from %s %s",
                                rowIdField,
                                arrIndex,
                                nativeQuery.getResultingQuery(),
                                newVariableName,
                                viewName2,
                                lateralViewString
                            )
                        );
                    String lateralViewName = FlworDataFrameUtils.createTempView(lateralViewDf);

                    // to return the correct number of empty results we perform a left join between input and
                    // lateral
                    return dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s %s.`%s` from %s left join %s on %s.`%s` = %s.`%s`",
                                selectSQL,
                                lateralViewName,
                                newVariableName,
                                viewName2,
                                lateralViewName,
                                viewName2,
                                rowIdField,
                                lateralViewName,
                                rowIdField
                            )
                        );
                }
            } else {
                List<String> lateralViewPart = nativeQuery.getLateralViewPart();
                if (lateralViewPart.size() == 0) {
                    // no array unboxing in the operation
                    return dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s %s as `%s` from %s",
                                selectSQL,
                                nativeQuery.getResultingQuery(),
                                newVariableName,
                                viewName
                            )
                        );
                } else {
                    // we have at least an array unboxing operation
                    // col is the default name of explode
                    StringBuilder lateralViewString = new StringBuilder();
                    int arrIndex = 0;
                    for (String lateralView : lateralViewPart) {
                        ++arrIndex;
                        lateralViewString.append(" lateral view ");
                        lateralViewString.append(lateralView);
                        lateralViewString.append(" arr");
                        lateralViewString.append(arrIndex);
                    }
                    return dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s arr%d.col%s as `%s` from %s %s",
                                selectSQL,
                                arrIndex,
                                nativeQuery.getResultingQuery(),
                                newVariableName,
                                viewName,
                                lateralViewString
                            )
                        );
                }
            }
        } else {
            // common part for positional variable handling
            List<String> lateralViewPart = nativeQuery.getLateralViewPart();
            if (lateralViewPart.size() == 0) {
                // if allowing empty we do not deal with this
                if (allowingEmpty) {
                    return null;
                }
                // no array unboxing in the operation
                // therefore position is for sure 1
                return dataFrame.sparkSession()
                    .sql(
                        String.format(
                            "select %s %s as `%s`, 1 as `%s` from %s",
                            selectSQL,
                            nativeQuery.getResultingQuery(),
                            newVariableName,
                            positionalVariableName,
                            viewName
                        )
                    );
            } else {
                // we have at least an array unboxing operation
                // pos, col are the default name of posexplode function
                // to deal with positional variable
                // we first add unique index to guarantee grouping correctly
                String rowIdField = "idx-9384-3948-1272-4375";
                dataFrame = dataFrame.sparkSession()
                    .sql("select *, monotonically_increasing_id() as `" + rowIdField + "` from " + viewName);
                String viewName2 = FlworDataFrameUtils.createTempView(dataFrame);

                // then we collect all values from lateral view
                // and group by original tuple
                // this is basically equivalent to flattening the array, in case of multiple unboxing operation
                StringBuilder lateralViewString = new StringBuilder();
                int arrIndex = 0;
                for (String lateralView : lateralViewPart) {
                    ++arrIndex;
                    lateralViewString.append(" lateral view ");
                    lateralViewString.append(lateralView);
                    lateralViewString.append(" arr");
                    lateralViewString.append(arrIndex);
                }
                dataFrame = dataFrame.sparkSession()
                    .sql(
                        String.format(
                            "select `%s`, %s collect_list(arr%d.col%s) as grouped from %s %s group by %s `%s`",
                            rowIdField,
                            selectSQL,
                            arrIndex,
                            nativeQuery.getResultingQuery(),
                            viewName2,
                            lateralViewString,
                            selectSQL,
                            rowIdField
                        )
                    );


                if (allowingEmpty) {
                    // register a support table to keep the empty values
                    String allRowViewName = FlworDataFrameUtils.createTempView(
                        dataFrame.sparkSession()
                            .sql("select `" + rowIdField + "` from " + viewName2)
                    );

                    // register previously created table
                    String viewName3 = FlworDataFrameUtils.createTempView(dataFrame);

                    // insert null values
                    dataFrame = dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s.`%s`, %s grouped from %s left join %s on %s.`%s` = %s.`%s`",
                                allRowViewName,
                                rowIdField,
                                selectSQL,
                                allRowViewName,
                                viewName3,
                                allRowViewName,
                                rowIdField,
                                viewName3,
                                rowIdField
                            )
                        );
                    String viewName4 = FlworDataFrameUtils.createTempView(dataFrame);

                    // we use a lateral view to handle proper counting and NULL handling
                    return dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s IF(exploded.pos IS NULL, 0, exploded.pos + 1) as `%s`, exploded.col as `%s`  from %s lateral view outer posexplode(grouped) exploded",
                                selectSQL,
                                positionalVariableName,
                                newVariableName,
                                viewName4
                            )
                        );
                } else {
                    // register previously created table
                    String viewName3 = FlworDataFrameUtils.createTempView(dataFrame);

                    // finally we unwrap it with a single posexplode
                    return dataFrame.sparkSession()
                        .sql(
                            String.format(
                                "select %s (exploded.pos + 1) as `%s`, exploded.col as `%s`  from %s lateral view posexplode(grouped) exploded",
                                selectSQL,
                                positionalVariableName,
                                newVariableName,
                                viewName3
                            )
                        );
                }
            }
        }
    }

    public boolean containsClause(FLWOR_CLAUSES kind) {
        if (kind == FLWOR_CLAUSES.FOR) {
            return true;
        }
        if (this.child == null || this.evaluationDepthLimit == 0) {
            return false;
        }
        return this.child.containsClause(kind);
    }

    public static void registerForClauseUDF(
            Dataset<Row> dataFrame,
            RuntimeIterator newVariableExpression,
            DynamicContext context,
            StructType inputSchema,
            List<String> UDFcolumns,
            SequenceType sequenceType
    ) {
        if (
            sequenceType != null
                && !sequenceType.isEmptySequence()
        ) {
            ItemType itemType = sequenceType.getItemType();

            if (itemType.equals(BuiltinTypesCatalogue.stringItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "forClauseUDF",
                        new GenericForClauseUDF<String>(
                                newVariableExpression,
                                context,
                                inputSchema,
                                UDFcolumns,
                                "String"
                        ),
                        DataTypes.StringType
                    );
                return;
            }

            if (itemType.equals(BuiltinTypesCatalogue.integerItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "forClauseUDF",
                        new GenericForClauseUDF<Integer>(
                                newVariableExpression,
                                context,
                                inputSchema,
                                UDFcolumns,
                                "Integer"
                        ),
                        DataTypes.IntegerType
                    );
                return;
            }

            if (itemType.equals(BuiltinTypesCatalogue.decimalItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "forClauseUDF",
                        new GenericForClauseUDF<BigDecimal>(
                                newVariableExpression,
                                context,
                                inputSchema,
                                UDFcolumns,
                                "BigDecimal"
                        ),
                        DataTypes.createDecimalType()
                    );
                return;
            }

            if (itemType.equals(BuiltinTypesCatalogue.doubleItem)) {
                dataFrame.sparkSession()
                    .udf()
                    .register(
                        "forClauseUDF",
                        new GenericForClauseUDF<Double>(
                                newVariableExpression,
                                context,
                                inputSchema,
                                UDFcolumns,
                                "Double"
                        ),
                        DataTypes.DoubleType
                    );
                return;
            }
        }

        // if it is not one of the allowed sequence type we just return the default udf
        dataFrame.sparkSession()
            .udf()
            .register(
                "forClauseUDF",
                new ForClauseUDF(newVariableExpression, context, inputSchema, UDFcolumns),
                DataTypes.BinaryType
            );
    }
}
