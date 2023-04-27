package org.rumbledb.runtime.update;

import org.apache.hadoop.mapred.lib.DelegatingInputFormat;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TooManyRenamesOnSameTargetSelectorException;
import org.rumbledb.exceptions.TooManyReplacesOnSameTargetSelectorException;
import org.rumbledb.runtime.update.primitives.*;
import org.rumbledb.types.ItemType;

import java.util.*;

public class PendingUpdateList {

    private Map<Item, Item> insertObjMap;
    private Map<Item, Map<Item, List<Item>>> insertArrayMap;
    private Map<Item, Map<Item, Item>> delReplaceObjMap;
    private Map<Item, Map<Item, Item>> delReplaceArrayMap;
    private Map<Item, Map<Item, Item>> renameObjMap;

    public PendingUpdateList() {
        this.insertObjMap = new HashMap<>();
        this.insertArrayMap = new HashMap<>();
        this.delReplaceObjMap = new HashMap<>();
        this.delReplaceArrayMap = new HashMap<>();
        this.renameObjMap = new HashMap<>();
    }

    public PendingUpdateList(UpdatePrimitive updatePrimitive) {
        this();
        this.addUpdatePrimitive(updatePrimitive);
    }

    public void addUpdatePrimitive(UpdatePrimitive updatePrimitive) {
        Item target = updatePrimitive.getTarget();
        if (updatePrimitive.isDeleteObject()) {
            Map<Item, Item> locSrcMap = delReplaceObjMap.getOrDefault(target, new HashMap<>());
            for (Item locator : updatePrimitive.getContentList()) {
                locSrcMap.put(locator, null);
            }
            delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceObject()) {
            Map<Item, Item> locSrcMap = delReplaceObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            delReplaceObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertObject()) {
            insertObjMap.put(target, updatePrimitive.getContent());

        } else if (updatePrimitive.isRenameObject()) {
            Map<Item, Item> locSrcMap = renameObjMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            renameObjMap.put(target, locSrcMap);

        } else if (updatePrimitive.isDeleteArray()) {
            Map<Item, Item> locSrcMap = delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), null);
            delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isReplaceArray()) {
            Map<Item, Item> locSrcMap = delReplaceArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContent());
            delReplaceArrayMap.put(target, locSrcMap);

        } else if (updatePrimitive.isInsertArray()) {
            Map<Item, List<Item>> locSrcMap = insertArrayMap.getOrDefault(target, new HashMap<>());
            locSrcMap.put(updatePrimitive.getSelector(), updatePrimitive.getContentList());
            insertArrayMap.put(target, locSrcMap);
        } else {
            throw new OurBadException("Invalid UpdatePrimitive created");
        }
    }

    public void applyUpdates() {

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        List<UpdatePrimitive> pul = new ArrayList<>();
        Map<Item, Item> tempSelSrcMap;
        Map<Item, List<Item>> tempSelSrcListMap;
        Item tempSrc;
        List<Item> tempSrcList;

        ////// OBJECTS

        // DELETES & REPLACES
        for (Item target : delReplaceObjMap.keySet()) {
            List<Item> toDel = new ArrayList<>();
            tempSelSrcMap = delReplaceObjMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                tempSrc = tempSelSrcMap.get(locator);
                if (tempSrc == null) {
                    toDel.add(locator);
                } else {
                    pul.add(factory.createReplaceInObjectPrimitive(target, locator, tempSrc));
                }
            }
            pul.add(factory.createDeleteFromObjectPrimitive(target, toDel));
        }

        // INSERTS

        for (Item target : insertObjMap.keySet()) {
            pul.add(factory.createInsertIntoObjectPrimitive(target, insertObjMap.get(target)));
        }

        // RENAMES

        for (Item target : renameObjMap.keySet()) {
            tempSelSrcMap = renameObjMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                pul.add(factory.createRenameInObjectPrimitive(target, locator, tempSelSrcMap.get(locator)));
            }
        }

        ////// ARRAYS

        // DELETES & REPLACES

        for (Item target : delReplaceArrayMap.keySet()) {
            tempSelSrcMap = delReplaceArrayMap.get(target);
            for (Item locator : tempSelSrcMap.keySet()) {
                tempSrc = tempSelSrcMap.get(locator);
                if (tempSrc == null) {
                    pul.add(factory.createDeleteFromArrayPrimitive(target, locator));
                } else {
                    pul.add(factory.createReplaceInArrayPrimitive(target, locator, tempSrc));
                }
            }
        }

        // INSERTS

        for (Item target : insertArrayMap.keySet()) {
            tempSelSrcListMap = insertArrayMap.get(target);
            for (Item locator : tempSelSrcListMap.keySet()) {
                pul.add(factory.createInsertIntoArrayPrimitive(target, locator, tempSelSrcListMap.get(locator)));
            }
        }

        ////// APPLY

        for (UpdatePrimitive updatePrimitive : pul) {
            updatePrimitive.apply();
        }

    }

    public static PendingUpdateList mergeUpdates(PendingUpdateList pul1, PendingUpdateList pul2) {
        PendingUpdateList res = new PendingUpdateList();
        Map<Item, Item> tempSelSrcMap;
        Map<Item, List<Item>> tempSelSrcListMap;
        Map<Item, Item> tempSelSrcResMap;
        Map<Item, List<Item>> tempSelSrcResListMap;
        Item tempSrc;
        List<Item> tempSrcList;

        ////// OBJECTS

        // DELETES & REPLACES

        for (Item target : pul1.delReplaceObjMap.keySet()) {
            tempSelSrcMap = pul1.delReplaceObjMap.get(target);
            tempSelSrcResMap = res.delReplaceObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        for (Item target : pul2.delReplaceObjMap.keySet()) {
            tempSelSrcMap = pul2.delReplaceObjMap.get(target);
            tempSelSrcResMap = res.delReplaceObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    tempSrc = tempSelSrcResMap.get(selector);
                    if (tempSrc != null) {
                        throw new TooManyReplacesOnSameTargetSelectorException(target.getDynamicType().getName().toString(), selector.getStringValue(), ExceptionMetadata.EMPTY_METADATA);
                    }
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        // INSERTS

        res.insertObjMap.putAll(pul1.insertObjMap);

        for (Item target : pul2.insertObjMap.keySet()) {
            tempSrc = pul2.insertObjMap.get(target);
            if (res.insertObjMap.containsKey(target)) {
                tempSrc = InsertIntoObjectPrimitive.mergeSources(res.insertObjMap.get(target), tempSrc);
            }
            res.insertObjMap.put(target,tempSrc);
        }

        // RENAME

        for (Item target : pul1.renameObjMap.keySet()) {
            tempSelSrcMap = pul1.renameObjMap.get(target);
            tempSelSrcResMap = res.renameObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        for (Item target : pul2.renameObjMap.keySet()) {
            tempSelSrcMap = pul2.renameObjMap.get(target);
            tempSelSrcResMap = res.renameObjMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    throw new TooManyRenamesOnSameTargetSelectorException(selector.getStringValue(), ExceptionMetadata.EMPTY_METADATA);
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceObjMap.put(target,tempSelSrcResMap);
        }

        ////// ARRAYS

        // DELETES & REPLACES

        for (Item target : pul1.delReplaceArrayMap.keySet()) {
            tempSelSrcMap = pul1.delReplaceArrayMap.get(target);
            tempSelSrcResMap = res.delReplaceArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceArrayMap.put(target,tempSelSrcResMap);
        }

        for (Item target : pul2.delReplaceArrayMap.keySet()) {
            tempSelSrcMap = pul2.delReplaceArrayMap.get(target);
            tempSelSrcResMap = res.delReplaceArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcMap.keySet()) {
                if (tempSelSrcResMap.containsKey(selector)) {
                    tempSrc = tempSelSrcResMap.get(selector);
                    if (tempSrc != null) {
                        throw new TooManyReplacesOnSameTargetSelectorException(target.getDynamicType().getName().toString(), Integer.toString(selector.getIntValue()), ExceptionMetadata.EMPTY_METADATA);
                    }
                    continue;
                }
                tempSelSrcResMap.put(selector, tempSelSrcMap.get(selector));
            }
            res.delReplaceArrayMap.put(target,tempSelSrcResMap);
        }

        // INSERTS

        for (Item target : pul1.insertArrayMap.keySet()) {
            tempSelSrcListMap = pul1.insertArrayMap.get(target);
            tempSelSrcResListMap = res.insertArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcListMap.keySet()) {
                tempSelSrcResListMap.put(selector, tempSelSrcListMap.get(selector));
            }
            res.insertArrayMap.put(target,tempSelSrcResListMap);
        }

        for (Item target : pul2.insertArrayMap.keySet()) {
            tempSelSrcListMap = pul2.insertArrayMap.get(target);
            tempSelSrcResListMap = res.insertArrayMap.getOrDefault(target, new HashMap<>());

            for (Item selector : tempSelSrcListMap.keySet()) {
                tempSrcList = tempSelSrcResListMap.getOrDefault(selector, null);
                tempSelSrcResListMap.put(selector, InsertIntoArrayPrimitive.mergeSources(tempSelSrcListMap.get(selector), tempSrcList));
            }
            res.insertArrayMap.put(target,tempSelSrcResListMap);
        }

        return res;
    }

}
