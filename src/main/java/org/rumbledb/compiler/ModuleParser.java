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

import java.net.URI;
import java.util.function.Supplier;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.parser.jsoniq.JsoniqLexer;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryLexer;
import org.rumbledb.parser.xquery.XQueryParser;

/** Language selection, ANTLR setup, translation, and source-aware syntax errors. No analysis passes run here. */
final class ModuleParser {

    private ModuleParser() {}

    enum Language {
        JSONIQ,
        XQUERY
    }

    static Language detectLanguage(String query, URI uri, RumbleConfiguration configuration) {
        return shouldParseAsXQuery(query, uri, configuration) ? Language.XQUERY : Language.JSONIQ;
    }

    static StaticContext createModuleContext(URI uri, RumbleConfiguration configuration) {
        StaticContext context = new StaticContext(uri, configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        context.setUserDefinedFunctionsExecutionModes(executionModes);
        return context;
    }

    static MainModule parseMainModule(
            String query,
            URI uri,
            CompilationConfiguration configuration,
            ExternalBindings bindings,
            Language language) {
        StaticContext moduleContext = createModuleContext(uri, configuration.runtimeConfiguration());
        return ModuleImportLoader.loadMainModule(
                () -> (MainModule) parseModule(query, uri, configuration, bindings, language, moduleContext, true),
                configuration.runtimeConfiguration());
    }

    static LibraryModule parseLibraryModule(
            String query, URI uri, StaticContext importingContext, CompilationConfiguration configuration) {
        RumbleConfiguration runtimeConfiguration = configuration.runtimeConfiguration();
        Language language = detectLanguage(query, uri, runtimeConfiguration);
        StaticContext moduleContext = new StaticContext(uri, runtimeConfiguration);
        moduleContext.setUserDefinedFunctionsExecutionModes(importingContext.getUserDefinedFunctionsExecutionModes());
        return (LibraryModule)
                parseModule(query, uri, configuration, ExternalBindings.empty(), language, moduleContext, false);
    }

    private static boolean shouldParseAsXQuery(String query, URI uri, RumbleConfiguration configuration) {
        if (query.contains("xquery version") || query.contains("xquery encoding")) {
            return true;
        }
        if (query.contains("jsoniq version")) {
            return false;
        }
        String location = uri.toString();
        if (location.endsWith(".xq") || location.endsWith(".xqy") || location.endsWith(".xquery")) {
            return true;
        }
        if (location.endsWith(".jq") || location.endsWith(".jsoniq")) {
            return false;
        }
        return configuration.semantics().queryLanguage().startsWith("xquery");
    }

    /**
     * Shares tokenization setup, translation, and syntax-error conversion for both module kinds.
     * Only lexer/parser/visitor construction and the grammar entry rule depend on the language.
     */
    private static Module parseModule(
            String query,
            URI uri,
            CompilationConfiguration configuration,
            ExternalBindings bindings,
            Language language,
            StaticContext moduleContext,
            boolean isMainModule) {
        if (language == Language.XQUERY) {
            // XQuery source line endings are normalized before tokenization, including inside literals.
            query = query.replace("\r\n", "\n");
            if ("1.1".equals(configuration.runtimeConfiguration().semantics().xmlVersion())) {
                query = query.replace("\r\u0085", "\n").replace('\u0085', '\n').replace('\u2028', '\n');
            }
            query = query.replace('\r', '\n');
        }
        // File extensions and version declarations can select a language different from the configured default.
        if (language == Language.XQUERY && !moduleContext.getQueryLanguage().startsWith("xquery")) {
            moduleContext.setQueryLanguage("xquery31");
        }
        Lexer lexer = language == Language.XQUERY
                ? new XQueryLexer(CharStreams.fromString(query))
                : new JsoniqLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Parser parser;
        ParseTreeVisitor<Node> visitor;

        // Return the parse tree for the module entry rule
        Supplier<? extends ParseTree> parseTreeSupplier;

        if (language == Language.XQUERY) {
            XQueryParser xqueryParser = new XQueryParser(tokens);
            parser = xqueryParser;
            parseTreeSupplier =
                    isMainModule ? () -> xqueryParser.moduleAndThisIsIt().module() : xqueryParser::module;
            visitor = new XQueryTranslationVisitor(moduleContext, isMainModule, configuration, bindings, query, tokens);
        } else {
            JsoniqParser jsoniqParser = new JsoniqParser(tokens);
            parser = jsoniqParser;
            parseTreeSupplier = () -> jsoniqParser.moduleAndThisIsIt().module();
            visitor = new TranslationVisitor(moduleContext, isMainModule, configuration, bindings, query, tokens);
        }

        parser.setErrorHandler(new BailErrorStrategy());

        try {
            if (!isMainModule) {
                // A namespace URI may retrieve an HTML page or a main query rather than a library.
                // Reject those as failed module resolution, preserving syntax errors inside real libraries.
                if (language == Language.XQUERY && tokens.LA(1) == XQueryParser.KW_XQUERY) {
                    ((XQueryParser) parser).versionDecl();
                } else if (language == Language.JSONIQ && tokens.LA(1) == JsoniqParser.KW_JSONIQ) {
                    ((JsoniqParser) parser).versionDecl();
                }
                int moduleToken = language == Language.XQUERY ? XQueryParser.KW_MODULE : JsoniqParser.KW_MODULE;
                if (tokens.LA(1) != moduleToken) {
                    throw new ModuleNotFoundException(
                            "The imported resource is not a library module.",
                            ExceptionMetadata.fromPoint(
                                    uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
                }
                tokens.seek(0);
            }
            // TODO Handle module extras
            ParseTree moduleParseTree = parseTreeSupplier.get();
            if (isMainModule && moduleParseTree == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            if (language == Language.XQUERY
                    && !configuration.runtimeConfiguration().semantics().allowScripting()) {
                new XQuerySyntaxValidator(uri, query).visit(moduleParseTree);
            }
            return (Module) visitor.visit(moduleParseTree);
        } catch (ParseCancellationException ex) {
            ParsingException exception = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
            exception.initCause(ex);
            throw exception;
        }
    }
}
