/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.items;

import java.io.Serial;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class DateTimeStampItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private DateTimeItem value;

    DateTimeStampItem(OffsetDateTime value, boolean checkTimezone) {
        if (!checkTimezone) {
            throw new IllegalArgumentException("There is no timezone in dateTime");
        }
        this.value = new DateTimeItem(value, true);
    }

    DateTimeStampItem(String dateTimeStampString) {
        this.value = new DateTimeItem(dateTimeStampString);
        if (!this.value.hasTimeZone()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Item copy(boolean mutable) {
        return new DateTimeStampItem(this.value.getDateTimeValue(), true);
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        return this.value.getDateTimeValue();
    }

    @Override
    public String getStringValue() {
        return this.value.getStringValue();
    }

    @Override
    public boolean isDateTime() {
        return true;
    }

    @Override
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public boolean hasTimeZone() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dateTimeStampItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public int getMonth() {
        return this.value.getMonth();
    }

    @Override
    public int getYear() {
        return this.value.getYear();
    }

    @Override
    public int getDay() {
        return this.value.getDay();
    }

    @Override
    public int getHour() {
        return this.value.getHour();
    }

    @Override
    public int getMinute() {
        return this.value.getMinute();
    }

    @Override
    public double getSecond() {
        return this.value.getSecond();
    }

    @Override
    public int getNanosecond() {
        return this.value.getNanosecond();
    }

    @Override
    public int getOffset() {
        return this.value.getOffset();
    }

    @Override
    public long getEpochMillis() {
        return this.value.getEpochMillis();
    }

    @Override
    public Object getVariantValue() {
        return Timestamp.valueOf(this.getDateTimeValue().toLocalDateTime());
    }
}
