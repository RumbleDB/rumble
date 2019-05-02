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

import java.util.Arrays;

public class SparksoniqRuntimeException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;
    private ExpressionMetadata metadata;

    public SparksoniqRuntimeException(String message) {
        super("Error [err: " + ErrorCodes.RuntimeExceptionErrorCode + " ] " + message);
        this.errorCode = ErrorCodes.RuntimeExceptionErrorCode;
        this.errorMessage = message;
    }

    public SparksoniqRuntimeException(String message, String errorCode) {
        super("Error [err: " + errorCode + " ] " + message);
        if (!Arrays.asList(ErrorCodes.class.getFields()).stream().anyMatch(f -> {
            try {
                return f.get(null).equals(errorCode);
            } catch (IllegalAccessException e) {
                return true;
            }
        }))
            this.errorCode = ErrorCodes.RuntimeExceptionErrorCode;
        else
            this.errorCode = errorCode;
        this.errorMessage = message;
    }


    public SparksoniqRuntimeException(String message, String errorCode, ExpressionMetadata metadata) {
        super("Error [err: " + errorCode + "]" + (metadata != null ?
                "LINE:" + metadata.getTokenLineNumber() +
                        ":COLUMN:" + metadata.getTokenColumnNumber() + ":" : "")
                + message);
        if (!Arrays.asList(ErrorCodes.class.getFields()).stream().anyMatch(f -> {
            try {
                return f.get(null).equals(errorCode);
            } catch (IllegalAccessException e) {
                return true;
            }
        }))
            this.errorCode = ErrorCodes.RuntimeExceptionErrorCode;
        else
            this.errorCode = errorCode;
        this.metadata = metadata;
        this.errorMessage = message;
    }

    public SparksoniqRuntimeException(String message, ExpressionMetadata metadata) {
        super("Error [err: " + ErrorCodes.RuntimeExceptionErrorCode + "]" + (metadata != null ?
                "LINE:" + metadata.getTokenLineNumber() +
                        ";COLUMN:" + metadata.getTokenColumnNumber() + ";" : "")
                + message);
        this.errorCode = ErrorCodes.RuntimeExceptionErrorCode;
        ;
        this.metadata = metadata;
        this.errorMessage = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ExpressionMetadata getMetadata() {
        return metadata;
    }

    public String getJSONiqErrorMessage() {
        return errorMessage;
    }
}
