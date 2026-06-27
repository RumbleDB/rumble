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

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class LanguageOptions {
    public static final boolean DEFAULT_DATES_WITH_TIMEZONE = false;
    public static final boolean DEFAULT_LAX_JSON_NULL_VALIDATION = true;
    public static final String DEFAULT_QUERY_LANGUAGE = "jsoniq10";

    /**
     * Whether dates with time zones should be supported.
     * If supported, RumbleDB will not be able to use DataFrames for data containing dates.
     */
    @Default
    private boolean datesWithTimeZone = DEFAULT_DATES_WITH_TIMEZONE;

    /**
     * Whether it is fine to consider JSON null values in an object as absent for validation against an optional
     * key.
     */
    @Default
    private boolean laxJSONNullValidation = DEFAULT_LAX_JSON_NULL_VALIDATION;

    /**
     * Version of the query language in use.
     */
    @Default
    private String queryLanguage = DEFAULT_QUERY_LANGUAGE;

    /**
     * Configured XML version.
     */
    public static final String DEFAULT_XML_VERSION = "1.1";

    @Default
    private String xmlVersion = DEFAULT_XML_VERSION;
}
