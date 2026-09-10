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
import org.antlr.v4.runtime.Lexer;
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
        boolean useXQuery = shouldParseAsXQuery(source.text(), source.staticBaseUri(), configuration);
        CharStream stream = CharStreams.fromString(source.text());
        Lexer lexer = useXQuery ? new XQueryLexer(stream) : new JsoniqLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);

        try {
            if (useXQuery) {
                XQueryParser parser = new XQueryParser(tokens);
                parser.setErrorHandler(new BailErrorStrategy());
                XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                        moduleContext, true, compilationConfiguration, externalBindings, source, tokens);
                XQueryParser.ModuleContext module = parser.moduleAndThisIsIt().module();
                if (module == null) {
                    throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
                }
                return new ParsedMainModule((MainModule) visitor.visit(module), Language.XQUERY);
            } else {
                JsoniqParser parser = new JsoniqParser(tokens);
                parser.setErrorHandler(new BailErrorStrategy());
                JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
                        moduleContext, true, compilationConfiguration, externalBindings, source, tokens);
                JsoniqParser.ModuleContext module = parser.moduleAndThisIsIt().module();
                if (module == null) {
                    throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
                }
                debugPrintHeader(configuration, "Parsing program");
                return new ParsedMainModule((MainModule) visitor.visit(module), Language.JSONIQ);
            }
        } catch (ParseCancellationException exception) {
            throw parsingException(lexer, source, exception);
        }
    }

    public static LibraryModule parseLibraryModule(
            ModuleSource source,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        boolean useXQuery = shouldParseAsXQuery(source.text(), source.staticBaseUri(), configuration);
        CharStream stream = CharStreams.fromString(source.text());
        Lexer lexer = useXQuery ? new XQueryLexer(stream) : new JsoniqLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StaticContext moduleContext = new StaticContext(source.staticBaseUri(), configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
                importingModuleContext.getUserDefinedFunctionsExecutionModes());

        try {
            if (useXQuery) {
                XQueryParser parser = new XQueryParser(tokens);
                parser.setErrorHandler(new BailErrorStrategy());
                XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                        moduleContext, false, compilationConfiguration, ExternalBindings.empty(), source, tokens);
                // Preserve the existing XQuery library entry rule, which does not require EOF.
                return (LibraryModule) visitor.visit(parser.module());
            } else {
                JsoniqParser parser = new JsoniqParser(tokens);
                parser.setErrorHandler(new BailErrorStrategy());
                JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
                        moduleContext, false, compilationConfiguration, ExternalBindings.empty(), source, tokens);
                return (LibraryModule) visitor.visit(parser.moduleAndThisIsIt().module());
            }
        } catch (ParseCancellationException exception) {
            throw parsingException(lexer, source, exception);
        }
    }

    private static ParsingException parsingException(
            Lexer lexer, ModuleSource source, ParseCancellationException cause) {
        ParsingException exception = new ParsingException(
                lexer.getText(),
                ExceptionMetadata.fromPoint(
                        source.sourceUri().toString(), lexer.getLine(), lexer.getCharPositionInLine(), source.text()));
        exception.initCause(cause);
        return exception;
    }
}
