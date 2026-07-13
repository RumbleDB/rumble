package org.rumbledb.runtime.functions.json;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serial;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.CannotInferEncodingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.URIUtils;
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

        String jsonText = readJsonResource(uri, context.getRumbleRuntimeConfiguration(), getMetadata());

        return ItemParser.getItemFromJSONString(
            jsonText,
            options,
            getConfiguration().getXmlVersion(),
            isJSONiq10,
            getMetadata()
        );
    }

    private static URI resolveJsonDocURI(String href, URI staticBaseUri, ExceptionMetadata metadata) {
        try {
            URI uri = URIUtils.resolveURIReference(staticBaseUri, href);

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



    private static String readJsonResource(URI uri, RumbleRuntimeConfiguration conf, ExceptionMetadata metadata) {
        try (
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                conf,
                metadata
            )
        ) {
            return readAll(is, metadata);
        } catch (CannotInferEncodingException e) {
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

    private static String readAll(InputStream is, ExceptionMetadata metadata) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            byte[] bytes = buffer.toByteArray();
            return decode(bytes, detectEncoding(bytes), metadata);
        } catch (CannotInferEncodingException e) {
            throw e;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to read or decode the resource supplied to fn:json-doc().",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private static String decode(byte[] bytes, Charset charset, ExceptionMetadata metadata) {
        try {
            CharsetDecoder decoder = charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
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
