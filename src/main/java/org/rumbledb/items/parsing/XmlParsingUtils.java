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

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidXmlDocumentException;
import org.rumbledb.exceptions.OurBadException;

/**
 * Parses XML inputs and translates parser failures into Rumble diagnostics.
 * Resource retrieval and DOM-to-item conversion belong to the callers.
 */
public final class XmlParsingUtils {
    private static final String FRAGMENT_WRAPPER = "rumble-parse-xml-fragment-wrapper";

    private XmlParsingUtils() {}

    /** Parses a document string. The description names the input in error messages. */
    public static Document parseDocument(String xml, String description, ExceptionMetadata metadata) {
        return parseString(xml, false, description, metadata);
    }

    /** Parses a fragment string without exposing the synthetic wrapper in nodes or diagnostics. */
    public static DocumentFragment parseFragment(String xml, String description, ExceptionMetadata metadata) {
        Document document = parseString(xml, true, description, metadata);
        Node wrapper = document.getDocumentElement();
        DocumentFragment fragment = document.createDocumentFragment();
        while (wrapper.hasChildNodes()) {
            fragment.appendChild(wrapper.getFirstChild());
        }
        return fragment;
    }

    /**
     * Parses resource XML, reporting FODC0002 with the resource's URI and position.
     * The caller supplies the input description used in read and parse errors.
     * The caller owns the input stream and handles failures while closing it.
     */
    public static Document parseResource(
            InputSource source, String location, String description, ExceptionMetadata metadata) {
        try {
            return newDocumentBuilder(metadata).parse(source);
        } catch (IOException e) {
            CannotRetrieveResourceException exception =
                    new CannotRetrieveResourceException("Unable to read " + description + describe(e), metadata);
            exception.initCause(e);
            throw exception;
        } catch (SAXException e) {
            CannotRetrieveResourceException exception = new CannotRetrieveResourceException(
                    "Unable to parse " + description + describe(e), documentMetadata(e, location, metadata));
            exception.initCause(e);
            throw exception;
        }
    }

    /** Tests XML syntax quietly. The caller owns the input stream. */
    public static boolean isWellFormed(InputSource source, ExceptionMetadata metadata) {
        try {
            newDocumentBuilder(metadata).parse(source);
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

    private static Document parseString(String xml, boolean fragment, String description, ExceptionMetadata metadata) {
        String input = fragment ? "<" + FRAGMENT_WRAPPER + ">" + xml + "</" + FRAGMENT_WRAPPER + ">" : xml;
        try {
            return newDocumentBuilder(metadata).parse(new InputSource(new StringReader(input)));
        } catch (SAXException | IOException e) {
            String detail = fragment ? describeFragment(e, xml) : describe(e);
            InvalidXmlDocumentException exception =
                    new InvalidXmlDocumentException("Unable to parse " + description + detail, metadata);
            exception.initCause(e);
            throw exception;
        }
    }

    private static DocumentBuilder newDocumentBuilder(ExceptionMetadata metadata) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            return builder;
        } catch (ParserConfigurationException e) {
            OurBadException exception = new OurBadException("Document builder creation failed with: " + e, metadata);
            exception.initCause(e);
            throw exception;
        }
    }

    private static String describe(Exception exception) {
        if (exception instanceof SAXParseException parseException) {
            return formatDiagnostic(
                    exception.getMessage(), parseException.getLineNumber(), parseException.getColumnNumber());
        }
        return ": " + exception.getMessage();
    }

    /**
     * Reports positions relative to the fragment rather than the synthetic wrapper. Errors in the wrapper's closing
     * tag, such as an unclosed element, are reported at the end of the input.
     */
    private static String describeFragment(Exception exception, String xml) {
        if (!(exception instanceof SAXParseException parseException)) {
            return describe(exception);
        }
        String message = parseException.getMessage().replace(FRAGMENT_WRAPPER, "fragment");
        int line = parseException.getLineNumber();
        int column = parseException.getColumnNumber();
        if (line == 1 && column > 0) {
            column = Math.max(1, column - FRAGMENT_WRAPPER.length() - 2);
        }
        // XML treats CR, LF and CRLF as line breaks.
        String[] lines = xml.split("\r\n|\r|\n", -1);
        int lastColumn = lines[lines.length - 1].length() + 1;
        if (line > lines.length || (line == lines.length && column > lastColumn)) {
            return " at end of input: " + message;
        }
        return formatDiagnostic(message, line, column);
    }

    private static String formatDiagnostic(String message, int line, int column) {
        String position = "";
        if (line > 0) {
            position = " at line " + line;
            if (column > 0) {
                position += ", column " + column;
            }
        }
        return position + ": " + message;
    }

    private static ExceptionMetadata documentMetadata(
            SAXException exception, String location, ExceptionMetadata fallback) {
        if (!(exception instanceof SAXParseException parseException) || parseException.getLineNumber() <= 0) {
            return fallback;
        }
        return ExceptionMetadata.fromPoint(
                parseException.getSystemId() == null ? location : parseException.getSystemId(),
                parseException.getLineNumber(),
                Math.max(0, parseException.getColumnNumber() - 1),
                "");
    }
}
