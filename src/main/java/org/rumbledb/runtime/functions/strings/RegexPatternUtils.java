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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexFlagException;
import org.rumbledb.exceptions.InvalidRegexPatternException;

public final class RegexPatternUtils {

    private static final Pattern XML_WHITESPACE_PATTERN = Pattern.compile("[\\t\\n\\r ]+");

    private static final String XML_I =
            ":A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u0131\u0134-\u013E\u0141-\u0148\u014A-\u017E\u0180-\u01C3"
                    + "\u01CD-\u01F0\u01F4-\u01F5\u01FA-\u0217\u0250-\u02A8\u02BB-\u02C1\u0386\u0388-\u038A\u038C\u038E-\u03A1"
                    + "\u03A3-\u03CE\u03D0-\u03D6\u03DA\u03DC\u03DE\u03E0\u03E2-\u03F3\u0401-\u040C\u040E-\u044F\u0451-\u045C"
                    + "\u045E-\u0481\u0490-\u04C4\u04C7-\u04C8\u04CB-\u04CC\u04D0-\u04EB\u04EE-\u04F5\u04F8-\u04F9\u0531-\u0556"
                    + "\u0559\u0561-\u0586\u05D0-\u05EA\u05F0-\u05F2\u0621-\u063A\u0641-\u064A\u0671-\u06B7\u06BA-\u06BE"
                    + "\u06C0-\u06CE\u06D0-\u06D3\u06D5\u06E5-\u06E6\u0905-\u0939\u093D\u0958-\u0961\u0985-\u098C\u098F-\u0990"
                    + "\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09DC-\u09DD\u09DF-\u09E1\u09F0-\u09F1\u0A05-\u0A0A"
                    + "\u0A0F-\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32-\u0A33\u0A35-\u0A36\u0A38-\u0A39\u0A59-\u0A5C\u0A5E"
                    + "\u0A72-\u0A74\u0A85-\u0A8B\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2-\u0AB3\u0AB5-\u0AB9"
                    + "\u0ABD\u0AE0\u0B05-\u0B0C\u0B0F-\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32-\u0B33\u0B36-\u0B39\u0B3D"
                    + "\u0B5C-\u0B5D\u0B5F-\u0B61\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99-\u0B9A\u0B9C\u0B9E-\u0B9F"
                    + "\u0BA3-\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB5\u0BB7-\u0BB9\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33"
                    + "\u0C35-\u0C39\u0C60-\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CDE"
                    + "\u0CE0-\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D60-\u0D61\u0E01-\u0E2E\u0E30"
                    + "\u0E32-\u0E33\u0E40-\u0E45\u0E81-\u0E82\u0E84\u0E87-\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F"
                    + "\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA-\u0EAB\u0EAD-\u0EAE\u0EB0\u0EB2-\u0EB3\u0EBD\u0EC0-\u0EC4\u0F40-\u0F47"
                    + "\u0F49-\u0F69\u10A0-\u10C5\u10D0-\u10F6\u1100\u1102-\u1103\u1105-\u1107\u1109\u110B-\u110C\u110E-\u1112"
                    + "\u113C\u113E\u1140\u114C\u114E\u1150\u1154-\u1155\u1159\u115F-\u1161\u1163\u1165\u1167\u1169"
                    + "\u116D-\u116E\u1172-\u1173\u1175\u119E\u11A8\u11AB\u11AE-\u11AF\u11B7-\u11B8\u11BA\u11BC-\u11C2"
                    + "\u11EB\u11F0\u11F9\u1E00-\u1E9B\u1EA0-\u1EF9\u1F00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D"
                    + "\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC"
                    + "\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2126\u212A-\u212B\u212E\u2180-\u2182"
                    + "\u3007\u3021-\u3029\u3041-\u3094\u30A1-\u30FA\u3105-\u312C\u4E00-\u9FA5\uAC00-\uD7A3";

