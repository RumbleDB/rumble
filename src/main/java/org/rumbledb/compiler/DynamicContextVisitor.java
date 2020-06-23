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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.Functions;
import org.rumbledb.types.SequenceType;


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
            argument = new DynamicContext(this.configuration);
        }
        return node.accept(this, argument);
    }

    @Override
    public DynamicContext visitFunctionDeclaration(FunctionDeclaration declaration, DynamicContext argument) {
        InlineFunctionExpression expression = (InlineFunctionExpression) declaration.getExpression();
        Map<Name, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>(expression.getParams().size(), 1);
        for (Map.Entry<Name, SequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue());
        }
        RuntimeIterator bodyIterator = VisitorHelpers.generateRuntimeIterator(expression);
        List<Item> functionInList = bodyIterator.materialize(argument);
        if (functionInList.size() != 1) {
            throw new OurBadException("A function declaration should produce exactly one function");
        }
        Item function = functionInList.get(0);
        if (expression.getName() == null) {
            throw new OurBadException("A function declaration must always have a name.");
        } else {
            // named (static function declaration)
            Functions.addUserDefinedFunction(function, expression.getMetadata());
        }

        return defaultAction(expression, argument);
    }

    @Override
    public DynamicContext visitVariableDeclaration(VariableDeclaration variableDeclaration, DynamicContext argument) {
        DynamicContext result = new DynamicContext(argument);
        Name name = variableDeclaration.getVariableName();
        if (variableDeclaration.external()) {
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
                Expression expression = variableDeclaration.getExpression();
                if (expression != null) {
                    RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression);
                    iterator.bindToVariableInDynamicContext(result, name, argument);
                    return result;
                }
                throw new AbsentPartOfDynamicContextException(
                        "External variable value is not provided!",
                        variableDeclaration.getMetadata()
                );
            }
            result.addVariableValue(
                name,
                values
            );
            return result;
        }
        Expression expression = variableDeclaration.getExpression();
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression);
        iterator.bindToVariableInDynamicContext(result, name, argument);
        return result;
    }

    @Override
    public DynamicContext visitLibraryModule(LibraryModule module, DynamicContext argument) {
        DynamicContext importedContext = visitDescendants(module, argument);
        argument.importModuleContext(importedContext, module.getNamespace());
        return argument;
    }
}
