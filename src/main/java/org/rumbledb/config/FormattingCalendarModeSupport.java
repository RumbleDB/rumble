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
package org.rumbledb.config;

import java.util.regex.Pattern;

public final class FormattingCalendarModeSupport {

    private FormattingCalendarModeSupport() {}

    public static final String DEFAULT = "ISO";

    // validates only the ASCII NCName subset needed by the W3C calendar designators.
    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    public static boolean isValidFormattingCalendar(String calendar) {
        return calendar != null && NCNAME_PATTERN.matcher(calendar).matches();
    }
}
