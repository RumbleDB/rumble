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
package org.rumbledb.api;

public final class RumbleConfigurationBuilder {
    private final org.rumbledb.config.RumbleConfiguration.RumbleConfigurationBuilder internalBuilder;

    public RumbleConfigurationBuilder() {
        this.internalBuilder = org.rumbledb.config.RumbleConfiguration.builder();
    }

    protected RumbleConfigurationBuilder(
            org.rumbledb.config.RumbleConfiguration.RumbleConfigurationBuilder internalBuilder) {
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
