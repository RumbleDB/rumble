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

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.SequenceType;

/**
 * An expression is the first-class citizen in JSONiq syntax. Any expression
 * returns a sequence of items.
 *
 * Expressions form a tree, but this tree may contain other nodes, such as clauses
 * and function declarations.
 *
 * An expression is associated with a static context containing information such as
 * the in-scope variables.
 */
public abstract class Expression extends Node {

    protected StaticContext staticContext;

    protected SequenceType staticSequenceType;

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
     * Sets the inferred static type, for used by the static
     * analysis visitor.
     * 
     * @param staticSequenceType the statically inferred sequence type to set.
     */
    public void setStaticSequenceType(SequenceType staticSequenceType) {
        this.staticSequenceType = staticSequenceType;
    }

    @Override
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + (this.staticSequenceType == null ? "not set" : this.staticSequenceType));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }
}
