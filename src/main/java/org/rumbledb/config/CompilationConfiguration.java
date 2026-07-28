package org.rumbledb.config;

import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.resources.ResourceResolver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Configuration and resource resolution services used during compilation. */
public final class CompilationConfiguration {

    private final RumbleRuntimeConfiguration runtimeConfiguration;
    private final ResourceResolver resourceResolver;
    private final Map<URI, LibraryModule> libraryModulesByLocation;

    public CompilationConfiguration(
            RumbleRuntimeConfiguration runtimeConfiguration,
            ResourceResolver resourceResolver
    ) {
        this.runtimeConfiguration = Objects.requireNonNull(
            runtimeConfiguration,
            "runtimeConfiguration must not be null"
        );
        this.resourceResolver = Objects.requireNonNull(resourceResolver, "resourceResolver must not be null");
        this.libraryModulesByLocation = new HashMap<>();
    }

    public CompilationConfiguration(RumbleRuntimeConfiguration runtimeConfiguration) {
        this(runtimeConfiguration, new ResourceResolver());
    }

    public RumbleRuntimeConfiguration runtimeConfiguration() {
        return this.runtimeConfiguration;
    }

    public ResourceResolver resourceResolver() {
        return this.resourceResolver;
    }

    public LibraryModule getLibraryModule(URI location) {
        return this.libraryModulesByLocation.get(location);
    }

    public LibraryModule cacheLibraryModule(URI location, LibraryModule module) {
        LibraryModule existing = this.libraryModulesByLocation.get(location);
        if (existing != null) {
            return existing;
        }
        this.libraryModulesByLocation.put(location, module);
        return module;
    }

    public void removeLibraryModule(URI location, LibraryModule module) {
        if (this.libraryModulesByLocation.get(location) == module) {
            this.libraryModulesByLocation.remove(location);
        }
    }
}
