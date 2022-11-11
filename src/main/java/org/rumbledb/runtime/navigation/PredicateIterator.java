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

package org.rumbledb.runtime.navigation;

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.types.DataTypes;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.logics.AndOperationIterator;
import org.rumbledb.runtime.logics.NotOperationIterator;
import org.rumbledb.runtime.logics.OrOperationIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;

import scala.Tuple2;

import java.math.BigInteger;
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
    private boolean isBooleanOnlyFilter;


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
        this.isBooleanOnlyFilter = isBooleanOnlyFilter();
    }

    public RuntimeIterator sequenceIterator() {
        return this.iterator;
    }

    public RuntimeIterator predicateIterator() {
        return this.filter;
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
    protected void resetLocal() {
        this.iterator.close();
        this.filterDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        if (this.filter.getVariableDependencies().containsKey(Name.CONTEXT_COUNT)) {
            setLast();
        }
        if (!this.isBooleanOnlyFilter) {
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

    private boolean isBooleanOnlyFilter() {
        return !this.filter.getVariableDependencies().containsKey(Name.CONTEXT_POSITION)
            && !this.filter.getVariableDependencies().containsKey(Name.CONTEXT_COUNT)
            && (this.filter instanceof BooleanRuntimeIterator
                || this.filter instanceof AndOperationIterator
                || this.filter instanceof OrOperationIterator
                || this.filter instanceof NotOperationIterator
                || this.filter instanceof ComparisonIterator);
    }

    @Override
    protected void openLocal() {
        if (this.children.size() < 2) {
            throw new OurBadException("Invalid Predicate! Must initialize filter before calling next");
        }
        this.filterDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        if (this.filter.getVariableDependencies().containsKey(Name.CONTEXT_COUNT)) {
            setLast();
        }
        if (!this.isBooleanOnlyFilter) {
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
        this.filterDynamicContext.getVariableValues().setLast(last);
    }

    private void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            this.filterDynamicContext.getVariableValues()
                .addVariableValue(
                    Name.CONTEXT_ITEM,
                    currentItems
                );
            if (this.mustMaintainPosition) {
                this.filterDynamicContext.getVariableValues().setPosition(++this.position);
            }

            this.filter.open(this.filterDynamicContext);
            Item fil = null;
            if (this.filter.hasNext()) {
                fil = this.filter.next();
            }
            this.filter.close();
            // if filter is an integer, it is used to return the element(s) with the index equal to the given integer
            if (fil != null && fil.isInt()) {
                int index = fil.getIntValue();
                if (index == this.position) {
                    this.nextResult = item;
                    break;
                }
            } else if (fil != null && fil.isInteger()) {
                BigInteger index = fil.getIntegerValue();
                if (index.equals(BigInteger.valueOf(this.position))) {
                    this.nextResult = item;
                    break;
                }
            } else if (fil != null && fil.getEffectiveBooleanValue()) {
                this.nextResult = item;
                break;
            }
        }
        this.filterDynamicContext.getVariableValues().removeVariable(Name.CONTEXT_ITEM);

        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator iterator = this.children.get(0);
        RuntimeIterator filter = this.children.get(1);
        JavaRDD<Item> childRDD = iterator.getRDD(dynamicContext);
        if (this.isBooleanOnlyFilter) {
            Function<Item, Boolean> transformation = new PredicateClosure(filter, dynamicContext);
            JavaRDD<Item> resultRDD = childRDD.filter(transformation);
            return resultRDD;
        } else {
            JavaPairRDD<Item, Long> zippedChildRDD = childRDD.zipWithIndex();
            long last = 0;
            if (filter.getVariableDependencies().containsKey(Name.CONTEXT_COUNT)) {
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

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.children.get(0).getDataFrame(context);
        RuntimeIterator filter = this.children.get(1);
        NativeClauseContext nativeClauseContext = new NativeClauseContext(
                FLWOR_CLAUSES.FILTER,
                childDataFrame.getDataFrame().schema(),
                context
        );
        NativeClauseContext nativeQuery = filter.generateNativeQuery(nativeClauseContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            if (this.isBooleanOnlyFilter) {
                String left = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
                List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
                    childDataFrame.getDataFrame().schema(),
                    null,
                    null,
                    null
                );

                childDataFrame.getDataFrame()
                    .sparkSession()
                    .udf()
                    .register(
                        "predicate",
                        new PredicateUDF(filter, context, getMetadata(), childDataFrame.getItemType()),
                        DataTypes.BooleanType
                    );
                String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT * FROM %s WHERE predicate(%s) = 'true'",
                        left,
                        UDFParameters
                    ),
                    childDataFrame.getItemType()
                );
            } else {
                JSoundDataFrame zippedChildDataFrame = FlworDataFrameUtils.zipWithIndex(
                    childDataFrame,
                    1L
                );
                String left = FlworDataFrameUtils.createTempView(zippedChildDataFrame.getDataFrame());
                List<String> UDFcolumns = FlworDataFrameUtils.getColumnNames(
                    zippedChildDataFrame.getDataFrame().schema(),
                    null,
                    null,
                    null
                );
                List<String> originalcolumns = FlworDataFrameUtils.getColumnNames(
                    childDataFrame.getDataFrame().schema(),
                    null,
                    null,
                    null
                );

                long contextSize = childDataFrame.getDataFrame().count();
                childDataFrame.getDataFrame()
                    .sparkSession()
                    .udf()
                    .register(
                        "predicate",
                        new PredicateWithZipUDF(
                                filter,
                                context,
                                getMetadata(),
                                childDataFrame.getItemType(),
                                contextSize
                        ),
                        DataTypes.BooleanType
                    );
                String UDFParameters = FlworDataFrameUtils.getUDFParameters(UDFcolumns);
                String projection = FlworDataFrameUtils.getSQLProjection(originalcolumns, false);
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT %s FROM %s WHERE predicate(%s) = 'true'",
                        projection,
                        left,
                        UDFParameters
                    ),
                    childDataFrame.getItemType()
                );
            }
        }
        LogManager.getLogger("PredicateIterator")
            .info(
                "Rumble was able to optimize a predicate to a native SQL query."
            );
        String left = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        return childDataFrame.evaluateSQL(
            String.format(
                "SELECT * FROM %s WHERE %s",
                left,
                nativeQuery.getResultingQuery()
            ),
            childDataFrame.getItemType()
        );

    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.filter.getVariableDependencies());
        result.remove(Name.CONTEXT_ITEM);
        result.putAll(this.iterator.getVariableDependencies());
        return result;
    }
}
