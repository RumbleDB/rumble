package org.rumbledb.runtime.functions.datetime.dateformatting;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.exceptions.IncorrectSyntaxFormatNumberException;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.FormatIntegerPictureParser;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.PrimaryFormatToken;

import java.util.Set;

final class TemporalPictureParser {

    static VariableMarker parse(
            String rawVariableMarker,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        String variableMarker = normalizeVariableMarker(rawVariableMarker);
        if (variableMarker.isEmpty()) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureString + "\": incorrect syntax",
                    metadata
            );
        }

        char component = variableMarker.charAt(0);
        if (!isRecognizedComponent(component)) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureString + "\": invalid picture string",
                    metadata
            );
        }

        String rest = variableMarker.substring(1);

        WidthModifier width = WidthModifier.parse(rest, pictureString, metadata);

        ParsedPresentationModifier presentation = ParsedPresentationModifier.parse(
            width.presentationPart
        );

        if (component == 'Z' || component == 'z') {
            ParsedTimezonePicture tz = TimezonePictureParser.parse(
                component,
                presentation.firstPresentationModifier,
                presentation.secondModifier,
                pictureString,
                metadata
            );
            return VariableMarker.forTimezone(
                component,
                presentation.firstPresentationModifier,
                width.minWidth,
                width.maxWidth,
                presentation.secondModifier,
                tz
            );
        }

        if (usesDefaultFallbackForSecondModifier(presentation.secondModifier)) {
            return defaultVariableMarker(
                component,
                width,
                ParsedPresentationModifier.NO_SECOND_MODIFIER
            );
        }

        if (isNamePresentation(presentation.firstPresentationModifier)) {
            if (!isNamePresentableComponent(component)) {
                return defaultVariableMarker(component, width, presentation.secondModifier);
            }

            return VariableMarker.forName(
                component,
                presentation.firstPresentationModifier,
                width.minWidth,
                width.maxWidth,
                presentation.secondModifier,
                parseNameForm(presentation.firstPresentationModifier)
            );
        }

        if (presentation.firstPresentationModifier.isEmpty()) {
            return defaultVariableMarker(component, width, presentation.secondModifier);
        }

        if (component == 'f') {
            FractionalSecondsFormatter.validatePresentation(
                presentation.firstPresentationModifier,
                pictureString,
                metadata
            );

            return VariableMarker.forFractionalSeconds(
                component,
                presentation.firstPresentationModifier,
                width.minWidth,
                width.maxWidth,
                presentation.secondModifier
            );
        }

        if (!isFormatIntegerPresentableComponent(component)) {
            return defaultVariableMarker(component, width, presentation.secondModifier);
        }

        PrimaryFormatToken primaryFormatToken = parsePrimaryFormatToken(
            presentation.firstPresentationModifier,
            pictureString,
            metadata
        );

        switch (primaryFormatToken.getType()) {
            case PrimaryFormatToken.DECIMAL:
                return VariableMarker.forNumeric(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    primaryFormatToken.getNumericPicture(),
                    true
                );

            case PrimaryFormatToken.ROMAN_UPPER:
                return VariableMarker.forRoman(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    false
                );

            case PrimaryFormatToken.ROMAN_LOWER:
                return VariableMarker.forRoman(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    true
                );

            case PrimaryFormatToken.ALPHABETIC_UPPER:
                return VariableMarker.forAlphabetic(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    false
                );

            case PrimaryFormatToken.ALPHABETIC_LOWER:
                return VariableMarker.forAlphabetic(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    true
                );

            case PrimaryFormatToken.WORDS_UPPER:
            case PrimaryFormatToken.WORDS_LOWER:
            case PrimaryFormatToken.WORDS_TITLE:
                return VariableMarker.forWords(
                    component,
                    presentation.firstPresentationModifier,
                    width.minWidth,
                    width.maxWidth,
                    presentation.secondModifier,
                    parseWordCase(presentation.firstPresentationModifier),
                    presentation.formatSpecifier
                );

            case PrimaryFormatToken.OTHER:
            default:
                return defaultVariableMarker(component, width, presentation.secondModifier);
        }
    }

    static String normalizeVariableMarker(String s) {
        StringBuilder sb = new StringBuilder();
        s.codePoints()
            .filter(cp -> !Character.isWhitespace(cp))
            .forEach(sb::appendCodePoint);
        return sb.toString();
    }

    private static VariableMarker defaultVariableMarker(
            char component,
            WidthModifier width,
            char secondPresentationModifier
    ) {
        if (component == 'f') {
            return VariableMarker.forFractionalSeconds(
                component,
                "",
                width.minWidth,
                width.maxWidth,
                secondPresentationModifier
            );
        }

        if (component == 'F') {
            int maxWidth = width.maxWidth == WidthModifier.UNBOUNDED && width.minWidth == 1
                ? 3
                : width.maxWidth;
            return VariableMarker.forName(
                component,
                "",
                width.minWidth,
                maxWidth,
                secondPresentationModifier,
                null
            );
        }

        if (component == 'P') {
            return VariableMarker.forAmPm(
                component,
                "",
                width.minWidth,
                width.maxWidth,
                secondPresentationModifier
            );
        }

        if (isDefaultNumericComponent(component)) {
            return VariableMarker.forNumeric(
                component,
                "1",
                width.minWidth,
                width.maxWidth,
                secondPresentationModifier,
                null,
                false
            );
        }

        return VariableMarker.forDefault(
            component,
            "",
            width.minWidth,
            width.maxWidth,
            secondPresentationModifier
        );
    }

    private static PrimaryFormatToken parsePrimaryFormatToken(
            String presentation,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        try {
            return FormatIntegerPictureParser.parsePrimaryFormatToken(
                presentation,
                pictureString,
                metadata
            );
        } catch (IncorrectSyntaxFormatNumberException e) {
            throw new IncorrectSyntaxFormatDateTimeException(
                    "\"" + pictureString + "\": invalid picture string",
                    metadata
            );
        }
    }

    private static final Set<Character> RECOGNIZED_COMPONENTS = Set.of(
        'Y',
        'M',
        'D',
        'd',
        'F',
        'W',
        'w',
        'H',
        'h',
        'P',
        'm',
        's',
        'f',
        'Z',
        'z',
        'X',
        'C',
        'E'
    );

    private static final Set<Character> NAME_PRESENTABLE_COMPONENTS = Set.of(
        'M',
        'F',
        'P'
    );

    private static final Set<Character> FORMAT_INTEGER_PRESENTABLE_COMPONENTS = Set.of(
        'Y',
        'M',
        'D',
        'd',
        'F',
        'W',
        'w',
        'H',
        'h',
        'm',
        's'
    );

    private static final Set<Character> DEFAULT_NUMERIC_COMPONENTS = Set.of(
        'Y',
        'M',
        'D',
        'd',
        'W',
        'w',
        'H',
        'h',
        'm',
        's'
    );

    private static boolean isFormatIntegerPresentableComponent(char component) {
        return FORMAT_INTEGER_PRESENTABLE_COMPONENTS.contains(component);
    }

    private static boolean isDefaultNumericComponent(char component) {
        return DEFAULT_NUMERIC_COMPONENTS.contains(component);
    }

    /**
     * We don't support Alphabetic or Traditional Second Presentation Modifiers right now
     */
    private static boolean usesDefaultFallbackForSecondModifier(char secondModifier) {
        return secondModifier == ParsedPresentationModifier.ALPHABETIC
            || secondModifier == ParsedPresentationModifier.TRADITIONAL;
    }

    private static boolean isRecognizedComponent(char component) {
        return RECOGNIZED_COMPONENTS.contains(component);
    }

    private static boolean isNamePresentableComponent(char component) {
        return NAME_PRESENTABLE_COMPONENTS.contains(component);
    }

    private static boolean isNamePresentation(String presentation) {
        return "N".equals(presentation) || "n".equals(presentation) || "Nn".equals(presentation);
    }

    private static String parseNameForm(String presentation) {
        switch (presentation) {
            case "N":
                return VariableMarker.NameForm.UPPER;
            case "n":
                return VariableMarker.NameForm.LOWER;
            case "Nn":
                return VariableMarker.NameForm.TITLE;
            default:
                throw new IllegalArgumentException("Unsupported name presentation: " + presentation);
        }
    }

    private static String parseWordCase(String presentation) {
        switch (presentation) {
            case "W":
                return VariableMarker.WordCase.UPPER;
            case "w":
                return VariableMarker.WordCase.LOWER;
            case "Ww":
                return VariableMarker.WordCase.TITLE;
            default:
                throw new IllegalArgumentException("Unsupported word presentation: " + presentation);
        }
    }
}
