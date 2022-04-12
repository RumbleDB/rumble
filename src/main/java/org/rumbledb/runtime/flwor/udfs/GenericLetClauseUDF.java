package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
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
            List<String> columnNames,
            String classSimpleName
    ) {
        this.dataFrameContext = new DataFrameContext(context, schema, columnNames);
        this.expression = expression;
        if (this.expression.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The expression in this clause requires parallel execution, but is itself executed in parallel. Please consider moving it up or unnest it if it is independent on previous FLWOR variables.",
                    this.expression.getMetadata()
            );
        }

        this.nextResult = new ArrayList<>();
        this.classSimpleName = classSimpleName;
    }

    @Override
    public T call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.expression.materialize(this.dataFrameContext.getContext(), this.nextResult);

        return toDFValue();
    }

    // TODO: check if there is a better/safer/faster way to do it
    @SuppressWarnings("unchecked")
    public T toDFValue() {
        // Arity and type check are done by the treat expression that is inserted in the let clause
        // when using the 'as' syntax
        switch (this.classSimpleName) {
            case "String":
                return (T) this.nextResult.get(0).getStringValue();
            case "Integer":
                // TODO: watch out for big integers
                return (T) (Integer) this.nextResult.get(0).getIntegerValue().intValue();
            case "BigDecimal":
                return (T) this.nextResult.get(0).getDecimalValue();
            case "Double":
                return (T) (Double) this.nextResult.get(0).getDoubleValue();
            default:
                throw new OurBadException("Unexpected type in Generic Let UDF");
        }
    }
}
