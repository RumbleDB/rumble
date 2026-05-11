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


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.ItemType;

import java.util.Collections;
import java.util.List;

public class TypeDeclaration extends Node {

    private final ItemType typeDefinition;

    public TypeDeclaration(
            ItemType typeDefinition,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.typeDefinition = typeDefinition;
    }

    public ItemType getDefinition() {
        return this.typeDefinition;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeDeclaration(this, argument);
    }

    /**
     * Prints the node tree to a string buffer.
     *
     * @param buffer a string buffer to write to
     * @param indent the current level of indentation
     */
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append("TypeDeclaration " + this.typeDefinition.getName());
        buffer.append(" as " + this.typeDefinition.toString());
        buffer.append("\n");
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("declare type " + this.typeDefinition.getName() + " as " + this.typeDefinition + ";\n");
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }
}

