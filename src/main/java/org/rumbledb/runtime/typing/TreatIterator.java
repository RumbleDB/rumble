package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.TreatAsClosure;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.Collections;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private final SequenceType sequenceType;
    private ErrorCode errorCode;

    private ItemType itemType;

    private Item nextResult;
    private Item currentResult;
    private int resultCount;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            ErrorCode errorCode,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.errorCode = errorCode;
        if (!this.sequenceType.isEmptySequence()) {
            this.itemType = this.sequenceType.getItemType();
        }
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public void resetLocal() {
        this.resultCount = 0;
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this.iterator.close();
    }

    @Override
    public void openLocal() {
        this.resultCount = 0;
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            this.currentResult = this.nextResult;
            setNextResult();
            return this.currentResult;
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    private void setNextResult() {
        this.nextResult = null;
        if (this.iterator.hasNext()) {
            if (this.iterator.isRDD()) {
                if (this.currentResult == null) {
                    JavaRDD<Item> childRDD = this.iterator.getRDD(this.currentDynamicContextForLocalExecution);
                    int size = childRDD.take(2).size();
                    checkMoreThanOneItemSequence(size);
                    this.nextResult = childRDD.first();
                } else {
                    this.nextResult = null;
                }
            } else {
                this.nextResult = this.iterator.next();
            }
            if (this.nextResult != null) {
                this.resultCount++;
            }
        } else {
            this.iterator.close();
            checkEmptySequence(this.resultCount);
        }

        this.hasNext = this.nextResult != null;
        if (!hasNext()) {
            return;
        }

        checkTreatAsEmptySequence(this.resultCount);
        checkMoreThanOneItemSequence(this.resultCount);
        if (!this.nextResult.isTypeOf(this.itemType)) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            this.nextResult.getDynamicType().toString()
                                + " cannot be treated as type "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            this.nextResult.getDynamicType().toString()
                                + " is not expected here. The expected type is "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.getMetadata());
            }
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (this.sequenceType.getArity() != SequenceType.Arity.ZeroOrMore) {
            checkEmptySequence(childRDD.take(2).size());
        }

        Function<Item, Boolean> transformation = new TreatAsClosure(
                this.sequenceType,
                this.errorCode,
                getMetadata()
        );
        return childRDD.filter(transformation);
    }

    @Override
    public String generateNativeQuery() {
        // TODO: do treat check how?
        return this.iterator.generateNativeQuery();
    }

    private void checkEmptySequence(int size) {
        if (
            size == 0
                && !this.sequenceType.isEmptySequence()
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            "Empty sequence cannot be treated as type "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            "An empty sequence is not expected here. The expected type is "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.getMetadata());
            }
        }
    }

    private void checkTreatAsEmptySequence(int size) {
        if (size > 0 && this.sequenceType.isEmptySequence()) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            this.nextResult.getDynamicType().toString()
                                + " cannot be treated as type empty-sequence()",
                            this.getMetadata()
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            this.nextResult.getDynamicType().toString()
                                + " is not expected here. The expected type is empty-sequence().",
                            this.getMetadata()
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.getMetadata());
            }
        }
    }

    private void checkMoreThanOneItemSequence(int size) {
        if (
            size > 1
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            "A sequence of more than one item cannot be treated as type "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            "A sequence of more than one item is not expected here. Expected type: "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.getMetadata()
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.getMetadata());
            }
        }
    }
}

