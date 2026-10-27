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
package org.rumbledb.items;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveIntegerItemTest {
    static Stream<Item> integers() {
        return Stream.of(
            new IntPosingAsIntegerItem(Integer.MIN_VALUE),
            new IntPosingAsIntegerItem(0),
            new IntPosingAsIntegerItem(Integer.MAX_VALUE),
            new LongPosingAsIntegerItem(Long.MIN_VALUE),
            new LongPosingAsIntegerItem(0),
            new LongPosingAsIntegerItem(Long.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("integers")
    void preservesIntegerSemanticsAcrossStorageWidthsAndCopies(Item item) {
        Item reference = new IntegerItem(new BigInteger(item.getStringValue()));
        for (Item value : new Item[] { item, item.copy(false), item.copy(true) }) {
            assertSame(BuiltinTypesCatalogue.integerItem, value.getDynamicType());
            assertTrue(value.isInteger());
            assertTrue(value.isDecimal());
            assertFalse(value.isInt());
            assertEquals(reference.getIntegerValue(), value.getIntegerValue());
            assertEquals(reference.getDecimalValue(), value.getDecimalValue());
            assertEquals(reference.getVariantValue(), value.getVariantValue());
            assertEquals(reference.getEffectiveBooleanValue(), value.getEffectiveBooleanValue());
            assertEquals(reference.getSparkSQLType(), value.getSparkSQLType());
            assertEquals(reference.getSparkSQLValue(), value.getSparkSQLValue());
            assertEquals(item.getClass(), value.getClass());
        }
    }
    @ParameterizedTest
    @CsvSource({
        "0, IntPosingAsIntegerItem",
        "3, IntPosingAsIntegerItem",
        "-2147483648, IntPosingAsIntegerItem",
        "2147483647, IntPosingAsIntegerItem",
        "-2147483649, LongPosingAsIntegerItem",
        "2147483648, LongPosingAsIntegerItem",
        "-9223372036854775808, LongPosingAsIntegerItem",
        "9223372036854775807, LongPosingAsIntegerItem",
        "-9223372036854775809, IntegerItem",
        "9223372036854775808, IntegerItem"
    })
    void factorySelectsCompactStorageWithoutChangingIntegerType(String lexicalValue, String expectedClass) {
        ItemFactory factory = ItemFactory.getInstance();
        BigInteger expectedValue = new BigInteger(lexicalValue);
        for (Item item : new Item[] {
            factory.createIntegerItem(lexicalValue), factory.createIntegerItem(expectedValue)
        }) {
            assertEquals(expectedClass, item.getClass().getSimpleName());
            assertSame(BuiltinTypesCatalogue.integerItem, item.getDynamicType());
            assertFalse(item.isInt());
            assertEquals(expectedValue, item.getIntegerValue());
        }
    }
}
