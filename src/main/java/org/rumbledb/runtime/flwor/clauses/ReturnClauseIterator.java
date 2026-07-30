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

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

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
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;
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
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReturnClauseIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeTupleIterator child;
    private transient DynamicContext tupleContext;
    private final RuntimeIterator expression;

    public ReturnClauseIterator(
            RuntimeTupleIterator child,
            RuntimeIterator expression,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(expression), staticContext);
        this.child = child;
        this.expression = expression;
        setInputAndOutputTupleVariableDependencies();
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ReturnLocalCursor(
                this.child,
                this.expression,
                context,
                getMetadata()
        );
    }

    private static final class ReturnLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<FlworTuple> tuplePlan;
        private final RuntimePlan<Item> expressionPlan;
        private final DynamicContext context;
        private final org.rumbledb.exceptions.ExceptionMetadata metadata;
        private Cursor<FlworTuple> tupleCursor;
        private Cursor<Item> expressionCursor;
        private DynamicContext tupleContext;
        private Item nextResult;
        private boolean hasNext;

        private ReturnLocalCursor(
                RuntimePlan<FlworTuple> tuplePlan,
                RuntimePlan<Item> expressionPlan,
                DynamicContext context,
                org.rumbledb.exceptions.ExceptionMetadata metadata
        ) {
            super(metadata);
            this.tuplePlan = tuplePlan;
            this.expressionPlan = expressionPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.tupleCursor = this.tuplePlan.getCursor(this.context);
            this.tupleContext = new DynamicContext(this.context);
            advance();
        }

        @Override
        protected boolean hasNextLocal() {
            return this.hasNext;
        }

        @Override
        protected Item nextLocal() {
            if (!this.hasNext) {
                throw new IteratorFlowException("Invalid next() call in return clause", this.metadata);
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        private void advance() {
            if (this.expressionCursor != null) {
                if (this.expressionCursor.hasNext()) {
                    this.nextResult = this.expressionCursor.next();
                    this.hasNext = true;
                    return;
                }
                this.expressionCursor.close();
                this.expressionCursor = null;
            }

            while (this.tupleCursor.hasNext()) {
                FlworTuple tuple = this.tupleCursor.next();
                this.tupleContext.getVariableValues().removeAllVariables();
                this.tupleContext.getVariableValues().setBindingsFromTuple(tuple, this.metadata);
                this.expressionCursor = this.expressionPlan.getCursor(this.tupleContext);
                if (this.expressionCursor.hasNext()) {
                    this.nextResult = this.expressionCursor.next();
                    this.hasNext = true;
                    return;
                }
                this.expressionCursor.close();
                this.expressionCursor = null;
            }

            this.nextResult = null;
            this.hasNext = false;
        }

        @Override
        protected void closeLocal() {
            if (this.expressionCursor != null) {
                this.expressionCursor.close();
                this.expressionCursor = null;
            }
            if (this.tupleCursor != null) {
                this.tupleCursor.close();
                this.tupleCursor = null;
            }
            this.tupleContext = null;
            this.nextResult = null;
            this.hasNext = false;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator expression = this.getChild(0);
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
            this.child.close();
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
        JavaRDD<Item> resultRDD = df.toJavaRDD().flatMap(new ReturnFlatMapClosure(expression, context, UDFcolumns));
        return resultRDD;
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
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext context) {
        RuntimeIterator expression = this.getChild(0);
        if (expression.isRDDOrDataFrame()) {
            if (this.child.isDataFrame())
                throw new JobWithinAJobException(
                        "A return clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            // context
            this.child.open(context);
            HomogeneousItemDataFrame result = null;
            while (this.child.hasNext()) {
                FlworTuple tuple = this.child.next();
                // We need a fresh context every time, because the evaluation of RDD is lazy.
                DynamicContext dynamicContext = new DynamicContext(context);
                dynamicContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                                                                                               // from new tuple

                HomogeneousItemDataFrame intermediateResult = this.expression.getDataFrame(dynamicContext);
                if (result == null) {
                    result = intermediateResult;
                } else {
                    result = result.union(intermediateResult);
                }
            }
            this.child.close();
            if (result == null) {
                return HomogeneousItemDataFrame.emptyDataFrame();
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
                                SparkSessionManager.nonObjectJSONiqItemColumnName,
                                input
                            )
                        );
            }
            HomogeneousItemDataFrame result = new HomogeneousItemDataFrame(
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
            true,
            this.staticContext
        );
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(this.expression.getVariableDependencies());
        for (Name variable : this.child.getOutputTupleVariableNames()) {
            result.remove(variable);
        }
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    @Override
    public void print(StringBuilder buffer, int indent) {
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

    @Serial
    private void readObject(ObjectInputStream i) throws ClassNotFoundException, IOException {
        i.defaultReadObject();
        setInputAndOutputTupleVariableDependencies();
    }

    @Serial
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
            SparkSessionManager.nonObjectJSONiqItemColumnName,
            nativeQuery.getView()
        );
        if (
            nativeQuery.getResultingType().getArity() == SequenceType.Arity.OneOrZero
                || nativeQuery.getResultingType().getArity() == SequenceType.Arity.ZeroOrMore
        ) {
            queryString = String.format(
                "select `%s` from (%s) where `%s` is not null",
                SparkSessionManager.nonObjectJSONiqItemColumnName,
                queryString,
                SparkSessionManager.nonObjectJSONiqItemColumnName
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
                TypeMappings.getDataFrameDataTypeFromItemType(
                    expressionContext.getResultingType().getItemType(),
                    this.getRuntimeStaticContext()
                )
            )
        );
        nativeClauseContext.setView(resultingQuery);
        resultColumnName = "`" + resultColumnName + "`";
        return new NativeClauseContext(nativeClauseContext, resultColumnName, resultType);
    }

    @Override
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

        RuntimeIterator expression = this.getChild(0);
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
