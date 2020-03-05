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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.base.BinaryOperationBaseIterator;
import org.rumbledb.types.ItemType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

public class MultiplicativeOperationIterator extends BinaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;
    Item left;
    Item right;

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

        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        this.rightIterator.open(this.currentDynamicContextForLocalExecution);

        // if left or right equals empty sequence, return empty sequence
        if (!this.leftIterator.hasNext() || !this.rightIterator.hasNext()) {
            this.hasNext = false;
        } else {
            this.left = this.leftIterator.next();
            this.right = this.rightIterator.next();
            this.checkBinaryOperation(this.left, this.right, this.operator);
            this.hasNext = true;
            if (this.leftIterator.hasNext() || this.rightIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "Sequence of more than one item can not be promoted to parameter type atomic of function add()",
                        getMetadata()
                );
            }
        }
        this.leftIterator.close();
        this.rightIterator.close();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                switch (this.operator) {
                    case MUL:
                        return this.left.multiply(this.right);
                    case DIV:
                        return this.left.divide(this.right);
                    case IDIV:
                        return this.left.idivide(this.right);
                    case MOD:
                        return this.left.modulo(this.right);
                    default:
                        throw new IteratorFlowException("Non recognized multiplicative operator.", getMetadata());
                }
            } catch (RuntimeException e) {
                throw new UnexpectedTypeException(
                        " \""
                            + this.operator.name().toLowerCase()
                            + "\": operation not possible with parameters of type \""
                            + ItemType.convertClassNameToItemTypeName(this.left.getClass().getSimpleName())
                            + "\" and \""
                            + ItemType.convertClassNameToItemTypeName(this.right.getClass().getSimpleName())
                            + "\"",
                        getMetadata()
                );
            }
        }
        throw new IteratorFlowException("Multiplicative expression has non numeric args", getMetadata());
    }

}
