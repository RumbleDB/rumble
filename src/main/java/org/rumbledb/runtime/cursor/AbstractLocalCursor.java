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

/**
 * Lifecycle template for local cursors.
 *
 * <p>
 * Implementations only manage evaluation-specific state in the four local methods. This class enforces the
 * single-use lifecycle and performs best-effort cleanup if opening fails.
 * </p>
 *
 * @param <T> the value type returned by this cursor
 */
public abstract class AbstractLocalCursor<T> implements LocalCursor<T> {

    private enum State {
        CREATED,
        OPEN,
        CLOSED
    }

    private State state = State.CREATED;

    @Override
    public final void open() {
        if (this.state != State.CREATED) {
            throw invalidState("Local cursor is single-use and cannot be reopened.");
        }
        try {
            openLocal();
            this.state = State.OPEN;
        } catch (RuntimeException | Error exception) {
            this.state = State.CLOSED;
            try {
                closeLocal();
            } catch (RuntimeException | Error closeException) {
                exception.addSuppressed(closeException);
            }
            throw exception;
        }
    }

    @Override
    public final boolean hasNext() {
        if (this.state != State.OPEN) {
            throw invalidState("Local cursor is not open.");
        }
        return hasNextLocal();
    }

    @Override
    public final T next() {
        if (this.state != State.OPEN) {
            throw invalidState("Local cursor is not open.");
        }
        return nextLocal();
    }

    @Override
    public final void close() {
        boolean wasOpen = this.state == State.OPEN;
        this.state = State.CLOSED;
        if (wasOpen) {
            closeLocal();
        }
    }

    protected abstract void openLocal();

    protected abstract boolean hasNextLocal();

    protected abstract T nextLocal();

    protected abstract void closeLocal();

    /**
     * Creates the exception used for invalid lifecycle calls. Implementations may override this to attach plan
     * metadata.
     *
     * @param message the lifecycle error
     * @return the exception to throw
     */
    protected RuntimeException invalidState(String message) {
        return new IllegalStateException(message);
    }
}
