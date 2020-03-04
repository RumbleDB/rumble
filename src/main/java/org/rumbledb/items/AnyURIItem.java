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

package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class AnyURIItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private URI value;

    public AnyURIItem() {
        super();
    }

    public AnyURIItem(String value) {
        super();
        this.value = parseAnyURIString(value);
    }

    static URI parseAnyURIString(String anyURIString) throws IllegalArgumentException {
        if (anyURIString == null)
            throw new IllegalArgumentException();
        try {
            return new URI(anyURIString);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String getStringValue() {
        return this.getValue().toString();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isAnyURI()) {
            return false;
        }
        return (getStringValue().equals(o.getStringValue()));
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : this.getStringValue().compareTo(other.getStringValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    public URI getValue() {
        return this.value;
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.getStringValue());
            case AnyURIItem:
                return this;
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return (itemType == AtomicTypes.AnyURIItem || itemType == AtomicTypes.StringItem);
    }

    @Override
    public String serialize() {
        return this.getStringValue();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.serialize());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = kryo.readObject(input, URI.class);
    }

    @Override
    public boolean isAnyURI() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.AnyURIItem) || super.isTypeOf(type);
    }

    private boolean isBooleanLiteral(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    private boolean isNullLiteral(String value) {
        return "null".equals(value);
    }
}
