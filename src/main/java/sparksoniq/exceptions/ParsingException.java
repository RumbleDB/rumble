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

package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

public class ParsingException extends SparksoniqRuntimeException {

    public ParsingException(String message, ExpressionMetadata metadata) {
        super(String.format("Parser failed. %s", message), ErrorCodes.ParsingErrorCode, metadata);
    }

    public ParsingException(String message, String code, ExpressionMetadata metadata) {
        super(String.format("Parser failed. %s", message), code, metadata);
    }

}
