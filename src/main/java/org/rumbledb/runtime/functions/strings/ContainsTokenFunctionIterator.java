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
import java.util.Comparator;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ContainsTokenFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ContainsTokenFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        String token = getNormalizedToken(context);
        if (token.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        Comparator<String> comparator = resolveComparator(context);
        return ItemFactory.getInstance().createBooleanItem(cursorContainsToken(context, token, comparator));
    }

    private Comparator<String> resolveComparator(DynamicContext context) {
        if (this.getChildren().size() < 3) {
            return null;
        }
        Item collationItem = this.getChild(2).materializeFirstOrNull(context);
        String collationUri = collationItem == null ? null : collationItem.getStringValue();
        return CollationResolver.resolve(collationUri, getMetadata());
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

    private boolean cursorContainsToken(DynamicContext context, String token, Comparator<String> comparator) {
        try (Cursor<Item> cursor = this.getChild(0).getCursor(context)) {
            while (cursor.hasNext()) {
                String[] inputTokens = cursor.next().getStringValue().split("[\\t\\n\\r ]+");
                if (isTokenInSequence(inputTokens, token, comparator)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isTokenInSequence(String[] inputTokens, String token, Comparator<String> comparator) {
        for (String inputToken : inputTokens) {
            boolean matches =
                    comparator == null ? inputToken.equals(token) : comparator.compare(inputToken, token) == 0;
            if (matches) {
                return true;
            }
        }
        return false;
    }
}
