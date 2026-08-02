package org.rumbledb.runtime.typing;

import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

public final class TypeInferrenceUtils {

    public enum TypeMergeMode {
        STRICT,
        LAX
    }

    private TypeInferrenceUtils() {
    }

    public static ItemType inferItemTypeOfRDDItems(
            JavaRDD<Item> itemRDD,
            ExceptionMetadata metadata,
            TypeMergeMode typeMergeMode
    ) {
        if (itemRDD.isEmpty()) {
            return BuiltinTypesCatalogue.item;
        }

        ItemType neutralElement = BuiltinTypesCatalogue.errorItem;

        return itemRDD.aggregate(
            neutralElement,
            (ItemType acc, Item item) -> {
                ItemType itemType = ItemTypeFactory.createItemTypeFromItem(item);
                return acc.equals(neutralElement) ? itemType : mergeItemTypes(acc, itemType, typeMergeMode);
            },
            (ItemType a, ItemType b) -> {
                if (a.equals(neutralElement)) {
                    return b;
                }
                if (b.equals(neutralElement)) {
                    return a;
                }
                return mergeItemTypes(a, b, typeMergeMode);
            }
        );
    }

    public static ItemType inferItemTypeOfLocalItems(
            List<Item> items,
            ExceptionMetadata metadata,
            TypeMergeMode typeMergeMode
    ) {
        if (items.isEmpty()) {
            return BuiltinTypesCatalogue.item;
        }

        ItemType result = ItemTypeFactory.createItemTypeFromItem(items.get(0));
        for (int i = 1; i < items.size(); i++) {
            result = mergeItemTypes(result, ItemTypeFactory.createItemTypeFromItem(items.get(i)), typeMergeMode);
        }

        return result;
    }

    public static SequenceType inferSequenceTypeOfLocalItems(
            List<Item> items,
            TypeMergeMode typeMergeMode
    ) {
        if (items.isEmpty()) {
            return SequenceType.createSequenceType("()");
        }
        ItemType itemType = inferItemTypeOfLocalItems(items, ExceptionMetadata.EMPTY_METADATA, typeMergeMode);
        if (items.size() == 1) {
            return new SequenceType(itemType, SequenceType.Arity.One);
        }
        return new SequenceType(itemType, SequenceType.Arity.OneOrMore);
    }

    public static SequenceType inferSequenceTypeOfLocalItemSequences(
            List<List<Item>> sequences,
            TypeMergeMode typeMergeMode
    ) {
        if (sequences.isEmpty()) {
            return SequenceType.createSequenceType("item*");
        }
        SequenceType result = inferSequenceTypeOfLocalItems(sequences.get(0), typeMergeMode);
        for (int i = 1; i < sequences.size(); i++) {
            result = mergeSequenceTypes(
                result,
                inferSequenceTypeOfLocalItems(sequences.get(i), typeMergeMode),
                typeMergeMode
            );
        }
        return result;
    }

    private static ItemType mergeItemTypes(ItemType left, ItemType right, TypeMergeMode typeMergeMode) {
        if (typeMergeMode == TypeMergeMode.LAX) {
            return left.findLeastCommonSuperTypeLax(right);
        }
        return left.findLeastCommonSuperTypeWith(right);
    }

    private static SequenceType mergeSequenceTypes(
            SequenceType left,
            SequenceType right,
            TypeMergeMode typeMergeMode
    ) {
        if (left.isEmptySequence()) {
            if (right.isEmptySequence()) {
                return left;
            }
            SequenceType.Arity resultingArity = right.getArity();
            if (resultingArity == SequenceType.Arity.One) {
                resultingArity = SequenceType.Arity.OneOrZero;
            } else if (resultingArity == SequenceType.Arity.OneOrMore) {
                resultingArity = SequenceType.Arity.ZeroOrMore;
            }
            return new SequenceType(right.getItemType(), resultingArity);
        }
        if (right.isEmptySequence()) {
            SequenceType.Arity resultingArity = left.getArity();
            if (resultingArity == SequenceType.Arity.One) {
                resultingArity = SequenceType.Arity.OneOrZero;
            } else if (resultingArity == SequenceType.Arity.OneOrMore) {
                resultingArity = SequenceType.Arity.ZeroOrMore;
            }
            return new SequenceType(left.getItemType(), resultingArity);
        }

        ItemType itemSupertype = mergeItemTypes(left.getItemType(), right.getItemType(), typeMergeMode);
        SequenceType.Arity aritySuperType = SequenceType.Arity.ZeroOrMore;
        if (left.isAritySubtypeOf(right.getArity())) {
            aritySuperType = right.getArity();
        } else if (right.isAritySubtypeOf(left.getArity())) {
            aritySuperType = left.getArity();
        }
        return new SequenceType(itemSupertype, aritySuperType);
    }
}
