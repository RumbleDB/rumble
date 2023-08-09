package org.rumbledb.runtime.update;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TooManyRenamesOnSameTargetSelectorException;
import org.rumbledb.exceptions.TooManyReplacesOnSameTargetSelectorException;
import org.rumbledb.runtime.update.primitives.*;

import java.util.*;

public class PendingUpdateList {

    private Map<Item, Item> insertObjMap;
    private Map<Item, Map<Item, List<Item>>> insertArrayMap;
    private Map<Item, Map<Item, Item>> delReplaceObjMap;
    private Map<Item, Map<Item, Item>> delReplaceArrayMap;
    private Map<Item, Map<Item, Item>> renameObjMap;
    private Comparator<Item> targetComparator;

    public PendingUpdateList() {
        // TODO: diff comparator for delta
        this.targetComparator = (item1, item2) -> {
            boolean itemIsDelta1 = item1.getTableLocation() != null && !item1.getTableLocation().equals("null");
            boolean itemIsDelta2 = item2.getTableLocation() != null && !item2.getTableLocation().equals("null");
            if (itemIsDelta1 && itemIsDelta2) {
                int tableComp = item1.getTableLocation().compareTo(item2.getTableLocation());
                if (tableComp != 0) {
                    return tableComp;
                }
                int rowComp = Long.compare(item1.getTopLevelID(), item2.getTopLevelID());
                if (rowComp != 0) {
                    return rowComp;
                }
                return item1.getPathIn().compareTo(item2.getPathIn());
            } else if (itemIsDelta1 || itemIsDelta2) {
                //TODO: what to compare when one is delta and one is not? Never occurs?
                return 0;
            }
            int hashCompare = Integer.compare(item1.hashCode(), item2.hashCode());
            if (item1.hashCode() != item2.hashCode()) {
                return hashCompare;
            }
            if (!item1.equals(item2)) {
                return hashCompare;
            }
            return Integer.compare(System.identityHashCode(item1), System.identityHashCode(item2));
        };
        this.insertObjMap = new TreeMap<>(this.targetComparator);
        this.insertArrayMap = new TreeMap<>(this.targetComparator);
        this.delReplaceObjMap = new TreeMap<>(this.targetComparator);
        this.delReplaceArrayMap = new TreeMap<>(this.targetComparator);
        this.renameObjMap = new TreeMap<>(this.targetComparator);
    }

    public PendingUpdateList(UpdatePrimitive updatePrimitive) {
        this();
        this.addUpdatePrimitive(updatePrimitive);
    }

