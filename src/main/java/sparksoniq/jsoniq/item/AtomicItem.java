/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.item;

import org.rumbledb.api.Item;

import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

public abstract class AtomicItem extends Item {

	private static final long serialVersionUID = 1L;

	protected AtomicItem() {
        super();
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.AtomicItem) || type.getType().equals(ItemTypes.Item);
    }

    public abstract boolean isCastableAs(AtomicType type);

    public abstract AtomicItem castAs(AtomicItem atomicItem);

    public AtomicItem createFromBoolean(BooleanItem booleanItem) {return null;}
    public AtomicItem createFromString(StringItem stringItem) {return null;}
    public AtomicItem createFromInteger(IntegerItem integerItem) {return null;}
    public AtomicItem createFromDecimal(DecimalItem decimalItem) {return null;}
    public AtomicItem createFromDouble(DoubleItem doubleItem) {return null;}
    public AtomicItem createFromDuration(DurationItem durationItem) {return null;}
    public AtomicItem createFromYearMonthDuration(YearMonthDurationItem yearMonthDurationItem) {return null;}
    public AtomicItem createFromDayTimeDuration(DayTimeDurationItem dayTimeDurationItem) {return null;}

}
