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

package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;

public class MultiplicativeOperationIterator extends BinaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;
    Item _left;
    Item _right;

    public MultiplicativeOperationIterator(
            RuntimeIterator left,
            RuntimeIterator right,
            OperationalExpressionBase.Operator operator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(left, right, operator, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this._leftIterator.open(this._currentDynamicContextForLocalExecution);
        this._rightIterator.open(this._currentDynamicContextForLocalExecution);

        // if left or right equals empty sequence, return empty sequence
        if (!this._leftIterator.hasNext() || !this._rightIterator.hasNext()) {
            this._hasNext = false;
        } else {
            this._left = this._leftIterator.next();
            this._right = this._rightIterator.next();
            this.checkBinaryOperation(this._left, this._right, this._operator);
            this._hasNext = true;
            if (this._leftIterator.hasNext() || this._rightIterator.hasNext())
                throw new UnexpectedTypeException(
                        "Sequence of more than one item can not be promoted to parameter type atomic of function add()",
                        getMetadata()
                );
        }
        this._leftIterator.close();
        this._rightIterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                switch (this._operator) {
                    case MUL:
                        return this._left.multiply(this._right);
                    case DIV:
                        return this._left.divide(this._right);
                    case IDIV:
                        return this._left.idivide(this._right);
                    case MOD:
                        return this._left.modulo(this._right);
                    default:
                        throw new IteratorFlowException("Non recognized multiplicative operator.", getMetadata());
                }
            } catch (RuntimeException e) {
                throw new UnexpectedTypeException(
                        " \""
                            + this._operator.name().toLowerCase()
                            + "\": operation not possible with parameters of type \""
                            + ItemTypes.getItemTypeName(this._left.getClass().getSimpleName())
                            + "\" and \""
                            + ItemTypes.getItemTypeName(this._right.getClass().getSimpleName())
                            + "\"",
                        getMetadata()
                );
            }
        }
        throw new IteratorFlowException("Multiplicative expression has non numeric args", getMetadata());
    }

}