    public void addUpdatePrimitive(UpdatePrimitive updatePrimitive) {
        Item target = updatePrimitive.getTarget();
        if (updatePrimitive.isDeleteObject()) {
            Map<Item, Item> locSrcMap = this.delReplaceObjMap.getOrDefault(target, new HashMap<>());
            for (Item locator : updatePrimitive.getContentList()) {
                locSrcMap.put(locator, null);
            }
            this.delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceObject()) {
            Map<Item, Item> locSrcMap = this.delReplaceObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertObject()) {
            this.insertObjMap.put(target, updatePrimitive.getContent());

        } else if (updatePrimitive.isRenameObject()) {
            Map<Item, Item> locSrcMap = this.renameObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.renameObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isDeleteArray()) {
            Map<Item, Item> locSrcMap = this.delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), null);
            this.delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceArray()) {
            Map<Item, Item> locSrcMap = this.delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertArray()) {
            Map<Item, List<Item>> locSrcMap = this.insertArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContentList());
            this.insertArrayMap.put(target, locSrcMap);
        } else {
            throw new OurBadException("Invalid UpdatePrimitive created");
        }
    }

    public void applyUpdates(ExceptionMetadata metadata) {
        UpdatePrimitiveFactory upFactory = UpdatePrimitiveFactory.getInstance();

        Map<Item, List<UpdatePrimitive>> targetArrayPULs = new HashMap<>();
        List<UpdatePrimitive> tempArrayPULs;

        List<UpdatePrimitive> objectPUL = new ArrayList<>();
        Map<Item, Item> tempSelSrcMap;
        Map<Item, List<Item>> tempSelSrcListMap;
        Item tempSrc;

        ////// OBJECTS

        // DELETES & REPLACES
        for (Item target : this.delReplaceObjMap.keySet()) {
            List<Item> toDel = new ArrayList<>();
            tempSelSrcMap = this.delReplaceObjMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                tempSrc = tempSelSrcMap.get(locator);
                if (tempSrc == null) {
                    toDel.add(locator);
                } else {
                    objectPUL.add(upFactory.createReplaceInObjectPrimitive(target, locator, tempSrc, metadata));
                }
            }
            if (!toDel.isEmpty()) {
                objectPUL.add(upFactory.createDeleteFromObjectPrimitive(target, toDel, metadata));
            }
        }

        // INSERTS

        for (Item target : this.insertObjMap.keySet()) {
            objectPUL.add(upFactory.createInsertIntoObjectPrimitive(target, this.insertObjMap.get(target)));
        }

        // RENAMES

        for (Item target : this.renameObjMap.keySet()) {
            tempSelSrcMap = this.renameObjMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                objectPUL.add(
                    upFactory.createRenameInObjectPrimitive(target, locator, tempSelSrcMap.get(locator), metadata)
                );
            }
        }

        ////// ARRAYS

        // DELETES & REPLACES

        for (Item target : this.delReplaceArrayMap.keySet()) {
            tempArrayPULs = targetArrayPULs.getOrDefault(target, new ArrayList<>());
            tempSelSrcMap = this.delReplaceArrayMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                UpdatePrimitive up;
                tempSrc = tempSelSrcMap.get(locator);
                if (tempSrc == null) {
                    up = upFactory.createDeleteFromArrayPrimitive(target, locator, metadata);
                } else {
                    up = upFactory.createReplaceInArrayPrimitive(target, locator, tempSrc, metadata);
                }
                int index = Collections.binarySearch(
                    tempArrayPULs,
                    up,
                    Comparator.comparing(UpdatePrimitive::getIntSelector)
                );
                if (index < 0) {
                    index = -index - 1;
                }
                tempArrayPULs.add(index, up);
            }
            targetArrayPULs.put(target, tempArrayPULs);
        }

        // INSERTS

        for (Item target : this.insertArrayMap.keySet()) {
            UpdatePrimitive up;
            tempArrayPULs = targetArrayPULs.getOrDefault(target, new ArrayList<>());
            tempSelSrcListMap = this.insertArrayMap.get(target);
            for (Item locator : tempSelSrcListMap.keySet()) {
                up = upFactory.createInsertIntoArrayPrimitive(target, locator, tempSelSrcListMap.get(locator), metadata);
                int index = Collections.binarySearch(
                    tempArrayPULs,
                    up,
                    Comparator.comparing(UpdatePrimitive::getIntSelector)
                );
                if (index < 0) {
                    index = -index - 1;
                }
                tempArrayPULs.add(index, up);
            }
            targetArrayPULs.put(target, tempArrayPULs);
        }

        ////// APPLY OBJECTS

        for (UpdatePrimitive updatePrimitive : objectPUL) {
            updatePrimitive.apply();
        }

        ////// APPLY ARRAYS

        for (Item target : targetArrayPULs.keySet()) {
            tempArrayPULs = targetArrayPULs.get(target);
            for (int i = tempArrayPULs.size() - 1; i >= 0; i--) {
                tempArrayPULs.get(i).apply();
            }
        }

    }

    public void mergeUpdates(
            PendingUpdateList otherPul,
            ExceptionMetadata metadata
    ) {
        Map<Item, Item> tempSelSrcMap;
        Map<Item, List<Item>> tempSelSrcListMap;
        Map<Item, Item> tempSelSrcResMap;
        Map<Item, List<Item>> tempSelSrcResListMap;
        Item tempSrc;
        Item tempSrcRes;
        List<Item> tempSrcList;

        ////// OBJECTS

        // DELETES & REPLACES
        for (Item target : otherPul.delReplaceObjMap.keySet()) {
            tempSelSrcMap = otherPul.delReplaceObjMap.get(target);
            tempSelSrcResMap = this.delReplaceObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                tempSrc = tempSelSrcMap.get(selector);
                tempSrcRes = tempSelSrcResMap.get(selector);
                if (tempSrc == null) {
                    boolean hasRename = this.renameObjMap.containsKey(target) && this.renameObjMap.get(target).containsKey(selector);
                    if (hasRename) {
                        this.renameObjMap.get(target).remove(selector);
                    }
                } else {
                    if (tempSelSrcResMap.containsKey(selector) && tempSrcRes != null) {
                        throw new TooManyReplacesOnSameTargetSelectorException(
                                target.getDynamicType().getName().toString(),
                                selector.getStringValue(),
                                metadata
                        );
                    } else if (tempSelSrcResMap.containsKey(selector)) {
                        continue;
                    }
                }
                tempSelSrcResMap.put(selector, tempSrc);

            }
            this.delReplaceObjMap.put(target, tempSelSrcResMap);
        }

        // INSERTS
        for (Item target : otherPul.insertObjMap.keySet()) {
            tempSrc = otherPul.insertObjMap.get(target);
            if (this.insertObjMap.containsKey(target)) {
                tempSrc = InsertIntoObjectPrimitive.mergeSources(this.insertObjMap.get(target), tempSrc, metadata);
            }
            this.insertObjMap.put(target, tempSrc);
        }

        // RENAME
        for (Item target : otherPul.renameObjMap.keySet()) {
            tempSelSrcMap = otherPul.renameObjMap.get(target);
            tempSelSrcResMap = this.renameObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    throw new TooManyRenamesOnSameTargetSelectorException(selector.getStringValue(), metadata);
                }
                boolean isDelete = this.delReplaceObjMap.containsKey(target) && this.delReplaceObjMap.get(target).containsKey(selector) && this.delReplaceObjMap.get(target).get(selector) == null;
                if (isDelete) {
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            this.renameObjMap.put(target, tempSelSrcResMap);
        }

        ////// ARRAYS

        // DELETES & REPLACES
        for (Item target : otherPul.delReplaceArrayMap.keySet()) {
            tempSelSrcMap = otherPul.delReplaceArrayMap.get(target);
            tempSelSrcResMap = this.delReplaceArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    tempSrc = tempSelSrcResMap.get(selector);
                    if (tempSrc != null) {
                        throw new TooManyReplacesOnSameTargetSelectorException(
                                target.getDynamicType().getName().toString(),
                                Integer.toString(selector.getIntValue()),
                                metadata
                        );
                    }
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            this.delReplaceArrayMap.put(target, tempSelSrcResMap);
        }

        // INSERTS
        for (Item target : otherPul.insertArrayMap.keySet()) {
            tempSelSrcListMap = otherPul.insertArrayMap.get(target);
            tempSelSrcResListMap = this.insertArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcListMap.keySet()) {
                tempSrcList = tempSelSrcResListMap.getOrDefault(selector, new ArrayList<>());
                tempSelSrcResListMap.put(
                        selector,
                        InsertIntoArrayPrimitive.mergeSources(tempSrcList, tempSelSrcListMap.get(selector))
                );
            }
            this.insertArrayMap.put(target, tempSelSrcResListMap);
        }
    }

}
