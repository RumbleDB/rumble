package org.rumbledb.runtime.functions.datetime.dateformatting;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;

import java.time.OffsetDateTime;

final class TemporalPictureFormatter {

    @FunctionalInterface
    interface ComponentSupport {
        boolean supports(char component);
    }

    private TemporalPictureFormatter() {
    }

    /**
     * Formats the given temporal value according to the picture string and formatting context.
     */
    static String format(
            OffsetDateTime value,
            String pictureString,
            boolean hasExplicitTimezone,
            FormattingContext formattingContext,
            ComponentSupport componentSupport,
            ExceptionMetadata metadata
    ) {
        FormattingRequest request = new FormattingRequest(
                value,
                pictureString,
                hasExplicitTimezone,
                formattingContext,
                componentSupport,
                metadata
        );

        PictureScanState state = new PictureScanState();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < pictureString.length(); i++) {
            i = consumeCharacter(request, state, result, i);
        }

        appendTrailingLiteralOrFail(request, state, result);
        return applyFallbackPrefixes(result.toString(), state, formattingContext);
    }

    private static int consumeCharacter(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        if (state.insideVariableMarker) {
            return consumeVariableMarkerCharacter(request, state, result, index);
        }

        return consumeLiteralCharacter(request, state, result, index);
    }

    /**
     * Consumes a character while scanning the inside of a variable marker.
     */
    private static int consumeVariableMarkerCharacter(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        if (request.pictureString.charAt(index) != ']') {
            return index;
        }

        String rawVariableMarker = request.pictureString.substring(state.startOfSequence, index);

        VariableMarker variableMarker = TemporalPictureParser.parse(
            rawVariableMarker,
            request.pictureString,
            request.metadata
        );

        state.languageSensitiveOutput = state.languageSensitiveOutput || usesLanguage(variableMarker);
        state.calendarSensitiveOutput = state.calendarSensitiveOutput || usesCalendar(variableMarker);

        result.append(
            TemporalComponentRenderer.render(
                request.value,
                variableMarker,
                request.hasExplicitTimezone,
                request.formattingContext,
                request.componentSupport,
                request.pictureString,
                request.metadata
            )
        );

        state.insideVariableMarker = false;
        state.startOfSequence = index + 1;

        return index;
    }

    /**
     * Consumes a character while scanning literal text outside a variable marker.
     */
    private static int consumeLiteralCharacter(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        char c = request.pictureString.charAt(index);

        if (c == ']') {
            return consumeClosingBracket(request, state, result, index);
        }

        if (c == '[') {
            return consumeOpeningBracket(request, state, result, index);
        }

        return index;
    }

    /**
     * Consumes an escaped closing bracket or reports a syntax error for an unmatched closing bracket.
     */
    private static int consumeClosingBracket(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        if (!hasNextCharacter(request.pictureString, index, ']')) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        result.append(request.pictureString, state.startOfSequence, index + 1);
        state.startOfSequence = index + 2;

        return index + 1;
    }

    /**
     * Consumes an opening bracket as either an escaped literal or the start of a variable marker.
     */
    private static int consumeOpeningBracket(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        if (isLastCharacter(request.pictureString, index)) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        if (hasNextCharacter(request.pictureString, index, '[')) {
            return consumeEscapedOpeningBracket(request, state, result, index);
        }

        return beginVariableMarker(request, state, result, index);
    }

    /**
     * Consumes an escaped opening bracket and appends it as literal text.
     */
    private static int consumeEscapedOpeningBracket(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        result.append(request.pictureString, state.startOfSequence, index + 1);
        state.startOfSequence = index + 2;

        return index + 1;
    }

    /**
     * Starts a variable marker and appends the preceding literal text.
     */
    private static int beginVariableMarker(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result,
            int index
    ) {
        result.append(request.pictureString, state.startOfSequence, index);
        state.insideVariableMarker = true;
        state.startOfSequence = index + 1;

        return index;
    }

    /**
     * Appends trailing literal text or reports a syntax error for an unclosed variable marker.
     */
    private static void appendTrailingLiteralOrFail(
            FormattingRequest request,
            PictureScanState state,
            StringBuilder result
    ) {
        if (state.startOfSequence == request.pictureString.length()) {
            return;
        }

        if (state.insideVariableMarker) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        result.append(request.pictureString.substring(state.startOfSequence));
    }

    /**
     * Adds fallback prefixes when the formatted output depends on fallback language or calendar data.
     */
    private static String applyFallbackPrefixes(
            String formatted,
            PictureScanState state,
            FormattingContext formattingContext
    ) {
        if (state.calendarSensitiveOutput && formattingContext.calendarFallback) {
            formatted = "[Calendar: " + formattingContext.calendarDesignator + "] " + formatted;
        }

        if (state.languageSensitiveOutput && formattingContext.languageFallback) {
            formatted = "[Language: " + formattingContext.effectiveLanguage + "] " + formatted;
        }

        return formatted;
    }

    /**
     * Returns true if the next character exists and matches the expected character.
     */
    private static boolean hasNextCharacter(String string, int index, char expected) {
        return index + 1 < string.length() && string.charAt(index + 1) == expected;
    }

    /**
     * Returns true if the given index points to the last character of the string.
     */
    private static boolean isLastCharacter(String string, int index) {
        return index == string.length() - 1;
    }

    /**
     * Returns true if the marker output depends on calendar-specific formatting.
     */
    private static boolean usesCalendar(VariableMarker marker) {
        switch (marker.component) {
            case 'Y':
            case 'M':
            case 'D':
            case 'd':
            case 'W':
            case 'w':
            case 'F':
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns true if the marker output depends on language-specific formatting.
     */
    private static boolean usesLanguage(VariableMarker marker) {
        switch (marker.kind) {
            case VariableMarker.Kind.NAME:
            case VariableMarker.Kind.WORDS:
                return true;
            case VariableMarker.Kind.TIMEZONE:
                return marker.timezonePicture.named;
            default:
                return false;
        }
    }

    private static IncorrectSyntaxFormatDateTimeException syntaxError(
            String pictureString,
            ExceptionMetadata metadata
    ) {
        String message = String.format("\"%s\": incorrect syntax", pictureString);
        return new IncorrectSyntaxFormatDateTimeException(message, metadata);
    }

    /**
     * Holds the mutable state used while scanning a picture string.
     */
    private static final class PictureScanState {
        int startOfSequence = 0;
        boolean insideVariableMarker = false;
        boolean languageSensitiveOutput = false;
        boolean calendarSensitiveOutput = false;
    }

    /**
     * Bundles the immutable inputs needed while formatting a picture string.
     */
    private static final class FormattingRequest {
        final OffsetDateTime value;
        final String pictureString;
        final boolean hasExplicitTimezone;
        final FormattingContext formattingContext;
        final ComponentSupport componentSupport;
        final ExceptionMetadata metadata;

        private FormattingRequest(
                OffsetDateTime value,
                String pictureString,
                boolean hasExplicitTimezone,
                FormattingContext formattingContext,
                ComponentSupport componentSupport,
                ExceptionMetadata metadata
        ) {
            this.value = value;
            this.pictureString = pictureString;
            this.hasExplicitTimezone = hasExplicitTimezone;
            this.formattingContext = formattingContext;
            this.componentSupport = componentSupport;
            this.metadata = metadata;
        }
    }
}
