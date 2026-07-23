package org.rumbledb.compiler;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.FunctionCallExpression;

public class ParentPointerAnalysisVisitor extends AbstractNodeVisitor<Boolean> {

    private boolean found;

    public boolean scan(Node module) {
        this.found = false;
        visit(module, null);
        return this.found;
    }

    @Override
    public Boolean visitFunctionCall(FunctionCallExpression expression, Boolean argument) {
        if (requiresParentPointers(expression.getFunctionIdentifier())) {
            this.found = true;
        }
        return defaultAction(expression, argument);
    }

    private boolean requiresParentPointers(FunctionIdentifier identifier) {
        Name name = identifier.getName();
        String namespace = name.getNamespace();
        if (
            !Name.FN_NS.equals(namespace)
                && !Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(namespace)
        ) {
            return false;
        }
        String localName = name.getLocalName();
        int arity = identifier.getArity();
        return ("lang".equals(localName) && (arity == 1 || arity == 2))
            || ("in-scope-prefixes".equals(localName) && arity == 1)
            || ("namespace-uri-for-prefix".equals(localName) && arity == 2)
            || ("innermost".equals(localName) && arity == 1)
            || ("outermost".equals(localName) && arity == 1);
    }
}
