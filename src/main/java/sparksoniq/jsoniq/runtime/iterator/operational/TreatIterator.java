package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TreatAsClosure;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.Collections;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private final SequenceType _sequenceType;
    private boolean _shouldThrowTreatException;

    private ItemType itemType;
    private String sequenceTypeName;

    private Item _nextResult;
    private Item _currentResult;
    private int _childIndex;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            boolean shouldThrowTreatException,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this._iterator = iterator;
        this._sequenceType = sequenceType;
        this._shouldThrowTreatException = shouldThrowTreatException;
        this.itemType = this._sequenceType.getItemType();
        this.sequenceTypeName = ItemTypes.getItemTypeName(this.itemType.getType().toString());
    }

    @Override
    public boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    public void resetLocal(DynamicContext context) {
        this._childIndex = 0;
        this._iterator.reset(this._currentDynamicContextForLocalExecution);
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
            this._currentResult = this._nextResult;
            setNextResult();
            return this._currentResult;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private void setNextResult() {
        this._nextResult = null;
        if (this._iterator.hasNext()) {
            if (this._iterator.isRDD()) {
                if (this._currentResult == null) {
                    JavaRDD<Item> childRDD = this._iterator.getRDD(this._currentDynamicContextForLocalExecution);
                    int size = childRDD.take(2).size();
                    checkMoreThanOneItemSequence(size);
                    this._nextResult = childRDD.first();
                } else {
                    this._nextResult = null;
                }
            } else {
                this._nextResult = this._iterator.next();
            }
            if (this._nextResult != null)
                this._childIndex++;
        } else {
            this._iterator.close();
            checkEmptySequence(this._childIndex);
        }

        this._hasNext = this._nextResult != null;
        if (!hasNext())
            return;

        checkTreatAsEmptySequence(this._childIndex);
        checkMoreThanOneItemSequence(this._childIndex);
        if (!this._nextResult.isTypeOf(this.itemType)) {
            String message = ItemTypes.getItemTypeName(this._nextResult.getClass().getSimpleName())
                + " cannot be treated as type "
                + this.sequenceTypeName
                + this._sequenceType.getArity().getSymbol();
            throw this._shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this._iterator.getRDD(dynamicContext);

        if (this._sequenceType.getArity() != SequenceType.Arity.ZeroOrMore)
            checkEmptySequence(childRDD.take(2).size());

        Function<Item, Boolean> transformation = new TreatAsClosure(this._sequenceType, getMetadata());
        return childRDD.filter(transformation);
    }

    private void checkEmptySequence(int size) {
        if (
            size == 0
                && (this._sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this._sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            String message = "Empty sequence cannot be treated as type "
                + this.sequenceTypeName
                + this._sequenceType.getArity().getSymbol();
            throw this._shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    private void checkTreatAsEmptySequence(int size) {
        if (size > 0 && this._sequenceType.isEmptySequence()) {
            String message = ItemTypes.getItemTypeName(this._nextResult.getClass().getSimpleName())
                + " cannot be treated as type empty-sequence()";
            throw this._shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    private void checkMoreThanOneItemSequence(int size) {
        if (
            size > 1
                && (this._sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this._sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            String message = "Sequences of more than one item cannot be treated as type "
                + this.sequenceTypeName
                + this._sequenceType.getArity().getSymbol();
            throw this._shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }
}

