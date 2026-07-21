package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class XhtmlSerializer extends XmlSerializer {

    private static final long serialVersionUID = 1L;

    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";

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

    private static final Set<String> XHTML_EMPTY_ELEMENTS = Set.of(
        "area",
        "base",
        "br",
        "col",
        "embed",
        "hr",
        "img",
        "input",
        "link",
        "meta",
        "basefont",
        "frame",
        "isindex",
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

    private static final Set<String> HTML5_ELEMENTS = Set.of(
        "a",
        "abbr",
        "address",
        "area",
        "article",
        "aside",
        "audio",
        "b",
        "base",
        "bdi",
        "bdo",
        "blockquote",
        "body",
        "br",
        "button",
        "canvas",
        "caption",
        "cite",
        "code",
        "col",
        "colgroup",
        "data",
        "datalist",
        "dd",
        "del",
        "details",
        "dfn",
        "dialog",
        "div",
        "dl",
        "dt",
        "em",
        "embed",
        "fieldset",
        "figcaption",
        "figure",
        "footer",
        "form",
        "h1",
        "h2",
        "h3",
        "h4",
        "h5",
        "h6",
        "head",
        "header",
        "hgroup",
        "hr",
        "html",
        "i",
        "iframe",
        "img",
        "input",
        "ins",
        "kbd",
        "label",
        "legend",
        "li",
        "link",
        "main",
        "map",
        "mark",
        "menu",
        "meta",
        "meter",
        "nav",
        "noscript",
        "object",
        "ol",
        "optgroup",
        "option",
        "output",
        "p",
        "param",
        "picture",
        "pre",
        "progress",
        "q",
        "rp",
        "rt",
        "ruby",
        "s",
        "samp",
        "script",
        "section",
        "select",
        "slot",
        "small",
        "source",
        "span",
        "strong",
        "style",
        "sub",
        "summary",
        "sup",
        "table",
        "tbody",
        "td",
        "template",
        "textarea",
        "tfoot",
        "th",
        "thead",
        "time",
        "title",
        "tr",
        "track",
        "u",
        "ul",
        "var",
        "video",
        "wbr"
    );

    public XhtmlSerializer(SerializationParameters params) {
        super(params);
    }

    @Override
    protected void serializeDocumentNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        Item firstElementChild = findFirstElementChild(item.children());
        if (shouldEmitHtml5Doctype(item.children(), firstElementChild)) {
            appendHtml5Doctype(firstElementChild, sb);
        } else if (firstElementChild != null) {
            appendDocTypeIfNeeded(firstElementChild, sb);
        }
        for (Item child : item.children()) {
            serialize(child, sb, indent, false);
        }
    }

    @Override
    protected void serializeElementNode(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        boolean injectContentTypeMeta = shouldInjectContentTypeMeta(item);
        boolean hasSerializedChildren = !item.children().isEmpty() || injectContentTypeMeta;
        if (isTopLevel) {
            if (shouldEmitHtml5Doctype(List.of(item), item)) {
                appendHtml5Doctype(item, sb);
            } else {
                appendDocTypeIfNeeded(item, sb);
            }
        }

        String serializedElementName = getSerializedElementName(item);
        sb.append("<");
        sb.append(serializedElementName);

        appendElementNamespaces(item, sb);
        for (Item attribute : item.attributes()) {
            appendAttributeOrNamespaceNode(attribute, sb);
        }

        if (!hasSerializedChildren) {
            if (isExpectedToBeEmpty(item)) {
                sb.append(isHtml5Mode() ? "/>" : " />");
            } else {
                sb.append("></");
                sb.append(serializedElementName);
                sb.append(">");
            }
            return;
        }

        sb.append(">");
        List<Item> children = item.children();
        boolean indenting = shouldIndentElement(item);
        boolean containsElementLikeChild = injectContentTypeMeta || containsElementLikeChild(children);
        boolean preserveWhitespace = mustPreserveWhitespace(item);
        boolean hasSignificantTextChild = hasSignificantTextChild(children);
        String childIndent = nextIndent(indent);
        if (injectContentTypeMeta) {
            if (
                indenting
                    && containsElementLikeChild
                    && !preserveWhitespace
                    && !hasSignificantTextChild
            ) {
                sb.append("\n").append(childIndent);
            }
            appendInjectedMetaElement(item, sb);
        }
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
        sb.append(serializedElementName);
        sb.append(">");
    }

    @Override
    protected String prepareAttributeValue(Item attribute) {
        String value = attribute.getStringValue();
        if (!this.params.getEscapeUriAttributes()) {
            return value;
        }
        Name attributeName = attribute.nodeName();
        String localName = attributeName == null ? null : attributeName.getLocalName();
        if (localName == null || !URI_ATTRIBUTES.contains(localName.toLowerCase())) {
            return value;
        }
        return escapeUriAttribute(value);
    }

    @Override
    protected String escapeAttribute(String value) {
        String escaped = super.escapeAttribute(value);
        if (isHtml5Mode()) {
            return escaped;
        }
        return escaped.replace("&apos;", "&#39;");
    }

    @Override
    protected boolean shouldEmitXmlDeclaration(Item item) {
        return !this.params.getOmitXmlDeclaration() && item.isDocumentNode();
    }

    @Override
    protected boolean matchesExpandedQNameEntry(Set<String> entries, Item element) {
        if (super.matchesExpandedQNameEntry(entries, element)) {
            return true;
        }
        if (entries == null || entries.isEmpty() || element.nodeName() == null) {
            return false;
        }
        if (isRecognizedHtmlElement(element) && element.nodeName().getNamespace() == null) {
            for (String entry : entries) {
                if (!entry.startsWith("Q{") && entry.equalsIgnoreCase(element.nodeName().getLocalName())) {
                    return true;
                }
                if (
                    entry.equals("Q{" + XHTML_NAMESPACE + "}" + element.nodeName().getLocalName())
                        || entry.equals("Q{" + XHTML_NAMESPACE + "}" + element.nodeName().getLocalName().toLowerCase())
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void appendElementNamespaces(Item item, StringBuilder sb) {
        boolean emittedDefaultNamespaceForNormalizedElement = false;
        if (shouldApplyPrefixNormalization(item)) {
            String normalizedNamespace = item.nodeName().getNamespace();
            if (!isNormalizedDefaultNamespaceInScope(item.parent(), normalizedNamespace)) {
                sb.append(" xmlns=\"");
                sb.append(escapeAttribute(normalizedNamespace));
                sb.append("\"");
                emittedDefaultNamespaceForNormalizedElement = true;
            }
        }
        for (Item namespace : item.declaredNamespaceNodes()) {
            if (shouldSkipNamespaceDeclaration(item, namespace, emittedDefaultNamespaceForNormalizedElement)) {
                continue;
            }
            appendAttributeOrNamespaceNode(namespace, sb);
        }
    }

    private boolean shouldSkipNamespaceDeclaration(
            Item element,
            Item namespace,
            boolean emittedDefaultNamespaceForNormalizedElement
    ) {
        if (!namespace.isNamespaceNode()) {
            return false;
        }
        String prefix = namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName();
        String uri = namespace.getStringValue();
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

    private boolean isNormalizedDefaultNamespaceInScope(Item context, String namespace) {
        Item current = context;
        while (current != null && current.isElementNode()) {
            if (
                shouldApplyPrefixNormalization(current)
                    && current.nodeName() != null
                    && namespace.equals(current.nodeName().getNamespace())
            ) {
                return true;
            }
            for (Item namespaceNode : current.declaredNamespaceNodes()) {
                Name name = namespaceNode.nodeName();
                String prefix = name == null ? "" : name.getLocalName();
                if (prefix.isEmpty()) {
                    return namespace.equals(namespaceNode.getStringValue());
                }
            }
            current = current.parent();
        }
        return false;
    }

    private boolean shouldInjectContentTypeMeta(Item item) {
        return this.params.getIncludeContentType() && isRecognizedHtmlElement(item) && hasLocalName(item, "head");
    }

    private boolean shouldSkipExistingContentTypeMeta(Item headElement, Item child) {
        if (!shouldInjectContentTypeMeta(headElement) || !child.isElementNode() || !isRecognizedHtmlElement(child)) {
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

    private void appendInjectedMetaElement(Item headElement, StringBuilder sb) {
        String metaName = getInjectedMetaElementName(headElement);
        sb.append("<");
        sb.append(metaName);
        sb.append(" http-equiv=\"Content-Type\" content=\"");
        sb.append(escapeAttribute(getEffectiveMediaType()));
        sb.append("; charset=");
        sb.append(this.params.getEncoding() == null ? "UTF-8" : this.params.getEncoding());
        sb.append("\"");
        if (isExpectedToBeEmptyMeta(headElement)) {
            sb.append(isHtml5Mode() ? "/>" : " />");
        } else {
            sb.append("></");
            sb.append(metaName);
            sb.append(">");
        }
    }

    private String getInjectedMetaElementName(Item headElement) {
        Name headName = headElement.nodeName();
        if (headName == null || headName.getNamespace() == null || headName.getNamespace().isEmpty()) {
            return "meta";
        }
        if (
            shouldApplyPrefixNormalization(headElement)
                || headName.getPrefix() == null
                || headName.getPrefix().isEmpty()
        ) {
            return "meta";
        }
        return headName.getPrefix() + ":meta";
    }

    private boolean isExpectedToBeEmptyMeta(Item headElement) {
        if (headElement.nodeName() == null || headElement.nodeName().getNamespace() == null) {
            return isHtml5Mode() ? HTML5_VOID_ELEMENTS.contains("meta") : XHTML_EMPTY_ELEMENTS.contains("meta");
        }
        return true;
    }

    private String getEffectiveMediaType() {
        if (this.params.getMediaType() != null && !this.params.getMediaType().isEmpty()) {
            return this.params.getMediaType();
        }
        return "application/xhtml+xml";
    }

    private Item findFirstElementChild(List<Item> children) {
        for (Item child : children) {
            if (child.isElementNode()) {
                return child;
            }
        }
        return null;
    }

    private boolean shouldEmitHtml5Doctype(List<Item> children, Item firstElementChild) {
        if (
            !isHtml5Mode()
                || this.params.getDoctypeSystem() != null
                || firstElementChild == null
        ) {
            return false;
        }
        if (!isRecognizedHtmlElement(firstElementChild) || !hasLocalName(firstElementChild, "html")) {
            return false;
        }
        for (Item child : children) {
            if (child == firstElementChild) {
                return true;
            }
            if (child.isTextNode() && !child.getStringValue().trim().isEmpty()) {
                return false;
            }
            if (!child.isTextNode()) {
                return false;
            }
        }
        return false;
    }

    private void appendHtml5Doctype(Item element, StringBuilder sb) {
        sb.append("<!DOCTYPE ");
        sb.append(element.nodeName().getLocalName());
        sb.append(">");
    }

    private String getSerializedElementName(Item item) {
        Name name = item.nodeName();
        if (name == null) {
            return "";
        }
        if (shouldApplyPrefixNormalization(item)) {
            return name.getLocalName();
        }
        if (name.getPrefix() != null && !name.getPrefix().isEmpty()) {
            return name.getPrefix() + ":" + name.getLocalName();
        }
        return name.getLocalName();
    }

    private boolean shouldApplyPrefixNormalization(Item item) {
        if (!isHtml5Mode() || !item.isElementNode() || item.nodeName() == null) {
            return false;
        }
        String namespace = item.nodeName().getNamespace();
        return XHTML_NAMESPACE.equals(namespace)
            || SVG_NAMESPACE.equals(namespace)
            || MATHML_NAMESPACE.equals(namespace);
    }

    private boolean isExpectedToBeEmpty(Item item) {
        if (!isRecognizedHtmlElement(item)) {
            return false;
        }
        String localName = item.nodeName().getLocalName().toLowerCase();
        if (isHtml5Mode()) {
            return HTML5_VOID_ELEMENTS.contains(localName);
        }
        return XHTML_EMPTY_ELEMENTS.contains(localName);
    }

    private boolean isRecognizedHtmlElement(Item item) {
        if (!item.isElementNode() || item.nodeName() == null) {
            return false;
        }
        Name name = item.nodeName();
        if (XHTML_NAMESPACE.equals(name.getNamespace())) {
            return true;
        }
        return isHtml5Mode()
            && name.getNamespace() == null
            && HTML5_ELEMENTS.contains(name.getLocalName().toLowerCase());
    }

    private boolean hasLocalName(Item item, String localName) {
        return item.nodeName() != null && item.nodeName().getLocalName().equalsIgnoreCase(localName);
    }

    private boolean isHtml5Mode() {
        return this.params.isRequestedHtml5Version();
    }

    private String escapeUriAttribute(String value) {
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
