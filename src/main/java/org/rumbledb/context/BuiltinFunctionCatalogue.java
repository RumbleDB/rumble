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
package org.rumbledb.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.config.model.LanguageVersion;
import org.rumbledb.runtime.functions.FunctionLookupFunctionIterator;
import org.rumbledb.runtime.functions.LoadXQueryModuleFunctionIterator;
import org.rumbledb.runtime.functions.NullFunctionIterator;
import org.rumbledb.runtime.functions.QNameFunctionIterator;
import org.rumbledb.runtime.functions.TransformFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayAppendFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayDescendantFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFilterFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFlattenFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFoldLeftFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFoldRightFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayForEachFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayForEachPairFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayGetFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayHeadFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayInsertBeforeFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayJoinFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayMembersFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayPutFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayRemoveFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayReverseFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArraySizeFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArraySortFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArraySubarrayFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayTailFunctionIterator;
import org.rumbledb.runtime.functions.base.ApplyFunctionIterator;
import org.rumbledb.runtime.functions.booleans.BooleanFunctionIterator;
import org.rumbledb.runtime.functions.booleans.FalseFunctionIterator;
import org.rumbledb.runtime.functions.booleans.NotFunctionIterator;
import org.rumbledb.runtime.functions.booleans.TrueFunctionIterator;
import org.rumbledb.runtime.functions.context.AvailableEnvironmentVariablesFunctionIterator;
import org.rumbledb.runtime.functions.context.DefaultLanguageFunctionIterator;
import org.rumbledb.runtime.functions.context.EnvironmentVariableFunctionIterator;
import org.rumbledb.runtime.functions.context.LastFunctionIterator;
import org.rumbledb.runtime.functions.context.PositionFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.DateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.ParseIETFDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.TimeInMillis;
import org.rumbledb.runtime.functions.datetime.components.AdjustDateTimeToTimezone;
import org.rumbledb.runtime.functions.datetime.components.AdjustDateToTimezone;
import org.rumbledb.runtime.functions.datetime.components.AdjustTimeToTimezone;
import org.rumbledb.runtime.functions.datetime.components.DayFromDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.DayFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.HoursFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.HoursFromTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.MinutesFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.MinutesFromTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.MonthFromDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.MonthFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.SecondsFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.SecondsFromTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.TimezoneFromDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.TimezoneFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.TimezoneFromTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.YearFromDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.components.YearFromDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.dateformatting.FormatDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.dateformatting.FormatDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.dateformatting.FormatTimeFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.DaysFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.HoursFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.ImplicitTimezoneIterator;
import org.rumbledb.runtime.functions.durations.components.MinutesFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.MonthsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.SecondsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.YearsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.error.ThrowErrorIterator;
import org.rumbledb.runtime.functions.input.AvroFileFunctionIterator;
import org.rumbledb.runtime.functions.input.CSVFileFunctionIterator;
import org.rumbledb.runtime.functions.input.DeltaFileFunctionIterator;
import org.rumbledb.runtime.functions.input.DeltaTableFunctionIterator;
import org.rumbledb.runtime.functions.input.IcebergTableFunctionIterator;
import org.rumbledb.runtime.functions.input.JsonLinesFunctionIterator;
import org.rumbledb.runtime.functions.input.LibSVMFileFunctionIterator;
import org.rumbledb.runtime.functions.input.MongoDBCollectionFunctionIterator;
import org.rumbledb.runtime.functions.input.ParallelizeFunctionIterator;
import org.rumbledb.runtime.functions.input.ParquetFileFunctionIterator;
import org.rumbledb.runtime.functions.input.PostgreSQLTableFunctionIterator;
import org.rumbledb.runtime.functions.input.RepartitionFunctionIterator;
import org.rumbledb.runtime.functions.input.RootFileFunctionIterator;
import org.rumbledb.runtime.functions.input.StructuredJsonLinesFunctionIterator;
import org.rumbledb.runtime.functions.input.TextFileFunctionIterator;
import org.rumbledb.runtime.functions.input.XmlFilesFunctionIterator;
import org.rumbledb.runtime.functions.io.CollectionFunctionIterator;
import org.rumbledb.runtime.functions.io.DocAvailableFunctionIterator;
import org.rumbledb.runtime.functions.io.DocFunctionIterator;
import org.rumbledb.runtime.functions.io.LocalTextFileFunctionIterator;
import org.rumbledb.runtime.functions.io.TraceFunctionIterator;
import org.rumbledb.runtime.functions.io.UnparsedTextAvailableFunctionIterator;
import org.rumbledb.runtime.functions.io.UnparsedTextFunctionIterator;
import org.rumbledb.runtime.functions.io.UnparsedTextLinesFunctionIterator;
import org.rumbledb.runtime.functions.io.UriCollectionFunctionIterator;
import org.rumbledb.runtime.functions.io.YamlDocFunctionIterator;
import org.rumbledb.runtime.functions.json.JsonDocFunctionIterator;
import org.rumbledb.runtime.functions.json.JsonToXMLFunctionIterator;
import org.rumbledb.runtime.functions.json.ParseJsonFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapContainsFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapEntryFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapFindFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapForEachFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapGetFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapKeysFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapMergeFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapPutFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapRemoveFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapSizeFunctionIterator;
import org.rumbledb.runtime.functions.nullable.IsNullIterator;
import org.rumbledb.runtime.functions.numerics.AbsFunctionIterator;
import org.rumbledb.runtime.functions.numerics.CeilingFunctionIterator;
import org.rumbledb.runtime.functions.numerics.FloorFunctionIterator;
import org.rumbledb.runtime.functions.numerics.FormatIntegerFunctionIterator;
import org.rumbledb.runtime.functions.numerics.FormatNumberFunctionIterator;
import org.rumbledb.runtime.functions.numerics.NumberFunctionIterator;
import org.rumbledb.runtime.functions.numerics.PiFunctionIterator;
import org.rumbledb.runtime.functions.numerics.RoundFunctionIterator;
import org.rumbledb.runtime.functions.numerics.RoundHalfToEvenFunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.Exp10FunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.ExpFunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.Log10FunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.LogFunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.PowFunctionIterator;
import org.rumbledb.runtime.functions.numerics.exponential.SqrtFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.ACosFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.ASinFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.ATan2FunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.ATanFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.CosFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.CoshFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.SinFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.SinhFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.TanFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.TanhFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectAccumulateFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantPairsFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectIntersectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectProjectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectRemoveKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectValuesFunctionIterator;
import org.rumbledb.runtime.functions.random.RandomNumberGeneratorFunctionIterator;
import org.rumbledb.runtime.functions.random.RandomNumberGeneratorIterator;
import org.rumbledb.runtime.functions.random.RandomSequenceGeneratorIterator;
import org.rumbledb.runtime.functions.random.RandomSequenceWithBoundsAndSeedIterator;
import org.rumbledb.runtime.functions.random.RandomSequenceWithBoundsIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.AvgFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.CountFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.MaxFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.MinFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.SumFunctionIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.ExactlyOneIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.OneOrMoreIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.ZeroOrOneIterator;
import org.rumbledb.runtime.functions.sequences.general.DataFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.EmptyFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ExistsFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.FilterFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.FoldLeftFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.FoldRightFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ForEachFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ForEachPairFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.HeadFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.InsertBeforeFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.RemoveFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ReverseFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.SortFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.SubsequenceFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.TailFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.UnorderedFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.DeepEqualFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.DistinctValuesFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.IndexOfFunctionIterator;
import org.rumbledb.runtime.functions.strings.AnalyzeStringFunctionIterator;
import org.rumbledb.runtime.functions.strings.CodepointEqualFunctionIterator;
import org.rumbledb.runtime.functions.strings.CodepointsToStringFunctionIterator;
import org.rumbledb.runtime.functions.strings.CollationKeyFunctionIterator;
import org.rumbledb.runtime.functions.strings.CompareFunctionIterator;
import org.rumbledb.runtime.functions.strings.ConcatFunctionIterator;
import org.rumbledb.runtime.functions.strings.ContainsFunctionIterator;
import org.rumbledb.runtime.functions.strings.ContainsTokenFunctionIterator;
import org.rumbledb.runtime.functions.strings.DefaultCollationFunctionIterator;
import org.rumbledb.runtime.functions.strings.EncodeForURIFunctionIterator;
import org.rumbledb.runtime.functions.strings.EndsWithFunctionIterator;
import org.rumbledb.runtime.functions.strings.EscapeHTMLURIFunctionIterator;
import org.rumbledb.runtime.functions.strings.IRIToURIFunctionIterator;
import org.rumbledb.runtime.functions.strings.LowerCaseFunctionIterator;
import org.rumbledb.runtime.functions.strings.MatchesFunctionIterator;
import org.rumbledb.runtime.functions.strings.NormalizeSpaceFunctionIterator;
import org.rumbledb.runtime.functions.strings.NormalizeUnicodeFunctionIterator;
import org.rumbledb.runtime.functions.strings.ReplaceFunctionIterator;
import org.rumbledb.runtime.functions.strings.ResolveURIFunctionIterator;
import org.rumbledb.runtime.functions.strings.SerializeFunctionIterator;
import org.rumbledb.runtime.functions.strings.StartsWithFunctionIterator;
import org.rumbledb.runtime.functions.strings.StaticBaseURIFunctionIterator;
import org.rumbledb.runtime.functions.strings.StringFunctionIterator;
import org.rumbledb.runtime.functions.strings.StringJoinFunctionIterator;
import org.rumbledb.runtime.functions.strings.StringLengthFunctionIterator;
import org.rumbledb.runtime.functions.strings.StringToCodepointsFunctionIterator;
import org.rumbledb.runtime.functions.strings.SubstringAfterFunctionIterator;
import org.rumbledb.runtime.functions.strings.SubstringBeforeFunctionIterator;
import org.rumbledb.runtime.functions.strings.SubstringFunctionIterator;
import org.rumbledb.runtime.functions.strings.TokenizeFunctionIterator;
import org.rumbledb.runtime.functions.strings.TranslateFunctionIterator;
import org.rumbledb.runtime.functions.strings.UpperCaseFunctionIterator;
import org.rumbledb.runtime.functions.typing.DynamicItemTypeIterator;
import org.rumbledb.runtime.functions.typing.FunctionArityFunctionIterator;
import org.rumbledb.runtime.functions.typing.FunctionNameFunctionIterator;
import org.rumbledb.runtime.functions.typing.LocalNameFromQNameFunctionIterator;
import org.rumbledb.runtime.functions.typing.NamespaceURIFromQNameFunctionIterator;
import org.rumbledb.runtime.functions.typing.PrefixFromQNameFunctionIterator;
import org.rumbledb.runtime.functions.typing.ResolveQNameFunctionIterator;
import org.rumbledb.runtime.functions.xml.BaseUriFunctionIterator;
import org.rumbledb.runtime.functions.xml.DocumentUriFunctionIterator;
import org.rumbledb.runtime.functions.xml.ElementWithIdFunctionIterator;
import org.rumbledb.runtime.functions.xml.GenerateIdFunctionIterator;
import org.rumbledb.runtime.functions.xml.GetRootFunctionIterator;
import org.rumbledb.runtime.functions.xml.HasChildrenFunctionIterator;
import org.rumbledb.runtime.functions.xml.IdFunctionIterator;
import org.rumbledb.runtime.functions.xml.IdRefFunctionIterator;
import org.rumbledb.runtime.functions.xml.InScopePrefixesFunctionIterator;
import org.rumbledb.runtime.functions.xml.InnermostFunctionIterator;
import org.rumbledb.runtime.functions.xml.LangFunctionIterator;
import org.rumbledb.runtime.functions.xml.LocalNameFunctionIterator;
import org.rumbledb.runtime.functions.xml.NamespaceUriForPrefixFunctionIterator;
import org.rumbledb.runtime.functions.xml.NamespaceUriFunctionIterator;
import org.rumbledb.runtime.functions.xml.NilledFunctionIterator;
import org.rumbledb.runtime.functions.xml.NodeNameFunctionIterator;
import org.rumbledb.runtime.functions.xml.NodeQNameFunctionIterator;
import org.rumbledb.runtime.functions.xml.OutermostFunctionIterator;
import org.rumbledb.runtime.functions.xml.ParseXMLFragmentFunctionIterator;
import org.rumbledb.runtime.functions.xml.ParseXMLFunctionIterator;
import org.rumbledb.runtime.functions.xml.PathFunctionIterator;
import org.rumbledb.runtime.functions.xml.XMLToJsonFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.spark.ml.AnnotateFunctionIterator;
import org.rumbledb.spark.ml.BinaryClassificationMetricsFunctionIterator;
import org.rumbledb.spark.ml.GetEstimatorFunctionIterator;
import org.rumbledb.spark.ml.GetTransformerFunctionIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

