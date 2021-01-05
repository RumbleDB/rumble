package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class GenericLetClauseUDF<T> implements UDF1<Row, T> {

    private static final long serialVersionUID = 1L;

    private DataFrameContext dataFrameContext;
    private RuntimeIterator expression;
    private String classSimpleName;

    private List<Item> nextResult;

    public GenericLetClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            StructType schema,
            List<String> columnNames
    ) {
        this.dataFrameContext = new DataFrameContext(context, schema, columnNames);
        this.expression = expression;
        this.nextResult = new ArrayList<>();
    }

    @Override
    public T call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.expression.materialize(this.dataFrameContext.getContext(), this.nextResult);

        return toDFValue(classSimpleName);
    }

    // TODO: check if there is a better/safer/faster way to do it
    @SuppressWarnings("unchecked")
    public T toDFValue(String classSimpleName){
        return (T) new Integer(4);
    }
}
