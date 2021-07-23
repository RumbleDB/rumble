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
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import sparksoniq.spark.SparkSessionManager;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MinFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item result;
    private Item nextResult;
    private Item minResult;
    private ItemComparator comparator;


    public MinFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.iterator = this.children.get(0);
        this.comparator = new ItemComparator(
                true,
                new InvalidArgumentTypeException(
                        "Min expression input error. Input has to be non-null atomics of matching types",
                        getMetadata()
                )
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {

        if (this.children.size() == 2) {
            String collation = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new DefaultCollationException("Wrong collation parameter", getMetadata());
            }
        }

        if (!this.iterator.isRDDOrDataFrame()) {

            List<Item> results = this.iterator.materialize(context);
            if (results.size() == 0) {
                return null;
            }

            try {
                System.out.println("before open context");
                open(context);
                System.out.println("before  next");
                return next();
                //return itemTypePromotion(Collections.min(results, comparator));

            } catch (RumbleException e) {
                RumbleException ex = new InvalidArgumentTypeException(
                        "Min expression input error. Input has to be non-null atomics of matching types.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
        }

        if (this.iterator.isDataFrame()) {
            JSoundDataFrame df = this.iterator.getDataFrame(context);
            if (df.isEmptySequence()) {
                return null;
            }
            df.createOrReplaceTempView("input");
            JSoundDataFrame minDF = df.evaluateSQL(
                String.format(
                    "SELECT MIN(`%s`) as `%s` FROM input",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName
                ),
                df.getItemType()
            );
            return itemTypePromotion(minDF.getExactlyOneItem());
        }

        JavaRDD<Item> rdd = this.iterator.getRDD(context);
        if (rdd.isEmpty()) {
            return null;
        }
        this.result = rdd.min(comparator); // this
        return itemTypePromotion(this.result);

    }

    @Override
    public void open(DynamicContext dynamicContext) {
        this.iterator.open(dynamicContext);
        System.out.println("next it  ");
        this.minResult = this.iterator.next();
        System.out.println("setNextRes");
        setNextResult();
    }

    @Override
    public Item next() {
        //Item result = this.nextResult;
        //setNextResult();
        System.out.println("NEXT");
        System.out.println(this.minResult);
        System.out.println(this.nextResult);
        return this.minResult;
        //throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "min function", getMetadata());
    }

    private void setNextResult() {
        this.nextResult = null;
        while (this.iterator.hasNext()) {
            System.out.println("init candidate");
            Item candidateItem = this.iterator.next();
            System.out.println("candidate is ");
            System.out.println(candidateItem.getIntValue());
            long c = ComparisonIterator.compareItems(candidateItem, this.minResult, ComparisonOperator.GC_LT, getMetadata());
            System.out.println("c");
            System.out.println(c);
            if (c < 0) {
                this.minResult = candidateItem;
                this.nextResult = this.minResult;
            }
        }
        System.out.println("out loop");
        if (this.nextResult == null) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
        next();
    }

    @Override
    public void reset(DynamicContext dynamicContext) {
        this.iterator.reset(dynamicContext);
        setNextResult();
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public void close() {
        this.iterator.close();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MIN);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

    private Item itemTypePromotion(Item item) {
        if (item.isAnyURI()) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        if (item.isFloat()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        if (item.isDecimal()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        return item;
    }

}
