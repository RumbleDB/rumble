package org.rumbledb.runtime.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.TreatAsClosure;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.Collections;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private final SequenceType sequenceType;
    private boolean shouldThrowTreatException;

    private ItemType itemType;
    private String sequenceTypeName;

    private Item nextResult;
    private Item currentResult;
    private int resultCount;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            boolean shouldThrowTreatException,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.shouldThrowTreatException = shouldThrowTreatException;
        if (!this.sequenceType.isEmptySequence()) {
            this.itemType = this.sequenceType.getItemType();
            this.sequenceTypeName = this.itemType.toString();
        }
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public void resetLocal(DynamicContext context) {
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
            String message = this.nextResult.getDynamicType().toString()
                + " cannot be treated as type "
                + this.sequenceTypeName
                + this.sequenceType.getArity().getSymbol();
            throw this.shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (this.sequenceType.getArity() != SequenceType.Arity.ZeroOrMore) {
            checkEmptySequence(childRDD.take(2).size());
        }

        Function<Item, Boolean> transformation = new TreatAsClosure(this.sequenceType, getMetadata());
        return childRDD.filter(transformation);
    }

    private void checkEmptySequence(int size) {
        if (
            size == 0
                && !this.sequenceType.isEmptySequence()
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            String message = "Empty sequence cannot be treated as type "
                + this.sequenceTypeName
                + this.sequenceType.getArity().getSymbol();
            throw this.shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    private void checkTreatAsEmptySequence(int size) {
        if (size > 0 && this.sequenceType.isEmptySequence()) {
            String message = this.nextResult.getDynamicType().toString()
                + " cannot be treated as type empty-sequence()";
            throw this.shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    private void checkMoreThanOneItemSequence(int size) {
        if (
            size > 1
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            String message = "Sequences of more than one item cannot be treated as type "
                + this.sequenceTypeName
                + this.sequenceType.getArity().getSymbol();
            throw this.shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }
}

