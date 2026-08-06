/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.resources;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;
import java.util.Map;

/** Resolves logical resource URIs to source content used during query compilation. */
public final class ResourceResolver {

    private final Map<URI, URI> mappings;

    public ResourceResolver() {
        this(Map.of());
    }

    public ResourceResolver(Map<URI, URI> mappings) {
        this.mappings = Map.copyOf(mappings);
    }

    public ResolvedResource resolve(
            URI requestedURI,
            RumbleConfiguration configuration,
            ExceptionMetadata metadata
    ) {
        URI physicalURI = this.mappings.getOrDefault(requestedURI, requestedURI);
        return new ResolvedResource(
                physicalURI,
                FileSystemUtil.getDataInputStream(physicalURI, metadata)
        );
    }
}
