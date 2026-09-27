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
package org.rumbledb.config;

import java.util.Map;

import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.serialization.SerializationParameters;

/**
 * @deprecated Use {@link SerializationParameterUtils} directly.
 *             This class is kept for backward compatibility only.
 */
@Deprecated
public final class SerializationParameterBuilder {

    private SerializationParameterBuilder() {}

    public static SerializationParameters build(Map<String, String> parameters) {
        return SerializationParameterUtils.buildFromConfig(parameters);
    }

    public static SerializationParameters build(Map<String, String> parameters, String queryLanguage) {
        return SerializationParameterUtils.buildFromConfig(parameters, queryLanguage);
    }

    public static SerializationParameters build(Map<String, String> parameters, SerializationParameters defaults) {
        SerializationParameters params = SerializationParameters.copy(defaults);
        if (parameters != null) {
            parameters.forEach((k, v) -> SerializationParameterUtils.applyConfigOption(params, k, v));
        }
        return params;
    }

    public static void update(SerializationParameters params, String name, String value) {
        SerializationParameterUtils.applyConfigOption(params, name, value);
    }
}
