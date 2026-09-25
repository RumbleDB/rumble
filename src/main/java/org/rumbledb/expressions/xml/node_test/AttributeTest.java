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
package org.rumbledb.expressions.xml.node_test;

import java.io.Serial;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.rumbledb.context.Name;

@NoArgsConstructor(force = true)
public class AttributeTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Expanded name from the kind test (namespace URI + local name).
     * Only valid when isNameWithoutTypeCheck is true.
     */
    @Getter
    private final Name attributeName;

    private final boolean hasWildcard;
    private final Name typeName;

    public AttributeTest(Name attributeName, Name typeName) {
        this.attributeName = attributeName;
        this.typeName = typeName;
        this.hasWildcard = false;
    }

    public AttributeTest(Name typeName) {
        this.attributeName = null;
        this.typeName = typeName;
        this.hasWildcard = true;
    }

    public AttributeTest(boolean hasWildcard) {
        this.attributeName = null;
        this.typeName = null;
        this.hasWildcard = hasWildcard;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("attribute(");
        if (this.hasWildcard) {
            sb.append("*");
        } else if (this.attributeName != null) {
            sb.append(this.attributeName);
        }
        if (this.typeName != null) {
            sb.append(",");
            sb.append(this.typeName);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return !this.hasWildcard && this.attributeName == null;
    }

    public boolean isNameWithoutTypeCheck() {
        return this.attributeName != null && this.typeName == null;
    }

    public boolean isWildcardOnly() {
        return this.attributeName == null && this.typeName == null && this.hasWildcard;
    }
}
