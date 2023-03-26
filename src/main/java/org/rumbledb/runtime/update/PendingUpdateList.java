package org.rumbledb.runtime.update;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.update.primitives.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public static PendingUpdateList mergeUpdates(PendingUpdateList pul1, PendingUpdateList pul2) {
        PendingUpdateList result = new PendingUpdateList();
        HashMap<Item, List<UpdatePrimitive>> itemToUpdateMap = new HashMap<>();
        List<UpdatePrimitive> tempUps;

        for (UpdatePrimitive up : pul1.getUpdatePrimitives()) {
            result.addUpdatePrimitive(up);
            Item target = up.getTarget();

            if (!itemToUpdateMap.containsKey(target)) {
                tempUps = new ArrayList<>();
            } else {
                tempUps = itemToUpdateMap.get(target);
            }
            tempUps.add(up);
            itemToUpdateMap.put(target, tempUps);
        }

        for (UpdatePrimitive up : pul2.getUpdatePrimitives()) {
            result.addUpdatePrimitive(up);
            Item target = up.getTarget();

            if (!itemToUpdateMap.containsKey(target)) {
                tempUps = new ArrayList<>();
            } else {
                tempUps = itemToUpdateMap.get(target);
            }
            tempUps.add(up);
            itemToUpdateMap.put(target, tempUps);
        }

        for (Item target : itemToUpdateMap.keySet()) {
            List<UpdatePrimitive> targetUpdates = itemToUpdateMap.get(target);

            // object
            if (target.isObject()) {
                boolean hasDeletePrimitives = targetUpdates.stream()
                    .anyMatch(u -> u instanceof DeleteFromObjectPrimitive);
                boolean hasInsertPrimitives = targetUpdates.stream()
                    .anyMatch(u -> u instanceof InsertIntoObjectPrimitive);

                if (hasInsertPrimitives) {
                    ObjectItem sourceObject =
                        targetUpdates
                            .stream()
                            .filter(u -> u instanceof InsertIntoObjectPrimitive)
                            .map(u -> ((InsertIntoObjectPrimitive) u).getSourceObject())
                            .reduce(new ObjectItem(), ObjectItem::mergeWith);
                    result.addUpdatePrimitive(new InsertIntoObjectPrimitive((ObjectItem) target, sourceObject));
                }

                if (hasDeletePrimitives) {
                    List<StringItem> names =
                        targetUpdates
                            .stream()
                            .filter(u -> u instanceof DeleteFromObjectPrimitive)
                            .flatMap(u -> ((DeleteFromObjectPrimitive) u).getNamesToRemove().stream())
                            .distinct()
                            .collect(Collectors.toList());
                    result.addUpdatePrimitive(new DeleteFromObjectPrimitive((ObjectItem) target, names));
                    continue;
                }

                List<UpdatePrimitive> renamePrimitives =
                    targetUpdates
                        .stream()
                        .filter(u -> u instanceof RenameInObjectPrimitive)
                        .collect(Collectors.toList());
                List<StringItem> uniqueRenameSelectors =
                    renamePrimitives
                        .stream()
                        .map(u -> ((RenameInObjectPrimitive) u).getTargetName())
                        .distinct()
                        .collect(Collectors.toList());

                if (renamePrimitives.size() != uniqueRenameSelectors.size()) {
                    // TODO Throw rename on same key error jerr:JNUP0010.
                }

                List<UpdatePrimitive> replacePrimitives =
                    targetUpdates
                        .stream()
                        .filter(u -> u instanceof ReplaceInObjectPrimitive)
                        .collect(Collectors.toList());
                List<StringItem> uniqueReplaceSelectors =
                    replacePrimitives
                        .stream()
                        .map(u -> ((ReplaceInObjectPrimitive) u).getTargetName())
                        .distinct()
                        .collect(Collectors.toList());

                if (replacePrimitives.size() != uniqueReplaceSelectors.size()) {
                    // TODO Throw replace on same key error jerr:JNUP0009.
                }

            }

            // array
            if (target.isArray()) {
                boolean hasDeletePrimitives = targetUpdates.stream()
                    .anyMatch(u -> u instanceof DeleteFromArrayPrimitive);
                boolean hasInsertPrimitives = targetUpdates.stream()
                    .anyMatch(u -> u instanceof InsertIntoArrayPrimitive);

                if (hasInsertPrimitives) {
                    HashMap<IntItem, List<Item>> locatorToInsertsMap = new HashMap<>();

                    targetUpdates
                        .stream()
                        .filter(u -> u instanceof InsertIntoArrayPrimitive)
                        .map(u -> (InsertIntoArrayPrimitive) u)
                        .forEach(u -> {
                            List<Item> temp;
                            if (!locatorToInsertsMap.containsKey(u.getPositionInt())) {
                                temp = new ArrayList<>();
                            } else {
                                temp = locatorToInsertsMap.get(u.getPositionInt());
                            }
                            temp.addAll(u.getSourceSequence());
                            locatorToInsertsMap.put(u.getPositionInt(), temp);
                        });

                    for (IntItem intItem : locatorToInsertsMap.keySet()) {
                        result.addUpdatePrimitive(
                            new InsertIntoArrayPrimitive(
                                    (ArrayItem) target,
                                    intItem,
                                    locatorToInsertsMap.get(intItem)
                            )
                        );
                    }
                }

                if (hasDeletePrimitives) {
                    List<IntItem> intItems =
                        targetUpdates
                            .stream()
                            .filter(u -> u instanceof DeleteFromArrayPrimitive)
                            .map(u -> ((DeleteFromArrayPrimitive) u).getPositionInt())
                            .distinct()
                            .collect(Collectors.toList());
                    for (IntItem intItem : intItems) {
                        result.addUpdatePrimitive(new DeleteFromArrayPrimitive((ArrayItem) target, intItem));
                    }
                    continue;
                }

                List<UpdatePrimitive> replacePrimitives =
                    targetUpdates
                        .stream()
                        .filter(u -> u instanceof ReplaceInArrayPrimitive)
                        .collect(Collectors.toList());
                List<IntItem> uniqueReplaceSelectors =
                    replacePrimitives
                        .stream()
                        .map(u -> ((ReplaceInArrayPrimitive) u).getPositionInt())
                        .distinct()
                        .collect(Collectors.toList());

                if (replacePrimitives.size() != uniqueReplaceSelectors.size()) {
                    // TODO Throw replace on same key error jerr:JNUP0009.
                }

            }
        }

        return result;
    }


}
