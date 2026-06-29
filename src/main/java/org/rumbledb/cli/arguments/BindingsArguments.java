package org.rumbledb.cli.arguments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.rumbledb.config.model.BindingsConfig;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CliException;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public final class BindingsArguments {
    private static final String DEFAULT_CONTEXT_ITEM_INPUT_FORMAT = "json";

    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private ContextItemSource contextItemSource = new ContextItemSource();

    private static final class ContextItemSource {
        @ArgGroup(exclusive = true, multiplicity = "1")
        private ContextItemValue contextItemValue = new ContextItemValue();

        @ArgGroup(exclusive = true, multiplicity = "1")
        private ContextItemInput contextItemInput = new ContextItemInput();
    }

    private static final class ContextItemValue {
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
    }

    private static final class ContextItemInput {
        @Option(
            names = { "-i", "--context-item-input" },
            paramLabel = "path",
            description = "Reads the context item value from the standard input."
        )
        private String value;
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
    private Map<String, String> variables = new HashMap<>();

    @Option(
        names = "--variable-from-file",
        paramLabel = "name=path",
        description = "Initializes a global variable with a value read from the supplied file."
    )
    private Map<String, String> variablesFromFiles = new HashMap<>();

    public BindingsConfig toConfig() {
        if (hasContextItemValue() && hasContextItemInput()) {
            throw new CliException("It is not possible to both set --context-item and --context-item-input.");
        }

        Map<Name, String> unparsedExternalVariableValues = new HashMap<>();
        this.variables.forEach(
            (name, value) -> unparsedExternalVariableValues.put(Name.createVariableInNoNamespace(name), value)
        );

        if (hasContextItemValue()) {
            unparsedExternalVariableValues.put(Name.CONTEXT_ITEM, this.contextItemSource.contextItemValue.value);
        }

        Map<Name, String> externalVariableValuesReadFromFiles = new HashMap<>();
        this.variablesFromFiles.forEach(
            (name, value) -> externalVariableValuesReadFromFiles.put(Name.createVariableInNoNamespace(name), value)
        );

        Set<Name> externalVariablesReadFromStandardInput = new HashSet<>();
        if (hasContextItemInput()) {
            if ("-".equals(this.contextItemSource.contextItemInput.value)) {
                externalVariablesReadFromStandardInput.add(Name.CONTEXT_ITEM);
            } else {
                externalVariableValuesReadFromFiles.put(
                    Name.CONTEXT_ITEM,
                    this.contextItemSource.contextItemInput.value
                );
            }
        }

        Map<Name, String> externalVariablesInputFormats = new HashMap<>();
        externalVariablesInputFormats.put(
            Name.CONTEXT_ITEM,
            Objects.requireNonNullElse(
                this.contextItemInputFormat,
                DEFAULT_CONTEXT_ITEM_INPUT_FORMAT
            )
        );

        return BindingsConfig.builder()
            .unparsedExternalVariableValues(unparsedExternalVariableValues)
            .externalVariableValuesReadFromFiles(externalVariableValuesReadFromFiles)
            .externalVariablesReadFromStandardInput(externalVariablesReadFromStandardInput)
            .externalVariablesInputFormats(externalVariablesInputFormats)
            .build();
    }

    private boolean hasContextItemValue() {
        return this.contextItemSource != null
            && this.contextItemSource.contextItemValue != null
            && this.contextItemSource.contextItemValue.value != null;
    }

    private boolean hasContextItemInput() {
        return this.contextItemSource != null
            && this.contextItemSource.contextItemInput != null
            && this.contextItemSource.contextItemInput.value != null;
    }
}
