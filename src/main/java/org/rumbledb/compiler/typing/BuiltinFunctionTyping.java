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
package org.rumbledb.compiler.typing;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Return types of builtin function calls that are more precise than their signatures. */
public final class BuiltinFunctionTyping {

    /** Infers the return type of a call more precisely than its signature does, or returns empty. */
    @FunctionalInterface
    private interface ReturnTypeRule {
        Optional<SequenceType> infer(FunctionCallExpression call, StaticContext staticContext);
    }

    private final StaticTypeErrors errors;

    private final Map<Name, ReturnTypeRule> rules = new HashMap<>();

    private final RumbleConfiguration configuration;

    private final ResourceResolver resourceResolver;

    public BuiltinFunctionTyping(
            StaticTypeErrors errors, RumbleConfiguration configuration, ResourceResolver resourceResolver) {
        this.errors = errors;
        this.configuration = configuration;
        this.resourceResolver = resourceResolver;

        addBuiltinRule("data", (call, staticContext) -> {
            if (call.getArguments().size() != 1) {
                return Optional.empty();
            }
            SequenceType input = call.getArguments().get(0).getStaticSequenceType();
            checkTypedValueAvailable(call, input, staticContext);
            return Optional.of(TypeAtomization.inferType(input));
        });
        addBuiltinRule("doc", this::inferDocumentType);
        // Numeric inputs retain their precise type. Atomized node/array inputs use the signature's numeric type.
        addBuiltinRule("round", (call, staticContext) -> {
            SequenceType input = call.getArguments().get(0).getStaticSequenceType();
            if (input.isEmptySequence() || input.getItemType().isSubtypeOf(BuiltinTypesCatalogue.numericItem)) {
                return Optional.of(input);
            }
            return TypeAtomization.containsNodeOrArray(input.getItemType())
                    ? Optional.of(new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceCardinality.ZERO_OR_ONE))
                    : Optional.empty();
        });
        // These functions return items of their first argument, so only its cardinality changes.
        addInputItemsRule("exactly-one", cardinality -> SequenceCardinality.ONE);
        addInputItemsRule("one-or-more", SequenceCardinality::replaceZeroWithOne);
        addInputItemsRule("zero-or-one", SequenceCardinality::atMostOne);
        addInputItemsRule("head", SequenceCardinality::atMostOne);
        addInputItemsRule("tail", SequenceCardinality::withoutFirst);
        addInputItemsRule("reverse", UnaryOperator.identity());
        addInputItemsRule("unordered", UnaryOperator.identity());
        addInputItemsRule("sort", UnaryOperator.identity());
        addInputItemsRule("subsequence", SequenceCardinality::filtered);
        addInputItemsRule("remove", SequenceCardinality::filtered);
        addBuiltinRule(
                "insert-before",
                (call, staticContext) -> Optional.of(call.getArguments()
                        .get(0)
                        .getStaticSequenceType()
                        .concatenateWith(call.getArguments().get(2).getStaticSequenceType())));
        // Equal values are merged, but a value remains.
        addBuiltinRule("distinct-values", (call, staticContext) -> {
            SequenceType values =
                    TypeAtomization.inferType(call.getArguments().get(0).getStaticSequenceType());
            return Optional.of(new SequenceType(
                    values.getItemType(), values.getCardinality().grouped()));
        });
        // set output type to 'Integer' if inputType is 'Array'
        addBuiltinRule(
                "size",
                (call, staticContext) ->
                        call.getArguments().get(0).getStaticSequenceType().getArity() == SequenceType.Arity.One
                                ? Optional.of(
                                        new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One))
                                : Optional.empty());
        addBuiltinRule("sum", (call, staticContext) -> Optional.of(inferSumReturnType(call)));
        addBuiltinRule(
                "avg",
                (call, staticContext) -> Optional.of(
                        inferStrictAggregateReturnType(call, call.getArguments().get(0))));
        addBuiltinRule(
                "min",
                (call, staticContext) -> Optional.of(
                        inferStrictMinMaxReturnType(call, call.getArguments().get(0), "fn:min")));
        addBuiltinRule(
                "max",
                (call, staticContext) -> Optional.of(
                        inferStrictMinMaxReturnType(call, call.getArguments().get(0), "fn:max")));
        // Cardinality functions keep the item type of their argument and only narrow its arity.
        addBuiltinRule("exactly-one", cardinalityRule(SequenceType.Arity.One));
        addBuiltinRule("zero-or-one", cardinalityRule(SequenceType.Arity.OneOrZero));
        addBuiltinRule("one-or-more", cardinalityRule(SequenceType.Arity.OneOrMore));

        // The map functions keep the key and value types of the maps they build, where their signatures say map(*).
        addMapRule("entry", (call, staticContext) -> inferMapEntryType(call));
        addMapRule("get", (call, staticContext) -> typedMapArgument(call).map(LookupTyping::inferLookupCallType));
        addMapRule("put", (call, staticContext) -> inferMapPutType(call));
        addMapRule("remove", (call, staticContext) -> typedMapArgument(call).map(SequenceType::new));

        // The array functions keep the member type of their array, where their signatures say item* or array(*).
        addArrayRule("get", (member, call) -> member);
        addArrayRule("head", (member, call) -> member);
        for (String localName : List.of("tail", "reverse", "remove", "subarray", "sort", "filter")) {
            addArrayRule(localName, (member, call) -> arrayOf(member));
        }
        addArrayRule(
                "append", (member, call) -> arrayOf(member, call.getArguments().get(1)));
        addArrayRule(
                "insert-before",
                (member, call) -> arrayOf(member, call.getArguments().get(2)));
        addArrayRule(
                "put", (member, call) -> arrayOf(member, call.getArguments().get(2)));

        // For input functions, we read the schema of the data to annotate the static type precisely
        this.rules.put(
                Name.createVariableInDefaultFunctionNamespace("parquet-file"),
                (call, staticContext) -> inferSparkInputType(call, staticContext, path -> {
                    try {
                        return ItemTypeFactory.createItemType(SparkSessionManager.getInstance()
                                .getOrCreateSession()
                                .read()
                                .parquet(path)
                                .schema());
                    } catch (Exception e) {
                        return null;
                    }
                }));
        this.rules.put(
                Name.createVariableInDefaultFunctionNamespace("delta-file"),
                (call, staticContext) -> inferSparkInputType(
                        call,
                        staticContext,
                        path -> ItemTypeFactory.createItemType(SparkSessionManager.getInstance()
                                .getOrCreateSession()
                                .read()
                                .format("delta")
                                .load(path)
                                .schema())));
        this.rules.put(Name.createVariableInDefaultFunctionNamespace("table"), (call, staticContext) -> {
            if (call.getArguments().isEmpty()
                    || !(call.getArguments().get(0) instanceof StringLiteralExpression stringLiteralExpr)) {
                return Optional.empty();
            }
            String name = stringLiteralExpr.getValue();
            SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
            if (session.catalog().tableExists(name) == false) {
                return Optional.empty();
            }

            StructType s = session.read().table(name).schema();
            ItemType schemaItemType = ItemTypeFactory.createItemType(s);
            return Optional.of(new SequenceType(schemaItemType, SequenceType.Arity.ZeroOrMore));
        });
    }

    /** Types a cardinality function by its argument, with the arity narrowed to the one the function enforces. */
    private static ReturnTypeRule cardinalityRule(SequenceType.Arity enforcedArity) {
        return (call, staticContext) -> {
            if (call.getArguments().size() != 1) {
                return Optional.empty();
            }
            SequenceType input = call.getArguments().get(0).getStaticSequenceType();
            if (input.isEmptySequence()) {
                return Optional.empty();
            }
            // The argument keeps its arity if it is already narrower than the enforced one.
            SequenceType.Arity inputArity = input.getArity();
            return Optional.of(new SequenceType(
                    input.getItemType(), inputArity.isSubtypeOf(enforcedArity) ? inputArity : enforcedArity));
        };
    }

    /** Rejects data() of an element whose schema type permits content without a typed value. */
    private void checkTypedValueAvailable(
            FunctionCallExpression call, SequenceType input, StaticContext staticContext) {
        if (input.isEmptySequence()) {
            return;
        }
        for (ItemType member : input.getItemType().getMemberTypes()) {
            if (member instanceof ElementNodeItemType element
                    && element.getSchemaTypeName() != null
                    && staticContext
                            .getInScopeSchemaTypes()
                            .getXmlSchemaCatalog()
                            .mayHaveUnavailableTypedValue(element.getSchemaTypeName())) {
                this.errors.report(
                        "The element type permits content without a typed value.",
                        ErrorCode.AtomizationError,
                        call.getMetadata());
            }
        }
    }

    /** Types doc() of a literal location by the name of its root element; validation supplies its schema later. */
    private Optional<SequenceType> inferDocumentType(FunctionCallExpression call, StaticContext staticContext) {
        if (call.getArguments().size() != 1
                || !(call.getArguments().get(0) instanceof StringLiteralExpression literal)) {
            return Optional.empty();
        }
        try {
            URI uri =
                    FileSystemUtil.resolveURI(staticContext.getStaticBaseURI(), literal.getValue(), call.getMetadata());
            // A supplied/cached document takes precedence over the physical resource at runtime.
            if (this.resourceResolver.document(uri) != null) {
                return Optional.empty();
            }
            try (ResolvedResource resource =
                    this.resourceResolver.resolve(uri, this.configuration, call.getMetadata())) {
                return XmlParsingUtils.peekDocumentElementName(resource.getInputStream())
                        .map(root -> new SequenceType(
                                ItemTypeFactory.documentNodeItemType(ItemTypeFactory.elementNodeItemType(root)),
                                SequenceType.Arity.One));
            }
        } catch (Exception ignored) {
            // Retrieval and parsing errors belong to evaluation, including in unexecuted branches.
            return Optional.empty();
        }
    }

    // A builtin function can be called in the JSONiq default function namespace or in the fn namespace.
    private void addBuiltinRule(String localName, ReturnTypeRule rule) {
        this.rules.put(Name.createVariableInDefaultFunctionNamespace(localName), rule);
        this.rules.put(Name.createVariableInDefaultBuiltinFunctionNamespace(localName), rule);
    }

    /** A function that returns items of its first argument, with a cardinality that depends on the argument's. */
    private void addInputItemsRule(String localName, UnaryOperator<SequenceCardinality> cardinality) {
        addBuiltinRule(localName, (call, staticContext) -> {
            SequenceType input = call.getArguments().get(0).getStaticSequenceType();
            return input.isEmptySequence()
                    ? Optional.of(input)
                    : Optional.of(new SequenceType(input.getItemType(), cardinality.apply(input.getCardinality())));
        });
    }

    private void addMapRule(String localName, ReturnTypeRule rule) {
        this.rules.put(new Name(Name.MAP_NS, "map", localName), rule);
    }

    /** A rule for a function whose first argument is one array, from the type of the array's members. */
    private void addArrayRule(String localName, BiFunction<SequenceType, FunctionCallExpression, SequenceType> rule) {
        this.rules.put(new Name(Name.ARRAY_NS, "array", localName), (call, staticContext) -> arrayMemberType(call)
                .map(member -> rule.apply(member, call)));
    }

    private static Optional<SequenceType> arrayMemberType(FunctionCallExpression call) {
        SequenceType type = call.getArguments().get(0).getStaticSequenceType();
        ItemType array = type.getItemType();
        return type.getArity() == SequenceType.Arity.One && (array.isArrayItemType() || array.isXQueryArrayItemType())
                ? Optional.of(LookupTyping.inferLookupCallType(array))
                : Optional.empty();
    }

    /** An array(M), which also holds a JSON array whose members are items of type M. */
    private static SequenceType arrayOf(SequenceType member) {
        return new SequenceType(ItemTypeFactory.xqueryArrayOf(member));
    }

    /** An array whose members are those of an array, or the value of an argument. */
    private static SequenceType arrayOf(SequenceType member, Expression added) {
        return arrayOf(member.leastCommonSupertypeWith(added.getStaticSequenceType()));
    }

    /** The type of the map passed as the first argument, when it is one map with known key and value types. */
    private static Optional<ItemType> typedMapArgument(FunctionCallExpression call) {
        SequenceType type = call.getArguments().get(0).getStaticSequenceType();
        return type.getArity() == SequenceType.Arity.One
                        && type.getItemType().isMapItemType()
                        && !type.getItemType().equals(BuiltinTypesCatalogue.mapItem)
                ? Optional.of(type.getItemType())
                : Optional.empty();
    }

    /** The type of a key argument, which is atomized. A key that is always empty makes the call fail. */
    private static Optional<ItemType> atomizedKeyType(Expression key) {
        SequenceType type = key.getStaticSequenceType();
        return type.isEmptySequence()
                ? Optional.empty()
                : Optional.of(TypeAtomization.atomizedItemType(type.getItemType()));
    }

    /** map:entry($key, $value) is a map from the key's type to the value's type. */
    private static Optional<SequenceType> inferMapEntryType(FunctionCallExpression call) {
        SequenceType valueType = call.getArguments().get(1).getStaticSequenceType();
        return atomizedKeyType(call.getArguments().get(0)).map(keyType -> mapType(keyType, valueType));
    }

    /** map:put($map, $key, $value) adds the key's type and the value's type to those of the map. */
    private static Optional<SequenceType> inferMapPutType(FunctionCallExpression call) {
        Optional<ItemType> map = typedMapArgument(call);
        Optional<ItemType> keyType = atomizedKeyType(call.getArguments().get(1));
        if (map.isEmpty() || keyType.isEmpty()) {
            return Optional.empty();
        }
        SequenceType valueType = call.getArguments().get(2).getStaticSequenceType();
        return Optional.of(mapType(
                ItemTypeFactory.createInferredUnionType(List.of(map.get().getMapKeyItemType(), keyType.get())),
                map.get().getMapValueSequenceType().leastCommonSupertypeWith(valueType)));
    }

    private static SequenceType mapType(ItemType keyType, SequenceType valueType) {
        return new SequenceType(ItemTypeFactory.mapOf(keyType, valueType));
    }

    /**
     * For specific functions, infers a more precise return type than the signature declares.
     *
     * @param call function call expression to be annotated
     * @return the inferred type, or empty if it is not one of these specific cases
     */
    public Optional<SequenceType> inferSpecificReturnType(FunctionCallExpression call, StaticContext staticContext) {
        ReturnTypeRule rule = this.rules.get(call.getFunctionName());
        return rule == null ? Optional.empty() : rule.infer(call, staticContext);
    }

    /** The type of the items read from a file whose path is a string literal, if the file exists. */
    private static Optional<SequenceType> inferSparkInputType(
            FunctionCallExpression call, StaticContext staticContext, Function<String, ItemType> readSchemaType) {
        if (call.getArguments().isEmpty()
                || !(call.getArguments().get(0) instanceof StringLiteralExpression stringLiteralExpr)) {
            return Optional.empty();
        }
        URI uri = FileSystemUtil.resolveFileSystemURI(
                staticContext.getStaticBaseURI(), stringLiteralExpr.getValue(), call.getMetadata());
        if (!FileSystemUtil.exists(uri, call.getMetadata())) {
            return Optional.empty();
        }
        ItemType schemaItemType = readSchemaType.apply(FileSystemUtil.convertURIToStringForSpark(uri));
        // TODO : check if arity is correct
        return Optional.ofNullable(schemaItemType)
                .map(itemType -> new SequenceType(itemType, SequenceType.Arity.ZeroOrMore));
    }

    private SequenceType validateStrictAggregateInputType(
            FunctionCallExpression expression, Expression inputExpression, String functionName) {
        SequenceType inputType =
                StaticTypeErrors.requireInferredType(inputExpression.getStaticSequenceType(), expression);
        if (inputType.isEmptySequence()) {
            return inputType;
        }

        ItemType inputItemType = normalizeAggregateItemType(inputType.getItemType());
        // The atomized values of nodes and arrays may only be known at runtime, so such inputs are checked per member
        // type.
        boolean validInput = TypeAtomization.containsNodeOrArray(inputType.getItemType())
                ? inputType
                        .getItemType()
                        .allMemberTypesMatch(member -> TypeAtomization.hasUnknownAtomizedType(member)
                                || isStrictAggregateItemType(normalizeAggregateItemType(member)))
                : isStrictAggregateItemType(inputItemType);
        if (!validInput) {
            this.errors.report(
                    functionName
                            + " requires its inferred input sequence type to be empty or have an item type that is a subtype of xs:numeric, xs:yearMonthDuration, or xs:dayTimeDuration, found "
                            + inputType,
                    ErrorCode.InvalidArgumentType,
                    expression.getMetadata());
        }

        return new SequenceType(
                inputItemType, TypeAtomization.inferType(inputType).getCardinality());
    }

    private static boolean isStrictAggregateItemType(ItemType type) {
        return type.isSubtypeOf(BuiltinTypesCatalogue.numericItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem);
    }

    // Aggregates can convert untyped values and produce values outside a derived type's restrictions.
    private ItemType normalizeAggregateItemType(ItemType type) {
        if (type.isUnionType()) {
            return ItemTypeFactory.createInferredUnionType(type.getMemberTypes().stream()
                    .map(this::normalizeAggregateItemType)
                    .collect(Collectors.toList()));
        }
        if (TypeAtomization.containsNodeOrArray(type)) {
            type = TypeAtomization.atomizedItemType(type);
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)) {
            return BuiltinTypesCatalogue.doubleItem;
        }
        if (type.isNumeric()
                || type.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            return type.getCastingPrimitiveType();
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return BuiltinTypesCatalogue.stringItem;
        }
        return type;
    }

    private SequenceType inferSumReturnType(FunctionCallExpression expression) {
        SequenceType inputType = validateStrictAggregateInputType(
                expression, expression.getArguments().get(0), "fn:sum");
        SequenceType zeroType = expression.getArguments().size() > 1
                ? expression.getArguments().get(1).getStaticSequenceType()
                : new SequenceType(BuiltinTypesCatalogue.integerItem);
        if (!zeroType.isEmptySequence() && TypeAtomization.containsNodeOrArray(zeroType.getItemType())) {
            zeroType = TypeAtomization.inferType(zeroType);
            // The zero parameter is atomic?: successful function conversion has
            // already excluded multiple typed values before sum is evaluated.
            zeroType = new SequenceType(
                    zeroType.getItemType(), zeroType.getCardinality().atMostOne());
        }
        if (inputType.isEmptySequence()) {
            return zeroType;
        }
        // A non-empty input sums to one item; an empty input returns the zero argument instead.
        SequenceType sumType = new SequenceType(inputType.getItemType());
        if (!inputType.getCardinality().allowsZero()) {
            return sumType;
        }
        return sumType.leastCommonSupertypeWith(zeroType);
    }

    private SequenceType inferStrictAggregateReturnType(FunctionCallExpression expression, Expression inputExpression) {
        SequenceType inputType = validateStrictAggregateInputType(expression, inputExpression, "fn:avg");
        if (inputType.isEmptySequence()) {
            return SequenceType.createSequenceType("anyAtomicType?");
        }

        ItemType inputItemType = inputType.getItemType();
        ItemType returnItemType = inputItemType.isSubtypeOf(BuiltinTypesCatalogue.numericItem)
                ? BuiltinTypesCatalogue.numericItem
                : inputType.getItemType();

        return new SequenceType(returnItemType, inputType.getCardinality().atMostOne());
    }

    private SequenceType inferStrictMinMaxReturnType(
            FunctionCallExpression expression, Expression inputExpression, String functionName) {
        SequenceType inputType =
                StaticTypeErrors.requireInferredType(inputExpression.getStaticSequenceType(), expression);
        if (inputType.isEmptySequence()) {
            return SequenceType.createSequenceType("anyAtomicType?");
        }

        // Untyped values are compared as xs:double. The atomized values of nodes and arrays are compared at runtime.
        ItemType inputItemType = normalizeAggregateItemType(inputType.getItemType());
        boolean comparedAtRuntime = TypeAtomization.containsNodeOrArray(inputType.getItemType())
                && TypeAtomization.isAtomizable(inputType.getItemType());
        if (!comparedAtRuntime && !ComparisonTyping.hasMutuallyComparableItems(inputItemType)) {
            this.errors.report(
                    functionName
                            + " requires an atomic input type other than xs:anyAtomicType whose member types can be"
                            + " compared with each other, found "
                            + inputType,
                    ErrorCode.InvalidArgumentType,
                    expression.getMetadata());
        }

        return new SequenceType(
                inputItemType,
                TypeAtomization.inferType(inputType).getCardinality().atMostOne());
    }
}
