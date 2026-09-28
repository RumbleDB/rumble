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
        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(pattern, flags, getMetadata());
        if (RegexPatternUtils.matchesEmptyString(compiledRegex.pattern())) {
            throw new MatchesEmptyStringException(
                    "'" + compiledRegex.effectivePattern() + "' matches empty string", getMetadata());
        }

        Item replacementStringItem = this.getChild(2).materializeFirstOrNull(context);
        String replacement = replacementStringItem.getStringValue();
        replacement = compiledRegex.replacement(replacement, getMetadata());

        String input;
        if (stringItem == null) {
            input = "";
        } else {
            input = stringItem.getStringValue();
        }

        Matcher m = compiledRegex.pattern().matcher(input);
        return ItemFactory.getInstance().createStringItem(m.replaceAll(replacement));
    }
}