    private static final String XML_C =
            "\\--.0-:A-Z_a-z\u00B7\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u0131\u0134-\u013E\u0141-\u0148\u014A-\u017E"
                    + "\u0180-\u01C3\u01CD-\u01F0\u01F4-\u01F5\u01FA-\u0217\u0250-\u02A8\u02BB-\u02C1\u02D0\u02D1\u0300-\u0345"
                    + "\u0360-\u0361\u0386\u0387\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03CE\u03D0-\u03D6\u03DA\u03DC"
                    + "\u03DE\u03E0\u03E2-\u03F3\u0401-\u040C\u040E-\u044F\u0451-\u045C\u045E-\u0481\u0483-\u0486\u0490-\u04C4"
                    + "\u04C7-\u04C8\u04CB-\u04CC\u04D0-\u04EB\u04EE-\u04F5\u04F8-\u04F9\u0531-\u0556\u0559\u0561-\u0586"
                    + "\u0591-\u05A1\u05A3-\u05B9\u05BB-\u05BD\u05BF\u05C1-\u05C2\u05C4\u05D0-\u05EA\u05F0-\u05F2\u0621-\u063A"
                    + "\u0640-\u0652\u0660-\u0669\u0670-\u06B7\u06BA-\u06BE\u06C0-\u06CE\u06D0-\u06D3\u06D5-\u06E8"
                    + "\u06EA-\u06ED\u06F0-\u06F9\u0901-\u0903\u0905-\u0939\u093C-\u094D\u0951-\u0954\u0958-\u0963"
                    + "\u0966-\u096F\u0981-\u0983\u0985-\u098C\u098F-\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9"
                    + "\u09BC\u09BE\u09BF\u09C0-\u09C4\u09C7-\u09C8\u09CB-\u09CD\u09D7\u09DC-\u09DD\u09DF-\u09E3\u09E6-\u09EF"
                    + "\u09F0-\u09F1\u0A02\u0A05-\u0A0A\u0A0F-\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32-\u0A33\u0A35-\u0A36"
                    + "\u0A38-\u0A39\u0A3C\u0A3E\u0A3F\u0A40-\u0A42\u0A47-\u0A48\u0A4B-\u0A4D\u0A59-\u0A5C\u0A5E\u0A66-\u0A74"
                    + "\u0A81-\u0A83\u0A85-\u0A8B\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2-\u0AB3\u0AB5-\u0AB9"
                    + "\u0ABC-\u0AC5\u0AC7-\u0AC9\u0ACB-\u0ACD\u0AD0\u0AE0\u0AE6-\u0AEF\u0B01-\u0B03\u0B05-\u0B0C\u0B0F-\u0B10"
                    + "\u0B13-\u0B28\u0B2A-\u0B30\u0B32-\u0B33\u0B36-\u0B39\u0B3C-\u0B43\u0B47-\u0B48\u0B4B-\u0B4D\u0B56-\u0B57"
                    + "\u0B5C-\u0B5D\u0B5F-\u0B61\u0B66-\u0B6F\u0B82-\u0B83\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99-\u0B9A"
                    + "\u0B9C\u0B9E-\u0B9F\u0BA3-\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB5\u0BB7-\u0BB9\u0BBE-\u0BC2\u0BC6-\u0BC8"
                    + "\u0BCA-\u0BCD\u0BD7\u0BE7-\u0BEF\u0C01-\u0C03\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33"
                    + "\u0C35-\u0C39\u0C3E-\u0C44\u0C46-\u0C48\u0C4A-\u0C4D\u0C55-\u0C56\u0C60-\u0C61\u0C66-\u0C6F\u0C82-\u0C83"
                    + "\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CBE-\u0CC4\u0CC6-\u0CC8"
                    + "\u0CCA-\u0CCD\u0CD5-\u0CD6\u0CDE\u0CE0-\u0CE1\u0CE6-\u0CEF\u0D02-\u0D03\u0D05-\u0D0C\u0D0E-\u0D10"
                    + "\u0D12-\u0D28\u0D2A-\u0D39\u0D3E-\u0D43\u0D46-\u0D48\u0D4A-\u0D4D\u0D57\u0D60-\u0D61\u0D66-\u0D6F"
                    + "\u0E01-\u0E2E\u0E30-\u0E3A\u0E40-\u0E4E\u0E50-\u0E59\u0E81-\u0E82\u0E84\u0E87-\u0E88\u0E8A\u0E8D"
                    + "\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA-\u0EAB\u0EAD-\u0EAE\u0EB0-\u0EB9\u0EBB-\u0EBD"
                    + "\u0EC0-\u0EC4\u0EC6\u0EC8-\u0ECD\u0ED0-\u0ED9\u0F18-\u0F19\u0F20-\u0F29\u0F35\u0F37\u0F39\u0F3E-\u0F3F"
                    + "\u0F40-\u0F47\u0F49-\u0F69\u0F71-\u0F84\u0F86-\u0F8B\u0F90-\u0F95\u0F97\u0F99-\u0FAD\u0FB1-\u0FB7"
                    + "\u0FB9\u10A0-\u10C5\u10D0-\u10F6\u1100\u1102-\u1103\u1105-\u1107\u1109\u110B-\u110C\u110E-\u1112"
                    + "\u113C\u113E\u1140\u114C\u114E\u1150\u1154-\u1155\u1159\u115F-\u1161\u1163\u1165\u1167\u1169"
                    + "\u116D-\u116E\u1172-\u1173\u1175\u119E\u11A8\u11AB\u11AE-\u11AF\u11B7-\u11B8\u11BA\u11BC-\u11C2"
                    + "\u11EB\u11F0\u11F9\u1E00-\u1E9B\u1EA0-\u1EF9\u1F00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D"
                    + "\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC"
                    + "\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u20D0-\u20DC\u20E1\u2126\u212A-\u212B"
                    + "\u212E\u2180-\u2182\u3005\u3007\u3021-\u302F\u3031-\u3035\u3041-\u3094\u3099-\u309A\u309D-\u309E"
                    + "\u30A1-\u30FA\u30FC-\u30FE\u3105-\u312C\u4E00-\u9FA5\uAC00-\uD7A3";

