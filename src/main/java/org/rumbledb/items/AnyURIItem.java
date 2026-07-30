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
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

import java.net.URI;
import java.net.URISyntaxException;

public class AnyURIItem implements Item {


    private static final long serialVersionUID = 1L;
    private URI value;

    public AnyURIItem() {
        super();
    }

    public AnyURIItem(String value) {
        super();
        this.value = parseAnyURIString(value);
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
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
    public int hashCode() {
        return this.getValue().hashCode();
    }

    public URI getValue() {
        return this.value;
    }

    @Override
    public Object getVariantValue() {
        return getStringValue();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
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
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.anyURIItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
