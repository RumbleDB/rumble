package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.xml.NamespaceItem;

public class XmlSerializer implements Serializer, java.io.Serializable {

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
        if (item.isAtomic()) {
            sb.append(item.getStringValue());
            return;
        }
        if (item.isArray() || item.isMap() || item.isObject()) {
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
            sb.append(escapeText(item.getStringValue()));
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
        sb.append("<");
        SerializerUtils.appendDmNodeNameLexical(sb, item);
        for (Item namespace : item.declaredNamespaceNodes()) {
            appendAttributeOrNamespaceNode(namespace, sb);
        }
        for (Item attribute : item.attributes()) {
            appendAttributeOrNamespaceNode(attribute, sb);
        }
        if (item.children().isEmpty()) {
            sb.append("/>");
            return;
        }
        sb.append(">");
        for (Item child : item.children()) {
            serialize(child, sb, indent, false);
        }
        sb.append("</");
        SerializerUtils.appendDmNodeNameLexical(sb, item);
        sb.append(">");
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

    protected String prepareAttributeValue(Item attribute) {
        return attribute.getStringValue();
    }

    protected String escapeAttribute(String value) {
        return isXml11() ? StringEscapeUtils.escapeXml11(value) : StringEscapeUtils.escapeXml10(value);
    }

    protected String escapeText(String value) {
        return isXml11() ? StringEscapeUtils.escapeXml11(value) : StringEscapeUtils.escapeXml10(value);
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
        sb.append("<?xml version=\"");
        sb.append(getEffectiveVersion("1.0"));
        sb.append("\" encoding=\"");
        sb.append(this.params.getEncoding() == null ? "UTF-8" : this.params.getEncoding());
        sb.append("\"");
        if (this.params.getStandalone() == SerializationParameters.Standalone.YES) {
            sb.append(" standalone=\"yes\"");
        } else if (this.params.getStandalone() == SerializationParameters.Standalone.NO) {
            sb.append(" standalone=\"no\"");
        }
        sb.append("?>");
    }

    protected void appendDocTypeIfNeeded(Item element, StringBuilder sb) {
        if (this.params.getDoctypeSystem() == null && this.params.getDoctypePublic() == null) {
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
        return this.params.getVersion() == null || this.params.getVersion().isEmpty()
            ? defaultValue
            : this.params.getVersion();
    }

    protected boolean isXml11() {
        return "1.1".equals(this.params.getVersion());
    }

    protected RumbleException serializationError(String message, String errorCode) {
        return new RumbleException(
                message,
                new ErrorCode(org.rumbledb.context.Name.createVariableInNoNamespace(errorCode)),
                ExceptionMetadata.EMPTY_METADATA
        );
    }
}
