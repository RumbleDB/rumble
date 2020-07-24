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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.udfs.LetClauseUDF;

import sparksoniq.jsoniq.tuple.FlworTuple;

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
    private Map<Name, DynamicContext.VariableDependency> dependencies;

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
        this.dependencies = this.assignmentIterator.getVariableDependencies();
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
                throw new JobWithinAJobException(
                        "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata()
                );
            }

            StructType inputSchema = df.schema();

            int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames()).indexOf(this.variableName.toString());

            List<String> allColumns = FlworDataFrameUtils.getColumnNames(
                inputSchema,
                duplicateVariableIndex,
                parentProjection
            );
            Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
                inputSchema,
                -1,
                this.dependencies
            );

            df.sparkSession()
                .udf()
                .register(
                    "letClauseUDF",
                    new LetClauseUDF(this.assignmentIterator, context, UDFcolumnsByType),
                    DataTypes.BinaryType
                );

            String selectSQL = FlworDataFrameUtils.getSQL(allColumns, true);
            String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

            df.createOrReplaceTempView("input");
            df = df.sparkSession()
                .sql(
                    String.format(
                        "select %s letClauseUDF(%s) as `%s` from input",
                        selectSQL,
                        UDFParameters,
                        this.variableName
                    )
                );
            return df;
        }
        throw new RuntimeException(
                "Unexpected program state reached. Initial let clauses are always locally executed."
        );
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
}
