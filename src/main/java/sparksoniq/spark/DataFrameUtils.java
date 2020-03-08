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
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.parsing.ItemParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFrameUtils {
    public static Dataset<Row> convertItemRDDToDataFrame(
            JavaRDD<Item> itemRDD,
            ObjectItem schemaItem,
            boolean forSparkML
    ) {
        ObjectItem firstDataItem = (ObjectItem) itemRDD.take(1).get(0);
        validateSchemaAgainstAnItem(schemaItem, firstDataItem);
        StructType schema = generateDataFrameSchemaFromSchemaItem(schemaItem, forSparkML);
        try {
            JavaRDD<Row> rowRDD = itemRDD.map(
                new Function<Item, Row>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Row call(Item item) {
                        return ItemParser.getRowFromItemUsingSchema(item, schema, forSparkML);
                    }
                }
            );
            return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
        } catch (MLInvalidDataFrameSchemaException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Error while applying the schema; " + ex.getJSONiqErrorMessage()
            );
        }
    }

    public static Dataset<Row> convertLocalItemsToDataFrame(
            List<Item> items,
            ObjectItem schemaItem,
            boolean forSparkML
    ) {
        ObjectItem firstDataItem = (ObjectItem) items.get(0);
        validateSchemaAgainstAnItem(schemaItem, firstDataItem);
        StructType schema = generateDataFrameSchemaFromSchemaItem(schemaItem, forSparkML);
        try {
            List<Row> rows = ItemParser.getRowsFromItemsUsingSchema(items, schema, forSparkML);
            return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
        } catch (MLInvalidDataFrameSchemaException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Error while applying the schema; " + ex.getJSONiqErrorMessage()
            );
        }
    }

    private static void validateSchemaAgainstAnItem(
            ObjectItem schemaItem,
            ObjectItem dataItem
    ) {
        for (String schemaColumn : schemaItem.getKeys()) {
            if (!dataItem.getKeys().contains(schemaColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "Columns defined in schema must fully match the columns of input data: "
                            + "missing type information for '"
                            + schemaColumn
                            + "' column."
                );
            }
        }

        for (String dataColumn : dataItem.getKeys()) {
            if (!schemaItem.getKeys().contains(dataColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "Columns defined in schema must fully match the columns of input data: "
                            + "redundant type information for non-existent column '"
                            + dataColumn
                            + "'."
                );
            }
        }
    }

    private static StructType generateDataFrameSchemaFromSchemaItem(ObjectItem schemaItem, boolean forSparkML) {
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : schemaItem.getKeys()) {
                StructField field = generateStructFieldFromNameAndItem(
                    columnName,
                    schemaItem.getItemByKey(columnName),
                    forSparkML
                );
                fields.add(field);
            }
        } catch (IllegalArgumentException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Error while applying the schema; " + ex.getMessage()
            );
        }
        return DataTypes.createStructType(fields);
    }

    private static StructField generateStructFieldFromNameAndItem(String columnName, Item item, boolean forSparkML) {
        DataType type = generateDataTypeFromItem(item, forSparkML);
        return DataTypes.createStructField(columnName, type, true);
    }

    private static DataType generateDataTypeFromItem(Item item, boolean forSparkML) {
        if (item.isArray()) {
            validateArrayItemInSchema(item);
            Item arrayContentsTypeItem = item.getItems().get(0);
            DataType arrayContentsType = generateDataTypeFromItem(arrayContentsTypeItem, forSparkML);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (item.isObject()) {
            return generateDataFrameSchemaFromSchemaItem((ObjectItem) item, forSparkML);
        }

        if (item.isString()) {
            String itemTypeName = item.getStringValue();
            return ItemParser.getDataFrameDataTypeFromItemTypeName(itemTypeName, forSparkML);
        }

        throw new MLInvalidDataFrameSchemaException(
                "Schema can only contain arrays, objects or strings: " + item.serialize() + " is not accepted"
        );
    }

    private static void validateArrayItemInSchema(Item item) {
        List<Item> arrayTypes = item.getItems();
        if (arrayTypes.size() == 0) {
            throw new MLInvalidDataFrameSchemaException(
                    "Arrays in schema must define a type for their contents."
            );
        }
        if (arrayTypes.size() > 1) {
            throw new MLInvalidDataFrameSchemaException(
                    "Arrays in schema can define only a single type for their contents: "
                        + item.serialize()
                        + " is invalid."
            );
        }
    }

    public static void validateSchemaItemAgainstDataFrame(
            ObjectItem schemaItem,
            StructType dataFrameSchema,
            boolean forSparkML
    ) {
        StructType generatedSchema = generateDataFrameSchemaFromSchemaItem(schemaItem, forSparkML);
        for (StructField column : dataFrameSchema.fields()) {
            final String columnName = column.name();
            final DataType columnDataType = column.dataType();

            boolean columnMatched = Arrays.stream(generatedSchema.fields()).anyMatch(structField -> {
                String generatedColumnName = structField.name();
                if (!generatedColumnName.equals(columnName)) {
                    return false;
                }

                DataType generatedDataType = structField.dataType();
                if (isUserTypeApplicable(generatedDataType, columnDataType, forSparkML)) {
                    return true;
                }

                throw new MLInvalidDataFrameSchemaException(
                        "Columns defined in schema must fully match the columns of input data: "
                            + "expected '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(columnDataType)
                            + "' type for column '"
                            + columnName
                            + "', but found '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(generatedDataType)
                            + "'"
                );
            });

            if (!columnMatched) {
                throw new MLInvalidDataFrameSchemaException(
                        "Columns defined in schema must fully match the columns of input data: "
                            + "missing type information for '"
                            + columnName
                            + "' column."
                );
            }
        }

        for (String generatedSchemaColumnName : generatedSchema.fieldNames()) {
            boolean userColumnMatched = Arrays.asList(dataFrameSchema.fieldNames()).contains(generatedSchemaColumnName);

            if (!userColumnMatched) {
                throw new MLInvalidDataFrameSchemaException(
                        "Columns defined in schema must fully match the columns of input data: "
                            + "redundant type information for non-existent column '"
                            + generatedSchemaColumnName
                            + "'."
                );
            }
        }
    }

    private static boolean isUserTypeApplicable(
            DataType userSchemaColumnDataType,
            DataType columnDataType,
            boolean forSparkML
    ) {
        return userSchemaColumnDataType.equals(columnDataType)
            ||
            (userSchemaColumnDataType.equals(ItemParser.decimalType) && columnDataType.equals(DataTypes.LongType))
            ||
            (forSparkML
                && userSchemaColumnDataType.equals(DataTypes.DoubleType)
                && columnDataType.equals(DataTypes.LongType))
            ||
            (userSchemaColumnDataType.equals(DataTypes.DoubleType) && columnDataType.equals(DataTypes.FloatType))
            ||
            (userSchemaColumnDataType.equals(DataTypes.IntegerType) && columnDataType.equals(DataTypes.ShortType));
    }
}
