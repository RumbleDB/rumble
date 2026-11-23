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
package org.rumbledb.xml.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.rumbledb.context.Name;
import org.rumbledb.types.ItemTypeFactory;

class SchemaContentModelTest {
    @Test
    void constructedElementsHaveNoSchemaContentModel() {
        Name name = Name.createVariableInNoNamespace("a");
        assertFalse(SchemaContentModel.isSchemaTyped(ItemTypeFactory.constructedElementNodeItemType(name, false)));
        assertFalse(SchemaContentModel.isSchemaTyped(ItemTypeFactory.constructedElementNodeItemType(name, true)));
    }
}
