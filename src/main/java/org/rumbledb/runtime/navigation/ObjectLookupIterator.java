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

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.FlatMappingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.primary.ContextExpressionIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

import sparksoniq.spark.SparkSessionManager;

public class ObjectLookupIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan iterator;
    private final ItemRuntimePlan lookupIterator;
    private Item lookupKey;
    private boolean contextLookup;

    public ObjectLookupIterator(
            ItemRuntimePlan object,
            ItemRuntimePlan lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(object, lookupIterator), staticContext);
        this.iterator = object;
        this.lookupIterator = lookupIterator;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        String key;
        if (this.lookupIterator instanceof ContextExpressionIterator) {
            key = context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0)
                .getStringValue();
        } else {
            key = requireLookupKey(this.lookupIterator.materialize(context));
        }
        return new FlatMappingLocalCursor<>(
                this.iterator,
                context,
                item -> {
                    if (!item.isObject()) {
                        return List.<Item>of().iterator();
                    }
                    Item result = item.getItemByKey(key);
                    return result == null ? List.<Item>of().iterator() : List.of(result).iterator();
                },
                getMetadata()
        );
    }

    private void initLookupKey(DynamicContext context) {
        this.contextLookup = this.lookupIterator instanceof ContextExpressionIterator;

        if (!this.contextLookup) {

            try {
                this.lookupKey = this.lookupIterator.materializeExactlyOne(context);
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

    private String requireLookupKey(List<Item> values) {
        if (values.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Object lookup can't be performed with no key.",
                    getMetadata()
            );
        }
        if (values.size() > 1) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Object lookup can't be performed with multiple keys.",
                    getMetadata()
            );
        }
        Item key = values.get(0);
        if (key.isNull() || key.isObject() || key.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; Object selector can't be converted to a string: " + key.serialize(),
                    getMetadata()
            );
        }
        if (key.isBoolean()) {
            return Boolean.toString(key.getBooleanValue());
        }
        if (key.isDecimal()) {
            return key.getDecimalValue().toString();
        }
        if (key.isDouble()) {
            return Double.toString(key.getDoubleValue());
        }
        if (key.isInt()) {
            return Integer.toString(key.getIntValue());
        }
        if (key.isInteger()) {
            return key.getIntegerValue().toString();
        }
        if (key.isString()) {
            return key.getStringValue();
        }
        throw new UnexpectedTypeException(
                "Non string object lookup for " + key.serialize(),
                getMetadata()
        );
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.getChild(0).getRDD(dynamicContext);
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
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        // check if the key has variable dependencies inside the FLWOR expression
        // in that case we switch over to UDF
        Map<Name, DynamicContext.VariableDependency> keyDependencies =
            this.lookupIterator.getVariableDependencies();
        // we use nativeClauseContext that contains the top level schema
        DataType outerContextSchema = nativeClauseContext.getSchema();
        // if the right hand side depends on the tuple stream, we cannot turn this into a native SQL query.
        if (outerContextSchema instanceof StructType structSchema) {
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
                ? TypeMappings.getDataFrameDataTypeFromItemType(
                    nativeClauseContext.getResultingType().getItemType(),
                    this.staticContext
                )
                : outerContextSchema;
            if (leftSchema instanceof StructType) {
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        null,
                        nativeClauseContext.getResultingType()
                );
            } else {
                if (leftSchema instanceof ArrayType arrayType) {
                    leftSchema = arrayType.elementType();
                }
                newContext = new NativeClauseContext(
                        nativeClauseContext,
                        "`" + SparkSessionManager.nonObjectJSONiqItemColumnName + "`",
                        nativeClauseContext.getResultingType()
                );
            }
        } else {
            newContext = NativeQueryRuntimePlan.generate(this.iterator, nativeClauseContext);
            if (newContext != NativeClauseContext.NoNativeQuery) {
                leftSchema = TypeMappings.getDataFrameDataTypeFromItemType(
                    newContext.getResultingType().getItemType(),
                    this.staticContext
                );
            } else {
                return NativeClauseContext.NoNativeQuery;
            }
            leftSchema = newContext.getSchema();
        }



        // get key (escape backtick)
        String key = this.lookupKey.getStringValue().replace("`", FlworDataFrameUtils.backtickEscape);
        String sequenceKey = key + SparkSessionManager.sequenceColumnName;
        if (!(leftSchema instanceof StructType structSchema)) {
            if (this.lookupIterator instanceof StringRuntimeIterator) {
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
                && (newContext.getResultingType().getItemType().getObjectKeysFacet().contains(key)
                    || newContext.getResultingType().getItemType().getObjectKeysFacet().contains(sequenceKey))
        ) {
            if (newContext.getResultingType().getItemType().getObjectKeysFacet().contains(sequenceKey)) {
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
                .getObjectContentFacet(key)
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
            if (this.lookupIterator instanceof StringRuntimeIterator) {
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

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame childDataFrame = ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.getChild(0), context);
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
            FieldDescriptor fieldDescriptor = childDataFrame.getItemType().getObjectContentFacet(key);
            ItemType type = BuiltinTypesCatalogue.item;
            if (fieldDescriptor != null) {
                type = fieldDescriptor.getType();
            }
            if (type.isObjectItemType()) {
                // TODO: Find another way to check if delta dataframe -- e.g. flag and mutability level
                // TODO: implement keyword vars to stop ust using strs
                String sql;
                if (childDataFrame.getKeys().contains(SparkSessionManager.tableLocationColumnName)) {
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
                HomogeneousItemDataFrame result = childDataFrame.evaluateSQL(
                    sql,
                    type
                );
                return result;
            } else {
                String sql;
                HomogeneousItemDataFrame result;
                if (childDataFrame.getKeys().contains(SparkSessionManager.tableLocationColumnName)) {
                    sql = String.format(
                        "SELECT `%s` AS `%s`, `%s`, `%s`, CONCAT(`%s`, '.%s') AS `%s`, `%s` FROM %s",
                        key,
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        SparkSessionManager.rowIdColumnName,
                        SparkSessionManager.mutabilityLevelColumnName,
                        SparkSessionManager.pathInColumnName,
                        key,
                        SparkSessionManager.pathInColumnName,
                        SparkSessionManager.tableLocationColumnName,
                        object
                    );
                    Dataset<Row> df = childDataFrame.getDataFrame().sparkSession().sql(sql);
                    result = new HomogeneousItemDataFrame(df, type);
                } else {
                    sql = String.format(
                        "SELECT `%s` AS `%s` FROM %s",
                        key,
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
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
        HomogeneousItemDataFrame result = HomogeneousItemDataFrame.emptyDataFrame();
        return result;
    }
}
