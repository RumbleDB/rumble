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
package org.rumbledb.expressions.flowr;

import lombok.Getter;

import org.rumbledb.expressions.Expression;

public class OrderByClauseSortingKey {
    @Getter
    private final Expression expression;

    @Getter
    private final boolean ascending;

    @Getter
    private final EMPTY_ORDER emptyOrder;

    private final String collationURI;

    public OrderByClauseSortingKey(
            Expression expression, boolean ascending, String collationURI, EMPTY_ORDER empty_order) {
        this.expression = expression;
        this.ascending = ascending;
        this.collationURI = collationURI;
        this.emptyOrder = empty_order;
    }

    public String getUri() {
        return this.collationURI;
    }

    public enum EMPTY_ORDER {
        LEAST,
        GREATEST,
        NONE
    }
}
