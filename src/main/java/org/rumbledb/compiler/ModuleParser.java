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
        return (MainModule) parseModule(query, uri, configuration, bindings, language, moduleContext, true);
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
        if (query.contains("xquery version")) {
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
            // TODO Handle module extras
            ParseTree moduleParseTree = parseTreeSupplier.get();
            if (isMainModule && moduleParseTree == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
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
