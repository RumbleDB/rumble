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

import org.rumbledb.context.DynamicContext.VariableDependency;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
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
            return switch (this.representation) {
                case SERIALIZED_SEQUENCE -> this.physicalType.equals(DataTypes.BinaryType);
                case NATIVE_SEQUENCE -> this.physicalType instanceof ArrayType;
                case COUNT -> this.physicalType.equals(DataTypes.LongType);
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

    /** Selects logical variables, retaining all their existing representations. */
    public FLWORSchema selectVariables(Collection<Name> names) {
        FLWORSchema result = new FLWORSchema();
        for (Name name : names) {
            VariableSchema variable = this.variables.get(name);
            if (variable == null) throw new OurBadException("Unknown FLWOR variable: " + name);
            result.replaceVariable(name, variable.logicalType(), variable.columns());
        }
        return result;
    }

    /**
     * Selects one suitable representation per required variable, in dependency order.
     * If ignoreExtraneousVariables is true, requirements for absent variables are skipped.
     * Existing variables must always provide a suitable representation.
     */
    public FLWORSchema select(Map<Name, VariableDependency> dependencies, boolean ignoreExtraneousVariables) {
        FLWORSchema result = new FLWORSchema();
        for (Map.Entry<Name, VariableDependency> entry : dependencies.entrySet()) {
            Name name = entry.getKey();
            if (ignoreExtraneousVariables && !this.variables.containsKey(name)) continue;
            List<ColumnFormat> formats = new ArrayList<>();
            if (entry.getValue() == VariableDependency.FULL) {
                formats.add(ColumnFormat.FULLY_NATIVE);
                formats.add(ColumnFormat.SERIALIZED_SEQUENCE);
                formats.add(ColumnFormat.NATIVE_SEQUENCE);
            } else {
                formats.add(ColumnFormat.valueOf(entry.getValue().name()));
                formats.add(ColumnFormat.NATIVE_SEQUENCE);
                formats.add(ColumnFormat.FULLY_NATIVE);
                formats.add(ColumnFormat.SERIALIZED_SEQUENCE);
            }
            ColumnBinding selected = null;
            for (ColumnFormat format : formats) {
                selected = getColumns(name).stream()
                        .filter(c -> c.representation() == format)
                        .findFirst()
                        .orElse(null);
                if (selected != null) break;
            }
            if (selected == null) {
                throw new OurBadException("No column for " + entry.getValue() + " dependency on " + name);
            }
            result.replaceVariable(name, getVariableType(name), List.of(selected));
        }
        return result;
    }

    /** Combines disjoint logical bindings; collisions must be renamed or replaced explicitly. */
    public FLWORSchema merge(FLWORSchema other) {
        FLWORSchema result = new FLWORSchema(this);
        for (Map.Entry<Name, VariableSchema> entry : other.variables.entrySet()) {
            if (result.variables.containsKey(entry.getKey())) {
                throw new OurBadException("Conflicting FLWOR variable: " + entry.getKey());
            }
            result.replaceVariable(
                    entry.getKey(),
                    entry.getValue().logicalType(),
                    entry.getValue().columns());
        }
        return result;
    }

    /** Columns for SQL generation come from the planned schema. */
    public List<FlworDataFrameColumn> getColumns() {
        List<FlworDataFrameColumn> columns = new ArrayList<>();
        this.variables.forEach((name, variable) ->
                variable.columns().forEach(binding -> columns.add(new FlworDataFrameColumn(name, binding))));
        return columns;
    }

    /** Removes a logical variable and all its physical bindings. Missing variables are ignored. */
    public void removeVariable(Name name) {
        this.variables.remove(Objects.requireNonNull(name));
    }

    /** Explicitly replaces a variable's logical type and all its physical bindings. */
    public void replaceVariable(Name name, SequenceType type, List<ColumnBinding> columns) {
        FLWORSchema replacement = new FLWORSchema(this);
        replacement.variables.remove(name);
        for (ColumnBinding column : columns) {
            replacement.addColumn(name, column);
        }
        if (!columns.isEmpty()) {
            replacement.setVariableType(name, type);
        }
        this.variables.clear();
        this.variables.putAll(replacement.variables);
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
