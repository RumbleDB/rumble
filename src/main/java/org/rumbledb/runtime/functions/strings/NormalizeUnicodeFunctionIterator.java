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
import java.util.Locale;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UnicodeSet;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidNormalizationException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class NormalizeUnicodeFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final UnicodeSet COMPOSING_CHARACTERS = composingCharacters();

    private static UnicodeSet composingCharacters() {
        // F&O 3.1 §5.4.6: nonzero combining class, or the second character of a canonical
        // decomposition whose source character is not excluded from composition.
        UnicodeSet result = new UnicodeSet("[:^ccc=0:]");
        Normalizer2 nfc = Normalizer2.getNFCInstance();
        for (String character : new UnicodeSet("[:NFD_Quick_Check=No:]")) {
            int codePoint = character.codePointAt(0);
            if (UCharacter.hasBinaryProperty(codePoint, UProperty.FULL_COMPOSITION_EXCLUSION)) {
                continue;
            }
            String decomposition = nfc.getRawDecomposition(codePoint);
            if (decomposition != null && decomposition.codePointCount(0, decomposition.length()) >= 2) {
                result.add(decomposition.codePointAt(Character.charCount(decomposition.codePointAt(0))));
            }
        }
        return result.freeze();
    }

    public NormalizeUnicodeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        boolean fullyNormalized = false;
        Normalizer2 normalizer = Normalizer2.getNFCInstance();
        Item inputItem = this.getChild(0).materializeFirstOrNull(context);

        if (inputItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }

        if (this.getChildren().size() > 1) {
            Item normalizationFormItem = this.getChild(1).materializeFirstOrNull(context);

            String normalizationFormRaw = normalizationFormItem
                    .getStringValue()
                    .replaceAll("[\\t\\n\\r ]+", " ")
                    .replaceAll("^ | $", "")
                    .toUpperCase(Locale.ROOT);
            if (normalizationFormRaw.length() == 0) {
                return inputItem;
            }

            if (normalizationFormRaw.equals("NFC")
                    || normalizationFormRaw.equals("NFD")
                    || normalizationFormRaw.equals("NFKC")
                    || normalizationFormRaw.equals("NFKD")) {
                normalizer = switch (normalizationFormRaw) {
                    case "NFD" -> Normalizer2.getNFDInstance();
                    case "NFKC" -> Normalizer2.getNFKCInstance();
                    case "NFKD" -> Normalizer2.getNFKDInstance();
                    default -> Normalizer2.getNFCInstance();};
            } else if (normalizationFormRaw.equals("FULLY-NORMALIZED")) {
                fullyNormalized = true;
                normalizer = Normalizer2.getNFCInstance();
            } else {
                throw new InvalidNormalizationException(
                        normalizationFormRaw + " is not a valid normalization form", getMetadata());
            }
        }

        String input = inputItem.getStringValue();
        if (fullyNormalized && !input.isEmpty() && COMPOSING_CHARACTERS.contains(input.codePointAt(0))) {
            input = " " + input;
        }
        String normalizedString = normalizer.normalize(input);
        return ItemFactory.getInstance().createStringItem(normalizedString);
    }
}
