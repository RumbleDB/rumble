package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.context.Name;

public class AttributeTest implements NodeTest {
    private Name attributeName;
    private boolean hasWildcard;
    private Name typeName;

    public AttributeTest(Name attributeName, Name typeName) {
        this.attributeName = attributeName;
        this.typeName = typeName;
        this.hasWildcard = false;
    }

    public AttributeTest(Name typeName) {
        this.attributeName = null;
        this.typeName = typeName;
        this.hasWildcard = true;
    }

    public AttributeTest(boolean hasWildcard) {
        this.attributeName = null;
        this.typeName = null;
        this.hasWildcard = hasWildcard;
    }

    public AttributeTest() {
        this.attributeName = null;
        this.typeName = null;
        this.hasWildcard = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("attribute(");
        if (this.hasWildcard) {
            sb.append("*");
        } else if (this.attributeName != null) {
            sb.append(this.attributeName);
        }
        if (this.typeName != null) {
            sb.append(",");
            sb.append(this.typeName);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return !this.hasWildcard && this.attributeName == null;
    }

    public boolean isNameWithoutTypeCheck() {
        return this.attributeName != null && this.typeName == null;
    }

    public String getAttributeName() {
        return this.attributeName.getLocalName();
    }

    public boolean isWildcardOnly() {
        return this.attributeName == null && this.typeName == null && this.hasWildcard;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.attributeName);
        kryo.writeObject(output, this.typeName);
        output.writeBoolean(this.hasWildcard);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.attributeName = kryo.readObject(input, Name.class);
        this.typeName = kryo.readObject(input, Name.class);
        this.hasWildcard = input.readBoolean();
    }
}
