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
package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.List;


public class ForClauseVar extends FlworVarDecl {

    private final boolean allowEmpty;
    private final VariableReference positionalVariableReference;

    public ForClauseVar(VariableReference varRef, FlworVarSequenceType seq, boolean emptyFlag,
                        VariableReference atVarRef, Expression expression, ExpressionMetadata metadataFromContext) {
        super(FLWOR_CLAUSES.FOR_VAR, varRef, seq, expression, metadataFromContext);
        this.allowEmpty = emptyFlag;
        this.positionalVariableReference = atVarRef;
    }

    public boolean allowsEmpty() {
        return allowEmpty;
    }

    public VariableReference getPositionalVariableReference() {
        return positionalVariableReference;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = super.getDescendants(depthSearch);

        if (positionalVariableReference != null)
            result.add(positionalVariableReference);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitForClauseVar(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(forVar " + variableReferenceNode.serializationString(false) + " ";
        if (this.asSequence != null)
            result += "as " + asSequence.serializationString(true) + " ";
        if (allowEmpty)
            result += "allowing empty ";
        if (positionalVariableReference != null)
            result += "at " + positionalVariableReference.serializationString(false) + " ";
        result += "in " + this.expression.serializationString(true);
        result += "))";
        return result;
    }
}
