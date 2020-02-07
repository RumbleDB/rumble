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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
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

    public VariableReferenceIterator(
            String variableName,
            SequenceType seq,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this._variableName = variableName;
        this._sequence = seq;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return context.getRDDVariableValue(this._variableName, getMetadata());
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        return context.getDataFrameVariableValue(this._variableName, getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    public Item nextLocal() {
        if (!this._hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this._variableName,
                    getMetadata()
            );
        }
        Item item = this._items.get(this._currentIndex);
        this._currentIndex++;
        if (this._currentIndex == this._items.size()) {
            this._hasNext = false;
        }
        return item;
    }

    @Override
    public void openLocal() {
        this._currentIndex = 0;
        this._items = this._currentDynamicContextForLocalExecution.getLocalVariableValue(
            this._variableName,
            getMetadata()
        );
        this._hasNext = this._items.size() != 0;
    }

    @Override
    protected void closeLocal() {
        // do nothing
    }

    @Override
    public void resetLocal(DynamicContext context) {
        this._currentIndex = 0;
        this._items = null;
    }

    public SequenceType getSequence() {
        return this._sequence;
    }

    public String getVariableName() {
        return this._variableName;
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(this._variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }
}
