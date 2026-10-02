/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler.translation.xml;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import org.rumbledb.compiler.context.xml.AxisStepContext;
import org.rumbledb.compiler.context.xml.ForwardStepContext;
import org.rumbledb.compiler.context.xml.PathExprContext;
import org.rumbledb.compiler.context.xml.RelativePathExprContext;
import org.rumbledb.compiler.context.xml.ReverseStepContext;
import org.rumbledb.compiler.context.xml.StepExprContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.SchemaNodeTest;
import org.rumbledb.types.AttributeNodeItemType;

public final class XmlPathTranslation {

    private XmlPathTranslation() {}

    public static <StepExprCtx extends ParserRuleContext> Expression pathExpr(
            PathExprContext<StepExprCtx> ctx,
            TranslationContext translationContext,
            Function<StepExprCtx, Expression> visitStepExpr) {
        if (ctx.singleslash() != null) {
            Token leadingSlash = ctx.context().getStart();
            PathRootExpression root = new PathRootExpression(translationContext.metadata(leadingSlash, leadingSlash));
            return relativePathExpr(ctx.singleslash(), root, leadingSlash, translationContext, visitStepExpr);
        } else if (ctx.doubleslash() != null) {
            Token leadingDoubleSlash = ctx.context().getStart();
            PathRootExpression root =
                    new PathRootExpression(translationContext.metadata(leadingDoubleSlash, leadingDoubleSlash));
            StepExpr stepExpr = new ForwardStepExpr(
                    ForwardAxis.DESCENDANT_OR_SELF,
                    new AnyKindTest(),
                    translationContext.metadata(leadingDoubleSlash, leadingDoubleSlash));
            Expression starter =
                    new SlashExpr(root, stepExpr, translationContext.metadata(leadingDoubleSlash, leadingDoubleSlash));
            return relativePathExpr(ctx.doubleslash(), starter, leadingDoubleSlash, translationContext, visitStepExpr);
        } else if (ctx.relative() != null) {
            return relativePathExpr(ctx.relative(), null, null, translationContext, visitStepExpr);
        }
        return new PathRootExpression(translationContext.metadata(ctx.context()));
    }

    public static <StepExprCtx extends ParserRuleContext> Expression relativePathExpr(
            RelativePathExprContext<StepExprCtx> ctx,
            Expression leftMost,
            Token expressionStart,
            TranslationContext translationContext,
            Function<StepExprCtx, Expression> visitStepExpr) {
        Token start = expressionStart != null ? expressionStart : ctx.context().getStart();
        Expression currentTop = leftMost;
        for (int i = 0; i < ctx.stepExpr().size(); ++i) {
            StepExprCtx stepCtx = ctx.stepExpr().get(i);
            Expression currentStepExpr = visitStepExpr.apply(stepCtx);
            if (i > 0 && ctx.sep().get(i - 1).getText().equals("//")) {
                Token sepToken = ctx.sep().get(i - 1);
                StepExpr intermediaryStepExpr = new ForwardStepExpr(
                        ForwardAxis.DESCENDANT_OR_SELF,
                        new AnyKindTest(),
                        translationContext.metadata(sepToken, sepToken));
                if (currentTop == null) {
                    currentTop = intermediaryStepExpr;
                } else {
                    currentTop = new SlashExpr(
                            currentTop, intermediaryStepExpr, translationContext.metadata(start, sepToken));
                }
            }
            if (currentTop == null) {
                currentTop = currentStepExpr;
            } else {
                currentTop = new SlashExpr(
                        currentTop, currentStepExpr, translationContext.metadata(start, stepCtx.getStop()));
            }
        }
        return currentTop;
    }

    public static <
                    PostfixExprCtx extends ParserRuleContext,
                    NodeTestCtx extends ParserRuleContext,
                    PredicateCtx extends ParserRuleContext>
            Expression stepExpr(
                    StepExprContext<PostfixExprCtx, NodeTestCtx, PredicateCtx> ctx,
                    TranslationContext translationContext,
                    Function<PostfixExprCtx, Expression> visitPostfixExpr,
                    BiFunction<NodeTestCtx, Boolean, NodeTest> getNodeTest,
                    Function<PredicateCtx, Expression> visitPredicate) {
        if (ctx.postfixExpr() != null) {
            return visitPostfixExpr.apply(ctx.postfixExpr());
        }
        return axisStep(ctx.axisStep(), translationContext, getNodeTest, visitPredicate);
    }

