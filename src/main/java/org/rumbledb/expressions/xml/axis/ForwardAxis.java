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
package org.rumbledb.expressions.xml.axis;

import lombok.Getter;

@Getter
public enum ForwardAxis {
    CHILD("child::"),
    DESCENDANT("descendant::"),
    ATTRIBUTE("attribute::"),
    SELF("self::"),
    DESCENDANT_OR_SELF("descendant-or-self::"),
    FOLLOWING_SIBLING("following-sibling::"),
    FOLLOWING("following::");

    private final String axisValue;

    ForwardAxis(String axisValue) {
        this.axisValue = axisValue;
    }

    public static ForwardAxis fromString(String text) {
        for (ForwardAxis forwardAxis : ForwardAxis.values()) {
            if (forwardAxis.axisValue.equalsIgnoreCase(text)) {
                return forwardAxis;
            }
        }
        throw new IllegalArgumentException("No constant with text: " + text + " found!");
    }
}
