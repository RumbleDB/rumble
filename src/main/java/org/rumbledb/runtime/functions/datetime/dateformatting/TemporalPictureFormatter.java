package org.rumbledb.runtime.functions.datetime.dateformatting;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class TemporalPictureFormatter {

    @FunctionalInterface
    interface ComponentSupport {
        boolean supports(char component);
    }

    private static final Map<String, ParsedPicture> PICTURE_CACHE = new ConcurrentHashMap<>();

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
        ParsedPicture parsed = PICTURE_CACHE.get(pictureString);
        if (parsed == null) {
            parsed = parsePicture(pictureString, metadata);
            PICTURE_CACHE.putIfAbsent(pictureString, parsed);
        }

        StringBuilder result = new StringBuilder();
        for (Segment segment : parsed.segments) {
            if (segment.marker == null) {
                result.append(segment.literal);
                continue;
            }

            result.append(
                TemporalComponentRenderer.render(
                    value,
                    segment.marker,
                    hasExplicitTimezone,
                    formattingContext,
                    componentSupport,
                    pictureString,
                    metadata
                )
            );
        }

        return applyFallbackPrefixes(result.toString(), parsed, formattingContext);
    }

    /**
     * Scans and parses a picture string once into an ordered list of literal/marker segments.
     */
    private static ParsedPicture parsePicture(String pictureString, ExceptionMetadata metadata) {
        ParseRequest request = new ParseRequest(pictureString, metadata);
        PictureScanState state = new PictureScanState();
        List<Segment> segments = new ArrayList<>();
        StringBuilder pendingLiteral = new StringBuilder();

        for (int i = 0; i < pictureString.length(); i++) {
            i = consumeCharacter(request, state, pendingLiteral, segments, i);
        }

        appendTrailingLiteralOrFail(request, state, pendingLiteral);
        flushLiteral(pendingLiteral, segments);

        return new ParsedPicture(segments, state.languageSensitiveOutput, state.calendarSensitiveOutput);
    }

    private static int consumeCharacter(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            List<Segment> segments,
            int index
    ) {
        if (state.insideVariableMarker) {
            return consumeVariableMarkerCharacter(request, state, pendingLiteral, segments, index);
        }

        return consumeLiteralCharacter(request, state, pendingLiteral, index);
    }

    /**
     * Consumes a character while scanning the inside of a variable marker.
     */
    private static int consumeVariableMarkerCharacter(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            List<Segment> segments,
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

        flushLiteral(pendingLiteral, segments);
        segments.add(Segment.marker(variableMarker));

        state.insideVariableMarker = false;
        state.startOfSequence = index + 1;

        return index;
    }

    /**
     * Consumes a character while scanning literal text outside a variable marker.
     */
    private static int consumeLiteralCharacter(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            int index
    ) {
        char c = request.pictureString.charAt(index);

        if (c == ']') {
            return consumeClosingBracket(request, state, pendingLiteral, index);
        }

        if (c == '[') {
            return consumeOpeningBracket(request, state, pendingLiteral, index);
        }

        return index;
    }

    /**
     * Consumes an escaped closing bracket or reports a syntax error for an unmatched closing bracket.
     */
    private static int consumeClosingBracket(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            int index
    ) {
        if (!hasNextCharacter(request.pictureString, index, ']')) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        pendingLiteral.append(request.pictureString, state.startOfSequence, index + 1);
        state.startOfSequence = index + 2;

        return index + 1;
    }

    /**
     * Consumes an opening bracket as either an escaped literal or the start of a variable marker.
     */
    private static int consumeOpeningBracket(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            int index
    ) {
        if (isLastCharacter(request.pictureString, index)) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        if (hasNextCharacter(request.pictureString, index, '[')) {
            return consumeEscapedOpeningBracket(request, state, pendingLiteral, index);
        }

        return beginVariableMarker(request, state, pendingLiteral, index);
    }

    /**
     * Consumes an escaped opening bracket and appends it as literal text.
     */
    private static int consumeEscapedOpeningBracket(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            int index
    ) {
        pendingLiteral.append(request.pictureString, state.startOfSequence, index + 1);
        state.startOfSequence = index + 2;

        return index + 1;
    }

    /**
     * Starts a variable marker and flushes the preceding literal text.
     */
    private static int beginVariableMarker(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral,
            int index
    ) {
        pendingLiteral.append(request.pictureString, state.startOfSequence, index);
        state.insideVariableMarker = true;
        state.startOfSequence = index + 1;

        return index;
    }

    /**
     * Appends trailing literal text or reports a syntax error for an unclosed variable marker.
     */
    private static void appendTrailingLiteralOrFail(
            ParseRequest request,
            PictureScanState state,
            StringBuilder pendingLiteral
    ) {
        if (state.startOfSequence == request.pictureString.length()) {
            return;
        }

        if (state.insideVariableMarker) {
            throw syntaxError(request.pictureString, request.metadata);
        }

        pendingLiteral.append(request.pictureString, state.startOfSequence, request.pictureString.length());
    }

    private static void flushLiteral(StringBuilder pendingLiteral, List<Segment> segments) {
        if (pendingLiteral.length() == 0) {
            return;
        }

        segments.add(Segment.literal(pendingLiteral.toString()));
        pendingLiteral.setLength(0);
    }

    /**
     * Adds fallback prefixes when the formatted output depends on fallback language or calendar data.
     */
    private static String applyFallbackPrefixes(
            String formatted,
            ParsedPicture parsed,
            FormattingContext formattingContext
    ) {
        if (parsed.calendarSensitiveOutput && formattingContext.calendarFallback) {
            formatted = "[Calendar: " + formattingContext.calendarDesignator + "] " + formatted;
        }

        if (parsed.languageSensitiveOutput && formattingContext.languageFallback) {
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
     * Bundles the inputs needed while parsing a picture string.
     */
    private static final class ParseRequest {
        final String pictureString;
        final ExceptionMetadata metadata;

        private ParseRequest(String pictureString, ExceptionMetadata metadata) {
            this.pictureString = pictureString;
            this.metadata = metadata;
        }
    }

    /**
     * One piece of a parsed picture string: either a literal run of text, or a variable marker to render
     * against a specific date value. Exactly one of the two fields is non-null.
     */
    private static final class Segment {
        final String literal;
        final VariableMarker marker;

        private Segment(String literal, VariableMarker marker) {
            this.literal = literal;
            this.marker = marker;
        }

        static Segment literal(String text) {
            return new Segment(text, null);
        }

        static Segment marker(VariableMarker marker) {
            return new Segment(null, marker);
        }
    }

    /**
     * The immutable result of parsing a picture string
     */
    private static final class ParsedPicture {
        final List<Segment> segments;
        final boolean languageSensitiveOutput;
        final boolean calendarSensitiveOutput;

        private ParsedPicture(
                List<Segment> segments,
                boolean languageSensitiveOutput,
                boolean calendarSensitiveOutput
        ) {
            this.segments = segments;
            this.languageSensitiveOutput = languageSensitiveOutput;
            this.calendarSensitiveOutput = calendarSensitiveOutput;
        }
    }
}
