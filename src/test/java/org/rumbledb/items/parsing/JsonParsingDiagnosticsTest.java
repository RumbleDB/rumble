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
package org.rumbledb.items.parsing;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.InvalidJSONException;

class JsonParsingDiagnosticsTest {
    private final Rumble rumble = new Rumble(RumbleConfiguration.builder().build());

    @Test
    void parseJsonReportsTheInputPositionAndKeepsTheCallSite() {
        InvalidJSONException error = assertThrows(
                InvalidJSONException.class,
                () -> this.rumble.runQuery("\n\n    parse-json(\"[1,\\n  x]\")").getAsList());
        assertTrue(
                error.getJSONiqErrorMessage()
                        .startsWith("Unable to parse the argument of fn:parse-json at line 2, column 3:"),
                error.getMessage());
        // The metadata points to the call in the query; the JSON position is part of the message.
        assertEquals(3, error.getMetadata().getStart().line());
    }

    @Test
    void jsonDocErrorsPointIntoTheDocument(@TempDir Path directory) throws Exception {
        Path file = directory.resolve("broken.json");
        Files.writeString(file, "{\n  \"a\": x\n}");
        String uri = file.toUri().toString();
        InvalidJSONException error = assertThrows(
                InvalidJSONException.class,
                () -> this.rumble.runQuery("json-doc(\"" + uri + "\")").getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains(uri), error.getMessage());
        assertEquals(uri, error.getMetadata().getLocation());
        assertEquals(2, error.getMetadata().getStart().line());
        assertEquals(7, error.getMetadata().getStart().column());
    }

    @Test
    void duplicateKeysReportTheKeyPosition() {
        DuplicateJSONKeyException error = assertThrows(DuplicateJSONKeyException.class, () -> this.rumble
                .runQuery(
                        "parse-json('{\"a\": 1, \"a\": 2}', map { 'duplicates': 'reject' })",
                        URI.create("file:///duplicates.xq"))
                .getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("at line 1, column 10:"), error.getMessage());
    }
}
