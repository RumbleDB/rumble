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
 * Exception for XQST0070: It is a static error if a namespace declaration attribute attempts to do any of the
 * following:
 * - Bind the prefix xml to some namespace URI other than http://www.w3.org/XML/1998/namespace
 * - Bind the prefix xmlns to any namespace URI
 * - Bind a prefix to the namespace URI http://www.w3.org/2000/xmlns/
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#ERRXQST0070">XQuery 3.1, F: XQST0070</a>
 */
public class PredefinedPrefixInNamespaceDeclarationException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public PredefinedPrefixInNamespaceDeclarationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.PredefinedPrefixInNamespaceDeclarationErrorCode, metadata);
    }

    public PredefinedPrefixInNamespaceDeclarationException(String message) {
        super(message, ErrorCode.PredefinedPrefixInNamespaceDeclarationErrorCode);
    }
}

