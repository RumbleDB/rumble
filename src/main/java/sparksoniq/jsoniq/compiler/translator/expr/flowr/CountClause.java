package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.Collections;
import java.util.List;

public class CountClause extends FlworClause {
    private VariableReference countClauseVar;

    public CountClause(VariableReference countClauseVar, ExpressionMetadata metadata) {
        super(FLWOR_CLAUSES.COUNT, metadata);
        this.countClauseVar = countClauseVar;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        return getDescendantsFromChildren(Collections.singletonList(countClauseVar), depthSearch);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitCountClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        return "(countClause count " + countClauseVar.serializationString(true) + ")";
    }

    public VariableReference getCountVariable() {
        return countClauseVar;
    }

}
