package org.rumbledb.runtime.functions.datetime.dateformatting;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;

final class WidthModifier {

    static final int UNBOUNDED = -1;

    final String presentationPart;
    final int minWidth;
    final int maxWidth;

    private WidthModifier(String presentationPart, int minWidth, int maxWidth) {
        this.presentationPart = presentationPart;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }

    static WidthModifier parse(
            String rest,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int comma = rest.lastIndexOf(',');

        if (comma < 0) {
            return new WidthModifier(rest, 1, UNBOUNDED);
        }

        String presentationPart = rest.substring(0, comma);
        String widthPart = rest.substring(comma + 1);

        ParsedWidth width = parseWidth(widthPart, pictureString, metadata);

        return new WidthModifier(
                presentationPart,
                width.minWidth,
                width.maxWidth
        );
    }

    private static ParsedWidth parseWidth(
            String widthPart,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        if (widthPart.isEmpty()) {
            throw incorrectSyntax(pictureString, metadata);
        }

        String[] parts = widthPart.split("-", -1);

        if (parts.length > 2 || parts[0].isEmpty()) {
            throw incorrectSyntax(pictureString, metadata);
        }

        int min = parseWidthValue(parts[0], pictureString, metadata);
        int max = parts.length == 2
            ? parseWidthValue(parts[1], pictureString, metadata)
            : UNBOUNDED;

        validateWidth(min, max, pictureString, metadata);

        return new ParsedWidth(min, max);
    }

    private static int parseWidthValue(
            String value,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        if ("*".equals(value)) {
            return UNBOUNDED;
        }

        if (value.isEmpty()) {
            throw incorrectSyntax(pictureString, metadata);
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw incorrectSyntax(pictureString, metadata);
        }
    }

    private static void validateWidth(
            int min,
            int max,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        if (min == 0 || max == 0) {
            throw invalidPicture(pictureString, metadata);
        }

        if (min != UNBOUNDED && max != UNBOUNDED && min > max) {
            throw invalidPicture(pictureString, metadata);
        }
    }

    private static IncorrectSyntaxFormatDateTimeException incorrectSyntax(
            String pictureString,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + pictureString + "\": incorrect syntax",
                metadata
        );
    }

    private static IncorrectSyntaxFormatDateTimeException invalidPicture(
            String pictureString,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + pictureString + "\": invalid picture string",
                metadata
        );
    }

    private static final class ParsedWidth {
        final int minWidth;
        final int maxWidth;

        private ParsedWidth(int minWidth, int maxWidth) {
            this.minWidth = minWidth;
            this.maxWidth = maxWidth;
        }
    }
}
