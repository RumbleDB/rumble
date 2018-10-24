package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.Collections;
import java.util.List;

public class CountClause extends FlworClause {
    public VariableReference getVariableReference() {
        return variableReference;
    }

    public CountClause(VariableReference variableReference, ExpressionMetadata metadata) {
        super(FLWOR_CLAUSES.COUNT, metadata);
        this.variableReference = variableReference;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = Collections.singletonList(variableReference);
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitCountClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        return "(countClause count " + variableReference.serializationString(true) + ")";
    }

    private final VariableReference variableReference;
}
