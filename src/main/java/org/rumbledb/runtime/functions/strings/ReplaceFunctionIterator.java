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
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.exceptions.InvalidReplacementStringException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReplaceFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(this.getChildren(), context, this::evaluate, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item evaluate(ComputedLocalCursor.Arguments<Item> arguments) {
        Item stringItem = arguments.get(0);
        Item patternStringItem = arguments.get(1);

        if (patternStringItem == null) {
            return null;
        }
        String pattern = patternStringItem.getStringValue();
        String flags = null;
        if (arguments.size() == 4) {
            Item flagsItem = arguments.get(3);
            if (flagsItem != null) {
                flags = flagsItem.getStringValue();
            }
        }
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(pattern, flags, getMetadata());
        if (RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern())) {
            throw new MatchesEmptyStringException(
                    "'" + compiledRegex.getEffectivePattern() + "' matches empty string",
                    getMetadata()
            );
        }

        Item replacementStringItem = arguments.get(2);
        String replacement = replacementStringItem.getStringValue();
        if (compiledRegex.isQuote()) {
            replacement = Matcher.quoteReplacement(replacement);
        } else if (!(checkReplacementStringForValidity(replacement))) {
            throw new InvalidReplacementStringException(
                    "'" + replacement + "' contains a disallowed sequence of characters",
                    getMetadata()
            );
        }

        String input;
        if (stringItem == null) {
            input = "";
        } else {
            input = stringItem.getStringValue();
        }

        Matcher m = compiledRegex.getPattern().matcher(input);
        return ItemFactory.getInstance().createStringItem(m.replaceAll(replacement));

    }

    private static boolean checkReplacementStringForValidity(String repl) {
        int i = 0;
        Pattern p = Pattern.compile("\\d");

        while (i < repl.length()) {
            if (repl.charAt(i) == '\\') { // '\' must be followed by another '\' or '$'
                if (i + 1 >= repl.length()) {
                    return false;
                }
                if ((!(repl.charAt(i + 1) == '\\')) && (!(repl.charAt(i + 1) == '$'))) {
                    return false;
                }
                i += 2;
            } else if (repl.charAt(i) == '$') { // '$' must always be followed by a digit
                if ((i + 1 >= repl.length()) || !(p.matcher(String.valueOf(repl.charAt(i + 1))).matches())) {
                    return false;
                }
                i += 2;
            } else {
                i++;
            }
        }
        return true;
    }
}
