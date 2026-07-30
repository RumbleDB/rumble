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
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanConversions;

import java.io.Serial;
import java.util.List;

public abstract class HybridRuntimeIterator extends RuntimeIterator
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    protected List<Item> result = null;
    private int currentResultIndex = 0;
    private transient Cursor<Item> localCursor;

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
                this.localCursor = createNativeCursor(context);
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

    public static JavaRDD<Item> dataFrameToRDDOfItems(HomogeneousItemDataFrame df, ExceptionMetadata metadata) {
        return df.toRDD(metadata);
    }

    protected abstract JavaRDD<Item> getRDDAux(DynamicContext context);

}
