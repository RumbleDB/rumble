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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CountFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator iterator = this.children.get(0);

        // the count($x) case is treated separately because we can short-circuit the
        // count, e.g., if it comes from the group-by aggregation of a non-grouping
        // key.
        if (iterator instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) iterator;
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
            return computeLocally(
                iterator,
                context,
                metadata
            );
        }
    }

    private static Item computeLocally(
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        iterator.open(context);
        long result = 0;

        while (iterator.hasNext()) {
            iterator.next();
            result += 1;
        }
        iterator.close();
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

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.COUNT);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
