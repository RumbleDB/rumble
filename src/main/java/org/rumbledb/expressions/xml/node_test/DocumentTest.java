package org.rumbledb.expressions.xml.node_test;

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
}
