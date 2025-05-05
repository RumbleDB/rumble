package org.rumbledb.compiler;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.io.IOUtils;
import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.config.RumbleRuntimeConfiguration;
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

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node node, RumbleRuntimeConfiguration conf) {
        RuntimeIterator result = new RuntimeIteratorVisitor(conf).visit(node, null);
        if (conf.isPrintIteratorTree()) {
            StringBuffer sb = new StringBuffer();
            result.print(sb, 0);
            System.err.println(sb);
        }
        return result;
    }

    private static void resolveDependencies(Node node, RumbleRuntimeConfiguration conf) {
        new VariableDependenciesVisitor(conf).visit(node, null);
    }

    private static void pruneModules(Node node, RumbleRuntimeConfiguration conf) {
        new ModulePruningVisitor(conf).visit(node, null);
    }

    private static void inferTypes(Module module, RumbleRuntimeConfiguration conf) {
        new InferTypeVisitor(conf).visit(module, module.getStaticContext());
        if (conf.printInferredTypes() || conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
    }

    private static MainModule applyTypeIndependentOptimizations(MainModule module, RumbleRuntimeConfiguration conf) {
        MainModule result = module;
        // Annotate recursive functions as such
        if (conf.isPrintIteratorTree()) {
            System.err.println("***************************************");
            System.err.println("Function dependencies visitor");
            System.err.println("***************************************");
        }
        new FunctionDependenciesVisitor().visit(result, null);
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
        // Inline non-recursive functions
        if (conf.functionInlining()) {
            if (conf.isPrintIteratorTree()) {
                System.err.println("***************************************");
                System.err.println("Function inlining");
                System.err.println("***************************************");
            }
            result = (MainModule) new FunctionInliningVisitor().visit(result, null);
            if (conf.isPrintIteratorTree()) {
                printTree(result, conf);
            }
        }
        if (conf.isPrintIteratorTree()) {
            System.err.println("***************************************");
            System.err.println("Projection pushdown");
            System.err.println("***************************************");
        }
        result = (MainModule) new ProjectionPushdownVisitor().visit(result, null);
        if (conf.isPrintIteratorTree()) {
            printTree(result, conf);
        }
        return result;
    }

    private static MainModule applyTypeDependentOptimizations(MainModule module) {
        MainModule result = module;
        result = (MainModule) new ComparisonVisitor().visit(result, null);
        return result;
    }

    private static void printTree(Module node, RumbleRuntimeConfiguration conf) {
        System.err.println("***************");
        System.err.println("Expression tree");
        System.err.println("***************");
        System.err.println("Unset execution modes: " + node.numberOfUnsetExecutionModes());
        System.err.println(node);
        System.err.println();
        System.err.println(node.getStaticContext());
    }

    public static MainModule parseMainModuleFromLocation(URI location, RumbleRuntimeConfiguration configuration)
            throws IOException {
        InputStream in = FileSystemUtil.getDataInputStream(location, configuration, ExceptionMetadata.EMPTY_METADATA);
        String query = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        if (configuration.getStaticBaseUri() != null) {
            location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                configuration.getStaticBaseUri(),
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        return parseMainModule(query, location, configuration);
    }

    public static LibraryModule parseLibraryModuleFromLocation(
            URI location,
            RumbleRuntimeConfiguration configuration,
            StaticContext importingModuleContext,
            ExceptionMetadata metadata
    )
            throws IOException {
        InputStream in = FileSystemUtil.getDataInputStream(location, configuration, metadata);
        String query = IOUtils.toString(in, StandardCharsets.UTF_8.name());
        if (configuration.getStaticBaseUri() != null) {
            location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                configuration.getStaticBaseUri(),
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        return parseLibraryModule(query, location, importingModuleContext, configuration);
    }

    public static MainModule parseMainModuleFromQuery(String query, RumbleRuntimeConfiguration configuration) {
        String url = ".";
        if (configuration.getStaticBaseUri() != null) {
            url = configuration.getStaticBaseUri();
        }
        URI location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            url,
            configuration,
            ExceptionMetadata.EMPTY_METADATA
        );
        return parseMainModule(query, location, configuration);
    }

    public static MainModule parseMainModule(String query, URI uri, RumbleRuntimeConfiguration configuration) {
        if (query.startsWith("xquery") || configuration.getQueryLanguage().equals("xquery31")) {
            return parseXQueryMainModule(query, uri, configuration);
        } else {
            // overwrite default version if query specifies jsoniq version
            if (query.startsWith("jsoniq version \"3.1\"")) {
                configuration.setQueryLanguage("jsoniq31");
            } else if (query.startsWith("jsoniq version \"1.0\"")) {
                configuration.setQueryLanguage("jsoniq10");
            }
            return parseJSONiqMainModule(query, uri, configuration);
        }

    }

    public static MainModule parseJSONiqMainModule(
            String query,
            URI uri,
            RumbleRuntimeConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(new UserDefinedFunctionExecutionModes());
        TranslationVisitor visitor = new TranslationVisitor(moduleContext, true, configuration, query);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleAndThisIsItContext module = parser.moduleAndThisIsIt();
            JsoniqParser.MainModuleContext main = module.module().main;
            if (main == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            if (configuration.isPrintIteratorTree()) {
                System.err.println("***************");
                System.err.println("Parsing program");
                System.err.println("***************");
            }
            MainModule mainModule = (MainModule) visitor.visit(main);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("***************");
                System.err.println("Pruning modules");
                System.err.println("***************");
            }
            pruneModules(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("**********************");
                System.err.println("Resolving dependencies");
                System.err.println("**********************");
            }
            resolveDependencies(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("*************************************");
                System.err.println("Populating sequential classifications");
                System.err.println("*************************************");
            }
            populateSequentialClassifications(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("***************************************");
                System.err.println("Applying type independent optimizations");
                System.err.println("***************************************");
            }
            mainModule = applyTypeIndependentOptimizations(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("*************************");
                System.err.println("Populating static context");
                System.err.println("*************************");
            }
            populateStaticContext(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("*************************************");
                System.err.println("Populating expression classifications");
                System.err.println("*************************************");
            }
            populateExpressionClassifications(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("**************");
                System.err.println("Infering types");
                System.err.println("**************");
            }
            inferTypes(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("************************");
                System.err.println("Applying type dependent optimizations");
                System.err.println("************************");
            }
            mainModule = applyTypeDependentOptimizations(mainModule);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("***************************************");
                System.err.println("Populating execution modes");
                System.err.println("***************************************");
            }
            populateExecutionModes(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("*************************************");
                System.err.println("Populating expression classifications");
                System.err.println("*************************************");
            }
            populateExpressionClassifications(mainModule, configuration);
            if (configuration.isPrintIteratorTree()) {
                System.err.println("********************************");
                System.err.println("Verify composability constraints");
                System.err.println("********************************");
            }
            verifyComposabilityConstraints(mainModule, configuration);
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
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
            RumbleRuntimeConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        XQueryLexer lexer = new XQueryLexer(stream);
        XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(new UserDefinedFunctionExecutionModes());
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(moduleContext, true, configuration, query);
        try {
            // TODO Handle module extras
            XQueryParser.ModuleAndThisIsItContext module = parser.moduleAndThisIsIt();
            XQueryParser.MainModuleContext main = module.module().main;
            if (main == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            MainModule mainModule = (MainModule) visitor.visit(main);
            pruneModules(mainModule, configuration);
            resolveDependencies(mainModule, configuration);
            populateStaticContext(mainModule, configuration);
            inferTypes(mainModule, configuration);
            mainModule = applyTypeDependentOptimizations(mainModule);
            populateExecutionModes(mainModule, configuration);
            // TODO populate expression classifications here?
            // populateExpressionClassifications(mainModule, configuration);
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
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
            RumbleRuntimeConfiguration configuration
    ) {
        if (query.startsWith("xquery") || configuration.getQueryLanguage().equals("xquery31")) {
            return parseXQueryLibraryModule(query, uri, importingModuleContext, configuration);
        } else {
            // overwrite default version if query specifies jsoniq version
            if (query.startsWith("jsoniq version \"3.1\"")) {
                configuration.setQueryLanguage("jsoniq31");
            } else if (query.startsWith("jsoniq version \"1.0\"")) {
                configuration.setQueryLanguage("jsoniq10");
            }
            return parseJSONiqLibraryModule(query, uri, importingModuleContext, configuration);
        }

    }

    public static LibraryModule parseJSONiqLibraryModule(
            String query,
            URI uri,
            StaticContext importingModuleContext,
            RumbleRuntimeConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(stream);
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
            importingModuleContext.getUserDefinedFunctionsExecutionModes()
        );
        TranslationVisitor visitor = new TranslationVisitor(moduleContext, false, configuration, query);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleAndThisIsItContext module = parser.moduleAndThisIsIt();
            JsoniqParser.LibraryModuleContext main = module.module().libraryModule();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            resolveDependencies(libraryModule, configuration);
            // no static context population, as this is done in a single shot via the importing main module.
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
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
            RumbleRuntimeConfiguration configuration
    ) {
        CharStream stream = CharStreams.fromString(query);
        XQueryLexer lexer = new XQueryLexer(stream);
        XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        StaticContext moduleContext = new StaticContext(uri, configuration);
        moduleContext.setUserDefinedFunctionsExecutionModes(
            importingModuleContext.getUserDefinedFunctionsExecutionModes()
        );
        XQueryTranslationVisitor visitor = new XQueryTranslationVisitor(moduleContext, false, configuration, query);
        try {
            // TODO Handle module extras
            XQueryParser.LibraryModuleContext main = parser.module().libraryModule();
            LibraryModule libraryModule = (LibraryModule) visitor.visit(main);
            resolveDependencies(libraryModule, configuration);
            // no static context population, as this is done in a single shot via the importing main module.
            return libraryModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
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

    private static void populateExecutionModes(Module module, RumbleRuntimeConfiguration conf) {
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
        if (!conf.parallelExecution()) {
            LocalExecutionModeVisitor visitor = new LocalExecutionModeVisitor(conf);
            visitor.visit(module, module.getStaticContext());
            if (conf.isPrintIteratorTree()) {
                printTree(module, conf);
            }
            if (module.numberOfUnsetExecutionModes() > 0) {
                System.err.println(
                    "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report."
                );
            }
            return;
        }
        ExecutionModeVisitor visitor = new ExecutionModeVisitor(conf);
        visitor.visit(module, module.getStaticContext());

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorIntermediatePassConfig);
        int prevUnsetCount = module.numberOfUnsetExecutionModes();
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
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
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
        if (module.numberOfUnsetExecutionModes() > 0) {
            System.err.println(
                "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report."
            );
        }
    }

    private static void populateStaticContext(Module module, RumbleRuntimeConfiguration conf) {
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(module, module.getStaticContext());

        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
    }

    private static void populateExpressionClassifications(Module module, RumbleRuntimeConfiguration conf) {
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }

        ExpressionClassificationVisitor visitor = new ExpressionClassificationVisitor();
        visitor.visit(module, ExpressionClassification.SIMPLE);

        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
    }

    private static void populateSequentialClassifications(
            MainModule mainModule,
            RumbleRuntimeConfiguration configuration
    ) {
        if (configuration.isPrintIteratorTree()) {
            printTree(mainModule, configuration);
        }

        SequentialClassificationVisitor visitor = new SequentialClassificationVisitor(mainModule.getProlog());
        visitor.visit(mainModule, new DescendentSequentialProperties(false, false));

        if (configuration.isPrintIteratorTree()) {
            printTree(mainModule, configuration);
        }
    }


    private static void verifyComposabilityConstraints(
            MainModule mainModule,
            RumbleRuntimeConfiguration configuration
    ) {
        if (configuration.isPrintIteratorTree()) {
            printTree(mainModule, configuration);
        }

        ComposabilityVisitor visitor = new ComposabilityVisitor();
        visitor.visit(mainModule, null);

        if (configuration.isPrintIteratorTree()) {
            printTree(mainModule, configuration);
        }
    }

    public static DynamicContext createDynamicContext(Node node, RumbleRuntimeConfiguration configuration) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration);
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
