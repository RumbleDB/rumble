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
import org.rumbledb.runtime.functions.strings.RegexPatternUtils;

public class RegexPatternUtilsTest {

    @Test
    public void caseInsensitiveNegatedClassesDoNotMatchEquivalentLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[^i]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals("I", compiledRegex.getPattern().matcher("I").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveSubtractedClassesPreserveExcludedLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[A-Z-[OI]]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals("O", compiledRegex.getPattern().matcher("O").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveAsciiRangesIncludeKelvinSign() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[A-Z]", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals(
                "x", compiledRegex.getPattern().matcher("\u212A").replaceAll("x"));
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveLiteralMatching() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("q", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(
                new String[] {"A", "Ba", ""}, RegexPatternUtils.tokenize("AqBaQ", compiledRegex.getPattern()));
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveMultiCharacterPatterns() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("[^q]é", "i", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(
                new String[] {"", ""}, RegexPatternUtils.tokenize("xÉ", compiledRegex.getPattern()));
    }

    @Test
    public void tokenizeReturnsEmptySequenceForEmptyInput() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("\\s+", null, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(new String[0], RegexPatternUtils.tokenize("", compiledRegex.getPattern()));
    }

    @Test
    public void tokenizePreservesLeadingEmptyToken() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("(ab)|(a)", null, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertArrayEquals(
                new String[] {"", "r", "c", "d", "r", ""},
                RegexPatternUtils.tokenize("abracadabra", compiledRegex.getPattern()));
    }

    @Test
    public void multilineCaretMatchesEmptyStringForTokenizeValidation() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("^", "m", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern()));
    }

    @Test
    public void multilineWhitespaceAnchorsMatchEmptyStringForTokenizeValidation() {
        RegexPatternUtils.CompiledRegex compiledRegex =
                RegexPatternUtils.compileRegex("^[\\s]*$", "m", ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern()));
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
                .getPattern()
                .matcher("aa" + backReferenceDigits.substring(1))
                .matches());
    }

    @Test
    public void tokenizeOnXmlWhitespaceSplitsOnXmlWhitespaceOnly() {
        Assertions.assertArrayEquals(
                new String[] {"abc", "def", "ghi", "jkl"},
                RegexPatternUtils.tokenizeOnXmlWhitespace(" abc\tdef\nghi\rjkl "));
    }
}
