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

package org.rumbledb.runtime.operational;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.AtomicItemType;

import java.util.ArrayList;
import java.util.Arrays;


public class ComparisonOperationIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private Item left;
    private Item right;
    private ComparisonExpression.ComparisonOperator comparisonOperator;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;


    public ComparisonOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightIterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.comparisonOperator = comparisonOperator;
    }

    public ComparisonExpression.ComparisonOperator getComparisonOperator() {
        return this.comparisonOperator;
    }

    public boolean isValueEquality() {
        return this.comparisonOperator.equals(ComparisonExpression.ComparisonOperator.VC_EQ);
    }

    public RuntimeIterator getLeftIterator() {
        return this.leftIterator;
    }

    public RuntimeIterator getRightIterator() {
        return this.rightIterator;
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this.hasNext = false;

            // use stored values for value comparison
            if (this.comparisonOperator.isValueComparison()) {
                return comparePair(this.left, this.right);
            } else {
                // fetch all values and perform comparison
                ArrayList<Item> left = new ArrayList<>();
                ArrayList<Item> right = new ArrayList<>();
                while (this.leftIterator.hasNext()) {
                    left.add(this.leftIterator.next());
                }
                while (this.rightIterator.hasNext()) {
                    right.add(this.rightIterator.next());
                }

                this.leftIterator.close();
                this.rightIterator.close();

                return compareAllPairs(left, right);
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        this.rightIterator.open(this.currentDynamicContextForLocalExecution);

        // value comparison may return an empty sequence
        if (this.comparisonOperator.isValueComparison()) {
            // if EMPTY SEQUENCE - eg. () or ((),())
            // this check is added here to provide lazy evaluation: eg. () eq (2,3) = () instead of exception
            if (!(this.leftIterator.hasNext() && this.rightIterator.hasNext())) {
                this.hasNext = false;
            } else {
                this.left = this.leftIterator.next();
                this.right = this.rightIterator.next();

                // value comparison doesn't support more than 1 items
                if (this.leftIterator.hasNext() || this.rightIterator.hasNext()) {
                    throw new UnexpectedTypeException(
                            "Invalid args. Value comparison can't be performed on sequences with more than 1 items",
                            getMetadata()
                    );
                }

                this.hasNext = true;
            }
        } else {
            // general comparison always returns a boolean
            this.hasNext = true;
        }

        this.leftIterator.close();
        this.rightIterator.close();
    }

    /**
     * Function to compare two lists of items one by one with each other.
     *
     * @param left item list of left iterator
     * @param right item list of right iterator
     * @return true if a single match is found, false if no matches. Given an empty sequence, false is returned.
     */
    private Item compareAllPairs(ArrayList<Item> left, ArrayList<Item> right) {
        for (Item l : left) {
            for (Item r : right) {
                Item result = comparePair(l, r);
                if (result.getBooleanValue()) {
                    return result;
                }
            }
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }

    private Item comparePair(Item left, Item right) {

        if (left.isArray() || right.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on array type",
                    getMetadata()
            );
        } else if (left.isObject() || right.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on object type",
                    getMetadata()
            );
        } else if (left.isFunction() || right.isFunction()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on function type",
                    getMetadata()
            );
        }

        if (!left.isAtomic()) {
            throw new IteratorFlowException("Invalid comparison expression", getMetadata());
        }
        return left.compareItem(right, this.comparisonOperator, getMetadata());
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.comparisonOperator.isValueComparison()) {
            NativeClauseContext leftResult = this.leftIterator.generateNativeQuery(nativeClauseContext);
            NativeClauseContext rightResult = this.rightIterator.generateNativeQuery(nativeClauseContext);

            if (leftResult == NativeClauseContext.NoNativeQuery || rightResult == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }

            // TODO: once done type system do proper comparison
            if(!(leftResult.getResultingType().isNumeric() && rightResult.getResultingType().isNumeric() || leftResult.getResultingType() == rightResult.getResultingType())){
                return NativeClauseContext.NoNativeQuery;
            }

            String operator = " = ";
            switch (this.comparisonOperator.name()) {
                case "eq":
                    operator = " = ";
                    break;
                case "ne":
                    operator = " <> ";
                    break;
                case "le":
                    operator = " <= ";
                    break;
                case "lt":
                    operator = " < ";
                    break;
                case "ge":
                    operator = " >= ";
                    break;
                case "gt":
                    operator = " > ";
                    break;
            }
            String query = "( " + leftResult.getResultingQuery() + operator + rightResult.getResultingQuery() + " )";
            return new NativeClauseContext(nativeClauseContext, query, AtomicItemType.booleanItem);
        } else {
            return NativeClauseContext.NoNativeQuery;
        }
    }
}
