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

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.NonNull;

import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Cursor backed by a fresh Java iterator created for each evaluation.
 *
 * @param <T> the produced value type
 */
public final class IteratorLocalCursor<T> extends AbstractLocalCursor<T> {

    private final Supplier<? extends Iterator<? extends T>> iteratorFactory;
    private Iterator<? extends T> iterator;

    public IteratorLocalCursor(
            @NonNull Supplier<? extends Iterator<? extends T>> iteratorFactory, @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.iteratorFactory = iteratorFactory;
    }

    @Override
    protected void openLocal() {
        this.iterator = Objects.requireNonNull(this.iteratorFactory.get(), "iterator factory returned null");
    }

    @Override
    protected boolean hasNextLocal() {
        return this.iterator.hasNext();
    }

    @Override
    protected T nextLocal() {
        if (!this.iterator.hasNext()) {
            throw invalidState("No more values are available.");
        }
        return this.iterator.next();
    }

    @Override
    protected void closeLocal() {
        this.iterator = null;
    }
}
