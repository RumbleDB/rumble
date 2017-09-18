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
 package jiqs.jsoniq.compiler.translator.expr.flowr;

import jiqs.semantics.types.SequenceType;
import jiqs.jsoniq.compiler.translator.expr.Expression;
import jiqs.jsoniq.compiler.translator.expr.ExpressionOrClause;
import jiqs.jsoniq.compiler.translator.expr.primary.VariableReference;

import java.util.ArrayList;
import java.util.List;

public abstract class FlworVarDecl extends FlworClause {

    public VariableReference getVariableReference() {
        return variableReferenceNode;
    }
    public Expression getExpression() {
        return expression;
    }
    public FlworVarSequenceType getAsSequence() {
        return asSequence;
    }


    private FlworVarDecl(FLWOR_CLAUSES clauseType) {
        super(clauseType);
    }


    public FlworVarDecl(FLWOR_CLAUSES forVar, VariableReference varRef,
                        FlworVarSequenceType seq, Expression expression) {
        this(forVar);
        if(varRef == null)
            throw new IllegalArgumentException("Flowr var decls cannot be empty");
        this.variableReferenceNode = varRef;
        this.asSequence = seq;
        this.expression = expression;

        //TODO add type inference?
        if(this.asSequence == null)
            this.variableReferenceNode.setType(new SequenceType());
        else
            this.variableReferenceNode.setType(this.asSequence.getSequence());
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(variableReferenceNode);
        if(asSequence !=null)
            result.add(asSequence);
        if(this.expression!=null)
            result.add(expression);
        return getDescendantsFromChildren(result,depthSearch);
    }



    protected VariableReference variableReferenceNode;
    protected Expression expression;
    protected FlworVarSequenceType asSequence;
}
