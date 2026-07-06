package org.rumbledb.api;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.context.Name;

import java.util.List;

public class ExternalBindings {
    private final org.rumbledb.bindings.ExternalBindings bindings;

    public ExternalBindings() {
        this.bindings = org.rumbledb.bindings.ExternalBindings.empty();
    }

    public static ExternalBindings empty() {
        return new ExternalBindings();
    }

    public void bindItem(String variableName, Item item) {
        this.bindItems(variableName, List.of(item));
    }

    public void bindItems(String variableName, List<Item> items) {
        this.bindings.bind(Name.createVariableInNoNamespace(variableName), new ItemSequenceBinding(items));
    }

    public void bindDataFrame(String variableName, Dataset<Row> dataFrame) {
        this.bindings.bind(Name.createVariableInNoNamespace(variableName), new DataFrameBinding(dataFrame));
    }

    public void unbind(String variableName) {
        this.bindings.unbind(Name.createVariableInNoNamespace(variableName));
    }

    org.rumbledb.bindings.ExternalBindings getInternalBindings() {
        return this.bindings;
    }
}
