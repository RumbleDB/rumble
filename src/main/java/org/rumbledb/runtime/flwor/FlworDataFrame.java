/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.flwor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.UDFRegistration;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;
import org.rumbledb.runtime.flwor.tuple.FlworTuple;
import org.rumbledb.types.SequenceType;

@Log4j2
public class FlworDataFrame implements RuntimeDataFrame<FlworTuple>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Dataset<Row> dataFrame;

    @Getter
    private List<FlworDataFrameColumn> columns;

    private FLWORSchema flworSchema;

    public FlworDataFrame(Dataset<Row> dataFrame) {
        this(
                dataFrame,
                FLWORSchema.inferFromSparkDataFrameSchema(
                        requireDataFrame(dataFrame).schema()));
    }

    private static Dataset<Row> requireDataFrame(Dataset<Row> dataFrame) {
        if (dataFrame == null) {
            throw new OurBadException("The dataframe cannot be null!");
        }
        return dataFrame;
    }

    public FlworDataFrame(Dataset<Row> dataFrame, FLWORSchema schema) {
        this.dataFrame = requireDataFrame(dataFrame);
        if (!schema.isConsistent(dataFrame)) {
            throw new OurBadException("Inconsistent FLWOR schema.");
        }
        this.flworSchema = new FLWORSchema(schema);
        this.columns = new ArrayList<>();
        // Keep physical order, including when one variable has multiple representations.
        Map<String, FlworDataFrameColumn> byColumn = new java.util.HashMap<>();
        schema.getVariables().forEach((name, variable) -> variable.columns()
                .forEach(binding -> byColumn.put(binding.columnName(), new FlworDataFrameColumn(name, binding))));
        for (String column : dataFrame.schema().fieldNames()) {
            this.columns.add(byColumn.get(column));
        }
    }

    /** Returns an independent snapshot of the stored physical and logical schema. */
    public FLWORSchema getSchema() {
        return new FLWORSchema(this.flworSchema);
    }

    public boolean isConsistent() {
        return getSchema().isConsistent(this.dataFrame);
    }

    @Override
    public JavaRDD<FlworTuple> toRDD(ExceptionMetadata metadata) {
        throw new OurBadException("Converting a FLWOR DataFrame to an RDD of tuples is not implemented.", metadata);
    }

    public List<Name> getVariableNames() {
        return new ArrayList<>(this.flworSchema.getVariables().keySet());
    }

    public boolean hasVariableName(Name name) {
        return this.flworSchema.getVariables().containsKey(name);
    }

    public UDFRegistration getUDFRegistration() {
        return this.dataFrame.sparkSession().udf();
    }

    public FlworDataFrame sql(String sqlQuery) {
        Dataset<Row> rows = this.dataFrame.sparkSession().sql(sqlQuery);
        FLWORSchema outputSchema =
                getSchema().project(Arrays.asList(rows.schema().fieldNames()));
        return new FlworDataFrame(rows, outputSchema);
    }

    public Dataset<Row> sqlRaw(String sqlQuery) {
        return this.dataFrame.sparkSession().sql(sqlQuery);
    }

    public List<FlworDataFrameColumn> getColumns(
            Map<Name, DynamicContext.VariableDependency> dependencies,
            List<Name> variablesToRestrictTo,
            List<Name> variablesToExclude) {
        List<FlworDataFrameColumn> result = new ArrayList<>();
        if (dependencies == null) {
            for (FlworDataFrameColumn column : this.columns) {
                Name name = column.getVariableName();
                if ((variablesToRestrictTo == null || variablesToRestrictTo.contains(name))
                        && (variablesToExclude == null || !variablesToExclude.contains(name))) {
                    result.add(column);
                }
            }
            return result;
        }
        for (Map.Entry<Name, DynamicContext.VariableDependency> entry : dependencies.entrySet()) {
            Name name = entry.getKey();
            if ((variablesToRestrictTo != null && !variablesToRestrictTo.contains(name))
                    || (variablesToExclude != null && variablesToExclude.contains(name))) {
                continue;
            }
            List<FLWORSchema.ColumnBinding> bindings = this.flworSchema.getColumns(name);
            List<FlworDataFrameColumn.ColumnFormat> formats = new ArrayList<>();
            if (entry.getValue() != DynamicContext.VariableDependency.FULL) {
                formats.add(FlworDataFrameColumn.ColumnFormat.valueOf(
                        entry.getValue().name()));
            } else {
                formats.add(FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE);
                formats.add(FlworDataFrameColumn.ColumnFormat.SERIALIZED_SEQUENCE);
            }
            formats.add(FlworDataFrameColumn.ColumnFormat.NATIVE_SEQUENCE);
            formats.add(FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE);
            formats.add(FlworDataFrameColumn.ColumnFormat.SERIALIZED_SEQUENCE);
            FLWORSchema.ColumnBinding selected = null;
            for (FlworDataFrameColumn.ColumnFormat format : formats) {
                selected = bindings.stream()
                        .filter(c -> c.representation() == format)
                        .findFirst()
                        .orElse(null);
                if (selected != null) break;
            }
            if (selected == null) {
                throw new OurBadException("No column for " + entry.getValue() + " dependency on " + name);
            }
            result.add(new FlworDataFrameColumn(name, selected));
        }
        return result;
    }

    public void setVariableType(Name name, SequenceType type) {
        if (!hasVariableName(name)) {
            throw new OurBadException("Variable " + name + "does not exist!");
        }
        this.flworSchema.setVariableType(name, type);
    }

    public SequenceType getVariableType(Name name) {
        if (!hasVariableName(name)) {
            throw new OurBadException("Variable " + name + "does not exist!");
        }
        return this.flworSchema.getVariableType(name);
    }

    public String createTempView() {
        return FlworDataFrameUtils.createTempView(this.dataFrame);
    }

    public void show() {
        StringBuilder sb = new StringBuilder();
        sb.append("FLWOR DataFrame\n");
        sb.append("Columns\n");
        for (FlworDataFrameColumn c : this.columns) {
            sb.append(c).append('\n');
        }
        sb.append("Column types\n");
        for (Name n : getVariableNames()) {
            sb.append(n).append(' ').append(getVariableType(n)).append('\n');
        }
        sb.append("Data Frame");
        log.debug(sb);
        this.dataFrame.show();
    }
}
