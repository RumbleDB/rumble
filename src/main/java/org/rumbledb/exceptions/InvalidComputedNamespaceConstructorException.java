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
 * Exception for XQDY0101: It is a dynamic error if a computed namespace constructor attempts to do any of the
 * following:
 * - Bind the prefix xml to some namespace URI other than http://www.w3.org/XML/1998/namespace
 * - Bind a prefix other than xml to the namespace URI http://www.w3.org/XML/1998/namespace
 * - Bind the prefix xmlns to any namespace URI
 * - Bind a prefix to the namespace URI http://www.w3.org/2000/xmlns/
 * - Bind any prefix (including the empty prefix) to a zero-length namespace URI
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#ERRXQDY0101">XQuery 3.1, F: XQDY0101</a>
 */
public class InvalidComputedNamespaceConstructorException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidComputedNamespaceConstructorException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComputedNamespaceConstructorErrorCode, metadata);
    }

    public InvalidComputedNamespaceConstructorException(String message) {
        super(message, ErrorCode.InvalidComputedNamespaceConstructorErrorCode);
    }
}