    private RegexPatternUtils() {}

    public static CompiledRegex compileRegex(String pattern, String flagsString, ExceptionMetadata metadata) {
        boolean quote = false;
        boolean caseInsensitive = false;
        boolean multiline = false;
        boolean comments = false;
        int flags = 0;
        if (flagsString != null) {
            for (char flag : flagsString.toCharArray()) {
                switch (flag) {
                    case 'i':
                        caseInsensitive = true;
                        flags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
                        break;
                    case 'm':
                        multiline = true;
                        flags |= Pattern.MULTILINE;
                        break;
                    case 's':
                        flags |= Pattern.DOTALL;
                        break;
                    case 'x':
                        comments = true;
                        break;
                    case 'q':
                        quote = true;
                        break;
                    default:
                        throw new InvalidRegexFlagException("Invalid regular expression flag: " + flag, metadata);
                }
            }
        }
        if (comments && !quote) {
            pattern = collapseWhitespace(pattern);
        }
        validateXQueryRegex(pattern, quote, metadata);
        if (quote) {
            pattern = Pattern.quote(pattern);
        } else {
            pattern = translatePattern(pattern, caseInsensitive);
            pattern = translateUnicodeBlockEscapes(pattern, caseInsensitive);
            if (!multiline) {
                pattern = translateDollarAnchors(pattern);
            }
        }
        try {
            return new CompiledRegex(Pattern.compile(pattern, flags), quote, pattern);
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexPatternException(e.getDescription(), metadata);
        }
    }

    /**
     * Checks if a character is considered whitespace per F&amp;O 3.1 &sect;5.6.1.1 (flag {@code 'x'}).
     * Specifically: {@code #x9} (tab), {@code #xA} (line feed), {@code #xD} (carriage return), and {@code #x20}
     * (space).
     */
    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Preprocesses the regular expression for flag {@code 'x'} (whitespace mode).
     * Per W3C F&amp;O 3.1 &sect;5.6.1.1, whitespace is ignored outside character classes,
     * but MUST be preserved inside character classes (e.g. {@code [ ]}).
     * Java's standard {@link Pattern#COMMENTS} incorrectly strips whitespace even inside character classes
     * (turning {@code [ ]} into an invalid empty {@code []}), so we strip whitespace outside classes here
     * and avoid passing {@link Pattern#COMMENTS} to the Java regex engine.
     */
    private static String collapseWhitespace(String pattern) {
        StringBuilder result = new StringBuilder(pattern.length());
        boolean inClass = false;
        int classDepth = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (inClass) {
                result.append(current);
                if (current == '\\' && i + 1 < pattern.length()) {
                    result.append(pattern.charAt(i + 1));
                    i++;
                } else if (current == '[') {
                    classDepth++;
                } else if (current == ']') {
                    classDepth--;
                    if (classDepth == 0) {
                        inClass = false;
                    }
                }
            } else {
                if (isWhitespace(current)) {
                    // Ignored per XQuery flag 'x'
                    continue;
                }
                if (current == '\\' && i + 1 < pattern.length()) {
                    if (!isWhitespace(pattern.charAt(i + 1))) {
                        result.append(current);
                        result.append(pattern.charAt(i + 1));
                        i++;
                        continue;
                    }
                }
                if (current == '[') {
                    inClass = true;
                    classDepth = 1;
                    result.append(current);
                } else {
                    result.append(current);
                }
            }
        }
        return result.toString();
    }

