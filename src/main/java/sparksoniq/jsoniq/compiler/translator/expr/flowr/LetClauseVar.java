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

package sparksoniq.jsoniq.compiler.translator.expr.flowr;


import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class LetClauseVar extends FlworVarDecl {

    public LetClauseVar(
            VariableReference varRef,
            FlworVarSequenceType sequence,
            Expression expr,
            ExpressionMetadata metadataFromContext
    ) {
        super(FLWOR_CLAUSES.LET_VAR, varRef, sequence, expr, metadataFromContext);
    }

    @Override
    protected void initIsRDD() {
        initializeVariableIsRDDIsDataFrame();
        if (this.previousClause == null) {
            this.isDataFrame = false;
        } else {
            this.isDataFrame = previousClause.isDataFrame();
        }
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitLetClauseVar(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(letVar " + variableReferenceNode.serializationString(false) + " ";
        if (this.asSequence != null)
            result += "as " + asSequence.serializationString(true) + " ";
        result += ":= " + this.expression.serializationString(true);
        result += "))";
        return result;
    }

}

