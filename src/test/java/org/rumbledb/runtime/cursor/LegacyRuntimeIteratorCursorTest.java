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

import org.junit.jupiter.api.Test;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
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
        LocalCursor<Item> first = prototype.createLocalCursor(dynamicContext);
        LocalCursor<Item> second = prototype.createLocalCursor(dynamicContext);

        first.open();
        second.open();
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
    public void cursorIsSingleUseAndCloseIsIdempotent() {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration();
        DynamicContext dynamicContext = new DynamicContext(configuration);
        RuntimeIterator prototype = new ConstantRuntimeIterator(
                ItemFactory.getInstance().createIntItem(1),
                createStaticContext(configuration)
        );
        LocalCursor<Item> cursor = prototype.createLocalCursor(dynamicContext);

        assertThrows(IteratorFlowException.class, cursor::hasNext);
        cursor.open();
        cursor.close();
        assertDoesNotThrow(cursor::close);
        assertThrows(IteratorFlowException.class, cursor::hasNext);
        assertThrows(IteratorFlowException.class, cursor::open);
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
