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

import java.util.List;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.Name;
import org.rumbledb.runtime.flwor.FLWORSchema.ColumnBinding;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn.ColumnFormat;
import org.rumbledb.types.SequenceType;

class FLWORSchemaTest {
    private Name name(String value) {
        return Name.createVariableInNoNamespace(value);
    }

    @Test
    void validatesExactPhysicalTypesAndColumnCoverage() {
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(name("x"), new ColumnBinding("storage", ColumnFormat.FULLY_NATIVE, DataTypes.StringType));
        assertFalse(schema.isConsistent(new StructType().add("storage", DataTypes.IntegerType)));
        assertTrue(schema.isConsistent(new StructType().add("storage", DataTypes.StringType)));
        assertFalse(schema.isConsistent(
                new StructType().add("storage", DataTypes.StringType).add("extra", DataTypes.StringType)));
        assertFalse(schema.isConsistent(new StructType()));
        assertFalse(schema.isConsistent(
                new StructType().add("storage", DataTypes.StringType).add("storage", DataTypes.StringType)));
        assertThrows(
                IllegalArgumentException.class,
                () -> schema.addColumn(
                        name("y"), new ColumnBinding("storage", ColumnFormat.FULLY_NATIVE, DataTypes.StringType)));
    }

    @Test
    void rejectsIncompatibleRepresentations() {
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(
                name("x"), new ColumnBinding("payload", ColumnFormat.SERIALIZED_SEQUENCE, DataTypes.StringType));
        assertFalse(schema.isConsistent(new StructType().add("payload", DataTypes.StringType)));
        FLWORSchema count = new FLWORSchema();
        count.addColumn(name("x"), new ColumnBinding("total", ColumnFormat.COUNT, DataTypes.StringType));
        assertFalse(count.isConsistent(new StructType().add("total", DataTypes.StringType)));
    }

    @Test
    void distinguishesArrayValuesFromSequencesAndSupportsMultipleColumns() {
        var binaryArray = DataTypes.createArrayType(DataTypes.BinaryType);
        StructType physical = new StructType()
                .add("x", binaryArray)
                .add("y.sequence", binaryArray)
                .add("y.count", DataTypes.LongType)
                .add("y.sum", DataTypes.DoubleType);
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(name("x"), new ColumnBinding("x", ColumnFormat.FULLY_NATIVE, binaryArray));
        schema.addColumn(name("y"), new ColumnBinding("y.sequence", ColumnFormat.NATIVE_SEQUENCE, binaryArray));
        schema.addColumn(name("y"), new ColumnBinding("y.count", ColumnFormat.COUNT, DataTypes.LongType));
        schema.addColumn(name("y"), new ColumnBinding("y.sum", ColumnFormat.SUM, DataTypes.DoubleType));
        assertTrue(schema.isConsistent(physical));
        assertFalse(schema.isArraysOfBytes(name("x")));
        assertNotNull(schema.getNativeType(name("x")));
        assertTrue(schema.isArraysOfBytes(name("y")));
        assertTrue(schema.isCount(name("y")));
        assertEquals(
                List.of("y.sequence", "y.count", "y.sum"),
                schema.getColumns(name("y")).stream()
                        .map(ColumnBinding::columnName)
                        .toList());
        assertEquals(2, schema.getVariables().size());
    }

    @Test
    void projectionKeepsExplicitOwnersAndSnapshotsAreIndependent() {
        Name x = name("x");
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(x, new ColumnBinding("payload", ColumnFormat.FULLY_NATIVE, DataTypes.IntegerType));
        schema.addColumn(x, new ColumnBinding("size", ColumnFormat.COUNT, DataTypes.LongType));
        schema.setVariableType(x, SequenceType.createSequenceType("int"));
        FLWORSchema projected =
                schema.select(java.util.Map.of(x, org.rumbledb.context.DynamicContext.VariableDependency.FULL));
        assertEquals(
                List.of("payload"),
                projected.getColumns(x).stream().map(ColumnBinding::columnName).toList());
        assertEquals(SequenceType.createSequenceType("int"), projected.getVariableType(x));
        assertEquals(2, schema.getColumns(x).size());
        FLWORSchema copy = new FLWORSchema(schema);
        copy.setVariableType(x, SequenceType.createSequenceType("string"));
        assertEquals(SequenceType.createSequenceType("int"), schema.getVariableType(x));
        assertThrows(
                UnsupportedOperationException.class, () -> copy.getColumns(x).clear());
        assertThrows(
                UnsupportedOperationException.class, () -> copy.getVariables().clear());
        assertTrue(schema.selectVariables(List.of()).getVariables().isEmpty());
        FLWORSchema replacement = new FLWORSchema(schema);
        replacement.replaceVariable(
                x,
                SequenceType.createSequenceType("string"),
                List.of(new ColumnBinding("new_payload", ColumnFormat.FULLY_NATIVE, DataTypes.StringType)));
        assertEquals(SequenceType.createSequenceType("string"), replacement.getVariableType(x));
        assertEquals(
                List.of("new_payload"),
                replacement.getColumns(x).stream()
                        .map(ColumnBinding::columnName)
                        .toList());
        assertEquals(2, schema.getColumns(x).size());
        assertThrows(
                org.rumbledb.exceptions.OurBadException.class, () -> schema.selectVariables(List.of(name("unknown"))));
    }

