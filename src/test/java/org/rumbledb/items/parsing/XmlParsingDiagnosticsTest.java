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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import scala.Tuple2;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.net.httpserver.HttpServer;

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
    void parseXmlIncludesParserReasonAndPosition() {
        InvalidXmlDocumentException error = assertThrows(
                InvalidXmlDocumentException.class,
                () -> this.rumble.runQuery("parse-xml(\"<a>\\n</b>\")").getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("fn:parse-xml"));
        assertTrue(error.getJSONiqErrorMessage().contains("XML input at line 2, column 3"));
        assertTrue(error.getJSONiqErrorMessage().contains("</a>"));
        assertInstanceOf(SAXParseException.class, error.getCause());
    }

    @Test
    void xmlInputPositionStaysSeparateFromQueryCallPosition() {
        InvalidXmlDocumentException error = assertThrows(InvalidXmlDocumentException.class, () -> this.rumble
                .runQuery("\n\n\n    parse-xml(\"<a>\\n</b>\")")
                .getAsList());
        assertEquals(4, error.getMetadata().getStart().line());
        assertEquals(4, error.getMetadata().getStart().column());
        assertTrue(error.getJSONiqErrorMessage().contains("XML input at line 2, column 3"));
    }

    @Test
    void fragmentPositionsExcludeWrapperPrefix() {
        InvalidXmlDocumentException error = assertThrows(
                InvalidXmlDocumentException.class,
                () -> this.rumble.runQuery("parse-xml-fragment(\"<a></b>\")").getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("at line 1, column 6"), error.getMessage());
        assertFalse(error.getJSONiqErrorMessage().contains("rumble-parse-xml-fragment-wrapper"));
        assertInstanceOf(SAXParseException.class, error.getCause());
    }

    @ParameterizedTest
    @CsvSource({"<a>, 1, 4", "<!--, 1, 5", "'<a>\n', 2, 1", "'<a>\r\n', 2, 1", "'<a>\r', 2, 1"})
    void incompleteFragmentPositionsStopAtEndOfInput(String xml, int line, int column) {
        InvalidXmlDocumentException error = assertThrows(
                InvalidXmlDocumentException.class,
                () -> XmlParsingUtils.parseFragment(xml, "Invalid fragment", ExceptionMetadata.EMPTY_METADATA));
        assertTrue(
                error.getJSONiqErrorMessage().contains("at line " + line + ", column " + column + ":"),
                error.getMessage());
    }

    @Test
    void fragmentLaterLinePositionsAreUnchanged() {
        InvalidXmlDocumentException error = assertThrows(
                InvalidXmlDocumentException.class,
                () -> this.rumble.runQuery("parse-xml-fragment(\"<a>\\n</b>\")").getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("at line 2, column 3"), error.getMessage());
    }

    @Test
    void xmlFilesIdentifiesFileWithoutDumpingItsContents() throws Exception {
        String path = "file:/broken.xml";
        String content = "<private-data>secret-value</wrong>";
        var items = new XmlSyntaxToItemMapper(ExceptionMetadata.EMPTY_METADATA, false)
                .call(Collections.singletonList(new Tuple2<>(path, content)).iterator());
        CannotRetrieveResourceException error = assertThrows(CannotRetrieveResourceException.class, items::next);
        assertTrue(error.getJSONiqErrorMessage().contains(path));
        assertTrue(error.getJSONiqErrorMessage().contains("jn:xml-files() at line 1, column"));
        assertFalse(error.getJSONiqErrorMessage().contains("secret-value"));
        assertEquals(path, error.getMetadata().getLocation());
        assertInstanceOf(SAXParseException.class, error.getCause());
    }

    @Test
    void httpXmlFilesPreserveTrailingNewlineInErrorPosition() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/broken.xml", exchange -> {
            byte[] content = "<a>\n".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, content.length);
            try (var output = exchange.getResponseBody()) {
                output.write(content);
            }
        });
        server.start();
        try {
            String uri = "http://127.0.0.1:" + server.getAddress().getPort() + "/broken.xml";
            Throwable failure = assertThrows(
                    Exception.class,
                    () -> this.rumble.runQuery("xml-files(\"" + uri + "\", 1)").getAsList());
            // Spark may wrap the parser error when it returns from the executor.
            while (!(failure instanceof CannotRetrieveResourceException) && failure.getCause() != null) {
                failure = failure.getCause();
            }
            CannotRetrieveResourceException error = assertInstanceOf(CannotRetrieveResourceException.class, failure);
            assertEquals(uri, error.getMetadata().getLocation());
            assertEquals(2, error.getMetadata().getStart().line());
            assertEquals(0, error.getMetadata().getStart().column());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void docRetainsFileDiagnostics(@TempDir Path directory) throws Exception {
        Path file = directory.resolve("broken.xml");
        Files.writeString(file, "<a>\n</b>");
        CannotRetrieveResourceException error = assertThrows(
                CannotRetrieveResourceException.class,
                () -> this.rumble.runQuery("doc(\"" + file.toUri() + "\")").getAsList());
        assertTrue(error.getJSONiqErrorMessage().contains("fn:doc() at line 2, column 3"));
        assertEquals(file.toUri().toString(), error.getMetadata().getLocation());
        assertEquals(2, error.getMetadata().getStart().line());
        assertEquals(2, error.getMetadata().getStart().column());
    }

    @Test
    void serializationParameterDocumentsIncludeFileAndReason(@TempDir Path directory) throws Exception {
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
        assertInstanceOf(SAXParseException.class, error.getCause());
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

    @Test
    void parserReadFailuresRetainCauseAndCallSiteMetadata() {
        IOException cause = new IOException("XML stream failed");
        InputStream stream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw cause;
            }
        };
        ExceptionMetadata metadata = ExceptionMetadata.fromPoint("file:/query.jq", 4, 8, "");
        CannotRetrieveResourceException error = assertThrows(
                CannotRetrieveResourceException.class,
                () -> XmlParsingUtils.parseResource(
                        new InputSource(stream),
                        "file:/document.xml",
                        "XML document \"file:/document.xml\" for fn:doc()",
                        metadata));
        assertEquals(
                "Unable to read XML document \"file:/document.xml\" for fn:doc(): XML stream failed",
                error.getJSONiqErrorMessage());
        assertSame(cause, error.getCause());
        assertSame(metadata, error.getMetadata());
    }

    @Test
    void stringParsingUsesCallerErrorMessages() {
        InvalidXmlDocumentException documentError = assertThrows(
                InvalidXmlDocumentException.class,
                () -> XmlParsingUtils.parseDocument(
                        "<a></b>", "Caller document error", ExceptionMetadata.EMPTY_METADATA));
        assertTrue(documentError.getJSONiqErrorMessage().startsWith("Caller document error at line 1, column "));
        InvalidXmlDocumentException fragmentError = assertThrows(
                InvalidXmlDocumentException.class,
                () -> XmlParsingUtils.parseFragment(
                        "<a></b>", "Caller fragment error", ExceptionMetadata.EMPTY_METADATA));
        assertTrue(fragmentError.getJSONiqErrorMessage().startsWith("Caller fragment error at line 1, column 6: "));
    }

    @Test
    void fragmentPreservesSiblingNodesAndNamespaces() {
        String fragment = "parse-xml-fragment(\"<a xmlns='urn:test'/><b/><!--c--><?p data?>\")";
        assertEquals(
                4,
                this.rumble
                        .runQuery("count(" + fragment + "/node())")
                        .getAsList()
                        .get(0)
                        .getIntValue());
        assertEquals(
                "urn:test",
                this.rumble
                        .runQuery("namespace-uri(" + fragment + "/*[1])")
                        .getAsList()
                        .get(0)
                        .getStringValue());
    }

    @Test
    void validAndEmptyStringInputsStillWork() {
        assertTrue(this.rumble.runQuery("parse-xml(())").getAsList().isEmpty());
        assertTrue(this.rumble.runQuery("parse-xml-fragment(())").getAsList().isEmpty());
        assertTrue(
                this.rumble.runQuery("parse-xml(\"<a/>\")").getAsList().get(0).isDocumentNode());
        assertTrue(this.rumble
                .runQuery("parse-xml-fragment(\"<a/><b/>\")")
                .getAsList()
                .get(0)
                .isDocumentNode());
    }
}
