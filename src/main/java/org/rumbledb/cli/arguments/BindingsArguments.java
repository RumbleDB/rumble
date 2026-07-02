package org.rumbledb.cli.arguments;

import org.rumbledb.bindings.FileBinding;
import org.rumbledb.bindings.LexicalValue;
import org.rumbledb.bindings.StandardInputBinding;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CliException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public final class BindingsArguments {
    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private ContextItemSource contextItemSource;

    private static final class ContextItemSource {
        @Option(
            names = { "-I", "--context-item" },
            paramLabel = "value",
            description = {
                "Initializes the global context item $$ to the supplied value.",
                "The query must contain the corresponding global variable declaration, e.g. "
                    + "\"declare context item external;\""
            }
        )
        private String value;

        @Option(
            names = {
                "-i",
                "--context-item-input" },
            paramLabel = "path",
            description = "Reads the context item from a file, or from standard input when the path is '-'."
        )
        private String input;
    }

    @Option(
        names = "--context-item-input-format",
        paramLabel = "format",
        description = "Sets the input format to use for parsing the standard input (as text or as a serialized json value)."
    )
    private String contextItemInputFormat;

    @Option(
        names = "--variable",
        paramLabel = "name=value",
        description = {
            "Initializes a global variable to the supplied value.",
            "The query must contain the corresponding global variable declaration, e.g. "
                + "\"declare variable $foo external;\""
        }
    )
    private Map<String, String> literalVariables = new HashMap<>();

    @Option(
        names = "--variable-from-file",
        paramLabel = "name=path",
        description = "Initializes a global variable with a value read from the supplied file."
    )
    private Map<String, String> fileVariables = new HashMap<>();

    /**
     * Converts parsed command-line arguments without reading or interpreting their
     * values.
     */
    public ExternalBindings toExternalBindings() {
        // Check that we don't have duplicated variables defined from different sources
        Set<String> duplicatedVariables = new HashSet<>(literalVariables.keySet());
        duplicatedVariables.retainAll(fileVariables.keySet());

        if (!duplicatedVariables.isEmpty()) {
            throw new CliException(
                    "Cannot define the same variable from both --variable and --variable-from-file: "
                        + String.join(", ", duplicatedVariables)
            );
        }

        ExternalBindings binding = new ExternalBindings();

        for (Map.Entry<String, String> entry : literalVariables.entrySet()) {
            binding.bind(Name.createVariableInNoNamespace(entry.getKey()), new LexicalValue(entry.getValue()));
        }

        for (Map.Entry<String, String> entry : fileVariables.entrySet()) {
            binding.bind(Name.createVariableInNoNamespace(entry.getKey()), new FileBinding(entry.getValue()));
        }

        if (contextItemSource != null) {
            if (contextItemSource.value != null) {
                binding.bind(Name.CONTEXT_ITEM, new LexicalValue(contextItemSource.value));
            } else if (contextItemSource.input != null) {
                if (contextItemSource.input.equals("-")) {
                    binding.bind(Name.CONTEXT_ITEM, new StandardInputBinding(this.contextItemInputFormat));
                } else {
                    binding.bind(Name.CONTEXT_ITEM, new FileBinding(contextItemSource.input));
                }
            }
        }

        return binding;
    }
}
