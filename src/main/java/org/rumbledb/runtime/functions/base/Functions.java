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

package org.rumbledb.runtime.functions.base;

import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.FunctionItemCallIterator;
import org.rumbledb.runtime.functions.NullFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayDescendantFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFlattenFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArrayMembersFunctionIterator;
import org.rumbledb.runtime.functions.arrays.ArraySizeFunctionIterator;
import org.rumbledb.runtime.functions.binaries.Base64BinaryFunctionIterator;
import org.rumbledb.runtime.functions.binaries.HexBinaryFunctionIterator;
import org.rumbledb.runtime.functions.booleans.BooleanFunctionIterator;
import org.rumbledb.runtime.functions.context.LastFunctionIterator;
import org.rumbledb.runtime.functions.context.PositionFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.CurrentTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.DateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.DateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatDateFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatDateTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.FormatTimeFunctionIterator;
import org.rumbledb.runtime.functions.datetime.TimeFunctionIterator;
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
import org.rumbledb.runtime.functions.durations.DayTimeDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.DurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.YearMonthDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.DaysFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.HoursFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.MinutesFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.MonthsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.SecondsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.durations.components.YearsFromDurationFunctionIterator;
import org.rumbledb.runtime.functions.input.CSVFileFunctionIterator;
import org.rumbledb.runtime.functions.input.JsonFileFunctionIterator;
import org.rumbledb.runtime.functions.input.LibSVMFileFunctionIterator;
import org.rumbledb.runtime.functions.input.ParallelizeFunctionIterator;
import org.rumbledb.runtime.functions.input.ParquetFileFunctionIterator;
import org.rumbledb.runtime.functions.input.RootFileFunctionIterator;
import org.rumbledb.runtime.functions.input.StructuredJsonFileFunctionIterator;
import org.rumbledb.runtime.functions.input.TextFileFunctionIterator;
import org.rumbledb.runtime.functions.io.JsonDocFunctionIterator;
import org.rumbledb.runtime.functions.numerics.AbsFunctionIterator;
import org.rumbledb.runtime.functions.numerics.CeilingFunctionIterator;
import org.rumbledb.runtime.functions.numerics.DecimalFunctionIterator;
import org.rumbledb.runtime.functions.numerics.DoubleFunctionIterator;
import org.rumbledb.runtime.functions.numerics.FloorFunctionIterator;
import org.rumbledb.runtime.functions.numerics.IntegerFunctionIterator;
import org.rumbledb.runtime.functions.numerics.PiFunctionIterator;
import org.rumbledb.runtime.functions.numerics.RoundFunctionIterator;
import org.rumbledb.runtime.functions.numerics.RoundHalfToEvenFunctionIterator;
import org.rumbledb.runtime.functions.numerics.NumberFunctionIterator;
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
import org.rumbledb.runtime.functions.numerics.trigonometric.SinFunctionIterator;
import org.rumbledb.runtime.functions.numerics.trigonometric.TanFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectAccumulateFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectDescendantPairsFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectIntersectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectProjectFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectRemoveKeysFunctionIterator;
import org.rumbledb.runtime.functions.object.ObjectValuesFunctionIterator;
import org.rumbledb.runtime.functions.resources.AnyURIFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.AvgFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.CountFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.MaxFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.MinFunctionIterator;
import org.rumbledb.runtime.functions.sequences.aggregate.SumFunctionIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.ExactlyOneIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.OneOrMoreIterator;
import org.rumbledb.runtime.functions.sequences.cardinality.ZeroOrOneIterator;
import org.rumbledb.runtime.functions.sequences.general.EmptyFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ExistsFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.HeadFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.InsertBeforeFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.RemoveFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.ReverseFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.SubsequenceFunctionIterator;
import org.rumbledb.runtime.functions.sequences.general.TailFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.DeepEqualFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.DistinctValuesFunctionIterator;
import org.rumbledb.runtime.functions.sequences.value.IndexOfFunctionIterator;
import org.rumbledb.runtime.functions.strings.CodepointEqualFunctionIterator;
import org.rumbledb.runtime.functions.strings.CodepointsToStringFunctionIterator;
import org.rumbledb.runtime.functions.strings.ConcatFunctionIterator;
import org.rumbledb.runtime.functions.strings.ContainsFunctionIterator;
import org.rumbledb.runtime.functions.strings.EndsWithFunctionIterator;
import org.rumbledb.runtime.functions.strings.LowerCaseFunctionIterator;
import org.rumbledb.runtime.functions.strings.MatchesFunctionIterator;
import org.rumbledb.runtime.functions.strings.NormalizeSpaceFunctionIterator;
import org.rumbledb.runtime.functions.strings.ReplaceFunctionIterator;
import org.rumbledb.runtime.functions.strings.StartsWithFunctionIterator;
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
import org.rumbledb.runtime.operational.TypePromotionIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.spark.ml.AnnotateFunctionIterator;
import sparksoniq.spark.ml.GetEstimatorFunctionIterator;
import sparksoniq.spark.ml.GetTransformerFunctionIterator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.abs;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.accumulate;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.acos;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_dateTime_to_timezone1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_dateTime_to_timezone2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_date_to_timezone1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_date_to_timezone2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_time_to_timezone1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.adjust_time_to_timezone2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.annotate;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.anyURI;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.asin;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.atan;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.atan2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.avg;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.base64Binary;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.boolean_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.ceiling;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.codepoint_equal;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.codepoints_to_string;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.concat;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.contains;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.cos;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.count;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.csv_file1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.csv_file2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.current_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.current_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.current_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.dayTimeDuration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.day_from_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.day_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.days_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.decimal_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.deep_equal;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.descendant_arrays;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.descendant_objects;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.descendant_pairs;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.distinct_values;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.double_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.empty;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.ends_with;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.exactly_one;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.exists;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.exp;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.exp10;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.flatten;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.floor;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.format_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.format_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.format_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.get_estimator;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.get_transformer;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.head;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.hexBinary;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.hours_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.hours_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.hours_from_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.index_of;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.insert_before;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.integer_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.intersect;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.json_doc;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.json_file1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.json_file2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.keys;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.last;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.libsvm_file;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.log;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.log10;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.lower_case;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.matches;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.max;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.members;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.min;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.minutes_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.minutes_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.minutes_from_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.month_from_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.month_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.months_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.normalize_space;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.null_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.number;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.one_or_more;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.parallelizeFunction1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.parallelizeFunction2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.parquet_file;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.pi;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.position;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.pow;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.project;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.remove;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.remove_keys;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.replace;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.reverse;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.root_file1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.root_file2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.round1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.round2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.round_half_to_even1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.round_half_to_even2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.seconds_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.seconds_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.seconds_from_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.sin;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.size;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.sqrt;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.starts_with;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.string_function;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.string_join1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.string_join2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.string_length;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.string_to_codepoints;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.structured_json_file;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.subsequence2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.subsequence3;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.substring2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.substring3;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.substring_after;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.substring_before;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.sum1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.sum2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.tail;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.tan;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.text_file1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.text_file2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.timezone_from_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.timezone_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.timezone_from_time;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.tokenize1;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.tokenize2;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.translate;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.upper_case;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.values;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.yearMonthDuration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.year_from_date;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.year_from_dateTime;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.years_from_duration;
import static org.rumbledb.runtime.functions.base.Functions.BuiltinFunctions.zero_or_one;

