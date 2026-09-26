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
package org.rumbledb.items.xml;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;

public class NodeBaseUriTest {

    @Test
    public void testNamespaceItemBaseUriAlwaysEmpty() {
        ElementItem parent =
                ItemFactory.getInstance().createXmlElementNode(new Name("", "", "elem"), List.of(), List.of());
        parent.setConstructionBaseUri(URI.create("http://example.org/base/"));
        NamespaceItem ns =
                (NamespaceItem) ItemFactory.getInstance().createXmlNamespaceNode("p", "http://example.org/ns");
        ns.setParent(parent);

        Assertions.assertTrue(ns.baseUri().isEmpty());
    }

    @Test
    public void testDocumentItemBaseUriOnlyAbsolute() {
        DocumentItem doc = ItemFactory.getInstance().createXmlDocumentNode(List.of());
        Assertions.assertTrue(doc.baseUri().isEmpty());

        doc.setConstructionBaseUri(URI.create("relative/path.xml"));
        Assertions.assertTrue(doc.baseUri().isEmpty());

        doc.setConstructionBaseUri(URI.create("http://example.org/doc.xml"));
        Assertions.assertFalse(doc.baseUri().isEmpty());
        Assertions.assertEquals(
                "http://example.org/doc.xml", doc.baseUri().get(0).getStringValue());
    }

    @Test
    public void testElementItemRelativeBaseWithoutBaseReturnsEmpty() {
        Item baseAttr =
                ItemFactory.getInstance().createXmlAttributeNode(new Name(Name.XML_NS, "xml", "base"), "relative/dir/");
        ElementItem elem =
                ItemFactory.getInstance().createXmlElementNode(new Name("", "", "elem"), List.of(), List.of(baseAttr));

        Assertions.assertTrue(elem.baseUri().isEmpty());
    }

    @Test
    public void testElementItemParentWithNoBaseDoesNotFallbackToConstructionBaseUri() {
        ElementItem parent =
                ItemFactory.getInstance().createXmlElementNode(new Name("", "", "parent"), List.of(), List.of());
        // parent has no base URI

        ElementItem child =
                ItemFactory.getInstance().createXmlElementNode(new Name("", "", "child"), List.of(), List.of());
        child.setConstructionBaseUri(URI.create("http://example.org/child-base/"));
        child.setParent(parent);

        // Child has a parent whose base URI is empty, so child's base URI must be empty
        Assertions.assertTrue(child.baseUri().isEmpty());
    }
}
