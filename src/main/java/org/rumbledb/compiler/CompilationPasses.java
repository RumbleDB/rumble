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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;

/** Stateless pass adapters. Every invocation constructs fresh visitors for its input tree. */
final class CompilationPasses {
    private CompilationPasses() {}

    /** Removes duplicate imported declarations before dependency ordering. */
    static <M extends Module> CompilationPass<M> pruneModules() {
        return analysis("Pruning modules", (module, context) -> new ModulePruningVisitor(context.configuration())
                .visit(module, null));
    }

    /** Collects declaration dependencies and orders initialization, rejecting illegal cycles. */
    static <M extends Module> CompilationPass<M> resolveDependencies() {
        return analysis(
                "Resolving dependencies",
                (module, context) -> new VariableDependenciesVisitor(context.configuration()).visit(module, null));
    }

    /** Annotates sequential behavior before function inlining uses it to determine eligibility. */
    static CompilationPass<MainModule> classifySequentialExpressions() {
        return analysis(
                "Populating sequential classifications",
                (module, context) -> new SequentialClassificationVisitor(module.getProlog())
                        .visit(module, new DescendentSequentialProperties(false, false)));
    }

    /** Lowers builtin partial applications before contexts and types are populated. */
    static CompilationPass<MainModule> rewriteBuiltinPartialApplications() {
        return new NamedPass<>("Builtin partial application rewrite", (module, context) ->
                (MainModule) new BuiltinPartialApplicationRewriteVisitor().visit(module, null));
    }

    /** Marks recursive functions before inlining uses the recursion annotations. */
    static CompilationPass<MainModule> analyzeFunctionDependencies() {
        return analysis(
                "Function dependencies", (module, context) -> new FunctionDependenciesVisitor().visit(module, null));
    }

    /** Requires recursion annotations and, in JSONiq, sequential classifications. */
    static CompilationPass<MainModule> inlineFunctions() {
        return new NamedPass<>("Function inlining", (module, context) ->
                (MainModule) new FunctionInliningVisitor().visit(module, null));
    }

    /** Marks eligible tail calls on the rewritten function bodies. */
    static CompilationPass<MainModule> optimizeTailCalls() {
        return new NamedPass<>("Tail call optimization", (module, context) ->
                (MainModule) new TailCallOptimizationVisitor().visit(module, null));
    }

    /** Detects field requirements and rewrites projections before context population. */
    static CompilationPass<MainModule> pushDownProjections() {
        return new NamedPass<>("Projection pushdown", (module, context) ->
                (MainModule) new ProjectionPushdownVisitor().visit(module, null));
    }

    /** Attaches lexical contexts to the tree produced by type-independent rewrites. */
    static <M extends Module> CompilationPass<M> populateStaticContext() {
        return analysis("Populating static context", (module, context) -> new StaticContextVisitor()
                .visit(module, module.getStaticContext()));
    }

    /** Annotates and validates simple/updating/vacuous expressions; rerun after later rewrites. */
    static <M extends Module> CompilationPass<M> classifyUpdatingExpressions() {
        return analysis(
                "Populating expression classifications", (module, context) -> new ExpressionClassificationVisitor()
                        .visit(module, ExpressionClassification.SIMPLE));
    }

    /** Requires static contexts and sequential/updating classifications. Does not replace the tree. */
    static CompilationPass<MainModule> verifyComposability() {
        return analysis("Verifying composability constraints", (module, context) -> new ComposabilityVisitor()
                .visit(module, null));
    }

    /** Requires populated static contexts and annotates expression sequence types. */
    static <M extends Module> CompilationPass<M> inferTypes() {
        return analysis("Inferring types", (module, context) -> new InferTypeVisitor(context.configuration())
                .visit(module, module.getStaticContext()));
    }

    /** Requires inferred types and contexts; creates typed expressions for general comparisons. */
    static CompilationPass<MainModule> normalizeComparisons() {
        return new NamedPass<>("Comparison normalization", (module, context) ->
                (MainModule) new ComparisonVisitor().visit(module, null));
    }

    /** Resolves modes on the final rewritten tree, including the resolver's internal repeated passes. */
    static <M extends Module> CompilationPass<M> resolveExecutionModes() {
        return analysis(
                "Populating execution modes",
                (module, context) ->
                        ExecutionModeResolver.resolve(module, context.configuration(), context.externalBindings()));
    }

    private static <M extends Module> CompilationPass<M> analysis(
            String name, BiConsumer<M, CompilationContext> action) {
        return new NamedPass<>(name, (module, context) -> {
            action.accept(module, context);
            return module;
        });
    }

    private record NamedPass<M extends Module>(String name, BiFunction<M, CompilationContext, M> action)
            implements CompilationPass<M> {
        @Override
        public M apply(M module, CompilationContext context) {
            return this.action.apply(module, context);
        }
    }
}
