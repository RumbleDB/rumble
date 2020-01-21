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

package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.exceptions.DuplicateFunctionIdentifierException;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.FunctionItemCallIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.NullFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayDescendantFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayFlattenFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayMembersFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArraySizeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.binaries.Base64BinaryFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.binaries.HexBinaryFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.booleans.BooleanFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.context.LastFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.context.PositionFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.DateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.DateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.TimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.AdjustDateTimeToTimezone;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.AdjustDateToTimezone;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.AdjustTimeToTimezone;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.DayFromDateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.DayFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.HoursFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.HoursFromTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.MinutesFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.MinutesFromTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.MonthFromDateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.MonthFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.SecondsFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.SecondsFromTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.TimezoneFromDateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.TimezoneFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.TimezoneFromTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.YearFromDateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.YearFromDateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.DayTimeDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.DurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.YearMonthDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.DaysFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.HoursFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.MinutesFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.MonthsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.SecondsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.YearsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.io.JsonDocFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.AbsFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.CeilingFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.DecimalFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.DoubleFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.FloorFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.IntegerFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.PiFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.RoundFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.RoundHalfToEvenFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.Exp10FunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.ExpFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.Log10FunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.LogFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.PowFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.SqrtFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.ACosFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.ASinFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.ATan2FunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.ATanFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.CosFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.SinFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.TanFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectAccumulateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectDescendantFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectDescendantPairsFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectIntersectFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectKeysFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectProjectFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectRemoveKeysFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectValuesFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.AvgFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.CountFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.MaxFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.MinFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.SumFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.ExactlyOneIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.OneOrMoreIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.ZeroOrOneIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.EmptyFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.ExistsFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.HeadFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.InsertBeforeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.RemoveFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.ReverseFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.SubsequenceFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TailFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.value.DeepEqualFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.value.DistinctValuesFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.value.IndexOfFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.ConcatFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.ContainsFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.EndsWithFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.MatchesFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.NormalizeSpaceFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StartsWithFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringJoinFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringLengthFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringAfterFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringBeforeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.TokenizeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.TypePromotionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.spark.iterator.function.JsonFileFunctionIterator;
import sparksoniq.spark.iterator.function.ParallelizeFunctionIterator;
import sparksoniq.spark.iterator.function.ParquetFileFunctionIterator;
import sparksoniq.spark.iterator.function.StructuredJsonFileFunctionIterator;
import sparksoniq.spark.iterator.function.TextFileFunctionIterator;
import sparksoniq.spark.ml.GetTransformerFunctionIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.abs;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.accumulate;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.acos;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_dateTime_to_timezone1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_dateTime_to_timezone2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_date_to_timezone1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_date_to_timezone2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_time_to_timezone1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.adjust_time_to_timezone2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.asin;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.atan;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.atan2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.avg;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.base64Binary;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.boolean_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.ceiling;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.concat;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.contains;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.cos;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.count;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.date;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.dayTimeDuration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.day_from_date;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.day_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.days_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.decimal_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.deep_equal;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.descendant_arrays;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.descendant_objects;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.descendant_pairs;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.distinct_values;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.double_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.empty;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.ends_with;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.exactly_one;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.exists;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.exp;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.exp10;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.flatten;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.floor;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.get_transformer;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.head;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.hexBinary;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.hours_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.hours_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.hours_from_time;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.index_of;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.insert_before;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.integer_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.intersect;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.json_doc;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.json_file1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.json_file2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.keys;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.last;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.log;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.log10;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.matches;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.max;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.members;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.min;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.minutes_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.minutes_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.minutes_from_time;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.month_from_date;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.month_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.months_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.normalize_space;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.null_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.one_or_more;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.parallelizeFunction1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.parallelizeFunction2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.parquet_file;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.pi;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.position;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.pow;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.project;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.remove;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.remove_keys;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.reverse;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.round1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.round2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.round_half_to_even1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.round_half_to_even2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.seconds_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.seconds_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.seconds_from_time;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.sin;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.size;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.sqrt;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.starts_with;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.string_function;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.string_join1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.string_join2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.string_length;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.structured_json_file;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.subsequence2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.subsequence3;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.substring2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.substring3;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.substring_after;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.substring_before;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.sum1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.sum2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.tail;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.tan;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.text_file1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.text_file2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.time;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.timezone_from_date;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.timezone_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.timezone_from_time;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.tokenize1;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.tokenize2;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.values;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.yearMonthDuration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.year_from_date;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.year_from_dateTime;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.years_from_duration;
import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.zero_or_one;
import static sparksoniq.semantics.types.SequenceType.mostGeneralSequenceType;

public class Functions {
    private static final HashMap<FunctionIdentifier, BuiltinFunction> builtInFunctions;
    private static HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;
    private static HashMap<FunctionIdentifier, ExecutionMode> userDefinedFunctionsExecutionMode;

    private static final Map<String, ItemType> itemTypes;

