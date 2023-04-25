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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.types.ArrayType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayUnboxingIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection

    public ArrayUnboxingIterator(
            RuntimeIterator arrayIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(arrayIterator), executionMode, iteratorMetadata);
        this.iterator = arrayIterator;
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResults.remove(); // save the result to be returned
            if (this.nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Array Unboxing", getMetadata());
    }

    @Override
    protected void resetLocal() {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    private void setNextResult() {
        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isArray()) {
                // if array is not empty, set the first item as the result
                if (0 < item.getSize()) {
                    this.nextResults.addAll(item.getItems());
                    break;
                }
            }
        }

        if (this.nextResults.isEmpty()) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayUnboxingClosure();
        JavaRDD<Item> resultRDD = childRDD.flatMap(transformation);
        return resultRDD;
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (nativeClauseContext.getClauseType() != FLWOR_CLAUSES.FOR) {
            // unboxing only available for the FOR clause
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext newContext = this.iterator.generateNativeQuery(nativeClauseContext);
        if (newContext != NativeClauseContext.NoNativeQuery) {
            ItemType newContextType = newContext.getResultingType().getItemType();
            if (!newContextType.isArrayItemType()) {
                // let control to UDF when what we are unboxing is not an array
                return NativeClauseContext.NoNativeQuery;
            }
            newContext.setResultingType(
                new SequenceType(
                        newContextType.getArrayContentFacet(),
                        SequenceType.Arity.ZeroOrMore
                )
            );
            List<String> lateralViewPart = newContext.getLateralViewPart();
            if (lateralViewPart.size() == 0) {
                lateralViewPart.add("explode(" + newContext.getResultingQuery() + ")");
            } else {
                // if we have multiple array unboxing we stack multiple lateral views and each one takes from the
                // previous
                lateralViewPart.add(
                    "explode( arr" + lateralViewPart.size() + ".col" + newContext.getResultingQuery() + ")"
                );
            }
            newContext.setResultingQuery(""); // dealt by for clause
        }
        ItemType newContextType = newContext.getResultingType().getItemType();
        if (!newContextType.isArrayItemType()) {
            // let control to UDF when what we are unboxing is not an array
            return NativeClauseContext.NoNativeQuery;
        }
        newContext.setResultingType(
            new SequenceType(
                    newContextType.getArrayContentFacet(),
                    SequenceType.Arity.ZeroOrMore
            )
        );

        List<String> lateralViewPart = newContext.getLateralViewPart();
        if (lateralViewPart.size() == 0) {
            lateralViewPart.add("explode(" + newContext.getResultingQuery() + ")");
        } else {
            // if we have multiple array unboxing we stack multiple lateral views and each one takes from the
            // previous
            lateralViewPart.add(
                "explode( arr" + lateralViewPart.size() + ".col" + newContext.getResultingQuery() + ")"
            );
        }
        newContext.setSchema(((ArrayType) newContext.getSchema()).elementType());
        newContext.setResultingQuery(""); // dealt by for clause
        return newContext;
    }

    public NativeClauseContext generateArrayReferenceQuery(NativeClauseContext nativeClauseContext) {
        return this.iterator.generateNativeQuery(nativeClauseContext);
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.children.get(0).getDataFrame(context);
        String array = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        if (childDataFrame.getItemType().isArrayItemType()) {
            ItemType elementType = childDataFrame.getItemType().getArrayContentFacet();
            if (elementType.isObjectItemType()) {
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT `%s`.* FROM (SELECT explode(`%s`) as `%s` FROM %s)",
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        array
                    ),
                    elementType
                );
            }
            return childDataFrame.evaluateSQL(
                String.format(
                    "SELECT explode(`%s`) AS `%s` FROM %s",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    array
                ),
                elementType
            );
        }
        return JSoundDataFrame.emptyDataFrame();
    }
}
