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

import lombok.extern.log4j.Log4j2;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/** Compatibility entry points for compilation, runtime planning, and context initialization. */
@Log4j2
public class VisitorHelpers {

    public static ItemRuntimePlan generateRuntimeIterator(Node node, RumbleConfiguration conf) {
        ItemRuntimePlan result = new RuntimeIteratorVisitor(conf).visit(node, null);
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            StringBuilder sb = new StringBuilder();
            result.print(sb, 0);
            log.debug(sb);
        }
        return result;
    }

    public static RumbleConfiguration getEffectiveConfiguration(
            Node node, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        return new EffectiveConfigurationVisitor().getEffectiveConfiguration(node, builder);
    }

    public static MainModule parseMainModuleFromLocation(URI location, RumbleConfiguration configuration)
            throws IOException {
        return parseMainModuleFromLocation(
                location, new CompilationConfiguration(configuration), ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, RumbleConfiguration configuration, ExternalBindings externalBindings) throws IOException {
        return parseMainModuleFromLocation(location, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration) throws IOException {
        return parseMainModuleFromLocation(location, compilationConfiguration, ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromLocation(
            URI location, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings)
            throws IOException {
        ModuleSourceLoader.ModuleSource source = ModuleSourceLoader.readModuleSource(
                location, compilationConfiguration, ExceptionMetadata.EMPTY_METADATA);
        return parseMainModule(source.query(), source.systemId(), compilationConfiguration, externalBindings);
    }

    static LibraryModule parseLibraryModuleFromLocation(
            URI location,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata)
            throws IOException {
        return CompilationPipeline.prepareLibraryModuleFromLocation(
                location, importingModuleContext, compilationConfiguration, metadata);
    }

    public static MainModule parseMainModuleFromQuery(
            String query, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return parseMainModuleFromQuery(query, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModuleFromQuery(String query, CompilationConfiguration compilationConfiguration) {
        return parseMainModuleFromQuery(query, compilationConfiguration, ExternalBindings.empty());
    }

    public static MainModule parseMainModuleFromQuery(
            String query, CompilationConfiguration compilationConfiguration, ExternalBindings externalBindings) {
        URI location = ModuleSourceLoader.queryLocation(compilationConfiguration.runtimeConfiguration());
        return parseMainModule(query, location, compilationConfiguration, externalBindings);
    }

    public static MainModule parseMainModule(
            String query, URI uri, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        return parseMainModule(query, uri, new CompilationConfiguration(configuration), externalBindings);
    }

    public static MainModule parseMainModule(
            String query,
            URI uri,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings) {
        return CompilationPipeline.compileMainModule(query, uri, compilationConfiguration, externalBindings);
    }

    /** Parses and analyzes a standalone library module for language-server callers. */
    public static LibraryModule parseLibraryModuleFromQueryWithStaticContextAndInference(
            String query, URI uri, RumbleConfiguration configuration) {
        return CompilationPipeline.analyzeLibraryModule(query, uri, configuration);
    }

    public static DynamicContext createDynamicContext(Node node, RumbleConfiguration configuration) {
        return createDynamicContext(node, configuration, ExternalBindings.empty());
    }

    public static DynamicContext createDynamicContext(
            Node node, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration, externalBindings);
        return visitor.visit(node, null);
    }
}
