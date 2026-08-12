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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * Resolves an explicit argument, or the context item when the argument is omitted, and maps the resolved value.
 *
 * <p>
 * An explicitly supplied empty sequence is passed to the mapper as {@code null}. A missing argument instead resolves
 * the context item, preserving the distinction required by context-dependent functions.
 * </p>
 */
public final class ContextOrArgumentLocalCursor<O> extends AbstractLocalCursor<O> {

    private final ItemRuntimePlan argumentPlan;
    private final DynamicContext context;
    private final Function<? super Item, ? extends List<? extends O>> mapper;
    private final ExceptionMetadata metadata;
    private List<? extends O> results;
    private int position;

    private ContextOrArgumentLocalCursor(
            ItemRuntimePlan argumentPlan,
            @NonNull DynamicContext context,
            @NonNull Function<? super Item, ? extends List<? extends O>> mapper,
            @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.argumentPlan = argumentPlan;
        this.context = context;
        this.mapper = mapper;
        this.metadata = metadata;
    }

    public static <O> ContextOrArgumentLocalCursor<O> mapArgument(
            @NonNull ItemRuntimePlan argumentPlan,
            @NonNull DynamicContext context,
            @NonNull Function<? super Item, ? extends O> mapper,
            @NonNull ExceptionMetadata metadata) {
        return new ContextOrArgumentLocalCursor<>(
                argumentPlan,
                context,
                item -> {
                    O result = mapper.apply(item);
                    return result == null ? Collections.emptyList() : Collections.singletonList(result);
                },
                metadata);
    }

    public static <O> ContextOrArgumentLocalCursor<O> mapFirstArgumentOrContext(
            @NonNull List<? extends ItemRuntimePlan> argumentPlans,
            @NonNull DynamicContext context,
            @NonNull Function<? super Item, ? extends O> mapper,
            @NonNull ExceptionMetadata metadata) {
        return new ContextOrArgumentLocalCursor<>(
                argumentPlans.isEmpty() ? null : argumentPlans.get(0),
                context,
                item -> {
                    O result = mapper.apply(item);
                    return result == null ? Collections.emptyList() : Collections.singletonList(result);
                },
                metadata);
    }

    public static <O> ContextOrArgumentLocalCursor<O> flatMapArgument(
            @NonNull ItemRuntimePlan argumentPlan,
            @NonNull DynamicContext context,
            @NonNull Function<? super Item, ? extends List<? extends O>> mapper,
            @NonNull ExceptionMetadata metadata) {
        return new ContextOrArgumentLocalCursor<>(argumentPlan, context, mapper, metadata);
    }

    public static <O> ContextOrArgumentLocalCursor<O> flatMapFirstArgumentOrContext(
            @NonNull List<? extends ItemRuntimePlan> argumentPlans,
            @NonNull DynamicContext context,
            @NonNull Function<? super Item, ? extends List<? extends O>> mapper,
            @NonNull ExceptionMetadata metadata) {
        return new ContextOrArgumentLocalCursor<>(
                argumentPlans.isEmpty() ? null : argumentPlans.get(0), context, mapper, metadata);
    }

    @Override
    protected void openLocal() {
        Item argument = this.argumentPlan == null
                ? this.context
                        .getVariableValues()
                        .getLocalVariableValue(Name.CONTEXT_ITEM, this.metadata)
                        .get(0)
                : this.argumentPlan.materializeFirstOrNull(this.context);
        List<? extends O> mappedResults = this.mapper.apply(argument);
        this.results = mappedResults == null ? Collections.emptyList() : mappedResults;
        this.position = 0;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.position < this.results.size();
    }

    @Override
    protected O nextLocal() {
        if (!hasNextLocal()) {
            throw invalidState("Local cursor is exhausted.");
        }
        return this.results.get(this.position++);
    }

    @Override
    protected void closeLocal() {
        this.results = null;
    }
}
