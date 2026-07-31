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

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.FlatMappingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ArrayLookupIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> iterator;
    private int lookup;

    public ArrayLookupIterator(
            RuntimePlan<Item> array,
            RuntimePlan<Item> iterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(array, iterator), staticContext);
        this.iterator = array;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        int position = requireLookupPosition(
            this.getChild(1).materialize(context)
        );
        return new FlatMappingLocalCursor<>(
                this.iterator,
                context,
                item -> {
                    if (!item.isArray() || position <= 0 || position > item.getSize()) {
                        return List.<Item>of().iterator();
                    }
                    if (item.isArrayOfItems()) {
                        return List.of(item.getItemAt(position - 1)).iterator();
                    }
                    return item.getSequenceAt(position - 1).iterator();
                },
                getMetadata()
        );
    }



    private void initLookupPosition(DynamicContext context) {
        RuntimePlan<Item> lookupIterator = this.getChild(1);

        try {
            Item lookupExpression = lookupIterator.materializeExactlyOne(context);
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

    private int requireLookupPosition(List<Item> values) {
        if (values.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Array lookup can't be performed with no key.",
                    getMetadata()
            );
        }
        if (values.size() > 1) {
            throw new InvalidSelectorException(
                    "Invalid Lookup Key; Array lookup can't be performed with multiple keys.",
                    getMetadata()
            );
        }
        Item lookupExpression = values.get(0);
        if (!lookupExpression.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; Non numeric array lookup for : " + lookupExpression.serialize(),
                    getMetadata()
            );
        }
        return lookupExpression.castToIntValue();
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.getChild(0).getRDD(dynamicContext);
        initLookupPosition(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayLookupClosure(this.lookup);

        JavaRDD<Item> resultRDD = childRDD.flatMap(transformation);
        return resultRDD;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext newContext = NativeQueryRuntimePlan.generate(
            this.iterator,
            nativeClauseContext
        );
        if (newContext != NativeClauseContext.NoNativeQuery) {
            if (SequenceType.Arity.OneOrMore.isSubtypeOf(newContext.getResultingType().getArity())) {
                return NativeClauseContext.NoNativeQuery;
            }
            // check if the key has variable dependencies inside the FLWOR expression
            // in that case we switch over to UDF
            Map<Name, DynamicContext.VariableDependency> keyDependencies =
                VariableDependencyRuntimePlan.get(this.getChild(1));
            // we use nativeClauseContext that contains the top level schema
            DataType schema = nativeClauseContext.getSchema();
            StructType structSchema;
            if (schema instanceof StructType structType) {
                structSchema = structType;
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
                if (getConfiguration().doStaticAnalysis()) {
                    throw new UnexpectedStaticTypeException(
                            "This is not a sequence of arrays,"
                                + " so that the lookup will always result in the empty sequence no matter what. "
                                + "Fortunately Rumble was able to catch this. This is probably a typo? Please check the spelling and try again.",
                            ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                            getMetadata()
                    );
                }
                LogManager.getLogger("ArrayLookupIterator")
                    .warn(
                        "Array lookup on a DataFrame that does not an array type. Empty sequence returned."
                    );
                return NativeClauseContext.NoNativeQuery;
            }

            schema = newContext.getSchema();
            if (!(schema instanceof ArrayType arraySchema)) {
                if (getConfiguration().doStaticAnalysis()) {
                    throw new UnexpectedStaticTypeException(
                            "This is not a sequence of arrays,"
                                + " so that the lookup will always result in the empty sequence no matter what. "
                                + "Fortunately Rumble was able to catch this. This is probably a typo? Please check the spelling and try again.",
                            ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                            getMetadata()
                    );
                }
                LogManager.getLogger("ArrayLookupIterator")
                    .warn(
                        "Array lookup on a DataFrame that does not an array type. Empty sequence returned."
                    );
                return NativeClauseContext.NoNativeQuery;
            }
            newContext.setResultingType(
                new SequenceType(
                        resultType.getArrayContentFacet(),
                        SequenceType.Arity.OneOrZero
                )
            );
            newContext.setSchema(arraySchema.elementType());
            newContext.setResultingQuery("get(" + newContext.getResultingQuery() + " ," + (this.lookup - 1) + ")");
        }
        return newContext;
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame childDataFrame = ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.getChild(0), context);
        initLookupPosition(context);
        String array = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        boolean isObject = childDataFrame.getItemType().isObjectItemType();
        boolean hasNonObjectJSONiqItem = isObject
            && childDataFrame.getItemType()
                .getObjectKeysFacet()
                .contains(SparkSessionManager.nonObjectJSONiqItemColumnName);

        // Check if metadata columns exist
        String[] fieldNames = childDataFrame.getDataFrame().schema().fieldNames();
        boolean hasRowIdColumn = Arrays.asList(fieldNames).contains(SparkSessionManager.rowIdColumnName);
        boolean hasMutabilityColumn = Arrays.asList(fieldNames).contains(SparkSessionManager.mutabilityLevelColumnName);
        boolean hasPathInColumn = Arrays.asList(fieldNames).contains(SparkSessionManager.pathInColumnName);
        boolean hasTableLocationColumn = Arrays.asList(fieldNames)
            .contains(
                SparkSessionManager.tableLocationColumnName
            );

        if (childDataFrame.getItemType().isArrayItemType()) {
            ItemType elementType = childDataFrame.getItemType().getArrayContentFacet();
            if (elementType.isObjectItemType()) {
                // element is an object, preserve metadata columns if they exist
                if (hasRowIdColumn && hasMutabilityColumn && hasPathInColumn && hasTableLocationColumn) {
                    return childDataFrame.evaluateSQL(
                        String.format(
                            "SELECT `%s`.*, `%s`, `%s`, CONCAT(`%s`, '[%s]') AS `%s`, `%s` FROM (SELECT `%s`[%s] as `%s`, `%s`, `%s`, `%s`, `%s` FROM %s WHERE size(`%s`) >= %s)",
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            SparkSessionManager.rowIdColumnName,
                            SparkSessionManager.mutabilityLevelColumnName,
                            SparkSessionManager.pathInColumnName,
                            Integer.toString(this.lookup - 1),
                            SparkSessionManager.pathInColumnName,
                            SparkSessionManager.tableLocationColumnName,
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            Integer.toString(this.lookup - 1),
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            SparkSessionManager.rowIdColumnName,
                            SparkSessionManager.mutabilityLevelColumnName,
                            SparkSessionManager.pathInColumnName,
                            SparkSessionManager.tableLocationColumnName,
                            array,
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            Integer.toString(this.lookup)
                        ),
                        elementType
                    );
                }
                // Otherwise just return the object
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT `%s`.* FROM (SELECT `%s`[%s] as `%s` FROM %s WHERE size(`%s`) >= %s)",
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        Integer.toString(this.lookup - 1),
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        array,
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        Integer.toString(this.lookup)
                    ),
                    elementType
                );
            }
            return childDataFrame.evaluateSQL(
                String.format(
                    "SELECT `%s`[%s] as `%s` FROM %s WHERE size(`%s`) >= %s",
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    Integer.toString(this.lookup - 1),
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    array,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    Integer.toString(this.lookup)
                ),
                elementType
            );
        } else if (
            hasNonObjectJSONiqItem
                &&
                childDataFrame.getItemType()
                    .getObjectContentFacet(SparkSessionManager.nonObjectJSONiqItemColumnName)
                    .getType()
                    .isArrayItemType()
                && childDataFrame.getItemType()
                    .getObjectKeysFacet()
                    .contains(SparkSessionManager.tableLocationColumnName)
        ) {
            ItemType elementType = childDataFrame.getItemType()
                .getObjectContentFacet(SparkSessionManager.nonObjectJSONiqItemColumnName)
                .getType()
                .getArrayContentFacet();
            String sql;
            HomogeneousItemDataFrame res;
            if (elementType.isObjectItemType()) {
                sql = String.format(
                    "SELECT `%s`.*, `%s`, `%s`, `%s`, `%s` FROM (SELECT `%s`[%s] as `%s`, `%s`, `%s`, CONCAT(`%s`, '[%s]') AS `%s`, `%s` FROM %s WHERE size(`%s`) >= %s)",
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    SparkSessionManager.rowIdColumnName,
                    SparkSessionManager.mutabilityLevelColumnName,
                    SparkSessionManager.pathInColumnName,
                    SparkSessionManager.tableLocationColumnName,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    Integer.toString(this.lookup - 1),
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    SparkSessionManager.rowIdColumnName,
                    SparkSessionManager.mutabilityLevelColumnName,
                    SparkSessionManager.pathInColumnName,
                    Integer.toString(this.lookup - 1),
                    SparkSessionManager.pathInColumnName,
                    SparkSessionManager.tableLocationColumnName,
                    array,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    Integer.toString(this.lookup)
                );
                res = childDataFrame.evaluateSQL(sql, elementType);
            } else {
                sql = String.format(
                    "SELECT `%s`[%s] as `%s`, `%s`, `%s`, CONCAT(`%s`, '[%s]') AS `%s`, `%s` FROM %s WHERE size(`%s`) >= %s",
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    this.lookup - 1,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    SparkSessionManager.rowIdColumnName,
                    SparkSessionManager.mutabilityLevelColumnName,
                    SparkSessionManager.pathInColumnName,
                    this.lookup - 1,
                    SparkSessionManager.pathInColumnName,
                    SparkSessionManager.tableLocationColumnName,
                    array,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    this.lookup
                );
                Dataset<Row> df = childDataFrame.getDataFrame().sparkSession().sql(sql);
                res = new HomogeneousItemDataFrame(df, elementType);
            }
            return res;
        }
        if (getConfiguration().doStaticAnalysis()) {
            throw new UnexpectedStaticTypeException(
                    "This is not a sequence of arrays,"
                        + " so that the lookup will always result in the empty sequence no matter what. "
                        + "Fortunately Rumble was able to catch this. This is probably a typo? Please check the spelling and try again.",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    getMetadata()
            );
        }
        LogManager.getLogger("ArrayLookupIterator")
            .warn(
                "Array lookup on a DataFrame that does not an array type. Empty sequence returned."
            );
        return HomogeneousItemDataFrame.emptyDataFrame();
    }
}
