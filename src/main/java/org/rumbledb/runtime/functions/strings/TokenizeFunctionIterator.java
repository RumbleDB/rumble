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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class TokenizeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private String[] results;
    private Item nextResult;
    private int currentPosition;
    private boolean lastEmptyString;

    public TokenizeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item next() {
        if (this.nextResult != null) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tokenize function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.results = null;
        this.currentPosition = -1;
        setNextResult();
    }

    public void setNextResult() {
        if (this.results == null) {
            // Getting first parameter
            RuntimeIterator stringIterator = this.children.get(0);
            String input = null;
            String separator = null;
            Item stringItem = stringIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            if (stringItem == null) {
                this.hasNext = false;
                return;
            }
            input = stringItem.getStringValue();

            // Getting second parameter
            if (this.children.size() == 1) {
                separator = "\\s+";
            } else {
                RuntimeIterator separatorIterator = this.children.get(1);
                separatorIterator.open(this.currentDynamicContextForLocalExecution);
                if (!separatorIterator.hasNext()) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
                stringItem = separatorIterator.next();
                if (separatorIterator.hasNext()) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
                separatorIterator.close();
                if (!stringItem.isString()) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
                try {
                    separator = stringItem.getStringValue();
                } catch (Exception e) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
            }
            this.results = input.split(separator);
            this.currentPosition = 0;
            if (this.children.size() == 1 && this.results.length != 0 && this.results[0].equals("")) {
                this.currentPosition++;
            }
            this.lastEmptyString = this.children.size() == 2 && input.matches(".*" + separator + "$");
        }
        if (this.currentPosition < this.results.length) {
            this.nextResult = ItemFactory.getInstance().createStringItem(this.results[this.currentPosition]);
            this.currentPosition++;
            this.hasNext = true;
        } else if (this.lastEmptyString) {
            this.nextResult = ItemFactory.getInstance().createStringItem(new String(""));
            this.hasNext = true;
            this.lastEmptyString = false;
        } else {
            this.hasNext = false;
        }
    }
}
