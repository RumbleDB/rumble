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

package org.rumbledb.expressions.module;


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.types.SequenceType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static org.rumbledb.expressions.scripting.annotations.AnnotationConstants.ASSIGNABLE;
import static org.rumbledb.expressions.scripting.annotations.AnnotationConstants.BUILT_IN_ANNOTATION;
import static org.rumbledb.expressions.scripting.annotations.AnnotationConstants.NON_ASSIGNABLE;

public class VariableDeclaration extends Node {

    private final Name variableName;
    private final boolean external;
    protected SequenceType sequenceType;
    protected Expression expression;
    private final List<Annotation> annotations;
    private boolean isAssignable = false;

    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;

    public VariableDeclaration(
            Name variableName,
            boolean external,
            SequenceType sequenceType,
            Expression expression,
            List<Annotation> annotations,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.variableName = variableName;
        this.external = external;
        this.sequenceType = sequenceType;
        this.expression = expression;
        this.annotations = annotations;
        if (!this.external && this.expression == null) {
            throw new OurBadException("If a variable is not external, an expression must be provided.");
        }
        if (this.annotations != null) {
            checkAssignable();
        }
    }

    private void checkAssignable() {
        // TODO: Is breaking early safe?
        for (Annotation annotation : this.annotations) {
            if (annotation.getAnnotationName().getPrefix().equals(BUILT_IN_ANNOTATION)) {
                if (annotation.getAnnotationName().getLocalName().equals(ASSIGNABLE)) {
                    isAssignable = true;
                    break;
                } else if (annotation.getAnnotationName().getLocalName().equals(NON_ASSIGNABLE)) {
                    isAssignable = false;
                    break;
                }
            }
        }
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public boolean external() {
        return this.external;
    }

    // return item* if sequenceType is [null]
    public SequenceType getSequenceType() {
        if (this.sequenceType != null) {
            return this.sequenceType;
        }
        if (this.expression != null && this.expression.getStaticSequenceType() != null) {
            return this.expression.getStaticSequenceType();
        }
        return SequenceType.ITEM_STAR;
    }

    // as above but does NOT default to item*
    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public List<Node> getChildren() {
        if (this.expression != null) {
            return Collections.singletonList(this.expression);
        }
        return Collections.emptyList();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitVariableDeclaration(this, argument);
    }

    public ExecutionMode getVariableHighestStorageMode(VisitorConfig visitorConfig) {
        if (
            !visitorConfig.suppressErrorsForAccessingUnsetExecutionModes()
                && this.variableHighestStorageMode == ExecutionMode.UNSET
        ) {
            throw new OurBadException("A variable storage mode is accessed without being set.");
        }
        return this.variableHighestStorageMode;
    }

    public void setVariableHighestStorageMode(ExecutionMode mode) {
        this.variableHighestStorageMode = mode;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(
            " ("
                + (this.variableName)
                + ", "
                + (this.external ? "external, " : "")
                + this.getSequenceType().toString()
                + ") "
        );
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("declare variable $" + this.variableName);
        if (this.sequenceType != null)
            sb.append(" as " + this.sequenceType.toString());
        if (this.external)
            sb.append(" external\n");
        else {
            sb.append(" ");
            this.expression.serializeToJSONiq(sb, 0);
            sb.append("\n");
        }
    }

    @Nullable
    public List<Annotation> getAnnotations() {
        return annotations;
    }


    public boolean isAssignable() {
        return isAssignable;
    }
}

