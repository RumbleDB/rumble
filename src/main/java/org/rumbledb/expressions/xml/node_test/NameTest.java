package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.context.Name;

// TODO: Add support for name test
public class NameTest implements NodeTest {
    private Name qname;
    private String wildcardWithNCName;

    public NameTest(Name qname) {
        this.qname = qname;
        this.wildcardWithNCName = null;
    }

    public NameTest(String wildcardWithNCName) {
        this.qname = null;
        this.wildcardWithNCName = wildcardWithNCName;
    }

    @Override
    public String toString() {
        if (this.qname != null) {
            return this.qname.toString();
        }
        return this.wildcardWithNCName;
    }

    public boolean hasQName() {
        return this.qname != null;
    }

    public String getQName() {
        return this.qname.toString();
    }

    public boolean hasWildcardOnly() {
        return this.wildcardWithNCName != null && this.wildcardWithNCName.equals("*");
    }

    public String getWildcardQName() {
        return this.wildcardWithNCName;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, qname);
        kryo.writeObject(output, wildcardWithNCName);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        qname = kryo.readObject(input, Name.class);
        wildcardWithNCName = kryo.readObject(input, String.class);
    }
}
