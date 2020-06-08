package org.rumbledb.expressions.module;

import java.util.Collections;
import java.util.List;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

public class NamespaceDeclaration extends Node {
    private String prefix;
    private String namespace;

    public NamespaceDeclaration(String prefix, String namespace) {
        this.prefix = prefix;
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visit(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append("NamespaceDeclaration: " + this.prefix + " = " + this.namespace);
        buffer.append("\n");
    }
}
