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
package org.rumbledb.runtime.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XmlBaseUtilsTest {

    @Test
    public void testEmptyValueReturnsBase() {
        Assertions.assertEquals("http://example.org/base/", XmlBaseUtils.resolve("http://example.org/base/", ""));
        Assertions.assertEquals("http://example.org/base/", XmlBaseUtils.resolve("http://example.org/base/", null));
    }

    @Test
    public void testAbsoluteUriOverridesBase() {
        Assertions.assertEquals("http://other.org/file.xml", XmlBaseUtils.resolve("http://example.org/base/", "http://other.org/file.xml"));
        Assertions.assertEquals("http://other.org/sub/file.xml", XmlBaseUtils.resolve("http://example.org/base/", "http://other.org/sub/../sub/file.xml"));
    }

    @Test
    public void testRelativeUriWithoutBaseReturnsNull() {
        Assertions.assertNull(XmlBaseUtils.resolve(null, "sub/file.xml"));
        Assertions.assertNull(XmlBaseUtils.resolve("relative/base/", "sub/file.xml"));
    }

    @Test
    public void testRelativeUriResolution() {
        Assertions.assertEquals("http://example.org/base/sub/file.xml", XmlBaseUtils.resolve("http://example.org/base/", "sub/file.xml"));
        Assertions.assertEquals("http://example.org/base/sub/file.xml", XmlBaseUtils.resolve("http://example.org/base/dir/", "../sub/file.xml"));
        Assertions.assertEquals("http://example.org/sub/file.xml", XmlBaseUtils.resolve("http://example.org/base/", "../sub/file.xml"));
    }

    @Test
    public void testLeiriResolution() {
        Assertions.assertEquals("http://example.com/café/résumé.xml", XmlBaseUtils.resolve("http://example.com/café/", "résumé.xml"));
        Assertions.assertEquals("http://example.com/café/résumé.xml", XmlBaseUtils.resolve(null, "http://example.com/café/résumé.xml"));
    }

    @Test
    public void testMalformedUriReturnsNull() {
        Assertions.assertNull(XmlBaseUtils.resolve("http://example.org/", "http://example.com/[invalid]"));
    }
}
