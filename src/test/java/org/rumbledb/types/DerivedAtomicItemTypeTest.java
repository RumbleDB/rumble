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
package org.rumbledb.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

public class DerivedAtomicItemTypeTest {
    @Test
    public void dateTimeStampAndItsSubtypesAreCompatibleWithDataFrames() {
        RumbleConfiguration configuration = RumbleConfiguration.builder().build();
        ItemType subtype = new DerivedAtomicItemType(
                new Name("urn:test", "test", "eventTimestamp"),
                BuiltinTypesCatalogue.dateTimeStampItem,
                BuiltinTypesCatalogue.dateTimeItem,
                new Facets());

        Assertions.assertTrue(BuiltinTypesCatalogue.dateTimeStampItem.isCompatibleWithDataFrames(configuration));
        Assertions.assertTrue(subtype.isCompatibleWithDataFrames(configuration));
        Assertions.assertFalse(BuiltinTypesCatalogue.dateTimeItem.isCompatibleWithDataFrames(configuration));
        ItemType dateTimeSubtype = new DerivedAtomicItemType(
                new Name("urn:test", "test", "eventDateTime"),
                BuiltinTypesCatalogue.dateTimeItem,
                BuiltinTypesCatalogue.dateTimeItem,
                new Facets());
        Assertions.assertFalse(dateTimeSubtype.isCompatibleWithDataFrames(configuration));
    }
}
