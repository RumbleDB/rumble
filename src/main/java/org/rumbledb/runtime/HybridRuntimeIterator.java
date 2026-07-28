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

package org.rumbledb.runtime;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanConversions;

import java.io.Serial;
import java.util.List;

public abstract class HybridRuntimeIterator extends RuntimeIterator implements RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    protected List<Item> result = null;
    private int currentResultIndex = 0;
    private transient LocalCursor<Item> localCursor;

    protected HybridRuntimeIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (isLocal()) {
            try {
                this.localCursor = createLocalCursor(context);
            } catch (RuntimeException exception) {
                this.localCursor = null;
                super.close();
                throw exception;
            }
        }
    }

    @Override
    public void close() {
        super.close();
        if (this.localCursor != null) {
            this.localCursor.close();
            this.localCursor = null;
            return;
        }
        this.result = null;
    }

    @Override
    public boolean hasNext() {
        if (isLocal()) {
            return this.localCursor != null && this.localCursor.hasNext();
        }
        if (this.result == null) {
            this.currentResultIndex = 0;
            JavaRDD<Item> rdd = this.getRDD(this.currentDynamicContextForLocalExecution);
            this.result = RuntimePlanConversions.collectRDDWithLimit(
                rdd,
                this.getConfiguration(),
                this.getMetadata()
            );
            this.hasNext = !this.result.isEmpty();
        }
        return this.hasNext;
    }

    @Override
    public Item next() {
        if (isLocal()) {
            if (this.localCursor == null) {
                throw new IteratorFlowException("Runtime iterator is not open", getMetadata());
            }
            return this.localCursor.next();
        }
        if (!this.isOpen) {
            throw new IteratorFlowException("Runtime iterator is not open", getMetadata());
        }

        if (!(this.currentResultIndex <= this.result.size() - 1)) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.getClass().getSimpleName(),
                    getMetadata()
            );
        }
        if (this.currentResultIndex == this.result.size() - 1) {
            this.hasNext = false;
        }

        Item item = this.result.get(this.currentResultIndex);
        this.currentResultIndex++;
        return item;
    }


    @Override
    public final JavaRDD<Item> getNativeRDD(DynamicContext context) {
        return getRDDAux(context);
    }

    public static JavaRDD<Item> dataFrameToRDDOfItems(JSoundDataFrame df, ExceptionMetadata metadata) {
        return df.toRDD(metadata);
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext context
    ) {
        if (!isRDDOrDataFrame()) {
            return super.materializeFirstItemOrNull(context);
        }
        JavaRDD<Item> items = this.getRDD(context);
        List<Item> collectedItems = items.take(1);
        if (collectedItems.size() == 1) {
            return collectedItems.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Item materializeExactlyOneItem(
            DynamicContext context
    )
            throws NoItemException,
                MoreThanOneItemException {
        if (!isRDDOrDataFrame()) {
            return super.materializeExactlyOneItem(context);
        }
        JavaRDD<Item> items = this.getRDD(context);
        List<Item> collectedItems = items.take(2);
        if (collectedItems.size() == 1) {
            return collectedItems.get(0);
        }
        if (collectedItems.size() == 0) {
            throw new NoItemException();
        }
        throw new MoreThanOneItemException();
    }

    @Override
    public Item materializeAtMostOneItemOrNull(
            DynamicContext context
    )
            throws MoreThanOneItemException {
        if (!isRDDOrDataFrame()) {
            return super.materializeAtMostOneItemOrNull(context);
        }
        JavaRDD<Item> items = this.getRDD(context);
        List<Item> collectedItems = items.take(2);
        if (collectedItems.size() == 1) {
            return collectedItems.get(0);
        }
        if (collectedItems.size() == 0) {
            return null;
        }
        throw new MoreThanOneItemException();
    }

    protected abstract JavaRDD<Item> getRDDAux(DynamicContext context);

    /**
     * Compatibility implementation for callers still using the legacy iterator lifecycle.
     * Cursor-native subclasses need no local lifecycle overrides.
     */
    protected void openLocal() {
        this.localCursor = createLocalCursor(this.currentDynamicContextForLocalExecution);
    }

    protected void closeLocal() {
        if (this.localCursor != null) {
            this.localCursor.close();
            this.localCursor = null;
        }
    }

    protected boolean hasNextLocal() {
        return this.localCursor.hasNext();
    }

    protected Item nextLocal() {
        return this.localCursor.next();
    }
}
