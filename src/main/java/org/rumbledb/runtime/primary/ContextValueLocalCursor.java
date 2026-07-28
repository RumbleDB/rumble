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

package org.rumbledb.runtime.primary;

import java.util.List;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;

/**
 * Local cursor for singleton values supplied by the dynamic context.
 */
public final class ContextValueLocalCursor extends AtMostOneLocalCursor<Item> {

    private enum Kind {
        ITEM,
        POSITION,
        LAST
    }

    private final DynamicContext context;
    private final Kind kind;

    private ContextValueLocalCursor(
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata,
            @NonNull Kind kind
    ) {
        super(metadata);
        this.context = context;
        this.kind = kind;
    }

    public static ContextValueLocalCursor contextItem(
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        return new ContextValueLocalCursor(context, metadata, Kind.ITEM);
    }

    public static ContextValueLocalCursor position(
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        return new ContextValueLocalCursor(context, metadata, Kind.POSITION);
    }

    public static ContextValueLocalCursor last(
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        return new ContextValueLocalCursor(context, metadata, Kind.LAST);
    }

    @Override
    protected Item materializeFirstItemOrNull() {
        switch (this.kind) {
            case ITEM:
                List<Item> items = this.context.getVariableValues()
                    .getLocalVariableValue(Name.CONTEXT_ITEM, this.getMetadata());
                if (items.isEmpty()) {
                    throw new UnexpectedTypeException(
                            "The context item cannot be an empty sequence.",
                            this.getMetadata()
                    );
                }
                return items.get(0);
            case POSITION:
                return requireContextValue(
                    this.context.getVariableValues().getPosition(),
                    "Context undefined (position) "
                );
            case LAST:
                return requireContextValue(
                    this.context.getVariableValues().getLast(),
                    "Context undefined (last) "
                );
            default:
                throw new IllegalStateException("Unknown dynamic context value kind.");
        }
    }

    private Item requireContextValue(Item value, String message) {
        if (value == null) {
            throw new AbsentPartOfDynamicContextException(message, this.getMetadata());
        }
        return value;
    }
}
