package org.rumbledb.runtime.functions.io;

import com.google.gson.stream.JsonReader;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.functions.json.JSONParsingOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonDocFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator pathIterator;
    private RuntimeIterator optionsIterator;

    public JsonDocFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.pathIterator = this.children.get(0);
        this.optionsIterator = this.children.size() > 1 ? this.children.get(1) : null;
        this.pathIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.pathIterator.hasNext();
        this.pathIterator.close();
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " json-doc function",
                    getMetadata()
            );
        }

        this.hasNext = false;

        Item pathItem = this.pathIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        Item optionsItem = this.optionsIterator != null
            ? this.optionsIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution)
            : null;

        if (pathItem == null) {
            return null;
        }

        URI uri;

        // We use the GSON reader when the function is called without the options parameter
        if (optionsItem == null) {
            System.err.println("[DEBUG] called with one argument");
            try {
                uri = FileSystemUtil.resolveURI(
                    this.staticURI,
                    pathItem.getStringValue(),
                    getMetadata()
                );
                InputStream is = FileSystemUtil.getDataInputStream(
                    uri,
                    this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                    getMetadata()
                );
                JsonReader object = new JsonReader(new InputStreamReader(is));
                return ItemParser.getItemFromObject(object, getMetadata());
            } catch (CannotRetrieveResourceException e) {
                throw new UnavailableResourceException(e.getMessage(), getMetadata());
            } catch (RumbleException e) {
                throw new InvalidJSONException(e.getMessage(), getMetadata());
            }
        }
        JSONParsingOptions options = JSONParsingOptions.resolveOptions(optionsItem, getMetadata());
        System.err.println(options);

        uri = resolveJsonDocURI(pathItem.getStringValue(), getMetadata());
        String jsonText = readJsonResource(uri);

        return ItemParser.getItemFromJSONDocument(jsonText, options, getConfiguration().getXmlVersion(), getMetadata());
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
            throw new UnavailableResourceException(e.getMessage(), metadata);
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
            throw new UnavailableResourceException(e.getMessage(), getMetadata());
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