public class BuiltinFunctionCatalogue {
    // Declared before the functions, which register themselves as they are defined.
    private static final HashMap<FunctionIdentifier, BuiltinFunction> builtinFunctions = new HashMap<>();

    private static final HashMap<Name, VariadicBuiltinFunction> variadicFunctions = new HashMap<>();

    /** The namespaces in which a function called in the JSONiq default function namespace is looked up, in order. */
    private static final List<String> defaultFunctionNamespaces =
            List.of(Name.FN_NS, Name.JN_NS, Name.MATH_NS, Name.MAP_NS, Name.ARRAY_NS);

    public static Map<FunctionIdentifier, BuiltinFunction> getBuiltinFunctions() {
        return Collections.unmodifiableMap(builtinFunctions);
    }

    /** The function of an identifier, with no resolution of the default function namespace. */
    private static BuiltinFunction lookup(FunctionIdentifier identifier) {
        BuiltinFunction function = builtinFunctions.get(identifier);
        if (function != null) {
            return function;
        }
        VariadicBuiltinFunction variadic = variadicFunctions.get(identifier.getName());
        return variadic == null ? null : variadic.forArity(identifier.getArity());
    }

    /** The function of an identifier, looked up in the builtin namespaces if it is in the default one. */
    private static BuiltinFunction lookupResolvingDefaultNamespace(FunctionIdentifier identifier) {
        BuiltinFunction function = lookup(identifier);
        Name name = identifier.getName();
        if (function != null || !Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(name.getNamespace())) {
            return function;
        }
        for (String namespace : defaultFunctionNamespaces) {
            function = lookup(new FunctionIdentifier(
                    new Name(namespace, name.getPrefix(), name.getLocalName()), identifier.getArity()));
            if (function != null) {
                return function;
            }
        }
        return null;
    }

    public static BuiltinFunction getBuiltinFunction(FunctionIdentifier identifier, LanguageVersion queryLanguage) {
        BuiltinFunction function = lookup(identifier);
        if (function == null) {
            function = resolveConstructorFunction(identifier, queryLanguage);
            if (function != null) {
                return function;
            }
            function = lookupResolvingDefaultNamespace(identifier);
        }
        return function == null ? null : forQueryLanguage(function, queryLanguage);
    }

