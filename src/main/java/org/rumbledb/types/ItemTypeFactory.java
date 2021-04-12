package org.rumbledb.types;

import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.Name;
import org.rumbledb.items.parsing.ItemParser;

import java.util.HashMap;
import java.util.Map;

public class ItemTypeFactory {
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
