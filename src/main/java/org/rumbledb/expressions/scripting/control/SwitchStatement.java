package org.rumbledb.expressions.scripting.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class SwitchStatement extends Statement {
    private final Expression testCondition;
    private final List<SwitchCaseStatement> cases;
    private final Statement defaultStatement;

    public SwitchStatement(
            Expression testCondition,
            List<SwitchCaseStatement> cases,
            Statement defaultStatement,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitSwitchStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (SwitchCaseStatement swc : this.cases) {
            result.addAll(swc.getConditionExpressions());
            result.add(swc.getReturnStatement());
        }
        result.add(this.defaultStatement);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("switch (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        for (SwitchCaseStatement swc : this.cases) {
            indentIt(sb, indent + 1);
            sb.append("case (");
            for (int i = 0; i < swc.getConditionExpressions().size(); i++) {
                swc.getConditionExpressions().get(i).serializeToJSONiq(sb, 0);
                if (i == swc.getConditionExpressions().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append("return (");
            swc.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }

        if (this.defaultStatement != null) {
            indentIt(sb, indent + 1);
            sb.append("default return (");
            this.defaultStatement.serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<SwitchCaseStatement> getCases() {
        return this.cases;
    }

    public Statement getDefaultStatement() {
        return this.defaultStatement;
    }
}
