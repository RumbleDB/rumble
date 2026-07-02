package org.rumbledb.api;

import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.binding.Binding;
import org.rumbledb.api.binding.DataFrameBinding;
import org.rumbledb.api.binding.ItemSequenceBinding;
import org.rumbledb.context.Name;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Name, Binding> variables;

    public ExternalBindings() {
        this.variables = new LinkedHashMap<>();
    }

    public void bindItem(String variableName, Item item) {
        this.bindItems(variableName, List.of(item));
    }

    public void bindItems(String variableName, List<Item> items) {
        Name variable = Name.createVariableInNoNamespace(variableName);
        if (this.variables.containsKey(variable)) {
            throw new IllegalArgumentException("Variable " + variableName + " is already bound.");
        }

        this.variables.put(variable, new ItemSequenceBinding(items));
    }

    public void bindDataFrame(String variableName, Dataset<Row> dataFrame) {
        Name variable = Name.createVariableInNoNamespace(variableName);
        if (this.variables.containsKey(variable)) {
            throw new IllegalArgumentException("Variable " + variableName + " is already bound.");
        }
        this.variables.put(variable, new DataFrameBinding(dataFrame));
    }

    public void unbind(String variableName) {
        this.variables.remove(Name.createVariableInNoNamespace(variableName));
    }
}
