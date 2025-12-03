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

package org.rumbledb.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default serialization parameters stored in the XQuery static context.
 *
 * Specification references:
 * - XQuery 3.1 Static Context Components — default serialization parameters (link:
 * https://www.w3.org/TR/xquery-31/#id-xq-static-context-components)
 * - XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
 * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
 *
 */
public class SerializationParameters implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    /**
     * Serialization method.
     * "method" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     * Note: RumbleDB supports additional methods in addition to the XQuery 3.1 specification.
     */
    private String method;

    /**
     * Character encoding.
     * "encoding" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String encoding;

    /**
     * Whether to omit the XML declaration.
     * "omit-xml-declaration" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean omitXmlDeclaration;

    public enum Standalone {
        YES, NO, OMIT
    }

    /**
     * XML standalone declaration.
     * "standalone" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Standalone standalone;

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
    private String mediaType;

    public enum NormalizationForm {
        NFC, NFD, NFKC, NFKD, FULLY_NORMALIZED, NONE
    }

    /**
     * Normalize characters using a Unicode normalization form.
     * "normalization-form" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private NormalizationForm normalizationForm;

    /**
     * Whether to declare namespace undeclarations.
     * "undeclare-prefixes" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean undeclarePrefixes;

    /**
     * Character maps, mapping strings to strings.
     * "use-character-maps" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Map<String, String> characterMaps;

    /**
     * Element QNames to output using CDATA sections.
     * "cdata-section-elements" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Set<String> cdataSectionElements;

    /**
     * Include meta http-equiv content-type.
     * "include-content-type" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean includeContentType;

    /**
     * Escape URI attributes.
     * "escape-uri-attributes" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean escapeUriAttributes;

    /**
     * HTML version (implementation-defined default).
     * "html-version" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String htmlVersion;

    /**
     * Insert byte-order mark.
     * "byte-order-mark" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean byteOrderMark;

    /**
     * Indentation control.
     * "indent" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean indent;

    /**
     * Number of spaces for indentation (implementation-defined default).
     * "indent-spaces" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private int indentSpaces; // -1 means unspecified

    /**
     * Elements whose content should not be indented.
     * "suppress-indentation" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private Set<String> suppressIndentation;

    /**
     * Separator between items of the top-level sequence.
     * "item-separator" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String itemSeparator;

    /**
     * JSON: allow duplicate map keys.
     * "allow-duplicate-names" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private boolean allowDuplicateNames;

    public enum JsonNodeOutputMethod {
        UNSPECIFIED, JSON, XML, HTML, TEXT
    }

    /**
     * JSON node output method.
     * "json-node-output-method" — XSLT and XQuery Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private JsonNodeOutputMethod jsonNodeOutputMethod;

    /**
     * Extension/unknown parameters preserved for forward compatibility.
     */
    private Map<String, String> extensionParameters;

    /**
     * Spark-specific options for DataFrameWriter (e.g., CSV delimiter, compression, etc.).
     * These are passed directly to Spark's DataFrameWriter.option() method.
     */
    private Map<String, String> sparkOptions;

    public SerializationParameters() {
        // empty for Kryo/Java serialization
    }

    public static SerializationParameters defaults() {
        SerializationParameters p = new SerializationParameters();
        // Spec-aligned conservative defaults; implementation-defined noted explicitly
        p.method = "xml-json-hybrid"; // implementation defined default
        p.encoding = "UTF-8";
        p.omitXmlDeclaration = false;
        p.standalone = Standalone.OMIT;
        p.doctypeSystem = null;
        p.doctypePublic = null;
        p.mediaType = null;
        p.normalizationForm = NormalizationForm.NONE;
        p.undeclarePrefixes = false;
        p.characterMaps = new HashMap<>();
        p.cdataSectionElements = new HashSet<>();
        p.includeContentType = true;
        p.escapeUriAttributes = true;
        p.htmlVersion = null; // implementation-defined
        p.byteOrderMark = false;
        p.indent = false;
        p.indentSpaces = -1; // implementation-defined/unspecified
        p.suppressIndentation = new HashSet<>();
        p.itemSeparator = null; // implementation-defined
        p.allowDuplicateNames = false;
        p.jsonNodeOutputMethod = JsonNodeOutputMethod.UNSPECIFIED;
        p.extensionParameters = new HashMap<>();
        p.sparkOptions = new HashMap<>();
        return p;
    }

    // Getters and setters
    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean getOmitXmlDeclaration() {
        return this.omitXmlDeclaration;
    }

    public void setOmitXmlDeclaration(boolean omitXmlDeclaration) {
        this.omitXmlDeclaration = omitXmlDeclaration;
    }

    public Standalone getStandalone() {
        return this.standalone;
    }

    public void setStandalone(Standalone standalone) {
        this.standalone = standalone;
    }

    public String getDoctypeSystem() {
        return this.doctypeSystem;
    }

    public void setDoctypeSystem(String doctypeSystem) {
        this.doctypeSystem = doctypeSystem;
    }

    public String getDoctypePublic() {
        return this.doctypePublic;
    }

    public void setDoctypePublic(String doctypePublic) {
        this.doctypePublic = doctypePublic;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public NormalizationForm getNormalizationForm() {
        return this.normalizationForm;
    }

    public void setNormalizationForm(NormalizationForm normalizationForm) {
        this.normalizationForm = normalizationForm;
    }

    public boolean getUndeclarePrefixes() {
        return this.undeclarePrefixes;
    }

    public void setUndeclarePrefixes(boolean undeclarePrefixes) {
        this.undeclarePrefixes = undeclarePrefixes;
    }

    public Map<String, String> getCharacterMaps() {
        return this.characterMaps;
    }

    public void setCharacterMaps(Map<String, String> characterMaps) {
        this.characterMaps = characterMaps;
    }

    public Set<String> getCdataSectionElements() {
        return this.cdataSectionElements;
    }

    public void setCdataSectionElements(Set<String> cdataSectionElements) {
        this.cdataSectionElements = cdataSectionElements;
    }

    public boolean getIncludeContentType() {
        return this.includeContentType;
    }

    public void setIncludeContentType(boolean includeContentType) {
        this.includeContentType = includeContentType;
    }

    public boolean getEscapeUriAttributes() {
        return this.escapeUriAttributes;
    }

    public void setEscapeUriAttributes(boolean escapeUriAttributes) {
        this.escapeUriAttributes = escapeUriAttributes;
    }

    public String getHtmlVersion() {
        return this.htmlVersion;
    }

    public void setHtmlVersion(String htmlVersion) {
        this.htmlVersion = htmlVersion;
    }

    public boolean getByteOrderMark() {
        return this.byteOrderMark;
    }

    public void setByteOrderMark(boolean byteOrderMark) {
        this.byteOrderMark = byteOrderMark;
    }

    public boolean getIndent() {
        return this.indent;
    }

    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    public int getIndentSpaces() {
        return this.indentSpaces;
    }

    public void setIndentSpaces(int indentSpaces) {
        this.indentSpaces = indentSpaces;
    }

    public Set<String> getSuppressIndentation() {
        return this.suppressIndentation;
    }

    public void setSuppressIndentation(Set<String> suppressIndentation) {
        this.suppressIndentation = suppressIndentation;
    }

    public String getItemSeparator() {
        return this.itemSeparator;
    }

    public void setItemSeparator(String itemSeparator) {
        this.itemSeparator = itemSeparator;
    }

    public boolean getAllowDuplicateNames() {
        return this.allowDuplicateNames;
    }

    public void setAllowDuplicateNames(boolean allowDuplicateNames) {
        this.allowDuplicateNames = allowDuplicateNames;
    }

    public JsonNodeOutputMethod getJsonNodeOutputMethod() {
        return this.jsonNodeOutputMethod;
    }

    public void setJsonNodeOutputMethod(JsonNodeOutputMethod jsonNodeOutputMethod) {
        this.jsonNodeOutputMethod = jsonNodeOutputMethod;
    }

    public Map<String, String> getExtensionParameters() {
        return this.extensionParameters;
    }

    public void setExtensionParameters(Map<String, String> extensionParameters) {
        this.extensionParameters = extensionParameters;
    }

    public Map<String, String> getSparkOptions() {
        return this.sparkOptions;
    }

    public void setSparkOptions(Map<String, String> sparkOptions) {
        this.sparkOptions = sparkOptions;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.method);
        output.writeString(this.encoding);
        output.writeBoolean(this.omitXmlDeclaration);
        output.writeString(this.standalone != null ? this.standalone.name() : null);
        output.writeString(this.doctypeSystem);
        output.writeString(this.doctypePublic);
        output.writeString(this.mediaType);
        output.writeString(this.normalizationForm != null ? this.normalizationForm.name() : null);
        output.writeBoolean(this.undeclarePrefixes);

        // characterMaps
        int cmSize = this.characterMaps != null ? this.characterMaps.size() : 0;
        output.writeInt(cmSize);
        if (cmSize > 0) {
            for (Map.Entry<String, String> e : this.characterMaps.entrySet()) {
                output.writeString(e.getKey());
                output.writeString(e.getValue());
            }
        }

        // cdataSectionElements
        int cdataSize = this.cdataSectionElements != null ? this.cdataSectionElements.size() : 0;
        output.writeInt(cdataSize);
        if (cdataSize > 0) {
            for (String qn : this.cdataSectionElements) {
                output.writeString(qn);
            }
        }

        output.writeBoolean(this.includeContentType);
        output.writeBoolean(this.escapeUriAttributes);
        output.writeString(this.htmlVersion);
        output.writeBoolean(this.byteOrderMark);
        output.writeBoolean(this.indent);
        output.writeInt(this.indentSpaces);

        // suppressIndentation
        int siSize = this.suppressIndentation != null ? this.suppressIndentation.size() : 0;
        output.writeInt(siSize);
        if (siSize > 0) {
            for (String qn : this.suppressIndentation) {
                output.writeString(qn);
            }
        }

        output.writeString(this.itemSeparator);
        output.writeBoolean(this.allowDuplicateNames);
        output.writeString(this.jsonNodeOutputMethod != null ? this.jsonNodeOutputMethod.name() : null);

        // extensionParameters
        int epSize = this.extensionParameters != null ? this.extensionParameters.size() : 0;
        output.writeInt(epSize);
        if (epSize > 0) {
            for (Map.Entry<String, String> e : this.extensionParameters.entrySet()) {
                output.writeString(e.getKey());
                output.writeString(e.getValue());
            }
        }

        // sparkOptions
        int soSize = this.sparkOptions != null ? this.sparkOptions.size() : 0;
        output.writeInt(soSize);
        if (soSize > 0) {
            for (Map.Entry<String, String> e : this.sparkOptions.entrySet()) {
                output.writeString(e.getKey());
                output.writeString(e.getValue());
            }
        }
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.method = input.readString();
        this.encoding = input.readString();
        this.omitXmlDeclaration = input.readBoolean();
        String standaloneName = input.readString();
        this.standalone = standaloneName != null ? Standalone.valueOf(standaloneName) : Standalone.OMIT;
        this.doctypeSystem = input.readString();
        this.doctypePublic = input.readString();
        this.mediaType = input.readString();
        String nfName = input.readString();
        this.normalizationForm = nfName != null ? NormalizationForm.valueOf(nfName) : NormalizationForm.NONE;
        this.undeclarePrefixes = input.readBoolean();

        int cmSize = input.readInt();
        this.characterMaps = new HashMap<>();
        for (int i = 0; i < cmSize; i++) {
            String k = input.readString();
            String v = input.readString();
            this.characterMaps.put(k, v);
        }

        int cdataSize = input.readInt();
        this.cdataSectionElements = new HashSet<>();
        for (int i = 0; i < cdataSize; i++) {
            this.cdataSectionElements.add(input.readString());
        }

        this.includeContentType = input.readBoolean();
        this.escapeUriAttributes = input.readBoolean();
        this.htmlVersion = input.readString();
        this.byteOrderMark = input.readBoolean();
        this.indent = input.readBoolean();
        this.indentSpaces = input.readInt();

        int siSize = input.readInt();
        this.suppressIndentation = new HashSet<>();
        for (int i = 0; i < siSize; i++) {
            this.suppressIndentation.add(input.readString());
        }

        this.itemSeparator = input.readString();
        this.allowDuplicateNames = input.readBoolean();
        String jnomName = input.readString();
        this.jsonNodeOutputMethod = jnomName != null
            ? JsonNodeOutputMethod.valueOf(jnomName)
            : JsonNodeOutputMethod.UNSPECIFIED;

        int epSize = input.readInt();
        this.extensionParameters = new HashMap<>();
        for (int i = 0; i < epSize; i++) {
            String k = input.readString();
            String v = input.readString();
            this.extensionParameters.put(k, v);
        }

        int soSize = input.readInt();
        this.sparkOptions = new HashMap<>();
        for (int i = 0; i < soSize; i++) {
            String k = input.readString();
            String v = input.readString();
            this.sparkOptions.put(k, v);
        }
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


