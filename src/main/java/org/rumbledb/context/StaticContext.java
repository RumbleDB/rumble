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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.runtime.functions.base.StaticallyKnownFunctionSignatures;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.jsoniq.ExecutionMode;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class StaticContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    private static class InScopeVariable implements Serializable, KryoSerializable {
        private static final long serialVersionUID = 1L;

        private Name name;
        private SequenceType sequenceType;
        private ExceptionMetadata metadata;
        private ExecutionMode storageMode;

        public InScopeVariable(
                Name name,
                SequenceType sequenceType,
                ExceptionMetadata metadata,
                ExecutionMode storageMode
        ) {
            this.name = name;
            this.sequenceType = sequenceType;
            this.metadata = metadata;
            this.storageMode = storageMode;
        }

        @SuppressWarnings("unused")
        public Name getName() {
            return this.name;
        }

        public SequenceType getSequenceType() {
            return this.sequenceType;
        }

        public ExceptionMetadata getMetadata() {
            return this.metadata;
        }

        @Override
        public void write(Kryo kryo, Output output) {
            kryo.writeObject(output, this.name);
            kryo.writeObject(output, this.sequenceType);
            kryo.writeObject(output, this.metadata);
            kryo.writeObject(output, this.storageMode);
        }

        @Override
        public void read(Kryo kryo, Input input) {
            this.name = kryo.readObject(input, Name.class);
            this.sequenceType = kryo.readObject(input, SequenceType.class);
            this.metadata = kryo.readObject(input, ExceptionMetadata.class);
            this.storageMode = kryo.readObject(input, ExecutionMode.class);
        }
    }

    private Map<Name, InScopeVariable> inScopeVariables;
    private Map<String, String> namespaceBindings;
    private StaticContext parent;
    private URI staticBaseURI;
    public StaticallyKnownFunctionSignatures staticallyKnownFunctionSignatures;

    public StaticContext(URI staticBaseURI) {
        this.parent = null;
        this.staticBaseURI = staticBaseURI;
        this.inScopeVariables = new HashMap<>();
        this.staticallyKnownFunctionSignatures = new StaticallyKnownFunctionSignatures();
    }

    public StaticContext(StaticContext parent) {
        this.parent = parent;
        this.inScopeVariables = new HashMap<>();
        this.staticallyKnownFunctionSignatures = null;
    }

    public StaticContext getParent() {
        return this.parent;
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
            throw new SemanticException("Variable " + varName + " not in scope", null);
        }
    }

    public SequenceType getVariableSequenceType(Name varName) {
        return getInScopeVariable(varName).getSequenceType();
    }

    public ExceptionMetadata getVariableMetadata(Name varName) {
        return getInScopeVariable(varName).getMetadata();
    }

    public ExecutionMode getVariableStorageMode(Name varName) {
        return getInScopeVariable(varName).storageMode;
    }

    public void addVariable(
            Name varName,
            SequenceType type,
            ExceptionMetadata metadata,
            ExecutionMode storageMode
    ) {
        this.inScopeVariables.put(varName, new InScopeVariable(varName, type, metadata, storageMode));
    }

    protected Map<Name, InScopeVariable> getInScopeVariables() {
        return this.inScopeVariables;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Static context with variables: ");
        this.inScopeVariables.keySet().forEach(a -> stringBuilder.append(a));
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

    public boolean bindNamespace(String prefix, String namespace) {
        if (this.namespaceBindings == null) {
            this.namespaceBindings = new HashMap<>();
        }
        if (!this.namespaceBindings.containsKey(prefix)) {
            this.namespaceBindings.put(prefix, namespace);
            return true;
        }
        return false;
    }

    public String resolveNamespace(String prefix) {
        if (this.namespaceBindings != null) {
            if (this.namespaceBindings.containsKey(prefix)) {
                return this.namespaceBindings.get(prefix);
            } else {
                return null;
            }
        }
        if (this.parent != null) {
            return this.parent.resolveNamespace(prefix);
        }
        return null;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.inScopeVariables);
        kryo.writeObject(output, this.namespaceBindings);
        kryo.writeObject(output, this.parent);
        kryo.writeObject(output, this.staticBaseURI);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.inScopeVariables = kryo.readObject(input, HashMap.class);
        this.namespaceBindings = kryo.readObject(input, HashMap.class);
        this.parent = kryo.readObject(input, StaticContext.class);
        this.staticBaseURI = kryo.readObject(input, URI.class);
    }

    public void importModuleContext(StaticContext moduleContext, String targetNamespace) {
        for (Name name : moduleContext.inScopeVariables.keySet()) {
            if (name.getNamespace().contentEquals(targetNamespace)) {
                InScopeVariable variable = moduleContext.inScopeVariables.get(name);
                this.inScopeVariables.put(name, variable);
            }
        }
    }

    public StaticallyKnownFunctionSignatures getStaticallyKnownFunctionSignatures() {
        if (this.staticallyKnownFunctionSignatures != null) {
            return this.staticallyKnownFunctionSignatures;
        }
        if (this.parent != null) {
            return this.parent.getStaticallyKnownFunctionSignatures();
        }
        throw new OurBadException("Statically known function signatures are not set up properly in static context.");
    }
}
