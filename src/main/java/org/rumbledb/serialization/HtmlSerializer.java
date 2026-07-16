package org.rumbledb.serialization;

import org.rumbledb.api.Item;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class HtmlSerializer extends XmlSerializer {

    private static final long serialVersionUID = 1L;
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
