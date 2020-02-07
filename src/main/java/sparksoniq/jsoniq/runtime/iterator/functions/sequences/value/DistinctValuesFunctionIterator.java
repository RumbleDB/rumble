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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class DistinctValuesFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _sequenceIterator;
    private Item _nextResult;
    private List<Item> _prevResults;

    public DistinctValuesFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this._sequenceIterator = arguments.get(0);
    }

    public Item nextLocal() {
        if (this._hasNext) {
            Item result = this._nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "distinct-values function", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this._sequenceIterator.reset(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this._sequenceIterator.close();
    }


    @Override
    public void openLocal() {
        this._prevResults = new ArrayList<>();
        this._sequenceIterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    public void setNextResult() {
        this._nextResult = null;

        while (this._sequenceIterator.hasNext()) {
            Item item = this._sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException(
                        "Invalid args. distinct-values can't be performed on non-atomics",
                        getMetadata()
                );
            } else {
                if (!this._prevResults.contains(item)) {
                    this._prevResults.add(item);
                    this._nextResult = item;
                    break;
                }
            }
        }

        if (this._nextResult == null) {
            this._hasNext = false;
            this._sequenceIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this._sequenceIterator.getRDD(dynamicContext);
        Function<Item, Boolean> transformation = new FilterNonAtomicClosure();
        if (childRDD.filter(transformation).isEmpty()) {
            return childRDD.distinct();
        }
        throw new NonAtomicKeyException(
                "Invalid args. distinct-values can't be performed on non-atomics",
                getMetadata()
        );
    }
}
