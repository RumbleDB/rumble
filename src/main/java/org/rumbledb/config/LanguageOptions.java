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
 *
 */

package org.rumbledb.config;

import java.util.Objects;

import org.rumbledb.exceptions.CliException;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class LanguageOptions {
    public static final boolean DEFAULT_DATES_WITH_TIMEZONE = false;
    public static final boolean DEFAULT_LAX_JSON_NULL_VALIDATION = true;
    public static final String DEFAULT_QUERY_LANGUAGE = "jsoniq10";

    /**
     * Whether dates with time zones should be supported.
     * If supported, RumbleDB will not be able to use DataFrames for data containing dates.
     */
    private boolean datesWithTimeZone;

    /**
     * Whether it is fine to consider JSON null values in an object as absent for validation against an optional
     * key.
     */
    private boolean laxJSONNullValidation;

    /**
     * Version of the query language in use.
     */
    private String queryLanguage;

    /**
     * Configured XML version.
     */
    public static final String DEFAULT_XML_VERSION = "1.1";

    private String xmlVersion;

    @Builder(toBuilder = true)
    private LanguageOptions(
            Boolean datesWithTimeZone,
            Boolean laxJSONNullValidation,
            String queryLanguage,
            String xmlVersion
    ) {
        this.datesWithTimeZone = Objects.requireNonNullElse(datesWithTimeZone, DEFAULT_DATES_WITH_TIMEZONE);
        this.laxJSONNullValidation = Objects.requireNonNullElse(
            laxJSONNullValidation,
            DEFAULT_LAX_JSON_NULL_VALIDATION
        );
        this.queryLanguage = Objects.requireNonNullElse(queryLanguage, DEFAULT_QUERY_LANGUAGE);
        this.xmlVersion = Objects.requireNonNullElse(xmlVersion, DEFAULT_XML_VERSION);
    }

    private static String normalizeXmlVersion(String xmlVersion) {
        if (xmlVersion == null) {
            return DEFAULT_XML_VERSION;
        }

        String normalized = xmlVersion.trim();
        if ("1.0".equals(normalized) || "1.1".equals(normalized)) {
            return normalized;
        }

        throw new CliException(
                "Argument --xml-version must be \"1.0\" or \"1.1\" (was: " + xmlVersion + ")."
        );
    }

    public static class LanguageOptionsBuilder {
        public LanguageOptionsBuilder xmlVersion(String xmlVersion) {
            this.xmlVersion = normalizeXmlVersion(xmlVersion);
            return this;
        }
    }
}
