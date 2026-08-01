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

package org.rumbledb.runtime.functions.sequences.aggregate;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.spark.SparkSessionManager;

public class MaxFunctionIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String CODEPOINT_COLLATION =
        "http://www.w3.org/2005/xpath-functions/collation/codepoint";

    private final RuntimePlan<Item> iterator;

    public MaxFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (!this.iterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return ExtremumLocalEvaluation.max(
                this.iterator,
                getCollationPlan(),
                context,
                getMetadata()
            );
        }
        validateCollation(context);

        if (this.iterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            HomogeneousItemDataFrame df = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
                this.iterator,
                context
            );
            if (df.isEmptySequence()) {
                return null;
            }
            ItemType maxType;
            if (
                df.getItemType().isObjectItemType()
                    && df.getItemType().getObjectKeysFacet().contains(SparkSessionManager.tableLocationColumnName)
            ) {
                maxType = df.getItemType()
                    .getObjectContentFacet(SparkSessionManager.nonObjectJSONiqItemColumnName)
                    .getType();
            } else {
                maxType = df.getItemType();
            }
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            HomogeneousItemDataFrame maxDF = df.evaluateSQL(
                String.format(
                    "SELECT MAX(`%s`) as `%s` FROM %s",
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    input
                ),
                maxType
            );
            return itemTypePromotion(maxDF.getExactlyOneItem());
        }

        JavaRDD<Item> rdd = this.iterator.getRDD(context);
        if (rdd.isEmpty()) {
            return null;
        }
        return rdd.max(
            new ItemComparator(
                    false,
                    new InvalidArgumentTypeException(
                            "Max expression input error. Input has to be non-null atomics of matching types",
                            getMetadata()
                    )
            )
        );
    }

    private RuntimePlan<Item> getCollationPlan() {
        return this.getChildren().size() > 1 ? this.getChild(1) : null;
    }

    private void validateCollation(DynamicContext context) {
        RuntimePlan<Item> collationPlan = getCollationPlan();
        if (collationPlan == null) {
            return;
        }
        Item collation = collationPlan.materializeFirstOrNull(context);
        if (!CODEPOINT_COLLATION.equals(collation.getStringValue())) {
            throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
        }
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.getChild(0) instanceof VariableReferenceIterator expression) {
            Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
            result.put(expression.getVariableName(), DynamicContext.VariableDependency.MAX);
            return result;
        }
        return super.getVariableDependencies();
    }

    private static Item itemTypePromotion(Item item) {
        if (item.isAnyURI()) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        if (item.isFloat() || item.isDecimal()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        return item;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.getChildren().size() > 1) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext nativeChildQuery = NativeQueryRuntimePlan.generate(
            this.getChild(0),
            nativeClauseContext
        );
        if (nativeChildQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!SequenceType.Arity.OneOrMore.isSubtypeOf(nativeChildQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                nativeChildQuery,
                "array_max(" + nativeChildQuery.getResultingQuery() + ")",
                nativeChildQuery.getResultingType()
        );
    }
}
