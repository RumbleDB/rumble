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

package sparksoniq.jsoniq.runtime.iterator.functions.base;

import java.io.Serializable;

public class FunctionIdentifier implements Serializable {
    private int arity;
    private String name;

    public FunctionIdentifier() {
    }

    public FunctionIdentifier(String functionName, int arity) {
        this.name = functionName;
        this.arity = arity;
    }

    public int getArity() {
        return arity;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof FunctionIdentifier
            && this.name.equals(((FunctionIdentifier) instance).getName())
            && this.arity == ((FunctionIdentifier) instance).getArity();
    }

    @Override
    public int hashCode() {
        return name.hashCode() + arity;
    }
}
