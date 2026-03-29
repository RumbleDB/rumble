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
    public static QNameItem nameFromElementOrAttributeDomNode(Node domNode) {
        String namespace = domNode.getNamespaceURI();
        String local = domNode.getLocalName();
        String prefix = domNode.getPrefix();

        Name name = null;
        if (local == null) {
            name = new Name(null, null, domNode.getNodeName());
        } else if (prefix != null && !prefix.equals("")) {
            name = new Name(namespace, prefix, local);
        } else {
            name = new Name(namespace, null, local);
        }
        return new QNameItem(name);
    }

    /**
     * QName with only a local name and no namespace URI or prefix (XDM processing-instruction target; namespace
     * prefix property absent).
     */
    public static Name nameLocalOnly(String localName) {
        return new Name(null, null, localName);
    }

    public static QNameItem toQNameItem(Name name) {
        return name == null ? null : new QNameItem(name);
    }
}
