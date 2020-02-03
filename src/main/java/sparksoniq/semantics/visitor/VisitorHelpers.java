package sparksoniq.semantics.visitor;

import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.StaticContext;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(ExpressionOrClause expression) {
        return new RuntimeIteratorVisitor().visit(expression, null);
    }

    public static void populateStaticContext(ExpressionOrClause expression) {
        // perform double pass over static context to support function hoisting
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(expression, null);
        visitor.setConfigForConsequentPasses();
        visitor.visit(expression, null);
    }
}
