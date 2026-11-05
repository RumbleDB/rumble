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
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;

class ModuleKindTest {
    private static final String OUTPUT_NAMESPACE = "http://www.w3.org/2010/xslt-xquery-serialization";

    @TempDir
    Path directory;

    private static Rumble rumble(String language) {
        return new Rumble(RumbleConfiguration.builder()
                .with("semantics.queryLanguage", language)
                .build());
    }

    private RumbleException importFailure(String library) throws IOException {
        Path file = this.directory.resolve("library");
        Files.writeString(file, library);
        String query = "import module namespace a = \"urn:a\" at \"" + file.toUri() + "\"; 1";
        return assertThrows(RumbleException.class, () -> rumble("xquery31").runQuery(query));
    }

    @ParameterizedTest
    @ValueSource(strings = {"jsoniq10", "xquery31"})
    void aLibraryModuleCannotBeRunAsAQuery(String language) {
        ParsingException exception = assertThrows(ParsingException.class, () -> rumble(language)
                .runQuery("module namespace a = \"urn:a\"; declare function a:f() { 1 };"));
        assertTrue(exception.getMessage().contains("Main module expected, but library module found."));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "1",
                "module + 1",
                "<html/>",
                "xquery version \"3.1\"; 1",
                "xquery version 3.1; module namespace a = \"urn:a\";",
                "jsoniq version 3.1; module namespace a = \"urn:a\";"
            })
    void importedResourcesThatDoNotStartLikeALibraryAreNotLibraries(String resource) throws IOException {
        RumbleException exception = importFailure(resource);
        assertTrue(exception instanceof ModuleNotFoundException, exception.toString());
    }

    @Test
    void syntaxErrorsInsideALibraryAreStillReportedAsSyntaxErrors() throws IOException {
        RumbleException exception = importFailure("xquery version \"3.1\"; module namespace a = ;");
        assertTrue(exception instanceof ParsingException, exception.toString());
    }

    @Test
    void libraryDeclarationsMustUseTheModuleNamespace() throws IOException {
        RumbleException exception = importFailure("module namespace a = \"urn:a\"; declare function local:f() { 1 };");
        assertEquals(
                ErrorCode.NamespaceDoesNotMatchModule.toString(),
                exception.getErrorCode().toString());
    }

    @Test
    void libraryContextItemsCannotHaveAnInitializer() throws IOException {
        RumbleException exception = importFailure("module namespace a = \"urn:a\"; declare context item := 1;");
        assertEquals(
                ErrorCode.LibraryContextItemInitializer.toString(),
                exception.getErrorCode().toString());
    }

    @Test
    void librariesCannotHaveOutputDeclarations() throws IOException {
        RumbleException exception = importFailure("module namespace a = \"urn:a\"; declare namespace o = \""
                + OUTPUT_NAMESPACE
                + "\"; declare option o:method \"xml\";");
        assertEquals(
                ErrorCode.LibraryModuleOutputDeclarationErrorCode.toString(),
                exception.getErrorCode().toString());
    }

    @Test
    void mainModulesAreNotHeldToLibraryRules() {
        String query = "declare namespace o = \""
                + OUTPUT_NAMESPACE
                + "\"; declare option o:method \"json\"; declare context item := 1;"
                + " declare function local:f() { . }; local:f()";
        assertEquals("1", rumble("xquery31").runQuery(query).serialize());
    }
}
