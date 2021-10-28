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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidNormalizationException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NormalizeUnicodeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private static final HashSet<Integer> exclusionCharacters = new HashSet<Integer>(
            Arrays.asList(
                0x00000958,
                0x00000959,
                0x0000095A,
                0x0000095B,
                0x0000095C,
                0x0000095D,
                0x0000095E,
                0x0000095F,
                0x000009DC,
                0x000009DD,
                0x000009DF,
                0x00000A33,
                0x00000A36,
                0x00000A59,
                0x00000A5A,
                0x00000A5B,
                0x00000A5E,
                0x00000B5C,
                0x00000B5D,
                0x00000F43,
                0x00000F4D,
                0x00000F52,
                0x00000F57,
                0x00000F5C,
                0x00000F69,
                0x00000F76,
                0x00000F78,
                0x00000F93,
                0x00000F9D,
                0x00000FA2,
                0x00000FA7,
                0x00000FAC,
                0x00000FB9,
                0x0000FB1D,
                0x0000FB1F,
                0x0000FB2A,
                0x0000FB2B,
                0x0000FB2C,
                0x0000FB2D,
                0x0000FB2E,
                0x0000FB2F,
                0x0000FB30,
                0x0000FB31,
                0x0000FB32,
                0x0000FB33,
                0x0000FB34,
                0x0000FB35,
                0x0000FB36,
                0x0000FB38,
                0x0000FB39,
                0x0000FB3A,
                0x0000FB3B,
                0x0000FB3C,
                0x0000FB3E,
                0x0000FB40,
                0x0000FB41,
                0x0000FB43,
                0x0000FB44,
                0x0000FB46,
                0x0000FB47,
                0x0000FB48,
                0x0000FB49,
                0x0000FB4A,
                0x0000FB4B,
                0x0000FB4C,
                0x0000FB4D,
                0x0000FB4E,
                0x00002ADC,
                0x0001D15E,
                0x0001D15F,
                0x0001D160,
                0x0001D161,
                0x0001D162,
                0x0001D163,
                0x0001D164,
                0x0001D1BB,
                0x0001D1BC,
                0x0001D1BD,
                0x0001D1BE,
                0x0001D1BF,
                0x0001D1C0
            )
            // https://www.unicode.org/Public/UCD/latest/ucd/CompositionExclusions.txt
    );

    public NormalizeUnicodeFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        boolean fullyNormalized = false;
        Normalizer.Form normalizationForm = Normalizer.Form.NFC;
        Item inputItem = this.children.get(0)
            .materializeFirstItemOrNull(context);

        if (inputItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }

        if (this.children.size() > 1) {
            Item normalizationFormItem = this.children.get(1)
                .materializeFirstItemOrNull(context);

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
                fullyNormalized = true;
                normalizationForm = Normalizer.Form.NFC;
            } else {
                throw new InvalidNormalizationException(
                        normalizationFormRaw + " is not a valid normalization form",
                        getMetadata()
                );
            }
        }

        String input = inputItem.getStringValue();
        if (fullyNormalized && exclusionCharacters.contains(input.codePointAt(0))) {
            input = " " + input;
        }
        String normalizedString = Normalizer.normalize(input, normalizationForm);
        return ItemFactory.getInstance().createStringItem(normalizedString);
    }

}
