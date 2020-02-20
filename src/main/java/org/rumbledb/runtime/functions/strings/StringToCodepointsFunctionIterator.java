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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class StringToCodepointsFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item nextResult;
    private String input;
    private int currentPosition;

    public StringToCodepointsFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
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
            RuntimeIterator stringIterator = this.children.get(0);
            stringIterator.open(this.currentDynamicContextForLocalExecution);
            if (!stringIterator.hasNext()) {
                this.hasNext = false;
                stringIterator.close();
                return;
            }
            this.input = null;
            Item stringItem = stringIterator.next();
            if (stringIterator.hasNext())
                throw new UnexpectedTypeException(
                        "Parameter of string-to-codepoints must be a string or the empty sequence.",
                        getMetadata()
                );
            stringIterator.close();
            if (!stringItem.isString())
                throw new UnexpectedTypeException(
                        "Parameter of string-to-codepoints must be a string or the empty sequence.",
                        getMetadata()
                );
            try {
                this.input = stringItem.getStringValue();
                stringIterator.close();
            } catch (Exception e) {
                throw new UnexpectedTypeException(
                        "Parameter of string-to-codepoints must be a string or the empty sequence.",
                        getMetadata()
                );
            }

            this.currentPosition = 0;
        }
        if (this.currentPosition < this.input.length()) {
            this.nextResult = ItemFactory.getInstance().createIntegerItem(this.input.codePointAt(this.currentPosition));
            this.currentPosition++;
            this.hasNext = true;
        } else {
            this.hasNext = false;
        }
    }
}
