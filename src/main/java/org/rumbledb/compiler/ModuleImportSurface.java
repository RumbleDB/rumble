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
 * Authors: OpenAI
 *
 */

package org.rumbledb.compiler;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The spec-visible surface of a library module import: only declarations made directly in the imported module.
 */
final class ModuleImportSurface {

    static final class ImportedVariableBinding {
        private final Name name;
        private final SequenceType sequenceType;
        private final ExceptionMetadata metadata;
        private final ExecutionMode storageMode;
        private final boolean assignable;

        ImportedVariableBinding(
                Name name,
                SequenceType sequenceType,
                ExceptionMetadata metadata,
                ExecutionMode storageMode,
                boolean assignable
        ) {
            this.name = name;
            this.sequenceType = sequenceType;
            this.metadata = metadata;
            this.storageMode = storageMode;
            this.assignable = assignable;
        }

        public Name getName() {
            return this.name;
        }

        public SequenceType getSequenceType() {
            return this.sequenceType;
        }

        public ExceptionMetadata getMetadata() {
            return this.metadata;
        }

        public ExecutionMode getStorageMode() {
            return this.storageMode;
        }

        public boolean isAssignable() {
            return this.assignable;
        }
    }

    private final Map<Name, ImportedVariableBinding> variableBindings;
    private final Map<FunctionIdentifier, FunctionSignature> functionSignatures;

    ModuleImportSurface() {
        this.variableBindings = new LinkedHashMap<>();
        this.functionSignatures = new LinkedHashMap<>();
    }

    public void addVariableBinding(ImportedVariableBinding binding) {
        this.variableBindings.put(binding.getName(), binding);
    }

    public void addFunctionSignature(FunctionIdentifier identifier, FunctionSignature signature) {
        this.functionSignatures.put(identifier, signature);
    }

    public Map<Name, ImportedVariableBinding> getVariableBindings() {
        return this.variableBindings;
    }

    public Map<FunctionIdentifier, FunctionSignature> getFunctionSignatures() {
        return this.functionSignatures;
    }
}
