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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.closures.CountClauseClosure;
import sparksoniq.spark.udf.CountClauseSerializeUDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountClauseSparkIterator extends SparkRuntimeTupleIterator {
    private String _variableName;
    private FlworTuple _nextLocalTupleResult;
    private int _currentCountIndex;

    public CountClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator variableReference, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _variableName = ((VariableReferenceIterator) variableReference).getVariableName();
        _currentCountIndex = 1;    // indices start at 1 in JSONiq
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

            setNextLocalTupleResult();
        } else {
            throw new SparksoniqRuntimeException("Invalid count clause.");
        }
    }

    @Override
    public void close() {
        super.close();
        _currentCountIndex = 1;
    }

    @Override
    public FlworTuple next() {
        if (_hasNext == true) {
            FlworTuple result = _nextLocalTupleResult;      // save the result to be returned
            setNextLocalTupleResult();              // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in count flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        if (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>();
            results.add(new IntegerItem(_currentCountIndex++));

            FlworTuple newTuple = new FlworTuple(inputTuple, _variableName, results);
            _nextLocalTupleResult = newTuple;
            this._hasNext = true;
        } else {
            _child.close();
            this._hasNext = false;
        }
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        String variableName = _variableName;
        // zipWithIndex starts from 0, increment indices by 1 for jsoniq convention
        return _child.getRDD(context).zipWithIndex()
                .mapValues(index -> index + 1)
                .map(new CountClauseClosure(variableName, getMetadata()));
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid count clause.");
        }
        Dataset<Row> df = _child.getDataFrame(context);
        StructType inputSchema = df.schema();
        int duplicateVariableIndex = Arrays.asList(inputSchema.fieldNames()).indexOf(_variableName);
        String selectSQL = DataFrameUtils.getSQL(inputSchema, duplicateVariableIndex, true);

        Dataset<Row> dfWithIndex = DataFrameUtils.zipWithIndex(df, new Long(1), _variableName);

        df.sparkSession().udf().register("serializeCountIndex",
                new CountClauseSerializeUDF(), DataTypes.BinaryType);

        dfWithIndex.createOrReplaceTempView("input");
        dfWithIndex = dfWithIndex.sparkSession().sql(
                String.format("select %s serializeCountIndex(`%s`) as `%s` from input",
                        selectSQL, _variableName, _variableName)
        );
        return dfWithIndex;
    }
}
