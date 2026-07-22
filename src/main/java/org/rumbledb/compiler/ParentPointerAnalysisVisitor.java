package org.rumbledb.compiler;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.FunctionCallExpression;

import java.util.Set;

public class ParentPointerAnalysisVisitor extends AbstractNodeVisitor<Boolean> {

    private static final Set<FunctionIdentifier> FUNCTIONS_REQUIRING_PARENT_POINTERS = Set.of(
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "lang"), 1),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "lang"), 2),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "namespace-uri-for-prefix"), 2),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "root"), 0),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "root"), 1),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "innermost"), 1),
        new FunctionIdentifier(new Name(Name.FN_NS, "fn", "outermost"), 1)
    );

    private boolean found;

    public boolean scan(Node module) {
        this.found = false;
        visit(module, null);
        return this.found;
    }

    @Override
    public Boolean visitFunctionCall(FunctionCallExpression expression, Boolean argument) {
        if (FUNCTIONS_REQUIRING_PARENT_POINTERS.contains(expression.getFunctionIdentifier())) {
            this.found = true;
        }
        return defaultAction(expression, argument);
    }
}
