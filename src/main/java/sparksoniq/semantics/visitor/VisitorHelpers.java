package sparksoniq.semantics.visitor;

import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.IntegerLiteral;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node expression) {
        return new RuntimeIteratorVisitor().visit(expression, null);
    }

    public static void populateStaticContext(Node expression) {
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(expression, null);

        visitor.setConfigForIntermediatePasses();
        int prevUnsetCount = Functions.getCurrentUnsetUserDefinedFunctionIdentifiers().size();
        while (true) {
            visitor.visit(expression, null);
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
        visitor.visit(expression, null);
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
