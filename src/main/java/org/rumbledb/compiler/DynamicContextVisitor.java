/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.FileBinding;
import org.rumbledb.bindings.InputFormat;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.bindings.LexicalBinding;
import org.rumbledb.bindings.StandardInputBinding;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.CycleInVariableDeclarationsException;
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
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanBindings;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

/**
 * Dynamic context visitor. Populates the dynamic context to evaluate the main expression.
 */
public class DynamicContextVisitor extends AbstractNodeVisitor<DynamicContext> {

    private final RumbleConfiguration configuration;
    private final ResourceResolver resourceResolver;
    private ExternalBindings externalBindings;
    private int libraryModuleDepth;
    private List<Item> initialContextItem;
    private Map<String, DynamicContext> importedModuleContexts;

    public DynamicContextVisitor(RumbleConfiguration configuration, ExternalBindings externalBindings) {
        this(configuration, externalBindings, new ResourceResolver());
    }

    public DynamicContextVisitor(
            RumbleConfiguration configuration, ExternalBindings externalBindings, ResourceResolver resourceResolver) {
        this.configuration = configuration;
        this.resourceResolver = resourceResolver;
        this.externalBindings = externalBindings;
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
            argument = new DynamicContext(this.configuration, this.externalBindings, this.resourceResolver);
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
        ItemRuntimePlan bodyIterator = new RuntimeIteratorVisitor(this.configuration).generateRuntimePlan(expression);
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
        DynamicContext result = initializeVariableDeclaration(variableDeclaration, argument);
        if (variableDeclaration.getVariableName().equals(Name.CONTEXT_ITEM)) {
            // Establish the initial focus only after evaluating its initializer.
            result.getVariableValues().setPosition(1);
            result.getVariableValues().setLast(1);
        }
        return result;
    }

