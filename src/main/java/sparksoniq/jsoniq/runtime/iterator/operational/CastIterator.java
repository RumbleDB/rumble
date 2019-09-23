package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.JsonItem;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    }

    public CastIterator(RuntimeIterator child, AtomicType atomicType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.CASTABLE, iteratorMetadata);
        this._atomicType = atomicType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> items = new ArrayList<>();
            _child.open(_currentDynamicContext);
            while (_child.hasNext())
                items.add(_child.next());
            _child.close();
            this._hasNext = false;

            String targetType = ItemTypes.getItemTypeName(_atomicType.getType().toString());

            if (items.size() > 1)
                throw new UnexpectedTypeException(" Sequence of more than one item can not be treated as type "
                        + targetType, getMetadata());


            if (items.isEmpty() || items.get(0) == null) return null;
            Item item = items.get(0);
            String itemType = ItemTypes.getItemTypeName(item.getClass().getSimpleName());



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
                } catch (InstantiationException | IllegalAccessException e) {
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
            return ItemFactory.getInstance().createStringItem("");
        }
        throw new UnexpectedTypeException(" Empty sequence can not be cast to type with quantifier '1'", getMetadata());
    }
}