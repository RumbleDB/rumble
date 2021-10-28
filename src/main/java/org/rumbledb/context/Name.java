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

package org.rumbledb.context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class Name implements Comparable<Name>, Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;
    private String namespace;
    private String prefix;
    private String localName;
    private transient int hashCode;
    public static final String JSONIQ_DEFAULT_TYPE_NS = "http://jsoniq.org/default-type-namespace";
    public static final String JSONIQ_DEFAULT_FUNCTION_NS = "http://jsoniq.org/default-function-namespace";
    public static final String FN_NS = "http://www.w3.org/2005/xpath-functions";
    public static final String JN_NS = "http://jsoniq.org/functions";
    public static final String MATH_NS = "http://www.w3.org/2005/xpath-functions/math";
    public static final String MAP_NS = "http://www.w3.org/2005/xpath-functions/map";
    public static final String ARRAY_NS = "http://www.w3.org/2005/xpath-functions/array";
    public static final String XS_NS = "http://www.w3.org/2001/XMLSchema";
    public static final String JS_NS = "http://jsoniq.org/types";
    public static final String LOCAL_NS = "http://www.w3.org/2005/xquery-local-functions";
    public static final String DEFAULT_COLLATION_NS = "http://www.w3.org/2005/xpath-functions/collation/codepoint";
    public static final Name CONTEXT_ITEM = createVariableInNoNamespace("$");
    public static final Name CONTEXT_POSITION = createVariableInNoNamespace("$position");
    public static final Name CONTEXT_COUNT = createVariableInNoNamespace("$count");

    public Name() {
        this.namespace = null;
        this.prefix = null;
        this.localName = null;
        this.hashCode = 0;
    }

    public Name(String namespace, String prefix, String localName) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.localName = localName;
        if (this.prefix != null && this.namespace == null) {
            throw new OurBadException("Namespace is null, but prefix is present");
        }
        precomputeHashCode();
    }

    /**
     * Creates an expanded name that has no namespace. In JSONiq, unprefixed variables are never in
     * a namespace.
     * 
     * @param localName the name of the variable
     * @return the expanded name
     */
    public static Name createVariableInNoNamespace(String localName) {
        return new Name(null, null, localName);
    }

    /**
     * Creates an expanded name that has the default JSONiq function namespace. By default, in Rumble, unprefixed
     * function names live in this namespace. This namespace is for convenience and includes
     * all functions in XQuery's fn namespace, JSONiq's jn (JSONiq core) and jnlib (JSONiq library)
     * namespaces, as well as any
     * user-defined functions with unprefixed names.
     * 
     * @param localName the name of the variable
     * @return the expanded name
     */
    public static Name createVariableInDefaultTypeNamespace(String localName) {
        return new Name(JSONIQ_DEFAULT_TYPE_NS, "", localName);
    }

    /**
     * Creates an expanded name that has the default JSONiq type namespace. By default, in Rumble, unprefixed
     * type names live in this namespace. This namespace is for convenience and includes
     * all builtin XQuery and JSONiq functions.
     * 
     * @param localName the name of the variable
     * @return the expanded name
     */
    public static Name createVariableInDefaultFunctionNamespace(String localName) {
        return new Name(JSONIQ_DEFAULT_FUNCTION_NS, "", localName);
    }

    public static Name createVariableInDefaultXQueryTypeNamespace(String localName) {
        return new Name(FN_NS, "", localName);
    }

    public static Name createVariableInDefaultXQueryFunctionNamespace(String localName) {
        return new Name(XS_NS, "", localName);
    }

    /**
     * Creates an expanded name resolving the prefix from namespace bindings.
     * 
     * @param prefix the prefix
     * @param localName the local name
     * @param moduleContext the module context containing the bindings.
     * @return the expanded name.
     */
    public static Name createVariableResolvingPrefix(String prefix, String localName, StaticContext moduleContext) {
        String namespace = moduleContext.resolveNamespace(prefix);
        if (namespace != null) {
            return new Name(namespace, prefix, localName);
        } else {
            return null;
        }
    }

    /**
     * Creates an expanded name resolving the prefix from namespace bindings.
     * 
     * @param prefixedName the QName literal.
     * @param moduleContext the module context containing the bindings.
     * @return the expanded name.
     */
    public static Name createTypeNameFromLiteral(String prefixedName, StaticContext moduleContext) {
        int pos = prefixedName.indexOf(":");
        String prefix = null;
        String localName = prefixedName;
        if (pos == -1) {
            return new Name(JSONIQ_DEFAULT_TYPE_NS, prefix, localName);
        }
        prefix = prefixedName.substring(0, pos);
        localName = prefixedName.substring(pos + 1);
        String namespace = moduleContext.resolveNamespace(prefix);
        if (namespace != null) {
            return new Name(namespace, prefix, localName);
        } else {
            throw new OurBadException("Prefix " + prefix + " could not be resolved against a namespace in scope.");
        }
    }

    /**
     * Converts the expanded name to an expanded name whose local name has a #n suffix, where n is the arity of the
     * function,
     * in order to encode the whole function identifier as an expanded name.
     * This is only used to keep track of dependencies between functions and variables in the dependency
     * resolution mechanism, and it should not be used for other purposes.
     * 
     * @param arity the arity of the function.
     * @return the expanded name
     */
    public Name addArityToFunctionName(int arity) {
        return new Name(this.namespace, this.prefix, this.localName + "#" + arity);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getLocalName() {
        return this.localName;
    }

    /**
     * Converts to a string. If there is a prefix, it is used. Otherwise, if there is a namespace, the Clark notation of
     * the expanded name is returned.
     * Otherwise, the local name is returned.
     * 
     * @return The converted string.
     */
    @Override
    public String toString() {
        if (this.prefix != null && !this.prefix.equals("")) {
            return this.prefix + ":" + this.localName;
        }
        if (this.prefix == null && this.namespace != null) {
            return "{" + this.namespace + "}" + this.localName;
        }
        return this.localName;
    }

    @Override
    public int compareTo(Name o) {
        Name other = (Name) o;
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
        if (!(o instanceof Name)) {
            return false;
        }
        Name other = (Name) o;
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
        return this.hashCode;
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
        precomputeHashCode();
    }

    public void precomputeHashCode() {
        if (this.localName == null) {
            this.hashCode = 0;
            return;
        }
        if (this.namespace == null) {
            this.hashCode = this.localName.hashCode();
            return;
        }
        this.hashCode = this.localName.hashCode() + this.namespace.hashCode();
    }

    private void readObject(ObjectInputStream i) throws ClassNotFoundException, IOException {
        i.defaultReadObject();
        precomputeHashCode();
    }

    private void writeObject(ObjectOutputStream i) throws IOException {
        i.defaultWriteObject();
    }
}
