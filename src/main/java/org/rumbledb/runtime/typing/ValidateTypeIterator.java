package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DatesWithTimezonesNotSupported;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.TypeMappings;

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

    private boolean isValidate;

    public ValidateTypeIterator(
            RuntimeIterator instance,
            ItemType itemType,
            boolean isValidate,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(instance), staticContext);
        this.itemType = itemType;
        this.isValidate = isValidate;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);
        if (!this.itemType.isResolved()) {
            this.itemType.resolve(context, getMetadata());
        }
        if (!this.itemType.isCompatibleWithDataFrames(context.getRumbleRuntimeConfiguration())) {
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
                return convertRDDToValidDataFrame(inputDataAsRDDOfItems, this.itemType, context, this.isValidate);
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, this.itemType, context, this.isValidate);
            }

            List<Item> items = inputDataIterator.materialize(context);
            JSoundDataFrame jdf = convertLocalItemsToDataFrame(items, this.itemType, context, this.isValidate);
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
            ItemType itemType,
            DynamicContext context,
            boolean isValidate
    ) {
        if (!itemType.isCompatibleWithDataFrames(context.getRumbleRuntimeConfiguration())) {
            throw new OurBadException(
                    "Type " + itemType + " cannot be converted to a DataFrame, but a DataFrame is expected."
            );
        }
        StructType schema = convertToDataFrameSchema(itemType);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<Item, Row>() {
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA, isValidate);
                    return convertLocalItemToRow(item, schema, context);
                }
            }
        );
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema),
                itemType
        );
    }

    public static StructType convertToDataFrameSchema(ItemType itemType) {
        if (itemType.isAtomicItemType()) {
            List<StructField> fields = new ArrayList<>();
            String columnName = SparkSessionManager.atomicJSONiqItemColumnName;
            StructField field = createStructField(
                columnName,
                itemType,
                false
            );
            fields.add(field);
            return DataTypes.createStructType(fields);
        }
        if (!itemType.isObjectItemType()) {
            throw new InvalidInstanceException(
                    "Error while checking against the DataFrame schema: it is not an object or an atomic type: "
                        + itemType
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
        return TypeMappings.getDataFrameDataTypeFromItemType(itemType);
    }

    public static JSoundDataFrame convertLocalItemsToDataFrame(
            List<Item> items,
            ItemType itemType,
            DynamicContext context,
            boolean isValidate
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
            item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA, isValidate);
            Row row = convertLocalItemToRow(item, schema, context);
            rows.add(row);
        }
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema),
                itemType
        );
    }

    private static Row convertLocalItemToRow(Item item, StructType schema, DynamicContext context) {
        int numColumns = schema.fields().length;
        Object[] rowColumns = new Object[numColumns];
        for (int fieldIndex = 0; fieldIndex < numColumns; fieldIndex++) {
            StructField field = schema.fields()[fieldIndex];
            Object rowColumn = convertColumn(item, field, context);
            rowColumns[fieldIndex] = rowColumn;
        }
        return RowFactory.create(rowColumns);
    }

    private static Object convertColumn(Item item, StructField field, DynamicContext context) {
        String fieldName = field.name();
        DataType fieldDataType = field.dataType();
        if (fieldName.equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            return getRowColumnFromItemUsingDataType(
                item,
                fieldDataType,
                context
            );
        }
        Item columnValueItem = item.getItemByKey(fieldName);
        return getRowColumnFromItemUsingDataType(
            columnValueItem,
            fieldDataType,
            context
        );
    }

    private static Object getRowColumnFromItemUsingDataType(
            Item item,
            DataType dataType,
            DynamicContext context
    ) {
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
                        elementType,
                        context
                    );
                }
                return arrayItemsForRow;
            }

            if (dataType instanceof StructType) {
                return ValidateTypeIterator.convertLocalItemToRow(item, (StructType) dataType, context);
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
            if (dataType instanceof DecimalType) {
                return item.getDecimalValue();
            }
            if (dataType.equals(DataTypes.StringType)) {
                return item.getStringValue();
            }
            if (dataType.equals(DataTypes.NullType)) {
                return null;
            }
            if (dataType.equals(DataTypes.DateType)) {
                if (!context.getRumbleRuntimeConfiguration().dateWithTimezone()) {
                    if (item.hasTimeZone()) {
                        throw new DatesWithTimezonesNotSupported(ExceptionMetadata.EMPTY_METADATA);
                    }
                }
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
        return childrenItems.map(x -> validate(x, this.itemType, getMetadata(), this.isValidate));
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
        Item i = validate(this.children.get(0).next(), this.itemType, getMetadata(), this.isValidate);
        return i;
    }

    private static Item validate(Item item, ItemType itemType, ExceptionMetadata metadata, boolean isValidate) {
        if (!isValidate) {
            return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
        }
        if (itemType.isAtomicItemType()) {
            if (!item.isAtomic()) {
                throw new InvalidInstanceException(
                        "Expected an atomic item for type " + itemType.getIdentifierString()
                );
            }
            if (InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
                return item;
            }
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
                members.add(validate(member, itemType.getArrayContentFacet(), metadata, true));
            }
            Integer minLength = itemType.getMinLengthFacet();
            Integer maxLength = itemType.getMaxLengthFacet();
            Item arrayItem = ItemFactory.getInstance().createArrayItem(members, true);
            if (itemType.getName() == null) {
                itemType = itemType.getBaseType();
            }
            if (minLength != null && members.size() < minLength) {
                throw new InvalidInstanceException(
                        "Array has " + members.size() + " members but the type requires at least " + minLength
                );
            }
            if (maxLength != null && members.size() > maxLength) {
                throw new InvalidInstanceException(
                        "Array has " + members.size() + " members but the type requires at most " + maxLength
                );
            }

            return ItemFactory.getInstance().createAnnotatedItem(arrayItem, itemType);
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
                    values.add(validate(item.getItemByKey(key), facets.get(key).getType(), metadata, true));
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
                .createObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA, true);
            if (itemType.getName() == null) {
                itemType = itemType.getBaseType();
            }
            return ItemFactory.getInstance().createAnnotatedItem(objectItem, itemType);
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

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.isValidate) {
            return NativeClauseContext.NoNativeQuery;
        }
        return this.children.get(0).generateNativeQuery(nativeClauseContext);
    }
}
