package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;

import java.util.List;

public class GenericForClauseUDF<T> implements UDF1<Row, List<T>> {

    private static final long serialVersionUID = 1L;

    private DataFrameContext dataFrameContext;
    private RuntimeIterator expression;
    private String classSimpleName;

    private List<T> results;

    public GenericForClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            List<FlworDataFrameColumn> columnNames,
            String classSimpleName
    ) {
        this.dataFrameContext = new DataFrameContext(context, columnNames);
        this.expression = expression;
        this.classSimpleName = classSimpleName;
    }

    @Override
    public List<T> call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();
        // apply expression in the dynamic context
        this.expression.open(this.dataFrameContext.getContext());
        while (this.expression.hasNext()) {
            this.results.add(toDFValue(this.expression.next()));
        }
        this.expression.close();

        return this.results;
    }

    // TODO: check if there is a better/safer/faster way to do it
    @SuppressWarnings("unchecked")
    private T toDFValue(Item item) {
        // Arity and type check are done by the treat expression that is inserted in the let clause
        // when using the 'as' syntax
        switch (this.classSimpleName) {
            case "String":
                return (T) item.getStringValue();
            case "Integer":
                // TODO: watch out for big integers
                return (T) (Integer) item.getIntegerValue().intValue();
            case "BigDecimal":
                return (T) item.getDecimalValue();
            case "Double":
                return (T) (Double) item.getDoubleValue();
            default:
                throw new OurBadException("Unexpected type in Generic Let UDF");
        }
    }
}
