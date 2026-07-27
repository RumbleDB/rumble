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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.AtomicValueComparison;
import org.rumbledb.runtime.misc.AtomicValueComparisonKey;
import org.rumbledb.runtime.misc.CollationSupport;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class DistinctValuesFunctionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Item nextResult;
    private List<Item> prevResults;
    private String activeCollation;

    public DistinctValuesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.sequenceIterator = arguments.get(0);
    }

    private String resolveCollation(DynamicContext context) {
        String explicitCollation = null;
        if (this.getChildren().size() == 2) {
            explicitCollation = this.getChild(1)
                .materializeFirstItemOrNull(context)
                .getStringValue();
        }
        String collation = CollationSupport.resolveCollation(explicitCollation, getRuntimeStaticContext());
        CollationSupport.checkCollationSupported(collation, getMetadata());
        return collation;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "distinct-values function", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }


    @Override
    public void openLocal() {
        this.prevResults = new ArrayList<>();
        this.activeCollation = resolveCollation(this.currentDynamicContextForLocalExecution);
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.sequenceIterator.hasNext()) {
            Item item = this.sequenceIterator.next();
            if (!containsEquivalentValue(item)) {
                this.prevResults.add(item);
                this.nextResult = item;
                break;
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
        } else {
            this.hasNext = true;
        }
    }

    private boolean containsEquivalentValue(Item candidate) {
        for (Item previous : this.prevResults) {
            if (AtomicValueComparison.equal(previous, candidate, this.activeCollation, getMetadata())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        String collation = resolveCollation(dynamicContext);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(dynamicContext);
        return childRDD.map(item -> new AtomicValueComparisonKey(item, collation))
            .distinct()
            .map(AtomicValueComparisonKey::getItem);
    }

    @Override
    protected boolean implementsDataFrames() {
        // SQL DISTINCT does not implement XDM numeric promotion or NaN equality.
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        resolveCollation(dynamicContext);
        JSoundDataFrame df = this.sequenceIterator.getDataFrame(dynamicContext);
        return df.distinct();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext sequenceQuery = this.sequenceIterator.generateNativeQuery(nativeClauseContext);
        if (sequenceQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "DISTINCT( "
            + sequenceQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(sequenceQuery, resultingQuery, sequenceQuery.getResultingType());
    }
}
