/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.sequences.aggregate;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public CountFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        RuntimeIterator child = getChild(0);
        Name countedVariable = child instanceof VariableReferenceIterator variable
            ? variable.getVariableName()
            : null;
        return new Cursor(child, countedVariable, context, getMetadata());
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator iterator = this.getChild(0);

        // the count($x) case is treated separately because we can short-circuit the
        // count, e.g., if it comes from the group-by aggregation of a non-grouping
        // key.
        if (iterator instanceof VariableReferenceIterator expr) {
            // this.hasNext = false;
            return context.getVariableValues()
                .getVariableCount(expr.getVariableName(), getMetadata());
        }
        return computeCount(
            iterator,
            context,
            getMetadata()
        );

    }

    public static Item computeCount(
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        if (iterator.isDataFrame()) {
            return computeDataFrame(
                iterator,
                context,
                metadata
            );
        } else if (iterator.isRDDOrDataFrame()) {
            return computeRDD(
                iterator,
                context,
                metadata
            );
        } else {
            return computeLocalCount(
                iterator,
                context
            );
        }
    }

    private static Item computeLocalCount(RuntimePlan<Item> plan, DynamicContext context) {
        long result = 0;
        try (LocalCursor<Item> cursor = plan.createLocalCursor(context)) {
            cursor.open();
            while (cursor.hasNext()) {
                cursor.next();
                result++;
            }
        }
        return ItemFactory.getInstance().createLongItem(result);
    }

    private static Item computeRDD(
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        long count = iterator.getRDD(context).count();
        if (count > (long) Integer.MAX_VALUE) {
            throw new OurBadException("The count value is too big to convert to integer type.");
        } else {
            return ItemFactory.getInstance().createLongItem(count);
        }
    }

    private static Item computeDataFrame(
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        long count = iterator.getDataFrame(context).count();
        if (count > (long) Integer.MAX_VALUE) {
            throw new OurBadException("The count value is too big to convert to integer type.");
        } else {
            return ItemFactory.getInstance().createLongItem(count);
        }

    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.getChild(0) instanceof VariableReferenceIterator expr) {
            Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.COUNT);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext nativeChildQuery = this.getChild(0).generateNativeQuery(nativeClauseContext);
        if (nativeChildQuery != NativeClauseContext.NoNativeQuery) {
            if (nativeChildQuery.getResultingQuery().trim().startsWith("explode")) {
                return new NativeClauseContext(
                        nativeClauseContext,
                        "size"
                            + nativeChildQuery.getResultingQuery()
                                .substring(nativeChildQuery.getResultingQuery().indexOf("explode") + 7),
                        new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One)
                );
            } else if (nativeChildQuery.getResultingQuery().contains(".count")) {
                return nativeChildQuery;
            } else if (nativeChildQuery.getResultingType().getArity().equals(SequenceType.Arity.One)) {
                return new NativeClauseContext(
                        nativeChildQuery,
                        "1",
                        new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One)
                );
            } else if (nativeChildQuery.getResultingType().getArity().equals(SequenceType.Arity.OneOrZero)) {
                return new NativeClauseContext(
                        nativeChildQuery,
                        "CASE WHEN (" + nativeChildQuery.getResultingQuery() + ") IS NULL THEN 0 ELSE 1 END",
                        new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One)
                );
            } else {
                return new NativeClauseContext(
                        nativeChildQuery,
                        "size (" + nativeChildQuery.getResultingQuery() + ")",
                        new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One)
                );
            }
        }
        return NativeClauseContext.NoNativeQuery;
    }

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final Name countedVariable;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;

        private Cursor(
                @NonNull RuntimePlan<Item> childPlan,
                Name countedVariable,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            this.childPlan = childPlan;
            this.countedVariable = countedVariable;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            if (this.countedVariable != null) {
                return this.context.getVariableValues()
                    .getVariableCount(this.countedVariable, this.metadata);
            }
            return computeLocalCount(this.childPlan, this.context);
        }
    }
}
