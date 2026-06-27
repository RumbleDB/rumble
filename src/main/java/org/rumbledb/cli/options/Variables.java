package org.rumbledb.cli.options;

import java.util.HashMap;
import java.util.Map;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public class Variables {
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
}
