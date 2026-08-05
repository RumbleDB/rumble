package org.rumbledb.context;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.exceptions.OurBadException;

@Getter
public class GlobalVariables implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final Set<Name> globalVariables;

    public GlobalVariables() {
        this.globalVariables = new HashSet<>();
    }

    public void addGlobalVariable(Name globalVariable) {
        if (this.globalVariables.contains(globalVariable)) {
            throw new OurBadException("Attempting to register global variable a second time.");
        }
        this.globalVariables.add(globalVariable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global variables:\n");
        this.globalVariables.forEach(variable -> sb.append(variable).append("\n"));
        return sb.toString();
    }
}
