package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;

import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.SparkSessionManager;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidateTypeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private ItemType itemType;

    public ValidateTypeIterator(
            RuntimeIterator instance,
            ItemType itemType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(instance), executionMode, iteratorMetadata);
        this.itemType = itemType;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);
        if (!this.itemType.isResolved()) {
            this.itemType.resolve(context, getMetadata());
        }

        try {

            if (inputDataIterator.isDataFrame()) {
                Dataset<Row> inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
                validateItemTypeAgainstDataFrame(
                    this.itemType,
                    inputDataAsDataFrame.schema()
                );
                return inputDataAsDataFrame;
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, this.itemType);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return convertLocalItemsToDataFrame(items, this.itemType);
        } catch (MLInvalidDataFrameSchemaException ex) {
            MLInvalidDataFrameSchemaException e = new MLInvalidDataFrameSchemaException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

    private static Dataset<Row> convertRDDToValidDataFrame(
            JavaRDD<Item> itemRDD,
            ItemType itemType
    ) {
        StructType schema = convertToDataFrameSchema(itemType);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<Item, Row>() {
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    return convertLocalItemToRow(item, itemType, schema);
                }
            }
        );
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
    }

    private static StructType convertToDataFrameSchema(ItemType itemType) {
        if (!itemType.isObjectItemType()) {
            throw new MLInvalidDataFrameSchemaException(
                    "Error while checking against the DataFrame schema: it is not an object type: " + itemType
            );

        }
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : itemType.getObjectContentFacet().keySet()) {
                StructField field = createStructField(
                    columnName,
                    itemType.getObjectContentFacet().get(columnName).getType()
                );
                fields.add(field);
            }
        } catch (IllegalArgumentException ex) {
            MLInvalidDataFrameSchemaException e = new MLInvalidDataFrameSchemaException(
                    "Error while applying the schema; " + ex.getMessage()
            );
            e.initCause(ex);
            throw e;
        }
        return DataTypes.createStructType(fields);
    }

    private static StructField createStructField(String columnName, ItemType item) {
        DataType type = convertToDataType(item);
        return DataTypes.createStructField(columnName, type, true);
    }

    private static DataType convertToDataType(ItemType itemType) {
        if (itemType.isArrayItemType()) {
            ItemType arrayContentsTypeItemType = itemType.getArrayContentFacet().getType();
            DataType arrayContentsType = convertToDataType(arrayContentsTypeItemType);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (itemType.isObjectItemType()) {
            return convertToDataFrameSchema(itemType);
        }

        return ItemParser.getDataFrameDataTypeFromItemType(itemType);
    }

    private static Dataset<Row> convertLocalItemsToDataFrame(
            List<Item> items,
            ItemType itemType
    ) {
        if (items.size() == 0) {
            return SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame();
        }
        StructType schema = convertToDataFrameSchema(itemType);
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            Row row = convertLocalItemToRow(item, itemType, schema);
            rows.add(row);
        }
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
    }

    public static Row convertLocalItemToRow(Item item, ItemType itemType, StructType schema) {
        Object[] rowColumns = new Object[schema.fields().length];
        for (int fieldIndex = 0; fieldIndex < schema.fields().length; fieldIndex++) {
            StructField field = schema.fields()[fieldIndex];
            String fieldName = field.name();
            FieldDescriptor fieldDescriptor = null;
            if (itemType != null) {
                fieldDescriptor = itemType.getObjectContentFacet().get(fieldName);
            }
            Object rowColumn = convertColumn(item, fieldDescriptor, field);
            rowColumns[fieldIndex] = rowColumn;
        }
        return RowFactory.create(rowColumns);
    }

    private static Object convertColumn(Item item, FieldDescriptor fieldDescriptor, StructField field) {
        String fieldName = field.name();
        DataType fieldDataType = field.dataType();
        Item columnValueItem = item.getItemByKey(fieldName);
        if (fieldDescriptor != null && fieldDescriptor.isRequired() && columnValueItem == null) {
            throw new MLInvalidDataFrameSchemaException(
                    "Missing field '" + fieldName + "' in object '" + item.serialize() + "'."
            );
        }
        try {
            if (columnValueItem == null) {
                return null;
            }
            return getRowColumnFromItemUsingDataType(columnValueItem, fieldDataType);
        } catch (MLInvalidDataFrameSchemaException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Data does not fit to the given schema in field '"
                        + fieldName
                        + "'; "
                        + ex.getJSONiqErrorMessage()
            );
        }
    }

    private static Object getRowColumnFromItemUsingDataType(Item item, DataType dataType) {
        try {
            if (dataType instanceof ArrayType) {
                if (!item.isArray()) {
                    throw new MLInvalidDataFrameSchemaException("Type mismatch " + dataType);
                }
                List<Item> arrayItems = item.getItems();
                Object[] arrayItemsForRow = new Object[arrayItems.size()];
                DataType elementType = ((ArrayType) dataType).elementType();
                for (int i = 0; i < arrayItems.size(); i++) {
                    Item arrayItem = item.getItemAt(i);
                    arrayItemsForRow[i] = getRowColumnFromItemUsingDataType(arrayItem, elementType);
                }
                return arrayItemsForRow;
            }

            if (dataType instanceof StructType) {
                if (!item.isObject()) {
                    throw new MLInvalidDataFrameSchemaException("Type mismatch " + dataType);
                }
                return ValidateTypeIterator.convertLocalItemToRow(item, null, (StructType) dataType);
            }

            if (dataType.equals(DataTypes.BooleanType)) {
                if (!item.isBoolean()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.booleanItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getBooleanValue();
                }
                return item.getBooleanValue();
            }
            if (dataType.equals(DataTypes.IntegerType)) {
                if (!item.isInt()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.intItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getIntValue();
                }
                return item.getIntValue();
            }
            if (dataType.equals(DataTypes.DoubleType)) {
                if (!item.isDouble()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.doubleItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getDoubleValue();
                }
                return item.getDoubleValue();
            }
            if (dataType.equals(DataTypes.FloatType)) {
                if (!item.isFloat()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.floatItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getFloatValue();
                }
                return item.getFloatValue();
            }
            if (dataType.equals(ItemParser.decimalType)) {
                if (!item.isDecimal()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.decimalItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getDecimalValue();
                }
                return item.getDecimalValue();
            }
            if (dataType.equals(DataTypes.StringType)) {
                if (!item.isString()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.stringItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return i.getStringValue();
                }
                return item.getStringValue();
            }
            if (dataType.equals(DataTypes.NullType)) {
                if (!item.isNull()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.nullItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return null;
                }
                return null;
            }
            if (dataType.equals(DataTypes.DateType)) {
                if (!item.isDate()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.dateItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return new Date(item.getDateTimeValue().getMillis());
                }
                return new Date(item.getDateTimeValue().getMillis());
            }
            if (dataType.equals(DataTypes.TimestampType)) {
                if (!item.isDateTime()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.dateTimeItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new MLInvalidDataFrameSchemaException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return new Timestamp(item.getDateTimeValue().getMillis());
                }
                return new Timestamp(item.getDateTimeValue().getMillis());
            }
        } catch (OurBadException ex) {
            // OurBadExceptions triggered by invalid use of value getters here are caused by user's schema
            throw new MLInvalidDataFrameSchemaException(ex.getJSONiqErrorMessage());
        }

        throw new OurBadException(
                "Unhandled item type found while generating rows: '" + dataType + "' ."
        );
    }


    private static void validateItemTypeAgainstDataFrame(
            ItemType itemType,
            StructType dataFrameSchema
    ) {
        StructType generatedSchema = convertToDataFrameSchema(itemType);
        for (StructField column : dataFrameSchema.fields()) {
            final String columnName = column.name();
            final DataType columnDataType = column.dataType();

            boolean columnMatched = Arrays.stream(generatedSchema.fields()).anyMatch(structField -> {
                String generatedColumnName = structField.name();
                if (!generatedColumnName.equals(columnName)) {
                    return false;
                }

                DataType generatedDataType = structField.dataType();
                if (DataFrameUtils.isUserTypeApplicable(generatedDataType, columnDataType)) {
                    return true;
                }

                throw new MLInvalidDataFrameSchemaException(
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
                throw new MLInvalidDataFrameSchemaException(
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
                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "redundant type information for non-existent field '"
                            + generatedSchemaColumnName
                            + "'."
                );
            }
        }
    }

}
