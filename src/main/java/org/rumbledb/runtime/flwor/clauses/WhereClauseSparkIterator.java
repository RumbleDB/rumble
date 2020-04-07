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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.udfs.WhereClauseUDF;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WhereClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private Map<String, DynamicContext.VariableDependency> dependencies;
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
        this.dependencies = this.expression.getVariableDependencies();
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
            this.tupleContext.removeAllVariables(); // clear the previous variables
            this.tupleContext.setBindingsFromTuple(inputTuple, getMetadata()); // assign new variables from new tuple

            this.expression.open(this.tupleContext);
            boolean effectiveBooleanValue = RuntimeIterator.getEffectiveBooleanValue(this.expression);
            this.expression.close();
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
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this.child == null) {
            throw new OurBadException("Invalid where clause.");
        }

        if (this.expression.isRDD()) {
            throw new JobWithinAJobException(
                    "A where clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                    getMetadata()
            );
        }

        Dataset<Row> df = this.child.getDataFrame(context, getProjection(parentProjection));
        StructType inputSchema = df.schema();

        Map<String, List<String>> UDFcolumnsByType = FlworDataFrameUtils.getColumnNamesByType(
            inputSchema,
            -1,
            this.dependencies
        );

        df.sparkSession()
            .udf()
            .register(
                "whereClauseUDF",
                new WhereClauseUDF(this.expression, context, inputSchema, UDFcolumnsByType),
                DataTypes.BooleanType
            );

        String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumnsByType);

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

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>(
                this.expression.getVariableDependencies()
        );
        for (String var : this.child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(var);
        }
        result.putAll(this.child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        return new HashSet<>(this.child.getVariablesBoundInCurrentFLWORExpression());
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        this.expression.print(buffer, indent + 1);
    }

    public Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        // copy over the projection needed by the parent clause.
        Map<String, DynamicContext.VariableDependency> projection = new TreeMap<>(parentProjection);

        // add the variable dependencies needed by this for clause's expression.
        Map<String, DynamicContext.VariableDependency> exprDependency = this.expression.getVariableDependencies();
        for (String variable : exprDependency.keySet()) {
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
