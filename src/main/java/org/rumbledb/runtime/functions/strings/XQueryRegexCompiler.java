/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexPatternException;
import org.rumbledb.runtime.functions.strings.RegexPatternUtils.CaptureGroup;
import org.rumbledb.runtime.functions.strings.RegexPatternUtils.RegexFlags;

/**
 * Parses F&O 3.1 §5.6 / XSD character classes and emits Java regex syntax.
 * Input is consumed once. In particular, generated Java syntax is never reparsed as XQuery.
 * User captures have stable names; empty internal captures track participation for back-references.
 */
final class XQueryRegexCompiler {
    private final String source;
    private final RegexFlags flags;
    private final ExceptionMetadata metadata;
    private final Set<Integer> openGroups = new HashSet<>();
    private int position;
    private int classDepth;
    private int captures;
    private final List<CaptureGroup> groups = new ArrayList<>();

    XQueryRegexCompiler(String source, RegexFlags flags, ExceptionMetadata metadata) {
        this.source = source;
        this.flags = flags;
        this.metadata = metadata;
    }

    String compile() {
        String result = expression(0);
        if (peek() != -1) {
            throw error("Unmatched closing parenthesis");
        }
        return result;
    }

    List<CaptureGroup> groups() {
        return List.copyOf(groups);
    }

    private String expression(int parent) {
        int branch = 0;
        StringBuilder result = new StringBuilder();
        while (peek() != -1 && peek() != ')') {
            if (take('|')) {
                result.append('|');
                branch++;
                continue;
            }
            result.append(atom(parent, branch));
            result.append(quantifier());
        }
        return result.toString();
    }

    private String atom(int parent, int branch) {
        int cp = read();
        switch (cp) {
            case '(':
                boolean capturing = !take('?');
                if (!capturing) {
                    expect(':');
                }
                int group = capturing ? ++captures : 0;
                if (capturing) {
                    openGroups.add(group);
                    groups.add(new CaptureGroup(group, parent, branch));
                }
                String body = expression(capturing ? group : parent);
                expect(')');
                openGroups.remove(group);
                // The marker follows the WHOLE body, including all alternatives.
                return capturing ? "(?<u" + group + ">(?:" + body + ")(?<p" + group + ">))" : "(?:" + body + ")";
            case '[':
                return characterClass();
            case '\\':
                if (isDigit(peek())) {
                    return backReference();
                }
                return "[" + escape().text() + "]";
            case '.':
                return flags.dotAll() ? "(?s:.)" : "[^\\n\\r]";
            case '^':
                return flags.multiline() ? "(?:\\A|(?<=\\n)(?!\\z))" : "\\A";
            case '$':
                return flags.multiline() ? "(?:(?=\\n)|(?<!\\n)\\z)" : "\\z";
            case '?':
            case '*':
            case '+':
            case '{':
                throw error("Quantifier with no preceding atom");
            case '}':
            case ']':
                throw error("Unescaped metacharacter " + (char) cp);
            default:
                return "[" + XQueryRegexUnicode.range(cp, cp, flags.ignoreCase()) + "]";
        }
    }

    private String quantifier() {
        StringBuilder result = new StringBuilder();
        int cp = peek();
        if (cp == '?' || cp == '*' || cp == '+') {
            result.appendCodePoint(read());
        } else if (take('{')) {
            result.append('{').append(digits());
            if (take(',')) {
                result.append(',');
                if (isDigit(peek())) {
                    result.append(digits());
                }
            }
            expect('}');
            result.append('}');
        } else {
            return "";
        }
        if (take('?')) {
            result.append('?');
        }
        return result.toString();
    }

    private String digits() {
        if (!isDigit(peek())) {
            throw error("Expected an ASCII digit in quantifier");
        }
        StringBuilder result = new StringBuilder();
        while (isDigit(peek())) {
            result.appendCodePoint(read());
        }
        return result.toString();
    }

    private String backReference() {
        int group = read() - '0';
        if (group == 0 || group > captures) {
            throw error("Invalid back-reference");
        }
        // Only digits naming a previously opened group belong to the reference.
        // Leave remaining digits to be parsed as literal atoms, even if later groups exist.
        while (isDigit(peek()) && group <= (captures - (peek() - '0')) / 10) {
            group = group * 10 + read() - '0';
        }
        if (openGroups.contains(group)) {
            throw error("Back-reference refers to an unclosed group");
        }
        // An empty marker participates exactly when its user group does. Its back-reference
        // succeeds at every position if set, and fails if unset. The negative lookahead is
        // therefore an empty fallback ONLY for an unmatched group, not an optional reference.
        // Java's normal capture restoration also restores the marker during backtracking.
        String reference = "\\k<u" + group + ">";
        if (flags.ignoreCase()) {
            reference = "(?iu:" + reference + ")";
        }
        return "(?:" + reference + "|(?!\\k<p" + group + ">))";
    }

