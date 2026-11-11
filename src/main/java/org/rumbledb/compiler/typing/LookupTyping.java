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
package org.rumbledb.compiler.typing;

import java.util.List;
import java.util.stream.Collectors;

import org.rumbledb.expressions.Node;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Typing rules of JSONiq object and array lookups, array unboxing and XQuery ? lookups. */
public final class LookupTyping {

    /** How many keys of an object type an error for a missing key lists before summarizing the rest. */
    private static final int MAX_LISTED_KEYS = 10;

    private final StaticTypeErrors errors;

    public LookupTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferArrayLookupType(SequenceType mainType, SequenceType lookupType, Node node) {
        SequenceType integer = SequenceType.createSequenceType("integer");
        if (!lookupType.isSubtypeOf(integer)) {
            this.errors.reportMismatch(
                    "An array lookup expects an xs:integer position, but its key is "
                            + TypeNotation.shortened(lookupType)
                            + ".",
                    lookupType,
                    integer,
                    node);
        }

        rejectInput(mainType, mainType.hasOverlapWith(SequenceType.createSequenceType("array*")), "an array", node);

        SequenceType.Arity inferredArity = mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        ItemType resultItemType = BuiltinTypesCatalogue.item;
        ItemType itemType = mainType.getItemType();
        ItemType arrayContentType = inferArrayContentType(itemType);
        if (arrayContentType != null) {
            resultItemType = arrayContentType;
        }
        return new SequenceType(resultItemType, inferredArity);
    }

    public SequenceType inferObjectLookupType(SequenceType mainType, SequenceType lookupType, String key, Node node) {
        // must be castable to string
        SequenceType atomic = SequenceType.createSequenceType("anyAtomicType");
        if (!lookupType.isSubtypeOf(atomic)) {
            this.errors.reportMismatch(
                    "A lookup expects an atomic key, but its key is " + TypeNotation.shortened(lookupType) + ".",
                    lookupType,
                    atomic,
                    node);
        }

        boolean overlapsObject = mainType.hasOverlapWith(SequenceType.createSequenceType("object*"));
        boolean overlapsMap = mainType.hasOverlapWith(SequenceType.createSequenceType("map*"));
        rejectInput(mainType, overlapsObject || overlapsMap, "an object or a map", node);

        SequenceType result = inferObjectSelectionType(mainType.getItemType(), mainType.getArity(), key);
        if (result.isEmptySequence()) {
            this.errors.reportInferredEmpty(missingKeyMessage(mainType, key), node);
        }
        return result;
    }

    /** Explains a lookup that selects nothing, listing the keys that an object type has instead. */
    private static String missingKeyMessage(SequenceType inputType, String key) {
        ItemType itemType = inputType.getItemType();
        List<String> keys = key != null && itemType.isObjectItemType()
                ? itemType.getObjectContentFacet().stream()
                        .map(FieldDescriptor::getName)
                        .toList()
                : List.of();
        if (keys.isEmpty()) {
            return TypeErrorMessages.inferredEmpty("its input, of type "
                            + TypeNotation.shortened(inputType)
                            + ", never has the key"
                            + (key == null ? "" : " \"" + key + "\""))
                    .text();
        }
        String closest = closestKey(key, keys);
        return TypeErrorMessages.inferredEmpty("its input never has the key \"" + key + "\"")
                        .text()
                + (closest == null ? "" : " Did you mean \"" + closest + "\"?")
                + (keys.size() == 1 ? " It has the key " : " It has the keys ")
                + keys.stream().limit(MAX_LISTED_KEYS).collect(Collectors.joining(", "))
                + (keys.size() > MAX_LISTED_KEYS ? ", and " + (keys.size() - MAX_LISTED_KEYS) + " more." : ".");
    }

