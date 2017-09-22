/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler.translator.expr.quantifiers;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpressionVar extends ExpressionOrClause {
    public Expression getExpression() {
        return _expression;
    }

    public VariableReference getVariableReference() {

        return _variableReference;
    }


    public SequenceType getSequenceType() {
        return _sequenceType;
    }

    public QuantifiedExpressionVar(VariableReference varRef, Expression varExpression, SequenceType sequenceType) {
        this._variableReference = varRef;
        this._expression = varExpression;
        this._sequenceType = sequenceType;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(_variableReference);
        result.add(_expression);
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitQuantifiedExpressionVar(this,argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        return null;
    }

    private final VariableReference _variableReference;
    private final Expression _expression;
    private final SequenceType _sequenceType;
}
