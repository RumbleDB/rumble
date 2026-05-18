package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.runtime.functions.util.formatting.NumericPictureParser;

import java.time.OffsetDateTime;

final class FractionalSecondsFormatter {

    private FractionalSecondsFormatter() {
    }

    static String format(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions
    ) {
        if ("I".equals(variableMarker.presentation) || "i".equals(variableMarker.presentation)) {
            int value = fractionAsInteger(dt);
            String roman = org.rumbledb.runtime.functions.util.formatting.NumericFormattingSupport.integerToRoman(
                value
            );
            return "i".equals(variableMarker.presentation) ? roman.toLowerCase(formattingOptions.locale) : roman;
        }

        FractionalPattern pattern = FractionalPattern.parse(variableMarker.presentation);
        String fractionDigits = canonicalFractionDigits(dt);

        int minDigits;
        int maxDigits;

        if (pattern.isDefaultLike()) {
            minDigits = variableMarker.minWidth == -1 ? 0 : variableMarker.minWidth;
            maxDigits = variableMarker.maxWidth > 0 ? variableMarker.maxWidth : Integer.MAX_VALUE;
        } else {
            minDigits = Math.max(pattern.mandatoryDigits, variableMarker.minWidth == -1 ? 0 : variableMarker.minWidth);

            int pictureMaxDigits = pattern.activeDigits;
            int widthMaxDigits = variableMarker.maxWidth > 0 ? variableMarker.maxWidth : -1;

            if (widthMaxDigits > 0) {
                maxDigits = Math.max(pictureMaxDigits, widthMaxDigits);
            } else {
                maxDigits = pictureMaxDigits;
            }
        }

        if (maxDigits < Integer.MAX_VALUE && fractionDigits.length() > maxDigits) {
            fractionDigits = fractionDigits.substring(0, maxDigits);
        }

        fractionDigits = suppressTrailingZeros(fractionDigits, minDigits);

        if (fractionDigits.length() < minDigits) {
            fractionDigits = rightPad(fractionDigits, minDigits);
        }

        if (fractionDigits.isEmpty()) {
            fractionDigits = "0";
        }

        return mapDigitsAndInsertSeparators(
            fractionDigits,
            pattern.zeroDigit,
            pattern
        );
    }

    static int fractionAsInteger(OffsetDateTime dt) {
        return Integer.parseInt(canonicalFractionDigits(dt));
    }

    private static String canonicalFractionDigits(OffsetDateTime dt) {
        int nanos = dt.getNano();

        if (nanos == 0) {
            return "0";
        }

        String digits = String.format("%09d", nanos);
        int end = digits.length();

        while (end > 1 && digits.charAt(end - 1) == '0') {
            end--;
        }

        return digits.substring(0, end);
    }

    private static String suppressTrailingZeros(String digits, int minDigits) {
        int end = digits.length();

        while (end > minDigits && end > 0 && digits.charAt(end - 1) == '0') {
            end--;
        }

        return digits.substring(0, end);
    }

    private static String mapDigitsAndInsertSeparators(
            String digits,
            int zeroDigit,
            FractionalPattern pattern
    ) {
        StringBuilder sb = new StringBuilder();
        int usedDigits = Math.min(digits.length(), pattern.activeDigits);

        for (int i = 0; i < usedDigits; i++) {
            char d = digits.charAt(i);
            sb.appendCodePoint(zeroDigit + (d - '0'));

            String sep = pattern.separatorAfterIndex(i);
            if (sep != null) {
                sb.append(sep);
            }
        }

        if (digits.length() > usedDigits) {
            for (int i = usedDigits; i < digits.length(); i++) {
                char d = digits.charAt(i);
                sb.appendCodePoint(zeroDigit + (d - '0'));
            }
        }

        return sb.toString();
    }

    private static String rightPad(String value, int width) {
        if (value.length() >= width) {
            return value;
        }

        return value + "0".repeat(width - value.length());
    }

    private static final class FractionalPattern {
        final String raw;
        final int mandatoryDigits;
        final int activeDigits;
        final int zeroDigit;
        final String[] separatorsAfterSlot;

        private FractionalPattern(
                String raw,
                int mandatoryDigits,
                int activeDigits,
                int zeroDigit,
                String[] separatorsAfterSlot
        ) {
            this.raw = raw;
            this.mandatoryDigits = mandatoryDigits;
            this.activeDigits = activeDigits;
            this.zeroDigit = zeroDigit;
            this.separatorsAfterSlot = separatorsAfterSlot;
        }

        static FractionalPattern parse(String raw) {
            if (raw == null || raw.isEmpty()) {
                return new FractionalPattern("", 1, Integer.MAX_VALUE, '0', new String[0]);
            }

            int[] cps = raw.codePoints().toArray();
            int mandatory = 0;
            int active = 0;
            int zeroDigit = -1;
            boolean sawOptional = false;
            boolean lastWasActive = false;
            String[] tmpSeparators = new String[cps.length];
            StringBuilder pendingSeparator = new StringBuilder();

            for (int cp : cps) {
                if (cp == '#') {
                    if (mandatory == 0) {
                        throw new IllegalArgumentException("Invalid fractional pattern");
                    }

                    sawOptional = true;
                    active++;

                    if (pendingSeparator.length() > 0 && active > 1) {
                        tmpSeparators[active - 2] = pendingSeparator.toString();
                        pendingSeparator.setLength(0);
                    }

                    lastWasActive = true;
                    continue;
                }

                if (Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER) {
                    int z = NumericPictureParser.zeroDigitOf(cp);

                    if (zeroDigit < 0) {
                        zeroDigit = z;
                    } else if (zeroDigit != z) {
                        throw new IllegalArgumentException("Mixed digit families");
                    }

                    if (sawOptional) {
                        throw new IllegalArgumentException("Mandatory digit after optional digit");
                    }

                    mandatory++;
                    active++;

                    if (pendingSeparator.length() > 0 && active > 1) {
                        tmpSeparators[active - 2] = pendingSeparator.toString();
                        pendingSeparator.setLength(0);
                    }

                    lastWasActive = true;
                    continue;
                }

                if (!lastWasActive) {
                    throw new IllegalArgumentException("Invalid fractional separator placement");
                }

                pendingSeparator.appendCodePoint(cp);
                lastWasActive = false;
            }

            if (mandatory == 0) {
                throw new IllegalArgumentException("No mandatory digit");
            }

            if (pendingSeparator.length() > 0) {
                throw new IllegalArgumentException("Trailing separator");
            }

            if (zeroDigit < 0) {
                zeroDigit = '0';
            }

            String[] separators = new String[Math.max(0, active - 1)];
            System.arraycopy(tmpSeparators, 0, separators, 0, separators.length);

            return new FractionalPattern(raw, mandatory, active, zeroDigit, separators);
        }

        boolean isDefaultLike() {
            if (this.raw == null || this.raw.isEmpty()) {
                return true;
            }

            if (this.separatorsAfterSlot.length != 0) {
                return false;
            }
            return this.activeDigits == 1 && this.mandatoryDigits == 1;
        }

        String separatorAfterIndex(int index) {
            if (index < 0 || index >= this.separatorsAfterSlot.length) {
                return null;
            }
            return this.separatorsAfterSlot[index];
        }
    }
}
