package org.rumbledb.bindings;

import java.util.Objects;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public final class DataFrameBinding implements Binding {
    private static final long serialVersionUID = 1L;

    Dataset<Row> dataFrame;

    public DataFrameBinding(Dataset<Row> dataFrame) {
        this.dataFrame = Objects.requireNonNull(dataFrame, "dataFrame");
    }
}
