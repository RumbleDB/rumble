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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;
import sparksoniq.spark.SparkSessionManager;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SumFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private Item item;

    public SumFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.item = computeSum(
            zeroElement(context),
            this.children.get(0),
            context,
            getMetadata()
        );
        if (this.item == null) {
            return null;
        }
        return this.item;
    }

    private Item zeroElement(DynamicContext context) {
        if (this.children.size() > 1) {
            return this.children.get(1).materializeFirstItemOrNull(context);
        } else {
            return ItemFactory.getInstance().createIntegerItem(BigInteger.ZERO);
        }
    }

    public static Item computeSum(
            Item zeroElement,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        if (iterator.isDataFrame()) {
            return computeDataFrame(
                zeroElement,
                iterator,
                context,
                metadata
            );
        } else if (iterator.isRDDOrDataFrame()) {
            return computeRDD(
                zeroElement,
                iterator,
                context,
                metadata
            );
        } else {
            return computeLocally(
                zeroElement,
                iterator,
                context,
                metadata
            );
        }
    }

    private static Item computeLocally(
            Item zeroElement,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        iterator.open(context);

        Item result = null;
        while (iterator.hasNext()) {
            Item nextValue = iterator.next();
            if (result == null) {
                result = nextValue;
            } else {
                Item sum = AdditiveOperationIterator.processItem(result, nextValue, false);
                if (sum == null) {
                    throw new InvalidArgumentTypeException(
                            " \"+\": operation not possible with parameters of type \""
                                + result.getDynamicType().toString()
                                + "\" and \""
                                + nextValue.getDynamicType().toString()
                                + "\"",
                            metadata
                    );
                }
                result = sum;
            }
        }
        if (result == null) {
            result = zeroElement;
        }
        iterator.close();
        return result;
    }

    private static Item computeRDD(
            Item zeroElement,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        JavaRDD<Item> rdd = iterator.getRDD(context);
        if (rdd.count() == 0) {
            return zeroElement;
        }
        return rdd.reduce(new SumClosure(metadata));
    }

    private static Item computeDataFrame(
            Item zeroElement,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        JSoundDataFrame df = iterator.getDataFrame(context);
        if (df.isEmptySequence()) {
            return zeroElement;
        }
        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        JSoundDataFrame summedDF = df.evaluateSQL(
            String.format(
                "SELECT SUM(`%s`) as `%s` FROM %s",
                SparkSessionManager.nonObjectJSONiqItemColumnName,
                SparkSessionManager.nonObjectJSONiqItemColumnName,
                input
            ),
            df.getItemType()
        );
        return summedDF.getExactlyOneItem();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.SUM);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childContext = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!childContext.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childContext.getResultingType().getArity())) {
            return new NativeClauseContext(
                    childContext,
                    String.format(
                        "aggregate(%s, decimal(0), (x, y) -> decimal(x + y))",
                        childContext.getResultingQuery()
                    ),
                    SequenceType.INTEGER
            );
        }
        // each row contains a single value
        return childContext;
    }
}
