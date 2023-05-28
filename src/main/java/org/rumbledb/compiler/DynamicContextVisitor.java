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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;


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
        for (Map.Entry<Name, SequenceType> paramEntry : expression.getParams().entrySet()) {
            if (!paramEntry.getValue().isResolved()) {
                paramEntry.getValue().resolve(argument, expression.getMetadata());
            }
        }
        if (!expression.getReturnType().isResolved()) {
            expression.getReturnType().resolve(argument, expression.getMetadata());
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

    // @Override
    // public DynamicContext visitTransformExpression(TransformExpression expression, DynamicContext argument) {
    //
    // for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
    // Expression child = copyDecl.getSourceExpression();
    // this.visit(child, argument);
    // RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(child, this.configuration);
    // iterator.bindToVariableInDynamicContext(argument, copyDecl.getVariableName(), argument);
    // }
    //
    // this.visit(expression.getModifyExpression(), argument);
    //
    // this.visit(expression.getReturnExpression(), argument);
    //
    // return argument;
    // }

    @Override
    public DynamicContext visitVariableDeclaration(VariableDeclaration variableDeclaration, DynamicContext argument) {
        Name name = variableDeclaration.getVariableName();

        // Variable is not external: we use the expression.
        if (!variableDeclaration.external()) {
            Expression expression = variableDeclaration.getExpression();
            RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression, this.configuration);
            iterator.bindToVariableInDynamicContext(argument, name, argument);
            return argument;
        }

        // Variable is external. Do we have supplied items?
        List<Item> items = this.configuration.getExternalVariableValue(name);
        if (items != null) {
            if (variableDeclaration.getSequenceType().isEmptySequence() && items.size() > 0) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence type ().",
                        variableDeclaration.getMetadata()
                );
            }
            if (
                !variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.One
                    && items.size() != 1
            ) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity 1.",
                        variableDeclaration.getMetadata()
                );
            }
            if (
                !variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.OneOrZero
                    && items.size() > 1
            ) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity ?.",
                        variableDeclaration.getMetadata()
                );
            }
            if (
                !variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.OneOrMore
                    && items.size() == 0
            ) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity +.",
                        variableDeclaration.getMetadata()
                );
            }
            for (Item item : items) {
                if (
                    variableDeclaration.getSequenceType().isEmptySequence()
                        || !InstanceOfIterator.doesItemTypeMatchItem(
                            variableDeclaration.getSequenceType().getItemType(),
                            item
                        )
                ) {
                    throw new UnexpectedTypeException(
                            "External variable value ("
                                + item
                                + ") does not match the expected type ("
                                + variableDeclaration.getSequenceType()
                                + ").",
                            variableDeclaration.getMetadata()
                    );
                }
            }

            argument.getVariableValues()
                .addVariableValue(
                    name,
                    items
                );
            return argument;
        }

        // Variable is external. Do we have supplied unparsed items?
        String value = this.configuration.getUnparsedExternalVariableValue(name);
        items = new ArrayList<>();
        if (value != null) {
            SequenceType sequenceType = variableDeclaration.getSequenceType();
            Item item = null;
            if (
                !sequenceType.equals(SequenceType.EMPTY_SEQUENCE)
                    && sequenceType.getItemType().equals(BuiltinTypesCatalogue.anyURIItem)
            ) {
                URI resolvedURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                    value,
                    this.configuration,
                    ExceptionMetadata.EMPTY_METADATA
                );
                item = ItemFactory.getInstance().createAnyURIItem(resolvedURI.toString());
            } else {
                item = ItemFactory.getInstance().createStringItem(value);
                ItemType itemType = variableDeclaration.getSequenceType().getItemType();
                if (
                    !InstanceOfIterator.doesItemTypeMatchItem(
                        itemType,
                        item
                    )
                ) {
                    Item castItem = CastIterator.castItemToType(item, itemType, variableDeclaration.getMetadata());
                    if (castItem == null) {
                        throw new UnexpectedTypeException(
                                "External variable value ("
                                    + item.serialize()
                                    + ") does not match the expected type ("
                                    + variableDeclaration.getSequenceType()
                                    + ").",
                                variableDeclaration.getMetadata()
                        );
                    }
                    item = castItem;
                }
            }
            items.add(item);
            if (
                variableDeclaration.getSequenceType().isEmptySequence()
                    || !InstanceOfIterator.doesItemTypeMatchItem(
                        variableDeclaration.getSequenceType().getItemType(),
                        item
                    )
            ) {
                throw new UnexpectedTypeException(
                        "External variable value ("
                            + item.serialize()
                            + ") does not match the expected type ("
                            + variableDeclaration.getSequenceType()
                            + ").",
                        variableDeclaration.getMetadata()
                );
            }
            argument.getVariableValues()
                .addVariableValue(
                    name,
                    items
                );
            return argument;
        }
        if (name.equals(Name.CONTEXT_ITEM) && this.configuration.readFromStandardInput(Name.CONTEXT_ITEM)) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String l;
            try {
                while ((l = reader.readLine()) != null) {
                    stringBuilder.append(l);
                }
                value = stringBuilder.toString();
                String inputFormat = this.configuration.getInputFormat(Name.CONTEXT_ITEM);
                switch (inputFormat) {
                    case "json":
                        items.add(ItemParser.getItemFromString(value, ExceptionMetadata.EMPTY_METADATA));
                        break;
                    case "text":
                        items.add(ItemFactory.getInstance().createStringItem(value));
                        break;
                    default:
                        throw new AbsentPartOfDynamicContextException(
                                "Unrecognized input format: "
                                    + this.configuration.getInputFormat(Name.CONTEXT_ITEM)
                                    + ". Expecting text or json.",
                                variableDeclaration.getMetadata()
                        );
                }
                argument.getVariableValues()
                    .addVariableValue(
                        name,
                        items
                    );
                return argument;
            } catch (IOException e) {
                throw new AbsentPartOfDynamicContextException(
                        "Could not read context item from standard input!",
                        variableDeclaration.getMetadata()
                );
            } catch (ParsingException ex) {
                RuntimeException e = new ParsingException(
                        "The text read from the standard input is not a well-formed JSON value!",
                        variableDeclaration.getMetadata()
                );
                e.initCause(ex);
                throw e;
            }
        }


        // Variable is external and we do not have any supplied value: we fall back to expression, if any.
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

    @Override
    public DynamicContext visitTypeDeclaration(TypeDeclaration declaration, DynamicContext argument) {
        ItemType type = declaration.getDefinition();
        argument.getInScopeSchemaTypes().addInScopeSchemaType(type, declaration.getMetadata());
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
        argument.getInScopeSchemaTypes()
            .importModuleTypes(
                this.importedModuleContexts.get(module.getNamespace()).getInScopeSchemaTypes(),
                module.getNamespace()
            );
        return argument;
    }

    @Override
    public DynamicContext visitProlog(Prolog prolog, DynamicContext argument) {
        DynamicContext generatedContext = visitDescendants(prolog, argument);
        for (ItemType itemType : generatedContext.getInScopeSchemaTypes().getInScopeSchemaTypes()) {
            itemType.resolve(generatedContext, ExceptionMetadata.EMPTY_METADATA);
        }
        return generatedContext;
    }
}
