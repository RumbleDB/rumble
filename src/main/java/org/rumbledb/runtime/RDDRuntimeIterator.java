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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import java.io.Serial;
import java.util.List;

public abstract class RDDRuntimeIterator extends RuntimeIterator implements RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient Cursor<Item> executionCursor;

    protected RDDRuntimeIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            this.executionCursor = this.getCursor(context);
        } catch (RuntimeException exception) {
            this.executionCursor = null;
            super.close();
            throw exception;
        }
    }

    @Override
    public boolean hasNext() {
        return this.executionCursor != null && this.executionCursor.hasNext();
    }

    @Override
    public Item next() {
        if (this.executionCursor == null) {
            throw new IteratorFlowException(
                    "Runtime iterator is not open",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }
        return this.executionCursor.next();
    }

    @Override
    public void close() {
        if (this.executionCursor != null) {
            this.executionCursor.close();
            this.executionCursor = null;
        }
        super.close();
    }

    @Override
    public final JavaRDD<Item> getNativeRDD(DynamicContext context) {
        return this.getRDDAux(context);
    }

    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("RDDs are not implemented for the iterator", getMetadata());
    }
}
