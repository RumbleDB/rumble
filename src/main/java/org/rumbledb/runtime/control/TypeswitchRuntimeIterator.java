package org.rumbledb.runtime.control;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TypeswitchRuntimeIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeswitchRuntimeIteratorCase> cases;
    private final TypeswitchRuntimeIteratorCase defaultCase;

    public TypeswitchRuntimeIterator(
            RuntimeIterator test,
            List<TypeswitchRuntimeIteratorCase> cases,
            TypeswitchRuntimeIteratorCase defaultCase,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.concat(
                Stream.concat(
                    Stream.of(test),
                    cases.stream().map(TypeswitchRuntimeIteratorCase::getReturnIterator)
                ),
                Stream.of(defaultCase.getReturnIterator())
            ).toList(),
            staticContext
        );

        this.testField = test;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new TypeswitchLocalCursor(this.testField, this.cases, this.defaultCase, context, getMetadata());
    }

    private static final class TypeswitchLocalCursor extends AbstractLocalCursor<Item> {
        private final RuntimeIterator testPlan;
        private final List<TypeswitchRuntimeIteratorCase> cases;
        private final TypeswitchRuntimeIteratorCase defaultCase;
        private final DynamicContext context;
        private LocalCursor<Item> selected;

        private TypeswitchLocalCursor(
                RuntimeIterator testPlan,
                List<TypeswitchRuntimeIteratorCase> cases,
                TypeswitchRuntimeIteratorCase defaultCase,
                DynamicContext context,
                org.rumbledb.exceptions.ExceptionMetadata metadata
        ) {
            super(metadata);
            this.testPlan = testPlan;
            this.cases = cases;
            this.defaultCase = defaultCase;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            Match match = selectMatch();
            bindMatch(match, this.context);
            this.selected = match.typeSwitchCase.getReturnIterator().createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.selected.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.selected.next();
        }

        @Override
        protected void closeLocal() {
            if (this.selected != null) {
                this.selected.close();
                this.selected = null;
            }
        }

        private Match selectMatch() {
            Item testValue = this.testPlan.materializeFirstOrNull(this.context);
            for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
                if (doesTypeMatch(typeSwitchCase, testValue)) {
                    return new Match(typeSwitchCase, testValue);
                }
            }
            return new Match(this.defaultCase, testValue);
        }
    }

    private static final class Match {
        private final TypeswitchRuntimeIteratorCase typeSwitchCase;
        private final Item testValue;

        private Match(TypeswitchRuntimeIteratorCase typeSwitchCase, Item testValue) {
            this.typeSwitchCase = typeSwitchCase;
            this.testValue = testValue;
        }
    }

    private Match selectMatch(DynamicContext context) {
        Item testValue = this.testField.materializeFirstOrNull(context);
        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            if (doesTypeMatch(typeSwitchCase, testValue)) {
                return new Match(typeSwitchCase, testValue);
            }
        }
        return new Match(this.defaultCase, testValue);
    }

    private static void bindMatch(Match match, DynamicContext context) {
        if (match.typeSwitchCase.getVariableName() != null) {
            context.getVariableValues()
                .addVariableValue(
                    match.typeSwitchCase.getVariableName(),
                    Collections.singletonList(match.testValue)
                );
        }
    }

    private static boolean doesTypeMatch(TypeswitchRuntimeIteratorCase typeSwitchCase, Item testValue) {
        if (typeSwitchCase.getSequenceTypeUnion() != null) {
            for (SequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
                if (testValue == null && sequenceType.isEmptySequence()) {
                    return true;
                }
                if (
                    testValue != null
                        && InstanceOfIterator.doesItemTypeMatchItem(sequenceType.getItemType(), testValue)
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        Match match = selectMatch(dynamicContext);
        bindMatch(match, dynamicContext);
        return match.typeSwitchCase.getReturnIterator().getRDD(dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        Match match = selectMatch(context);
        bindMatch(match, context);
        return match.typeSwitchCase.getReturnIterator().getPendingUpdateList(context);
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext context) {
        Match match = selectMatch(context);
        bindMatch(match, context);
        return match.typeSwitchCase.getReturnIterator().getDataFrame(context);
    }
}
