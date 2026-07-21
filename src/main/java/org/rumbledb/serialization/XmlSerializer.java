package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.xml.NamespaceItem;

import java.io.Serial;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Map;

public class XmlSerializer implements Serializer, java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected final SerializationParameters params;

    public XmlSerializer(SerializationParameters params) {
        this.params = params != null ? params : SerializationParameters.defaults();
    }

    @Override
    public String serialize(Item item) {
        StringBuilder sb = new StringBuilder();
        if (shouldEmitXmlDeclaration(item)) {
            appendXmlDeclaration(sb);
        }
        serialize(item, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isArray()) {
            String separator = this.params.getItemSeparator() == null ? "" : this.params.getItemSeparator();
            boolean first = true;
            for (List<Item> member : item.getSequenceMembers()) {
                for (Item memberItem : member) {
                    if (!first) {
                        sb.append(separator);
                    }
                    serialize(memberItem, sb, indent, isTopLevel);
                    first = false;
                }
            }
            return;
        }
        if (item.isAtomic()) {
            sb.append(escapeText(item.getStringValue()));
            return;
        }
        if (item.isMap() || item.isObject()) {
            throw serializationError("Serialization method does not support arrays or maps.", "SENR0001");
        }
        if (item.isDocumentNode()) {
            serializeDocumentNode(item, sb, indent, isTopLevel);
            return;
        }
        if (item.isElementNode()) {
            serializeElementNode(item, sb, indent, isTopLevel);
            return;
        }
        if (item.isAttributeNode() || item.isNamespaceNode()) {
            if (isTopLevel) {
                throw serializationError("Top-level attribute and namespace nodes cannot be serialized.", "SENR0001");
            }
            appendAttributeOrNamespaceNode(item, sb);
            return;
        }
        if (item.isProcessingInstructionNode()) {
            sb.append("<?");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ");
                sb.append(content);
            }
            sb.append("?>");
            return;
        }
        if (item.isTextNode()) {
            appendTextNode(item, sb);
            return;
        }
        if (item.isCommentNode()) {
            sb.append("<!--");
            sb.append(item.getStringValue());
            sb.append("-->");
        }
    }

    protected void serializeDocumentNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        boolean doctypeEmitted = false;
        for (Item child : item.children()) {
            if (!doctypeEmitted && child.isElementNode()) {
                appendDocTypeIfNeeded(child, sb);
                doctypeEmitted = true;
            }
            serialize(child, sb, indent, false);
        }
    }

    protected void serializeElementNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (isTopLevel) {
            appendDocTypeIfNeeded(item, sb);
        }
        boolean indenting = shouldIndentElement(item);
        String childIndent = nextIndent(indent);
        sb.append("<");
        SerializerUtils.appendDmNodeNameLexical(sb, item);
        boolean namespaceAlreadyDeclared = false;
        for (Item namespace : item.declaredNamespaceNodes()) {
            if (matchesElementNamespace(item, namespace)) {
                namespaceAlreadyDeclared = true;
            }
            appendAttributeOrNamespaceNode(namespace, sb);
        }
        appendImplicitElementNamespace(item, sb, namespaceAlreadyDeclared);
        for (Item attribute : item.attributes()) {
            appendAttributeOrNamespaceNode(attribute, sb);
        }
        if (item.children().isEmpty()) {
            sb.append("/>");
            return;
        }
        sb.append(">");
        List<Item> children = item.children();
        boolean containsElementLikeChild = containsElementLikeChild(children);
        boolean preserveWhitespace = mustPreserveWhitespace(item);
        boolean hasSignificantTextChild = hasSignificantTextChild(children);
        for (Item child : children) {
            if (
                indenting
                    && containsElementLikeChild
                    && !preserveWhitespace
                    && !hasSignificantTextChild
                    && shouldIndentBeforeChild(child)
            ) {
                sb.append("\n").append(childIndent);
            }
            serialize(child, sb, childIndent, false);
        }
        if (indenting && containsElementLikeChild && !preserveWhitespace && !hasSignificantTextChild) {
            sb.append("\n").append(indent);
        }
        sb.append("</");
        SerializerUtils.appendDmNodeNameLexical(sb, item);
        sb.append(">");
    }

    protected void appendTextNode(Item item, StringBuilder sb) {
        Item parent = item.parent();
        if (parent != null && isCDataSectionElement(parent)) {
            appendCDataText(item.getStringValue(), sb);
            return;
        }
        sb.append(escapeText(item.getStringValue()));
    }

    protected void appendAttributeOrNamespaceNode(Item item, StringBuilder sb) {
        if (item.isNamespaceNode()) {
            NamespaceItem ns = (NamespaceItem) item;
            String prefix = ns.getPrefix();
            String uri = ns.getUri();
            if (uri == null) {
                uri = "";
            }
            if (!shouldSerializeNamespace(prefix, uri)) {
                return;
            }
            sb.append(" ");
            if (prefix == null || prefix.isEmpty()) {
                sb.append("xmlns=\"");
            } else {
                sb.append("xmlns:").append(prefix).append("=\"");
            }
            sb.append(escapeAttribute(uri));
            sb.append("\"");
            return;
        }
        sb.append(" ");
        SerializerUtils.appendDmNodeNameLexical(sb, item);
        sb.append("=\"");
        sb.append(escapeAttribute(prepareAttributeValue(item)));
        sb.append("\"");
    }

    protected boolean matchesElementNamespace(Item element, Item namespace) {
        if (
            element == null
                || element.nodeName() == null
                || namespace == null
                || !namespace.isNamespaceNode()
        ) {
            return false;
        }
        NamespaceItem namespaceItem = (NamespaceItem) namespace;
        String elementPrefix = element.nodeName().getPrefix();
        String namespacePrefix = namespaceItem.getPrefix();
        String elementNamespace = element.nodeName().getNamespace();
        String namespaceUri = namespaceItem.getUri();
        if (elementNamespace == null || namespaceUri == null) {
            return false;
        }
        if (elementPrefix == null || elementPrefix.isEmpty()) {
            return namespacePrefix.isEmpty() && elementNamespace.equals(namespaceUri);
        }
        return elementPrefix.equals(namespacePrefix) && elementNamespace.equals(namespaceUri);
    }

    protected void appendImplicitElementNamespace(Item element, StringBuilder sb, boolean namespaceAlreadyDeclared) {
        if (element == null || element.nodeName() == null || namespaceAlreadyDeclared) {
            return;
        }
        String namespace = element.nodeName().getNamespace();
        if (namespace == null || namespace.isEmpty()) {
            return;
        }
        String prefix = element.nodeName().getPrefix();
        if (isNamespaceBindingInScope(element.parent(), prefix, namespace)) {
            return;
        }
        if (prefix == null || prefix.isEmpty()) {
            sb.append(" xmlns=\"");
            sb.append(escapeAttribute(namespace));
            sb.append("\"");
            return;
        }
        sb.append(" xmlns:");
        sb.append(prefix);
        sb.append("=\"");
        sb.append(escapeAttribute(namespace));
        sb.append("\"");
    }

    protected boolean isNamespaceBindingInScope(Item context, String prefix, String namespace) {
        Item current = context;
        String normalizedPrefix = prefix == null ? "" : prefix;
        while (current != null && current.isElementNode()) {
            if (current.nodeName() != null) {
                String currentPrefix = current.nodeName().getPrefix() == null ? "" : current.nodeName().getPrefix();
                String currentNamespace = current.nodeName().getNamespace();
                if (
                    normalizedPrefix.equals(currentPrefix)
                        && namespace.equals(currentNamespace)
                ) {
                    return true;
                }
            }
            for (Item namespaceNode : current.declaredNamespaceNodes()) {
                NamespaceItem ns = (NamespaceItem) namespaceNode;
                if (normalizedPrefix.equals(ns.getPrefix())) {
                    return namespace.equals(ns.getUri());
                }
            }
            current = current.parent();
        }
        return false;
    }

    protected String prepareAttributeValue(Item attribute) {
        return attribute.getStringValue();
    }

    protected String escapeAttribute(String value) {
        return escapeXml(value, true);
    }

    protected String escapeText(String value) {
        return escapeXml(value, false);
    }

    protected boolean shouldSerializeNamespace(String prefix, String uri) {
        if (!uri.isEmpty()) {
            return true;
        }
        if (prefix == null || prefix.isEmpty()) {
            return true;
        }
        return this.params.getUndeclarePrefixes() && isXml11();
    }

    protected boolean shouldEmitXmlDeclaration(Item item) {
        return !this.params.getOmitXmlDeclaration() && (item.isDocumentNode() || item.isElementNode());
    }

    protected void appendXmlDeclaration(StringBuilder sb) {
        SerializerUtils.appendXmlDeclaration(sb, this.params);
    }

    protected void appendDocTypeIfNeeded(Item element, StringBuilder sb) {
        if (this.params.getDoctypeSystem() == null) {
            return;
        }
        sb.append("<!DOCTYPE ");
        SerializerUtils.appendDmNodeNameLexical(sb, element);
        if (this.params.getDoctypePublic() != null) {
            sb.append(" PUBLIC \"").append(this.params.getDoctypePublic()).append("\"");
            if (this.params.getDoctypeSystem() != null) {
                sb.append(" \"").append(this.params.getDoctypeSystem()).append("\"");
            }
        } else {
            sb.append(" SYSTEM \"").append(this.params.getDoctypeSystem()).append("\"");
        }
        sb.append(">");
    }

    protected String getEffectiveVersion(String defaultValue) {
        String effectiveVersion = SerializerUtils.getEffectiveXmlVersion(this.params);
        return effectiveVersion == null || effectiveVersion.isEmpty() ? defaultValue : effectiveVersion;
    }

    protected boolean isXml11() {
        return "1.1".equals(this.params.getVersion());
    }

    protected boolean isCDataSectionElement(Item element) {
        if (element == null || !element.isElementNode() || element.nodeName() == null) {
            return false;
        }
        return matchesExpandedQNameEntry(this.params.getCdataSectionElements(), element);
    }

    protected boolean shouldIndentElement(Item item) {
        return this.params.getIndent() && item.isElementNode();
    }

    protected boolean mustPreserveWhitespace(Item element) {
        return matchesExpandedQNameEntry(this.params.getSuppressIndentation(), element)
            || hasXmlSpacePreserve(element)
            || hasPreserveWhitespaceAncestor(element);
    }

    private boolean hasPreserveWhitespaceAncestor(Item element) {
        Item current = element == null ? null : element.parent();
        while (current != null && current.isElementNode()) {
            if (
                matchesExpandedQNameEntry(this.params.getSuppressIndentation(), current)
                    || hasXmlSpacePreserve(current)
            ) {
                return true;
            }
            current = current.parent();
        }
        return false;
    }

    private boolean hasXmlSpacePreserve(Item element) {
        for (Item attribute : element.attributes()) {
            if (
                attribute.nodeName() != null
                    && "space".equals(attribute.nodeName().getLocalName())
                    && Name.XML_NS.equals(attribute.nodeName().getNamespace())
                    && "preserve".equals(attribute.getStringValue())
            ) {
                return true;
            }
        }
        return false;
    }

    protected boolean matchesExpandedQNameEntry(java.util.Set<String> entries, Item element) {
        if (entries == null || entries.isEmpty() || element == null || element.nodeName() == null) {
            return false;
        }
        String namespace = element.nodeName().getNamespace();
        String localName = element.nodeName().getLocalName();
        boolean hasNoNamespace = namespace == null || namespace.isEmpty();
        String expandedName = hasNoNamespace
            ? localName
            : "Q{" + namespace + "}" + localName;
        if (entries.contains(expandedName)) {
            return true;
        }
        if (hasNoNamespace) {
            for (String entry : entries) {
                if (!entry.startsWith("Q{") && entry.equals(localName)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean containsElementLikeChild(List<Item> children) {
        for (Item child : children) {
            if (child.isElementNode() || child.isCommentNode() || child.isProcessingInstructionNode()) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasSignificantTextChild(List<Item> children) {
        for (Item child : children) {
            if (child.isTextNode() && child.getStringValue() != null && !child.getStringValue().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean shouldIndentBeforeChild(Item child) {
        return child.isElementNode() || child.isCommentNode() || child.isProcessingInstructionNode();
    }

    protected String nextIndent(String indent) {
        int indentWidth = this.params.getIndentSpaces() > 0 ? this.params.getIndentSpaces() : 2;
        return indent + " ".repeat(indentWidth);
    }

    protected void appendCDataText(String value, StringBuilder sb) {
        Map<String, String> characterMaps = this.params.getCharacterMaps();
        StringBuilder cdataSegment = new StringBuilder();
        for (int index = 0; index < value.length();) {
            int codePoint = value.codePointAt(index);
            String current = new String(Character.toChars(codePoint));
            String replacement = characterMaps == null ? null : characterMaps.get(current);
            if (replacement != null) {
                flushCDataSegment(cdataSegment, sb);
                sb.append(replacement);
            } else if (
                mustSerializeAsCharacterReference(codePoint, false) || !isEncodableInSelectedEncoding(codePoint)
            ) {
                flushCDataSegment(cdataSegment, sb);
                appendEscapedXmlCodePoint(sb, codePoint, false);
            } else if (!isRepresentableInSelectedXmlVersion(codePoint)) {
                throw new RumbleException(
                        "Character #" + codePoint + " is not representable in XML " + getEffectiveVersion("1.0") + ".",
                        ErrorCode.CodepointNotValidErrorCode,
                        ExceptionMetadata.EMPTY_METADATA
                );
            } else {
                cdataSegment.appendCodePoint(codePoint);
            }
            index += Character.charCount(codePoint);
        }
        flushCDataSegment(cdataSegment, sb);
    }

    protected RumbleException serializationError(String message, String errorCode) {
        return new RumbleException(
                message,
                new ErrorCode(new Name(Name.ERROR_NS, "err", errorCode)),
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    private String escapeXml(String value, boolean inAttribute) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        StringBuilder result = new StringBuilder(value.length());
        Map<String, String> characterMaps = this.params.getCharacterMaps();
        value.codePoints().forEach(codePoint -> {
            String current = new String(Character.toChars(codePoint));
            String replacement = characterMaps == null ? null : characterMaps.get(current);
            if (replacement != null) {
                result.append(replacement);
            } else {
                appendEscapedXmlCodePoint(result, codePoint, inAttribute);
            }
        });
        return result.toString();
    }

    private void appendEscapedXmlCodePoint(StringBuilder result, int codePoint, boolean inAttribute) {
        switch (codePoint) {
            case '&':
                result.append("&amp;");
                return;
            case '<':
                result.append("&lt;");
                return;
            case '>':
                result.append("&gt;");
                return;
            case '"':
                if (inAttribute) {
                    result.append("&quot;");
                    return;
                }
                break;
            default:
                break;
        }

        if (mustSerializeAsCharacterReference(codePoint, inAttribute)) {
            appendDecimalCharacterReference(result, codePoint);
            return;
        }

        if (!isRepresentableInSelectedXmlVersion(codePoint)) {
            throw new RumbleException(
                    "Character #" + codePoint + " is not representable in XML " + getEffectiveVersion("1.0") + ".",
                    ErrorCode.CodepointNotValidErrorCode,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }

        if (!isEncodableInSelectedEncoding(codePoint)) {
            appendDecimalCharacterReference(result, codePoint);
            return;
        }

        result.appendCodePoint(codePoint);
    }

    private boolean mustSerializeAsCharacterReference(int codePoint, boolean inAttribute) {
        if (codePoint == 0x85 || codePoint == 0x2028) {
            return true;
        }
        if (inAttribute && (codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD)) {
            return true;
        }
        if (!inAttribute && codePoint == 0xD) {
            return true;
        }
        if (codePoint < 0x20) {
            return codePoint != 0x9 && codePoint != 0xA && codePoint != 0xD;
        }
        return codePoint >= 0x7F && codePoint <= 0x9F;
    }

    private boolean isRepresentableInSelectedXmlVersion(int codePoint) {
        if (codePoint == 0) {
            return false;
        }
        if (isXml11()) {
            return codePoint <= 0xD7FF
                || (codePoint >= 0xE000 && codePoint <= 0xFFFD)
                || (codePoint >= 0x10000 && codePoint <= 0x10FFFF);
        }
        if (codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD) {
            return true;
        }
        return (codePoint >= 0x20 && codePoint <= 0xD7FF)
            || (codePoint >= 0xE000 && codePoint <= 0xFFFD)
            || (codePoint >= 0x10000 && codePoint <= 0x10FFFF);
    }

    private boolean isEncodableInSelectedEncoding(int codePoint) {
        String encoding = this.params.getEncoding() == null ? "UTF-8" : this.params.getEncoding();
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        return encoder.canEncode(new String(Character.toChars(codePoint)));
    }

    private void appendDecimalCharacterReference(StringBuilder result, int codePoint) {
        result.append("&#");
        result.append(codePoint);
        result.append(";");
    }

    private void flushCDataSegment(StringBuilder cdataSegment, StringBuilder sb) {
        if (cdataSegment.isEmpty()) {
            return;
        }
        String value = cdataSegment.toString();
        int start = 0;
        while (start <= value.length()) {
            int split = value.indexOf("]]>", start);
            String segment = split < 0
                ? value.substring(start)
                : value.substring(start, split + 2);
            sb.append("<![CDATA[").append(segment).append("]]>");
            if (split < 0) {
                break;
            }
            start = split + 2;
        }
        cdataSegment.setLength(0);
    }
}
