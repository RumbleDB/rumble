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
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.primary.ContextExpressionIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;
import org.rumbledb.types.TypeMappings;

import sparksoniq.spark.SparkSessionManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

public class ObjectLookupIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item lookupKey;
    private boolean contextLookup;
    private Item nextResult;

    public ObjectLookupIterator(
            RuntimeIterator object,
            RuntimeIterator lookupIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(object, lookupIterator), executionMode, iteratorMetadata);
        this.iterator = object;
    }

    private void initLookupKey(DynamicContext context) {

        RuntimeIterator lookupIterator = this.children.get(1);

        this.contextLookup = lookupIterator instanceof ContextExpressionIterator;

        if (!this.contextLookup) {

            try {
                this.lookupKey = lookupIterator.materializeExactlyOneItem(context);
            } catch (NoItemException e) {
                throw new InvalidSelectorException(
                        "Invalid Lookup Key; Object lookup can't be performed with no key.",
                        getMetadata()
                );
            } catch (MoreThanOneItemException e) {
                throw new InvalidSelectorException(
                        "Invalid Lookup Key; Object lookup can't be performed with multiple keys.",
                        getMetadata()
                );
            }

            if (this.lookupKey.isNull() || this.lookupKey.isObject() || this.lookupKey.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; Object selector can't be converted to a string: "
                            + this.lookupKey.serialize(),
                        getMetadata()
                );
            } else {
                // convert to string
                if (this.lookupKey.isBoolean()) {
                    Boolean value = this.lookupKey.getBooleanValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isDecimal()) {
                    BigDecimal value = this.lookupKey.getDecimalValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isDouble()) {
                    Double value = this.lookupKey.getDoubleValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isInt()) {
                    Integer value = this.lookupKey.getIntValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isInteger()) {
                    BigInteger value = this.lookupKey.getIntegerValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isString()) {
                    // do nothing
                }
            }
            if (!this.lookupKey.isString()) {
                throw new UnexpectedTypeException(
                        "Non string object lookup for " + this.lookupKey.serialize(),
                        getMetadata()
                );
            }
        }
    }

    @Override
    public void openLocal() {
        initLookupKey(this.currentDynamicContextForLocalExecution);
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
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

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isObject()) {
                if (!this.contextLookup) {
                    Item result = item.getItemByKey(this.lookupKey.getStringValue());
                    if (result != null) {
                        this.nextResult = result;
                        break;
                    }
                } else {
                    Item contextItem = this.currentDynamicContextForLocalExecution.getVariableValues()
                        .getLocalVariableValue(
                            Name.CONTEXT_ITEM,
                            getMetadata()
                        )
                        .get(0);
                    this.nextResult = item.getItemByKey(contextItem.getStringValue());
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
        initLookupKey(dynamicContext);
        String key;
        if (this.contextLookup) {
            // For now this will always be an error. Later on we will pass the dynamic context from the parent iterator.
            key = dynamicContext.getVariableValues()
                .getLocalVariableValue(
                    Name.CONTEXT_ITEM,
                    getMetadata()
                )
                .get(0)
                .getStringValue();
        } else {
            key = this.lookupKey.getStringValue();
        }
        FlatMapFunction<Item, Item> transformation = new ObjectLookupClosure(key);

        return childRDD.flatMap(transformation);
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        // check if the key has variable dependencies inside the FLWOR expression
        // in that case we switch over to UDF
        Map<Name, DynamicContext.VariableDependency> keyDependencies = this.children.get(1)
            .getVariableDependencies();
        // we use nativeClauseContext that contains the top level schema
        DataType outerContextSchema = nativeClauseContext.getSchema();
        // if the right hand side depends on the tuple stream, we cannot turn this into a native SQL query.
        if (outerContextSchema instanceof StructType) {
            StructType structSchema = (StructType) outerContextSchema;
            if (
                Arrays.stream(structSchema.fieldNames())
                    .anyMatch(field -> keyDependencies.containsKey(Name.createVariableInNoNamespace(field)))
            ) {
                return NativeClauseContext.NoNativeQuery;
            }
        }
        // otherwise, we can directly resolve the key statically.
        initLookupKey(nativeClauseContext.getContext());

        // Next we determine the schema against which the key is resolved
        // If this is a filter, then this is the outer schema. Otherwise
        // this is the schema from the left hand side.
        DataType leftSchema;
        NativeClauseContext newContext;
        if (
            nativeClauseContext.getClauseType().equals(FLWOR_CLAUSES.FILTER)
                && (this.iterator instanceof ContextExpressionIterator)
        ) {
            leftSchema = outerContextSchema;
            if (outerContextSchema instanceof StructType) {
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        null,
                        nativeClauseContext.getResultingType()
                );
            } else {
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        nativeClauseContext.getResultingType()
                );
            }
        } else {
            newContext = this.iterator.generateNativeQuery(nativeClauseContext);
            if (newContext == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }
            leftSchema = newContext.getSchema();
        }



        // get key (escape backtick)
        String key = this.lookupKey.getStringValue().replace("`", FlworDataFrameUtils.backtickEscape);
        if (!(leftSchema instanceof StructType)) {
            if (this.children.get(1) instanceof StringRuntimeIterator) {
                throw new UnexpectedStaticTypeException(
                        "You are trying to look up the value associated with the field "
                            + key
                            + ". However, the left-hand-side cannot contain any objects and it will always return the empty sequence! "
                            + "Fortunately Rumble was able to catch this. This is probably an overlook? "
                            + "Please check your query and try again.",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        getMetadata()
                );
            }
            return NativeClauseContext.NoNativeQuery;
        }
        StructType structSchema = (StructType) leftSchema;
        if (Arrays.stream(structSchema.fieldNames()).anyMatch(field -> field.equals(key))) {
            String leftQuery = newContext.getResultingQuery();
            if (leftQuery != null) {
                newContext.setResultingQuery(leftQuery + ".`" + key + "`");
            } else {
                newContext.setResultingQuery("`" + key + "`");
            }
            StructField field = structSchema.fields()[structSchema.fieldIndex(key)];
            newContext.setSchema(field.dataType());
            newContext.setResultingType(
                new SequenceType(TypeMappings.getItemTypeFromDataFrameDataType(field.dataType()), Arity.One)
            );
        } else {
            if (this.children.get(1) instanceof StringRuntimeIterator) {
                throw new UnexpectedStaticTypeException(
                        "There is no field with the name "
                            + key
                            + " so that the lookup will always result in the empty sequence no matter what. "
                            + "Fortunately Rumble was able to catch this. This is probably a typo? Please check the spelling and try again.",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        getMetadata()
                );
            }
            return NativeClauseContext.NoNativeQuery;
        }
        return newContext;
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.children.get(0).getDataFrame(context);
        initLookupKey(context);
        String key;
        if (this.contextLookup) {
            key = context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0)
                .getStringValue();
        } else {
            key = this.lookupKey.getStringValue();
        }
        String object = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        if (childDataFrame.hasKey(key)) {
            FieldDescriptor fieldDescriptor = childDataFrame.getItemType().getObjectContentFacet().get(key);
            ItemType type = BuiltinTypesCatalogue.item;
            if (fieldDescriptor != null) {
                type = fieldDescriptor.getType();
            }
            if (type.isObjectItemType()) {
                JSoundDataFrame result = childDataFrame.evaluateSQL(
                    String.format("SELECT `%s`.* FROM %s", key, object),
                    type
                );
                return result;
            } else {
                JSoundDataFrame result = childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT `%s` AS `%s` FROM %s",
                        key,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        object
                    ),
                    type
                );
                return result;
            }
        }
        JSoundDataFrame result = JSoundDataFrame.emptyDataFrame();
        return result;
    }
}
