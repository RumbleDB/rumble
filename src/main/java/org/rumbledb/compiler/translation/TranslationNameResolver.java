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
package org.rumbledb.compiler.translation;

import java.util.HashSet;
import java.util.Set;

import org.rumbledb.compiler.URIQualifiedNameParser;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.PrefixCannotBeExpandedException;

/** Resolves lexical names after a grammar-specific visitor has extracted their components. */
public final class TranslationNameResolver {

    public enum NameRole {
        FUNCTION,
        TYPE,
        ANNOTATION,
        ELEMENT_CONSTRUCTOR,
        NO_DEFAULT_NAMESPACE
    }

    private final TranslationContext context;
    private final Set<Name> moduleLocalVariables = new HashSet<>();
    private final Set<Name> moduleLocalFunctions = new HashSet<>();
    private final Set<Name> sharedFunctions = new HashSet<>();
    private String owner;

    TranslationNameResolver(TranslationContext context) {
        this.context = context;
    }

    public Name resolveEQName(String lexicalName, NameRole role, ExceptionMetadata metadata) {
        if (lexicalName.startsWith("Q{")) {
            return URIQualifiedNameParser.parse(
                    lexicalName, this.context.configuration().semantics().xmlVersion(), metadata);
        }
        int colonIndex = lexicalName.indexOf(':');
        if (colonIndex >= 0) {
            return this.resolvePrefixedName(
                    lexicalName.substring(0, colonIndex), lexicalName.substring(colonIndex + 1), metadata);
        }
        return this.resolveUnprefixedName(lexicalName, role);
    }

    public Name resolveFunctionName(String lexicalName, ExceptionMetadata metadata) {
        Name name = this.resolveEQName(lexicalName, NameRole.FUNCTION, metadata);
        return this.moduleLocalFunctions.contains(name) && !this.sharedFunctions.contains(name)
                ? name.ownedBy(this.owner())
                : name;
    }

    /** A variable name in this module's code, which is the module's own if it declares that name as non-public. */
    public Name variableName(Name name) {
        return this.moduleLocalVariables.contains(name) ? name.ownedBy(this.owner()) : name;
    }

    public void declareVariable(Name name, boolean isPrivate) {
        if (this.isModuleLocal(name.withoutOwner(), isPrivate)) {
            this.moduleLocalVariables.add(name.withoutOwner());
        }
    }

    /** One name covers every arity, so it is only the module's own if all its arities are. */
    public void declareFunction(Name name, boolean isPrivate) {
        (this.isModuleLocal(name.withoutOwner(), isPrivate) ? this.moduleLocalFunctions : this.sharedFunctions)
                .add(name.withoutOwner());
    }

    /** Private to a library, or a main module's name in a namespace that only a module it does not import has. */
    private boolean isModuleLocal(Name name, boolean isPrivate) {
        if (this.context.isLibraryModule()) {
            return isPrivate;
        }
        String namespace = name.getNamespace();
        return namespace != null
                && !this.context.moduleContext().getImportedNamespaces().containsKey(namespace)
                && this.context.importLoader().hasModules(namespace);
    }

    private String owner() {
        if (this.owner == null) {
            this.owner = this.context.importLoader().newModuleOwner();
        }
        return this.owner;
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
            if (role == NameRole.TYPE
                    && this.context.moduleContext().getQueryLanguage().isXQuery()) {
                return Name.createVariableInNoNamespace(localName);
            }
            return role == NameRole.TYPE
                    ? Name.createVariableInDefaultTypeNamespace(localName)
                    : Name.createNameInDefaultXQueryAnnotationsNamespace(localName);
        }
        if (role == NameRole.ELEMENT_CONSTRUCTOR) {
            String namespace = this.context.resolveNamespace("");
            if (namespace != null && !namespace.isEmpty()) {
                return new Name(namespace, "", localName);
            }
        }
        return Name.createVariableInNoNamespace(localName);
    }

    private Name resolvePrefixedName(String prefix, String localName, ExceptionMetadata metadata) {
        String namespace = this.context.resolveNamespace(prefix);
        if (namespace == null || namespace.isEmpty()) {
            throw new PrefixCannotBeExpandedException("Cannot expand prefix " + prefix, metadata);
        }
        return new Name(namespace, prefix, localName);
    }
}
