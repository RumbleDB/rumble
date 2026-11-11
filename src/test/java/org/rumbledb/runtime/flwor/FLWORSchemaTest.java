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
package org.rumbledb.runtime.flwor;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.Name;

class FLWORSchemaTest {
    private Name name(String value) {
        return Name.createVariableInNoNamespace(value);
    }

    @Test
    void checksColumnsAfterSerializedColumnAndMissingColumns() {
        FLWORSchema schema = new FLWORSchema();
        schema.setBytes(name("x"));
        StructType physical = new StructType().add("x", DataTypes.BinaryType).add("y", DataTypes.BinaryType);
        assertFalse(schema.isConsistent(physical));
        schema.setBytes(name("y"));
        assertTrue(schema.isConsistent(physical));
        schema.setBytes(name("missing"));
        assertFalse(schema.isConsistent(physical));
    }

    @Test
    void infersRepresentationsAndCopiesIndependently() {
        StructType physical = new StructType()
                .add("x", DataTypes.BinaryType)
                .add("y.sequence", DataTypes.createArrayType(DataTypes.BinaryType))
                .add("z.count", DataTypes.LongType)
                .add("n", DataTypes.IntegerType);
        FLWORSchema schema = new FLWORSchema(physical);
        assertTrue(schema.isConsistent(physical));
        assertTrue(schema.isBytes(name("x")));
        assertTrue(schema.isArraysOfBytes(name("y.sequence")));
        assertTrue(schema.isCount(name("z.count")));
        assertNotNull(schema.getNativeType(name("n")));
        FLWORSchema copy = new FLWORSchema(schema);
        copy.setCount(name("x"));
        assertFalse(copy.isConsistent(physical));
        assertTrue(schema.isConsistent(physical));
    }
}
