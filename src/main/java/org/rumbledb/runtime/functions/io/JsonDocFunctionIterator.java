package org.rumbledb.runtime.functions.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import com.google.gson.stream.JsonReader;

public class JsonDocFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

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
            getMetadata()
        );

        URI uri = resolveJsonDocURI(pathItem.getStringValue(), this.staticURI, getMetadata());

        if (optionsItem == null) {
            try (
                InputStream is = FileSystemUtil.getDataInputStream(
                    uri,
                    context.getRumbleRuntimeConfiguration(),
                    getMetadata()
                )
            ) {
                JsonReader object = new JsonReader(new InputStreamReader(is));
                return ItemParser.getItemFromObject(object, isJSONiq10, options.getNumberFormat(), getMetadata());
            } catch (CannotRetrieveResourceException e) {
                UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
                ex.initCause(e);
                throw ex;
            } catch (UnavailableResourceException e) {
                throw e;
            } catch (Exception e) {
                String jsonText = readJsonResource(uri, context.getRumbleRuntimeConfiguration(), getMetadata());

                return ItemParser.getItemFromJSONString(
                    jsonText,
                    options,
                    getConfiguration().getXmlVersion(),
                    isJSONiq10,
                    getMetadata()
                );
            }
        }


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



    private static String readJsonResource(URI uri, RumbleRuntimeConfiguration conf, ExceptionMetadata metadata) {
        try (
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                conf,
                metadata
            )
        ) {
            return readAll(is, metadata);
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
            return buffer.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to read or decode the resource supplied to fn:json-doc().",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
