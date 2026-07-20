/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

public final class ResolvedResource implements AutoCloseable {

    private final URI systemId;
    private final InputStream inputStream;

    public ResolvedResource(URI systemId, InputStream inputStream) {
        this.systemId = Objects.requireNonNull(systemId, "systemId must not be null");
        this.inputStream = Objects.requireNonNull(inputStream, "inputStream must not be null");
    }

    public URI getSystemId() {
        return this.systemId;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
