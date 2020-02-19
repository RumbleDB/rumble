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
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.base.BinaryOperationBaseIterator;

import sparksoniq.jsoniq.ExecutionMode;

public class OrOperationIterator extends BinaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;

    public OrOperationIterator(
            RuntimeIterator left,
            RuntimeIterator right,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(left, right, OperationalExpressionBase.Operator.OR, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        this.rightIterator.open(this.currentDynamicContextForLocalExecution);

        boolean leftEffectiveBooleanValue = getEffectiveBooleanValue(this.leftIterator);
        boolean rightEffectiveBooleanValue = getEffectiveBooleanValue(this.rightIterator);

        this.leftIterator.close();
        this.rightIterator.close();
        this.hasNext = false;
        return ItemFactory.getInstance().createBooleanItem((leftEffectiveBooleanValue || rightEffectiveBooleanValue));
    }
}
