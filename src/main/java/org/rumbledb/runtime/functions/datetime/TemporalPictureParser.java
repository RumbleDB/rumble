package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.runtime.functions.base.formatting.NumericPicture;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

final class TemporalPictureParser {

    static ParsedVariableMarker parse(
            String rawVariableMarker,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        String variableMarker = normalizeVariableMarker(rawVariableMarker);
        if (variableMarker.isEmpty()) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": incorrect syntax",
                    metadata
            );
        }

        char component = variableMarker.charAt(0);
        if (!isRecognizedComponent(component)) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": invalid picture string",
                    metadata
            );
        }

        String rest = variableMarker.substring(1);

        int widthComma = findWidthSeparatorComma(rest);
        String presentationWithModifiers = widthComma >= 0 ? rest.substring(0, widthComma) : rest;
        String widthPart = widthComma >= 0 ? rest.substring(widthComma + 1) : "";

        ParsedPresentation presentation = parsePresentationModifiers(
            component,
            presentationWithModifiers,
            pictureStringForErrors,
            metadata
        );
        ParsedWidth width = parseWidth(widthPart, pictureStringForErrors, metadata);

        if (component == 'Z' || component == 'z') {
            ParsedTimezonePicture tz = TimezonePictureParser.parse(
                component,
                presentation.basePresentation,
                pictureStringForErrors,
                metadata
            );
            return ParsedVariableMarker.forTimezone(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                tz
            );
        }

        if ("I".equals(presentation.basePresentation)) {
            return ParsedVariableMarker.forRoman(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                false
            );
        }
        if ("i".equals(presentation.basePresentation)) {
            return ParsedVariableMarker.forRoman(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                true
            );
        }

        if ("A".equals(presentation.basePresentation) && isAlphabeticPresentableComponent(component)) {
            return ParsedVariableMarker.forAlphabetic(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                false
            );
        }
        if ("a".equals(presentation.basePresentation) && isAlphabeticPresentableComponent(component)) {
            return ParsedVariableMarker.forAlphabetic(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                true
            );
        }

        if (isNamePresentation(presentation.basePresentation) && isNamePresentableComponent(component)) {
            return ParsedVariableMarker.forName(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                parseNameForm(presentation.basePresentation)
            );
        }

        if (isWordPresentation(presentation.basePresentation) && isWordPresentableComponent(component)) {
            return ParsedVariableMarker.forWords(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                parseWordCase(presentation.basePresentation)
            );
        }

        if (presentation.basePresentation.isEmpty()) {
            if (component == 'f') {
                return ParsedVariableMarker.forDefault(
                    component,
                    presentation.basePresentation,
                    width.minWidth,
                    width.maxWidth,
                    presentation.ordinal
                );
            }

            if (
                component == 'Y'
                    || component == 'M'
                    || component == 'D'
                    || component == 'd'
                    || component == 'H'
                    || component == 'h'
                    || component == 'm'
                    || component == 's'
            ) {
                return ParsedVariableMarker.forNumeric(
                    component,
                    "1",
                    width.minWidth,
                    width.maxWidth,
                    presentation.ordinal,
                    null,
                    false
                );
            }
            return ParsedVariableMarker.forDefault(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal
            );
        }

        if (component == 'f') {
            validateFractionalPresentation(presentation.basePresentation, pictureStringForErrors, metadata);
            return ParsedVariableMarker.forNumeric(
                component,
                presentation.basePresentation,
                width.minWidth,
                width.maxWidth,
                presentation.ordinal,
                null,
                true
            );
        }

        NumericPicture np = NumericPictureParser.parseForDate(
            presentation.basePresentation,
            pictureStringForErrors,
            metadata
        );
        return ParsedVariableMarker.forNumeric(
            component,
            presentation.basePresentation,
            width.minWidth,
            width.maxWidth,
            presentation.ordinal,
            np,
            true
        );
    }

    static String normalizeVariableMarker(String s) {
        StringBuilder sb = new StringBuilder();
        s.codePoints()
            .filter(cp -> !Character.isWhitespace(cp))
            .forEach(sb::appendCodePoint);
        return sb.toString();
    }

    private static boolean isRecognizedComponent(char c) {
        return c == 'Y'
            || c == 'M'
            || c == 'D'
            || c == 'd'
            || c == 'F'
            || c == 'W'
            || c == 'w'
            || c == 'H'
            || c == 'h'
            || c == 'P'
            || c == 'm'
            || c == 's'
            || c == 'f'
            || c == 'Z'
            || c == 'z'
            || c == 'X'
            || c == 'C'
            || c == 'E';
    }

    private static boolean isNamePresentation(String presentation) {
        return "N".equals(presentation) || "n".equals(presentation) || "Nn".equals(presentation);
    }

    private static boolean isWordPresentation(String presentation) {
        return "W".equals(presentation) || "w".equals(presentation) || "Ww".equals(presentation);
    }

    private static boolean isNamePresentableComponent(char component) {
        return component == 'M' || component == 'F' || component == 'P';
    }

    private static boolean isWordPresentableComponent(char component) {
        return component == 'Y'
            || component == 'M'
            || component == 'D'
            || component == 'd'
            || component == 'F'
            || component == 'W'
            || component == 'w';
    }

    private static boolean isAlphabeticPresentableComponent(char component) {
        return component == 'Y'
            || component == 'M'
            || component == 'D'
            || component == 'd'
            || component == 'F'
            || component == 'W'
            || component == 'w'
            || component == 'H'
            || component == 'h'
            || component == 'm'
            || component == 's';
    }

    private static ParsedVariableMarker.NameForm parseNameForm(String presentation) {
        switch (presentation) {
            case "N":
                return ParsedVariableMarker.NameForm.UPPER;
            case "n":
                return ParsedVariableMarker.NameForm.LOWER;
            case "Nn":
                return ParsedVariableMarker.NameForm.TITLE;
            default:
                throw new IllegalArgumentException("Unsupported name presentation: " + presentation);
        }
    }

    private static ParsedVariableMarker.WordCase parseWordCase(String presentation) {
        switch (presentation) {
            case "W":
                return ParsedVariableMarker.WordCase.UPPER;
            case "w":
                return ParsedVariableMarker.WordCase.LOWER;
            case "Ww":
                return ParsedVariableMarker.WordCase.TITLE;
            default:
                throw new IllegalArgumentException("Unsupported word presentation: " + presentation);
        }
    }

    private static ParsedPresentation parsePresentationModifiers(
            char component,
            String presentation,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        boolean ordinal = false;
        String base = presentation;

        if (base.endsWith("o")) {
            ordinal = true;
            base = base.substring(0, base.length() - 1);
        }

        if (component != 'Z' && component != 'z' && base.endsWith("t")) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": invalid picture string",
                    metadata
            );
        }

        if (base.endsWith("c")) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": invalid picture string",
                    metadata
            );
        }

        return new ParsedPresentation(base, ordinal);
    }

    private static int findWidthSeparatorComma(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ',') {
                String suffix = s.substring(i + 1);
                if (suffix.matches("(\\*|\\d+)(-(\\*|\\d+))?")) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static ParsedWidth parseWidth(String widthPart, String pictureStringForErrors, ExceptionMetadata metadata) {
        if (widthPart.isEmpty()) {
            return new ParsedWidth(1, -1);
        }

        String[] parts = widthPart.split("-", -1);
        if (parts.length > 2 || parts[0].isEmpty()) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": incorrect syntax",
                    metadata
            );
        }

        int min = parseWidthPart(parts[0], pictureStringForErrors, metadata);
        int max = parts.length == 2 ? parseWidthPart(parts[1], pictureStringForErrors, metadata) : -1;

        if (min == 0 || max == 0) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": invalid picture string",
                    metadata
            );
        }
        if (max != -1 && min != -1 && min > max) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": invalid picture string",
                    metadata
            );
        }

        return new ParsedWidth(min, max);
    }

    private static int parseWidthPart(String s, String pictureStringForErrors, ExceptionMetadata metadata) {
        if ("*".equals(s)) {
            return -1;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureStringForErrors + "\": incorrect syntax",
                    metadata
            );
        }
    }

    private static void validateFractionalPresentation(
            String picture,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if ("I".equals(picture) || "i".equals(picture)) {
            return;
        }
        if (picture.isEmpty()) {
            return;
        }

        int[] cps = picture.codePoints().toArray();
        boolean sawActive = false;
        boolean sawMandatory = false;
        boolean sawOptional = false;
        boolean lastWasActive = false;

        for (int cp : cps) {
            if (cp == '#') {
                if (!sawMandatory) {
                    throw invalidPicture(pictureStringForErrors, metadata);
                }
                sawOptional = true;
                sawActive = true;
                lastWasActive = true;
                continue;
            }

            if (Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER) {
                if (sawOptional) {
                    throw invalidPicture(pictureStringForErrors, metadata);
                }
                sawMandatory = true;
                sawActive = true;
                lastWasActive = true;
                continue;
            }

            if (!sawActive || !lastWasActive) {
                throw invalidPicture(pictureStringForErrors, metadata);
            }
            lastWasActive = false;
        }

        if (!sawMandatory || !lastWasActive) {
            throw invalidPicture(pictureStringForErrors, metadata);
        }
    }

    private static IncorrectSyntaxFormatDateTimeException invalidPicture(
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + pictureStringForErrors + "\": invalid picture string",
                metadata
        );
    }

    private static final class ParsedPresentation {
        final String basePresentation;
        final boolean ordinal;

        ParsedPresentation(String basePresentation, boolean ordinal) {
            this.basePresentation = basePresentation;
            this.ordinal = ordinal;
        }
    }

    private static final class ParsedWidth {
        final int minWidth;
        final int maxWidth;

        ParsedWidth(int minWidth, int maxWidth) {
            this.minWidth = minWidth;
            this.maxWidth = maxWidth;
        }
    }
}
