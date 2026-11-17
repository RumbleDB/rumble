/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.cursor;

import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Mutable state for one local evaluation of a {@link RuntimePlan}.
 *
 * <p>
 * A cursor has a single owner, is single-use, and must not be shared between evaluations. Its lifecycle is
 * {@code created -> open -> closed}; closing an already closed cursor should be harmless. A fresh cursor replaces the
 * reset and cloning operations used by the legacy iterator architecture. Implementations should normally extend
 * {@link AbstractLocalCursor} instead of implementing lifecycle handling themselves.
 * </p>
 *
 * @param <T> the value type returned by this cursor
 */
public interface Cursor<T> extends AutoCloseable {

    /**
     * @return whether another value is available
     *         Opens the cursor if needed, then returns whether another value is available.
     */
    boolean hasNext();

    /**
     * @return the next value
     *         Opens the cursor if needed, then returns the next value.
     */
    T next();

    /**
     * Releases resources owned by this evaluation. This method is idempotent.
     */
    @Override
    void close();
}
