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

public class TextItem implements NodeItem {
    private static final long serialVersionUID = 1L;
    private String content;
    private NodeItem parent;

    public TextItem() {
        super();
    }

    public TextItem(String content) {
        super();
        this.content = content;
    }

    @Override
    public void setParent(NodeItem parent) {
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
        return this.content;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getTextValue().isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getTextValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.content = input.readString();
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
        return this.content;
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
