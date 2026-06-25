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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MatchesFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public MatchesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item regexpItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (stringItem == null) {
            stringItem = ItemFactory.getInstance().createStringItem("");
        }
        String pattern = regexpItem.getStringValue();
        boolean quote = false;
        int flags = 0;
        if (this.children.size() == 3) {
            Item flagsItem = this.children.get(2)
                .materializeFirstItemOrNull(context);
            if (flagsItem != null) {
                for (char flag : flagsItem.getStringValue().toCharArray()) {
                    switch (flag) {
                        case 'i':
                            flags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
                            break;
                        case 'm':
                            flags |= Pattern.MULTILINE;
                            break;
                        case 's':
                            flags |= Pattern.DOTALL;
                            break;
                        case 'x':
                            flags |= Pattern.COMMENTS;
                            break;
                        case 'q':
                            quote = true;
                            break;
                        default:
                            throw new InvalidRegexPatternException(
                                    "Invalid regular expression flag: " + flag,
                                    getMetadata()
                            );
                    }
                }
            }
        }
        if (quote) {
            pattern = Pattern.quote(pattern);
        }
        if ((flags & Pattern.CASE_INSENSITIVE) != 0 && !quote) {
            if (containsCaseInsensitiveCharacterClassEdgeCase(pattern)) {
                pattern = normalizeCaseInsensitivePattern(pattern);
                flags &= ~(Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
        }
        try {
            Matcher matcher = Pattern.compile(pattern, flags).matcher(stringItem.getStringValue());
            boolean result = matcher.find();
            return ItemFactory.getInstance().createBooleanItem(result);
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexPatternException(
                    e.getDescription(),
                    getMetadata()
            );
        }
    }

    private static boolean containsCaseInsensitiveCharacterClassEdgeCase(String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                if ((pattern.charAt(i + 1) == 'p' || pattern.charAt(i + 1) == 'P') && i + 2 < pattern.length()) {
                    i = skipUnicodeEscape(pattern, i);
                } else {
                    i++;
                }
                continue;
            }
            if (current == '[') {
                int endIndex = findCharacterClassEnd(pattern, i);
                if (endIndex > i + 1) {
                    int contentStart = i + 1;
                    if (pattern.charAt(contentStart) == '^') {
                        return true;
                    }
                    if (pattern.substring(contentStart, endIndex).contains("-[")) {
                        return true;
                    }
                }
                i = endIndex;
            }
        }
        return false;
    }

    private static int findCharacterClassEnd(String pattern, int startIndex) {
        int index = startIndex + 1;
        boolean firstToken = true;
        while (index < pattern.length()) {
            if (pattern.charAt(index) == ']' && !firstToken) {
                return index;
            }
            if (pattern.charAt(index) == '\\' && index + 1 < pattern.length()) {
                EscapedToken escapedToken = readEscapedToken(pattern, index);
                index = escapedToken.endIndex + 1;
            } else {
                index++;
            }
            firstToken = false;
        }
        return startIndex;
    }

    private static String normalizeCaseInsensitivePattern(String pattern) {
        StringBuilder result = new StringBuilder(pattern.length());
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                EscapedToken escapedToken = readEscapedToken(pattern, i);
                result.append(escapedToken.text);
                i = escapedToken.endIndex;
                continue;
            }
            if (current == '[') {
                ClassRewriteResult classResult = rewriteCharacterClass(pattern, i);
                result.append(classResult.rewrittenClass);
                i = classResult.endIndex;
                continue;
            }
            if (Character.isHighSurrogate(current) && i + 1 < pattern.length() && Character.isLowSurrogate(pattern.charAt(i + 1))) {
                int codePoint = Character.toCodePoint(current, pattern.charAt(i + 1));
                result.append(expandLiteralCodePoint(codePoint));
                i++;
                continue;
            }
            if (hasCaseVariant(current)) {
                result.append(expandLiteralCodePoint(current));
                continue;
            }
            result.append(current);
        }
        return result.toString();
    }

    private static ClassRewriteResult rewriteCharacterClass(String pattern, int startIndex) {
        StringBuilder result = new StringBuilder();
        result.append('[');
        int index = startIndex + 1;
        if (index < pattern.length() && pattern.charAt(index) == '^') {
            result.append('^');
            index++;
        }

        boolean firstToken = true;
        while (index < pattern.length()) {
            if (pattern.charAt(index) == ']' && !firstToken) {
                result.append(']');
                return new ClassRewriteResult(result.toString(), index);
            }

            if (
                !firstToken
                    && pattern.charAt(index) == '-'
                    && index + 1 < pattern.length()
                    && pattern.charAt(index + 1) == '['
            ) {
                ClassRewriteResult nestedClass = rewriteCharacterClass(pattern, index + 1);
                result.append("&&[^");
                result.append(stripCharacterClassBrackets(nestedClass.rewrittenClass));
                result.append(']');
                index = nestedClass.endIndex + 1;
                continue;
            }

            ClassToken token = readClassToken(pattern, index, firstToken);
            index = token.nextIndex;
            firstToken = false;

            if (index < pattern.length() && pattern.charAt(index) == '-' && index + 1 < pattern.length()) {
                ClassToken rightToken = readClassToken(pattern, index + 1, false);
                if (isRange(token, rightToken, pattern, index + 1)) {
                    result.append(expandRange(token.codePoint, rightToken.codePoint));
                    index = rightToken.nextIndex;
                    continue;
                }
            }

            result.append(expandSimpleToken(token));
        }

        return new ClassRewriteResult(result.toString(), pattern.length() - 1);
    }

    private static boolean isRange(ClassToken left, ClassToken right, String pattern, int rightStartIndex) {
        if (!left.isLiteral || !right.isLiteral) {
            return false;
        }
        if (left.text.length() != 1 || right.text.length() != 1) {
            return false;
        }
        if (pattern.charAt(rightStartIndex - 1) != '-') {
            return false;
        }
        return right.text.charAt(0) != ']';
    }

    private static String expandSimpleToken(ClassToken token) {
        if (!token.isLiteral || token.text.length() != 1) {
            return token.text;
        }
        char current = token.text.charAt(0);
        if (!hasCaseVariant(current)) {
            return token.text;
        }
        return expandLiteralCodePoint(current);
    }

    private static String expandRange(int left, int right) {
        if (left > right) {
            return new StringBuilder().appendCodePoint(left).append('-').appendCodePoint(right).toString();
        }
        if (isAsciiUppercase(left) && isAsciiUppercase(right)) {
            return new StringBuilder()
                .appendCodePoint(left)
                .append('-')
                .appendCodePoint(right)
                .appendCodePoint(toAsciiLower(left))
                .append('-')
                .appendCodePoint(toAsciiLower(right))
                .toString();
        }
        if (isAsciiLowercase(left) && isAsciiLowercase(right)) {
            return new StringBuilder()
                .appendCodePoint(left)
                .append('-')
                .appendCodePoint(right)
                .appendCodePoint(toAsciiUpper(left))
                .append('-')
                .appendCodePoint(toAsciiUpper(right))
                .toString();
        }
        int lowerLeft = Character.toLowerCase(left);
        int lowerRight = Character.toLowerCase(right);
        if ((lowerLeft != left || lowerRight != right) && lowerLeft <= lowerRight) {
            return new StringBuilder()
                .appendCodePoint(left)
                .append('-')
                .appendCodePoint(right)
                .appendCodePoint(lowerLeft)
                .append('-')
                .appendCodePoint(lowerRight)
                .toString();
        }
        int upperLeft = Character.toUpperCase(left);
        int upperRight = Character.toUpperCase(right);
        if ((upperLeft != left || upperRight != right) && upperLeft <= upperRight) {
            return new StringBuilder()
                .appendCodePoint(left)
                .append('-')
                .appendCodePoint(right)
                .appendCodePoint(upperLeft)
                .append('-')
                .appendCodePoint(upperRight)
                .toString();
        }
        return new StringBuilder().appendCodePoint(left).append('-').appendCodePoint(right).toString();
    }

    private static String expandLiteralCodePoint(int codePoint) {
        int lower = Character.toLowerCase(codePoint);
        int upper = Character.toUpperCase(codePoint);
        if (lower == upper) {
            return new StringBuilder().appendCodePoint(codePoint).toString();
        }
        return new StringBuilder()
            .append('[')
            .appendCodePoint(lower)
            .appendCodePoint(upper)
            .append(']')
            .toString();
    }

    private static boolean hasCaseVariant(int codePoint) {
        return Character.toLowerCase(codePoint) != Character.toUpperCase(codePoint);
    }

    private static ClassToken readClassToken(String pattern, int startIndex, boolean firstToken) {
        char current = pattern.charAt(startIndex);
        if ((current == ']' || current == '-') && firstToken) {
            return new ClassToken(String.valueOf(current), true, current, startIndex + 1);
        }
        if (current == '\\' && startIndex + 1 < pattern.length()) {
            EscapedToken escapedToken = readEscapedToken(pattern, startIndex);
            return new ClassToken(escapedToken.text, false, -1, escapedToken.endIndex + 1);
        }
        return new ClassToken(String.valueOf(current), true, current, startIndex + 1);
    }

    private static EscapedToken readEscapedToken(String pattern, int startIndex) {
        if (
            startIndex + 2 < pattern.length()
                && (pattern.charAt(startIndex + 1) == 'p' || pattern.charAt(startIndex + 1) == 'P')
                && pattern.charAt(startIndex + 2) == '{'
        ) {
            int endIndex = skipUnicodeEscape(pattern, startIndex);
            return new EscapedToken(pattern.substring(startIndex, endIndex + 1), endIndex);
        }
        return new EscapedToken(pattern.substring(startIndex, startIndex + 2), startIndex + 1);
    }

    private static String stripCharacterClassBrackets(String characterClass) {
        if (
            characterClass.length() >= 2
                && characterClass.charAt(0) == '['
                && characterClass.charAt(characterClass.length() - 1) == ']'
        ) {
            return characterClass.substring(1, characterClass.length() - 1);
        }
        return characterClass;
    }

    private static int skipUnicodeEscape(String pattern, int startIndex) {
        int index = startIndex + 3;
        while (index < pattern.length() && pattern.charAt(index) != '}') {
            index++;
        }
        return Math.min(index, pattern.length() - 1);
    }

    private static boolean isAsciiLetter(int codePoint) {
        return isAsciiUppercase(codePoint) || isAsciiLowercase(codePoint);
    }

    private static boolean isAsciiUppercase(int codePoint) {
        return codePoint >= 'A' && codePoint <= 'Z';
    }

    private static boolean isAsciiLowercase(int codePoint) {
        return codePoint >= 'a' && codePoint <= 'z';
    }

    private static int toAsciiLower(int codePoint) {
        return codePoint + ('a' - 'A');
    }

    private static int toAsciiUpper(int codePoint) {
        return codePoint - ('a' - 'A');
    }

    private static final class ClassRewriteResult {
        private final String rewrittenClass;
        private final int endIndex;

        private ClassRewriteResult(String rewrittenClass, int endIndex) {
            this.rewrittenClass = rewrittenClass;
            this.endIndex = endIndex;
        }
    }

    private static final class ClassToken {
        private final String text;
        private final boolean isLiteral;
        private final int codePoint;
        private final int nextIndex;

        private ClassToken(String text, boolean isLiteral, int codePoint, int nextIndex) {
            this.text = text;
            this.isLiteral = isLiteral;
            this.codePoint = codePoint;
            this.nextIndex = nextIndex;
        }
    }

    private static final class EscapedToken {
        private final String text;
        private final int endIndex;

        private EscapedToken(String text, int endIndex) {
            this.text = text;
            this.endIndex = endIndex;
        }
    }
}