    /** Called after '['. Each iteration consumes a complete character-group part. */
    private String characterClass() {
        classDepth++;
        boolean negative = take('^');
        StringBuilder parts = new StringBuilder();
        while (peek() != ']' && peek() != -1) {
            if (startsSubtraction()) {
                if (parts.length() == 0) {
                    throw error("Empty base of character class subtraction");
                }
                read();
                expect('[');
                String excluded = characterClass();
                expect(']');
                classDepth--;
                return "[" + (negative ? "[^" : "[") + parts + "]&&[^" + excluded + "]]";
            }
            ClassAtom left = classAtom();
            if (peek() == '-' && !startsSubtraction() && !startsTrailingHyphen()) {
                read();
                ClassAtom right = classAtom();
                if (left.codePoint() < 0
                        || right.codePoint() < 0
                        || left.unescapedHyphen()
                        || right.unescapedHyphen()) {
                    throw error("Range endpoints must be single characters (escape literal hyphens)");
                }
                if (left.codePoint() > right.codePoint()) {
                    throw error("Character range is in descending code point order");
                }
                parts.append(XQueryRegexUnicode.range(left.codePoint(), right.codePoint(), flags.ignoreCase()));
                // A following '-' starts a new part; it is not a second range operator.
            } else {
                parts.append(left.text());
            }
        }
        if (parts.length() == 0) {
            throw error("Empty character class");
        }
        expect(']');
        classDepth--;
        return (negative ? "[^" : "[") + parts + "]";
    }

    private boolean startsSubtraction() {
        return source.startsWith("-[", position);
    }

    private boolean startsTrailingHyphen() {
        return source.startsWith("-]", position) || source.startsWith("--[", position);
    }

    private ClassAtom classAtom() {
        int cp = read();
        if (cp == '\\') {
            return escape();
        }
        if (cp == -1 || cp == '[' || cp == ']') {
            throw error("Expected a character class member");
        }
        return literal(cp, cp == '-');
    }

    private ClassAtom literal(int cp, boolean unescapedHyphen) {
        return new ClassAtom(XQueryRegexUnicode.range(cp, cp, flags.ignoreCase()), cp, unescapedHyphen);
    }

    /** Escapes have a single shared interpretation inside and outside character classes. */
    private ClassAtom escape() {
        int cp = read();
        switch (cp) {
            case 'n':
                return literal('\n', false);
            case 'r':
                return literal('\r', false);
            case 't':
                return literal('\t', false);
            case 'p':
            case 'P':
                return new ClassAtom(property(cp), -1, false);
            case 'i':
                return set("[" + XQueryRegexUnicode.XML_I + "]");
            case 'I':
                return set("[^" + XQueryRegexUnicode.XML_I + "]");
            case 'c':
                return set("[" + XQueryRegexUnicode.XML_C + "]");
            case 'C':
                return set("[^" + XQueryRegexUnicode.XML_C + "]");
            case 'd':
                return set("\\p{Nd}");
            case 'D':
                return set("\\P{Nd}");
            case 'w':
                return set("[^\\p{P}\\p{Z}\\p{C}]");
            case 'W':
                return set("[\\p{P}\\p{Z}\\p{C}]");
            case 's':
                return set("[ \\t\\n\\r]");
            case 'S':
                return set("[^ \\t\\n\\r]");
            default:
                if (cp < 0 || "\\|.?*+(){}-[]^$".indexOf(cp) < 0) {
                    throw error("Invalid regular expression escape");
                }
                return literal(cp, false);
        }
    }

    private ClassAtom set(String text) {
        return new ClassAtom(text, -1, false);
    }

    private String property(int prefix) {
        expect('{');
        StringBuilder name = new StringBuilder();
        while (peek() != '}' && peek() != -1) {
            name.appendCodePoint(read());
        }
        expect('}');
        String value = name.toString();
        if (value.matches("Is[A-Za-z0-9-]+")) {
            value = "In" + value.substring(2);
        } else if (!value.matches("L[ultmo]?|M[nce]?|N[dlo]?|P[cdseifo]?|Z[slp]?|S[mcko]?|C[cfson]?")) {
            throw error("Invalid Unicode category or block name: " + value);
        }
        return "\\" + (char) prefix + "{" + value + "}";
    }

    /**
     * Whitespace is skipped during token recognition, including after a backslash.
     * Inside a class it remains significant. No separate preprocessing pass is needed.
     */
    private int peek() {
        if (flags.whitespace() && classDepth == 0) {
            while (position < source.length() && " \t\n\r".indexOf(source.charAt(position)) >= 0) {
                position++;
            }
        }
        return position == source.length() ? -1 : source.codePointAt(position);
    }

    private int read() {
        int cp = peek();
        if (cp != -1) {
            position += Character.charCount(cp);
        }
        return cp;
    }

    private boolean take(int cp) {
        if (peek() != cp) {
            return false;
        }
        read();
        return true;
    }

    private void expect(int cp) {
        if (!take(cp)) {
            throw error("Expected '" + (char) cp + "'");
        }
    }

    private static boolean isDigit(int cp) {
        return cp >= '0' && cp <= '9';
    }

    private InvalidRegexPatternException error(String message) {
        return new InvalidRegexPatternException(message + " at regex offset " + position, metadata);
    }

    private record ClassAtom(String text, int codePoint, boolean unescapedHyphen) {}
}