    public static <NodeTestCtx extends ParserRuleContext, PredicateCtx extends ParserRuleContext> Expression axisStep(
            AxisStepContext<NodeTestCtx, PredicateCtx> ctx,
            TranslationContext translationContext,
            BiFunction<NodeTestCtx, Boolean, NodeTest> getNodeTest,
            Function<PredicateCtx, Expression> visitPredicate) {
        StepExpr stepExpr = ctx.forwardStep() != null
                ? forwardStep(ctx.forwardStep(), translationContext, getNodeTest)
                : reverseStep(ctx.reverseStep(), translationContext, getNodeTest);
        Expression result = stepExpr;
        for (PredicateCtx predicateCtx : ctx.predicates()) {
            Expression predicate = visitPredicate.apply(predicateCtx);
            result = new FilterExpression(
                    result, predicate, translationContext.metadata(ctx.context().getStart(), predicateCtx.getStop()));
        }
        return result;
    }

    public static <NodeTestCtx extends ParserRuleContext> StepExpr forwardStep(
            ForwardStepContext<NodeTestCtx> ctx,
            TranslationContext translationContext,
            BiFunction<NodeTestCtx, Boolean, NodeTest> getNodeTest) {
        ForwardAxis forwardAxis;
        NodeTest nodeTest;
        if (ctx.forwardAxisText() == null) {
            // Abbreviated step: unprefixed names use default element namespace on child axis, not on @attr.
            boolean unprefixedUsesDefaultElementNs = !ctx.isAt();
            nodeTest = getNodeTest.apply(ctx.nodeTest(), unprefixedUsesDefaultElementNs);
            if (ctx.isAt()) {
                // @ equivalent with 'attribute::'
                forwardAxis = ForwardAxis.ATTRIBUTE;
            } else if (nodeTest instanceof AttributeTest
                    || (nodeTest instanceof SchemaNodeTest schemaTest
                            && schemaTest.itemType() instanceof AttributeNodeItemType)) {
                forwardAxis = ForwardAxis.ATTRIBUTE;
            } else {
                forwardAxis = ForwardAxis.CHILD;
            }
            return new ForwardStepExpr(forwardAxis, nodeTest, translationContext.metadata(ctx.context()));
        }
        forwardAxis = ForwardAxis.fromString(ctx.forwardAxisText());
        boolean unprefixedUsesDefaultElementNs = forwardAxis != ForwardAxis.ATTRIBUTE;
        nodeTest = getNodeTest.apply(ctx.nodeTest(), unprefixedUsesDefaultElementNs);
        return new ForwardStepExpr(forwardAxis, nodeTest, translationContext.metadata(ctx.context()));
    }

    public static <NodeTestCtx extends ParserRuleContext> StepExpr reverseStep(
            ReverseStepContext<NodeTestCtx> ctx,
            TranslationContext translationContext,
            BiFunction<NodeTestCtx, Boolean, NodeTest> getNodeTest) {
        if (ctx.nodeTest() == null) {
            // .. equivalent with 'parent::node()'
            ReverseAxis reverseAxis = ReverseAxis.PARENT;
            NodeTest nodeTest = new AnyKindTest();
            return new ReverseStepExpr(reverseAxis, nodeTest, translationContext.metadata(ctx.context()));
        }
        ReverseAxis reverseAxis = ReverseAxis.fromString(ctx.reverseAxisText());
        // Reverse axes only match element (and similar) nodes; unprefixed QNames use default element namespace.
        NodeTest nodeTest = getNodeTest.apply(ctx.nodeTest(), true);
        return new ReverseStepExpr(reverseAxis, nodeTest, translationContext.metadata(ctx.context()));
    }
}
