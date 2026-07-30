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


import lombok.Getter;
import lombok.Setter;
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
@Getter
public abstract class Expression extends Node {

    /**
     * Static context attached to this expression
     */
    @Setter
    protected StaticContext staticContext;

    /**
     * Statically inferred sequence type.
     */
    @Setter
    protected SequenceType staticSequenceType;

    /**
     * Expression Classification of the expression.
     */
    @Setter
    protected ExpressionClassification expressionClassification = ExpressionClassification.UNSET;

    protected boolean isSequential;

    protected Expression(ExceptionMetadata metadata) {
        super(metadata);
    }

    public RuntimeStaticContext getStaticContextForRuntime(
            RumbleRuntimeConfiguration conf,
            VisitorConfig visitorConfig
    ) {
        return RuntimeStaticContext.fromStaticContext(this.staticContext)
            .configuration(conf)
            .staticType(getStaticSequenceType())
            .executionMode(getHighestExecutionMode(visitorConfig))
            .metadata(getMetadata())
            .isUpdating(isUpdating())
            .isSequential(isSequential())
            .build();
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
     * 
     * <ul>
     * <li>non-updating sequential,</li>
     * <li>non-updating non-sequential,</li>
     * <li>updating non-sequential.</li>
     * </ul>
     *
     * @param isSequential a boolean value defining if the expression is
     *        sequential or not.
     * @throws InvalidExpressionClassification if the expression is both
     *         updating and sequential.
     */
    public void setSequential(boolean isSequential) {
        this.isSequential = isSequential;
        if (isSequential) {
            setIsInSequentialBlock(true);
        }
    }

    @Override
    public void setIsInSequentialBlock(boolean isInSequentialBlock) {
        this.isInSequentialBlock = isInSequentialBlock;
        for (Node child : getChildren()) {
            child.setIsInSequentialBlock(isInSequentialBlock);
        }
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
    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        if (this.isSequential) {
            buffer.append(" | " + "sequential");
        } else {
            buffer.append(" | " + "non-sequential");
        }
        if (this.isInSequentialBlock) {
            buffer.append(" | " + "in sequential block");
        } else {
            buffer.append(" | " + "not in sequential block");
        }
        buffer.append(
            " | "
                + (this.staticSequenceType == null
                    ? "not set"
                    : this.staticSequenceType
                        + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)"))
        );
        buffer.append(
            " | "
                + (this.getStaticContext() != null && this.getStaticContext().isQuerySideEffecting()
                    ? "query side effecting"
                    : "query without side effects")
        );
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }
}
