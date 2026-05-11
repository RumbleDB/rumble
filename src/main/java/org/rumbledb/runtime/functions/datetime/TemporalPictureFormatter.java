package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;

final class TemporalPictureFormatter {

    @FunctionalInterface
    interface ComponentSupport {
        boolean supports(char component);
    }

    private TemporalPictureFormatter() {
    }

    static String format(
            OffsetDateTime value,
            String pictureString,
            String pictureStringForErrors,
            boolean hasExplicitTimezone,
            FormattingOptions formattingOptions,
            ComponentSupport componentSupport,
            ExceptionMetadata metadata
    ) {
        int startOfSequence = 0;
        boolean variableMarkerSequence = false;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < pictureString.length(); i++) {
            char c = pictureString.charAt(i);

            if (variableMarkerSequence) {
                if (c == ']') {
                    String rawVariableMarker = pictureString.substring(startOfSequence, i);

                    ParsedVariableMarker parsed = TemporalPictureParser.parse(
                        rawVariableMarker,
                        pictureStringForErrors,
                        metadata
                    );

                    result.append(
                        TemporalComponentRenderer.render(
                            value,
                            parsed,
                            hasExplicitTimezone,
                            formattingOptions,
                            componentSupport,
                            pictureStringForErrors,
                            metadata
                        )
                    );

                    variableMarkerSequence = false;
                    startOfSequence = i + 1;
                }
            } else {
                if (c == ']') {
                    if (i == pictureString.length() - 1 || pictureString.charAt(i + 1) != ']') {
                        throw syntaxError(pictureStringForErrors, metadata);
                    } else {
                        result.append(pictureString, startOfSequence, i + 1);
                        startOfSequence = i + 2;
                        i++;
                    }
                } else if (c == '[') {
                    if (i == pictureString.length() - 1) {
                        throw syntaxError(pictureStringForErrors, metadata);
                    }

                    if (pictureString.charAt(i + 1) == '[') {
                        result.append(pictureString, startOfSequence, i + 1);
                        startOfSequence = i + 2;
                        i++;
                    } else {
                        result.append(pictureString, startOfSequence, i);
                        variableMarkerSequence = true;
                        startOfSequence = i + 1;
                    }
                }
            }
        }

        if (startOfSequence != pictureString.length()) {
            if (variableMarkerSequence) {
                throw syntaxError(pictureStringForErrors, metadata);
            }
            result.append(pictureString.substring(startOfSequence));
        }

        return result.toString();
    }

    private static IncorrectSyntaxFormatDateTimeException syntaxError(
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        String message = String.format("\"%s\": incorrect syntax", pictureStringForErrors);
        return new IncorrectSyntaxFormatDateTimeException(message, metadata);
    }
}
