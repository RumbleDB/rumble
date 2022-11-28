package org.rumbledb.runtime.flwor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;

public class FlworDataFrame implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dataset<Row> dataFrame;
    private List<FlworDataFrameColumn> columns;

    public FlworDataFrame(Dataset<Row> dataFrame) {
        this.dataFrame = dataFrame;
        StructType schema = dataFrame.schema();
        this.columns = new ArrayList<>();
        for (String c : schema.fieldNames()) {
            this.columns.add(new FlworDataFrameColumn(c, schema));
        }
    }

    public Dataset<Row> getDataFrame() {
        return this.dataFrame;
    }

    public List<FlworDataFrameColumn> getColumns() {
        return this.columns;
    }
}
