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

package org.rumbledb.expressions.flowr;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public abstract class FlworVarDecl extends FlworClause {

    protected VariableReferenceExpression variableReferenceNode;
    protected Expression expression;

    // asSequence is null by default if the type of the variable in the for/let/groupBy clause is not specified.
    protected FlworVarSequenceType asSequence;

    // Holds whether the variable will be stored in materialized(local) or native/spark(RDD or DF) format in a tuple
    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;

    private FlworVarDecl(FLWOR_CLAUSES clauseType, ExceptionMetadata metadata) {
        super(clauseType, metadata);
    }

    public FlworVarDecl(
            FLWOR_CLAUSES forVar,
            VariableReferenceExpression varRef,
            FlworVarSequenceType seq,
            Expression expression,
            ExceptionMetadata metadata
    ) {
        this(forVar, metadata);
        if (varRef == null)
            throw new IllegalArgumentException("Flowr var decls cannot be empty");
        this.variableReferenceNode = varRef;
        this.asSequence = seq;
        this.expression = expression;

        // TODO add type inference?
        if (this.asSequence == null)
            this.variableReferenceNode.setType(new SequenceType());
        else
            this.variableReferenceNode.setType(this.asSequence.getSequence());
    }

    public VariableReferenceExpression getVariableReference() {
        return this.variableReferenceNode;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public FlworVarSequenceType getAsSequence() {
        return this.asSequence;
    }

    @Override
    public final void initHighestExecutionMode() {
        throw new OurBadException("Flwor variable declarations do not use the highestExecutionMode initializer");
    }

    public abstract void initHighestExecutionAndVariableHighestStorageModes();

    public ExecutionMode getVariableHighestStorageMode() {
        return this.variableHighestStorageMode;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.variableReferenceNode);
        if (this.asSequence != null)
            result.add(this.asSequence);
        if (this.expression != null)
            result.add(this.expression);
        return result;
    }
}
