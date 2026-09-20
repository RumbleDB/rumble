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

import java.io.IOException;
import java.net.URI;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;

import static org.rumbledb.compiler.CompilationDiagnostics.debugPrintHeader;
import static org.rumbledb.compiler.CompilationDiagnostics.debugPrintTree;

/** Orders analysis and rewrite passes. Visitors mutate annotations and may replace the module tree. */
final class CompilationPipeline {

    private CompilationPipeline() {}

    static LibraryModule prepareLibraryModuleFromLocation(
            URI location,
            StaticContext importingContext,
            CompilationConfiguration configuration,
            ExceptionMetadata metadata)
            throws IOException {
        ModuleSourceLoader.ModuleSource source = ModuleSourceLoader.readModuleSource(location, configuration, metadata);
        return prepareLibraryModule(source.query(), source.systemId(), importingContext, configuration);
    }

    static MainModule compileMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        ModuleParser.Language language = ModuleParser.detectLanguage(query, uri, configuration);
        MainModule module =
                ModuleParser.parseMainModule(query, uri, compilationConfiguration, externalBindings, language);
        return language == ModuleParser.Language.XQUERY
                ? compileXQuery(module, configuration, externalBindings)
                : compileJSONiq(module, configuration, externalBindings);
    }

    /**
     * Imported modules have their declarations ordered now. Context population and type inference
     * happen later through the importing main module, using the shared function execution-mode registry.
     */
    static LibraryModule prepareLibraryModule(
            String query, URI uri, StaticContext importingContext, CompilationConfiguration configuration) {
        LibraryModule module = ModuleParser.parseLibraryModule(query, uri, importingContext, configuration);
        resolveDependencies(module, configuration.runtimeConfiguration());
        return module;
    }

    /** Standalone library analysis for language-server callers; deliberately does not run the main-module pipeline. */
    static LibraryModule analyzeLibraryModule(String query, URI uri, RumbleConfiguration configuration) {
        StaticContext importingContext = ModuleParser.createModuleContext(uri, configuration);
        LibraryModule module =
                prepareLibraryModule(query, uri, importingContext, new CompilationConfiguration(configuration));
        populateStaticContext(module, configuration);
        inferTypes(module, configuration);
        return module;
    }

    /**
     * Dependency ordering and sequential classification precede inlining. Rewrites run before context
     * population, then updating classification enables composability checks. Comparison normalization
     * requires inferred types; execution modes and updating classifications are populated on its output.
     */
    private static MainModule compileJSONiq(
            MainModule mainModule, RumbleConfiguration configuration, ExternalBindings externalBindings) {
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
        ExecutionModeResolver.resolve(mainModule, configuration, externalBindings);

        debugPrintHeader(configuration, "Populating expression classifications");
        populateExpressionClassifications(mainModule, configuration);

        debugPrintTree(mainModule, configuration);

        return mainModule;
    }

    /**
     * Preserves the XQuery pipeline: sequential/updating classification and composability checks are
     * not currently run for this language. Adding them is a semantic change, not part of orchestration.
     */
    private static MainModule compileXQuery(
            MainModule mainModule, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        pruneModules(mainModule, configuration);
        resolveDependencies(mainModule, configuration);
        mainModule = applyTypeIndependentOptimizations(mainModule, configuration);
        populateStaticContext(mainModule, configuration);
        inferTypes(mainModule, configuration);
        mainModule = applyTypeDependentOptimizations(mainModule);
        ExecutionModeResolver.resolve(mainModule, configuration, externalBindings);
        if (configuration.debug().printIteratorTree()) {
            debugPrintTree(mainModule, configuration);
        }
        return mainModule;
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

    private static void populateSequentialClassifications(MainModule mainModule, RumbleConfiguration configuration) {
        debugPrintTree(mainModule, configuration);

        SequentialClassificationVisitor visitor = new SequentialClassificationVisitor(mainModule.getProlog());
        visitor.visit(mainModule, new DescendentSequentialProperties(false, false));

        debugPrintTree(mainModule, configuration);
    }

    private static void verifyComposabilityConstraints(MainModule mainModule, RumbleConfiguration configuration) {
        debugPrintTree(mainModule, configuration);

        ComposabilityVisitor visitor = new ComposabilityVisitor();
        visitor.visit(mainModule, null);

        debugPrintTree(mainModule, configuration);
    }
}
