package org.rumbledb.context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GlobalVariables implements Serializable, KryoSerializable {
    private Set<Name> globalVariables;

    public GlobalVariables() {
        this.globalVariables = new HashSet<>();
    }

    public void addGlobalVariable(Name globalVariable) {
        if (this.globalVariables.contains(globalVariable)) {
            throw new OurBadException("Attempting to register global variable a second time.");
        }
        this.globalVariables.add(globalVariable);
    }

    public boolean isGlobalVariable(Name globalVariable) {
        return this.globalVariables.contains(globalVariable);
    }

    public Set<Name> getGlobalVariables() {
        return this.globalVariables;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.globalVariables);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.globalVariables = kryo.readObject(input, HashSet.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global variables:\n");
        this.globalVariables.forEach(variable -> sb.append(variable).append("\n"));
        return sb.toString();
    }
}
