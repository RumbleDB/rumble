package org.rumbledb.runtime.update;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TooManyRenamesOnSameTargetSelectorException;
import org.rumbledb.exceptions.TooManyReplacesOnSameTargetSelectorException;
import org.rumbledb.exceptions.TooManyCollectionCreationsOnSameTargetException;
import org.rumbledb.exceptions.TooManyEditsOnSameTargetException;
import org.rumbledb.runtime.update.primitives.*;

import java.util.*;

public class PendingUpdateList {

    private Map<Item, Item> insertObjMap;
    private Map<Item, Map<Item, List<Item>>> insertArrayMap;
    private Map<Item, Map<Item, Item>> delReplaceObjMap;
    private Map<Item, Map<Item, Item>> delReplaceArrayMap;
    private Map<Item, Map<Item, Item>> renameObjMap;
    private Comparator<Item> targetComparator;
    private Comparator<Item> arraySelectorComparator;

    private Map<String, UpdatePrimitive> createCollectionMap;
    private Map<String, UpdatePrimitive> truncateCollectionMap;
    private Map<String, Map<Double, UpdatePrimitive>> deleteTupleMap;
    private Map<String, Map<Double, UpdatePrimitive>> editTupleMap;
    private List<UpdatePrimitive> insertFirstList;
    private List<UpdatePrimitive> insertLastList;
    private List<UpdatePrimitive> insertBeforeList;
    private List<UpdatePrimitive> insertAfterList;


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
                // TODO: what to compare when one is delta and one is not? Never occurs?
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
        this.arraySelectorComparator = Comparator.comparingInt(Item::getIntValue).reversed();
        this.insertObjMap = new TreeMap<>(this.targetComparator);
        this.insertArrayMap = new TreeMap<>(this.targetComparator);
        this.delReplaceObjMap = new TreeMap<>(this.targetComparator);
        this.delReplaceArrayMap = new TreeMap<>(this.targetComparator);
        this.renameObjMap = new TreeMap<>(this.targetComparator);
        this.createCollectionMap = new TreeMap<>();
        this.truncateCollectionMap = new TreeMap<>();
        this.deleteTupleMap = new TreeMap<>();
        this.editTupleMap = new TreeMap<>();
        this.insertFirstList = new ArrayList<>();
        this.insertLastList = new ArrayList<>();
        this.insertBeforeList = new ArrayList<>();
        this.insertAfterList = new ArrayList<>();
    }

    public PendingUpdateList(UpdatePrimitive updatePrimitive) {
        this();
        this.addUpdatePrimitive(updatePrimitive);
    }

    public void addUpdatePrimitive(UpdatePrimitive updatePrimitive) {
        if (updatePrimitive.isDeleteObject()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, Item> locSrcMap = this.delReplaceObjMap.getOrDefault(target, new HashMap<>());
            for (Item locator : updatePrimitive.getContentList()) {
                locSrcMap.put(locator, null);
            }
            this.delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceObject()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, Item> locSrcMap = this.delReplaceObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertObject()) {
            Item target = updatePrimitive.getTarget();
            this.insertObjMap.put(target, updatePrimitive.getContent());

        } else if (updatePrimitive.isRenameObject()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, Item> locSrcMap = this.renameObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.renameObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isDeleteArray()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, Item> locSrcMap = this.delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), null);
            this.delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceArray()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, Item> locSrcMap = this.delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            this.delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertArray()) {
            Item target = updatePrimitive.getTarget();
            Map<Item, List<Item>> locSrcMap = this.insertArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContentList());
            this.insertArrayMap.put(target, locSrcMap);
        } else if (updatePrimitive.isCreateCollection()) {
            String collectionPath = updatePrimitive.getCollectionPath();
            this.createCollectionMap.put(collectionPath, updatePrimitive);
        } else if (updatePrimitive.isTruncateCollection()) {
            String collectionName = updatePrimitive.getCollectionName();
            this.truncateCollectionMap.put(collectionName, updatePrimitive);
        } else if (updatePrimitive.isDeleteTuple()) {
            String collection = updatePrimitive.getCollectionPath();
            Double rowOrder = updatePrimitive.getRowOrder();
            this.deleteTupleMap.computeIfAbsent(collection, k -> new TreeMap<>()).put(rowOrder, updatePrimitive);
        } else if (updatePrimitive.isEditTuple()) {
            String collection = updatePrimitive.getCollectionPath();
            Double rowOrder = updatePrimitive.getRowOrder();
            this.editTupleMap.computeIfAbsent(collection, k -> new TreeMap<>()).put(rowOrder, updatePrimitive);
        } else if (updatePrimitive.isInsertFirstIntoCollection()) {
            this.insertFirstList.add(updatePrimitive);
        } else if (updatePrimitive.isInsertLastIntoCollection()) {
            this.insertLastList.add(updatePrimitive);
        } else if (updatePrimitive.isInsertBeforeIntoCollection()) {
            this.insertBeforeList.add(updatePrimitive);
        } else if (updatePrimitive.isInsertAfterIntoCollection()) {
            this.insertAfterList.add(updatePrimitive);
        } else {
            throw new OurBadException("Invalid UpdatePrimitive created");
        }
    }

    public void applyUpdates(ExceptionMetadata metadata) {
        UpdatePrimitiveFactory upFactory = UpdatePrimitiveFactory.getInstance();

        // new TreeMap<>(this.targetComparator) sometimes causes null on apply?
        Map<Item, Map<Item, List<UpdatePrimitive>>> targetArrayPULs = new HashMap<>();
        Map<Item, List<UpdatePrimitive>> tempSelPULsMap;
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
            objectPUL.add(upFactory.createInsertIntoObjectPrimitive(target, this.insertObjMap.get(target), metadata));
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
            tempSelPULsMap = targetArrayPULs.getOrDefault(target, new TreeMap<>(this.arraySelectorComparator));
            tempSelSrcMap = this.delReplaceArrayMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                UpdatePrimitive up;
                tempSrc = tempSelSrcMap.get(locator);
                if (tempSrc == null) {
                    up = upFactory.createDeleteFromArrayPrimitive(target, locator, metadata);
                } else {
                    up = upFactory.createReplaceInArrayPrimitive(target, locator, tempSrc, metadata);
                }
                tempArrayPULs = tempSelPULsMap.getOrDefault(locator, new ArrayList<>());
                tempArrayPULs.add(up);
                tempSelPULsMap.put(locator, tempArrayPULs);
            }
            targetArrayPULs.put(target, tempSelPULsMap);
        }

        // INSERTS

        for (Item target : this.insertArrayMap.keySet()) {
            UpdatePrimitive up;
            tempSelPULsMap = targetArrayPULs.getOrDefault(target, new TreeMap<>(this.arraySelectorComparator));
            tempSelSrcListMap = this.insertArrayMap.get(target);
            for (Item locator : tempSelSrcListMap.keySet()) {
                up = upFactory.createInsertIntoArrayPrimitive(
                    target,
                    locator,
                    tempSelSrcListMap.get(locator),
                    metadata
                );
                tempArrayPULs = tempSelPULsMap.getOrDefault(locator, new ArrayList<>());
                tempArrayPULs.add(up);
                tempSelPULsMap.put(locator, tempArrayPULs);
            }
            targetArrayPULs.put(target, tempSelPULsMap);
        }

        ////// APPLY OBJECTS

        for (UpdatePrimitive updatePrimitive : objectPUL) {
            updatePrimitive.apply();
        }

        ////// APPLY ARRAYS
        for (Item target : targetArrayPULs.keySet()) {
            tempSelPULsMap = targetArrayPULs.get(target);
            for (Item selector : tempSelPULsMap.keySet()) {
                tempArrayPULs = tempSelPULsMap.get(selector);
                for (UpdatePrimitive up : tempArrayPULs) {
                    up.apply();
                }
            }
        }

        ////// APPLY INSERT TUPLE
        this.insertBeforeList.forEach(UpdatePrimitive::apply);
        this.insertAfterList.forEach(UpdatePrimitive::apply);
        this.insertFirstList.forEach(UpdatePrimitive::apply);
        this.insertLastList.forEach(UpdatePrimitive::apply);

        ////// APPLY EDIT TUPLE
        for (Map<Double, UpdatePrimitive> tables : this.editTupleMap.values()) {
            tables.values().forEach(UpdatePrimitive::apply);
        }

        ////// APPLY DELETE TUPLE
        for (Map<Double, UpdatePrimitive> tables : this.deleteTupleMap.values()) {
            tables.values().forEach(UpdatePrimitive::apply);
        }

        ////// APPLY CREATE COLLECTION
        this.createCollectionMap.values().forEach(UpdatePrimitive::apply);

        ////// APPLY TRUNCATE COLLECTION
        this.truncateCollectionMap.values().forEach(UpdatePrimitive::apply);

    }

    public void mergeUpdates(PendingUpdateList otherPul, ExceptionMetadata metadata) {
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
                boolean srcResMapHasSel = tempSelSrcResMap.containsKey(selector);
                if (tempSrc == null) {
                    boolean hasRename = this.renameObjMap.containsKey(target)
                        && this.renameObjMap.get(target).containsKey(selector);
                    if (hasRename) {
                        this.renameObjMap.get(target).remove(selector);
                    }
                } else {
                    if (srcResMapHasSel && tempSrcRes != null) {
                        throw new TooManyReplacesOnSameTargetSelectorException(
                                target.getDynamicType().getName().toString(),
                                selector.getStringValue(),
                                metadata
                        );
                    } else if (srcResMapHasSel) {
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
                boolean isDelete = this.delReplaceObjMap.containsKey(target)
                    && this.delReplaceObjMap.get(target).containsKey(selector)
                    && this.delReplaceObjMap.get(target).get(selector) == null;
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
                tempSrc = tempSelSrcMap.get(selector);
                tempSrcRes = tempSelSrcResMap.get(selector);
                boolean srcResMapHasSel = tempSelSrcResMap.containsKey(selector);
                if (tempSrc != null && srcResMapHasSel) {
                    if (tempSrcRes == null) {
                        continue;
                    } else {
                        throw new TooManyReplacesOnSameTargetSelectorException(
                                target.getDynamicType().getName().toString(),
                                Integer.toString(selector.getIntValue()),
                                metadata
                        );
                    }
                }
                tempSelSrcResMap.put(selector, tempSrc);
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

        // CREATE COLLECTION
        for (Map.Entry<String, UpdatePrimitive> entry : otherPul.createCollectionMap.entrySet()) {
            if (this.createCollectionMap.containsKey(entry.getKey())) {
                throw new TooManyCollectionCreationsOnSameTargetException(entry.getKey(), metadata);
            } else {
                this.createCollectionMap.put(entry.getKey(), entry.getValue());
            }
        }

        // TRUNCATE COLLECTION
        for (Map.Entry<String, UpdatePrimitive> entry : otherPul.truncateCollectionMap.entrySet()) {
            this.truncateCollectionMap.putIfAbsent(entry.getKey(), entry.getValue());
        }

        // DELETE TUPLE
        for (Map.Entry<String, Map<Double, UpdatePrimitive>> tableEntry : otherPul.deleteTupleMap.entrySet()) {
            String collection = tableEntry.getKey();
            Map<Double, UpdatePrimitive> tableMap = this.deleteTupleMap.computeIfAbsent(
                collection,
                k -> new TreeMap<>()
            );
            Map<Double, UpdatePrimitive> editMap = this.editTupleMap.get(collection);

            for (Map.Entry<Double, UpdatePrimitive> entry : tableEntry.getValue().entrySet()) {
                if (editMap != null && editMap.containsKey(entry.getKey())) {
                    continue;
                } else {
                    tableMap.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }

        // EDIT TUPLE
        for (Map.Entry<String, Map<Double, UpdatePrimitive>> tableEntry : otherPul.editTupleMap.entrySet()) {
            String collection = tableEntry.getKey();
            Map<Double, UpdatePrimitive> tableMap = this.editTupleMap.computeIfAbsent(collection, k -> new TreeMap<>());
            Map<Double, UpdatePrimitive> deleteMap = this.deleteTupleMap.get(collection);

            for (Map.Entry<Double, UpdatePrimitive> entry : tableEntry.getValue().entrySet()) {
                if (deleteMap != null) {
                    deleteMap.remove(entry.getKey());
                }

                if (tableMap.containsKey(entry.getKey())) {
                    throw new TooManyEditsOnSameTargetException(collection, entry.getKey(), metadata);
                } else {
                    tableMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // No merge conflicts in present implementations of Insertion primitives
        this.insertFirstList.addAll(otherPul.insertFirstList);
        this.insertLastList.addAll(otherPul.insertLastList);
        this.insertBeforeList.addAll(otherPul.insertBeforeList);
        this.insertAfterList.addAll(otherPul.insertAfterList);

    }

}
