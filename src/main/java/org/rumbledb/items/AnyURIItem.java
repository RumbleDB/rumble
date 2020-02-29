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
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.net.URI;
import java.net.URISyntaxException;

public class AnyURIItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private URI value;

    public AnyURIItem() {
        super();
    }

    public AnyURIItem(URI value) {
        super();
        this.value = value;
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
        return this;
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        if (itemType == AtomicTypes.AnyURIItem || itemType == AtomicTypes.StringItem)
            return true;
        try {
            if (itemType == AtomicTypes.IntegerItem) {
                Integer.parseInt(this.getStringValue());
            } else if (itemType == AtomicTypes.DecimalItem) {
                if (this.getStringValue().contains("e") || this.getStringValue().contains("E"))
                    return false;
                Float.parseFloat(this.getStringValue());
            } else if (itemType == AtomicTypes.DoubleItem) {
                Double.parseDouble(this.getStringValue());
            } else if (itemType == AtomicTypes.NullItem) {
                return isNullLiteral(this.getStringValue());
            } else if (itemType == AtomicTypes.DurationItem) {
                DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.DurationItem);
            } else if (itemType == AtomicTypes.YearMonthDurationItem) {
                DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.YearMonthDurationItem);
            } else if (itemType == AtomicTypes.DayTimeDurationItem) {
                DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.DayTimeDurationItem);
            } else if (itemType == AtomicTypes.DateTimeItem) {
                DateTimeItem.parseDateTime(this.getStringValue(), AtomicTypes.DateTimeItem);
            } else if (itemType == AtomicTypes.DateItem) {
                DateTimeItem.parseDateTime(this.getStringValue(), AtomicTypes.DateItem);
            } else if (itemType == AtomicTypes.TimeItem) {
                DateTimeItem.parseDateTime(this.getStringValue(), AtomicTypes.TimeItem);
            } else if (itemType == AtomicTypes.HexBinaryItem) {
                HexBinaryItem.parseHexBinaryString(this.getStringValue());
            } else if (itemType == AtomicTypes.Base64BinaryItem) {
                Base64BinaryItem.parseBase64BinaryString(this.getStringValue());
            } else
                return isBooleanLiteral(this.getStringValue());
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void write(Kryo kryo, Output output) {

    }

    @Override
    public void read(Kryo kryo, Input input) {

    }

    @Override
    public boolean isAnyURI() {
        return true;
    }

    private boolean isBooleanLiteral(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    private boolean isNullLiteral(String value) {
        return "null".equals(value);
    }
}
