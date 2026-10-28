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
package org.rumbledb.types;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.CompilationPipeline;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import static org.junit.jupiter.api.Assertions.*;

class SchemaDataStaticTypingTest {
    @TempDir
    Path directory;

    @Test
    void mixedTypeWithElementOnlyRestrictionFailsStaticAtomization() throws Exception {
        Path schema = directory.resolve("mixed.xsd");
        Files.writeString(schema, """
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:mixed"
                           targetNamespace="urn:mixed">
                  <xs:complexType name="mixed" mixed="true">
                    <xs:sequence><xs:element name="e" type="xs:string"/></xs:sequence>
                  </xs:complexType>
                  <xs:complexType name="elements">
                    <xs:complexContent mixed="false"><xs:restriction base="t:mixed">
                      <xs:sequence><xs:element name="e" type="xs:string"/></xs:sequence>
                    </xs:restriction></xs:complexContent>
                  </xs:complexType>
                </xs:schema>
                """);
        String prolog = "import schema namespace t='urn:mixed' at '" + schema.toUri()
                + "'; declare context item as document-node() external; ";
        CompilationConfiguration config = new CompilationConfiguration(RumbleConfiguration.builder()
                .configureSemantics(s -> s.queryLanguage("xquery31"))
                .configureAnalysis(a -> a.enableStaticTyping(true)).build());
        UnexpectedStaticTypeException error = assertThrows(UnexpectedStaticTypeException.class,
                () -> CompilationPipeline.compileMainModuleFromQuery(prolog
                        + "let $n as element(*, t:mixed) := /* return data($n)", config, ExternalBindings.empty()));
        assertTrue(error.getMessage().contains("FOTY0012"), error.getMessage());
        assertDoesNotThrow(() -> CompilationPipeline.compileMainModuleFromQuery(prolog
                + "let $n as element(*, t:mixed) := /* return count($n)", config, ExternalBindings.empty()));
    }
}
