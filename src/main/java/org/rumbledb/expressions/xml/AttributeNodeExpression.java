package org.rumbledb.expressions.xml;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.exceptions.ExceptionMetadata;

public class AttributeNodeExpression extends Expression {
    /**
     * The qname of the attribute node.
     */
    private String qname;
    /**
     * The value of the attribute node.
     * 
     * The value is a list of expressions. This is because an attribute node can be
     * constructed from multiple expressions and literals, which are materialized at runtime.
     */
    private List<Expression> value;

    public AttributeNodeExpression(String qname, List<Expression> value, ExceptionMetadata metadata) {
        super(metadata);
        this.qname = qname;
        this.value = value;
    }

    public String getQName() {
        return this.qname;
    }

    public List<Expression> getValue() {
        return this.value;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.value);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAttributeNode(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.qname);
        sb.append("=");
        for (Expression child : this.value) {
            child.serializeToJSONiq(sb, indent);
        }
    }
}
