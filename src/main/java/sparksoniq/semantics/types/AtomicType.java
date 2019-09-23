package sparksoniq.semantics.types;


import java.io.Serializable;

public class AtomicType implements Serializable {

    private static final long serialVersionUID = 1L;
    private final AtomicTypes _type;
    private boolean _zeroOrOne;

    public AtomicType(AtomicTypes type) {
        this._type = type;
        this._zeroOrOne = false;
    }

    public AtomicType(AtomicTypes itemType, boolean zeroOrOne) {
        this._type = itemType;
        this._zeroOrOne = zeroOrOne;
    }

    public AtomicTypes getType() {
        return _type;
    }

    public boolean getZeroOrOne() {
        return _zeroOrOne;
    }


}
