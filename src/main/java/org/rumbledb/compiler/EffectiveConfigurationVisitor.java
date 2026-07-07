package org.rumbledb.compiler;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;

/**
 * Derives query-wide configuration adjustments from the AST without mutating the user configuration.
 */
public class EffectiveConfigurationVisitor extends AbstractNodeVisitor<RumbleConfiguration.RumbleConfigurationBuilder> {

    public RumbleConfiguration getEffectiveConfiguration(
            Node node,
            RumbleConfiguration.RumbleConfigurationBuilder builder
    ) {
        visit(node, builder);
        return builder.build();
    }

    @Override
    public RumbleConfiguration.RumbleConfigurationBuilder visitStepExpr(
            StepExpr stepExpr,
            RumbleConfiguration.RumbleConfigurationBuilder builder
    ) {
        /**
         * Check if the step expression requires parent pointers.
         */
        if (requiresParentPointers(stepExpr)) {
            builder.configureOptimization(optimization -> optimization.optimizeParentPointers(false));
        }
        return builder;
    }

    private static boolean requiresParentPointers(StepExpr stepExpr) {
        if (stepExpr instanceof ForwardStepExpr) {
            ForwardAxis axis = ((ForwardStepExpr) stepExpr).getForwardAxis();
            return axis == ForwardAxis.FOLLOWING || axis == ForwardAxis.FOLLOWING_SIBLING;
        }
        return true;
    }
}
