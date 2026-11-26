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

import java.io.Serial;
import java.util.List;
import java.util.regex.Matcher;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidReplacementStringException;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ReplaceFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReplaceFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item stringItem = this.getChild(0).materializeFirstOrNull(context);
        Item patternStringItem = this.getChild(1).materializeFirstOrNull(context);

        if (patternStringItem == null) {
            return null;
        }
        String pattern = patternStringItem.getStringValue();
        String flags = null;
        if (this.getChildren().size() == 4) {
            Item flagsItem = this.getChild(3).materializeFirstOrNull(context);
            if (flagsItem != null) {
                flags = flagsItem.getStringValue();
            }
        }
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
                pattern, flags, getMetadata(), getConfiguration().semantics().xsdVersion());
        if (RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern())) {
            throw new MatchesEmptyStringException(
                    "'" + compiledRegex.getEffectivePattern() + "' matches empty string", getMetadata());
        }

        Item replacementStringItem = this.getChild(2).materializeFirstOrNull(context);
        String replacement = replacementStringItem.getStringValue();

        String input;
        if (stringItem == null) {
            input = "";
        } else {
            input = stringItem.getStringValue();
        }

        Matcher matcher = compiledRegex.getPattern().matcher(input);
        String javaReplacement = compiledRegex.isQuote()
                ? Matcher.quoteReplacement(replacement)
                : translateReplacement(replacement, matcher.groupCount());
        return ItemFactory.getInstance().createStringItem(matcher.replaceAll(javaReplacement));
    }

    private String translateReplacement(String replacement, int groupCount) {
        StringBuilder result = new StringBuilder(replacement.length());
        int maxReference = Math.max(9, groupCount);
        for (int i = 0; i < replacement.length(); i++) {
            char current = replacement.charAt(i);
            if (current == '\\' && i + 1 < replacement.length()
                    && (replacement.charAt(i + 1) == '\\' || replacement.charAt(i + 1) == '$')) {
                result.append(current).append(replacement.charAt(++i));
                continue;
            }
            if (current == '\\' || (current == '$'
                    && (i + 1 == replacement.length() || !isDigit(replacement.charAt(i + 1))))) {
                throw new InvalidReplacementStringException(
                        "'" + replacement + "' contains a disallowed sequence of characters", getMetadata());
            }
            if (current != '$') {
                result.append(current);
                continue;
            }

            int reference = 0;
            while (i + 1 < replacement.length() && isDigit(replacement.charAt(i + 1))) {
                long next = 10L * reference + replacement.charAt(i + 1) - '0';
                if (next > maxReference) {
                    break;
                }
                reference = (int) next;
                i++;
            }
            if (reference <= groupCount) {
                result.append('$').append(reference);
            }
            // Escape literal trailing digits so Java cannot absorb them into a group reference.
            while (i + 1 < replacement.length() && isDigit(replacement.charAt(i + 1))) {
                result.append('\\').append(replacement.charAt(++i));
            }
        }
        return result.toString();
    }

    private static boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }
}
