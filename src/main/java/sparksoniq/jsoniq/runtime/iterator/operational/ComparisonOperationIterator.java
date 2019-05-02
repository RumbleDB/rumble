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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */
package sparksoniq.jsoniq.runtime.iterator.operational;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase.Operator;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Arrays;


public class ComparisonOperationIterator extends BinaryOperationBaseIterator {

    private Item _nextResult;
    private boolean _isValueComparison;
    private Item _left;
    private Item _right;



    public static final Operator[] valueComparisonOperators = new Operator[] {
            Operator.VC_GE, Operator.VC_GT, Operator.VC_EQ, Operator.VC_NE, Operator.VC_LE, Operator.VC_LT};
    public static final Operator[] generalComparisonOperators = new Operator[] {
            Operator.GC_GE, Operator.GC_GT, Operator.GC_EQ, Operator.GC_NE, Operator.GC_LE, Operator.GC_LT};


    public ComparisonOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                       OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public AtomicItem next() {
        if(this.hasNext()){
            this._hasNext = false;

            // use stored values for value comparison
            if (_isValueComparison) {
                return comparePair(_left, _right);
            } else {
                // fetch all values and perform comparison
                ArrayList<Item> left = new ArrayList<>();
                ArrayList<Item> right = new ArrayList<>();
                while (_leftIterator.hasNext())
                    left.add(_leftIterator.next());
                while (_rightIterator.hasNext())
                    right.add(_rightIterator.next());

                _leftIterator.close();
                _rightIterator.close();

                return compareAllPairs(left, right);
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);

        // value comparison may return an empty sequence
        if (Arrays.asList(valueComparisonOperators).contains(this._operator)){
            // if EMPTY SEQUENCE - eg. () or ((),())
            // this check is added here to provide lazy evaluation: eg. () eq (2,3) = () instead of exception
            if (!(_leftIterator.hasNext() && _rightIterator.hasNext())) {
                this._hasNext = false;
            } else {
                _left = _leftIterator.next();
                _right = _rightIterator.next();

                // value comparison doesn't support more than 1 items
                if (_leftIterator.hasNext() || _rightIterator.hasNext()) {
                    throw new UnexpectedTypeException("Invalid args. Value comparison can't be performed on sequences with more than 1 items", getMetadata());
                }

                _isValueComparison = true;
                this._hasNext = true;
            }
        }
        else if (Arrays.asList(generalComparisonOperators).contains(this._operator)) {
            // general comparison always returns a boolean
            this._hasNext = true;
        }

        _leftIterator.close();
        _rightIterator.close();
    }

    /**
     * Function to compare two lists of items one by one with each other.
     * @param left  item list of left iterator
     * @param right item list of right iterator
     * @return true if a single match is found, false if no matches. Given an empty sequence, false is returned.
     */
    public BooleanItem compareAllPairs (ArrayList<Item> left, ArrayList<Item> right) {
        for (Item l:left) {
            for (Item r:right) {
                BooleanItem result = comparePair(l, r);
                if (result.getBooleanValue() == true)
                    return result;
            }
        }
        return new BooleanItem(false);
    }

    public BooleanItem comparePair(Item left, Item right) {

        if (left.isArray() || right.isArray()) {
            throw new NonAtomicKeyException("Invalid args. Comparison can't be performed on array type", getMetadata().getExpressionMetadata());
        }
        else if (left.isObject() || right.isObject()) {
            throw new NonAtomicKeyException("Invalid args. Comparison can't be performed on object type", getMetadata().getExpressionMetadata());
        }
        if (left.isNull() || right.isNull()) {
            return compareItems(left, right);
        }
        else if (Item.isNumeric(left)) {
            if (!Item.isNumeric(right))
                throw new UnexpectedTypeException("Invalid args for numerics comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        }
        else if (left.isString()) {
            if (!(right.isString()))
                throw new UnexpectedTypeException("Invalid args for string comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        }
        else if (left.isBoolean()) {
            if (!right.isBoolean())
                throw new UnexpectedTypeException("Invalid args for boolean comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareItems(left, right);
        }
        else {
            throw new IteratorFlowException("Invalid comparison expression", getMetadata());
        }
    }

    public BooleanItem compareItems (Item left, Item right) {
        int comparison = Item.compareItems(left, right);
        switch (this._operator) {
            case VC_EQ:
            case GC_EQ:
                return new BooleanItem(comparison == 0);
            case VC_NE:
            case GC_NE:
                return new BooleanItem(comparison != 0);
            case VC_LT:
            case GC_LT:
                return new BooleanItem(comparison < 0);
            case VC_LE:
            case GC_LE:
                return new BooleanItem(comparison < 0 || comparison == 0);
            case VC_GT:
            case GC_GT:
                return new BooleanItem(comparison > 0);
            case VC_GE:
            case GC_GE:
                return new BooleanItem(comparison > 0 || comparison == 0);
            default:
        }
        throw new IteratorFlowException("Unrecognized operator found", getMetadata());
    }
}
