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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.logics.AndOperationIterator;
import org.rumbledb.runtime.logics.NotOperationIterator;
import org.rumbledb.runtime.logics.OrOperationIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.TypeMappings;
import scala.Tuple2;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.*;

public class PredicateIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new PredicateLocalCursor(
                this.iterator,
                this.filter,
                this.isBooleanOnlyFilter,
                context,
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator iterator;
    private final RuntimeIterator filter;
    private final boolean isBooleanOnlyFilter;


    public PredicateIterator(
            RuntimeIterator sequence,
            RuntimeIterator filterExpression,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence, filterExpression), staticContext);
        this.iterator = sequence;
        this.filter = filterExpression;
        this.isBooleanOnlyFilter = isBooleanOnlyFilter();
    }

    public RuntimeIterator sequenceIterator() {
        return this.iterator;
    }

    public RuntimeIterator predicateIterator() {
        return this.filter;
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

    private static final class PredicateLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator inputPlan;
        private final RuntimeIterator filterPlan;
        private final boolean booleanOnlyFilter;
        private final DynamicContext context;
        private DynamicContext filterContext;
        private LocalCursor<Item> inputCursor;
        private Item nextResult;
        private long position;

        private PredicateLocalCursor(
                RuntimeIterator inputPlan,
                RuntimeIterator filterPlan,
                boolean booleanOnlyFilter,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.inputPlan = inputPlan;
            this.filterPlan = filterPlan;
            this.booleanOnlyFilter = booleanOnlyFilter;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.filterContext = new DynamicContext(this.context);
            if (this.filterPlan.getVariableDependencies().containsKey(Name.CONTEXT_COUNT)) {
                this.filterContext.getVariableValues().setLast(countInput());
            }
            this.position = 0;
            this.inputCursor = this.inputPlan.createLocalCursor(this.context);
            advance();
        }

        private long countInput() {
            long count = 0;
            try (LocalCursor<Item> cursor = this.inputPlan.createLocalCursor(this.context)) {
                while (cursor.hasNext()) {
                    cursor.next();
                    count++;
                }
            }
            return count;
        }

        private void advance() {
            this.nextResult = null;
            while (this.inputCursor.hasNext()) {
                Item item = this.inputCursor.next();
                this.position++;
                this.filterContext.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, Collections.singletonList(item));
                if (!this.booleanOnlyFilter) {
                    this.filterContext.getVariableValues().setPosition(this.position);
                }
                Item filterResult;
                try {
                    filterResult = this.filterPlan.materializeAtMostOne(this.filterContext);
                } catch (MoreThanOneItemException e) {
                    throw new InvalidArgumentTypeException(
                            "Effective boolean value not defined for sequences of more than one atomic item. Sequence must be singleton.",
                            this.filterPlan.getMetadata()
                    );
                }
                if (matches(filterResult)) {
                    this.nextResult = item;
                    break;
                }
            }
            this.filterContext.getVariableValues().removeVariable(Name.CONTEXT_ITEM);
        }

        private boolean matches(Item filterResult) {
            if (filterResult == null) {
                return false;
            }
            if (!filterResult.isNumeric()) {
                return filterResult.getEffectiveBooleanValue();
            }
            BigDecimal numericValue;
            if (filterResult.isInt() || filterResult.isInteger()) {
                numericValue = new BigDecimal(filterResult.getIntegerValue());
            } else if (filterResult.isDecimal()) {
                numericValue = filterResult.getDecimalValue();
            } else if (filterResult.isDouble()) {
                double value = filterResult.getDoubleValue();
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    return false;
                }
                numericValue = BigDecimal.valueOf(value);
            } else if (filterResult.isFloat()) {
                float value = filterResult.getFloatValue();
                if (Float.isNaN(value) || Float.isInfinite(value)) {
                    return false;
                }
                numericValue = new BigDecimal(Float.toString(value));
            } else {
                return false;
            }
            return numericValue.stripTrailingZeros().scale() <= 0
                && numericValue.toBigIntegerExact().equals(BigInteger.valueOf(this.position));
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more predicate results are available.");
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
            }
            if (this.filterContext != null) {
                this.filterContext.getVariableValues().removeVariable(Name.CONTEXT_ITEM);
            }
            this.inputCursor = null;
            this.filterContext = null;
            this.nextResult = null;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator iterator = this.getChild(0);
        RuntimeIterator filter = this.getChild(1);
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
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame childDataFrame = this.getChild(0).getDataFrame(context);
        RuntimeIterator filter = this.getChild(1);
        NativeClauseContext nativeClauseContext = new NativeClauseContext(
                FLWOR_CLAUSES.FILTER,
                childDataFrame.getDataFrame().schema(),
                context
        );
        NativeClauseContext nativeQuery = NativeClauseContext.NoNativeQuery;
        if (getConfiguration().nativeExecution()) {
            nativeQuery = filter.generateNativeQuery(nativeClauseContext);
        }
        if (nativeQuery == NativeClauseContext.NoNativeQuery || !this.isBooleanOnlyFilter) {
            if (this.isBooleanOnlyFilter) {
                String left = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
                List<FlworDataFrameColumn> UDFcolumns = FlworDataFrameUtils.getColumns(
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
                String UDFParameters = FlworDataFrameUtils.getUDFParametersFromColumns(UDFcolumns);
                return childDataFrame.evaluateSQL(
                    String.format(
                        "SELECT * FROM %s WHERE predicate(%s) = 'true'",
                        left,
                        UDFParameters
                    ),
                    childDataFrame.getItemType()
                );
            } else {
                Dataset<Row> zippedChildDataFrame = FlworDataFrameUtils.zipWithIndex(
                    childDataFrame,
                    1L
                );
                String left = FlworDataFrameUtils.createTempView(zippedChildDataFrame);
                List<FlworDataFrameColumn> UDFcolumns = FlworDataFrameUtils.getColumns(
                    zippedChildDataFrame.schema(),
                    null,
                    null,
                    null
                );
                List<FlworDataFrameColumn> originalcolumns = FlworDataFrameUtils.getColumns(
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
                String UDFParameters = FlworDataFrameUtils.getUDFParametersFromColumns(UDFcolumns);
                String projection = FlworDataFrameUtils.getSQLColumnProjection(originalcolumns, false);
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

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.filter.getVariableDependencies());
        result.remove(Name.CONTEXT_ITEM);
        result.putAll(this.iterator.getVariableDependencies());
        return result;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (!(this.iterator instanceof ArrayUnboxingIterator arrayUnboxingIterator)) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext arrayReferenceQuery = arrayUnboxingIterator.generateArrayReferenceQuery(
            nativeClauseContext
        );
        if (arrayReferenceQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        arrayReferenceQuery.setSchema(
            ((StructType) arrayReferenceQuery.getSchema()).add(
                SparkSessionManager.nonObjectJSONiqItemColumnName,
                TypeMappings.getDataFrameDataTypeFromItemType(
                    arrayReferenceQuery.getResultingType().getItemType().getArrayContentFacet(),
                    this.staticContext
                )
            )
        );
        FLWOR_CLAUSES previousType = arrayReferenceQuery.getClauseType();
        arrayReferenceQuery.setClauseType(FLWOR_CLAUSES.FILTER);
        NativeClauseContext filterQuery = this.filter.generateNativeQuery(arrayReferenceQuery);
        if (
            filterQuery == NativeClauseContext.NoNativeQuery
                || filterQuery.getResultingType().getItemType() != BuiltinTypesCatalogue.booleanItem
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        arrayReferenceQuery.setClauseType(previousType);
        if (
            filterQuery != NativeClauseContext.NoNativeQuery
        ) {
            String resultingQuery = " explode ( filter ( "
                + arrayReferenceQuery.getResultingQuery()
                + ", "
                + "`"
                + SparkSessionManager.nonObjectJSONiqItemColumnName
                + "`"
                + " -> "
                + filterQuery.getResultingQuery()
                + " ) ) ";
            return new NativeClauseContext(
                    filterQuery,
                    resultingQuery,
                    arrayReferenceQuery.getResultingType()
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }
}
