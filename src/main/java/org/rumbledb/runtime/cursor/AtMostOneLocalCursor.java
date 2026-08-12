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

import lombok.NonNull;

import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Cursor template for an evaluation that produces zero or one value.
 *
 * @param <T> the value type
 */
public final class AtMostOneLocalCursor<T> extends AbstractLocalCursor<T> {

    private T result;
    private boolean hasNext;

    public AtMostOneLocalCursor(T value, @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.result = value;
        this.hasNext = value != null;
    }

    @Override
    protected final void openLocal() {}

    @Override
    protected final boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected final T nextLocal() {
        if (!this.hasNext) {
            throw this.invalidState("At-most-one cursor is exhausted.");
        }
        this.hasNext = false;
        return this.result;
    }

    @Override
    protected final void closeLocal() {
        this.result = null;
        this.hasNext = false;
    }
}
