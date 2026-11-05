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

import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.compiler.ModuleImportLoader;
import org.rumbledb.compiler.context.LibraryModuleContext;
import org.rumbledb.compiler.context.MainModuleContext;
import org.rumbledb.compiler.context.ModuleContext;
import org.rumbledb.compiler.context.ProgramContext;
import org.rumbledb.compiler.context.VersionDeclContext;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.model.LanguageVersion;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.EmptyModuleURIException;
import org.rumbledb.exceptions.JsoniqVersionException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;

/**
 * Translates module-level constructs: Module, MainModule, LibraryModule, and Program.
 */
@Log4j2
public final class ModuleTranslation {

    private ModuleTranslation() {}

    /** Validates a version declaration's encoding and applies its version to the module. */
    public static <
                    StringLiteralCtx extends ParserRuleContext,
                    MainModuleCtx extends ParserRuleContext,
                    LibraryModuleCtx extends ParserRuleContext>
            Module module(
                    ModuleContext<StringLiteralCtx, MainModuleCtx, LibraryModuleCtx> ctx,
                    TranslationContext translationContext,
                    Function<StringLiteralCtx, String> processStringLiteral,
                    Function<MainModuleCtx, MainModule> visitMainModule,
                    Function<LibraryModuleCtx, LibraryModule> visitLibraryModule) {
        if (ctx.versionDecl() != null) {
            versionDecl(ctx.versionDecl(), translationContext, processStringLiteral);
        }
        return ctx.libraryModule() != null
                ? visitLibraryModule.apply(ctx.libraryModule())
                : visitMainModule.apply(ctx.mainModule());
    }

    private static <StringLiteralCtx extends ParserRuleContext> void versionDecl(
            VersionDeclContext<StringLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<StringLiteralCtx, String> processStringLiteral) {
        if (ctx.encoding() != null) {
            String encoding = processStringLiteral.apply(ctx.encoding());
            if (!encoding.matches("[A-Za-z][A-Za-z0-9._-]*")) {
                throw new SemanticException(
                        "Invalid encoding name: " + encoding,
                        ErrorCode.InvalidEncodingNameErrorCode,
                        translationContext.metadata(ctx.encoding()));
            }
        }
        if (ctx.version() != null) {
            String version = processStringLiteral.apply(ctx.version()).trim();
            translationContext
                    .moduleContext()
                    .setQueryLanguage(LanguageVersion.fromDeclaration(ctx.language(), version)
                            .orElseThrow(() -> new JsoniqVersionException(translationContext.metadata(ctx.context()))));
        }
    }

    public static <StatementsCtx extends ParserRuleContext> Program program(
            ProgramContext<StatementsCtx> ctx,
            TranslationContext translationContext,
            Function<StatementsCtx, StatementsAndOptionalExpr> visitStatementsAndOptionalExpr) {
        StatementsAndOptionalExpr statementsAndOptionalExpr =
                visitStatementsAndOptionalExpr.apply(ctx.statementsAndOptionalExpr());
        return new Program(statementsAndOptionalExpr, translationContext.metadata(ctx.context()));
    }

    public static <PrologCtx extends ParserRuleContext, ProgramCtx extends ParserRuleContext> MainModule mainModule(
            MainModuleContext<PrologCtx, ProgramCtx> ctx,
            TranslationContext translationContext,
            Function<PrologCtx, Prolog> visitProlog,
            Function<ProgramCtx, Program> visitProgram) {
        Prolog prolog = visitProlog.apply(ctx.prolog());
        Program program = visitProgram.apply(ctx.program());
        if (ExternalVariableTranslation.process(
                prolog, translationContext.externalBindings(), translationContext.metadata(ctx.context()))) {
            log.warn("Adding context item declaration.");
        }

        MainModule module = new MainModule(prolog, program, translationContext.metadata(ctx.context()));
        module.setStaticContext(translationContext.moduleContext());
        return module;
    }

    public static <UriLiteralCtx extends ParserRuleContext, PrologCtx extends ParserRuleContext>
            LibraryModule libraryModule(
                    LibraryModuleContext<UriLiteralCtx, PrologCtx> ctx,
                    TranslationContext translationContext,
                    Function<UriLiteralCtx, String> processURILiteral,
                    Function<PrologCtx, Prolog> visitProlog) {
        String prefix = ctx.prefix();
        String namespace = URILiteralUtils.normalizeAsAnyURI(processURILiteral.apply(ctx.uriLiteral()));
        if (namespace.equals("")) {
            throw new EmptyModuleURIException("Module URI is empty.", translationContext.metadata(ctx.context()));
        }
        NamespaceBindingUtils.validatePrologNamespaceDeclaration(
                prefix, namespace, translationContext.metadata(ctx.context()));
        ModuleImportLoader.registerActiveContext(namespace, translationContext.moduleContext());
        translationContext.setLibraryModuleNamespace(namespace);
        translationContext.bindNamespace(prefix, namespace, translationContext.metadata(ctx.context()));

        Prolog prolog = visitProlog.apply(ctx.prolog());
        LibraryModule module = new LibraryModule(prolog, namespace, translationContext.metadata(ctx.context()));
        module.setStaticContext(translationContext.moduleContext());
        return module;
    }
}
