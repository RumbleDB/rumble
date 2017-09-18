/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package jiqs.jsoniq.item;

import jiqs.semantics.types.ItemType;
import jiqs.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;

public abstract class JsonItem extends Item {

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public String getStringValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("JSON items are not strings");
    }

    @Override
    public boolean getBooleanValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("JSON items are not booleans");
    }

    @Override
    public double getDoubleValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("JSON items are not doubles");
    }

    @Override
    public int getIntegerValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("JSON items are not integers");
    }

    @Override
    public BigDecimal getDecimalValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("JSON items are not decimals");
    }

    @Override
    public int getSize() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Can not call getSize on non-array item");
    }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.JSONItem) || type.getType().equals(ItemTypes.Item))
            return true;
        return false;
    }
}
