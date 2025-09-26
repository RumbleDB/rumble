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


import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VariableReferenceExpression extends Expression implements Serializable {

    private static final long serialVersionUID = 1L;
    private Name name;
    private SequenceType type;


    public VariableReferenceExpression(Name name, ExceptionMetadata metadata) {
        super(metadata);
        this.name = name;
    }

    public Name getVariableName() {
        return this.name;
    }

    // default to item* if type is null
    public SequenceType getType() {
        return this.type == null ? SequenceType.ITEM_STAR : this.type;
    }

    public SequenceType getActualType() {
        return this.type;
    }

    public void setActualType(SequenceType type) {
        this.type = type;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitVariableReference(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" ($" + this.name + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append(
            " | "
                + (this.staticSequenceType == null
                    ? "not set"
                    : this.staticSequenceType
                        + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)"))
        );
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("($" + this.name + ")\n");
    }
}
