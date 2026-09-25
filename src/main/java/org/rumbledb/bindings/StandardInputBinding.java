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

import java.util.Objects;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public final class StandardInputBinding implements Binding {
    private static final long serialVersionUID = 1L;

    InputFormat format;

    public StandardInputBinding(String format) {
        this(InputFormat.fromString(format));
    }

    public StandardInputBinding(InputFormat format) {
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }
}
