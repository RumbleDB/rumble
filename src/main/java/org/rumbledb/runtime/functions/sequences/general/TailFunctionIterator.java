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
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class TailFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan iterator;

    public TailFunctionIterator(
            List<ItemRuntimePlan> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.iterator, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        if (!childRDD.isEmpty()) {
            JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
            JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((input) -> input._2() != 0);
            return filteredRDD.map(x -> x._1);
        }
        return childRDD;
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> childCursor;

        private EvaluationCursor(
                @NonNull ItemRuntimePlan childPlan,
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
            this.childCursor = this.childPlan.getCursor(this.context);
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
                throw new IteratorFlowException(
                        IteratorFlowException.FLOW_EXCEPTION_MESSAGE + "tail function",
                        this.metadata
                );
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
