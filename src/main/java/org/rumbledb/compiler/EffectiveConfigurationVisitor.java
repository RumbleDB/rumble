package org.rumbledb.compiler;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;

/**
 * Derives query-wide configuration adjustments from the AST without mutating the user configuration.
 */
public class EffectiveConfigurationVisitor extends AbstractNodeVisitor<RumbleConfiguration.RumbleConfigurationBuilder> {

    public RumbleConfiguration getEffectiveConfiguration(
            Node node, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        visit(node, builder);
        return builder.build();
    }

    @Override
    public RumbleConfiguration.RumbleConfigurationBuilder visitStepExpr(
            StepExpr stepExpr, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        /**
         * Check if the step expression requires parent pointers.
         */
        if (stepRequiresParentPointers(stepExpr)) {
            disableParentPointerOptimization(builder);
        }
        return builder;
    }

    @Override
    public RumbleConfiguration.RumbleConfigurationBuilder visitFunctionCall(
            FunctionCallExpression expression, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        if (functionRequiresParentPointers(expression.getFunctionIdentifier())) {
            disableParentPointerOptimization(builder);
        }
        return defaultAction(expression, builder);
    }

    @Override
    public RumbleConfiguration.RumbleConfigurationBuilder visitPathRootExpr(
            PathRootExpression expression, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        disableParentPointerOptimization(builder);
        return builder;
    }

    private static void disableParentPointerOptimization(RumbleConfiguration.RumbleConfigurationBuilder builder) {
        builder.configureOptimization(optimization -> optimization.optimizeParentPointers(false));
    }

    private static boolean stepRequiresParentPointers(StepExpr stepExpr) {
        if (stepExpr instanceof ForwardStepExpr) {
            ForwardAxis axis = ((ForwardStepExpr) stepExpr).getForwardAxis();
            return axis == ForwardAxis.FOLLOWING || axis == ForwardAxis.FOLLOWING_SIBLING;
        }
        return true;
    }

    private static boolean functionRequiresParentPointers(FunctionIdentifier identifier) {
        Name name = identifier.getName();
        String namespace = name.getNamespace();
        if (!Name.FN_NS.equals(namespace) && !Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(namespace)) {
            return false;
        }
        String localName = name.getLocalName();
        int arity = identifier.getArity();
        return ("lang".equals(localName) && (arity == 1 || arity == 2))
                || ("in-scope-prefixes".equals(localName) && arity == 1)
                || ("namespace-uri-for-prefix".equals(localName) && arity == 2)
                || ("serialize".equals(localName) && (arity == 1 || arity == 2))
                || ("innermost".equals(localName) && arity == 1)
                || ("outermost".equals(localName) && arity == 1)
                || ("id".equals(localName) && (arity == 1 || arity == 2))
                || ("idref".equals(localName) && (arity == 1 || arity == 2))
                || ("element-with-id".equals(localName) && (arity == 1 || arity == 2))
                || ("path".equals(localName) && (arity == 0 || arity == 1))
                || ("resolve-QName".equals(localName) && arity == 2);
    }
}
