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

package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.util.Arrays;

import org.apache.spark.SparkException;

public class RumbleException extends RuntimeException {


    private static final long serialVersionUID = 1L;
    private final ErrorCode errorCode;
    private final String errorMessage;
    private ExceptionMetadata metadata;

    public RumbleException(String message) {
        super(formatMessage(ErrorCode.RuntimeExceptionErrorCode, ExceptionMetadata.EMPTY_METADATA, message));
        this.errorCode = ErrorCode.RuntimeExceptionErrorCode;
        this.errorMessage = message;
        this.metadata = ExceptionMetadata.EMPTY_METADATA;
    }

    public RumbleException(String message, ErrorCode errorCode) {
        super(formatMessage(errorCode, ExceptionMetadata.EMPTY_METADATA, message));
        if (!Arrays.asList(ErrorCode.class.getFields()).stream().anyMatch(f -> {
            try {
                return f.get(null).equals(errorCode);
            } catch (IllegalAccessException e) {
                return true;
            }
        })) {
            this.errorCode = ErrorCode.RuntimeExceptionErrorCode;
        } else {
            this.errorCode = errorCode;
        }
        this.errorMessage = message;
        this.metadata = ExceptionMetadata.EMPTY_METADATA;
    }


    public RumbleException(String message, ErrorCode errorCode, ExceptionMetadata metadata) {
        super(formatMessage(errorCode, metadata, message));
        if (!Arrays.asList(ErrorCode.class.getFields()).stream().anyMatch(f -> {
            try {
                return f.get(null).equals(errorCode);
            } catch (IllegalAccessException e) {
                return true;
            }
        })) {
            this.errorCode = ErrorCode.RuntimeExceptionErrorCode;
        } else {
            this.errorCode = errorCode;
        }
        this.metadata = metadata;
        this.errorMessage = message;
    }

    public RumbleException(String message, ExceptionMetadata metadata) {
        super(formatMessage(ErrorCode.RuntimeExceptionErrorCode, metadata, message));
        this.errorCode = ErrorCode.RuntimeExceptionErrorCode;
        this.metadata = metadata;
        this.errorMessage = message;
    }

    private static String formatMessage(ErrorCode errorCode, ExceptionMetadata metadata, String message) {
        if (metadata.getTokenLineNumber() == 0) {
            return "There was an error."
                + "\nCode: ["
                + errorCode
                + "]\n"
                + "Message: "
                + message
                + "\n"
                + "Metadata: "
                + ((metadata != null) ? metadata.toString() : null)
                + "\n"
                + "This code can also be looked up in the documentation and specifications for more information.\n";
        }
        return "There was an error on line "
            + metadata.getTokenLineNumber()
            + " in "
            + metadata.getLocation()
            + ":\n\n"
            + metadata.getLineInContext()
            + "\nCode: ["
            + errorCode
            + "]\n"
            + "Message: "
            + message
            + "\n"
            + "Metadata: "
            + ((metadata != null) ? metadata.toString() : null)
            + "\n"
            + "This code can also be looked up in the documentation and specifications for more information.\n";
    }

    public String getErrorCode() {
        return this.errorCode.toString();
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    public String getJSONiqErrorMessage() {
        return this.errorMessage;
    }

    public static RumbleException unnestException(Throwable ex) {
        if (ex instanceof SparkException) {
            Throwable sparkExceptionCause = ex.getCause();
            return unnestException(sparkExceptionCause);
        } else if (ex instanceof RumbleException) {
            return (RumbleException) ex;
        } else {
            RumbleException e2 = new OurBadException("Unanticipated exception!");
            e2.initCause(ex);
            return e2;
        }
    }
}
