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
 * Fundamental facet kinds per
 * <a href="https://www.w3.org/TR/xmlschema11-2/#rf-fund-facets">XSD 1.1 Part 2 §4.2</a>.
 *
 * Fundamental facets provide metadata about a datatype (ordered, bounded,
 * cardinality, numeric). Unlike constraining facets, they do not restrict the
 * value space but describe inherent properties of the type.
 */
public enum FundamentalFacetTypes {
    ORDERED("ordered"),
    BOUNDED("bounded"),
    CARDINALITY("cardinality"),
    NUMERIC("numeric");

    private final String facetName;

    FundamentalFacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return this.facetName;
    }
}
