package org.rumbledb.runtime.functions.datetime.dateformatting;


final class ParsedPresentationModifier {

    static final char NO_SECOND_MODIFIER = '\0';
    static final char ALPHABETIC = 'a';
    static final char TRADITIONAL = 't';
    static final char CARDINAL = 'c';
    static final char ORDINAL = 'o';

    final String firstPresentationModifier;
    final char secondModifier;

    private ParsedPresentationModifier(String firstPresentationModifier, char secondModifier) {
        this.firstPresentationModifier = firstPresentationModifier;
        this.secondModifier = secondModifier;
    }

    static ParsedPresentationModifier parse(
            String presentation
    ) {
        String basePresentation = presentation;
        char secondModifier = NO_SECOND_MODIFIER;

        if (presentation.length() > 1) {
            char last = presentation.charAt(presentation.length() - 1);

            if (isSecondPresentationModifier(last)) {
                basePresentation = presentation.substring(0, presentation.length() - 1);
                secondModifier = last;
            }
        }

        return new ParsedPresentationModifier(basePresentation, secondModifier);
    }

    private static boolean isSecondPresentationModifier(char c) {
        return c == ALPHABETIC || c == TRADITIONAL || c == CARDINAL || c == ORDINAL;
    }
}
