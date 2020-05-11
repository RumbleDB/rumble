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

package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidNormalizationException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.text.Normalizer;
import java.util.List;

public class NormalizeUnicodeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public NormalizeUnicodeFunctionIterator(
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
            Normalizer.Form normalizationForm = Normalizer.Form.NFC;

            Item inputItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

            if (inputItem == null) {
                return ItemFactory.getInstance().createStringItem("");
            }

            if (this.children.size() > 1) {
                Item normalizationFormItem = this.children.get(1)
                    .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

                String normalizationFormRaw = normalizationFormItem.getStringValue();
                if (normalizationFormRaw.length() == 0) {
                    return inputItem;
                }

                if (
                    normalizationFormRaw.equals("NFC")
                        || normalizationFormRaw.equals("NFD")
                        || normalizationFormRaw.equals("NFKC")
                        || normalizationFormRaw.equals("NFKD")
                ) {
                    normalizationForm = Normalizer.Form.valueOf(normalizationFormItem.getStringValue());
                } else if (normalizationFormRaw.equals("FULLY-NORMALIZED")) {
                    normalizationForm = Normalizer.Form.NFC;
                } else {
                    throw new InvalidNormalizationException(
                            normalizationFormRaw + " is not a valid normalization form",
                            getMetadata()
                    );
                }
            }

            String normalizedString = Normalizer.normalize(inputItem.getStringValue(), normalizationForm);
            return ItemFactory.getInstance().createStringItem(normalizedString);

        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " translate function",
                    getMetadata()
            );
    }
}
