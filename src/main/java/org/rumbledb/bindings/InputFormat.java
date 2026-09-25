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
package org.rumbledb.bindings;

public enum InputFormat {
    JSON,
    TEXT;

    public static InputFormat fromString(String format) {
        if (format == null) {
            return JSON;
        }
        for (InputFormat inputFormat : values()) {
            if (inputFormat.name().equalsIgnoreCase(format)) {
                return inputFormat;
            }
        }
        throw new IllegalArgumentException("Invalid input format: " + format);
    }
}
