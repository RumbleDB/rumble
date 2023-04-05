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
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrame;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.flwor.udfs.LongSerializeUDF;

import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CountClauseSparkIterator extends RuntimeTupleIterator {

    private static final long serialVersionUID = 1L;
    private Name variableName;
    private FlworTuple nextLocalTupleResult;
    private int currentCountIndex;

    public CountClauseSparkIterator(
            RuntimeTupleIterator child,
            Name variableName,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.currentCountIndex = 1; // indices start at 1 in JSONiq
    }

    public Name getVariableName() {
        return this.variableName;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this.child != null) {
            this.child.open(this.currentDynamicContext);

            setNextLocalTupleResult();
        } else {
            throw new OurBadException("Invalid count clause.");
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        if (this.child != null) {
            this.child.reset(this.currentDynamicContext);

            setNextLocalTupleResult();
        } else {
            throw new OurBadException("Invalid count clause.");
        }
    }

    @Override
    public void close() {
        super.close();
        this.currentCountIndex = 1;
    }

    @Override
    public FlworTuple next() {
        if (this.hasNext) {
            FlworTuple result = this.nextLocalTupleResult; // save the result to be returned
            setNextLocalTupleResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in count flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        if (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();

            List<Item> results = new ArrayList<>();
            results.add(ItemFactory.getInstance().createIntItem(this.currentCountIndex++));

            this.nextLocalTupleResult = new FlworTuple(inputTuple).putValue(this.variableName, results);
            this.hasNext = true;
        } else {
            this.child.close();
            this.hasNext = false;
        }
    }

    @Override
    public FlworDataFrame getDataFrame(
            DynamicContext context
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid count clause.");
        }
        Dataset<Row> df = this.child.getDataFrame(context).getDataFrame();
        if (!this.outputTupleProjection.containsKey(this.variableName)) {
            return new FlworDataFrame(df);
        }

        Dataset<Row> dfWithIndex = addSerializedCountColumn(df, this.outputTupleProjection, this.variableName);
        return new FlworDataFrame(dfWithIndex);
    }

    // This method, which implements count semantics, is also intended for use by other clauses (e.g., for clause with
    // positional variables).
    public static Dataset<Row> addSerializedCountColumn(
            Dataset<Row> df,
            Map<Name, DynamicContext.VariableDependency> outputDependencies,
            Name variableName
    ) {
        StructType inputSchema = df.schema();

        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            inputSchema,
            outputDependencies,
            null,
            Collections.singletonList(variableName)
        );

        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);

        Dataset<Row> dfWithIndex = FlworDataFrameUtils.zipWithIndex(df, 1L, variableName.toString());

        df.sparkSession()
            .udf()
            .register(
                "serializeCountIndex",
                new LongSerializeUDF(),
                DataTypes.BinaryType
            );

        String viewName = FlworDataFrameUtils.createTempView(dfWithIndex);
        dfWithIndex = dfWithIndex.sparkSession()
            .sql(
                String.format(
                    "select %s serializeCountIndex(`%s`) as `%s` from %s",
                    selectSQL,
                    variableName,
                    variableName,
                    viewName
                )
            );
        return dfWithIndex;
    }

    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        result.addAll(this.child.getOutputTupleVariableNames());
        result.add(this.variableName);
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (int i = 0; i < indent + 1; ++i) {
            buffer.append("  ");
        }
        buffer.append("Variable ").append(this.variableName);
        buffer.append("\n");
    }

    public Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        // start with an empty projection.
        Map<Name, DynamicContext.VariableDependency> projection =
            new TreeMap<Name, DynamicContext.VariableDependency>();

        // copy over the projection needed by the parent clause.
        projection.putAll(parentProjection);

        // remove the variable that this clause binds.
        projection.remove(this.variableName);
        return projection;
    }

    public boolean containsClause(FLWOR_CLAUSES kind) {
        if (kind == FLWOR_CLAUSES.COUNT) {
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

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.child == null) {
            throw new OurBadException("Invalid count clause.");
        }
        NativeClauseContext childContext = this.child.generateNativeQuery(nativeClauseContext);
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        List<FlworDataFrameColumn> allColumns = FlworDataFrameUtils.getColumns(
            (StructType) childContext.getSchema(),
            null,
            null,
            null
        );
        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, true);
        String variableName = childContext.addVariable(this.variableName).toString();
        String resultingQuery;
        if (childContext.isExplodedView()) {
            Map<String, Boolean> sortingColumns = childContext.getSortingColumns().isEmpty()
                ? Collections.singletonMap(childContext.getPositionalVariableName().toString(), false)
                : childContext.getSortingColumns();
            String aggregateString;
            if (childContext.getConditionalColumns().size() > 0) {
                String condition = childContext.getConditionalColumns()
                    .stream()
                    .map(name -> "`" + name + "`")
                    .collect(Collectors.joining(" and "));
                aggregateString = String.format(
                    "if((%s) ,`%s`, null)",
                    condition,
                    childContext.getRowIdField()
                );
            } else {
                aggregateString = String.format("`%s`", childContext.getRowIdField());
            }
            resultingQuery = String.format(
                "select %s count(%s) over (partition by `%s` order by %s) as `%s` from (%s)",
                selectSQL,
                aggregateString,
                childContext.getRowIdField(),
                sortingColumns.entrySet()
                    .stream()
                    .map(entry -> String.format("`%s` %s", entry.getKey(), entry.getValue() ? "desc" : "asc"))
                    .collect(Collectors.joining(",")),
                variableName,
                childContext.getView()
            );
        } else {
            resultingQuery = String.format(
                "select %s 1 as `%s` from (%s)",
                selectSQL,
                variableName,
                childContext.getView()
            );
        }
        childContext.setSchema(
            ((StructType) childContext.getSchema()).add(
                variableName,
                DataTypes.IntegerType
            )
        );
        childContext.setView(resultingQuery);
        return new NativeClauseContext(childContext, null, null);
    }
}
