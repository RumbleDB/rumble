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

import org.rumbledb.expressions.Node;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;

/** Typing rules of JSONiq object and array lookups, array unboxing and XQuery ? lookups. */
public final class LookupTyping {

    private final StaticTypeErrors errors;

    public LookupTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferArrayLookupType(SequenceType mainType, SequenceType lookupType, Node node) {
        SequenceType integer = SequenceType.createSequenceType("integer");
        if (!lookupType.isSubtypeOf(integer)) {
            this.errors.reportMismatch(
                    "the lookup expression type must match integer, instead " + lookupType + " was inferred",
                    lookupType,
                    integer,
                    node);
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            this.errors.reportInferredEmpty(node);
        }

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
                    "the lookup expression type must be castable to string (i.e. must match atomic), instead "
                            + lookupType
                            + " was inferred",
                    lookupType,
                    atomic,
                    node);
        }

        boolean overlapsObject = mainType.hasOverlapWith(SequenceType.createSequenceType("object*"));
        boolean overlapsMap = mainType.hasOverlapWith(SequenceType.createSequenceType("map*"));
        if ((!overlapsObject && !overlapsMap) || mainType.isEmptySequence()) {
            this.errors.reportInferredEmpty(node);
        }

        SequenceType result = inferObjectSelectionType(mainType.getItemType(), mainType.getArity(), key);
        this.errors.rejectInferredEmpty(result, node);
        return result;
    }

    /** The items that an XQuery lookup can select from. */
    private static final List<SequenceType> LOOKUP_INPUTS = List.of(
            new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, SequenceCardinality.ANY),
            new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceCardinality.ANY));

    /**
     * Infers an XQuery lookup E?K, or E?* when wildcard (XQuery 3.1, 3.11.3.1). Each input item contributes
     * separately: a map the value of its key K, which may be absent, or all its values; an array its member at
     * position K, or all its members.
     */
    public SequenceType inferPostfixLookupType(SequenceType mainType, boolean wildcard, Node node) {
        if (mainType.isEmptySequence() || LOOKUP_INPUTS.stream().noneMatch(mainType::hasOverlapWith)) {
            this.errors.reportInferredEmpty(node);
            return SequenceType.createSequenceType("item*");
        }
        SequenceType perItem = null;
        for (ItemType member : mainType.getItemType().getMemberTypes()) {
            SequenceType memberType = inferItemLookupType(member, wildcard);
            perItem = perItem == null ? memberType : perItem.leastCommonSupertypeWith(memberType);
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
        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            this.errors.reportInferredEmpty(node);
        }
        ItemType contentType = inferArrayContentType(mainType.getItemType());
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
    private ItemType inferArrayContentType(ItemType itemType) {
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
