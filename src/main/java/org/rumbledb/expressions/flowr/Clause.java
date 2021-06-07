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

package org.rumbledb.expressions.flowr;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Node;

/**
 * This is a clause, which is a component of a FLWOR expression.
 *
 * Clauses, unlike expressions, return tuple streams.
 */
public abstract class Clause extends Node {

    /* Clauses are organized in doubly-linked lists */
    private static final long serialVersionUID = 1L;

    protected Clause previousClause;
    protected Clause nextClause;
    protected FLWOR_CLAUSES clauseType;
    protected StaticContext staticContext;

    public Clause(FLWOR_CLAUSES clauseType, ExceptionMetadata metadata) {
        super(metadata);
        this.clauseType = clauseType;
        this.staticContext = null;
        this.previousClause = null;
        this.nextClause = null;
    }

    public FLWOR_CLAUSES getClauseType() {
        return this.clauseType;
    }

    public Clause getPreviousClause() {
        return this.previousClause;
    }

    public Clause getNextClause() {
        return this.nextClause;
    }

    /**
     * This method is overridden in clauses such as for and let for special behavior
     */
    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = this.previousClause.getHighestExecutionMode(visitorConfig);
    }

    public Clause getFirstClause() {
        Clause result = this;
        while (result.previousClause != null) {
            result = result.previousClause;
        }
        return result;
    }

    public Clause getLastClause() {
        Clause result = this;
        while (result.nextClause != null) {
            result = result.nextClause;
        }
        return result;
    }

    public void chainWith(Clause otherClause) {
        if (this.nextClause != null) {
            throw new OurBadException("Previous clause already chained!");
        }
        if (otherClause.previousClause != null) {
            throw new OurBadException("Next clause already chained!");
        }
        this.nextClause = otherClause;
        otherClause.previousClause = this;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
        if (this.previousClause != null) {
            this.previousClause.print(buffer, indent + 1);
        }
    }

    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }
}
