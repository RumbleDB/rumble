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
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class LibraryModule extends Module {

    protected StaticContext staticContext;
    private String namespace;
    private final Prolog prolog;

    public LibraryModule(Prolog prolog, String namespace, ExceptionMetadata metadata) {
        super(metadata);
        this.prolog = prolog;
        this.namespace = namespace;
    }

    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }

    public Prolog getProlog() {
        return this.prolog;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.prolog != null) {
            result.add(this.prolog);
        }
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitLibraryModule(this, argument);
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append("Library module " + this.namespace);
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("module namespace " + this.namespace + ";\n");
        this.prolog.serializeToJSONiq(sb, indent);
    }
}

