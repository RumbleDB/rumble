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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class GenerateIdFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public GenerateIdFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:generate-id must be a node", getMetadata());
        }
        return ItemFactory.getInstance().createStringItem(generateId(node));
    }

    private static String generateId(Item node) {
        XMLDocumentPosition position = node.getXmlDocumentPosition();
        if (position == null) {
            return "N" + Long.toUnsignedString(System.identityHashCode(node), 36);
        }
        BigInteger pathValue = new BigInteger(1, position.getPath().getBytes(StandardCharsets.UTF_8));
        return "N" + pathValue.toString(36) + "P" + Integer.toUnsignedString(position.getDocPosition(), 36);
    }

    private Item getContextNode(DynamicContext context) {
        if (this.getChildren().size() == 1) {
            return this.getChild(0).materializeFirstOrNull(context);
        }
        return context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
    }
}
