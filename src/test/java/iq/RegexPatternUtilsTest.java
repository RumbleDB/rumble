package iq;

import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.strings.RegexPatternUtils;

public class RegexPatternUtilsTest {

    @Test
    public void caseInsensitiveNegatedClassesDoNotMatchEquivalentLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "[^i]",
            "i",
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertEquals("I", compiledRegex.getPattern().matcher("I").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveSubtractedClassesPreserveExcludedLetters() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "[A-Z-[OI]]",
            "i",
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertEquals("O", compiledRegex.getPattern().matcher("O").replaceAll("x"));
    }

    @Test
    public void caseInsensitiveAsciiRangesIncludeKelvinSign() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "[A-Z]",
            "i",
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertEquals("x", compiledRegex.getPattern().matcher("\u212A").replaceAll("x"));
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveLiteralMatching() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "q",
            "i",
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertArrayEquals(
            new String[] { "A", "Ba", "" },
            RegexPatternUtils.tokenize("AqBaQ", compiledRegex.getPattern())
        );
    }

    @Test
    public void tokenizeSeparatorsHonorCaseInsensitiveMultiCharacterPatterns() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "[^q]é",
            "i",
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertArrayEquals(
            new String[] { "", "" },
            RegexPatternUtils.tokenize("xÉ", compiledRegex.getPattern())
        );
    }

    @Test
    public void tokenizeReturnsEmptySequenceForEmptyInput() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "\\s+",
            null,
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertArrayEquals(
            new String[0],
            RegexPatternUtils.tokenize("", compiledRegex.getPattern())
        );
    }

    @Test
    public void tokenizePreservesLeadingEmptyToken() {
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            "(ab)|(a)",
            null,
            ExceptionMetadata.EMPTY_METADATA
        );

        Assert.assertArrayEquals(
            new String[] { "", "r", "c", "d", "r", "" },
            RegexPatternUtils.tokenize("abracadabra", compiledRegex.getPattern())
        );
    }

    @Test
    public void tokenizeOnXmlWhitespaceDoesNotSplitOnFormFeed() {
        Assert.assertArrayEquals(
            new String[] { "abc\fdef" },
            RegexPatternUtils.tokenizeOnXmlWhitespace("abc\fdef")
        );
    }

    @Test
    public void tokenizeOnXmlWhitespaceSplitsOnXmlWhitespaceOnly() {
        Assert.assertArrayEquals(
            new String[] { "abc", "def", "ghi", "jkl" },
            RegexPatternUtils.tokenizeOnXmlWhitespace(" abc\tdef\nghi\rjkl ")
        );
    }
}
