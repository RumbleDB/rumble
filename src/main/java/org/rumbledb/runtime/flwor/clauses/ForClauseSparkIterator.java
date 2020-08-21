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
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.closures.ForClauseLocalTupleToRowClosure;
import org.rumbledb.runtime.flwor.closures.ForClauseSerializeClosure;
import org.rumbledb.runtime.flwor.udfs.ForClauseUDF;
import org.rumbledb.runtime.flwor.udfs.IntegerSerializeUDF;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ForClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private Name variableName; // for efficient use in local iteration
    private Name positionalVariableName; // for efficient use in local iteration
    private RuntimeIterator assignmentIterator;
    private Map<Name, DynamicContext.VariableDependency> dependencies;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private long position;
    private FlworTuple nextLocalTupleResult;
    private FlworTuple inputTuple; // tuple received from child, used for tuple creation

    public ForClauseSparkIterator(
            RuntimeTupleIterator child,
            Name variableName,
            Name positionalVariableName,
            RuntimeIterator assignmentIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.positionalVariableName = positionalVariableName;
        this.assignmentIterator = assignmentIterator;
        this.dependencies = this.assignmentIterator.getVariableDependencies();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        if (this.child != null) { // if it's not a start clause
            this.child.open(this.currentDynamicContext);
            this.tupleContext = new DynamicContext(this.currentDynamicContext); // assign current context as parent
            this.position = 1;
            setNextLocalTupleResult();
        } else { // if it's a start clause, get results using only the assignmentIterator
            this.assignmentIterator.open(this.currentDynamicContext);
            this.position = 1;
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
            setNextLocalTupleResult();
        } else { // if it's a start clause, get results using only the assignmentIterator
            this.assignmentIterator.reset(this.currentDynamicContext);
            this.position = 1;
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
            if (setResultFromExpression()) {
                return;
            }
        }

        // execution reaches here when there are no more results
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
            return true;
        } else {
            this.assignmentIterator.close();
            this.hasNext = false;
            return false;
        }
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
        // Check that the expression does not depend functionally on the input tuples
        Set<Name> intersection = new HashSet<>(
                this.assignmentIterator.getVariableDependencies().keySet()
        );
        intersection.retainAll(getVariablesBoundInCurrentFLWORExpression());
        boolean expressionUsesVariablesOfCurrentFlwor = !intersection.isEmpty();

        // If it does, we cannot handle it.
        if (expressionUsesVariablesOfCurrentFlwor) {
            throw new JobWithinAJobException(
                    "A for clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        // Since no variable dependency to the current FLWOR expression exists for the expression
        // evaluate the DataFrame with the parent context and calculate the cartesian product
        Dataset<Row> expressionDF;
        expressionDF = getDataFrameStartingClause(context, parentProjection);
        // And we add a column for the positional variable if there is one.
        if (this.positionalVariableName != null) {
            // Add column for positional variable, similar to count clause.
            expressionDF = CountClauseSparkIterator.addSerializedCountColumn(
                expressionDF,
                parentProjection,
                this.positionalVariableName
            );
        }

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
        if (duplicatePositionalVariableIndex == -1) {
            columnsToSelect.add(this.positionalVariableName.toString());
        } else {
            columnsToSelect.add(expressionDFTableName + "`.`" + this.positionalVariableName);
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
        this.tupleContext = new DynamicContext(context); // assign current context as parent
        StructType schema = null;
        while (this.child.hasNext()) {
            this.inputTuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(this.inputTuple, getMetadata()); // assign new
                                                                                                        // variables
                                                                                                        // from new
            // tuple
            JavaRDD<Item> expressionRDD = this.assignmentIterator.getRDD(this.tupleContext);

            if (schema == null) {
                schema = generateSchema();
            }

            JavaRDD<Row> rowRDD = expressionRDD.map(
                new ForClauseLocalTupleToRowClosure(this.inputTuple, getMetadata())
            );

            Dataset<Row> nextDataFrame = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(rowRDD, schema);

            if (this.positionalVariableName != null) {
                // Add column for positional variable, similar to count clause.
                nextDataFrame = CountClauseSparkIterator.addSerializedCountColumn(
                    nextDataFrame,
                    parentProjection,
                    this.positionalVariableName
                );
            }

            if (df == null) {
                df = nextDataFrame;
            } else {
                df = df.union(nextDataFrame);
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
            df = df.sparkSession()
                .sql(
                    String.format(
                        "select %s explode(forClauseUDF(%s)) as `%s` from input",
                        projectionVariables,
                        UDFParameters,
                        this.variableName
                    )
                );
        } else {
            df.sparkSession()
                .udf()
                .register(
                    "serializePositionIndex",
                    new IntegerSerializeUDF(),
                    DataTypes.BinaryType
                );

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
        return df;
    }

    private StructType generateSchema() {
        Set<Name> oldColumnNames = this.inputTuple.getLocalKeys();
        List<Name> newColumnNames = new ArrayList<>(oldColumnNames);
        newColumnNames.add(this.variableName);

        List<StructField> fields = new ArrayList<>();
        for (Name columnName : newColumnNames) {
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
        // create initial RDD from expression
        JavaRDD<Item> expressionRDD = this.assignmentIterator.getRDD(context);
        Dataset<Row> df = getDataFrameFromItemRDD(expressionRDD);
        if (this.positionalVariableName == null) {
            return df;
        }

        // Add column for positional variable, similar to count clause.
        Dataset<Row> dfWithIndex = CountClauseSparkIterator.addSerializedCountColumn(
            df,
            parentProjection,
            this.positionalVariableName
        );
        return dfWithIndex;
    }

    private Dataset<Row> getDataFrameFromItemRDD(JavaRDD<Item> expressionRDD) {
        // define a schema
        List<StructField> fields = new ArrayList<>();
        StructField field = DataTypes.createStructField(this.variableName.toString(), DataTypes.BinaryType, true);
        fields.add(field);
        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> rowRDD = expressionRDD.map(new ForClauseSerializeClosure());

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
