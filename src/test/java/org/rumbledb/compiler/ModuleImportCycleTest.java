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

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.resources.ResourceResolver;

class ModuleImportCycleTest {
    @TempDir
    Path directory;

    @Test
    void staticAndDynamicImportsShareFunctionsAndVariablesAcrossACycle() throws Exception {
        Path first = this.directory.resolve("first.xqm");
        Path second = this.directory.resolve("second.xqm");
        Files.writeString(
                first,
                """
                    module namespace a = 'urn:first';
                    import module namespace b = 'urn:second';
                    declare variable $a:value as xs:string external;
                    declare function a:get() { b:get() };
                    """);
        Files.writeString(
                second,
                """
                    module namespace b = 'urn:second';
                    import module namespace a = 'urn:first';
                    declare function b:get() { $a:value };
                    """);
        var configuration = RumbleConfiguration.builder()
                .configureSemantics(s -> s.queryLanguage("xquery31"))
                .build();
        var resolver = new ResourceResolver(
                Map.of(URI.create("urn:first"), first.toUri(), URI.create("urn:second"), second.toUri()));
        for (boolean dynamic : List.of(false, true)) {
            ExternalBindings bindings = ExternalBindings.empty();
            if (!dynamic) {
                bindings.bind(
                        new Name("urn:first", "a", "value"),
                        new ItemSequenceBinding(
                                List.of(ItemFactory.getInstance().createStringItem("bound"))));
            }
            String query = dynamic
                    ? """
                        load-xquery-module('urn:first', map{
                            'variables': map{QName('urn:first','value'): 'bound'}
                        })?functions(QName('urn:first','get'))(0)()
                        """
                    : "import module namespace a = 'urn:first'; a:get()";
            var module = CompilationPipeline.compileMainModule(
                    "xquery version '3.1'; " + query,
                    this.directory.resolve("main.xq").toUri(),
                    new CompilationConfiguration(configuration, resolver),
                    bindings);
            var context = new DynamicContextVisitor(configuration, bindings, resolver).visit(module, null);
            var result = new RuntimeIteratorVisitor(configuration)
                    .generateRuntimePlan(module)
                    .materialize(context);
            assertEquals(1, result.size());
            assertEquals("bound", result.get(0).getStringValue());
        }
    }
}
