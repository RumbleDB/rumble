package org.rumbledb.runtime.functions.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotInferEncodingException;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidJSONException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;


public class JsonDocFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public JsonDocFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator pathIterator = this.children.get(0);
        RuntimeIterator optionsIterator = this.children.size() > 1 ? this.children.get(1) : null;

        Item pathItem = pathIterator.materializeFirstItemOrNull(context);
        Item optionsItem = optionsIterator != null
            ? optionsIterator.materializeFirstItemOrNull(context)
            : null;

        if (pathItem == null) {
            return null;
        }

        boolean isJSONiq10 = this.staticContext.getQueryLanguage().equals("jsoniq10");

        JSONParsingOptions options = JSONParsingOptions.resolveOptions(
            optionsItem,
            isJSONiq10,
            context,
            this.staticContext,
            getMetadata()
        );

        URI uri = resolveJsonDocURI(pathItem.getStringValue(), this.staticURI, getMetadata());

        return parseJsonResource(
            uri,
            options,
            context.getRumbleRuntimeConfiguration(),
            getConfiguration().getXmlVersion(),
            isJSONiq10,
            getMetadata()
        );
    }

    private static URI resolveJsonDocURI(String href, URI staticBaseUri, ExceptionMetadata metadata) {
        try {
            URI uri = FileSystemUtil.resolveURI(
                staticBaseUri,
                href,
                metadata
            );

            if (uri.getFragment() != null) {
                throw new UnavailableResourceException(
                        "A URI containing a fragment identifier is not valid for fn:json-doc().",
                        metadata
                );
            }

            return uri;
        } catch (UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), metadata);
            ex.initCause(e);
            throw ex;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "The URI supplied to fn:json-doc() is invalid or cannot be resolved.",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private static Item parseJsonResource(
            URI uri,
            JSONParsingOptions options,
            RumbleRuntimeConfiguration conf,
            String xmlVersion,
            boolean isJSONiq10,
            ExceptionMetadata metadata
    ) {
        try (
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                conf,
                metadata
            )
        ) {
            PushbackInputStream pushback = new PushbackInputStream(is, 4);
            Charset charset = detectEncoding(peekLeadingBytes(pushback, 4));
            Reader reader = new InputStreamReader(pushback, strictDecoder(charset));
            try {
                return ItemParser.getItemFromJSONReader(reader, options, xmlVersion, isJSONiq10, metadata);
            } catch (UncheckedIOException e) {
                if (e.getCause() instanceof CharacterCodingException) {
                    CannotInferEncodingException ex = new CannotInferEncodingException(
                            "Unable to infer the encoding of the resource supplied to fn:json-doc(): "
                                + "the content is not valid "
                                + charset.name()
                                + ".",
                            metadata
                    );
                    ex.initCause(e);
                    throw ex;
                }
                throw e;
            }
        } catch (InvalidJSONException | DuplicateJSONKeyException | CannotInferEncodingException e) {
            throw e;
        } catch (UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), metadata);
            ex.initCause(e);
            throw ex;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to read, resolve, or decode the resource supplied to fn:json-doc().",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private static CharsetDecoder strictDecoder(Charset charset) {
        return charset.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);
    }

    private static byte[] peekLeadingBytes(PushbackInputStream stream, int maxBytes) throws IOException {
        byte[] buffer = new byte[maxBytes];
        int totalRead = 0;
        while (totalRead < maxBytes) {
            int n = stream.read(buffer, totalRead, maxBytes - totalRead);
            if (n == -1) {
                break;
            }
            totalRead += n;
        }
        if (totalRead > 0) {
            stream.unread(buffer, 0, totalRead);
        }
        return totalRead == maxBytes ? buffer : Arrays.copyOf(buffer, totalRead);
    }

    /**
     * Detects the resource encoding as required by fn:json-doc: UTF-8, UTF-16, or UTF-32,
     * from a leading byte-order mark or from the NUL pattern of the first
     * bytes.
     */
    private static Charset detectEncoding(byte[] b) {
        int b0 = b.length > 0 ? b[0] & 0xFF : -1;
        int b1 = b.length > 1 ? b[1] & 0xFF : -1;
        int b2 = b.length > 2 ? b[2] & 0xFF : -1;
        int b3 = b.length > 3 ? b[3] & 0xFF : -1;

        if (b0 == 0x00 && b1 == 0x00 && b2 == 0xFE && b3 == 0xFF) {
            return Charset.forName("UTF-32BE");
        }
        if (b0 == 0xFF && b1 == 0xFE && b2 == 0x00 && b3 == 0x00) {
            return Charset.forName("UTF-32LE");
        }
        if (b0 == 0xFE && b1 == 0xFF) {
            return StandardCharsets.UTF_16BE;
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            return StandardCharsets.UTF_16LE;
        }
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return StandardCharsets.UTF_8;
        }

        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 > 0x00) {
            return Charset.forName("UTF-32BE");
        }
        if (b0 > 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            return Charset.forName("UTF-32LE");
        }
        if (b0 == 0x00 && b1 > 0x00) {
            return StandardCharsets.UTF_16BE;
        }
        if (b0 > 0x00 && b1 == 0x00) {
            return StandardCharsets.UTF_16LE;
        }

        return StandardCharsets.UTF_8;
    }
}
