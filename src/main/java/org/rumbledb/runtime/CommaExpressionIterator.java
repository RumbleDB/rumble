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

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.ConcatLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import org.rumbledb.runtime.update.PendingUpdateList;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommaExpressionIterator extends HybridRuntimeIterator
        implements
            UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public CommaExpressionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> childIterators,
            RuntimeStaticContext staticContext
    ) {
        super(childIterators, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ConcatLocalCursor<>(getChildren(), context, getMetadata());
    }

    public List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> getOperands() {
        // This method is currently used in SequenceLookupIterator and ObjectConstructorRuntimeIterator
        // Because getChildren is protected and not visible from there
        return getChildren();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (!this.getChildren().isEmpty()) {
            int childIndex = 0;
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> currentChild = this.getChild(childIndex);

            JavaRDD<Item> childRDD = currentChild.getRDD(dynamicContext);
            childIndex++;

            while (childIndex < this.getChildren().size()) {
                currentChild = this.getChild(childIndex);
                JavaRDD<Item> nextChildRDD = currentChild.getRDD(dynamicContext);
                childRDD = childRDD.union(nextChildRDD);
                childIndex++;
            }
            return childRDD;
        } else {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            return sparkContext.emptyRDD();
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        List<NativeClauseContext> childClauses = new ArrayList<>();
        for (org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator : this.getChildren()) {
            NativeClauseContext childContext = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
                iterator,
                nativeClauseContext
            );
            if (childContext == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }
            childClauses.add(childContext);
            nativeClauseContext = new NativeClauseContext(childContext, null, null);
        }
        ItemType resultType;
        if (
            childClauses.stream()
                .allMatch(childClause -> childClause.getResultingType().getItemType().isObjectItemType())
        ) {
            // all keys and types must be equal
            resultType = childClauses.stream()
                .map(childClause -> childClause.getResultingType().getItemType())
                .reduce(
                    (a, b) -> (a.isObjectItemType()
                        && a.getObjectKeysFacet().size() == b.getObjectKeysFacet().size()
                        && a.getObjectKeysFacet()
                            .stream()
                            .allMatch(
                                key -> b.getObjectKeysFacet().contains(key)
                                    && a.getObjectContentFacet(key)
                                        .getType()
                                        .equals(b.getObjectContentFacet(key).getType())
                            ))
                                ? a
                                : BuiltinTypesCatalogue.item
                )
                .orElse(BuiltinTypesCatalogue.item);
        } else {
            resultType = childClauses.stream()
                .map(childClause -> childClause.getResultingType().getItemType())
                .reduce((a, b) -> a.equals(b) ? a : BuiltinTypesCatalogue.item)
                .orElse(BuiltinTypesCatalogue.item);
        }
        if (BuiltinTypesCatalogue.item.equals(resultType)) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingString;
        // if a child is already a sequence, use concat to merge the sequences
        if (
            childClauses.stream()
                .anyMatch(child -> SequenceType.Arity.OneOrMore.isSubtypeOf(child.getResultingType().getArity()))
        ) {
            resultingString = childClauses.stream()
                .map(
                    child -> (SequenceType.Arity.OneOrMore.isSubtypeOf(child.getResultingType().getArity()))
                        ? child.getResultingQuery()
                        : "array(" + child.getResultingQuery() + ")"
                )
                .collect(Collectors.joining(","));
            resultingString = String.format("concat(%s)", resultingString);
        } else {
            resultingString = String.format(
                "array(%s)",
                childClauses.stream()
                    .map(NativeClauseContext::getResultingQuery)
                    .collect(Collectors.joining(","))
            );
        }
        // if there is a OneOrZero, null values have to be filtered out
        SequenceType.Arity resultingArity = childClauses.stream()
            .anyMatch(childClause -> childClause.getResultingType().getArity() == SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.ZeroOrMore
                : SequenceType.Arity.OneOrMore;
        return new NativeClauseContext(
                nativeClauseContext,
                resultingString,
                new SequenceType(resultType, resultingArity)
        );
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }

        PendingUpdateList pul = new PendingUpdateList();
        for (org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> child : this.getChildren()) {
            pul.mergeUpdates(
                org.rumbledb.runtime.plan.UpdatingRuntimePlan.get(child, context),
                this.getRuntimeStaticContext().getMetadata()
            );
        }
        return pul;
    }
}
