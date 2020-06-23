package org.rumbledb.compiler;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.parser.JsoniqLexer;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.Functions;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.jsoniq.ExecutionMode;

public class VisitorHelpers {

    public static RuntimeIterator generateRuntimeIterator(Node node) {
        return new RuntimeIteratorVisitor().visit(node, null);
    }

    private static void resolveDependencies(Node node, RumbleRuntimeConfiguration conf) {
        new VariableDependenciesVisitor(conf).visit(node, null);
    }

    private static void printTree(Node node, RumbleRuntimeConfiguration conf) {
        System.out.println("***********************");
        System.out.println("Initial expression tree");
        System.out.println("***********************");
        System.out.println("Unset execution modes: " + node.numberOfUnsetExecutionModes());
        System.out.println(node);
        System.out.println();
    }

    public static MainModule parseMainModuleFromLocation(URI location, RumbleRuntimeConfiguration configuration)
            throws IOException {
        FSDataInputStream in = FileSystemUtil.getDataInputStream(location, ExceptionMetadata.EMPTY_METADATA);
        return parseMainModule(CharStreams.fromStream(in), location, configuration);
    }

    public static LibraryModule parseLibraryModuleFromLocation(
            URI location,
            RumbleRuntimeConfiguration configuration,
            ExceptionMetadata metadata
    )
            throws IOException {
        FSDataInputStream in = FileSystemUtil.getDataInputStream(location, metadata);
        return parseLibraryModule(CharStreams.fromStream(in), location, configuration);
    }

    public static MainModule parseMainModuleFromQuery(String query, RumbleRuntimeConfiguration configuration) {
        URI location = FileSystemUtil.resolveURIAgainstWorkingDirectory(".", ExceptionMetadata.EMPTY_METADATA);
        return parseMainModule(CharStreams.fromString(query), location, configuration);
    }

    public static MainModule parseMainModule(CharStream stream, URI uri, RumbleRuntimeConfiguration configuration) {
        JsoniqLexer lexer = new JsoniqLexer(stream);
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        TranslationVisitor visitor = new TranslationVisitor(uri, configuration);
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleAndThisIsItContext module = parser.moduleAndThisIsIt();
            JsoniqParser.MainModuleContext main = module.module().main;
            if (main == null) {
                throw new ParsingException("A library module is not executable.", ExceptionMetadata.EMPTY_METADATA);
            }
            MainModule mainModule = (MainModule) visitor.visit(main);
            resolveDependencies(mainModule, configuration);
            populateStaticContext(mainModule, configuration);
            return mainModule;
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
                            uri.toString(),
                            lexer.getLine(),
                            lexer.getCharPositionInLine()
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    public static LibraryModule parseLibraryModule(
            CharStream stream,
            URI uri,
            RumbleRuntimeConfiguration configuration
    ) {
        JsoniqLexer lexer = new JsoniqLexer(stream);
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        TranslationVisitor visitor = new TranslationVisitor(uri, configuration);
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
                            lexer.getCharPositionInLine()
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    private static void populateStaticContext(Module module, RumbleRuntimeConfiguration conf) {
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }
        StaticContextVisitor visitor = new StaticContextVisitor();
        visitor.visit(module, module.getStaticContext());


        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorIntermediatePassConfig);
        int prevUnsetCount = Functions.getUserDefinedFunctionIdentifiersWithUnsetExecutionModes().size();
        if (conf.isPrintIteratorTree()) {
            printTree(module, conf);
        }

        while (true) {
            visitor.visit(module, module.getStaticContext());
            int currentUnsetCount = Functions.getUserDefinedFunctionIdentifiersWithUnsetExecutionModes().size();

            if (conf.isPrintIteratorTree()) {
                printTree(module, conf);
            }

            if (currentUnsetCount > prevUnsetCount) {
                throw new OurBadException(
                        "Unexpected program state reached while performing multi-pass over StaticContext."
                );
            }
            if (currentUnsetCount == prevUnsetCount) {
                setLocalExecutionForUnsetUserDefinedFunctions();
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
                "Warning! Some execution modes could not be set. The query may still work, but we would welcome a bug report."
            );
        }

    }

    public static DynamicContext createDynamicContext(Node node, RumbleRuntimeConfiguration configuration) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration);
        return visitor.visit(node, null);
    }

    private static void setLocalExecutionForUnsetUserDefinedFunctions() {
        try {
            List<FunctionIdentifier> unsetFunctionIdentifiers = new ArrayList<>();
            unsetFunctionIdentifiers.addAll(
                Functions
                    .getUserDefinedFunctionIdentifiersWithUnsetExecutionModes()
            );
            for (
                FunctionIdentifier functionIdentifier : unsetFunctionIdentifiers
            ) {
                Functions.addUserDefinedFunctionExecutionMode(
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
