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

package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class FormatNumberFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public FormatNumberFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item integerItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            if (integerItem == null) {
                return ItemFactory.getInstance().createStringItem("");
            }

            double source = integerItem.getDoubleValue();
            boolean isNegative = false;
            if (source < 0) {
                isNegative = true;
                source = Math.abs(source);
            }

            Item pictureItem = this.children.get(1)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            String picture = pictureItem.getStringValue();

            NumberFormat nf = NumberFormat.getInstance();
            if (nf instanceof DecimalFormat) {
                ((DecimalFormat) nf).applyPattern(picture);
            } else {
                throw new OurBadException("NumberFormat is not of subtype DecimalFormat", getMetadata());
            }

            String result = nf.format(source);
            if (isNegative) {
                result = "-" + result;
            }
            return ItemFactory.getInstance().createStringItem(result);
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " pi function", getMetadata());
        }
    }
}
