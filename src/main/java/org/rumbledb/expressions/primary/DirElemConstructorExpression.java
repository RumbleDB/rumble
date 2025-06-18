package org.rumbledb.expressions.primary;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class DirElemConstructorExpression extends Expression {
    private final String tagName;
    private final List<Expression> content;
    private final List<Expression> attributes;

    public DirElemConstructorExpression(
            String tagName,
            List<Expression> content,
            List<Expression> attributes,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.tagName = tagName;
        this.content = content;
        this.attributes = attributes;
    }

    public String getTagName() {
        return this.tagName;
    }

    public List<Expression> getContent() {
        return this.content;
    }

    public List<Expression> getAttributes() {
        return this.attributes;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDirElemConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.content != null) {
            result.addAll(this.content);
        }
        // in the XML data model, attributes are considered children
        if (this.attributes != null) {
            result.addAll(this.attributes);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("<" + this.tagName);
        if (this.attributes != null && !this.attributes.isEmpty()) {
            for (Expression attr : this.attributes) {
                attr.serializeToJSONiq(sb, indent);
                sb.append(" ");
            }
        }
        sb.append(">");
        if (this.content != null && !this.content.isEmpty()) {
            sb.append("\n");
            for (Expression expr : this.content) {
                expr.serializeToJSONiq(sb, indent + 1);
            }
            indentIt(sb, indent);
        }
        sb.append("</" + this.tagName + ">\n");
    }

}
