package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.AttributeNodeItemType;

public class AttributeTest implements NodeTest {
    private static final long serialVersionUID = 1L;
    private AttributeNodeItemType itemType;
    private boolean explicitWildcard;

    public AttributeTest(Name attributeName, Name typeName) {
        this.itemType = typeName == null
            ? new AttributeNodeItemType(attributeName)
            : new AttributeNodeItemType(attributeName, typeName);
        this.explicitWildcard = attributeName == null;
    }

    public AttributeTest(Name typeName) {
        this.itemType = new AttributeNodeItemType(null, typeName);
        this.explicitWildcard = true;
    }

    public AttributeTest(boolean explicitWildcard) {
        this.itemType = new AttributeNodeItemType();
        this.explicitWildcard = explicitWildcard;
    }

    public AttributeTest() {
        this(false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("attribute(");
        if (this.explicitWildcard) {
            sb.append("*");
        } else if (this.itemType.getNodeName() != null) {
            sb.append(this.itemType.getNodeName());
        }
        if (this.itemType.getSchemaTypeName() != null) {
            sb.append(", ");
            sb.append(this.itemType.getSchemaTypeName());
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean matches(Item item) {
        return this.itemType.matches(item);
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        this.itemType.resolve(context, metadata);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.itemType);
        output.writeBoolean(this.explicitWildcard);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.itemType = kryo.readObject(input, AttributeNodeItemType.class);
        this.explicitWildcard = input.readBoolean();
    }
}
