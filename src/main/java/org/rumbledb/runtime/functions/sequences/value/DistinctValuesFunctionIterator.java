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
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class DistinctValuesFunctionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Item nextResult;
    private List<Item> prevResults;

    public DistinctValuesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.sequenceIterator = arguments.get(0);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new DistinctLocalCursor(
                this.sequenceIterator,
                this.getChildren().size() == 2 ? this.getChild(1) : null,
                context,
                getMetadata()
        );
    }

    private void checkCollation(DynamicContext context) {
        if (this.getChildren().size() == 2) {
            String collation = this.getChild(1)
                .materializeFirstItemOrNull(context)
                .getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new DefaultCollationException("Wrong collation parameter", getMetadata());
            }
        }
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
        checkCollation(this.currentDynamicContextForLocalExecution);
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.sequenceIterator.hasNext()) {
            Item item = this.sequenceIterator.next();
            if (!this.prevResults.contains(item)) {
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

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        checkCollation(dynamicContext);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(dynamicContext);
        return childRDD.distinct();
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        checkCollation(dynamicContext);
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

    private static final class DistinctLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator sequencePlan;
        private final RuntimeIterator collationPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private final List<Item> seen = new ArrayList<>();
        private LocalCursor<Item> sequenceCursor;
        private Item nextResult;

        private DistinctLocalCursor(
                RuntimeIterator sequencePlan,
                RuntimeIterator collationPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.collationPlan = collationPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            if (this.collationPlan != null) {
                String collation = LocalCursorUtils.materializeFirst(this.collationPlan, this.context).getStringValue();
                if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                    throw new DefaultCollationException("Wrong collation parameter", this.metadata);
                }
            }
            this.sequenceCursor = this.sequencePlan.createLocalCursor(this.context);
            advance();
        }

        private void advance() {
            this.nextResult = null;
            while (this.sequenceCursor.hasNext()) {
                Item item = this.sequenceCursor.next();
                if (!this.seen.contains(item)) {
                    this.seen.add(item);
                    this.nextResult = item;
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
