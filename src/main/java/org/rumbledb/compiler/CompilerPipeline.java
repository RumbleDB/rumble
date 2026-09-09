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
import org.rumbledb.compiler.analysis.ComposabilityVisitor;
import org.rumbledb.compiler.analysis.DescendentSequentialProperties;
import org.rumbledb.compiler.analysis.ExpressionClassificationVisitor;
import org.rumbledb.compiler.analysis.InferTypeVisitor;
import org.rumbledb.compiler.analysis.SequentialClassificationVisitor;
import org.rumbledb.compiler.analysis.StaticContextVisitor;
import org.rumbledb.compiler.analysis.VariableDependenciesVisitor;
import org.rumbledb.compiler.backend.ExecutionModeInference;
import org.rumbledb.compiler.frontend.ModuleParser;
import org.rumbledb.compiler.optimization.FunctionDependenciesVisitor;
import org.rumbledb.compiler.optimization.FunctionInliningVisitor;
import org.rumbledb.compiler.optimization.ModulePruningVisitor;
import org.rumbledb.compiler.optimization.ProjectionPushdownVisitor;
import org.rumbledb.compiler.optimization.TailCallOptimizationVisitor;
import org.rumbledb.compiler.rewriting.BuiltinPartialApplicationRewriteVisitor;
import org.rumbledb.compiler.rewriting.ComparisonRewriteVisitor;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;

import static org.rumbledb.compiler.CompilerDiagnostics.debugPrintHeader;
import static org.rumbledb.compiler.CompilerDiagnostics.debugPrintTree;

/** Runs the language-specific compiler passes in their established order. */
public final class CompilerPipeline {
    private CompilerPipeline() {}

    public static MainModule compileMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings)
            throws IOException {
        return compile(
                ModuleParser.parseMainModuleFromLocation(location, compilationConfiguration, externalBindings),
                compilationConfiguration.runtimeConfiguration(),
                externalBindings);
    }

    public static MainModule compileMainModuleFromQuery(
            String query, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        return compile(
                ModuleParser.parseMainModuleFromQuery(query, compilationConfiguration, externalBindings),
                compilationConfiguration.runtimeConfiguration(),
                externalBindings);
    }

    public static MainModule compileMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        return compile(
                ModuleParser.parseMainModule(query, uri, compilationConfiguration, externalBindings),
                compilationConfiguration.runtimeConfiguration(),
                externalBindings);
    }

    private static MainModule compile(
            ModuleParser.ParsedMainModule parsed,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings) {
        return parsed.xquery()
                ? compileXQuery(parsed.module(), configuration, externalBindings)
                : compileJsoniq(parsed.module(), configuration, externalBindings);
    }

    public static LibraryModule compileLibraryModuleFromQuery(
            String query, URI uri, CompilationConfiguration compilationConfiguration) {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        StaticContext importingModuleContext = new StaticContext(uri, configuration);
        UserDefinedFunctionExecutionModes executionModes = new UserDefinedFunctionExecutionModes();
        executionModes.setQueryLanguage(configuration.semantics().queryLanguage());
        importingModuleContext.setUserDefinedFunctionsExecutionModes(executionModes);

        LibraryModule libraryModule =
                ModuleParser.parseLibraryModule(query, uri, importingModuleContext, compilationConfiguration);
        populateStaticContext(libraryModule, configuration);
        inferTypes(libraryModule, configuration);
        return libraryModule;
    }

    private static MainModule compileJsoniq(
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
        ExecutionModeInference.populateExecutionModes(mainModule, configuration, externalBindings);

        debugPrintHeader(configuration, "Populating expression classifications");
        populateExpressionClassifications(mainModule, configuration);

        debugPrintTree(mainModule, configuration);

        return mainModule;
    }

    private static MainModule compileXQuery(
            MainModule mainModule, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        pruneModules(mainModule, configuration);
        resolveDependencies(mainModule, configuration);
        mainModule = applyTypeIndependentOptimizations(mainModule, configuration);
        populateStaticContext(mainModule, configuration);
        inferTypes(mainModule, configuration);
        mainModule = applyTypeDependentOptimizations(mainModule);
        ExecutionModeInference.populateExecutionModes(mainModule, configuration, externalBindings);
        // TODO populate expression classifications here?
        // populateExpressionClassifications(mainModule, configuration);
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
        result = (MainModule) new ComparisonRewriteVisitor().visit(result, null);
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
