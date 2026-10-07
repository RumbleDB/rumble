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
package org.rumbledb.xml.schema;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.CompilationPipeline;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.module.MainModule;

public class XmlSchemaStepNamesTest {

    private static final Path SCHEMA =
            Path.of("src/test/resources/test_files/static-typing/schema-path-steps/PathSteps.xsd");

    @Test
    public void listsDeclaredChildrenAndAttributes() {
        Assertions.assertEquals(
                Optional.of(List.of("p:item", "p:special", "p:paid", "p:due", "p:customer", "p:meta")),
                stepNames("validate { <p:order/> }", false));
        Assertions.assertEquals(Optional.of(List.of("id", "note")), stepNames("validate { <p:item/> }", true));
    }

    @Test
    public void listsNothingWhenAWildcardAllowsOtherNames() {
        Assertions.assertEquals(Optional.empty(), stepNames("(validate { <p:order/> })/p:meta", false));
    }

    private static Optional<List<String>> stepNames(String expression, boolean attributeAxis) {
        String query = "import schema namespace p = \"urn:path-steps\" at \"" + SCHEMA.toUri() + "\"; " + expression;
        MainModule module = CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///step-names.xq"),
                new CompilationConfiguration(RumbleConfiguration.builder().build()),
                ExternalBindings.empty());
        return module.getStaticContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getStepNames(module.getExpression().getStaticSequenceType().getItemType(), attributeAxis)
                .map(names -> names.stream().map(Name::toString).toList());
    }
}
