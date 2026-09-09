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
package org.rumbledb.runtime.typing;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/** Local castability test for an imported XML Schema simple type. */
public final class XmlSchemaCastableIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan operand;
    private final Name targetTypeName;
    private final boolean allowsEmptyInput;

    /** Xerces grammars are intentionally local-only and are not serialized with a runtime plan. */
    private final transient XmlSchemaCatalog schemaCatalog;

    public XmlSchemaCastableIterator(
            ItemRuntimePlan operand,
            Name targetTypeName,
            boolean allowsEmptyInput,
            XmlSchemaCatalog schemaCatalog,
            RuntimeStaticContext staticContext) {
        super(List.of(operand), staticContext);
        this.operand = operand;
        this.targetTypeName = targetTypeName;
        this.allowsEmptyInput = allowsEmptyInput;
        this.schemaCatalog = schemaCatalog;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> atomizedValue =
                XmlSchemaCastSupport.materializeAtomizedAtMostTwo(this.operand, context, getMetadata());
        if (atomizedValue.size() > 1) {
            return booleanItem(false);
        }
        if (atomizedValue.isEmpty()) {
            return booleanItem(this.allowsEmptyInput);
        }
        if (this.schemaCatalog == null) {
            throw new OurBadException(
                    "The local XML Schema catalog is unavailable to the castable runtime plan.", getMetadata());
        }
        try {
            this.schemaCatalog.castSimpleType(
                    this.targetTypeName,
                    atomizedValue.get(0),
                    NamespaceBindingUtils.namespaceResolver(this.staticContext),
                    getMetadata());
            return booleanItem(true);
        } catch (OurBadException exception) {
            throw exception;
        } catch (RumbleException exception) {
            return booleanItem(false);
        }
    }

    private static Item booleanItem(boolean value) {
        return ItemFactory.getInstance().createBooleanItem(value);
    }
}
