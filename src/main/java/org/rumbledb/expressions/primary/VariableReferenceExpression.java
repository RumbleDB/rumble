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


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VariableReferenceExpression extends Expression implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private SequenceType type;


    public VariableReferenceExpression(String name, ExceptionMetadata metadata) {
        super(metadata);
        this.name = name;
    }

    public void setHighestExecutionMode(ExecutionMode highestExecutionMode) {
        this.highestExecutionMode = highestExecutionMode;
    }

    public String getVariableName() {
        return this.name;
    }

    public SequenceType getType() {
        return this.type;
    }

    public void setType(SequenceType type) {
        this.type = type;
    }

    @Override
    public final void initHighestExecutionMode() {
        // Variable reference execution mode can only be resolved in conjunction with a static context
        // variable reference's execution mode gets initialized in staticContextVisitor by a setter
        throw new OurBadException("Variable references do not use the highestExecutionMode initializer");
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitVariableReference(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = (prefix ? "(primaryExpr " : "") + "(varRef $ " + this.name;
        result += (prefix ? ")" : "") + ")";
        return result;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }
}
