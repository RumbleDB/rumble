package org.rumbledb.runtime.functions.io;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidJSONException;
import org.rumbledb.exceptions.InvalidOptionException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

        JSONOptions options = resolveOptions(optionsItem);

        if (pathItem == null) {
            return null;
        }

        URI uri = resolveJsonDocURI(pathItem.getStringValue(), getMetadata());
        String jsonText = readJsonResource(uri);

        return ItemParser.getItemFromJSONDocument(jsonText, options, getMetadata());
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

    private String validatedDuplicateOption(String duplicates, ExceptionMetadata metadata) {
        if (duplicates == null) {
            return JSONOptions.DEFAULT_DUPLICATES;
        }
        if (
            !duplicates.equals(JSONOptions.DUPLICATES_REJECT)
                && !duplicates.equals(JSONOptions.DUPLICATES_USE_FIRST)
                && !duplicates.equals(JSONOptions.DUPLICATES_USE_LAST)
        ) {
            throw new InvalidOptionException(
                    "Invalid value for option 'duplicates': expected one of ('reject', 'use-first', 'use-last'), but got '"
                        + duplicates
                        + "'.",
                    metadata
            );
        }
        return duplicates;
    }

    private JSONOptions resolveOptions(Item optionsItem) {
        boolean liberal = JSONOptions.DEFAULT_LIBERAL;
        String duplicates = JSONOptions.DEFAULT_DUPLICATES;

        // In the current qt3 behavior you are targeting for json-doc(),
        // "escape" only affects behavior when it is explicitly supplied.
        // So the default here is false, and FOJS0005 only fires if escape:true()
        // is explicitly present together with fallback.
        boolean escape = false;
        boolean escapeExplicitlySet = false;

        Function<String, String> fallback = JSONOptions.DEFAULT_FALLBACK;
        boolean fallbackExplicitlySet = false;

        if (optionsItem == null) {
            return new JSONOptions(liberal, duplicates, escape, fallback);
        }

        if (!optionsItem.isMap()) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:json-doc() must be a map.",
                    getMetadata()
            );
        }

        List<String> keys = optionsItem.getStringKeys();
        List<List<Item>> values = optionsItem.getSequenceValues();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            List<Item> sequence = values.get(i);

            switch (key) {
                case "liberal":
                    liberal = requireSingleBooleanOption("liberal", sequence);
                    break;

                case "duplicates":
                    duplicates = validatedDuplicateOption(
                        requireSingleStringOption("duplicates", sequence),
                        getMetadata()
                    );
                    break;

                case "escape":
                    escape = requireSingleBooleanOption("escape", sequence);
                    escapeExplicitlySet = true;
                    break;

                case "fallback": {
                    Item functionItem = requireSingleFunctionOption("fallback", sequence);

                    List<Name> parameterNames = functionItem.getParameterNames();
                    if (parameterNames == null || parameterNames.size() != 1) {
                        throw new UnexpectedTypeException(
                                "Invalid value for option 'fallback': expected a function of arity 1.",
                                getMetadata()
                        );
                    }

                    final Item capturedFunction = functionItem;
                    fallback = s -> callFallbackFunction(capturedFunction, s);
                    fallbackExplicitlySet = true;
                    break;
                }

                default:
                    // Unknown options are ignored.
                    break;
            }
        }

        if (escapeExplicitlySet && escape && fallbackExplicitlySet) {
            throw new InvalidOptionException(
                    "Invalid options: option 'fallback' cannot be supplied when option 'escape' is true.",
                    getMetadata()
            );
        }

        return new JSONOptions(liberal, duplicates, escape, fallback);
    }

    private boolean requireSingleBooleanOption(String optionName, List<Item> sequence) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isBoolean()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one xs:boolean.",
                    getMetadata()
            );
        }
        return sequence.get(0).getBooleanValue();
    }

    private String requireSingleStringOption(String optionName, List<Item> sequence) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one xs:string.",
                    getMetadata()
            );
        }
        return sequence.get(0).getStringValue();
    }

    private Item requireSingleFunctionOption(String optionName, List<Item> sequence) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one function item.",
                    getMetadata()
            );
        }
        return sequence.get(0);
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

    private String callFallbackFunction(Item functionItem, String escapedSequence) {
        try {
            if (functionItem == null || !functionItem.isFunction()) {
                throw new InvalidJSONException(
                        "Invalid value for option 'fallback': expected a function item.",
                        getMetadata()
                );
            }

            List<Name> parameterNames = functionItem.getParameterNames();
            if (parameterNames == null || parameterNames.size() != 1) {
                throw new InvalidJSONException(
                        "Invalid value for option 'fallback': expected a function of arity 1.",
                        getMetadata()
                );
            }

            DynamicContext functionContext = new DynamicContext(functionItem.getModuleDynamicContext());
            VariableValues variableValues = functionContext.getVariableValues();

            Map<Name, List<Item>> localClosure = functionItem.getLocalVariablesInClosure();
            if (localClosure != null) {
                for (Map.Entry<Name, List<Item>> entry : localClosure.entrySet()) {
                    variableValues.addVariableValue(entry.getKey(), entry.getValue());
                }
            }

            Map<Name, JavaRDD<Item>> rddClosure = functionItem.getRDDVariablesInClosure();
            if (rddClosure != null) {
                for (Map.Entry<Name, JavaRDD<Item>> entry : rddClosure.entrySet()) {
                    variableValues.addVariableValue(entry.getKey(), entry.getValue());
                }
            }

            Map<Name, JSoundDataFrame> dfClosure = functionItem.getDFVariablesInClosure();
            if (dfClosure != null) {
                for (Map.Entry<Name, JSoundDataFrame> entry : dfClosure.entrySet()) {
                    variableValues.addVariableValue(entry.getKey(), entry.getValue());
                }
            }

            Item argument = ItemFactory.getInstance().createStringItem(escapedSequence);
            variableValues.addVariableValue(
                parameterNames.get(0),
                Collections.singletonList(argument)
            );

            List<Item> result = functionItem.getBodyIterator().materialize(functionContext);

            if (result == null || result.size() != 1) {
                throw new InvalidJSONException(
                        "Invalid result returned by option 'fallback': expected exactly one xs:string.",
                        getMetadata()
                );
            }

            Item resultItem = result.get(0);
            if (resultItem == null || !resultItem.isString()) {
                throw new InvalidJSONException(
                        "Invalid result returned by option 'fallback': expected exactly one xs:string, but got "
                            + (resultItem == null ? "empty-sequence()" : resultItem.getDynamicType()),
                        getMetadata()
                );
            }

            return resultItem.getStringValue();
        } catch (RumbleException e) {
            throw e;
        } catch (Exception e) {
            InvalidJSONException ex = new InvalidJSONException(
                    "An error occurred while invoking the function supplied in option 'fallback'.",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
