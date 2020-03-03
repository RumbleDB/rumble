package sparksoniq.spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.parsing.ItemParser;

import java.util.ArrayList;
import java.util.List;

public class DataFrameUtils {
    public static Dataset<Row> convertItemRDDToDataFrame(
            JavaRDD<Item> itemRDD,
            ObjectItem schemaItem,
            ExceptionMetadata metadata
    ) {
        validateSchemaAgainstData(schemaItem, (ObjectItem) itemRDD.take(1).get(0), metadata);
        StructType schema = generateSchemaFromSchemaItem(schemaItem, metadata);
        JavaRDD<Row> rowRDD = itemRDD.map((Function<Item, Row>) item -> ItemParser.getRowFromItem(item));
        try {
            Dataset<Row> result = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(rowRDD, schema);
            result.take(1);
            return result;
        } catch (ClassCastException | IllegalArgumentException ex) {
            throw new MLInvalidDataFrameSchemaException(ex.getMessage(), metadata);
        }
    }

    public static Dataset<Row> convertLocalItemsToDataFrame(
            List<Item> items,
            ObjectItem schemaItem,
            ExceptionMetadata metadata
    ) {
        validateSchemaAgainstData(schemaItem, (ObjectItem) items.get(0), metadata);
        StructType schema = generateSchemaFromSchemaItem(schemaItem, metadata);
        List<Row> rows = ItemParser.getRowsFromItems(items);
        try {
            Dataset<Row> result = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
            result.take(1);
            return result;
        } catch (ClassCastException | IllegalArgumentException ex) {
            throw new MLInvalidDataFrameSchemaException(ex.getMessage(), metadata);
        }
    }

    private static void validateSchemaAgainstData(
            ObjectItem schemaItem,
            ObjectItem dataItem,
            ExceptionMetadata metadata
    ) {
        for (String schemaColumn : schemaItem.getKeys()) {
            if (!dataItem.getKeys().contains(schemaColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "annotate() schema must fully match the columns of input data",
                        metadata
                );
            }
        }
        for (String dataColumn : dataItem.getKeys()) {
            if (!schemaItem.getKeys().contains(dataColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "annotate() schema must fully match the columns of input data",
                        metadata
                );
            }
        }
    }

    private static StructType generateSchemaFromSchemaItem(ObjectItem schemaItem, ExceptionMetadata metadata) {
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : schemaItem.getKeys()) {
                String itemTypeName = schemaItem.getItemByKey(columnName).getStringValue();
                StructField field = DataTypes.createStructField(
                    columnName,
                    getDataTypeFromItemTypeName(itemTypeName),
                    true
                );
                fields.add(field);
            }
        } catch (RuntimeException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Unexpected item type found in the annotate() schema",
                    metadata
            );
        }
        return DataTypes.createStructType(fields);
    }

    private static DataType getDataTypeFromItemTypeName(String itemTypeName) {
        if (itemTypeName.equals("boolean")) {
            return DataTypes.BooleanType;
        } else if (itemTypeName.equals("integer")) {
            return DataTypes.IntegerType;
        } else if (itemTypeName.equals("double")) {
            return DataTypes.DoubleType;
        } else if (itemTypeName.equals("decimal")) {
            return DataTypes.DoubleType;
        } else if (itemTypeName.equals("string")) {
            return DataTypes.StringType;
        } else if (itemTypeName.equals("null")) {
            return DataTypes.NullType;
        } else if (itemTypeName.equals("date")) {
            return DataTypes.DateType;
        } else if (itemTypeName.equals("datetime")) {
            return DataTypes.TimestampType;
        } else {
            throw new RuntimeException("Unexpected item type found.");
        }
    }
}
