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
package org.rumbledb.compiler;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.PrefixCannotBeExpandedException;

/** Resolves lexical names after a grammar-specific visitor has extracted their components. */
final class TranslationNameResolver {

    enum NameRole {
        FUNCTION,
        TYPE,
        ANNOTATION,
        ELEMENT_CONSTRUCTOR,
        NO_DEFAULT_NAMESPACE
    }

    private final TranslationContext context;

    TranslationNameResolver(TranslationContext context) {
        this.context = context;
    }

    Name resolveFunctionName(String lexicalName, ExceptionMetadata metadata) {
        if (lexicalName.startsWith("Q{")) {
            return URIQualifiedNameParser.parse(lexicalName, metadata);
        }
        int colonIndex = lexicalName.indexOf(':');
        if (colonIndex >= 0) {
            return this.resolvePrefixedName(
                    lexicalName.substring(0, colonIndex), lexicalName.substring(colonIndex + 1), metadata);
        }
        return this.resolveUnprefixedName(lexicalName, NameRole.FUNCTION);
    }

    Name resolveQName(String fullQName, String prefix, String localName, NameRole role, ExceptionMetadata metadata) {
        if (fullQName != null) {
            int colonIndex = fullQName.indexOf(':');
            if (colonIndex < 0) {
                throw new ParsingException("Invalid FullQName format: " + fullQName, metadata);
            }
            prefix = fullQName.substring(0, colonIndex);
            localName = fullQName.substring(colonIndex + 1);
        }
        return prefix == null
                ? this.resolveUnprefixedName(localName, role)
                : this.resolvePrefixedName(prefix, localName, metadata);
    }

    private Name resolveUnprefixedName(String localName, NameRole role) {
        if (role == NameRole.FUNCTION) {
            String namespace = this.context.moduleContext().getDefaultFunctionNamespaceUri();
            return namespace == null
                    ? Name.createVariableInDefaultFunctionNamespace(localName)
                    : new Name(namespace, "", localName);
        }
        if (role == NameRole.TYPE || role == NameRole.ANNOTATION) {
            String namespace = this.context.resolveNamespace("");
            if (namespace != null) {
                return new Name(namespace, "", localName);
            }
            return role == NameRole.TYPE
                    ? Name.createVariableInDefaultTypeNamespace(localName)
                    : Name.createNameInDefaultXQueryAnnotationsNamespace(localName);
        }
        if (role == NameRole.ELEMENT_CONSTRUCTOR) {
            String namespace = this.context.resolveNamespace("");
            if (namespace != null) {
                return new Name(namespace, "", localName);
            }
        }
        return Name.createVariableInNoNamespace(localName);
    }

    private Name resolvePrefixedName(String prefix, String localName, ExceptionMetadata metadata) {
        String namespace = this.context.resolveNamespace(prefix);
        if (namespace == null) {
            throw new PrefixCannotBeExpandedException("Cannot expand prefix " + prefix, metadata);
        }
        return new Name(namespace, prefix, localName);
    }
}
