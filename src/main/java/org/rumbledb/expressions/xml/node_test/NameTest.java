package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.context.Name;

// TODO: Add support for name test
public class NameTest implements NodeTest {
    private static final long serialVersionUID = 1L;
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

    /**
     * Expanded name (namespace URI + local name). Prefer {@link Name#equals} over string forms for node matching:
     * the same expanded name can stringify differently when the prefix is empty vs absent.
     */
    public Name getExpandedName() {
        return this.qname;
    }

    public boolean hasWildcardOnly() {
        return this.wildcardWithNCName != null && this.wildcardWithNCName.equals("*");
    }

    public String getWildcardQName() {
        return this.wildcardWithNCName;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.qname);
        kryo.writeObject(output, this.wildcardWithNCName);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.qname = kryo.readObject(input, Name.class);
        this.wildcardWithNCName = kryo.readObject(input, String.class);
    }
}
