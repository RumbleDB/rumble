package org.rumbledb.bindings;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;

public interface Binding {
    public default boolean isLexicalBinding() {
        return false;
    }

    public default String getLexicalValue() {
        throw new UnsupportedOperationException("Not a lexical value");
    }

    public default boolean isFileBinding() {
        return false;
    }

    public default String getFileLocation() {
        throw new UnsupportedOperationException("Not a file value");
    }

    public default boolean isStandardInputBinding() {
        return false;
    }

    public default StandardInputBinding.InputFormat getStandardInputFormat() {
        throw new UnsupportedOperationException("Not a standard input value");
    }

    public default boolean isItemSequenceBinding() {
        return false;
    }

    public default List<Item> getItemSequence() {
        throw new UnsupportedOperationException("Not an item sequence value");
    }

    public default boolean isDataFrameBinding() {
        return false;
    }

    public default Dataset<Row> getDataFrame() {
        throw new UnsupportedOperationException("Not a data frame value");
    }
}
