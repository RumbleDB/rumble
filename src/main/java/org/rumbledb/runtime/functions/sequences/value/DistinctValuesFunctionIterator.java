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

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.AtomicValueComparison;
import org.rumbledb.runtime.misc.AtomicValueComparisonKey;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.ItemType;

public class DistinctValuesFunctionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;

    public DistinctValuesFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.sequenceIterator = arguments.get(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new DistinctLocalCursor(
                this.sequenceIterator,
                this.getChildren().size() == 2 ? this.getChild(1) : null,
                context,
                getMetadata());
    }

    private String resolveCollation(DynamicContext context) {
        String explicitCollation = this.getChildren().size() == 2
                ? this.getChild(1).materializeFirstOrNull(context).getStringValue()
                : null;
        String collation = CollationSupport.resolveCollation(explicitCollation, getRuntimeStaticContext());
        CollationSupport.checkCollationSupported(collation, getMetadata());
        return collation;
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        String collation = resolveCollation(dynamicContext);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(dynamicContext);
        return childRDD.map(item -> new AtomicValueComparisonKey(
                        item, collation, this.getRuntimeStaticContext().getMetadata()))
                .distinct()
                .map(AtomicValueComparisonKey::getItem);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        JavaRDD<Item> rdd = createNativeRDD(dynamicContext);
        ItemType itemType = getStaticType().getItemType();
        if (!itemType.isCompatibleWithDataFrames(getConfiguration())) {
            itemType = TypeInferrenceUtils.inferItemTypeOfRDDItems(
                    rdd, getMetadata(), TypeInferrenceUtils.TypeMergeMode.LAX);
        }
        return ValidateTypeIterator.convertRDDToValidDataFrame(
                rdd, itemType, dynamicContext, true, getRuntimeStaticContext());
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext sequenceQuery = NativeQueryRuntimePlan.generate(this.sequenceIterator, nativeClauseContext);
        if (sequenceQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "DISTINCT( " + sequenceQuery.getResultingQuery() + " )";
        return new NativeClauseContext(sequenceQuery, resultingQuery, sequenceQuery.getResultingType());
    }

    private static final class DistinctLocalCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final ItemRuntimePlan collationPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private final List<Item> seen = new ArrayList<>();
        private Cursor<Item> sequenceCursor;
        private Item nextResult;
        private String activeCollation;

        private DistinctLocalCursor(
                ItemRuntimePlan sequencePlan,
                ItemRuntimePlan collationPlan,
                DynamicContext context,
                ExceptionMetadata metadata) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.collationPlan = collationPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            String explicitCollation = this.collationPlan == null
                    ? null
                    : this.collationPlan.materializeFirstOrNull(this.context).getStringValue();
            this.activeCollation =
                    CollationSupport.resolveCollation(explicitCollation, this.sequencePlan.getRuntimeStaticContext());
            CollationSupport.checkCollationSupported(this.activeCollation, this.metadata);
            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            advance();
        }

        private void advance() {
            this.nextResult = null;
            while (this.sequenceCursor.hasNext()) {
                Item item = this.sequenceCursor.next();
                if (!containsEquivalentValue(item)) {
                    this.seen.add(item);
                    this.nextResult = item;
                    return;
                }
            }
        }

        private boolean containsEquivalentValue(Item candidate) {
            for (Item previous : this.seen) {
                if (AtomicValueComparison.equal(previous, candidate, this.activeCollation, this.metadata)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more distinct values are available.");
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
            this.seen.clear();
            this.nextResult = null;
        }
    }
}
