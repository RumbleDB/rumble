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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.compiler;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;

public class ContextItemSetterVisitor extends AbstractNodeVisitor<Expression> {
    
    private Name variableName;

    public ContextItemSetterVisitor(Name variableName) {
        this.variableName = variableName;
    }

    @Override
    public Expression visit(Node node, Expression argument) {
        return node.accept(this, argument);
    }
    
    // TODO recursively copy all expressions.

    @Override
    public Expression visitVariableReference(VariableReferenceExpression expression, Expression argument) {
        if(expression.getVariableName().equals(this.variableName))
        {
            return new ContextItemExpression(expression.getMetadata());
        }
        return expression;
    }
}
