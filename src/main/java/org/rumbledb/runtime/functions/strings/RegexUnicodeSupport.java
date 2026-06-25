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

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexPatternException;

import java.util.regex.Pattern;

public final class RegexUnicodeSupport {

    private static final ULocale CASE_MAPPING_LOCALE = ULocale.ROOT;

    private RegexUnicodeSupport() {
    }

    public static String lower(String input) {
        return UCharacter.toLowerCase(CASE_MAPPING_LOCALE, input);
    }

    public static String upper(String input) {
        return UCharacter.toUpperCase(CASE_MAPPING_LOCALE, input);
    }

    public static String foldCase(String input) {
        return UCharacter.foldCase(input, UCharacter.FOLD_CASE_DEFAULT);
    }

    public static ParsedRegexFlags parseFlags(String flags, ExceptionMetadata metadata) {
        if (flags == null || flags.isEmpty()) {
            return ParsedRegexFlags.NONE;
        }
        boolean quote = false;
        boolean caseInsensitive = false;
        boolean multiline = false;
        boolean dotAll = false;
        boolean comments = false;
        int javaPatternFlags = 0;
        for (char flag : flags.toCharArray()) {
            switch (flag) {
                case 'i':
                    caseInsensitive = true;
                    javaPatternFlags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
                    break;
                case 'm':
                    multiline = true;
                    javaPatternFlags |= Pattern.MULTILINE;
                    break;
                case 's':
                    dotAll = true;
                    javaPatternFlags |= Pattern.DOTALL;
                    break;
                case 'x':
                    comments = true;
                    javaPatternFlags |= Pattern.COMMENTS;
                    break;
                case 'q':
                    quote = true;
                    break;
                default:
                    throw new InvalidRegexPatternException(
                            "Invalid regular expression flag: " + flag,
                            metadata
                    );
            }
        }
        return new ParsedRegexFlags(javaPatternFlags, quote, caseInsensitive, multiline, dotAll, comments);
    }

    public static final class ParsedRegexFlags {

        public static final ParsedRegexFlags NONE = new ParsedRegexFlags(0, false, false, false, false, false);

        public final int javaPatternFlags;
        public final boolean quote;
        public final boolean caseInsensitive;
        public final boolean multiline;
        public final boolean dotAll;
        public final boolean comments;

        private ParsedRegexFlags(
                int javaPatternFlags,
                boolean quote,
                boolean caseInsensitive,
                boolean multiline,
                boolean dotAll,
                boolean comments
        ) {
            this.javaPatternFlags = javaPatternFlags;
            this.quote = quote;
            this.caseInsensitive = caseInsensitive;
            this.multiline = multiline;
            this.dotAll = dotAll;
            this.comments = comments;
        }
    }
}
