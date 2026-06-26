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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexFlagException;
import org.rumbledb.exceptions.InvalidRegexPatternException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class RegexPatternUtils {

    private RegexPatternUtils() {
    }

    public static CompiledRegex compileRegex(String pattern, String flagsString, ExceptionMetadata metadata) {
        boolean quote = false;
        int flags = 0;
        if (flagsString != null) {
            for (char flag : flagsString.toCharArray()) {
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
                        throw new InvalidRegexFlagException(
                                "Invalid regular expression flag: " + flag,
                                metadata
                        );
                }
            }
        }
        if (quote) {
            pattern = Pattern.quote(pattern);
        }
        if ((flags & Pattern.CASE_INSENSITIVE) != 0 && !quote) {
            pattern = normalizeCaseInsensitivePattern(pattern);
            flags &= ~(Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }
        try {
            return new CompiledRegex(Pattern.compile(pattern, flags), quote, pattern);
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexPatternException(
                    e.getDescription(),
                    metadata
            );
        }
    }

    public static boolean matchesEmptyString(CompiledRegex compiledRegex) {
        return new RegexZeroLengthAnalyzer(compiledRegex).canMatchZeroLength();
    }

    private static boolean hasZeroLengthMatch(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.start() == matcher.end()) {
                return true;
            }
        }
        return false;
    }

    public static String[] tokenize(String input, Pattern pattern) {
        if (input.isEmpty()) {
            return new String[0];
        }

        List<String> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        int lastEnd = 0;
        while (matcher.find()) {
            tokens.add(input.substring(lastEnd, matcher.start()));
            lastEnd = matcher.end();
        }
        tokens.add(input.substring(lastEnd));
        return tokens.toArray(new String[0]);
    }

    static String normalizeCaseInsensitivePattern(String pattern) {
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
            if (
                Character.isHighSurrogate(current)
                    && i + 1 < pattern.length()
                    && Character.isLowSurrogate(pattern.charAt(i + 1))
            ) {
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
        StringBuilder result = new StringBuilder();
        if (left > right) {
            result.appendCodePoint(left).append('-').appendCodePoint(right);
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        if (isAsciiUppercase(left) && isAsciiUppercase(right)) {
            result.append(
                new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(toAsciiLower(left))
                    .append('-')
                    .appendCodePoint(toAsciiLower(right))
            );
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        if (isAsciiLowercase(left) && isAsciiLowercase(right)) {
            result.append(
                new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(toAsciiUpper(left))
                    .append('-')
                    .appendCodePoint(toAsciiUpper(right))
            );
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        int lowerLeft = Character.toLowerCase(left);
        int lowerRight = Character.toLowerCase(right);
        if ((lowerLeft != left || lowerRight != right) && lowerLeft <= lowerRight) {
            result.append(
                new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(lowerLeft)
                    .append('-')
                    .appendCodePoint(lowerRight)
            );
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        int upperLeft = Character.toUpperCase(left);
        int upperRight = Character.toUpperCase(right);
        if ((upperLeft != left || upperRight != right) && upperLeft <= upperRight) {
            result.append(
                new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(upperLeft)
                    .append('-')
                    .appendCodePoint(upperRight)
            );
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        result.appendCodePoint(left).append('-').appendCodePoint(right);
        appendSpecialCaseClosure(result, left, right);
        return result.toString();
    }

    private static String expandLiteralCodePoint(int codePoint) {
        int lower = Character.toLowerCase(codePoint);
        int upper = Character.toUpperCase(codePoint);
        if (lower == upper) {
            return new StringBuilder().appendCodePoint(codePoint).toString();
        }
        StringBuilder result = new StringBuilder()
            .append('[')
            .appendCodePoint(lower)
            .appendCodePoint(upper);
        appendLiteralSpecialCaseClosure(result, codePoint);
        return result.append(']').toString();
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

    private static void appendSpecialCaseClosure(StringBuilder result, int left, int right) {
        appendSpecialCaseCodePoint(result, left, right, 0x212A, 'K', 'k');
        appendSpecialCaseCodePoint(result, left, right, 0x017F, 'S', 's');
    }

    private static void appendLiteralSpecialCaseClosure(StringBuilder result, int codePoint) {
        appendLiteralSpecialCaseCodePoint(result, codePoint, 0x212A, 'K', 'k');
        appendLiteralSpecialCaseCodePoint(result, codePoint, 0x017F, 'S', 's');
    }

    private static void appendSpecialCaseCodePoint(
            StringBuilder result,
            int left,
            int right,
            int specialCodePoint,
            int upperEquivalent,
            int lowerEquivalent
    ) {
        if (withinRange(upperEquivalent, left, right) || withinRange(lowerEquivalent, left, right)) {
            result.appendCodePoint(specialCodePoint);
        }
    }

    private static void appendLiteralSpecialCaseCodePoint(
            StringBuilder result,
            int codePoint,
            int specialCodePoint,
            int upperEquivalent,
            int lowerEquivalent
    ) {
        if (
            codePoint == upperEquivalent
                || codePoint == lowerEquivalent
                || codePoint == specialCodePoint
        ) {
            result.appendCodePoint(specialCodePoint);
        }
    }

    private static boolean withinRange(int codePoint, int left, int right) {
        return codePoint >= Math.min(left, right) && codePoint <= Math.max(left, right);
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

    public static final class CompiledRegex {
        private final Pattern pattern;
        private final boolean quote;
        private final String effectivePattern;

        private CompiledRegex(Pattern pattern, boolean quote, String effectivePattern) {
            this.pattern = pattern;
            this.quote = quote;
            this.effectivePattern = effectivePattern;
        }

        public Pattern getPattern() {
            return this.pattern;
        }

        public boolean isQuote() {
            return this.quote;
        }

        public String getEffectivePattern() {
            return this.effectivePattern;
        }
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

    private static final class RegexZeroLengthAnalyzer {
        private static final int MAX_SAMPLES = 8;
        private static final String[] GENERIC_WITNESSES = new String[] {
            "",
            "a",
            "0",
            " ",
            "\n",
            "_",
            ","
        };

        private final CompiledRegex compiledRegex;
        private final int flags;
        private final String source;
        private int index;

        private RegexZeroLengthAnalyzer(CompiledRegex compiledRegex) {
            this.compiledRegex = compiledRegex;
            this.flags = compiledRegex.getPattern().flags();
            this.source = compiledRegex.getEffectivePattern();
            this.index = 0;
        }

        private boolean canMatchZeroLength() {
            try {
                AnalysisResult analysisResult = parseExpression();
                if (this.index < this.source.length()) {
                    return fallbackHeuristic();
                }
                for (ContextSample contextSample : analysisResult.zeroLengthContexts) {
                    if (hasVerifiedZeroLengthMatch(contextSample)) {
                        return true;
                    }
                }
                return false;
            } catch (UnsupportedOperationException e) {
                return fallbackHeuristic();
            }
        }

        private boolean fallbackHeuristic() {
            return hasZeroLengthMatch(this.compiledRegex.getPattern(), "")
                || hasZeroLengthMatch(this.compiledRegex.getPattern(), "a")
                || hasZeroLengthMatch(this.compiledRegex.getPattern(), "\n")
                || hasZeroLengthMatch(this.compiledRegex.getPattern(), "a\nb");
        }

        private boolean hasVerifiedZeroLengthMatch(ContextSample contextSample) {
            List<String> candidates = new ArrayList<>();
            String materialized = contextSample.materialize();
            addSample(candidates, materialized);
            if (!contextSample.mustBeStart) {
                for (String leftPadding : GENERIC_WITNESSES) {
                    if (!leftPadding.isEmpty()) {
                        addSample(candidates, leftPadding + materialized);
                    }
                }
            }
            if (!contextSample.mustBeEnd) {
                int size = candidates.size();
                for (int i = 0; i < size; i++) {
                    String baseCandidate = candidates.get(i);
                    for (String rightPadding : GENERIC_WITNESSES) {
                        if (!rightPadding.isEmpty()) {
                            addSample(candidates, baseCandidate + rightPadding);
                        }
                    }
                }
            }
            for (String candidate : candidates) {
                if (hasZeroLengthMatch(this.compiledRegex.getPattern(), candidate)) {
                    return true;
                }
            }
            return false;
        }

        private AnalysisResult parseExpression() {
            AnalysisResult result = parseSequence();
            while (peek() == '|') {
                this.index++;
                result = result.alternate(parseSequence());
            }
            return result;
        }

        private AnalysisResult parseSequence() {
            List<AnalysisResult> parts = new ArrayList<>();
            while (this.index < this.source.length()) {
                char current = peek();
                if (current == ')' || current == '|') {
                    break;
                }
                parts.add(parseQuantifiedAtom());
            }
            return AnalysisResult.sequence(parts);
        }

        private AnalysisResult parseQuantifiedAtom() {
            AnalysisResult atom = parseAtom();
            while (true) {
                skipCommentsWhitespace();
                Quantifier quantifier = parseQuantifier();
                if (quantifier == null) {
                    return atom;
                }
                atom = atom.quantify(quantifier.min, quantifier.max);
            }
        }

        private AnalysisResult parseAtom() {
            skipCommentsWhitespace();
            if (this.index >= this.source.length()) {
                return AnalysisResult.empty();
            }
            char current = this.source.charAt(this.index);
            if (current == '(') {
                return parseGroup();
            }
            if (current == '[') {
                return parseCharacterClass();
            }
            if (current == '\\') {
                return parseEscapeAtom();
            }
            if (current == '.') {
                this.index++;
                return AnalysisResult.consuming(singleCharacterWitness("."));
            }
            if (current == '^') {
                this.index++;
                return AnalysisResult.zeroWidth(
                    isMultiline()
                        ? List.of(ContextSample.startOfInput(), ContextSample.beforeNewline())
                        : List.of(ContextSample.startOfInput())
                );
            }
            if (current == '$') {
                this.index++;
                return AnalysisResult.zeroWidth(
                    isMultiline()
                        ? List.of(ContextSample.endOfInput(), ContextSample.afterNewline())
                        : List.of(ContextSample.endOfInput())
                );
            }
            this.index++;
            return AnalysisResult.literal(String.valueOf(current));
        }

        private AnalysisResult parseGroup() {
            int groupStart = this.index;
            this.index++;
            if (peek() == '?') {
                this.index++;
                if (match(':')) {
                    AnalysisResult result = parseExpression();
                    expect(')');
                    return result;
                }
                if (match('=')) {
                    AnalysisResult inner = parseExpression();
                    expect(')');
                    return AnalysisResult.lookahead(inner);
                }
                if (match('!')) {
                    int groupContentStart = this.index;
                    AnalysisResult inner = parseExpression();
                    expect(')');
                    String groupSource = this.source.substring(groupContentStart, this.index - 1);
                    return AnalysisResult.negativeLookahead(findNegativeLookaroundWitness(groupSource, false), inner);
                }
                if (match('>')) {
                    AnalysisResult result = parseExpression();
                    expect(')');
                    return result;
                }
                if (match('<')) {
                    if (match('=')) {
                        int groupContentStart = this.index;
                        AnalysisResult inner = parseExpression();
                        expect(')');
                        String groupSource = this.source.substring(groupContentStart, this.index - 1);
                        return AnalysisResult.lookbehind(inner, groupSource, this.flags);
                    }
                    if (match('!')) {
                        int groupContentStart = this.index;
                        AnalysisResult inner = parseExpression();
                        expect(')');
                        String groupSource = this.source.substring(groupContentStart, this.index - 1);
                        return AnalysisResult.negativeLookbehind(
                            findNegativeLookaroundWitness(groupSource, true),
                            inner
                        );
                    }
                }
                throw new UnsupportedOperationException("Unsupported group: " + this.source.substring(groupStart));
            }
            AnalysisResult result = parseExpression();
            expect(')');
            return result;
        }

        private AnalysisResult parseCharacterClass() {
            int start = this.index;
            this.index++;
            int nesting = 1;
            boolean escaped = false;
            while (this.index < this.source.length() && nesting > 0) {
                char current = this.source.charAt(this.index++);
                if (escaped) {
                    escaped = false;
                    continue;
                }
                if (current == '\\') {
                    escaped = true;
                    continue;
                }
                if (current == '[') {
                    nesting++;
                    continue;
                }
                if (current == ']') {
                    nesting--;
                }
            }
            String classSource = this.source.substring(start, this.index);
            return AnalysisResult.consuming(singleCharacterWitness(classSource));
        }

        private AnalysisResult parseEscapeAtom() {
            int start = this.index;
            if (this.index + 1 >= this.source.length()) {
                this.index = this.source.length();
                return AnalysisResult.literal("\\");
            }
            char escapeType = this.source.charAt(this.index + 1);
            if (escapeType == 'Q') {
                int quoteEnd = this.source.indexOf("\\E", this.index + 2);
                if (quoteEnd == -1) {
                    throw new UnsupportedOperationException("Unterminated quoted literal");
                }
                String literal = this.source.substring(this.index + 2, quoteEnd);
                this.index = quoteEnd + 2;
                if (literal.isEmpty()) {
                    return AnalysisResult.empty();
                }
                return AnalysisResult.literal(literal);
            }
            EscapedToken escapedToken = readEscapedToken(this.source, this.index);
            this.index = escapedToken.endIndex + 1;
            String escapedSource = this.source.substring(start, this.index);
            switch (escapeType) {
                case 'A':
                    return AnalysisResult.zeroWidth(List.of(ContextSample.startOfInput()));
                case 'Z':
                case 'z':
                    return AnalysisResult.zeroWidth(List.of(ContextSample.endOfInput()));
                case 'b':
                    return AnalysisResult.zeroWidth(List.of(ContextSample.wordBoundary()));
                case 'B':
                    return AnalysisResult.zeroWidth(List.of(ContextSample.nonWordBoundary()));
                default:
                    return AnalysisResult.consuming(singleCharacterWitness(escapedSource));
            }
        }

        private Quantifier parseQuantifier() {
            if (this.index >= this.source.length()) {
                return null;
            }
            char current = this.source.charAt(this.index);
            Quantifier quantifier;
            switch (current) {
                case '*':
                    this.index++;
                    quantifier = new Quantifier(0, -1);
                    break;
                case '+':
                    this.index++;
                    quantifier = new Quantifier(1, -1);
                    break;
                case '?':
                    this.index++;
                    quantifier = new Quantifier(0, 1);
                    break;
                case '{':
                    quantifier = parseExplicitQuantifier();
                    break;
                default:
                    return null;
            }
            if (this.index < this.source.length()) {
                char modifier = this.source.charAt(this.index);
                if (modifier == '?' || modifier == '+') {
                    this.index++;
                }
            }
            return quantifier;
        }

        private Quantifier parseExplicitQuantifier() {
            int start = this.index;
            this.index++;
            int min = readInteger();
            int max = min;
            if (peek() == ',') {
                this.index++;
                if (Character.isDigit(peek())) {
                    max = readInteger();
                } else {
                    max = -1;
                }
            }
            if (!match('}')) {
                throw new UnsupportedOperationException("Unterminated quantifier at " + start);
            }
            return new Quantifier(min, max);
        }

        private int readInteger() {
            skipCommentsWhitespace();
            int start = this.index;
            while (this.index < this.source.length() && Character.isDigit(this.source.charAt(this.index))) {
                this.index++;
            }
            if (start == this.index) {
                throw new UnsupportedOperationException("Expected integer at " + this.index);
            }
            return Integer.parseInt(this.source.substring(start, this.index));
        }

        private String findNegativeLookaroundWitness(String innerSource, boolean lookbehind) {
            Pattern innerPattern = Pattern.compile(innerSource, this.flags);
            List<String> candidates = buildWitnessCandidates(innerSource);
            for (String candidate : candidates) {
                boolean matches = lookbehind
                    ? matchesAtEnd(innerPattern, candidate)
                    : innerPattern.matcher(candidate).lookingAt();
                if (!matches) {
                    return candidate;
                }
            }
            return null;
        }

        private List<String> buildWitnessCandidates(String innerSource) {
            List<String> candidates = new ArrayList<>();
            addSample(candidates, "");
            for (String genericWitness : GENERIC_WITNESSES) {
                addSample(candidates, genericWitness);
            }
            for (int i = 0; i < innerSource.length(); i++) {
                char current = innerSource.charAt(i);
                if (current == '\\' && i + 1 < innerSource.length()) {
                    EscapedToken escapedToken = readEscapedToken(innerSource, i);
                    addSample(candidates, singleCharacterWitness(escapedToken.text));
                    i = escapedToken.endIndex;
                    continue;
                }
                if (current == '[') {
                    int classEnd = skipNestedCharacterClass(innerSource, i);
                    addSample(candidates, singleCharacterWitness(innerSource.substring(i, classEnd + 1)));
                    i = classEnd;
                    continue;
                }
                if ("()|*+?{}.^$".indexOf(current) == -1) {
                    addSample(candidates, String.valueOf(current));
                }
            }
            int size = candidates.size();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size && candidates.size() < MAX_SAMPLES; j++) {
                    addSample(candidates, candidates.get(i) + candidates.get(j));
                }
            }
            return candidates;
        }

        private int skipNestedCharacterClass(String pattern, int startIndex) {
            int nesting = 1;
            boolean escaped = false;
            int currentIndex = startIndex + 1;
            while (currentIndex < pattern.length() && nesting > 0) {
                char current = pattern.charAt(currentIndex++);
                if (escaped) {
                    escaped = false;
                    continue;
                }
                if (current == '\\') {
                    escaped = true;
                    continue;
                }
                if (current == '[') {
                    nesting++;
                    continue;
                }
                if (current == ']') {
                    nesting--;
                }
            }
            return currentIndex - 1;
        }

        private String singleCharacterWitness(String atomSource) {
            Pattern atomPattern = Pattern.compile(atomSource, this.flags);
            for (String genericWitness : GENERIC_WITNESSES) {
                if (genericWitness.length() == 1 && atomPattern.matcher(genericWitness).matches()) {
                    return genericWitness;
                }
            }
            for (int i = 32; i < 127; i++) {
                String candidate = Character.toString((char) i);
                if (atomPattern.matcher(candidate).matches()) {
                    return candidate;
                }
            }
            if (atomPattern.matcher("\n").matches()) {
                return "\n";
            }
            throw new UnsupportedOperationException("Could not derive witness for " + atomSource);
        }

        private boolean matchesAtEnd(Pattern pattern, String input) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                if (matcher.end() == input.length()) {
                    return true;
                }
            }
            return false;
        }

        private void skipCommentsWhitespace() {
            if ((this.flags & Pattern.COMMENTS) == 0) {
                return;
            }
            while (this.index < this.source.length()) {
                char current = this.source.charAt(this.index);
                if (Character.isWhitespace(current)) {
                    this.index++;
                    continue;
                }
                if (current == '#') {
                    this.index++;
                    while (this.index < this.source.length() && this.source.charAt(this.index) != '\n') {
                        this.index++;
                    }
                    continue;
                }
                break;
            }
        }

        private boolean isMultiline() {
            return (this.flags & Pattern.MULTILINE) != 0;
        }

        private boolean match(char expected) {
            if (peek() == expected) {
                this.index++;
                return true;
            }
            return false;
        }

        private void expect(char expected) {
            if (!match(expected)) {
                throw new UnsupportedOperationException("Expected '" + expected + "' at " + this.index);
            }
        }

        private char peek() {
            if (this.index >= this.source.length()) {
                return '\0';
            }
            return this.source.charAt(this.index);
        }
    }

    private static final class AnalysisResult {
        private final List<String> fullMatchSamples;
        private final List<ContextSample> zeroLengthContexts;

        private AnalysisResult(List<String> fullMatchSamples, List<ContextSample> zeroLengthContexts) {
            this.fullMatchSamples = fullMatchSamples;
            this.zeroLengthContexts = zeroLengthContexts;
        }

        private static AnalysisResult empty() {
            return new AnalysisResult(List.of(""), List.of(ContextSample.empty()));
        }

        private static AnalysisResult literal(String literal) {
            List<String> samples = new ArrayList<>();
            addSample(samples, literal);
            List<ContextSample> contexts = literal.isEmpty() ? List.of(ContextSample.empty()) : List.of();
            return new AnalysisResult(samples, contexts);
        }

        private static AnalysisResult consuming(String witness) {
            List<String> samples = new ArrayList<>();
            addSample(samples, witness);
            return new AnalysisResult(samples, List.of());
        }

        private static AnalysisResult zeroWidth(List<ContextSample> contexts) {
            List<String> samples = new ArrayList<>();
            addSample(samples, "");
            return new AnalysisResult(samples, limitContexts(contexts));
        }

        private static AnalysisResult lookahead(AnalysisResult inner) {
            List<ContextSample> contexts = new ArrayList<>();
            for (String sample : inner.fullMatchSamples) {
                addContext(contexts, ContextSample.right(sample));
            }
            return new AnalysisResult(List.of(""), contexts);
        }

        private static AnalysisResult lookbehind(AnalysisResult inner, String source, int flags) {
            List<ContextSample> contexts = new ArrayList<>();
            for (String sample : inner.fullMatchSamples) {
                addContext(contexts, ContextSample.left(sample));
            }
            Pattern innerPattern = Pattern.compile(source, flags);
            for (String sample : inner.fullMatchSamples) {
                if (matchesAtEnd(innerPattern, sample)) {
                    addContext(contexts, ContextSample.left(sample));
                }
            }
            return new AnalysisResult(List.of(""), contexts);
        }

        private static AnalysisResult negativeLookahead(String witness, AnalysisResult inner) {
            if (witness == null) {
                return new AnalysisResult(List.of(), List.of());
            }
            return new AnalysisResult(List.of(""), List.of(ContextSample.right(witness)));
        }

        private static AnalysisResult negativeLookbehind(String witness, AnalysisResult inner) {
            if (witness == null) {
                return new AnalysisResult(List.of(), List.of());
            }
            return new AnalysisResult(List.of(""), List.of(ContextSample.left(witness)));
        }

        private static AnalysisResult sequence(List<AnalysisResult> parts) {
            if (parts.isEmpty()) {
                return empty();
            }
            List<String> samples = new ArrayList<>();
            addSample(samples, "");
            List<ContextSample> contexts = new ArrayList<>();
            addContext(contexts, ContextSample.empty());
            for (AnalysisResult part : parts) {
                samples = combineSamples(samples, part.fullMatchSamples);
                contexts = combineContexts(contexts, part.zeroLengthContexts);
            }
            return new AnalysisResult(samples, contexts);
        }

        private AnalysisResult alternate(AnalysisResult other) {
            List<String> samples = new ArrayList<>(this.fullMatchSamples);
            for (String sample : other.fullMatchSamples) {
                addSample(samples, sample);
            }
            List<ContextSample> contexts = new ArrayList<>(this.zeroLengthContexts);
            for (ContextSample contextSample : other.zeroLengthContexts) {
                addContext(contexts, contextSample);
            }
            return new AnalysisResult(limitSamples(samples), limitContexts(contexts));
        }

        private AnalysisResult quantify(int min, int max) {
            if (min == 0) {
                return new AnalysisResult(List.of(""), List.of(ContextSample.empty()));
            }
            List<String> samples = repeatSamples(this.fullMatchSamples, min);
            List<ContextSample> contexts = repeatContexts(this.zeroLengthContexts, min);
            return new AnalysisResult(samples, contexts);
        }

        private static List<String> repeatSamples(List<String> samples, int count) {
            List<String> result = new ArrayList<>();
            addSample(result, "");
            for (int i = 0; i < count; i++) {
                result = combineSamples(result, samples);
                if (result.isEmpty()) {
                    return result;
                }
            }
            return result;
        }

        private static List<ContextSample> repeatContexts(List<ContextSample> contexts, int count) {
            List<ContextSample> result = new ArrayList<>();
            addContext(result, ContextSample.empty());
            for (int i = 0; i < count; i++) {
                result = combineContexts(result, contexts);
                if (result.isEmpty()) {
                    return result;
                }
            }
            return result;
        }

        private static List<String> combineSamples(List<String> leftSamples, List<String> rightSamples) {
            List<String> combined = new ArrayList<>();
            for (String leftSample : leftSamples) {
                for (String rightSample : rightSamples) {
                    addSample(combined, leftSample + rightSample);
                }
            }
            return limitSamples(combined);
        }

        private static List<ContextSample> combineContexts(
                List<ContextSample> leftContexts,
                List<ContextSample> rightContexts
        ) {
            List<ContextSample> combined = new ArrayList<>();
            for (ContextSample leftContext : leftContexts) {
                for (ContextSample rightContext : rightContexts) {
                    ContextSample merged = ContextSample.combine(leftContext, rightContext);
                    if (merged != null) {
                        addContext(combined, merged);
                    }
                }
            }
            return limitContexts(combined);
        }

        private static boolean matchesAtEnd(Pattern pattern, String input) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                if (matcher.end() == input.length()) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class ContextSample {
        private final boolean mustBeStart;
        private final String leftSuffix;
        private final boolean mustBeEnd;
        private final String rightPrefix;

        private ContextSample(boolean mustBeStart, String leftSuffix, boolean mustBeEnd, String rightPrefix) {
            this.mustBeStart = mustBeStart;
            this.leftSuffix = leftSuffix;
            this.mustBeEnd = mustBeEnd;
            this.rightPrefix = rightPrefix;
        }

        private static ContextSample empty() {
            return new ContextSample(false, "", false, "");
        }

        private static ContextSample startOfInput() {
            return new ContextSample(true, "", false, "");
        }

        private static ContextSample endOfInput() {
            return new ContextSample(false, "", true, "");
        }

        private static ContextSample beforeNewline() {
            return new ContextSample(false, "\n", false, "");
        }

        private static ContextSample afterNewline() {
            return new ContextSample(false, "", false, "\n");
        }

        private static ContextSample left(String leftSuffix) {
            return new ContextSample(false, leftSuffix, false, "");
        }

        private static ContextSample right(String rightPrefix) {
            return new ContextSample(false, "", false, rightPrefix);
        }

        private static ContextSample wordBoundary() {
            return new ContextSample(false, "a", false, " ");
        }

        private static ContextSample nonWordBoundary() {
            return new ContextSample(false, "a", false, "a");
        }

        private static ContextSample combine(ContextSample left, ContextSample right) {
            boolean mustBeStart = left.mustBeStart || right.mustBeStart;
            String leftSuffix = combineSuffix(left.leftSuffix, right.leftSuffix);
            if (leftSuffix == null) {
                return null;
            }
            if (mustBeStart && !leftSuffix.isEmpty()) {
                return null;
            }

            boolean mustBeEnd = left.mustBeEnd || right.mustBeEnd;
            String rightPrefix = combinePrefix(left.rightPrefix, right.rightPrefix);
            if (rightPrefix == null) {
                return null;
            }
            if (mustBeEnd && !rightPrefix.isEmpty()) {
                return null;
            }

            return new ContextSample(mustBeStart, leftSuffix, mustBeEnd, rightPrefix);
        }

        private static String combineSuffix(String left, String right) {
            if (left.endsWith(right)) {
                return left;
            }
            if (right.endsWith(left)) {
                return right;
            }
            return null;
        }

        private static String combinePrefix(String left, String right) {
            if (left.startsWith(right)) {
                return left;
            }
            if (right.startsWith(left)) {
                return right;
            }
            return null;
        }

        private String materialize() {
            return this.leftSuffix + this.rightPrefix;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ContextSample)) {
                return false;
            }
            ContextSample otherContext = (ContextSample) other;
            return this.mustBeStart == otherContext.mustBeStart
                && this.mustBeEnd == otherContext.mustBeEnd
                && this.leftSuffix.equals(otherContext.leftSuffix)
                && this.rightPrefix.equals(otherContext.rightPrefix);
        }

        @Override
        public int hashCode() {
            int result = Boolean.hashCode(this.mustBeStart);
            result = 31 * result + this.leftSuffix.hashCode();
            result = 31 * result + Boolean.hashCode(this.mustBeEnd);
            result = 31 * result + this.rightPrefix.hashCode();
            return result;
        }
    }

    private static final class Quantifier {
        private final int min;
        private final int max;

        private Quantifier(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    private static void addSample(List<String> samples, String sample) {
        if (!samples.contains(sample) && samples.size() < RegexZeroLengthAnalyzer.MAX_SAMPLES) {
            samples.add(sample);
        }
    }

    private static void addContext(List<ContextSample> contexts, ContextSample context) {
        if (!contexts.contains(context) && contexts.size() < RegexZeroLengthAnalyzer.MAX_SAMPLES) {
            contexts.add(context);
        }
    }

    private static List<String> limitSamples(List<String> samples) {
        if (samples.size() <= RegexZeroLengthAnalyzer.MAX_SAMPLES) {
            return samples;
        }
        return new ArrayList<>(samples.subList(0, RegexZeroLengthAnalyzer.MAX_SAMPLES));
    }

    private static List<ContextSample> limitContexts(List<ContextSample> contexts) {
        if (contexts.size() <= RegexZeroLengthAnalyzer.MAX_SAMPLES) {
            return contexts;
        }
        return new ArrayList<>(contexts.subList(0, RegexZeroLengthAnalyzer.MAX_SAMPLES));
    }
}
