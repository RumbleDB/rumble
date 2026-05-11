package org.rumbledb.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class InScopeSchemaTypes implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    private HashMap<Name, ItemType> inScopeSchemaTypes;

    public InScopeSchemaTypes() {
        this.inScopeSchemaTypes = new HashMap<>();
    }

    public void clearInScopeSchemaTypes() {
        this.inScopeSchemaTypes.clear();
    }

    public void addInScopeSchemaType(ItemType type, ExceptionMetadata meta) {
        if (!type.hasName()) {
            throw new InvalidSchemaException("A top-level user-defined type must have a name.", meta);
        }
        if (
            BuiltinTypesCatalogue.typeExists(type.getName())
                || this.inScopeSchemaTypes.containsKey(type.getName())
        ) {
            throw new InvalidSchemaException("This type is already defined: " + type.getName(), meta);
        }
        this.inScopeSchemaTypes.put(type.getName(), type);
    }

    public boolean checkInScopeSchemaTypeExists(Name name) {
        if (BuiltinTypesCatalogue.typeExists(name)) {
            return true;
        }
        return this.inScopeSchemaTypes.containsKey(name);
    }

    public ItemType getInScopeSchemaType(Name name) {
        if (
            BuiltinTypesCatalogue.typeExists(name)
        ) {
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        return this.inScopeSchemaTypes.get(name);
    }

    public List<ItemType> getInScopeSchemaTypes() {
        return new ArrayList<ItemType>(this.inScopeSchemaTypes.values());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.inScopeSchemaTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.inScopeSchemaTypes = kryo.readObject(input, HashMap.class);
    }

    public void importModuleTypes(InScopeSchemaTypes inScopeSchemaTypes) {
        for (Name name : inScopeSchemaTypes.inScopeSchemaTypes.keySet()) {
            ItemType itemType = inScopeSchemaTypes.inScopeSchemaTypes.get(name);
            this.inScopeSchemaTypes.put(name, itemType);
        }
    }


}
