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
 * Authors: OpenAI Codex
 *
 */

package org.rumbledb.runtime.functions.sequences.aggregate;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

final class AggregateSemanticsHelper {

    private enum SumDomain {
        NUMERIC,
        YEAR_MONTH_DURATION,
        DAY_TIME_DURATION
    }

    private AggregateSemanticsHelper() {
    }

    static List<Item> materializeItemsForSumAndAvg(
            RuntimeIterator iterator,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        iterator.open(context);
        try {
            List<Item> items = new ArrayList<>();
            SumDomain domain = null;
            while (iterator.hasNext()) {
                Item item = normalizeAggregateItem(iterator.next(), metadata);
                domain = checkAndRefineSumDomain(domain, item, metadata);
                items.add(item);
            }
            return items;
        } finally {
            iterator.close();
        }
    }

    private static Item normalizeAggregateItem(Item item, ExceptionMetadata metadata) {
        if (item == null || item.isNull()) {
            throw new InvalidArgumentTypeException(
                    "Aggregate functions require atomic values in a homogeneous numeric or duration domain.",
                    metadata
            );
        }
        if (!item.isAtomic()) {
            throw new InvalidArgumentTypeException(
                    "Aggregate functions require atomic values in a homogeneous numeric or duration domain.",
                    metadata
            );
        }
        if (item.isUntypedAtomic()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        return item;
    }

    private static SumDomain checkAndRefineSumDomain(
            SumDomain currentDomain,
            Item item,
            ExceptionMetadata metadata
    ) {
        if (item.isNumeric()) {
            if (currentDomain == null || currentDomain == SumDomain.NUMERIC) {
                return SumDomain.NUMERIC;
            }
            throw incompatibleAggregateInput(item, metadata);
        }
        if (item.isYearMonthDuration()) {
            if (currentDomain == null || currentDomain == SumDomain.YEAR_MONTH_DURATION) {
                return SumDomain.YEAR_MONTH_DURATION;
            }
            throw incompatibleAggregateInput(item, metadata);
        }
        if (item.isDayTimeDuration()) {
            if (currentDomain == null || currentDomain == SumDomain.DAY_TIME_DURATION) {
                return SumDomain.DAY_TIME_DURATION;
            }
            throw incompatibleAggregateInput(item, metadata);
        }
        throw incompatibleAggregateInput(item, metadata);
    }

    private static InvalidArgumentTypeException incompatibleAggregateInput(
            Item item,
            ExceptionMetadata metadata
    ) {
        return new InvalidArgumentTypeException(
                "Aggregate functions require all items to be numeric, all xs:yearMonthDuration, or all xs:dayTimeDuration. Found "
                    + item.getDynamicType()
                    + ".",
                metadata
        );
    }
}
