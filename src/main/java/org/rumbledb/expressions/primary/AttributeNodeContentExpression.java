package org.rumbledb.expressions.primary;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Expression representing text content within XML attribute values.
 * This is distinct from StringLiteralExpression as it exists specifically
 * in the context of XML attribute values and has different semantics.
 */
public class AttributeNodeContentExpression extends Expression {

    private String content;

    public AttributeNodeContentExpression(String content, ExceptionMetadata metadata) {
        super(metadata);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAttributeNodeContent(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        // just append the content as is
        // this is not a string literal, but it's only used in the context of attribute values
        sb.append(this.content);
    }
}
