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

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.parser.jsoniq.JsoniqLexer;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryLexer;
import org.rumbledb.parser.xquery.XQueryParser;

import static org.rumbledb.compiler.CompilationDiagnostics.debugPrintHeader;

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
        return language == Language.XQUERY
                ? parseXQueryMainModule(query, uri, configuration, bindings)
                : parseJSONiqMainModule(query, uri, configuration, bindings);
    }

    static LibraryModule parseLibraryModule(
            String query, URI uri, StaticContext importingContext, CompilationConfiguration configuration) {
        return detectLanguage(query, uri, configuration.runtimeConfiguration()) == Language.XQUERY
                ? parseXQueryLibraryModule(query, uri, importingContext, configuration)
                : parseJSONiqLibraryModule(query, uri, importingContext, configuration);
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

    private static MainModule parseJSONiqMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = createModuleContext(uri, configuration);
        TranslationVisitor visitor = new TranslationVisitor(
                moduleContext, true, compilationConfiguration, externalBindings, query, jsoniqTokens);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext modulectx = parser.moduleAndThisIsIt().module();
            if (modulectx == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }

            debugPrintHeader(configuration, "Parsing program");
            MainModule mainModule = (MainModule) visitor.visit(modulectx);
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
            e.initCause(ex);
            throw e;
        }
    }

    private static MainModule parseXQueryMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(query);
        XQueryLexer lexer = new XQueryLexer(stream);
        CommonTokenStream xQueryTokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(xQueryTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = createModuleContext(uri, configuration);
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                moduleContext, true, compilationConfiguration, externalBindings, query, xQueryTokens);
        try {
            // TODO Handle module extras
            XQueryParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            if (main == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            MainModule mainModule = (MainModule) visitor.visit(main);
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
            e.initCause(ex);
            throw e;
        }
    }

    private static LibraryModule parseJSONiqLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
                importingModuleContext.getUserDefinedFunctionsExecutionModes());
        TranslationVisitor visitor = new TranslationVisitor(
                moduleContext, false, compilationConfiguration, ExternalBindings.empty(), query, jsoniqTokens);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
            e.initCause(ex);
            throw e;
        }
    }

    private static LibraryModule parseXQueryLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(query);
        XQueryLexer lexer = new XQueryLexer(stream);
        CommonTokenStream xQueryTokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(xQueryTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
                importingModuleContext.getUserDefinedFunctionsExecutionModes());
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                moduleContext, false, compilationConfiguration, ExternalBindings.empty(), query, xQueryTokens);
        try {
            // TODO Handle module extras
            XQueryParser.ModuleContext main = parser.module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(uri.toString(), lexer.getLine(), lexer.getCharPositionInLine(), query));
            e.initCause(ex);
            throw e;
        }
    }
}
