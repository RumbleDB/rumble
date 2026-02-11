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

public class ProcessingInstructionItem implements Item {
    private static final long serialVersionUID = 1L;
    private String target;
    private String content;
    private Item parent;
    private XMLDocumentPosition documentPos;

    // needed for kryo
    public ProcessingInstructionItem() {
    }

    public ProcessingInstructionItem(Node processingInstructionNode) {
        this.target = processingInstructionNode.getNodeName();
        this.content = processingInstructionNode.getNodeValue();
    }

    /**
     * Constructor for a processing instruction item.
     * 
     * @param target The processing instruction target
     * @param content The processing instruction content
     */
    public ProcessingInstructionItem(String target, String content) {
        this.target = target;
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
    public Item parent() {
        return this.parent;
    }

    @Override
    public String nodeName() {
        return this.target;
    }

    @Override
    public String getStringValue() {
        return this.content;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.processingInstructionNode;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isProcessingInstructionNode() {
        return true;
    }

    @Override
    public List<Item> atomizedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.content));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProcessingInstructionItem)) {
            return false;
        }
        ProcessingInstructionItem otherItem = (ProcessingInstructionItem) other;
        return this.getXmlDocumentPosition().equals(otherItem.getXmlDocumentPosition());
    }

    @Override
    public int hashCode() {
        return this.documentPos.hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.target);
        output.writeString(this.content);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.target = input.readString();
        this.content = input.readString();
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
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — node-kind.
     *
     * For a Processing Instruction Node, dm:node-kind returns the string "processing-instruction".
     */
    @Override
    public String nodeKind() {
        return "processing-instruction";
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — attributes.
     *
     * For a Processing Instruction Node, dm:attributes returns the empty sequence.
     */
    @Override
    public List<Item> attributes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — children.
     *
     * For a Processing Instruction Node, dm:children returns the empty sequence.
     */
    @Override
    public List<Item> children() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — base-uri.
     *
     * For a Processing Instruction Node, dm:base-uri returns the base URI of the parent node,
     * if it has one; otherwise it returns the empty sequence.
     */
    @Override
    public List<Item> baseUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.baseUri();
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — document-uri.
     *
     * For a Processing Instruction Node, dm:document-uri returns the document-uri of the
     * document node that is the root of the tree containing the processing instruction node,
     * if it has one; otherwise it returns the empty sequence.
     */
    @Override
    public List<Item> documentUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.documentUri();
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — is-id.
     *
     * For a Processing Instruction Node, dm:is-id returns false.
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — is-idrefs.
     *
     * For a Processing Instruction Node, dm:is-idrefs returns false.
     */
    @Override
    public boolean isIdrefs() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — nilled.
     *
     * For a Processing Instruction Node, dm:nilled returns the empty sequence.
     */
    @Override
    public List<Item> nilled() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.5 Processing Instruction Node Accessors — type-name.
     *
     * For a Processing Instruction Node, dm:type-name returns the empty sequence.
     */
    @Override
    public List<Item> typeName() {
        return Collections.emptyList();
    }
}

