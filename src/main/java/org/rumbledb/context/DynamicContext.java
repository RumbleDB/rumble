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

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private DynamicContext parent;
    private RumbleRuntimeConfiguration conf;
    private VariableValues variableValues;
    private NamedFunctions namedFunctions;
    private InScopeSchemaTypes inScopeSchemaTypes;
    private DateTime currentDateTime;
    private int currentMutabilityLevel;
    private final GlobalVariables globalVariables;

    /**
     * The default constructor is for Kryo deserialization purposes.
     */
    public DynamicContext() {
        this.parent = null;
        this.variableValues = null;
        this.conf = null;
        this.namedFunctions = null;
        this.inScopeSchemaTypes = null;
        this.currentDateTime = new DateTime();
        this.currentMutabilityLevel = 0;
        this.globalVariables = new GlobalVariables();
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
        this.namedFunctions = new NamedFunctions(conf);
        this.inScopeSchemaTypes = new InScopeSchemaTypes();
        this.currentDateTime = new DateTime();
        this.currentMutabilityLevel = 0;
        this.globalVariables = new GlobalVariables();
    }

    public DynamicContext(DynamicContext parent) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.variableValues = new VariableValues(this.parent.variableValues);
        this.conf = null;
        this.namedFunctions = null;
        this.inScopeSchemaTypes = null;
        this.currentMutabilityLevel = parent.getCurrentMutabilityLevel();
        this.globalVariables = parent.globalVariables;
    }

    public DynamicContext(
            DynamicContext parent,
            Map<Name, List<Item>> localVariableValues,
            Map<Name, JavaRDD<Item>> rddVariableValues,
            Map<Name, JSoundDataFrame> dataFrameVariableValues
    ) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.variableValues = new VariableValues(
                this.parent.variableValues,
                localVariableValues,
                rddVariableValues,
                dataFrameVariableValues,
                parent.globalVariables
        );
        this.namedFunctions = null;
        this.currentMutabilityLevel = parent.getCurrentMutabilityLevel();
        this.globalVariables = parent.globalVariables;
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

    public int getCurrentMutabilityLevel() {
        return this.currentMutabilityLevel;
    }

    public void setCurrentMutabilityLevel(int currentMutabilityLevel) {
        this.currentMutabilityLevel = currentMutabilityLevel;
    }

    public enum VariableDependency {
        FULL,
        COUNT,
        SUM,
        AVERAGE,
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

    public static Map<Name, DynamicContext.VariableDependency> copyVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> from
    ) {
        Map<Name, DynamicContext.VariableDependency> result = new HashMap<>();
        for (Name v : from.keySet()) {
            result.put(v, from.get(v));
        }
        return result;
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

    public DateTime getCurrentDateTime() {
        if (this.parent != null) {
            return this.parent.currentDateTime;
        }
        return this.currentDateTime;
    }

    public static void printDependencies(Map<Name, VariableDependency> exprDependency) {
        LogManager.getLogger("DynamicContext").debug("System.err Variable dependencies:");
        for (Map.Entry<Name, VariableDependency> e : exprDependency.entrySet()) {
            LogManager.getLogger("DynamicContext").debug(e.getKey() + " : " + e.getValue());
        }
    }


    public void addGlobalVariable(Name globalVariable) {
        this.globalVariables.addGlobalVariable(globalVariable);
    }
}

