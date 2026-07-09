/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.cli;

import java.util.Arrays;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.StandardLevel;
import org.rumbledb.config.model.DebugConfig;
import org.rumbledb.exceptions.CliException;

final class LoggingConfiguration {
    private static final String LOGGER_NAME = "org.rumbledb";
    private static final Level DEFAULT_LEVEL = Level.WARN;
    private static final String VALID_LEVELS = String.join(
        ", ",
        Arrays.stream(StandardLevel.values())
            .map(StandardLevel::name)
            .map(level -> level.toLowerCase(Locale.ROOT))
            .toList()
    );

    private LoggingConfiguration() {
    }

    static void configure(DebugConfig debugConfig) {
        Configurator.setLevel(LOGGER_NAME, resolveLevel(debugConfig));
    }

    private static Level resolveLevel(DebugConfig debugConfig) {
        String configuredLevel = debugConfig.logLevel();
        if (configuredLevel == null || configuredLevel.isBlank()) {
            // Backward compatibility: if --log-level is not specified, we use the value of --debug to determine the log
            // level.
            return debugConfig.logging()
                ? Level.DEBUG
                : DEFAULT_LEVEL;
        }

        String normalizedLevel = configuredLevel.trim().toUpperCase(Locale.ROOT);
        try {
            StandardLevel.valueOf(normalizedLevel);
        } catch (IllegalArgumentException e) {
            throw new CliException(
                    "Invalid --log-level value: "
                        + configuredLevel
                        + ". Valid values are: "
                        + VALID_LEVELS
                        + "."
            );
        }
        return Level.toLevel(normalizedLevel);
    }
}
