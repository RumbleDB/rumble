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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.JobWithinAJobException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.udf.LetClauseUDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LetClauseSparkIterator extends RuntimeTupleIterator {


    private static final long serialVersionUID = 1L;
    private String _variableName; // for efficient use in local iteration
    private RuntimeIterator _assignmentIterator;
    private DynamicContext _tupleContext; // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;
    private Map<String, DynamicContext.VariableDependency> _dependencies;

    public LetClauseSparkIterator(
            RuntimeTupleIterator child,
            String variableName,
            RuntimeIterator assignmentIterator,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(child, executionMode, iteratorMetadata);
        _variableName = variableName;
        _assignmentIterator = assignmentIterator;
        _dependencies = _assignmentIterator.getVariableDependencies();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (this._child == null) {
            _nextLocalTupleResult = generateTupleFromExpressionWithContext(null, _currentDynamicContext);
        } else {
            _child.open(_currentDynamicContext);
            _tupleContext = new DynamicContext(_currentDynamicContext); // assign current context as parent
            setNextLocalTupleResult();
        }
    }

    private void setNextLocalTupleResult() {
        // if starting clause: result is a single tuple -> no more tuples after the first next call
        if (this._child == null) {
            this._hasNext = false;
            return;
        }

        if (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();
            _tupleContext.removeAllVariables(); // clear the previous variables
            _tupleContext.setBindingsFromTuple(inputTuple, getMetadata()); // assign new variables from new tuple

            _nextLocalTupleResult = generateTupleFromExpressionWithContext(inputTuple, _tupleContext);
            this._hasNext = true;
        } else {
            _child.close();
            this._hasNext = false;
        }
    }

    private FlworTuple generateTupleFromExpressionWithContext(FlworTuple inputTuple, DynamicContext context) {
        FlworTuple resultTuple;
        if (inputTuple == null) {
            resultTuple = new FlworTuple();
        } else {
            resultTuple = new FlworTuple(inputTuple);
        }
        if (_assignmentIterator.isDataFrame()) {
            Dataset<Row> df = _assignmentIterator.getDataFrame(context);
            resultTuple.putValue(_variableName, df);
        } else if (_assignmentIterator.isRDD()) {
            JavaRDD<Item> itemRDD = _assignmentIterator.getRDD(context);
            resultTuple.putValue(_variableName, itemRDD);
        } else {
            List<Item> results = new ArrayList<>();
            _assignmentIterator.open(context);
            while (_assignmentIterator.hasNext()) {
                results.add(_assignmentIterator.next());
            }
            _assignmentIterator.close();
            resultTuple.putValue(_variableName, results);
        }
        return resultTuple;
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

    @Override
    public void close() {
        this._isOpen = false;
        if (_child != null) {
            _child.close();
        }
    }

    @Override
    public Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (this._child != null) {
            Dataset<Row> df = _child.getDataFrame(context, getProjection(parentProjection));

            if (_assignmentIterator.isRDD()) {
                throw new JobWithinAJobException(
                        "A let clause expression cannot produce a big sequence of items for a big number of tuples, as this would lead to a data flow explosion.",
                        getMetadata().getExpressionMetadata()
                );
            }

            StructType inputSchema = df.schema();

            int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames()).indexOf(_variableName);

            List<String> allColumns = DataFrameUtils.getColumnNames(inputSchema, duplicateVariableIndex, null);
            Map<String, List<String>> UDFcolumnsByType = DataFrameUtils.getColumnNamesByType(
                inputSchema,
                -1,
                _dependencies
            );

            df.sparkSession()
                .udf()
                .register(
                    "letClauseUDF",
                    new LetClauseUDF(_assignmentIterator, context, UDFcolumnsByType),
                    DataTypes.BinaryType
                );

            String selectSQL = DataFrameUtils.getSQL(allColumns, true);
            String UDFParameters = DataFrameUtils.getUDFParameters(UDFcolumnsByType);

            df.createOrReplaceTempView("input");
            df = df.sparkSession()
                .sql(
                    String.format(
                        "select %s letClauseUDF(%s) as `%s` from input",
                        selectSQL,
                        UDFParameters,
                        _variableName
                    )
                );
            return df;
        }
        throw new RuntimeException(
                "Unexpected program state reached. Initial let clauses are always locally executed."
        );
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<>(_assignmentIterator.getVariableDependencies());
        if (_child != null) {
            for (String var : _child.getVariablesBoundInCurrentFLWORExpression()) {
                result.remove(var);
            }
            result.putAll(_child.getVariableDependencies());
        }
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        Set<String> result = new HashSet<>();
        if (_child != null) {
            result.addAll(_child.getVariablesBoundInCurrentFLWORExpression());
        }
        result.add(_variableName);
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        super.print(buffer, indent);
        for (int i = 0; i < indent + 1; ++i) {
            buffer.append("  ");
        }
        buffer.append("Variable ").append(_variableName).append("\n");
        _assignmentIterator.print(buffer, indent + 1);
    }

    public Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    ) {
        if (_child == null) {
            return null;
        }

        // start with an empty projection.

        // copy over the projection needed by the parent clause.
        Map<String, DynamicContext.VariableDependency> projection =
            new TreeMap<>(parentProjection);

        // remove the variable that this clause binds.
        projection.remove(_variableName);

        // add the variable dependencies needed by this for clause's expression.
        Map<String, DynamicContext.VariableDependency> exprDependency = _assignmentIterator.getVariableDependencies();
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
