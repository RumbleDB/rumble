package org.rumbledb.types;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;

import java.util.Arrays;
import java.util.List;

public class BuiltinTypesCatalogue {
    public static final ItemType item = ItemItemType.item;
    public static final ItemType atomicItem = AtomicItemType.atomicItem;
    public static final ItemType stringItem = AtomicItemType.stringItem;
    public static final ItemType integerItem = DerivedAtomicItemType.integerItem;
    public static final ItemType numericItem = AtomicItemType.numericItem;
    public static final ItemType decimalItem = AtomicItemType.decimalItem;
    public static final ItemType doubleItem = AtomicItemType.doubleItem;
    public static final ItemType floatItem = AtomicItemType.floatItem;
    public static final ItemType booleanItem = AtomicItemType.booleanItem;
    public static final ItemType nullItem = AtomicItemType.nullItem;
    public static final ItemType durationItem = AtomicItemType.durationItem;
    public static final ItemType yearMonthDurationItem = AtomicItemType.yearMonthDurationItem;
    public static final ItemType dayTimeDurationItem = AtomicItemType.dayTimeDurationItem;
    public static final ItemType dateTimeItem = AtomicItemType.dateTimeItem;
    public static final ItemType dateItem = AtomicItemType.dateItem;
    public static final ItemType timeItem = AtomicItemType.timeItem;
    public static final ItemType hexBinaryItem = AtomicItemType.hexBinaryItem;
    public static final ItemType anyURIItem = AtomicItemType.anyURIItem;
    public static final ItemType base64BinaryItem = AtomicItemType.base64BinaryItem;
    public static final ItemType JSONItem = JsonItemType.jsonItem;
    public static final ItemType objectItem = ObjectItemType.anyObjectItem;
    public static final ItemType arrayItem = ArrayItemType.anyArrayItem;
    public static final ItemType longItem = DerivedAtomicItemType.longItem;
    public static final ItemType intItem = DerivedAtomicItemType.intItem;
    public static final ItemType shortItem = DerivedAtomicItemType.shortItem;
    public static final ItemType anyFunctionItem = FunctionItemType.anyFunctionItem;

    public static boolean typeExists(Name name) {
        for (ItemType builtInItemType : builtInItemTypes) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemType.getName().getLocalName().equals(name.getLocalName())) {
                    return true;
                }
            } else {
                if (builtInItemType.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final List<ItemType> builtInItemTypes = Arrays.asList(
        objectItem,
        atomicItem,
        stringItem,
        integerItem,
        intItem,
        decimalItem,
        doubleItem,
        floatItem,
        numericItem,
        booleanItem,
        arrayItem,
        nullItem,
        JSONItem,
        durationItem,
        yearMonthDurationItem,
        dayTimeDurationItem,
        dateTimeItem,
        dateItem,
        timeItem,
        hexBinaryItem,
        anyURIItem,
        base64BinaryItem,
        item,
        longItem,
        shortItem
    );

    public static ItemType getItemTypeByName(Name name) {
        for (ItemType builtInItemType : builtInItemTypes) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemType.getName().getLocalName().equals(name.getLocalName())) {
                    return builtInItemType;
                }
            } else {
                if (builtInItemType.getName().equals(name)) {
                    return builtInItemType;
                }
            }
        }
        throw new OurBadException("Type unrecognized: " + name + "(namespace: " + name.getNamespace() + ")");
    }
}
