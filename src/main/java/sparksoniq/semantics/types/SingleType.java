package sparksoniq.semantics.types;


import java.io.Serializable;

public class SingleType implements Serializable {

    private static final long serialVersionUID = 1L;
    private final AtomicTypes _type;
    private boolean _zeroOrOne;

    public SingleType(AtomicTypes type) {
        this._type = type;
        this._zeroOrOne = false;
    }

    public SingleType(AtomicTypes itemType, boolean zeroOrOne) {
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
