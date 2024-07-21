package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

public class TextItem implements Item {
    private static final long serialVersionUID = 1L;
    private Node textNode;
    private Item parent;

    public TextItem() {
        super();
    }

    public TextItem(Node textNode) {
        super();
        this.textNode = textNode;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public String getTextValue() {
        return this.textNode.getNodeValue();
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getTextValue().isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.textNode);
        kryo.writeObject(output, this.parent);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.textNode = kryo.readObject(input, Node.class);
        this.parent = kryo.readObject(input, Item.class);
    }

    public int hashCode() {
        return getTextValue().hashCode();
    }

    @Override
    public boolean nilled() {
        return false;
    }

    @Override
    public String nodeKind() {
        return "text";
    }

    @Override
    public String stringValue() {
        return this.textNode.getTextContent();
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }
}
