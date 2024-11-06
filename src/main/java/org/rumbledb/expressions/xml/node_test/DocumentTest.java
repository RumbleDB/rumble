package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class DocumentTest implements NodeTest {
    // TODO: schemaElement test unsupported yet.
    private NodeTest nodeTest;

    public DocumentTest(NodeTest nodeTest) {
        this.nodeTest = nodeTest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("document-node(");
        if (this.nodeTest != null) {
            sb.append(this.nodeTest);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return nodeTest == null;
    }

    public NodeTest getNodeTest() {
        return this.nodeTest;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, nodeTest);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        nodeTest = kryo.readObject(input, NodeTest.class);
    }
}
