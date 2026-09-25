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
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
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
        CompilationContext context = new CompilationContext(configuration, externalBindings);
        List<CompilationPass<MainModule>> passes = mainModulePasses(language, configuration);
        return run(module, context, passes);
    }

    /**
     * Imported modules have their declarations ordered now. Context population and type inference
     * happen later through the importing main module, using the shared function execution-mode registry.
     */
    static LibraryModule prepareLibraryModule(
            String query, URI uri, StaticContext importingContext, CompilationConfiguration configuration) {
        LibraryModule module = ModuleParser.parseLibraryModule(query, uri, importingContext, configuration);
        return run(
                module,
                new CompilationContext(configuration.runtimeConfiguration(), ExternalBindings.empty()),
                List.of(CompilationPasses.resolveDependencies()));
    }

    /** Standalone library analysis for language-server callers; deliberately does not run the main-module pipeline. */
    static LibraryModule analyzeLibraryModule(String query, URI uri, RumbleConfiguration configuration) {
        StaticContext importingContext = ModuleParser.createModuleContext(uri, configuration);
        LibraryModule module =
                prepareLibraryModule(query, uri, importingContext, new CompilationConfiguration(configuration));
        return run(
                module,
                new CompilationContext(configuration, ExternalBindings.empty()),
                List.of(CompilationPasses.populateStaticContext(), CompilationPasses.inferTypes()));
    }

    /**
     * Dependency ordering and sequential classification precede inlining. Context population and
     * updating classification enable composability checks. Comparison normalization requires types;
     * execution modes and updating classifications are populated on its output.
     * XQuery retains its existing omission of sequential/updating classification and composability checks.
     */
    private static List<CompilationPass<MainModule>> mainModulePasses(
            ModuleParser.Language language, RumbleConfiguration configuration) {
        boolean isJSONiq = language == ModuleParser.Language.JSONIQ;
        List<CompilationPass<MainModule>> passes = new ArrayList<>();
        passes.add(CompilationPasses.pruneModules());
        passes.add(CompilationPasses.resolveDependencies());
        if (isJSONiq) {
            passes.add(CompilationPasses.classifySequentialExpressions());
        }
        addTypeIndependentOptimizations(passes, configuration);
        passes.add(CompilationPasses.populateStaticContext());
        if (isJSONiq) {
            passes.add(CompilationPasses.classifyUpdatingExpressions());
            passes.add(CompilationPasses.verifyComposability());
        }
        passes.add(CompilationPasses.inferTypes());
        passes.add(CompilationPasses.normalizeComparisons());
        passes.add(CompilationPasses.resolveExecutionModes());
        if (isJSONiq) {
            passes.add(CompilationPasses.classifyUpdatingExpressions());
        }
        return List.copyOf(passes);
    }

    private static void addTypeIndependentOptimizations(
            List<CompilationPass<MainModule>> passes, RumbleConfiguration configuration) {
        passes.add(CompilationPasses.rewriteBuiltinPartialApplications());
        passes.add(CompilationPasses.analyzeFunctionDependencies());
        if (configuration.optimization().useFunctionInlining()) {
            passes.add(CompilationPasses.inlineFunctions());
        }
        if (configuration.optimization().useTailCallOptimization()) {
            passes.add(CompilationPasses.optimizeTailCalls());
        }
        passes.add(CompilationPasses.pushDownProjections());
    }

    private static <M extends Module> M run(M module, CompilationContext context, List<CompilationPass<M>> passes) {
        for (CompilationPass<M> pass : passes) {
            debugPrintHeader(context.configuration(), pass.name());
            module = pass.apply(module, context);
            debugPrintTree(module, context.configuration());
        }
        return module;
    }
}
