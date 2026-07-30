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
 */

package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

/**
 * Shared plan for extracting one component from a date, time, dateTime, or duration value.
 */
public abstract class TemporalComponentFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    protected enum Component {
        YEAR {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createIntItem(item.getYear());
            }
        },
        MONTH {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createIntItem(item.getMonth());
            }
        },
        DAY {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createIntItem(item.getDay());
            }
        },
        HOUR {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createIntItem(item.getHour());
            }
        },
        MINUTE {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createIntItem(item.getMinute());
            }
        },
        SECOND {
            @Override
            Item evaluate(Item item) {
                return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(item.getSecond()));
            }
        },
        TIMEZONE {
            @Override
            Item evaluate(Item item) {
                if (!item.hasTimeZone()) {
                    return null;
                }
                return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofMinutes(item.getOffset()));
            }
        };

        abstract Item evaluate(Item item);
    }

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> argument;
    private final Component component;

    protected TemporalComponentFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext,
            Component component
    ) {
        super(arguments, staticContext);
        this.argument = arguments.get(0);
        this.component = component;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item item = this.argument.materializeFirstOrNull(context);
        return item == null ? null : this.component.evaluate(item);
    }
}
