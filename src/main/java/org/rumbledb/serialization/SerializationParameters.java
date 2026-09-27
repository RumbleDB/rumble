/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.serialization;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Serialization parameters with the fixed fn:serialize map defaults from F&O 3.1.
 * RumbleDB also uses these defaults for fn:serialize with omitted or XML parameters.
 * Field initializers apply to every new instance; mutable collections belong to that instance.
 * Application output defaults are selected separately by {@link #defaults(String)}.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-serialize">F&O 3.1 fn:serialize</a>
 *
 *      Specification references:
 *
 *      <ul>
 *      <li>XQuery 3.1 Static Context Components — default serialization parameters (link:
 *      https://www.w3.org/TR/xquery-31/#id-xq-static-context-components)</li>
 *      <li>XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
 *      https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)</li>
 *      </ul>
 *
 */
@Getter
@Setter
public class SerializationParameters implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Serialization method.
     * "method" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     * Note: RumbleDB supports additional methods in addition to the XQuery 3.1 specification.
     */
    private String method = "xml";

    /**
     * Character encoding.
     * "encoding" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String encoding = "UTF-8";

    /**
     * Output version (for example XML 1.0/1.1 or HTML 4.0/5.0 depending on the method).
     * "version" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String version = "1.0";

    /**
     * Whether to omit the XML declaration.
     * "omit-xml-declaration" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean omitXmlDeclaration = true;

    public enum Standalone {
        YES,
        NO,
        OMIT
    }

    /**
     * XML standalone declaration.
     * "standalone" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Standalone standalone = Standalone.OMIT;

    /**
     * DocType system identifier.
     * "doctype-system" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String doctypeSystem;

    /**
     * DocType public identifier.
     * "doctype-public" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String doctypePublic;

    /**
     * Media type.
     * "media-type" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String mediaType; // null selects a default for the current method

    /**
     * Normalize characters using a Unicode normalization form.
     * "normalization-form" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String normalizationForm = "none";

    /**
     * Whether to declare namespace undeclarations.
     * "undeclare-prefixes" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean undeclarePrefixes;

    /**
     * Character maps, mapping strings to strings.
     * "use-character-maps" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Map<String, String> characterMaps = new HashMap<>();

    /**
     * Element QNames to output using CDATA sections.
     * "cdata-section-elements" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Set<String> cdataSectionElements = new HashSet<>();

    /**
     * Include meta http-equiv content-type.
     * "include-content-type" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean includeContentType = true;

    /**
     * Escape URI attributes.
     * "escape-uri-attributes" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean escapeUriAttributes = true;

    /**
     * HTML version.
     * "html-version" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String htmlVersion = "5";

    /**
     * Insert byte-order mark.
     * "byte-order-mark" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean byteOrderMark;

    /**
     * Indentation control.
     * "indent" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean indent;

    /**
     * Number of spaces for indentation (implementation-defined default).
     * "indent-spaces" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private int indentSpaces = -1; // -1 means unspecified

    /**
     * Elements whose content should not be indented.
     * "suppress-indentation" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Set<String> suppressIndentation = new HashSet<>();

    /**
     * Separator between items of the top-level sequence; null means absent, not an empty separator.
     * "item-separator" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String itemSeparator;

    /**
     * JSON: allow duplicate map keys.
     * "allow-duplicate-names" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    @Getter(AccessLevel.NONE)
    private boolean allowDuplicateNames;

    public enum JsonNodeOutputMethod {
        UNSPECIFIED,
        JSON,
        XML,
        XHTML,
        HTML,
        TEXT
    }

    /**
     * JSON node output method.
     * "json-node-output-method" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private JsonNodeOutputMethod jsonNodeOutputMethod = JsonNodeOutputMethod.XML;

    /**
     * Extension/unknown parameters preserved for forward compatibility.
     */
    private Map<String, String> extensionParameters = new HashMap<>();

    /**
     * Spark-specific options for DataFrameWriter (e.g., CSV delimiter, compression, etc.).
     * These are passed directly to Spark's DataFrameWriter.option() method.
     */
    private Map<String, String> sparkOptions = new HashMap<>();

    public static SerializationParameters defaults() {
        return defaults(null);
    }

    /**
     * Application output defaults, including the JSONiq hybrid method and newline-separated results.
     * fn:serialize uses the standard field defaults through the no-argument constructor instead.
     */
    public static SerializationParameters defaults(String queryLanguage) {
        SerializationParameters p = new SerializationParameters();
        p.method = defaultMethod(queryLanguage);
        p.version = null;
        p.omitXmlDeclaration = false;
        p.htmlVersion = null;
        p.itemSeparator = "\n";
        p.jsonNodeOutputMethod = JsonNodeOutputMethod.UNSPECIFIED;
        return p;
    }

    /** Returns the method name, accepting the equivalent Q{}name spelling for standard methods. */
    public String getMethod() {
        if (this.method == null) {
            return null;
        }
        String name = this.method.trim();
        return name.startsWith("Q{}") ? name.substring(3) : name;
    }

    /**
     * An explicit media type takes precedence. Otherwise choose a suitable type for the current
     * method, so overriding the method never leaves a stale default media type behind.
     */
    public String getMediaType() {
        if (this.mediaType != null && !this.mediaType.isEmpty()) {
            return this.mediaType;
        }
        String methodName = getMethod();
        if (methodName == null) {
            return "application/json";
        }
        return switch (methodName.toLowerCase(Locale.ROOT)) {
            case "xml" -> "application/xml";
            case "xhtml" -> "application/xhtml+xml";
            case "html" -> "text/html";
            case "json" -> "application/json";
            default -> "text/plain"; // text, adaptive, and implementation-defined text formats
        };
    }

    private static String defaultMethod(String queryLanguage) {
        if (queryLanguage != null && queryLanguage.startsWith("xquery")) {
            return "xml";
        }
        return "xml-json-hybrid";
    }

    public boolean getOmitXmlDeclaration() {
        return this.omitXmlDeclaration;
    }

    public boolean getUndeclarePrefixes() {
        return this.undeclarePrefixes;
    }

    public boolean getIncludeContentType() {
        return this.includeContentType;
    }

    public boolean getEscapeUriAttributes() {
        return this.escapeUriAttributes;
    }

    /**
     * Requested HTML version for the HTML/XHTML output methods.
     *
     * Per XSLT and XQuery Serialization 3.1, the requested HTML version is the
     * value of {@code html-version} when that parameter is present; otherwise it
     * falls back to {@code version}.
     *
     * @return the requested HTML version, or {@code null} if neither parameter is set
     */
    public String getRequestedHtmlVersion() {
        return this.htmlVersion != null ? this.htmlVersion : this.version;
    }

    /**
     * Whether the requested HTML version denotes HTML5.
     *
     * This check is intentionally narrow: we recognize the specific lexical forms
     * that should trigger the HTML5 branch ({@code 5} and {@code 5.0}) without
     * treating arbitrary version strings as decimals.
     *
     * @return {@code true} if the requested HTML version is {@code "5"} or {@code "5.0"}
     */
    public boolean isRequestedHtml5Version() {
        String requestedHtmlVersion = getRequestedHtmlVersion();
        if (requestedHtmlVersion == null || requestedHtmlVersion.trim().isEmpty()) {
            return false;
        }
        String trimmed = requestedHtmlVersion.trim();
        return "5".equals(trimmed) || "5.0".equals(trimmed);
    }

    public boolean getByteOrderMark() {
        return this.byteOrderMark;
    }

    public boolean getIndent() {
        return this.indent;
    }

    public boolean getAllowDuplicateNames() {
        return this.allowDuplicateNames;
    }

    /**
     * Returns a copy of the SerializationParameters instance.
     *
     * @param parameters the SerializationParameters instance to copy
     * @return a copy of the SerializationParameters instance
     */
    public static SerializationParameters copy(SerializationParameters parameters) {
        SerializationParameters copy = new SerializationParameters();
        copy.method = parameters.method;
        copy.encoding = parameters.encoding;
        copy.version = parameters.version;
        copy.omitXmlDeclaration = parameters.omitXmlDeclaration;
        copy.standalone = parameters.standalone;
        copy.doctypeSystem = parameters.doctypeSystem;
        copy.doctypePublic = parameters.doctypePublic;
        copy.mediaType = parameters.mediaType;
        copy.normalizationForm = parameters.normalizationForm;
        copy.undeclarePrefixes = parameters.undeclarePrefixes;
        copy.sparkOptions = new HashMap<>(parameters.sparkOptions);
        copy.characterMaps = new HashMap<>(parameters.characterMaps);
        copy.cdataSectionElements = new HashSet<>(parameters.cdataSectionElements);
        copy.includeContentType = parameters.includeContentType;
        copy.escapeUriAttributes = parameters.escapeUriAttributes;
        copy.htmlVersion = parameters.htmlVersion;
        copy.byteOrderMark = parameters.byteOrderMark;
        copy.indent = parameters.indent;
        copy.indentSpaces = parameters.indentSpaces;
        copy.suppressIndentation = new HashSet<>(parameters.suppressIndentation);
        copy.itemSeparator = parameters.itemSeparator;
        copy.allowDuplicateNames = parameters.allowDuplicateNames;
        copy.jsonNodeOutputMethod = parameters.jsonNodeOutputMethod;
        copy.extensionParameters = new HashMap<>(parameters.extensionParameters);
        return copy;
    }
}
