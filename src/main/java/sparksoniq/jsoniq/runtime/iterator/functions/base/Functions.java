/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionCall;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.NullFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayDescendantFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayFlattenFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArrayMembersFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arrays.ArraySizeFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.*;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate.*;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.*;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential.*;
import sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric.*;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.ExactlyOneIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.OneOrMoreIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality.ZeroOrOneIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.ConcatFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.StringJoinFunction;
import sparksoniq.jsoniq.runtime.iterator.functions.strings.SubstringFunctionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.iterator.function.ParallelizeFunctionIterator;
import sparksoniq.spark.iterator.function.ParseJsonFunctionIterator;

import java.util.HashMap;
import java.util.List;

import static sparksoniq.jsoniq.runtime.iterator.functions.base.Functions.FunctionNames.*;

public class Functions {
    private static HashMap<SparksoniqFunctionSignature, Class<? extends RuntimeIterator>> buildInFunctions;

    static {
        buildInFunctions = new HashMap<>();
        buildInFunctions.put(new SparksoniqFunctionSignature(JSON_FILE, 1), ParseJsonFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(JSON_FILE, 2), ParseJsonFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(PARALLELIZE, 1), ParallelizeFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(COUNT, 1), CountFunctionIterator.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(MIN, 1), MinFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(MAX, 1), MaxFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(SUM, 1), SumFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(AVG, 1), AvgFunctionIterator.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(ZEROORONE, 1), ZeroOrOneIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ONEORMORE, 1), OneOrMoreIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(EXACTLYONE, 1), ExactlyOneIterator.class);


        buildInFunctions.put(new SparksoniqFunctionSignature(ABS, 1), AbsFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(CEILING, 1), CeilingFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(FLOOR, 1), FloorFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ROUND, 1), RoundFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ROUND, 2), RoundFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ROUNDHALFTOEVEN, 1), RoundHalfToEvenFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ROUNDHALFTOEVEN, 2), RoundHalfToEvenFunctionIterator.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(PI, 0), PiFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(EXP, 1), ExpFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(EXP10, 1), Exp10FunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(LOG, 1), LogFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(LOG10, 1), Log10FunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(POW, 2), PowFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(SQRT, 1), SqrtFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(SIN, 1), SinFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(COS, 1), CosFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(TAN, 1), TanFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ASIN, 1), ASinFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ACOS, 1), ACosFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ATAN, 1), ATanFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ATAN2, 2), ATan2FunctionIterator.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(SUBSTRING, 2), SubstringFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(SUBSTRING, 3), SubstringFunctionIterator.class);
        for(int i = 0; i <= 100; i++)
            buildInFunctions.put(new SparksoniqFunctionSignature(CONCAT, i), ConcatFunctionIterator.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(STRINGJOIN, 1), StringJoinFunction.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(STRINGJOIN, 2), StringJoinFunction.class);

        buildInFunctions.put(new SparksoniqFunctionSignature(KEYS, 1), ObjectKeysFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(MEMBERS, 1), ArrayMembersFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(NULL, 0), NullFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(SIZE, 1), ArraySizeFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(ACCUMULATE, 1), ObjectAccumulateFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(DESCENDANTARRAYS, 1), ArrayDescendantFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(DESCENDANTOBJECTS, 1), ObjectDescendantFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(DESCENDANTPAIRS, 1), ObjectDescendantPairsFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(FLATTEN, 1), ArrayFlattenFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(INTERSECT, 1), ObjectIntersectFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(PROJECT, 2), ObjectProjectFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(REMOVEKEYS, 2), ObjectRemoveKeysFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(VALUES, 1), ObjectValuesFunctionIterator.class);

    }

    public static Class<? extends RuntimeIterator> getFunctionIteratorClass(FunctionCall expression, List<RuntimeIterator> arguments) {
        SparksoniqFunctionSignature functionSignature = new SparksoniqFunctionSignature(expression.getFunctionName(), arguments.size());
        if(buildInFunctions.containsKey(functionSignature))
            return buildInFunctions.get(functionSignature);
        throw new UnknownFunctionCallException(new IteratorMetadata(expression.getMetadata()));
    }

    public static class FunctionNames {

        /**
         * function that parses a text file
         */
        public static final String JSON_FILE = "json-file";
        /**
         * function that parallelizes item collections into a Spark RDD
         */
        public static final String PARALLELIZE = "parallelize";
        /**
         * function that returns the length of a sequence
         */
        public static final String COUNT = "count";

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
         * function that returns substrings
         */
        public static final String CONCAT = "concat";
        /**
         * function that returns substrings
         */
        public static final String STRINGJOIN = "string-join";


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