    static {
        itemTypes = new HashMap<>();
        itemTypes.put("item", new ItemType(ItemTypes.Item));

        itemTypes.put("object", new ItemType(ItemTypes.ObjectItem));
        itemTypes.put("array", new ItemType(ItemTypes.ArrayItem));

        itemTypes.put("atomic", new ItemType(ItemTypes.AtomicItem));
        itemTypes.put("string", new ItemType(ItemTypes.StringItem));
        itemTypes.put("integer", new ItemType(ItemTypes.IntegerItem));
        itemTypes.put("decimal", new ItemType(ItemTypes.DecimalItem));
        itemTypes.put("double", new ItemType(ItemTypes.DoubleItem));
        itemTypes.put("boolean", new ItemType(ItemTypes.BooleanItem));

        itemTypes.put("duration", new ItemType(ItemTypes.DurationItem));
        itemTypes.put("yearMonthDuration", new ItemType(ItemTypes.YearMonthDurationItem));
        itemTypes.put("dayTimeDuration", new ItemType(ItemTypes.DayTimeDurationItem));

        itemTypes.put("dateTime", new ItemType(ItemTypes.DateTimeItem));
        itemTypes.put("date", new ItemType(ItemTypes.DateItem));
        itemTypes.put("time", new ItemType(ItemTypes.TimeItem));

        itemTypes.put("hexBinary", new ItemType(ItemTypes.HexBinaryItem));
        itemTypes.put("base64Binary", new ItemType(ItemTypes.Base64BinaryItem));

        itemTypes.put("null", new ItemType(ItemTypes.NullItem));

    }

