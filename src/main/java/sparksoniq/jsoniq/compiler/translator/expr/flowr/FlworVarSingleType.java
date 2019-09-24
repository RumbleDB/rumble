package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.*;

import java.util.ArrayList;
import java.util.List;

public class FlworVarSingleType extends Expression {

    private AtomicType _atomicType;
    private boolean isEmpty;

    public FlworVarSingleType(ExpressionMetadata metadata) {
        super(metadata);
        this._atomicType = new AtomicType(AtomicTypes.AtomicItem);
        this.isEmpty = true;
    }
    public FlworVarSingleType(AtomicTypes atomicType, ExpressionMetadata metadata) {
        super(metadata);
        this._atomicType = new AtomicType(atomicType);
    }

    public FlworVarSingleType(AtomicTypes atomicType, boolean zeroOrOne, ExpressionMetadata metadata) {
        super(metadata);
        this._atomicType = new AtomicType(atomicType, zeroOrOne);
        this.isEmpty = false;

    }

    public static AtomicTypes getAtomicType(String text) {
        text = text.toLowerCase();
        switch (text) {
            case "string":
                return AtomicTypes.StringItem;
            case "integer":
                return AtomicTypes.IntegerItem;
            case "decimal":
                return AtomicTypes.DecimalItem;
            case "double":
                return AtomicTypes.DoubleItem;
            case "boolean":
                return AtomicTypes.BooleanItem;
            case "duration":
                return AtomicTypes.DurationItem;
            case "yearmonthduration":
                return AtomicTypes.YearMonthDurationItem;
            case "daytimeduration":
                return AtomicTypes.DayTimeDurationItem;
            default:
                return AtomicTypes.AtomicItem;
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public AtomicType getAtomicType() {
        return _atomicType;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        return new ArrayList<>();
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(atomicType ";
        result += "(itemType ";
        result += getSerializationOfAtomicType(this._atomicType.getType());
        result += "))";
        return result;
    }

    private String getSerializationOfAtomicType(AtomicTypes item) {
        switch (item) {
            case IntegerItem:
                return "(atomicType integer)";
            case DecimalItem:
                return "(atomicType decimal)";
            case DoubleItem:
                return "(atomicType double)";
            case StringItem:
                return "(atomicType string)";
            case BooleanItem:
                return "(atomicType boolean)";
            default:
                return "item";
        }
    }
}
