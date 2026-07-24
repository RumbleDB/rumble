package org.rumbledb.expressions.xml.node_test;


import java.io.Serial;

public class DocumentTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
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
        return this.nodeTest == null;
    }

    public NodeTest getNodeTest() {
        return this.nodeTest;
    }


}
