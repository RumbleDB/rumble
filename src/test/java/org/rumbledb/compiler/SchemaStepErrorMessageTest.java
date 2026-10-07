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
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;

class SchemaStepErrorMessageTest {
    private static final Path SCHEMA =
            Path.of("src/test/resources/test_files/static-typing/schema-path-steps/PathSteps.xsd");

    @Test
    void listsTheDeclaredChildren() {
        assertEquals(
                "The schema declares no child element p:prices for schema-element(p:order). "
                        + "It declares p:item, p:special, p:paid, p:due, p:customer, p:meta.",
                message("$order/p:prices"));
    }

    @Test
    void listsTheDeclaredAttributes() {
        assertEquals(
                "The schema declares no attribute ids for element(p:item, p:Item). It declares id, note.",
                message("$order/p:item/@ids"));
    }

    @Test
    void listsTheDeclaredDescendants() {
        // p:order is not used here, since a wildcard below it allows elements that the schema does not describe.
        assertEquals(
                "The schema declares no descendant element p:prise below element(p:item, p:Item). "
                        + "It declares p:price, p:tag.",
                message("$order/p:item//p:prise"));
    }

    @Test
    void explainsThatAnElementHasNoChildren() {
        assertEquals(
                "The schema declares no child elements for element(p:paid, xs:boolean).", message("$order/p:paid/*"));
    }

    private static String message(String path) {
        String query = "import schema namespace p = \"urn:path-steps\" at \""
                + SCHEMA.toUri()
                + "\"; declare function local:f($order as schema-element(p:order)) { "
                + path
                + " }; 1";
        UnexpectedStaticTypeException exception = assertThrows(
                UnexpectedStaticTypeException.class,
                () -> CompilationPipeline.compileMainModule(
                        query,
                        URI.create("file:///schema-step.xq"),
                        new CompilationConfiguration(RumbleConfiguration.builder()
                                .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
                                .build()),
                        ExternalBindings.empty()));
        return exception.getJSONiqErrorMessage();
    }
}
