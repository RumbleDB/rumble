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

import java.io.Serializable;
import java.util.*;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.*;

import org.rumbledb.context.Name;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn.ColumnFormat;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

/** Logical variables and the physical columns representing them. */
public class FLWORSchema implements Serializable {
    private static final long serialVersionUID = 1L;

    public record ColumnBinding(String columnName, ColumnFormat representation, DataType physicalType)
            implements Serializable {
        public ColumnBinding {
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(representation);
            Objects.requireNonNull(physicalType);
        }

        private boolean hasValidRepresentation() {
            return switch (representation) {
                case SERIALIZED_SEQUENCE -> physicalType.equals(DataTypes.BinaryType);
                case NATIVE_SEQUENCE -> physicalType instanceof ArrayType;
                case COUNT -> physicalType.equals(DataTypes.LongType);
                default -> true;
            };
        }
    }

    public record VariableSchema(SequenceType logicalType, List<ColumnBinding> columns) implements Serializable {
        public VariableSchema {
            Objects.requireNonNull(logicalType);
            columns = List.copyOf(columns);
            if (columns.isEmpty()) {
                throw new IllegalArgumentException("A variable must have at least one physical column.");
            }
        }
    }

    private final Map<Name, VariableSchema> variables;

    public FLWORSchema() {
        this.variables = new LinkedHashMap<>();
    }

    public FLWORSchema(FLWORSchema source) {
        this.variables = new LinkedHashMap<>(source.variables);
    }

    /** Compatibility boundary for DataFrames using the existing column naming conventions. */
    public static FLWORSchema inferFromSparkDataFrameSchema(StructType schema) {
        FLWORSchema result = new FLWORSchema();
        for (StructField field : schema.fields()) {
            FlworDataFrameColumn column = new FlworDataFrameColumn(field.name(), schema);
            result.addColumn(
                    column.getVariableName(),
                    new ColumnBinding(
                            field.name(),
                            FlworDataFrameColumn.getColumnFormat(field.name(), schema),
                            field.dataType()));
        }
        return result;
    }

    public Map<Name, VariableSchema> getVariables() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(this.variables));
    }

    public List<ColumnBinding> getColumns(Name variable) {
        VariableSchema schema = this.variables.get(variable);
        return schema == null ? List.of() : schema.columns();
    }

    /** Adds a physical binding without encoding the variable identity in the column name. */
    public void addColumn(Name variable, ColumnBinding column) {
        Objects.requireNonNull(variable);
        Objects.requireNonNull(column);
        for (VariableSchema schema : this.variables.values()) {
            if (schema.columns().stream().anyMatch(c -> c.columnName().equals(column.columnName()))) {
                throw new IllegalArgumentException("Column already bound: " + column.columnName());
            }
        }
        List<ColumnBinding> columns = new ArrayList<>(getColumns(variable));
        columns.add(column);
        this.variables.put(variable, new VariableSchema(getVariableType(variable), columns));
    }

    public void setVariableType(Name name, SequenceType type) {
        if (!this.variables.containsKey(name)) {
            throw new IllegalArgumentException("Unknown variable: " + name);
        }
        this.variables.put(
                name,
                new VariableSchema(type == null ? SequenceType.createSequenceType("item*") : type, getColumns(name)));
    }

    public SequenceType getVariableType(Name name) {
        VariableSchema schema = this.variables.get(name);
        return schema == null ? SequenceType.createSequenceType("item*") : schema.logicalType();
    }

    /** Keeps explicit bindings for surviving columns; new/rebound columns use the naming convention. */
    public FLWORSchema project(StructType physicalSchema, Collection<Name> reboundVariables) {
        FLWORSchema result = new FLWORSchema();
        Map<String, Name> owners = new HashMap<>();
        Map<String, ColumnBinding> bindings = new HashMap<>();
        this.variables.forEach((name, schema) -> schema.columns().forEach(column -> {
            owners.put(column.columnName(), name);
            bindings.put(column.columnName(), column);
        }));
        for (StructField field : physicalSchema.fields()) {
            Name owner = owners.get(field.name());
            if (owner != null && !reboundVariables.contains(owner)) {
                ColumnBinding previous = bindings.get(field.name());
                result.addColumn(owner, new ColumnBinding(field.name(), previous.representation(), field.dataType()));
            } else {
                FlworDataFrameColumn column = new FlworDataFrameColumn(field.name(), physicalSchema);
                result.addColumn(
                        column.getVariableName(),
                        new ColumnBinding(
                                field.name(),
                                FlworDataFrameColumn.getColumnFormat(field.name(), physicalSchema),
                                field.dataType()));
            }
        }
        for (Name name : result.variables.keySet()) {
            if (this.variables.containsKey(name) && !reboundVariables.contains(name)) {
                result.setVariableType(name, getVariableType(name));
            }
        }
        return result;
    }

    public void inheritTypes(FLWORSchema source, Collection<Name> names) {
        for (Name name : names) {
            if (this.variables.containsKey(name) && source.variables.containsKey(name)) {
                setVariableType(name, source.getVariableType(name));
            }
        }
    }

    public boolean isConsistent(Dataset<Row> frame) {
        return isConsistent(frame.schema());
    }

    public boolean isConsistent(StructType physicalSchema) {
        Map<String, DataType> actual = new HashMap<>();
        for (StructField field : physicalSchema.fields()) {
            if (actual.put(field.name(), field.dataType()) != null) {
                return false;
            }
        }
        Set<String> bound = new HashSet<>();
        for (VariableSchema variable : this.variables.values()) {
            for (ColumnBinding column : variable.columns()) {
                if (!bound.add(column.columnName())
                        || !column.physicalType().equals(actual.get(column.columnName()))
                        || !column.hasValidRepresentation()) {
                    return false;
                }
            }
        }
        return bound.equals(actual.keySet());
    }

    public boolean isCount(Name name) {
        return getColumns(name).stream().anyMatch(c -> c.representation() == ColumnFormat.COUNT);
    }

    public boolean isArraysOfBytes(Name name) {
        return getColumns(name).stream()
                .anyMatch(c -> c.representation() == ColumnFormat.NATIVE_SEQUENCE
                        && c.physicalType() instanceof ArrayType array
                        && array.elementType().equals(DataTypes.BinaryType));
    }

    public boolean isBytes(Name name) {
        return getColumns(name).stream().anyMatch(c -> c.representation() == ColumnFormat.SERIALIZED_SEQUENCE);
    }

    public ItemType getNativeType(Name name) {
        return getColumns(name).stream()
                .filter(c -> c.representation() == ColumnFormat.FULLY_NATIVE)
                .map(c -> TypeMappings.getItemTypeFromDataFrameDataType(c.physicalType()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return this.variables.toString();
    }
}
