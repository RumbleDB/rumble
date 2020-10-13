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
 * Authors: Can Berker Cikis, Ghislain Fourny
 *
 */

package org.rumbledb.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.jsoniter.JsonIterator;

public class JsonIterUtils {

    /**
     * JsonIter can process both strings and inputstreams to parse input.
     * Using JsonIter with InputStreams trigger dynamic code generation in JsonIter.
     * This dynamic code generation is called via enableStreamingSupport() method.
     * If this call is made first, then all consequent inputstream or string input usages are successful.
     *
     * However, if a string input is parsed first and dynamic code generation of streaming inputs is triggered later.
     * JsonIter reaches an invalid state where no streaming input can be processed correctly.
     * This function triggers a streaming input and hence dynamic code generation.
     * This workaround should be applied during startup for reliable use of JsonIter
     */
    public static void applyJsonIterFaultyInitializationWorkAround() {
        InputStream is = new ByteArrayInputStream("{}".getBytes());
        JsonIterator object = JsonIterator.parse(is, 1024);
    }
}
