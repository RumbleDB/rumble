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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * W3C XPath/XQuery {@code map:keys}:
 * <ul>
 * <li>requires exactly one map argument</li>
 * <li>returns the atomic keys present in the map</li>
 * </ul>
 *
 * This built-in is local execution only (consistent with map/array accessors).
 */
public class MapKeysFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final Queue<Item> pendingResults;

    public MapKeysFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("map:keys must have exactly one argument.");
        }
        this.mapIterator = arguments.get(0);
        this.pendingResults = new LinkedList<>();
    }

    @Override
    protected void openLocal() {
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void initializeResults(DynamicContext context) {
        this.pendingResults.clear();

        Item mapItem;
        try {
            mapItem = this.mapIterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "map:keys expects exactly one map argument.",
                    getMetadata()
            );
        }

        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to map:keys must be a map.",
                    getMetadata()
            );
        }

        // MapItem already enforces distinct atomic keys (via op:same-key) during construction/merge.
        for (Item key : mapItem.getItemKeys()) {
            this.pendingResults.add(key);
        }
    }

    private void setNextResult() {
        this.hasNext = !this.pendingResults.isEmpty();
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

    @Override
    protected void resetLocal() {
        this.mapIterator.reset(this.currentDynamicContextForLocalExecution);
        this.pendingResults.clear();
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.mapIterator.isOpen()) {
            this.mapIterator.close();
        }
        this.pendingResults.clear();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("map:keys is currently supported only in local execution mode.");
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:keys is currently supported only in local execution mode.");
    }
}

