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

public class CommentItem implements Item {
    private static final long serialVersionUID = 1L;
    private String content;
    private Item parent;
    private XMLDocumentPosition documentPos;

    // needed for kryo
    public CommentItem() {
    }

    public CommentItem(String content) {
        this.content = content;
    }

    public CommentItem(Node commentNode) {
        this.content = commentNode.getNodeValue();
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
    public Item parent() {
        return this.parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.commentNode;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isCommentNode() {
        return true;
    }

    /**
     * XDM 3.1 Section 6.6 Comment Node Accessors — node-kind.
     *
     * "For a Comment Node, dm:node-kind returns the string \"comment\"."
     */
    @Override
    public String nodeKind() {
        return "comment";
    }

    /**
     * XDM 3.1 Section 6.6 Comment Node Accessors — node-name.
     *
     * "For a Comment Node, dm:node-name returns the empty sequence."
     *
     * An empty string is used here to represent the empty sequence.
     */
    @Override
    public String nodeName() {
        return "";
    }

    @Override
    public String getStringValue() {
        return this.content;
    }

    @Override
    public List<Item> atomizedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.content));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CommentItem)) {
            return false;
        }
        CommentItem otherComment = (CommentItem) other;
        return this.getXmlDocumentPosition() != null
            && this.getXmlDocumentPosition().equals(otherComment.getXmlDocumentPosition());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.content);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.content = input.readString();
    }

    @Override
    public List<Item> namespaceNodes() {
        return new ArrayList<>();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return new ArrayList<>();
    }
}

