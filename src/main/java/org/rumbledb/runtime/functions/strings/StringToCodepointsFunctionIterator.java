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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class StringToCodepointsFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item nextResult;
    private String input;
    private int currentPosition;

    public StringToCodepointsFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "string-to-codepoints function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.input = null;
        this.currentPosition = -1;
        setNextResult();
    }

    public void setNextResult() {
        if (this.input == null) {
            // Getting first parameter
            Item stringItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

            if (stringItem == null) {
                this.hasNext = false;
                return;
            }
            this.input = stringItem.getStringValue();
            if (this.input.equals("")) {
                this.hasNext = false;
                return;
            }

            this.currentPosition = 0;
        }
        if (this.currentPosition < this.input.length()) {
            this.nextResult = ItemFactory.getInstance().createIntItem(this.input.codePointAt(this.currentPosition));
            this.currentPosition = this.input.offsetByCodePoints(this.currentPosition, 1);
            this.hasNext = true;
        } else {
            this.hasNext = false;
        }
    }
}
