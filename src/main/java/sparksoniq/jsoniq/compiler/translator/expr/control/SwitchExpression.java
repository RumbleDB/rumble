package sparksoniq.jsoniq.compiler.translator.expr.control;

import java.util.ArrayList;
import java.util.List;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class SwitchExpression extends Expression {

    public Expression getTestCondition() {
        return testCondition;
    }

    public List<SwitchCaseExpression> getCases() {
        return cases;
    }

    public Expression getDefaultExpression() {
        return defaultExpression;
    }

    public SwitchExpression(Expression testCondition, List<SwitchCaseExpression> cases, Expression defaultExpression, ExpressionMetadata metadataFromContext) {
        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultExpression = defaultExpression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(testCondition);
        result.addAll(cases);
        result.add(defaultExpression);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitSwitchExpression(this, argument);
    }

    @Override
    //TODO implement serialization for switch expr
    public String serializationString(boolean prefix){
        return "";
    }

    private final Expression testCondition;
    private final List<SwitchCaseExpression> cases;
    private final Expression defaultExpression;
}
