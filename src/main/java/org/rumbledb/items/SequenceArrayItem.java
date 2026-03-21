package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenceArrayItem implements Item {

    private static final long serialVersionUID = 1L;
    private List<List<Item>> memberSequences;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private Collection collection;

    public SequenceArrayItem() {
        this.memberSequences = new ArrayList<>();
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.collection = null;
    }

    public SequenceArrayItem(List<List<Item>> memberSequences) {
        this.memberSequences = memberSequences;
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.collection = null;
    }

    @Override
    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isArray()) {
            return false;
        }
        if (getSize() != o.getSize()) {
            return false;
        }
        for (int i = 0; i < getSize(); ++i) {
            List<Item> thisMember = this.getSequenceAt(i);
            List<Item> otherMember = o.getSequenceAt(i);
            if (thisMember.size() != otherMember.size()) {
                return false;
            }
            for (int j = 0; j < thisMember.size(); j++) {
                if (!thisMember.get(j).equals(otherMember.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean allowsNonSingletons() {
        return true;
    }

    @Override
    public int getSize() {
        return this.memberSequences.size();
    }

    @Override
    public List<Item> getItems() {
        List<Item> flattened = new ArrayList<>();
        for (List<Item> sequence : this.memberSequences) {
            flattened.addAll(sequence);
        }
        return flattened;
    }

    @Override
    public List<List<Item>> getSequences() {
        return this.memberSequences;
    }

    @Override
    public Item getItemAt(int i) {
        if (i < 0 || i >= getSize()) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + (i + 1) + ", of array with length: " + getSize(),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        List<Item> member = this.memberSequences.get(i);
        if (member.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access empty member sequence at position: " + (i + 1),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (member.size() > 1) {
            throw new UnsupportedOperationException(
                    "getItemAt() is not defined for multi-item member sequences; use getSequenceAt(int) instead."
            );
        }
        return member.get(0);
    }

    @Override
    public List<Item> getSequenceAt(int i) {
        if (i < 0 || i >= getSize()) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + (i + 1) + ", of array with length: " + getSize(),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        return this.memberSequences.get(i);
    }

    @Override
    public void append(Item value) {
        this.memberSequences.add(Collections.singletonList(value));
    }

    @Override
    public void appendSequence(List<Item> items) {
        if (items == null) {
            this.memberSequences.add(Collections.<Item>emptyList());
        } else {
            this.memberSequences.add(items);
        }
    }

    @Override
    public void putItem(Item value) {
        this.memberSequences.add(Collections.singletonList(value));
    }

    @Override
    public void putItemAt(Item value, int i) {
        this.memberSequences.add(i, Collections.singletonList(value));
    }

    @Override
    public void putSequenceAt(List<Item> values, int i) {
        if (values == null) {
            this.memberSequences.set(i, Collections.<Item>emptyList());
        } else {
            this.memberSequences.set(i, values);
        }
    }

    @Override
    public void putItemsAt(List<Item> values, int i) {
        if (values == null) {
            this.memberSequences.add(i, Collections.<Item>emptyList());
        } else {
            this.memberSequences.add(i, values);
        }
    }

    @Override
    public void removeItemAt(int i) {
        this.memberSequences.remove(i);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.memberSequences);
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        kryo.writeObject(output, this.pathIn);
        kryo.writeObject(output, this.location);
        kryo.writeObjectOrNull(output, this.collection, Collection.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.memberSequences = kryo.readObject(input, ArrayList.class);
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = kryo.readObject(input, String.class);
        this.location = kryo.readObject(input, String.class);
        this.collection = kryo.readObjectOrNull(input, Collection.class);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += getSize();
        for (int i = 0; i < getSize(); ++i) {
            List<Item> member = this.memberSequences.get(i);
            for (Item item : member) {
                result += item.hashCode();
            }
        }
        return result;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.arrayItem;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return true;
    }

    @Override
    public int getMutabilityLevel() {
        return this.mutabilityLevel;
    }

    @Override
    public void setMutabilityLevel(int mutabilityLevel) {
        this.mutabilityLevel = mutabilityLevel;
        for (List<Item> member : this.memberSequences) {
            for (Item item : member) {
                item.setMutabilityLevel(mutabilityLevel);
            }
        }
    }

    @Override
    public long getTopLevelID() {
        return this.topLevelID;
    }

    @Override
    public void setTopLevelID(long topLevelID) {
        this.topLevelID = topLevelID;
        for (List<Item> member : this.memberSequences) {
            for (Item item : member) {
                item.setTopLevelID(topLevelID);
            }
        }
    }

    @Override
    public String getPathIn() {
        return this.pathIn;
    }

    @Override
    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
        for (int i = 0; i < this.memberSequences.size(); i++) {
            List<Item> member = this.memberSequences.get(i);
            for (Item item : member) {
                item.setPathIn(pathIn + "[" + i + "]");
            }
        }
    }

    @Override
    public String getTableLocation() {
        return this.location;
    }

    @Override
    public void setTableLocation(String location) {
        this.location = location;
        for (List<Item> member : this.memberSequences) {
            for (Item item : member) {
                item.setTableLocation(location);
            }
        }
    }

    @Override
    public String getSparkSQLValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("array(");
        for (int i = 0; i < this.memberSequences.size(); i++) {
            List<Item> member = this.memberSequences.get(i);
            if (member.isEmpty()) {
                sb.append("NULL");
            } else if (member.size() == 1) {
                sb.append(member.get(0).getSparkSQLValue());
            } else {
                // Fallback: use JSON representation of the member sequence as a scalar.
                // This keeps Spark integration conservative for now.
                List<Item> asSequence = member;
                Item sequenceWrapper = ItemFactory.getInstance()
                    .createArrayItem(asSequence, false);
                sb.append(sequenceWrapper.getSparkSQLValue());
            }
            if (i + 1 < this.memberSequences.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        // For now, delegate to the untyped Spark SQL value; tighter typing for
        // sequence-valued members will be handled in a later phase.
        return this.getSparkSQLValue();
    }

    @Override
    public String getSparkSQLType() {
        // Keep the same ARRAY<...> typing as ArrayItem for now, based on the first
        // non-empty member sequence (if any).
        StringBuilder sb = new StringBuilder();
        sb.append("ARRAY<");
        Item firstElement = null;
        for (List<Item> member : this.memberSequences) {
            if (!member.isEmpty()) {
                firstElement = member.get(0);
                break;
            }
        }
        if (firstElement == null) {
            sb.append("STRING");
        } else {
            sb.append(firstElement.getSparkSQLType());
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public List<Item> atomizedValue() {
        throw new CannotAtomizeException("tried to atomize Array", ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public Object getVariantValue() {
        List<Object> arrayItemsForRow = new ArrayList<>(this.memberSequences.size());
        for (List<Item> member : this.memberSequences) {
            if (member.isEmpty()) {
                arrayItemsForRow.add(null);
            } else if (member.size() == 1) {
                arrayItemsForRow.add(member.get(0).getVariantValue());
            } else {
                List<Object> nested = new ArrayList<>(member.size());
                for (Item item : member) {
                    nested.add(item.getVariantValue());
                }
                arrayItemsForRow.add(nested);
            }
        }
        return arrayItemsForRow;
    }

    @Override
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    public Collection getCollection() {
        return this.collection;
    }
}