    private static BuiltinFunction forQueryLanguage(BuiltinFunction function, LanguageVersion queryLanguage) {
        if (StringJoinFunctionIterator.acceptsAtomicItems(queryLanguage)
                && (function == string_join1 || function == string_join2)) {
            return createBuiltinFunction(
                    function.getIdentifier().getName(),
                    function.getIdentifier().getArity() == 1
                            ? List.of("anyAtomicType*")
                            : List.of("anyAtomicType*", "string"),
                    "string",
                    StringJoinFunctionIterator.class,
                    BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
        }
        return function;
    }

    public static BuiltinFunction getBuiltinFunction(FunctionIdentifier identifier, StaticContext staticContext) {
        var constructor = ConstructorFunctionResolver.resolve(identifier, staticContext);
        return constructor == null
                ? getBuiltinFunction(identifier, staticContext.getQueryLanguage())
                : constructor.asBuiltinFunction();
    }

    public static boolean exists(FunctionIdentifier identifier, LanguageVersion queryLanguage) {
        return lookup(identifier) != null
                || resolveConstructorFunction(identifier, queryLanguage) != null
                || lookupResolvingDefaultNamespace(identifier) != null;
    }

    public static boolean exists(FunctionIdentifier identifier, StaticContext staticContext) {
        return getBuiltinFunction(identifier, staticContext) != null;
    }

    // Compatibility adapter for compiler passes that still query all built-in signatures through this catalogue.
    private static BuiltinFunction resolveConstructorFunction(
            FunctionIdentifier identifier, LanguageVersion queryLanguage) {
        var constructor = ConstructorFunctionResolver.resolveBuiltIn(identifier, queryLanguage);
        return constructor == null ? null : constructor.asBuiltinFunction();
    }

    /** A function with any number of arguments from a minimum, all of one type, such as fn:concat. */
    private record VariadicBuiltinFunction(
            Name name,
            int minArity,
            SequenceType parameterType,
            SequenceType returnType,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode executionMode) {

        /** The function with this number of arguments, or null if it takes more. */
        BuiltinFunction forArity(int arity) {
            return arity < this.minArity
                    ? null
                    : new BuiltinFunction(
                            new FunctionIdentifier(this.name, arity),
                            new FunctionSignature(Collections.nCopies(arity, this.parameterType), this.returnType),
                            this.functionIteratorClass,
                            this.executionMode);
        }
    }

    private static Name fn(String localName) {
        return new Name(Name.FN_NS, "fn", localName);
    }

    private static Name jn(String localName) {
        return new Name(Name.JN_NS, "jn", localName);
    }

    private static Name math(String localName) {
        return new Name(Name.MATH_NS, "math", localName);
    }

    private static Name map(String localName) {
        return new Name(Name.MAP_NS, "map", localName);
    }

    private static Name array(String localName) {
        return new Name(Name.ARRAY_NS, "array", localName);
    }

    /** Defines a function, and registers it in the catalogue. */
    private static BuiltinFunction define(
            Name functionName,
            List<String> paramTypes,
            String returnType,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode) {
        return register(createBuiltinFunction(
                functionName, paramTypes, returnType, functionIteratorClass, builtInFunctionExecutionMode));
    }

    /** Defines a function with any number of arguments from a minimum, all of one type, and registers it. */
    private static VariadicBuiltinFunction defineVariadic(
            Name functionName,
            int minArity,
            String parameterType,
            String returnType,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode) {
        VariadicBuiltinFunction function = new VariadicBuiltinFunction(
                functionName,
                minArity,
                SequenceType.createSequenceType(parameterType),
                SequenceType.createSequenceType(returnType),
                functionIteratorClass,
                builtInFunctionExecutionMode);
        if (variadicFunctions.put(functionName, function) != null) {
            throw new IllegalStateException("Duplicate built-in function registration: " + functionName);
        }
        return function;
    }

    private static BuiltinFunction register(BuiltinFunction builtinFunction) {
        if (builtinFunctions.put(builtinFunction.getIdentifier(), builtinFunction) != null) {
            throw new IllegalStateException(
                    "Duplicate built-in function registration: " + builtinFunction.getIdentifier());
        }
        return builtinFunction;
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            List<String> paramTypes,
            String returnType,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode) {
        List<SequenceType> parameterTypes =
                paramTypes.stream().map(SequenceType::createSequenceType).toList();
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, parameterTypes.size()),
                new FunctionSignature(parameterTypes, SequenceType.createSequenceType(returnType)),
                functionIteratorClass,
                builtInFunctionExecutionMode);
    }

    /**
     * function that returns the context position
     */
    static final BuiltinFunction position = define(
            fn("position"),
            List.of(),
            "integer",
            PositionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the context size
     */
    static final BuiltinFunction last = define(
            fn("last"),
            List.of(),
            "integer",
            LastFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * deprecated function that parses a JSON lines file
     * replaced by 'json-lines'
     */
    static final BuiltinFunction json_file1 = define(
            jn("json-file"),
            List.of("string"),
            "item*",
            JsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);

    static final BuiltinFunction json_file2 = define(
            jn("json-file"),
            List.of("string", "integer?"),
            "item*",
            JsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    /**
     * function that parses a JSON lines file
     */
    static final BuiltinFunction json_lines1 = define(
            jn("json-lines"),
            List.of("string"),
            "item*",
            JsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);

    static final BuiltinFunction json_lines2 = define(
            jn("json-lines"),
            List.of("string", "integer?"),
            "item*",
            JsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    /**
     * deprecated function that parses a structured JSON lines file into a DataFrame
     * replaced by 'structured-json-lines'
     */
    static final BuiltinFunction structured_json_file = define(
            jn("structured-json-file"),
            List.of("string"),
            "item*",
            StructuredJsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses a structured JSON lines file into a DataFrame
     */
    static final BuiltinFunction structured_json_lines = define(
            jn("structured-json-lines"),
            List.of("string"),
            "item*",
            StructuredJsonLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses a libSVM formatted file into a DataFrame
     */
    static final BuiltinFunction libsvm_file = define(
            jn("libsvm-file"),
            List.of("string"),
            "item*",
            LibSVMFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses a JSON doc file
     */
    static final BuiltinFunction json_doc = define(
            fn("json-doc"),
            List.of("string?"),
            "item?",
            JsonDocFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction json_doc3 = define(
            fn("json-doc"),
            List.of("string?", "map"),
            "item?",
            JsonDocFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction yaml_doc = define(
            jn("yaml-doc"),
            List.of("string"),
            "item*",
            YamlDocFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction parse_json = define(
            fn("parse-json"),
            List.of("string?"),
            "item?",
            ParseJsonFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction parse_json2 = define(
            fn("parse-json"),
            List.of("string?", "map"),
            "item?",
            ParseJsonFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that parses a xml file
     */
    static final BuiltinFunction doc = define(
            fn("doc"),
            List.of("string?"),
            "document-node()?",
            DocFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that parses multiple xml files into an RDD
     */
    static final BuiltinFunction xml_files = define(
            jn("xml-files"),
            List.of("string"),
            "item*",
            XmlFilesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    /**
     * function that parses multiple xml files into an RDD
     */
    static final BuiltinFunction xml_files2 = define(
            jn("xml-files"),
            List.of("string", "integer?"),
            "item*",
            XmlFilesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);

    static final BuiltinFunction root_with_arg = define(
            fn("root"),
            List.of("node()?"),
            "node()?",
            GetRootFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction root_without_arg = define(
            fn("root"),
            List.of(),
            "node()",
            GetRootFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction name_with_arg = define(
            fn("name"),
            List.of("node()?"),
            "string",
            NodeNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction name_without_arg = define(
            fn("name"),
            List.of(),
            "string",
            NodeNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:nilled (Functions and Operators 3.1, wrapping XDM 3.1 Section 5.8 dm:nilled)
     *
     * fn:nilled() as xs:boolean?
     * fn:nilled($arg as node()?) as xs:boolean?
     *
     * "The dm:nilled accessor returns true if the element node is nilled, false if the element
     * node is not nilled, or the empty sequence if the concept of nilled does not apply."
     *
     * See {@code https://www.w3.org/TR/xpath-functions-31/#func-nilled}.
     */
    static final BuiltinFunction nilled_without_arg = define(
            fn("nilled"),
            List.of(),
            "boolean?",
            NilledFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction nilled_with_arg = define(
            fn("nilled"),
            List.of("item?"),
            "boolean?",
            NilledFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:node-name (Functions and Operators 3.1, wrapping XDM 3.1 Section 5.10 dm:node-name)
     *
     * fn:node-name() as xs:QName?
     * fn:node-name($arg as node()?) as xs:QName?
     *
     * "The dm:node-name accessor returns the name of the node as an xs:QName, or the empty
     * sequence if the node does not have a name."
     *
     * See {@code https://www.w3.org/TR/xpath-functions-31/#func-node-name}.
     */
    static final BuiltinFunction node_name_without_arg = define(
            fn("node-name"),
            List.of(),
            "QName?",
            NodeQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction node_name_with_arg = define(
            fn("node-name"),
            List.of("item?"),
            "QName?",
            NodeQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:QName (Functions and Operators 3.1)
     *
     * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-QName">func-QName</a>
     */
    static final BuiltinFunction qname = define(
            fn("QName"),
            List.of("string?", "string"),
            "QName",
            QNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    // Optional dynamic execution facilities remain discoverable while disabled.
    static final BuiltinFunction load_xquery_module1 = define(
            fn("load-xquery-module"),
            List.of("string"),
            "map",
            LoadXQueryModuleFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction load_xquery_module2 = define(
            fn("load-xquery-module"),
            List.of("string", "map"),
            "map",
            LoadXQueryModuleFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction transform = define(
            fn("transform"),
            List.of("map"),
            "map",
            TransformFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:function-lookup (Functions and Operators 3.1)
     *
     * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-function-lookup">func-function-lookup</a>
     */
    static final BuiltinFunction function_lookup = define(
            fn("function-lookup"),
            List.of("QName", "integer"),
            "function(*)?",
            FunctionLookupFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:base-uri (Functions and Operators 3.1, wrapping XDM 3.1 Section 5.2 dm:base-uri)
     *
     * fn:base-uri() as xs:anyURI?
     * fn:base-uri($arg as node()?) as xs:anyURI?
     *
     * "The dm:base-uri accessor returns the value of the base-uri property of the node, if it
     * has one; otherwise it returns the empty sequence."
     *
     * See {@code https://www.w3.org/TR/xpath-functions-31/#func-base-uri}.
     */
    static final BuiltinFunction base_uri_without_arg = define(
            fn("base-uri"),
            List.of(),
            "anyURI?",
            BaseUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction base_uri_with_arg = define(
            fn("base-uri"),
            List.of("item?"),
            "anyURI?",
            BaseUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:document-uri (Functions and Operators 3.1, wrapping XDM 3.1 Section 5.4 dm:document-uri)
     *
     * fn:document-uri() as xs:anyURI?
     * fn:document-uri($arg as node()?) as xs:anyURI?
     *
     * "The dm:document-uri accessor returns the value of the document-uri property of a
     * document node, if it has one; otherwise it returns the empty sequence."
     *
     * See {@code https://www.w3.org/TR/xpath-functions-31/#func-document-uri}.
     */
    static final BuiltinFunction document_uri_without_arg = define(
            fn("document-uri"),
            List.of(),
            "anyURI?",
            DocumentUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction document_uri_with_arg = define(
            fn("document-uri"),
            List.of("item?"),
            "anyURI?",
            DocumentUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * fn:in-scope-prefixes (Functions and Operators 3.1 Section 14.1)
     * fn:in-scope-prefixes($element as element()) as xs:string*
     * Returns the prefixes of the in-scope namespaces for $element.
     */
    static final BuiltinFunction in_scope_prefixes = define(
            fn("in-scope-prefixes"),
            List.of("element()"),
            "string*",
            InScopePrefixesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that parses a text file
     */
    static final BuiltinFunction unparsed_text = define(
            fn("unparsed-text"),
            List.of("string?"),
            "string?",
            UnparsedTextFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction unparsed_text2 = define(
            fn("unparsed-text"),
            List.of("string?", "string"),
            "string?",
            UnparsedTextFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction unparsed_text_lines = define(
            fn("unparsed-text-lines"),
            List.of("string?"),
            "string*",
            UnparsedTextLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    static final BuiltinFunction unparsed_text_lines2 = define(
            fn("unparsed-text-lines"),
            List.of("string?", "string"),
            "string*",
            UnparsedTextLinesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    static final BuiltinFunction text_file1 = define(
            jn("text-file"),
            List.of("string"),
            "item*",
            TextFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    static final BuiltinFunction text_file2 = define(
            jn("text-file"),
            List.of("string", "integer?"),
            "item*",
            TextFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    /**
     * function that parses a text file locally
     */
    static final BuiltinFunction local_text_file = define(
            jn("local-text-file"),
            List.of("string"),
            "string*",
            LocalTextFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that parallelizes item collections into a Spark RDD
     */
    static final BuiltinFunction parallelizeFunction1 = define(
            jn("parallelize"),
            List.of("item*"),
            "item*",
            ParallelizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);

    static final BuiltinFunction parallelizeFunction2 = define(
            jn("parallelize"),
            List.of("item*", "integer"),
            "item*",
            ParallelizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD);
    /**
     * function that parses a parquet file
     */
    static final BuiltinFunction parquet_file1 = define(
            jn("parquet-file"),
            List.of("string"),
            "item*",
            ParquetFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses a parquet file
     */
    static final BuiltinFunction parquet_file2 = define(
            jn("parquet-file"),
            List.of("string", "integer"),
            "item*",
            ParquetFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses a delta file
     */
    static final BuiltinFunction delta_file = define(
            jn("delta-file"),
            List.of("string"),
            "item*",
            DeltaFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses a hive registered delta table
     */
    static final BuiltinFunction delta_table = define(
            jn("table"),
            List.of("string"),
            "item*",
            DeltaTableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses an iceberg table
     */
    static final BuiltinFunction iceberg_table = define(
            jn("iceberg-table"),
            List.of("string"),
            "item*",
            IcebergTableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses a csv file
     */
    static final BuiltinFunction csv_file1 = define(
            jn("csv-file"),
            List.of("string"),
            "item*",
            CSVFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses a csv file
     */
    static final BuiltinFunction csv_file2 = define(
            jn("csv-file"),
            List.of("string", "object"),
            "item*",
            CSVFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses an avro file
     */
    static final BuiltinFunction avro_file1 = define(
            jn("avro-file"),
            List.of("string"),
            "item*",
            AvroFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses an avro file
     */
    static final BuiltinFunction avro_file2 = define(
            jn("avro-file"),
            List.of("string", "object"),
            "item*",
            AvroFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that parses a ROOT file
     */
    static final BuiltinFunction root_file1 = define(
            jn("root-file"),
            List.of("string"),
            "item*",
            RootFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that parses a ROOT file
     */
    static final BuiltinFunction root_file2 = define(
            jn("root-file"),
            List.of("string", "string"),
            "item*",
            RootFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that opens a table from PostgreSQL.
     */
    static final BuiltinFunction postgresql_table2 = define(
            jn("postgresql-table"),
            List.of("string", "string"),
            "object*",
            PostgreSQLTableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that opens a table from PostgreSQL.
     */
    static final BuiltinFunction postgresql_table3 = define(
            jn("postgresql-table"),
            List.of("string", "string", "integer"),
            "object*",
            PostgreSQLTableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that opens a table from PostgreSQL.
     */
    static final BuiltinFunction mongodb_collection2 = define(
            jn("mongodb-collection"),
            List.of("string", "string"),
            "object*",
            MongoDBCollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);
    /**
     * function that opens a table from PostgreSQL.
     */
    static final BuiltinFunction mongodb_collection3 = define(
            jn("mongodb-collection"),
            List.of("string", "string", "integer"),
            "object*",
            MongoDBCollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    /**
     * function that returns the length of a sequence
     */
    static final BuiltinFunction count = define(
            fn("count"),
            List.of("item*"),
            "integer",
            CountFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the effective boolean value of the given parameter
     */
    static final BuiltinFunction boolean_function = define(
            fn("boolean"),
            List.of("item*"),
            "boolean",
            BooleanFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns true if the effective boolean value of the given parameter is false, and false if it is
     * true.
     */
    static final BuiltinFunction not_function = define(
            fn("not"),
            List.of("item*"),
            "boolean",
            NotFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns true.
     */
    static final BuiltinFunction true_function = define(
            fn("true"),
            List.of(),
            "boolean",
            TrueFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns false.
     */
    static final BuiltinFunction false_function = define(
            fn("false"),
            List.of(),
            "boolean",
            FalseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the minimum of a sequence
     */
    static final BuiltinFunction min1 = define(
            fn("min"),
            List.of("anyAtomicType*"),
            "anyAtomicType?",
            MinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction min2 = define(
            fn("min"),
            List.of("anyAtomicType*", "string"),
            "anyAtomicType?",
            MinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the maximum of a sequence
     */
    static final BuiltinFunction max1 = define(
            fn("max"),
            List.of("anyAtomicType*"),
            "anyAtomicType?",
            MaxFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction max2 = define(
            fn("max"),
            List.of("anyAtomicType*", "string"),
            "anyAtomicType?",
            MaxFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the average of a sequence
     */
    static final BuiltinFunction avg = define(
            fn("avg"),
            List.of("anyAtomicType*"),
            "anyAtomicType?",
            AvgFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the sum of a sequence
     */
    static final BuiltinFunction sum1 = define(
            fn("sum"),
            List.of("anyAtomicType*"),
            "anyAtomicType?",
            SumFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction sum2 = define(
            fn("sum"),
            List.of("anyAtomicType*", "anyAtomicType?"),
            "anyAtomicType?",
            SumFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns true if the argument is the empty sequence
     */
    static final BuiltinFunction empty = define(
            fn("empty"),
            List.of("item*"),
            "boolean",
            EmptyFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns true if the argument is a non-empty sequence
     */
    static final BuiltinFunction exists = define(
            fn("exists"),
            List.of("item*"),
            "boolean",
            ExistsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the first item in a sequence
     */
    static final BuiltinFunction head = define(
            fn("head"),
            List.of("item*"),
            "item?",
            HeadFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns all but the first item in a sequence
     */
    static final BuiltinFunction tail = define(
            fn("tail"),
            List.of("item*"),
            "item*",
            TailFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns the items of a given sequence
     */
    static final BuiltinFunction unordered = define(
            fn("unordered"),
            List.of("item*"),
            "item*",
            UnorderedFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns a sequence constructed by inserting an item or a sequence of items at a given position
     * within an existing sequence
     */
    static final BuiltinFunction insert_before = define(
            fn("insert-before"),
            List.of("item*", "integer", "item*"),
            "item*",
            InsertBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns a new sequence containing all the items of $target except the item at position
     * $position.
     */
    static final BuiltinFunction remove = define(
            fn("remove"),
            List.of("item*", "integer"),
            "item*",
            RemoveFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * `
     * function that reverses the order of items in a sequence.
     */
    static final BuiltinFunction reverse = define(
            fn("reverse"),
            List.of("item*"),
            "item*",
            ReverseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that applies a subsequence operation to the given sequence with the given start index and length
     * parameters
     */
    static final BuiltinFunction subsequence2 = define(
            fn("subsequence"),
            List.of("item*", "double"),
            "item*",
            SubsequenceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction subsequence3 = define(
            fn("subsequence"),
            List.of("item*", "double", "double"),
            "item*",
            SubsequenceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    /**
     * function that returns $arg if it contains zero or one items. Otherwise, raises an error.
     */
    static final BuiltinFunction zero_or_one = define(
            fn("zero-or-one"),
            List.of("item*"),
            "item?",
            ZeroOrOneIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns $arg if it contains one or more items. Otherwise, raises an error.
     */
    static final BuiltinFunction one_or_more = define(
            fn("one-or-more"),
            List.of("item*"),
            "item+",
            OneOrMoreIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns $arg if it contains exactly one item. Otherwise, raises an error.
     */
    static final BuiltinFunction exactly_one = define(
            fn("exactly-one"),
            List.of("item*"),
            "item",
            ExactlyOneIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the values that appear in a sequence, with duplicates eliminated
     */
    static final BuiltinFunction distinct_values1 = define(
            fn("distinct-values"),
            List.of("anyAtomicType*"),
            "anyAtomicType*",
            DistinctValuesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction distinct_values2 = define(
            fn("distinct-values"),
            List.of("anyAtomicType*", "string"),
            "anyAtomicType*",
            DistinctValuesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    /**
     * function that returns indices of items that are equal to the search parameter
     */
    static final BuiltinFunction index_of1 = define(
            fn("index-of"),
            List.of("anyAtomicType*", "anyAtomicType"),
            "integer*",
            IndexOfFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction index_of2 = define(
            fn("index-of"),
            List.of("anyAtomicType*", "anyAtomicType", "string"),
            "integer*",
            IndexOfFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns whether two sequences are deep-equal to each other
     */
    static final BuiltinFunction deep_equal1 = define(
            fn("deep-equal"),
            List.of("item*", "item*"),
            "boolean",
            DeepEqualFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction deep_equal2 = define(
            fn("deep-equal"),
            List.of("item*", "item*", "string"),
            "boolean",
            DeepEqualFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the absolute value of the arg
     */
    static final BuiltinFunction abs = define(
            fn("abs"),
            List.of("numeric?"),
            "numeric?",
            AbsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that rounds $arg upwards to a whole number
     */
    static final BuiltinFunction ceiling = define(
            fn("ceiling"),
            List.of("numeric?"),
            "numeric?",
            CeilingFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that rounds $arg downwards to a whole number
     */
    static final BuiltinFunction floor = define(
            fn("floor"),
            List.of("numeric?"),
            "numeric?",
            FloorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that rounds a value to a specified number of decimal places, rounding upwards if two such values are
     * equally near
     */
    static final BuiltinFunction round1 = define(
            fn("round"),
            List.of("numeric?"),
            "numeric?",
            RoundFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction round2 = define(
            fn("round"),
            List.of("numeric?", "integer"),
            "numeric?",
            RoundFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that rounds a value to a specified number of decimal places, rounding to make the last digit even if
     * two such values are equally nearinT
     */
    static final BuiltinFunction round_half_to_even1 = define(
            fn("round-half-to-even"),
            List.of("numeric?"),
            "numeric?",
            RoundHalfToEvenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction round_half_to_even2 = define(
            fn("round-half-to-even"),
            List.of("numeric?", "integer"),
            "numeric?",
            RoundHalfToEvenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the approximation the mathematical constant
     */
    static final BuiltinFunction pi = define(
            math("pi"),
            List.of(),
            "double",
            PiFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the value of e^x
     */
    static final BuiltinFunction exp = define(
            math("exp"),
            List.of("double?"),
            "double?",
            ExpFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the value of 10^x
     */
    static final BuiltinFunction exp10 = define(
            math("exp10"),
            List.of("double?"),
            "double?",
            Exp10FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the natural logarithm of the argument
     */
    static final BuiltinFunction log = define(
            math("log"),
            List.of("double?"),
            "double?",
            LogFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the base-ten logarithm of the argument
     */
    static final BuiltinFunction log10 = define(
            math("log10"),
            List.of("double?"),
            "double?",
            Log10FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the result of raising the first argument to the power of the second
     */
    static final BuiltinFunction pow = define(
            math("pow"),
            List.of("double?", "double"),
            "double?",
            PowFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the non-negative square root of the argument
     */
    static final BuiltinFunction sqrt = define(
            math("sqrt"),
            List.of("double?"),
            "double?",
            SqrtFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the sine of the angle given in radians
     */
    static final BuiltinFunction sin = define(
            math("sin"),
            List.of("double?"),
            "double?",
            SinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the cosine of the angle given in radians
     */
    static final BuiltinFunction cos = define(
            math("cos"),
            List.of("double?"),
            "double?",
            CosFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the tangent of the angle given in radians
     */
    static final BuiltinFunction tan = define(
            math("tan"),
            List.of("double?"),
            "double?",
            TanFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the arc sine of the angle given in radians
     */
    static final BuiltinFunction asin = define(
            math("asin"),
            List.of("double?"),
            "double?",
            ASinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the arc cosine of the angle given in radians
     */
    static final BuiltinFunction acos = define(
            math("acos"),
            List.of("double?"),
            "double?",
            ACosFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the arc tangent of the angle given in radians
     */
    static final BuiltinFunction atan = define(
            math("atan"),
            List.of("double?"),
            "double?",
            ATanFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the the angle in radians subtended at the origin by the point on a plane with
     * coordinates (x, y) and the positive x-axis.
     */
    static final BuiltinFunction atan2 = define(
            math("atan2"),
            List.of("double", "double"),
            "double",
            ATan2FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the hyperbolic cosine of the angle given in radians
     */
    static final BuiltinFunction cosh = define(
            jn("cosh"),
            List.of("double?"),
            "double?",
            CoshFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the hyperbolic sine of the angle given in radians
     */
    static final BuiltinFunction sinh = define(
            jn("sinh"),
            List.of("double?"),
            "double?",
            SinhFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the hyperbolic tangent of the angle given in radians
     */
    static final BuiltinFunction tanh = define(
            jn("tanh"),
            List.of("double?"),
            "double?",
            TanhFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns string
     */
    static final BuiltinFunction string0 = define(
            fn("string"),
            List.of(),
            "string",
            StringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction string1 = define(
            fn("string"),
            List.of("item?"),
            "string",
            StringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns substrings
     */
    static final BuiltinFunction substring2 = define(
            fn("substring"),
            List.of("string?", "double"),
            "string",
            SubstringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction substring3 = define(
            fn("substring"),
            List.of("string?", "double", "double"),
            "string",
            SubstringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the part of the first variable that precedes the first occurrence of the second
     * variable.
     */
    static final BuiltinFunction substring_before1 = define(
            fn("substring-before"),
            List.of("string?", "string?"),
            "string",
            SubstringBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction substring_before2 = define(
            fn("substring-before"),
            List.of("string?", "string?", "string"),
            "string",
            SubstringBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the part of the first variable that follows the first occurrence of the second
     * vairable.
     */
    static final BuiltinFunction substring_after1 = define(
            fn("substring-after"),
            List.of("string?", "string?"),
            "string",
            SubstringAfterFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction substring_after2 = define(
            fn("substring-after"),
            List.of("string?", "string?", "string"),
            "string",
            SubstringAfterFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns substrings
     */
    static final VariadicBuiltinFunction concat = defineVariadic(
            fn("concat"),
            2,
            "anyAtomicType?",
            "string",
            ConcatFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that converts codepoints to a string
     */
    static final BuiltinFunction codepoints_to_string = define(
            fn("codepoints-to-string"),
            List.of("integer*"),
            "string",
            CodepointsToStringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that converts a string to codepoints
     */
    static final BuiltinFunction string_to_codepoints = define(
            fn("string-to-codepoints"),
            List.of("string?"),
            "integer*",
            StringToCodepointsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction analyze_string1 = define(
            fn("analyze-string"),
            List.of("string?", "string"),
            "item",
            AnalyzeStringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction analyze_string2 = define(
            fn("analyze-string"),
            List.of("string?", "string", "string"),
            "item",
            AnalyzeStringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that compares Strings codepoint-by-codepoint
     */
    static final BuiltinFunction codepoint_equal = define(
            fn("codepoint-equal"),
            List.of("string?", "string?"),
            "boolean?",
            CodepointEqualFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string created by concatenating the items in a sequence, with an optional defined
     * separator between adjacent items.
     */
    static final BuiltinFunction string_join1 = define(
            fn("string-join"),
            List.of("string*"),
            "string",
            StringJoinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction string_join2 = define(
            fn("string-join"),
            List.of("string*", "string"),
            "string",
            StringJoinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that replaces parts of a string according to a regex expression
     */
    static final BuiltinFunction replace1 = define(
            fn("replace"),
            List.of("string?", "string", "string"),
            "string",
            ReplaceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that replaces parts of a string according to a regex expression
     */
    static final BuiltinFunction replace2 = define(
            fn("replace"),
            List.of("string?", "string", "string", "string"),
            "string",
            ReplaceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the string length
     */
    static final BuiltinFunction string_length0 = define(
            fn("string-length"),
            List.of(),
            "integer",
            StringLengthFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction string_length1 = define(
            fn("string-length"),
            List.of("string?"),
            "integer",
            StringLengthFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns tokens
     */
    static final BuiltinFunction tokenize1 = define(
            fn("tokenize"),
            List.of("string?"),
            "string*",
            TokenizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction tokenize2 = define(
            fn("tokenize"),
            List.of("string?", "string"),
            "string*",
            TokenizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction tokenize3 = define(
            fn("tokenize"),
            List.of("string?", "string", "string"),
            "string*",
            TokenizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that turns all upper-case characters to lower-case
     */
    static final BuiltinFunction lower_case = define(
            fn("lower-case"),
            List.of("string?"),
            "string",
            LowerCaseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that turns all lower-case characters to upper-case
     */
    static final BuiltinFunction upper_case = define(
            fn("upper-case"),
            List.of("string?"),
            "string",
            UpperCaseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that turns all upper-case characters to upper-case
     */
    static final BuiltinFunction translate = define(
            fn("translate"),
            List.of("string?", "string", "string"),
            "string",
            TranslateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that performs Unicode normalization
     */
    static final BuiltinFunction normalize_unicode1 = define(
            fn("normalize-unicode"),
            List.of("string?"),
            "string",
            NormalizeUnicodeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction normalize_unicode2 = define(
            fn("normalize-unicode"),
            List.of("string?", "string"),
            "string",
            NormalizeUnicodeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that encodes reserved characters
     */
    static final BuiltinFunction encode_for_uri = define(
            fn("encode-for-uri"),
            List.of("string?"),
            "string",
            EncodeForURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that resolves IRI reference against an absolute IRI
     */
    static final BuiltinFunction resolve_uri1 = define(
            fn("resolve-uri"),
            List.of("string?"),
            "anyURI?",
            ResolveURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction resolve_uri2 = define(
            fn("resolve-uri"),
            List.of("string?", "string"),
            "anyURI?",
            ResolveURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the value of the Static Base URI property from the static context.
     */
    static final BuiltinFunction static_base_uri = define(
            fn("static-base-uri"),
            List.of(),
            "anyURI?",
            StaticBaseURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that checks whether a string ends with a substring
     */
    static final BuiltinFunction ends_with1 = define(
            fn("ends-with"),
            List.of("string?", "string?"),
            "boolean",
            EndsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction ends_with2 = define(
            fn("ends-with"),
            List.of("string?", "string?", "string"),
            "boolean",
            EndsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that checks whether a string starts with a substring
     */
    static final BuiltinFunction starts_with1 = define(
            fn("starts-with"),
            List.of("string?", "string?"),
            "boolean",
            StartsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction starts_with2 = define(
            fn("starts-with"),
            List.of("string?", "string?", "string"),
            "boolean",
            StartsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that checks whether a string contains a substring
     */
    static final BuiltinFunction contains1 = define(
            fn("contains"),
            List.of("string?", "string?"),
            "boolean",
            ContainsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction contains2 = define(
            fn("contains"),
            List.of("string?", "string?", "string"),
            "boolean",
            ContainsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that checks whether a string collates before or after another string
     */
    static final BuiltinFunction compare1 = define(
            fn("compare"),
            List.of("string?", "string?"),
            "integer",
            CompareFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction compare2 = define(
            fn("compare"),
            List.of("string?", "string?", "string"),
            "integer",
            CompareFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that checks whether a string matches a regular expression
     */
    static final BuiltinFunction matches1 = define(
            fn("matches"),
            List.of("string?", "string"),
            "boolean",
            MatchesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that checks whether a string matches a regular expression
     */
    static final BuiltinFunction matches2 = define(
            fn("matches"),
            List.of("string?", "string", "string"),
            "boolean",
            MatchesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that normalizes spaces in a string
     */
    static final BuiltinFunction normalize_space0 = define(
            fn("normalize-space"),
            List.of(),
            "string",
            NormalizeSpaceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction normalize_space1 = define(
            fn("normalize-space"),
            List.of("string?"),
            "string",
            NormalizeSpaceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that serializes a given input sequence
     */
    static final BuiltinFunction serialize = define(
            fn("serialize"),
            List.of("item*"),
            "string",
            SerializeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction serialize2 = define(
            fn("serialize"),
            List.of("item*", "item?"),
            "string",
            SerializeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that serializes a given input sequence
     */
    static final BuiltinFunction data0 = define(
            fn("data"),
            List.of(),
            "anyAtomicType*",
            DataFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction data1 = define(
            fn("data"),
            List.of("item*"),
            "anyAtomicType*",
            DataFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns the value of the default collation property
     */
    static final BuiltinFunction default_collation = define(
            fn("default-collation"),
            List.of(),
            "string",
            DefaultCollationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that that returns the double representation of the input string or number
     */
    static final BuiltinFunction number0 = define(
            fn("number"),
            List.of(),
            "double",
            NumberFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction number1 = define(
            fn("number"),
            List.of("anyAtomicType?"),
            "double",
            NumberFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the years from a duration
     */
    static final BuiltinFunction years_from_duration = define(
            fn("years-from-duration"),
            List.of("duration?"),
            "integer?",
            YearsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the months from a duration
     */
    static final BuiltinFunction months_from_duration = define(
            fn("months-from-duration"),
            List.of("duration?"),
            "integer?",
            MonthsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the days from a duration
     */
    static final BuiltinFunction days_from_duration = define(
            fn("days-from-duration"),
            List.of("duration?"),
            "integer?",
            DaysFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the hours from a duration
     */
    static final BuiltinFunction hours_from_duration = define(
            fn("hours-from-duration"),
            List.of("duration?"),
            "integer?",
            HoursFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the minutes from a duration
     */
    static final BuiltinFunction minutes_from_duration = define(
            fn("minutes-from-duration"),
            List.of("duration?"),
            "integer?",
            MinutesFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a duration
     */
    static final BuiltinFunction seconds_from_duration = define(
            fn("seconds-from-duration"),
            List.of("duration?"),
            "decimal?",
            SecondsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the current dateTime item
     */
    static final BuiltinFunction current_dateTime = define(
            fn("current-dateTime"),
            List.of(),
            "dateTime?",
            CurrentDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing a dateTime value formated for display
     */
    static final BuiltinFunction format_dateTime = define(
            fn("format-dateTime"),
            List.of("dateTime?", "string"),
            "string?",
            FormatDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing a dateTime value formated for display
     */
    static final BuiltinFunction format_dateTime5 = define(
            fn("format-dateTime"),
            List.of("dateTime?", "string", "string?", "string?", "string?"),
            "string?",
            FormatDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a xs:dateTime value created by combining an xs:date and an xs:time
     */
    static final BuiltinFunction dateTime = define(
            fn("dateTime"),
            List.of("date?", "time?"),
            "dateTime?",
            DateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the year from a dateTime
     */
    static final BuiltinFunction year_from_dateTime = define(
            fn("year-from-dateTime"),
            List.of("dateTime?"),
            "integer?",
            YearFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the month from a dateTime
     */
    static final BuiltinFunction month_from_dateTime = define(
            fn("month-from-dateTime"),
            List.of("dateTime?"),
            "integer?",
            MonthFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the day from a dateTime
     */
    static final BuiltinFunction day_from_dateTime = define(
            fn("day-from-dateTime"),
            List.of("dateTime?"),
            "integer?",
            DayFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the hours from a dateTime
     */
    static final BuiltinFunction hours_from_dateTime = define(
            fn("hours-from-dateTime"),
            List.of("dateTime?"),
            "integer?",
            HoursFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the minutes from a dateTime
     */
    static final BuiltinFunction minutes_from_dateTime = define(
            fn("minutes-from-dateTime"),
            List.of("dateTime?"),
            "integer?",
            MinutesFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a dateTime
     */
    static final BuiltinFunction seconds_from_dateTime = define(
            fn("seconds-from-dateTime"),
            List.of("dateTime?"),
            "decimal?",
            SecondsFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a dateTime
     */
    static final BuiltinFunction timezone_from_dateTime = define(
            fn("timezone-from-dateTime"),
            List.of("dateTime?"),
            "dayTimeDuration?",
            TimezoneFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_dateTime_to_timezone1 = define(
            fn("adjust-dateTime-to-timezone"),
            List.of("dateTime?"),
            "dateTime?",
            AdjustDateTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction adjust_dateTime_to_timezone2 = define(
            fn("adjust-dateTime-to-timezone"),
            List.of("dateTime?", "dayTimeDuration?"),
            "dateTime?",
            AdjustDateTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the current date item
     */
    static final BuiltinFunction current_date = define(
            fn("current-date"),
            List.of(),
            "date?",
            CurrentDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a string containing a numeric value formatted for display
     */
    static final BuiltinFunction format_number = define(
            fn("format-number"),
            List.of("numeric?", "string"),
            "string",
            FormatNumberFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a string containing a numeric value formatted for display
     */
    static final BuiltinFunction format_number3 = define(
            fn("format-number"),
            List.of("numeric?", "string", "string?"),
            "string",
            FormatNumberFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a string containing an integer value formatted for display
     */
    static final BuiltinFunction format_integer = define(
            fn("format-integer"),
            List.of("integer?", "string"),
            "string",
            FormatIntegerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing an integer value formatted for display
     */
    static final BuiltinFunction format_integer3 = define(
            fn("format-integer"),
            List.of("integer?", "string", "string?"),
            "string",
            FormatIntegerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a string containing a date value formatted for display
     */
    static final BuiltinFunction format_date = define(
            fn("format-date"),
            List.of("date?", "string"),
            "string?",
            FormatDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing a date value formatted for display
     */
    static final BuiltinFunction format_date5 = define(
            fn("format-date"),
            List.of("date?", "string", "string?", "string?", "string?"),
            "string?",
            FormatDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the year from a date
     */
    static final BuiltinFunction year_from_date = define(
            fn("year-from-date"),
            List.of("date?"),
            "integer?",
            YearFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the month from a date
     */
    static final BuiltinFunction month_from_date = define(
            fn("month-from-date"),
            List.of("date?"),
            "integer?",
            MonthFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the day from a date
     */
    static final BuiltinFunction day_from_date = define(
            fn("day-from-date"),
            List.of("date?"),
            "integer?",
            DayFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a date
     */
    static final BuiltinFunction timezone_from_date = define(
            fn("timezone-from-date"),
            List.of("date?"),
            "dayTimeDuration?",
            TimezoneFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that adjusts a date value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_date_to_timezone1 = define(
            fn("adjust-date-to-timezone"),
            List.of("date?"),
            "date?",
            AdjustDateToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction adjust_date_to_timezone2 = define(
            fn("adjust-date-to-timezone"),
            List.of("date?", "dayTimeDuration?"),
            "date?",
            AdjustDateToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the current time item
     */
    static final BuiltinFunction current_time = define(
            fn("current-time"),
            List.of(),
            "time?",
            CurrentTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing a time value formated for display
     */
    static final BuiltinFunction format_time = define(
            fn("format-time"),
            List.of("time?", "string"),
            "string?",
            FormatTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns a string containing a time value formated for display
     */
    static final BuiltinFunction format_time5 = define(
            fn("format-time"),
            List.of("time?", "string", "string?", "string?", "string?"),
            "string?",
            FormatTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the hours from a time
     */
    static final BuiltinFunction hours_from_time = define(
            fn("hours-from-time"),
            List.of("time?"),
            "integer?",
            HoursFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the minutes from a time
     */
    static final BuiltinFunction minutes_from_time = define(
            fn("minutes-from-time"),
            List.of("time?"),
            "integer?",
            MinutesFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a time
     */
    static final BuiltinFunction seconds_from_time = define(
            fn("seconds-from-time"),
            List.of("time?"),
            "decimal?",
            SecondsFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the seconds from a time
     */
    static final BuiltinFunction timezone_from_time = define(
            fn("timezone-from-time"),
            List.of("time?"),
            "dayTimeDuration?",
            TimezoneFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that adjusts a time value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_time_to_timezone1 = define(
            fn("adjust-time-to-timezone"),
            List.of("time?"),
            "time?",
            AdjustTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction adjust_time_to_timezone2 = define(
            fn("adjust-time-to-timezone"),
            List.of("time?", "dayTimeDuration?"),
            "time?",
            AdjustTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the value of the implicit timezone property from the dynamic context.
     */
    static final BuiltinFunction implicit_timezone = define(
            fn("implicit-timezone"),
            List.of(),
            "dayTimeDuration?",
            ImplicitTimezoneIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns the keys of a Json Object
     */
    static final BuiltinFunction keys = define(
            jn("keys"),
            List.of("item*"),
            "string*",
            ObjectKeysFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL);
    /**
     * function that returns returns all members of all arrays of the supplied sequence
     */
    static final BuiltinFunction members = define(
            jn("members"),
            List.of("item*"),
            "item*",
            ArrayMembersFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    /**
     * W3C map:get — F&O 3.1: map:get($map as map(*), $key as xs:anyAtomicType) as item()*.
     */
    static final BuiltinFunction map_get = define(
            map("get"),
            List.of("map", "anyAtomicType"),
            "item*",
            MapGetFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:contains — F&O 3.1: map:contains($map as map(*), $key as xs:anyAtomicType) as xs:boolean.
     */
    static final BuiltinFunction map_contains = define(
            map("contains"),
            List.of("map", "anyAtomicType"),
            "boolean",
            MapContainsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:put — F&O 3.1: map:put($map as map(*), $key as xs:anyAtomicType, $value as item()*) as map(*).
     */
    static final BuiltinFunction map_put = define(
            map("put"),
            List.of("map", "anyAtomicType", "item*"),
            "map",
            MapPutFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:remove — F&O 3.1: map:remove($map as map(*), $keys as xs:anyAtomicType*) as map(*).
     */
    static final BuiltinFunction map_remove = define(
            map("remove"),
            List.of("map", "anyAtomicType*"),
            "map",
            MapRemoveFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:merge — F&O 3.1: map:merge($maps as map(*)*) as map(*).
     */
    static final BuiltinFunction map_merge_1 = define(
            map("merge"),
            List.of("map*"),
            "map",
            MapMergeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:merge — F&O 3.1: map:merge($maps as map(*)*, $options as map(*)) as map(*).
     */
    static final BuiltinFunction map_merge_2 = define(
            map("merge"),
            List.of("map*", "map"),
            "map",
            MapMergeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:entry — F&O 3.1: map:entry($key as xs:anyAtomicType, $value as item()*) as map(*).
     */
    static final BuiltinFunction map_entry = define(
            map("entry"),
            List.of("anyAtomicType", "item*"),
            "map",
            MapEntryFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * W3C map:keys — F&O 3.1: map:keys($map as map(*)) as xs:anyAtomicType*.
     */
    static final BuiltinFunction map_keys = define(
            map("keys"),
            List.of("map"),
            "anyAtomicType*",
            MapKeysFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * W3C map:size — F&O 3.1: map:size($map as map(*)) as xs:integer.
     */
    static final BuiltinFunction map_size = define(
            map("size"),
            List.of("map"),
            "integer",
            MapSizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:for-each — F&O 3.1:
     * map:for-each($map as map(*), $action as function(xs:anyAtomicType, item()*) as item()*) as item()*.
     */
    static final BuiltinFunction map_for_each = define(
            map("for-each"),
            List.of("map", "function(anyAtomicType, item*) as item*"),
            "item*",
            MapForEachFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C map:find -- F&O 3.1: map:find($input as item()*, $key as xs:anyAtomicType) as array(*).
     */
    static final BuiltinFunction map_find_2 = define(
            map("find"),
            List.of("item*", "anyAtomicType"),
            "array",
            MapFindFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the JSON null
     */
    static final BuiltinFunction null_function = define(
            jn("null"),
            List.of(),
            "null?",
            NullFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns the length of an array
     */
    static final BuiltinFunction size = define(
            jn("size"),
            List.of("item-array?"),
            "integer?",
            ArraySizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:size function that returns the length of an array
     */
    static final BuiltinFunction array_size = define(
            array("size"),
            List.of("array*"),
            "integer",
            ArraySizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:tail function that returns all members except the first
     */
    static final BuiltinFunction array_tail = define(
            array("tail"),
            List.of("array"),
            "array",
            ArrayTailFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:get function that returns members at a given position
     */
    static final BuiltinFunction array_get = define(
            array("get"),
            List.of("array", "integer"),
            "item*",
            ArrayGetFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:head function that returns the first member of an array
     */
    static final BuiltinFunction array_head = define(
            array("head"),
            List.of("array"),
            "item*",
            ArrayHeadFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:reverse function that returns all members of an array in reverse order
     */
    static final BuiltinFunction array_reverse = define(
            array("reverse"),
            List.of("array"),
            "array",
            ArrayReverseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:insert-before function that inserts a member before a given position
     */
    static final BuiltinFunction array_insert_before = define(
            array("insert-before"),
            List.of("array", "integer", "item*"),
            "array",
            ArrayInsertBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:append — appends a member (a sequence) to an array, returning a new array.
     */
    static final BuiltinFunction array_append = define(
            array("append"),
            List.of("array", "item*"),
            "array",
            ArrayAppendFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:remove — returns a new array omitting members at the given 1-based positions (F&amp;O 3.1).
     */
    static final BuiltinFunction array_remove = define(
            array("remove"),
            List.of("array", "integer*"),
            "array",
            ArrayRemoveFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:put — returns a new array with the member at a 1-based position replaced (F&amp;O 3.1).
     */
    static final BuiltinFunction array_put = define(
            array("put"),
            List.of("array", "integer", "item*"),
            "array",
            ArrayPutFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:subarray function that extracts a subarray
     */
    static final BuiltinFunction array_subarray_2 = define(
            array("subarray"),
            List.of("array", "integer"),
            "array",
            ArraySubarrayFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:subarray function that extracts a subarray of a given length
     */
    static final BuiltinFunction array_subarray_3 = define(
            array("subarray"),
            List.of("array", "integer", "integer"),
            "array",
            ArraySubarrayFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:fold-left function that evaluates a function cumulatively over array members
     */
    static final BuiltinFunction array_fold_left = define(
            array("fold-left"),
            List.of("array", "item*", "function(item*, item*) as item*"),
            "item*",
            ArrayFoldLeftFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:fold-right function that evaluates a function cumulatively over array members from right to left
     */
    static final BuiltinFunction array_fold_right = define(
            array("fold-right"),
            List.of("array", "item*", "function(item*, item*) as item*"),
            "item*",
            ArrayFoldRightFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:for-each — F&amp;O 3.1: array:for-each($array as array(*), $action as function(item()*) as item()*) as
     * array(*).
     * Returns an array of the same size; each member is $action applied to the corresponding member of $array.
     */
    static final BuiltinFunction array_for_each = define(
            array("for-each"),
            List.of("array", "function(item*) as item*"),
            "array",
            ArrayForEachFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:for-each-pair — F&amp;O 3.1: array:for-each-pair($array1 as array(*), $array2 as array(*),
     * $function as function(item()*, item()*) as item()*) as array(*).
     */
    static final BuiltinFunction array_for_each_pair = define(
            array("for-each-pair"),
            List.of("array", "array", "function(item*, item*) as item*"),
            "array",
            ArrayForEachPairFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:filter — F&amp;O 3.1: array:filter($array as array(*), $predicate as function(item()*) as xs:boolean)
     * as
     * array(*). Map-as-predicate is not supported yet.
     */
    static final BuiltinFunction array_filter = define(
            array("filter"),
            List.of("array", "function(item*) as boolean"),
            "array",
            ArrayFilterFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:join — F&amp;O 3.1: array:join($arrays as array(*)*) as array(*).
     */
    static final BuiltinFunction array_join = define(
            array("join"),
            List.of("array*"),
            "array",
            ArrayJoinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:flatten — F&amp;O 3.1: array:flatten($input as item()*) as item()*.
     */
    static final BuiltinFunction array_flatten = define(
            array("flatten"),
            List.of("item*"),
            "item*",
            ArrayFlattenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * W3C array:sort — F&amp;O 3.1: array:sort($array as array(*)) as array(*).
     */
    static final BuiltinFunction array_sort_1 = define(
            array("sort"),
            List.of("array"),
            "array",
            ArraySortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:sort — F&amp;O 3.1: array:sort($array as array(*), $collation as xs:string?) as array(*).
     */
    static final BuiltinFunction array_sort_2 = define(
            array("sort"),
            List.of("array", "string?"),
            "array",
            ArraySortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * W3C array:sort — F&amp;O 3.1: array:sort($array as array(*), $collation as xs:string?,
     * $key as function(item()*) as xs:anyAtomicType*) as array(*).
     * Third parameter is registered as {@code item} so map/array function items reach runtime (like
     * {@code array:filter}).
     */
    static final BuiltinFunction array_sort_3 = define(
            array("sort"),
            List.of("array", "string?", "item"),
            "array",
            ArraySortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that dynamically creates an object that merges the values of key collisions into arrays
     */
    static final BuiltinFunction accumulate = define(
            jn("accumulate"),
            List.of("item*"),
            "object",
            ObjectAccumulateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that returns all arrays contained within the supplied items, regardless of depth.
     */
    static final BuiltinFunction descendant_arrays = define(
            jn("descendant-arrays"),
            List.of("item*"),
            "item*",
            ArrayDescendantFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns all objects contained within the supplied items, regardless of depth
     */
    static final BuiltinFunction descendant_objects = define(
            jn("descendant-objects"),
            List.of("item*"),
            "item*",
            ObjectDescendantFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns all objects contained within the supplied items, regardless of depth
     */
    static final BuiltinFunction descendant_pairs = define(
            jn("descendant-pairs"),
            List.of("item*"),
            "item*",
            ObjectDescendantPairsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that recursively flattens arrays in the input sequence, leaving non-arrays intact
     */
    static final BuiltinFunction flatten = define(
            jn("flatten"),
            List.of("item*"),
            "item*",
            ArrayFlattenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns the intersection of the supplied objects, and aggregates values corresponding to the
     * same name into an array
     */
    static final BuiltinFunction intersect = define(
            jn("intersect"),
            List.of("item*"),
            "object",
            ObjectIntersectFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    /**
     * function that projects objects by filtering their pairs and leaves non-objects intact
     */
    static final BuiltinFunction project = define(
            jn("project"),
            List.of("item*", "string*"),
            "item*",
            ObjectProjectFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that removes the pairs with the given keys from all objects and leaves non-objects intact
     */
    static final BuiltinFunction remove_keys = define(
            jn("remove-keys"),
            List.of("item*", "string*"),
            "item*",
            ObjectRemoveKeysFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);
    /**
     * function that returns the values of a Json Object
     */
    static final BuiltinFunction values = define(
            jn("values"),
            List.of("item*"),
            "item*",
            ObjectValuesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    /**
     * function fetches the transformer class from SparkML API
     */
    static final BuiltinFunction get_transformer = define(
            jn("get-transformer"),
            List.of("string"),
            "function(object*, object) as object*",
            GetTransformerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function fetches the transformer class from SparkML API
     */
    static final BuiltinFunction get_transformer2 = define(
            jn("get-transformer"),
            List.of("string", "object"),
            "function(object*, object) as object*",
            GetTransformerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function fetches the estimator class from SparkML API
     */
    static final BuiltinFunction get_estimator = define(
            jn("get-estimator"),
            List.of("string"),
            "function(object*, object) as function(object*, object) as object*",
            GetEstimatorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function fetches the estimator class from SparkML API
     */
    static final BuiltinFunction get_estimator2 = define(
            jn("get-estimator"),
            List.of("string", "object"),
            "function(object*, object) as function(object*, object) as object*",
            GetEstimatorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function converts given RDD or local data to a DataFrame using a schema
     */
    static final BuiltinFunction annotate = define(
            jn("annotate"),
            List.of(
                    "object*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
                    "object"),
            "object*",
            // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            AnnotateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME);

    static final BuiltinFunction trace1 = define(
            fn("trace"),
            List.of("item*"),
            // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            "item*",
            // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            TraceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction trace2 = define(
            fn("trace"),
            List.of(
                    "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
                    "string"),
            "item*",
            // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            TraceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction repartition = define(
            jn("repartition"),
            List.of("item*", "integer"),
            "item*",
            RepartitionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction binary_classification_metrics1 = define(
            jn("binary-classification-metrics"),
            List.of("object*", "string", "string"),
            "item*",
            BinaryClassificationMetricsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction binary_classification_metrics2 = define(
            jn("binary-classification-metrics"),
            List.of("object*", "string", "string", "integer"),
            "item*",
            BinaryClassificationMetricsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a random number
     */
    static final BuiltinFunction random = define(
            fn("random"),
            List.of(),
            "double",
            RandomNumberGeneratorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a sequence of random numbers
     */
    static final BuiltinFunction random_sequence_generator = define(
            fn("random"),
            List.of("integer"),
            "item*",
            RandomSequenceGeneratorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a sequence of random numbers using a seed
     */
    static final BuiltinFunction random_sequence_generator_with_seed = define(
            fn("seeded_random"),
            List.of("integer", "integer"),
            "item*",
            RandomSequenceGeneratorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    /**
     * function that returns a sequence of random numbers using a low and high bound, while also allowing to limit the
     * number of elements generated and their type
     */
    static final BuiltinFunction random_sequence_with_bounds_double = define(
            fn("random-between"),
            List.of("double", "double", "integer", "string"),
            "item*",
            RandomSequenceWithBoundsIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction random_sequence_with_bounds_seeded_int = define(
            fn("random-between"),
            List.of("integer", "integer", "integer", "string", "integer"),
            "item*",
            RandomSequenceWithBoundsAndSeedIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction error = define(
            fn("error"),
            List.of(),
            "error",
            ThrowErrorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction error_with_code = define(
            fn("error"),
            List.of("QName?"),
            "error",
            ThrowErrorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction error_with_code_and_description = define(
            fn("error"),
            List.of("QName?", "string"),
            "error",
            ThrowErrorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction error_with_code_description_and_object = define(
            fn("error"),
            List.of("QName?", "string", "item*"),
            "error",
            ThrowErrorIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction item_type = define(
            fn("item-type"),
            List.of("item*"),
            "string",
            DynamicItemTypeIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction is_null = define(
            jn("is-null"),
            List.of("item*"),
            "boolean",
            IsNullIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction current_time_millis = define(
            jn("current-time-milis"),
            List.of(),
            "integer",
            TimeInMillis.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
    static final BuiltinFunction collation_key1 = define(
            fn("collation-key"),
            List.of("string"),
            "base64Binary",
            CollationKeyFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction collation_key2 = define(
            fn("collation-key"),
            List.of("string", "string"),
            "base64Binary",
            CollationKeyFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction contains_token2 = define(
            fn("contains-token"),
            List.of("string*", "string"),
            "boolean",
            ContainsTokenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction contains_token3 = define(
            fn("contains-token"),
            List.of("string*", "string", "string"),
            "boolean",
            ContainsTokenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction iri_to_uri = define(
            fn("iri-to-uri"),
            List.of("string?"),
            "string",
            IRIToURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction escape_html_uri = define(
            fn("escape-html-uri"),
            List.of("string?"),
            "string",
            EscapeHTMLURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction filter = define(
            fn("filter"),
            List.of("item*", "function(item) as boolean"),
            "item*",
            FilterFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction fold_left = define(
            fn("fold-left"),
            List.of("item*", "item*", "function(item*, item) as item*"),
            "item*",
            FoldLeftFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction fold_right = define(
            fn("fold-right"),
            List.of("item*", "item*", "function(item, item*) as item*"),
            "item*",
            FoldRightFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction for_each = define(
            fn("for-each"),
            List.of("item*", "function(item) as item*"),
            "item*",
            ForEachFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction for_each_pair = define(
            fn("for-each-pair"),
            List.of("item*", "item*", "function(item, item) as item*"),
            "item*",
            ForEachPairFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction sort1 = define(
            fn("sort"),
            List.of("item*"),
            "item*",
            SortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction sort2 = define(
            fn("sort"),
            List.of("item*", "string?"),
            "item*",
            SortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction sort3 = define(
            fn("sort"),
            List.of("item*", "string?", "function(item) as anyAtomicType*"),
            "item*",
            SortFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT);

    static final BuiltinFunction local_name_without_arg = define(
            fn("local-name"),
            List.of(),
            "string",
            LocalNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction local_name_with_arg = define(
            fn("local-name"),
            List.of("node()?"),
            "string",
            LocalNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction namespace_uri_without_arg = define(
            fn("namespace-uri"),
            List.of(),
            "anyURI",
            NamespaceUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction namespace_uri_with_arg = define(
            fn("namespace-uri"),
            List.of("item?"),
            "anyURI",
            NamespaceUriFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction lang1 = define(
            fn("lang"),
            List.of("string?"),
            "boolean",
            LangFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction lang2 = define(
            fn("lang"),
            List.of("string?", "item"),
            "boolean",
            LangFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction path_without_arg = define(
            fn("path"),
            List.of(),
            "string?",
            PathFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction path_with_arg = define(
            fn("path"),
            List.of("item?"),
            "string?",
            PathFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction has_children_without_arg = define(
            fn("has-children"),
            List.of(),
            "boolean",
            HasChildrenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction has_children_with_arg = define(
            fn("has-children"),
            List.of("node()?"),
            "boolean",
            HasChildrenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction innermost = define(
            fn("innermost"),
            List.of("node()*"),
            "node()*",
            InnermostFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction outermost = define(
            fn("outermost"),
            List.of("node()*"),
            "node()*",
            OutermostFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction id1 = define(
            fn("id"),
            List.of("string*"),
            "item*",
            IdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction id2 = define(
            fn("id"),
            List.of("string*", "item"),
            "item*",
            IdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction element_with_id1 = define(
            fn("element-with-id"),
            List.of("string*"),
            "item*",
            ElementWithIdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction element_with_id2 = define(
            fn("element-with-id"),
            List.of("string*", "item"),
            "item*",
            ElementWithIdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction idref1 = define(
            fn("idref"),
            List.of("string*"),
            "item*",
            IdRefFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction idref2 = define(
            fn("idref"),
            List.of("string*", "item"),
            "item*",
            IdRefFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction generate_id_without_arg = define(
            fn("generate-id"),
            List.of(),
            "string",
            GenerateIdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction generate_id_with_arg = define(
            fn("generate-id"),
            List.of("node()?"),
            "string",
            GenerateIdFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction namespace_uri_for_prefix = define(
            fn("namespace-uri-for-prefix"),
            List.of("string?", "item"),
            "anyURI?",
            NamespaceUriForPrefixFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction resolve_QName = define(
            fn("resolve-QName"),
            List.of("string?", "item"),
            "QName?",
            ResolveQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction prefix_from_QName = define(
            fn("prefix-from-QName"),
            List.of("QName?"),
            "NCName?",
            PrefixFromQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction local_name_from_QName = define(
            fn("local-name-from-QName"),
            List.of("QName?"),
            "NCName?",
            LocalNameFromQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction namespace_uri_from_QName = define(
            fn("namespace-uri-from-QName"),
            List.of("QName?"),
            "anyURI?",
            NamespaceURIFromQNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction function_name = define(
            fn("function-name"),
            List.of("function"),
            "QName?",
            FunctionNameFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction function_arity = define(
            fn("function-arity"),
            List.of("function"),
            "integer",
            FunctionArityFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction collection0 = define(
            fn("collection"),
            List.of(),
            "item*",
            CollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction collection1 = define(
            fn("collection"),
            List.of("string?"),
            "item*",
            CollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction doc_available = define(
            fn("doc-available"),
            List.of("string?"),
            "boolean",
            DocAvailableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction unparsed_text_available1 = define(
            fn("unparsed-text-available"),
            List.of("string?"),
            "boolean",
            UnparsedTextAvailableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction unparsed_text_available2 = define(
            fn("unparsed-text-available"),
            List.of("string?", "string"),
            "boolean",
            UnparsedTextAvailableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction uri_collection0 = define(
            fn("uri-collection"),
            List.of(),
            "anyURI*",
            UriCollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction uri_collection1 = define(
            fn("uri-collection"),
            List.of("string?"),
            "anyURI*",
            UriCollectionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction environment_variable = define(
            fn("environment-variable"),
            List.of("string"),
            "string?",
            EnvironmentVariableFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction available_environment_variables = define(
            fn("available-environment-variables"),
            List.of(),
            "string*",
            AvailableEnvironmentVariablesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction parse_xml = define(
            fn("parse-xml"),
            List.of("string?"),
            "document-node()?",
            ParseXMLFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction parse_xml_fragment = define(
            fn("parse-xml-fragment"),
            List.of("string?"),
            "document-node()?",
            ParseXMLFragmentFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction default_language = define(
            fn("default-language"),
            List.of(),
            "language",
            DefaultLanguageFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction apply = define(
            fn("apply"),
            List.of("function", "array"),
            "item*",
            ApplyFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction random_number_generator0 = define(
            fn("random-number-generator"),
            List.of(),
            "map",
            RandomNumberGeneratorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction random_number_generator1 = define(
            fn("random-number-generator"),
            List.of("anyAtomicType?"),
            "map",
            RandomNumberGeneratorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction parse_ietf_date = define(
            fn("parse-ietf-date"),
            List.of("string?"),
            "dateTime?",
            ParseIETFDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction json_to_xml1 = define(
            fn("json-to-xml"),
            List.of("string?"),
            "item?",
            JsonToXMLFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction json_to_xml2 = define(
            fn("json-to-xml"),
            List.of("string?", "map"),
            "item?",
            JsonToXMLFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction xml_to_json1 = define(
            fn("xml-to-json"),
            List.of("item?"),
            "string?",
            XMLToJsonFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);

    static final BuiltinFunction xml_to_json2 = define(
            fn("xml-to-json"),
            List.of("item?", "map"),
            "string?",
            XMLToJsonFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
}