    private DynamicContext initializeVariableDeclaration(
            VariableDeclaration variableDeclaration, DynamicContext argument) {
        Name name = variableDeclaration.getVariableName();
        // Variable is not external: we use the expression.
        if (!variableDeclaration.external()) {
            Expression expression = variableDeclaration.getExpression();
            ItemRuntimePlan iterator = new RuntimeIteratorVisitor(this.configuration).generateRuntimePlan(expression);
            if (iterator.getRuntimeStaticContext().getExecutionMode().isLocal()
                    && !variableDeclaration.isAssignable()
                    && !expression.isSequential()) {
                argument.getVariableValues()
                        .addVariableValue(
                                name,
                                new GlobalVariableValue(iterator, argument, variableDeclaration.getMetadata(), name));
            } else {
                RuntimePlanBindings.bind(iterator, argument, name, argument);
            }
            return argument;
        }

        // Variable is external. Do we have supplied items?
        List<Item> items = name.equals(Name.CONTEXT_ITEM) && this.libraryModuleDepth > 0
                ? this.initialContextItem
                : this.getItemsBinding(name);
        if (items != null) {
            if (variableDeclaration.getSequenceType().isEmptySequence() && items.size() > 0) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence type ().", variableDeclaration.getMetadata());
            }
            if (!variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.One
                    && items.size() != 1) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity 1.", variableDeclaration.getMetadata());
            }
            if (!variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.OneOrZero
                    && items.size() > 1) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity ?.", variableDeclaration.getMetadata());
            }
            if (!variableDeclaration.getSequenceType().isEmptySequence()
                    && variableDeclaration.getSequenceType().getArity() == Arity.OneOrMore
                    && items.size() == 0) {
                throw new UnexpectedTypeException(
                        "External variable values does not match sequence arity +.", variableDeclaration.getMetadata());
            }
            for (Item item : items) {
                if (variableDeclaration.getSequenceType().isEmptySequence()
                        || !InstanceOfIterator.doesItemTypeMatchItem(
                                variableDeclaration.getSequenceType().getItemType(), item)) {
                    throw new UnexpectedTypeException(
                            "External variable value ("
                                    + item
                                    + ") does not match the expected type ("
                                    + variableDeclaration.getSequenceType()
                                    + ").",
                            variableDeclaration.getMetadata());
                }
            }

            argument.getVariableValues().addVariableValue(name, items);
            return argument;
        }

        // Variable is external. Do we have supplied unparsed items?
        String value = this.getLexicalValueBinding(name);
        items = new ArrayList<>();
        if (value == null) {
            value = this.getFileValueBinding(name, variableDeclaration.getMetadata());
        }
        if (value != null) {
            SequenceType sequenceType = variableDeclaration.getSequenceType();
            Item item = null;
            if (!sequenceType.equals(SequenceType.createSequenceType("()"))
                    && sequenceType.getItemType().equals(BuiltinTypesCatalogue.anyURIItem)) {
                URI resolvedURI =
                        FileSystemUtil.resolveURIAgainstWorkingDirectory(value, ExceptionMetadata.EMPTY_METADATA);
                item = ItemFactory.getInstance().createAnyURIItem(resolvedURI.toString());
            } else {
                boolean jsonFile =
                        this.externalBindings.get(name, FileBinding.class).isPresent()
                                && this.getInputFormat(name).equals("json");
                item = jsonFile
                        ? parseJSONItem(value, variableDeclaration.getMetadata())
                        : ItemFactory.getInstance().createStringItem(value);
                ItemType itemType = variableDeclaration.getSequenceType().getItemType();
                if (!InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
                    Item castItem = CastIterator.castItemToType(item, itemType, variableDeclaration.getMetadata());
                    if (castItem == null) {
                        throw new UnexpectedTypeException(
                                "External variable value ("
                                        + item.serialize()
                                        + ") does not match the expected type ("
                                        + variableDeclaration.getSequenceType()
                                        + ").",
                                variableDeclaration.getMetadata());
                    }
                    item = castItem;
                }
            }
            items.add(item);
            if (variableDeclaration.getSequenceType().isEmptySequence()
                    || !InstanceOfIterator.doesItemTypeMatchItem(
                            variableDeclaration.getSequenceType().getItemType(), item)) {
                throw new UnexpectedTypeException(
                        "External variable value ("
                                + item.serialize()
                                + ") does not match the expected type ("
                                + variableDeclaration.getSequenceType()
                                + ").",
                        variableDeclaration.getMetadata());
            }
            argument.getVariableValues().addVariableValue(name, items);
            return argument;
        }

        // Variable is external. Do we have supplied DataFrame items?
        Dataset<Row> df = this.getDataFrameBinding(name);
        if (df != null) {
            HomogeneousItemDataFrame jdf = new HomogeneousItemDataFrame(df);
            argument.getVariableValues().addVariableValue(name, jdf);
            return argument;
        }

        if (name.equals(Name.CONTEXT_ITEM) && this.isReadFromStandardInput(Name.CONTEXT_ITEM)) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String l;
            try {
                while ((l = reader.readLine()) != null) {
                    stringBuilder.append(l);
                }
                value = stringBuilder.toString();
                String inputFormat = this.getInputFormat(Name.CONTEXT_ITEM);
                switch (inputFormat) {
                    case "json":
                        items.add(parseJSONItem(value, ExceptionMetadata.EMPTY_METADATA));
                        break;
                    case "text":
                        items.add(ItemFactory.getInstance().createStringItem(value));
                        break;
                    default:
                        throw new AbsentPartOfDynamicContextException(
                                "Unrecognized input format: "
                                        + this.getInputFormat(Name.CONTEXT_ITEM)
                                        + ". Expecting text or json.",
                                variableDeclaration.getMetadata());
                }
                argument.getVariableValues().addVariableValue(name, items);
                return argument;
            } catch (IOException e) {
                throw new AbsentPartOfDynamicContextException(
                        "Could not read context item from standard input!", variableDeclaration.getMetadata());
            } catch (ParsingException ex) {
                RuntimeException e = new ParsingException(
                        "The text read from the standard input is not a well-formed JSON value!",
                        variableDeclaration.getMetadata());
                e.initCause(ex);
                throw e;
            }
        }

        // Variable is external and we do not have any supplied value: we fall back to expression, if any.
        Expression expression = variableDeclaration.getExpression();
        if (expression != null) {
            ItemRuntimePlan iterator = new RuntimeIteratorVisitor(this.configuration).generateRuntimePlan(expression);
            if (iterator.getRuntimeStaticContext().getExecutionMode().isLocal()
                    && !variableDeclaration.isAssignable()
                    && !expression.isSequential()) {
                argument.getVariableValues()
                        .addVariableValue(
                                name,
                                new GlobalVariableValue(iterator, argument, variableDeclaration.getMetadata(), name));
            } else {
                RuntimePlanBindings.bind(iterator, argument, name, argument);
            }
            return argument;
        }

        throw new AbsentPartOfDynamicContextException(
                "External variable value is not provided!", variableDeclaration.getMetadata());
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
            DynamicContext newContext =
                    new DynamicContext(this.configuration, this.externalBindings, this.resourceResolver);
            newContext.setNamedFunctions(argument.getNamedFunctions());
            DynamicContext importedContext;
            this.libraryModuleDepth++;
            try {
                importedContext = visitDescendants(module, newContext);
            } finally {
                this.libraryModuleDepth--;
            }
            this.importedModuleContexts.put(module.getNamespace(), importedContext);
        }
        argument.getVariableValues()
                .importModuleValues(
                        this.importedModuleContexts.get(module.getNamespace()).getVariableValues());
        argument.getInScopeSchemaTypes()
                .importModuleTypes(
                        this.importedModuleContexts.get(module.getNamespace()).getInScopeSchemaTypes());
        return argument;
    }

    @Override
    public DynamicContext visitProlog(Prolog prolog, DynamicContext argument) {
        for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
            argument.addGlobalVariable(declaration.getVariableName());
        }
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            visit(declaration, argument);
        }
        // Context initialization can depend on another global declared in this module.
        // Bind local immutable initializers before an imported module checks the shared focus.
        Set<Node> earlyBound = new HashSet<>();
        if (prolog.hasContextItemDeclaration()) {
            for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
                Expression initializer = declaration.getExpression();
                if (!declaration.external()
                        && !Name.CONTEXT_ITEM.equals(declaration.getVariableName())
                        && !declaration.isAssignable()
                        && !initializer.isSequential()) {
                    ItemRuntimePlan plan =
                            new RuntimeIteratorVisitor(this.configuration).generateRuntimePlan(initializer);
                    if (plan.getRuntimeStaticContext().getExecutionMode().isLocal()) {
                        argument.getVariableValues()
                                .addVariableValue(
                                        declaration.getVariableName(),
                                        new GlobalVariableValue(
                                                plan,
                                                argument,
                                                declaration.getMetadata(),
                                                declaration.getVariableName()));
                        earlyBound.add(declaration);
                    }
                }
            }
        }
        VariableDeclaration contextDeclaration = null;
        if (this.libraryModuleDepth == 0) {
            for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
                if (declaration.getVariableName().equals(Name.CONTEXT_ITEM)) {
                    contextDeclaration = declaration;
                    visit(declaration, argument);
                    this.initialContextItem = argument.getVariableValues()
                            .getLocalVariableValue(Name.CONTEXT_ITEM, declaration.getMetadata());
                    break;
                }
            }
        }
        DynamicContext generatedContext = argument;
        for (Node child : prolog.getChildren()) {
            if (!(child instanceof FunctionDeclaration) && child != contextDeclaration && !earlyBound.contains(child))
                generatedContext = visit(child, generatedContext);
        }
        for (ItemType itemType : generatedContext.getInScopeSchemaTypes().getInScopeSchemaTypes()) {
            itemType.resolve(generatedContext, ExceptionMetadata.EMPTY_METADATA);
        }
        // Deferred bindings resolve forward dependencies during module initialization. Complete
        // initialization before entering the query body, where try/catch must not catch global
        // initializer errors, and before Spark can capture initializer plans in task closures.
        for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
            Name name = declaration.getVariableName();
            if (generatedContext.getVariableValues().isRDD(name, declaration.getMetadata())
                    || generatedContext.getVariableValues().isDataFrame(name, declaration.getMetadata())) {
                continue;
            }
            List<Item> items =
                    generatedContext.getVariableValues().getLocalVariableValue(name, declaration.getMetadata());
            if (items instanceof GlobalVariableValue deferred) {
                generatedContext.getVariableValues().addVariableValue(name, deferred.value());
            }
        }
        return generatedContext;
    }

    private Item parseJSONItem(String value, ExceptionMetadata metadata) {
        return ItemParser.getItemFromJSONString(
                value,
                JSONParsingOptions.defaultInstance(true),
                this.configuration.semantics().xmlVersion(),
                true,
                metadata);
    }

    private List<Item> getItemsBinding(Name name) {
        return this.externalBindings
                .get(name, ItemSequenceBinding.class)
                .map(ItemSequenceBinding::getItems)
                .orElse(null);
    }

    private String getLexicalValueBinding(Name name) {
        return this.externalBindings
                .get(name, LexicalBinding.class)
                .map(LexicalBinding::getValue)
                .orElse(null);
    }

    private Dataset<Row> getDataFrameBinding(Name name) {
        return this.externalBindings
                .get(name, DataFrameBinding.class)
                .map(DataFrameBinding::getDataFrame)
                .orElse(null);
    }

    private String getFileValueBinding(Name name, ExceptionMetadata metadata) {
        FileBinding fileBinding =
                this.externalBindings.get(name, FileBinding.class).orElse(null);
        if (fileBinding == null) {
            return null;
        }
        try {
            URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(fileBinding.getLocation(), metadata);
            try (InputStream inputStream = FileSystemUtil.getDataInputStream(uri, metadata)) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new AbsentPartOfDynamicContextException(
                    "Could not read external variable value from file.", metadata);
        }
    }

    private boolean isReadFromStandardInput(Name name) {
        return this.externalBindings.get(name, StandardInputBinding.class).isPresent();
    }

    private String getInputFormat(Name name) {
        if (this.externalBindings.get(name, StandardInputBinding.class).isPresent()) {
            InputFormat format = this.externalBindings
                    .get(name, StandardInputBinding.class)
                    .get()
                    .getFormat();
            return format.name().toLowerCase();
        }
        if (this.externalBindings.get(name, FileBinding.class).isPresent()) {
            InputFormat format =
                    this.externalBindings.get(name, FileBinding.class).get().getFormat();
            return format.name().toLowerCase();
        }
        return InputFormat.JSON.name().toLowerCase();
    }

    /** A local global initializer is evaluated once, when its value is needed. */
    private static final class GlobalVariableValue extends AbstractList<Item> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final ItemRuntimePlan plan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private final Name variableName;
        private List<Item> value;
        private boolean evaluating;

        GlobalVariableValue(
                ItemRuntimePlan plan, DynamicContext context, ExceptionMetadata metadata, Name variableName) {
            this.plan = plan;
            this.context = context;
            this.metadata = metadata;
            this.variableName = variableName;
        }

        private List<Item> value() {
            if (this.value != null) return this.value;
            if (this.evaluating) {
                throw new CycleInVariableDeclarationsException(
                        "A global variable initializer depends on its own value.", this.metadata);
            }
            this.evaluating = true;
            // The initial focus is absent while its initializer is evaluated. In particular,
            // creating a function item must not capture the initializer's own deferred value.
            boolean focusInitializer = Name.CONTEXT_ITEM.equals(this.variableName);
            Item position = null;
            Item last = null;
            if (focusInitializer) {
                position = this.context.getVariableValues().getPosition();
                last = this.context.getVariableValues().getLast();
                this.context.getVariableValues().removeVariable(Name.CONTEXT_ITEM);
                this.context.getVariableValues().removeVariable(Name.CONTEXT_POSITION);
                this.context.getVariableValues().removeVariable(Name.CONTEXT_COUNT);
            }
            try {
                this.value = this.plan.materialize(this.context);
                return this.value;
            } finally {
                if (focusInitializer) {
                    this.context.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, this);
                    if (position != null)
                        this.context.getVariableValues().addVariableValue(Name.CONTEXT_POSITION, List.of(position));
                    if (last != null)
                        this.context.getVariableValues().addVariableValue(Name.CONTEXT_COUNT, List.of(last));
                }
                this.evaluating = false;
            }
        }

        @Override
        public Item get(int index) {
            return value().get(index);
        }

        @Override
        public int size() {
            return value().size();
        }

        @Serial
        private Object writeReplace() {
            // A Spark initializer can serialize its module context before it has produced a
            // value. Preserve a cycle check for that binding without evaluating it again merely
            // because an unrelated task captures the context.
            if (this.evaluating) return new InitializingGlobalVariableValue(this.metadata);
            return new ArrayList<>(value());
        }
    }

    private static final class InitializingGlobalVariableValue extends AbstractList<Item> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final ExceptionMetadata metadata;

        InitializingGlobalVariableValue(ExceptionMetadata metadata) {
            this.metadata = metadata;
        }

        @Override
        public Item get(int index) {
            throw cycle();
        }

        @Override
        public int size() {
            throw cycle();
        }

        private CycleInVariableDeclarationsException cycle() {
            return new CycleInVariableDeclarationsException(
                    "A global variable initializer depends on its own value.", this.metadata);
        }
    }
}
