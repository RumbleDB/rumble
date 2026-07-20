package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    private static final Set<String> HTML4_EMPTY_ELEMENTS = Set.of(
        "area",
        "base",
        "br",
        "col",
        "embed",
        "frame",
        "hr",
        "img",
        "input",
        "isindex",
        "link",
        "meta",
        "param"
    );
    private static final Set<String> HTML5_VOID_ELEMENTS = Set.of(
        "area",
        "base",
        "br",
        "col",
        "embed",
        "hr",
        "img",
        "input",
        "keygen",
        "link",
        "meta",
        "param",
        "source",
        "track",
        "wbr"
    );
    private static final Set<String> HTML_BOOLEAN_ATTRIBUTES = Set.of(
        "allowfullscreen",
        "async",
        "autofocus",
        "autoplay",
        "checked",
        "controls",
        "default",
        "defer",
        "disabled",
        "formnovalidate",
        "hidden",
        "ismap",
        "itemscope",
        "loop",
        "multiple",
        "muted",
        "nomodule",
        "novalidate",
        "open",
        "playsinline",
        "readonly",
        "required",
        "reversed",
        "selected"
    );
    private static final Set<String> RAW_TEXT_ELEMENTS = Set.of("script", "style");

    public HtmlSerializer(SerializationParameters params) {
        super(params);
    }

    @Override
    protected boolean shouldEmitXmlDeclaration(Item item) {
        return false;
    }

    @Override
    protected void serializeDocumentNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        Item firstElementChild = findFirstElementChild(item.children());
        if (firstElementChild != null) {
            appendDocTypeIfNeeded(firstElementChild, sb);
        }
        for (Item child : item.children()) {
            serialize(child, sb, indent, false);
        }
    }

    @Override
    protected void appendDocTypeIfNeeded(Item element, StringBuilder sb) {
        if (shouldEmitDefaultHtmlDoctype(element)) {
            sb.append("<!DOCTYPE html>");
            return;
        }
        if (this.params.getDoctypeSystem() == null && this.params.getDoctypePublic() == null) {
            return;
        }
        sb.append("<!DOCTYPE ");
        sb.append(getSerializedElementName(element));
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
    protected void serializeElementNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (item.nodeName() != null && !isHtmlElement(item)) {
            super.serializeElementNode(item, sb, indent, isTopLevel);
            return;
        }
        boolean injectContentTypeMeta = shouldInjectContentTypeMeta(item);
        if (isTopLevel) {
            appendDocTypeIfNeeded(item, sb);
        }
        sb.append("<");
        sb.append(getSerializedElementName(item));
        appendElementNamespaces(item, sb);
        for (Item attribute : item.attributes()) {
            appendAttributeOrNamespaceNode(attribute, sb);
        }
        if (item.children().isEmpty() && injectContentTypeMeta) {
            sb.append(">");
            appendInjectedMetaElement(sb);
            sb.append("</");
            sb.append(getSerializedElementName(item));
            sb.append(">");
            return;
        }
        if (item.children().isEmpty() && isHtmlEmptyElement(item)) {
            sb.append(">");
            return;
        }
        if (item.children().isEmpty()) {
            sb.append("></");
            sb.append(getSerializedElementName(item));
            sb.append(">");
            return;
        }
        sb.append(">");
        if (injectContentTypeMeta) {
            appendInjectedMetaElement(sb);
        }
        List<Item> children = item.children();
        boolean indenting = shouldIndentElement(item);
        boolean containsElementLikeChild = containsElementLikeChild(children);
        boolean preserveWhitespace = mustPreserveWhitespace(item);
        boolean hasSignificantTextChild = hasSignificantTextChild(children);
        String childIndent = nextIndent(indent);
        for (Item child : children) {
            if (shouldSkipExistingContentTypeMeta(item, child)) {
                continue;
            }
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
        sb.append(getSerializedElementName(item));
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
    protected void appendTextNode(Item item, StringBuilder sb) {
        Item parent = item.parent();
        if (parent != null && isRawTextElement(parent)) {
            sb.append(item.getStringValue());
            return;
        }
        super.appendTextNode(item, sb);
    }

    @Override
    protected void appendAttributeOrNamespaceNode(Item item, StringBuilder sb) {
        if (item.isNamespaceNode()) {
            super.appendAttributeOrNamespaceNode(item, sb);
            return;
        }
        if (shouldMinimizeBooleanAttribute(item)) {
            sb.append(" ");
            sb.append(item.nodeName().getLocalName());
            return;
        }
        super.appendAttributeOrNamespaceNode(item, sb);
    }

    @Override
    protected String escapeAttribute(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '&') {
                if (i + 1 < value.length() && value.charAt(i + 1) == '{') {
                    result.append('&');
                } else {
                    result.append("&amp;");
                }
            } else if (current == '<') {
                result.append("&lt;");
            } else if (current == '>') {
                result.append("&gt;");
            } else if (current == '"') {
                result.append("&quot;");
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (item.isProcessingInstructionNode()) {
            sb.append("<?");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ");
                sb.append(content);
            }
            sb.append(">");
            return;
        }
        super.serialize(item, sb, indent, isTopLevel);
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
        String prefix = element.nodeName().getPrefix();
        if (!isHtmlFamilyNamespace(namespace)) {
            if (prefix != null && !prefix.isEmpty()) {
                for (String entry : entries) {
                    if (entry.equals(prefix + ":" + localName)) {
                        return true;
                    }
                }
            }
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
            } else if (
                (namespace == null || XHTML_NS.equals(namespace)) && localName.equalsIgnoreCase(entry)
            ) {
                return true;
            } else if (
                prefix != null
                    && !prefix.isEmpty()
                    && entry.equalsIgnoreCase(prefix + ":" + localName)
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isCDataSectionElement(Item element) {
        if (element == null || !element.isElementNode() || element.nodeName() == null) {
            return false;
        }
        if (isHtmlElement(element)) {
            if (isHtml5Version()) {
                return false;
            }
            String prefix = element.nodeName().getPrefix();
            if (prefix == null || prefix.isEmpty()) {
                return false;
            }
            return matchesExactExpandedQNameEntry(this.params.getCdataSectionElements(), element);
        }
        return matchesExpandedQNameEntry(this.params.getCdataSectionElements(), element);
    }

    private boolean isHtmlFamilyNamespace(String namespace) {
        return namespace == null
            || XHTML_NS.equals(namespace)
            || SVG_NS.equals(namespace)
            || MATHML_NS.equals(namespace);
    }

    private boolean matchesExactExpandedQNameEntry(Set<String> entries, Item element) {
        if (entries == null || entries.isEmpty() || element == null || element.nodeName() == null) {
            return false;
        }
        String namespace = element.nodeName().getNamespace();
        String localName = element.nodeName().getLocalName();
        String prefix = element.nodeName().getPrefix();
        String expandedName = namespace == null
            ? localName
            : "Q{" + namespace + "}" + localName;
        if (entries.contains(expandedName)) {
            return true;
        }
        if (namespace == null && entries.contains(localName)) {
            return true;
        }
        return prefix != null && !prefix.isEmpty() && entries.contains(prefix + ":" + localName);
    }

    private void appendElementNamespaces(Item item, StringBuilder sb) {
        boolean emittedDefaultNamespaceForNormalizedElement = false;
        if (shouldApplyPrefixNormalization(item)) {
            sb.append(" xmlns=\"");
            sb.append(escapeAttribute(item.nodeName().getNamespace()));
            sb.append("\"");
            emittedDefaultNamespaceForNormalizedElement = true;
        }
        boolean namespaceAlreadyDeclared = false;
        for (Item namespace : item.declaredNamespaceNodes()) {
            if (shouldSkipNamespaceDeclaration(item, namespace, emittedDefaultNamespaceForNormalizedElement)) {
                continue;
            }
            if (matchesElementNamespace(item, namespace)) {
                namespaceAlreadyDeclared = true;
            }
            appendAttributeOrNamespaceNode(namespace, sb);
        }
        appendImplicitElementNamespace(item, sb, namespaceAlreadyDeclared, emittedDefaultNamespaceForNormalizedElement);
    }

    @Override
    protected boolean matchesElementNamespace(Item element, Item namespace) {
        return super.matchesElementNamespace(element, namespace);
    }

    private void appendImplicitElementNamespace(
            Item element,
            StringBuilder sb,
            boolean namespaceAlreadyDeclared,
            boolean emittedDefaultNamespaceForNormalizedElement
    ) {
        if (element == null || element.nodeName() == null || namespaceAlreadyDeclared) {
            return;
        }
        String namespace = element.nodeName().getNamespace();
        if (namespace == null || namespace.isEmpty()) {
            return;
        }
        String prefix = element.nodeName().getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            if (!emittedDefaultNamespaceForNormalizedElement) {
                sb.append(" xmlns=\"");
                sb.append(escapeAttribute(namespace));
                sb.append("\"");
            }
            return;
        }
        sb.append(" xmlns:");
        sb.append(prefix);
        sb.append("=\"");
        sb.append(escapeAttribute(namespace));
        sb.append("\"");
    }

    private boolean shouldSkipNamespaceDeclaration(
            Item element,
            Item namespace,
            boolean emittedDefaultNamespaceForNormalizedElement
    ) {
        if (!namespace.isNamespaceNode() || element.nodeName() == null) {
            return false;
        }
        String prefix = namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName();
        String elementNamespace = element.nodeName().getNamespace();
        String uri = namespace.getStringValue();
        if (uri == null) {
            return false;
        }
        if (XHTML_NS.equals(elementNamespace) && XHTML_NS.equals(uri)) {
            if (
                isHtml5Version()
                    && shouldApplyPrefixNormalization(element)
                    && prefix.equals(element.nodeName().getPrefix())
            ) {
                return true;
            }
            if (
                isHtml5Version()
                    && shouldApplyPrefixNormalization(element)
                    && prefix.isEmpty()
                    && emittedDefaultNamespaceForNormalizedElement
            ) {
                return true;
            }
        }
        if (shouldApplyPrefixNormalization(element) && element.nodeName().getNamespace().equals(uri)) {
            if (prefix.equals(element.nodeName().getPrefix())) {
                return true;
            }
            if (prefix.isEmpty() && emittedDefaultNamespaceForNormalizedElement) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldEmitDefaultHtmlDoctype(Item element) {
        if (
            element == null
                || !hasLocalName(element, "html")
                || this.params.getDoctypeSystem() != null
                || this.params.getDoctypePublic() != null
        ) {
            return false;
        }
        return isHtml5Version() && isHtmlElement(element);
    }

    private boolean shouldInjectContentTypeMeta(Item item) {
        return this.params.getIncludeContentType() && isHtmlElement(item) && hasLocalName(item, "head");
    }

    private boolean shouldSkipExistingContentTypeMeta(Item headElement, Item child) {
        if (!shouldInjectContentTypeMeta(headElement) || !child.isElementNode() || !isHtmlElement(child)) {
            return false;
        }
        if (!hasLocalName(child, "meta")) {
            return false;
        }
        for (Item attribute : child.attributes()) {
            Name name = attribute.nodeName();
            if (name == null || name.getNamespace() != null) {
                continue;
            }
            if (!"http-equiv".equalsIgnoreCase(name.getLocalName())) {
                continue;
            }
            if ("content-type".equalsIgnoreCase(attribute.getStringValue().trim())) {
                return true;
            }
        }
        return false;
    }

    private void appendInjectedMetaElement(StringBuilder sb) {
        sb.append("<meta http-equiv=\"Content-Type\" content=\"");
        sb.append(escapeAttribute(getEffectiveMediaType()));
        sb.append("; charset=");
        sb.append(this.params.getEncoding() == null ? "UTF-8" : this.params.getEncoding());
        sb.append("\">");
    }

    private String getEffectiveMediaType() {
        if (this.params.getMediaType() != null && !this.params.getMediaType().isEmpty()) {
            return this.params.getMediaType();
        }
        return "text/html";
    }

    private boolean isRawTextElement(Item item) {
        return isHtmlElement(item)
            && item.nodeName() != null
            && RAW_TEXT_ELEMENTS.contains(item.nodeName().getLocalName().toLowerCase());
    }

    private boolean shouldMinimizeBooleanAttribute(Item attribute) {
        if (attribute == null || attribute.nodeName() == null || attribute.nodeName().getNamespace() != null) {
            return false;
        }
        Item parent = attribute.parent();
        if (!isHtmlElement(parent)) {
            return false;
        }
        String localName = attribute.nodeName().getLocalName();
        if (localName == null || !HTML_BOOLEAN_ATTRIBUTES.contains(localName.toLowerCase())) {
            return false;
        }
        String value = attribute.getStringValue();
        return value != null && value.equalsIgnoreCase(localName);
    }

    private Item findFirstElementChild(List<Item> children) {
        for (Item child : children) {
            if (child.isElementNode()) {
                return child;
            }
        }
        return null;
    }

    private String getSerializedElementName(Item item) {
        if (item == null || item.nodeName() == null) {
            return "";
        }
        Name name = item.nodeName();
        if (XHTML_NS.equals(name.getNamespace()) && (name.getPrefix() == null || name.getPrefix().isEmpty())) {
            return name.getLocalName();
        }
        if (shouldApplyPrefixNormalization(item)) {
            return name.getLocalName();
        }
        if (name.getPrefix() != null && !name.getPrefix().isEmpty()) {
            return name.getPrefix() + ":" + name.getLocalName();
        }
        return name.getLocalName();
    }

    private boolean isHtmlElement(Item item) {
        if (item == null || !item.isElementNode() || item.nodeName() == null) {
            return false;
        }
        String namespace = item.nodeName().getNamespace();
        return namespace == null || XHTML_NS.equals(namespace);
    }

    private boolean shouldApplyPrefixNormalization(Item item) {
        if (!isHtml5Version() || item == null || !item.isElementNode() || item.nodeName() == null) {
            return false;
        }
        String namespace = item.nodeName().getNamespace();
        if (SVG_NS.equals(namespace) || MATHML_NS.equals(namespace)) {
            return true;
        }
        return XHTML_NS.equals(namespace)
            && item.nodeName().getPrefix() != null
            && !item.nodeName().getPrefix().isEmpty();
    }

    private boolean hasLocalName(Item item, String localName) {
        return item != null
            && item.nodeName() != null
            && item.nodeName().getLocalName().equalsIgnoreCase(localName);
    }

    @Override
    protected boolean containsElementLikeChild(List<Item> children) {
        for (Item child : children) {
            if (child.isElementNode()) {
                return true;
            }
        }
        return false;
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

    private boolean isHtmlEmptyElement(Item item) {
        if (!item.isElementNode() || item.nodeName() == null) {
            return false;
        }
        if (!isHtml5Version() && XHTML_NS.equals(item.nodeName().getNamespace())) {
            return false;
        }
        String localName = item.nodeName().getLocalName();
        if (localName == null) {
            return false;
        }
        String lower = localName.toLowerCase();
        if (isHtml5Version()) {
            return HTML5_VOID_ELEMENTS.contains(lower);
        }
        return HTML4_EMPTY_ELEMENTS.contains(lower);
    }

    private boolean isHtml5Version() {
        String version = this.params.getVersion();
        if (version == null || version.trim().isEmpty()) {
            return false;
        }
        try {
            return BigDecimal.valueOf(5L).compareTo(new BigDecimal(version.trim())) == 0;
        } catch (NumberFormatException e) {
            return version.trim().startsWith("5");
        }
    }
}
