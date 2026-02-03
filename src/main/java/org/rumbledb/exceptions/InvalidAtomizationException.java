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
 * Exception for XQDY0074: It is a dynamic error if the value of the name expression in a computed element or attribute
 * constructor cannot be converted to an expanded QName (for example, because it contains a namespace prefix not found
 * in statically known namespaces).
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#ERRXQDY0074">XQuery 3.1, F: XQDY0074</a>
 */
public class InvalidAtomizationException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidAtomizationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAtomizationErrorCode, metadata);
    }

    public InvalidAtomizationException(String message) {
        super(message, ErrorCode.InvalidAtomizationErrorCode);
    }
}

