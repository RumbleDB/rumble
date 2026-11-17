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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

class AtomicItemEquivalenceTest {
    private static final ItemFactory F = ItemFactory.getInstance();

    @Test
    void sameKeysHaveTheSameHash() {
        List<List<Item>> sameKeys = List.of(
                List.of(F.createIntItem(100), F.createIntegerItem(BigInteger.valueOf(100))),
                List.of(F.createIntItem(100), F.createDecimalItem(new BigDecimal("100.00"))),
                List.of(F.createIntItem(100), F.createDoubleItem(1e2), F.createFloatItem(100f)),
                List.of(F.createDecimalItem(new BigDecimal("0.5")), F.createDoubleItem(0.5)),
                List.of(F.createDecimalItem(BigDecimal.ZERO), F.createDoubleItem(-0.0)),
                List.of(F.createDoubleItem(Double.NaN), F.createFloatItem(Float.NaN)),
                List.of(F.createDoubleItem(1e300), F.createDecimalItem(new BigDecimal(1e300))),
                List.of(F.createStringItem("a"), F.createUntypedAtomicItem("a"), F.createAnyURIItem("a")),
                List.of(
                        F.createDateTimeItem("2000-01-01T12:00:00Z"),
                        F.createDateTimeItem("2000-01-01T13:00:00+01:00")),
                List.of(F.createDateItem("2000-01-01Z"), F.createDateItem("2000-01-01+00:00")),
                List.of(F.createTimeItem("12:00:00Z"), F.createTimeItem("13:00:00+01:00")),
                List.of(F.createGYearItem("2000Z"), F.createGYearItem("2000+00:00")),
                List.of(F.createGYearMonthItem("2000-01"), F.createGYearMonthItem("2000-01")),
                List.of(F.createGMonthDayItem("--01-01Z"), F.createGMonthDayItem("--01-01+00:00")),
                List.of(F.createGMonthItem("--01"), F.createGMonthItem("--01")),
                List.of(F.createGDayItem("---01"), F.createGDayItem("---01")),
                List.of(F.createYearMonthDurationItem("P1Y"), F.createDurationItem("P12M")),
                List.of(F.createDayTimeDurationItem("P1D"), F.createDurationItem("PT24H")),
                List.of(F.createYearMonthDurationItem("P0M"), F.createDayTimeDurationItem("PT0S")),
                List.of(F.createHexBinaryItem("0A"), F.createHexBinaryItem("0a")),
                List.of(F.createBase64BinaryItem("AQI="), F.createBase64BinaryItem("AQI=")),
                List.of(F.createBooleanItem(true), F.createBooleanItem(true)),
                List.of(
                        F.createQNameItem(new Name("http://example.com", "a", "x")),
                        F.createQNameItem(new Name("http://example.com", "b", "x"))));
        for (List<Item> keys : sameKeys) {
            for (Item other : keys) {
                Assertions.assertTrue(AtomicItemEquivalence.equivalent(keys.get(0), other), keys + " should be equal");
                Assertions.assertEquals(
                        AtomicItemEquivalence.hash(keys.get(0)),
                        AtomicItemEquivalence.hash(other),
                        keys + " should have the same hash");
            }
        }
    }
}
