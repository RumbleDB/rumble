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
 * Authors: Ghislain Fourny
 *
 */

package org.rumbledb.expressions.module;

import java.io.Serializable;

import org.rumbledb.exceptions.OurBadException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * This class represents expanded names, corresponding to QNames in the W3C XQuery standard.
 * 
 * An expanded name consists logically of a namespace (possibly absent) and a local name.
 * 
 * A namespace may also have a proxy called a namespace prefix, used in lexical representation
 * (e.g., name of the function or variable in JSONiq code).
 * 
 * Expanded names serve as (global or local) variable names, function names, and error codes.
 * 
 * @author Ghislain Fourny
 *
 */
public class FunctionOrVariableName implements Comparable<FunctionOrVariableName>, Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;
    private String namespace;
    private String prefix;
    private String localName;
    private static final String RUMBLE_NS = "http://rumbledb.org/main-namespace";
    public static final FunctionOrVariableName CONTEXT_ITEM = createVariableInNoNamespace("$");
    public static final FunctionOrVariableName CONTEXT_POSITION = createVariableInNoNamespace("$position");
    public static final FunctionOrVariableName CONTEXT_COUNT = createVariableInNoNamespace("$count");

    private FunctionOrVariableName(String namespace, String prefix, String localName) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.localName = localName;
        if (this.prefix != null && this.namespace == null) {
            throw new OurBadException("Namespace is null, but prefix is present");
        }
    }

    /**
     * Creates an expanded name that has no namespace. In JSONiq, unprefixed variables are never in
     * a namespace.
     * @param localName the name of the variable
     * @return the expanded name
     */
    public static FunctionOrVariableName createVariableInNoNamespace(String localName) {
        return new FunctionOrVariableName(null, null, localName);
    }

    /**
     * Creates an expanded name that has the Rumble namespace. By default, in Rumble, unprefixed
     * function names live in the Rumble namespace. This namespace is for convenience and includes
     * all functions in XQuery's fn namespace, JSONiq's jn and jnlib namespaces, as well as any
     * user-defined functions with unprefixed names.
     * @param localName the name of the variable
     * @return the expanded name
     */
    public static FunctionOrVariableName createVariableInRumbleNamespace(String localName) {
        return new FunctionOrVariableName(RUMBLE_NS, "", localName);
    }

    /**
     * Creates an expanded name whose local name has a #n suffix, where n is the arity of the function,
     * in order to encode the whole function identifier as an expanded name.
     * This is only used to keep track of dependencies between functions and variables in the dependency
     * resolution mechanism, and it should not be used for other purposes.
     * @param localName the name of the variable
     * @return the expanded name
     */
    public FunctionOrVariableName addArityToFunctionName(int arity) {
        return new FunctionOrVariableName(this.namespace, this.prefix, this.localName + "#" + arity);
    }

    /**
     * Returns the namespace.
     * @return the namespace, or null.
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix, or null.
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Returns the local name.
     * @return the local name.
     */
    public String getLocalName() {
        return this.localName;
    }

    /**
     * Converts to a string. If there is a prefix, it is used. Otherwise, if there is a namespace, the Clark notation of
     * the expanded name is returned.
     * Otherwise, the local name is returned.
     * @return The converted string.
     */
    @Override
    public String toString() {
        if (this.prefix!= null) {
            return "this.prefix"+":" + this.localName;
        }
        if (this.namespace != null) {
            return "{" + this.namespace + "}" + this.localName;
        }
        return this.localName;
    }

    @Override
    public int compareTo(FunctionOrVariableName o) {
        FunctionOrVariableName other = (FunctionOrVariableName) o;
        if (this.namespace == null && other.namespace != null) {
            return -1;
        }
        if (this.namespace != null && other.namespace == null) {
            return 1;
        }
        if (this.namespace == null && other.namespace == null) {
            return this.localName.compareTo(other.localName);
        }
        int compare = this.namespace.compareTo(other.namespace);
        if (compare != 0) {
            return compare;
        }
        return this.localName.compareTo(other.localName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionOrVariableName)) {
            return false;
        }
        FunctionOrVariableName other = (FunctionOrVariableName) o;
        if (this.namespace == null && other.namespace != null) {
            return false;
        }
        if (this.namespace != null && other.namespace == null) {
            return false;
        }
        if (this.namespace == null && other.namespace == null) {
            return this.localName.equals(other.localName);
        }
        if (!this.namespace.equals(other.namespace)) {
            return false;
        }
        return this.localName.equals(other.localName);
    }

    @Override
    public int hashCode() {
        if (this.namespace == null) {
            return this.localName.hashCode();
        }
        return this.localName.hashCode() + this.namespace.hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.namespace);
        output.writeString(this.prefix);
        output.writeString(this.localName);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.namespace = input.readString();
        this.prefix = input.readString();
        this.localName = input.readString();
    }
}
