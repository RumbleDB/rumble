package org.rumbledb.runtime.functions.json;

import com.google.gson.stream.JsonReader;

import java.io.Serial;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.cli.ConsoleOutput;
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
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.functions.io.TextResourceUtil;


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

        URI uri = resolveJsonDocURI(pathItem.getStringValue(), this.staticContext.getStaticURI(), getMetadata());

        String jsonText = readJsonResource(uri, context.getRumbleRuntimeConfiguration(), getMetadata());

        if (options.isLegacy()) {
            ConsoleOutput.warn(
                "Warning: fn:json-doc option 'legacy' skips spec-conformant JSON parsing in favor of "
                    + "RumbleDB's previous Gson-based parser and may produce unexpected results; "
                    + "retry without it if the output looks wrong."
            );
            return ItemParser.getItemFromObject(
                new JsonReader(new StringReader(jsonText)),
                isJSONiq10,
                options.getNumberFormat(),
                getMetadata(),
                false
            );
        }

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
        try {
            byte[] bytes = TextResourceUtil.fetchBytes(uri, conf, metadata);
            return decode(bytes, TextResourceUtil.detectEncoding(bytes), metadata);
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

    private static String decode(byte[] bytes, Charset charset, ExceptionMetadata metadata) {
        try {
            return TextResourceUtil.decodeStrict(bytes, charset);
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
}
