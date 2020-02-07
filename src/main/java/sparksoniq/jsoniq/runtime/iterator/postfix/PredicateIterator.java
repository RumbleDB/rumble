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

package sparksoniq.jsoniq.runtime.iterator.postfix;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import scala.Tuple2;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.AndOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.NotOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.OrOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.BooleanRuntimeIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PredicateIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private RuntimeIterator filter;
    private Item nextResult;
    private long position;
    private boolean mustMaintainPosition;
    private DynamicContext filterDynamicContext;


    public PredicateIterator(
            RuntimeIterator sequence,
            RuntimeIterator filterExpression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(sequence, filterExpression), executionMode, iteratorMetadata);
        this.iterator = sequence;
        this.filter = filterExpression;
        this.filterDynamicContext = null;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext == true) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Predicate!", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.iterator.close();
        this.filterDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        if (this.filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (this.filter.getVariableDependencies().containsKey("$position")) {
            this.position = 0;
            this.mustMaintainPosition = true;
        }
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    protected void openLocal() {
        if (this.children.size() < 2) {
            throw new OurBadException("Invalid Predicate! Must initialize filter before calling next");
        }
        this.filterDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        if (this.filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (this.filter.getVariableDependencies().containsKey("$position")) {
            this.position = 0;
            this.mustMaintainPosition = true;
        }
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void setLast() {
        long last = 0;
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        while (this.iterator.hasNext()) {
            this.iterator.next();
            ++last;
        }
        this.iterator.close();
        this.filterDynamicContext.setLast(last);
    }

    private void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            this.filterDynamicContext.addVariableValue("$$", currentItems);
            if (this.mustMaintainPosition)
                this.filterDynamicContext.setPosition(++this.position);

            this.filter.open(this.filterDynamicContext);
            Item fil = null;
            if (this.filter.hasNext()) {
                fil = this.filter.next();
            }
            this.filter.close();
            // if filter is an integer, it is used to return the element with the index equal to the given integer
            if (fil instanceof IntegerItem) {
                this.iterator.close();
                List<Item> sequence = this.iterator.materialize(this.currentDynamicContextForLocalExecution);
                int index = ((IntegerItem) fil).getIntegerValue();
                // less than or equal to size -> b/c of -1
                if (index >= 1 && index <= sequence.size()) {
                    // -1 for Jsoniq convention, arrays start from 1
                    this.nextResult = sequence.get(index - 1);
                }
                break;
            } else if (fil != null && fil.getEffectiveBooleanValue()) {
                this.nextResult = item;
                break;
            }
            this.filter.close();
        }
        this.filterDynamicContext.removeVariable("$$");

        if (this.nextResult == null) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator iterator = this.children.get(0);
        RuntimeIterator filter = this.children.get(1);
        JavaRDD<Item> childRDD = iterator.getRDD(dynamicContext);
        if (
            !filter.getVariableDependencies().containsKey("$position")
                && !filter.getVariableDependencies().containsKey("$last")
                && (filter instanceof BooleanRuntimeIterator
                    || filter instanceof AndOperationIterator
                    || filter instanceof OrOperationIterator
                    || filter instanceof NotOperationIterator
                    || filter instanceof ComparisonOperationIterator)
        ) {
            Function<Item, Boolean> transformation = new PredicateClosure(filter, dynamicContext);
            JavaRDD<Item> resultRDD = childRDD.filter(transformation);
            return resultRDD;
        } else {
            JavaPairRDD<Item, Long> zippedChildRDD = childRDD.zipWithIndex();
            long last = 0;
            if (filter.getVariableDependencies().containsKey("$last")) {
                last = childRDD.count();
            }
            Function<Tuple2<Item, Long>, Boolean> transformation = new PredicateClosureZipped(
                    filter,
                    dynamicContext,
                    last
            );
            JavaPairRDD<Item, Long> resultRDD = zippedChildRDD.filter(transformation);
            return resultRDD.keys();
        }
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(this.filter.getVariableDependencies());
        result.remove("$");
        result.putAll(this.iterator.getVariableDependencies());
        return result;
    }
}
