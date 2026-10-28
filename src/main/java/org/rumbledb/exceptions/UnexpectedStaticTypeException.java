/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class UnexpectedStaticTypeException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean certain;

    public UnexpectedStaticTypeException(String message) {
        super(message, ErrorCode.UnexpectedTypeErrorCode);
        this.certain = true;
    }

    public UnexpectedStaticTypeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedTypeErrorCode, metadata);
        this.certain = true;
    }

    public UnexpectedStaticTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
        this.certain = true;
    }

    public UnexpectedStaticTypeException(String message, ErrorCode errorCode, ExceptionMetadata metadata) {
        this(message, errorCode, metadata, true);
    }

    public UnexpectedStaticTypeException(
            String message, ErrorCode errorCode, ExceptionMetadata metadata, boolean certain) {
        super(message, errorCode, metadata);
        this.certain = certain;
    }

    /**
     * Whether every value the expression can have fails the check. Otherwise, the expression fails only for some
     * values, such as an item* argument passed as an xs:integer.
     */
    public boolean isCertain() {
        return this.certain;
    }
}
