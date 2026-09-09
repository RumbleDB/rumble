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
package org.rumbledb.types;

/**
 * Legal values for the {@code ordered} fundamental facet per
 * <a href="https://www.w3.org/TR/xmlschema11-2/#rf-ordered">XSD 1.1 Part 2 §4.2.1</a>.
 *
 * <ul>
 * <li>{@link #FALSE} – the value space has no inherent order.</li>
 * <li>{@link #PARTIAL} – the value space is partially ordered.</li>
 * <li>{@link #TOTAL} – the value space is totally ordered.</li>
 * </ul>
 */
public enum OrderedFacetValue {
    FALSE,
    PARTIAL,
    TOTAL
}
