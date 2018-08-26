/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.jsoniq.runtime.iterator.operational;

import com.sun.org.apache.xpath.internal.operations.Bool;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase.Operator;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ArrayRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.NullRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ObjectConstructorRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Arrays;


public class ComparisonOperationIterator extends BinaryOperationBaseIterator {
    public static final Operator[] valueComparisonOperators = new Operator[]{
            Operator.VC_GE, Operator.VC_GT, Operator.VC_EQ, Operator.VC_NE, Operator.VC_LE, Operator.VC_LT};
    public static final Operator[] generalComparisonOperators = new Operator[]{
            Operator.GC_GE, Operator.GC_GT, Operator.GC_EQ, Operator.GC_NE, Operator.GC_LE, Operator.GC_LT};

    private AtomicItem result;


    public ComparisonOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                       OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public AtomicItem next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);

        if (Arrays.asList(valueComparisonOperators).contains(this._operator)) {

            // if EMPTY SEQUENCE - eg. () or ((),())
            // this check is added here to provide lazy evaluation: eg. () eq (2,3) = () instead of exception
            if (!(_leftIterator.hasNext() && _rightIterator.hasNext())) {
                result = null;
            } else {
                Item left = _leftIterator.next();
                Item right = _rightIterator.next();

                // value comparison doesn't support more than 1 items
                if (_leftIterator.hasNext() || _rightIterator.hasNext()) {
                    throw new UnexpectedTypeException("Invalid args. Value comparison can't be performed on sequences with more than 1 items", getMetadata());
                } else {
                    result = comparePair(left, right);
                }
            }
            _leftIterator.close();
            _rightIterator.close();
        } else if (Arrays.asList(generalComparisonOperators).contains(this._operator)) {
            ArrayList<Item> left = new ArrayList<>();
            ArrayList<Item> right = new ArrayList<>();
            while (_leftIterator.hasNext())
                left.add(_leftIterator.next());
            while (_rightIterator.hasNext())
                right.add(_rightIterator.next());

            _leftIterator.close();
            _rightIterator.close();

            result = compareAllPairs(left, right);
        }

        if (result == null) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    /**
     * Function to compare two lists of items one by one with each other.
     *
     * @param left  item list of left iterator
     * @param right item list of right iterator
     * @return true if a single match is found, false if no matches. Given an empty sequence, false is returned.
     */
    public BooleanItem compareAllPairs(ArrayList<Item> left, ArrayList<Item> right) {
        for (Item l : left) {
            for (Item r : right) {
                BooleanItem result = comparePair(l, r);
                if (result.getBooleanValue() == true)
                    return result;
            }
        }
        return new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
    }

    public BooleanItem comparePair(Item left, Item right) {

        if (left instanceof ArrayItem || right instanceof ArrayItem) {
            throw new NonAtomicKeyException("Invalid args. Comparison can't be performed on array type", getMetadata().getExpressionMetadata());
        } else if (left instanceof ObjectItem || right instanceof ObjectItem) {
            throw new NonAtomicKeyException("Invalid args. Comparison can't be performed on object type", getMetadata().getExpressionMetadata());
        }
        if (left instanceof NullItem || right instanceof NullItem) {
            return compareItems(left, right);
        } else if (Item.isNumeric(left)) {
            if (!Item.isNumeric(right))
                throw new UnexpectedTypeException("Invalid args for numerics comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        } else if (left instanceof StringItem) {
            if (!(right instanceof StringItem))
                throw new UnexpectedTypeException("Invalid args for string comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        } else if (left instanceof BooleanItem) {
            if (!(right instanceof BooleanItem))
                throw new UnexpectedTypeException("Invalid args for boolean comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        } else {
            throw new IteratorFlowException("Invalid comparison expression", getMetadata());
        }
    }

    public BooleanItem compareItems(Item left, Item right) {
        int comparison = Item.compareItems(left, right);
        switch (this._operator) {
            case VC_EQ:
            case GC_EQ:
                return new BooleanItem(comparison == 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_NE:
            case GC_NE:
                return new BooleanItem(comparison != 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LT:
            case GC_LT:
                return new BooleanItem(comparison < 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LE:
            case GC_LE:
                return new BooleanItem(comparison < 0 || comparison == 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GT:
            case GC_GT:
                return new BooleanItem(comparison > 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GE:
            case GC_GE:
                return new BooleanItem(comparison > 0 || comparison == 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        throw new IteratorFlowException("Unrecognized operator found", getMetadata());
    }
}
