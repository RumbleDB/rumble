package org.rumbledb.api;

import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.Name;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Query-scoped external bindings supplied at execution time.
 *
 * This is intentionally separate from {@code RumbleConfiguration}: bindings are execution inputs, not engine
 * configuration. The model is broader than what the current runtime bridge can consume so the public API can settle
 * before the remaining plumbing is migrated away from {@code RumbleRuntimeConfiguration}.
 */
@Value
@Accessors(fluent = true)
public class ExternalBindings {
    public static final String INPUT_FORMAT_JSON = "json";
    public static final String INPUT_FORMAT_TEXT = "text";

    private static final ExternalBindings EMPTY = new Builder().build();

    private Map<Name, Binding> variables;
    private Binding contextItem;

    private ExternalBindings(Map<Name, Binding> variables, Binding contextItem) {
        this.variables = Collections.unmodifiableMap(new LinkedHashMap<>(variables));
        this.contextItem = contextItem;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ExternalBindings empty() {
        return EMPTY;
    }

    public boolean isEmpty() {
        return this.variables.isEmpty() && this.contextItem == null;
    }

    public interface Binding {
        Kind kind();
    }

    public enum Kind {
        ITEM_SEQUENCE,
        DATAFRAME,
        LEXICAL_VALUE,
        URI_SOURCE,
        STDIN_SOURCE
    }

    @Value
    public static class ItemSequenceBinding implements Binding {
        List<Item> items;

        public ItemSequenceBinding(List<Item> items) {
            this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        }

        @Override
        public Kind kind() {
            return Kind.ITEM_SEQUENCE;
        }
    }

    @Value
    public static class DataFrameBinding implements Binding {
        Dataset<Row> dataFrame;

        public DataFrameBinding(Dataset<Row> dataFrame) {
            this.dataFrame = Objects.requireNonNull(dataFrame, "dataFrame");
        }

        @Override
        public Kind kind() {
            return Kind.DATAFRAME;
        }
    }

    @Value
    public static class LexicalValueBinding implements Binding {
        String value;

        public LexicalValueBinding(String value) {
            this.value = Objects.requireNonNull(value, "value");
        }

        @Override
        public Kind kind() {
            return Kind.LEXICAL_VALUE;
        }
    }

    @Value
    public static class UriSourceBinding implements Binding {
        URI uri;
        String inputFormat;

        public UriSourceBinding(URI uri, String inputFormat) {
            this.uri = Objects.requireNonNull(uri, "uri");
            this.inputFormat = normalizeInputFormat(inputFormat);
        }

        @Override
        public Kind kind() {
            return Kind.URI_SOURCE;
        }
    }

    @Value
    public static class StdinSourceBinding implements Binding {
        String inputFormat;

        public StdinSourceBinding(String inputFormat) {
            this.inputFormat = normalizeInputFormat(inputFormat);
        }

        @Override
        public Kind kind() {
            return Kind.STDIN_SOURCE;
        }
    }

    public static class Builder {
        private final Map<Name, Binding> variables = new LinkedHashMap<>();
        private Binding contextItem;

        public Builder variables(Map<Name, Binding> variables) {
            this.variables.clear();
            this.variables.putAll(Objects.requireNonNull(variables, "variables"));
            return this;
        }

        /**
         * Replaces {@code RumbleRuntimeConfiguration.setExternalVariableValue(String, List<Item>)}.
         */
        public Builder bindItems(String variableName, List<Item> items) {
            return bindItems(Name.createVariableInNoNamespace(variableName), items);
        }

        /**
         * Replaces {@code RumbleRuntimeConfiguration.setExternalVariableValue(Name, List<Item>)}.
         */
        public Builder bindItems(Name variableName, List<Item> items) {
            this.variables.put(
                Objects.requireNonNull(variableName, "variableName"),
                new ItemSequenceBinding(items)
            );
            return this;
        }

        /**
         * Replaces {@code RumbleRuntimeConfiguration.setExternalVariableValue(String, Dataset<Row>)}.
         */
        public Builder bindDataFrame(String variableName, Dataset<Row> dataFrame) {
            return bindDataFrame(Name.createVariableInNoNamespace(variableName), dataFrame);
        }

        /**
         * Replaces {@code RumbleRuntimeConfiguration.setExternalVariableValue(Name, Dataset<Row>)}.
         */
        public Builder bindDataFrame(Name variableName, Dataset<Row> dataFrame) {
            this.variables.put(
                Objects.requireNonNull(variableName, "variableName"),
                new DataFrameBinding(dataFrame)
            );
            return this;
        }

        /**
         * Replaces CLI-style lexical bindings such as {@code --variable name=value}, which are later read via
         * {@code RumbleRuntimeConfiguration.getUnparsedExternalVariableValue(Name)}.
         */
        public Builder bindLexical(String variableName, String value) {
            return bindLexical(Name.createVariableInNoNamespace(variableName), value);
        }

        /**
         * Replaces CLI-style lexical bindings such as {@code --variable name=value}, which are later read via
         * {@code RumbleRuntimeConfiguration.getUnparsedExternalVariableValue(Name)}.
         */
        public Builder bindLexical(Name variableName, String value) {
            this.variables.put(
                Objects.requireNonNull(variableName, "variableName"),
                new LexicalValueBinding(value)
            );
            return this;
        }

        /**
         * Replaces CLI-style file bindings such as {@code --variable-from-file name=path}, which are later read via
         * {@code RumbleRuntimeConfiguration.getExternalVariableValueReadFromFile(Name)}.
         */
        public Builder bindFromUri(String variableName, URI uri, String inputFormat) {
            return bindFromUri(Name.createVariableInNoNamespace(variableName), uri, inputFormat);
        }

        /**
         * Replaces CLI-style file bindings such as {@code --variable-from-file name=path}, which are later read via
         * {@code RumbleRuntimeConfiguration.getExternalVariableValueReadFromFile(Name)}.
         */
        public Builder bindFromUri(Name variableName, URI uri, String inputFormat) {
            this.variables.put(
                Objects.requireNonNull(variableName, "variableName"),
                new UriSourceBinding(uri, inputFormat)
            );
            return this;
        }

        /**
         * Replaces binding the context item through
         * {@code RumbleRuntimeConfiguration.setExternalVariableValue(Name.CONTEXT_ITEM, List<Item>)}.
         */
        public Builder bindContextItemItems(List<Item> items) {
            this.contextItem = new ItemSequenceBinding(items);
            return this;
        }

        /**
         * Replaces binding the context item through
         * {@code RumbleRuntimeConfiguration.setExternalVariableValue(Name.CONTEXT_ITEM, Dataset<Row>)}.
         */
        public Builder bindContextItemDataFrame(Dataset<Row> dataFrame) {
            this.contextItem = new DataFrameBinding(dataFrame);
            return this;
        }

        /**
         * Replaces {@code --context-item value}, which is later read via
         * {@code RumbleRuntimeConfiguration.getUnparsedExternalVariableValue(Name.CONTEXT_ITEM)}.
         */
        public Builder bindContextItemLexical(String value) {
            this.contextItem = new LexicalValueBinding(value);
            return this;
        }

        /**
         * Replaces {@code --context-item-input path}, together with
         * {@code RumbleRuntimeConfiguration.getInputFormat(Name.CONTEXT_ITEM)}.
         */
        public Builder bindContextItemFromUri(URI uri, String inputFormat) {
            this.contextItem = new UriSourceBinding(uri, inputFormat);
            return this;
        }

        /**
         * Replaces {@code --context-item-input -}, together with
         * {@code RumbleRuntimeConfiguration.readFromStandardInput(Name.CONTEXT_ITEM)} and
         * {@code RumbleRuntimeConfiguration.getInputFormat(Name.CONTEXT_ITEM)}.
         */
        public Builder bindContextItemFromStdin(String inputFormat) {
            this.contextItem = new StdinSourceBinding(inputFormat);
            return this;
        }

        public ExternalBindings build() {
            return new ExternalBindings(this.variables, this.contextItem);
        }
    }

    private static String normalizeInputFormat(String inputFormat) {
        String normalized = Objects.requireNonNullElse(inputFormat, INPUT_FORMAT_JSON).trim().toLowerCase();
        if (!INPUT_FORMAT_JSON.equals(normalized) && !INPUT_FORMAT_TEXT.equals(normalized)) {
            throw new IllegalArgumentException("Unsupported input format: " + inputFormat);
        }
        return normalized;
    }
}
