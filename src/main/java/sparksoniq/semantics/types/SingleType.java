package sparksoniq.semantics.types;


import java.io.Serializable;

public class SingleType implements Serializable {

    private static final long serialVersionUID = 1L;
    private final AtomicTypes type;
    private boolean zeroOrOne;

    public SingleType(AtomicTypes type) {
        this.type = type;
        this.zeroOrOne = false;
    }

    public SingleType(AtomicTypes itemType, boolean zeroOrOne) {
        this.type = itemType;
        this.zeroOrOne = zeroOrOne;
    }

    public AtomicTypes getType() {
        return this.type;
    }

    public boolean getZeroOrOne() {
        return this.zeroOrOne;
    }
}
