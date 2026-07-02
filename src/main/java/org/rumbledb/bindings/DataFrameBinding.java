package org.rumbledb.bindings;

import lombok.Value;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import java.util.Objects;

@Value
public class DataFrameBinding implements Binding {
    Dataset<Row> dataFrame;

    public DataFrameBinding(Dataset<Row> dataFrame) {
        this.dataFrame = Objects.requireNonNull(dataFrame, "dataFrame");
    }

    @Override
    public Dataset<Row> getDataFrame() {
        return this.dataFrame;
    }
}
