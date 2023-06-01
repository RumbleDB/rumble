package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.List;

public class InsertIntoArrayPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private List<Item> content;

    public InsertIntoArrayPrimitive(Item targetArray, Item positionInt, List<Item> sourceSequence) {
        if (!targetArray.isArray() || !positionInt.isNumeric()) {
            // TODO ERROR
        }
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }

        this.target = targetArray;
        this.selector = positionInt;
        this.content = sourceSequence;

    }

    @Override
    public void apply() {
        this.target.putItemsAt(this.content, this.selector.getIntValue() - 1);
    }

    @Override
    public boolean hasSelector() {
        return true;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public Item getSelector() {
        return this.selector;
    }

    @Override
    public int getIntSelector() {
        return this.selector.getIntValue();
    }

    @Override
    public List<Item> getContentList() {
        return this.content;
    }

    @Override
    public boolean isInsertArray() {
        return true;
    }

    public static List<Item> mergeSources(List<Item> first, List<Item> second) {
        List<Item> merged = new ArrayList<>(first);
        merged.addAll(second);
        return merged;
    }
}
