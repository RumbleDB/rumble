package org.rumbledb.runtime.update;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.update.primitives.*;

import java.util.*;

public class PendingUpdateList {

    private List<UpdatePrimitive> updatePrimitives;

    private Map<UpdatePrimitiveTarget, UpdatePrimitiveSource> insertObjMap;
    private Map<UpdatePrimitiveTarget, Map<UpdatePrimitiveSelector, UpdatePrimitiveSource>> insertArrayMap;
    private Map<UpdatePrimitiveTarget, Map<UpdatePrimitiveSelector, UpdatePrimitiveSource>> delReplaceObjMap;
    private Map<UpdatePrimitiveTarget, Map<UpdatePrimitiveSelector, UpdatePrimitiveSource>> delReplaceArrayMap;
    private Map<UpdatePrimitiveTarget, Map<UpdatePrimitiveSelector, UpdatePrimitiveSource>> renameObjMap;

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
                List<Item> delStrs = deleteFromObjSrc.getSourceAsListOfStrings();
                Set<Item> delStrsSet = new HashSet<>(delStrs);
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

    public static PendingUpdateList mergeUpdatesMaps(PendingUpdateList pul1, PendingUpdateList pul2) {
        PendingUpdateList res = new PendingUpdateList();
        Map<UpdatePrimitiveTarget, Map<UpdatePrimitiveSelector, UpdatePrimitiveSource>> resDelRepObjMap;
        Map<UpdatePrimitiveSelector, UpdatePrimitiveSource> tempSelSrcMap;
        Map<UpdatePrimitiveSelector, UpdatePrimitiveSource> tempSelSrcResMap;
        UpdatePrimitiveSource tempSrc;

        ////// OBJECTS

        // DELETES & REPLACES

        for (UpdatePrimitiveTarget target : pul1.delReplaceObjMap.keySet()) {
            tempSelSrcMap = pul1.delReplaceObjMap.get(target);
            tempSelSrcResMap = res.delReplaceObjMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        for (UpdatePrimitiveTarget target : pul2.delReplaceObjMap.keySet()) {
            tempSelSrcMap = pul2.delReplaceObjMap.get(target);
            tempSelSrcResMap = res.delReplaceObjMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    tempSrc = tempSelSrcResMap.get(selector);
                    if (tempSrc != null) {
                        // TODO implement jerr:NUP0009
                        throw new OurBadException("MULTIPLE REPLACE OF SAME TARGET & SELECTOR");
                    }
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        // INSERTS

        res.insertObjMap.putAll(pul1.insertObjMap);

        for (UpdatePrimitiveTarget target : pul2.insertObjMap.keySet()) {
            tempSrc = pul2.insertObjMap.get(target);
            if (res.insertObjMap.containsKey(target)) {
                tempSrc = InsertIntoObjectPrimitive.mergeSources(res.insertObjMap.get(target), tempSrc);
            }
            res.insertObjMap.put(target,tempSrc);
        }

        // RENAME

        for (UpdatePrimitiveTarget target : pul1.renameObjMap.keySet()) {
            tempSelSrcMap = pul1.renameObjMap.get(target);
            tempSelSrcResMap = res.renameObjMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        for (UpdatePrimitiveTarget target : pul2.renameObjMap.keySet()) {
            tempSelSrcMap = pul2.renameObjMap.get(target);
            tempSelSrcResMap = res.renameObjMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    // TODO implement jerr:NUP0010
                    throw new OurBadException("MULTIPLE RENAME OF SAME TARGET & SELECTOR");
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        ////// ARRAYS

        // DELETES & REPLACES

        for (UpdatePrimitiveTarget target : pul1.delReplaceArrayMap.keySet()) {
            tempSelSrcMap = pul1.delReplaceArrayMap.get(target);
            tempSelSrcResMap = res.delReplaceArrayMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceArrayMap.put(target,tempSelSrcResMap);
        }

        for (UpdatePrimitiveTarget target : pul2.delReplaceArrayMap.keySet()) {
            tempSelSrcMap = pul2.delReplaceArrayMap.get(target);
            tempSelSrcResMap = res.delReplaceArrayMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    tempSrc = tempSelSrcResMap.get(selector);
                    if (tempSrc != null) {
                        // TODO implement jerr:NUP0009
                        throw new OurBadException("MULTIPLE REPLACE OF SAME TARGET & SELECTOR");
                    }
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceArrayMap.put(target,tempSelSrcResMap);
        }

        // INSERTS

        for (UpdatePrimitiveTarget target : pul1.insertArrayMap.keySet()) {
            tempSelSrcMap = pul1.insertArrayMap.get(target);
            tempSelSrcResMap = res.insertArrayMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.insertArrayMap.put(target,tempSelSrcResMap);
        }

        for (UpdatePrimitiveTarget target : pul2.insertArrayMap.keySet()) {
            tempSelSrcMap = pul2.insertArrayMap.get(target);
            tempSelSrcResMap = res.insertArrayMap.getOrDefault(target, new HashMap<>());

            for (UpdatePrimitiveSelector selector : tempSelSrcMap.keySet()) {
                tempSrc = tempSelSrcResMap.getOrDefault(selector, new UpdatePrimitiveSource(new ObjectItem()));
                tempSelSrcResMap.put(selector, InsertIntoArrayPrimitive.mergeSources(tempSelSrcMap.get(selector), tempSrc));
            }
            res.insertArrayMap.put(target,tempSelSrcResMap);
        }

        return res;
    }

}
