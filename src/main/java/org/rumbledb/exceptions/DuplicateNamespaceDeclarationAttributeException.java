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

/** Static error for duplicate namespace declaration attributes in a direct element constructor [err:XQST0071]. */
public class DuplicateNamespaceDeclarationAttributeException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateNamespaceDeclarationAttributeException(String prefix, ExceptionMetadata metadata) {
        super(
                "Duplicate namespace declaration attribute in direct element constructor: "
                        + (prefix.isEmpty() ? "xmlns" : "xmlns:" + prefix),
                ErrorCode.DuplicateNamespaceDeclarationAttributeErrorCode,
                metadata);
    }
}
