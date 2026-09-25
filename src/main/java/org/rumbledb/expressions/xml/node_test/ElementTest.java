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
public class ElementTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Expanded name from the kind test (namespace URI + local name).
     * Only valid when isNameWithoutTypeCheck is true.
     */
    @Getter
    private final Name elementName;

    private final boolean hasWildcard;
    private final Name typeName;
    // TODO: add support for optional type

    public ElementTest(Name elementName, Name typeName) {
        this.elementName = elementName;
        this.typeName = typeName;
        this.hasWildcard = false;
    }

    public ElementTest(Name typeName) {
        this.elementName = null;
        this.typeName = typeName;
        this.hasWildcard = true;
    }

    public ElementTest(boolean hasWildcard) {
        this.elementName = null;
        this.typeName = null;
        this.hasWildcard = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("element(");
        if (this.hasWildcard) {
            sb.append("*");
        } else if (this.elementName != null) {
            sb.append(this.elementName);
        }
        if (this.typeName != null) {
            sb.append(this.typeName);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return !this.hasWildcard && this.elementName == null;
    }

    public boolean isNameWithoutTypeCheck() {
        return this.elementName != null && this.typeName == null;
    }

    public boolean isWildcardOnly() {
        return this.elementName == null && this.typeName == null && this.hasWildcard;
    }
}
