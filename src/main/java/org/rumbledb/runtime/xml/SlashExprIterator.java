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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.*;

public class SlashExprIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private List<Item> results = null;
    private int nextResultCounter = 0;
    private Item nextResult;


    public SlashExprIterator(
            RuntimeIterator sequence,
            RuntimeIterator stepIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence, stepIterator), staticContext);
        this.leftIterator = sequence;
        this.rightIterator = stepIterator;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.leftIterator.getRDD(dynamicContext);

        // apply right iterator, could be step or predicate/sequencelookup
        FlatMapFunction<Item, Item> transformation = new SlashExprClosure(this.rightIterator, dynamicContext);
        JavaRDD<Item> result = childRDD.flatMap(transformation);

        // get unique items based on unique document position
        JavaRDD<Item> res = result.mapToPair(item -> new Tuple2<>(item.getXmlDocumentPosition(), item))
            .groupByKey()
            .map(value -> value._2.iterator().next());

        // sort because spark doesnt guarantee any ordering
        return res.sortBy(Item::getXmlDocumentPosition, true, 1);
    }

    @Override
    public void openLocal() {
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this.hasNext = false;
        this.results = null;
        this.nextResult = null;
        this.nextResultCounter = 0;
        this.leftIterator.close();
        this.rightIterator.close();
    }

    @Override
    public void resetLocal() {
        this.results = null;
        this.nextResultCounter = 0;
        setNextResult();
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item nextResult = this.nextResult;
            setNextResult();
            return nextResult;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in Slash Expression",
                getMetadata()
        );
    }

    private void setNextResult() {
        if (this.results == null) {
            List<Item> left = this.leftIterator.materialize(this.currentDynamicContextForLocalExecution);
            this.results = new ArrayList<>();
            for (Item currentItem : left) {
                DynamicContext currentContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
                currentContext.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, Collections.singletonList(currentItem));
                this.results.addAll(this.rightIterator.materialize(currentContext));
            }
            // take unique
            this.results = new ArrayList<>(new LinkedHashSet<>(this.results));
            // Sort values in document order.
            this.results.sort(Comparator.comparing(Item::getXmlDocumentPosition));
        }
        if (this.nextResultCounter < this.results.size()) {
            this.nextResult = this.results.get(this.nextResultCounter++);
        } else {
            this.hasNext = false;
        }
    }
}
