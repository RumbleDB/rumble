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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

/**
 * This is a clause, which is a component of a FLWOR expression.
 *
 * Clauses, unlike expressions, return tuple streams.
 */
public abstract class Clause extends Node {

    /* Clauses are organized in doubly-linked lists */
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

    public ReturnClause detachInitialLetClauses() {
        if (this.nextClause != null) {
            throw new OurBadException("Detaching a let clause can only be done from the last clause");
        }
        if (!this.getClauseType().equals(FLWOR_CLAUSES.RETURN)) {
            throw new OurBadException("Detaching a let clause can only be done from a return clause");
        }
        ReturnClause returnClause = (ReturnClause) this;
        Clause lastLetClause = this.getFirstClause();
        if (!lastLetClause.getClauseType().equals(FLWOR_CLAUSES.LET)) {
            return returnClause;
        }
        if (
            !(lastLetClause.nextClause.getClauseType().equals(FLWOR_CLAUSES.LET)
                ||
                lastLetClause.nextClause.getClauseType().equals(FLWOR_CLAUSES.FOR))
        ) {
            return returnClause;
        }
        Clause newFirstClause = lastLetClause.nextClause;
        while (
            newFirstClause.getClauseType().equals(FLWOR_CLAUSES.LET)
                && (newFirstClause.nextClause.getClauseType().equals(FLWOR_CLAUSES.LET)
                    ||
                    newFirstClause.nextClause.getClauseType().equals(FLWOR_CLAUSES.FOR))
        ) {
            lastLetClause = lastLetClause.nextClause;
            newFirstClause = lastLetClause.nextClause;
        }
        for (Clause c = newFirstClause; c != null; c = c.nextClause) {
            if (c.getClauseType().equals(FLWOR_CLAUSES.GROUP_BY)) {
                // No optimization possible if there is a group by.
                System.err.println(
                    "[WARNING] It seems you are using a group by clause in a FLWOR expression that starts with a let clause. This is rather unusual and it might lead to surprises. We recommend always inserting a 'return' after a series of initial let clauses."
                );
                System.err.println("For example:");
                System.err.println();
                System.err.println("let $x := 1");
                System.err.println("let $y := $x + 1");
                System.err.println("let $z := $x + $y");
                System.err.println("return");
                System.err.println("  for $t in 1 to $z");
                System.err.println("  group by $m := $t mod 2");
                System.err.println("  return $m + $x");

                return returnClause;
            }
        }
        newFirstClause.previousClause = null;
        lastLetClause.nextClause = null;

        Expression returnExpr = new FlworExpression(
                returnClause,
                this.getMetadata()
        );
        returnClause = new ReturnClause(
                returnExpr,
                this.getMetadata()
        );
        lastLetClause.chainWith(returnClause);

        return returnClause;
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
    }

    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }

    public RuntimeStaticContext getStaticContextForRuntime(
            RumbleRuntimeConfiguration conf,
            VisitorConfig visitorConfig
    ) {
        return new RuntimeStaticContext(
                conf,
                getHighestExecutionMode(visitorConfig),
                getMetadata()
        );
    }
}
