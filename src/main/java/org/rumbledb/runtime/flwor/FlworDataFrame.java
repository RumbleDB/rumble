package org.rumbledb.runtime.flwor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.UDFRegistration;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.SequenceType;

public class FlworDataFrame implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dataset<Row> dataFrame;
    private List<FlworDataFrameColumn> columns;
    private Map<Name, SequenceType> columnTypes;

    public FlworDataFrame(Dataset<Row> dataFrame) {
        if (dataFrame == null) {
            throw new OurBadException("The dataframe cannot be null!");
        }
        this.dataFrame = dataFrame;
        StructType schema = dataFrame.schema();
        this.columns = new ArrayList<>();
        this.columnTypes = new HashMap<>();
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

    public List<Name> getVariableNames() {
        List<Name> result = new ArrayList<>();
        for (FlworDataFrameColumn c : getColumns()) {
            result.add(c.getVariableName());
        }
        return result;
    }

    public boolean hasVariableName(Name name) {
        for (FlworDataFrameColumn c : getColumns()) {
            if (name.equals(c.getVariableName()))
                return true;
        }
        return false;
    }

    public UDFRegistration getUDFRegistration() {
        return this.dataFrame.sparkSession().udf();
    }

    public FlworDataFrame sql(String sqlQuery) {
        FlworDataFrame result = new FlworDataFrame(
                this.dataFrame.sparkSession()
                    .sql(sqlQuery)
        );
        for (Name v : this.getVariableNames()) {
            SequenceType type = this.getVariableType(v);
            if (result.hasVariableName(v)) {
                result.setVariableType(v, type);
            }
        }
        return result;
    }

    public Dataset<Row> sqlRaw(String sqlQuery) {
        return this.dataFrame.sparkSession()
            .sql(sqlQuery);
    }

    public List<FlworDataFrameColumn> getColumns(
            Map<Name, DynamicContext.VariableDependency> dependencies,
            List<Name> variablesToRestrictTo,
            List<Name> variablesToExclude
    ) {
        return FlworDataFrameUtils.getColumns(
            this.dataFrame.schema(),
            dependencies,
            variablesToRestrictTo,
            variablesToExclude
        );
    }

    public void setVariableType(Name name, SequenceType type) {
        if (!hasVariableName(name)) {
            throw new OurBadException("Variable " + name + "does not exist!");
        }
        this.columnTypes.put(name, type);
    }

    public SequenceType getVariableType(Name name) {
        if (!hasVariableName(name)) {
            throw new OurBadException("Variable " + name + "does not exist!");
        }
        if (!this.columnTypes.containsKey(name)) {
            return SequenceType.ITEM_STAR;
        }
        return this.columnTypes.get(name);
    }

    public String createTempView() {
        return FlworDataFrameUtils.createTempView(this.dataFrame);
    }


}
