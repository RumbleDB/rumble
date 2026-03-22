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
 */

package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Runtime value for {@code xs:QName}. Expanded name (namespace URI + local name) defines XDM value equality;
 * the prefix is retained for display only.
 */
public class QNameItem implements Item {

    private static final long serialVersionUID = 1L;

    private Name expandedName;

    public QNameItem() {
        super();
    }

    public QNameItem(Name expandedName) {
        this.expandedName = expandedName;
    }

    public Name getExpandedName() {
        return this.expandedName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QNameItem)) {
            return false;
        }
        QNameItem other = (QNameItem) o;
        return expandedQNameEqual(this.expandedName, other.expandedName);
    }

    @Override
    public int hashCode() {
        String ns = this.expandedName.getNamespace();
        String local = this.expandedName.getLocalName();
        int h = local != null ? local.hashCode() : 0;
        if (ns != null) {
            h = 31 * h + ns.hashCode();
        }
        return h;
    }

    /**
     * XDM QName equality: same namespace URI and local name (prefix irrelevant).
     */
    public static boolean expandedQNameEqual(Name a, Name b) {
        if (a == null || b == null) {
            return a == b;
        }
        String nsA = a.getNamespace();
        String nsB = b.getNamespace();
        if (nsA == null && nsB != null || nsA != null && nsB == null) {
            return false;
        }
        if (nsA != null && !nsA.equals(nsB)) {
            return false;
        }
        return a.getLocalName().equals(b.getLocalName());
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.QNameItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isQName() {
        return true;
    }

    @Override
    public String getStringValue() {
        return this.expandedName.toString();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return true;
    }

    @Override
    public Object getVariantValue() {
        return this.expandedName;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, this.expandedName, Name.class);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.expandedName = kryo.readObjectOrNull(input, Name.class);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, getStringValue(), SequenceType.STRING);
    }

    @Override
    public BigInteger getIntegerValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + getDynamicType());
    }

    @Override
    public BigDecimal getDecimalValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + getDynamicType());
    }

    @Override
    public double castToDoubleValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + getDynamicType());
    }
}
