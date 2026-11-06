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

@Getter
public class DocumentTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    // TODO: schemaElement test unsupported yet.
    private final NodeTest nodeTest;

    public DocumentTest(NodeTest nodeTest) {
        this.nodeTest = nodeTest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("document-node(");
        if (this.nodeTest != null) {
            sb.append(this.nodeTest);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return this.nodeTest == null;
    }
}
