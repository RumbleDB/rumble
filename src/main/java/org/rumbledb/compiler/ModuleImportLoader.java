/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.compiler;

import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.module.LibraryModule;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/** Shared module import semantics for the JSONiq and XQuery frontends. */
final class ModuleImportLoader {

    private ModuleImportLoader() {
    }

    public static LibraryModule load(
            String namespace,
            List<String> locationHints,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata
    ) {
        URI baseURI = importingModuleContext.getStaticBaseURI();
        URI namespaceURI = URILiteralUtils.resolve(baseURI, namespace, metadata);
        List<String> candidates = locationHints.isEmpty() ? List.of(namespace) : locationHints;
        Exception lastFailure = null;

        for (String candidate : candidates) {
            URI location = URILiteralUtils.resolve(baseURI, candidate, metadata);
            try {
                LibraryModule module = VisitorHelpers.parseLibraryModuleFromLocation(
                    location,
                    importingModuleContext,
                    compilationConfiguration,
                    metadata
                );
                validateNamespace(namespaceURI, module, metadata);
                return module;
            } catch (IOException | CannotRetrieveResourceException e) {
                lastFailure = e;
            }
        }

        RumbleException exception = new ModuleNotFoundException(
                "Module not found: " + namespace + failureMessage(lastFailure),
                metadata
        );
        if (lastFailure != null) {
            exception.initCause(lastFailure);
        }
        throw exception;
    }

    private static void validateNamespace(
            URI expectedNamespace,
            LibraryModule module,
            ExceptionMetadata metadata
    ) {
        if (expectedNamespace.toString().equals(module.getNamespace())) {
            return;
        }
        throw new ModuleNotFoundException(
                "A module with namespace "
                    + expectedNamespace
                    + " was not found. The namespace of the module at this location was: "
                    + module.getNamespace(),
                metadata
        );
    }

    private static String failureMessage(Exception failure) {
        if (failure == null || failure.getMessage() == null) {
            return "";
        }
        return " Cause: " + failure.getMessage();
    }
}
