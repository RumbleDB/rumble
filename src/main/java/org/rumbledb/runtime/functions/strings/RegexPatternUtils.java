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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexFlagException;
import org.rumbledb.exceptions.InvalidRegexPatternException;

public final class RegexPatternUtils {
    private static final Pattern XML_WHITESPACE_PATTERN = Pattern.compile("[\\t\\n\\r ]+");

    private RegexPatternUtils() {}

    public static CompiledRegex compileRegex(String pattern, String flagsString, ExceptionMetadata metadata) {
        RegexFlags flags = RegexFlags.parse(flagsString, metadata);
        String translated =
                flags.quote() ? Pattern.quote(pattern) : new XQueryRegexCompiler(pattern, flags, metadata).compile();
        int javaFlags = flags.quote() && flags.ignoreCase() ? Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE : 0;
        try {
            return new CompiledRegex(Pattern.compile(translated, javaFlags), flags.quote(), translated);
        } catch (PatternSyntaxException e) {
            throw new InvalidRegexPatternException(e.getDescription(), metadata);
        }
    }

    public static boolean matchesEmptyString(Pattern pattern) {
        return pattern.matcher("").find();
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

    public record CompiledRegex(Pattern pattern, boolean quote, String effectivePattern) {}

    record RegexFlags(boolean ignoreCase, boolean multiline, boolean dotAll, boolean whitespace, boolean quote) {
        static RegexFlags parse(String text, ExceptionMetadata metadata) {
            boolean i = false, m = false, s = false, x = false, q = false;
            if (text != null) {
                for (char flag : text.toCharArray()) {
                    switch (flag) {
                        case 'i':
                            i = true;
                            break;
                        case 'm':
                            m = true;
                            break;
                        case 's':
                            s = true;
                            break;
                        case 'x':
                            x = true;
                            break;
                        case 'q':
                            q = true;
                            break;
                        default:
                            throw new InvalidRegexFlagException("Invalid regular expression flag: " + flag, metadata);
                    }
                }
            }
            return new RegexFlags(i, m, s, x, q);
        }
    }
}