    /** The key that a misspelled one most likely stands for, or null if none is close to it. */
    private static String closestKey(String key, List<String> keys) {
        String closest = null;
        // A key that differs in more than a third of its characters is another word.
        int closestDistance = Math.max(1, key.length() / 3) + 1;
        for (String candidate : keys) {
            int distance = editDistance(key.toLowerCase(), candidate.toLowerCase());
            if (distance < closestDistance) {
                closest = candidate;
                closestDistance = distance;
            }
        }
        return closest;
    }

    /** How many characters have to be inserted, deleted or replaced to turn one string into the other. */
    private static int editDistance(String first, String second) {
        int[] distances = new int[second.length() + 1];
        for (int j = 0; j <= second.length(); j++) {
            distances[j] = j;
        }
        for (int i = 1; i <= first.length(); i++) {
            int diagonal = distances[0];
            distances[0] = i;
            for (int j = 1; j <= second.length(); j++) {
                int replaced = diagonal + (first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1);
                diagonal = distances[j];
                distances[j] = Math.min(replaced, Math.min(distances[j], distances[j - 1]) + 1);
            }
        }
        return distances[second.length()];
    }

    /** The items that an XQuery lookup can select from. */
    private static final List<SequenceType> LOOKUP_INPUTS = List.of(
            new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceCardinality.ANY));

    /**
     * Infers an XQuery lookup E?K, or E?* when the key type is null (XQuery 3.1, 3.11.3.1). Each input item
     * contributes separately: a map the value of each key in K, which may be absent, or all its values; an array its
     * member at each position in K, or all its members.
     */
    public SequenceType inferPostfixLookupType(SequenceType mainType, SequenceType keyType, Node node) {
        if (rejectInput(
                mainType, LOOKUP_INPUTS.stream().anyMatch(mainType::hasOverlapWith), "a map or an array", node)) {
            return SequenceType.createSequenceType("item*");
        }
        boolean wildcard = keyType == null;
        // A type always has a member; the empty sequence only stands in for none.
        SequenceType perItem = mainType.getItemType().getMemberTypes().stream()
                .map(member -> inferItemLookupType(member, wildcard))
                .reduce(SequenceType::leastCommonSupertypeWith)
                .orElse(SequenceType.createSequenceType("()"));
        if (!wildcard) {
            // Each atomized key selects separately, so E?(1 to 2) gives a value for each of the two keys.
            perItem = perItem.repeated(TypeAtomization.inferType(keyType).getCardinality());
        }
        return perItem.repeated(mainType.getCardinality());
    }

    /** The value of one key of a map, which may be absent from it. */
    private static SequenceType mapValueOfKey(ItemType mapType) {
        SequenceType value = mapType.getMapValueSequenceType();
        return value.isEmptySequence()
                ? value
                : new SequenceType(value.getItemType(), value.getCardinality().union(SequenceCardinality.EMPTY));
    }

    /** Whether a dynamic call can select from this item, like a lookup of one key. */
    public static boolean isCallableLookupInput(ItemType itemType) {
        return itemType.isMapItemType() || itemType.isArrayItemType() || itemType.isXQueryArrayItemType();
    }

    /** Infers a dynamic call $m(K) of a map or an array, which selects what the lookup $m?K does. */
    public static SequenceType inferLookupCallType(ItemType itemType) {
        return inferItemLookupType(itemType, false);
    }

    private static SequenceType inferItemLookupType(ItemType itemType, boolean wildcard) {
        if (itemType.isMapItemType()) {
            return wildcard
                    ? itemType.getMapValueSequenceType().repeated(SequenceCardinality.ANY)
                    : mapValueOfKey(itemType);
        }
        SequenceType member = null;
        if (itemType.isXQueryArrayItemType()) {
            member = itemType.getMemberSequenceType();
        } else if (itemType.isArrayItemType()) {
            member = new SequenceType(itemType.getArrayContentFacet());
        }
        if (member == null) {
            return SequenceType.createSequenceType("item*");
        }
        // A position outside the array is an error, not the empty sequence.
        return wildcard ? member.repeated(SequenceCardinality.ANY) : member;
    }

    public SequenceType inferArrayUnboxingType(SequenceType mainType, Node node) {
        rejectInput(mainType, mainType.hasOverlapWith(SequenceType.createSequenceType("array*")), "an array", node);
        return inferArrayMembersType(mainType);
    }

    /**
     * Reports a lookup whose input is empty, or never has an item it selects from, the kinds of which are named, such
     * as "an array". Returns whether it reported it.
     */
    private boolean rejectInput(SequenceType inputType, boolean selectable, String kinds, Node node) {
        if (inputType.isEmptySequence()) {
            this.errors.reportInferredEmpty(node, "its input does");
            return true;
        }
        if (!selectable) {
            this.errors.reportInferredEmpty(
                    node, "its input, of type " + TypeNotation.shortened(inputType) + ", is never " + kinds);
            return true;
        }
        return false;
    }

    /** Infers E[], or members(E): the members of the arrays among the items of E. */
    public static SequenceType inferArrayMembersType(SequenceType type) {
        ItemType contentType = inferArrayContentType(type.getItemType());
        if (contentType != null) {
            return new SequenceType(contentType, SequenceType.Arity.ZeroOrMore);
        }
        return SequenceType.createSequenceType("item*");
    }

    /**
     * Infers JSONiq dot selection separately for each possible input type.
     * For example, selecting n from ({n: integer} | null) yields integer?:
     * the object branch yields an integer, while the null branch yields the empty sequence.
     * A null key here denotes an node whose string value is not known statically.
     */
    private SequenceType inferObjectSelectionType(ItemType itemType, SequenceType.Arity inputArity, String key) {
        if (itemType.isUnionType()) {
            SequenceType result = null;
            for (ItemType member : itemType.getTypes()) {
                SequenceType memberResult = inferObjectSelectionType(member, inputArity, key);
                // Include empty branches: they make a required field's lookup result optional.
                result = result == null ? memberResult : result.leastCommonSupertypeWith(memberResult);
            }
            return result == null ? SequenceType.createSequenceType("()") : result;
        }
        if (itemType.isMapItemType()) {
            return mapValueOfKey(itemType).repeated(SequenceCardinality.fromArity(inputArity));
        }
        SequenceType.Arity outputArity = inputArity.isSubtypeOf(SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        if (itemType.isObjectItemType() && key != null) {
            FieldDescriptor field = itemType.getObjectContentFacet(key);
            if (field != null) {
                return new SequenceType(field.getType(), field.isRequired() ? inputArity : outputArity);
            }
            if (itemType.getClosedFacet()) {
                return SequenceType.createSequenceType("()");
            }
        }
        SequenceType type = new SequenceType(itemType, inputArity);
        if (!type.hasOverlapWith(SequenceType.createSequenceType("object*"))
                && !type.hasOverlapWith(SequenceType.createSequenceType("map*"))) {
            // JSONiq object selection ignores nonobjects, including null.
            return SequenceType.createSequenceType("()");
        }
        return new SequenceType(BuiltinTypesCatalogue.item, outputArity);
    }

    /**
     * Combines content types of possible arrays for JSONiq lookup and unboxing.
     * Nonarray alternatives contribute no results. Returns Java null when no array is possible;
     * a broad type that could contain arrays instead contributes item.
     */
    private static ItemType inferArrayContentType(ItemType itemType) {
        if (itemType.isUnionType()) {
            ItemType result = null;
            for (ItemType member : itemType.getTypes()) {
                ItemType memberContent = inferArrayContentType(member);
                if (memberContent != null) {
                    result = result == null ? memberContent : result.findLeastCommonSuperTypeWith(memberContent);
                }
            }
            return result;
        }
        if (itemType.isArrayItemType()) {
            return itemType.getArrayContentFacet();
        }
        return BuiltinTypesCatalogue.arrayItem.isSubtypeOf(itemType) ? BuiltinTypesCatalogue.item : null;
    }
}
