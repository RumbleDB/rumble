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

import java.util.ArrayList;
import java.util.List;

public class MainModule extends Module {

    private static final long serialVersionUID = 1L;
    protected StaticContext staticContext;
    private final Prolog prolog;
    private final Expression expression;

    public MainModule(Prolog prolog, Expression expression, ExceptionMetadata metadata) {
        super(metadata);
        this.prolog = prolog;
        if (expression == null) {
            throw new OurBadException("The main module must have a non-null expression");
        }
        this.expression = expression;
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
        return this.expression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.prolog != null) {
            result.add(this.prolog);
        }
        result.add(this.expression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.prolog.serializeToJSONiq(sb, indent);
        this.expression.serializeToJSONiq(sb, indent);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMainModule(this, argument);
    }
}

