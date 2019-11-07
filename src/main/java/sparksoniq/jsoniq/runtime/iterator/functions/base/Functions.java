/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.NullFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayDescendantFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayFlattenFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayMembersFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArraySizeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.binaries.Base64BinaryFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.binaries.HexBinaryFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.booleans.BooleanFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.DateFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.DateTimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.TimeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.datetime.components.*;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.DayTimeDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.DurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.YearMonthDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.DaysFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.HoursFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.MinutesFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.MonthsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.SecondsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.durations.components.YearsFromDurationFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.AbsFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.CeilingFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.FloorFunctionIterator;
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
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringJoinFunction;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringLengthFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringAfterFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringBeforeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.TokenizeFunctionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.iterator.function.ParallelizeFunctionIterator;
import sparksoniq.spark.iterator.function.ParseJsonFunctionIterator;
import sparksoniq.spark.iterator.function.ParseTextFunctionIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.*;


public class Functions {
    private static HashMap<FunctionIdentifier, Class<? extends RuntimeIterator>> builtInFunctions;
    private static HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    static {
        userDefinedFunctions = new HashMap<>();
        builtInFunctions = new HashMap<>();

        builtInFunctions.put(new FunctionIdentifier(JSON_FILE, 1), ParseJsonFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(JSON_FILE, 2), ParseJsonFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TEXT_FILE, 1), ParseTextFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TEXT_FILE, 2), ParseTextFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(PARALLELIZE, 1), ParallelizeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(PARALLELIZE, 2), ParallelizeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(COUNT, 1), CountFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(BOOLEAN, 1), BooleanFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(MIN, 1), MinFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MAX, 1), MaxFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUM, 1), SumFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUM, 2), SumFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(AVG, 1), AvgFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(EMPTY, 1), EmptyFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(EXISTS, 1), ExistsFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(HEAD, 1), HeadFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TAIL, 1), TailFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(INSERTBEFORE, 3), InsertBeforeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(REMOVE, 2), RemoveFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(REVERSE, 1), ReverseFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUBSEQUENCE, 2), SubsequenceFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUBSEQUENCE, 3), SubsequenceFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(ZEROORONE, 1), ZeroOrOneIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ONEORMORE, 1), OneOrMoreIterator.class);
        builtInFunctions.put(new FunctionIdentifier(EXACTLYONE, 1), ExactlyOneIterator.class);

        builtInFunctions.put(new FunctionIdentifier(DISTINCTVALUES, 1), DistinctValuesFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(INDEXOF, 2), IndexOfFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DEEPEQUAL, 2), DeepEqualFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(ABS, 1), AbsFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(CEILING, 1), CeilingFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(FLOOR, 1), FloorFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ROUND, 1), RoundFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ROUND, 2), RoundFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ROUNDHALFTOEVEN, 1), RoundHalfToEvenFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ROUNDHALFTOEVEN, 2), RoundHalfToEvenFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(PI, 0), PiFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(EXP, 1), ExpFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(EXP10, 1), Exp10FunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(LOG, 1), LogFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(LOG10, 1), Log10FunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(POW, 2), PowFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SQRT, 1), SqrtFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SIN, 1), SinFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(COS, 1), CosFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TAN, 1), TanFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ASIN, 1), ASinFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ACOS, 1), ACosFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ATAN, 1), ATanFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ATAN2, 2), ATan2FunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(SUBSTRING, 2), SubstringFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUBSTRING, 3), SubstringFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUBSTRING_BEFORE, 2), SubstringBeforeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SUBSTRING_AFTER, 2), SubstringAfterFunctionIterator.class);
        for (int i = 0; i <= 100; i++)
            builtInFunctions.put(new FunctionIdentifier(CONCAT, i), ConcatFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(ENDSWITH, 2), EndsWithFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(STRINGJOIN, 1), StringJoinFunction.class);
        builtInFunctions.put(new FunctionIdentifier(STRINGJOIN, 2), StringJoinFunction.class);
        builtInFunctions.put(new FunctionIdentifier(STRINGLENGTH, 1), StringLengthFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TOKENIZE, 1), TokenizeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TOKENIZE, 2), TokenizeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(STARTSWITH, 2), StartsWithFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MATCHES, 2), MatchesFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(CONTAINS, 2), ContainsFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(NORMALIZESPACE, 1), NormalizeSpaceFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(DURATION, 1), DurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(YEARMONTHDURATION, 1), YearMonthDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DAYTIMEDURATION, 1), DayTimeDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(YEARSFROMDURATION, 1), YearsFromDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MONTHSFROMDURATION, 1), MonthsFromDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DAYSFROMDURATION, 1), DaysFromDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(HOURSFROMDURATION, 1), HoursFromDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MINUTESFROMDURATION, 1), MinutesFromDurationFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SECONDSFROMDURATION, 1), SecondsFromDurationFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(DATETIME, 1), DateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(YEARFROMDATETIME, 1), YearFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MONTHFROMDATETIME, 1), MonthFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DAYFROMDATETIME, 1), DayFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(HOURSFROMDATETIME, 1), HoursFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MINUTESFROMDATETIME, 1), MinutesFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SECONDSFROMDATETIME, 1), SecondsFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TIMEZONEFROMDATETIME, 1), TimezoneFromDateTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTDATETIMETOTIMEZONE, 1), AdjustDateTimeToTimezone.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTDATETIMETOTIMEZONE, 2), AdjustDateTimeToTimezone.class);

        builtInFunctions.put(new FunctionIdentifier(DATE, 1), DateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(YEARFROMDATE, 1), YearFromDateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MONTHFROMDATE, 1), MonthFromDateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DAYFROMDATE, 1), DayFromDateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TIMEZONEFROMDATE, 1), TimezoneFromDateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTDATETOTIMEZONE, 1), AdjustDateToTimezone.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTDATETOTIMEZONE, 2), AdjustDateToTimezone.class);

        builtInFunctions.put(new FunctionIdentifier(TIME, 1), TimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(HOURSFROMTIME, 1), HoursFromTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MINUTESFROMTIME, 1), MinutesFromTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SECONDSFROMTIME, 1), SecondsFromTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(TIMEZONEFROMTIME, 1), TimezoneFromTimeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTTIMETOTIMEZONE, 1), AdjustTimeToTimezone.class);
        builtInFunctions.put(new FunctionIdentifier(ADJUSTTIMETOTIMEZONE, 2), AdjustTimeToTimezone.class);

        builtInFunctions.put(new FunctionIdentifier(HEXBINARY, 1), HexBinaryFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(BASE64BINARY, 1), Base64BinaryFunctionIterator.class);

        builtInFunctions.put(new FunctionIdentifier(KEYS, 1), ObjectKeysFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(MEMBERS, 1), ArrayMembersFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(NULL, 0), NullFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(SIZE, 1), ArraySizeFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(ACCUMULATE, 1), ObjectAccumulateFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DESCENDANTARRAYS, 1), ArrayDescendantFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DESCENDANTOBJECTS, 1), ObjectDescendantFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(DESCENDANTPAIRS, 1), ObjectDescendantPairsFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(FLATTEN, 1), ArrayFlattenFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(INTERSECT, 1), ObjectIntersectFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(PROJECT, 2), ObjectProjectFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(REMOVEKEYS, 2), ObjectRemoveKeysFunctionIterator.class);
        builtInFunctions.put(new FunctionIdentifier(VALUES, 1), ObjectValuesFunctionIterator.class);
    }

    public static void clearUserDefinedFunctions() {
        userDefinedFunctions.clear();
    }

    public static void addUserDefinedFunction(FunctionItem function, ExpressionMetadata meta) {
        if (builtInFunctions.containsKey(function.getIdentifier())
                || userDefinedFunctions.containsKey(function.getIdentifier())) {
            throw new DuplicateFunctionIdentifierException(function.getIdentifier(), meta);
        }
        userDefinedFunctions.put(function.getIdentifier(), function);
    }

    public static Class<? extends RuntimeIterator> getBuiltInFunction(FunctionIdentifier identifier, IteratorMetadata metadata) {
        if (builtInFunctions.containsKey(identifier))
            return builtInFunctions.get(identifier);
        throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
    }

    public static FunctionItem getUserDefinedFunction(FunctionIdentifier identifier, IteratorMetadata metadata) {
        if (userDefinedFunctions.containsKey(identifier)) {
            FunctionItem fnItem = userDefinedFunctions.get(identifier);
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(fnItem);
                oos.flush();
                byte[] data = bos.toByteArray();
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return (FunctionItem) ois.readObject();
            } catch (Exception e) {
                throw new SparksoniqRuntimeException("Error while deep copying the function body runtimeIterator");
            }
        }
        throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
    }

    public static class FunctionNames {

        /**
         * function that parses a JSON lines file
         */
        public static final String JSON_FILE = "json-file";
        /**
         * function that parses a text file
         */
        public static final String TEXT_FILE = "text-file";
        /**
         * function that parallelizes item collections into a Spark RDD
         */
        public static final String PARALLELIZE = "parallelize";
        /**
         * function that returns the length of a sequence
         */
        public static final String COUNT = "count";


        /**
         * function that returns the effective boolean value of the given parameter
         */
        public static final String BOOLEAN = "boolean";


        /**
         * function that returns the minimum of a sequence
         */
        public static final String MIN = "min";
        /**
         * function that returns the maximum of a sequence
         */
        public static final String MAX = "max";
        /**
         * function that returns the average of a sequence
         */
        public static final String AVG = "avg";
        /**
         * function that returns the sum of a sequence
         */
        public static final String SUM = "sum";


        /**
         * function that returns true if the argument is the empty sequence
         */
        public static final String EMPTY = "empty";
        /**
         * function that returns true if the argument is a non-empty sequence
         */
        public static final String EXISTS = "exists";
        /**
         * function that returns the first item in a sequence
         */
        public static final String HEAD = "head";
        /**
         * function that returns all but the first item in a sequence
         */
        public static final String TAIL = "tail";
        /**
         * function that returns a sequence constructed by inserting an item or a sequence of items at a given position within an existing sequence
         */
        public static final String INSERTBEFORE = "insert-before";
        /**
         * function that returns a new sequence containing all the items of $target except the item at position $position.
         */
        public static final String REMOVE = "remove";
        /**
         * function that reverses the order of items in a sequence.
         */
        public static final String REVERSE = "reverse";
        /**
         * function that applies a subsequence operation to the given sequence with the given start index and length parameters
         */
        public static final String SUBSEQUENCE = "subsequence";


        /**
         * function that returns $arg if it contains zero or one items. Otherwise, raises an error.
         */
        public static final String ZEROORONE = "zero-or-one";
        /**
         * function that returns $arg if it contains one or more items. Otherwise, raises an error.
         */
        public static final String ONEORMORE = "one-or-more";
        /**
         * function that returns $arg if it contains exactly one item. Otherwise, raises an error.
         */
        public static final String EXACTLYONE = "exactly-one";


        /**
         * function that returns the values that appear in a sequence, with duplicates eliminated
         */
        public static final String DISTINCTVALUES = "distinct-values";
        /**
         * function that returns indices of items that are equal to the search parameter
         */
        public static final String INDEXOF = "index-of";
        /**
         * function that returns whether two sequences are deep-equal to each other
         */
        public static final String DEEPEQUAL = "deep-equal";

        /**
         * function that returns the absolute value of the arg
         */
        public static final String ABS = "abs";
        /**
         * function that rounds $arg upwards to a whole number
         */
        public static final String CEILING = "ceiling";
        /**
         * function that rounds $arg downwards to a whole number
         */
        public static final String FLOOR = "floor";
        /**
         * function that rounds a value to a specified number of decimal places, rounding upwards if two such values are equally near
         */
        public static final String ROUND = "round";
        /**
         * function that rounds a value to a specified number of decimal places, rounding to make the last digit even if two such values are equally near
         */
        public static final String ROUNDHALFTOEVEN = "round-half-to-even";


        /**
         * function that returns the approximation the mathematical constant
         */
        public static final String PI = "pi";
        /**
         * function that returns the value of e^x
         */
        public static final String EXP = "exp";
        /**
         * function that returns the value of 10^x
         */
        public static final String EXP10 = "exp10";
        /**
         * function that returns the natural logarithm of the argument
         */
        public static final String LOG = "log";
        /**
         * function that returns the  base-ten logarithm of the argument
         */
        public static final String LOG10 = "log10";
        /**
         * function that returns the result of raising the first argument to the power of the second
         */
        public static final String POW = "pow";
        /**
         * function that returns the non-negative square root of the argument
         */
        public static final String SQRT = "sqrt";
        /**
         * function that returns the sine of the angle given in radians
         */
        public static final String SIN = "sin";
        /**
         * function that returns the cosine of the angle given in radians
         */
        public static final String COS = "cos";
        /**
         * function that returns the tangent of the angle given in radians
         */
        public static final String TAN = "tan";
        /**
         * function that returns the arc sine of the angle given in radians
         */
        public static final String ASIN = "asin";
        /**
         * function that returns the arc cosine of the angle given in radians
         */
        public static final String ACOS = "acos";
        /**
         * function that returns the arc tangent of the angle given in radians
         */
        public static final String ATAN = "atan";
        /**
         * function that returns the the angle in radians subtended at the origin by the point on a plane with coordinates (x, y) and the positive x-axis.
         */
        public static final String ATAN2 = "atan2";


        /**
         * function that returns substrings
         */
        public static final String SUBSTRING = "substring";
        /**
         * function that returns the part of the first variable that precedes the first occurrence of the second vairable.
         */
        public static final String SUBSTRING_BEFORE = "substring-before";
        /**
         * function that returns the part of the first variable that follows the first occurrence of the second vairable.
         */
        public static final String SUBSTRING_AFTER = "substring-after";
        /**
         * function that returns substrings
         */
        public static final String CONCAT = "concat";
        /**
         * function that returns substrings
         */
        public static final String STRINGJOIN = "string-join";
        /**
         * function that returns the string length
         */
        public static final String STRINGLENGTH = "string-length";
        /**
         * function that returns tokens
         */
        public static final String TOKENIZE = "tokenize";
        /**
         * function that checks whether a string ends with a substring
         */
        public static final String ENDSWITH = "ends-with";
        /**
         * function that checks whether a string starts with a substring
         */
        public static final String STARTSWITH = "starts-with";
        /**
         * function that checks whether a string contains a substring
         */
        public static final String CONTAINS = "contains";
        /**
         * function that checks whether a string matches a regular expression
         */
        public static final String MATCHES = "matches";
        /**
         * function that normalizes spaces in a string
         */
        public static final String NORMALIZESPACE = "normalize-space";


        /**
         * function that returns the duration item from the supplied string
         */
        public static final String DURATION = "duration";
        /**
         * function that returns the yearMonthDuration item from the supplied string
         */
        public static final String YEARMONTHDURATION = "yearMonthDuration";
        /**
         * function that returns the dayTimeDuration item from the supplied string
         */
        public static final String DAYTIMEDURATION = "dayTimeDuration";


        /**
         * function that returns the years from a duration
         */
        public static final String YEARSFROMDURATION = "years-from-duration";
        /**
         * function that returns the months from a duration
         */
        public static final String MONTHSFROMDURATION = "months-from-duration";
        /**
         * function that returns the days from a duration
         */
        public static final String DAYSFROMDURATION = "days-from-duration";
        /**
         * function that returns the hours from a duration
         */
        public static final String HOURSFROMDURATION = "hours-from-duration";
        /**
         * function that returns the minutes from a duration
         */
        public static final String MINUTESFROMDURATION = "minutes-from-duration";
        /**
         * function that returns the seconds from a duration
         */
        public static final String SECONDSFROMDURATION = "seconds-from-duration";


        /**
         * function that returns the dateTime item from the supplied string
         */
        public static final String DATETIME = "dateTime";
        /**
         * function that returns the year from a dateTime
         */
        public static final String YEARFROMDATETIME = "year-from-dateTime";
        /**
         * function that returns the month from a dateTime
         */
        public static final String MONTHFROMDATETIME = "month-from-dateTime";
        /**
         * function that returns the day from a dateTime
         */
        public static final String DAYFROMDATETIME = "day-from-dateTime";
        /**
         * function that returns the hours from a dateTime
         */
        public static final String HOURSFROMDATETIME = "hours-from-dateTime";
        /**
         * function that returns the minutes from a dateTime
         */
        public static final String MINUTESFROMDATETIME = "minutes-from-dateTime";
        /**
         * function that returns the seconds from a dateTime
         */
        public static final String SECONDSFROMDATETIME = "seconds-from-dateTime";
        /**
         * function that returns the seconds from a dateTime
         */
        public static final String TIMEZONEFROMDATETIME = "timezone-from-dateTime";
        /**
         * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
         */
        public static final String ADJUSTDATETIMETOTIMEZONE = "adjust-dateTime-to-timezone";


        /**
         * function that returns the date item from the supplied string
         */
        public static final String DATE = "date";
        /**
         * function that returns the year from a date
         */
        public static final String YEARFROMDATE = "year-from-date";
        /**
         * function that returns the month from a date
         */
        public static final String MONTHFROMDATE = "month-from-date";
        /**
         * function that returns the day from a date
         */
        public static final String DAYFROMDATE = "day-from-date";
        /**
         * function that returns the seconds from a date
         */
        public static final String TIMEZONEFROMDATE = "timezone-from-date";
        /**
         * function that adjusts a dateTime value to a specific timezone, or to no timezone at all.
         */
        public static final String ADJUSTDATETOTIMEZONE = "adjust-date-to-timezone";


        /**
         * function that returns the time item from the supplied string
         */
        public static final String TIME = "time";
        /**
         * function that returns the hours from a time
         */
        public static final String HOURSFROMTIME = "hours-from-time";
        /**
         * function that returns the minutes from a time
         */
        public static final String MINUTESFROMTIME = "minutes-from-time";
        /**
         * function that returns the seconds from a time
         */
        public static final String SECONDSFROMTIME = "seconds-from-time";
        /**
         * function that returns the seconds from a time
         */
        public static final String TIMEZONEFROMTIME = "timezone-from-time";
        /**
         * function that adjusts a time value to a specific timezone, or to no timezone at all.
         */
        public static final String ADJUSTTIMETOTIMEZONE = "adjust-time-to-timezone";


        /**
         * function that returns the hexBinary item from the supplied string
         */
        public static final String HEXBINARY = "hexBinary";
        /**
         * function that returns the base64Binary item from the supplied string
         */
        public static final String BASE64BINARY = "base64Binary";

        /**
         * function that returns the keys of a Json Object
         */
        public static final String KEYS = "keys";
        /**
         * function that returns returns all members of all arrays of the supplied sequence
         */
        public static final String MEMBERS = "members";
        /**
         * function that returns the JSON null
         */
        public static final String NULL = "null";
        /**
         * function that returns the length of an array
         */
        public static final String SIZE = "size";
        /**
         * function that dynamically creates an object that merges the values of key collisions into arrays
         */
        public static final String ACCUMULATE = "accumulate";
        /**
         * function that returns all arrays contained within the supplied items, regardless of depth.
         */
        public static final String DESCENDANTARRAYS = "descendant-arrays";
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        public static final String DESCENDANTOBJECTS = "descendant-objects";
        /**
         * function that returns all objects contained within the supplied items, regardless of depth
         */
        public static final String DESCENDANTPAIRS = "descendant-pairs";
        /**
         * function that recursively flattens arrays in the input sequence, leaving non-arrays intact
         */
        public static final String FLATTEN = "flatten";
        /**
         * function that returns the intersection of the supplied objects, and aggregates values corresponding to the same name into an array
         */
        public static final String INTERSECT = "intersect";
        /**
         * function that projects objects by filtering their pairs and leaves non-objects intact
         */
        public static final String PROJECT = "project";
        /**
         * function that removes the pairs with the given keys from all objects and leaves non-objects intact
         */
        public static final String REMOVEKEYS = "remove-keys";
        /**
         * function that returns the values of a Json Object
         */
        public static final String VALUES = "values";

    }

}
