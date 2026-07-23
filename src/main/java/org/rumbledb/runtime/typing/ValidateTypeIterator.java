package org.rumbledb.runtime.typing;

import java.io.Serial;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import static org.apache.spark.sql.functions.expr;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.DatesWithTimezonesNotSupported;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.TypeMappings;

import sparksoniq.spark.SparkSessionManager;

public class ValidateTypeIterator extends HybridRuntimeIterator {

    @Serial
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
        RuntimeIterator inputDataIterator = this.getChild(0);
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
                return convertRDDToValidDataFrame(
                    inputDataAsRDDOfItems,
                    this.itemType,
                    context,
                    this.isValidate,
                    this.staticContext
                );
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, this.itemType, context, this.isValidate, this.staticContext);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return convertLocalItemsToDataFrame(items, this.itemType, context, this.isValidate, this.staticContext);
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
            boolean isValidate,
            RuntimeStaticContext staticContext
    ) {
        if (!itemType.isCompatibleWithDataFrames(staticContext.getConfiguration())) {
            throw new OurBadException(
                    "Type " + itemType + " cannot be converted to a DataFrame, but a DataFrame is expected."
            );
        }
        StructType schema = convertToDataFrameSchema(itemType, staticContext);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<>() {
                @Serial
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA, isValidate, staticContext);
                    return convertLocalItemToRow(item, schema, context);
                }
            }
        );
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema),
                itemType
        );
    }

    public static JSoundDataFrame convertRDDToVariantDataFrame(
            JavaRDD<Item> itemRDD
    ) {
        StructType schema = new StructType(
                new StructField[] {
                    DataTypes.createStructField(
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        DataTypes.StringType,
                        false
                    )
                }
        );
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<>() {
                @Serial
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    return RowFactory.create(item.serializeAsJSON());
                }
            }
        );

        return new JSoundDataFrame(
                SparkSessionManager.getInstance()
                    .getOrCreateSession()
                    .createDataFrame(rowRDD, schema)
                    .withColumn(
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        expr("parse_json(`" + SparkSessionManager.nonObjectJSONiqItemColumnName + "`)")
                    ),
                BuiltinTypesCatalogue.item
        );
    }

    public static StructType convertToDataFrameSchema(ItemType itemType, RuntimeStaticContext staticContext) {
        if (itemType.isAtomicItemType()) {
            List<StructField> fields = new ArrayList<>();
            String columnName = SparkSessionManager.nonObjectJSONiqItemColumnName;
            boolean nullable = itemType.canBeNull();
            StructField field = createStructField(
                columnName,
                itemType,
                staticContext.getConfiguration().getLaxJSONNullValidation() && nullable,
                staticContext
            );
            fields.add(field);
            return DataTypes.createStructType(fields);
        }
        if (itemType.isArrayItemType()) {
            List<StructField> fields = new ArrayList<>();
            String columnName = SparkSessionManager.nonObjectJSONiqItemColumnName;
            boolean nullable = itemType.canBeNull();
            StructField field = createStructField(
                columnName,
                itemType,
                staticContext.getConfiguration().getLaxJSONNullValidation() && nullable,
                staticContext
            );
            fields.add(field);
            return DataTypes.createStructType(fields);
        }
        if (!itemType.isObjectItemType()) {
            throw new InvalidInstanceException(
                    "Error while checking against the DataFrame schema: it is not an object, an array, or an atomic type: "
                        + itemType
            );

        }
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : itemType.getObjectKeysFacet()) {
                FieldDescriptor fieldDescriptor = itemType.getObjectContentFacet(columnName);
                ItemType columnType = fieldDescriptor.getType();
                boolean required = fieldDescriptor.isRequired();
                boolean nullable = columnType.canBeNull();
                StructField field = createStructField(
                    columnName,
                    columnType,
                    !required || (staticContext.getConfiguration().getLaxJSONNullValidation() && nullable),
                    staticContext
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

    private static StructField createStructField(
            String columnName,
            ItemType item,
            boolean nullable,
            RuntimeStaticContext staticContext
    ) {
        DataType type = convertToDataType(item, staticContext);
        return DataTypes.createStructField(columnName, type, nullable);
    }

    private static DataType convertToDataType(ItemType itemType, RuntimeStaticContext staticContext) {
        if (itemType.isArrayItemType()) {
            ItemType arrayContentsTypeItemType = itemType.getArrayContentFacet();
            DataType arrayContentsType = convertToDataType(arrayContentsTypeItemType, staticContext);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (itemType.isObjectItemType()) {
            return convertToDataFrameSchema(itemType, staticContext);
        }
        return TypeMappings.getDataFrameDataTypeFromItemType(itemType, staticContext);
    }

    public static JSoundDataFrame convertLocalItemsToDataFrame(
            List<Item> items,
            ItemType itemType,
            DynamicContext context,
            boolean isValidate,
            RuntimeStaticContext staticContext
    ) {
        if (items.isEmpty()) {
            return new JSoundDataFrame(
                    SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                    itemType
            );
        }
        StructType schema = convertToDataFrameSchema(itemType, staticContext);
        if (staticContext.getConfiguration().printInferredTypes()) {
            System.err.println("Inferred DataFrame type:\n");
            schema.printTreeString();
        }
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            item = validate(item, itemType, ExceptionMetadata.EMPTY_METADATA, isValidate, staticContext);
            Row row = convertLocalItemToRow(item, schema, context);
            rows.add(row);
        }
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema),
                itemType
        );
    }

    public static JSoundDataFrame convertLocalItemsToVariantDataFrame(
            List<Item> items
    ) {
        if (items.isEmpty()) {
            return new JSoundDataFrame(
                    SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                    BuiltinTypesCatalogue.item
            );
        }
        StructType schema = new StructType(
                new StructField[] {
                    DataTypes.createStructField(
                        SparkSessionManager.nonObjectJSONiqItemColumnName,
                        DataTypes.StringType,
                        false
                    )
                }
        );
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            rows.add(RowFactory.create(item.serializeAsJSON()));
        }
        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
        dataFrame = dataFrame.withColumn(
            SparkSessionManager.nonObjectJSONiqItemColumnName,
            expr("parse_json(`" + SparkSessionManager.nonObjectJSONiqItemColumnName + "`)")
        );

        return new JSoundDataFrame(
                dataFrame,
                BuiltinTypesCatalogue.item
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
        if (fieldName.equals(SparkSessionManager.nonObjectJSONiqItemColumnName)) {
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
        // Handling of missing value
        if (item == null) {
            return null;
        }
        // Handling of null
        if (item.isNull()) {
            if (context.getRumbleRuntimeConfiguration().getLaxJSONNullValidation()) {
                return null;
            } else if (dataType.equals(DataTypes.NullType)) {
                return null;
            } else if (dataType.equals(DataTypes.StringType)) {
                return "null";
            } else {
                throw new OurBadException(
                        "Null value found where a non-null value of type " + dataType + " was expected."
                );
            }
        }
        try {
            if (dataType instanceof ArrayType arrayType) {
                List<Item> arrayItems = item.getItemMembers();
                Object[] arrayItemsForRow = new Object[arrayItems.size()];
                DataType elementType = arrayType.elementType();
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

            if (dataType instanceof StructType structType) {
                return ValidateTypeIterator.convertLocalItemToRow(item, structType, context);
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
                if (item.isAtomic()) {
                    return item.getStringValue();
                }
                return item.serialize();
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
                return Date.valueOf(item.getDateTimeValue().toLocalDate());
            }
            if (dataType.equals(DataTypes.TimestampType)) {
                return Timestamp.valueOf(item.getDateTimeValue().toLocalDateTime());
            }
            if (dataType.equals(DataTypes.BinaryType)) {
                return item.getBinaryValue();
            }
        } catch (OurBadException ex) {
            // OurBadExceptions triggered by invalid use of value getters here are caused by user's schema
            throw new InvalidInstanceException(
                    "RumbleDB was not able to infer a Schema. Please select another output method such as json. "
                        + ex.getJSONiqErrorMessage()
            );
        }

        throw new OurBadException(
                "Unhandled item type found while generating rows: '" + dataType + "' ."
        );
    }


    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childrenItems = this.getChild(0).getRDD(context);
        return childrenItems.map(x -> validate(x, this.itemType, getMetadata(), this.isValidate, this.staticContext));
    }

    @Override
    protected void openLocal() {
        this.children.get(0).open(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        this.getChild(0).close();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.getChild(0).hasNext();
    }

    @Override
    protected Item nextLocal() {
        return validate(this.getChild(0).next(), this.itemType, getMetadata(), this.isValidate, this.staticContext);
    }

    private static Item validate(
            Item item,
            ItemType itemType,
            ExceptionMetadata metadata,
            boolean isValidate,
            RuntimeStaticContext staticContext
    ) {
        if (!isValidate) {
            return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
        }
        if (itemType.isAtomicItemType()) {
            if (item.isElementNode() || item.isDocumentNode()) {
                return validateXmlNodeAgainstAtomicType(item, itemType, metadata, staticContext);
            }
            if (InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
                return item;
            }
            try {
                Item castType = CastIterator.castItemToType(item, itemType, metadata, staticContext);
                if (castType == null) {
                    throw new InvalidInstanceException(
                            "Cannot cast " + item.serialize() + " to type " + itemType.getIdentifierString()
                    );
                }
                return castType;
            } catch (Exception e) {
                throw new InvalidInstanceException(
                        "Cannot cast " + item.serialize() + " to type " + itemType.getIdentifierString()
                );
            }
        }
        if (itemType.isArrayItemType()) {
            if (!item.isArray()) {
                throw new InvalidInstanceException(
                        "Expected array item for array type " + itemType.getIdentifierString()
                );
            }
            List<Item> members = new ArrayList<>();
            for (Item member : item.getItemMembers()) {
                members.add(validate(member, itemType.getArrayContentFacet(), metadata, true, staticContext));
            }

            // Test of length facets
            Integer minLength = itemType.getMinLengthFacet();
            Integer maxLength = itemType.getMaxLengthFacet();
            Item arrayItem = ItemFactory.getInstance().createArrayItem(members, staticContext.isQuerySideEffecting());
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

            // Test of uniqueness
            if (itemType.getArrayContentFacet().isObjectItemType()) {
                ItemType arrayContentFacet = itemType.getArrayContentFacet();
                List<String> uniqueKeys = new ArrayList<>();
                for (String key : arrayContentFacet.getObjectKeysFacet()) {
                    if (arrayContentFacet.getObjectContentFacet(key).isUnique()) {
                        uniqueKeys.add(key);
                    }
                }
                if (!uniqueKeys.isEmpty()) {
                    Set<List<Item>> uniqueCombinations = new HashSet<>();
                    for (Item member : members) {
                        List<Item> combination = new ArrayList<>();
                        for (String uniqueKey : uniqueKeys) {
                            combination.add(member.getItemByKey(uniqueKey));
                        }
                        if (!uniqueCombinations.add(combination)) {
                            throw new InvalidInstanceException(
                                    "Duplicate combination found for unique keys " + uniqueKeys
                            );
                        }
                    }
                }
            }

            if (itemType.getName() == null) {
                itemType = itemType.getBaseType();
            }
            return ItemFactory.getInstance().createAnnotatedItem(arrayItem, itemType);
        }
        if (itemType.isObjectItemType()) {
            if (!item.isObject()) {
                throw new InvalidInstanceException(
                        "Expected an object item for object type "
                            + itemType.toString()
                            + ", but have "
                            + item.serialize()
                );
            }
            List<String> keys = new ArrayList<>();
            List<Item> values = new ArrayList<>();
            List<String> facetKeys = itemType.getObjectKeysFacet();
            for (String key : item.getStringKeys()) {
                if (facetKeys.contains(key)) {
                    FieldDescriptor fieldDescriptor = itemType.getObjectContentFacet(key);
                    ItemType expectedType = fieldDescriptor.getType();
                    Item value = item.getItemByKey(key);
                    if (value.isNull()) {
                        if (expectedType.canBeNull()) {
                            keys.add(key);
                            values.add(validate(value, expectedType, metadata, true, staticContext));
                        } else if (fieldDescriptor.isRequired()) {
                            throw new InvalidInstanceException(
                                    "Null associated with required, non-nullable key in object type "
                                        + itemType.getIdentifierString()
                                        + " : "
                                        + key
                            );
                        } else if (!staticContext.getConfiguration().getLaxJSONNullValidation()) {
                            keys.add(key);
                            values.add(validate(value, expectedType, metadata, true, staticContext));
                        } else {
                            // In lax mode, prefer a successful cast when possible (e.g., null -> "null" for strings),
                            // and only treat null as absent if the cast fails.
                            try {
                                Item validatedNullValue = validate(
                                    value,
                                    expectedType,
                                    metadata,
                                    true,
                                    staticContext
                                );
                                keys.add(key);
                                values.add(validatedNullValue);
                            } catch (InvalidInstanceException ex) {
                                // Keep lax behavior: consider JSON null as absent for optional fields.
                            }
                        }
                    } else {
                        keys.add(key);
                        values.add(validate(item.getItemByKey(key), expectedType, metadata, true, staticContext));
                    }
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
            for (String key : itemType.getObjectKeysFacet()) {
                if (!item.getStringKeys().contains(key)) {
                    FieldDescriptor fieldDescriptor = itemType.getObjectContentFacet(key);
                    Item defaultValue = fieldDescriptor.getDefaultValue();
                    if (defaultValue != null) {
                        keys.add(key);
                        values.add(defaultValue);
                    }
                    if (fieldDescriptor.isRequired()) {
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
        if (itemType.isUnionType()) {
            List<ItemType> memberTypes = itemType.getTypes();
            if (item.isNull()) {
                for (ItemType memberType : memberTypes) {
                    if (memberType.equals(BuiltinTypesCatalogue.nullItem)) {
                        continue;
                    }
                    try {
                        return validate(item, memberType, metadata, true, staticContext);
                    } catch (InvalidInstanceException ex) {
                        // try next type
                    }
                }
            }
            for (ItemType memberType : memberTypes) {
                try {
                    return validate(item, memberType, metadata, true, staticContext);
                } catch (InvalidInstanceException ex) {
                    // try next type
                }
            }
            throw new InvalidInstanceException(
                    "Item " + item.serialize() + " does not conform to union type " + itemType.getIdentifierString()
            );
        }
        return item;
    }

    private static Item validateXmlNodeAgainstAtomicType(
            Item item,
            ItemType itemType,
            ExceptionMetadata metadata,
            RuntimeStaticContext staticContext
    ) {
        Item copiedRoot;
        Item validatedElement;
        if (item.isDocumentNode()) {
            validateAtomicDocumentShape(item);
            copiedRoot = item.copy(false);
            reattachXmlParents(copiedRoot, null);
            Item copiedDocumentElement = getSingleElementChild(copiedRoot);
            if (copiedDocumentElement == null) {
                throw new InvalidInstanceException(
                        "Document validation requires exactly one element child for atomic type "
                            + itemType.getIdentifierString(),
                        metadata
                );
            }
            validatedElement = copiedDocumentElement;
        } else if (item.isElementNode()) {
            validateAtomicElementShape(item);
            copiedRoot = item.copy(false);
            reattachXmlParents(copiedRoot, null);
            validatedElement = copiedRoot;
        } else {
            throw new InvalidInstanceException(
                    "Atomic XML validation is only supported for document and element nodes.",
                    metadata
            );
        }

        Item castType;
        try {
            castType = CastIterator.castItemToType(
                ItemFactory.getInstance().createUntypedAtomicItem(validatedElement.getStringValue()),
                itemType,
                metadata,
                staticContext
            );
        } catch (Exception e) {
            throw new InvalidInstanceException(
                    "Cannot cast " + item.serialize() + " to type " + itemType.getIdentifierString()
            );
        }
        if (castType == null) {
            throw new InvalidInstanceException(
                    "Cannot cast " + item.serialize() + " to type " + itemType.getIdentifierString()
            );
        }
        validatedElement.setSchemaType(itemType);
        return copiedRoot;
    }

    private static void validateAtomicDocumentShape(Item document) {
        Item documentElement = getSingleElementChild(document);
        if (documentElement == null) {
            throw new InvalidInstanceException(
                    "Document validation requires exactly one element child for atomic type validation."
            );
        }
        for (Item child : document.children()) {
            if (
                !child.isElementNode()
                    && !child.isCommentNode()
                    && !child.isProcessingInstructionNode()
            ) {
                throw new InvalidInstanceException(
                        "Document validation for atomic types only supports element, comment, and processing-instruction children."
                );
            }
        }
        validateAtomicElementShape(documentElement);
    }

    private static void validateAtomicElementShape(Item element) {
        if (!element.attributes().isEmpty()) {
            throw new InvalidInstanceException(
                    "Element validation for atomic types does not support attributes."
            );
        }
        for (Item child : element.children()) {
            if (child.isElementNode()) {
                throw new InvalidInstanceException(
                        "Element validation for atomic types does not support nested element children."
                );
            }
        }
    }

    private static Item getSingleElementChild(Item document) {
        Item elementChild = null;
        for (Item child : document.children()) {
            if (!child.isElementNode()) {
                continue;
            }
            if (elementChild != null) {
                return null;
            }
            elementChild = child;
        }
        return elementChild;
    }

    private static void reattachXmlParents(Item node, Item parent) {
        if (parent != null) {
            node.setParent(parent);
        }
        for (Item attribute : node.attributes()) {
            attribute.setParent(node);
        }
        for (Item child : node.children()) {
            reattachXmlParents(child, node);
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.isValidate) {
            return NativeClauseContext.NoNativeQuery;
        }
        return this.getChild(0).generateNativeQuery(nativeClauseContext);
    }


    public static ItemType inferSchemaTypeOfVariantDataFrame(Dataset<Row> df, ExceptionMetadata metadata) {
        if (df.isEmpty()) {
            return BuiltinTypesCatalogue.item;
        }
        df.createOrReplaceTempView("variant_table");

        Dataset<Row> schemaDf = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .sql(
                String.format(
                    "SELECT schema_of_variant_agg(`%s`) AS ddl FROM variant_table",
                    SparkSessionManager.nonObjectJSONiqItemColumnName
                )
            );
        String ddl = schemaDf.collectAsList().get(0).getString(0);

        if (ddl.contains("VARIANT")) {
            throw new CannotInferSchemaOnNonStructuredDataException(
                    "Cannot infer fully structured schema on non-structured data. The detected schema is: " + ddl,
                    metadata
            );
        }

        ddl = ddl.replace("OBJECT<", "STRUCT<");
        ItemType type = ItemTypeFactory.createItemType(
            DataType.fromDDL(String.format("`%s` %s", SparkSessionManager.nonObjectJSONiqItemColumnName, ddl))
        );
        type = type.getObjectContentFacet(SparkSessionManager.nonObjectJSONiqItemColumnName).getType();
        return type;
    }

}
