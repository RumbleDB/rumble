package org.rumbledb.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidDecimalFormatPropertyConflict;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DecimalFormatDeclarationHelper {

    private DecimalFormatDeclarationHelper() {
    }

    public static void processDecimalFormatDeclaration(
            ParserRuleContext declarationContext,
            boolean isDefaultDecimalFormat,
            ParseTree nameContext,
            List<? extends ParseTree> propertyNames,
            List<? extends ParseTree> stringLiterals,
            StaticContext moduleContext,
            ExceptionMetadata metadata
    ) {
        Name name = null;
        if (!isDefaultDecimalFormat) {
            name = processDecimalFormatName(nameContext, moduleContext, metadata);
        }

        DecimalFormatDefinition defaults = DecimalFormatDefinition.defaultInstance();

        int decimalSeparator = defaults.getDecimalSeparator();
        int groupingSeparator = defaults.getGroupingSeparator();
        String infinity = defaults.getInfinity();
        int minusSign = defaults.getMinusSign();
        String nanSymbol = defaults.getNanSymbol();
        int percent = defaults.getPercent();
        int perMille = defaults.getPerMille();
        int zeroDigit = defaults.getZeroDigit();
        int optionalDigit = defaults.getOptionalDigit();
        int patternSeparator = defaults.getPatternSeparator();
        int exponentSeparator = defaults.getExponentSeparator();

        Set<String> seenProperties = new HashSet<>();

        for (int i = 0; i < propertyNames.size(); i++) {
            String propertyName = propertyNames.get(i).getText();
            String value = parseStringLiteral(stringLiterals.get(i).getText());

            boolean hasSeen = !seenProperties.add(propertyName);
            if (hasSeen) {
                throw new SemanticException(
                        "Decimal format property defined more than once: " + propertyName,
                        metadata
                );
            }

            switch (propertyName) {
                case "decimal-separator":
                    decimalSeparator = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "grouping-separator":
                    groupingSeparator = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "infinity":
                    infinity = value;
                    break;
                case "minus-sign":
                    minusSign = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "NaN":
                    nanSymbol = value;
                    break;
                case "percent":
                    percent = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "per-mille":
                    perMille = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "zero-digit":
                    zeroDigit = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "digit":
                    optionalDigit = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "pattern-separator":
                    patternSeparator = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                case "exponent-separator":
                    exponentSeparator = requireSingleCodePoint(propertyName, value, metadata);
                    break;
                default:
                    throw new OurBadException("Unknown decimal format property: " + propertyName);
            }
        }

        requireValidZeroDigitFamily(zeroDigit, metadata);

        DecimalFormatDefinition decimalFormat = new DecimalFormatDefinition(
                decimalSeparator,
                groupingSeparator,
                infinity,
                minusSign,
                nanSymbol,
                percent,
                perMille,
                zeroDigit,
                optionalDigit,
                patternSeparator,
                exponentSeparator
        );

        validateDecimalFormat(decimalFormat, metadata);

        if (isDefaultDecimalFormat) {
            moduleContext.setDefaultDecimalFormat(decimalFormat);
        } else {
            moduleContext.addDecimalFormat(name, decimalFormat, metadata);
        }
    }

    public static String parseStringLiteral(String text) {
        if (text == null || text.length() < 2) {
            throw new OurBadException("Invalid string literal: " + text);
        }

        char quote = text.charAt(0);
        char last = text.charAt(text.length() - 1);

        if ((quote != '"' && quote != '\'') || quote != last) {
            throw new OurBadException("Invalid string literal: " + text);
        }

        String content = text.substring(1, text.length() - 1);

        if (quote == '"') {
            return content.replace("\"\"", "\"");
        }
        return content.replace("''", "'");
    }

    public static int requireSingleCodePoint(String propertyName, String value, ExceptionMetadata metadata) {
        if (value == null || value.codePointCount(0, value.length()) != 1) {
            throw new SemanticException(
                    "Decimal format property '" + propertyName + "' must be exactly one character.",
                    metadata
            );
        }
        return value.codePointAt(0);
    }

    public static void addUnique(
            Set<Integer> characters,
            int codePoint,
            String propertyName,
            ExceptionMetadata metadata
    ) {
        if (!characters.add(codePoint)) {
            throw new InvalidDecimalFormatPropertyConflict(
                    "Decimal format contains duplicate picture-string character at property: " + propertyName,
                    metadata
            );
        }
    }

    public static void validateDecimalFormat(DecimalFormatDefinition decimalFormat, ExceptionMetadata metadata) {
        Set<Integer> characters = new HashSet<>();

        addUnique(characters, decimalFormat.getDecimalSeparator(), "decimal-separator", metadata);
        addUnique(characters, decimalFormat.getExponentSeparator(), "exponent-separator", metadata);
        addUnique(characters, decimalFormat.getGroupingSeparator(), "grouping-separator", metadata);
        addUnique(characters, decimalFormat.getPercent(), "percent", metadata);
        addUnique(characters, decimalFormat.getPerMille(), "per-mille", metadata);
        addUnique(characters, decimalFormat.getOptionalDigit(), "digit", metadata);
        addUnique(characters, decimalFormat.getPatternSeparator(), "pattern-separator", metadata);

        for (int i = 0; i < 10; i++) {
            addUnique(characters, decimalFormat.getZeroDigit() + i, "zero-digit family", metadata);
        }
    }

    public static void requireValidZeroDigitFamily(int zeroDigit, ExceptionMetadata metadata) {
        for (int i = 0; i < 10; i++) {
            int cp = zeroDigit + i;
            if (!Character.isDigit(cp)) {
                throw new SemanticException(
                        "The zero-digit property must define a family of 10 consecutive digits.",
                        metadata
                );
            }
        }
    }

    public static Name processDecimalFormatName(
            ParseTree nameContext,
            StaticContext moduleContext,
            ExceptionMetadata metadata
    ) {
        if (nameContext == null) {
            throw new OurBadException("Decimal format name context must not be null.");
        }

        String text = nameContext.getText();
        if (text == null || text.isEmpty()) {
            throw new SemanticException(
                    "Invalid empty decimal format name.",
                    metadata
            );
        }

        if (text.startsWith("Q{")) {
            int closingBrace = text.indexOf('}');
            if (closingBrace < 0 || closingBrace == text.length() - 1) {
                throw new SemanticException(
                        "Invalid URIQualifiedName: " + text,
                        metadata
                );
            }

            String namespace = text.substring(2, closingBrace);
            String localName = text.substring(closingBrace + 1);

            if (localName.isEmpty()) {
                throw new SemanticException(
                        "Invalid URIQualifiedName, missing local name: " + text,
                        metadata
                );
            }

            return new Name(namespace, null, localName);
        }

        int colon = text.indexOf(':');

        if (colon < 0) {
            return Name.createVariableInNoNamespace(text);
        }

        String prefix = text.substring(0, colon);
        String localName = text.substring(colon + 1);

        if (prefix.isEmpty() || localName.isEmpty()) {
            throw new SemanticException(
                    "Invalid QName: " + text,
                    metadata
            );
        }

        String namespace = moduleContext.resolveNamespace(prefix);
        if (namespace == null) {
            throw new SemanticException(
                    "Prefix " + prefix + " could not be resolved against a namespace in scope.",
                    metadata
            );
        }

        return new Name(namespace, prefix, localName);
    }
}
