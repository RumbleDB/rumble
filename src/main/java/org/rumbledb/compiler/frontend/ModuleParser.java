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

import static org.rumbledb.compiler.CompilerDiagnostics.debugPrintHeader;

/** Selects the language and translates module source into an AST. Analysis is performed by callers. */
public final class ModuleParser {
    private ModuleParser() {}

    /** Supported source languages. */
    public enum Language {
        JSONIQ,
        XQUERY
    }

    /** The AST and the language selected from the source, URI, and configuration. */
    public record ParsedMainModule(MainModule module, Language language) {}

    // Preserve the pre-refactor selection policy: a configured static base URI takes
    // precedence over the resource URI for extension detection. Source identity is
    // kept separately for diagnostics and must not silently change the parser.
    private static boolean shouldParseAsXQuery(String query, URI staticBaseUri, RumbleConfiguration configuration) {
        if (query.contains("xquery version")) {
            return true;
        }
        if (query.contains("jsoniq version")) {
            return false;
        }
        String location = staticBaseUri.toString();
        if (location.endsWith(".xq") || location.endsWith(".xqy") || location.endsWith(".xquery")) {
            return true;
        }
        if (location.endsWith(".jq") || location.endsWith(".jsoniq")) {
            return false;
        }
        return configuration.semantics().queryLanguage().startsWith("xquery");
    }

    public static ParsedMainModule parseMainModule(
            ModuleSource source, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        if (shouldParseAsXQuery(source.text(), source.staticBaseUri(), configuration)) {
            return new ParsedMainModule(
                    parseXQueryMainModule(source, compilationConfiguration, externalBindings), Language.XQUERY);
        }
        return new ParsedMainModule(
                parseJSONiqMainModule(source, compilationConfiguration, externalBindings), Language.JSONIQ);
    }

    private static MainModule parseJSONiqMainModule(
            ModuleSource source, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(source.text());
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);
        JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
                moduleContext, true, compilationConfiguration, externalBindings, source, jsoniqTokens);
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
                    ExceptionMetadata.fromPoint(
                            source.sourceUri().toString(),
                            lexer.getLine(),
                            lexer.getCharPositionInLine(),
                            source.text()));
            e.initCause(ex);
            throw e;
        }
    }

    private static MainModule parseXQueryMainModule(
            ModuleSource source, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(source.text());
        XQueryLexer lexer = new XQueryLexer(stream);
        CommonTokenStream xQueryTokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(xQueryTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                moduleContext, true, compilationConfiguration, externalBindings, source, xQueryTokens);
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
                    ExceptionMetadata.fromPoint(
                            source.sourceUri().toString(),
                            lexer.getLine(),
                            lexer.getCharPositionInLine(),
                            source.text()));
            e.initCause(ex);
            throw e;
        }
    }

    public static LibraryModule parseLibraryModule(
            ModuleSource source,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        if (shouldParseAsXQuery(source.text(), source.staticBaseUri(), configuration)) {
            return parseXQueryLibraryModule(source, importingModuleContext, compilationConfiguration);
        }
        return parseJSONiqLibraryModule(source, importingModuleContext, compilationConfiguration);
    }

    private static LibraryModule parseJSONiqLibraryModule(
            ModuleSource source,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(source.text());
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
                importingModuleContext.getUserDefinedFunctionsExecutionModes());
        JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
                moduleContext, false, compilationConfiguration, ExternalBindings.empty(), source, jsoniqTokens);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                            source.sourceUri().toString(),
                            lexer.getLine(),
                            lexer.getCharPositionInLine(),
                            source.text()));
            e.initCause(ex);
            throw e;
        }
    }

    private static LibraryModule parseXQueryLibraryModule(
            ModuleSource source,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        CharStream stream = CharStreams.fromString(source.text());
        XQueryLexer lexer = new XQueryLexer(stream);
        CommonTokenStream xQueryTokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(xQueryTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
                importingModuleContext.getUserDefinedFunctionsExecutionModes());
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                moduleContext, false, compilationConfiguration, ExternalBindings.empty(), source, xQueryTokens);
        try {
            // TODO Handle module extras
            XQueryParser.ModuleContext main = parser.module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                            source.sourceUri().toString(),
                            lexer.getLine(),
                            lexer.getCharPositionInLine(),
                            source.text()));
            e.initCause(ex);
            throw e;
        }
    }
}
