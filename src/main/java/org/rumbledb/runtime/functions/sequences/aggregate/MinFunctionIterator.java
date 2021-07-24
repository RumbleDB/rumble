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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MinFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item nextResult;
    private ItemType returnType;
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

        // if (!this.iterator.isRDDOrDataFrame()) {
        try {
            return this.nextResult;
        } catch (RumbleException e) {
            RumbleException ex = new InvalidArgumentTypeException(
                    "Min expression input error. Input has to be non-null atomics of matching types.",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
        // }
        /*
         * if (this.iterator.isDataFrame()) {
         * JSoundDataFrame df = this.iterator.getDataFrame(context);
         * if (df.isEmptySequence()) {
         * return null;
         * }
         * df.createOrReplaceTempView("input");
         * JSoundDataFrame minDF = df.evaluateSQL(
         * String.format(
         * "SELECT MIN(`%s`) as `%s` FROM input",
         * SparkSessionManager.atomicJSONiqItemColumnName,
         * SparkSessionManager.atomicJSONiqItemColumnName
         * ),
         * df.getItemType()
         * );
         * return itemTypePromotion(minDF.getExactlyOneItem());
         * }
         * 
         * JavaRDD<Item> rdd = this.iterator.getRDD(context);
         * if (rdd.isEmpty()) {
         * return null;
         * }
         * this.result = rdd.min(comparator); // this
         * return itemTypePromotion(this.result);
         */
    }


    @Override
    public void open(DynamicContext dynamicContext) {
        super.open(dynamicContext);
        this.iterator.open(dynamicContext);
        this.hasNext = this.iterator.hasNext();
        if (!this.hasNext) {
            return;
        }
        this.nextResult = this.iterator.next();
        this.returnType = this.nextResult.getDynamicType();
        setNextResult();
    }

    @Override
    public Item next() {
        if (this.nextResult == null) {
            return null;
        }
        if (this.hasNext) {
            this.hasNext = false;
            return CastIterator.castItemToType(this.nextResult, this.returnType, getMetadata());
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "min function", getMetadata());
    }

    private void setNextResult() {
        while (this.iterator.hasNext()) {
            Item candidateItem = this.iterator.next();
            promoteType(candidateItem);
            int c = this.comparator.compare(candidateItem, this.nextResult);
            if (c < 0) {
                this.nextResult = candidateItem;
            }
        }
        this.iterator.close();
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



    private void promoteType(Item candidateItem) {
        if (this.returnType != BuiltinTypesCatalogue.doubleItem && candidateItem.isFloat()) {
            this.returnType = BuiltinTypesCatalogue.floatItem;
        }
        if (candidateItem.isDouble()) {
            this.returnType = BuiltinTypesCatalogue.doubleItem;
        }
        if (candidateItem.isString()) {
            this.returnType = BuiltinTypesCatalogue.stringItem;
        }
    }



}
