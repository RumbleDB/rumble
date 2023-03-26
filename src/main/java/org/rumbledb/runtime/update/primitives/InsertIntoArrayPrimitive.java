package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

import java.util.List;

public class InsertIntoArrayPrimitive extends UpdatePrimitive {

    private ArrayItem targetArray;

    private IntItem positionInt;

    private List<Item> sourceSequence;

    public InsertIntoArrayPrimitive(ArrayItem targetArray, IntItem positionInt, List<Item> sourceSequence) {
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            //TODO throw error or do nothing?
        }
        this.targetArray = targetArray;
        this.positionInt = positionInt;
        this.sourceSequence = sourceSequence;
    }

    public ArrayItem getTargetArray() {
        return targetArray;
    }

    public IntItem getPositionInt() {
        return positionInt;
    }

    public List<Item> getSourceSequence() {
        return sourceSequence;
    }

    @Override
    public void apply() {
        this.targetArray.putItemsAt(this.sourceSequence, this.positionInt.getIntValue());
    }
}
