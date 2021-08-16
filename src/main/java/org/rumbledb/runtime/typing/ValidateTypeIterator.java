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
                ItemType actualType = inputDataAsDataFrame.getItemType();
                if (actualType.isSubtypeOf(this.itemType)) {
                    return inputDataAsDataFrame;
                }
                JavaRDD<Item> inputDataAsRDDOfItems = dataFrameToRDDOfItems(inputDataAsDataFrame, getMetadata());
                return convertRDDToValidDataFrame(inputDataAsRDDOfItems, this.itemType);
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, this.itemType);
            }

            List<Item> items = inputDataIterator.materialize(context);
            JSoundDataFrame jdf = convertLocalItemsToDataFrame(items, this.itemType);
            System.err.println("Returning from DF validation with");
            jdf.getDataFrame().show();
            return jdf;
        } catch (InvalidInstanceException ex) {
            InvalidInstanceException e = new InvalidInstanceException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

    public static JSoundDataFrame convertRDDToValidDataFrame(
            JavaRDD<Item> itemRDD,
            ItemType itemType
    ) {
        StructType schema = convertToDataFrameSchema(itemType);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<Item, Row>() {
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA);
                    return convertLocalItemToRow(item, schema);
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
                    itemType.getObjectContentFacet().get(columnName).getType(),
                    !itemType.getObjectContentFacet().get(columnName).isRequired()
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

    private static StructField createStructField(String columnName, ItemType item, boolean nullable) {
        DataType type = convertToDataType(item);
        return DataTypes.createStructField(columnName, type, nullable);
    }

    private static DataType convertToDataType(ItemType itemType) {
        if (itemType.isArrayItemType()) {
            ItemType arrayContentsTypeItemType = itemType.getArrayContentFacet();
            DataType arrayContentsType = convertToDataType(arrayContentsTypeItemType);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (itemType.isObjectItemType()) {
            return convertToDataFrameSchema(itemType);
        }
        return ItemParser.getDataFrameDataTypeFromItemType(itemType);
    }

    public static JSoundDataFrame convertLocalItemsToDataFrame(
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
            item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA);
            Row row = convertLocalItemToRow(item, schema);
            rows.add(row);
        }
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema),
                itemType
        );
    }

    private static Row convertLocalItemToRow(Item item, StructType schema) {
        Object[] rowColumns = new Object[schema.fields().length];
        for (int fieldIndex = 0; fieldIndex < schema.fields().length; fieldIndex++) {
            StructField field = schema.fields()[fieldIndex];
            Object rowColumn = convertColumn(item, field);
            rowColumns[fieldIndex] = rowColumn;
        }
        return RowFactory.create(rowColumns);
    }

    private static Object convertColumn(Item item, StructField field) {
        String fieldName = field.name();
        DataType fieldDataType = field.dataType();
        Item columnValueItem = item.getItemByKey(fieldName);
        return getRowColumnFromItemUsingDataType(
            columnValueItem,
            fieldDataType
        );
    }

    private static Object getRowColumnFromItemUsingDataType(Item item, DataType dataType) {
        if (item == null) {
            return null;
        }
        try {
            if (dataType instanceof ArrayType) {
                List<Item> arrayItems = item.getItems();
                Object[] arrayItemsForRow = new Object[arrayItems.size()];
                DataType elementType = ((ArrayType) dataType).elementType();
                for (int i = 0; i < arrayItems.size(); i++) {
                    Item arrayItem = item.getItemAt(i);
                    arrayItemsForRow[i] = getRowColumnFromItemUsingDataType(
                        arrayItem,
                        elementType
                    );
                }
                return arrayItemsForRow;
            }

            if (dataType instanceof StructType) {
                return ValidateTypeIterator.convertLocalItemToRow(item, (StructType) dataType);
            }

            if (dataType.equals(DataTypes.BooleanType)) {
                return item.getBooleanValue();
            }
            if (dataType.equals(DataTypes.IntegerType)) {
                return item.getIntValue();
            }
            if (dataType.equals(DataTypes.ByteType)) {
                return (byte) item.getIntValue();
            }
            if (dataType.equals(DataTypes.ShortType)) {
                return (short) item.getIntValue();
            }
            if (dataType.equals(DataTypes.LongType)) {
                return item.getIntegerValue().longValue();
            }
            if (dataType.equals(DataTypes.DoubleType)) {
                return item.getDoubleValue();
            }
            if (dataType.equals(DataTypes.FloatType)) {
                return item.getFloatValue();
            }
            if (dataType.equals(ItemParser.decimalType)) {
                return item.getDecimalValue();
            }
            if (dataType.equals(DataTypes.StringType)) {
                return item.getStringValue();
            }
            if (dataType.equals(DataTypes.NullType)) {
                return null;
            }
            if (dataType.equals(DataTypes.DateType)) {
                return new Date(item.getDateTimeValue().getMillis());
            }
            if (dataType.equals(DataTypes.TimestampType)) {
                return new Timestamp(item.getDateTimeValue().getMillis());
            }
            if (dataType.equals(DataTypes.BinaryType)) {
                return item.getBinaryValue();
            }
        } catch (OurBadException ex) {
            // OurBadExceptions triggered by invalid use of value getters here are caused by user's schema
            throw new InvalidInstanceException(ex.getJSONiqErrorMessage());
        }

        throw new OurBadException(
                "Unhandled item type found while generating rows: '" + dataType + "' ."
        );
    }


    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childrenItems = this.children.get(0).getRDD(context);
        return childrenItems.map(x -> validate(x, this.itemType, getMetadata()));
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
        System.err.println("local validation nextLocal.");
        Item i = validate(this.children.get(0).next(), this.itemType, getMetadata());
        System.err.println("Item: " + i);
        return i;
    }

    private static Item validate(Item item, ItemType itemType, ExceptionMetadata metadata) {
        if (itemType.isAtomicItemType()) {
            if (!item.isAtomic()) {
                throw new InvalidInstanceException(
                        "Expected an atomic item for type " + itemType.getIdentifierString()
                );
            }
            if (InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
                return item;
            }
            System.out.println(item.getDynamicType().getName());
            Item castType = CastIterator.castItemToType(
                ItemFactory.getInstance().createStringItem(item.getStringValue()),
                itemType,
                metadata
            );
            if (castType == null) {
                throw new InvalidInstanceException(
                        "Cannot cast " + item.serialize() + " to type " + itemType.getIdentifierString()
                );
            }
            return castType;
        }
        if (itemType.isArrayItemType()) {
            if (!item.isArray()) {
                throw new InvalidInstanceException(
                        "Expected array item for array type " + itemType.getIdentifierString()
                );
            }
            List<Item> members = new ArrayList<>();
            for (Item member : item.getItems()) {
                members.add(validate(member, itemType.getArrayContentFacet(), metadata));
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
                    values.add(validate(item.getItemByKey(key), facets.get(key).getType(), metadata));
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
