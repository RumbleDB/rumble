package sparksoniq.jsoniq.compiler.translator.expr.control;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

public class SwitchCaseExpression extends Expression{

    public SwitchCaseExpression(Expression condition, Expression returnExpression,
                                ExpressionMetadata metadataFromContext) {
        super(metadataFromContext);
        this.condition = condition;
        this.returnExpression = returnExpression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(condition);
        result.add(returnExpression);
        return getDescendantsFromChildren(result, depthSearch);
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    public Expression getCondition() {
        return condition;
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitSwitchCaseExpression(this, argument);
    }

    @Override
    //TODO implement serialization for switch expr
    public String serializationString(boolean prefix){
        return "";
    }

    private final Expression returnExpression;
    private final Expression condition;
}
