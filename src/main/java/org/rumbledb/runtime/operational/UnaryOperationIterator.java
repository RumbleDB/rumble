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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;

public class UnaryOperationIterator extends UnaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;

    public UnaryOperationIterator(
            RuntimeIterator child,
            OperationalExpressionBase.Operator operator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, operator, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            this.child.open(this.currentDynamicContextForLocalExecution);
            Item child = this.child.next();
            this.child.close();

            if (this.operator == OperationalExpressionBase.Operator.MINUS) {
                if (child.isNumeric()) {
                    if (child.isInteger()) {
                        return ItemFactory.getInstance().createIntegerItem(-1 * child.getIntegerValue());
                    }
                    if (child.isDouble()) {
                        return ItemFactory.getInstance().createDoubleItem(-1 * child.getDoubleValue());
                    }
                    if (child.isDecimal()) {
                        return ItemFactory.getInstance()
                            .createDecimalItem(child.getDecimalValue().multiply(new BigDecimal(-1)));
                    }
                }
                throw new UnexpectedTypeException(
                        "Unary expression has non numeric args "
                            +
                            child.serialize(),
                        getMetadata()
                );
            } else {
                return child;
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());

    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.child.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.child.hasNext();
        this.child.close();
    }

}
