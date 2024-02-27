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
import org.rumbledb.exceptions.InvalidRegexPatternException;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.exceptions.InvalidReplacementStringException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class ReplaceFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ReplaceFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        Item patternStringItem = this.children.get(1)
            .materializeFirstItemOrNull(context);

        if (patternStringItem == null) {
            return null;
        }
        String pattern = patternStringItem.getStringValue();
        Pattern p;

        try {
            p = Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexPatternException(
                    e.getDescription(),
                    getMetadata()
            );
        }
        if ("".matches(pattern)) {
            throw new MatchesEmptyStringException(
                    "'" + pattern + "' matches empty string",
                    getMetadata()
            );
        }

        Item replacementStringItem = this.children.get(2)
            .materializeFirstItemOrNull(context);
        String replacement = replacementStringItem.getStringValue();
        if (!(checkReplacementStringForValidity(replacement))) {
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

        Matcher m = p.matcher(input);
        return ItemFactory.getInstance().createStringItem(m.replaceAll(replacement));

    }

    private static boolean checkReplacementStringForValidity(String repl) {
        int i = 0;
        Pattern p = Pattern.compile("\\d");

        while (i < repl.length()) {
            if (repl.charAt(i) == '\\') { // '\' must be followed by another '\' or '$'
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
