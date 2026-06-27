package org.rumbledb.cli.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class DynamicOptions {
    @Option(
        names = "--variable",
        scope = ScopeType.INHERIT,
        paramLabel = "name=value",
        description = {
            "Initializes a global variable to the supplied value.",
            "The query must contain the corresponding global variable declaration, e.g. "
                + "\"declare variable $foo external;\""
        }
    )
    private Map<String, String> variables = new HashMap<>();

    @Option(
        names = "--variable-from-file",
        scope = ScopeType.INHERIT,
        paramLabel = "name=path",
        description = "Initializes a global variable with a value read from the supplied file."
    )
    private Map<String, String> variablesFromFiles = new HashMap<>();

    @Option(
        names = "--output-format-option",
        scope = ScopeType.INHERIT,
        paramLabel = "name=value",
        description = "Options to further specify the output format, for example a separator character for CSV or a compression format."
    )
    private Map<String, String> outputFormatOptions = new HashMap<>();

    public static String[] normalizeLegacyDynamicOptions(String[] args) {
        List<String> normalizedArguments = new ArrayList<>(args.length);
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            String normalizedOptionName = getNormalizedDynamicOptionName(argument);
            if (normalizedOptionName == null) {
                normalizedArguments.add(argument);
                continue;
            }
            if (i + 1 >= args.length) {
                normalizedArguments.add(argument);
                continue;
            }
            String value = args[++i];
            normalizedArguments.add(normalizedOptionName);
            normalizedArguments.add(getDynamicOptionSuffix(argument) + "=" + value);
        }
        return normalizedArguments.toArray(new String[0]);
    }

    private static String getNormalizedDynamicOptionName(String argument) {
        if (argument.startsWith("--variable-from-file:")) {
            return "--variable-from-file";
        }
        if (argument.startsWith("--variable:")) {
            return "--variable";
        }
        if (argument.startsWith("--output-format-option:")) {
            return "--output-format-option";
        }
        return null;
    }

    private static String getDynamicOptionSuffix(String argument) {
        return argument.substring(argument.indexOf(':') + 1);
    }
}
