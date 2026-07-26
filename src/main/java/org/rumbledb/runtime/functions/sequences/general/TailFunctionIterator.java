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

package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class TailFunctionIterator extends HybridRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator iterator;
    private Item nextResult;

    public TailFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.iterator, context, getMetadata());
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tail function", getMetadata());
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);

        if (!this.iterator.hasNext()) {
            this.hasNext = false;
        } else {
            this.iterator.next(); // skip the first item
            setNextResult();
        }
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    public void setNextResult() {
        this.nextResult = null;

        if (this.iterator.hasNext()) {
            this.nextResult = this.iterator.next();
        }

        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        if (!childRDD.isEmpty()) {
            JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
            JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((input) -> input._2() != 0);
            return filteredRDD.map(x -> x._1);
        }
        return childRDD;
    }

    private static final class Cursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> childCursor;

        private Cursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.childCursor = this.childPlan.createLocalCursor(this.context);
            if (this.childCursor.hasNext()) {
                this.childCursor.next();
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.childCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!this.childCursor.hasNext()) {
                throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tail function", this.metadata);
            }
            return this.childCursor.next();
        }

        @Override
        protected void closeLocal() {
            if (this.childCursor != null) {
                this.childCursor.close();
                this.childCursor = null;
            }
        }

    }
}
