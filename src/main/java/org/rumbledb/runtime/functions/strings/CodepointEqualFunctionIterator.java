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

package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class CodepointEqualFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private String input1;
    private String input2;
    private Item nextResult;

    public CodepointEqualFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.input1 = null;
        this.input2 = null;
        setNextResult();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " codepoint-equal function",
                    getMetadata()
            );
    }

    public void setNextResult() {
        if (this.input1 == null || this.input2 == null) {
            Item operandOneItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            Item operandTwoItem = this.children.get(1)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            if (operandOneItem == null || operandTwoItem == null) {
                this.hasNext = false;
                return;
            }
            this.hasNext = true;
            this.input1 = operandOneItem.getStringValue();
            this.input2 = operandTwoItem.getStringValue();
            this.nextResult = ItemFactory.getInstance().createBooleanItem(this.input1.equals(this.input2));
        } else {
            this.hasNext = false;
        }
    }
}
