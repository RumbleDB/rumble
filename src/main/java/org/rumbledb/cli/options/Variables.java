package org.rumbledb.cli.options;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.rumbledb.config.ExternalVariableBindings;
import org.rumbledb.context.Name;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Variables {
    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private ContextItemSource contextItemSource;

    @Option(
        names = "--context-item-input-format",
        scope = ScopeType.INHERIT,
        paramLabel = "format",
        description = "Sets the input format to use for parsing the standard input (as text or as a serialized json value)."
    )
    private String contextItemInputFormat;

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

    public ExternalVariableBindings toExternalVariableBindings() {
        Map<Name, String> unparsedExternalVariableValues = new HashMap<>();
        this.variables.forEach(
            (name, value) -> unparsedExternalVariableValues.put(Name.createVariableInNoNamespace(name), value)
        );

        if (this.contextItemSource != null && this.contextItemSource.contextItem != null) {
            unparsedExternalVariableValues.put(Name.CONTEXT_ITEM, this.contextItemSource.contextItem);
        }

        Map<Name, String> externalVariableValuesReadFromFiles = new HashMap<>();
        this.variablesFromFiles.forEach(
            (name, value) -> externalVariableValuesReadFromFiles.put(Name.createVariableInNoNamespace(name), value)
        );

        Set<Name> externalVariablesReadFromStandardInput = new HashSet<>();
        if (this.contextItemSource != null && this.contextItemSource.contextItemInput != null) {
            if ("-".equals(this.contextItemSource.contextItemInput)) {
                externalVariablesReadFromStandardInput.add(Name.CONTEXT_ITEM);
            } else {
                externalVariableValuesReadFromFiles.put(Name.CONTEXT_ITEM, this.contextItemSource.contextItemInput);
            }
        }

        Map<Name, String> externalVariablesInputFormats = new HashMap<>();
        if (this.contextItemInputFormat != null) {
            externalVariablesInputFormats.put(Name.CONTEXT_ITEM, this.contextItemInputFormat);
        }

        return ExternalVariableBindings.builder()
            .unparsedExternalVariableValues(unparsedExternalVariableValues)
            .externalVariableValuesReadFromFiles(externalVariableValuesReadFromFiles)
            .externalVariablesReadFromStandardInput(externalVariablesReadFromStandardInput)
            .externalVariablesInputFormats(externalVariablesInputFormats)
            .build();
    }

    private static final class ContextItemSource {
        @Option(
            names = { "-I", "--context-item" },
            scope = ScopeType.INHERIT,
            paramLabel = "value",
            description = {
                "Initializes the global context item $$ to the supplied value.",
                "The query must contain the corresponding global variable declaration, e.g. "
                    + "\"declare context item external;\""
            }
        )
        private String contextItem;

        @Option(
            names = { "-i", "--context-item-input" },
            scope = ScopeType.INHERIT,
            paramLabel = "path",
            description = "Reads the context item value from the standard input."
        )
        private String contextItemInput;
    }
}
