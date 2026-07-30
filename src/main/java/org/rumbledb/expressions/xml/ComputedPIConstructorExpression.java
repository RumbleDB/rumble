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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression representing a computed processing instruction constructor.
 *
 * XQuery 3.1, 3.9.3.5: Computed Processing Instruction Constructors.
 * "A computed processing instruction constructor (CompPIConstructor) constructs a new processing instruction node with
 * its own node identity."
 * "If the keyword processing-instruction is followed by an NCName, that NCName is used as the target property of the
 * constructed node. If the keyword processing-instruction is followed by a name expression, the name expression is
 * processed as follows:"
 * Step 1: "Atomization is applied to the value of the name expression. If the result of atomization is not a single
 * atomic value of type xs:NCName, xs:string, or xs:untypedAtomic, a type error is raised [err:XPTY0004]."
 * Step 2: "If the atomized value of the name expression is of type xs:string or xs:untypedAtomic, that value is cast to
 * the type xs:NCName. If the value cannot be cast to xs:NCName, a dynamic error is raised [err:XQDY0041]."
 * Step 3: "The resulting NCName is then used as the target property of the newly constructed processing instruction
 * node. However, a dynamic error is raised if the NCName is equal to "XML" (in any combination of upper and lower case)
 * [err:XQDY0064]."
 * Content Step 1: "Atomization is applied to the value of the content expression, converting it to a sequence of atomic
 * values. (If the content expression is absent, the result of this step is an empty sequence.)"
 * Content Step 2: "If the result of atomization is an empty sequence, it is replaced by a zero-length string.
 * Otherwise, each atomic value in the atomized sequence is cast into a string. If any of the resulting strings contains
 * the string "?>", a dynamic error is raised [err:XQDY0026]."
 * Content Step 3: "The individual strings resulting from the previous step are merged into a single string by
 * concatenating them with a single space character between each pair. Leading whitespace is removed from the resulting
 * string. The resulting string then becomes the content property of the constructed processing instruction node."
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-computed-pis">XQuery 3.1, 3.9.3.5: Computed Processing Instruction
 *      Constructors</a>
 */
public class ComputedPIConstructorExpression extends Expression {
    /** The static processing instruction target (if specified). */
    private final String target;
    /** The dynamic name expression (if specified). */
    private final Expression nameExpression;
    /** The content expression. */
    private final Expression contentExpression;

    /**
     * Constructor for static target: processing-instruction NCName { content }
     *
     * @param target The static processing instruction target
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public ComputedPIConstructorExpression(
            String target,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.target = target;
        this.nameExpression = null;
        this.contentExpression = contentExpression;
    }

    /**
     * Constructor for dynamic target: processing-instruction { nameExpression } { content }
     *
     * @param nameExpression The dynamic name expression
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public ComputedPIConstructorExpression(
            Expression nameExpression,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.target = null;
        this.nameExpression = nameExpression;
        this.contentExpression = contentExpression;
    }

    public boolean hasStaticTarget() {
        return this.target != null;
    }

    public String getTarget() {
        return this.target;
    }

    public Expression getNameExpression() {
        return this.nameExpression;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitComputedPIConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.nameExpression != null) {
            result.add(this.nameExpression);
        }
        if (this.contentExpression != null) {
            result.add(this.contentExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("processing-instruction ");
        if (this.hasStaticTarget()) {
            sb.append(this.target);
        } else {
            sb.append("{ ");
            this.nameExpression.serializeToJSONiq(sb, 0);
            sb.append(" }");
        }
        sb.append(" { ");
        if (this.contentExpression != null) {
            this.contentExpression.serializeToJSONiq(sb, 0);
        }
        sb.append(" }\n");
    }
}

