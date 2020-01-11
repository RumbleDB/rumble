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

package sparksoniq.jsoniq.runtime.iterator.primary;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VariableReferenceIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private SequenceType _sequence;
    private String _variableName;
    private List<Item> _items = null;
    private int _currentIndex = 0;

    public VariableReferenceIterator(String variableName, SequenceType seq, ExecutionMode executionMode, IteratorMetadata iteratorMetadata) {
        super(null, executionMode, iteratorMetadata);
        _variableName = variableName;
        _sequence = seq;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return context.getRDDVariableValue(_variableName, getMetadata());
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        return context.getDataFrameVariableValue(_variableName, getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    public Item nextLocal() {
        if (!_hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _variableName,
                    getMetadata()
            );
        }
        Item item = _items.get(_currentIndex);
        _currentIndex++;
        if (_currentIndex == _items.size()) {
            _hasNext = false;
        }
        return item;
    }

    @Override
    public void openLocal() {
        _currentIndex = 0;
        _items = _currentDynamicContextForLocalExecution.getLocalVariableValue(_variableName, getMetadata());
        _hasNext = _items.size() != 0;
    }

    @Override
    protected void closeLocal() {
        // do nothing
    }

    @Override
    public void resetLocal(DynamicContext context) {
        _currentIndex = 0;
        _items = null;
    }

    public SequenceType getSequence() {
        return _sequence;
    }

    public String getVariableName() {
        return _variableName;
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(_variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }
}
