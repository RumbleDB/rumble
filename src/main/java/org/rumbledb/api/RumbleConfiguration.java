package org.rumbledb.api;

import org.rumbledb.config.RumbleConfigurationResolver;

public class RumbleConfiguration {
    // Internal configuration, not meant to be exposed to the user.
    private final org.rumbledb.config.RumbleConfiguration configuration;

    protected RumbleConfiguration(org.rumbledb.config.RumbleConfiguration configuration) {
        this.configuration = configuration;
    }

    public RumbleConfiguration() {
        // Initialize with default configuration values
        this(org.rumbledb.config.RumbleConfiguration.builder().build());
    }

    public static RumbleConfigurationBuilder builder() {
        return new RumbleConfigurationBuilder();
    }

    /**
     * Returns a builder initialized with the current configuration values.
     *
     * @return a builder initialized with the current configuration values
     */
    public RumbleConfigurationBuilder toBuilder() {
        return new RumbleConfigurationBuilder(this.configuration.toBuilder());
    }

    /**
     * Returns a configuration value as a plain Java value.
     *
     * @param path dot-separated configuration path
     * @return the value at the requested path
     */
    public Object get(String path) {
        return RumbleConfigurationResolver.get(this.configuration, path);
    }

    /**
     * Returns a configuration value converted to the requested Java type.
     *
     * @param path dot-separated configuration path
     * @param valueType requested Java type
     * @param <T> requested Java type
     * @return the value at the requested path
     */
    public <T> T get(String path, Class<T> valueType) {
        return RumbleConfigurationResolver.get(this.configuration, path, valueType);
    }

    /**
     * Returns the internal configuration object. This method is not meant to be used by the user (only for internal
     * purposes).
     * 
     * @return the internal configuration object
     */
    org.rumbledb.config.RumbleConfiguration getInternalConfiguration() {
        return this.configuration;
    }
}
