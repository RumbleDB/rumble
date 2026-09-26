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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class IRIToURIFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public IRIToURIFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item inputItem = this.getChild(0).materializeFirstOrNull(context);
        if (inputItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (!(inputItem.isString() || inputItem.isAnyURI() || inputItem.isUntypedAtomic())) {
            throw new UnexpectedTypeException(
                    "fn:iri-to-uri expects a string, xs:anyURI, or xs:untypedAtomic argument [err:XPTY0004].",
                    getMetadata());
        }
        return ItemFactory.getInstance().createStringItem(IriUtils.encodeIri(inputItem.getStringValue()));
    }

    public static String encodeIri(String value) {
        return IriUtils.encodeIri(value);
    }
}
