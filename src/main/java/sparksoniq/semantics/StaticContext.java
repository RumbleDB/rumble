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

package sparksoniq.semantics;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SemanticException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.SequenceType;

import java.util.HashMap;
import java.util.Map;

public class StaticContext {

    private static class InScopeVariable {
        private String name;
        private SequenceType sequenceType;
        private ExceptionMetadata metadata;
        private ExecutionMode storageMode;

        public InScopeVariable(
                String name,
                SequenceType sequenceType,
                ExceptionMetadata metadata,
                ExecutionMode storageMode
        ) {
            this.name = name;
            this.sequenceType = sequenceType;
            this.metadata = metadata;
            this.storageMode = storageMode;
        }

        public String getName() {
            return this.name;
        }

        public SequenceType getSequenceType() {
            return this.sequenceType;
        }

        public ExceptionMetadata getMetadata() {
            return this.metadata;
        }
    }

    private Map<String, InScopeVariable> inScopeVariables;
    private StaticContext parent;

    public StaticContext() {
        this.parent = null;
        this.inScopeVariables = new HashMap<>();
    }

    public StaticContext(StaticContext parent) {
        this.parent = parent;
        this.inScopeVariables = new HashMap<>();
    }

    public StaticContext getParent() {
        return this.parent;
    }

    public boolean isInScope(String varName) {
        boolean found = false;
        if (this.inScopeVariables.containsKey(varName))
            return true;
        else {
            StaticContext ancestor = this.parent;
            while (ancestor != null) {
                found = found || ancestor.getInScopeVariables().containsKey(varName);
                ancestor = ancestor.parent;
            }
        }
        return found;
    }

    private InScopeVariable getInScopeVariable(String varName) {
        if (this.inScopeVariables.containsKey(varName))
            return this.inScopeVariables.get(varName);
        else {
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

    public SequenceType getVariableSequenceType(String varName) {
        return getInScopeVariable(varName).getSequenceType();
    }

    public ExceptionMetadata getVariableMetadata(String varName) {
        return getInScopeVariable(varName).getMetadata();
    }

    public ExecutionMode getVariableStorageMode(String varName) {
        return getInScopeVariable(varName).storageMode;
    }

    public void addVariable(
            String varName,
            SequenceType type,
            ExceptionMetadata metadata,
            ExecutionMode storageMode
    ) {
        this.inScopeVariables.put(varName, new InScopeVariable(varName, type, metadata, storageMode));
    }

    protected Map<String, InScopeVariable> getInScopeVariables() {
        return this.inScopeVariables;
    }
}
