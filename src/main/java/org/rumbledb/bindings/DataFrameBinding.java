package org.rumbledb.bindings;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Objects;

@Value
@NoArgsConstructor(force = true)
public final class DataFrameBinding implements Binding {
    private static final long serialVersionUID = 1L;

    Dataset<Row> dataFrame;

    public DataFrameBinding(Dataset<Row> dataFrame) {
        this.dataFrame = Objects.requireNonNull(dataFrame, "dataFrame");
    }
}
