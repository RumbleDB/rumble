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

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase.Operator;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ObjectConstructorRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.ArrayList;
import java.util.Arrays;


public class ComparisonOperationIterator extends BinaryOperationBaseIterator {

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
        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);

        BooleanItem result = null;

        if (_leftIterator instanceof ObjectConstructorRuntimeIterator
                || _rightIterator instanceof ObjectConstructorRuntimeIterator)
        {
            throw new NonAtomicKeyException("Invalid args. Comparison can't be performed on object type", getMetadata().getExpressionMetadata());
        }
        else if (Arrays.asList(valueComparisonOperators).contains(this._operator)){
            Item left = _leftIterator.next();
            Item right = _rightIterator.next();

            // value comparison doesn't support more than 1 items
            if (_leftIterator.hasNext() || _rightIterator.hasNext())
            {
                throw new UnexpectedTypeException("Invalid args. Value comparison can't be performed on sequences with more than 1 items", getMetadata());
            }
            _leftIterator.close();
            _rightIterator.close();
            this._hasNext = false;

            result = comparePair(left, right);
        }
        else if (Arrays.asList(generalComparisonOperators).contains(this._operator)) {
            ArrayList<Item> left = new ArrayList<>();
            ArrayList<Item> right = new ArrayList<>();
            while (_leftIterator.hasNext())
                left.add(_leftIterator.next());
            while (_rightIterator.hasNext())
                right.add(_rightIterator.next());

            _leftIterator.close();
            _rightIterator.close();
            this._hasNext = false;

            result = compareAllPairs(left, right);
        }

        if (result == null)
            throw new IteratorFlowException("Invalid comparison expression", getMetadata());
        else
            return result;
    }

    public BooleanItem compareAllPairs (ArrayList<Item> left, ArrayList<Item> right) {
        for (Item l:left) {
            for (Item r:right) {
                BooleanItem result = comparePair(l, r);
                if (result != null && result.getBooleanValue() == true)
                    return result;
            }
        }
        return new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
    }

    public BooleanItem comparePair(Item left, Item right) {
        if (Item.isNumeric(left)) {
            if (!Item.isNumeric(left) || !Item.isNumeric(right))
                throw new UnexpectedTypeException("Invalid args for numerics comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareNumerics(left, right);

        }
        if (left instanceof StringItem) {
            if (!(right instanceof StringItem))
                throw new UnexpectedTypeException("Invalid args for string comparison " + left.serialize() +
                        ", " + right.serialize(), getMetadata());
            return compareStrings(left, right);
        }

        return null;
    }

    public BooleanItem compareNumerics(Item left, Item right)
    {
        double l = Item.getNumericValue(left, Double.class);
        double r = Item.getNumericValue(right, Double.class);
        switch (this._operator) {
            case VC_EQ:
            case GC_EQ:
                return new BooleanItem(l == r, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_NE:
            case GC_NE:
                return new BooleanItem(l != r, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LT:
            case GC_LT:
                return new BooleanItem(l < r, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LE:
            case GC_LE:
                return new BooleanItem(l <= r, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GT:
            case GC_GT:
                return new BooleanItem(l > r, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GE:
            case GC_GE:
                return new BooleanItem(l >= r, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        return null;
    }

    public BooleanItem compareStrings(Item left, Item right)
    {
        String l = ((StringItem) left).getStringValue();
        String r = ((StringItem) right).getStringValue();
        switch (this._operator) {
            case VC_EQ:
            case GC_EQ:
                return new BooleanItem(l.equals(r), ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_NE:
            case GC_NE:
                return new BooleanItem(!l.equals(r), ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LT:
            case GC_LT:
                return new BooleanItem(l.compareTo(r) < 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_LE:
            case GC_LE:
                return new BooleanItem(l.compareTo(r) < 0 || l.compareTo(r) == 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GT:
            case GC_GT:
                return new BooleanItem(l.compareTo(r) > 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
            case VC_GE:
            case GC_GE:
                return new BooleanItem(l.compareTo(r) > 0 || l.compareTo(r) == 0, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        return null;
    }
}
