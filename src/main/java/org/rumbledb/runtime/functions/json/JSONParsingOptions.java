package org.rumbledb.runtime.functions.json;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class JSONParsingOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DUPLICATES_REJECT = "reject";
    public static final String DUPLICATES_USE_FIRST = "use-first";
    public static final String DUPLICATES_USE_LAST = "use-last";
    public static final List<String> DUPLICATES_OPTIONS = Arrays.asList(
        DUPLICATES_REJECT,
        DUPLICATES_USE_FIRST,
        DUPLICATES_USE_LAST
    );

    public static final String NUMBER_FORMAT_DOUBLE = "double";
    public static final String NUMBER_FORMAT_ADAPTIVE = "adaptive";
    public static final String NUMBER_FORMAT_DECIMAL = "decimal";
    public static final List<String> NUMBER_FORMAT_OPTIONS = Arrays.asList(
        NUMBER_FORMAT_ADAPTIVE,
        NUMBER_FORMAT_DOUBLE,
        NUMBER_FORMAT_DECIMAL
    );

    // Default Values as per W3C xpath-functions-31 specification, section 17.5.1
    public static final boolean DEFAULT_LIBERAL = false;
    public static final String DEFAULT_DUPLICATES = DUPLICATES_USE_FIRST;
    public static final boolean DEFAULT_ESCAPE = true;
    public static final Function<String, String> DEFAULT_FALLBACK = s -> "\uFFFD";

    private final boolean liberal;
    private final String duplicates;
    private final boolean escape;
    private final String numberFormat;
    private final Function<String, String> fallback;

    private JSONParsingOptions(
            boolean liberal,
            String duplicates,
            boolean escape,
            Function<String, String> fallback,
            String numberFormat
    ) {
        this.liberal = liberal;
        this.duplicates = duplicates;
        this.escape = escape;
        this.fallback = fallback;
        this.numberFormat = numberFormat;
    }

    public static JSONParsingOptions defaultInstance(boolean isJSONiq) {
        return new JSONParsingOptions(
                DEFAULT_LIBERAL,
                DEFAULT_DUPLICATES,
                DEFAULT_ESCAPE,
                DEFAULT_FALLBACK,
                getDefaultNumberFormat(isJSONiq)
        );
    }

    public boolean isLiberal() {
        return this.liberal;
    }

    public String getDuplicates() {
        return this.duplicates;
    }

    public boolean isEscape() {
        return this.escape;
    }

    public Function<String, String> getFallback() {
        return this.fallback;
    }

    public String getNumberFormat() {
        return this.numberFormat;
    }

    @Override
    public String toString() {
        return "[ liberal: "
            + liberal
            + ", duplicates: "
            + duplicates
            + ", escape: "
            + escape
            + ", fallback: "
            + fallback
            + "]";
    }

    // TODO look over this
    private static String callFallbackFunction(Item functionItem, String escapedSequence, ExceptionMetadata metadata) {
        try {
            if (functionItem == null || !functionItem.isFunction()) {
                throw new InvalidJSONException(
                        "Invalid value for option 'fallback': expected a function item.",
                        metadata
                );
            }

            List<Name> parameterNames = functionItem.getParameterNames();
            if (parameterNames == null || parameterNames.size() != 1) {
                throw new InvalidJSONException(
                        "Invalid value for option 'fallback': expected a function of arity 1.",
                        metadata
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

            Item result;
            try {
                result = functionItem.getBodyIterator().materializeAtMostOneItemOrNull(functionContext);
                if (result == null || !result.isString()) {
                    throw new InvalidJSONException(
                            "Invalid result returned by option 'fallback': expected exactly one xs:string.",
                            metadata
                    );
                }
            } catch (MoreThanOneItemException e) {
                InvalidJSONException ex = new InvalidJSONException(
                        "Invalid result returned by option 'fallback': expected exactly one xs:string.",
                        metadata
                );
                ex.initCause(e);
                throw ex;
            }

            return result.getStringValue();
        } catch (RumbleException e) {
            throw e;
        } catch (Exception e) {
            InvalidJSONException ex = new InvalidJSONException(
                    "An error occurred while invoking the function supplied in option 'fallback'.",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }

    public static JSONParsingOptions resolveOptions(
            Item optionsItem,
            boolean isJSONiq,
            ExceptionMetadata metadata
    ) {
        boolean liberal = JSONParsingOptions.DEFAULT_LIBERAL;
        String duplicates = JSONParsingOptions.DEFAULT_DUPLICATES;

        boolean escape = false;
        boolean escapeExplicitlySet = false;

        String numberFormat = JSONParsingOptions.getDefaultNumberFormat(isJSONiq);

        Function<String, String> fallback = JSONParsingOptions.DEFAULT_FALLBACK;
        boolean fallbackExplicitlySet = false;

        if (optionsItem == null) {
            return JSONParsingOptions.defaultInstance(isJSONiq);
        }

        if (!optionsItem.isMap()) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:json-doc() must be a map.",
                    metadata
            );
        }

        List<String> keys = optionsItem.getStringKeys();
        List<List<Item>> values = optionsItem.getSequenceValues();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            List<Item> sequence = values.get(i);

            switch (key) {
                case "liberal":
                    liberal = requireSingleBooleanOption("liberal", sequence, metadata);
                    break;

                case "duplicates":
                    duplicates = validatedStringOption(
                        "duplicates",
                        requireSingleStringOption("duplicates", sequence, metadata),
                        isJSONiq,
                        metadata
                    );
                    break;

                case "escape":
                    escape = requireSingleBooleanOption("escape", sequence, metadata);
                    escapeExplicitlySet = true;
                    break;
                case "number-format":
                    numberFormat = validatedStringOption(
                        "number-format",
                        requireSingleStringOption("number-format", sequence, metadata),
                        isJSONiq,
                        metadata
                    );
                    break;
                case "fallback": {
                    Item functionItem = requireSingleFunctionOption(sequence, metadata);

                    List<Name> parameterNames = functionItem.getParameterNames();
                    if (parameterNames == null || parameterNames.size() != 1) {
                        throw new UnexpectedTypeException(
                                "Invalid value for option 'fallback': expected a function of arity 1.",
                                metadata
                        );
                    }

                    final Item capturedFunction = functionItem;
                    fallback = s -> JSONParsingOptions.callFallbackFunction(capturedFunction, s, metadata);
                    fallbackExplicitlySet = true;
                    break;
                }
                default:
                    break;
            }
        }

        if (escapeExplicitlySet && escape && fallbackExplicitlySet) {
            throw new InvalidOptionException(
                    "Invalid options: option 'fallback' cannot be supplied when option 'escape' is true.",
                    metadata
            );
        }

        JSONParsingOptions options = new JSONParsingOptions(liberal, duplicates, escape, fallback, numberFormat);
        return options;
    }

    private static String validatedStringOption(
            String optionName,
            String optionValue,
            boolean isJSONiq,
            ExceptionMetadata metadata
    ) {
        if (optionValue == null) {
            if (optionName.equals("number-format")) {
                return JSONParsingOptions.getDefaultNumberFormat(isJSONiq);
            }
            return JSONParsingOptions.DEFAULT_DUPLICATES;
        }
        List<String> optionValues;
        if (optionName.equals("number-format"))
            optionValues = JSONParsingOptions.NUMBER_FORMAT_OPTIONS;
        else
            optionValues = JSONParsingOptions.DUPLICATES_OPTIONS;
        for (String possibleValue : optionValues) {
            if (optionValue.equals(possibleValue))
                return optionValue;
        }
        throw new InvalidOptionException(
                "Invalid value for option '"
                    + optionName
                    + "': expected one of "
                    + optionValues
                    + ", but got '"
                    + optionValue
                    + "'.",
                metadata
        );
    }

    private static boolean requireSingleBooleanOption(
            String optionName,
            List<Item> sequence,
            ExceptionMetadata metadata
    ) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isBoolean()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one xs:boolean.",
                    metadata
            );
        }
        return sequence.get(0).getBooleanValue();
    }

    private static String requireSingleStringOption(
            String optionName,
            List<Item> sequence,
            ExceptionMetadata metadata
    ) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one xs:string.",
                    metadata
            );
        }
        return sequence.get(0).getStringValue();
    }

    private static Item requireSingleFunctionOption(
            List<Item> sequence,
            ExceptionMetadata metadata
    ) {
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option 'fallback': expected exactly one function item.",
                    metadata
            );
        }
        return sequence.get(0);
    }

    private static String getDefaultNumberFormat(boolean isJSONiq) {
        if (isJSONiq)
            return JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE;
        return JSONParsingOptions.NUMBER_FORMAT_DOUBLE;
    }

}
