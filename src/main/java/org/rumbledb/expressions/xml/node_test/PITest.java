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
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * XQuery 3.1 Section 2.5.5 - SequenceType Matching
 * PITest ::= "processing-instruction" "(" (NCName | StringLiteral)? ")"
 * A PITest with no argument matches any processing-instruction node.
 * A PITest with an NCName or StringLiteral argument matches any processing-instruction node
 * whose PITarget equals fn:normalize-space of the given name.
 */
@Getter
@NoArgsConstructor(force = true)
public class PITest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String targetName;

    /**
     * Creates a PITest that matches any processing-instruction node.
     */
    /**
     * Creates a PITest that matches processing-instruction nodes with the given target name.
     *
     * @param targetName the target name to match against (from NCName or StringLiteral)
     */
    public PITest(String targetName) {
        this.targetName = StringUtils.normalizeSpace(Objects.requireNonNull(targetName));
    }

    public boolean hasTargetName() {
        return this.targetName != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("processing-instruction(");
        if (this.targetName != null) {
            sb.append(this.targetName);
        }
        sb.append(")");
        return sb.toString();
    }
}
