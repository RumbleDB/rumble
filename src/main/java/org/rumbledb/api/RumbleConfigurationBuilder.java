package org.rumbledb.api;

public final class RumbleConfigurationBuilder {
    private final org.rumbledb.config.RumbleConfiguration.RumbleConfigurationBuilder internalBuilder;

    public RumbleConfigurationBuilder() {
        this.internalBuilder = org.rumbledb.config.RumbleConfiguration.builder();
    }

    protected RumbleConfigurationBuilder(
            org.rumbledb.config.RumbleConfiguration.RumbleConfigurationBuilder internalBuilder
    ) {
        this.internalBuilder = internalBuilder;
    }

    public RumbleConfigurationBuilder with(String key, Object value) {
        this.internalBuilder.with(key, value);
        return this;
    }

    public RumbleConfiguration build() {
        org.rumbledb.config.RumbleConfiguration runtimeConfig = this.internalBuilder.build();
        return new RumbleConfiguration(runtimeConfig);
    }
}
