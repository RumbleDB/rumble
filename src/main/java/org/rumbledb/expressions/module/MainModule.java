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


import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

import java.util.ArrayList;
import java.util.List;

public class MainModule extends Module {

    protected StaticContext staticContext;
    private final Prolog prolog;
    private final Program program;

    public MainModule(Prolog prolog, Program program, ExceptionMetadata metadata) {
        super(metadata);
        this.prolog = prolog;
        if (program == null) {
            throw new OurBadException("The main module must have a non-null program");
        }
        this.program = program;
    }

    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }

    public Prolog getProlog() {
        return this.prolog;
    }

    public Expression getExpression() {
        return this.program.getStatementsAndOptionalExpr().getExpression();
    }

    public StatementsAndOptionalExpr getStatementsAndOptionalExpr() {
        return this.program.getStatementsAndOptionalExpr();
    }

    public Program getProgram() {
        return this.program;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.prolog != null) {
            result.add(this.prolog);
        }
        result.add(this.program);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.prolog.serializeToJSONiq(sb, indent);
        this.program.serializeToJSONiq(sb, indent);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMainModule(this, argument);
    }
}

