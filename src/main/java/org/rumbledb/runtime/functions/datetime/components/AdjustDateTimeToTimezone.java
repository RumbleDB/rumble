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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.misc.TimezoneSupport;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class AdjustDateTimeToTimezone extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public AdjustDateTimeToTimezone(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
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
        OffsetDateTime value = timeItem.getDateTimeValue();
        ZoneOffset offset;
        if (this.getChildren().size() == 1) {
            offset = Objects.requireNonNullElse(
                    TimezoneSupport.xqueryImplicitTimezone(context, getRuntimeStaticContext()), ZoneOffset.UTC);
        } else {
            Item timezone = this.getChild(1).materializeFirstOrNull(context);
            if (timezone == null) {
                return ItemFactory.getInstance().createDateTimeItem(value.withOffsetSameLocal(ZoneOffset.UTC), false);
            }
            Duration duration = timezone.getDurationValue();
            if (!TimezoneValidation.isValid(duration)) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            offset = ZoneOffset.ofTotalSeconds((int) duration.getSeconds());
        }
        OffsetDateTime adjusted =
                timeItem.hasTimeZone() ? value.withOffsetSameInstant(offset) : value.withOffsetSameLocal(offset);
        return ItemFactory.getInstance().createDateTimeItem(adjusted, true);
    }
}
