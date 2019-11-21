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

package sparksoniq.spark.iterator.flowr;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.udf.WhereClauseUDF;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WhereClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _expression;
    private DynamicContext _tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;
    Map<String, DynamicContext.VariableDependency> _dependencies;

    public WhereClauseSparkIterator(
            RuntimeTupleIterator child,
            RuntimeIterator whereExpression,
            IteratorMetadata iteratorMetadata
    ) {
        super(child, iteratorMetadata);
        _expression = whereExpression;
        _dependencies = _expression.getVariableDependencies();
    }

    @Override
    public boolean isDataFrame() {
        return _child.isDataFrame();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this._child != null) {
            _child.open(_currentDynamicContext);
            _tupleContext = new DynamicContext(_currentDynamicContext); // assign current context as parent

            setNextLocalTupleResult();

        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (_hasNext) {
            FlworTuple result = _nextLocalTupleResult; // save the result to be returned
            setNextLocalTupleResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        // for each incoming tuple, evaluate the expression to a boolean.
        // forward if true, drop if false

        FlworTuple inputTuple;
        while (_child.hasNext()) {
            // tuple received from child, used for tuple creation
            inputTuple = _child.next();
            _tupleContext.removeAllVariables(); // clear the previous variables
            _tupleContext.setBindingsFromTuple(inputTuple, getMetadata()); // assign new variables from new tuple

            _expression.open(_tupleContext);
            boolean effectiveBooleanValue = RuntimeIterator.getEffectiveBooleanValue(_expression);
            _expression.close();
            if (effectiveBooleanValue) {
                _nextLocalTupleResult = inputTuple;
                this._hasNext = true;
                return;
            }
        }

        // execution reaches here when there are no more results
        _child.close();
        this._hasNext = false;
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
        Dataset<Row> df = _child.getDataFrame(context, getProjection(parentProjection));
        StructType inputSchema = df.schema();

        List<String> UDFcolumns = DataFrameUtils.getColumnNames(inputSchema, -1, _dependencies);
        df.sparkSession()
            .udf()
            .register(
                "whereClauseUDF",
                new WhereClauseUDF(_expression, inputSchema, UDFcolumns),
                DataTypes.BooleanType
            );

        String udfSQL = DataFrameUtils.getSQL(UDFcolumns, false);

        df.createOrReplaceTempView("input");
        df = df.sparkSession()
            .sql(
                String.format("select * from input where whereClauseUDF(array(%s)) = 'true'", udfSQL)
            );
        return df;
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(_expression.getVariableDependencies());
        for (String var : _child.getVariablesBoundInCurrentFLWORExpression()) {
            result.remove(var);
        }
        result.putAll(_child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        Set<String> result = new HashSet<String>();
        result.addAll(_child.getVariablesBoundInCurrentFLWORExpression());
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        _expression.print(buffer, indent + 1);
    }

    public Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        // start with an empty projection.
        Map<String, DynamicContext.VariableDependency> projection =
            new TreeMap<String, DynamicContext.VariableDependency>();

        // copy over the projection needed by the parent clause.
        projection.putAll(parentProjection);

        // add the variable dependencies needed by this for clause's expression.
        Map<String, DynamicContext.VariableDependency> exprDependency = _expression.getVariableDependencies();
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
