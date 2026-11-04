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
package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.compiler.CompilationPipeline;
import org.rumbledb.compiler.DynamicContextVisitor;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.NotLibraryModuleException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

import static org.rumbledb.compiler.utils.AnnotationValidator.hasPrivateAnnotation;

/** Loads an isolated XQuery module using the query's resource resolver. */
public class LoadXQueryModuleFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoadXQueryModuleFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        String namespace = getChild(0).materializeFirstOrNull(context).getStringValue();
        if (namespace.isEmpty()) {
            throw error("The module namespace must not be empty.", ErrorCode.EmptyDynamicModuleURI);
        }
        Item options = getChildren().size() > 1 ? getChild(1).materializeFirstOrNull(context) : null;
        List<Item> version = option(options, "xquery-version");
        if (version != null) {
            if (version.size() != 1 || !version.get(0).isDecimal()) {
                throw optionType("xquery-version must be an xs:decimal.");
            }
            if (version.get(0).getDecimalValue().compareTo(new BigDecimal("3.1")) > 0) {
                throw error(
                        "The requested XQuery version is not supported.", ErrorCode.DynamicModuleLoadingNotSupported);
            }
        }
        List<Item> focus = option(options, "context-item");
        if (focus != null && focus.size() > 1) {
            throw optionType("context-item must contain at most one item.");
        }
        Item variables = mapOption(options, "variables");
        mapOption(options, "vendor-options");
        ExternalBindings bindings = ExternalBindings.empty();
        if (variables != null) {
            for (Item key : variables.getItemKeys()) {
                bindings.bind(key.getQNameValue(), new ItemSequenceBinding(variables.getSequenceByKey(key)));
            }
        }
        StringBuilder query = new StringBuilder("xquery version \"3.1\"; import module namespace loaded = ");
        query.append(literal(namespace));
        List<Item> hints = option(options, "location-hints");
        if (hints != null && !hints.isEmpty()) {
            List<String> locations = new ArrayList<>();
            for (Item hint : hints) {
                if (!hint.isString()) {
                    throw optionType("location-hints must contain xs:string values.");
                }
                locations.add(literal(hint.getStringValue()));
            }
            query.append(" at ").append(String.join(", ", locations));
        }
        query.append("; ");
        if (focus != null && !focus.isEmpty()) {
            bindings.bind(Name.CONTEXT_ITEM, new ItemSequenceBinding(focus));
            query.append("declare context item external; ");
        }
        query.append("()");
        var configuration = this.staticContext.getConfiguration().toBuilder()
                .configureSemantics(s -> s.queryLanguage("xquery31"))
                .build();
        MainModule module;
        try {
            URI base = this.staticContext.getStaticURI();
            module = CompilationPipeline.compileMainModule(
                    query.toString(),
                    base == null ? URI.create("file:/") : base,
                    new CompilationConfiguration(configuration, context.getResourceResolver()),
                    ExternalBindings.empty());
        } catch (ModuleNotFoundException e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof NotLibraryModuleException) {
                    throw error(e.getJSONiqErrorMessage(), ErrorCode.InvalidDynamicModule);
                }
                cause = cause.getCause();
            }
            throw error(e.getJSONiqErrorMessage(), ErrorCode.DynamicModuleNotFound);
        } catch (RumbleException e) {
            if (ErrorCode.InvalidJsoniqVersionErrorCode.equals(e.getErrorCode())) {
                throw error(e.getJSONiqErrorMessage(), ErrorCode.DynamicModuleLoadingNotSupported);
            }
            throw error(e.getJSONiqErrorMessage(), ErrorCode.InvalidDynamicModule);
        }
        DynamicContext loaded = new DynamicContextVisitor(
                        configuration, bindings, context.getResourceResolver(), ErrorCode.DynamicModuleParameterType)
                .visit(module, null);
        Map<Item, List<Item>> variableExports = new LinkedHashMap<>();
        Map<Item, Map<Item, List<Item>>> functionExports = new LinkedHashMap<>();
        ItemFactory factory = ItemFactory.getInstance();
        // The prolog holds the declarations of every module the requested one reaches.
        for (var declaration : module.getProlog().getVariableDeclarations()) {
            Name name = declaration.getVariableName();
            if (namespace.equals(name.getNamespace()) && !hasPrivateAnnotation(declaration.getAnnotations())) {
                variableExports.put(
                        factory.createQNameItem(name),
                        loaded.getVariableValues().getLocalVariableValue(name, getMetadata()));
            }
        }
        for (var declaration : module.getProlog().getFunctionDeclarations()) {
            var id = declaration.getFunctionIdentifier();
            if (!namespace.equals(id.getName().getNamespace())
                    || hasPrivateAnnotation(
                            ((InlineFunctionExpression) declaration.getExpression()).getAnnotations())) {
                continue;
            }
            functionExports
                    .computeIfAbsent(factory.createQNameItem(id.getName()), ignored -> new LinkedHashMap<>())
                    .put(
                            factory.createIntItem(id.getArity()),
                            List.of(loaded.getNamedFunctions().getUserDefinedFunction(id)));
        }
        Map<Item, List<Item>> functions = new LinkedHashMap<>();
        functionExports.forEach((name, arities) -> functions.put(name, List.of(map(arities))));
        return map(Map.of(
                factory.createStringItem("variables"),
                List.of(map(variableExports)),
                factory.createStringItem("functions"),
                List.of(map(functions))));
    }

    private Item map(Map<Item, List<Item>> entries) {
        return ItemFactory.getInstance()
                .createMapItem(
                        new ArrayList<>(entries.keySet()), new ArrayList<>(entries.values()), getMetadata(), false);
    }

    private List<Item> option(Item options, String name) {
        return options == null ? null : options.getSequenceByKey(name);
    }

    private Item mapOption(Item options, String name) {
        List<Item> value = option(options, name);
        if (value == null) {
            return null;
        }
        if (value.size() != 1 || !value.get(0).isMap()) {
            throw optionType(name + " must be a map with xs:QName keys.");
        }
        Item result = value.get(0);
        if (result.getItemKeys().stream().anyMatch(key -> !key.isQName())) {
            throw optionType(name + " must have xs:QName keys.");
        }
        return result;
    }

    private UnexpectedTypeException optionType(String message) {
        return new UnexpectedTypeException(message, getMetadata());
    }

    private RumbleException error(String message, ErrorCode code) {
        return new RumbleException(message, code, getMetadata());
    }

    private static String literal(String value) {
        return "'" + value.replace("&", "&amp;").replace("'", "''") + "'";
    }
}
