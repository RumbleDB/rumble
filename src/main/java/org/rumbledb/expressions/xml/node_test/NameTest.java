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

import org.rumbledb.context.Name;

public class NameTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum WildcardType {
        NONE,
        ALL, // *
        ANY_NAMESPACE, // *:localName
        ANY_LOCAL_NAME // namespaceURI:* (from prefix:* or Q{uri}*)
    }

    private final Name qname;
    private final WildcardType wildcardType;

    @Getter
    private final String localName;

    @Getter
    private final String namespaceURI;

    private final String wildcardText;

    public NameTest(Name qname) {
        this.qname = qname;
        this.wildcardType = WildcardType.NONE;
        this.localName = qname != null ? qname.getLocalName() : null;
        this.namespaceURI = qname != null ? qname.getNamespace() : null;
        this.wildcardText = null;
    }

    public NameTest(WildcardType wildcardType, String localName, String namespaceURI, String wildcardText) {
        this.qname = null;
        this.wildcardType = wildcardType;
        this.localName = localName;
        this.namespaceURI = namespaceURI;
        this.wildcardText = wildcardText;
    }

    public static NameTest all() {
        return new NameTest(WildcardType.ALL, null, null, "*");
    }

    public static NameTest anyNamespace(String localName, String wildcardText) {
        return new NameTest(WildcardType.ANY_NAMESPACE, localName, null, wildcardText);
    }

    public static NameTest anyLocalName(String namespaceURI, String wildcardText) {
        return new NameTest(WildcardType.ANY_LOCAL_NAME, null, namespaceURI, wildcardText);
    }

    @Override
    public String toString() {
        if (this.qname != null) {
            return this.qname.toString();
        }
        return this.wildcardText;
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
        return this.wildcardType == WildcardType.ALL;
    }

    public boolean hasWildcardNamespace() {
        return this.wildcardType == WildcardType.ANY_NAMESPACE;
    }

    public boolean hasWildcardLocalName() {
        return this.wildcardType == WildcardType.ANY_LOCAL_NAME;
    }
}
