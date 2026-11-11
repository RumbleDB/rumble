/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.sequences.general;

import java.io.Serial;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

public class DataFunctionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;

    public DataFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChildren().isEmpty() ? null : this.getChild(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.sequenceIterator, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        FlatMapFunction<Item, Item> transformation = new AtomizationClosure();
        return childRDD.flatMap(transformation);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame childDF =
                ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.sequenceIterator, dynamicContext);
        // Atomic unions such as xs:decimal | js:null atomize to themselves too.
        if (childDF.getItemType().isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            return childDF;
        }
        if (childDF.isEmptySequence()) {
            return childDF;
        }
        if (childDF.getItemType().isObjectItemType()) {
            throw new CannotAtomizeException("Cannot atomize objects. Type: " + childDF.getItemType(), getMetadata());
        }
        if (childDF.getItemType().isArrayItemType()) {
            throw new CannotAtomizeException("Cannot atomize arrays. Type: " + childDF.getItemType(), getMetadata());
        }
        throw new CannotAtomizeException("Cannot atomize. Type: " + childDF.getItemType(), getMetadata());
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> inputCursor;
        private Iterator<Item> atomizedValues;

        private EvaluationCursor(
                ItemRuntimePlan sequencePlan, @NonNull DynamicContext context, @NonNull ExceptionMetadata metadata) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.atomizedValues = Collections.emptyIterator();
            if (this.sequencePlan != null) {
                this.inputCursor = this.sequencePlan.getCursor(this.context);
            } else {
                List<Item> contextItems =
                        this.context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, this.metadata);
                if (contextItems.size() != 1) {
                    throw new OurBadException("The context item is not a singleton.", this.metadata);
                }
                this.inputCursor = new AtMostOneLocalCursor<>(contextItems.get(0), this.metadata);
            }
        }

        @Override
        protected boolean hasNextLocal() {
            while (!this.atomizedValues.hasNext() && this.inputCursor.hasNext()) {
                this.atomizedValues = atomize(this.inputCursor.next()).iterator();
            }
            return this.atomizedValues.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw exhausted();
            }
            return this.atomizedValues.next();
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
                this.inputCursor = null;
            }
            this.atomizedValues = Collections.emptyIterator();
        }

        private List<Item> atomize(Item item) {
            try {
                return item.atomizedValue();
            } catch (CannotAtomizeException exception) {
                if (this.sequencePlan == null) {
                    throw exception;
                }
                throw new CannotAtomizeException("The sequence cannot be atomized.", this.metadata);
            }
        }

        private RuntimeException exhausted() {
            return new IteratorFlowException(
                    IteratorFlowException.FLOW_EXCEPTION_MESSAGE + " atomization iterator", this.metadata);
        }
    }

    /**
     * The atomized type of the argument. Rejects an element whose schema type permits content without a typed value.
     */
    public static Optional<SequenceType> inferReturnType(FunctionCallExpression call, ReturnTypeContext context) {
        SequenceType input = call.getArguments().get(0).getStaticSequenceType();
        if (!input.isEmptySequence()) {
            for (ItemType member : input.getItemType().getMemberTypes()) {
                if (member instanceof ElementNodeItemType element
                        && element.getSchemaTypeName() != null
                        && context.staticContext()
                                .getInScopeSchemaTypes()
                                .getXmlSchemaCatalog()
                                .mayHaveUnavailableTypedValue(element.getSchemaTypeName())) {
                    context.errors()
                            .report(
                                    "The schema type of "
                                            + member
                                            + " allows content without a typed value, so it may not be"
                                            + " atomized.",
                                    ErrorCode.AtomizationError,
                                    call);
                }
            }
        }
        return Optional.of(TypeAtomization.inferType(input));
    }
}
