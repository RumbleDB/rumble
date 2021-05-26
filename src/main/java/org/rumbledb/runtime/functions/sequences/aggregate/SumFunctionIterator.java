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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import sparksoniq.spark.SparkSessionManager;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SumFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private Item item;

    public SumFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.item = computeSum(
            zeroElement(),
            this.children.get(0),
            this.currentDynamicContextForLocalExecution,
            getMetadata()
        );
        this.hasNext = this.item != null;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            return this.item;
        } else {
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata()
            );
        }
    }

    private Item zeroElement() {
        if (this.children.size() > 1) {
            return this.children.get(1).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
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
        List<Item> results = iterator.materialize(context);
        if (results.isEmpty()) {
            return zeroElement;
        }

        Item result = results.get(0);
        for (int i = 1; i < results.size(); ++i) {
            Item sum = AdditiveOperationIterator.processItem(result, results.get(i), false);
            if (sum == null) {
                throw new InvalidArgumentTypeException(
                        " \"+\": operation not possible with parameters of type \""
                            + result.getDynamicType().toString()
                            + "\" and \""
                            + results.get(i).getDynamicType().toString()
                            + "\"",
                        metadata
                );
            }
            result = sum;
        }
        return result;
    }

    private static Item computeRDD(
            Item zeroElement,
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        JavaRDD<Item> rdd = iterator.getRDD(context);
        Item result = rdd.fold(zeroElement, new SumClosure(metadata));
        return result;
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
        df.createOrReplaceTempView("input");
        JSoundDataFrame summedDF = df.evaluateSQL(
                String.format(
                    "SELECT SUM(`%s`) as `%s` FROM input",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName
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
}