import static org.rumbledb.types.SequenceType.mostGeneralSequenceType;

public class Functions {
    private static final HashMap<FunctionIdentifier, BuiltinFunction> builtInFunctions;

    // two maps for User defined function are needed as execution mode is known at static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private static HashMap<FunctionIdentifier, ExecutionMode> userDefinedFunctionsExecutionMode;
    private static HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    private static List<FunctionIdentifier> userDefinedFunctionIdentifiersWithUnsetExecutionModes;


    private static final Map<String, SequenceType> sequenceTypes;

    static {
        sequenceTypes = new HashMap<>();
        sequenceTypes.put("item", new SequenceType(ItemType.item, SequenceType.Arity.One));
        sequenceTypes.put("item?", new SequenceType(ItemType.item, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("item*", new SequenceType(ItemType.item, SequenceType.Arity.ZeroOrMore));
        sequenceTypes.put("item+", new SequenceType(ItemType.item, SequenceType.Arity.OneOrMore));

        sequenceTypes.put("object", new SequenceType(ItemType.objectItem, SequenceType.Arity.One));
        sequenceTypes.put("object+", new SequenceType(ItemType.objectItem, SequenceType.Arity.OneOrMore));
        sequenceTypes.put("object*", new SequenceType(ItemType.objectItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("array?", new SequenceType(ItemType.arrayItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("atomic", new SequenceType(ItemType.atomicItem, SequenceType.Arity.One));
        sequenceTypes.put("atomic?", new SequenceType(ItemType.atomicItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("atomic*", new SequenceType(ItemType.atomicItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("string", new SequenceType(ItemType.stringItem, SequenceType.Arity.One));
        sequenceTypes.put("string?", new SequenceType(ItemType.stringItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("string*", new SequenceType(ItemType.stringItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("integer", new SequenceType(ItemType.integerItem, SequenceType.Arity.One));
        sequenceTypes.put("integer?", new SequenceType(ItemType.integerItem, SequenceType.Arity.OneOrZero));
        sequenceTypes.put("integer*", new SequenceType(ItemType.integerItem, SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("decimal?", new SequenceType(ItemType.decimalItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("double", new SequenceType(ItemType.doubleItem, SequenceType.Arity.One));
        sequenceTypes.put("double?", new SequenceType(ItemType.doubleItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("boolean", new SequenceType(ItemType.booleanItem, SequenceType.Arity.One));
        sequenceTypes.put("boolean?", new SequenceType(ItemType.booleanItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("duration?", new SequenceType(ItemType.durationItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put(
            "yearMonthDuration?",
            new SequenceType(ItemType.yearMonthDurationItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "dayTimeDuration?",
            new SequenceType(ItemType.dayTimeDurationItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("dateTime?", new SequenceType(ItemType.dateTimeItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("date?", new SequenceType(ItemType.dateItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("time?", new SequenceType(ItemType.timeItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("anyURI?", new SequenceType(ItemType.anyURIItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put("hexBinary?", new SequenceType(ItemType.hexBinaryItem, SequenceType.Arity.OneOrZero));

        sequenceTypes.put(
            "base64Binary?",
            new SequenceType(ItemType.base64BinaryItem, SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("null?", new SequenceType(ItemType.nullItem, SequenceType.Arity.OneOrZero));
    }


    static {
        builtInFunctions = new HashMap<>();

        builtInFunctions.put(position.getIdentifier(), position);
        builtInFunctions.put(last.getIdentifier(), last);

        builtInFunctions.put(json_file1.getIdentifier(), json_file1);
        builtInFunctions.put(json_file2.getIdentifier(), json_file2);
        builtInFunctions.put(structured_json_file.getIdentifier(), structured_json_file);
        builtInFunctions.put(libsvm_file.getIdentifier(), libsvm_file);
        builtInFunctions.put(json_doc.getIdentifier(), json_doc);
        builtInFunctions.put(text_file1.getIdentifier(), text_file1);
        builtInFunctions.put(text_file2.getIdentifier(), text_file2);
        builtInFunctions.put(parallelizeFunction1.getIdentifier(), parallelizeFunction1);
        builtInFunctions.put(parallelizeFunction2.getIdentifier(), parallelizeFunction2);
        builtInFunctions.put(parquet_file.getIdentifier(), parquet_file);
        builtInFunctions.put(csv_file1.getIdentifier(), csv_file1);
        builtInFunctions.put(csv_file2.getIdentifier(), csv_file2);
        builtInFunctions.put(root_file1.getIdentifier(), root_file1);
        builtInFunctions.put(root_file2.getIdentifier(), root_file2);

        builtInFunctions.put(count.getIdentifier(), count);
        builtInFunctions.put(boolean_function.getIdentifier(), boolean_function);

        builtInFunctions.put(min.getIdentifier(), min);
        builtInFunctions.put(max.getIdentifier(), max);
        builtInFunctions.put(sum1.getIdentifier(), sum1);
        builtInFunctions.put(sum2.getIdentifier(), sum2);
        builtInFunctions.put(avg.getIdentifier(), avg);

        builtInFunctions.put(empty.getIdentifier(), empty);
        builtInFunctions.put(exists.getIdentifier(), exists);
        builtInFunctions.put(head.getIdentifier(), head);
        builtInFunctions.put(tail.getIdentifier(), tail);
        builtInFunctions.put(insert_before.getIdentifier(), insert_before);
        builtInFunctions.put(remove.getIdentifier(), remove);
        builtInFunctions.put(reverse.getIdentifier(), reverse);
        builtInFunctions.put(subsequence2.getIdentifier(), subsequence2);
        builtInFunctions.put(subsequence3.getIdentifier(), subsequence3);

        builtInFunctions.put(zero_or_one.getIdentifier(), zero_or_one);
        builtInFunctions.put(one_or_more.getIdentifier(), one_or_more);
        builtInFunctions.put(exactly_one.getIdentifier(), exactly_one);

        builtInFunctions.put(distinct_values.getIdentifier(), distinct_values);
        builtInFunctions.put(index_of.getIdentifier(), index_of);
        builtInFunctions.put(deep_equal.getIdentifier(), deep_equal);

        builtInFunctions.put(integer_function.getIdentifier(), integer_function);
        builtInFunctions.put(decimal_function.getIdentifier(), decimal_function);
        builtInFunctions.put(double_function.getIdentifier(), double_function);
        builtInFunctions.put(abs.getIdentifier(), abs);
        builtInFunctions.put(ceiling.getIdentifier(), ceiling);
        builtInFunctions.put(floor.getIdentifier(), floor);
        builtInFunctions.put(round1.getIdentifier(), round1);
        builtInFunctions.put(round2.getIdentifier(), round2);
        builtInFunctions.put(round_half_to_even1.getIdentifier(), round_half_to_even1);
        builtInFunctions.put(round_half_to_even2.getIdentifier(), round_half_to_even2);

        builtInFunctions.put(pi.getIdentifier(), pi);
        builtInFunctions.put(exp.getIdentifier(), exp);
        builtInFunctions.put(exp10.getIdentifier(), exp10);
        builtInFunctions.put(log.getIdentifier(), log);
        builtInFunctions.put(log10.getIdentifier(), log10);
        builtInFunctions.put(pow.getIdentifier(), pow);
        builtInFunctions.put(sqrt.getIdentifier(), sqrt);
        builtInFunctions.put(sin.getIdentifier(), sin);
        builtInFunctions.put(cos.getIdentifier(), cos);
        builtInFunctions.put(tan.getIdentifier(), tan);
        builtInFunctions.put(asin.getIdentifier(), asin);
        builtInFunctions.put(acos.getIdentifier(), acos);
        builtInFunctions.put(atan.getIdentifier(), atan);
        builtInFunctions.put(atan2.getIdentifier(), atan2);

        builtInFunctions.put(string_function.getIdentifier(), string_function);
        builtInFunctions.put(codepoints_to_string.getIdentifier(), codepoints_to_string);
        builtInFunctions.put(string_to_codepoints.getIdentifier(), string_to_codepoints);
        builtInFunctions.put(replace.getIdentifier(), replace);
        builtInFunctions.put(substring2.getIdentifier(), substring2);
        builtInFunctions.put(substring3.getIdentifier(), substring3);
        builtInFunctions.put(substring_before.getIdentifier(), substring_before);
        builtInFunctions.put(substring_after.getIdentifier(), substring_after);
        for (int i = 0; i < 100; i++) {
            builtInFunctions.put(new FunctionIdentifier("concat", i), concat);
        }
        builtInFunctions.put(ends_with.getIdentifier(), ends_with);
        builtInFunctions.put(string_join1.getIdentifier(), string_join1);
        builtInFunctions.put(string_join2.getIdentifier(), string_join2);
        builtInFunctions.put(string_length.getIdentifier(), string_length);
        builtInFunctions.put(tokenize1.getIdentifier(), tokenize1);
        builtInFunctions.put(tokenize2.getIdentifier(), tokenize2);
        builtInFunctions.put(lower_case.getIdentifier(), lower_case);
        builtInFunctions.put(upper_case.getIdentifier(), upper_case);
        builtInFunctions.put(translate.getIdentifier(), translate);
        builtInFunctions.put(codepoint_equal.getIdentifier(), codepoint_equal);
        builtInFunctions.put(starts_with.getIdentifier(), starts_with);
        builtInFunctions.put(matches.getIdentifier(), matches);
        builtInFunctions.put(contains.getIdentifier(), contains);
        builtInFunctions.put(normalize_space.getIdentifier(), normalize_space);
        builtInFunctions.put(number.getIdentifier(), number);

        builtInFunctions.put(duration.getIdentifier(), duration);
        builtInFunctions.put(dayTimeDuration.getIdentifier(), dayTimeDuration);
        builtInFunctions.put(yearMonthDuration.getIdentifier(), yearMonthDuration);
        builtInFunctions.put(years_from_duration.getIdentifier(), years_from_duration);
        builtInFunctions.put(months_from_duration.getIdentifier(), months_from_duration);
        builtInFunctions.put(days_from_duration.getIdentifier(), days_from_duration);
        builtInFunctions.put(hours_from_duration.getIdentifier(), hours_from_duration);
        builtInFunctions.put(minutes_from_duration.getIdentifier(), minutes_from_duration);
        builtInFunctions.put(seconds_from_duration.getIdentifier(), seconds_from_duration);

        builtInFunctions.put(dateTime.getIdentifier(), dateTime);
        builtInFunctions.put(current_dateTime.getIdentifier(), current_dateTime);
        builtInFunctions.put(format_dateTime.getIdentifier(), format_dateTime);
        builtInFunctions.put(year_from_dateTime.getIdentifier(), year_from_dateTime);
        builtInFunctions.put(month_from_dateTime.getIdentifier(), month_from_dateTime);
        builtInFunctions.put(day_from_dateTime.getIdentifier(), day_from_dateTime);
        builtInFunctions.put(hours_from_dateTime.getIdentifier(), hours_from_dateTime);
        builtInFunctions.put(minutes_from_dateTime.getIdentifier(), minutes_from_dateTime);
        builtInFunctions.put(seconds_from_dateTime.getIdentifier(), seconds_from_dateTime);
        builtInFunctions.put(timezone_from_dateTime.getIdentifier(), timezone_from_dateTime);
        builtInFunctions.put(adjust_dateTime_to_timezone1.getIdentifier(), adjust_dateTime_to_timezone1);
        builtInFunctions.put(adjust_dateTime_to_timezone2.getIdentifier(), adjust_dateTime_to_timezone2);

        builtInFunctions.put(date.getIdentifier(), date);
        builtInFunctions.put(current_date.getIdentifier(), current_date);
        builtInFunctions.put(format_date.getIdentifier(), format_date);
        builtInFunctions.put(year_from_date.getIdentifier(), year_from_date);
        builtInFunctions.put(month_from_date.getIdentifier(), month_from_date);
        builtInFunctions.put(day_from_date.getIdentifier(), day_from_date);
        builtInFunctions.put(timezone_from_date.getIdentifier(), timezone_from_date);
        builtInFunctions.put(adjust_date_to_timezone1.getIdentifier(), adjust_date_to_timezone1);
        builtInFunctions.put(adjust_date_to_timezone2.getIdentifier(), adjust_date_to_timezone2);

        builtInFunctions.put(time.getIdentifier(), time);
        builtInFunctions.put(current_time.getIdentifier(), current_time);
        builtInFunctions.put(format_time.getIdentifier(), format_time);
        builtInFunctions.put(hours_from_time.getIdentifier(), hours_from_time);
        builtInFunctions.put(minutes_from_time.getIdentifier(), minutes_from_time);
        builtInFunctions.put(seconds_from_time.getIdentifier(), seconds_from_time);
        builtInFunctions.put(timezone_from_time.getIdentifier(), timezone_from_time);
        builtInFunctions.put(adjust_time_to_timezone1.getIdentifier(), adjust_time_to_timezone1);
        builtInFunctions.put(adjust_time_to_timezone2.getIdentifier(), adjust_time_to_timezone2);

        builtInFunctions.put(anyURI.getIdentifier(), anyURI);

        builtInFunctions.put(hexBinary.getIdentifier(), hexBinary);
        builtInFunctions.put(base64Binary.getIdentifier(), base64Binary);

        builtInFunctions.put(keys.getIdentifier(), keys);
        builtInFunctions.put(members.getIdentifier(), members);
        builtInFunctions.put(null_function.getIdentifier(), null_function);
        builtInFunctions.put(size.getIdentifier(), size);
        builtInFunctions.put(accumulate.getIdentifier(), accumulate);
        builtInFunctions.put(descendant_arrays.getIdentifier(), descendant_arrays);
        builtInFunctions.put(descendant_objects.getIdentifier(), descendant_objects);
        builtInFunctions.put(descendant_pairs.getIdentifier(), descendant_pairs);
        builtInFunctions.put(flatten.getIdentifier(), flatten);
        builtInFunctions.put(intersect.getIdentifier(), intersect);
        builtInFunctions.put(project.getIdentifier(), project);
        builtInFunctions.put(remove_keys.getIdentifier(), remove_keys);
        builtInFunctions.put(values.getIdentifier(), values);

        builtInFunctions.put(get_transformer.getIdentifier(), get_transformer);
        builtInFunctions.put(get_estimator.getIdentifier(), get_estimator);
        builtInFunctions.put(annotate.getIdentifier(), annotate);
    }

    static {
        userDefinedFunctionsExecutionMode = new HashMap<>();
        userDefinedFunctions = new HashMap<>();
        userDefinedFunctionIdentifiersWithUnsetExecutionModes = new ArrayList<>();
    }

    public static void clearUserDefinedFunctions() {
        userDefinedFunctionsExecutionMode.clear();
        userDefinedFunctions.clear();
    }

    public static boolean checkUserDefinedFunctionExecutionModeExists(FunctionIdentifier identifier) {
        return userDefinedFunctionsExecutionMode.containsKey(identifier);
    }

    public static ExecutionMode getUserDefinedFunctionExecutionMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (checkUserDefinedFunctionExecutionModeExists(identifier)) {
            return userDefinedFunctionsExecutionMode.get(identifier);
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public static void addUserDefinedFunctionExecutionMode(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata meta
    ) {
        if (
            builtInFunctions.containsKey(functionIdentifier)
                ||
                (!suppressErrorsForFunctionSignatureCollision
                    && userDefinedFunctionsExecutionMode.containsKey(functionIdentifier))
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }

        if (isAddingNewUnsetUserDefinedFunction(functionIdentifier, executionMode)) {
            userDefinedFunctionIdentifiersWithUnsetExecutionModes.add(functionIdentifier);
        } else if (isUpdatingUnsetUserDefinedFunctionToNonUnset(functionIdentifier, executionMode)) {
            userDefinedFunctionIdentifiersWithUnsetExecutionModes.remove(functionIdentifier);
        }
        userDefinedFunctionsExecutionMode.put(functionIdentifier, executionMode);
    }

    private static boolean isAddingNewUnsetUserDefinedFunction(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return !userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && executionMode == ExecutionMode.UNSET;
    }

    private static boolean isUpdatingUnsetUserDefinedFunctionToNonUnset(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && userDefinedFunctionsExecutionMode.get(functionIdentifier) == ExecutionMode.UNSET
            && executionMode != ExecutionMode.UNSET;
    }

    public static List<FunctionIdentifier> getUserDefinedFunctionIdentifiersWithUnsetExecutionModes() {
        return userDefinedFunctionIdentifiersWithUnsetExecutionModes;
    }

    public static RuntimeIterator getUserDefinedFunctionCallIterator(
            FunctionIdentifier identifier,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        if (Functions.checkUserDefinedFunctionExists(identifier)) {
            return buildUserDefinedFunctionCallIterator(
                getUserDefinedFunction(identifier),
                executionMode,
                metadata,
                arguments
            );
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public static RuntimeIterator buildUserDefinedFunctionCallIterator(
            FunctionItem functionItem,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        FunctionItemCallIterator functionCallIterator = new FunctionItemCallIterator(
                functionItem,
                arguments,
                executionMode,
                metadata
        );
        if (!functionItem.getSignature().getReturnType().equals(mostGeneralSequenceType)) {
            return new TypePromotionIterator(
                    functionCallIterator,
                    functionItem.getSignature().getReturnType(),
                    "Invalid return type for "
                        + (functionItem.getIdentifier().getName().equals("")
                            ? ""
                            : (functionItem.getIdentifier().getName()) + " ")
                        + "function. ",
                    executionMode,
                    metadata
            );
        }
        return functionCallIterator;
    }

    public static void addUserDefinedFunction(FunctionItem function, ExceptionMetadata meta) {
        FunctionIdentifier functionIdentifier = function.getIdentifier();
        if (
            builtInFunctions.containsKey(functionIdentifier)
                || userDefinedFunctions.containsKey(functionIdentifier)
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        userDefinedFunctions.put(functionIdentifier, function);
    }

    public static boolean checkUserDefinedFunctionExists(FunctionIdentifier identifier) {
        return userDefinedFunctions.containsKey(identifier);
    }

    public static FunctionItem getUserDefinedFunction(FunctionIdentifier identifier) {
        FunctionItem functionItem = userDefinedFunctions.get(identifier);
        return functionItem.deepCopy();
    }

    public static boolean checkBuiltInFunctionExists(FunctionIdentifier identifier) {
        return builtInFunctions.containsKey(identifier);
    }

    public static BuiltinFunction getBuiltInFunction(FunctionIdentifier identifier) {
        return builtInFunctions.get(identifier);
    }

    public static RuntimeIterator getBuiltInFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        BuiltinFunction builtinFunction = builtInFunctions.get(identifier);

        for (int i = 0; i < arguments.size(); i++) {
            if (!builtinFunction.getSignature().getParameterTypes().get(i).equals(mostGeneralSequenceType)) {
                TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                        arguments.get(i),
                        builtinFunction.getSignature().getParameterTypes().get(i),
                        "Invalid argument for function " + identifier.getName() + ". ",
                        arguments.get(i).getHighestExecutionMode(),
                        arguments.get(i).getMetadata()
                );

                arguments.set(i, typePromotionIterator);
            }
        }

        RuntimeIterator functionCallIterator;
        try {
            Constructor<? extends RuntimeIterator> constructor = builtinFunction.getFunctionIteratorClass()
                .getConstructor(
                    List.class,
                    ExecutionMode.class,
                    ExceptionMetadata.class
                );
            functionCallIterator = constructor.newInstance(arguments, executionMode, metadata);
        } catch (ReflectiveOperationException ex) {
            throw new UnknownFunctionCallException(
                    identifier.getName(),
                    arguments.size(),
                    metadata
            );
        }

        if (!builtinFunction.getSignature().getReturnType().equals(mostGeneralSequenceType)) {
            return new TypePromotionIterator(
                    functionCallIterator,
                    builtinFunction.getSignature().getReturnType(),
                    "Invalid return type for function " + identifier.getName() + ". ",
                    functionCallIterator.getHighestExecutionMode(),
                    functionCallIterator.getMetadata()
            );
        }
        return functionCallIterator;
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 0),
                new FunctionSignature(
                        Collections.emptyList(),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String param1Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunction.BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 1),
                new FunctionSignature(
                        Collections.singletonList(sequenceTypes.get(param1Type)),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
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
                            Arrays.asList(sequenceTypes.get(param1Type), sequenceTypes.get(param2Type))
                        ),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
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
                                sequenceTypes.get(param1Type),
                                sequenceTypes.get(param2Type),
                                sequenceTypes.get(param3Type)
                            )
                        ),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass,
                builtInFunctionExecutionMode
        );
    }

    static final class BuiltinFunctions {
        /**
         * function that returns the context position
         */
        static final BuiltinFunction position = createBuiltinFunction(
            "position",
            "integer?",
            PositionFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the context size
         */
        static final BuiltinFunction last = createBuiltinFunction(
            "last",
            "integer?",
            LastFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that parses a JSON lines file
         */
        static final BuiltinFunction json_file1 = createBuiltinFunction(
            "json-file",
            "string",
            "item*",
            JsonFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        static final BuiltinFunction json_file2 = createBuiltinFunction(
            "json-file",
            "string",
            "integer?",
            "item*",
            JsonFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        /**
         * function that parses a structured JSON lines file into a DataFrame
         */
        static final BuiltinFunction structured_json_file = createBuiltinFunction(
            "structured-json-file",
            "string",
            "item*",
            StructuredJsonFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
        /**
         * function that parses a libSVM formatted file into a DataFrame
         */
        static final BuiltinFunction libsvm_file = createBuiltinFunction(
            "libsvm-file",
            "string",
            "item*",
            LibSVMFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
        /**
         * function that parses a JSON doc file
         */
        static final BuiltinFunction json_doc = createBuiltinFunction(
            "json-doc",
            "string",
            "item*",
            JsonDocFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that parses a text file
         */
        static final BuiltinFunction text_file1 = createBuiltinFunction(
            "text-file",
            "string",
            "item*",
            TextFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        static final BuiltinFunction text_file2 = createBuiltinFunction(
            "text-file",
            "string",
            "integer?",
            "item*",
            TextFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        /**
         * function that parallelizes item collections into a Spark RDD
         */
        static final BuiltinFunction parallelizeFunction1 = createBuiltinFunction(
            "parallelize",
            "item*",
            "item*",
            ParallelizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        static final BuiltinFunction parallelizeFunction2 = createBuiltinFunction(
            "parallelize",
            "item*",
            "integer",
            "item*",
            ParallelizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.RDD
        );
        /**
         * function that parses a parquet file
         */
        static final BuiltinFunction parquet_file = createBuiltinFunction(
            "parquet-file",
            "string",
            "item*",
            ParquetFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
        /**
         * function that parses a csv file
         */
        static final BuiltinFunction csv_file1 = createBuiltinFunction(
            "csv-file",
            "string",
            "item*",
            CSVFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
        /**
         * function that parses a csv file
         */
        static final BuiltinFunction csv_file2 = createBuiltinFunction(
            "csv-file",
            "string",
            "object",
            "item*",
            CSVFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );

        /**
         * function that parses a ROOT file
         */
        static final BuiltinFunction root_file1 = createBuiltinFunction(
            "root-file",
            "string",
            "item*",
            RootFileFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
        /**
         * function that parses a ROOT file
         */
        static final BuiltinFunction root_file2 = createBuiltinFunction(
            "root-file",
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
            "count",
            "item*",
            "integer",
            CountFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the effective boolean value of the given parameter
         */
        static final BuiltinFunction boolean_function = createBuiltinFunction(
            "boolean",
            "item*",
            "boolean",
            BooleanFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the minimum of a sequence
         */
        static final BuiltinFunction min = createBuiltinFunction(
            "min",
            "item*",
            "atomic?",
            MinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the maximum of a sequence
         */
        static final BuiltinFunction max = createBuiltinFunction(
            "max",
            "item*",
            "atomic?",
            MaxFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the average of a sequence
         */
        static final BuiltinFunction avg = createBuiltinFunction(
            "avg",
            "item*",
            "atomic?",
            AvgFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the sum of a sequence
         */
        static final BuiltinFunction sum1 = createBuiltinFunction(
            "sum",
            "item*",
            "atomic?",
            SumFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction sum2 = createBuiltinFunction(
            "sum",
            "item*",
            "item?",
            "atomic?",
            SumFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that returns true if the argument is the empty sequence
         */
        static final BuiltinFunction empty = createBuiltinFunction(
            "empty",
            "item*",
            "boolean",
            EmptyFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns true if the argument is a non-empty sequence
         */
        static final BuiltinFunction exists = createBuiltinFunction(
            "exists",
            "item*",
            "boolean",
            ExistsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the first item in a sequence
         */
        static final BuiltinFunction head = createBuiltinFunction(
            "head",
            "item*",
            "item?",
            HeadFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns all but the first item in a sequence
         */
        static final BuiltinFunction tail = createBuiltinFunction(
            "tail",
            "item*",
            "item*",
            TailFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns a sequence constructed by inserting an item or a sequence of items at a given position
         * within an existing sequence
         */
        static final BuiltinFunction insert_before = createBuiltinFunction(
            "insert-before",
            "item*",
            "item*",
            "item*",
            "item*",
            InsertBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns a new sequence containing all the items of $target except the item at position
         * $position.
         */
        static final BuiltinFunction remove = createBuiltinFunction(
            "remove",
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
            "reverse",
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
            "subsequence",
            "item*",
            "double",
            "item*",
            SubsequenceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        static final BuiltinFunction subsequence3 = createBuiltinFunction(
            "subsequence",
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
            "zero-or-one",
            "item*",
            "item?",
            ZeroOrOneIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns $arg if it contains one or more items. Otherwise, raises an error.
         */
        static final BuiltinFunction one_or_more = createBuiltinFunction(
            "one-or-more",
            "item*",
            "item+",
            OneOrMoreIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns $arg if it contains exactly one item. Otherwise, raises an error.
         */
        static final BuiltinFunction exactly_one = createBuiltinFunction(
            "exactly-one",
            "item*",
            "item",
            ExactlyOneIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the values that appear in a sequence, with duplicates eliminated
         */
        static final BuiltinFunction distinct_values = createBuiltinFunction(
            "distinct-values",
            "item*",
            "atomic*",
            DistinctValuesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns indices of items that are equal to the search parameter
         */
        static final BuiltinFunction index_of = createBuiltinFunction(
            "index-of",
            "atomic*",
            "atomic",
            "integer*",
            IndexOfFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns whether two sequences are deep-equal to each other
         */
        static final BuiltinFunction deep_equal = createBuiltinFunction(
            "deep-equal",
            "item*",
            "item*",
            "boolean",
            DeepEqualFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction integer_function = createBuiltinFunction(
            "integer",
            "item?",
            "integer?",
            IntegerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction double_function = createBuiltinFunction(
            "double",
            "item?",
            "double?",
            DoubleFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction decimal_function = createBuiltinFunction(
            "decimal",
            "item?",
            "decimal?",
            DecimalFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the absolute value of the arg
         */
        static final BuiltinFunction abs = createBuiltinFunction(
            "abs",
            "double?",
            "double?",
            AbsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that rounds $arg upwards to a whole number
         */
        static final BuiltinFunction ceiling = createBuiltinFunction(
            "ceiling",
            "double?",
            "double?",
            CeilingFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that rounds $arg downwards to a whole number
         */
        static final BuiltinFunction floor = createBuiltinFunction(
            "floor",
            "double?",
            "double?",
            FloorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that rounds a value to a specified number of decimal places, rounding upwards if two such values are
         * equally near
         */
        static final BuiltinFunction round1 = createBuiltinFunction(
            "round",
            "double?",
            "double?",
            RoundFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction round2 = createBuiltinFunction(
            "round",
            "double?",
            "integer",
            "double?",
            RoundFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that rounds a value to a specified number of decimal places, rounding to make the last digit even if
         * two such values are equally near
         */
        static final BuiltinFunction round_half_to_even1 = createBuiltinFunction(
            "round-half-to-even",
            "double?",
            "double?",
            RoundHalfToEvenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction round_half_to_even2 = createBuiltinFunction(
            "round-half-to-even",
            "double?",
            "integer",
            "double?",
            RoundHalfToEvenFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the approximation the mathematical constant
         */
        static final BuiltinFunction pi = createBuiltinFunction(
            "pi",
            "double?",
            PiFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the value of e^x
         */
        static final BuiltinFunction exp = createBuiltinFunction(
            "exp",
            "double?",
            "double?",
            ExpFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the value of 10^x
         */
        static final BuiltinFunction exp10 = createBuiltinFunction(
            "exp10",
            "double?",
            "double?",
            Exp10FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the natural logarithm of the argument
         */
        static final BuiltinFunction log = createBuiltinFunction(
            "log",
            "double?",
            "double?",
            LogFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the base-ten logarithm of the argument
         */
        static final BuiltinFunction log10 = createBuiltinFunction(
            "log10",
            "double?",
            "double?",
            Log10FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the result of raising the first argument to the power of the second
         */
        static final BuiltinFunction pow = createBuiltinFunction(
            "pow",
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
            "sqrt",
            "double?",
            "double?",
            SqrtFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the sine of the angle given in radians
         */
        static final BuiltinFunction sin = createBuiltinFunction(
            "sin",
            "double?",
            "double?",
            SinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the cosine of the angle given in radians
         */
        static final BuiltinFunction cos = createBuiltinFunction(
            "cos",
            "double?",
            "double?",
            CosFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the tangent of the angle given in radians
         */
        static final BuiltinFunction tan = createBuiltinFunction(
            "tan",
            "double?",
            "double?",
            TanFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the arc sine of the angle given in radians
         */
        static final BuiltinFunction asin = createBuiltinFunction(
            "asin",
            "double?",
            "double?",
            ASinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the arc cosine of the angle given in radians
         */
        static final BuiltinFunction acos = createBuiltinFunction(
            "acos",
            "double?",
            "double?",
            ACosFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the arc tangent of the angle given in radians
         */
        static final BuiltinFunction atan = createBuiltinFunction(
            "atan",
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
            "atan2",
            "double",
            "double",
            "double",
            ATan2FunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that returns the string from the supplied argument
         */
        static final BuiltinFunction string_function = createBuiltinFunction(
            "string",
            "item?",
            "string?",
            StringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns substrings
         */
        static final BuiltinFunction substring2 = createBuiltinFunction(
            "substring",
            "string?",
            "double",
            "string",
            SubstringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction substring3 = createBuiltinFunction(
            "substring",
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
        static final BuiltinFunction substring_before = createBuiltinFunction(
            "substring-before",
            "string?",
            "string?",
            "string",
            SubstringBeforeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the part of the first variable that follows the first occurrence of the second
         * vairable.
         */
        static final BuiltinFunction substring_after = createBuiltinFunction(
            "substring-after",
            "string?",
            "string?",
            "string",
            SubstringAfterFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns substrings
         */
        static final BuiltinFunction concat =
            new BuiltinFunction(
                    new FunctionIdentifier("concat", 100),
                    new FunctionSignature(
                            Collections.nCopies(
                                100,
                                sequenceTypes.get("atomic*")
                            ),
                            sequenceTypes.get("string")
                    ),
                    ConcatFunctionIterator.class,
                    BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
            );
        /**
         * function that converts codepoints to a string
         */
        static final BuiltinFunction codepoints_to_string = createBuiltinFunction(
            "codepoints-to-string",
            "integer*",
            "string",
            CodepointsToStringFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that converts a string to codepoints
         */
        static final BuiltinFunction string_to_codepoints = createBuiltinFunction(
            "string-to-codepoints",
            "string?",
            "integer*",
            StringToCodepointsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that compares Strings codepoint-by-codepoint
         */
        static final BuiltinFunction codepoint_equal = createBuiltinFunction(
            "codepoint-equal",
            "string?",
            "string?",
            "boolean?",
            CodepointEqualFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns substrings
         */
        static final BuiltinFunction string_join1 = createBuiltinFunction(
            "string-join",
            "string*",
            "string",
            StringJoinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction string_join2 = createBuiltinFunction(
            "string-join",
            "string*",
            "string",
            "string",
            StringJoinFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that replaces parts of a string according to a regex expression
         */
        static final BuiltinFunction replace = createBuiltinFunction(
            "replace",
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
        static final BuiltinFunction string_length = createBuiltinFunction(
            "string-length",
            "string?",
            "integer",
            StringLengthFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns tokens
         */
        static final BuiltinFunction tokenize1 = createBuiltinFunction(
            "tokenize",
            "string?",
            "string*",
            TokenizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction tokenize2 = createBuiltinFunction(
            "tokenize",
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
            "lower-case",
            "string?",
            "string",
            LowerCaseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that turns all upper-case characters to upper-case
         */
        static final BuiltinFunction upper_case = createBuiltinFunction(
            "upper-case",
            "string?",
            "string",
            UpperCaseFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that turns all upper-case characters to upper-case
         */
        static final BuiltinFunction translate = createBuiltinFunction(
            "translate",
            "string?",
            "string",
            "string",
            "string",
            TranslateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that checks whether a string ends with a substring
         */
        static final BuiltinFunction ends_with = createBuiltinFunction(
            "ends-with",
            "string?",
            "string?",
            "boolean",
            EndsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that checks whether a string starts with a substring
         */
        static final BuiltinFunction starts_with = createBuiltinFunction(
            "starts-with",
            "string?",
            "string?",
            "boolean",
            StartsWithFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that checks whether a string contains a substring
         */
        static final BuiltinFunction contains = createBuiltinFunction(
            "contains",
            "string?",
            "string?",
            "boolean",
            ContainsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that checks whether a string matches a regular expression
         */
        static final BuiltinFunction matches = createBuiltinFunction(
            "matches",
            "string?",
            "string",
            "boolean",
            MatchesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that normalizes spaces in a string
         */
        static final BuiltinFunction normalize_space = createBuiltinFunction(
            "normalize-space",
            "string?",
            "string",
            NormalizeSpaceFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that that returns the double representation of the input string or number
         */
        static final BuiltinFunction number = createBuiltinFunction(
            "number",
            "atomic?",
            "double",
            NumberFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the duration item from the supplied string
         */
        static final BuiltinFunction duration = createBuiltinFunction(
            "duration",
            "string?",
            "duration?",
            DurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the yearMonthDuration item from the supplied string
         */
        static final BuiltinFunction yearMonthDuration = createBuiltinFunction(
            "yearMonthDuration",
            "string?",
            "yearMonthDuration?",
            YearMonthDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the dayTimeDuration item from the supplied string
         */
        static final BuiltinFunction dayTimeDuration = createBuiltinFunction(
            "dayTimeDuration",
            "string?",
            "dayTimeDuration?",
            DayTimeDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that returns the years from a duration
         */
        static final BuiltinFunction years_from_duration = createBuiltinFunction(
            "years-from-duration",
            "duration?",
            "integer?",
            YearsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the months from a duration
         */
        static final BuiltinFunction months_from_duration = createBuiltinFunction(
            "months-from-duration",
            "duration?",
            "integer?",
            MonthsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the days from a duration
         */
        static final BuiltinFunction days_from_duration = createBuiltinFunction(
            "days-from-duration",
            "duration?",
            "integer?",
            DaysFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the hours from a duration
         */
        static final BuiltinFunction hours_from_duration = createBuiltinFunction(
            "hours-from-duration",
            "duration?",
            "integer?",
            HoursFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the minutes from a duration
         */
        static final BuiltinFunction minutes_from_duration = createBuiltinFunction(
            "minutes-from-duration",
            "duration?",
            "integer?",
            MinutesFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a duration
         */
        static final BuiltinFunction seconds_from_duration = createBuiltinFunction(
            "seconds-from-duration",
            "duration?",
            "decimal?",
            SecondsFromDurationFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the dateTime item from the supplied string
         */
        static final BuiltinFunction dateTime = createBuiltinFunction(
            "dateTime",
            "string?",
            "dateTime?",
            DateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the current dateTime item
         */
        static final BuiltinFunction current_dateTime = createBuiltinFunction(
            "current-dateTime",
            "dateTime?",
            CurrentDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns a string containing a dateTime value formated for display
         */
        static final BuiltinFunction format_dateTime = createBuiltinFunction(
            "format-dateTime",
            "dateTime?",
            "string",
            "string?",
            FormatDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the year from a dateTime
         */
        static final BuiltinFunction year_from_dateTime = createBuiltinFunction(
            "year-from-dateTime",
            "dateTime?",
            "integer?",
            YearFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the month from a dateTime
         */
        static final BuiltinFunction month_from_dateTime = createBuiltinFunction(
            "month-from-dateTime",
            "dateTime?",
            "integer?",
            MonthFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the day from a dateTime
         */
        static final BuiltinFunction day_from_dateTime = createBuiltinFunction(
            "day-from-dateTime",
            "dateTime?",
            "integer?",
            DayFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the hours from a dateTime
         */
        static final BuiltinFunction hours_from_dateTime = createBuiltinFunction(
            "hours-from-dateTime",
            "dateTime?",
            "integer?",
            HoursFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the minutes from a dateTime
         */
        static final BuiltinFunction minutes_from_dateTime = createBuiltinFunction(
            "minutes-from-dateTime",
            "dateTime?",
            "integer?",
            MinutesFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a dateTime
         */
        static final BuiltinFunction seconds_from_dateTime = createBuiltinFunction(
            "seconds-from-dateTime",
            "dateTime?",
            "decimal?",
            SecondsFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a dateTime
         */
        static final BuiltinFunction timezone_from_dateTime = createBuiltinFunction(
            "timezone-from-dateTime",
            "dateTime?",
            "dayTimeDuration?",
            TimezoneFromDateTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_dateTime_to_timezone1 = createBuiltinFunction(
            "adjust-dateTime-to-timezone",
            "dateTime?",
            "dateTime?",
            AdjustDateTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction adjust_dateTime_to_timezone2 = createBuiltinFunction(
            "adjust-dateTime-to-timezone",
            "dateTime?",
            "dayTimeDuration?",
            "dateTime?",
            AdjustDateTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that returns the date item from the supplied string
         */
        static final BuiltinFunction date = createBuiltinFunction(
            "date",
            "string?",
            "date?",
            DateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the current date item
         */
        static final BuiltinFunction current_date = createBuiltinFunction(
            "current-date",
            "date?",
            CurrentDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns a string containing a date value formated for display
         */
        static final BuiltinFunction format_date = createBuiltinFunction(
            "format-date",
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
            "year-from-date",
            "date?",
            "integer?",
            YearFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the month from a date
         */
        static final BuiltinFunction month_from_date = createBuiltinFunction(
            "month-from-date",
            "date?",
            "integer?",
            MonthFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the day from a date
         */
        static final BuiltinFunction day_from_date = createBuiltinFunction(
            "day-from-date",
            "date?",
            "integer?",
            DayFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a date
         */
        static final BuiltinFunction timezone_from_date = createBuiltinFunction(
            "timezone-from-date",
            "date?",
            "dayTimeDuration?",
            TimezoneFromDateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );


        /**
         * function that adjusts a date value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_date_to_timezone1 = createBuiltinFunction(
            "adjust-date-to-timezone",
            "date?",
            "date?",
            AdjustDateToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction adjust_date_to_timezone2 = createBuiltinFunction(
            "adjust-date-to-timezone",
            "date?",
            "dayTimeDuration?",
            "date?",
            AdjustDateToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the time item from the supplied string
         */
        static final BuiltinFunction time = createBuiltinFunction(
            "time",
            "string?",
            "time?",
            TimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the current time item
         */
        static final BuiltinFunction current_time = createBuiltinFunction(
            "current-time",
            "time?",
            CurrentTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns a string containing a time value formated for display
         */
        static final BuiltinFunction format_time = createBuiltinFunction(
            "format-time",
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
            "hours-from-time",
            "time?",
            "integer?",
            HoursFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the minutes from a time
         */
        static final BuiltinFunction minutes_from_time = createBuiltinFunction(
            "minutes-from-time",
            "time?",
            "integer?",
            MinutesFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a time
         */
        static final BuiltinFunction seconds_from_time = createBuiltinFunction(
            "seconds-from-time",
            "time?",
            "decimal?",
            SecondsFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the seconds from a time
         */
        static final BuiltinFunction timezone_from_time = createBuiltinFunction(
            "timezone-from-time",
            "time?",
            "dayTimeDuration?",
            TimezoneFromTimeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that adjusts a time value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_time_to_timezone1 = createBuiltinFunction(
            "adjust-time-to-timezone",
            "time?",
            "time?",
            AdjustTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        static final BuiltinFunction adjust_time_to_timezone2 = createBuiltinFunction(
            "adjust-time-to-timezone",
            "time?",
            "dayTimeDuration?",
            "time?",
            AdjustTimeToTimezone.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the time item from the supplied string
         */
        static final BuiltinFunction anyURI = createBuiltinFunction(
            "anyURI",
            "atomic?",
            "anyURI?",
            AnyURIFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the hexBinary item from the supplied string
         */
        static final BuiltinFunction hexBinary = createBuiltinFunction(
            "hexBinary",
            "string?",
            "hexBinary?",
            HexBinaryFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the base64Binary item from the supplied string
         */
        static final BuiltinFunction base64Binary = createBuiltinFunction(
            "base64Binary",
            "string?",
            "base64Binary?",
            Base64BinaryFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function that returns the keys of a Json Object
         */
        static final BuiltinFunction keys = createBuiltinFunction(
            "keys",
            "item*",
            "item*",
            ObjectKeysFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
        );
        /**
         * function that returns returns all members of all arrays of the supplied sequence
         */
        static final BuiltinFunction members = createBuiltinFunction(
            "members",
            "item*",
            "item*",
            ArrayMembersFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the JSON null
         */
        static final BuiltinFunction null_function = createBuiltinFunction(
            "null",
            "null?",
            NullFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns the length of an array
         */
        static final BuiltinFunction size = createBuiltinFunction(
            "size",
            "array?",
            "integer?",
            ArraySizeFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that dynamically creates an object that merges the values of key collisions into arrays
         */
        static final BuiltinFunction accumulate = createBuiltinFunction(
            "accumulate",
            "item*",
            "object",
            ObjectAccumulateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );
        /**
         * function that returns all arrays contained within the supplied items, regardless of depth.
         */
        static final BuiltinFunction descendant_arrays = createBuiltinFunction(
            "descendant-arrays",
            "item*",
            "item*",
            ArrayDescendantFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        static final BuiltinFunction descendant_objects = createBuiltinFunction(
            "descendant-objects",
            "item*",
            "item*",
            ObjectDescendantFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        static final BuiltinFunction descendant_pairs = createBuiltinFunction(
            "descendant-pairs",
            "item*",
            "item*",
            ObjectDescendantPairsFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that recursively flattens arrays in the input sequence, leaving non-arrays intact
         */
        static final BuiltinFunction flatten = createBuiltinFunction(
            "flatten",
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
            "intersect",
            "item*",
            "object+",
            ObjectIntersectFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );
        /**
         * function that projects objects by filtering their pairs and leaves non-objects intact
         */
        static final BuiltinFunction project = createBuiltinFunction(
            "project",
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
            "remove-keys",
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
            "values",
            "item*",
            "item*",
            ObjectValuesFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT
        );

        /**
         * function fetches the transformer class from SparkML API
         */
        static final BuiltinFunction get_transformer = createBuiltinFunction(
            "get-transformer",
            "string",
            "item",
            GetTransformerFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function fetches the estimator class from SparkML API
         */
        static final BuiltinFunction get_estimator = createBuiltinFunction(
            "get-estimator",
            "string",
            "item",
            GetEstimatorFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL
        );

        /**
         * function converts given RDD or local data to a DataFrame using a schema
         */
        static final BuiltinFunction annotate = createBuiltinFunction(
            "annotate",
            "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            "object",
            "item*", // TODO: revert back to ObjectItem when TypePromotionIter. has DF implementation
            AnnotateFunctionIterator.class,
            BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME
        );
    }
}
