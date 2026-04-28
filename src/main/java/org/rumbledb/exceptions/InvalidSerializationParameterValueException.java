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
 * Authors: Matteo Agnoletto (EPMatt)
 * 
 */

package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

/**
 * Exception for SEPM0016: It is an error if a parameter value is invalid for the defined domain.
 * 
 * @see <a href="https://www.w3.org/TR/xslt-xquery-serialization-31/#ERRSEPM0016">XSLT/XQuery Serialization 3.1, C:
 *      SEPM0016</a>
 */
public class InvalidSerializationParameterValueException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidSerializationParameterValueException(
            String parameterName,
            String value,
            String expectedValue,
            ExceptionMetadata metadata
    ) {
        super(
            "The value for the serialization parameter "
                + parameterName
                + " must be "
                + expectedValue
                + ", got: "
                + value,
            ErrorCode.InvalidSerializationParameterValue,
            metadata
        );
    }

    public InvalidSerializationParameterValueException(String parameterName, String value, String expectedValue) {
        super(
            "The value for the serialization parameter "
                + parameterName
                + " must be "
                + expectedValue
                + ", got: "
                + value,
            ErrorCode.InvalidSerializationParameterValue
        );
    }
}
