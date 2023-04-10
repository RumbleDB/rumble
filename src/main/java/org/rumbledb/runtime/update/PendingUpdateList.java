package org.rumbledb.runtime.update;

import com.amazonaws.services.dynamodbv2.model.ItemCollectionMetrics;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.update.primitives.*;

import java.util.*;

public class PendingUpdateList {

    private Map<Item, Item> insertObjMap;
    private Map<Item, Map<Item, List<Item>>> insertArrayMap;
    private Map<Item, Map<Item, Item>> delReplaceObjMap;
    private Map<Item, Map<Item, Item>> delReplaceArrayMap;
    private Map<Item, Map<Item, Item>> renameObjMap;

    public PendingUpdateList() {

    }

//    public void addUpdatePrimitive(UpdatePrimitive updatePrimitive) {
//        this.updatePrimitives.add(updatePrimitive);
//    }
//
//    public void addAllUpdatePrimitives(List<UpdatePrimitive> updatePrimitives) {
//        this.updatePrimitives.addAll(updatePrimitives);
//    }


//    public void applyUpdates() {
//        for (UpdatePrimitive up : this.updatePrimitives) {
//            up.apply();
//        }
//    }

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

        for (Item target : pul2.insertObjMap.keySet()) {
            tempSrc = pul2.insertObjMap.get(target);
            if (res.insertObjMap.containsKey(target)) {
//                tempSrc = InsertIntoObjectPrimitive.mergeSources(res.insertObjMap.get(target), tempSrc);
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
                    // TODO implement jerr:NUP0010
                    throw new OurBadException("MULTIPLE RENAME OF SAME TARGET & SELECTOR");
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
