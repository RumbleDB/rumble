package org.rumbledb.items.parsing;

import lombok.Getter;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Getter
public final class JSONParsingOptions {

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
    public static final boolean DEFAULT_ESCAPE = false;
    public static final Function<String, String> DEFAULT_FALLBACK = s -> "\uFFFD";

    // RumbleDB-specific extension, not part of the W3C specification.
    public static final boolean DEFAULT_LEGACY = false;

    private final boolean liberal;
    private final String duplicates;
    private final boolean escape;
    private final String numberFormat;
    private final Function<String, String> fallback;
    private final boolean legacy;

    private JSONParsingOptions(
            boolean liberal,
            String duplicates,
            boolean escape,
            Function<String, String> fallback,
            String numberFormat,
            boolean legacy
    ) {
        this.liberal = liberal;
        this.duplicates = duplicates;
        this.escape = escape;
        this.fallback = fallback;
        this.numberFormat = numberFormat;
        this.legacy = legacy;
    }

    public static JSONParsingOptions defaultInstance(boolean isJSONiq10) {
        return new JSONParsingOptions(
                DEFAULT_LIBERAL,
                DEFAULT_DUPLICATES,
                DEFAULT_ESCAPE,
                DEFAULT_FALLBACK,
                getDefaultNumberFormat(isJSONiq10),
                DEFAULT_LEGACY
        );
    }

    @Override
    public String toString() {
        return "[ liberal: "
            + this.liberal
            + ", duplicates: "
            + this.duplicates
            + ", escape: "
            + this.escape
            + ", fallback: "
            + this.fallback
            + ", legacy: "
            + this.legacy
            + "]";
    }

    private static String callFallbackFunction(
            Item functionItem,
            String escapedSequence,
            DynamicContext dynamicContext,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata
    ) {
        try {
            List<Name> parameterNames = functionItem.getParameterNames();
            if (parameterNames == null || parameterNames.size() != 1) {
                throw new InvalidJSONException(
                        "Invalid value for option 'fallback': expected a function of arity 1.",
                        metadata
                );
            }

            RuntimeStaticContext callContext = new RuntimeStaticContext(
                    staticContext.getConfiguration(),
                    SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    metadata
            );

            List<RuntimeIterator> arguments = new ArrayList<>(1);
            arguments.add(new StringRuntimeIterator(escapedSequence, callContext));
            RuntimeIterator call = NamedFunctions.buildFunctionItemCallIterator(
                functionItem,
                callContext,
                ExecutionMode.LOCAL,
                arguments,
                false
            );

            List<Item> results = call.materialize(dynamicContext);
            if (results.size() != 1 || !results.get(0).isString()) {
                throw new InvalidJSONException(
                        "Invalid result returned by option 'fallback': expected exactly one xs:string.",
                        metadata
                );
            }

            return results.get(0).getStringValue();
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
            boolean isJSONiq10,
            DynamicContext dynamicContext,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata
    ) {
        boolean liberal = JSONParsingOptions.DEFAULT_LIBERAL;
        String duplicates = JSONParsingOptions.DEFAULT_DUPLICATES;

        boolean escape = JSONParsingOptions.DEFAULT_ESCAPE;
        boolean escapeExplicitlySet = false;

        String numberFormat = JSONParsingOptions.getDefaultNumberFormat(isJSONiq10);

        Function<String, String> fallback = JSONParsingOptions.DEFAULT_FALLBACK;
        boolean fallbackExplicitlySet = false;

        boolean legacy = JSONParsingOptions.DEFAULT_LEGACY;

        if (optionsItem == null) {
            return JSONParsingOptions.defaultInstance(isJSONiq10);
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
                        isJSONiq10,
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
                        isJSONiq10,
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

                    fallback = s -> JSONParsingOptions.callFallbackFunction(
                        functionItem,
                        s,
                        dynamicContext,
                        staticContext,
                        metadata
                    );
                    fallbackExplicitlySet = true;
                    break;
                }
                case "legacy":
                    legacy = requireSingleBooleanOption("legacy", sequence, metadata);
                    break;
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

        if (legacy) {
            boolean othersAreDefault = liberal == JSONParsingOptions.DEFAULT_LIBERAL
                && JSONParsingOptions.DEFAULT_DUPLICATES.equals(duplicates)
                && escape == JSONParsingOptions.DEFAULT_ESCAPE
                && !fallbackExplicitlySet
                && numberFormat.equals(JSONParsingOptions.getDefaultNumberFormat(isJSONiq10));
            if (!othersAreDefault) {
                throw new InvalidOptionException(
                        "Invalid options: option 'legacy' can only be combined with default values "
                            + "for 'liberal', 'duplicates', 'escape', 'fallback', and 'number-format'.",
                        metadata
                );
            }
        }

        return new JSONParsingOptions(liberal, duplicates, escape, fallback, numberFormat, legacy);
    }

    private static String validatedStringOption(
            String optionName,
            String optionValue,
            boolean isJSONiq10,
            ExceptionMetadata metadata
    ) {
        if (optionValue == null) {
            if (optionName.equals("number-format")) {
                return JSONParsingOptions.getDefaultNumberFormat(isJSONiq10);
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
        if (sequence == null || sequence.size() != 1 || sequence.get(0) == null) {
            throw new UnexpectedTypeException(
                    "Invalid value for option '" + optionName + "': expected exactly one xs:string.",
                    metadata
            );
        }
        Item value = sequence.get(0);
        if (value.isString() || value.isNode()) {
            return value.getStringValue();
        }
        throw new UnexpectedTypeException(
                "Invalid value for option '" + optionName + "': expected exactly one xs:string.",
                metadata
        );
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

    private static String getDefaultNumberFormat(boolean isJSONiq10) {
        if (isJSONiq10)
            return JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE;
        return JSONParsingOptions.NUMBER_FORMAT_DOUBLE;
    }

}
