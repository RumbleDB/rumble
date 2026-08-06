/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.config;

import org.rumbledb.resources.ResourceResolver;

import java.util.Objects;

/** Configuration and resource resolution services used during compilation. */
public record CompilationConfiguration(
        RumbleConfiguration runtimeConfiguration,
        ResourceResolver resourceResolver) {

    public CompilationConfiguration {
        Objects.requireNonNull(runtimeConfiguration, "runtimeConfiguration must not be null");
        Objects.requireNonNull(resourceResolver, "resourceResolver must not be null");
    }

    public CompilationConfiguration(RumbleConfiguration runtimeConfiguration) {
        this(runtimeConfiguration, new ResourceResolver());
    }
}
