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
 * Constraining facet kinds per XSD 1.1 Part 2 §4.3.
 * Constraining facets restrict the value or lexical space during type derivation.
 *
 * Entries CONTENT, CLOSED, METADATA, and CONSTRAINTS are JSONiq-specific
 * extensions used by object/array types.
 **/
public enum ConstrainingFacetTypes {
    LENGTH("length"),
    MINLENGTH("minLength"),
    MAXLENGTH("maxLength"),
    MININCLUSIVE("minInclusive"),
    MAXINCLUSIVE("maxInclusive"),
    MINEXCLUSIVE("minExclusive"),
    MAXEXCLUSIVE("maxExclusive"),
    TOTALDIGITS("totalDigits"),
    FRACTIONDIGITS("fractionDigits"),
    EXPLICITTIMEZONE("explicitTimezone"),

    CONTENT("content"),
    CLOSED("closed"),

    WHITESPACE("whiteSpace"),
    PATTERN("pattern"),

    ENUMERATION("enumeration"),
    METADATA("metadata"),
    CONSTRAINTS("constraints");

    private final String facetName;

    ConstrainingFacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return this.facetName;
    }
}
