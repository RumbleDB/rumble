package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.Functions;
import sparksoniq.jsoniq.ExecutionMode;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node node) {
        return new RuntimeIteratorVisitor().visit(node, null);
    }

    public static void resolveDependencies(Node node, RumbleRuntimeConfiguration conf) {
        new VariableDependenciesVisitor(conf).visit(node, null);
    }
    
    private static void printTree(Node node, RumbleRuntimeConfiguration conf)
    {
        if (conf.isPrintIteratorTree()) {
            System.out.println("***********************");
            System.out.println("Initial expression tree");
            System.out.println("***********************");
            System.out.println("Unset execution modes: " + node.numberOfUnsetExecutionModes());
            System.out.println(node);
            System.out.println();
        }
   }

    public static void populateStaticContext(Node node, RumbleRuntimeConfiguration conf) {
        printTree(node, conf);
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(node, null);


        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorIntermediatePassConfig);
        int prevUnsetCount = Functions.getUserDefinedFunctionIdentifiersWithUnsetExecutionModes().size();
        printTree(node, conf);

        while (true) {
            visitor.visit(node, null);
            int currentUnsetCount = Functions.getUserDefinedFunctionIdentifiersWithUnsetExecutionModes().size();

            printTree(node, conf);

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

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorFinalPassConfig);
        visitor.visit(node, null);
        printTree(node, conf);
        if (node.numberOfUnsetExecutionModes() > 0) {
            System.err.println(
                "Warning! Some execution modes could not be set. The query may still work, but we would welcome a bug report."
            );
        }

    }

    public static DynamicContext createDynamicContext(Node node, RumbleRuntimeConfiguration configuration) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration);
        return visitor.visit(node, null);
    }

    private static void setLocalExecutionForUnsetUserDefinedFunctions() {
        try {
            List<FunctionIdentifier> unsetFunctionIdentifiers = new ArrayList<>();
            unsetFunctionIdentifiers.addAll(
                Functions
                    .getUserDefinedFunctionIdentifiersWithUnsetExecutionModes()
            );
            for (
                FunctionIdentifier functionIdentifier : unsetFunctionIdentifiers
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
