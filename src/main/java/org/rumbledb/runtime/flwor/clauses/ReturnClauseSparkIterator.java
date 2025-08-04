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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.closures.ReturnFlatMapClosure;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

import org.rumbledb.runtime.update.PendingUpdateList;

import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReturnClauseSparkIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeTupleIterator child;
    private DynamicContext tupleContext; // re-use same DynamicContext object for efficiency
    private RuntimeIterator expression;
    private Item nextResult;

    public ReturnClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator expression,
            boolean isUpdating,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(expression), staticContext);
        this.child = child;
        this.expression = expression;
        this.isUpdating = isUpdating;
        setInputAndOutputTupleVariableDependencies();
    }

    public ReturnClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator expression,
            RuntimeStaticContext staticContext
    ) {
        this(child, expression, false, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );

            this.child.open(context);
            JavaRDD<Item> result = null;
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                               // from new tuple

                JavaRDD<Item> intermediateResult = this.expression.getRDD(dynamicContext);
                if (result == null) {
                    result = intermediateResult;
                } else {
                    result = result.union(intermediateResult);
                }
            }
            if (result == null) {
                return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
            }
            return result;
        }
        Dataset<Row> df = this.child.getDataFrame(context).getDataFrame();
        StructType oldSchema = df.schema();
        List<FlworDataFrameColumn> UDFcolumns = FlworDataFrameUtils.getColumns(
            oldSchema,
            this.expression.getVariableDependencies(),
            new ArrayList<Name>(this.child.getOutputTupleVariableNames()),
            null
        );
        return df.toJavaRDD().flatMap(new ReturnFlatMapClosure(expression, context, UDFcolumns));
    }

    private void setInputAndOutputTupleVariableDependencies() {
        Map<Name, VariableDependency> dependencies = this.expression.getVariableDependencies();
        Set<Name> allTupleNames = this.child.getOutputTupleVariableNames();
        Map<Name, VariableDependency> projection = new HashMap<>();
        for (Name n : dependencies.keySet()) {
            if (allTupleNames.contains(n)) {
                projection.put(n, dependencies.get(n));
            }
        }
        this.child.setInputAndOutputTupleVariableDependencies(projection);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            // context
            this.child.open(context);
            JSoundDataFrame result = null;
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                               // from new tuple

                JSoundDataFrame intermediateResult = this.expression.getDataFrame(dynamicContext);
                if (result == null) {
                    result = intermediateResult;
                } else {
                    result = result.union(intermediateResult);
                }
            }
            this.child.close();
            if (result == null) {
                return JSoundDataFrame.emptyDataFrame();
            }
            return result;
        }
        if (!this.child.isDataFrame()) {
            throw new OurBadException(
                    "Unexpected application state: a dataframe was expected even though the previous tuple does not produce one.",
                    getMetadata()
            );
        }

        Dataset<Row> df = this.child.getDataFrame(context).getDataFrame();
        StructType inputSchema = df.schema();
        Dataset<Row> nativeQueryResult = null;
        if (getConfiguration().nativeExecution()) {
            nativeQueryResult = tryNativeQuery(
                df,
                this.expression,
                inputSchema,
                context
            );
        }
        if (nativeQueryResult != null) {
            if (this.expression.getStaticType().getItemType().isObjectItemType()) {
                String input = FlworDataFrameUtils.createTempView(nativeQueryResult);
                nativeQueryResult =
                    nativeQueryResult.sparkSession()
                        .sql(
                            String.format(
                                "SELECT `%s`.* FROM %s",
                                SparkSessionManager.atomicJSONiqItemColumnName,
                                input
                            )
                        );
            }
            JSoundDataFrame result = new JSoundDataFrame(
                    nativeQueryResult,
                    this.expression.getStaticType().getItemType()
            );
            return result;
        }

        JavaRDD<Item> rdd = getRDDAux(context);
        return ValidateTypeIterator.convertRDDToValidDataFrame(
            rdd,
            this.expression.getStaticType().getItemType(),
            context,
            true
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    @Override
    protected void openLocal() {
        this.child.open(this.currentDynamicContextForLocalExecution);
        this.tupleContext = new DynamicContext(this.currentDynamicContextForLocalExecution); // assign current context
        // as parent
        setNextResult();
    }

    private void setNextResult() {
        if (this.expression.isOpen()) {
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        while (this.child.hasNext()) {
            FlworTuple tuple = this.child.next();
            this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
            this.tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                              // from new tuple

            this.expression.open(this.tupleContext);
            boolean isResultSet = setResultFromExpression();
            if (isResultSet) {
                return;
            }
        }

        // execution reaches here when there are no more results
        this.hasNext = false;
    }

    /**
     * expression has to be open prior to call.
     *
     * @return true if nextResult is set and hasNext is true, false otherwise
     */
    private boolean setResultFromExpression() {
        if (this.expression.hasNext()) { // if expression returns a value, set it as next
            this.nextResult = this.expression.next();
            this.hasNext = true;
            return true;
        } else { // if not, keep iterating
            this.expression.close();
            return false;
        }
    }

    @Override
    protected void closeLocal() {
        this.child.close();
        if (this.expression.isOpen()) {
            this.expression.close();
        }
    }

    @Override
    protected void resetLocal() {
        this.child.reset(this.currentDynamicContextForLocalExecution);
        if (this.expression.isOpen()) {
            this.expression.close();
        }
        this.tupleContext = new DynamicContext(this.currentDynamicContextForLocalExecution); // assign current context
        setNextResult();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.expression.getVariableDependencies());
        for (Name variable : this.child.getOutputTupleVariableNames()) {
            result.remove(variable);
        }
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | ");
        buffer.append(getHighestExecutionMode());
        buffer.append(" | ");

        buffer.append("Variable dependencies: ");
        Map<Name, DynamicContext.VariableDependency> dependencies = getVariableDependencies();
        for (Name v : dependencies.keySet()) {
            buffer.append(v + "(" + dependencies.get(v) + ")" + " ");
        }
        buffer.append("\n");

        this.child.print(buffer, indent + 1);
        this.expression.print(buffer, indent + 1);
    }

    private void readObject(ObjectInputStream i) throws ClassNotFoundException, IOException {
        i.defaultReadObject();
        setInputAndOutputTupleVariableDependencies();
    }

    private void writeObject(ObjectOutputStream i) throws IOException {
        i.defaultWriteObject();
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
        String input = FlworDataFrameUtils.createTempView(dataFrame);
        NativeClauseContext letContext = new NativeClauseContext(FLWOR_CLAUSES.RETURN, inputSchema, context);
        letContext.setView(input);
        NativeClauseContext nativeQuery = iterator.generateNativeQuery(letContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            return null;
        }
        String queryString = String.format(
            "select %s as `%s` from (%s)",
            SequenceType.Arity.OneOrMore.isSubtypeOf(nativeQuery.getResultingType().getArity())
                ? "explode(" + nativeQuery.getResultingQuery() + ")"
                : nativeQuery.getResultingQuery(),
            SparkSessionManager.atomicJSONiqItemColumnName,
            nativeQuery.getView()
        );
        if (
            nativeQuery.getResultingType().getArity() == SequenceType.Arity.OneOrZero
                || nativeQuery.getResultingType().getArity() == SequenceType.Arity.ZeroOrMore
        ) {
            queryString = String.format(
                "select `%s` from (%s) where `%s` is not null",
                SparkSessionManager.atomicJSONiqItemColumnName,
                queryString,
                SparkSessionManager.atomicJSONiqItemColumnName
            );
        }
        LogManager.getLogger("ReturnClauseSparkIterator")
            .info(
                "Rumble was able to optimize a return clause to a native SQL query: "
                    + queryString
            );
        return dataFrame.sparkSession().sql(queryString);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (nativeClauseContext.getView() == null) {
            return NativeClauseContext.NoNativeQuery;
        }
        String rowIdField = nativeClauseContext.addVariable().toString();
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            (StructType) nativeClauseContext.getSchema(),
            null,
            null,
            null
        );
        // add an id column to get the initial dataframe back
        NativeClauseContext subQueryContext = nativeClauseContext.createChild();
        subQueryContext.setView(
            String.format(
                "select %s monotonically_increasing_id() as `%s` from (%s)",
                FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
                rowIdField,
                nativeClauseContext.getView()
            )
        );
        // update schema
        subQueryContext.setSchema(
            ((StructType) subQueryContext.getSchema()).add(
                rowIdField,
                DataTypes.IntegerType
            )
        );
        subQueryContext.setRowId(rowIdField);
        // get child query
        NativeClauseContext childContext = this.child.generateNativeQuery(subQueryContext);
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        // get expression
        childContext.setClauseType(FLWOR_CLAUSES.RETURN);
        NativeClauseContext expressionContext = this.expression.generateNativeQuery(childContext);
        if (expressionContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultColumnName = expressionContext.addVariable().toString();
        String resultingQuery;
        // if there are conditional columns, use "if(condition,then,else)"
        if (childContext.getConditionalColumns().isEmpty()) {
            resultingQuery = String.format(
                "select %s%s (%s) as `%s` from (%s)",
                FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
                childContext.isExplodedView() ? " `" + rowIdField + "`," : "",
                expressionContext.getResultingQuery(),
                resultColumnName,
                expressionContext.getView()
            );
        } else {
            String condition = childContext.getConditionalColumns()
                .stream()
                .map(name -> "`" + name + "`")
                .collect(Collectors.joining(" and "));
            resultingQuery = String.format(
                "select %s%s%s (if(%s, %s, null)) as `%s` from (%s)",
                FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
                childContext.isExplodedView() ? " `" + rowIdField + "`," : "",
                childContext.isExplodedView() && childContext.getSortingColumns().size() > 0
                    ? childContext.getSortingColumns()
                        .keySet()
                        .stream()
                        .map(key -> "`" + key + "`")
                        .collect(Collectors.joining(","))
                        + ","
                    : "",
                condition,
                expressionContext.getResultingQuery(),
                resultColumnName,
                expressionContext.getView()
            );
        }
        SequenceType resultType;
        if (childContext.isExplodedView()) {
            if (childContext.getSortingColumns().size() == 0) {
                // if the resulting expression is already a sequence type, then create one sequence from it
                String collectingString = expressionContext.getResultingType()
                    .getArity() == SequenceType.Arity.ZeroOrMore
                        ? "flatten(collect_list(`" + resultColumnName + "`))"
                        : "collect_list(`" + resultColumnName + "`)";
                resultingQuery = String.format(
                    "select %s, first(`%s`) as `%s`, %s as `%s.sequence` from (%s) group by `%s`",
                    allColumns.stream()
                        .map(
                            name -> String.format(
                                "first(%s) as %s",
                                name,
                                name
                            )
                        )
                        .collect(Collectors.joining(",")),
                    rowIdField,
                    rowIdField,
                    collectingString,
                    resultColumnName,
                    resultingQuery,
                    rowIdField
                );
            } else {
                String collectingString = expressionContext.getResultingType()
                    .getArity() == SequenceType.Arity.ZeroOrMore
                        ? "flatten(collect_list(`" + resultColumnName + "`))"
                        : "collect_list(`" + resultColumnName + "`)";
                // group by doesn't keep the order, because of this first partition by the row ID to collect the list,
                // then do group by row ID
                collectingString = String.format(
                    "%s over (partition by `%s` order by %s) as `%s`",
                    collectingString,
                    rowIdField,
                    childContext.getSortingColumns()
                        .entrySet()
                        .stream()
                        .map(entry -> String.format("`%s` %s", entry.getKey(), entry.getValue() ? "desc" : "asc"))
                        .collect(Collectors.joining(",")),
                    resultColumnName
                );
                resultingQuery = String.format(
                    "select %s %s, `%s` from (%s)",
                    FlworDataFrameUtils.getSQLColumnProjection(allColumns, true),
                    collectingString,
                    rowIdField,
                    resultingQuery
                );
                resultingQuery = String.format(
                    "select %s, last(`%s`) as `%s`, last(`%s`) as `%s.sequence` from (%s) group by `%s`",
                    allColumns.stream()
                        .map(
                            name -> String.format(
                                "last(%s) as %s",
                                name,
                                name
                            )
                        )
                        .collect(Collectors.joining(",")),
                    rowIdField,
                    rowIdField,
                    resultColumnName,
                    resultColumnName,
                    resultingQuery,
                    rowIdField
                );
            }
            resultColumnName = resultColumnName + ".sequence";
            resultingQuery = String.format(
                "select %s, `%s` from (%s) order by `%s`",
                allColumns.stream()
                    .map(FlworDataFrameColumn::toString)
                    .collect(Collectors.joining(",")),
                resultColumnName,
                resultingQuery,
                rowIdField
            );

            resultType = new SequenceType(
                    expressionContext.getResultingType().getItemType(),
                    expressionContext.getResultingType().getArity() == SequenceType.Arity.One
                        ? SequenceType.Arity.OneOrMore
                        : SequenceType.Arity.ZeroOrMore
            );
        } else {
            resultType = expressionContext.getResultingType();
        }
        nativeClauseContext.setSchema(
            ((StructType) nativeClauseContext.getSchema()).add(
                resultColumnName,
                TypeMappings.getDataFrameDataTypeFromItemType(expressionContext.getResultingType().getItemType())
            )
        );
        nativeClauseContext.setView(resultingQuery);
        resultColumnName = "`" + resultColumnName + "`";
        return new NativeClauseContext(nativeClauseContext, resultColumnName, resultType);
    }

    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        PendingUpdateList result = new PendingUpdateList();

        if (!this.expression.isRDDOrDataFrame()) {
            this.child.open(context);
            this.tupleContext = new DynamicContext(context); // assign current context

            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                this.tupleContext.getVariableValues().removeAllVariables(); // clear the previous variables
                this.tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new
                                                                                                  // variables
                // from new tuple
                result.mergeUpdates(this.expression.getPendingUpdateList(this.tupleContext), this.getMetadata());

            }
            this.child.close();
            return result;

            // execution reaches here when there are no more results
        }

        RuntimeIterator expression = this.children.get(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            // context
            this.child.open(context);
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                // from new tuple

                PendingUpdateList intermediateResult = this.expression.getPendingUpdateList(dynamicContext);
                result.mergeUpdates(intermediateResult, this.getMetadata());
            }
        }
        return result;
    }
}
