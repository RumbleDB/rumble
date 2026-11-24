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

import java.time.Duration;

/** Validation shared by the three timezone adjustment functions. */
final class TimezoneValidation {
    private TimezoneValidation() {}

    static boolean isValid(Duration offset) {
        long seconds = offset.getSeconds();
        return offset.getNano() == 0 && seconds % 60 == 0 && seconds >= -50400 && seconds <= 50400;
    }
}
