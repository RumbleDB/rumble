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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.io.IOUtils;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.analysis.VariableDependenciesVisitor;
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
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import static org.rumbledb.compiler.CompilerDiagnostics.debugPrintHeader;

/** Loads and translates modules. Imported libraries also receive dependency resolution, as before. */
public final class ModuleParser {
    private ModuleParser() {}

    /** The AST and the language selected from the source, URI, and configuration. */
    public record ParsedMainModule(MainModule module, boolean xquery) {}

    private record ModuleSource(String query, URI systemId) {}

    private static URI resolveStaticBaseUri(String url) {
        URI resolved = FileSystemUtil.resolveURIAgainstWorkingDirectory(url, ExceptionMetadata.EMPTY_METADATA);
        if (url != null && url.endsWith("/") && !resolved.toString().endsWith("/")) {
            resolved = URI.create(resolved + "/");
        }
        return resolved;
    }

    private static ModuleSource readModuleSource(
            URI location, CompilationConfiguration compilationConfiguration, ExceptionMetadata metadata)
            throws IOException {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        try (ResolvedResource resource =
                compilationConfiguration.resourceResolver().resolve(location, configuration, metadata)) {
            String query = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8.name());
            URI systemId = resource.getSystemId();
            if (configuration.semantics().staticBaseUri() != null) {
                systemId = resolveStaticBaseUri(configuration.semantics().staticBaseUri());
            }
            return new ModuleSource(query, systemId);
        }
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

    public static ParsedMainModule parseMainModuleFromLocation(URI location, RumbleConfiguration configuration)
            throws IOException {
        return parseMainModuleFromLocation(
                location, new CompilationConfiguration(configuration), ExternalBindings.empty());
    }

    public static ParsedMainModule parseMainModuleFromLocation(
            URI location, RumbleConfiguration configuration, ExternalBindings externalBindings) throws IOException {
        return parseMainModuleFromLocation(location, new CompilationConfiguration(configuration), externalBindings);
    }

    public static ParsedMainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration) throws IOException {
        return parseMainModuleFromLocation(location, compilationConfiguration, ExternalBindings.empty());
    }

    public static ParsedMainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings)
            throws IOException {
        ModuleSource source = readModuleSource(location, compilationConfiguration, ExceptionMetadata.EMPTY_METADATA);
        return parseMainModule(source.query(), source.systemId(), compilationConfiguration, externalBindings);
    }

    static LibraryModule parseLibraryModuleFromLocation(
            URI location,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata)
            throws IOException {
        ModuleSource source = readModuleSource(location, compilationConfiguration, metadata);
        return parseLibraryModule(source.query(), source.systemId(), importingModuleContext, compilationConfiguration);
    }

    public static ParsedMainModule parseMainModuleFromQuery(
            String query, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return parseMainModuleFromQuery(query, new CompilationConfiguration(configuration), externalBindings);
    }

    public static ParsedMainModule parseMainModuleFromQuery(
            String query, CompilationConfiguration compilationConfiguration) {
        return parseMainModuleFromQuery(query, compilationConfiguration, ExternalBindings.empty());
    }

    public static ParsedMainModule parseMainModuleFromQuery(
            String query, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        String url = ".";
        if (configuration.semantics().staticBaseUri() != null) {
            url = configuration.semantics().staticBaseUri();
        }
        URI location = resolveStaticBaseUri(url);
        return parseMainModule(query, location, compilationConfiguration, externalBindings);
    }

    public static ParsedMainModule parseMainModule(
            String query, URI uri, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return parseMainModule(query, uri, new CompilationConfiguration(configuration), externalBindings);
    }

    public static ParsedMainModule parseMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        if (shouldParseAsXQuery(query, uri, configuration)) {
            return new ParsedMainModule(
                    parseXQueryMainModule(query, uri, compilationConfiguration, externalBindings), true);
        }
        return new ParsedMainModule(
                parseJSONiqMainModule(query, uri, compilationConfiguration, externalBindings), false);
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
        StaticContext moduleContext = new StaticContext(uri, configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);
        JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
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
        StaticContext moduleContext = new StaticContext(uri, configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);
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

    public static LibraryModule parseLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        if (shouldParseAsXQuery(query, uri, configuration)) {
            return parseXQueryLibraryModule(query, uri, importingModuleContext, compilationConfiguration);
        }
        return parseJSONiqLibraryModule(query, uri, importingModuleContext, compilationConfiguration);
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
        JsoniqTranslationVisitor visitor = new JsoniqTranslationVisitor(
                moduleContext, false, compilationConfiguration, ExternalBindings.empty(), query, jsoniqTokens);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            new VariableDependenciesVisitor(configuration).visit(libraryModule, null);
            // no static context population, as this is done in a single shot via the importing main module.
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
            new VariableDependenciesVisitor(configuration).visit(libraryModule, null);
            // no static context population, as this is done in a single shot via the importing main module.
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
