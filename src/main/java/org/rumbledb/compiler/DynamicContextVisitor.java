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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.ExceptionMetadata;
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
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;


/**
 * Dynamic context visitor. Populates the dynamic context to evaluate the main expression.
 */
public class DynamicContextVisitor extends AbstractNodeVisitor<DynamicContext> {

    private RumbleRuntimeConfiguration configuration;
    private Map<String, DynamicContext> importedModuleContexts;

    DynamicContextVisitor(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        this.importedModuleContexts = new HashMap<>();
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
        Map<Name, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>();
        for (Map.Entry<Name, SequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue());
        }
        RuntimeIterator bodyIterator = VisitorHelpers.generateRuntimeIterator(expression, this.configuration);
        List<Item> functionInList = bodyIterator.materialize(argument);
        if (functionInList.size() != 1) {
            throw new OurBadException("A function declaration should produce exactly one function");
        }
        Item function = functionInList.get(0);
        if (expression.getName() == null) {
            throw new OurBadException("A function declaration must always have a name.");
        } else {
            // named (static function declaration)
            argument.getNamedFunctions().addUserDefinedFunction(function, expression.getMetadata());
        }

        return defaultAction(expression, argument);
    }

    @Override
    public DynamicContext visitVariableDeclaration(VariableDeclaration variableDeclaration, DynamicContext argument) {
        Name name = variableDeclaration.getVariableName();
        if (variableDeclaration.external()) {
            String value = this.configuration.getExternalVariableValue(name);
            List<Item> values = new ArrayList<>();
            if (value != null) {
                SequenceType sequenceType = variableDeclaration.getSequenceType();
                Item item = null;
                if (
                    !sequenceType.equals(SequenceType.EMPTY_SEQUENCE)
                        && sequenceType.getItemType().equals(ItemType.anyURIItem)
                ) {
                    URI resolvedURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                        value,
                        this.configuration,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    item = ItemFactory.getInstance().createAnyURIItem(resolvedURI.toString());
                } else {
                    item = ItemFactory.getInstance().createStringItem(value);
                }
                values.add(item);
                if (
                    variableDeclaration.getSequenceType().isEmptySequence()
                        || !item.isTypeOf(variableDeclaration.getSequenceType().getItemType())
                ) {
                    throw new UnexpectedTypeException(
                            "External variable value ("
                                + value
                                + ") does not match the expected type ("
                                + variableDeclaration.getSequenceType()
                                + ").",
                            variableDeclaration.getMetadata()
                    );
                }
            } else {
                Expression expression = variableDeclaration.getExpression();
                if (expression != null) {
                    RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression, this.configuration);
                    iterator.bindToVariableInDynamicContext(argument, name, argument);
                    return argument;
                }
                throw new AbsentPartOfDynamicContextException(
                        "External variable value is not provided!",
                        variableDeclaration.getMetadata()
                );
            }
            argument.getVariableValues()
                .addVariableValue(
                    name,
                    values
                );
            return argument;
        }
        Expression expression = variableDeclaration.getExpression();
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression, this.configuration);
        iterator.bindToVariableInDynamicContext(argument, name, argument);
        return argument;
    }

    @Override
    public DynamicContext visitLibraryModule(LibraryModule module, DynamicContext argument) {
        if (!this.importedModuleContexts.containsKey(module.getNamespace())) {
            DynamicContext newContext = new DynamicContext(this.configuration);
            newContext.setNamedFunctions(argument.getNamedFunctions());
            DynamicContext importedContext = visitDescendants(module, newContext);
            this.importedModuleContexts.put(module.getNamespace(), importedContext);
        }
        argument.getVariableValues()
            .importModuleValues(
                this.importedModuleContexts.get(module.getNamespace()).getVariableValues(),
                module.getNamespace()
            );
        return argument;
    }
}
