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
package org.rumbledb.compiler.frontend;

import java.util.Set;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidFunctionNamespaceException;

/** Static validation shared by the JSONiq and XQuery function declaration translators. */
public final class FunctionDeclarationValidator {

    private FunctionDeclarationValidator() {}

    private static final Set<String> RESERVED_NAMESPACES = Set.of(
            Name.XML_NS,
            Name.XS_NS,
            Name.XSI_NS,
            Name.FN_NS,
            Name.MATH_NS,
            Name.XQUERY_ANNOTATIONS_NS,
            Name.ARRAY_NS,
            Name.MAP_NS);

    /**
     * Functions cannot be declared in the reserved namespaces.
     * See https://www.w3.org/TR/xquery-31/#FunctionDeclns for more information.
     */
    public static void validateFunctionName(Name name, ExceptionMetadata metadata) {
        String namespace = name.getNamespace();
        if (RESERVED_NAMESPACES.contains(namespace)) {
            throw new InvalidFunctionNamespaceException(
                    "Functions cannot be declared in the reserved namespace " + namespace + ".", metadata);
        }
    }
}
