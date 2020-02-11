package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TypePromotionClosure;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.Collections;

public class TypePromotionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final String _exceptionMessage;
    private RuntimeIterator _iterator;
    private SequenceType _sequenceType;

    private ItemType itemType;
    private String sequenceTypeName;

    private Item _nextResult;
    private int _childIndex;

    public TypePromotionIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this._exceptionMessage = exceptionMessage;
        this._iterator = iterator;
        this._sequenceType = sequenceType;
        this.itemType = this._sequenceType.getItemType();
        this.sequenceTypeName = ItemTypes.getItemTypeName(this.itemType.getType().toString());
    }

    @Override
    public boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    public void resetLocal(DynamicContext context) {
        this._iterator.reset(this._currentDynamicContextForLocalExecution);
        this._childIndex = 0;
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this._iterator.close();
    }

    @Override
    public void openLocal() {
        this._childIndex = 0;
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        this.setNextResult();
    }


    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item _currentResult = this._nextResult;
            setNextResult();
            return _currentResult;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private void setNextResult() {
        this._nextResult = null;
        if (this._iterator.hasNext()) {
            this._nextResult = this._iterator.next();
            this._childIndex++;
        } else {
            this._iterator.close();
            checkEmptySequence(this._childIndex);
        }

        this._hasNext = this._nextResult != null;
        if (!hasNext())
            return;

        checkItemsSize(this._childIndex);
        if (!this._nextResult.isTypeOf(this.itemType)) {
            checkTypePromotion();
        }
    }

    private void checkEmptySequence(long size) {
        if (
            size == 0
                && (this._sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this._sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            throw new UnexpectedTypeException(
                    this._exceptionMessage
                        + "Expecting"
                        + ((this._sequenceType.getArity() == SequenceType.Arity.OneOrMore) ? " at least" : "")
                        + " one item, but the value provided is the empty sequence.",
                    getMetadata()
            );
        }
    }

    private void checkItemsSize(long size) {
        if (size > 0 && this._sequenceType.isEmptySequence()) {
            throw new UnexpectedTypeException(
                    this._exceptionMessage
                        + "Expecting empty sequence, but the value provided has at least one item.",
                    getMetadata()
            );
        }


        if (
            size > 1
                && (this._sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this._sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw new UnexpectedTypeException(
                    this._exceptionMessage
                        + "Expecting"
                        + ((this._sequenceType.getArity() == SequenceType.Arity.OneOrZero) ? " at most" : "")
                        + " one item, but the value provided has at least two items.",
                    getMetadata()
            );
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this._iterator.getRDD(context);

        int count = childRDD.take(2).size();
        checkEmptySequence(count);
        checkItemsSize(count);
        Function<Item, Item> transformation = new TypePromotionClosure(
                this._exceptionMessage,
                this._sequenceType,
                getMetadata()
        );
        return childRDD.map(transformation);
    }

    private void checkTypePromotion() {
        if (this._nextResult.isFunction())
            return;
        if (!this._nextResult.canBePromotedTo(this._sequenceType.getItemType()))
            throw new UnexpectedTypeException(
                    this._exceptionMessage
                        + ItemTypes.getItemTypeName(this._nextResult.getClass().getSimpleName())
                        + " cannot be promoted to type "
                        + this.sequenceTypeName
                        + this._sequenceType.getArity().getSymbol()
                        + ".",
                    getMetadata()
            );
        this._nextResult = this._nextResult.promoteTo(this._sequenceType.getItemType());
    }
}
