package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.ElementNodeItemType;

public class ElementTest implements NodeTest {
    private static final long serialVersionUID = 1L;
    private ElementNodeItemType itemType;
    private boolean explicitWildcard;

    public ElementTest(Name elementName, Name typeName) {
        this(elementName, typeName, false);
    }

    public ElementTest(Name elementName, Name typeName, boolean allowsNilled) {
        this.itemType = typeName == null
            ? new ElementNodeItemType(elementName)
            : new ElementNodeItemType(elementName, typeName, allowsNilled);
        this.explicitWildcard = elementName == null;
    }

    public ElementTest(Name typeName) {
        this.itemType = new ElementNodeItemType(null, typeName, false);
        this.explicitWildcard = true;
    }

    public ElementTest(boolean explicitWildcard) {
        this.itemType = new ElementNodeItemType();
        this.explicitWildcard = explicitWildcard;
    }

    public ElementTest() {
        this(false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("element(");
        if (this.explicitWildcard) {
            sb.append("*");
        } else if (this.itemType.getNodeName() != null) {
            sb.append(this.itemType.getNodeName());
        }
        if (this.itemType.getSchemaTypeName() != null) {
            sb.append(", ");
            sb.append(this.itemType.getSchemaTypeName());
            if (this.itemType.allowsNilled()) {
                sb.append("?");
            }
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
        this.itemType = kryo.readObject(input, ElementNodeItemType.class);
        this.explicitWildcard = input.readBoolean();
    }
}
