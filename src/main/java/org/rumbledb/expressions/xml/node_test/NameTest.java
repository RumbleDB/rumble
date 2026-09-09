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

import org.rumbledb.context.Name;

// TODO: Add support for name test
public class NameTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Name qname;
    private final String wildcardWithNCName;

    public NameTest(Name qname) {
        this.qname = qname;
        this.wildcardWithNCName = null;
    }

    public NameTest(String wildcardWithNCName) {
        this.qname = null;
        this.wildcardWithNCName = wildcardWithNCName;
    }

    @Override
    public String toString() {
        if (this.qname != null) {
            return this.qname.toString();
        }
        return this.wildcardWithNCName;
    }

    public boolean hasQName() {
        return this.qname != null;
    }

    /**
     * Expanded name (namespace URI + local name). Prefer {@link Name#equals} over string forms for node matching:
     * the same expanded name can stringify differently when the prefix is empty vs absent.
     */
    public Name getExpandedName() {
        return this.qname;
    }

    public boolean hasWildcardOnly() {
        return this.wildcardWithNCName != null && this.wildcardWithNCName.equals("*");
    }

    public String getWildcardQName() {
        return this.wildcardWithNCName;
    }
}