    private static void validateXQueryRegex(String pattern, boolean quote, ExceptionMetadata metadata) {
        if (quote) {
            return;
        }

        int nextCaptureGroupNumber = 1;
        Deque<GroupContext> openGroups = new ArrayDeque<>();
        boolean previousWasAtom = false;
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '[') {
                i = skipCharacterClass(pattern, i, metadata);
                previousWasAtom = true;
                continue;
            }
            if (current == '\\') {
                if (i + 1 >= pattern.length()) {
                    throw new InvalidRegexPatternException("Trailing unescaped backslash", metadata);
                }
                char next = pattern.charAt(i + 1);
                if (Character.isDigit(next)) {
                    int end = i + 1;
                    while (end < pattern.length() && Character.isDigit(pattern.charAt(end))) {
                        end++;
                    }
                    validateBackReference(
                            pattern.substring(i, end),
                            pattern.substring(i + 1, end),
                            openGroups,
                            nextCaptureGroupNumber,
                            metadata);
                    i = end - 1;
                    previousWasAtom = true;
                    continue;
                }
                if (next == 'p' || next == 'P') {
                    if (i + 2 >= pattern.length() || pattern.charAt(i + 2) != '{') {
                        throw new InvalidRegexPatternException(
                                "Invalid Unicode category or block escape \\" + next, metadata);
                    }
                    int end = skipUnicodeEscape(pattern, i);
                    if (pattern.charAt(end) != '}') {
                        throw new InvalidRegexPatternException(
                                "Unterminated Unicode category or block escape", metadata);
                    }
                    validateUnicodePropertyEscape(pattern.substring(i + 3, end), metadata);
                    i = end;
                    previousWasAtom = true;
                    continue;
                }
                if (!isLegalRegexEscape(next)) {
                    throw new InvalidRegexPatternException("Invalid regular expression escape \\" + next, metadata);
                }
                i++;
                previousWasAtom = true;
                continue;
            }
            if (current == '(') {
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '?') {
                    if (i + 2 >= pattern.length() || pattern.charAt(i + 2) != ':') {
                        throw new InvalidRegexPatternException("Invalid group syntax starting with '(?'", metadata);
                    }
                    openGroups.push(GroupContext.nonCapturingGroup());
                    i += 2;
                } else {
                    openGroups.push(new GroupContext(nextCaptureGroupNumber));
                    nextCaptureGroupNumber++;
                }
                previousWasAtom = false;
                continue;
            }
            if (current == ')') {
                if (!openGroups.isEmpty()) {
                    openGroups.pop();
                }
                previousWasAtom = true;
                continue;
            }
            if (current == '|') {
                previousWasAtom = false;
                continue;
            }
            if (current == ']') {
                throw new InvalidRegexPatternException("Unmatched ']' outside a character class", metadata);
            }
            if (current == '?' || current == '*' || current == '+') {
                if (!previousWasAtom) {
                    throw new InvalidRegexPatternException(
                            "Quantifier '" + current + "' with no preceding atom", metadata);
                }
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '?') {
                    i++;
                }
                previousWasAtom = false;
                continue;
            }
            if (current == '{') {
                int j = i + 1;
                int digitsBeforeComma = 0;
                while (j < pattern.length() && Character.isDigit(pattern.charAt(j))) {
                    j++;
                    digitsBeforeComma++;
                }
                if (digitsBeforeComma == 0) {
                    throw new InvalidRegexPatternException("Invalid quantifier '{...}'", metadata);
                }
                if (j < pattern.length() && pattern.charAt(j) == ',') {
                    j++;
                    while (j < pattern.length() && Character.isDigit(pattern.charAt(j))) {
                        j++;
                    }
                }
                if (j >= pattern.length() || pattern.charAt(j) != '}') {
                    throw new InvalidRegexPatternException("Unterminated quantifier '{...}'", metadata);
                }
                if (!previousWasAtom) {
                    throw new InvalidRegexPatternException("Quantifier '{...}' with no preceding atom", metadata);
                }
                i = j;
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '?') {
                    i++;
                }
                previousWasAtom = false;
                continue;
            }
            previousWasAtom = true;
        }
    }

    /**
     * Legal escapes per F&amp;O 3.1 &sect;5.6.1 / XSD Part 2 Appendix F: SingleCharEsc
     * ({@code n r t \ | . ? * + ( ) { } - [ ] ^ $}) union MultiCharEsc ({@code s S i I c C d D w W}).
     * {@code \p{...}}/{@code \P{...}} and digit back-references are validated separately by their callers.
     */
    private static boolean isLegalRegexEscape(char c) {
        switch (c) {
            case 'n':
            case 'r':
            case 't':
            case '\\':
            case '|':
            case '.':
            case '?':
            case '*':
            case '+':
            case '(':
            case ')':
            case '{':
            case '}':
            case '-':
            case '[':
            case ']':
            case '^':
            case '$':
            case 's':
            case 'S':
            case 'i':
            case 'I':
            case 'c':
            case 'C':
            case 'd':
            case 'D':
            case 'w':
            case 'W':
                return true;
            default:
                return false;
        }
    }

    private static void validateUnicodePropertyEscape(String name, ExceptionMetadata metadata) {
        if (name.isEmpty()) {
            throw new InvalidRegexPatternException("Empty Unicode property escape", metadata);
        }
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isWhitespace(c)) {
                throw new InvalidRegexPatternException(
                        "Whitespace is not allowed in Unicode property escape: \\p{" + name + "}", metadata);
            }
        }
    }

    private static void validateBackReference(
            String token,
            String groupNumberText,
            Deque<GroupContext> openGroups,
            int nextCaptureGroupNumber,
            ExceptionMetadata metadata) {
        if (groupNumberText.isEmpty() || groupNumberText.charAt(0) == '0') {
            throw new InvalidRegexPatternException("Invalid back-reference " + token, metadata);
        }

        int longestExistingPrefixLength =
                findLongestExistingBackReferencePrefixLength(groupNumberText, nextCaptureGroupNumber);
        if (longestExistingPrefixLength == 0) {
            throw new InvalidRegexPatternException("Invalid back-reference " + token, metadata);
        }
        int referencedGroupNumber = Integer.parseInt(groupNumberText.substring(0, longestExistingPrefixLength));

        for (GroupContext groupContext : openGroups) {
            if (groupContext.isCapturing() && groupContext.getNumber() == referencedGroupNumber) {
                throw new InvalidRegexPatternException("Invalid back-reference " + token, metadata);
            }
        }
    }

    private static int findLongestExistingBackReferencePrefixLength(
            String groupNumberText, int nextCaptureGroupNumber) {
        int maxExistingGroupNumber = nextCaptureGroupNumber - 1;
        int referencedGroupNumber = 0;
        int longestExistingPrefixLength = 0;
        for (int i = 0; i < groupNumberText.length(); i++) {
            int digit = Character.digit(groupNumberText.charAt(i), 10);
            if (referencedGroupNumber > (maxExistingGroupNumber - digit) / 10) {
                break;
            }
            referencedGroupNumber = referencedGroupNumber * 10 + digit;
            if (referencedGroupNumber >= nextCaptureGroupNumber) {
                break;
            }
            longestExistingPrefixLength = i + 1;
        }
        return longestExistingPrefixLength;
    }

    public static boolean matchesEmptyString(Pattern pattern) {
        return hasZeroLengthMatch(pattern, "")
                || hasZeroLengthMatch(pattern, "a")
                || hasZeroLengthMatch(pattern, "\n")
                || hasZeroLengthMatch(pattern, "a\nb")
                || matchesEmptyStringWithoutLineAnchors(pattern);
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

    private static boolean matchesEmptyStringWithoutLineAnchors(Pattern pattern) {
        String source = pattern.pattern();
        if (source.startsWith("^")) {
            source = source.substring(1);
        }
        if (source.endsWith("$")) {
            source = source.substring(0, source.length() - 1);
        }
        if (source.endsWith("\\z")) {
            source = source.substring(0, source.length() - 2);
        }
        if (source.equals(pattern.pattern())) {
            return false;
        }
        try {
            return Pattern.compile(source, pattern.flags()).matcher("").matches();
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static String[] tokenizeOnXmlWhitespace(String input) {
        String[] result = XML_WHITESPACE_PATTERN.split(input, 0);
        if (result.length != 0 && result[0].isEmpty()) {
            String[] trimmed = new String[result.length - 1];
            System.arraycopy(result, 1, trimmed, 0, trimmed.length);
            return trimmed;
        }
        return result;
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

    /**
     * XPath/XSD regex uses the "Is" prefix exclusively for Unicode block names (F&amp;O 3.1
     * &sect;5.6.1.5, e.g. \p{IsBasicLatin}), whereas java.util.regex reserves "Is" for scripts and
     * binary properties and requires the "In" prefix for blocks (e.g. \p{InBasicLatin}). Translate
     * every \p{Is...}/\P{Is...} escape accordingly before compiling with Pattern.compile.
     * <p>
     * Additionally, per W3C F&amp;O 3.1 &sect;5.6.1.1, Unicode character property escapes
     * ({@code \p{...}} and {@code \P{...}}) are not affected by the {@code 'i'} flag.
     * When {@code caseInsensitive} is active, property escapes outside character classes are wrapped
     * with {@code (?-i:...)} so that Java's case-insensitive matching does not fold properties like {@code \p{Lu}}.
     */
    static String translateUnicodeBlockEscapes(String pattern, boolean caseInsensitive) {
        StringBuilder result = new StringBuilder(pattern.length());
        boolean inClass = false;
        int classDepth = 0;
        int i = 0;
        while (i < pattern.length()) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                if (i + 2 < pattern.length()
                        && (pattern.charAt(i + 1) == 'p' || pattern.charAt(i + 1) == 'P')
                        && pattern.charAt(i + 2) == '{') {
                    int end = pattern.indexOf('}', i + 3);
                    if (end < 0) {
                        result.append(current);
                        i++;
                        continue;
                    }
                    String name = pattern.substring(i + 3, end);
                    boolean wrapCase = caseInsensitive && !inClass;
                    if (wrapCase) {
                        result.append("(?-i:");
                    }
                    result.append('\\').append(pattern.charAt(i + 1)).append('{');
                    if (name.startsWith("Is") && name.length() > 2) {
                        result.append("In").append(name, 2, name.length());
                    } else {
                        result.append(name);
                    }
                    result.append('}');
                    if (wrapCase) {
                        result.append(')');
                    }
                    i = end + 1;
                    continue;
                }
                result.append(current);
                result.append(pattern.charAt(i + 1));
                i += 2;
                continue;
            }
            if (inClass) {
                result.append(current);
                if (current == '[') {
                    classDepth++;
                } else if (current == ']') {
                    classDepth--;
                    if (classDepth == 0) {
                        inClass = false;
                    }
                }
            } else {
                if (current == '[') {
                    inClass = true;
                    classDepth = 1;
                    result.append(current);
                } else {
                    result.append(current);
                }
            }
            i++;
        }
        return result.toString();
    }

    /**
     * Translates unescaped {@code '$'} outside character classes to {@code '\z'} when flag {@code 'm'} is absent.
     * Per W3C F&amp;O 3.1 &sect;5.6.1, without flag {@code 'm'}, {@code '$'} matches only the end of the input string
     * and never before a trailing newline ({@code '\n'}). In contrast, Java's default {@code '$'} matches before
     * a trailing newline. {@code '\z'} matches only at the absolute end of the input sequence.
     */
    static String translateDollarAnchors(String pattern) {
        StringBuilder result = new StringBuilder(pattern.length());
        boolean inClass = false;
        int classDepth = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                result.append(current);
                result.append(pattern.charAt(i + 1));
                i++;
                continue;
            }
            if (inClass) {
                result.append(current);
                if (current == '[') {
                    classDepth++;
                } else if (current == ']') {
                    classDepth--;
                    if (classDepth == 0) {
                        inClass = false;
                    }
                }
            } else {
                if (current == '[') {
                    inClass = true;
                    classDepth = 1;
                    result.append(current);
                } else if (current == '$') {
                    result.append("\\z");
                } else {
                    result.append(current);
                }
            }
        }
        return result.toString();
    }

    /**
     * Translates an XQuery / XML Schema regular expression into a Java {@link Pattern} regular expression.
     * <p>
     * Performs the following transformations:
     * <ul>
     * <li>Expands XML Schema character classes {@code \i}, {@code \I}, {@code \c}, {@code \C} into explicit
     * Unicode character class sets ({@link #XML_I} and {@link #XML_C}).</li>
     * <li>Rewrites character class subtraction {@code [A-[B]]} into Java intersection syntax {@code [[A]&&[^B]]}
     * in both case-sensitive and case-insensitive modes.</li>
     * <li>When {@code caseInsensitive} is true, expands literal characters and ranges with Unicode case-folding
     * closures (including special closures for Kelvin sign and long S).</li>
     * </ul>
     */
    static String translatePattern(String pattern, boolean caseInsensitive) {
        StringBuilder result = new StringBuilder(pattern.length());
        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                char next = pattern.charAt(i + 1);
                if (next == 'i') {
                    result.append("[").append(XML_I).append("]");
                    i++;
                    continue;
                }
                if (next == 'I') {
                    result.append("[^").append(XML_I).append("]");
                    i++;
                    continue;
                }
                if (next == 'c') {
                    result.append("[").append(XML_C).append("]");
                    i++;
                    continue;
                }
                if (next == 'C') {
                    result.append("[^").append(XML_C).append("]");
                    i++;
                    continue;
                }
                if (next == 'd') {
                    result.append("\\p{Nd}");
                    i++;
                    continue;
                }
                if (next == 'D') {
                    result.append("\\P{Nd}");
                    i++;
                    continue;
                }
                if (next == 'w') {
                    result.append("[^\\p{P}\\p{Z}\\p{C}]");
                    i++;
                    continue;
                }
                if (next == 'W') {
                    result.append("[\\p{P}\\p{Z}\\p{C}]");
                    i++;
                    continue;
                }
                if (next == 's') {
                    result.append("[ \\t\\n\\r]");
                    i++;
                    continue;
                }
                if (next == 'S') {
                    result.append("[^ \\t\\n\\r]");
                    i++;
                    continue;
                }
                EscapedToken escapedToken = readEscapedToken(pattern, i);
                result.append(escapedToken.text);
                i = escapedToken.endIndex;
                continue;
            }
            if (current == '[') {
                ClassRewriteResult classResult = rewriteCharacterClass(pattern, i, caseInsensitive);
                result.append(classResult.rewrittenClass);
                i = classResult.endIndex;
                continue;
            }
            if (caseInsensitive) {
                if (Character.isHighSurrogate(current)
                        && i + 1 < pattern.length()
                        && Character.isLowSurrogate(pattern.charAt(i + 1))) {
                    int codePoint = Character.toCodePoint(current, pattern.charAt(i + 1));
                    result.append(expandLiteralCodePoint(codePoint));
                    i++;
                    continue;
                }
                if (hasCaseVariant(current)) {
                    result.append(expandLiteralCodePoint(current));
                    continue;
                }
            }
            result.append(current);
        }
        return result.toString();
    }

    static String normalizeCaseInsensitivePattern(String pattern) {
        return translatePattern(pattern, true);
    }

    /**
     * Rewrites an XSD character class expression ({@code [...]}) into Java regex syntax.
     * Handles universal character class subtraction (translating {@code -[...]} to {@code &&[^...]}),
     * expands XML name classes ({@code \i}, {@code \c}), and conditionally expands case variants.
     */
    private static ClassRewriteResult rewriteCharacterClass(String pattern, int startIndex, boolean caseInsensitive) {
        int index = startIndex + 1;
        boolean isNegated = false;
        if (index < pattern.length() && pattern.charAt(index) == '^') {
            isNegated = true;
            index++;
        }

        StringBuilder currentGroup = new StringBuilder();
        boolean hasSubtraction = false;
        StringBuilder subtractionClause = new StringBuilder();

        boolean firstToken = true;
        while (index < pattern.length()) {
            if (pattern.charAt(index) == ']' && !firstToken) {
                if (hasSubtraction) {
                    String baseClass = (isNegated ? "[^" : "[") + currentGroup + "]";
                    return new ClassRewriteResult("[" + baseClass + subtractionClause + "]", index);
                } else {
                    return new ClassRewriteResult((isNegated ? "[^" : "[") + currentGroup + "]", index);
                }
            }

            if (!firstToken
                    && pattern.charAt(index) == '-'
                    && index + 1 < pattern.length()
                    && pattern.charAt(index + 1) == '[') {
                ClassRewriteResult nestedClass = rewriteCharacterClass(pattern, index + 1, caseInsensitive);
                hasSubtraction = true;
                subtractionClause
                        .append("&&[^")
                        .append(nestedClass.rewrittenClass)
                        .append(']');
                index = nestedClass.endIndex + 1;
                continue;
            }

            ClassToken token = readClassToken(pattern, index, firstToken);
            index = token.nextIndex;
            firstToken = false;

            if (index < pattern.length()
                    && pattern.charAt(index) == '-'
                    && index + 1 < pattern.length()
                    && pattern.charAt(index + 1) != '[') {
                ClassToken rightToken = readClassToken(pattern, index + 1, false);
                if (isRange(token, rightToken, pattern, index + 1)) {
                    if (caseInsensitive) {
                        currentGroup.append(expandRange(token.codePoint, rightToken.codePoint));
                    } else {
                        currentGroup
                                .appendCodePoint(token.codePoint)
                                .append('-')
                                .appendCodePoint(rightToken.codePoint);
                    }
                    index = rightToken.nextIndex;
                    continue;
                }
            }

            currentGroup.append(expandClassToken(token, caseInsensitive));
        }

        if (hasSubtraction) {
            String baseClass = (isNegated ? "[^" : "[") + currentGroup + "]";
            return new ClassRewriteResult("[" + baseClass + subtractionClause + "]", pattern.length() - 1);
        } else {
            return new ClassRewriteResult((isNegated ? "[^" : "[") + currentGroup + "]", pattern.length() - 1);
        }
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
        return right.text.charAt(0) != ']' && right.text.charAt(0) != '[';
    }

    /**
     * Expands a single token within a character class body.
     * Handles XML Name escapes ({@code \i}, {@code \I}, {@code \c}, {@code \C}) and expands
     * literal characters to their case variants when {@code caseInsensitive} is true.
     */
    private static String expandClassToken(ClassToken token, boolean caseInsensitive) {
        if (!token.isLiteral) {
            switch (token.text) {
                case "\\i":
                    return "[" + XML_I + "]";
                case "\\I":
                    return "[^" + XML_I + "]";
                case "\\c":
                    return "[" + XML_C + "]";
                case "\\C":
                    return "[^" + XML_C + "]";
                case "\\d":
                    return "\\p{Nd}";
                case "\\D":
                    return "\\P{Nd}";
                case "\\w":
                    return "[^\\p{P}\\p{Z}\\p{C}]";
                case "\\W":
                    return "[\\p{P}\\p{Z}\\p{C}]";
                case "\\s":
                    return "[ \\t\\n\\r]";
                case "\\S":
                    return "[^ \\t\\n\\r]";
                default:
                    return token.text;
            }
        }
        if (!caseInsensitive || token.text.length() != 1) {
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
            result.append(new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(toAsciiLower(left))
                    .append('-')
                    .appendCodePoint(toAsciiLower(right)));
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        if (isAsciiLowercase(left) && isAsciiLowercase(right)) {
            result.append(new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(toAsciiUpper(left))
                    .append('-')
                    .appendCodePoint(toAsciiUpper(right)));
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        int lowerLeft = Character.toLowerCase(left);
        int lowerRight = Character.toLowerCase(right);
        if ((lowerLeft != left || lowerRight != right) && lowerLeft <= lowerRight) {
            result.append(new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(lowerLeft)
                    .append('-')
                    .appendCodePoint(lowerRight));
            appendSpecialCaseClosure(result, left, right);
            return result.toString();
        }
        int upperLeft = Character.toUpperCase(left);
        int upperRight = Character.toUpperCase(right);
        if ((upperLeft != left || upperRight != right) && upperLeft <= upperRight) {
            result.append(new StringBuilder()
                    .appendCodePoint(left)
                    .append('-')
                    .appendCodePoint(right)
                    .appendCodePoint(upperLeft)
                    .append('-')
                    .appendCodePoint(upperRight));
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
        StringBuilder result =
                new StringBuilder().append('[').appendCodePoint(lower).appendCodePoint(upper);
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
        if (startIndex + 2 < pattern.length()
                && (pattern.charAt(startIndex + 1) == 'p' || pattern.charAt(startIndex + 1) == 'P')
                && pattern.charAt(startIndex + 2) == '{') {
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
            StringBuilder result, int left, int right, int specialCodePoint, int upperEquivalent, int lowerEquivalent) {
        if (withinRange(upperEquivalent, left, right) || withinRange(lowerEquivalent, left, right)) {
            result.appendCodePoint(specialCodePoint);
        }
    }

    private static void appendLiteralSpecialCaseCodePoint(
            StringBuilder result, int codePoint, int specialCodePoint, int upperEquivalent, int lowerEquivalent) {
        if (codePoint == upperEquivalent || codePoint == lowerEquivalent || codePoint == specialCodePoint) {
            result.appendCodePoint(specialCodePoint);
        }
    }

    private static boolean withinRange(int codePoint, int left, int right) {
        return codePoint >= Math.min(left, right) && codePoint <= Math.max(left, right);
    }

    private static int skipUnicodeEscape(String pattern, int startIndex) {
        int index = startIndex + 3;
        while (index < pattern.length() && pattern.charAt(index) != '}') {
            index++;
        }
        return Math.min(index, pattern.length() - 1);
    }

    /**
     * Parses a {@code charClassExpr} per XSD Appendix F: {@code charGroup ::= posCharGroup | negCharGroup |
     * charClassSub}, {@code charClassSub ::= (posCharGroup|negCharGroup) '-' charClassExpr}. A {@code '['}
     * inside the class body has no legal role except opening the trailing subtraction's nested class,
     * immediately after a {@code '-'} that isn't itself the class's leading literal dash.
     */
    private static int skipCharacterClass(String pattern, int startIndex, ExceptionMetadata metadata) {
        int index = startIndex + 1;
        if (index < pattern.length() && pattern.charAt(index) == '^') {
            index++;
        }
        int contentStart = index;
        while (index < pattern.length()) {
            char current = pattern.charAt(index);
            if (current == '\\') {
                if (index + 1 >= pattern.length()) {
                    throw new InvalidRegexPatternException("Trailing unescaped backslash in character class", metadata);
                }
                char next = pattern.charAt(index + 1);
                if (next == 'p' || next == 'P') {
                    if (index + 2 >= pattern.length() || pattern.charAt(index + 2) != '{') {
                        throw new InvalidRegexPatternException(
                                "Invalid Unicode category or block escape \\" + next + " in character class", metadata);
                    }
                    int end = skipUnicodeEscape(pattern, index);
                    if (pattern.charAt(end) != '}') {
                        throw new InvalidRegexPatternException(
                                "Unterminated Unicode category or block escape in character class", metadata);
                    }
                    validateUnicodePropertyEscape(pattern.substring(index + 3, end), metadata);
                    index = end;
                } else if (!isLegalRegexEscape(next)) {
                    throw new InvalidRegexPatternException(
                            "Invalid regular expression escape \\" + next + " in character class", metadata);
                } else {
                    index++;
                }
            } else if (current == '-' && index + 1 < pattern.length() && pattern.charAt(index + 1) == '[') {
                if (index == contentStart) {
                    throw new InvalidRegexPatternException("Invalid character class subtraction", metadata);
                }
                int nestedEnd = skipCharacterClass(pattern, index + 1, metadata);
                if (nestedEnd + 1 >= pattern.length() || pattern.charAt(nestedEnd + 1) != ']') {
                    throw new InvalidRegexPatternException(
                            "Character class subtraction must be the last element of the class", metadata);
                }
                return nestedEnd + 1;
            } else if (current == '[') {
                throw new InvalidRegexPatternException("Invalid nested '[' in character class", metadata);
            } else if (current == ']') {
                if (index == contentStart) {
                    throw new InvalidRegexPatternException("Empty character class", metadata);
                }
                return index;
            }
            index++;
        }
        throw new InvalidRegexPatternException("Unterminated character class", metadata);
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

    @Getter
    public static final class CompiledRegex {
        private final Pattern pattern;
        private final boolean quote;
        private final String effectivePattern;

        private CompiledRegex(Pattern pattern, boolean quote, String effectivePattern) {
            this.pattern = pattern;
            this.quote = quote;
            this.effectivePattern = effectivePattern;
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

    private static final class GroupContext {
        private final boolean capturing;
        private final int number;

        private GroupContext(boolean capturing, int number) {
            this.capturing = capturing;
            this.number = number;
        }

        private GroupContext(int number) {
            this(true, number);
        }

        private static GroupContext nonCapturingGroup() {
            return new GroupContext(false, -1);
        }

        private boolean isCapturing() {
            return this.capturing;
        }

        private int getNumber() {
            return this.number;
        }
    }
}
