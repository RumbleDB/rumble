/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.closures.OLD_WhereClauseClosure;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import sparksoniq.spark.udf.WhereClauseUDF;

public class WhereClauseSparkIterator extends SparkRuntimeTupleIterator {

    private RuntimeIterator _expression;
    private DynamicContext _tupleContext;   // re-use same DynamicContext object for efficiency
    private FlworTuple _nextLocalTupleResult;
    private FlworTuple _inputTuple;     // tuple received from child, used for tuple creation
    Map<String, RuntimeIterator.VariableDependency> _dependencies;

    public WhereClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator whereExpression, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _expression = whereExpression;
        _dependencies = _expression.getVariableDependencies();
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public boolean isDataFrame() {
        return _child.isDataFrame();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);
            _tupleContext = new DynamicContext(_currentDynamicContext);     // assign current context as parent

            setNextLocalTupleResult();

        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (_hasNext == true) {
            FlworTuple result = _nextLocalTupleResult;      // save the result to be returned
            setNextLocalTupleResult();              // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        // for each incoming tuple, evaluate the expression to a boolean.
        // forward if true, drop if false

        while (_child.hasNext()) {
            _inputTuple = _child.next();
            _tupleContext.removeAllVariables();             // clear the previous variables
            _tupleContext.setBindingsFromTuple(_inputTuple);      // assign new variables from new tuple

            _expression.open(_tupleContext);
            boolean effectiveBooleanValue = RuntimeIterator.getEffectiveBooleanValue(_expression);
            _expression.close();
            if (effectiveBooleanValue) {
                _nextLocalTupleResult = _inputTuple;
                this._hasNext = true;
                return;
            }
        }

        // execution reaches here when there are no more results
        _child.close();
        this._hasNext = false;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
        this._rdd = _child.getRDD(context);
        this._rdd = this._rdd.filter(new OLD_WhereClauseClosure(_expression));
        return _rdd;

    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
        Dataset<Row> df = _child.getDataFrame(context);
        StructType inputSchema = df.schema();

        List<String> UDFcolumns = DataFrameUtils.getColumnNames(inputSchema, -1, _dependencies);


        df.sparkSession().udf().register("whereClauseUDF",
                new WhereClauseUDF(_expression, inputSchema, UDFcolumns), DataTypes.BooleanType);

        String udfSQL = DataFrameUtils.getSQL(UDFcolumns, false);

        df.createOrReplaceTempView("input");
        df = df.sparkSession().sql(
                String.format("select * from input where whereClauseUDF(array(%s)) = 'true'", udfSQL)
        );
        return df;
    }

    public Map<String, RuntimeIterator.VariableDependency> getVariableDependencies()
    {
        Map<String, RuntimeIterator.VariableDependency> result = new TreeMap<String, RuntimeIterator.VariableDependency>();
        result.putAll(_expression.getVariableDependencies());
        for (String var : _child.getVariablesBoundInCurrentFLWORExpression())
        {
            result.remove(var);
        }
        result.putAll(_child.getVariableDependencies());
        return result;
    }

    public Set<String> getVariablesBoundInCurrentFLWORExpression()
    {
        Set<String> result = new HashSet<String>();
        result.addAll(_child.getVariablesBoundInCurrentFLWORExpression());
        return result;
    }
    
    public void print(StringBuffer buffer, int indent)
    {
        super.print(buffer,  indent);
        _expression.print(buffer, indent + 1);
    }
    
    public void setParentDependencies(Map<String, RuntimeIterator.VariableDependency> parentDependencies)
    {
        _parentDependencies = parentDependencies;
        
        // passing dependencies to parent
        Map<String, RuntimeIterator.VariableDependency> recursiveDependencies = new TreeMap<String, RuntimeIterator.VariableDependency>();
        recursiveDependencies.putAll(parentDependencies);
        
        Map<String, RuntimeIterator.VariableDependency> exprDependency = _expression.getVariableDependencies();
        for(String k : exprDependency.keySet())
        {
            if(recursiveDependencies.containsKey(k)) {
                if(recursiveDependencies.get(k) != exprDependency.get(k))
                {
                    recursiveDependencies.put(k, RuntimeIterator.VariableDependency.FULL);
                }
            } else {
                recursiveDependencies.put(k, exprDependency.get(k));
            }
        }
        _child.setParentDependencies(recursiveDependencies);
    }
}
