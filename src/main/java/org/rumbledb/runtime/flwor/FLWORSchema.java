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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import org.rumbledb.context.Name;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

public class FLWORSchema implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Name, SequenceType> variableTypes;

    private Set<String> counts;
    private Set<String> sequencesAsArraysOfBytes;
    private Set<String> sequencesAsBytes;
    private Map<String, ItemType> nativelyTypedSequences;

    public FLWORSchema() {
        this.variableTypes = new HashMap<>();
        this.counts = new HashSet<>();
        this.sequencesAsArraysOfBytes = new HashSet<>();
        this.sequencesAsBytes = new HashSet<>();
        this.nativelyTypedSequences = new HashMap<>();
    }

    public FLWORSchema(FLWORSchema fdf) {
        this.variableTypes = new HashMap<>(fdf.variableTypes);
        this.counts = new HashSet<>(fdf.counts);
        this.sequencesAsArraysOfBytes = new HashSet<>(fdf.sequencesAsArraysOfBytes);
        this.sequencesAsBytes = new HashSet<>(fdf.sequencesAsBytes);
        this.nativelyTypedSequences = new HashMap<>(fdf.nativelyTypedSequences);
    }

    public void setCount(Name name) {
        this.counts.add(name.toString());
    }

    public void setArraysOfBytes(Name name) {
        this.sequencesAsArraysOfBytes.add(name.toString());
    }

    public void setBytes(Name name) {
        this.sequencesAsBytes.add(name.toString());
    }

    public void setNativeType(Name name, ItemType itemType) {
        this.nativelyTypedSequences.put(name.toString(), itemType);
    }

    /** Infers physical sequence representations using master's column naming conventions. */
    public FLWORSchema(StructType schema) {
        this();
        for (StructField field : schema.fields()) {
            String name = field.name();
            if (name.endsWith(".count")) {
                this.counts.add(name);
            } else if (field.dataType().equals(DataTypes.BinaryType)) {
                this.sequencesAsBytes.add(name);
            } else if (field.dataType() instanceof ArrayType
                    && ((ArrayType) field.dataType()).elementType().equals(DataTypes.BinaryType)) {
                this.sequencesAsArraysOfBytes.add(name);
            } else {
                this.nativelyTypedSequences.put(name, TypeMappings.getItemTypeFromDataFrameDataType(field.dataType()));
            }
        }
    }

    /** Logical types are independent of whether values are native or serialized. */
    public void setVariableType(Name name, SequenceType type) {
        this.variableTypes.put(name, type == null ? SequenceType.createSequenceType("item*") : type);
    }

    public SequenceType getVariableType(Name name) {
        return this.variableTypes.getOrDefault(name, SequenceType.createSequenceType("item*"));
    }

    /** Retains types only for surviving variables, excluding newly rebound variables. */
    public FLWORSchema project(StructType physicalSchema, Collection<Name> reboundVariables) {
        FLWORSchema result = new FLWORSchema(physicalSchema);
        Set<Name> survivingVariables = new HashSet<>();
        for (String column : physicalSchema.fieldNames()) {
            survivingVariables.add(new FlworDataFrameColumn(column, physicalSchema).getVariableName());
        }
        for (Map.Entry<Name, SequenceType> entry : this.variableTypes.entrySet()) {
            if (survivingVariables.contains(entry.getKey()) && !reboundVariables.contains(entry.getKey())) {
                result.setVariableType(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public void inheritTypes(FLWORSchema source, Collection<Name> variables) {
        for (Name variable : variables) {
            if (source.variableTypes.containsKey(variable)) {
                this.variableTypes.put(variable, source.variableTypes.get(variable));
            }
        }
    }

    public boolean isConsistent(Dataset<Row> dataFrame) {
        return isConsistent(dataFrame.schema());
    }

    public boolean isConsistent(StructType schema) {
        FLWORSchema inferred = new FLWORSchema(schema);
        Set<String> actual = new HashSet<>();
        for (StructField field : schema.fields()) {
            String name = field.name();
            if (!actual.add(name)) {
                return false;
            }
            int representations = (this.counts.contains(name) ? 1 : 0)
                    + (this.sequencesAsBytes.contains(name) ? 1 : 0)
                    + (this.sequencesAsArraysOfBytes.contains(name) ? 1 : 0)
                    + (this.nativelyTypedSequences.containsKey(name) ? 1 : 0);
            if (representations != 1) {
                return false;
            }
        }
        Set<String> expected = new HashSet<>(this.counts);
        expected.addAll(this.sequencesAsBytes);
        expected.addAll(this.sequencesAsArraysOfBytes);
        expected.addAll(this.nativelyTypedSequences.keySet());
        return actual.equals(expected)
                && this.counts.equals(inferred.counts)
                && this.sequencesAsBytes.equals(inferred.sequencesAsBytes)
                && this.sequencesAsArraysOfBytes.equals(inferred.sequencesAsArraysOfBytes)
                && this.nativelyTypedSequences.keySet().equals(inferred.nativelyTypedSequences.keySet());
    }

    public boolean isCount(Name name) {
        return this.counts.contains(name.toString()) || this.counts.contains(name + ".count");
    }

    public boolean isArraysOfBytes(Name name) {
        return this.sequencesAsArraysOfBytes.contains(name.toString())
                || this.sequencesAsArraysOfBytes.contains(name + ".sequence");
    }

    public boolean isBytes(Name name) {
        return this.sequencesAsBytes.contains(name.toString());
    }

    public ItemType getNativeType(Name name) {
        return this.nativelyTypedSequences.get(name.toString());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Counts:\n");
        for (String s : this.counts) {
            sb.append(s + "\n");
        }
        sb.append("Array of bytes:\n");
        for (String s : this.sequencesAsArraysOfBytes) {
            sb.append(s + "\n");
        }
        sb.append("Bytes:\n");
        for (String s : this.sequencesAsBytes) {
            sb.append(s + "\n");
        }
        sb.append("Native types:\n");
        for (String s : this.nativelyTypedSequences.keySet()) {
            sb.append(s + " : " + this.nativelyTypedSequences.get(s));
        }
        return sb.toString();
    }
}
