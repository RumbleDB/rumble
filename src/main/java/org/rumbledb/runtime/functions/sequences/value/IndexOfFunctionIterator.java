/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.sequences.value;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.AtomicDeepEqual;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

public class IndexOfFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;
    private final ItemRuntimePlan searchIterator;

    public IndexOfFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.searchIterator = this.getChild(1);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IndexOfLocalCursor(context);
    }

    private String resolveCollation(DynamicContext context) {
        String explicitCollation = this.getChildren().size() == 3
                ? this.getChild(2).materializeFirstOrNull(context).getStringValue()
                : null;
        return CollationSupport.resolveAndCheckCollation(explicitCollation, getRuntimeStaticContext(), getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        String collation = resolveCollation(context);
        Item search = this.searchIterator.materializeFirstOrNull(context);
        boolean searchIsNaN = isSearchNaN(search);
        ExceptionMetadata metadata = getMetadata();

        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        return childRDD.zipWithIndex()
                .filter(item -> matches(item._1(), search, searchIsNaN, collation, metadata))
                .map(item -> ItemFactory.getInstance().createIntItem(item._2.intValue() + 1));
    }

    private static boolean matches(
            Item item, Item search, boolean searchIsNaN, String collation, ExceptionMetadata metadata) {
        if (!item.isAtomic()) {
            throw new NonAtomicKeyException(
                    "Invalid args. index-of can't be performed with a non-atomic in the input sequence", metadata);
        }
        if (search == null || searchIsNaN) {
            return false;
        }
        if (CollationSupport.isStringCollationType(item) && CollationSupport.isStringCollationType(search)) {
            return CollationSupport.compareStrings(item.getStringValue(), search.getStringValue(), collation, metadata)
                    == 0;
        }
        return AtomicDeepEqual.deepEqual(item, search);
    }

    private static boolean isSearchNaN(Item search) {
        return search != null && (search.isDouble() || search.isFloat()) && search.isNaN();
    }

    private final class IndexOfLocalCursor extends AbstractLocalCursor<Item> {

        private final DynamicContext context;
        private Cursor<Item> sequenceCursor;
        private Item search;
        private boolean searchIsNaN;
        private String collation;
        private Item nextResult;
        private int index;

        private IndexOfLocalCursor(DynamicContext context) {
            super(IndexOfFunctionIterator.this.getMetadata());
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.collation = IndexOfFunctionIterator.this.resolveCollation(this.context);
            this.search = IndexOfFunctionIterator.this.searchIterator.materializeFirstOrNull(this.context);
            this.searchIsNaN = isSearchNaN(this.search);
            this.sequenceCursor = IndexOfFunctionIterator.this.sequenceIterator.getCursor(this.context);
            this.index = 0;
            advance();
        }

        private void advance() {
            this.nextResult = null;
            while (this.sequenceCursor.hasNext()) {
                Item item = this.sequenceCursor.next();
                this.index++;
                if (matches(item, this.search, this.searchIsNaN, this.collation, getMetadata())) {
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
            this.collation = null;
            this.index = 0;
        }
    }
}
