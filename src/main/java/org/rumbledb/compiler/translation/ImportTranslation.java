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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.ModuleImportLoader;
import org.rumbledb.compiler.context.ModuleImportContext;
import org.rumbledb.compiler.context.SchemaImportContext;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.exceptions.EmptyModuleURIException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.SchemaImport;

/** Translates grammar-neutral import contexts into imported modules and schema declarations. */
public final class ImportTranslation {

    private ImportTranslation() {}

    public static <UriLiteralCtx extends ParserRuleContext> SchemaImport schemaImport(
            SchemaImportContext<UriLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<UriLiteralCtx, String> processURILiteral) {
        String targetNamespace = URILiteralUtils.normalizeAsAnyURI(processURILiteral.apply(ctx.targetNamespace()));
        List<String> locationHints = ctx.locations().stream()
                .map(processURILiteral)
                .map(URILiteralUtils::normalizeAsAnyURI)
                .collect(Collectors.toList());
        return new SchemaImport(
                targetNamespace,
                ctx.bindingKind(),
                ctx.prefix(),
                locationHints,
                translationContext.metadata(ctx.context()));
    }

    public static <UriLiteralCtx extends ParserRuleContext> LibraryModule moduleImport(
            ModuleImportContext<UriLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<UriLiteralCtx, String> processURILiteral,
            NamespaceBinder bindNamespace) {
        ExceptionMetadata metadata = translationContext.metadata(ctx.context());
        String namespace = processURILiteral.apply(ctx.targetNamespace());
        if (namespace.isEmpty()) {
            throw new EmptyModuleURIException("Module URI is empty.", metadata);
        }
        if (ctx.prefix() != null) {
            String prefix = ctx.prefix();
            if (prefix.equals("xml") || prefix.equals("xmlns")) {
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Module import prefix " + prefix + " is reserved.", metadata);
            }
        }
        namespace = URILiteralUtils.normalizeAsAnyURI(namespace);
        List<String> locationHints = ctx.locations().stream()
                .map(processURILiteral)
                .map(URILiteralUtils::normalizeAsAnyURI)
                .collect(Collectors.toList());
        LibraryModule libraryModule = ModuleImportLoader.load(
                namespace,
                locationHints,
                translationContext.moduleContext(),
                translationContext.compilationConfiguration(),
                metadata);
        if (ctx.prefix() != null) {
            bindNamespace.bind(ctx.prefix(), libraryModule.getNamespace(), metadata);
        }
        return libraryModule;
    }
}
