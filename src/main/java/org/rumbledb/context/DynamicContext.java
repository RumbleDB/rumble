/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DynamicContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private DynamicContext parent;
    private RumbleRuntimeConfiguration conf;
    private VariableValues variableValues;
    private NamedFunctions namedFunctions;
    private InScopeSchemaTypes inScopeSchemaTypes;

    /**
     * The default constructor is for Kryo deserialization purposes.
     */
    public DynamicContext() {
        this.parent = null;
        this.variableValues = null;
        this.conf = null;
        this.namedFunctions = null;
    }

    /**
     * Creates a new, empty module context (without parent).
     * 
     * @param conf the Rumble configuration.
     */
    public DynamicContext(RumbleRuntimeConfiguration conf) {
        this.parent = null;
        this.variableValues = new VariableValues();
        this.conf = conf;
        this.namedFunctions = new NamedFunctions();
        this.inScopeSchemaTypes = new InScopeSchemaTypes();
    }

    public DynamicContext(DynamicContext parent) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.variableValues = new VariableValues(this.parent.variableValues);
        this.namedFunctions = null;
    }

    public DynamicContext(
            DynamicContext parent,
            Map<Name, List<Item>> localVariableValues,
            Map<Name, JavaRDD<Item>> rddVariableValues,
            Map<Name, Dataset<Row>> dataFrameVariableValues
    ) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.variableValues = new VariableValues(
                this.parent.variableValues,
                localVariableValues,
                rddVariableValues,
                dataFrameVariableValues
        );
        this.namedFunctions = null;

    }

    public RumbleRuntimeConfiguration getRumbleRuntimeConfiguration() {
        if (this.conf != null) {
            return this.conf;
        }
        if (this.parent != null) {
            return this.parent.getRumbleRuntimeConfiguration();
        }
        return null;
    }

    public VariableValues getVariableValues() {
        return this.variableValues;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, this.parent, DynamicContext.class);
        kryo.writeObject(output, this.variableValues);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObjectOrNull(input, DynamicContext.class);
        this.variableValues = kryo.readObject(input, VariableValues.class);
    }

    public enum VariableDependency {
        FULL,
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN
    }

    private static VariableDependency mergeSingleVariableDependency(VariableDependency left, VariableDependency right) {
        if (left.equals(right)) {
            return left;
        }
        return VariableDependency.FULL;
    }

    public static void mergeVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> into,
            Map<Name, DynamicContext.VariableDependency> from
    ) {
        for (Name v : from.keySet()) {
            if (into.containsKey(v)) {
                into.put(v, DynamicContext.mergeSingleVariableDependency(into.get(v), from.get(v)));
            } else {
                into.put(v, from.get(v));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Local:\n");
        sb.append(this.variableValues.toString());
        if (this.namedFunctions != null) {
            sb.append("  Known functions:\n");
            sb.append(this.namedFunctions + "\n");
        }
        if (this.parent != null) {
            sb.append("Parent context:\n");
            sb.append(this.parent.toString());
        }
        return sb.toString();
    }

    public void setNamedFunctions(
            NamedFunctions knownFunctions
    ) {
        if (this.parent != null) {
            throw new OurBadException("Known function scan only be stored in the module context.");
        }
        this.namedFunctions = knownFunctions;
    }

    public NamedFunctions getNamedFunctions() {
        if (this.namedFunctions != null) {
            return this.namedFunctions;
        }
        if (this.parent != null) {
            return this.parent.getNamedFunctions();
        }
        throw new OurBadException("Known functions are not set up properly in dynamic context.");
    }

    public DynamicContext getModuleContext() {
        if (this.parent != null) {
            return this.parent.getModuleContext();
        }
        return this;
    }

    public InScopeSchemaTypes getInScopeSchemaTypes() {
        if (this.inScopeSchemaTypes != null) {
            return this.inScopeSchemaTypes;
        }
        if (this.parent != null) {
            return this.parent.getInScopeSchemaTypes();
        }
        throw new OurBadException("In-scope schema types are not set up properly in dynamic context.");
    }

}

