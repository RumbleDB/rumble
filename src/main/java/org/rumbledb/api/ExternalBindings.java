package org.rumbledb.api;

import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.Name;

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
    private static final ExternalBindings EMPTY = new Builder().build();

    private Map<Name, Binding> variables;

    private ExternalBindings(Map<Name, Binding> variables) {
        this.variables = Collections.unmodifiableMap(new LinkedHashMap<>(variables));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ExternalBindings empty() {
        return EMPTY;
    }

    public boolean isEmpty() {
        return this.variables.isEmpty();
    }

    public interface Binding {
        Kind kind();
    }

    public enum Kind {
        ITEM_SEQUENCE,
        DATAFRAME
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

    public static class Builder {
        private final Map<Name, Binding> variables = new LinkedHashMap<>();

        public Builder variables(Map<Name, Binding> variables) {
            this.variables.clear();
            this.variables.putAll(Objects.requireNonNull(variables, "variables"));
            return this;
        }

        /**
         * Convenience equivalent of binding a singleton sequence of one item.
         */
        public Builder bindItem(String variableName, Item item) {
            return bindItem(Name.createVariableInNoNamespace(variableName), item);
        }

        /**
         * Convenience equivalent of binding a singleton sequence of one item.
         */
        public Builder bindItem(Name variableName, Item item) {
            return bindItems(variableName, List.of(Objects.requireNonNull(item, "item")));
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

        public ExternalBindings build() {
            return new ExternalBindings(this.variables);
        }
    }
}
