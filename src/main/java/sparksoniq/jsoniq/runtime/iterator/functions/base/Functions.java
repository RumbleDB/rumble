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

public class Functions {
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
     * function that returns the maximum of a sequence
     */
    public static final String MIN = "min";
    /**
     * function that returns the minimum of a sequence
     */
    public static final String MAX = "max";
    /**
     * function that returns the minimum of a sequence
     */
    public static final String AVG = "avg";
    /**
     * function that returns the sum of a sequence
     */
    public static final String SUM = "sum";


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
