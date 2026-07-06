package org.rumbledb.api;

import java.util.List;

import org.rumbledb.config.RumbleConfigurationResolver;

public class RumbleConfiguration {
    // Internal configuration, not meant to be exposed to the user.
    private final org.rumbledb.config.RumbleConfiguration configuration;

    protected RumbleConfiguration(org.rumbledb.config.RumbleConfiguration configuration) {
        this.configuration = configuration;
    }

    public RumbleConfiguration() {
        // Initialize with default configuration values
        this(org.rumbledb.config.RumbleConfiguration.defaultConfiguration());
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

    public int getInt(String path) {
        return RumbleConfigurationResolver.get(this.configuration, path, Integer.class);
    }

    public String getString(String path) {
        return RumbleConfigurationResolver.get(this.configuration, path, String.class);
    }

    public boolean getBoolean(String path) {
        return RumbleConfigurationResolver.get(this.configuration, path, Boolean.class);
    }

    public List<String> getStringList(String path) {
        return RumbleConfigurationResolver.getList(this.configuration, path, String.class);
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
