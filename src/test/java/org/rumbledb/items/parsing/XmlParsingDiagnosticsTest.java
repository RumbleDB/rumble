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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import scala.Tuple2;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidXmlDocumentException;
import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.serialization.SerializationParameters;

class XmlParsingDiagnosticsTest {
    private final Rumble rumble = new Rumble(RumbleConfiguration.builder().build());

    @Test
    void parseXmlReportsTheInputPositionAndKeepsTheCallSite() {
        InvalidXmlDocumentException error = assertThrows(InvalidXmlDocumentException.class, () -> this.rumble
                .runQuery("\n\n\n    parse-xml(\"<a>\\n</b>\")")
                .getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("fn:parse-xml"));
        assertTrue(error.getJSONiqErrorMessage().contains("at line 2, column 3"), error.getMessage());
        // The metadata points to the call in the query; the XML position is part of the message.
        assertEquals(4, error.getMetadata().getStart().line());
        assertEquals(4, error.getMetadata().getStart().column());
    }

    @Test
    void fragmentPositionsAreRelativeToTheInput() {
        InvalidXmlDocumentException mismatched = assertThrows(
                InvalidXmlDocumentException.class,
                () -> XmlParsingUtils.parseFragment("<a></b>", "a fragment", ExceptionMetadata.EMPTY_METADATA));
        assertTrue(mismatched.getJSONiqErrorMessage().contains("at line 1, column 6"), mismatched.getMessage());
        assertFalse(mismatched.getJSONiqErrorMessage().contains("rumble-parse-xml-fragment-wrapper"));
        InvalidXmlDocumentException unclosed = assertThrows(
                InvalidXmlDocumentException.class,
                () -> XmlParsingUtils.parseFragment("<a>\n", "a fragment", ExceptionMetadata.EMPTY_METADATA));
        assertTrue(unclosed.getJSONiqErrorMessage().contains("at end of input"), unclosed.getMessage());
    }

    @Test
    void xmlFilesIdentifiesTheFileWithoutDumpingItsContents() throws Exception {
        String path = "file:/broken.xml";
        String content = "<private-data>secret-value</wrong>";
        var items = new XmlSyntaxToItemMapper(ExceptionMetadata.EMPTY_METADATA, false)
                .call(Collections.singletonList(new Tuple2<>(path, content)).iterator());
        CannotRetrieveResourceException error = assertThrows(CannotRetrieveResourceException.class, items::next);
        assertTrue(error.getJSONiqErrorMessage().contains(path));
        assertFalse(error.getJSONiqErrorMessage().contains("secret-value"));
        assertEquals(path, error.getMetadata().getLocation());
        assertEquals(1, error.getMetadata().getStart().line());
    }

    @Test
    void serializationParameterDocumentsIncludeTheFileAndPosition(@TempDir Path directory) throws Exception {
        Path file = directory.resolve("parameters.xml");
        Files.writeString(file, "<a>\n</b>");
        CannotRetrieveResourceException error = assertThrows(
                CannotRetrieveResourceException.class,
                () -> SerializationParameterUtils.applyParameterDocument(
                        new SerializationParameters(),
                        new StaticContext(
                                directory.toUri(), RumbleConfiguration.builder().build()),
                        file.toUri().toString(),
                        Set.of(),
                        ExceptionMetadata.EMPTY_METADATA));
        assertTrue(error.getJSONiqErrorMessage().contains(file.toUri().toString()));
        assertTrue(error.getJSONiqErrorMessage().contains("at line 2, column 3"));
    }

    @Test
    void docAvailableReturnsFalseWithoutPrintingParserErrors(@TempDir Path directory) throws Exception {
        Path file = directory.resolve("broken.xml");
        Files.writeString(file, "<a></b>");
        PrintStream previous = System.err;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output)) {
            System.setErr(capture);
            assertFalse(this.rumble
                    .runQuery("doc-available(\"" + file.toUri() + "\")")
                    .getAsList()
                    .get(0)
                    .getBooleanValue());
        } finally {
            System.setErr(previous);
        }
        assertEquals("", output.toString());
    }
}
