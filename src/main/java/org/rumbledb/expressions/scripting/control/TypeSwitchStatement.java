package org.rumbledb.expressions.scripting.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class TypeSwitchStatement extends Statement {
    private final Expression testCondition;
    private final List<TypeSwitchStatementCase> cases;
    private final TypeSwitchStatementCase defaultCase;

    public TypeSwitchStatement(
            Expression testCondition,
            List<TypeSwitchStatementCase> cases,
            TypeSwitchStatementCase defaultCase,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (TypeSwitchStatementCase tswc : this.cases) {
            result.add(tswc.getReturnStatement());
        }
        result.add(this.defaultCase.getReturnStatement());
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("typeswitch (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        for (TypeSwitchStatementCase c : this.cases) {
            indentIt(sb, indent + 1);
            sb.append("case ($");
            sb.append(c.getVariableName().toString() + " as ");
            for (int i = 0; i < c.getUnion().size(); i++) {
                c.getUnion().get(i).toString();
                if (i == c.getUnion().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(" | ");
                }
            }
            sb.append("return (");
            c.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }

        if (this.defaultCase != null) {
            indentIt(sb, indent + 1);
            // TODO seems somehow wrong, should've been Statement not the case
            sb.append("default ($");
            sb.append(this.defaultCase.getVariableName().toString() + " as ");
            for (int i = 0; i < this.defaultCase.getUnion().size(); i++) {
                this.defaultCase.getUnion().get(i).toString();
                if (i == this.defaultCase.getUnion().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(" | ");
                }
            }
            sb.append(")\n");

            sb.append("return (");
            this.defaultCase.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<TypeSwitchStatementCase> getCases() {
        return this.cases;
    }

    public TypeSwitchStatementCase getDefaultCase() {
        return this.defaultCase;
    }
}
