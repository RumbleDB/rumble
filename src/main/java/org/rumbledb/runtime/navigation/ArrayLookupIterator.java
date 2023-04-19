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
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.Map;

public class ArrayLookupIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private int lookup;
    private Item nextResult;

    public ArrayLookupIterator(
            RuntimeIterator array,
            RuntimeIterator iterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(array, iterator), executionMode, iteratorMetadata);
        this.iterator = array;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Array Lookup", getMetadata());
    }


    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
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

    private void initLookupPosition(DynamicContext context) {
        RuntimeIterator lookupIterator = this.children.get(1);

        try {
            Item lookupExpression = lookupIterator.materializeExactlyOneItem(context);
            if (!lookupExpression.isNumeric()) {
                throw new UnexpectedTypeException(
                        "Type error; Non numeric array lookup for : "
                            + lookupExpression.serialize(),
                        getMetadata()
                );
            }
            this.lookup = lookupExpression.castToIntValue();
        } catch (NoItemException e) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Array lookup can't be performed with no key.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Array lookup can't be performed with multiple keys.",
                    getMetadata()
            );
        }
    }

    @Override
    public void openLocal() {
        initLookupPosition(this.currentDynamicContextForLocalExecution);
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isArray()) {
                if (this.lookup > 0 && this.lookup <= item.getSize()) {
                    // -1 for Jsoniq convention, arrays start from 1
                    Item result = item.getItemAt(this.lookup - 1);
                    this.nextResult = result;
                    break;
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        initLookupPosition(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayLookupClosure(this.lookup);

        JavaRDD<Item> resultRDD = childRDD.flatMap(transformation);
        return resultRDD;
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext newContext = this.iterator.generateNativeQuery(nativeClauseContext);
        if (newContext != NativeClauseContext.NoNativeQuery) {
            if (SequenceType.Arity.OneOrMore.isSubtypeOf(newContext.getResultingType().getArity())) {
                return NativeClauseContext.NoNativeQuery;
            }
            // check if the key has variable dependencies inside the FLWOR expression
            // in that case we switch over to UDF
            Map<Name, DynamicContext.VariableDependency> keyDependencies = this.children.get(1)
                .getVariableDependencies();
            // we use nativeClauseContext that contains the top level schema
            DataType schema = nativeClauseContext.getSchema();
            StructType structSchema;
            if (schema instanceof StructType) {
                structSchema = (StructType) schema;
                if (
                    Arrays.stream(structSchema.fieldNames())
                        .anyMatch(field -> keyDependencies.containsKey(Name.createVariableInNoNamespace(field)))
                ) {
                    return NativeClauseContext.NoNativeQuery;
                }
            }

            initLookupPosition(newContext.getContext());

            ItemType resultType = newContext.getResultingType().getItemType();
            if (!(resultType.isArrayItemType())) {
                return NativeClauseContext.NoNativeQuery;
            }

            schema = newContext.getSchema();
            if (!(schema instanceof ArrayType)) {
                return NativeClauseContext.NoNativeQuery;
            }
            newContext.setResultingType(
                new SequenceType(
                        resultType.getArrayContentFacet(),
                        SequenceType.Arity.OneOrZero
                )
            );
            newContext.setResultingQuery(newContext.getResultingQuery() + "[" + (this.lookup - 1) + "]");
        }
        return newContext;
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.children.get(0).getDataFrame(context);
        initLookupPosition(context);
        String array = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        if (childDataFrame.getItemType().isArrayItemType()) {
            ItemType elementType = childDataFrame.getItemType().getArrayContentFacet();
            if (elementType.isObjectItemType()) {
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT `%s`.* FROM (SELECT `%s`[%s] as `%s` FROM %s WHERE size(`%s`) >= %s)",
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        Integer.toString(this.lookup - 1),
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        array,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        Integer.toString(this.lookup)
                    ),
                    elementType
                );
            }
            return childDataFrame.evaluateSQL(
                String.format(
                    "SELECT `%s`[%s] as `%s` FROM %s WHERE size(`%s`) >= %s",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    Integer.toString(this.lookup - 1),
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    array,
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    Integer.toString(this.lookup)
                ),
                elementType
            );
        }
        return JSoundDataFrame.emptyDataFrame();
    }
}