    @Test
    void explicitReplacementRetainsExpandedNamesAndIsAtomicOnConflicts() {
        Name variable = new Name("urn:test", "p", "customer.name");
        StructType physical = new StructType()
                .add("p:customer.name", DataTypes.StringType)
                .add("p:customer.name.count", DataTypes.LongType);
        FLWORSchema schema = new FLWORSchema();
        schema.replaceVariable(
                variable,
                SequenceType.createSequenceType("string"),
                FlworDataFrameUtils.getColumnBindings(variable, physical));
        assertEquals(List.of(variable), List.copyOf(schema.getVariables().keySet()));
        assertEquals(2, schema.getColumns(variable).size());
        assertTrue(schema.isConsistent(physical));
        assertEquals(
                SequenceType.createSequenceType("string"),
                schema.selectVariables(List.of(variable)).getVariableType(variable));
        Name other = name("other");
        schema.addColumn(other, new ColumnBinding("other", ColumnFormat.FULLY_NATIVE, DataTypes.IntegerType));
        assertThrows(
                IllegalArgumentException.class,
                () -> schema.replaceVariable(
                        variable,
                        SequenceType.createSequenceType("int"),
                        List.of(new ColumnBinding("other", ColumnFormat.FULLY_NATIVE, DataTypes.IntegerType))));
        assertEquals(2, schema.getColumns(variable).size());
        assertEquals(SequenceType.createSequenceType("string"), schema.getVariableType(variable));
    }

    @Test
    void logicalDependenciesChooseRepresentationsWithoutColumnNames() {
        Name x = name("x");
        FLWORSchema schema = new FLWORSchema();
        schema.replaceVariable(
                x,
                SequenceType.createSequenceType("int*"),
                List.of(
                        new ColumnBinding("arbitrary_payload", ColumnFormat.SERIALIZED_SEQUENCE, DataTypes.BinaryType),
                        new ColumnBinding("arbitrary_total", ColumnFormat.COUNT, DataTypes.LongType)));
        var full = schema.select(java.util.Map.of(x, org.rumbledb.context.DynamicContext.VariableDependency.FULL));
        var count = schema.select(java.util.Map.of(x, org.rumbledb.context.DynamicContext.VariableDependency.COUNT));
        assertEquals("arbitrary_payload", full.getColumns().get(0).getColumnName());
        assertEquals("arbitrary_total", count.getColumns().get(0).getColumnName());
        assertEquals(SequenceType.createSequenceType("int*"), count.getVariableType(x));
        assertEquals(2, schema.selectVariables(List.of(x)).getColumns(x).size());
        assertEquals(
                "arbitrary_payload",
                full.select(java.util.Map.of(x, org.rumbledb.context.DynamicContext.VariableDependency.COUNT))
                        .getColumns()
                        .get(0)
                        .getColumnName());
        assertThrows(
                org.rumbledb.exceptions.OurBadException.class,
                () -> count.select(java.util.Map.of(x, org.rumbledb.context.DynamicContext.VariableDependency.FULL)));
        assertThrows(
                org.rumbledb.exceptions.OurBadException.class,
                () -> schema.select(java.util.Map.of(
                        name("missing"), org.rumbledb.context.DynamicContext.VariableDependency.FULL)));
    }

    @Test
    void mergingSchemasRejectsLogicalAndPhysicalCollisions() {
        FLWORSchema left = new FLWORSchema();
        left.replaceVariable(
                name("x"),
                SequenceType.createSequenceType("int"),
                List.of(new ColumnBinding("left_value", ColumnFormat.FULLY_NATIVE, DataTypes.IntegerType)));
        FLWORSchema right = new FLWORSchema();
        right.replaceVariable(
                name("y"),
                SequenceType.createSequenceType("string"),
                List.of(new ColumnBinding("right_value", ColumnFormat.FULLY_NATIVE, DataTypes.StringType)));
        assertEquals(2, left.merge(right).getVariables().size());
        assertEquals(1, left.getVariables().size());
        assertThrows(org.rumbledb.exceptions.OurBadException.class, () -> left.merge(left));
        FLWORSchema collision = new FLWORSchema();
        collision.addColumn(
                name("z"), new ColumnBinding("left_value", ColumnFormat.FULLY_NATIVE, DataTypes.IntegerType));
        assertThrows(IllegalArgumentException.class, () -> left.merge(collision));
    }
}
