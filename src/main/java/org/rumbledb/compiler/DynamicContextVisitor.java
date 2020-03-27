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

package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.SequenceType.Arity;

/**
 * Dynamic context visitor. Populates the dynamic context to evaluate the main expression.
 */
public class DynamicContextVisitor extends AbstractNodeVisitor<DynamicContext> {

    private RumbleRuntimeConfiguration configuration;

    DynamicContextVisitor(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected DynamicContext defaultAction(Node node, DynamicContext argument) {
        DynamicContext generatedContext = visitDescendants(node, argument);
        return generatedContext;
    }

    @Override
    public DynamicContext visit(Node node, DynamicContext argument) {
        if (argument == null) {
            argument = new DynamicContext();
        }
        return node.accept(this, argument);
    }

    @Override
    public DynamicContext visitVariableDeclaration(VariableDeclaration variableDeclaration, DynamicContext argument) {
        DynamicContext result = new DynamicContext(argument);
        String name = variableDeclaration.getVariableName();
        String value = this.configuration.getExternalVariableValue(name);
        List<Item> values = new ArrayList<>();
        if (value != null) {
            Item item = ItemFactory.getInstance().createStringItem(value);
            values.add(item);
            if (
                variableDeclaration.getSequenceType().isEmptySequence()
                    || !item.isTypeOf(variableDeclaration.getSequenceType().getItemType())
            ) {
                throw new UnexpectedTypeException(
                        "External variable value does not match the expected type.",
                        variableDeclaration.getMetadata()
                );
            }
        } else {
            if (
                !variableDeclaration.getSequenceType().isEmptySequence()
                    && (variableDeclaration.getSequenceType().getArity().equals(Arity.One)
                        || variableDeclaration.getSequenceType().getArity().equals(Arity.OneOrMore))
            ) {
                throw new UnexpectedTypeException(
                        "External variable value is empty and does not match the expected type.",
                        variableDeclaration.getMetadata()
                );
            }
        }
        result.addVariableValue(
            name,
            values
        );
        return result;
    }

}
