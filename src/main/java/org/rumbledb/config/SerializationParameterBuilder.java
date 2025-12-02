/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.config;

import org.rumbledb.exceptions.InvalidSerializationParameterValueException;
import org.rumbledb.serialization.SerializationParameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Builder class for constructing and updating SerializationParameters from string parameters.
 * Handles parsing and validation of serialization parameter values according to the
 * XQuery 3.1 Serialization Parameters specification.
 */
public final class SerializationParameterBuilder {

    /**
     * Private constructor to prevent instantiation.
     */
    private SerializationParameterBuilder() {
        // empty
    }

    /**
     * Builds a fresh {@link SerializationParameters} instance from the provided parameters.
     * This is equivalent to calling {@link #build(Map, SerializationParameters)} with
     * {@link SerializationParameters#defaults()}.
     * All parameter values are validated and an {@link InvalidSerializationParameterValueException} is thrown for
     * invalid inputs.
     *
     * @param parameters the raw parameter map (name → value) to apply
     * @return a configured {@link SerializationParameters} instance
     * @throws InvalidSerializationParameterValueException if any parameter value is invalid
     */
    public static SerializationParameters build(Map<String, String> parameters) {
        return build(parameters, SerializationParameters.defaults());
    }

    /**
     * Builds a {@link SerializationParameters} instance by copying an existing template and applying overrides.
     * A defensive copy of {@code defaults} is created so that the original instance is never mutated.
     * Each provided entry is applied via {@link #update(SerializationParameters, String, String)}, guaranteeing
     * consistent validation.
     *
     * @param parameters the override map (name → value) to apply
     * @param defaults the template to inherit unspecified parameters from
     * @return a configured {@link SerializationParameters} instance
     * @throws InvalidSerializationParameterValueException if any parameter value is invalid
     */
    public static SerializationParameters build(Map<String, String> parameters, SerializationParameters defaults) {
        SerializationParameters params = SerializationParameters.copy(defaults);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            update(params, entry.getKey(), entry.getValue());
        }
        return params;
    }

    /**
     * Updates a {@link SerializationParameters} instance in place with the provided parameter name and value.
     * Each update is validated; unknown parameter names are forwarded to {@code sparkOptions}.
     *
     * @param params the {@link SerializationParameters} instance to mutate (typically a local copy)
     * @param optionName the name of the parameter to update
     * @param optionValue the value of the parameter to update
     * @throws InvalidSerializationParameterValueException if the parameter value is invalid
     */
    public static void update(SerializationParameters params, String optionName, String optionValue)
            throws InvalidSerializationParameterValueException {

        if (optionValue == null || optionValue.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    optionName,
                    optionValue == null ? "null" : "''",
                    "a non-empty string"
            );
        }

        try {
            switch (optionName) {
                case "method":
                    validateMethod(optionName, optionValue);
                    params.setMethod(optionValue);
                    break;
                case "encoding":
                    validateEncoding(optionName, optionValue);
                    params.setEncoding(optionValue);
                    break;
                case "omit-xml-declaration":
                    params.setOmitXmlDeclaration(parseBoolean(optionName, optionValue));
                    break;
                case "standalone":
                    params.setStandalone(parseStandalone(optionName, optionValue));
                    break;
                case "doctype-system":
                    params.setDoctypeSystem(optionValue);
                    break;
                case "doctype-public":
                    params.setDoctypePublic(optionValue);
                    break;
                case "media-type":
                    params.setMediaType(optionValue);
                    break;
                case "normalization-form":
                    params.setNormalizationForm(parseNormalizationForm(optionName, optionValue));
                    break;
                case "undeclare-prefixes":
                    params.setUndeclarePrefixes(parseBoolean(optionName, optionValue));
                    break;
                case "include-content-type":
                    params.setIncludeContentType(parseBoolean(optionName, optionValue));
                    break;
                case "escape-uri-attributes":
                    params.setEscapeUriAttributes(parseBoolean(optionName, optionValue));
                    break;
                case "html-version":
                    params.setHtmlVersion(optionValue);
                    break;
                case "byte-order-mark":
                    params.setByteOrderMark(parseBoolean(optionName, optionValue));
                    break;
                case "indent":
                    params.setIndent(parseBoolean(optionName, optionValue));
                    break;
                case "indent-spaces":
                    params.setIndentSpaces(parseIndentSpaces(optionName, optionValue));
                    break;
                case "item-separator":
                    params.setItemSeparator(optionValue);
                    break;
                case "allow-duplicate-names":
                    params.setAllowDuplicateNames(parseBoolean(optionName, optionValue));
                    break;
                case "json-node-output-method":
                    params.setJsonNodeOutputMethod(parseJsonNodeOutputMethod(optionName, optionValue));
                    break;
                case "use-character-maps":
                    params.setCharacterMaps(parseCharacterMaps(optionName, optionValue));
                    break;
                case "cdata-section-elements":
                    params.setCdataSectionElements(parseStringSet(optionName, optionValue));
                    break;
                case "suppress-indentation":
                    params.setSuppressIndentation(parseStringSet(optionName, optionValue));
                    break;
                default:
                    // Unknown parameters go to sparkOptions (Spark DataFrameWriter options)
                    params.getSparkOptions().put(optionName, optionValue);
                    break;
            }
        } catch (IllegalArgumentException e) {
            // This should not happen as parse methods now throw
            // InvalidSerializationParameterValueException
            // But keeping as fallback
            throw new InvalidSerializationParameterValueException(
                    optionName,
                    optionValue,
                    "a valid value (" + e.getMessage() + ")"
            );
        }
    }

    /**
     * Validates the method parameter value.
     */
    private static void validateMethod(String parameterName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value == null ? "null" : "''",
                    "a non-empty string (e.g., 'xml', 'html', 'xhtml', 'text', 'json')"
            );
        }
        // Method validation: should be a valid serialization method
        // Common values: xml, html, xhtml, text, json, etc.
        // We accept any non-empty string as method names can be implementation-defined
    }

    /**
     * Validates the encoding parameter value.
     */
    private static void validateEncoding(String parameterName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value == null ? "null" : "''",
                    "a non-empty string (e.g., 'UTF-8', 'UTF-16', 'ISO-8859-1')"
            );
        }
        // Encoding validation: should be a valid IANA character encoding name
        // Common values: UTF-8, UTF-16, ISO-8859-1, etc.
        // We accept any non-empty string as encoding names can vary
    }

    /**
     * Parses a boolean value from string, accepting yes/no, true/false, or 1/0.
     */
    private static boolean parseBoolean(String parameterName, String value) {
        if (value == null) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    "null",
                    "'yes'/'no', 'true'/'false', or '1'/'0'"
            );
        }
        String lower = value.toLowerCase().trim();
        if (lower.equals("yes") || lower.equals("true") || lower.equals("1")) {
            return true;
        } else if (lower.equals("no") || lower.equals("false") || lower.equals("0")) {
            return false;
        } else {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "'yes'/'no', 'true'/'false', or '1'/'0'"
            );
        }
    }

    /**
     * Parses a Standalone enum value from string.
     * Accepts: yes, no, true, false, 1, 0, or omit.
     */
    private static SerializationParameters.Standalone parseStandalone(String parameterName, String value) {
        if (value == null) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    "null",
                    "'yes', 'no', 'true', 'false', '1', '0', or 'omit'"
            );
        }
        String lower = value.toLowerCase().trim();
        // Map boolean-like values to yes/no, then to enum
        if (lower.equals("true") || lower.equals("1")) {
            return SerializationParameters.Standalone.YES;
        } else if (lower.equals("false") || lower.equals("0")) {
            return SerializationParameters.Standalone.NO;
        } else {
            // Try direct enum value (yes, no, omit)
            String upper = value.toUpperCase().trim();
            try {
                return SerializationParameters.Standalone.valueOf(upper);
            } catch (IllegalArgumentException e) {
                throw new InvalidSerializationParameterValueException(
                        parameterName,
                        value,
                        "'yes', 'no', 'true', 'false', '1', '0', or 'omit'"
                );
            }
        }
    }

    /**
     * Parses a NormalizationForm enum value from string.
     * Accepts: NFC, NFD, NFKC, NFKD, fully-normalized, or none (case-sensitive).
     */
    private static SerializationParameters.NormalizationForm parseNormalizationForm(
            String parameterName,
            String value
    ) {
        if (value == null) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    "null",
                    "'NFC', 'NFD', 'NFKC', 'NFKD', 'fully-normalized', or 'none'"
            );
        }
        String trimmed = value.trim();
        // Map "fully-normalized" to "FULLY_NORMALIZED" enum value
        if (trimmed.equals("fully-normalized")) {
            return SerializationParameters.NormalizationForm.FULLY_NORMALIZED;
        }
        // Try direct enum value (case-sensitive)
        try {
            return SerializationParameters.NormalizationForm.valueOf(trimmed);
        } catch (IllegalArgumentException e) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "'NFC', 'NFD', 'NFKC', 'NFKD', 'fully-normalized', or 'none'"
            );
        }
    }

    /**
     * Parses a JsonNodeOutputMethod enum value from string.
     */
    private static SerializationParameters.JsonNodeOutputMethod parseJsonNodeOutputMethod(
            String parameterName,
            String value
    ) {
        if (value == null) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    "null",
                    "'UNSPECIFIED', 'JSON', 'XML', 'HTML', or 'TEXT'"
            );
        }
        String upper = value.toUpperCase().trim();
        try {
            return SerializationParameters.JsonNodeOutputMethod.valueOf(upper);
        } catch (IllegalArgumentException e) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "'UNSPECIFIED', 'JSON', 'XML', 'HTML', or 'TEXT'"
            );
        }
    }

    /**
     * Parses indent-spaces as an integer.
     */
    private static int parseIndentSpaces(String parameterName, String value) {
        if (value == null) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    "null",
                    "a non-negative integer"
            );
        }
        try {
            int spaces = Integer.parseInt(value.trim());
            if (spaces < 0) {
                throw new InvalidSerializationParameterValueException(
                        parameterName,
                        String.valueOf(spaces),
                        "a non-negative integer"
                );
            }
            return spaces;
        } catch (NumberFormatException e) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "a valid non-negative integer"
            );
        }
    }

    /**
     * Parses a comma-separated string into a Set of strings.
     */
    private static Set<String> parseStringSet(String parameterName, String value) {
        Set<String> result = new HashSet<>();
        if (value != null && !value.trim().isEmpty()) {
            String[] parts = value.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
        }
        if (result.isEmpty() && value != null && !value.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "a comma-separated list with at least one non-empty value"
            );
        }
        return result;
    }

    /**
     * Parses character maps from string format: key1=value1,key2=value2
     */
    private static Map<String, String> parseCharacterMaps(String parameterName, String value) {
        Map<String, String> result = new HashMap<>();
        if (value != null && !value.trim().isEmpty()) {
            String[] pairs = value.split(",");
            for (String pair : pairs) {
                String trimmed = pair.trim();
                if (!trimmed.isEmpty()) {
                    int separatorIndex = trimmed.indexOf('=');
                    if (separatorIndex > 0 && separatorIndex < trimmed.length() - 1) {
                        String key = trimmed.substring(0, separatorIndex).trim();
                        String val = trimmed.substring(separatorIndex + 1).trim();
                        if (!key.isEmpty() && !val.isEmpty()) {
                            result.put(key, val);
                        } else {
                            throw new InvalidSerializationParameterValueException(
                                    parameterName,
                                    trimmed,
                                    "a key=value pair with non-empty key and value"
                            );
                        }
                    } else {
                        throw new InvalidSerializationParameterValueException(
                                parameterName,
                                trimmed,
                                "a key=value pair"
                        );
                    }
                }
            }
        }
        if (result.isEmpty() && value != null && !value.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    parameterName,
                    value,
                    "a comma-separated list of key=value pairs with at least one valid pair"
            );
        }
        return result;
    }
}
