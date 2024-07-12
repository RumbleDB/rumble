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
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;

import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import org.rumbledb.runtime.update.PendingUpdateList;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommaExpressionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator currentChild;
    private Item nextResult;
    private int childIndex;


    public CommaExpressionIterator(
            List<RuntimeIterator> childIterators,
            boolean isUpdating,
            RuntimeStaticContext staticContext
    ) {
        super(childIterators, staticContext);
        this.isUpdating = isUpdating;
    }

    public CommaExpressionIterator(
            List<RuntimeIterator> childIterators,
            RuntimeStaticContext staticContext
    ) {
        this(childIterators, false, staticContext);
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Comma expression", getMetadata());
    }

    private void startLocal() {
        this.childIndex = 0;

        if (this.children.size() >= 1) {
            this.currentChild = this.children.get(this.childIndex);
            this.currentChild.open(this.currentDynamicContextForLocalExecution);
        } else {
            this.currentChild = null;
        }

        setNextResult();
    }

    @Override
    public void openLocal() {
        startLocal();
    }

    public void setNextResult() {
        if (this.currentChild == null) {
            this.hasNext = false;
            return;
        }

        this.nextResult = null;

        while (this.nextResult == null) {
            if (this.currentChild.hasNext()) {
                this.nextResult = this.currentChild.next();
            } else {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    this.currentChild = null;
                    break;
                }
                this.currentChild = this.children.get(this.childIndex);
                this.currentChild.open(this.currentDynamicContextForLocalExecution);
            }
        }

        this.hasNext = this.nextResult != null;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        startLocal();
    }

    @Override
    protected void closeLocal() {
        if (this.currentChild != null) {
            this.currentChild.close();
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (!this.children.isEmpty()) {
            this.childIndex = 0;
            this.currentChild = this.children.get(this.childIndex);

            JavaRDD<Item> childRDD = this.currentChild.getRDD(dynamicContext);
            this.childIndex++;

            while (this.childIndex < this.children.size()) {
                this.currentChild = this.children.get(this.childIndex);
                JavaRDD<Item> nextChildRDD = this.currentChild.getRDD(dynamicContext);
                childRDD = childRDD.union(nextChildRDD);
                this.childIndex++;
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
        for (RuntimeIterator iterator : this.children) {
            NativeClauseContext childContext = iterator.generateNativeQuery(nativeClauseContext);
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
                        && a.getObjectContentFacet().keySet().size() == b.getObjectContentFacet().keySet().size()
                        && a.getObjectContentFacet()
                            .keySet()
                            .stream()
                            .allMatch(
                                key -> b.getObjectContentFacet().containsKey(key)
                                    && a.getObjectContentFacet()
                                        .get(key)
                                        .getType()
                                        .equals(b.getObjectContentFacet().get(key).getType())
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

    public List<RuntimeIterator> getChildren() {
        return this.children;
    }

    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }

        PendingUpdateList pul = new PendingUpdateList();
        for (RuntimeIterator child : children) {
            pul = PendingUpdateList.mergeUpdates(pul, child.getPendingUpdateList(context), this.getMetadata());
        }
        return pul;
    }
}
