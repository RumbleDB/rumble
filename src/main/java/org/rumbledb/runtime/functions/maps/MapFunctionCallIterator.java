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
 */

package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Dynamic function call when the function item is an XDM map ({@code $map($key)}), equivalent to {@code map:get}.
 */
public class MapFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final Item mapItem;
    private final RuntimeIterator keyIterator;
    private Queue<Item> pendingResults;

    public MapFunctionCallIterator(
            Item mapItem,
            RuntimeIterator keyIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            keyIterator == null ? null : java.util.Collections.singletonList(keyIterator),
            staticContext
        );
        this.mapItem = mapItem;
        this.keyIterator = keyIterator;
        this.pendingResults = new LinkedList<>();
    }

    @Override
    protected void openLocal() {
        if (this.keyIterator == null) {
            throw new UnexpectedTypeException(
                    "Map function calls must have exactly one argument.",
                    getMetadata()
            );
        }
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void initializeResults(DynamicContext context) {
        this.pendingResults.clear();
        List<Item> rawKey = new ArrayList<>();
        this.keyIterator.materialize(context, rawKey);
        List<Item> atomized = new ArrayList<>();
        for (Item it : rawKey) {
            atomized.addAll(it.atomizedValue());
        }
        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map lookup key must atomize to a single atomic value [err:XPTY0004].",
                    getMetadata()
            );
        }
        Item key = atomized.get(0);
        List<Item> seq = this.mapItem.getSequenceByKey(key);
        if (seq != null) {
            this.pendingResults.addAll(seq);
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.pendingResults.remove();
        setNextResult();
        return result;
    }

    private void setNextResult() {
        this.hasNext = !this.pendingResults.isEmpty();
    }

    @Override
    protected void resetLocal() {
        if (this.keyIterator != null) {
            this.keyIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        this.pendingResults.clear();
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.keyIterator != null && this.keyIterator.isOpen()) {
            this.keyIterator.close();
        }
        this.pendingResults.clear();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("Map function calls are currently supported only in local execution mode.");
    }

    @Override
    public boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("Map function calls are currently supported only in local execution mode.");
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
