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
package org.rumbledb.runtime.misc;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.rumbledb.api.Item;
import org.rumbledb.config.model.LanguageVersion;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;

public final class TimezoneSupport {

    private TimezoneSupport() {}

    public static ZoneOffset xqueryImplicitTimezone(DynamicContext context, RuntimeStaticContext staticContext) {
        LanguageVersion language = staticContext.getQueryLanguage();
        if (language == null) {
            language = staticContext.getConfiguration().semantics().queryLanguage();
        }
        // JSONiq operators retain the offsets stored on their operands.
        return language.isXQuery() ? context.getImplicitTimezone() : null;
    }

    public static OffsetDateTime dateTimeWithImplicitTimezone(Item item, ZoneOffset timezone) {
        OffsetDateTime value = item.getDateTimeValue();
        return timezone == null || item.hasTimeZone() ? value : value.withOffsetSameLocal(timezone);
    }

    public static OffsetTime timeWithImplicitTimezone(Item item, ZoneOffset timezone) {
        OffsetTime value = item.getTimeValue();
        return timezone == null || item.hasTimeZone() ? value : value.withOffsetSameLocal(timezone);
    }
}
