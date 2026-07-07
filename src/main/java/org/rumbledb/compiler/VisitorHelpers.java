package org.rumbledb.compiler;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.io.IOUtils;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.parser.jsoniq.JsoniqLexer;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryLexer;
import org.rumbledb.parser.xquery.XQueryParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node node, RumbleConfiguration conf) {
        RuntimeIterator result = new RuntimeIteratorVisitor(conf).visit(node, null);
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            StringBuilder sb = new StringBuilder();
            result.print(sb, 0);
            System.err.println(sb);
        }
        return result;
    }

    public static RumbleConfiguration getEffectiveConfiguration(
            Node node,
            RumbleConfiguration.RumbleConfigurationBuilder builder
    ) {
        return new EffectiveConfigurationVisitor().getEffectiveConfiguration(node, builder);
    }

    private static void resolveDependencies(Node node, RumbleConfiguration conf) {
        new VariableDependenciesVisitor(conf).visit(node, null);
    }

    private static void pruneModules(Node node, RumbleConfiguration conf) {
        new ModulePruningVisitor(conf).visit(node, null);
    }

    private static void inferTypes(Module module, RumbleConfiguration conf) {
        new InferTypeVisitor(conf).visit(module, module.getStaticContext());
        debugPrintTree(module, conf);
    }

    private static MainModule applyTypeIndependentOptimizations(MainModule module, RumbleConfiguration conf) {
        MainModule result = module;

        debugPrintHeader(conf, "Builtin Partial Application Rewrite Visitor");
        result = (MainModule) new BuiltinPartialApplicationRewriteVisitor().visit(result, null);
        debugPrintTree(result, conf);

        // Annotate recursive functions as such
        debugPrintHeader(conf, "Function dependencies visitor");
        new FunctionDependenciesVisitor().visit(result, null);
        debugPrintTree(module, conf);

        // Inline non-recursive functions
        if (conf.optimization().useFunctionInlining()) {
            debugPrintHeader(conf, "Function inlining");
            result = (MainModule) new FunctionInliningVisitor().visit(result, null);
            debugPrintTree(result, conf);
        }

        // Apply tail call optimization
        if (conf.optimization().useTailCallOptimization()) {
            debugPrintHeader(conf, "Tail call optimization");
            result = (MainModule) new TailCallOptimizationVisitor().visit(result, null);
            debugPrintTree(result, conf);
        }

        debugPrintHeader(conf, "Projection pushdown");
        result = (MainModule) new ProjectionPushdownVisitor().visit(result, null);
        debugPrintTree(result, conf);

        return result;
    }

    private static MainModule applyTypeDependentOptimizations(MainModule module) {
        MainModule result = module;
        result = (MainModule) new ComparisonVisitor().visit(result, null);
        return result;
    }

    /**
     * Utility function to print the tree of iterators if the flag is set in the configuration. This is useful for
     * debugging purposes.
     * 
     * @param node the root node of the tree to print
     * @param conf the configuration object
     */
    private static void debugPrintTree(Module node, RumbleConfiguration conf) {
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            System.err.println("***************");
            System.err.println("Expression tree");
            System.err.println("***************");
            System.err.println("Unset execution modes: " + node.numberOfUnsetExecutionModes());
            System.err.println(node);
            System.err.println();
            System.err.println(node.getStaticContext());
        }
    }

    private static void debugPrintHeader(RumbleConfiguration conf, String header) {
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            System.err.println("*".repeat(header.length()));
            System.err.println(header);
            System.err.println("*".repeat(header.length()));
        }
    }

    public static MainModule parseMainModuleFromLocation(
            URI location,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    )
            throws IOException {
        InputStream in = FileSystemUtil.getDataInputStream(location, configuration, ExceptionMetadata.EMPTY_METADATA);
        String query = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        if (configuration.semantics().staticBaseUri() != null) {
            location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                configuration.semantics().staticBaseUri(),
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        return parseMainModule(query, location, configuration, externalBindings);
    }

    public static LibraryModule parseLibraryModuleFromLocation(
            URI location,
            RumbleConfiguration configuration,
            StaticContext importingModuleContext,
            ExceptionMetadata metadata
    )
            throws IOException {
        InputStream in = FileSystemUtil.getDataInputStream(location, configuration, metadata);
        String query = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        if (configuration.semantics().staticBaseUri() != null) {
            location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                configuration.semantics().staticBaseUri(),
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        return parseLibraryModule(query, location, importingModuleContext, configuration);
    }

    public static MainModule parseMainModuleFromQuery(
            String query,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    ) {
        String url = Objects.requireNonNullElse(configuration.semantics().staticBaseUri(), ".");
        URI location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            url,
            configuration,
            ExceptionMetadata.EMPTY_METADATA
        );
        return parseMainModule(query, location, configuration, externalBindings);
    }

    public static MainModule parseMainModule(
            String query,
            URI uri,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    ) {
        if (query.contains("xquery version")) {
            return parseXQueryMainModule(query, uri, configuration, externalBindings);
        } else if (query.contains("jsoniq version")) {
            return parseJSONiqMainModule(query, uri, configuration, externalBindings);
        }
        if (uri.toString().endsWith(".xq") || uri.toString().endsWith(".xqy") || uri.toString().endsWith(".xquery")) {
            return parseXQueryMainModule(query, uri, configuration, externalBindings);
        }
        if (uri.toString().endsWith(".jq") || uri.toString().endsWith(".jsoniq")) {
            return parseJSONiqMainModule(query, uri, configuration, externalBindings);
        } else if (configuration.semantics().queryLanguage().startsWith("xquery")) {
            return parseXQueryMainModule(query, uri, configuration, externalBindings);
        } else {
            return parseJSONiqMainModule(query, uri, configuration, externalBindings);
        }
    }

    public static MainModule parseJSONiqMainModule(
            String query,
            URI uri,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    ) {
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        moduleContext.setUserDefinedFunctionsExecutionModes(executionModes);
        TranslationVisitor visitor = new TranslationVisitor(
                moduleContext,
                true,
                configuration,
                externalBindings,
                query,
                jsoniqTokens
        );
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext modulectx = parser.moduleAndThisIsIt().module();
            if (modulectx == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }

            debugPrintHeader(configuration, "Parsing program");
            MainModule mainModule = (MainModule) visitor.visit(modulectx);

            debugPrintHeader(configuration, "Pruning modules");
            pruneModules(mainModule, configuration);

            debugPrintHeader(configuration, "Resolving dependencies");
            resolveDependencies(mainModule, configuration);

            debugPrintHeader(configuration, "Populating sequential classifications");
            populateSequentialClassifications(mainModule, configuration);

            debugPrintHeader(configuration, "Applying type independent optimizations");
            mainModule = applyTypeIndependentOptimizations(mainModule, configuration);

            debugPrintHeader(configuration, "Populating static context");
            populateStaticContext(mainModule, configuration);

            debugPrintHeader(configuration, "Populating expression classifications");
            populateExpressionClassifications(mainModule, configuration);

            debugPrintHeader(configuration, "Verifying composability constraints");
            verifyComposabilityConstraints(mainModule, configuration);

            debugPrintHeader(configuration, "Infering types");
            inferTypes(mainModule, configuration);

            debugPrintHeader(configuration, "Applying type dependent optimizations");
            mainModule = applyTypeDependentOptimizations(mainModule);

            debugPrintHeader(configuration, "Populating execution modes");
            populateExecutionModes(mainModule, configuration, externalBindings);

            debugPrintHeader(configuration, "Populating expression classifications");
            populateExpressionClassifications(mainModule, configuration);

            debugPrintTree(mainModule, configuration);

            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                        uri.toString(),
                        lexer.getLine(),
                        lexer.getCharPositionInLine(),
                        query
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    public static MainModule parseXQueryMainModule(
            String query,
            URI uri,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    ) {
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
                moduleContext,
                true,
                configuration,
                externalBindings,
                query,
                xQueryTokens
        );
        try {
            // TODO Handle module extras
            XQueryParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            if (main == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            MainModule mainModule = (MainModule) visitor.visit(main);
            pruneModules(mainModule, configuration);
            resolveDependencies(mainModule, configuration);
            mainModule = applyTypeIndependentOptimizations(mainModule, configuration);
            populateStaticContext(mainModule, configuration);
            inferTypes(mainModule, configuration);
            mainModule = applyTypeDependentOptimizations(mainModule);
            populateExecutionModes(mainModule, configuration, externalBindings);
            // TODO populate expression classifications here?
            // populateExpressionClassifications(mainModule, configuration);
            if (configuration.debug().printIteratorTree()) {
                debugPrintTree(mainModule, configuration);
            }
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                        uri.toString(),
                        lexer.getLine(),
                        lexer.getCharPositionInLine(),
                        query
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    public static LibraryModule parseLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            RumbleConfiguration configuration
    ) {
        if (query.contains("xquery version")) {
            return parseXQueryLibraryModule(query, uri, importingModuleContext, configuration);
        } else if (query.contains("jsoniq version")) {
            return parseJSONiqLibraryModule(query, uri, importingModuleContext, configuration);
        }
        if (uri.toString().endsWith(".xq") || uri.toString().endsWith(".xqy") || uri.toString().endsWith(".xquery")) {
            return parseXQueryLibraryModule(query, uri, importingModuleContext, configuration);
        }
        if (uri.toString().endsWith(".jq") || uri.toString().endsWith(".jsoniq")) {
            return parseJSONiqLibraryModule(query, uri, importingModuleContext, configuration);
        } else if (configuration.semantics().queryLanguage().startsWith("xquery")) {
            return parseXQueryLibraryModule(query, uri, importingModuleContext, configuration);
        } else {
            return parseJSONiqLibraryModule(query, uri, importingModuleContext, configuration);
        }
    }

    public static LibraryModule parseJSONiqLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            RumbleConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        CommonTokenStream jsoniqTokens = new CommonTokenStream(lexer);
        JsoniqParser parser = new JsoniqParser(jsoniqTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
            importingModuleContext.getUserDefinedFunctionsExecutionModes()
        );
        TranslationVisitor visitor = new TranslationVisitor(
                moduleContext,
                false,
                configuration,
                ExternalBindings.empty(),
                query,
                jsoniqTokens
        );
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext main = parser.moduleAndThisIsIt().module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            resolveDependencies(libraryModule, configuration);
            // no static context population, as this is done in a single shot via the importing main module.
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                        uri.toString(),
                        lexer.getLine(),
                        lexer.getCharPositionInLine(),
                        query
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    public static LibraryModule parseXQueryLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            RumbleConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        XQueryLexer lexer = new XQueryLexer(stream);
        CommonTokenStream xQueryTokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(xQueryTokens);
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
            importingModuleContext.getUserDefinedFunctionsExecutionModes()
        );
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(
                moduleContext,
                false,
                configuration,
                ExternalBindings.empty(),
                query,
                xQueryTokens
        );
        try {
            // TODO Handle module extras
            XQueryParser.ModuleContext main = parser.module();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            resolveDependencies(libraryModule, configuration);
            // no static context population, as this is done in a single shot via the importing main module.
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    ExceptionMetadata.fromPoint(
                        uri.toString(),
                        lexer.getLine(),
                        lexer.getCharPositionInLine(),
                        query
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    private static void populateExecutionModes(
            Module module,
            RumbleConfiguration conf,
            ExternalBindings externalBindings
    ) {
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }
        if (!conf.runtime().useParallelExecution()) {
            LocalExecutionModeVisitor visitor = new LocalExecutionModeVisitor(conf);
            visitor.visit(module, module.getStaticContext());
            if (conf.debug().printIteratorTree()) {
                debugPrintTree(module, conf);
            }
            if (module.numberOfUnsetExecutionModes() > 0) {
                System.err.println(
                    "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report."
                );
            }
            return;
        }
        ExecutionModeVisitor visitor = new ExecutionModeVisitor(conf, externalBindings);
        visitor.visit(module, module.getStaticContext());

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorIntermediatePassConfig);
        int prevUnsetCount = module.numberOfUnsetExecutionModes();
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }

        while (true) {
            visitor.visit(module, module.getStaticContext());
            int currentUnsetCount = module.numberOfUnsetExecutionModes();

            if (currentUnsetCount == 0) {
                break;
            }

            /*
             * if (conf.isPrintIteratorTree()) {
             * printTree(module, conf);
             * }
             */

            if (currentUnsetCount > prevUnsetCount) {
                throw new OurBadException(
                        "Unexpected program state reached while performing multi-pass over StaticContext."
                );
            }
            if (currentUnsetCount == prevUnsetCount) {
                setLocalExecutionForUnsetUserDefinedFunctions(
                    module.getStaticContext().getUserDefinedFunctionsExecutionModes()
                );
                break;
            }
            prevUnsetCount = currentUnsetCount;
        }

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorFinalPassConfig);
        visitor.visit(module, module.getStaticContext());
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }
        if (module.numberOfUnsetExecutionModes() > 0) {
            System.err.println(
                "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report."
            );
        }
    }

    private static void populateStaticContext(Module module, RumbleConfiguration conf) {
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(module, module.getStaticContext());

        debugPrintTree(module, conf);
    }

    private static void populateExpressionClassifications(Module module, RumbleConfiguration conf) {
        debugPrintTree(module, conf);

        ExpressionClassificationVisitor visitor = new ExpressionClassificationVisitor();
        visitor.visit(module, ExpressionClassification.SIMPLE);

        debugPrintTree(module, conf);
    }

    private static void populateSequentialClassifications(
            MainModule mainModule,
            RumbleConfiguration configuration
    ) {
        debugPrintTree(mainModule, configuration);

        SequentialClassificationVisitor visitor = new SequentialClassificationVisitor(mainModule.getProlog());
        visitor.visit(mainModule, new DescendentSequentialProperties(false, false));

        debugPrintTree(mainModule, configuration);
    }


    private static void verifyComposabilityConstraints(
            MainModule mainModule,
            RumbleConfiguration configuration
    ) {
        debugPrintTree(mainModule, configuration);

        ComposabilityVisitor visitor = new ComposabilityVisitor();
        visitor.visit(mainModule, null);

        debugPrintTree(mainModule, configuration);
    }

    public static DynamicContext createDynamicContext(Node node, RumbleConfiguration configuration) {
        return createDynamicContext(node, configuration, ExternalBindings.empty());
    }

    public static DynamicContext createDynamicContext(
            Node node,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings
    ) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration, externalBindings);
        return visitor.visit(node, null);
    }

    private static void setLocalExecutionForUnsetUserDefinedFunctions(
            UserDefinedFunctionExecutionModes userDefinedFunctionExecutionModes
    ) {
        try {
            List<FunctionIdentifier> unsetFunctionIdentifiers = new ArrayList<>();
            unsetFunctionIdentifiers.addAll(
                userDefinedFunctionExecutionModes
                    .getUserDefinedFunctionIdentifiersWithUnsetExecutionModes()
            );
            for (
                FunctionIdentifier functionIdentifier : unsetFunctionIdentifiers
            ) {
                userDefinedFunctionExecutionModes.setExecutionMode(
                    functionIdentifier,
                    ExecutionMode.LOCAL,
                    true,
                    null
                );
            }
        } catch (DuplicateFunctionIdentifierException e) {
            throw new OurBadException(
                    "Unexpected program state reached while setting local execution for unset user defined functions."
            );
        }
    }
}
