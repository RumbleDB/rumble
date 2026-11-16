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
package org.rumbledb.items;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;

class DoubleZeroTest {
    @Test
    void cachedZerosPreserveTheirSignRegardlessOfRequestOrder() {
        ItemFactory factory = ItemFactory.getInstance();
        var negative = factory.createDoubleItem(-0.0);
        var positive = factory.createDoubleItem(0.0);
        assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(negative.getDoubleValue()));
        assertEquals(Double.doubleToRawLongBits(0.0), Double.doubleToRawLongBits(positive.getDoubleValue()));
        assertSame(negative, factory.createDoubleItem(-0.0));
        assertSame(positive, factory.createDoubleItem(0.0));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "string(-0e0) eq '-0'",
                "string(xs:double('-0')) eq '-0'",
                "string(floor(-0e0)) eq '-0'",
                "string(round(-0.5e0)) eq '-0'",
                "string(round(-0.1e0)) eq '-0'",
                "string(round(-0e0)) eq '-0'",
                "string(round(0e0)) eq '0'",
                "string(round(-0.1e0, 0)) eq '-0'",
                "string(round(-0.1e0, -1)) eq '-0'",
                "string(round(xs:float('-0.5'))) eq '-0'",
                "string(format-number(-0e0, '0.0e0')) eq '-0.0e0'",
                "math:atan2(0e0, -0e0) eq math:pi()",
                "math:atan2(-0e0, -0e0) eq -math:pi()",
                "math:atan2(-0e0, -1) eq -math:pi()",
                "math:pow(-0e0, -3) eq xs:double('-INF')",
                "string(3 div -0e0) eq '-INF'",
                "string(-3 div -0e0) eq 'INF'",
                "xml-to-json(<number xmlns='http://www.w3.org/2005/xpath-functions'> -0e0 &#xa;</number>) eq '-0'"
            })
    void preservesSignedZeroAcrossQueries(String query) {
        assertTrue(new Rumble(RumbleConfiguration.defaultConfiguration())
                .runQuery(query, URI.create("file:///double-zero.xq"))
                .getAsList()
                .get(0)
                .getBooleanValue());
    }
}
