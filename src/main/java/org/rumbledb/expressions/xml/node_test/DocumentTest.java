package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;

public class DocumentTest implements NodeTest {
    private static final long serialVersionUID = 1L;
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
        return this.nodeTest == null;
    }

    public NodeTest getNodeTest() {
        return this.nodeTest;
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.nodeTest != null) {
            this.nodeTest.resolve(context, metadata);
        }
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.nodeTest);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.nodeTest = kryo.readObject(input, NodeTest.class);
    }
}
