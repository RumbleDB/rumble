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
            compiledRegex.getPattern().split("AqBaQ", -1)
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
            compiledRegex.getPattern().split("xÉ", -1)
        );
    }
}
