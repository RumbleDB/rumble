package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

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
        return BuiltinTypesCatalogue.item;
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
}

