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
package iq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRegexPatternException;
import org.rumbledb.runtime.functions.strings.RegexPatternUtils;

public class RegexPatternUtilsTest {

    @Test
    public void caseInsensitiveNegatedClassesDoNotMatchEquivalentLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[^i]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals("I", compiledRegex.pattern().matcher("I").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveSubtractedClassesPreserveExcludedLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[A-Z-[OI]]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals("O", compiledRegex.pattern().matcher("O").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveAsciiRangesIncludeKelvinSign() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[A-Z]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals("x", compiledRegex.pattern().matcher("\u212A").replaceAll("x"));
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveLiteralMatching() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("q", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(
                new String[] {"A", "Ba", ""}, RegexPatternUtils.tokenize("AqBaQ", compiledRegex.pattern()));
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveMultiCharacterPatterns() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[^q]é", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(new String[] {"", ""}, RegexPatternUtils.tokenize("xÉ", compiledRegex.pattern()));
    }

    @Test
    public void tokenizeReturnsEmptySequenceForEmptyInput() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("\\s+", null, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(new String[0], RegexPatternUtils.tokenize("", compiledRegex.pattern()));
    }

    @Test
    public void tokenizePreservesLeadingEmptyToken() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("(ab)|(a)", null, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(
                new String[] {"", "r", "c", "d", "r", ""},
                RegexPatternUtils.tokenize("abracadabra", compiledRegex.pattern()));
    }

    @Test
    public void multilineCaretMatchesEmptyStringForTokenizeValidation() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("^", "m", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(RegexPatternUtils.matchesEmptyString(compiledRegex.pattern()));
    }

    @Test
    public void multilineWhitespaceAnchorsMatchEmptyStringForTokenizeValidation() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("^[\\s]*$", "m", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(RegexPatternUtils.matchesEmptyString(compiledRegex.pattern()));
    }

    @Test
    public void tokenizeOnXmlWhitespaceDoesNotSplitOnFormFeed() {
        Assertions.assertArrayEquals(new String[] {"abc\fdef"}, RegexPatternUtils.tokenizeOnXmlWhitespace("abc\fdef"));
    }

    @Test
    public void longTrailingDigitsAfterValidBackReferenceRemainValid() {
        String backReferenceDigits = "111111111111111111111111111111";
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("(a)\\" + backReferenceDigits, null, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(compiledRegex
                .pattern()
                .matcher("aa" + backReferenceDigits.substring(1))
                .matches());
    }

    @Test
    public void tokenizeOnXmlWhitespaceSplitsOnXmlWhitespaceOnly() {
        Assertions.assertArrayEquals(
                new String[] {"abc", "def", "ghi", "jkl"},
                RegexPatternUtils.tokenizeOnXmlWhitespace(" abc\tdef\nghi\rjkl "));
    }

    @Test
    public void emptyCharacterClassThrowsInvalidRegexPatternException() {
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("a[]b", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("[^]", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("[a-f-[]]+", null, ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    public void unterminatedCharacterClassThrowsInvalidRegexPatternException() {
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("[\\]", null, ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    public void whitespaceInUnicodePropertyEscapeThrowsInvalidRegexPatternException() {
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("\\p{ IsBasicLatin}+", null, ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    public void flagXCollapsesWhitespaceFollowingBackslashOutsideClasses() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("hello\\ sworld", "x", ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(compiledRegex.pattern().matcher("hello world").matches());
    }

    @Test
    public void multiCharacterEscapesSupportUnicodeSets() {
        RegexPatternUtils.CompiledRegex digitRegex =
                RegexPatternUtils.compileRegex("^(?:\\d)$", null, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(digitRegex.pattern().matcher("۰").matches());

        RegexPatternUtils.CompiledRegex nonDigitRegex =
                RegexPatternUtils.compileRegex("^(?:\\D)$", null, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(nonDigitRegex.pattern().matcher("a").matches());
        Assertions.assertFalse(nonDigitRegex.pattern().matcher("۰").matches());

        RegexPatternUtils.CompiledRegex wordRegex =
                RegexPatternUtils.compileRegex("^(?:[\\w\\-\\.]+@.*)$", null, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(
                wordRegex.pattern().matcher("first-last@seznam.cz").matches());
        Assertions.assertFalse(
                wordRegex.pattern().matcher("first_last@seznam.cz").matches());

        RegexPatternUtils.CompiledRegex nonWordRegex =
                RegexPatternUtils.compileRegex("^(?:\\W)$", null, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(nonWordRegex.pattern().matcher("_").matches());
        Assertions.assertFalse(nonWordRegex.pattern().matcher("a").matches());
    }

    @Test
    public void invalidCharacterRangeEndpointsThrowInvalidRegexPatternException() {
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("foo([6-\\s]*)bar", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("foo([c-\\S]*)", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("foo([7-\\w]*)", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("foo([a-\\W]*)bar", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("foo([a-\\d]*)bar", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("([5-\\D]*)bar", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("([f-\\p{Lu}]\\w*)", null, ExceptionMetadata.EMPTY_METADATA));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("([\\s-6]*)", null, ExceptionMetadata.EMPTY_METADATA));
    }

    @org.junit.jupiter.api.Disabled("Known conformance gap: Java back-reference case folding equates dotted I with i")
    @Test
    public void caseInsensitiveBackReferencesDistinguishDottedI() {
        Assertions.assertFalse(matches("\u0130i", "^(\u0130)\\1$", "i"));
    }

    @org.junit.jupiter.api.Disabled(
            "Known conformance gap: Java back-reference case folding misses equal uppercase expansions")
    @Test
    public void caseInsensitiveBackReferencesMatchLigatureVariants() {
        Assertions.assertTrue(matches("\ufb05\ufb06", "^(\ufb05)\\1$", "i"));
    }

    private static boolean matches(String input, String regex, String flags) {
        return RegexPatternUtils.compileRegex(regex, flags, ExceptionMetadata.EMPTY_METADATA)
                .pattern()
                .matcher(input)
                .find();
    }

    @Test
    public void propertiesRemainCaseSensitiveInsideMixedAndSubtractedClasses() {
        Assertions.assertFalse(matches("m", "[\\p{Lu}]", "i"));
        Assertions.assertTrue(matches("m", "[\\P{Lu}]", "i"));
        Assertions.assertTrue(matches("M", "[m\\p{Nd}]", "i"));
        Assertions.assertTrue(matches("a", "[a-z-[\\p{Lu}]]", "i"));
        Assertions.assertFalse(matches("A", "[a-z-[\\p{Lu}]]", "i"));
    }

    @Test
    public void hyphensAfterRangesAreLiteralClassMembers() {
        // QT3 K2-MatchesFunc-16, re00056a, re00086a and re00102 (XSD 1.1).
        Assertions.assertTrue(matches("-", "[0-9-.]", ""));
        Assertions.assertTrue(matches("a-x", "^[a-a-x-x]+$", ""));
        Assertions.assertTrue(matches("a-1x-7", "^[a-c-1-4x-z-7-9]*$", ""));
        Assertions.assertFalse(matches("-", "[^a-d-b-c]", ""));
        Assertions.assertTrue(matches("-", "[a--[a]]", ""));
    }

    @Test
    public void whitespaceAfterEscapeDoesNotStartACharacterClass() {
        Assertions.assertTrue(matches("[a", "\\ [ a", "x"));
        Assertions.assertTrue(matches(" ", "[ ]", "x"));
        Assertions.assertTrue(matches("#", "#", "x"));
    }

    @Test
    public void classLiteralsCannotIntroduceJavaOperators() {
        Assertions.assertTrue(matches("&", "[a&&b]", ""));
        Assertions.assertTrue(matches("a", "[a&&b]", ""));
        Assertions.assertFalse(matches("c", "[a&&b]", ""));
        Assertions.assertTrue(matches("]", "[\\[-\\]]", ""));
    }

    @Test
    public void caseExpansionDoesNotInventRanges() {
        Assertions.assertFalse(matches("\u00f7", "[\u00c0-\u00de]", "i"));
        Assertions.assertTrue(matches("\u00e0", "[\u00c0-\u00de]", "i"));
        Assertions.assertTrue(matches("\u1e9e", "[\u00df]", "i"));
        Assertions.assertTrue(matches("\u212a", "k", "i"));
        Assertions.assertTrue(matches("\ud801\udc28", "[\ud801\udc00-\ud801\udc27]", "i"));
    }

    @Test
    public void anchorsAndDotUseXQueryLineTerminators() {
        Assertions.assertTrue(matches("\u2028", ".", ""));
        Assertions.assertFalse(matches("\r", ".", ""));
        Assertions.assertTrue(matches("\r", ".", "s"));
        Assertions.assertFalse(matches("a\rb", "^b", "m"));
        Assertions.assertTrue(matches("a\nb", "^b", "m"));
        Assertions.assertFalse(matches("a\n", "a$", ""));
        Assertions.assertTrue(matches("a\n", "a$", "m"));
        Assertions.assertTrue(matches("", "^$", "m"));
    }

    @Test
    public void parserRejectsJavaOnlySyntaxAndUnescapedBraces() {
        for (String regex : new String[] {"}", "\\p{javaLowerCase}", "[--z]", "a++", "(?=a)", "(a)\\0", "[\\1]"}) {
            Assertions.assertThrows(
                    InvalidRegexPatternException.class,
                    () -> RegexPatternUtils.compileRegex(regex, "", ExceptionMetadata.EMPTY_METADATA),
                    regex);
        }
    }

    @Test
    public void backReferenceDigitsCannotReferToLaterGroups() {
        Assertions.assertTrue(matches("aa0bcdefghij", "(a)\\10(b)(c)(d)(e)(f)(g)(h)(i)(j)", ""));
        Assertions.assertTrue(matches("aA", "(a)\\1", "i"));
        Assertions.assertThrows(
                InvalidRegexPatternException.class,
                () -> RegexPatternUtils.compileRegex("(a\\1)", "", ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    public void quotedPatternsIgnoreWhitespaceAndMetacharacters() {
        Assertions.assertTrue(matches("[ A ]", "[ a ]", "qixms"));
        Assertions.assertFalse(matches("a", " a ", "qx"));
    }

    @Test
    public void unmatchedBackReferencesMatchEmptyButParticipatingGroupsRemainMandatory() {
        Assertions.assertTrue(matches("", "(a)?\\1", ""));
        Assertions.assertTrue(matches("b", "^(a)?b\\1$", ""));
        Assertions.assertTrue(matches("aba", "^(a)?b\\1$", ""));
        Assertions.assertFalse(matches("ab", "^(a)?b\\1$", ""));
        Assertions.assertTrue(matches("a", "^(a|(b))\\2$", ""));
        Assertions.assertTrue(matches("bb", "^(a|(b))\\2$", ""));
        Assertions.assertFalse(matches("b", "^(a|(b))\\2$", ""));
        Assertions.assertTrue(matches("b", "^(a)?b\\1$", "i"));
        Assertions.assertTrue(matches("abA", "^(a)?b\\1$", "i"));
        Assertions.assertFalse(matches("ab", "^(a)?b\\1$", "i"));
    }

    @Test
    public void participationIsRestoredWhenBacktracking() {
        Assertions.assertTrue(matches("ab", "^(?:(a)c|ab)\\1$", ""));
        Assertions.assertTrue(matches("a", "^(a)?a\\1$", ""));
        Assertions.assertTrue(matches("b", "^(a*)b\\1$", ""));
        Assertions.assertTrue(matches("", "^(a)?\\1*$", ""));
        Assertions.assertTrue(matches("abb", "^(a|b)+\\1$", ""));
        Assertions.assertFalse(matches("abb", "^(a|b)\\1$", ""));
    }

    @Test
    public void internalMarkersDoNotChangeUserCapturesOrReplacementReferences() {
        var regex = RegexPatternUtils.compileRegex("((a)?b)(c)\\2", "", ExceptionMetadata.EMPTY_METADATA);
        var matcher = regex.pattern().matcher("bc");
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals(3, regex.groupCount());
        Assertions.assertEquals("b", regex.group(matcher, 1));
        Assertions.assertNull(regex.group(matcher, 2));
        Assertions.assertEquals("c", regex.group(matcher, 3));
        Assertions.assertEquals(-1, regex.start(matcher, 2));
        Assertions.assertEquals(1, regex.start(matcher, 3));
        Assertions.assertEquals(2, regex.end(matcher, 3));
        Assertions.assertEquals(1, regex.groups().get(1).parentNumber());
        Assertions.assertEquals(0, regex.groups().get(2).parentNumber());
        Assertions.assertEquals(
                "bc|b||c||b0",
                matcher.replaceAll(regex.replacement("$0|$1|$2|$3|$9|$10", ExceptionMetadata.EMPTY_METADATA)));
        Assertions.assertEquals(
                "|bc||b2",
                matcher.replaceAll(regex.replacement("$0002|$00|$0009|$012", ExceptionMetadata.EMPTY_METADATA)));
    }

    @Test
    public void multiDigitReferencesUseUserGroupNumbers() {
        var regex = RegexPatternUtils.compileRegex(
                "(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)\\10", "", ExceptionMetadata.EMPTY_METADATA);
        var matcher = regex.pattern().matcher("abcdefghijj");
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals("ja", matcher.replaceAll(regex.replacement("$10$1", ExceptionMetadata.EMPTY_METADATA)));
    }

    @Test
    public void unmatchedBackReferencesAreIncludedInEmptyStringDetection() {
        var regex = RegexPatternUtils.compileRegex("(a)?\\1", "", ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(RegexPatternUtils.matchesEmptyString(regex.pattern()));
        var nonEmpty = RegexPatternUtils.compileRegex("(a)?b\\1", "", ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertFalse(RegexPatternUtils.matchesEmptyString(nonEmpty.pattern()));
        Assertions.assertArrayEquals(new String[] {"", "x", ""}, RegexPatternUtils.tokenize("bxb", nonEmpty.pattern()));
    }

    @Test
    public void quotedAndUnquotedLiteralsUseTheSameUnicodeCaseVariants() {
        String[] characters = {
            "I",
            "i",
            "\u0130",
            "\u0131",
            "S",
            "s",
            "\u017f",
            "K",
            "k",
            "\u212a",
            "\u00df",
            "\u1e9e",
            "\ufb05",
            "\ufb06",
            "\u03a3",
            "\u03c3",
            "\u03c2",
            "\ud801\udc00",
            "\ud801\udc28"
        };
        for (String pattern : characters) {
            for (String input : characters) {
                boolean expected = pattern.toLowerCase(java.util.Locale.ROOT)
                                .equals(input.toLowerCase(java.util.Locale.ROOT))
                        || pattern.toUpperCase(java.util.Locale.ROOT).equals(input.toUpperCase(java.util.Locale.ROOT));
                Assertions.assertEquals(expected, matches(input, pattern, "iq"), "quoted " + pattern + " / " + input);
                Assertions.assertEquals(expected, matches(input, pattern, "i"), "literal " + pattern + " / " + input);
                Assertions.assertEquals(
                        expected, matches(input, "[" + pattern + "]", "i"), "class " + pattern + " / " + input);
            }
        }
    }

    @Test
    public void quotedCaseInsensitivePatternsKeepWhitespaceAndSyntaxLiteral() {
        String pattern = " [\ufb05]$\\ ";
        String input = " [\ufb06]$\\ ";
        Assertions.assertTrue(matches(input, pattern, "iqxms"));
        Assertions.assertFalse(matches(input.trim(), pattern, "iqxms"));
        Assertions.assertFalse(matches("ss", "\u00df", "iq"));
        var regex = RegexPatternUtils.compileRegex(pattern, "iq", ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertEquals(0, regex.groupCount());
        Assertions.assertEquals(
                "$1\\",
                regex.pattern().matcher(input).replaceAll(regex.replacement("$1\\", ExceptionMetadata.EMPTY_METADATA)));
    }
}
