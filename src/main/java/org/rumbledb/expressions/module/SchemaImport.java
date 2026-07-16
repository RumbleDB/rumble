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
 */

package org.rumbledb.expressions.module;

import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

/**
 * An XQuery schema import declaration. Location hints remain unresolved until schema loading.
 */
public class SchemaImport extends Node {

    public enum BindingKind {
        NONE,
        PREFIX,
        DEFAULT_ELEMENT_NAMESPACE
    }

    private final String targetNamespace;
    private final BindingKind bindingKind;
    private final String prefix;
    private final List<String> locationHints;

    public SchemaImport(
            String targetNamespace,
            BindingKind bindingKind,
            String prefix,
            List<String> locationHints,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (targetNamespace == null || bindingKind == null || locationHints == null) {
            throw new OurBadException("A schema import cannot contain null components.");
        }
        if ((bindingKind == BindingKind.PREFIX) != (prefix != null)) {
            throw new OurBadException(
                    "A schema import with a prefix binding must have a non-null prefix, and vice versa."
            );
        }
        this.targetNamespace = targetNamespace;
        this.bindingKind = bindingKind;
        this.prefix = prefix;
        this.locationHints = List.copyOf(locationHints);
    }

    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    public BindingKind getBindingKind() {
        return this.bindingKind;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public List<String> getLocationHints() {
        return this.locationHints;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitSchemaImport(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("import schema ");
        if (this.bindingKind == BindingKind.PREFIX) {
            sb.append("namespace ").append(this.prefix).append(" = ");
        } else if (this.bindingKind == BindingKind.DEFAULT_ELEMENT_NAMESPACE) {
            sb.append("default element namespace ");
        }
        appendURILiteral(sb, this.targetNamespace);
        if (!this.locationHints.isEmpty()) {
            sb.append(" at ");
            for (int i = 0; i < this.locationHints.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                appendURILiteral(sb, this.locationHints.get(i));
            }
        }
        sb.append(";\n");
    }

    private static void appendURILiteral(StringBuilder sb, String value) {
        sb.append('"').append(value.replace("\"", "\"\"")).append('"');
    }
}
