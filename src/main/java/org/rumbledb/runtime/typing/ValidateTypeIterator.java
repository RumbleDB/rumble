package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
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
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;

import sparksoniq.spark.SparkSessionManager;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValidateTypeIterator extends HybridRuntimeIterator {

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
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);
        if (!this.itemType.isResolved()) {
            this.itemType.resolve(context, getMetadata());
        }
        if (!this.itemType.isDataFrameType()) {
            throw new OurBadException(
                    "Cannot build a dataframe for a type not compatible with DataFrames: "
                        + this.itemType.getIdentifierString()
            );
        }

        try {

            if (inputDataIterator.isDataFrame()) {
                JSoundDataFrame inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
                checkAnnotationPossibleOrThrowError(
                    this.itemType,
                    inputDataAsDataFrame.getItemType()
                );
                return new JSoundDataFrame(inputDataAsDataFrame.getDataFrame(), this.itemType);
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, this.itemType);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return convertLocalItemsToDataFrame(items, this.itemType);
        } catch (InvalidInstanceException ex) {
            InvalidInstanceException e = new InvalidInstanceException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

    private static JSoundDataFrame convertRDDToValidDataFrame(
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
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema),
                itemType
        );
    }

    private static StructType convertToDataFrameSchema(ItemType itemType) {
        if (!itemType.isObjectItemType()) {
            throw new InvalidInstanceException(
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
            InvalidInstanceException e = new InvalidInstanceException(
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

    private static JSoundDataFrame convertLocalItemsToDataFrame(
            List<Item> items,
            ItemType itemType
    ) {
        if (items.size() == 0) {
            return new JSoundDataFrame(
                    SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                    itemType
            );
        }
        StructType schema = convertToDataFrameSchema(itemType);
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            Row row = convertLocalItemToRow(item, itemType, schema);
            rows.add(row);
        }
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema),
                itemType
        );
    }

    public static Row convertLocalItemToRow(Item item, ItemType itemType, StructType schema) {
        if (itemType == null || itemType.isObjectItemType()) {
            if (!item.isObject()) {
                throw new InvalidInstanceException(
                        "Item " + item.serialize() + " is not an object, but an object was expected."
                );
            }
            Object[] rowColumns = new Object[schema.fields().length];
            if (itemType != null && itemType.getClosedFacet()) {
                for (String key : item.getKeys()) {
                    boolean found = false;
                    for (int fieldIndex = 0; fieldIndex < schema.fields().length; fieldIndex++) {
                        if (key.equals(schema.fields()[fieldIndex].name())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        throw new InvalidInstanceException(
                                "Unexpected key in closed object type "
                                    + itemType.getIdentifierString()
                                    + " : "
                                    + key
                        );
                    }
                }
            }
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
        throw new OurBadException(
                "We do not support validation against non-object types yet. Please contact us if you would like us to prioritize this feature.",
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    private static Object convertColumn(Item item, FieldDescriptor fieldDescriptor, StructField field) {
        String fieldName = field.name();
        DataType fieldDataType = field.dataType();
        Item columnValueItem = item.getItemByKey(fieldName);
        Item defaultValue = fieldDescriptor != null ? fieldDescriptor.getDefaultValue() : null;
        if (
            fieldDescriptor != null && defaultValue == null && fieldDescriptor.isRequired() && columnValueItem == null
        ) {
            throw new InvalidInstanceException(
                    "Missing field '" + fieldName + "' in object '" + item.serialize() + "'."
            );
        }
        try {
            if (columnValueItem == null) {
                if (defaultValue != null) {
                    return getRowColumnFromItemUsingDataType(
                        defaultValue,
                        fieldDescriptor.getType(),
                        fieldDataType
                    );
                }
                return null;
            }
            return getRowColumnFromItemUsingDataType(
                columnValueItem,
                fieldDescriptor != null ? fieldDescriptor.getType() : null,
                fieldDataType
            );
        } catch (InvalidInstanceException ex) {
            throw new InvalidInstanceException(
                    "Data does not fit to the given schema in field '"
                        + fieldName
                        + "'; "
                        + ex.getJSONiqErrorMessage()
            );
        }
    }

    private static Object getRowColumnFromItemUsingDataType(Item item, ItemType itemType, DataType dataType) {
        try {
            if (dataType instanceof ArrayType) {
                if (!item.isArray()) {
                    throw new InvalidInstanceException("Type mismatch " + dataType);
                }
                List<Item> arrayItems = item.getItems();
                Object[] arrayItemsForRow = new Object[arrayItems.size()];
                DataType elementType = ((ArrayType) dataType).elementType();
                for (int i = 0; i < arrayItems.size(); i++) {
                    Item arrayItem = item.getItemAt(i);
                    arrayItemsForRow[i] = getRowColumnFromItemUsingDataType(
                        arrayItem,
                        itemType != null ? itemType.getArrayContentFacet().getType() : null,
                        elementType
                    );
                }
                return arrayItemsForRow;
            }

            if (dataType instanceof StructType) {
                if (!item.isObject()) {
                    throw new InvalidInstanceException("Type mismatch " + dataType);
                }
                return ValidateTypeIterator.convertLocalItemToRow(item, itemType, (StructType) dataType);
            }

            if (dataType.equals(DataTypes.BooleanType)) {
                if (!item.isBoolean()) {
                    Item i = CastIterator.castItemToType(
                        item,
                        BuiltinTypesCatalogue.booleanItem,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (i == null) {
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
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
                        throw new InvalidInstanceException(
                                "Type mismatch and cast unsuccessful to " + dataType
                        );
                    }
                    return new Timestamp(item.getDateTimeValue().getMillis());
                }
                return new Timestamp(item.getDateTimeValue().getMillis());
            }
        } catch (OurBadException ex) {
            // OurBadExceptions triggered by invalid use of value getters here are caused by user's schema
            throw new InvalidInstanceException(ex.getJSONiqErrorMessage());
        }

        throw new OurBadException(
                "Unhandled item type found while generating rows: '" + dataType + "' ."
        );
    }


    private static void checkAnnotationPossibleOrThrowError(
            ItemType expectedType,
            ItemType actualType
    ) {
        if (expectedType.isObjectItemType()) {
            if (!actualType.isObjectItemType()) {
                throw new InvalidInstanceException(
                        "Unexpected type. Expected "
                            + expectedType
                            + " but actually encountered "
                            + actualType
                );
            }
            // StructType generatedSchema = convertToDataFrameSchema(itemType);
            for (String columnName : actualType.getObjectContentFacet().keySet()) {
                final ItemType actualColumnType = actualType.getObjectContentFacet().get(columnName).getType();

                boolean columnMatched = false;
                for (String generatedColumnName : expectedType.getObjectContentFacet().keySet()) {
                    if (!generatedColumnName.equals(columnName)) {
                        continue;
                    }

                    ItemType expectedColumnType = expectedType.getObjectContentFacet()
                        .get(generatedColumnName)
                        .getType();
                    checkAnnotationPossibleOrThrowError(expectedColumnType, actualColumnType);
                    columnMatched = true;
                }

                if (expectedType != null && expectedType.getClosedFacet()) {
                    if (!columnMatched) {
                        throw new InvalidInstanceException(
                                "Unexpected key in closed object type "
                                    + expectedType.getIdentifierString()
                                    + " : "
                                    + columnName
                        );
                    }
                }
            }

            for (String generatedSchemaColumnName : expectedType.getObjectContentFacet().keySet()) {
                boolean userColumnMatched = actualType.getObjectContentFacet()
                    .keySet()
                    .contains(generatedSchemaColumnName);

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
        if (expectedType.isArrayItemType()) {
            if (!actualType.isArrayItemType()) {
                throw new InvalidInstanceException(
                        "Unexpected type. Expected "
                            + expectedType
                            + " but actually encountered "
                            + actualType
                );
            }
        }
        if (expectedType.isAtomicItemType()) {
            if (!actualType.isAtomicItemType()) {
                throw new InvalidInstanceException(
                        "Unexpected type, and dataframe casts are not supported at this point (contact us to prioritize). Expected "
                            + expectedType
                            + " but actually encountered "
                            + actualType
                );
            }
            if (!actualType.isSubtypeOf(expectedType)) {
                throw new InvalidInstanceException(
                        "Unexpected type, and dataframe casts are not supported at this point (contact us to prioritize). Expected "
                            + expectedType
                            + " but actually encountered "
                            + actualType
                );
            }
        }
        if (expectedType.isUnionType()) {
            throw new InvalidInstanceException("Union types are not supported at this point.");
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childrenItems = this.children.get(0).getRDD(context);
        return childrenItems.map(x -> validate(x, this.itemType));
    }

    @Override
    protected void openLocal() {
        this.children.get(0).open(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        this.children.get(0).close();
    }

    @Override
    protected void resetLocal() {
        this.children.get(0).reset(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.children.get(0).hasNext();
    }

    @Override
    protected Item nextLocal() {
        return validate(this.children.get(0).next(), this.itemType);
    }

    private static Item validate(Item item, ItemType itemType) {
        if (itemType.isAtomicItemType()) {
            if (!item.isAtomic()) {
                throw new InvalidInstanceException(
                        "Expected an atomic item for type " + itemType.getIdentifierString()
                );
            }
            return ItemFactory.getInstance().createUserDefinedItem(item, itemType);
        }
        if (itemType.isArrayItemType()) {
            if (!item.isArray()) {
                throw new InvalidInstanceException(
                        "Expected array item for array type " + itemType.getIdentifierString()
                );
            }
            List<Item> members = new ArrayList<>();
            for (Item member : item.getItems()) {
                members.add(validate(member, itemType.getArrayContentFacet().getType()));
            }
            Item arrayItem = ItemFactory.getInstance().createArrayItem(members);
            return ItemFactory.getInstance().createUserDefinedItem(arrayItem, itemType);
        }
        if (itemType.isObjectItemType()) {
            if (!item.isObject()) {
                throw new InvalidInstanceException(
                        "Expected an object item for object type "
                            + itemType.getIdentifierString()
                            + ", but have "
                            + item.serialize()
                );
            }
            List<String> keys = new ArrayList<>();
            List<Item> values = new ArrayList<>();
            Map<String, FieldDescriptor> facets = itemType.getObjectContentFacet();
            for (String key : item.getKeys()) {
                if (facets.containsKey(key)) {
                    keys.add(key);
                    values.add(validate(item.getItemByKey(key), facets.get(key).getType()));
                } else {
                    if (itemType.getClosedFacet()) {
                        throw new InvalidInstanceException(
                                "Unexpected key in closed object type + "
                                    + itemType.getIdentifierString()
                                    + " : "
                                    + key
                        );
                    }
                    keys.add(key);
                    values.add(item.getItemByKey(key));
                }
            }
            for (String key : facets.keySet()) {
                if (!item.getKeys().contains(key)) {
                    Item defaultValue = facets.get(key).getDefaultValue();
                    if (defaultValue != null) {
                        keys.add(key);
                        values.add(defaultValue);
                    }
                    if (facets.get(key).isRequired()) {
                        throw new InvalidInstanceException(
                                "Missing required key in object type + "
                                    + itemType.getIdentifierString()
                                    + " : "
                                    + key
                        );
                    }
                }
            }
            Item objectItem = ItemFactory.getInstance()
                .createObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA);
            return ItemFactory.getInstance().createUserDefinedItem(objectItem, itemType);
        }
        if (itemType.isFunctionItemType()) {
            if (!item.isFunction()) {
                throw new InvalidInstanceException(
                        "Expected function item of type " + itemType.getIdentifierString()
                );
            }
            return item;
        }
        return item;
    }

}
