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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ContainsTokenFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ContainsTokenFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        String collation = CollationSupport.resolveCollationArgument(
                this.getChildren().size() > 2 ? this.getChild(2) : null,
                context,
                getRuntimeStaticContext(),
                getMetadata());
        String token = getNormalizedToken(context);
        if (token.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        return ItemFactory.getInstance().createBooleanItem(cursorContainsToken(context, token, collation));
    }

    private String getNormalizedToken(DynamicContext context) {
        Item tokenItem = this.getChild(1).materializeFirstOrNull(context);
        return trimXmlWhitespace(tokenItem.getStringValue());
    }

    private static String trimXmlWhitespace(String value) {
        int start = 0;
        int end = value.length();
        while (start < end && isXmlWhitespace(value.charAt(start))) {
            start++;
        }
        while (end > start && isXmlWhitespace(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(start, end);
    }

    private static boolean isXmlWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n' || character == '\r';
    }

    private boolean cursorContainsToken(DynamicContext context, String token, String collation) {
        try (Cursor<Item> cursor = this.getChild(0).getCursor(context)) {
            while (cursor.hasNext()) {
                String[] inputTokens = cursor.next().getStringValue().split("[\\t\\n\\r ]+");
                if (isTokenInSequence(inputTokens, token, collation)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isTokenInSequence(String[] inputTokens, String token, String collation) {
        for (String inputToken : inputTokens) {
            if (CollationSupport.compareStrings(inputToken, token, collation, getMetadata()) == 0) {
                return true;
            }
        }
        return false;
    }
}
