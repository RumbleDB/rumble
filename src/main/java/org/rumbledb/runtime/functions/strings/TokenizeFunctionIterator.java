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

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class TokenizeFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public TokenizeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new TokenizeLocalCursor(this.getChildren(), context, getMetadata());
    }

    private static String[] tokenize(String input, String separator, String flags, ExceptionMetadata metadata) {
        if (separator == null) {
            return RegexPatternUtils.tokenizeOnXmlWhitespace(input);
        }
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(separator, flags, metadata);
        if (RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern())) {
            throw new MatchesEmptyStringException(
                    "'" + compiledRegex.getEffectivePattern() + "' matches empty string", metadata);
        }
        return RegexPatternUtils.tokenize(input, compiledRegex.getPattern());
    }

    private static final class TokenizeLocalCursor extends AbstractLocalCursor<Item> {

        private final List<ItemRuntimePlan> arguments;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private String[] results;
        private int position;

        private TokenizeLocalCursor(
                List<ItemRuntimePlan> arguments, DynamicContext context, ExceptionMetadata metadata) {
            super(metadata);
            this.arguments = arguments;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            Item inputItem = this.arguments.get(0).materializeFirstOrNull(this.context);
            if (inputItem == null) {
                this.results = new String[0];
                return;
            }
            String separator = null;
            String flags = null;
            if (this.arguments.size() > 1) {
                separator = materializeSeparator();
                if (this.arguments.size() == 3) {
                    Item flagsItem = this.arguments.get(2).materializeFirstOrNull(this.context);
                    flags = flagsItem == null ? null : flagsItem.getStringValue();
                }
            }
            this.results = tokenize(inputItem.getStringValue(), separator, flags, this.metadata);
            this.position = 0;
        }

        private String materializeSeparator() {
            try (Cursor<Item> cursor = this.arguments.get(1).getCursor(this.context)) {
                if (!cursor.hasNext()) {
                    throw invalidSeparator();
                }
                Item separator = cursor.next();
                if (cursor.hasNext() || !separator.isString()) {
                    throw invalidSeparator();
                }
                try {
                    return separator.getStringValue();
                } catch (RuntimeException exception) {
                    throw invalidSeparator();
                }
            }
        }

        private UnexpectedTypeException invalidSeparator() {
            return new UnexpectedTypeException("Second parameter of tokenize must be a string.", this.metadata);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.position < this.results.length;
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("Tokenize cursor is exhausted.");
            }
            return ItemFactory.getInstance().createStringItem(this.results[this.position++]);
        }

        @Override
        protected void closeLocal() {
            this.results = null;
        }
    }
}
