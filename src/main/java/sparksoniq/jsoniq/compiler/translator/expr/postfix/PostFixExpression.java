/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
package sparksoniq.jsoniq.compiler.translator.expr.postfix;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PostfixExtension;
import sparksoniq.jsoniq.compiler.translator.expr.primary.PrimaryExpression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;


public class PostFixExpression extends Expression {

    private PrimaryExpression _primaryExpressionNode;
    private List<PostfixExtension> _extensions = null;

    public PostFixExpression(PrimaryExpression primaryExpressionNode, ExpressionMetadata metadata) {
        super(metadata);
        this._primaryExpressionNode = primaryExpressionNode;
    }

    public PostFixExpression(PrimaryExpression primaryExpressionNode, List<PostfixExtension> extensions,
                             ExpressionMetadata metadata) {
        super(metadata);
        this._primaryExpressionNode = primaryExpressionNode;
        this._extensions = new ArrayList<>();
        this._extensions.addAll(extensions);
    }

    public boolean isPrimary() {
        return _extensions == null || _extensions.isEmpty();
    }

    public PrimaryExpression get_primaryExpressionNode() {
        return _primaryExpressionNode;
    }

    public List<PostfixExtension> getExtensions() {
        return _extensions;
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitPostfixExpression(this, argument);
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        result.add(this._primaryExpressionNode);
        if (this._extensions != null)
            _extensions.forEach(e -> {
                if (e != null)
                    result.add(e);
            });
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(postFixExpr ";
        result += get_primaryExpressionNode().serializationString(true);
        if (this._extensions != null && this._extensions.size() > 0) {
            for (PostfixExtension ext : this._extensions)
                result += " " + ext.serializationString(true)
                        + (_extensions.indexOf(ext) < _extensions.size() - 1 ? " " : "");
        }
        result += ")";
        return result;
    }

}
