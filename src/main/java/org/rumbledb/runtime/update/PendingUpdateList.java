package org.rumbledb.runtime.update;

import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.update.primitives.*;

import java.util.*;

public class PendingUpdateList {

    private List<UpdatePrimitive> updatePrimitives;

    public PendingUpdateList() {
        this.updatePrimitives = new ArrayList<>();
    }

    public PendingUpdateList(List<UpdatePrimitive> updatePrimitives) {
        this.updatePrimitives = updatePrimitives;
    }

    public List<UpdatePrimitive> getUpdatePrimitives() {
        return updatePrimitives;
    }

    public void addUpdatePrimitive(UpdatePrimitive updatePrimitive) {
        this.updatePrimitives.add(updatePrimitive);
    }

    public void addAllUpdatePrimitives(List<UpdatePrimitive> updatePrimitives) {
        this.updatePrimitives.addAll(updatePrimitives);
    }

    public boolean isEmpty() {
        return this.updatePrimitives.isEmpty();
    }

    public int size() {
        return this.updatePrimitives.size();
    }

    public void applyUpdates() {
        for (UpdatePrimitive up : this.updatePrimitives) {
            up.apply();
        }
    }

    // public static PendingUpdateList mergeUpdates(PendingUpdateList pul1, PendingUpdateList pul2) {
    // PendingUpdateList result = new PendingUpdateList();
    // HashMap<Item, List<UpdatePrimitive>> itemToUpdateMap = new HashMap<>();
    // List<UpdatePrimitive> tempUps;
    //
    // for (UpdatePrimitive up : pul1.getUpdatePrimitives()) {
    // result.addUpdatePrimitive(up);
    // Item target = up.getTarget();
    //
    // if (!itemToUpdateMap.containsKey(target)) {
    // tempUps = new ArrayList<>();
    // } else {
    // tempUps = itemToUpdateMap.get(target);
    // }
    // tempUps.add(up);
    // itemToUpdateMap.put(target, tempUps);
    // }
    //
    // for (UpdatePrimitive up : pul2.getUpdatePrimitives()) {
    // result.addUpdatePrimitive(up);
    // Item target = up.getTarget();
    //
    // if (!itemToUpdateMap.containsKey(target)) {
    // tempUps = new ArrayList<>();
    // } else {
    // tempUps = itemToUpdateMap.get(target);
    // }
    // tempUps.add(up);
    // itemToUpdateMap.put(target, tempUps);
    // }
    //
    // for (Item target : itemToUpdateMap.keySet()) {
    // List<UpdatePrimitive> targetUpdates = itemToUpdateMap.get(target);
    //
    // // object
    // if (target.isObject()) {
    // boolean hasDeletePrimitives = targetUpdates.stream()
    // .anyMatch(u -> u instanceof DeleteFromObjectPrimitive);
    // boolean hasInsertPrimitives = targetUpdates.stream()
    // .anyMatch(u -> u instanceof InsertIntoObjectPrimitive);
    //
    // if (hasInsertPrimitives) {
    // ObjectItem sourceObject =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof InsertIntoObjectPrimitive)
    // .map(u -> ((InsertIntoObjectPrimitive) u).getSourceObject())
    // .reduce(new ObjectItem(), ObjectItem::mergeWith);
    // result.addUpdatePrimitive(new InsertIntoObjectPrimitive((ObjectItem) target, sourceObject));
    // }
    //
    // if (hasDeletePrimitives) {
    // List<StringItem> names =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof DeleteFromObjectPrimitive)
    // .flatMap(u -> ((DeleteFromObjectPrimitive) u).getNamesToRemove().stream())
    // .distinct()
    // .collect(Collectors.toList());
    // result.addUpdatePrimitive(new DeleteFromObjectPrimitive((ObjectItem) target, names));
    // continue;
    // }
    //
    // List<UpdatePrimitive> renamePrimitives =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof RenameInObjectPrimitive)
    // .collect(Collectors.toList());
    // List<StringItem> uniqueRenameSelectors =
    // renamePrimitives
    // .stream()
    // .map(u -> ((RenameInObjectPrimitive) u).getTargetName())
    // .distinct()
    // .collect(Collectors.toList());
    //
    // if (renamePrimitives.size() != uniqueRenameSelectors.size()) {
    // // TODO Throw rename on same key error jerr:JNUP0010.
    // }
    //
    // List<UpdatePrimitive> replacePrimitives =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof ReplaceInObjectPrimitive)
    // .collect(Collectors.toList());
    // List<StringItem> uniqueReplaceSelectors =
    // replacePrimitives
    // .stream()
    // .map(u -> ((ReplaceInObjectPrimitive) u).getTargetName())
    // .distinct()
    // .collect(Collectors.toList());
    //
    // if (replacePrimitives.size() != uniqueReplaceSelectors.size()) {
    // // TODO Throw replace on same key error jerr:JNUP0009.
    // }
    //
    // }
    //
    // // array
    // if (target.isArray()) {
    // boolean hasDeletePrimitives = targetUpdates.stream()
    // .anyMatch(u -> u instanceof DeleteFromArrayPrimitive);
    // boolean hasInsertPrimitives = targetUpdates.stream()
    // .anyMatch(u -> u instanceof InsertIntoArrayPrimitive);
    //
    // if (hasInsertPrimitives) {
    // HashMap<IntItem, List<Item>> locatorToInsertsMap = new HashMap<>();
    //
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof InsertIntoArrayPrimitive)
    // .map(u -> (InsertIntoArrayPrimitive) u)
    // .forEach(u -> {
    // List<Item> temp;
    // if (!locatorToInsertsMap.containsKey(u.getPositionInt())) {
    // temp = new ArrayList<>();
    // } else {
    // temp = locatorToInsertsMap.get(u.getPositionInt());
    // }
    // temp.addAll(u.getSourceSequence());
    // locatorToInsertsMap.put(u.getPositionInt(), temp);
    // });
    //
    // for (IntItem intItem : locatorToInsertsMap.keySet()) {
    // result.addUpdatePrimitive(
    // new InsertIntoArrayPrimitive(
    // (ArrayItem) target,
    // intItem,
    // locatorToInsertsMap.get(intItem)
    // )
    // );
    // }
    // }
    //
    // if (hasDeletePrimitives) {
    // List<IntItem> intItems =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof DeleteFromArrayPrimitive)
    // .map(u -> ((DeleteFromArrayPrimitive) u).getPositionInt())
    // .distinct()
    // .collect(Collectors.toList());
    // for (IntItem intItem : intItems) {
    // result.addUpdatePrimitive(new DeleteFromArrayPrimitive((ArrayItem) target, intItem));
    // }
    // continue;
    // }
    //
    // List<UpdatePrimitive> replacePrimitives =
    // targetUpdates
    // .stream()
    // .filter(u -> u instanceof ReplaceInArrayPrimitive)
    // .collect(Collectors.toList());
    // List<IntItem> uniqueReplaceSelectors =
    // replacePrimitives
    // .stream()
    // .map(u -> ((ReplaceInArrayPrimitive) u).getPositionInt())
    // .distinct()
    // .collect(Collectors.toList());
    //
    // if (replacePrimitives.size() != uniqueReplaceSelectors.size()) {
    // // TODO Throw replace on same key error jerr:JNUP0009.
    // }
    //
    // }
    // }
    //
    // return result;
    // }

    public static PendingUpdateList mergeUpdates(PendingUpdateList pul1, PendingUpdateList pul2) {
        PendingUpdateList result = new PendingUpdateList();
        HashMap<UpdatePrimitiveTarget, HashMap<UpdatePrimitiveEnum, HashMap<UpdatePrimitiveSelector, UpdatePrimitiveSource>>> itemToUpdateMap =
            new HashMap<>();
        HashMap<UpdatePrimitiveEnum, HashMap<UpdatePrimitiveSelector, UpdatePrimitiveSource>> tempEnumMap;
        HashMap<UpdatePrimitiveSelector, UpdatePrimitiveSource> tempSelectorMap;
        UpdatePrimitiveSource tempSource;
        UpdatePrimitiveSelector placeholderSelector = new UpdatePrimitiveSelector(new IntItem(0));

        for (List<UpdatePrimitive> us : Arrays.asList(pul1.getUpdatePrimitives(), pul2.getUpdatePrimitives())) {
            for (UpdatePrimitive u : us) {
                UpdatePrimitiveTarget target = u.getTarget();
                tempEnumMap = itemToUpdateMap.getOrDefault(target, new HashMap<>());

                if (u instanceof DeleteFromArrayPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.DELETE_FROM_ARRAY, new HashMap<>());
                    if (!tempSelectorMap.containsKey(u.getSelector())) {
                        tempSelectorMap.put(u.getSelector(), u.getSource());
                    }
                    // No merge for same selector, just ignore

                    tempEnumMap.put(UpdatePrimitiveEnum.DELETE_FROM_ARRAY, tempSelectorMap);

                } else if (u instanceof InsertIntoArrayPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.INSERT_INTO_ARRAY, new HashMap<>());
                    if (!tempSelectorMap.containsKey(u.getSelector())) {
                        tempSelectorMap.put(u.getSelector(), u.getSource());
                    } else {
                        tempSource = tempSelectorMap.get(u.getSelector());
                        tempSelectorMap.put(
                            u.getSelector(),
                            InsertIntoArrayPrimitive.mergeSources(tempSource, u.getSource())
                        );
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.INSERT_INTO_ARRAY, tempSelectorMap);
                } else if (u instanceof ReplaceInArrayPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.REPLACE_IN_ARRAY, new HashMap<>());
                    if (!tempSelectorMap.containsKey(u.getSelector())) {
                        tempSelectorMap.put(u.getSelector(), u.getSource());
                    } else {
                        // TODO implement jerr:NUP0009
                        throw new OurBadException("MULTIPLE REPLACE OF SAME TARGET & SELECTOR");
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.REPLACE_IN_ARRAY, tempSelectorMap);
                } else if (u instanceof DeleteFromObjectPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.DELETE_FROM_OBJECT, new HashMap<>());
                    if (!tempSelectorMap.containsKey(placeholderSelector)) {
                        tempSelectorMap.put(placeholderSelector, u.getSource());
                    } else {
                        tempSource = tempSelectorMap.get(placeholderSelector);
                        tempSelectorMap.put(
                            placeholderSelector,
                            DeleteFromObjectPrimitive.mergeSources(tempSource, u.getSource())
                        );
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.DELETE_FROM_OBJECT, tempSelectorMap);
                } else if (u instanceof InsertIntoObjectPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.INSERT_INTO_OBJECT, new HashMap<>());
                    if (!tempSelectorMap.containsKey(placeholderSelector)) {
                        tempSelectorMap.put(placeholderSelector, u.getSource());
                    } else {
                        tempSource = tempSelectorMap.get(placeholderSelector);
                        tempSelectorMap.put(
                            u.getSelector(),
                            InsertIntoObjectPrimitive.mergeSources(tempSource, u.getSource())
                        );
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.INSERT_INTO_OBJECT, tempSelectorMap);
                } else if (u instanceof ReplaceInObjectPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.REPLACE_IN_OBJECT, new HashMap<>());
                    if (!tempSelectorMap.containsKey(u.getSelector())) {
                        tempSelectorMap.put(u.getSelector(), u.getSource());
                    } else {
                        // TODO implement jerr:NUP0009
                        throw new OurBadException("MULTIPLE REPLACE OF SAME TARGET & SELECTOR");
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.REPLACE_IN_OBJECT, tempSelectorMap);
                } else if (u instanceof RenameInObjectPrimitive) {
                    tempSelectorMap = tempEnumMap.getOrDefault(UpdatePrimitiveEnum.RENAME_IN_OBJECT, new HashMap<>());
                    if (!tempSelectorMap.containsKey(u.getSelector())) {
                        tempSelectorMap.put(u.getSelector(), u.getSource());
                    } else {
                        // TODO implement jerr:NUP0010
                        throw new OurBadException("MULTIPLE REPLACE OF SAME TARGET & SELECTOR");
                    }
                    tempEnumMap.put(UpdatePrimitiveEnum.RENAME_IN_OBJECT, tempSelectorMap);
                }

                itemToUpdateMap.put(target, tempEnumMap);
            }
        }

        for (UpdatePrimitiveTarget target : itemToUpdateMap.keySet()) {
            tempEnumMap = itemToUpdateMap.get(target);

            if (target.isObject()) {
                UpdatePrimitiveSource deleteFromObjSrc = tempEnumMap.get(UpdatePrimitiveEnum.DELETE_FROM_OBJECT)
                    .get(placeholderSelector);
                List<StringItem> delStrs = deleteFromObjSrc.getSourceAsListOfStrings();
                Set<StringItem> delStrsSet = new HashSet<>(delStrs);
                result.addUpdatePrimitive(
                    new DeleteFromObjectPrimitive(
                            target.getTargetAsObject(),
                            deleteFromObjSrc.getSourceAsListOfStrings()
                    )
                );

                UpdatePrimitiveSource insertIntoObjSrc = tempEnumMap.get(UpdatePrimitiveEnum.INSERT_INTO_OBJECT)
                    .get(placeholderSelector);
                ObjectItem mergedObjectItem = (ObjectItem) insertIntoObjSrc.getSingletonSource();
                result.addUpdatePrimitive(new InsertIntoObjectPrimitive(target.getTargetAsObject(), mergedObjectItem));

                tempSelectorMap = tempEnumMap.get(UpdatePrimitiveEnum.REPLACE_IN_OBJECT);
                for (UpdatePrimitiveSelector selector : tempSelectorMap.keySet()) {
                    if (!(delStrsSet.contains(selector.getSelectorAsString()))) {
                        result.addUpdatePrimitive(
                            new ReplaceInObjectPrimitive(
                                    target.getTargetAsObject(),
                                    selector.getSelectorAsString(),
                                    tempSelectorMap.get(selector).getSingletonSource()
                            )
                        );
                    }
                }

                tempSelectorMap = tempEnumMap.get(UpdatePrimitiveEnum.RENAME_IN_OBJECT);
                for (UpdatePrimitiveSelector selector : tempSelectorMap.keySet()) {
                    if (!(delStrsSet.contains(selector.getSelectorAsString()))) {
                        result.addUpdatePrimitive(
                            new RenameInObjectPrimitive(
                                    target.getTargetAsObject(),
                                    selector.getSelectorAsString(),
                                    (StringItem) tempSelectorMap.get(selector).getSingletonSource()
                            )
                        );
                    }
                }

            } else if (target.isArray()) {
                tempSelectorMap = tempEnumMap.get(UpdatePrimitiveEnum.DELETE_FROM_ARRAY);
                Set<IntItem> delIntsSet = new HashSet<>();
                for (UpdatePrimitiveSelector selector : tempSelectorMap.keySet()) {
                    delIntsSet.add(selector.getSelectorAsInt());
                    result.addUpdatePrimitive(
                        new DeleteFromArrayPrimitive(target.getTargetAsArray(), selector.getSelectorAsInt())
                    );
                }

                tempSelectorMap = tempEnumMap.get(UpdatePrimitiveEnum.INSERT_INTO_ARRAY);
                for (UpdatePrimitiveSelector selector : tempSelectorMap.keySet()) {
                    result.addUpdatePrimitive(
                        new InsertIntoArrayPrimitive(
                                target.getTargetAsArray(),
                                selector.getSelectorAsInt(),
                                tempSelectorMap.get(selector).getSourceAsListOfItems()
                        )
                    );
                }

                tempSelectorMap = tempEnumMap.get(UpdatePrimitiveEnum.REPLACE_IN_ARRAY);
                for (UpdatePrimitiveSelector selector : tempSelectorMap.keySet()) {
                    if (!(delIntsSet.contains(selector.getSelectorAsInt()))) {
                        result.addUpdatePrimitive(
                            new ReplaceInArrayPrimitive(
                                    target.getTargetAsArray(),
                                    selector.getSelectorAsInt(),
                                    tempSelectorMap.get(selector).getSingletonSource()
                            )
                        );
                    }
                }
            }
        }

        return result;
    }

}
