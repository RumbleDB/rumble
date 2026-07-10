package org.rumbledb.runtime.functions.datetime.dateformatting;


final class ParsedPresentationModifier {

    static final char NO_SECOND_MODIFIER = '\0';
    static final char ALPHABETIC = 'a';
    static final char TRADITIONAL = 't';
    static final char CARDINAL = 'c';
    static final char ORDINAL = 'o';

    final String firstPresentationModifier;
    final char secondModifier;
    final String formatSpecifier;

    private ParsedPresentationModifier(
            String firstPresentationModifier,
            char secondModifier,
            String formatSpecifier
    ) {
        this.firstPresentationModifier = firstPresentationModifier;
        this.secondModifier = secondModifier;
        this.formatSpecifier = formatSpecifier;
    }

    static ParsedPresentationModifier parse(
            String presentation
    ) {
        // A cardinal or ordinal modifier may carry a parenthesized format specifier, e.g. o(-te).
        if (presentation.endsWith(")")) {
            int open = presentation.lastIndexOf('(');
            if (
                open > 1
                    && open < presentation.length() - 2
                    && isCardinalityModifier(presentation.charAt(open - 1))
            ) {
                return new ParsedPresentationModifier(
                        presentation.substring(0, open - 1),
                        presentation.charAt(open - 1),
                        presentation.substring(open + 1, presentation.length() - 1)
                );
            }
        }

        String basePresentation = presentation;
        char secondModifier = NO_SECOND_MODIFIER;

        if (presentation.length() > 1) {
            char last = presentation.charAt(presentation.length() - 1);

            if (isSecondPresentationModifier(last)) {
                basePresentation = presentation.substring(0, presentation.length() - 1);
                secondModifier = last;
            }
        }

        return new ParsedPresentationModifier(basePresentation, secondModifier, null);
    }

    private static boolean isSecondPresentationModifier(char c) {
        return c == ALPHABETIC || c == TRADITIONAL || c == CARDINAL || c == ORDINAL;
    }

    private static boolean isCardinalityModifier(char c) {
        return c == CARDINAL || c == ORDINAL;
    }
}
