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
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public abstract class FlworVarDecl extends Clause {

    protected VariableReferenceExpression variableReferenceExpression;
    protected Expression expression;

    protected SequenceType sequenceType;

    // Holds whether the variable will be stored in materialized(local) or native/spark(RDD or DF) format in a tuple
    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;

    private FlworVarDecl(FLWOR_CLAUSES clauseType, ExceptionMetadata metadata) {
        super(clauseType, metadata);
    }

    public FlworVarDecl(
            FLWOR_CLAUSES forVar,
            VariableReferenceExpression variableReferenceExpression,
            SequenceType sequenceType,
            Expression expression,
            ExceptionMetadata metadata
    ) {
        this(forVar, metadata);
        if (variableReferenceExpression == null) {
            throw new IllegalArgumentException("Flowr var decls cannot be empty");
        }
        this.variableReferenceExpression = variableReferenceExpression;
        this.sequenceType = sequenceType;
        if (this.sequenceType == null) {
            throw new OurBadException("A sequence type cannot be null");
        }
        this.expression = expression;

        this.variableReferenceExpression.setType(this.sequenceType);
    }

    public VariableReferenceExpression getVariableReference() {
        return this.variableReferenceExpression;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
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
        result.add(this.variableReferenceExpression);
        if (this.expression != null) {
            result.add(this.expression);
        }
        return result;
    }
}
