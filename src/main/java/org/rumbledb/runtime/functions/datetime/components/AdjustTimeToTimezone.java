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
package org.rumbledb.runtime.functions.datetime.components;

import java.io.Serial;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class AdjustTimeToTimezone extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public AdjustTimeToTimezone(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return adjust(context);
    }

    private Item adjust(DynamicContext context) {
        Item timeItem = this.getChild(0).materializeFirstOrNull(context);
        if (timeItem == null) {
            return null;
        }
        Item timezone = this.getChildren().size() == 2 ? this.getChild(1).materializeFirstOrNull(context) : null;
        if (timezone == null && this.getChildren().size() == 1) {
            return ItemFactory.getInstance()
                    .createTimeItem(timeItem.getTimeValue().withOffsetSameInstant(ZoneOffset.UTC), true);
        }
        if (timezone == null) {
            return ItemFactory.getInstance()
                    .createTimeItem(timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.UTC), false);
        } else {
            if (checkTimeZoneArgument(timezone)) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = timezone.getDurationValue();
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) timezoneDuration.toMinutes() % 60;
            if (timeItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                        .createTimeItem(
                                timeItem.getTimeValue()
                                        .withOffsetSameInstant(ZoneOffset.ofHoursMinutes(hours, minutes)),
                                true);
            }
            return ItemFactory.getInstance()
                    .createTimeItem(
                            timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.ofHoursMinutes(hours, minutes)),
                            true);
        }
    }

    private static boolean checkTimeZoneArgument(Item timezone) {
        Duration timezoneDuration = timezone.getDurationValue();
        return (Math.abs(timezoneDuration.toMinutes()) > 840) || (Double.compare(timezoneDuration.getNano(), 0) != 0);
    }
}
