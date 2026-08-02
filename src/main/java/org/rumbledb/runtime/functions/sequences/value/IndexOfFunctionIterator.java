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

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serial;
import java.util.List;

public class IndexOfFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan sequenceIterator;
    private final ItemRuntimePlan searchIterator;
    private Item search;

    public IndexOfFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.searchIterator = this.getChild(1);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IndexOfLocalCursor(
                this.sequenceIterator,
                this.searchIterator,
                this.getChildren().size() == 3 ? this.getChild(2) : null,
                context,
                getMetadata()
        );
    }

    private void checkCollation(DynamicContext context) {
        if (this.getChildren().size() == 3) {
            String collation = this.getChild(2)
                .materializeFirstOrNull(context)
                .getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
            }
        }
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        checkCollation(context);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        this.search = this.searchIterator.materializeFirstOrNull(context);

        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
        JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((item) -> item._1().equals(this.search));
        return filteredRDD.map((item) -> ItemFactory.getInstance().createIntItem(item._2.intValue() + 1));
    }

    private static final class IndexOfLocalCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final ItemRuntimePlan searchPlan;
        private final ItemRuntimePlan collationPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private Item search;
        private Item nextResult;
        private int index;

        private IndexOfLocalCursor(
                ItemRuntimePlan sequencePlan,
                ItemRuntimePlan searchPlan,
                ItemRuntimePlan collationPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.searchPlan = searchPlan;
            this.collationPlan = collationPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            if (this.collationPlan != null) {
                String collation = this.collationPlan.materializeFirstOrNull(this.context).getStringValue();
                if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                    throw new UnsupportedCollationException("Wrong collation parameter", this.metadata);
                }
            }
            this.search = this.searchPlan.materializeFirstOrNull(this.context);
            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            this.index = 0;
            advance();
        }

        private void advance() {
            this.nextResult = null;
            while (this.sequenceCursor.hasNext()) {
                Item item = this.sequenceCursor.next();
                this.index++;
                if (!item.isAtomic()) {
                    throw new NonAtomicKeyException(
                            "Invalid args. index-of can't be performed with a non-atomic in the input sequence",
                            this.metadata
                    );
                }
                boolean searchIsNaN = (this.search.isDouble() || this.search.isFloat()) && this.search.isNaN();
                if (!searchIsNaN && AtomicDeepEqual.deepEqual(item, this.search)) {
                    this.nextResult = ItemFactory.getInstance().createIntItem(this.index);
                    return;
                }
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more index-of results are available.");
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
            this.search = null;
            this.nextResult = null;
            this.index = 0;
        }
    }
}
