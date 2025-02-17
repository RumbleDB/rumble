/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.numerics.exponential;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class PowFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public PowFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item base = this.children.get(0).materializeFirstItemOrNull(context);
        if (base == null) {
            return null;
        }
        Item exponent = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (exponent == null) {
            return null;
        }
        try {
            double baseDouble = base.castToDoubleValue();
            double exponentDouble = exponent.castToDoubleValue();

            // special cases where java doesnt implement IEEE754 standard
            // 1 or -1 to the power of INF or -INF should be 1
            if (Math.abs(baseDouble) == 1 && Double.isInfinite(exponentDouble)) {
                return ItemFactory.getInstance().createDoubleItem(1);
            }
            // 1 (but not -1!) to the power of NaN should be 1
            if (baseDouble == 1 && Double.isNaN(exponentDouble)) {
                return ItemFactory.getInstance().createDoubleItem(1);
            }

            return ItemFactory.getInstance()
                .createDoubleItem(Math.pow(base.castToDoubleValue(), exponent.castToDoubleValue()));
        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }

    }


}
