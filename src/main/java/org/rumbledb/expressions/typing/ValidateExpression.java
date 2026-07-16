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
 */

package org.rumbledb.expressions.typing;

import java.util.List;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

/**
 * An XQuery 3.1 validate expression.
 *
 * This is intentionally separate from {@link ValidateTypeExpression}, which represents JSONiq sequence validation.
 */
public class ValidateExpression extends Expression {

    public enum ValidationMode {
        STRICT,
        LAX,
        TYPE
    }

    private final Expression mainExpression;
    private final ValidationMode validationMode;
    private final Name typeName;

    public ValidateExpression(
            Expression mainExpression,
            ValidationMode validationMode,
            Name typeName,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("The operand of a validate expression cannot be null.");
        }
        if (validationMode == null) {
            throw new OurBadException("The mode of a validate expression cannot be null.");
        }
        if ((validationMode == ValidationMode.TYPE) != (typeName != null)) {
            throw new OurBadException("A validate type expression must have exactly one type name.");
        }
        this.mainExpression = mainExpression;
        this.validationMode = validationMode;
        this.typeName = typeName;
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    public ValidationMode getValidationMode() {
        return this.validationMode;
    }

    public Name getTypeName() {
        return this.typeName;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitValidateExpression(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(this.mainExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("validate ");
        switch (this.validationMode) {
            case STRICT -> sb.append("strict ");
            case LAX -> sb.append("lax ");
            case TYPE -> sb.append("type ").append(this.typeName).append(" ");
        }
        sb.append("{\n");
        this.mainExpression.serializeToJSONiq(sb, indent + 1);
        sb.append("\n");
        indentIt(sb, indent);
        sb.append("}");
    }
}
