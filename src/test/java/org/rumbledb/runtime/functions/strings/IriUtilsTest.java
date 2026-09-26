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
package org.rumbledb.runtime.functions.strings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IriUtilsTest {

    @Test
    public void testNull() {
        Assertions.assertNull(IriUtils.encodeIri(null));
    }

    @Test
    public void testEmpty() {
        Assertions.assertEquals("", IriUtils.encodeIri(""));
    }

    @Test
    public void testAsciiPassthrough() {
        String ascii = "http://example.org/path/to/resource?query=1#frag";
        Assertions.assertEquals(ascii, IriUtils.encodeIri(ascii));
    }

    @Test
    public void testNonAsciiCharacters() {
        Assertions.assertEquals("http://example.org/%C3%A9%C3%A0%C3%BC", IriUtils.encodeIri("http://example.org/éàü"));
    }

    @Test
    public void testSpacesAndDisallowedChars() {
        Assertions.assertEquals(
                "http://example.org/~%C3%A9%20and%20%3Cx%3E", IriUtils.encodeIri("http://example.org/~é and <x>"));
    }

    @Test
    public void testDisallowedAsciiChars() {
        // Space, double quote, <, >, \, ^, `, {, |, }
        Assertions.assertEquals("%20", IriUtils.encodeIri(" "));
        Assertions.assertEquals("%22", IriUtils.encodeIri("\""));
        Assertions.assertEquals("%3C", IriUtils.encodeIri("<"));
        Assertions.assertEquals("%3E", IriUtils.encodeIri(">"));
        Assertions.assertEquals("%5C", IriUtils.encodeIri("\\"));
        Assertions.assertEquals("%5E", IriUtils.encodeIri("^"));
        Assertions.assertEquals("%60", IriUtils.encodeIri("`"));
        Assertions.assertEquals("%7B", IriUtils.encodeIri("{"));
        Assertions.assertEquals("%7C", IriUtils.encodeIri("|"));
        Assertions.assertEquals("%7D", IriUtils.encodeIri("}"));
    }
}
