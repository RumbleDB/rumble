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
import org.rumbledb.exceptions.InvalidRegexPatternException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ReplaceFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public ReplaceFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item stringItem = this.children.get(0)
                    .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

            Item patternStringItem = this.children.get(1)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            String pattern = patternStringItem.getStringValue();
            if ("".matches(pattern)) {
                throw new MatchesEmptyStringException("'" + pattern + "' matches empty string",
                        getMetadata());
            }
            try {
                Pattern.compile(pattern);
            } catch (PatternSyntaxException e) {
                throw new InvalidRegexPatternException(e.getDescription(),
                        getMetadata());
                //TODO: Figure out why exception isn't being properly caught.
            }

            Item replacementStringItem = this.children.get(2)
                    .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            String replacement = replacementStringItem.getStringValue();

            String result = stringItem.getStringValue().replaceAll(pattern, replacement);

            return ItemFactory.getInstance().createStringItem(result);
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " replace function",
                    getMetadata()
            );
    }
}
