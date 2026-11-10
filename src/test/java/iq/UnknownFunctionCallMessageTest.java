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
package iq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnknownFunctionCallException;

public class UnknownFunctionCallMessageTest {

    private static String message(String localName, int arity) {
        Name name = new Name(Name.FN_NS, "fn", localName);
        return new UnknownFunctionCallException(name, arity, ExceptionMetadata.EMPTY_METADATA).getJSONiqErrorMessage();
    }

    @Test
    public void builtinWithSingleArityListsIt() {
        Assertions.assertTrue(message("abs", 2).endsWith("called with 2 parameters. Expected 1 parameter."));
    }

    @Test
    public void builtinWithSeveralAritiesListsAllOfThem() {
        Assertions.assertTrue(message("substring", 1).endsWith("called with 1 parameter. Expected 2 or 3 parameters."));
    }

    @Test
    public void unknownNameKeepsOriginalMessage() {
        Assertions.assertTrue(message("no-such-function", 1).endsWith("called with 1 parameter."));
    }

    @Test
    public void userDefinedArityIsListed() {
        Name name = new Name("http://example.com", "ex", "myfunc");
        StaticContext context =
                new StaticContext(null, RumbleConfiguration.builder().build());
        context.addFunctionSignature(new FunctionIdentifier(name, 2), null);
        String text = new UnknownFunctionCallException(
                        new FunctionIdentifier(name, 1), ExceptionMetadata.EMPTY_METADATA, context)
                .getJSONiqErrorMessage();
        Assertions.assertTrue(text.endsWith("called with 1 parameter. Expected 2 parameters."));
    }
}
