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

package org.rumbledb.expressions;


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidExpressionClassification;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

/**
 * An expression is the first-class citizen in JSONiq syntax. Any expression
 * returns a sequence of items.
 *
 * Expressions form a tree, but this tree may contain other nodes, such as clauses
 * and function declarations.
 *
 * An expression is associated with a static context containing information such as
 * the in-scope variables.
 *
 * An expression has a classification, largely denoting it as UPDATING or SIMPLE.
 */
public abstract class Expression extends Node {

    protected StaticContext staticContext;

    protected SequenceType staticSequenceType;

    protected ExpressionClassification expressionClassification = ExpressionClassification.UNSET;

    protected boolean isSequential;

    protected Expression(ExceptionMetadata metadata) {
        super(metadata);
    }

    /**
     * Retrieves the static context attached to this expression.
     * 
     * @return the static context.
     */
    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    /**
     * Sets the static context of the expression.
     * 
     * @param staticContext the static context to set.
     */
    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }

    public RuntimeStaticContext getStaticContextForRuntime(
            RumbleRuntimeConfiguration conf,
            VisitorConfig visitorConfig
    ) {
        return new RuntimeStaticContext(
                conf,
                getStaticSequenceType(),
                getHighestExecutionMode(visitorConfig),
                getMetadata()
        );
    }

    /**
     * Provides the inferred static type, only if static analysis
     * is activated.
     * 
     * @return the statically inferred sequence type.
     */
    public SequenceType getStaticSequenceType() {
        return this.staticSequenceType;
    }

    /**
     * Tells whether this expression is guaranteed to return
     * zero or one item but not more.
     *
     * @return true if yes, false otherwise.
     */
    public boolean alwaysReturnsAtMostOneItem() {
        return this.staticSequenceType.getArity().equals(Arity.One)
            ||
            this.staticSequenceType.getArity().equals(Arity.OneOrZero)
            ||
            this.staticSequenceType.getArity().equals(Arity.Zero);
    }

    /**
     * Sets the inferred static type, for used by the static
     * analysis visitor.
     * 
     * @param staticSequenceType the statically inferred sequence type to set.
     */
    public void setStaticSequenceType(SequenceType staticSequenceType) {
        this.staticSequenceType = staticSequenceType;
    }

    /**
     * Gets the inferred expression classification of this node, for use ...
     *
     * @return Expression Classification of the expression.
     */
    public ExpressionClassification getExpressionClassification() {
        return this.expressionClassification;
    }

    /**
     * Sets the inferred expression classification of this node, for use ...
     *
     * @param expressionClassification the statically inferred expression classification.
     */
    public void setExpressionClassification(ExpressionClassification expressionClassification) {
        this.expressionClassification = expressionClassification;
    }

    /**
     * Tells whether this node is an updating expression or not.
     *
     * @return true if yes, false otherwise.
     */
    public boolean isUpdating() {
        return this.expressionClassification.isUpdating();
    }

    /**
     * Tells whether this node has an unset expression classification.
     *
     * @return true if yes, false otherwise.
     */
    public boolean isUnset() {
        return this.expressionClassification.isUnset();
    }

    /**
     * Sets the sequential property of the expression. An expression can only
     * be one of the following:
     * - non-updating sequential,
     * - non-updating non-sequential,
     * - updating non-sequential.
     *
     * @param isSequential a boolean value defining if the expression is
     *        sequential or not.
     * @throws InvalidExpressionClassification if the expression is both
     *         updating and sequential.
     */
    public void setSequential(boolean isSequential) {
        this.isSequential = isSequential;
    }

    public boolean isSequential() {
        return this.isSequential;
    }

    /**
     * Returns true if this node is updating and non-sequential.
     *
     * @return true if updating and non-sequential, false otherwise.
     */
    public boolean isUpdatingNonSequential() {
        return isUpdating() && !this.isSequential;
    }


    @Override
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
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
}
