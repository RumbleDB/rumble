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
package org.rumbledb.runtime.functions.xml;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class LangFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public LangFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item testlangItem = this.getChild(0).materializeFirstOrNull(context);
        String testlang = testlangItem == null ? "" : testlangItem.getStringValue();

        Item node = this.getChildren().size() == 2
                ? this.getChild(1).materializeFirstOrNull(context)
                : context.getVariableValues()
                        .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                        .get(0);
        if (node == null || !node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:lang must be a node", getMetadata());
        }

        Item current = node.isElementNode() ? node : node.parent();
        while (current != null && current.isElementNode()) {
            for (Item attribute : current.attributes()) {
                Name name = attribute.nodeName();
                if (name != null && Name.XML_NS.equals(name.getNamespace()) && "lang".equals(name.getLocalName())) {
                    return ItemFactory.getInstance()
                            .createBooleanItem(matchesLanguage(attribute.getStringValue(), testlang));
                }
            }
            current = current.parent();
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }

    private static boolean matchesLanguage(String lang, String testlang) {
        if (lang.equalsIgnoreCase(testlang)) {
            return true;
        }
        return lang.length() > testlang.length()
                && lang.charAt(testlang.length()) == '-'
                && lang.regionMatches(true, 0, testlang, 0, testlang.length());
    }
}
