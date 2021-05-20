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
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFrameUtils {
    public static Dataset<Row> convertItemRDDToDataFrame(
            JavaRDD<Item> itemRDD,
            Item schemaItem
    ) {
        Item firstDataItem = (Item) itemRDD.take(1).get(0);
        validateSchemaAgainstAnItem(schemaItem, firstDataItem);
        StructType schema = generateDataFrameSchemaFromSchemaItem(schemaItem);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<Item, Row>() {
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    return ValidateTypeIterator.convertLocalItemToRow(item, null, schema);
                }
            }
        );
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
    }

    public static boolean isSequenceOfObjects(Dataset<Row> dataFrame) {
        StructType schema = dataFrame.schema();
        List<String> fieldNames = Arrays.asList(schema.fieldNames());
        return fieldNames.size() != 1 || !fieldNames.get(0).equals(SparkSessionManager.atomicJSONiqItemColumnName);
    }

    public static List<String> getFields(Dataset<Row> dataFrame) {
        if (!isSequenceOfObjects(dataFrame)) {
            throw new OurBadException("Cannot get fields if the sequence is not a sequence of objects.");
        }
        StructType schema = dataFrame.schema();
        List<String> fieldNames = Arrays.asList(schema.fieldNames());
        return fieldNames;
    }

    public static Dataset<Row> convertLocalItemsToDataFrame(
            List<Item> items,
            Item schemaItem
    ) {
        if (items.size() == 0) {
            return SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame();
        }
        ObjectItem firstDataItem = (ObjectItem) items.get(0);
        validateSchemaAgainstAnItem(schemaItem, firstDataItem);
        StructType schema = generateDataFrameSchemaFromSchemaItem(schemaItem);
        List<Row> rows = getRowsFromItemsUsingSchema(items, null, schema);
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
    }

    private static List<Row> getRowsFromItemsUsingSchema(List<Item> items, ItemType itemType, StructType schema) {
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            Row row = ValidateTypeIterator.convertLocalItemToRow(item, itemType, schema);
            rows.add(row);
        }
        return rows;
    }

    private static void validateSchemaAgainstAnItem(
            Item schemaItem,
            Item dataItem
    ) {
        for (String schemaColumn : schemaItem.getKeys()) {
            if (!dataItem.getKeys().contains(schemaColumn)) {
                throw new InvalidInstanceException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "redundant type information for non-existent field '"
                            + schemaColumn
                            + "' column."
                );
            }
        }

        for (String dataColumn : dataItem.getKeys()) {
            if (!schemaItem.getKeys().contains(dataColumn)) {
                throw new InvalidInstanceException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "missing type information for '"
                            + dataColumn
                            + "' field."
                );
            }
        }
    }

    private static StructType generateDataFrameSchemaFromSchemaItem(Item schemaItem) {
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : schemaItem.getKeys()) {
                StructField field = generateStructFieldFromNameAndItem(
                    columnName,
                    schemaItem.getItemByKey(columnName)
                );
                fields.add(field);
            }
        } catch (IllegalArgumentException ex) {
            InvalidInstanceException e = new InvalidInstanceException(
                    "Error while applying the schema; " + ex.getMessage()
            );
            e.initCause(ex);
            throw e;
        }
        return DataTypes.createStructType(fields);
    }

    private static StructField generateStructFieldFromNameAndItem(String columnName, Item item) {
        DataType type = generateDataTypeFromItem(item);
        return DataTypes.createStructField(columnName, type, true);
    }

    private static DataType generateDataTypeFromItem(Item item) {
        if (item.isArray()) {
            validateArrayItemInSchema(item);
            Item arrayContentsTypeItem = item.getItemAt(0);
            DataType arrayContentsType = generateDataTypeFromItem(arrayContentsTypeItem);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (item.isObject()) {
            return generateDataFrameSchemaFromSchemaItem((ObjectItem) item);
        }

        if (item.isString()) {
            ItemType itemType = BuiltinTypesCatalogue.getItemTypeByName(
                Name.createVariableInDefaultTypeNamespace(item.getStringValue())
            );
            return ItemParser.getDataFrameDataTypeFromItemType(itemType);
        }

        throw new InvalidInstanceException(
                "Schema can only contain arrays, objects or strings: " + item.serialize() + " is not accepted"
        );
    }

    private static void validateArrayItemInSchema(Item item) {
        List<Item> arrayTypes = item.getItems();
        if (arrayTypes.size() == 0) {
            throw new InvalidInstanceException(
                    "Arrays in schema must define a type for their contents."
            );
        }
        if (arrayTypes.size() > 1) {
            throw new InvalidInstanceException(
                    "Arrays in schema can define only a single type for their contents: "
                        + item.serialize()
                        + " is invalid."
            );
        }
    }

    public static void validateSchemaItemAgainstDataFrame(
            Item schemaItem,
            StructType dataFrameSchema
    ) {
        StructType generatedSchema = generateDataFrameSchemaFromSchemaItem(schemaItem);
        for (StructField column : dataFrameSchema.fields()) {
            final String columnName = column.name();
            final DataType columnDataType = column.dataType();

            boolean columnMatched = Arrays.stream(generatedSchema.fields()).anyMatch(structField -> {
                String generatedColumnName = structField.name();
                if (!generatedColumnName.equals(columnName)) {
                    return false;
                }

                DataType generatedDataType = structField.dataType();
                if (isUserTypeApplicable(generatedDataType, columnDataType)) {
                    return true;
                }

                throw new InvalidInstanceException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "expected '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(columnDataType)
                            + "' type for field '"
                            + columnName
                            + "', but found '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(generatedDataType)
                            + "'"
                );
            });

            if (!columnMatched) {
                throw new InvalidInstanceException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "missing type information for '"
                            + columnName
                            + "' field."
                );
            }
        }

        for (String generatedSchemaColumnName : generatedSchema.fieldNames()) {
            boolean userColumnMatched = Arrays.asList(dataFrameSchema.fieldNames()).contains(generatedSchemaColumnName);

            if (!userColumnMatched) {
                throw new InvalidInstanceException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "redundant type information for non-existent field '"
                            + generatedSchemaColumnName
                            + "'."
                );
            }
        }
    }

    public static boolean isUserTypeApplicable(
            DataType userSchemaColumnDataType,
            DataType columnDataType
    ) {
        return userSchemaColumnDataType.equals(columnDataType)
            ||
            (userSchemaColumnDataType.equals(ItemParser.decimalType) && columnDataType.equals(DataTypes.LongType))
            ||
            (userSchemaColumnDataType.equals(DataTypes.DoubleType) && columnDataType.equals(DataTypes.FloatType))
            ||
            (userSchemaColumnDataType.equals(DataTypes.IntegerType) && columnDataType.equals(DataTypes.ShortType));
    }
}
