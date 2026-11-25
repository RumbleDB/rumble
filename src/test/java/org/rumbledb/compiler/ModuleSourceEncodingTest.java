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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;

class ModuleSourceEncodingTest {
    @TempDir
    Path directory;

    private final Rumble rumble = new Rumble(RumbleConfiguration.builder().build());

    @ParameterizedTest
    @CsvSource({
        "UTF-8, encoding 'UTF-8'",
        "UTF-16BE, encoding 'UTF-16'",
        "UTF-16LE, encoding 'UTF-16'",
        "UTF-32BE, encoding 'UTF-32'",
        "UTF-32LE, encoding 'UTF-32'",
        "UTF-16BE, ''",
        "UTF-16LE, ''",
        "UTF-32BE, ''",
        "UTF-32LE, ''",
        "UTF-16BE, encoding 'UTF-8'",
        "UTF-16LE, encoding 'UTF-8'",
        "UTF-32BE, encoding 'UTF-8'",
        "UTF-32LE, encoding 'UTF-8'"
    })
    void usesTheByteOrderMarkToDecodeQueries(String charset, String encodingClause) throws Exception {
        Path query = this.directory.resolve("main.xq");
        Files.writeString(
                query,
                "\uFEFFxquery version '3.1' " + encodingClause + "; string-to-codepoints('é𐐷')",
                Charset.forName(charset));

        assertCodepoints(query, 233, 66615);
    }

    @ParameterizedTest
    @CsvSource({"windows-1252, €, 8364", "ISO-8859-15, €, 8364", "Shift_JIS, 漢, 28450"})
    void usesDeclaredEncodingsForQueriesWithoutAByteOrderMark(String charset, String text, int codepoint)
            throws Exception {
        Path query = this.directory.resolve("main.xq");
        Files.writeString(
                query,
                "xquery version '3.1' encoding '" + charset + "'; string-to-codepoints('" + text + "')",
                Charset.forName(charset));

        assertCodepoints(query, codepoint);
    }

    @Test
    void honorsTheEncodingDeclarationOfAnImportedModule() throws Exception {
        Files.writeString(
                this.directory.resolve("library.xqm"),
                """
                    (: A comment before the version declaration :)
                    xquery version '3.1' encoding 'ISO&#45;8859-1';
                    module namespace lib = 'urn:encoding-test';
                    declare function lib:codepoint() { string-to-codepoints('é') };
                    """,
                StandardCharsets.ISO_8859_1);
        Path query = this.directory.resolve("main.xq");
        Files.writeString(
                query,
                """
                    xquery version '3.1';
                    import module namespace lib = 'urn:encoding-test' at 'library.xqm';
                    lib:codepoint()
                    """);

        assertCodepoints(query, 233);
    }

    private void assertCodepoints(Path query, Integer... expected) throws IOException {
        List<Integer> result = this.rumble.runQuery(query.toUri()).getAsList().stream()
                .map(Item::getIntValue)
                .toList();
        assertEquals(List.of(expected), result);
    }
}
