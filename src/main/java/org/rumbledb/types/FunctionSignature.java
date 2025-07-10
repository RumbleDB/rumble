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

package org.rumbledb.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class FunctionSignature implements Serializable, KryoSerializable {
    private List<SequenceType> parameterTypes;
    private SequenceType returnType;
    private boolean isUpdating;
    private static final long serialVersionUID = 1L;

    public FunctionSignature(
            List<SequenceType> parameterTypes,
            SequenceType returnType,
            boolean isUpdating
    ) {
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.isUpdating = isUpdating;
    }

    public FunctionSignature(
            List<SequenceType> parameterTypes,
            SequenceType returnType
    ) {
        this(parameterTypes, returnType, false);
    }

    public FunctionSignature() {
        this.parameterTypes = new ArrayList<>();
        this.returnType = SequenceType.ITEM_STAR;
    }


    public List<SequenceType> getParameterTypes() {
        return this.parameterTypes;
    }

    public SequenceType getReturnType() {
        return this.returnType;
    }

    public boolean isUpdating() {
        return this.isUpdating;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof FunctionSignature
            && this.getParameterTypes() == ((FunctionSignature) instance).getParameterTypes()
            && this.getReturnType() == ((FunctionSignature) instance).getReturnType();
    }

    public boolean isSubtypeOf(FunctionSignature other) {
        // a signature is a subtype of another signature if it always respect its contract typewise (i.e. no static type
        // errors)
        // this return type must be a subtype of other return type
        if (!this.returnType.isSubtypeOf(other.returnType)) {
            return false;
        }
        int paramsLength = this.parameterTypes.size();
        // must have same number of parameters
        if (paramsLength != other.parameterTypes.size()) {
            return false;
        }
        for (int i = 0; i < paramsLength; ++i) {
            // any parameter type of other must be a subtype of the corresponding parameter of this
            if (!other.parameterTypes.get(i).isSubtypeOf(this.parameterTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.getParameterTypes().hashCode() + this.getReturnType().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function(");
        String separator = "";
        for (SequenceType type : this.parameterTypes) {
            sb.append(separator + type);
            separator = ", ";
        }
        sb.append(") as ");
        sb.append(this.returnType);
        return sb.toString();
    }


    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.parameterTypes);
        kryo.writeObject(output, this.returnType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.parameterTypes = kryo.readObject(input, ArrayList.class);
        this.returnType = kryo.readObject(input, SequenceType.class);
    }
}
