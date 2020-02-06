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

package org.rumbledb.expressions.primary;


import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.io.Serializable;

import org.rumbledb.exceptions.OurBadException;

public class VariableReference extends PrimaryExpression implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private SequenceType _type;


    public VariableReference(String _name, ExpressionMetadata metadata) {
        super(metadata);
        this.name = _name;
    }

    public void setHighestExecutionMode(ExecutionMode highestExecutionMode) {
        this._highestExecutionMode = highestExecutionMode;
    }

    public String getVariableName() {
        return name;
    }

    public SequenceType getType() {
        return _type;
    }

    public void setType(SequenceType type) {
        this._type = type;
    }

    @Override
    public final void initHighestExecutionMode() {
        // Variable reference execution mode can only be resolved in conjunction with a static context
        // variable reference's execution mode gets initialized in staticContextVisitor by a setter
        throw new OurBadException("Variable references do not use the highestExecutionMode initializer");
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitVariableReference(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = (prefix ? "(primaryExpr " : "") + "(varRef $ " + name;
        result += (prefix ? ")" : "") + ")";
        return result;
    }
}
