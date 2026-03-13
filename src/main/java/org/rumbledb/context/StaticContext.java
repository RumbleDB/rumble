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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StaticContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    private transient Map<Name, InScopeVariable> inScopeVariables;
    private transient Map<String, String> staticallyKnownNamespaces;
    private transient UserDefinedFunctionExecutionModes userDefinedFunctionExecutionModes;
    private transient InScopeSchemaTypes inScopeSchemaTypes;
    private StaticContext parent;
    private URI staticBaseURI;
    private boolean emptySequenceOrderLeast;

    // TODO: should these be transient?
    private transient SequenceType contextItemStaticType;
    private transient Map<FunctionIdentifier, FunctionSignature> staticallyKnownFunctionSignatures;

    private static final Map<String, String> defaultBindings;

    private int currentMutabilityLevel;

    static {
        defaultBindings = new HashMap<>();
        defaultBindings.put("local", Name.LOCAL_NS);
        defaultBindings.put("fn", Name.FN_NS);
        defaultBindings.put("math", Name.MATH_NS);
        defaultBindings.put("map", Name.MAP_NS);
        defaultBindings.put("array", Name.ARRAY_NS);
        defaultBindings.put("xs", Name.XS_NS);
        defaultBindings.put("jn", Name.JN_NS);
        defaultBindings.put("js", Name.JS_NS);
        // defaultBindings.put("an", Name.AN_NS);
    }

    private RumbleRuntimeConfiguration configuration;

    public StaticContext() {
        this.parent = null;
        this.staticBaseURI = null;
        this.inScopeVariables = null;
        this.userDefinedFunctionExecutionModes = null;
        this.emptySequenceOrderLeast = true;
        this.contextItemStaticType = null;
        this.configuration = null;
        this.inScopeSchemaTypes = null;
        this.currentMutabilityLevel = 0;
    }

    public StaticContext(URI staticBaseURI, RumbleRuntimeConfiguration configuration) {
        this.parent = null;
        this.staticBaseURI = staticBaseURI;
        this.configuration = configuration;
        this.inScopeVariables = new HashMap<>();
        this.userDefinedFunctionExecutionModes = null;
        this.emptySequenceOrderLeast = true;
        this.contextItemStaticType = null;
        this.staticallyKnownFunctionSignatures = new HashMap<>();
        this.inScopeSchemaTypes = new InScopeSchemaTypes();
        this.currentMutabilityLevel = 0;
    }

    public StaticContext(StaticContext parent) {
        this.parent = parent;
        this.inScopeVariables = new HashMap<>();
        this.userDefinedFunctionExecutionModes = null;
        this.contextItemStaticType = null;
        this.staticallyKnownFunctionSignatures = new HashMap<>();
        this.configuration = null;
        this.inScopeSchemaTypes = null;
        this.currentMutabilityLevel = parent.currentMutabilityLevel;
    }

    public StaticContext getParent() {
        return this.parent;
    }

    public RumbleRuntimeConfiguration getRumbleConfiguration() {
        if (this.configuration != null) {
            return this.configuration;
        }
        if (this.parent != null) {
            return this.parent.getRumbleConfiguration();
        }
        throw new OurBadException("Configuration not set.");
    }

    public URI getStaticBaseURI() {
        if (this.staticBaseURI != null) {
            return this.staticBaseURI;
        }
        if (this.parent != null) {
            return this.parent.getStaticBaseURI();
        }
        throw new OurBadException("Static context not set.");
    }

    public void setStaticBaseUri(URI staticBaseURI) {
        this.staticBaseURI = staticBaseURI;
    }

    public boolean isInScope(Name varName) {
        boolean found = false;
        if (this.inScopeVariables.containsKey(varName)) {
            return true;
        } else {
            StaticContext ancestor = this.parent;
            while (ancestor != null) {
                found = found || ancestor.getInScopeVariables().containsKey(varName);
                ancestor = ancestor.parent;
            }
        }
        return found;
    }

    private InScopeVariable getInScopeVariable(Name varName) {
        if (this.inScopeVariables.containsKey(varName)) {
            return this.inScopeVariables.get(varName);
        } else {
            StaticContext ancestor = this.parent;
            while (ancestor != null) {
                if (ancestor.inScopeVariables.containsKey(varName)) {
                    return ancestor.inScopeVariables.get(varName);
                }
                ancestor = ancestor.parent;
            }
            throw new SemanticException("Variable " + varName + " not in scope", ExceptionMetadata.EMPTY_METADATA);
        }
    }

    public FunctionSignature getFunctionSignature(FunctionIdentifier identifier) {
        if (this.staticallyKnownFunctionSignatures.containsKey(identifier)) {
            return this.staticallyKnownFunctionSignatures.get(identifier);
        } else {
            StaticContext ancestor = this.parent;
            while (ancestor != null) {
                if (ancestor.staticallyKnownFunctionSignatures.containsKey(identifier)) {
                    return ancestor.staticallyKnownFunctionSignatures.get(identifier);
                }
                ancestor = ancestor.parent;
            }
            throw new UnknownFunctionCallException(
                    identifier.getName(),
                    identifier.getArity(),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    // replace the sequence type of an existing InScopeVariable, throws an error if the variable does not exists
    public void replaceVariableSequenceType(Name varName, SequenceType newSequenceType) {
        InScopeVariable variable = getInScopeVariable(varName);
        this.inScopeVariables.replace(
            varName,
            new InScopeVariable(
                    varName,
                    newSequenceType,
                    variable.getMetadata(),
                    variable.getStorageMode(),
                    variable.isAssignable()
            )
        );
    }

    public SequenceType getVariableSequenceType(Name varName) {
        return getInScopeVariable(varName).getSequenceType();
    }

    public ExceptionMetadata getVariableMetadata(Name varName) {
        return getInScopeVariable(varName).getMetadata();
    }

    public ExecutionMode getVariableStorageMode(Name varName) {
        return getInScopeVariable(varName).getStorageMode();
    }

    public void setVariableStorageMode(Name varName, ExecutionMode mode) {
        getInScopeVariable(varName).setStorageMode(mode);
    }

    public void addVariable(
            Name varName,
            SequenceType type,
            ExceptionMetadata metadata
    ) {
        this.inScopeVariables.put(
            varName,
            new InScopeVariable(varName, type, metadata, ExecutionMode.UNSET)
        );
    }

    public void addVariable(
            Name varName,
            SequenceType type,
            ExceptionMetadata metadata,
            boolean isAssignable
    ) {
        this.inScopeVariables.put(
            varName,
            new InScopeVariable(varName, type, metadata, ExecutionMode.UNSET, isAssignable)
        );
    }

    public void addFunctionSignature(FunctionIdentifier identifier, FunctionSignature signature) {
        this.staticallyKnownFunctionSignatures.put(identifier, signature);
    }

    public Map<Name, InScopeVariable> getInScopeVariables() {
        return this.inScopeVariables;
    }

    public void show() {
        System.err.println(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Static context with variables:\n");
        for (Entry<Name, InScopeVariable> entry : this.inScopeVariables.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" as " + entry.getValue().getSequenceType());
            stringBuilder.append(" (namespace " + entry.getKey().getNamespace() + ")");
            stringBuilder.append(" | " + entry.getValue().getStorageMode());
            if (entry.getValue().isAssignable()) {
                stringBuilder.append(" | assignable");
            } else {
                stringBuilder.append(" | not assignable");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("Static context with user-defined functions:\n");
        for (Entry<FunctionIdentifier, FunctionSignature> entry : this.staticallyKnownFunctionSignatures.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" as " + entry.getValue());
            stringBuilder.append(" (namespace " + entry.getKey().getName().getNamespace() + ")");
            stringBuilder.append("\n");
        }
        if (this.inScopeSchemaTypes != null) {
            stringBuilder.append("Static context with user-defined types:\n");
            for (ItemType itemType : this.inScopeSchemaTypes.getInScopeSchemaTypes()) {
                stringBuilder.append(itemType.getName());
                stringBuilder.append(itemType.isResolved() ? " (resolved)" : " (unresolved)");
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
        }
        if (this.userDefinedFunctionExecutionModes != null) {
            stringBuilder.append(this.userDefinedFunctionExecutionModes.toString());
        }
        if (this.parent != null) {
            stringBuilder.append("\nParent:");
            stringBuilder.append(this.parent.toString());
        }
        return stringBuilder.toString();
    }

    public boolean hasVariable(Name variableName) {
        if (this.inScopeVariables.containsKey(variableName)) {
            return true;
        }
        if (this.parent != null) {
            return this.parent.hasVariable(variableName);
        }
        return false;
    }

    public boolean hasVariableInScopeOnly(Name variableName) {
        return this.inScopeVariables.containsKey(variableName);
    }

    public boolean bindNamespace(String prefix, String namespace) {
        if (this.staticallyKnownNamespaces == null) {
            this.staticallyKnownNamespaces = new HashMap<>();
        }
        if (!this.staticallyKnownNamespaces.containsKey(prefix)) {
            this.staticallyKnownNamespaces.put(prefix, namespace);
            return true;
        }
        if (defaultBindings.containsKey(prefix)) {
            if (this.staticallyKnownNamespaces.get(prefix).equals(defaultBindings.get(prefix))) {
                this.staticallyKnownNamespaces.put(prefix, namespace);
                return true;
            }
        }
        return false;
    }

    public String resolveNamespace(String prefix) {
        if (this.staticallyKnownNamespaces != null) {
            if (this.staticallyKnownNamespaces.containsKey(prefix)) {
                return this.staticallyKnownNamespaces.get(prefix);
            } else {
                return null;
            }
        }
        if (this.parent != null) {
            return this.parent.resolveNamespace(prefix);
        }
        return null;
    }

    public Map<String, String> getInScopeNamespaceBindings() {
        Map<String, String> bindings = new HashMap<>();
        if (this.parent != null) {
            bindings.putAll(this.parent.getInScopeNamespaceBindings());
        }
        if (this.staticallyKnownNamespaces != null) {
            bindings.putAll(this.staticallyKnownNamespaces);
        }
        return bindings;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, this.parent, StaticContext.class);
        kryo.writeObject(output, this.staticBaseURI);
        output.writeBoolean(this.emptySequenceOrderLeast);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObjectOrNull(input, StaticContext.class);
        this.staticBaseURI = kryo.readObject(input, URI.class);
        this.emptySequenceOrderLeast = input.readBoolean();
    }

    public void importModuleContext(StaticContext moduleContext) {
        for (Name name : moduleContext.inScopeVariables.keySet()) {
            InScopeVariable variable = moduleContext.inScopeVariables.get(name);
            this.inScopeVariables.put(name, variable);
        }
        for (FunctionIdentifier fi : moduleContext.staticallyKnownFunctionSignatures.keySet()) {
            FunctionSignature signature = moduleContext.staticallyKnownFunctionSignatures.get(fi);
            this.staticallyKnownFunctionSignatures.put(fi, signature);
        }
    }

    public void setUserDefinedFunctionsExecutionModes(
            UserDefinedFunctionExecutionModes staticallyKnownFunctionSignatures
    ) {
        if (this.parent != null) {
            throw new OurBadException("Statically known function signatures can only be stored in the module context.");
        }
        this.userDefinedFunctionExecutionModes = staticallyKnownFunctionSignatures;
    }

    public UserDefinedFunctionExecutionModes getUserDefinedFunctionsExecutionModes() {
        if (this.userDefinedFunctionExecutionModes != null) {
            return this.userDefinedFunctionExecutionModes;
        }
        if (this.parent != null) {
            return this.parent.getUserDefinedFunctionsExecutionModes();
        }
        throw new OurBadException("Statically known function signatures are not set up properly in static context.");
    }

    public void setEmptySequenceOrderLeast(boolean emptySequenceOrderLeast) {
        if (this.parent != null) {
            throw new OurBadException("Empty sequence ordering can only be set in the root static context.");
        }
        this.emptySequenceOrderLeast = emptySequenceOrderLeast;
    }

    public boolean isEmptySequenceOrderLeast() {
        if (this.parent != null) {
            return this.parent.isEmptySequenceOrderLeast();
        }
        return this.emptySequenceOrderLeast;
    }

    public StaticContext getModuleContext() {
        if (this.parent != null) {
            return this.parent.getModuleContext();
        }
        return this;
    }

    public SequenceType getContextItemStaticType() {
        return this.contextItemStaticType;
    }

    public void setContextItemStaticType(SequenceType contextItemStaticType) {
        this.contextItemStaticType = contextItemStaticType;
    }

    // replace all inScopeVariable in this context and all parents until [stopContext] with name not in [varToExclude]
    // with same variable with sequence type arity changed from 1 to + and form ? to *
    // used by groupBy clause
    public void incrementArities(StaticContext stopContext, Set<Name> varToExclude) {
        this.inScopeVariables.replaceAll(
            (key, value) -> varToExclude.contains(key)
                ? value
                : new InScopeVariable(
                        value.getName(),
                        value.getSequenceType().incrementArity(),
                        value.getMetadata(),
                        value.getStorageMode()
                )
        );
        StaticContext current = this.parent;
        while (current != null && current != stopContext) {
            for (Map.Entry<Name, InScopeVariable> entry : current.inScopeVariables.entrySet()) {
                if (!this.inScopeVariables.containsKey(entry.getKey())) {
                    this.addVariable(
                        entry.getKey(),
                        varToExclude.contains(entry.getKey())
                            ? entry.getValue().getSequenceType()
                            : entry.getValue().getSequenceType().incrementArity(),
                        entry.getValue().getMetadata(),
                        entry.getValue().isAssignable()
                    );
                }
            }
            current = current.parent;
        }
    }

    public void bindDefaultNamespaces() {
        for (String prefix : defaultBindings.keySet()) {
            bindNamespace(prefix, defaultBindings.get(prefix));
        }
    }

    public InScopeSchemaTypes getInScopeSchemaTypes() {
        if (this.inScopeSchemaTypes != null) {
            return this.inScopeSchemaTypes;
        }
        if (this.parent != null) {
            return this.parent.getInScopeSchemaTypes();
        }
        throw new OurBadException("In-scope schema types are not set up properly in static context.");
    }

    public int getCurrentMutabilityLevel() {
        return this.currentMutabilityLevel;
    }

    public void setCurrentMutabilityLevel(int currentMutabilityLevel) {
        this.currentMutabilityLevel = currentMutabilityLevel;
    }

    public boolean getIsAssignable(Name name) {
        return this.getInScopeVariable(name).isAssignable();
    }
}
