package org.rumbledb.context;

import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.NullFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayDescendantFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFlattenFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayMembersFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArraySizeFunctionIterator;
import org.rumbledb.runtime.functions.booleans.BooleanFunctionIterator;
import org.rumbledb.runtime.functions.booleans.FalseFunctionIterator;
import org.rumbledb.runtime.functions.booleans.NotFunctionIterator;
import org.rumbledb.runtime.functions.booleans.TrueFunctionIterator;
import org.rumbledb.runtime.functions.context.LastFunctionIterator;
import org.rumbledb.runtime.functions.context.PositionFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.DateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatTimeFunctionIterator;
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
import org.rumbledb.runtime.functions.delta_lake.CreateDeltaLakeTableFunctionIterator;
import org.rumbledb.runtime.functions.delta_lake.DeleteDeltaLakeTableFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.DaysFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.HoursFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.ImplicitTimezoneIterator;
import org.rumbledb.runtime.functions.durations.components.MinutesFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.MonthsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.SecondsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.YearsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.error.ThrowErrorIterator;
import org.rumbledb.runtime.functions.input.*;
import org.rumbledb.runtime.functions.io.DebugFunctionIterator;
import org.rumbledb.runtime.functions.io.JsonDocFunctionIterator;
import org.rumbledb.runtime.functions.io.LocalTextFileFunctionIterator;
import org.rumbledb.runtime.functions.io.ParseJsonFunctionIterator;
import org.rumbledb.runtime.functions.io.TraceFunctionIterator;
import org.rumbledb.runtime.functions.io.UnparsedTextFunctionIterator;
import org.rumbledb.runtime.functions.io.DocFunctionIterator;
import org.rumbledb.runtime.functions.io.YamlDocFunctionIterator;
import org.rumbledb.runtime.functions.nullable.IsNullIterator;
import org.rumbledb.runtime.functions.numerics.AbsFunctionIterator;
import org.rumbledb.runtime.functions.numerics.CeilingFunctionIterator;
import org.rumbledb.runtime.functions.numerics.FloorFunctionIterator;
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
import org.rumbledb.runtime.functions.object.ObjectAccumulateFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantPairsFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectIntersectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectProjectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectRemoveKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectValuesFunctionIterator;
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
import org.rumbledb.runtime.functions.sequences.general.*;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;
import org.rumbledb.runtime.functions.sequences.value.DeepEqualFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.DistinctValuesFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.IndexOfFunctionIterator;
import org.rumbledb.runtime.functions.strings.*;
import org.rumbledb.runtime.functions.typing.DynamicItemTypeIterator;
import org.rumbledb.runtime.functions.xml.GetRootFunctionIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;
import sparksoniq.spark.ml.AnnotateFunctionIterator;
import sparksoniq.spark.ml.BinaryClassificationMetricsFunctionIterator;
import sparksoniq.spark.ml.GetEstimatorFunctionIterator;
import sparksoniq.spark.ml.GetTransformerFunctionIterator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class BuiltinFunctionCatalogue {
    private static final HashMap<FunctionIdentifier, BuiltinFunction> builtinFunctions;

    public static BuiltinFunction getBuiltinFunction(FunctionIdentifier identifier) {
        if (builtinFunctions.containsKey(identifier)) {
            return builtinFunctions.get(identifier);
        }
        Name name = identifier.getName();
        if (name.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)) {
            FunctionIdentifier fn = new FunctionIdentifier(
                    new Name(Name.FN_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(fn)) {
                return builtinFunctions.get(fn);
            }
            FunctionIdentifier jn = new FunctionIdentifier(
                    new Name(Name.JN_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(jn)) {
                return builtinFunctions.get(jn);
            }
            FunctionIdentifier math = new FunctionIdentifier(
                    new Name(Name.MATH_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(math)) {
                return builtinFunctions.get(math);
            }
            FunctionIdentifier map = new FunctionIdentifier(
                    new Name(Name.MAP_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(map)) {
                return builtinFunctions.get(map);
            }
            FunctionIdentifier array = new FunctionIdentifier(
                    new Name(Name.ARRAY_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(array)) {
                return builtinFunctions.get(array);
            }
        }
        return null;
    }

    public static boolean exists(FunctionIdentifier identifier) {
        if (builtinFunctions.containsKey(identifier)) {
            return true;
        }
        Name name = identifier.getName();
        if (name.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)) {
            FunctionIdentifier fn = new FunctionIdentifier(
                    new Name(Name.FN_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(fn)) {
                return true;
            }
            FunctionIdentifier jn = new FunctionIdentifier(
                    new Name(Name.JN_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(jn)) {
                return true;
            }
            FunctionIdentifier math = new FunctionIdentifier(
                    new Name(Name.MATH_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(math)) {
                return true;
            }
            FunctionIdentifier map = new FunctionIdentifier(
                    new Name(Name.MAP_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(map)) {
                return true;
            }
            FunctionIdentifier array = new FunctionIdentifier(
                    new Name(Name.ARRAY_NS, name.getPrefix(), name.getLocalName()),
                    identifier.getArity()
            );
            if (builtinFunctions.containsKey(array)) {
                return true;
            }
        }
        return false;
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 0),
                new FunctionSignature(
                        Collections.emptyList(),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String param1Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 1),
                new FunctionSignature(
                        Collections.singletonList(SequenceType.createSequenceType(param1Type)),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String param1Type,
            String param2Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 2),
                new FunctionSignature(
                        Collections.unmodifiableList(
                            Arrays.asList(
                                SequenceType.createSequenceType(param1Type),
                                SequenceType.createSequenceType(param2Type)
                            )
                        ),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String param1Type,
            String param2Type,
            String param3Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 3),
                new FunctionSignature(
                        Collections.unmodifiableList(
                            Arrays.asList(
                                SequenceType.createSequenceType(param1Type),
                                SequenceType.createSequenceType(param2Type),
                                SequenceType.createSequenceType(param3Type)
                            )
                        ),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String param1Type,
            String param2Type,
            String param3Type,
            String param4Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 4),
                new FunctionSignature(
                        Collections.unmodifiableList(
                            Arrays.asList(
                                SequenceType.createSequenceType(param1Type),
                                SequenceType.createSequenceType(param2Type),
                                SequenceType.createSequenceType(param3Type),
                                SequenceType.createSequenceType(param4Type)
                            )
                        ),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            Name functionName,
            String param1Type,
            String param2Type,
            String param3Type,
            String param4Type,
            String param5Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 5),
                new FunctionSignature(
                        Collections.unmodifiableList(
                            Arrays.asList(
                                SequenceType.createSequenceType(param1Type),
                                SequenceType.createSequenceType(param2Type),
                                SequenceType.createSequenceType(param3Type),
                                SequenceType.createSequenceType(param4Type),
                                SequenceType.createSequenceType(param5Type)
                            )
                        ),
                        SequenceType.createSequenceType(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    /**
     * function that returns the context position
     */
    static final BuiltinFunction position = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "position"),
        "integer",
        PositionFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the context size
     */
    static final BuiltinFunction last = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "last"),
        "integer",
        LastFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * deprecated function that parses a JSON lines file
     * replaced by 'json-lines'
     */
    static final BuiltinFunction json_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "json-file"),
        "string",
        "item*",
        JsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    static final BuiltinFunction json_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "json-file"),
        "string",
        "integer?",
        "item*",
        JsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    /**
     * function that parses a JSON lines file
     */
    static final BuiltinFunction json_lines1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "json-lines"),
        "string",
        "item*",
        JsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    static final BuiltinFunction json_lines2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "json-lines"),
        "string",
        "integer?",
        "item*",
        JsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    /**
     * deprecated function that parses a structured JSON lines file into a DataFrame
     * replaced by 'structured-json-lines'
     */
    static final BuiltinFunction structured_json_file = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "structured-json-file"),
        "string",
        "item*",
        StructuredJsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses a structured JSON lines file into a DataFrame
     */
    static final BuiltinFunction structured_json_lines = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "structured-json-lines"),
        "string",
        "item*",
        StructuredJsonLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses a libSVM formatted file into a DataFrame
     */
    static final BuiltinFunction libsvm_file = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "libsvm-file"),
        "string",
        "item*",
        LibSVMFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses a JSON doc file
     */
    static final BuiltinFunction json_doc = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "json-doc"),
        "string",
        "item*",
        JsonDocFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction yaml_doc = createBuiltinFunction(
        new Name(Name.JN_NS, "fn", "yaml-doc"),
        "string",
        "item*",
        YamlDocFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction parse_json = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "parse-json"),
        "string?",
        "item?",
        ParseJsonFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that parses a xml file
     */
    static final BuiltinFunction doc = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "doc"),
        "string?",
        "item*",
        DocFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that parses multiple xml files into an RDD
     */
    static final BuiltinFunction xml_files = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "xml-files"),
        "string",
        "item*",
        XmlFilesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    /**
     * function that parses multiple xml files into an RDD
     */
    static final BuiltinFunction xml_files2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "xml-files"),
        "string",
        "integer?",
        "item*",
        XmlFilesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );

    static final BuiltinFunction root_with_arg = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "root"),
        "item",
        "item",
        GetRootFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction root_without_arg = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "root"),
        "item",
        GetRootFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that parses a text file
     */
    static final BuiltinFunction unparsed_text = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "unparsed-text"),
        "string?",
        "string?",
        UnparsedTextFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction unparsed_text_lines = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "unparsed-text-lines"),
        "string?",
        "string*",
        UnparsedTextLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    static final BuiltinFunction text_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "text-file"),
        "string",
        "item*",
        UnparsedTextLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    static final BuiltinFunction text_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "text-file"),
        "string",
        "integer?",
        "item*",
        UnparsedTextLinesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    /**
     * function that parses a text file locally
     */
    static final BuiltinFunction local_text_file = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "local-text-file"),
        "string",
        "string*",
        LocalTextFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that parallelizes item collections into a Spark RDD
     */
    static final BuiltinFunction parallelizeFunction1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "parallelize"),
        "item*",
        "item*",
        ParallelizeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    static final BuiltinFunction parallelizeFunction2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "parallelize"),
        "item*",
        "integer",
        "item*",
        ParallelizeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.RDD
    );
    /**
     * function that parses a parquet file
     */
    static final BuiltinFunction parquet_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "parquet-file"),
        "string",
        "item*",
        ParquetFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that parses a parquet file
     */
    static final BuiltinFunction parquet_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "parquet-file"),
        "string",
        "integer",
        "item*",
        ParquetFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that parses a delta file
     */
    static final BuiltinFunction delta_file = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "delta-file"),
        "string",
        "item*",
        DeltaFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that parses a hive registered delta table
     */
    static final BuiltinFunction delta_table = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "delta-table"),
        "string",
        "item*",
        DeltaTableFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that parses a csv file
     */
    static final BuiltinFunction csv_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "csv-file"),
        "string",
        "item*",
        CSVFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses a csv file
     */
    static final BuiltinFunction csv_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "csv-file"),
        "string",
        "object",
        "item*",
        CSVFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses an avro file
     */
    static final BuiltinFunction avro_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "avro-file"),
        "string",
        "item*",
        AvroFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses an avro file
     */
    static final BuiltinFunction avro_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "avro-file"),
        "string",
        "object",
        "item*",
        AvroFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that parses a ROOT file
     */
    static final BuiltinFunction root_file1 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "root-file"),
        "string",
        "item*",
        RootFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );
    /**
     * function that parses a ROOT file
     */
    static final BuiltinFunction root_file2 = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "root-file"),
        "string",
        "string",
        "item*",
        RootFileFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    /**
     * function that returns the length of a sequence
     */
    static final BuiltinFunction count = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "count"),
        "item*",
        "integer",
        CountFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the effective boolean value of the given parameter
     */
    static final BuiltinFunction boolean_function = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "boolean"),
        "item*",
        "boolean",
        BooleanFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns true if the effective boolean value of the given parameter is false, and false if it is
     * true.
     */
    static final BuiltinFunction not_function = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "not"),
        "item*",
        "boolean",
        NotFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns true.
     */
    static final BuiltinFunction true_function = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "true"),
        "boolean",
        TrueFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns false.
     */
    static final BuiltinFunction false_function = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "false"),
        "boolean",
        FalseFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the minimum of a sequence
     */
    static final BuiltinFunction min1 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "min"),
        "item*",
        "anyAtomicType?",
        MinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction min2 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "min"),
        "item*",
        "string",
        "anyAtomicType?",
        MinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the maximum of a sequence
     */
    static final BuiltinFunction max1 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "max"),
        "item*",
        "anyAtomicType?",
        MaxFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction max2 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "max"),
        "item*",
        "string",
        "anyAtomicType?",
        MaxFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the average of a sequence
     */
    static final BuiltinFunction avg = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "avg"),
        "item*",
        "anyAtomicType?",
        AvgFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the sum of a sequence
     */
    static final BuiltinFunction sum1 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "sum"),
        "anyAtomicType*",
        "anyAtomicType?",
        SumFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction sum2 = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "sum"),
        "anyAtomicType*",
        "anyAtomicType?",
        "anyAtomicType?",
        SumFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );


    /**
     * function that returns true if the argument is the empty sequence
     */
    static final BuiltinFunction empty = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "empty"),
        "item*",
        "boolean",
        EmptyFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns true if the argument is a non-empty sequence
     */
    static final BuiltinFunction exists = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "exists"),
        "item*",
        "boolean",
        ExistsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the first item in a sequence
     */
    static final BuiltinFunction head = createBuiltinFunction(
        new Name(Name.FN_NS, "fn", "head"),
        "item*",
        "item?",
        HeadFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns all but the first item in a sequence
     */
    static final BuiltinFunction tail = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "tail"
        ),
        "item*",
        "item*",
        TailFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns the items of a given sequence
     */
    static final BuiltinFunction unordered = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "unordered"
        ),
        "item*",
        "item*",
        UnorderedFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns a sequence constructed by inserting an item or a sequence of items at a given position
     * within an existing sequence
     */
    static final BuiltinFunction insert_before = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "insert-before"
        ),
        "item*",
        "integer",
        "item*",
        "item*",
        InsertBeforeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns a new sequence containing all the items of $target except the item at position
     * $position.
     */
    static final BuiltinFunction remove = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "remove"
        ),
        "item*",
        "integer",
        "item*",
        RemoveFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * `
     * function that reverses the order of items in a sequence.
     */
    static final BuiltinFunction reverse = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "reverse"
        ),
        "item*",
        "item*",
        ReverseFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that applies a subsequence operation to the given sequence with the given start index and length
     * parameters
     */
    static final BuiltinFunction subsequence2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "subsequence"
        ),
        "item*",
        "double",
        "item*",
        SubsequenceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    static final BuiltinFunction subsequence3 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "subsequence"
        ),
        "item*",
        "double",
        "double",
        "item*",
        SubsequenceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );

    /**
     * function that returns $arg if it contains zero or one items. Otherwise, raises an error.
     */
    static final BuiltinFunction zero_or_one = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "zero-or-one"
        ),
        "item*",
        "item?",
        ZeroOrOneIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns $arg if it contains one or more items. Otherwise, raises an error.
     */
    static final BuiltinFunction one_or_more = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "one-or-more"
        ),
        "item*",
        "item+",
        OneOrMoreIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns $arg if it contains exactly one item. Otherwise, raises an error.
     */
    static final BuiltinFunction exactly_one = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "exactly-one"
        ),
        "item*",
        "item",
        ExactlyOneIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the values that appear in a sequence, with duplicates eliminated
     */
    static final BuiltinFunction distinct_values1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "distinct-values"
        ),
        "anyAtomicType*",
        "anyAtomicType*",
        DistinctValuesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    static final BuiltinFunction distinct_values2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "distinct-values"
        ),
        "anyAtomicType*",
        "string",
        "anyAtomicType*",
        DistinctValuesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );

    /**
     * function that returns indices of items that are equal to the search parameter
     */
    static final BuiltinFunction index_of1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "index-of"
        ),
        "anyAtomicType*",
        "anyAtomicType",
        "integer*",
        IndexOfFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    static final BuiltinFunction index_of2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "index-of"
        ),
        "anyAtomicType*",
        "anyAtomicType",
        "string",
        "integer*",
        IndexOfFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns whether two sequences are deep-equal to each other
     */
    static final BuiltinFunction deep_equal1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "deep-equal"
        ),
        "item*",
        "item*",
        "boolean",
        DeepEqualFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction deep_equal2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "deep-equal"
        ),
        "item*",
        "item*",
        "string",
        "boolean",
        DeepEqualFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the absolute value of the arg
     */
    static final BuiltinFunction abs = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "abs"
        ),
        "numeric?",
        "numeric?",
        AbsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that rounds $arg upwards to a whole number
     */
    static final BuiltinFunction ceiling = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "ceiling"
        ),
        "numeric?",
        "numeric?",
        CeilingFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that rounds $arg downwards to a whole number
     */
    static final BuiltinFunction floor = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "floor"
        ),
        "numeric?",
        "numeric?",
        FloorFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that rounds a value to a specified number of decimal places, rounding upwards if two such values are
     * equally near
     */
    static final BuiltinFunction round1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "round"
        ),
        "numeric?",
        "numeric?",
        RoundFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction round2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "round"
        ),
        "numeric?",
        "integer",
        "numeric?",
        RoundFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that rounds a value to a specified number of decimal places, rounding to make the last digit even if
     * two such values are equally nearinT
     */
    static final BuiltinFunction round_half_to_even1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "round-half-to-even"
        ),
        "numeric?",
        "numeric?",
        RoundHalfToEvenFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction round_half_to_even2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "round-half-to-even"
        ),
        "numeric?",
        "integer",
        "numeric?",
        RoundHalfToEvenFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the approximation the mathematical constant
     */
    static final BuiltinFunction pi = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "pi"
        ),
        "double",
        PiFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the value of e^x
     */
    static final BuiltinFunction exp = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "exp"
        ),
        "double?",
        "double?",
        ExpFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the value of 10^x
     */
    static final BuiltinFunction exp10 = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "exp10"
        ),
        "double?",
        "double?",
        Exp10FunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the natural logarithm of the argument
     */
    static final BuiltinFunction log = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "log"
        ),
        "double?",
        "double?",
        LogFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the base-ten logarithm of the argument
     */
    static final BuiltinFunction log10 = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "log10"
        ),
        "double?",
        "double?",
        Log10FunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the result of raising the first argument to the power of the second
     */
    static final BuiltinFunction pow = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "pow"
        ),
        "double?",
        "double",
        "double?",
        PowFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the non-negative square root of the argument
     */
    static final BuiltinFunction sqrt = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "sqrt"
        ),
        "double?",
        "double?",
        SqrtFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the sine of the angle given in radians
     */
    static final BuiltinFunction sin = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "sin"
        ),
        "double?",
        "double?",
        SinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the cosine of the angle given in radians
     */
    static final BuiltinFunction cos = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "cos"
        ),
        "double?",
        "double?",
        CosFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the tangent of the angle given in radians
     */
    static final BuiltinFunction tan = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "tan"
        ),
        "double?",
        "double?",
        TanFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the arc sine of the angle given in radians
     */
    static final BuiltinFunction asin = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "asin"
        ),
        "double?",
        "double?",
        ASinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the arc cosine of the angle given in radians
     */
    static final BuiltinFunction acos = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "acos"
        ),
        "double?",
        "double?",
        ACosFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the arc tangent of the angle given in radians
     */
    static final BuiltinFunction atan = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "atan"
        ),
        "double?",
        "double?",
        ATanFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the the angle in radians subtended at the origin by the point on a plane with
     * coordinates (x, y) and the positive x-axis.
     */
    static final BuiltinFunction atan2 = createBuiltinFunction(
        new Name(
                Name.MATH_NS,
                "math",
                "atan2"
        ),
        "double",
        "double",
        "double",
        ATan2FunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the hyperbolic cosine of the angle given in radians
     */
    static final BuiltinFunction cosh = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "cosh"
        ),
        "double?",
        "double?",
        CoshFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the hyperbolic sine of the angle given in radians
     */
    static final BuiltinFunction sinh = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "sinh"
        ),
        "double?",
        "double?",
        SinhFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns string
     */
    static final BuiltinFunction string0 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string"
        ),
        "string",
        StringFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction string1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string"
        ),
        "item?",
        "string",
        StringFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns substrings
     */
    static final BuiltinFunction substring2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring"
        ),
        "string?",
        "double",
        "string",
        SubstringFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction substring3 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring"
        ),
        "string?",
        "double",
        "double",
        "string",
        SubstringFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the part of the first variable that precedes the first occurrence of the second
     * variable.
     */
    static final BuiltinFunction substring_before1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring-before"
        ),
        "string?",
        "string?",
        "string",
        SubstringBeforeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction substring_before2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring-before"
        ),
        "string?",
        "string?",
        "string",
        "string",
        SubstringBeforeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the part of the first variable that follows the first occurrence of the second
     * vairable.
     */
    static final BuiltinFunction substring_after1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring-after"
        ),
        "string?",
        "string?",
        "string",
        SubstringAfterFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction substring_after2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "substring-after"
        ),
        "string?",
        "string?",
        "string",
        "string",
        SubstringAfterFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns substrings
     */
    static final BuiltinFunction concat =
        new BuiltinFunction(
                new FunctionIdentifier(new Name(Name.FN_NS, "fn", "concat"), 100),
                new FunctionSignature(
                        Collections.nCopies(
                            100,
                            SequenceType.createSequenceType("anyAtomicType?")
                        ),
                        SequenceType.createSequenceType("string")
                ),
                ConcatFunctionIterator.class,
                BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
    /**
     * function that converts codepoints to a string
     */
    static final BuiltinFunction codepoints_to_string = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "codepoints-to-string"
        ),
        "integer*",
        "string",
        CodepointsToStringFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that converts a string to codepoints
     */
    static final BuiltinFunction string_to_codepoints = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string-to-codepoints"
        ),
        "string?",
        "integer*",
        StringToCodepointsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that compares Strings codepoint-by-codepoint
     */
    static final BuiltinFunction codepoint_equal = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "codepoint-equal"
        ),
        "string?",
        "string?",
        "boolean?",
        CodepointEqualFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns a string created by concatenating the items in a sequence, with an optional defined
     * separator between adjacent items.
     */
    static final BuiltinFunction string_join1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string-join"
        ),
        "string*",
        "string",
        StringJoinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction string_join2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string-join"
        ),
        "string*",
        "string",
        "string",
        StringJoinFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that replaces parts of a string according to a regex expression
     */
    static final BuiltinFunction replace1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "replace"
        ),
        "string?",
        "string",
        "string",
        "string",
        ReplaceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the string length
     */
    static final BuiltinFunction string_length0 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string-length"
        ),
        "integer",
        StringLengthFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction string_length1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "string-length"
        ),
        "string?",
        "integer",
        StringLengthFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns tokens
     */
    static final BuiltinFunction tokenize1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "tokenize"
        ),
        "string?",
        "string*",
        TokenizeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction tokenize2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "tokenize"
        ),
        "string?",
        "string",
        "string*",
        TokenizeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that turns all upper-case characters to lower-case
     */
    static final BuiltinFunction lower_case = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "lower-case"
        ),
        "string?",
        "string",
        LowerCaseFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that turns all lower-case characters to upper-case
     */
    static final BuiltinFunction upper_case = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "upper-case"
        ),
        "string?",
        "string",
        UpperCaseFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that turns all upper-case characters to upper-case
     */
    static final BuiltinFunction translate = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "translate"
        ),
        "string?",
        "string",
        "string",
        "string",
        TranslateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that performs Unicode normalization
     */
    static final BuiltinFunction normalize_unicode1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "normalize-unicode"
        ),
        "string?",
        "string",
        NormalizeUnicodeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction normalize_unicode2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "normalize-unicode"
        ),
        "string?",
        "string",
        "string",
        NormalizeUnicodeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that encodes reserved characters
     */
    static final BuiltinFunction encode_for_uri = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "encode-for-uri"
        ),
        "string?",
        "string",
        EncodeForURIFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that resolves IRI reference against an absolute IRI
     */
    static final BuiltinFunction resolve_uri1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "resolve-uri"
        ),
        "string?",
        "anyURI?",
        ResolveURIFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction resolve_uri2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "resolve-uri"
        ),
        "string?",
        "string",
        "anyURI?",
        ResolveURIFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the value of the Static Base URI property from the static context.
     */
    static final BuiltinFunction static_base_uri = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "static-base-uri"
        ),
        "anyURI?",
        StaticBaseURIFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that checks whether a string ends with a substring
     */
    static final BuiltinFunction ends_with1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "ends-with"
        ),
        "string?",
        "string?",
        "boolean",
        EndsWithFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction ends_with2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "ends-with"
        ),
        "string?",
        "string?",
        "string",
        "boolean",
        EndsWithFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that checks whether a string starts with a substring
     */
    static final BuiltinFunction starts_with1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "starts-with"
        ),
        "string?",
        "string?",
        "boolean",
        StartsWithFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction starts_with2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "starts-with"
        ),
        "string?",
        "string?",
        "string",
        "boolean",
        StartsWithFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that checks whether a string contains a substring
     */
    static final BuiltinFunction contains1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "contains"
        ),
        "string?",
        "string?",
        "boolean",
        ContainsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction contains2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "contains"
        ),
        "string?",
        "string?",
        "string",
        "boolean",
        ContainsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that checks whether a string collates before or after another string
     */
    static final BuiltinFunction compare1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "compare"
        ),
        "string?",
        "string?",
        "integer",
        CompareFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction compare2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "compare"
        ),
        "string?",
        "string?",
        "string",
        "integer",
        CompareFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that checks whether a string matches a regular expression
     */
    static final BuiltinFunction matches1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "matches"
        ),
        "string?",
        "string",
        "boolean",
        MatchesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that normalizes spaces in a string
     */
    static final BuiltinFunction normalize_space0 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "normalize-space"
        ),
        "string",
        NormalizeSpaceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction normalize_space1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "normalize-space"
        ),
        "string?",
        "string",
        NormalizeSpaceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that serializes a given input sequence
     */
    static final BuiltinFunction serialize = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "serialize"
        ),
        "item*",
        "string",
        SerializeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that serializes a given input sequence
     */
    static final BuiltinFunction data0 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "data"
        ),
        "anyAtomicType*",
        AtomizationIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction data1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "data"
        ),
        "item*",
        "anyAtomicType*",
        AtomizationIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns the value of the default collation property
     */
    static final BuiltinFunction default_collation = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "default-collation"
        ),
        "string",
        DefaultCollationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );


    /**
     * function that that returns the double representation of the input string or number
     */
    static final BuiltinFunction number0 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "number"
        ),
        "double",
        NumberFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction number1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "number"
        ),
        "anyAtomicType?",
        "double",
        NumberFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the years from a duration
     */
    static final BuiltinFunction years_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "years-from-duration"
        ),
        "duration?",
        "integer?",
        YearsFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the months from a duration
     */
    static final BuiltinFunction months_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "months-from-duration"
        ),
        "duration?",
        "integer?",
        MonthsFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the days from a duration
     */
    static final BuiltinFunction days_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "days-from-duration"
        ),
        "duration?",
        "integer?",
        DaysFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the hours from a duration
     */
    static final BuiltinFunction hours_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "hours-from-duration"
        ),
        "duration?",
        "integer?",
        HoursFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the minutes from a duration
     */
    static final BuiltinFunction minutes_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "minutes-from-duration"
        ),
        "duration?",
        "integer?",
        MinutesFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a duration
     */
    static final BuiltinFunction seconds_from_duration = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "seconds-from-duration"
        ),
        "duration?",
        "decimal?",
        SecondsFromDurationFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the current dateTime item
     */
    static final BuiltinFunction current_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "current-dateTime"
        ),
        "dateTime?",
        CurrentDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns a string containing a dateTime value formated for display
     */
    static final BuiltinFunction format_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "format-dateTime"
        ),
        "dateTime?",
        "string",
        "string?",
        FormatDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns a xs:dateTime value created by combining an xs:date and an xs:time
     */
    static final BuiltinFunction dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "dateTime"
        ),
        "date?",
        "time?",
        "dateTime?",
        DateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the year from a dateTime
     */
    static final BuiltinFunction year_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "year-from-dateTime"
        ),
        "dateTime?",
        "integer?",
        YearFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the month from a dateTime
     */
    static final BuiltinFunction month_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "month-from-dateTime"
        ),
        "dateTime?",
        "integer?",
        MonthFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the day from a dateTime
     */
    static final BuiltinFunction day_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "day-from-dateTime"
        ),
        "dateTime?",
        "integer?",
        DayFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the hours from a dateTime
     */
    static final BuiltinFunction hours_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "hours-from-dateTime"
        ),
        "dateTime?",
        "integer?",
        HoursFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the minutes from a dateTime
     */
    static final BuiltinFunction minutes_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "minutes-from-dateTime"
        ),
        "dateTime?",
        "integer?",
        MinutesFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a dateTime
     */
    static final BuiltinFunction seconds_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "seconds-from-dateTime"
        ),
        "dateTime?",
        "decimal?",
        SecondsFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a dateTime
     */
    static final BuiltinFunction timezone_from_dateTime = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "timezone-from-dateTime"
        ),
        "dateTime?",
        "dayTimeDuration?",
        TimezoneFromDateTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );


    /**
     * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_dateTime_to_timezone1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-dateTime-to-timezone"
        ),
        "dateTime?",
        "dateTime?",
        AdjustDateTimeToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction adjust_dateTime_to_timezone2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-dateTime-to-timezone"
        ),
        "dateTime?",
        "dayTimeDuration?",
        "dateTime?",
        AdjustDateTimeToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );


    /**
     * function that returns the current date item
     */
    static final BuiltinFunction current_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "current-date"
        ),
        "date?",
        CurrentDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns a string containing a date value formated for display
     */
    static final BuiltinFunction format_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "format-date"
        ),
        "date?",
        "string",
        "string?",
        FormatDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the year from a date
     */
    static final BuiltinFunction year_from_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "year-from-date"
        ),
        "date?",
        "integer?",
        YearFromDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the month from a date
     */
    static final BuiltinFunction month_from_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "month-from-date"
        ),
        "date?",
        "integer?",
        MonthFromDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the day from a date
     */
    static final BuiltinFunction day_from_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "day-from-date"
        ),
        "date?",
        "integer?",
        DayFromDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a date
     */
    static final BuiltinFunction timezone_from_date = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "timezone-from-date"
        ),
        "date?",
        "dayTimeDuration?",
        TimezoneFromDateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );


    /**
     * function that adjusts a date value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_date_to_timezone1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-date-to-timezone"
        ),
        "date?",
        "date?",
        AdjustDateToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction adjust_date_to_timezone2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-date-to-timezone"
        ),
        "date?",
        "dayTimeDuration?",
        "date?",
        AdjustDateToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the current time item
     */
    static final BuiltinFunction current_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "current-time"
        ),
        "time?",
        CurrentTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns a string containing a time value formated for display
     */
    static final BuiltinFunction format_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "format-time"
        ),
        "time?",
        "string",
        "string?",
        FormatTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the hours from a time
     */
    static final BuiltinFunction hours_from_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "hours-from-time"
        ),
        "time?",
        "integer?",
        HoursFromTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the minutes from a time
     */
    static final BuiltinFunction minutes_from_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "minutes-from-time"
        ),
        "time?",
        "integer?",
        MinutesFromTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a time
     */
    static final BuiltinFunction seconds_from_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "seconds-from-time"
        ),
        "time?",
        "decimal?",
        SecondsFromTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the seconds from a time
     */
    static final BuiltinFunction timezone_from_time = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "timezone-from-time"
        ),
        "time?",
        "dayTimeDuration?",
        TimezoneFromTimeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that adjusts a time value to a specific timezone, or to no timezone at all.
     */
    static final BuiltinFunction adjust_time_to_timezone1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-time-to-timezone"
        ),
        "time?",
        "time?",
        AdjustTimeToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    static final BuiltinFunction adjust_time_to_timezone2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "adjust-time-to-timezone"
        ),
        "time?",
        "dayTimeDuration?",
        "time?",
        AdjustTimeToTimezone.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the value of the implicit timezone property from the dynamic context.
     */
    static final BuiltinFunction implicit_timezone = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "implicit-timezone"
        ),
        "dayTimeDuration?",
        ImplicitTimezoneIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns the keys of a Json Object
     */
    static final BuiltinFunction keys = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "keys"
        ),
        "item*",
        "string*",
        ObjectKeysFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
    );
    /**
     * function that returns returns all members of all arrays of the supplied sequence
     */
    static final BuiltinFunction members = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "members"
        ),
        "item*",
        "item*",
        ArrayMembersFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns the JSON null
     */
    static final BuiltinFunction null_function = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "null"
        ),
        "null?",
        NullFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns the length of an array
     */
    static final BuiltinFunction size = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "size"
        ),
        "array?",
        "integer?",
        ArraySizeFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that dynamically creates an object that merges the values of key collisions into arrays
     */
    static final BuiltinFunction accumulate = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "accumulate"
        ),
        "item*",
        "object",
        ObjectAccumulateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that returns all arrays contained within the supplied items, regardless of depth.
     */
    static final BuiltinFunction descendant_arrays = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "descendant-arrays"
        ),
        "item*",
        "item*",
        ArrayDescendantFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns all objects contained within the supplied items, regardless of depth
     */
    static final BuiltinFunction descendant_objects = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "descendant-objects"
        ),
        "item*",
        "item*",
        ObjectDescendantFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns all objects contained within the supplied items, regardless of depth
     */
    static final BuiltinFunction descendant_pairs = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "descendant-pairs"
        ),
        "item*",
        "item*",
        ObjectDescendantPairsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that recursively flattens arrays in the input sequence, leaving non-arrays intact
     */
    static final BuiltinFunction flatten = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "flatten"
        ),
        "item*",
        "item*",
        ArrayFlattenFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns the intersection of the supplied objects, and aggregates values corresponding to the
     * same name into an array
     */
    static final BuiltinFunction intersect = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "intersect"
        ),
        "item*",
        "object",
        ObjectIntersectFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );
    /**
     * function that projects objects by filtering their pairs and leaves non-objects intact
     */
    static final BuiltinFunction project = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "project"
        ),
        "item*",
        "string*",
        "item*",
        ObjectProjectFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that removes the pairs with the given keys from all objects and leaves non-objects intact
     */
    static final BuiltinFunction remove_keys = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "remove-keys"
        ),
        "item*",
        "string*",
        "item*",
        ObjectRemoveKeysFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );
    /**
     * function that returns the values of a Json Object
     */
    static final BuiltinFunction values = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "values"
        ),
        "item*",
        "item*",
        ObjectValuesFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );

    /**
     * function fetches the transformer class from SparkML API
     */
    static final BuiltinFunction get_transformer = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "get-transformer"
        ),
        "string",
        "function(object*, object) as object*",
        GetTransformerFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function fetches the transformer class from SparkML API
     */
    static final BuiltinFunction get_transformer2 = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "get-transformer"
        ),
        "string",
        "object",
        "function(object*, object) as object*",
        GetTransformerFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function fetches the estimator class from SparkML API
     */
    static final BuiltinFunction get_estimator = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "get-estimator"
        ),
        "string",
        "function(object*, object) as function(object*, object) as object*",
        GetEstimatorFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function fetches the estimator class from SparkML API
     */
    static final BuiltinFunction get_estimator2 = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "get-estimator"
        ),
        "string",
        "object",
        "function(object*, object) as function(object*, object) as object*",
        GetEstimatorFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function converts given RDD or local data to a DataFrame using a schema
     */
    static final BuiltinFunction annotate = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "annotate"
        ),
        "object*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        "object",
        "object*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        AnnotateFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
    );

    static final BuiltinFunction trace1 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "trace"
        ),
        "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        TraceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction trace2 = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "trace"
        ),
        "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        "string",
        "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
        TraceFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction repartition = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "repartition"
        ),
        "item*",
        "integer",
        "item*",
        RepartitionFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
    );

    static final BuiltinFunction binary_classification_metrics1 = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "binary-classification-metrics"
        ),
        "object*",
        "string",
        "string",
        "item*",
        BinaryClassificationMetricsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction binary_classification_metrics2 = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "binary-classification-metrics"
        ),
        "object*",
        "string",
        "string",
        "integer",
        "item*",
        BinaryClassificationMetricsFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction print_variable_values = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "fn",
                "print_vars"
        ),
        "item*",
        "null?",
        DebugFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns a random number
     */
    static final BuiltinFunction random_number_generator = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "random"
        ),
        "double",
        RandomNumberGeneratorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns a sequence of random numbers
     */
    static final BuiltinFunction random_sequence_generator = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "random"
        ),
        "integer",
        "item*",
        RandomSequenceGeneratorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns a sequence of random numbers using a seed
     */
    static final BuiltinFunction random_sequence_generator_with_seed = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "seeded_random"
        ),
        "integer",
        "integer",
        "item*",
        RandomSequenceGeneratorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that returns a sequence of random numbers using a low and high bound, while also allowing to limit the
     * number of elements generated and their type
     */
    static final BuiltinFunction random_sequence_with_bounds = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "random-between"
        ),
        "integer",
        "integer",
        "integer",
        "string",
        "integer",
        "item*",
        RandomSequenceWithBoundsIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction random_sequence_with_bounds_double = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "random-between"
        ),
        "double",
        "double",
        "integer",
        "string",
        "item*",
        RandomSequenceWithBoundsIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction random_sequence_with_bounds_seeded_int = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "random-between"
        ),
        "integer",
        "integer",
        "integer",
        "string",
        "integer",
        "item*",
        RandomSequenceWithBoundsAndSeedIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction error = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "error"
        ),
        "null?",
        ThrowErrorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction error_with_code = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "error"
        ),
        "string",
        "null?",
        ThrowErrorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction error_with_code_and_description = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "error"
        ),
        "string",
        "string",
        "null?",
        ThrowErrorIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction item_type = createBuiltinFunction(
        new Name(
                Name.FN_NS,
                "",
                "item-type"
        ),
        "item*",
        "string",
        DynamicItemTypeIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction is_null = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "is-null"
        ),
        "item*",
        "boolean",
        IsNullIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static final BuiltinFunction current_time_millis = createBuiltinFunction(
        new Name(
                Name.JN_NS,
                "jn",
                "current-time-milis"
        ),
        "integer",
        TimeInMillis.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that creates a delta lake table at a given path location
     */
    static final BuiltinFunction create_delta_lake_table = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "create-delta-lake-table"),
        "string",
        "boolean",
        CreateDeltaLakeTableFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    /**
     * function that deletes a delta lake table at a given path location
     */
    static final BuiltinFunction delete_delta_lake_table = createBuiltinFunction(
        new Name(Name.JN_NS, "jn", "delete-delta-lake-table"),
        "string",
        "boolean",
        DeleteDeltaLakeTableFunctionIterator.class,
        BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
    );

    static {
        builtinFunctions = new HashMap<>();

        builtinFunctions.put(position.getIdentifier(), position);
        builtinFunctions.put(last.getIdentifier(), last);

        builtinFunctions.put(json_file1.getIdentifier(), json_file1);
        builtinFunctions.put(json_file2.getIdentifier(), json_file2);
        builtinFunctions.put(json_lines1.getIdentifier(), json_lines1);
        builtinFunctions.put(json_lines2.getIdentifier(), json_lines2);
        builtinFunctions.put(structured_json_file.getIdentifier(), structured_json_file);
        builtinFunctions.put(structured_json_lines.getIdentifier(), structured_json_lines);
        builtinFunctions.put(xml_files.getIdentifier(), xml_files);
        builtinFunctions.put(xml_files2.getIdentifier(), xml_files2);
        builtinFunctions.put(libsvm_file.getIdentifier(), libsvm_file);
        builtinFunctions.put(json_doc.getIdentifier(), json_doc);
        builtinFunctions.put(yaml_doc.getIdentifier(), yaml_doc);
        builtinFunctions.put(unparsed_text.getIdentifier(), unparsed_text);
        builtinFunctions.put(unparsed_text_lines.getIdentifier(), unparsed_text_lines);
        builtinFunctions.put(text_file1.getIdentifier(), text_file1);
        builtinFunctions.put(text_file2.getIdentifier(), text_file2);
        builtinFunctions.put(local_text_file.getIdentifier(), local_text_file);
        builtinFunctions.put(parallelizeFunction1.getIdentifier(), parallelizeFunction1);
        builtinFunctions.put(parallelizeFunction2.getIdentifier(), parallelizeFunction2);
        builtinFunctions.put(parquet_file1.getIdentifier(), parquet_file1);
        builtinFunctions.put(parquet_file2.getIdentifier(), parquet_file2);
        builtinFunctions.put(delta_file.getIdentifier(), delta_file);
        builtinFunctions.put(delta_table.getIdentifier(), delta_table);
        builtinFunctions.put(csv_file1.getIdentifier(), csv_file1);
        builtinFunctions.put(csv_file2.getIdentifier(), csv_file2);
        builtinFunctions.put(root_file1.getIdentifier(), root_file1);
        builtinFunctions.put(root_file2.getIdentifier(), root_file2);
        builtinFunctions.put(avro_file1.getIdentifier(), avro_file1);
        builtinFunctions.put(avro_file2.getIdentifier(), avro_file2);
        builtinFunctions.put(parse_json.getIdentifier(), parse_json);

        builtinFunctions.put(create_delta_lake_table.getIdentifier(), create_delta_lake_table);
        builtinFunctions.put(delete_delta_lake_table.getIdentifier(), delete_delta_lake_table);

        builtinFunctions.put(count.getIdentifier(), count);
        builtinFunctions.put(boolean_function.getIdentifier(), boolean_function);
        builtinFunctions.put(not_function.getIdentifier(), not_function);
        builtinFunctions.put(true_function.getIdentifier(), true_function);
        builtinFunctions.put(false_function.getIdentifier(), false_function);

        builtinFunctions.put(min1.getIdentifier(), min1);
        builtinFunctions.put(min2.getIdentifier(), min2);
        builtinFunctions.put(max1.getIdentifier(), max1);
        builtinFunctions.put(max2.getIdentifier(), max2);
        builtinFunctions.put(sum1.getIdentifier(), sum1);
        builtinFunctions.put(sum2.getIdentifier(), sum2);
        builtinFunctions.put(avg.getIdentifier(), avg);

        builtinFunctions.put(empty.getIdentifier(), empty);
        builtinFunctions.put(exists.getIdentifier(), exists);
        builtinFunctions.put(head.getIdentifier(), head);
        builtinFunctions.put(tail.getIdentifier(), tail);
        builtinFunctions.put(unordered.getIdentifier(), unordered);
        builtinFunctions.put(insert_before.getIdentifier(), insert_before);
        builtinFunctions.put(remove.getIdentifier(), remove);
        builtinFunctions.put(reverse.getIdentifier(), reverse);
        builtinFunctions.put(subsequence2.getIdentifier(), subsequence2);
        builtinFunctions.put(subsequence3.getIdentifier(), subsequence3);

        builtinFunctions.put(zero_or_one.getIdentifier(), zero_or_one);
        builtinFunctions.put(one_or_more.getIdentifier(), one_or_more);
        builtinFunctions.put(exactly_one.getIdentifier(), exactly_one);

        builtinFunctions.put(distinct_values1.getIdentifier(), distinct_values1);
        builtinFunctions.put(distinct_values2.getIdentifier(), distinct_values2);

        builtinFunctions.put(index_of1.getIdentifier(), index_of1);
        builtinFunctions.put(index_of2.getIdentifier(), index_of2);
        builtinFunctions.put(deep_equal1.getIdentifier(), deep_equal1);
        builtinFunctions.put(deep_equal2.getIdentifier(), deep_equal2);
        builtinFunctions.put(abs.getIdentifier(), abs);
        builtinFunctions.put(ceiling.getIdentifier(), ceiling);
        builtinFunctions.put(floor.getIdentifier(), floor);
        builtinFunctions.put(round1.getIdentifier(), round1);
        builtinFunctions.put(round2.getIdentifier(), round2);
        builtinFunctions.put(round_half_to_even1.getIdentifier(), round_half_to_even1);
        builtinFunctions.put(round_half_to_even2.getIdentifier(), round_half_to_even2);

        builtinFunctions.put(pi.getIdentifier(), pi);
        builtinFunctions.put(exp.getIdentifier(), exp);
        builtinFunctions.put(exp10.getIdentifier(), exp10);
        builtinFunctions.put(log.getIdentifier(), log);
        builtinFunctions.put(log10.getIdentifier(), log10);
        builtinFunctions.put(pow.getIdentifier(), pow);
        builtinFunctions.put(sqrt.getIdentifier(), sqrt);
        builtinFunctions.put(sin.getIdentifier(), sin);
        builtinFunctions.put(cos.getIdentifier(), cos);
        builtinFunctions.put(tan.getIdentifier(), tan);
        builtinFunctions.put(asin.getIdentifier(), asin);
        builtinFunctions.put(acos.getIdentifier(), acos);
        builtinFunctions.put(atan.getIdentifier(), atan);
        builtinFunctions.put(atan2.getIdentifier(), atan2);
        builtinFunctions.put(cosh.getIdentifier(), cosh);
        builtinFunctions.put(sinh.getIdentifier(), sinh);

        builtinFunctions.put(string0.getIdentifier(), string0);
        builtinFunctions.put(string1.getIdentifier(), string1);
        builtinFunctions.put(codepoints_to_string.getIdentifier(), codepoints_to_string);
        builtinFunctions.put(string_to_codepoints.getIdentifier(), string_to_codepoints);
        builtinFunctions.put(replace1.getIdentifier(), replace1);
        builtinFunctions.put(substring2.getIdentifier(), substring2);
        builtinFunctions.put(substring3.getIdentifier(), substring3);
        builtinFunctions.put(substring_before1.getIdentifier(), substring_before1);
        builtinFunctions.put(substring_before2.getIdentifier(), substring_before2);
        builtinFunctions.put(substring_after1.getIdentifier(), substring_after1);
        builtinFunctions.put(substring_after2.getIdentifier(), substring_after2);
        for (int i = 2; i < 100; i++) {
            builtinFunctions.put(
                new FunctionIdentifier(concat.getIdentifier().getName(), i),
                concat
            );
        }
        builtinFunctions.put(ends_with1.getIdentifier(), ends_with1);
        builtinFunctions.put(ends_with2.getIdentifier(), ends_with2);
        builtinFunctions.put(string_join1.getIdentifier(), string_join1);
        builtinFunctions.put(string_join2.getIdentifier(), string_join2);
        builtinFunctions.put(string_length0.getIdentifier(), string_length0);
        builtinFunctions.put(string_length1.getIdentifier(), string_length1);
        builtinFunctions.put(tokenize1.getIdentifier(), tokenize1);
        builtinFunctions.put(tokenize2.getIdentifier(), tokenize2);
        builtinFunctions.put(lower_case.getIdentifier(), lower_case);
        builtinFunctions.put(upper_case.getIdentifier(), upper_case);
        builtinFunctions.put(translate.getIdentifier(), translate);
        builtinFunctions.put(codepoint_equal.getIdentifier(), codepoint_equal);
        builtinFunctions.put(starts_with1.getIdentifier(), starts_with1);
        builtinFunctions.put(starts_with2.getIdentifier(), starts_with2);
        builtinFunctions.put(matches1.getIdentifier(), matches1);
        builtinFunctions.put(contains1.getIdentifier(), contains1);
        builtinFunctions.put(contains2.getIdentifier(), contains2);
        builtinFunctions.put(compare1.getIdentifier(), compare1);
        builtinFunctions.put(compare2.getIdentifier(), compare2);
        builtinFunctions.put(normalize_space0.getIdentifier(), normalize_space0);
        builtinFunctions.put(normalize_space1.getIdentifier(), normalize_space1);
        builtinFunctions.put(normalize_unicode1.getIdentifier(), normalize_unicode1);
        builtinFunctions.put(normalize_unicode2.getIdentifier(), normalize_unicode2);
        builtinFunctions.put(serialize.getIdentifier(), serialize);
        builtinFunctions.put(default_collation.getIdentifier(), default_collation);
        builtinFunctions.put(number0.getIdentifier(), number0);
        builtinFunctions.put(number1.getIdentifier(), number1);
        builtinFunctions.put(encode_for_uri.getIdentifier(), encode_for_uri);
        builtinFunctions.put(resolve_uri1.getIdentifier(), resolve_uri1);
        builtinFunctions.put(resolve_uri2.getIdentifier(), resolve_uri2);
        builtinFunctions.put(static_base_uri.getIdentifier(), static_base_uri);

        builtinFunctions.put(years_from_duration.getIdentifier(), years_from_duration);
        builtinFunctions.put(months_from_duration.getIdentifier(), months_from_duration);
        builtinFunctions.put(days_from_duration.getIdentifier(), days_from_duration);
        builtinFunctions.put(hours_from_duration.getIdentifier(), hours_from_duration);
        builtinFunctions.put(minutes_from_duration.getIdentifier(), minutes_from_duration);
        builtinFunctions.put(seconds_from_duration.getIdentifier(), seconds_from_duration);

        builtinFunctions.put(current_dateTime.getIdentifier(), current_dateTime);
        builtinFunctions.put(format_dateTime.getIdentifier(), format_dateTime);
        builtinFunctions.put(dateTime.getIdentifier(), dateTime);
        builtinFunctions.put(year_from_dateTime.getIdentifier(), year_from_dateTime);
        builtinFunctions.put(month_from_dateTime.getIdentifier(), month_from_dateTime);
        builtinFunctions.put(day_from_dateTime.getIdentifier(), day_from_dateTime);
        builtinFunctions.put(hours_from_dateTime.getIdentifier(), hours_from_dateTime);
        builtinFunctions.put(minutes_from_dateTime.getIdentifier(), minutes_from_dateTime);
        builtinFunctions.put(seconds_from_dateTime.getIdentifier(), seconds_from_dateTime);
        builtinFunctions.put(timezone_from_dateTime.getIdentifier(), timezone_from_dateTime);
        builtinFunctions.put(adjust_dateTime_to_timezone1.getIdentifier(), adjust_dateTime_to_timezone1);
        builtinFunctions.put(adjust_dateTime_to_timezone2.getIdentifier(), adjust_dateTime_to_timezone2);

        builtinFunctions.put(implicit_timezone.getIdentifier(), implicit_timezone);

        builtinFunctions.put(current_date.getIdentifier(), current_date);
        builtinFunctions.put(format_date.getIdentifier(), format_date);
        builtinFunctions.put(year_from_date.getIdentifier(), year_from_date);
        builtinFunctions.put(month_from_date.getIdentifier(), month_from_date);
        builtinFunctions.put(day_from_date.getIdentifier(), day_from_date);
        builtinFunctions.put(timezone_from_date.getIdentifier(), timezone_from_date);
        builtinFunctions.put(adjust_date_to_timezone1.getIdentifier(), adjust_date_to_timezone1);
        builtinFunctions.put(adjust_date_to_timezone2.getIdentifier(), adjust_date_to_timezone2);

        builtinFunctions.put(current_time.getIdentifier(), current_time);
        builtinFunctions.put(format_time.getIdentifier(), format_time);
        builtinFunctions.put(hours_from_time.getIdentifier(), hours_from_time);
        builtinFunctions.put(minutes_from_time.getIdentifier(), minutes_from_time);
        builtinFunctions.put(seconds_from_time.getIdentifier(), seconds_from_time);
        builtinFunctions.put(timezone_from_time.getIdentifier(), timezone_from_time);
        builtinFunctions.put(adjust_time_to_timezone1.getIdentifier(), adjust_time_to_timezone1);
        builtinFunctions.put(adjust_time_to_timezone2.getIdentifier(), adjust_time_to_timezone2);
        builtinFunctions.put(data0.getIdentifier(), data0);
        builtinFunctions.put(data1.getIdentifier(), data1);
        builtinFunctions.put(keys.getIdentifier(), keys);
        builtinFunctions.put(members.getIdentifier(), members);
        builtinFunctions.put(null_function.getIdentifier(), null_function);
        builtinFunctions.put(size.getIdentifier(), size);
        builtinFunctions.put(accumulate.getIdentifier(), accumulate);
        builtinFunctions.put(descendant_arrays.getIdentifier(), descendant_arrays);
        builtinFunctions.put(descendant_objects.getIdentifier(), descendant_objects);
        builtinFunctions.put(descendant_pairs.getIdentifier(), descendant_pairs);
        builtinFunctions.put(flatten.getIdentifier(), flatten);
        builtinFunctions.put(intersect.getIdentifier(), intersect);
        builtinFunctions.put(project.getIdentifier(), project);
        builtinFunctions.put(remove_keys.getIdentifier(), remove_keys);
        builtinFunctions.put(values.getIdentifier(), values);

        builtinFunctions.put(get_transformer.getIdentifier(), get_transformer);
        builtinFunctions.put(get_transformer2.getIdentifier(), get_transformer2);
        builtinFunctions.put(get_estimator.getIdentifier(), get_estimator);
        builtinFunctions.put(get_estimator2.getIdentifier(), get_estimator2);
        builtinFunctions.put(annotate.getIdentifier(), annotate);

        builtinFunctions.put(trace2.getIdentifier(), trace2);
        builtinFunctions.put(trace1.getIdentifier(), trace1);

        builtinFunctions.put(repartition.getIdentifier(), repartition);
        builtinFunctions.put(binary_classification_metrics1.getIdentifier(), binary_classification_metrics1);
        builtinFunctions.put(binary_classification_metrics2.getIdentifier(), binary_classification_metrics2);
        builtinFunctions.put(print_variable_values.getIdentifier(), print_variable_values);
        builtinFunctions.put(random_number_generator.getIdentifier(), random_number_generator);
        builtinFunctions.put(random_sequence_generator.getIdentifier(), random_sequence_generator);
        builtinFunctions.put(random_sequence_generator_with_seed.getIdentifier(), random_sequence_generator_with_seed);
        builtinFunctions.put(random_sequence_with_bounds.getIdentifier(), random_sequence_with_bounds);
        builtinFunctions.put(random_sequence_with_bounds_double.getIdentifier(), random_sequence_with_bounds_double);
        builtinFunctions.put(error.getIdentifier(), error);
        builtinFunctions.put(error_with_code.getIdentifier(), error_with_code);
        builtinFunctions.put(error_with_code_and_description.getIdentifier(), error_with_code_and_description);
        builtinFunctions.put(item_type.getIdentifier(), item_type);
        builtinFunctions.put(is_null.getIdentifier(), is_null);
        builtinFunctions.put(
            random_sequence_with_bounds_seeded_int.getIdentifier(),
            random_sequence_with_bounds_seeded_int
        );
        builtinFunctions.put(doc.getIdentifier(), doc);
        builtinFunctions.put(root_with_arg.getIdentifier(), root_with_arg);
        builtinFunctions.put(root_without_arg.getIdentifier(), root_without_arg);
        builtinFunctions.put(current_time_millis.getIdentifier(), current_time_millis);
    }


}
