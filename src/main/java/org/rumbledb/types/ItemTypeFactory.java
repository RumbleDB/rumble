package org.rumbledb.types;

import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.items.parsing.ItemParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemTypeFactory {

    public static ItemType createItemTypeFromJSoundCompactItem(Item item) {
        if (item.isString()) {
            String typeString = item.getStringValue();
            if (typeString.contains("=")) {
                throw new InvalidSchemaException("= not supported yet", ExceptionMetadata.EMPTY_METADATA);
            }
            Name typeName = Name.createVariableInDefaultTypeNamespace(typeString);
            if (!BuiltinTypesCatalogue.typeExists(typeName)) {
                throw new InvalidSchemaException("Type " + typeName + " not found.", ExceptionMetadata.EMPTY_METADATA);
            }
            BuiltinTypesCatalogue.getItemTypeByName(typeName);
        }
        if (item.isArray()) {
            List<Item> members = item.getItems();
            if (members.size() != 1) {
                throw new InvalidSchemaException("Invalid JSound: " + item, ExceptionMetadata.EMPTY_METADATA);
            }
            ItemType memberType = createItemTypeFromJSoundCompactItem(members.get(0));
            return new ArrayItemType(
                    null,
                    BuiltinTypesCatalogue.arrayItem,
                    new ArrayContentDescriptor(memberType),
                    null,
                    null,
                    Collections.emptyList()
            );
        }
        if (item.isObject()) {
            Map<String, FieldDescriptor> fields = new TreeMap<>();
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
                FieldDescriptor fieldDescriptor = new FieldDescriptor();
                fieldDescriptor.setName(key);
                fieldDescriptor.setRequired(required);
                fieldDescriptor.setType(createItemTypeFromJSoundCompactItem(value));
                fieldDescriptor.setUnique(false);
                fieldDescriptor.setDefaultValue(null);
                fields.put(key, fieldDescriptor);
            }
            return new ObjectItemType(
                    null,
                    BuiltinTypesCatalogue.objectItem,
                    false,
                    fields,
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }
        return null;
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
     * @param name of the new type (if null it is an anonymous type)
     * @param structType descriptor of the object
     * @return an object item type representing the type in Rumble
     */
    public static ObjectItemType createItemTypeFromSparkStructType(String name, StructType structType) {
        // TODO : handle type registration
        // TODO : identical anonymous types should be equivalent?
        Name objectName = name != null && !name.equals("") ? Name.createVariableInDefaultTypeNamespace(name) : null;
        Map<String, FieldDescriptor> content = new HashMap<>();
        for (StructField field : structType.fields()) {
            DataType filedType = field.dataType();
            ItemType mappedItemType;
            if (filedType instanceof StructType) {
                mappedItemType = createItemTypeFromSparkStructType(null, (StructType) filedType);
            } else if (filedType instanceof ArrayType) {
                // TODO : add proper function
                mappedItemType = BuiltinTypesCatalogue.arrayItem;
            } else {
                mappedItemType = ItemParser.convertDataTypeToItemType(filedType);
            }
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            fieldDescriptor.setName(field.name());
            fieldDescriptor.setType(mappedItemType);
            fieldDescriptor.setRequired(!field.nullable());
            // TODO : how to deal with duplicate keys?
            content.put(field.name(), fieldDescriptor);
        }

        return new ObjectItemType(objectName, BuiltinTypesCatalogue.objectItem, true, content, null, null);
    }
}
