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

package org.rumbledb.context.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default serialization parameters stored in the XQuery static context.
 *
 * <p>
 * Specification references:
 * </p>
 * <ul>
 * <li>XQuery 3.1 Static Context Components — default serialization parameters
 * (link: https://www.w3.org/TR/xquery-31/#id-xq-static-context-components)</li>
 * <li>XSLT and XQuery Serialization 3.1 — Serialization Parameters
 * (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)</li>
 * </ul>
 *
 * <p>
 * Override order (to be enforced by higher layers later): default values (static context) → command line
 * (implementation override) → prolog → serialize function (dynamic context).
 * </p>
 */
public class SerializationParameters implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    /**
     * Serialization method.
     *
     * "method" — Serialization 3.1 Parameters (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    private String method; // xml | html | text | json | adaptive | untyped

    /**
     * Character encoding.
     *
     * "encoding" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_ENCODING),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_ENCODING),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_ENCODING),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_ENCODING),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_ENCODING)
     */
    private String encoding;

    /**
     * Whether to omit the XML declaration.
     *
     * "omit-xml-declaration" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_OMIT-XML-DECLARATION),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_OMIT-XML-DECLARATION),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_OMIT-XML-DECLARATION),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_OMIT-XML-DECLARATION)
     */
    private Boolean omitXmlDeclaration;

    /**
     * XML standalone declaration.
     *
     * "standalone" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_STANDALONE),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_STANDALONE),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_STANDALONE),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_STANDALONE)
     */
    private String standalone; // yes | no | omit

    /**
     * DocType system identifier.
     *
     * "doctype-system" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_DOCTYPE-SYSTEM),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_DOCTYPE-SYSTEM),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_DOCTYPE-SYSTEM),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_DOCTYPE-SYSTEM)
     */
    private String doctypeSystem;

    /**
     * DocType public identifier.
     *
     * "doctype-public" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_DOCTYPE-PUBLIC),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_DOCTYPE-PUBLIC),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_DOCTYPE-PUBLIC),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_DOCTYPE-PUBLIC)
     */
    private String doctypePublic;

    /**
     * Media type.
     *
     * "media-type" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_MEDIA-TYPE),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_MEDIA-TYPE),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_MEDIA-TYPE),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_MEDIA-TYPE)
     */
    private String mediaType;

    /**
     * Normalize characters using a Unicode normalization form.
     *
     * "normalization-form" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_NORMALIZATION-FORM),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_NORMALIZATION-FORM),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_NORMALIZATION-FORM),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_NORMALIZATION-FORM)
     */
    private String normalizationForm; // NFC | NFD | NFKC | NFKD | fully-normalized | none

    /**
     * Whether to declare namespace undeclarations.
     *
     * "undeclare-prefixes" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_UNDECLARE-PREFIXES),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_UNDECLARE-PREFIXES),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_UNDECLARE-PREFIXES),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_UNDECLARE-PREFIXES)
     */
    private Boolean undeclarePrefixes;

    /**
     * Character maps, mapping strings to strings.
     *
     * "use-character-maps" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_USE-CHARACTER-MAPS),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_USE-CHARACTER-MAPS),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_USE-CHARACTER-MAPS),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_USE-CHARACTER-MAPS)
     */
    private Map<String, String> characterMaps;

    /**
     * Element QNames to output using CDATA sections.
     *
     * "cdata-section-elements" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_CDATA-SECTION-ELEMENTS),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_CDATA-SECTION-ELEMENTS),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_CDATA-SECTION-ELEMENTS),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_CDATA-SECTION-ELEMENTS)
     */
    private Set<String> cdataSectionElements;

    /**
     * Include meta http-equiv content-type.
     *
     * "include-content-type" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_INCLUDE-CONTENT-TYPE),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_INCLUDE-CONTENT-TYPE),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_INCLUDE-CONTENT-TYPE),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_INCLUDE-CONTENT-TYPE)
     */
    private Boolean includeContentType;

    /**
     * Escape URI attributes.
     *
     * "escape-uri-attributes" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_ESCAPE-URI-ATTRIBUTES),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_ESCAPE-URI-ATTRIBUTES),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_ESCAPE-URI-ATTRIBUTES),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_ESCAPE-URI-ATTRIBUTES)
     */
    private Boolean escapeUriAttributes;

    /**
     * HTML version (implementation-defined default).
     *
     * "html-version" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_HTML-VERSION),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_HTML-VERSION),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_HTML-VERSION),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_HTML-VERSION)
     */
    private String htmlVersion;

    /**
     * Insert byte-order mark.
     *
     * "byte-order-mark" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_BYTE-ORDER-MARK),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_BYTE-ORDER-MARK),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_BYTE-ORDER-MARK),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_BYTE-ORDER-MARK),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_BYTE-ORDER-MARK)
     */
    private Boolean byteOrderMark;

    /**
     * Indentation control.
     *
     * "indent" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_INDENT),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_INDENT),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_INDENT),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_INDENT),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_INDENT)
     */
    private Boolean indent;

    /**
     * Number of spaces for indentation (implementation-defined default).
     *
     * "indent-spaces" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_INDENT),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_INDENT),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_INDENT),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_INDENT),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_INDENT)
     */
    private Integer indentSpaces;

    /**
     * Elements whose content should not be indented.
     *
     * "suppress-indentation" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_SUPPRESS-INDENTATION),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_SUPPRESS-INDENTATION),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_SUPPRESS-INDENTATION),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_SUPPRESS-INDENTATION)
     */
    private Set<String> suppressIndentation;

    /**
     * Separator between items of the top-level sequence.
     *
     * "item-separator" — XML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_ITEM-SEPARATOR),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_ITEM-SEPARATOR),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_ITEM-SEPARATOR),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_ITEM-SEPARATOR)
     */
    private String itemSeparator;

    /**
     * JSON: allow duplicate map keys.
     *
     * "allow-duplicate-names" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_ALLOW-DUPLICATE-NAMES),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_ALLOW-DUPLICATE-NAMES),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_ALLOW-DUPLICATE-NAMES),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_ALLOW-DUPLICATE-NAMES),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_ALLOW-DUPLICATE-NAMES)
     */
    private Boolean allowDuplicateNames;

    /**
     * JSON node output method.
     *
     * "json-node-output-method" — XML (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#XML_JSON-NODE-OUTPUT-METHOD),
     * HTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#HTML_JSON-NODE-OUTPUT-METHOD),
     * XHTML (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#XHTML_JSON-NODE-OUTPUT-METHOD),
     * Text (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#TEXT_JSON-NODE-OUTPUT-METHOD),
     * JSON (link: https://www.w3.org/TR/xslt-xquery-serialization-31/#JSON_JSON-NODE-OUTPUT-METHOD)
     */
    private String jsonNodeOutputMethod; // json | xml | html | text

    /**
     * Extension/unknown parameters preserved for forward compatibility.
     */
    private Map<String, String> extensionParameters;

    public SerializationParameters() {
        // empty for Kryo/Java serialization
    }

    public static SerializationParameters defaults() {
        SerializationParameters p = new SerializationParameters();
        // Spec-aligned conservative defaults; implementation-defined noted explicitly
        p.method = null; // let the processor choose (adaptive in many impls)
        p.encoding = "UTF-8";
        p.omitXmlDeclaration = Boolean.FALSE;
        p.standalone = "omit";
        p.doctypeSystem = null;
        p.doctypePublic = null;
        p.mediaType = null;
        p.normalizationForm = "none";
        p.undeclarePrefixes = Boolean.FALSE;
        p.characterMaps = new HashMap<>();
        p.cdataSectionElements = new HashSet<>();
        p.includeContentType = Boolean.TRUE;
        p.escapeUriAttributes = Boolean.TRUE;
        p.htmlVersion = null; // implementation-defined
        p.byteOrderMark = Boolean.FALSE;
        p.indent = Boolean.FALSE;
        p.indentSpaces = null; // implementation-defined
        p.suppressIndentation = new HashSet<>();
        p.itemSeparator = null; // implementation-defined
        p.allowDuplicateNames = Boolean.FALSE;
        p.jsonNodeOutputMethod = null; // implementation-defined behavior if null
        p.extensionParameters = new HashMap<>();
        return p;
    }

    public SerializationParameters copy() {
        SerializationParameters p = new SerializationParameters();
        p.method = this.method;
        p.encoding = this.encoding;
        p.omitXmlDeclaration = this.omitXmlDeclaration;
        p.standalone = this.standalone;
        p.doctypeSystem = this.doctypeSystem;
        p.doctypePublic = this.doctypePublic;
        p.mediaType = this.mediaType;
        p.normalizationForm = this.normalizationForm;
        p.undeclarePrefixes = this.undeclarePrefixes;
        p.characterMaps = new HashMap<>(this.characterMaps != null ? this.characterMaps : Collections.emptyMap());
        p.cdataSectionElements = new HashSet<>(
                this.cdataSectionElements != null ? this.cdataSectionElements : Collections.emptySet()
        );
        p.includeContentType = this.includeContentType;
        p.escapeUriAttributes = this.escapeUriAttributes;
        p.htmlVersion = this.htmlVersion;
        p.byteOrderMark = this.byteOrderMark;
        p.indent = this.indent;
        p.indentSpaces = this.indentSpaces;
        p.suppressIndentation = new HashSet<>(
                this.suppressIndentation != null ? this.suppressIndentation : Collections.emptySet()
        );
        p.itemSeparator = this.itemSeparator;
        p.allowDuplicateNames = this.allowDuplicateNames;
        p.jsonNodeOutputMethod = this.jsonNodeOutputMethod;
        p.extensionParameters = new HashMap<>(
                this.extensionParameters != null ? this.extensionParameters : Collections.emptyMap()
        );
        return p;
    }

    // Getters and setters
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Boolean getOmitXmlDeclaration() {
        return omitXmlDeclaration;
    }

    public void setOmitXmlDeclaration(Boolean omitXmlDeclaration) {
        this.omitXmlDeclaration = omitXmlDeclaration;
    }

    public String getStandalone() {
        return standalone;
    }

    public void setStandalone(String standalone) {
        this.standalone = standalone;
    }

    public String getDoctypeSystem() {
        return doctypeSystem;
    }

    public void setDoctypeSystem(String doctypeSystem) {
        this.doctypeSystem = doctypeSystem;
    }

    public String getDoctypePublic() {
        return doctypePublic;
    }

    public void setDoctypePublic(String doctypePublic) {
        this.doctypePublic = doctypePublic;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getNormalizationForm() {
        return normalizationForm;
    }

    public void setNormalizationForm(String normalizationForm) {
        this.normalizationForm = normalizationForm;
    }

    public Boolean getUndeclarePrefixes() {
        return undeclarePrefixes;
    }

    public void setUndeclarePrefixes(Boolean undeclarePrefixes) {
        this.undeclarePrefixes = undeclarePrefixes;
    }

    public Map<String, String> getCharacterMaps() {
        return characterMaps;
    }

    public void setCharacterMaps(Map<String, String> characterMaps) {
        this.characterMaps = characterMaps;
    }

    public Set<String> getCdataSectionElements() {
        return cdataSectionElements;
    }

    public void setCdataSectionElements(Set<String> cdataSectionElements) {
        this.cdataSectionElements = cdataSectionElements;
    }

    public Boolean getIncludeContentType() {
        return includeContentType;
    }

    public void setIncludeContentType(Boolean includeContentType) {
        this.includeContentType = includeContentType;
    }

    public Boolean getEscapeUriAttributes() {
        return escapeUriAttributes;
    }

    public void setEscapeUriAttributes(Boolean escapeUriAttributes) {
        this.escapeUriAttributes = escapeUriAttributes;
    }

    public String getHtmlVersion() {
        return htmlVersion;
    }

    public void setHtmlVersion(String htmlVersion) {
        this.htmlVersion = htmlVersion;
    }

    public Boolean getByteOrderMark() {
        return byteOrderMark;
    }

    public void setByteOrderMark(Boolean byteOrderMark) {
        this.byteOrderMark = byteOrderMark;
    }

    public Boolean getIndent() {
        return indent;
    }

    public void setIndent(Boolean indent) {
        this.indent = indent;
    }

    public Integer getIndentSpaces() {
        return indentSpaces;
    }

    public void setIndentSpaces(Integer indentSpaces) {
        this.indentSpaces = indentSpaces;
    }

    public Set<String> getSuppressIndentation() {
        return suppressIndentation;
    }

    public void setSuppressIndentation(Set<String> suppressIndentation) {
        this.suppressIndentation = suppressIndentation;
    }

    public String getItemSeparator() {
        return itemSeparator;
    }

    public void setItemSeparator(String itemSeparator) {
        this.itemSeparator = itemSeparator;
    }

    public Boolean getAllowDuplicateNames() {
        return allowDuplicateNames;
    }

    public void setAllowDuplicateNames(Boolean allowDuplicateNames) {
        this.allowDuplicateNames = allowDuplicateNames;
    }

    public String getJsonNodeOutputMethod() {
        return jsonNodeOutputMethod;
    }

    public void setJsonNodeOutputMethod(String jsonNodeOutputMethod) {
        this.jsonNodeOutputMethod = jsonNodeOutputMethod;
    }

    public Map<String, String> getExtensionParameters() {
        return extensionParameters;
    }

    public void setExtensionParameters(Map<String, String> extensionParameters) {
        this.extensionParameters = extensionParameters;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.method);
        output.writeString(this.encoding);
        output.writeBoolean(this.omitXmlDeclaration != null && this.omitXmlDeclaration);
        output.writeString(this.standalone);
        output.writeString(this.doctypeSystem);
        output.writeString(this.doctypePublic);
        output.writeString(this.mediaType);
        output.writeString(this.normalizationForm);
        output.writeBoolean(this.undeclarePrefixes != null && this.undeclarePrefixes);

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

        output.writeBoolean(this.includeContentType != null && this.includeContentType);
        output.writeBoolean(this.escapeUriAttributes != null && this.escapeUriAttributes);
        output.writeString(this.htmlVersion);
        output.writeBoolean(this.byteOrderMark != null && this.byteOrderMark);
        output.writeBoolean(this.indent != null && this.indent);
        output.writeInt(this.indentSpaces != null ? this.indentSpaces : -1);

        // suppressIndentation
        int siSize = this.suppressIndentation != null ? this.suppressIndentation.size() : 0;
        output.writeInt(siSize);
        if (siSize > 0) {
            for (String qn : this.suppressIndentation) {
                output.writeString(qn);
            }
        }

        output.writeString(this.itemSeparator);
        output.writeBoolean(this.allowDuplicateNames != null && this.allowDuplicateNames);
        output.writeString(this.jsonNodeOutputMethod);

        // extensionParameters
        int epSize = this.extensionParameters != null ? this.extensionParameters.size() : 0;
        output.writeInt(epSize);
        if (epSize > 0) {
            for (Map.Entry<String, String> e : this.extensionParameters.entrySet()) {
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
        this.standalone = input.readString();
        this.doctypeSystem = input.readString();
        this.doctypePublic = input.readString();
        this.mediaType = input.readString();
        this.normalizationForm = input.readString();
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
        int indentSpacesRead = input.readInt();
        this.indentSpaces = indentSpacesRead >= 0 ? indentSpacesRead : null;

        int siSize = input.readInt();
        this.suppressIndentation = new HashSet<>();
        for (int i = 0; i < siSize; i++) {
            this.suppressIndentation.add(input.readString());
        }

        this.itemSeparator = input.readString();
        this.allowDuplicateNames = input.readBoolean();
        this.jsonNodeOutputMethod = input.readString();

        int epSize = input.readInt();
        this.extensionParameters = new HashMap<>();
        for (int i = 0; i < epSize; i++) {
            String k = input.readString();
            String v = input.readString();
            this.extensionParameters.put(k, v);
        }
    }
}


