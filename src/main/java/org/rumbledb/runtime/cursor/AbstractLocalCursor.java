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

import lombok.Getter;
import lombok.NonNull;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;

/**
 * Lifecycle template for local cursors.
 *
 * <p>
 * Implementations only manage evaluation-specific state in the four local methods. This class opens a cursor on its
 * first read, enforces the single-use lifecycle, and performs best-effort cleanup if opening fails.
 * </p>
 *
 * @param <T> the value type returned by this cursor
 */
public abstract class AbstractLocalCursor<T> implements LocalCursor<T> {

    @Getter
    private final ExceptionMetadata metadata;

    private enum State {
        CREATED,
        OPEN,
        CLOSED
    }

    private State state = State.CREATED;

    protected AbstractLocalCursor(@NonNull ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    private void openIfNeeded() {
        if (this.state != State.CREATED) {
            return;
        }
        try {
            this.openLocal();
            this.state = State.OPEN;
        } catch (RuntimeException | Error exception) {
            this.state = State.CLOSED;
            try {
                this.closeLocal();
            } catch (RuntimeException | Error closeException) {
                exception.addSuppressed(closeException);
            }
            throw exception;
        }
    }

    @Override
    public final boolean hasNext() {
        this.openIfNeeded();
        if (this.state != State.OPEN) {
            throw this.invalidState("Local cursor is not open.");
        }
        return this.hasNextLocal();
    }

    @Override
    public final T next() {
        this.openIfNeeded();
        if (this.state != State.OPEN) {
            throw this.invalidState("Local cursor is not open.");
        }
        return this.nextLocal();
    }

    @Override
    public final void close() {
        boolean wasOpen = this.state == State.OPEN;
        this.state = State.CLOSED;
        if (wasOpen) {
            this.closeLocal();
        }
    }

    protected abstract void openLocal();

    protected abstract boolean hasNextLocal();

    protected abstract T nextLocal();

    protected abstract void closeLocal();

    /**
     * Creates the exception used for invalid lifecycle calls.
     *
     * <p>
     * Cursors constructed with query metadata preserve the legacy {@link IteratorFlowException}; generic cursors use
     * {@link IllegalStateException}.
     * </p>
     *
     * @param message the lifecycle error
     * @return the exception to throw
     */
    protected final RuntimeException invalidState(String message) {
        return new IteratorFlowException(message, this.metadata);
    }
}
