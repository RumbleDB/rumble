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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.exceptions.UnexpectedTypeException;
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
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

import sparksoniq.spark.SparkSessionManager;

public class ObjectLookupIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item lookupKey;
    private boolean contextLookup;
    private Item nextResult;

    public ObjectLookupIterator(
            RuntimeIterator object,
            RuntimeIterator lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(object, lookupIterator), staticContext);
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
            for (Name n : keyDependencies.keySet()) {
                if (FlworDataFrameUtils.hasColumnForVariable(structSchema, n)) {
                    return NativeClauseContext.NoNativeQuery;
                }
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
            leftSchema = (nativeClauseContext.getResultingType() != null)
                ? TypeMappings.getDataFrameDataTypeFromItemType(nativeClauseContext.getResultingType().getItemType())
                : outerContextSchema;
            if (leftSchema instanceof StructType) {
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        null
                );
            } else {
                if (leftSchema instanceof ArrayType) {
                    leftSchema = ((ArrayType) leftSchema).elementType();
                }
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        "`" + SparkSessionManager.atomicJSONiqItemColumnName + "`",
                        nativeClauseContext.getResultingType()
                );
            }
        } else {
            newContext = this.iterator.generateNativeQuery(nativeClauseContext);
            if (newContext != NativeClauseContext.NoNativeQuery) {
                leftSchema = TypeMappings.getDataFrameDataTypeFromItemType(newContext.getResultingType().getItemType());
            } else {
                return NativeClauseContext.NoNativeQuery;
            }
            leftSchema = newContext.getSchema();
        }



        // get key (escape backtick)
        String key = this.lookupKey.getStringValue().replace("`", FlworDataFrameUtils.backtickEscape);
        String sequenceKey = key + SparkSessionManager.sequenceColumnName;
        if (!(leftSchema instanceof StructType)) {
            if (this.children.get(1) instanceof StringRuntimeIterator) {
                if (getConfiguration().doStaticAnalysis()) {
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
                LogManager.getLogger("ObjectLookupIterator")
                    .warn(
                        "Object lookup on a DataFrame that does not have this column. Empty sequence returned."
                    );
            }
            return NativeClauseContext.NoNativeQuery;
        }
        StructType structSchema = (StructType) leftSchema;
        if (
            Arrays.asList(structSchema.fieldNames()).contains(key)
                || Arrays.asList(structSchema.fieldNames()).contains(sequenceKey)
        ) {
            if (Arrays.asList(structSchema.fieldNames()).contains(sequenceKey)) {
                key = sequenceKey;
            }
            String leftQuery = newContext.getResultingQuery();
            if (leftQuery != null) {
                newContext.setResultingQuery(leftQuery + ".`" + key + "`");
            } else {
                newContext.setResultingQuery("`" + key + "`");
            }
            StructField field = structSchema.fields()[structSchema.fieldIndex(key)];
            newContext.setResultingType(
                new SequenceType(
                        TypeMappings.getItemTypeFromDataFrameDataType(field.dataType()),
                        SequenceType.Arity.OneOrZero
                )
            );
            newContext.setSchema(field.dataType());
        } else if (
            newContext.getResultingType().getItemType().isObjectItemType()
                && (newContext.getResultingType().getItemType().getObjectContentFacet().containsKey(key)
                    || newContext.getResultingType().getItemType().getObjectContentFacet().containsKey(sequenceKey))
        ) {
            if (newContext.getResultingType().getItemType().getObjectContentFacet().containsKey(sequenceKey)) {
                key = sequenceKey;
            }
            String leftQuery = newContext.getResultingQuery();
            if (leftQuery != null) {
                newContext.setResultingQuery(leftQuery + ".`" + key + "`");
            } else {
                newContext.setResultingQuery("`" + key + "`");
            }
            ItemType resultType = newContext.getResultingType()
                .getItemType()
                .getObjectContentFacet()
                .get(key)
                .getType();
            newContext.setResultingType(new SequenceType(resultType, SequenceType.Arity.OneOrZero));
            StructField field = structSchema.fields()[structSchema.fieldIndex(key)];
            newContext.setResultingType(
                new SequenceType(
                        TypeMappings.getItemTypeFromDataFrameDataType(field.dataType()),
                        SequenceType.Arity.OneOrZero
                )
            );
            newContext.setSchema(field.dataType());
        } else {
            if (this.children.get(1) instanceof StringRuntimeIterator) {
                LogManager.getLogger("ObjectLookupIterator")
                    .warn(
                        "Object lookup on a DataFrame that does not have this column. Empty sequence returned."
                    );
                if (getConfiguration().doStaticAnalysis()) {
                    throw new UnexpectedStaticTypeException(
                            "There is no field with the name "
                                + key
                                + " so that the lookup will always result in the empty sequence no matter what. "
                                + "Fortunately Rumble was able to catch this. This is probably a typo? Please check the spelling and try again.",
                            ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                            getMetadata()
                    );
                }
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
                // TODO: Find another way to check if delta dataframe -- e.g. flag and mutability level
                // TODO: implement keyword vars to stop ust using strs
                String sql;
                if (childDataFrame.getKeys().contains("tableLocation")) {
                    sql = String.format(
                        "SELECT `%s`.*, `%s`, `%s`, CONCAT(`%s`, '.%s') AS `%s`, `%s` FROM %s",
                        key,
                        SparkSessionManager.rowIdColumnName,
                        SparkSessionManager.mutabilityLevelColumnName,
                        SparkSessionManager.pathInColumnName,
                        key,
                        SparkSessionManager.pathInColumnName,
                        SparkSessionManager.tableLocationColumnName,
                        object
                    );

                } else {
                    sql = String.format("SELECT `%s`.* FROM %s", key, object);
                }
                JSoundDataFrame result = childDataFrame.evaluateSQL(
                    sql,
                    type
                );
                return result;
            } else {
                String sql;
                JSoundDataFrame result;
                if (childDataFrame.getKeys().contains("tableLocation")) {
                    sql = String.format(
                        "SELECT `%s` AS `%s`, `%s`, `%s`, CONCAT(`%s`, '.%s') AS `%s`, `%s` FROM %s",
                        key,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        SparkSessionManager.rowIdColumnName,
                        SparkSessionManager.mutabilityLevelColumnName,
                        SparkSessionManager.pathInColumnName,
                        key,
                        SparkSessionManager.pathInColumnName,
                        SparkSessionManager.tableLocationColumnName,
                        object
                    );
                    Dataset<Row> df = childDataFrame.getDataFrame().sparkSession().sql(sql);
                    ItemType deltaItemType = ItemTypeFactory.createItemType(df.schema());
                    result = new JSoundDataFrame(df, deltaItemType);
                } else {
                    sql = String.format(
                        "SELECT `%s` AS `%s` FROM %s",
                        key,
                        SparkSessionManager.atomicJSONiqItemColumnName,
                        object
                    );
                    result = childDataFrame.evaluateSQL(
                        sql,
                        type
                    );
                }
                return result;
            }
        }
        LogManager.getLogger("ObjectLookupIterator")
            .warn(
                "Object lookup on a DataFrame that does not have this column. Empty sequence returned."
            );
        JSoundDataFrame result = JSoundDataFrame.emptyDataFrame();
        return result;
    }
}
