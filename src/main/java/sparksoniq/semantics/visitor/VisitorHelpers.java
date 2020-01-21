package sparksoniq.semantics.visitor;

import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.StaticContext;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(ExpressionOrClause expression, RuntimeIterator argument) {
        return new RuntimeIteratorVisitor().visit(expression, argument);
    }

    public static StaticContext generateStaticContextDoublePass(ExpressionOrClause expression, StaticContext argument) {
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(expression, argument);
        visitor.setConfigForConsequentPasses();
        return visitor.visit(expression, argument);
    }
}
