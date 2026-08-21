/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rumbledb.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.parser.rumble.RumbleParser;
import org.rumbledb.parser.rumble.RumbleParserBaseVisitor;

/** Shared translation logic for parse-tree contexts produced by {@link RumbleParser}. */
public abstract class SharedTranslationVisitor extends RumbleParserBaseVisitor<Node> {

    protected abstract boolean supportsJsoniqPostfixSyntax();

    protected abstract ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx);

    protected abstract ExceptionMetadata createMetadataFromRange(Token start, Token end);

    @Override
    public Node visitJsoniqPrimaryExpr(RumbleParser.JsoniqPrimaryExprContext ctx) {
        return visitPrimaryExpression(ctx);
    }

    @Override
    public Node visitXqueryPrimaryExpr(RumbleParser.XqueryPrimaryExprContext ctx) {
        return visitPrimaryExpression(ctx);
    }

    private Node visitPrimaryExpression(ParserRuleContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof RumbleParser.VarRefContext varRefContext) {
            return this.visitVarRef(varRefContext);
        }
        if (child instanceof RumbleParser.JsoniqObjectConstructorContext objectConstructorContext) {
            return this.visitJsoniqObjectConstructor(objectConstructorContext);
        }
        if (child instanceof RumbleParser.XqueryObjectConstructorContext objectConstructorContext) {
            return this.visitXqueryObjectConstructor(objectConstructorContext);
        }
        if (child instanceof RumbleParser.ArrayConstructorContext arrayConstructorContext) {
            return this.visitArrayConstructor(arrayConstructorContext);
        }
        if (child instanceof RumbleParser.ParenthesizedExprContext parenthesizedExprContext) {
            return this.visitParenthesizedExpr(parenthesizedExprContext);
        }
        if (child instanceof RumbleParser.LiteralContext literalContext) {
            return this.visitLiteral(literalContext);
        }
        if (child instanceof RumbleParser.JsoniqContextItemExprContext contextItemExprContext) {
            return this.visitJsoniqContextItemExpr(contextItemExprContext);
        }
        if (child instanceof RumbleParser.XqueryContextItemExprContext contextItemExprContext) {
            return this.visitXqueryContextItemExpr(contextItemExprContext);
        }
        if (child instanceof RumbleParser.FunctionCallContext functionCallContext) {
            return this.visitFunctionCall(functionCallContext);
        }
        if (child instanceof RumbleParser.FunctionItemExprContext functionItemExprContext) {
            return this.visitFunctionItemExpr(functionItemExprContext);
        }
        if (child instanceof RumbleParser.BlockExprContext blockExprContext) {
            return this.visitBlockExpr(blockExprContext);
        }
        if (child instanceof RumbleParser.UnaryLookupContext unaryLookupContext) {
            return new UnaryLookupExpression(
                    (Expression) this.visitUnaryLookup(unaryLookupContext), createMetadataFromContext(ctx));
        }
        if (child instanceof RumbleParser.NodeConstructorContext nodeConstructorContext) {
            return this.visitNodeConstructor(nodeConstructorContext);
        }
        if (child instanceof TerminalNode terminalNode) {
            return getLiteralExpressionFromToken(terminalNode.getText(), createMetadataFromContext(ctx));
        }
        throw new UnsupportedFeatureException("Primary expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitPostfixExpr(RumbleParser.PostfixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof RumbleParser.PredicateContext predicateContext) {
                Expression expression = (Expression) this.visitPredicate(predicateContext);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expression,
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop()));
            } else if (child instanceof RumbleParser.LookupContext lookupContext) {
                Expression expression = (Expression) this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expression,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop()));
            } else if (child instanceof RumbleParser.ArgumentListContext argumentListContext) {
                mainExpression = new DynamicFunctionCallExpression(
                        mainExpression,
                        getArgumentsFromArgumentListContext(argumentListContext),
                        createMetadataFromRange(ctx.main_expr.getStart(), argumentListContext.getStop()));
            } else if (this.supportsJsoniqPostfixSyntax()
                    && child instanceof RumbleParser.ObjectLookupContext objectLookupContext) {
                Expression expression = (Expression) this.visitObjectLookup(objectLookupContext);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expression,
                        createMetadataFromRange(ctx.main_expr.getStart(), objectLookupContext.getStop()));
            } else if (this.supportsJsoniqPostfixSyntax()
                    && child instanceof RumbleParser.ArrayLookupContext arrayLookupContext) {
                Expression expression = (Expression) this.visitArrayLookup(arrayLookupContext);
                mainExpression = new ArrayLookupExpression(
                        mainExpression,
                        expression,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayLookupContext.getStop()));
            } else if (this.supportsJsoniqPostfixSyntax()
                    && child instanceof RumbleParser.ArrayUnboxingContext arrayUnboxingContext) {
                this.visitArrayUnboxing(arrayUnboxingContext);
                mainExpression = new ArrayUnboxingExpression(
                        mainExpression,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayUnboxingContext.getStop()));
            } else if (child instanceof RumbleParser.ObjectLookupContext
                    || child instanceof RumbleParser.ArrayLookupContext
                    || child instanceof RumbleParser.ArrayUnboxingContext) {
                throw new ParsingException(
                        "JSONiq postfix expressions are not part of XQuery.",
                        createMetadataFromContext((ParserRuleContext) child));
            } else {
                throw new OurBadException("Unrecognized postfix expression found.");
            }
        }
        return mainExpression;
    }

    protected final List<Expression> getArgumentsFromArgumentListContext(RumbleParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (RumbleParser.ArgumentContext argument : ctx.args) {
                arguments.add((Expression) this.visitArgument(argument));
            }
        }
        return arguments;
    }

    protected final Expression getLiteralExpressionFromToken(String token, ExceptionMetadata metadata) {
        switch (token) {
            case "null":
                return new NullLiteralExpression(metadata);
            case "true":
                return new BooleanLiteralExpression(true, metadata);
            case "false":
                return new BooleanLiteralExpression(false, metadata);
            default:
        }
        if (token.contains("E") || token.contains("e")) {
            return new DoubleLiteralExpression(Double.parseDouble(token), metadata);
        }
        if (token.contains(".")) {
            return new DecimalLiteralExpression(new BigDecimal(token), metadata);
        }
        return new IntegerLiteralExpression(token, metadata);
    }
}
