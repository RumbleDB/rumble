/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

/**
 * Converts an argument to a parameter type with the function conversion rules of a function call.
 * Function inlining uses it so that an inlined call converts its arguments exactly like the call it replaces.
 */
@Getter
public class FunctionArgumentConversionExpression extends Expression {

    private final Expression argument;
    private final SequenceType parameterType;
    private final Name functionName;

    public FunctionArgumentConversionExpression(
            Expression argument, SequenceType parameterType, Name functionName, ExceptionMetadata metadata) {
        super(metadata);
        this.argument = argument;
        this.parameterType = parameterType;
        this.functionName = functionName;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFunctionArgumentConversion(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        // Function conversion has no query syntax; it applies to the argument of the inlined call.
        this.argument.serializeToJSONiq(sb, indent);
    }
}
