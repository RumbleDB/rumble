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
 * Authors: Marco Schöb
 *
 */

package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.*;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NodeAndNonNodeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

public class SlashExprIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeLocalResults(context).iterator(), getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Comparator<Item> DOCUMENT_ORDER_COMPARATOR =
            Comparator.comparing(Item::getXmlDocumentPosition, Comparator.nullsLast(Comparator.naturalOrder()));
    private ItemRuntimePlan leftIterator;
    private ItemRuntimePlan rightIterator;

    public SlashExprIterator(
            ItemRuntimePlan sequence, ItemRuntimePlan stepIterator, RuntimeStaticContext staticContext) {
        super(Arrays.asList(sequence, stepIterator), staticContext);
        this.leftIterator = sequence;
        this.rightIterator = stepIterator;
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.leftIterator.getRDD(dynamicContext);
        JavaPairRDD<Item, Long> zippedChildRDD = childRDD.zipWithIndex();
        long count = childRDD.count();

        // apply right iterator, usually a step
        FlatMapFunction<Tuple2<Item, Long>, Item> transformation =
                new SlashExprClosureZipped(this.rightIterator, dynamicContext, count);
        JavaRDD<Item> result = zippedChildRDD.flatMap(transformation);

        boolean allNodes;
        boolean allNonNodes = false;
        if (this.rightIterator instanceof StepExprIterator) {
            allNodes = true;
        } else {
            if (result.isEmpty()) return result;
            allNodes = result.map(Item::isNode).reduce(Boolean::logicalAnd);
            allNonNodes = !result.map(Item::isNode).reduce(Boolean::logicalOr);
        }

        if (allNodes) {
            if (this.getRuntimeStaticContext().getConfiguration().optimization().optimizeSteps()) {
                if (this.getRuntimeStaticContext()
                                .getConfiguration()
                                .optimization()
                                .optimizeStepsExperimental()
                        && this.getRuntimeStaticContext()
                                .getConfiguration()
                                .optimization()
                                .optimizeParentPointers()) {
                    // skip sorting and uniqueness if not needed
                    // use optimizeParent as approximation for now, this is not verified
                    return result;
                }
                // faster because we avoid shuffle for uniqueness and global sorting
                // but could theoretically violate document order over multiple calls if spark groupby order is not
                // stable

                // group by document
                JavaPairRDD<Object, Iterable<Item>> res = result.groupBy((Function<Item, Object>)
                        item -> item.getXmlDocumentPosition().getPath());
                // sort and uniqueness per document
                JavaRDD<Iterator<Item>> r2 =
                        res.map((Function<Tuple2<Object, Iterable<Item>>, Iterator<Item>>) tuple -> {
                            ArrayList<Item> l = new ArrayList<>();
                            tuple._2().iterator().forEachRemaining(l::add);
                            l = new ArrayList<>(new HashSet<>(l));
                            l.sort(DOCUMENT_ORDER_COMPARATOR);
                            return l.iterator();
                        });
                // put all documents together again
                return r2.flatMap((FlatMapFunction<Iterator<Item>, Item>) it -> it);
            } else {
                // get unique items (uses hashCode() and equals())
                JavaRDD<Item> res = result.distinct();
                // sort because spark doesnt guarantee any ordering
                return res.sortBy(Item::getXmlDocumentPosition, true, 1);
            }
        } else if (allNonNodes) {
            return result;
        } else {
            throw new NodeAndNonNodeException(
                    "A mix of nodes and non-nodes was encountered as a result of a step expression.", getMetadata());
        }
    }

    private List<Item> computeLocalResults(DynamicContext context) {
        List<Item> left = this.leftIterator.materialize(context);
        List<Item> localResults = new ArrayList<>();
        long last = left.size();
        long position = 0;
        for (Item currentItem : left) {
            DynamicContext currentContext = new DynamicContext(context);
            currentContext
                    .getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, Collections.singletonList(currentItem));
            currentContext.getVariableValues().setPosition(++position);
            currentContext.getVariableValues().setLast(last);
            localResults.addAll(this.rightIterator.materialize(currentContext));
        }
        boolean allNodes = true;
        boolean allNonNodes = true;
        for (Item current : localResults) {
            if (current.isNode()) {
                allNonNodes = false;
            } else {
                allNodes = false;
            }
        }
        if (allNodes) {
            localResults = new ArrayList<>(new LinkedHashSet<>(localResults));
            localResults.sort(DOCUMENT_ORDER_COMPARATOR);
        } else if (!allNonNodes) {
            throw new NodeAndNonNodeException(
                    "A mix of nodes and non-nodes was encountered as a result of a step expression.", getMetadata());
        }
        return localResults;
    }
}
