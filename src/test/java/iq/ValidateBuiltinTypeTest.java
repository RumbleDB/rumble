/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.RumbleException;

@Timeout(1000)
public class ValidateBuiltinTypeTest {

    private static Rumble rumble;

    @BeforeAll
    public static void setUp() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        rumble = new Rumble(configuration);
    }

    @Test
    public void validatesStringAndIntegerValues() {
        assertTrue("data(validate type xs:string { <value>hello</value> }) instance of xs:string");
        assertTrue("data(validate type xs:integer { <value>42</value> }) instance of xs:integer");
        assertTrue("data(validate type xs:integer { <value> 42 </value> }) eq 42");
    }

    @Test
    public void resolvesQNameAgainstTheValidatedElement() {
        assertTrue("namespace-uri-from-QName(data(validate type xs:QName { "
                + "<value xmlns:p=\"urn:test\">p:name</value> })) eq \"urn:test\"");
    }

    @Test
    public void returnsANewNodeWithTheSameKind() {
        assertTrue("let $input := <value>42</value> "
                + "let $validated := validate type xs:integer { $input } "
                + "return $validated instance of element() and not($input is $validated)");
    }

    @Test
    public void rejectsInvalidLexicalValues() {
        assertErrorCode("validate type xs:integer { <value>not-an-integer</value> }", "XQDY0027");
    }

    @Test
    public void rejectsOperandsThatAreNotExactlyOneDocumentOrElement() {
        assertErrorCode("validate type xs:string { () }", "XQTY0030");
        assertErrorCode("validate type xs:string { 1 }", "XQTY0030");
        assertErrorCode("validate type xs:string { (<a/>, <b/>) }", "XQTY0030");
    }

    @Test
    public void rejectsMalformedDocumentNodesWithTheDocumentStructureError() {
        assertErrorCode("validate type xs:string { document { <a/>, <b/> } }", "XQDY0061");
        assertErrorCode("validate type xs:string { document { text { 'text' }, <a/> } }", "XQDY0061");
    }

    @Test
    public void rejectsUnknownTypesStatically() {
        assertErrorCode("validate type xs:notAType { <value/> }", "XQST0104");
    }

    private static void assertErrorCode(String query, String expectedCode) {
        RumbleException exception =
                Assertions.assertThrows(RumbleException.class, () -> rumble.runQueryToString(query));
        Assertions.assertEquals(expectedCode, exception.getErrorCode().toString());
    }

    private static void assertTrue(String query) {
        Assertions.assertTrue(rumble.runQuery(query).getAsList().get(0).getBooleanValue());
    }
}
