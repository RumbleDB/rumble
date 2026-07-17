package org.rumbledb.serialization;

import org.rumbledb.api.Item;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class HtmlSerializer extends XmlSerializer {

    private static final long serialVersionUID = 1L;
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    private static final String MATHML_NS = "http://www.w3.org/1998/Math/MathML";
    private static final Set<String> URI_ATTRIBUTES = Set.of(
        "action",
        "archive",
        "background",
        "cite",
        "classid",
        "codebase",
        "data",
        "formaction",
        "href",
        "icon",
        "longdesc",
        "manifest",
        "poster",
        "profile",
        "src",
        "usemap"
    );

    public HtmlSerializer(SerializationParameters params) {
        super(params);
    }

    @Override
    protected boolean shouldEmitXmlDeclaration(Item item) {
        return false;
    }

    @Override
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

    @Override
    protected String prepareAttributeValue(Item attribute) {
        String value = attribute.getStringValue();
        if (!this.params.getEscapeUriAttributes()) {
            return value;
        }
        String localName = attribute.nodeName() == null ? null : attribute.nodeName().getLocalName();
        if (localName == null || !URI_ATTRIBUTES.contains(localName.toLowerCase())) {
            return value;
        }
        return escapeHtmlUriAttribute(value);
    }

    @Override
    protected String escapeText(String value) {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    @Override
    protected boolean matchesExpandedQNameEntry(java.util.Set<String> entries, Item element) {
        if (super.matchesExpandedQNameEntry(entries, element)) {
            return true;
        }
        if (entries == null || entries.isEmpty() || element == null || element.nodeName() == null) {
            return false;
        }
        String namespace = element.nodeName().getNamespace();
        String localName = element.nodeName().getLocalName();
        if (!isHtmlFamilyNamespace(namespace)) {
            return false;
        }
        for (String entry : entries) {
            if (entry.startsWith("Q{")) {
                int closingBrace = entry.indexOf('}');
                if (closingBrace < 0) {
                    continue;
                }
                String entryNamespace = entry.substring(2, closingBrace);
                String entryLocalName = entry.substring(closingBrace + 1);
                if (
                    ((namespace == null && entryNamespace.isEmpty())
                        || (namespace != null && namespace.equals(entryNamespace)))
                        && localName.equalsIgnoreCase(entryLocalName)
                ) {
                    return true;
                }
            } else if ((namespace == null || XHTML_NS.equals(namespace)) && localName.equalsIgnoreCase(entry)) {
                return true;
            }
        }
        return false;
    }

    private boolean isHtmlFamilyNamespace(String namespace) {
        return namespace == null
            || XHTML_NS.equals(namespace)
            || SVG_NS.equals(namespace)
            || MATHML_NS.equals(namespace);
    }

    private String escapeHtmlUriAttribute(String value) {
        StringBuilder result = new StringBuilder(value.length());
        value.codePoints().forEach(codePoint -> appendEscapedUriCodePoint(result, codePoint));
        return result.toString();
    }

    private void appendEscapedUriCodePoint(StringBuilder result, int codePoint) {
        if (codePoint >= 0x20 && codePoint <= 0x7E) {
            result.appendCodePoint(codePoint);
            return;
        }
        byte[] utf8Bytes = new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8);
        for (byte currentByte : utf8Bytes) {
            int unsigned = currentByte & 0xFF;
            result.append('%');
            result.append(Character.toUpperCase(Character.forDigit((unsigned >>> 4) & 0xF, 16)));
            result.append(Character.toUpperCase(Character.forDigit(unsigned & 0xF, 16)));
        }
    }
}
