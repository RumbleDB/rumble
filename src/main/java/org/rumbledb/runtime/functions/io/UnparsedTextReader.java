package org.rumbledb.runtime.functions.io;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidEncodingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.xml.XMLUtils;

import java.net.URI;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public final class UnparsedTextReader {

    private UnparsedTextReader() {
    }


    private static URI resolveHref(URI staticBaseUri, String href, ExceptionMetadata metadata) {
        if (href.isEmpty()) {
            return staticBaseUri;
        }
        return FileSystemUtil.resolveURI(staticBaseUri, href, metadata);
    }

    public static String read(
            URI staticBaseUri,
            String href,
            String encoding,
            String xmlVersion,
            ExceptionMetadata metadata
    ) {
        URI uri;
        try {
            uri = resolveHref(staticBaseUri, href, metadata);
        } catch (UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), metadata);
            ex.initCause(e);
            throw ex;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "The URI supplied to fn:unparsed-text() is invalid or cannot be resolved: " + href,
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }

        if (uri.getFragment() != null) {
            throw new UnavailableResourceException(
                    "A URI containing a fragment identifier is not valid for fn:unparsed-text().",
                    metadata
            );
        }

        byte[] bytes;
        try {
            bytes = TextResourceUtil.fetchBytes(uri, metadata);
        } catch (UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), metadata);
            ex.initCause(e);
            throw ex;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to retrieve the resource supplied to fn:unparsed-text(): " + uri,
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }

        Charset charset;
        if (encoding != null) {
            try {
                charset = Charset.forName(encoding);
            } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
                InvalidEncodingException ex = new InvalidEncodingException(
                        "Invalid or unsupported encoding supplied to fn:unparsed-text(): " + encoding,
                        metadata
                );
                ex.initCause(e);
                throw ex;
            }
        } else {
            charset = TextResourceUtil.detectEncoding(bytes);
        }

        String decoded;
        try {
            decoded = TextResourceUtil.decodeStrict(bytes, charset);
        } catch (CharacterCodingException e) {
            InvalidEncodingException ex = new InvalidEncodingException(
                    "Unable to decode the resource supplied to fn:unparsed-text() using encoding "
                        + charset.name()
                        + ".",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }

        if (!decoded.isEmpty() && decoded.charAt(0) == '\uFEFF') {
            decoded = decoded.substring(1);
        }

        int i = 0;
        while (i < decoded.length()) {
            int codepoint = decoded.codePointAt(i);
            if (!XMLUtils.isValidXmlCharacter(codepoint, xmlVersion)) {
                throw new InvalidEncodingException(
                        "The resource supplied to fn:unparsed-text() contains a character "
                            + "that is not permitted in XML: U+"
                            + Integer.toHexString(codepoint).toUpperCase(),
                        metadata
                );
            }
            i += Character.charCount(codepoint);
        }

        return decoded;
    }
}