    private static final Map<String, SequenceType> sequenceTypes;
    static {
        sequenceTypes = new HashMap<>();
        sequenceTypes.put("item", new SequenceType(itemTypes.get("item"), SequenceType.Arity.One));
        sequenceTypes.put("item?", new SequenceType(itemTypes.get("item"), SequenceType.Arity.OneOrZero));
        sequenceTypes.put("item*", new SequenceType(itemTypes.get("item"), SequenceType.Arity.ZeroOrMore));
        sequenceTypes.put("item+", new SequenceType(itemTypes.get("item"), SequenceType.Arity.OneOrMore));

        sequenceTypes.put("object", new SequenceType(itemTypes.get("object"), SequenceType.Arity.One));
        sequenceTypes.put("object+", new SequenceType(itemTypes.get("object"), SequenceType.Arity.OneOrMore));

        sequenceTypes.put("array?", new SequenceType(itemTypes.get("array"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("atomic", new SequenceType(itemTypes.get("atomic"), SequenceType.Arity.One));
        sequenceTypes.put("atomic?", new SequenceType(itemTypes.get("atomic"), SequenceType.Arity.OneOrZero));
        sequenceTypes.put("atomic*", new SequenceType(itemTypes.get("atomic"), SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("string", new SequenceType(itemTypes.get("string"), SequenceType.Arity.One));
        sequenceTypes.put("string?", new SequenceType(itemTypes.get("string"), SequenceType.Arity.OneOrZero));
        sequenceTypes.put("string*", new SequenceType(itemTypes.get("string"), SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("integer", new SequenceType(itemTypes.get("integer"), SequenceType.Arity.One));
        sequenceTypes.put("integer?", new SequenceType(itemTypes.get("integer"), SequenceType.Arity.OneOrZero));
        sequenceTypes.put("integer*", new SequenceType(itemTypes.get("integer"), SequenceType.Arity.ZeroOrMore));

        sequenceTypes.put("decimal?", new SequenceType(itemTypes.get("decimal"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("double", new SequenceType(itemTypes.get("double"), SequenceType.Arity.One));
        sequenceTypes.put("double?", new SequenceType(itemTypes.get("double"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("boolean", new SequenceType(itemTypes.get("boolean"), SequenceType.Arity.One));

        sequenceTypes.put("duration?", new SequenceType(itemTypes.get("duration"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put(
            "yearMonthDuration?",
            new SequenceType(itemTypes.get("yearMonthDuration"), SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put(
            "dayTimeDuration?",
            new SequenceType(itemTypes.get("dayTimeDuration"), SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("dateTime?", new SequenceType(itemTypes.get("dateTime"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("date?", new SequenceType(itemTypes.get("date"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("time?", new SequenceType(itemTypes.get("time"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put("hexBinary?", new SequenceType(itemTypes.get("hexBinary"), SequenceType.Arity.OneOrZero));

        sequenceTypes.put(
            "base64Binary?",
            new SequenceType(itemTypes.get("base64Binary"), SequenceType.Arity.OneOrZero)
        );

        sequenceTypes.put("null?", new SequenceType(itemTypes.get("null"), SequenceType.Arity.OneOrZero));

    }


    static {
        builtInFunctions = new HashMap<>();
        userDefinedFunctions = new HashMap<>();
        userDefinedFunctionsExecutionMode = new HashMap<>();

        builtInFunctions.put(position.getIdentifier(), position);
        builtInFunctions.put(last.getIdentifier(), last);

        builtInFunctions.put(json_file1.getIdentifier(), json_file1);
        builtInFunctions.put(json_file2.getIdentifier(), json_file2);
        builtInFunctions.put(structured_json_file.getIdentifier(), structured_json_file);
        builtInFunctions.put(json_doc.getIdentifier(), json_doc);
        builtInFunctions.put(text_file1.getIdentifier(), text_file1);
        builtInFunctions.put(text_file2.getIdentifier(), text_file2);
        builtInFunctions.put(parallelizeFunction1.getIdentifier(), parallelizeFunction1);
        builtInFunctions.put(parallelizeFunction2.getIdentifier(), parallelizeFunction2);
        builtInFunctions.put(parquet_file.getIdentifier(), parquet_file);

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
        builtInFunctions.put(starts_with.getIdentifier(), starts_with);
        builtInFunctions.put(matches.getIdentifier(), matches);
        builtInFunctions.put(contains.getIdentifier(), contains);
        builtInFunctions.put(normalize_space.getIdentifier(), normalize_space);

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
        builtInFunctions.put(year_from_date.getIdentifier(), year_from_date);
        builtInFunctions.put(month_from_date.getIdentifier(), month_from_date);
        builtInFunctions.put(day_from_date.getIdentifier(), day_from_date);
        builtInFunctions.put(timezone_from_date.getIdentifier(), timezone_from_date);
        builtInFunctions.put(adjust_date_to_timezone1.getIdentifier(), adjust_date_to_timezone1);
        builtInFunctions.put(adjust_date_to_timezone2.getIdentifier(), adjust_date_to_timezone2);

        builtInFunctions.put(time.getIdentifier(), time);
        builtInFunctions.put(hours_from_time.getIdentifier(), hours_from_time);
        builtInFunctions.put(minutes_from_time.getIdentifier(), minutes_from_time);
        builtInFunctions.put(seconds_from_time.getIdentifier(), seconds_from_time);
        builtInFunctions.put(timezone_from_time.getIdentifier(), timezone_from_time);
        builtInFunctions.put(adjust_time_to_timezone1.getIdentifier(), adjust_time_to_timezone1);
        builtInFunctions.put(adjust_time_to_timezone2.getIdentifier(), adjust_time_to_timezone2);

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
            IteratorMetadata metadata
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
                    IteratorMetadata.class
                );
            functionCallIterator = constructor.newInstance(arguments, metadata);
        } catch (ReflectiveOperationException e) {
            // TODO: Some functions have constructors with 2 and some with 3 params. Handle this more elegantly
            // TODO: Or alternatively, extend all constructors to have 3 params (executionMode)
            try {
                Constructor<? extends RuntimeIterator> constructor = builtinFunction.getFunctionIteratorClass()
                    .getConstructor(
                        List.class,
                        ExecutionMode.class,
                        IteratorMetadata.class
                    );
                functionCallIterator = constructor.newInstance(arguments, executionMode, metadata);
            } catch (ReflectiveOperationException ex) {
                throw new UnknownFunctionCallException(
                        identifier.getName(),
                        arguments.size(),
                        metadata.getExpressionMetadata()
                );
            }
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

    public static RuntimeIterator getUserDefinedFunctionCallIterator(
            FunctionIdentifier identifier,
            ExecutionMode executionMode,
            IteratorMetadata metadata,
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
                metadata.getExpressionMetadata()
        );

    }

    public static ExecutionMode getUserDefinedFunctionExecutionMode(
            FunctionIdentifier identifier,
            ExpressionMetadata metadata
    ) {
        if (userDefinedFunctionsExecutionMode.containsKey(identifier)) {
            return userDefinedFunctionsExecutionMode.get(identifier);
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
            IteratorMetadata metadata,
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

    public static void clearUserDefinedFunctions() {
        userDefinedFunctions.clear();
        userDefinedFunctionsExecutionMode.clear();
    }

    public static void addUserDefinedFunctionExecutionMode(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            boolean ignoreDuplicateFunction,
            ExpressionMetadata meta
    ) {
        if (
            builtInFunctions.containsKey(functionIdentifier)
                || userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
        ) {
            if (ignoreDuplicateFunction) {
                return;
            }
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        userDefinedFunctionsExecutionMode.put(functionIdentifier, executionMode);
    }

    public static void addUserDefinedFunction(FunctionItem function, ExpressionMetadata meta) {
        FunctionIdentifier functionIdentifier = function.getIdentifier();
        if (
            builtInFunctions.containsKey(functionIdentifier)
                || userDefinedFunctions.containsKey(functionIdentifier)
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        userDefinedFunctions.put(functionIdentifier, function);
    }

    public static boolean checkUserDefinedFunctionExecutionModeExists(FunctionIdentifier identifier) {
        return userDefinedFunctionsExecutionMode.containsKey(identifier);
    }

    public static boolean checkUserDefinedFunctionExists(FunctionIdentifier identifier) {
        return userDefinedFunctions.containsKey(identifier);
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 0),
                new FunctionSignature(
                        Collections.emptyList(),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String param1Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 1),
                new FunctionSignature(
                        Collections.singletonList(sequenceTypes.get(param1Type)),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String param1Type,
            String param2Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass
    ) {
        return new BuiltinFunction(
                new FunctionIdentifier(functionName, 2),
                new FunctionSignature(
                        Collections.unmodifiableList(
                            Arrays.asList(sequenceTypes.get(param1Type), sequenceTypes.get(param2Type))
                        ),
                        sequenceTypes.get(returnType)
                ),
                functionIteratorClass
        );
    }

    private static BuiltinFunction createBuiltinFunction(
            String functionName,
            String param1Type,
            String param2Type,
            String param3Type,
            String returnType,
            Class<? extends RuntimeIterator> functionIteratorClass
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
                functionIteratorClass
        );
    }

    public static FunctionItem getUserDefinedFunction(FunctionIdentifier identifier) {
        FunctionItem functionItem = userDefinedFunctions.get(identifier);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(functionItem);
            oos.flush();
            byte[] data = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (FunctionItem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new OurBadException("Error while deep copying the function body runtimeIterator");
        }
    }

    static final class FunctionNames {
        /**
         * function that returns the context position
         */
        static final BuiltinFunction position = createBuiltinFunction(
            "position",
            "integer?",
            PositionFunctionIterator.class
        );
        /**
         * function that returns the context size
         */
        static final BuiltinFunction last = createBuiltinFunction(
            "last",
            "integer?",
            LastFunctionIterator.class
        );
        /**
         * function that parses a JSON lines file
         */
        static final BuiltinFunction json_file1 = createBuiltinFunction(
            "json-file",
            "string?",
            "item*",
            JsonFileFunctionIterator.class
        );
        static final BuiltinFunction json_file2 = createBuiltinFunction(
            "json-file",
            "string?",
            "integer?",
            "item*",
            JsonFileFunctionIterator.class
        );
        /**
         * function that parses a structured JSON lines file into a DataFrame
         */
        static final BuiltinFunction structured_json_file = createBuiltinFunction(
            "structured-json-file",
            "string?",
            "item*",
            StructuredJsonFileFunctionIterator.class
        );
        /**
         * function that parses a JSON doc file
         */
        static final BuiltinFunction json_doc = createBuiltinFunction(
            "json-doc",
            "string?",
            "item*",
            JsonDocFunctionIterator.class
        );
        /**
         * function that parses a text file
         */
        static final BuiltinFunction text_file1 = createBuiltinFunction(
            "text-file",
            "string?",
            "item*",
            TextFileFunctionIterator.class
        );
        static final BuiltinFunction text_file2 = createBuiltinFunction(
            "text-file",
            "string?",
            "integer?",
            "item*",
            TextFileFunctionIterator.class
        );
        /**
         * function that parallelizes item collections into a Spark RDD
         */
        static final BuiltinFunction parallelizeFunction1 = createBuiltinFunction(
            "parallelize",
            "item*",
            "item*",
            ParallelizeFunctionIterator.class
        );
        static final BuiltinFunction parallelizeFunction2 = createBuiltinFunction(
            "parallelize",
            "item*",
            "integer",
            "item*",
            ParallelizeFunctionIterator.class
        );
        /**
         * function that parses a parquet file
         */
        static final BuiltinFunction parquet_file = createBuiltinFunction(
            "parquet-file",
            "string?",
            "item*",
            ParquetFileFunctionIterator.class
        );
        /**
         * function that returns the length of a sequence
         */
        static final BuiltinFunction count = createBuiltinFunction(
            "count",
            "item*",
            "integer",
            CountFunctionIterator.class
        );

        /**
         * function that returns the effective boolean value of the given parameter
         */
        static final BuiltinFunction boolean_function = createBuiltinFunction(
            "boolean",
            "item*",
            "boolean",
            BooleanFunctionIterator.class
        );

        /**
         * function that returns the minimum of a sequence
         */
        static final BuiltinFunction min = createBuiltinFunction(
            "min",
            "item*",
            "atomic?",
            MinFunctionIterator.class
        );
        /**
         * function that returns the maximum of a sequence
         */
        static final BuiltinFunction max = createBuiltinFunction(
            "max",
            "item*",
            "atomic?",
            MaxFunctionIterator.class
        );
        /**
         * function that returns the average of a sequence
         */
        static final BuiltinFunction avg = createBuiltinFunction(
            "avg",
            "item*",
            "atomic?",
            AvgFunctionIterator.class
        );
        /**
         * function that returns the sum of a sequence
         */
        static final BuiltinFunction sum1 = createBuiltinFunction(
            "sum",
            "item*",
            "atomic?",
            SumFunctionIterator.class
        );
        static final BuiltinFunction sum2 = createBuiltinFunction(
            "sum",
            "item*",
            "item?",
            "atomic?",
            SumFunctionIterator.class
        );


        /**
         * function that returns true if the argument is the empty sequence
         */
        static final BuiltinFunction empty = createBuiltinFunction(
            "empty",
            "item*",
            "boolean",
            EmptyFunctionIterator.class
        );
        /**
         * function that returns true if the argument is a non-empty sequence
         */
        static final BuiltinFunction exists = createBuiltinFunction(
            "exists",
            "item*",
            "boolean",
            ExistsFunctionIterator.class
        );
        /**
         * function that returns the first item in a sequence
         */
        static final BuiltinFunction head = createBuiltinFunction(
            "head",
            "item*",
            "item?",
            HeadFunctionIterator.class
        );
        /**
         * function that returns all but the first item in a sequence
         */
        static final BuiltinFunction tail = createBuiltinFunction(
            "tail",
            "item*",
            "item*",
            TailFunctionIterator.class
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
            InsertBeforeFunctionIterator.class
        );
        /**
         * function that returns a new sequence containing all the items of $target except the item at position
         * $position.
         */
        static final BuiltinFunction remove = createBuiltinFunction(
            "remove",
            "item*",
            "item*",
            "item*",
            RemoveFunctionIterator.class
        );
        /**
         * function that reverses the order of items in a sequence.
         */
        static final BuiltinFunction reverse = createBuiltinFunction(
            "reverse",
            "item*",
            "item*",
            ReverseFunctionIterator.class
        );
        /**
         * function that applies a subsequence operation to the given sequence with the given start index and length
         * parameters
         */
        static final BuiltinFunction subsequence2 = createBuiltinFunction(
            "subsequence",
            "item*",
            "item*",
            "item*",
            SubsequenceFunctionIterator.class
        );
        static final BuiltinFunction subsequence3 = createBuiltinFunction(
            "subsequence",
            "item*",
            "item*",
            "item*",
            "item*",
            SubsequenceFunctionIterator.class
        );

        /**
         * function that returns $arg if it contains zero or one items. Otherwise, raises an error.
         */
        static final BuiltinFunction zero_or_one = createBuiltinFunction(
            "zero-or-one",
            "item*",
            "item?",
            ZeroOrOneIterator.class
        );
        /**
         * function that returns $arg if it contains one or more items. Otherwise, raises an error.
         */
        static final BuiltinFunction one_or_more = createBuiltinFunction(
            "one-or-more",
            "item*",
            "item+",
            OneOrMoreIterator.class
        );
        /**
         * function that returns $arg if it contains exactly one item. Otherwise, raises an error.
         */
        static final BuiltinFunction exactly_one = createBuiltinFunction(
            "exactly-one",
            "item*",
            "item",
            ExactlyOneIterator.class
        );

        /**
         * function that returns the values that appear in a sequence, with duplicates eliminated
         */
        static final BuiltinFunction distinct_values = createBuiltinFunction(
            "distinct-values",
            "item*",
            "atomic*",
            DistinctValuesFunctionIterator.class
        );
        /**
         * function that returns indices of items that are equal to the search parameter
         */
        static final BuiltinFunction index_of = createBuiltinFunction(
            "index-of",
            "item*",
            "item",
            "integer*",
            IndexOfFunctionIterator.class
        );
        /**
         * function that returns whether two sequences are deep-equal to each other
         */
        static final BuiltinFunction deep_equal = createBuiltinFunction(
            "deep-equal",
            "item*",
            "item*",
            "boolean",
            DeepEqualFunctionIterator.class
        );


        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction integer_function = createBuiltinFunction(
            "integer",
            "item?",
            "integer?",
            IntegerFunctionIterator.class
        );
        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction double_function = createBuiltinFunction(
            "double",
            "item?",
            "double?",
            DoubleFunctionIterator.class
        );
        /**
         * function that returns the integer from the supplied argument
         */
        static final BuiltinFunction decimal_function = createBuiltinFunction(
            "decimal",
            "item?",
            "decimal?",
            DecimalFunctionIterator.class
        );
        /**
         * function that returns the absolute value of the arg
         */
        static final BuiltinFunction abs = createBuiltinFunction(
            "abs",
            "double?",
            "double?",
            AbsFunctionIterator.class
        );
        /**
         * function that rounds $arg upwards to a whole number
         */
        static final BuiltinFunction ceiling = createBuiltinFunction(
            "ceiling",
            "double?",
            "double?",
            CeilingFunctionIterator.class
        );
        /**
         * function that rounds $arg downwards to a whole number
         */
        static final BuiltinFunction floor = createBuiltinFunction(
            "floor",
            "double?",
            "double?",
            FloorFunctionIterator.class
        );
        /**
         * function that rounds a value to a specified number of decimal places, rounding upwards if two such values are
         * equally near
         */
        static final BuiltinFunction round1 = createBuiltinFunction(
            "round",
            "double?",
            "double?",
            RoundFunctionIterator.class
        );
        static final BuiltinFunction round2 = createBuiltinFunction(
            "round",
            "double?",
            "integer",
            "double?",
            RoundFunctionIterator.class
        );
        /**
         * function that rounds a value to a specified number of decimal places, rounding to make the last digit even if
         * two such values are equally near
         */
        static final BuiltinFunction round_half_to_even1 = createBuiltinFunction(
            "round-half-to-even",
            "double?",
            "double?",
            RoundHalfToEvenFunctionIterator.class
        );
        static final BuiltinFunction round_half_to_even2 = createBuiltinFunction(
            "round-half-to-even",
            "double?",
            "integer",
            "double?",
            RoundHalfToEvenFunctionIterator.class
        );

        /**
         * function that returns the approximation the mathematical constant
         */
        static final BuiltinFunction pi = createBuiltinFunction(
            "pi",
            "double?",
            PiFunctionIterator.class
        );
        /**
         * function that returns the value of e^x
         */
        static final BuiltinFunction exp = createBuiltinFunction(
            "exp",
            "double?",
            "double?",
            ExpFunctionIterator.class
        );
        /**
         * function that returns the value of 10^x
         */
        static final BuiltinFunction exp10 = createBuiltinFunction(
            "exp10",
            "double?",
            "double?",
            Exp10FunctionIterator.class
        );
        /**
         * function that returns the natural logarithm of the argument
         */
        static final BuiltinFunction log = createBuiltinFunction(
            "log",
            "double?",
            "double?",
            LogFunctionIterator.class
        );
        /**
         * function that returns the base-ten logarithm of the argument
         */
        static final BuiltinFunction log10 = createBuiltinFunction(
            "log10",
            "double?",
            "double?",
            Log10FunctionIterator.class
        );
        /**
         * function that returns the result of raising the first argument to the power of the second
         */
        static final BuiltinFunction pow = createBuiltinFunction(
            "pow",
            "double?",
            "double",
            "double?",
            PowFunctionIterator.class
        );
        /**
         * function that returns the non-negative square root of the argument
         */
        static final BuiltinFunction sqrt = createBuiltinFunction(
            "sqrt",
            "double?",
            "double?",
            SqrtFunctionIterator.class
        );
        /**
         * function that returns the sine of the angle given in radians
         */
        static final BuiltinFunction sin = createBuiltinFunction(
            "sin",
            "double?",
            "double?",
            SinFunctionIterator.class
        );
        /**
         * function that returns the cosine of the angle given in radians
         */
        static final BuiltinFunction cos = createBuiltinFunction(
            "cos",
            "double?",
            "double?",
            CosFunctionIterator.class
        );
        /**
         * function that returns the tangent of the angle given in radians
         */
        static final BuiltinFunction tan = createBuiltinFunction(
            "tan",
            "double?",
            "double?",
            TanFunctionIterator.class
        );
        /**
         * function that returns the arc sine of the angle given in radians
         */
        static final BuiltinFunction asin = createBuiltinFunction(
            "asin",
            "double?",
            "double?",
            ASinFunctionIterator.class
        );
        /**
         * function that returns the arc cosine of the angle given in radians
         */
        static final BuiltinFunction acos = createBuiltinFunction(
            "acos",
            "double?",
            "double?",
            ACosFunctionIterator.class
        );
        /**
         * function that returns the arc tangent of the angle given in radians
         */
        static final BuiltinFunction atan = createBuiltinFunction(
            "atan",
            "double?",
            "double?",
            ATanFunctionIterator.class
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
            ATan2FunctionIterator.class
        );


        /**
         * function that returns the string from the supplied argument
         */
        static final BuiltinFunction string_function = createBuiltinFunction(
            "string",
            "item?",
            "string?",
            StringFunctionIterator.class
        );
        /**
         * function that returns substrings
         */
        static final BuiltinFunction substring2 = createBuiltinFunction(
            "substring",
            "string?",
            "double",
            "string",
            SubstringFunctionIterator.class
        );
        static final BuiltinFunction substring3 = createBuiltinFunction(
            "substring",
            "string?",
            "double",
            "double",
            "string",
            SubstringFunctionIterator.class
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
            SubstringBeforeFunctionIterator.class
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
            SubstringAfterFunctionIterator.class
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
                    ConcatFunctionIterator.class
            );
        /**
         * function that returns substrings
         */
        static final BuiltinFunction string_join1 = createBuiltinFunction(
            "string-join",
            "string*",
            "string",
            StringJoinFunctionIterator.class
        );
        static final BuiltinFunction string_join2 = createBuiltinFunction(
            "string-join",
            "string*",
            "string",
            "string",
            StringJoinFunctionIterator.class
        );
        /**
         * function that returns the string length
         */
        static final BuiltinFunction string_length = createBuiltinFunction(
            "string-length",
            "string?",
            "integer",
            StringLengthFunctionIterator.class
        );
        /**
         * function that returns tokens
         */
        static final BuiltinFunction tokenize1 = createBuiltinFunction(
            "tokenize",
            "string?",
            "string*",
            TokenizeFunctionIterator.class
        );
        static final BuiltinFunction tokenize2 = createBuiltinFunction(
            "tokenize",
            "string?",
            "string",
            "string*",
            TokenizeFunctionIterator.class
        );
        /**
         * function that checks whether a string ends with a substring
         */
        static final BuiltinFunction ends_with = createBuiltinFunction(
            "ends-with",
            "string?",
            "string?",
            "boolean",
            EndsWithFunctionIterator.class
        );
        /**
         * function that checks whether a string starts with a substring
         */
        static final BuiltinFunction starts_with = createBuiltinFunction(
            "starts-with",
            "string?",
            "string?",
            "boolean",
            StartsWithFunctionIterator.class
        );
        /**
         * function that checks whether a string contains a substring
         */
        static final BuiltinFunction contains = createBuiltinFunction(
            "contains",
            "string?",
            "string?",
            "boolean",
            ContainsFunctionIterator.class
        );
        /**
         * function that checks whether a string matches a regular expression
         */
        static final BuiltinFunction matches = createBuiltinFunction(
            "matches",
            "string?",
            "string",
            "boolean",
            MatchesFunctionIterator.class
        );
        /**
         * function that normalizes spaces in a string
         */
        static final BuiltinFunction normalize_space = createBuiltinFunction(
            "normalize-space",
            "string?",
            "string",
            NormalizeSpaceFunctionIterator.class
        );

        /**
         * function that returns the duration item from the supplied string
         */
        static final BuiltinFunction duration = createBuiltinFunction(
            "duration",
            "string?",
            "duration?",
            DurationFunctionIterator.class
        );
        /**
         * function that returns the yearMonthDuration item from the supplied string
         */
        static final BuiltinFunction yearMonthDuration = createBuiltinFunction(
            "yearMonthDuration",
            "string?",
            "yearMonthDuration?",
            YearMonthDurationFunctionIterator.class
        );
        /**
         * function that returns the dayTimeDuration item from the supplied string
         */
        static final BuiltinFunction dayTimeDuration = createBuiltinFunction(
            "dayTimeDuration",
            "string?",
            "dayTimeDuration?",
            DayTimeDurationFunctionIterator.class
        );


        /**
         * function that returns the years from a duration
         */
        static final BuiltinFunction years_from_duration = createBuiltinFunction(
            "years-from-duration",
            "duration?",
            "integer?",
            YearsFromDurationFunctionIterator.class
        );
        /**
         * function that returns the months from a duration
         */
        static final BuiltinFunction months_from_duration = createBuiltinFunction(
            "months-from-duration",
            "duration?",
            "integer?",
            MonthsFromDurationFunctionIterator.class
        );
        /**
         * function that returns the days from a duration
         */
        static final BuiltinFunction days_from_duration = createBuiltinFunction(
            "days-from-duration",
            "duration?",
            "integer?",
            DaysFromDurationFunctionIterator.class
        );
        /**
         * function that returns the hours from a duration
         */
        static final BuiltinFunction hours_from_duration = createBuiltinFunction(
            "hours-from-duration",
            "duration?",
            "integer?",
            HoursFromDurationFunctionIterator.class
        );
        /**
         * function that returns the minutes from a duration
         */
        static final BuiltinFunction minutes_from_duration = createBuiltinFunction(
            "minutes-from-duration",
            "duration?",
            "integer?",
            MinutesFromDurationFunctionIterator.class
        );
        /**
         * function that returns the seconds from a duration
         */
        static final BuiltinFunction seconds_from_duration = createBuiltinFunction(
            "seconds-from-duration",
            "duration?",
            "decimal?",
            SecondsFromDurationFunctionIterator.class
        );


        /**
         * function that returns the dateTime item from the supplied string
         */
        static final BuiltinFunction dateTime = createBuiltinFunction(
            "dateTime",
            "string?",
            "dateTime?",
            DateTimeFunctionIterator.class
        );

        /**
         * function that returns the year from a dateTime
         */
        static final BuiltinFunction year_from_dateTime = createBuiltinFunction(
            "year-from-dateTime",
            "dateTime?",
            "integer?",
            YearFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the month from a dateTime
         */
        static final BuiltinFunction month_from_dateTime = createBuiltinFunction(
            "month-from-dateTime",
            "dateTime?",
            "integer?",
            MonthFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the day from a dateTime
         */
        static final BuiltinFunction day_from_dateTime = createBuiltinFunction(
            "day-from-dateTime",
            "dateTime?",
            "integer?",
            DayFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the hours from a dateTime
         */
        static final BuiltinFunction hours_from_dateTime = createBuiltinFunction(
            "hours-from-dateTime",
            "dateTime?",
            "integer?",
            HoursFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the minutes from a dateTime
         */
        static final BuiltinFunction minutes_from_dateTime = createBuiltinFunction(
            "minutes-from-dateTime",
            "dateTime?",
            "integer?",
            MinutesFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the seconds from a dateTime
         */
        static final BuiltinFunction seconds_from_dateTime = createBuiltinFunction(
            "seconds-from-dateTime",
            "dateTime?",
            "decimal?",
            SecondsFromDateTimeFunctionIterator.class
        );
        /**
         * function that returns the seconds from a dateTime
         */
        static final BuiltinFunction timezone_from_dateTime = createBuiltinFunction(
            "timezone-from-dateTime",
            "dateTime?",
            "dayTimeDuration?",
            TimezoneFromDateTimeFunctionIterator.class
        );


        /**
         * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_dateTime_to_timezone1 = createBuiltinFunction(
            "adjust-dateTime-to-timezone",
            "dateTime?",
            "dateTime?",
            AdjustDateTimeToTimezone.class
        );
        static final BuiltinFunction adjust_dateTime_to_timezone2 = createBuiltinFunction(
            "adjust-dateTime-to-timezone",
            "dateTime?",
            "dayTimeDuration?",
            "dateTime?",
            AdjustDateTimeToTimezone.class
        );



        /**
         * function that returns the date item from the supplied string
         */
        static final BuiltinFunction date = createBuiltinFunction(
            "date",
            "string?",
            "date?",
            DateFunctionIterator.class
        );
        /**
         * function that returns the year from a date
         */
        static final BuiltinFunction year_from_date = createBuiltinFunction(
            "year-from-date",
            "date?",
            "integer?",
            YearFromDateFunctionIterator.class
        );
        /**
         * function that returns the month from a date
         */
        static final BuiltinFunction month_from_date = createBuiltinFunction(
            "month-from-date",
            "date?",
            "integer?",
            MonthFromDateFunctionIterator.class
        );
        /**
         * function that returns the day from a date
         */
        static final BuiltinFunction day_from_date = createBuiltinFunction(
            "day-from-date",
            "date?",
            "integer?",
            DayFromDateFunctionIterator.class
        );
        /**
         * function that returns the seconds from a date
         */
        static final BuiltinFunction timezone_from_date = createBuiltinFunction(
            "timezone-from-date",
            "date?",
            "dayTimeDuration?",
            TimezoneFromDateFunctionIterator.class
        );


        /**
         * function that adjusts a date value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_date_to_timezone1 = createBuiltinFunction(
            "adjust-date-to-timezone",
            "date?",
            "date?",
            AdjustDateToTimezone.class
        );
        static final BuiltinFunction adjust_date_to_timezone2 = createBuiltinFunction(
            "adjust-date-to-timezone",
            "date?",
            "dayTimeDuration?",
            "date?",
            AdjustDateToTimezone.class
        );

        /**
         * function that returns the time item from the supplied string
         */
        static final BuiltinFunction time = createBuiltinFunction(
            "time",
            "string?",
            "time?",
            TimeFunctionIterator.class
        );
        /**
         * function that returns the hours from a time
         */
        static final BuiltinFunction hours_from_time = createBuiltinFunction(
            "hours-from-time",
            "time?",
            "integer?",
            HoursFromTimeFunctionIterator.class
        );
        /**
         * function that returns the minutes from a time
         */
        static final BuiltinFunction minutes_from_time = createBuiltinFunction(
            "minutes-from-time",
            "time?",
            "integer?",
            MinutesFromTimeFunctionIterator.class
        );
        /**
         * function that returns the seconds from a time
         */
        static final BuiltinFunction seconds_from_time = createBuiltinFunction(
            "seconds-from-time",
            "time?",
            "decimal?",
            SecondsFromTimeFunctionIterator.class
        );
        /**
         * function that returns the seconds from a time
         */
        static final BuiltinFunction timezone_from_time = createBuiltinFunction(
            "timezone-from-time",
            "time?",
            "dayTimeDuration?",
            TimezoneFromTimeFunctionIterator.class
        );
        /**
         * function that adjusts a time value to a specific timezone, or to no timezone at all.
         */
        static final BuiltinFunction adjust_time_to_timezone1 = createBuiltinFunction(
            "adjust-time-to-timezone",
            "time?",
            "time?",
            AdjustTimeToTimezone.class
        );
        static final BuiltinFunction adjust_time_to_timezone2 = createBuiltinFunction(
            "adjust-time-to-timezone",
            "time?",
            "dayTimeDuration?",
            "time?",
            AdjustTimeToTimezone.class
        );

        /**
         * function that returns the hexBinary item from the supplied string
         */
        static final BuiltinFunction hexBinary = createBuiltinFunction(
            "hexBinary",
            "string?",
            "hexBinary?",
            HexBinaryFunctionIterator.class
        );
        /**
         * function that returns the base64Binary item from the supplied string
         */
        static final BuiltinFunction base64Binary = createBuiltinFunction(
            "base64Binary",
            "string?",
            "base64Binary?",
            Base64BinaryFunctionIterator.class
        );

        /**
         * function that returns the keys of a Json Object
         */
        static final BuiltinFunction keys = createBuiltinFunction(
            "keys",
            "item*",
            "item*",
            ObjectKeysFunctionIterator.class
        );
        /**
         * function that returns returns all members of all arrays of the supplied sequence
         */
        static final BuiltinFunction members = createBuiltinFunction(
            "members",
            "item*",
            "item*",
            ArrayMembersFunctionIterator.class
        );
        /**
         * function that returns the JSON null
         */
        static final BuiltinFunction null_function = createBuiltinFunction(
            "null",
            "null?",
            NullFunctionIterator.class
        );
        /**
         * function that returns the length of an array
         */
        static final BuiltinFunction size = createBuiltinFunction(
            "size",
            "array?",
            "integer?",
            ArraySizeFunctionIterator.class
        );
        /**
         * function that dynamically creates an object that merges the values of key collisions into arrays
         */
        static final BuiltinFunction accumulate = createBuiltinFunction(
            "accumulate",
            "item*",
            "object",
            ObjectAccumulateFunctionIterator.class
        );
        /**
         * function that returns all arrays contained within the supplied items, regardless of depth.
         */
        static final BuiltinFunction descendant_arrays = createBuiltinFunction(
            "descendant-arrays",
            "item*",
            "item*",
            ArrayDescendantFunctionIterator.class
        );
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        static final BuiltinFunction descendant_objects = createBuiltinFunction(
            "descendant-objects",
            "item*",
            "item*",
            ObjectDescendantFunctionIterator.class
        );
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        static final BuiltinFunction descendant_pairs = createBuiltinFunction(
            "descendant-pairs",
            "item*",
            "item*",
            ObjectDescendantPairsFunctionIterator.class
        );
        /**
         * function that recursively flattens arrays in the input sequence, leaving non-arrays intact
         */
        static final BuiltinFunction flatten = createBuiltinFunction(
            "flatten",
            "item*",
            "item*",
            ArrayFlattenFunctionIterator.class
        );
        /**
         * function that returns the intersection of the supplied objects, and aggregates values corresponding to the
         * same name into an array
         */
        static final BuiltinFunction intersect = createBuiltinFunction(
            "intersect",
            "item*",
            "object+",
            ObjectIntersectFunctionIterator.class
        );
        /**
         * function that projects objects by filtering their pairs and leaves non-objects intact
         */
        static final BuiltinFunction project = createBuiltinFunction(
            "project",
            "item*",
            "string*",
            "item*",
            ObjectProjectFunctionIterator.class
        );
        /**
         * function that removes the pairs with the given keys from all objects and leaves non-objects intact
         */
        static final BuiltinFunction remove_keys = createBuiltinFunction(
            "remove-keys",
            "item*",
            "string*",
            "item*",
            ObjectRemoveKeysFunctionIterator.class
        );
        /**
         * function that returns the values of a Json Object
         */
        static final BuiltinFunction values = createBuiltinFunction(
            "values",
            "item*",
            "item*",
            ObjectValuesFunctionIterator.class
        );

        /**
         * function fetches the transformer class from SparkML API
         */
        static final BuiltinFunction get_transformer = createBuiltinFunction(
            "get-transformer",
            "string",
            "item",
            GetTransformerFunctionIterator.class
        );

    }

}
