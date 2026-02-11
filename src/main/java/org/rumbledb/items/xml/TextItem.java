package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextItem implements Item {
    private static final long serialVersionUID = 1L;
    private String content; // is also typed-value
    private Item parent;
    private XMLDocumentPosition documentPos;

    // needed for kryo
    public TextItem() {
    }

    public TextItem(Node textNode) {
        this.content = textNode.getTextContent();
    }

    /**
     * Create a new Text Node with the given content.
     * 
     * @param content the content of the text node
     */
    public TextItem(String content) {
        this.content = content;
    }

    @Override
    public int setXmlDocumentPosition(String path, int current) {
        this.documentPos = new XMLDocumentPosition(path, current);
        return ++current;
    }

    @Override
    public XMLDocumentPosition getXmlDocumentPosition() {
        return this.documentPos;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextItem)) {
            return false;
        }
        TextItem otherTextItem = (TextItem) other;
        return this.getXmlDocumentPosition().equals(otherTextItem.getXmlDocumentPosition());
    }

    @Override
    public String getTextValue() {
        return this.content;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.content.isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.content);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.content = input.readString();
    }

    public int hashCode() {
        return this.documentPos.hashCode();
    }

    @Override
    public String getStringValue() {
        return this.content;
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.textNode;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isTextNode() {
        return true;
    }

    @Override
    public String nodeName() {
        return "";
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — node-kind.
     *
     * "For a Text Node, dm:node-kind returns the string \"text\"."
     */
    @Override
    public String nodeKind() {
        return "text";
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — attributes.
     *
     * "For a Text Node, dm:attributes returns the empty sequence."
     */
    @Override
    public List<Item> attributes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — children.
     *
     * "For a Text Node, dm:children returns the empty sequence."
     */
    @Override
    public List<Item> children() {
        return Collections.emptyList();
    }

    @Override
    public List<Item> atomizedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.content));
    }

    @Override
    public List<Item> namespaceNodes() {
        return Collections.emptyList();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — base-uri.
     *
     * For a Text Node, dm:base-uri returns the base URI of the parent node, if it has one;
     * otherwise it returns the empty sequence.
     */
    @Override
    public List<Item> baseUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.baseUri();
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — document-uri.
     *
     * For a Text Node, dm:document-uri returns the document-uri of the document node that is
     * the root of the tree containing the text node, if it has one; otherwise it returns the
     * empty sequence.
     */
    @Override
    public List<Item> documentUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.documentUri();
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — is-id.
     *
     * For a Text Node, dm:is-id returns false.
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — is-idrefs.
     *
     * For a Text Node, dm:is-idrefs returns false.
     */
    @Override
    public boolean isIdrefs() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — nilled.
     *
     * For a Text Node, dm:nilled returns the empty sequence.
     */
    @Override
    public List<Item> nilled() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.7 Text Node Accessors — type-name.
     *
     * For a Text Node, dm:type-name returns the empty sequence.
     */
    @Override
    public List<Item> typeName() {
        return Collections.emptyList();
    }
}
