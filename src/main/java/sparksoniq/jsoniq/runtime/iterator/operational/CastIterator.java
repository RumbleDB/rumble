package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.HashMap;
import java.util.Map;

public class CastIterator extends UnaryOperationIterator{
    private static final long serialVersionUID = 1L;
    private final AtomicType _atomicType;

    private static final Map<String, Class> PRIMITIVE_NAME_TYPE_MAP = new HashMap<>();

    static {
        PRIMITIVE_NAME_TYPE_MAP.put("JSONItem", JsonItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("ObjectItem", ObjectItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("ArrayItem", ArrayItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("AtomicItem", AtomicItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("StringItem", StringItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("IntegerItem", IntegerItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DecimalItem", DecimalItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DoubleItem", DoubleItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("BooleanItem", BooleanItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("NullItem", NullItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DurationItem", DurationItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("YearMonthDurationItem", YearMonthDurationItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DayTimeDurationItem", DayTimeDurationItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DateTimeItem", DateTimeItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("DateItem", DateItem.class);
        PRIMITIVE_NAME_TYPE_MAP.put("TimeItem", TimeItem.class);
    }

    public CastIterator(RuntimeIterator child, AtomicType atomicType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.CAST, iteratorMetadata);
        this._atomicType = atomicType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item item = null;

            _child.open(_currentDynamicContext);
            if (_child.hasNext()) {
                item = _child.next();
            }
            _child.close();
            this._hasNext = false;

            if (item == null) return null;

            String itemType = ItemTypes.getItemTypeName(item.getClass().getSimpleName());
            String targetType = ItemTypes.getItemTypeName(_atomicType.getType().toString());

            if (itemType.equals(targetType)) return item;
            if (item.isNull() && _atomicType.getType() == AtomicTypes.StringItem) {
                return ItemFactory.getInstance().createStringItem(item.serialize());
            }

            String message = String.format("\"%s\": value of type %s is not castable to type %s",
                    item.serialize(), itemType, targetType);

            AtomicItem atomicItem = CastableIterator.checkInvalidCastable(item, getMetadata(), _atomicType);

            Class classToLoad = PRIMITIVE_NAME_TYPE_MAP.get(_atomicType.getType().toString());
            assert classToLoad != null;

            if (atomicItem.isCastableAs(_atomicType)) {
                try {
                    return atomicItem.castAs((AtomicItem) classToLoad.newInstance());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
                    throw new CastException(message, getMetadata());
                }

            }
            throw new CastException(message, getMetadata());
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }


    @Override
    public Item getResultIfEmpty() {
        if (_atomicType.getZeroOrOne()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }
}