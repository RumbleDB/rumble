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
 * Exception for XQDY0072: computed comment constructor content contains illegal hyphen sequences.
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#ERRXQDY0072">XQuery 3.1, F: XQDY0072</a>
 */
public class InvalidCommentContentException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidCommentContentException(String content, ExceptionMetadata metadata) {
        super(
            "Dynamic error; invalid comment content: \"" + content + "\"",
            ErrorCode.InvalidCommentContentErrorCode,
            metadata
        );
    }

    public InvalidCommentContentException(String content) {
        super(
            "Dynamic error; invalid comment content: \"" + content + "\"",
            ErrorCode.InvalidCommentContentErrorCode
        );
    }
}

