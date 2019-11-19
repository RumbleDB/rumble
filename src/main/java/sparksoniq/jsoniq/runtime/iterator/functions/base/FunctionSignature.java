/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.semantics.types.SequenceType;

import java.io.Serializable;
import java.util.List;

public class FunctionSignature implements Serializable {
    private FunctionIdentifier identifier;
    private List<SequenceType> parameters;
    private List<String> parameterNames;
    private SequenceType returnType;

    public FunctionSignature(
            FunctionIdentifier identifier,
            List<SequenceType> parameters,
            List<String> parameterNames,
            SequenceType returnType) {
        this(identifier, parameters, returnType);
        this.parameterNames = parameterNames;
    }

    FunctionSignature(
            FunctionIdentifier identifier,
            List<SequenceType> parameters,
            SequenceType returnType) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public FunctionIdentifier getIdentifier() {
        return identifier;
    }

    public List<SequenceType> getParameters() {
        return parameters;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public SequenceType getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof FunctionSignature
                && this.getIdentifier().equals(((FunctionSignature) instance).getIdentifier())
                && this.getParameters() == ((FunctionSignature) instance).getParameters()
                && this.getReturnType() == ((FunctionSignature) instance).getReturnType();
    }

    @Override
    public int hashCode() {
        return this.getIdentifier().hashCode() + this.getParameters().hashCode() + this.getReturnType().hashCode();
    }
}
