package org.rumbledb.cli.options;

import java.util.ArrayList;
import java.util.List;

public class LegacyCompatibility {

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
