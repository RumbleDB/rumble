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

package org.rumbledb.runtime.cursor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class LegacyRuntimeIteratorCursorTest {

    @Test
    public void cursorsCreatedFromOnePrototypeExecuteIndependently() {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration();
        DynamicContext dynamicContext = new DynamicContext(configuration);
        RuntimeIterator prototype = new ConstantRuntimeIterator(
                ItemFactory.getInstance().createIntItem(42),
                createStaticContext(configuration)
        );
        Cursor<Item> first = prototype.getCursor(dynamicContext);
        Cursor<Item> second = prototype.getCursor(dynamicContext);

        try {
            assertEquals(42, first.next().getIntValue());
            assertEquals(42, second.next().getIntValue());
            assertFalse(first.hasNext());
            assertFalse(second.hasNext());
            assertFalse(prototype.isOpen(), "The legacy prototype must never be opened directly.");
        } finally {
            first.close();
            second.close();
        }
    }

    @Test
    public void cursorOpensOnFirstReadAndCloseIsIdempotent() {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration();
        DynamicContext dynamicContext = new DynamicContext(configuration);
        RuntimeIterator prototype = new ConstantRuntimeIterator(
                ItemFactory.getInstance().createIntItem(1),
                createStaticContext(configuration)
        );
        Cursor<Item> cursor = prototype.getCursor(dynamicContext);

        assertTrue(cursor.hasNext());
        cursor.close();
        assertDoesNotThrow(cursor::close);
        assertThrows(IteratorFlowException.class, cursor::hasNext);
    }

    @Test
    public void localExecutionRequiresTheLocalCapability() {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration();
        DynamicContext dynamicContext = new DynamicContext(configuration);
        RuntimeStaticContext staticContext = createStaticContext(configuration);
        RuntimePlan<Item> planWithoutLocalCapability = new RuntimePlan<>() {
            @Override
            public RuntimeStaticContext getRuntimeStaticContext() {
                return staticContext;
            }
        };

        OurBadException exception = assertThrows(
            OurBadException.class,
            () -> planWithoutLocalCapability.getCursor(dynamicContext)
        );
        assertTrue(exception.getMessage().contains("does not implement the corresponding capability"));
    }

    @Test
    public void atMostOneMaterializationDoesNotCreateACursor() throws Exception {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration();
        DynamicContext dynamicContext = new DynamicContext(configuration);
        DirectAtMostOnePlan plan = new DirectAtMostOnePlan(
                createStaticContext(configuration),
                ItemFactory.getInstance().createIntItem(7)
        );

        assertEquals(7, plan.materializeFirstOrNull(dynamicContext).getIntValue());
        assertEquals(7, plan.materializeAtMostOne(dynamicContext).getIntValue());
        assertEquals(1, plan.materialize(dynamicContext).size());
        assertTrue(plan.materializeAtMost(dynamicContext, 0).isEmpty());
        assertEquals(3, plan.evaluationCount);
        assertEquals(0, plan.cursorCreationCount);
    }

    private static final class DirectAtMostOnePlan extends RuntimePlan<Item>
            implements
                AtMostOneLocalRuntimePlan<Item> {

        private final RuntimeStaticContext staticContext;
        private final Item result;
        private int evaluationCount;
        private int cursorCreationCount;

        private DirectAtMostOnePlan(RuntimeStaticContext staticContext, Item result) {
            this.staticContext = staticContext;
            this.result = result;
        }

        @Override
        public Item evaluateAtMostOne(DynamicContext context) {
            this.evaluationCount++;
            return this.result;
        }

        @Override
        public Cursor<Item> createNativeCursor(DynamicContext context) {
            this.cursorCreationCount++;
            return new SingletonLocalCursor<>(this.result, this.staticContext.getMetadata());
        }

        @Override
        public RuntimeStaticContext getRuntimeStaticContext() {
            return this.staticContext;
        }
    }

    private static RuntimeStaticContext createStaticContext(RumbleRuntimeConfiguration configuration) {
        return RuntimeStaticContext.builder()
            .configuration(configuration)
            .staticType(new SequenceType(BuiltinTypesCatalogue.intItem))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(ExceptionMetadata.EMPTY_METADATA)
            .build();
    }
}
