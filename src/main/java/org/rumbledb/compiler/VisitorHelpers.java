package org.rumbledb.compiler;

import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Node;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node node) {
        return new RuntimeIteratorVisitor().visit(node, null);
    }

    public static void populateStaticContext(Node node) {
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(node, null);

        visitor.setConfigForIntermediatePasses();
        int prevUnsetCount = Functions.getCurrentUnsetUserDefinedFunctionIdentifiers().size();
        while (true) {
            visitor.visit(node, null);
            int currentUnsetCount = Functions.getCurrentUnsetUserDefinedFunctionIdentifiers().size();

            if (currentUnsetCount > prevUnsetCount) {
                throw new OurBadException(
                        "Unexpected program state reached while performing multi-pass over StaticContext."
                );
            }
            if (currentUnsetCount == prevUnsetCount) {
                setLocalExecutionForUnsetUserDefinedFunctions();
                break;
            }
            prevUnsetCount = currentUnsetCount;
        }

        visitor.setConfigForFinalPass();
        visitor.visit(node, null);
    }

    private static void setLocalExecutionForUnsetUserDefinedFunctions() {
        try {
            for (
                FunctionIdentifier functionIdentifier : Functions
                    .getCurrentUnsetUserDefinedFunctionIdentifiers()
            ) {
                Functions.addUserDefinedFunctionExecutionMode(
                    functionIdentifier,
                    ExecutionMode.LOCAL,
                    true,
                    null
                );
            }
        } catch (DuplicateFunctionIdentifierException e) {
            throw new OurBadException(
                    "Unexpected program state reached while setting local execution for unset user defined functions."
            );
        }
    }
}
