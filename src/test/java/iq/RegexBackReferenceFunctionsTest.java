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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidReplacementStringException;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.functions.strings.AnalyzeStringFunctionIterator;
import org.rumbledb.runtime.functions.strings.MatchesFunctionIterator;
import org.rumbledb.runtime.functions.strings.ReplaceFunctionIterator;
import org.rumbledb.runtime.functions.strings.TokenizeFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class RegexBackReferenceFunctionsTest {
    private final RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
    private final DynamicContext context = new DynamicContext(configuration);
    private final RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
            .configuration(configuration)
            .staticType(new SequenceType(BuiltinTypesCatalogue.item))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(ExceptionMetadata.EMPTY_METADATA)
            .build();

    private List<ItemRuntimePlan> arguments(String... values) {
        return Arrays.stream(values)
                .<ItemRuntimePlan>map(value ->
                        new ConstantRuntimeIterator(ItemFactory.getInstance().createStringItem(value), staticContext))
                .toList();
    }

    @Test
    public void matchesAndReplaceHonorAbsentGroups() {
        Assertions.assertTrue(new MatchesFunctionIterator(arguments("", "(a)?\\1"), staticContext)
                .evaluateAtMostOne(context)
                .getBooleanValue());
        Assertions.assertEquals(
                "-c",
                new ReplaceFunctionIterator(arguments("bc", "(a)?b\\1(c)", "$1-$2"), staticContext)
                        .evaluateAtMostOne(context)
                        .getStringValue());
        Assertions.assertEquals(
                "c0",
                new ReplaceFunctionIterator(arguments("bc", "(a)?b\\1(c)", "$9$20"), staticContext)
                        .evaluateAtMostOne(context)
                        .getStringValue());
    }

    @Test
    public void zeroLengthBackReferencesRaiseForx0003InAllThreeFunctions() {
        for (String flags : new String[] {"", "i"}) {
            Assertions.assertThrows(MatchesEmptyStringException.class, () -> new ReplaceFunctionIterator(
                            arguments("x", "(a)?\\1", "y", flags), staticContext)
                    .evaluateAtMostOne(context));
            Assertions.assertThrows(MatchesEmptyStringException.class, () -> new AnalyzeStringFunctionIterator(
                            arguments("x", "(a)?\\1", flags), staticContext)
                    .evaluateAtMostOne(context));
            Assertions.assertThrows(MatchesEmptyStringException.class, () -> {
                try (var cursor = new TokenizeFunctionIterator(arguments("x", "(a)?\\1", flags), staticContext)
                        .getCursor(context)) {
                    cursor.hasNext();
                }
            });
        }
    }

    @Test
    public void analyzeStringExposesOnlyUserGroupsWithTheirOriginalParents() {
        Item result = new AnalyzeStringFunctionIterator(arguments("bc", "((a)?b)(c)\\2"), staticContext)
                .evaluateAtMostOne(context);
        Item match = result.children().get(0);
        Assertions.assertEquals(2, match.children().size());
        Item first = match.children().get(0);
        Item third = match.children().get(1);
        Assertions.assertEquals("1", first.attributes().get(0).getStringValue());
        Assertions.assertEquals("2", first.children().get(0).attributes().get(0).getStringValue());
        Assertions.assertTrue(first.children().get(0).children().isEmpty());
        Assertions.assertEquals("3", third.attributes().get(0).getStringValue());
        Assertions.assertEquals("c", third.getStringValue());
    }

    @Test
    public void replacementValidationAndQuotedReplacementsArePreserved() {
        Assertions.assertThrows(
                InvalidReplacementStringException.class,
                () -> new ReplaceFunctionIterator(arguments("x", "z", "$"), staticContext).evaluateAtMostOne(context));
        Assertions.assertThrows(InvalidReplacementStringException.class, () -> new ReplaceFunctionIterator(
                        arguments("x", "x", "\\a"), staticContext)
                .evaluateAtMostOne(context));
        Assertions.assertEquals(
                "$1\\",
                new ReplaceFunctionIterator(arguments("x", "x", "$1\\", "q"), staticContext)
                        .evaluateAtMostOne(context)
                        .getStringValue());
    }

    @Test
    public void quotedUnicodeCaseVariantsWorkAcrossAllRegexFunctions() {
        Assertions.assertTrue(new MatchesFunctionIterator(arguments("\ufb05", "\ufb06", "iq"), staticContext)
                .evaluateAtMostOne(context)
                .getBooleanValue());
        Assertions.assertFalse(new MatchesFunctionIterator(arguments("\u0130", "i", "iq"), staticContext)
                .evaluateAtMostOne(context)
                .getBooleanValue());
        Assertions.assertEquals(
                "$1",
                new ReplaceFunctionIterator(arguments("\ufb05", "\ufb06", "$1", "iq"), staticContext)
                        .evaluateAtMostOne(context)
                        .getStringValue());
        try (var cursor =
                new TokenizeFunctionIterator(arguments("a\ufb05b", "\ufb06", "iq"), staticContext).getCursor(context)) {
            Assertions.assertEquals("a", cursor.next().getStringValue());
            Assertions.assertEquals("b", cursor.next().getStringValue());
            Assertions.assertFalse(cursor.hasNext());
        }
        Item result = new AnalyzeStringFunctionIterator(arguments("\ufb05", "\ufb06", "iq"), staticContext)
                .evaluateAtMostOne(context);
        Assertions.assertEquals(1, result.children().size());
        Assertions.assertEquals("match", result.children().get(0).nodeName().getLocalName());
        Assertions.assertEquals("\ufb05", result.children().get(0).getStringValue());
        Assertions.assertTrue(result.children().get(0).children().get(0).isTextNode());
    }
}
