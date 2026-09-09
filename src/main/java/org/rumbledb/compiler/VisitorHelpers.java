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
import org.rumbledb.compiler.backend.RuntimePlanBuilder;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/** Compatibility facade. New callers should use CompilerPipeline or RuntimePlanBuilder. */
public class VisitorHelpers {

    public static ItemRuntimePlan generateRuntimeIterator(Node node, RumbleConfiguration conf) {
        return RuntimePlanBuilder.generateRuntimeIterator(node, conf);
    }

    public static RumbleConfiguration getEffectiveConfiguration(
            Node node, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        return RuntimePlanBuilder.getEffectiveConfiguration(node, builder);
    }

    public static MainModule parseMainModuleFromLocation(URI location, RumbleConfiguration configuration)
            throws IOException {
        return CompilerPipeline.compileMainModuleFromLocation(
                location, new CompilationConfiguration(configuration), ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, RumbleConfiguration configuration, ExternalBindings externalBindings) throws IOException {
        return CompilerPipeline.compileMainModuleFromLocation(
                location, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration) throws IOException {
        return CompilerPipeline.compileMainModuleFromLocation(
                location, compilationConfiguration, ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings)
            throws IOException {
        return CompilerPipeline.compileMainModuleFromLocation(location, compilationConfiguration, externalBindings);
    }

    public static MainModule parseMainModuleFromQuery(
            String query, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return CompilerPipeline.compileMainModuleFromQuery(
                query, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModuleFromQuery(String query, CompilationConfiguration compilationConfiguration) {
        return CompilerPipeline.compileMainModuleFromQuery(query, compilationConfiguration, ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromQuery(
            String query, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        return CompilerPipeline.compileMainModuleFromQuery(query, compilationConfiguration, externalBindings);
    }

    public static MainModule parseMainModule(
            String query, URI uri, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return CompilerPipeline.compileMainModule(
                query, uri, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        return CompilerPipeline.compileMainModule(query, uri, compilationConfiguration, externalBindings);
    }

    public static LibraryModule parseLibraryModuleFromQueryWithStaticContextAndInference(
            String query, URI uri, RumbleConfiguration configuration) {
        return CompilerPipeline.compileLibraryModuleFromQuery(query, uri, new CompilationConfiguration(configuration));
    }

    public static DynamicContext createDynamicContext(Node node, RumbleConfiguration configuration) {
        return RuntimePlanBuilder.createDynamicContext(node, configuration);
    }

    public static DynamicContext createDynamicContext(
            Node node, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return RuntimePlanBuilder.createDynamicContext(node, configuration, externalBindings);
    }
}
