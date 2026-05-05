package org.rumbledb.runtime.functions.io;

import com.google.gson.stream.JsonReader;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.functions.json.JSONParsingOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        Item pathItem = pathIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        Item optionsItem = optionsIterator != null
            ? optionsIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution)
            : null;

        if (pathItem == null) {
            return null;
        }

        boolean isJSONiq = getConfiguration().getQueryLanguage().equals("jsoniq10");

        JSONParsingOptions options = JSONParsingOptions.resolveOptions(
            optionsItem,
            isJSONiq,
            getMetadata()
        );

        URI uri = resolveJsonDocURI(pathItem.getStringValue(), getMetadata());

        if (optionsItem == null) {
            try (
                InputStream is = FileSystemUtil.getDataInputStream(
                    uri,
                    this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                    getMetadata()
                )
            ) {
                JsonReader object = new JsonReader(new InputStreamReader(is));
                return ItemParser.getItemFromObject(object, isJSONiq, options.getNumberFormat(), getMetadata());
            } catch (CannotRetrieveResourceException e) {
                UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
                ex.initCause(e);
                throw ex;
            } catch (UnavailableResourceException e) {
                throw e;
            } catch (Exception e) {
                String jsonText = readJsonResource(uri);

                return ItemParser.getItemFromJSONString(
                    jsonText,
                    options,
                    getConfiguration().getXmlVersion(),
                    isJSONiq,
                    getMetadata()
                );
            }
        }


        String jsonText = readJsonResource(uri);

        return ItemParser.getItemFromJSONString(
            jsonText,
            options,
            getConfiguration().getXmlVersion(),
            isJSONiq,
            getMetadata()
        );
    }

    private URI resolveJsonDocURI(String href, ExceptionMetadata metadata) {
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticURI,
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
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
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



    private String readJsonResource(URI uri) {
        try (
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                getMetadata()
            )
        ) {
            return readAll(is);
        } catch (UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
            ex.initCause(e);
            throw ex;
        } catch (Exception e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to read, resolve, or decode the resource supplied to fn:json-doc().",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private String readAll(InputStream is) {
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
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
