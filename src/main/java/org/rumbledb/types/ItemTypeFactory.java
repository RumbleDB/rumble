package org.rumbledb.types;

import org.apache.log4j.LogManager;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.CharType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.VarcharType;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.ItemFactory;

import java.util.*;

public class ItemTypeFactory {

    public static ItemType createItemTypeFromJSoundCompactItem(Name name, Item item, StaticContext staticContext) {
        if (item.isString()) {
            String typeString = item.getStringValue();
            if (typeString.contains("=")) {
                throw new InvalidSchemaException(
                        "= is only supported for the types of object values",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            return new ItemTypeReference(Name.createTypeNameFromLiteral(typeString, staticContext));
        }
        if (item.isArray()) {
            List<Item> members = item.getItems();
            if (members.size() != 1) {
                throw new InvalidSchemaException(
                        "Invalid JSound, an array type should only contain one member type: " + item.serialize(),
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            ItemType memberType = createItemTypeFromJSoundCompactItem(null, members.get(0), staticContext);
            return new ArrayItemType(
                    null,
                    BuiltinTypesCatalogue.arrayItem,
                    memberType,
                    null,
                    null,
                    Collections.emptyList()
            );
        }
        if (item.isObject()) {
            Map<String, FieldDescriptor> fields = new LinkedHashMap<>();
            for (String key : item.getKeys()) {
                Item value = item.getItemByKey(key);
                boolean required = false;
                if (key.startsWith("!")) {
                    key = key.substring(1);
                    required = true;
                }
                if (key.endsWith("?")) {
                    throw new InvalidSchemaException("? not supported yet", ExceptionMetadata.EMPTY_METADATA);
                }
                if (key.startsWith("@")) {
                    throw new InvalidSchemaException("@ not supported yet", ExceptionMetadata.EMPTY_METADATA);
                }
                Item defaultValueLiteral = null;
                if (value.isString() && value.getStringValue().contains("=")) {
                    String typeString = value.getStringValue();
                    int index = typeString.indexOf("=");
                    String defaultLiteral = typeString.substring(index + 1);
                    if (defaultLiteral.contains("=")) {
                        throw new InvalidSchemaException(
                                "= can only appear once in a field descriptor type reference",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    typeString = typeString.substring(0, index);
                    value = ItemFactory.getInstance().createStringItem(typeString);
                    defaultValueLiteral = ItemFactory.getInstance().createStringItem(defaultLiteral);
                }

                FieldDescriptor fieldDescriptor = new FieldDescriptor();
                fieldDescriptor.setName(key);
                fieldDescriptor.setRequired(required);
                ItemType type = createItemTypeFromJSoundCompactItem(null, value, staticContext);
                fieldDescriptor.setType(type);
                fieldDescriptor.setUnique(false);
                fieldDescriptor.setDefaultValue(defaultValueLiteral);
                fields.put(key, fieldDescriptor);
            }
            return new ObjectItemType(
                    name,
                    BuiltinTypesCatalogue.objectItem,
                    true,
                    fields,
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }
        throw new InvalidSchemaException("Invalid JSound type definition: " + item, ExceptionMetadata.EMPTY_METADATA);
    }

    /**
     * Create an anonymous object type from keys and values.
     * 
     * @param keys a list of String representing the keys of the object
     * @param values a list of ItemType of the values, all with arity == Arity.One
     * @return an anonymous object type based on the provided keys and values
     */
    public static ItemType createAnonymousObjectType(List<String> keys, List<ItemType> values) {
        if (keys.size() != values.size()) {
            throw new InvalidSchemaException(
                    "Key list and value list must have the same dimensions",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        Map<String, FieldDescriptor> content = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            ItemType field = values.get(i);
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            fieldDescriptor.setName(key);
            fieldDescriptor.setType(field);
            fieldDescriptor.setRequired(true);
            content.put(key, fieldDescriptor);
        }
        return new ObjectItemType(
                null,
                BuiltinTypesCatalogue.objectItem,
                true,
                content,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    /**
     * Create an anonymous array type from keys and values.
     * 
     * @param content an item type for the content
     * @return an anonymous array type based on the provided content type
     */
    public static ItemType createAnonymousArrayType(ItemType content) {
        return new ArrayItemType(
                null,
                BuiltinTypesCatalogue.arrayItem,
                content,
                null,
                null,
                null
        );
    }

    public static ItemType createItemTypeFromJSoundVerboseItem(Name name, Item item, StaticContext staticContext) {
        if (!item.isObject()) {
            throw new InvalidSchemaException(
                    "A JSound verbose schema must be an object",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        List<String> keys = item.getKeys();
        if (!keys.contains("kind")) {
            throw new InvalidSchemaException(
                    "A JSound verbose schema must contain a 'kind' field.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        String kind = item.getItemByKey("kind").getStringValue();
        ItemType baseType = null;
        if (keys.contains("baseType")) {
            String baseTypeString = item.getItemByKey("baseType").getStringValue();
            Name baseTypeName = Name.createTypeNameFromLiteral(baseTypeString, staticContext);
            baseType = new ItemTypeReference(baseTypeName);
        }
        if (keys.contains("name")) {
            String declaredNameString = item.getItemByKey("name").getStringValue();
            Name declaredName = Name.createTypeNameFromLiteral(declaredNameString, staticContext);
            if (!declaredName.equals(name)) {
                throw new InvalidSchemaException(
                        "The 'name' field does not match the type's name.",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
        }
        if (keys.contains("enumeration")) {
            throw new UnsupportedFeatureException(
                    "The enumeration facet is not supported yet, but it will come in a subsequent release.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (keys.contains("constraints")) {
            throw new UnsupportedFeatureException(
                    "The constraints facet is not supported yet, but it will come in a subsequent release.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        switch (kind) {
            case "object":
                if (baseType == null) {
                    baseType = BuiltinTypesCatalogue.objectItem;
                }

                Item contentItem = item.getItemByKey("content");
                if (!keys.contains("content")) {
                    LogManager.getLogger("ItemTypeFactory")
                        .warn(
                            "The content facet of an object type is missing. By default, no fields are defined or overriden."
                        );
                    contentItem = ItemFactory.getInstance().createArrayItem();
                } else {
                    if (contentItem == null) {
                        contentItem = ItemFactory.getInstance().createArrayItem();
                    }
                    if (!contentItem.isArray()) {
                        throw new InvalidSchemaException(
                                "The content facet must be an array",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                }

                boolean closed = false;
                Item closedItem = item.getItemByKey("closed");
                if (closedItem != null && !closedItem.isBoolean()) {
                    throw new InvalidSchemaException(
                            "'closed' must be a boolean.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (closedItem != null) {
                    closed = closedItem.getBooleanValue();
                } else {
                    LogManager.getLogger("ItemTypeFactory")
                        .warn(
                            "The closed facet of an object type is missing. By default, a closed object type is created. Set closed to false to keep the type open and allow arbitrary fields."
                        );
                    closed = true;
                }
                List<Item> contents = contentItem.getItems();
                Map<String, FieldDescriptor> fields = new LinkedHashMap<>();
                for (Item c : contents) {
                    Item fieldItem = c.getItemByKey("name");
                    if (fieldItem == null) {
                        throw new InvalidSchemaException(
                                "Field descriptor is missing a name.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    if (!fieldItem.isString()) {
                        throw new InvalidSchemaException(
                                "The name of a field must be a string.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    String fieldName = fieldItem.getStringValue();

                    Item typeItem = c.getItemByKey("type");
                    if (typeItem == null) {
                        throw new InvalidSchemaException(
                                "Field descriptor is missing a type.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    ItemType type = null;
                    if (typeItem.isString()) {
                        type = new ItemTypeReference(
                                Name.createTypeNameFromLiteral(typeItem.getStringValue(), staticContext)
                        );
                    } else if (typeItem.isObject()) {
                        type = createItemTypeFromJSoundVerboseItem(null, typeItem, staticContext);
                    } else {
                        throw new InvalidSchemaException(
                                "The tyep of a field descriptor must be a string or an object.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }

                    boolean required = false;
                    boolean isRequiredSet = false;
                    Item requiredItem = c.getItemByKey("required");
                    if (requiredItem != null && !requiredItem.isBoolean()) {
                        throw new InvalidSchemaException(
                                "'required' must be a boolean.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    if (requiredItem != null) {
                        required = requiredItem.getBooleanValue();
                        isRequiredSet = true;
                    }

                    boolean unique = false;
                    boolean isUniqueSet = false;
                    Item uniqueItem = c.getItemByKey("unique");
                    if (uniqueItem != null && !uniqueItem.isBoolean()) {
                        throw new InvalidSchemaException(
                                "'unique' must be a boolean.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    if (uniqueItem != null) {
                        unique = uniqueItem.getBooleanValue();
                        isUniqueSet = true;
                    }

                    Item defaultValue = c.getItemByKey("default");
                    if (defaultValue != null && !defaultValue.isAtomic()) {
                        throw new InvalidSchemaException(
                                "'default' must be an atomic value. Default values for non-atomic types are not supported yet.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    FieldDescriptor fieldDescriptor = new FieldDescriptor();
                    fieldDescriptor.setName(fieldName);
                    fieldDescriptor.setType(type);
                    if (isRequiredSet) {
                        fieldDescriptor.setRequired(required);
                    }
                    if (isUniqueSet) {
                        fieldDescriptor.setUnique(unique);
                    }
                    if (defaultValue != null) {
                        fieldDescriptor.setDefaultValue(defaultValue);
                    }
                    fields.put(fieldName, fieldDescriptor);
                }
                ItemType it = new ObjectItemType(
                        name,
                        baseType,
                        closed,
                        fields,
                        Collections.emptyList(),
                        Collections.emptyList()
                );
                return it;
            case "array":
                if (baseType == null) {
                    baseType = BuiltinTypesCatalogue.arrayItem;
                }
                if (!keys.contains("content")) {
                    throw new InvalidSchemaException(
                            "The content facet is required in an array type declaration.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                contentItem = item.getItemByKey("content");
                if (contentItem == null) {
                    contentItem = ItemFactory.getInstance().createArrayItem();
                }
                ItemType memberType = null;
                if (contentItem.isString()) {
                    memberType = new ItemTypeReference(
                            Name.createTypeNameFromLiteral(contentItem.getStringValue(), staticContext)
                    );
                } else if (contentItem.isObject()) {
                    memberType = createItemTypeFromJSoundVerboseItem(null, contentItem, staticContext);
                } else {
                    throw new InvalidSchemaException(
                            "The content of an array must be a string or an object.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                Integer length = null;
                Integer minLength = null;
                Integer maxLength = null;
                List<Item> enumeration = null;
                Item minInclusive = null;
                Item maxInclusive = null;
                Item minExclusive = null;
                Item maxExclusive = null;
                Integer totalDigits = null;
                Integer fractionDigits = null;

                if (keys.contains("enumeration")) {
                    throw new InvalidSchemaException(
                            "The enumeration facet is not released yet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (keys.contains("minLength")) {
                    Item minLengthItem = item.getItemByKey("minLength");
                    if (!minLengthItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The minLength facet must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    minLength = minLengthItem.castToIntValue();
                }
                if (keys.contains("maxLength")) {
                    Item maxLengthItem = item.getItemByKey("maxLength");
                    if (!maxLengthItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The maxLength facet must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    maxLength = maxLengthItem.castToIntValue();
                }

                return new ArrayItemType(
                        name,
                        baseType,
                        memberType,
                        minLength,
                        maxLength,
                        enumeration
                );
            case "atomic":
                length = null;
                minLength = null;
                maxLength = null;
                enumeration = null;
                minInclusive = null;
                maxInclusive = null;
                minExclusive = null;
                maxExclusive = null;
                totalDigits = null;
                fractionDigits = null;
                if (baseType == null) {
                    throw new InvalidSchemaException(
                            "BaseType is required for an atomic user-defined type.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (keys.contains("length")) {
                    Item lengthItem = item.getItemByKey("length");
                    if (!lengthItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The length facet must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    length = lengthItem.castToIntValue();
                }

                if (keys.contains("minLength")) {
                    Item minLengthItem = item.getItemByKey("minLength");
                    if (!minLengthItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The minLength facet must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    minLength = minLengthItem.castToIntValue();
                }

                if (keys.contains("maxLength")) {
                    Item maxLengthItem = item.getItemByKey("maxLength");
                    if (!maxLengthItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The maxLength facet must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    maxLength = maxLengthItem.castToIntValue();
                }

                if (keys.contains("enumeration")) {
                    throw new InvalidSchemaException(
                            "The enumeration facet is not released yet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (keys.contains("minInclusive")) {
                    Item minInclusiveItem = item.getItemByKey("minInclusive");
                    if (!minInclusiveItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The minInclusive fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    minInclusive = minInclusiveItem;
                }
                if (keys.contains("maxInclusive")) {
                    Item maxInclusiveItem = item.getItemByKey("maxInclusive");
                    if (!maxInclusiveItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The maxInclusive fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    maxInclusive = maxInclusiveItem;
                }
                if (keys.contains("minExclusive")) {
                    Item minExclusiveItem = item.getItemByKey("minExclusive");
                    if (!minExclusiveItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The minExclusive fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    minExclusive = minExclusiveItem;
                }
                if (keys.contains("maxExclusive")) {
                    Item maxExclusiveItem = item.getItemByKey("maxExclusive");
                    if (!maxExclusiveItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The maxExclusive fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    maxExclusive = maxExclusiveItem;
                }
                if (keys.contains("totalDigits")) {
                    Item totalDigitsItem = item.getItemByKey("totalDigits");
                    if (!totalDigitsItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The totalDigits fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    totalDigits = totalDigitsItem.castToIntValue();
                }
                if (keys.contains("fractionDigits")) {
                    Item fractionDigitsItem = item.getItemByKey("fractionDigits");
                    if (!fractionDigitsItem.isNumeric()) {
                        throw new InvalidSchemaException(
                                "The fractionDigits fact must be a numeric value.",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    fractionDigits = fractionDigitsItem.castToIntValue();
                }

                // Item contains syntax of JSound schema
                return new DerivedAtomicItemType(
                        name,
                        baseType,
                        baseType,
                        Facets.createAtomicTypeFacets(
                            length,
                            enumeration,
                            minLength,
                            maxLength,
                            minInclusive,
                            maxInclusive,
                            minExclusive,
                            maxExclusive,
                            totalDigits,
                            fractionDigits
                        )
                );
            case "union":
                throw new OurBadException("Kind union is not supported yet.");
            default:
                throw new InvalidSchemaException(
                        "Kind '" + kind + "' does not exist.",
                        ExceptionMetadata.EMPTY_METADATA
                );
        }
    }

    public static ItemType createItemTypeFromJSONSchemaItem(Name name, Item item, StaticContext staticContext) {
        throw new OurBadException("The JSON Schema syntax is not supported yet.");
    }

    /**
     * @param signature of the wanted function item type
     * @return a function item type with the given signature
     */
    public static FunctionItemType createFunctionItemType(FunctionSignature signature) {
        return new FunctionItemType(signature);
    }

    /**
     * Create an object item type from a spark struct type (count as restriction on generic object type)
     * 
     * @param structType descriptor of the object
     * @return an object item type representing the type in Rumble
     */
    private static ItemType createItemTypeFromSparkStructType(StructType structType) {
        // TODO : handle type registration
        // TODO : identical anonymous types should be equivalent?
        Map<String, FieldDescriptor> content = new LinkedHashMap<>();
        for (StructField field : structType.fields()) {
            DataType filedType = field.dataType();
            ItemType mappedItemType;
            if (filedType instanceof StructType) {
                mappedItemType = createItemTypeFromSparkStructType((StructType) filedType);
            } else {
                mappedItemType = createItemType(filedType);
            }
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            fieldDescriptor.setName(field.name());
            fieldDescriptor.setType(mappedItemType);
            fieldDescriptor.setRequired(!field.nullable());
            // TODO : how to deal with duplicate keys?
            content.put(field.name(), fieldDescriptor);
        }

        return new ObjectItemType(null, BuiltinTypesCatalogue.objectItem, true, content, null, null);
    }

    private static ItemType createArrayTypeWithSparkDataTypeContent(DataType type) {
        return new ArrayItemType(
                null,
                BuiltinTypesCatalogue.arrayItem,
                createItemType(type),
                null,
                null,
                null
        );
    }

    public static ItemType createItemType(DataType dt) {
        if (dt instanceof StructType) {
            return createItemTypeFromSparkStructType((StructType) dt);
        }
        if (dt instanceof ArrayType) {
            return createArrayTypeWithSparkDataTypeContent(
                ((ArrayType) dt).elementType()
            );
        }
        if (dt.equals(DataTypes.StringType)) {
            return BuiltinTypesCatalogue.stringItem;
        } else if (dt instanceof VarcharType) {
            return BuiltinTypesCatalogue.stringItem;
        } else if (dt instanceof CharType) {
            return BuiltinTypesCatalogue.stringItem;
        } else if (dt.equals(DataTypes.BooleanType)) {
            return BuiltinTypesCatalogue.booleanItem;
        } else if (dt.equals(DataTypes.DoubleType)) {
            return BuiltinTypesCatalogue.doubleItem;
        } else if (dt.equals(DataTypes.IntegerType)) {
            return BuiltinTypesCatalogue.integerItem;
        } else if (dt.equals(DataTypes.FloatType)) {
            return BuiltinTypesCatalogue.floatItem;
        } else if (dt instanceof DecimalType && ((DecimalType) dt).scale() == 0) {
            return BuiltinTypesCatalogue.integerItem;
        } else if (dt instanceof DecimalType) {
            return BuiltinTypesCatalogue.decimalItem;
        } else if (dt.equals(DataTypes.LongType)) {
            return BuiltinTypesCatalogue.integerItem;
        } else if (dt.equals(DataTypes.NullType)) {
            return BuiltinTypesCatalogue.nullItem;
        } else if (dt.equals(DataTypes.ShortType)) {
            return BuiltinTypesCatalogue.integerItem;
        } else if (dt.equals(DataTypes.ByteType)) {
            return BuiltinTypesCatalogue.integerItem;
        } else if (dt.equals(DataTypes.TimestampType)) {
            return BuiltinTypesCatalogue.dateTimeItem;
        } else if (dt.equals(DataTypes.DateType)) {
            return BuiltinTypesCatalogue.dateItem;
        } else if (dt.equals(DataTypes.BinaryType)) {
            return BuiltinTypesCatalogue.hexBinaryItem;
        } else if (dt.equals(DataTypes.VariantType)) {
            return BuiltinTypesCatalogue.item;
        } else if (dt instanceof VectorUDT) {
            return createArrayTypeWithSparkDataTypeContent(
                DataTypes.DoubleType
            );
        }
        throw new OurBadException("DataFrame type unsupported: " + dt);
    }
}
