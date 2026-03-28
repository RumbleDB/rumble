package org.rumbledb.items.xml;

import org.rumbledb.context.Name;
import org.rumbledb.items.QNameItem;
import org.w3c.dom.Node;

/**
 * Builds XDM {@code xs:QName} values ({@link QNameItem} / {@link Name}) for XML node-name accessors.
 */
public final class XmlNodeQNameHelper {

    private XmlNodeQNameHelper() {
    }

    /**
     * Expanded name for an element or attribute DOM node (namespace-aware when the DOM provides it).
     */
    public static Name nameFromElementOrAttributeDomNode(Node domNode) {
        String namespace = domNode.getNamespaceURI();
        String local = domNode.getLocalName();
        if (local == null) {
            return nameFromLexicalQualifiedNameOnly(domNode.getNodeName());
        }
        String prefix = domNode.getPrefix();
        if (prefix != null && !prefix.equals("")) {
            return new Name(namespace, prefix, local);
        }
        return new Name(namespace, null, local);
    }

    /**
     * QName with only a local name and no namespace URI or prefix (XDM processing-instruction target; namespace
     * prefix property absent).
     */
    public static Name nameLocalOnly(String localName) {
        return new Name(null, null, localName);
    }

    /**
     * For constructors and other paths where only a lexical QName string is available (possibly {@code prefix:local}
     * without a resolved namespace URI). Stored as a single local-name component to satisfy {@link Name} invariants.
     */
    public static Name nameFromLexicalQualifiedNameOnly(String qualifiedName) {
        return new Name(null, null, qualifiedName);
    }

    public static QNameItem toQNameItem(Name name) {
        return name == null ? null : new QNameItem(name);
    }
}
