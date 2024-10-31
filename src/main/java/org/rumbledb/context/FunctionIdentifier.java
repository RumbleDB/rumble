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

import java.io.Serializable;

public class FunctionIdentifier implements Serializable {
    private int arity;
    private Name name;
    private static final long serialVersionUID = 1L;

    public FunctionIdentifier() {
    }

    public FunctionIdentifier(Name functionName, int arity) {
        this.name = functionName;
        this.arity = arity;
    }

    public int getArity() {
        return this.arity;
    }

    public Name getName() {
        return this.name;
    }

    public Name getNameWithArity() {
        return this.name.addArityToFunctionName(this.getArity());
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof FunctionIdentifier
            && this.name.equals(((FunctionIdentifier) instance).getName())
            && this.arity == ((FunctionIdentifier) instance).getArity();
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.arity;
    }

    @Override
    public String toString() {
        return this.name + "#" + this.arity;
    }
}
