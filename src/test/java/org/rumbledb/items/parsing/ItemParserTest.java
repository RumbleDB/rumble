/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.items.parsing;

import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

class ItemParserTest {
    private static final StructType BOOLEAN_FIELDS = DataTypes.createStructType(List.of(
            DataTypes.createStructField("is_1", DataTypes.BooleanType, true),
            DataTypes.createStructField("is_not_1", DataTypes.BooleanType, true)));

    @Test
    void retainsNullForRequiredConstructorFields() {
        Item object = parse(new GenericRowWithSchema(new Object[] {true, null}, BOOLEAN_FIELDS), constructorType());
        assertEquals(List.of("is_1", "is_not_1"), object.getStringKeys());
        assertTrue(object.getItemByKey("is_1").getBooleanValue());
        assertTrue(object.getItemByKey("is_not_1").isNull());
    }

    @Test
    void omitsNullForOptionalSourceFields() {
        ItemType sourceType = ItemTypeFactory.createItemType(BOOLEAN_FIELDS);
        Item object = parse(new GenericRowWithSchema(new Object[] {true, null}, BOOLEAN_FIELDS), sourceType);
        assertEquals(List.of("is_1"), object.getStringKeys());
    }

    @Test
    void omitsNullWithoutFieldTypeInformation() {
        Item object = parse(new GenericRowWithSchema(new Object[] {true, null}, BOOLEAN_FIELDS), null);
        assertEquals(List.of("is_1"), object.getStringKeys());
    }

    @Test
    void retainsRequiredNullInsideNestedObjects() {
        StructType schema =
                DataTypes.createStructType(List.of(DataTypes.createStructField("nested", BOOLEAN_FIELDS, false)));
        ItemType type = ItemTypeFactory.createAnonymousObjectType(List.of("nested"), List.of(constructorType()));
        Row nested = new GenericRowWithSchema(new Object[] {null, true}, BOOLEAN_FIELDS);
        Item object = parse(new GenericRowWithSchema(new Object[] {nested}, schema), type);
        Item nestedObject = object.getItemByKey("nested");
        assertEquals(List.of("is_1", "is_not_1"), nestedObject.getStringKeys());
        assertTrue(nestedObject.getItemByKey("is_1").isNull());
        assertTrue(nestedObject.getItemByKey("is_not_1").getBooleanValue());
    }

    @Test
    void retainsExplicitNullColumnsButOmitsEmptyObjectSentinel() {
        StructType schema = DataTypes.createStructType(List.of(
                DataTypes.createStructField("explicit_null", DataTypes.NullType, true),
                DataTypes.createStructField(
                        SparkSessionManager.emptyObjectJSONiqItemColumnName, DataTypes.NullType, true)));
        Item object = parse(new GenericRowWithSchema(new Object[] {null, null}, schema), null);
        assertEquals(List.of("explicit_null"), object.getStringKeys());
        assertTrue(object.getItemByKey("explicit_null").isNull());
    }

    private static ItemType constructorType() {
        ItemType fieldType = ItemTypeFactory.createObjectFieldType(
                new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.OneOrZero));
        return ItemTypeFactory.createAnonymousObjectType(List.of("is_1", "is_not_1"), List.of(fieldType, fieldType));
    }

    private static Item parse(Row row, ItemType type) {
        return ItemParser.getItemFromRow(row, ExceptionMetadata.EMPTY_METADATA, type);
    }
}
