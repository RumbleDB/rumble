/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.compiler;

import java.util.List;

import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

/** Rewrites partial applications of constructor functions supplied by imported XML Schema types. */
final class XmlSchemaConstructorPartialApplicationRewriteVisitor
        extends
            CloneVisitor {

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        XmlSchemaConstructorFunction constructor = XmlSchemaConstructorFunction.resolve(
            expression.getFunctionIdentifier(),
            expression.getStaticContext()
        );
        if (!expression.isPartialApplication() || constructor == null) {
            return super.visitFunctionCall(expression, argument);
        }
        List<Expression> arguments = expression.getArguments()
            .stream()
            .map(value -> value == null ? null : (Expression) visit(value, argument))
            .toList();
        return new DynamicFunctionCallExpression(
                new NamedFunctionReferenceExpression(
                        expression.getFunctionIdentifier(),
                        expression.getMetadata()
                ),
                arguments,
                expression.getMetadata()
        );
    }
}
