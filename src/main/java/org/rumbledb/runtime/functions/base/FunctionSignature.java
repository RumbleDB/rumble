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

package org.rumbledb.runtime.functions.base;

import java.io.Serializable;
import java.util.List;

import org.rumbledb.types.SequenceType;

public class FunctionSignature implements Serializable {
    private List<SequenceType> parameterTypes;
    private SequenceType returnType;

    public FunctionSignature(
            List<SequenceType> parameterTypes,
            SequenceType returnType
    ) {
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }


    public List<SequenceType> getParameterTypes() {
        return this.parameterTypes;
    }

    public SequenceType getReturnType() {
        return this.returnType;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof FunctionSignature
            && this.getParameterTypes() == ((FunctionSignature) instance).getParameterTypes()
            && this.getReturnType() == ((FunctionSignature) instance).getReturnType();
    }

    @Override
    public int hashCode() {
        return this.getParameterTypes().hashCode() + this.getReturnType().hashCode();
    }
}
