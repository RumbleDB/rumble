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
 */

package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

/**
 * Exception for XQDY0025: It is a dynamic error if any attribute of a constructed element
 * does not have a name that is distinct from the names of all other attributes of the constructed element.
 */
public class DuplicateAttributeException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DuplicateAttributeException(String attributeName, ExceptionMetadata metadata) {
        super(
            "Dynamic error; Duplicate attribute name in element constructor: " + attributeName,
            ErrorCode.DuplicateAttributeErrorCode,
            metadata
        );
    }

    public DuplicateAttributeException(String attributeName) {
        super(
            "Dynamic error; Duplicate attribute name in element constructor: " + attributeName,
            ErrorCode.DuplicateAttributeErrorCode
        );
    }
}
