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
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.*;
import sparksoniq.jsoniq.runtime.iterator.functions.arithmetic.AvgFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arithmetic.MaxFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arithmetic.MinFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.arithmetic.SumFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectKeysFunctionIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectValuesFunctionIterator;
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

        buildInFunctions.put(new SparksoniqFunctionSignature(SIZE, 1), ArraySizeFunctionIterator.class);
        buildInFunctions.put(new SparksoniqFunctionSignature(KEYS, 1), ObjectKeysFunctionIterator.class);
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
         * function that returns the keys of a Json Object
         */
        public static final String KEYS = "keys";
        /**
         * function that returns the values of a Json Object
         */
        public static final String VALUES = "values";
        /**
         * function that returns the length of an array
         */
        public static final String SIZE = "size";
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
    }

}
