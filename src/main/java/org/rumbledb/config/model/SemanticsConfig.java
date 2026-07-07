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

package org.rumbledb.config.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.exceptions.CliException;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;

@Value
@NoArgsConstructor
@Jacksonized
@Accessors(fluent = true)
@JsonDeserialize(builder = SemanticsConfig.SemanticsConfigBuilder.class)
public class SemanticsConfig implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;

    public static final boolean DEFAULT_DATES_WITH_TIMEZONE = false;
    public static final boolean DEFAULT_LAX_JSON_NULL_VALIDATION = true;
    public static final String DEFAULT_QUERY_LANGUAGE = "jsoniq10";
    public static final String DEFAULT_XML_VERSION = "1.1";

    @NonFinal
    private boolean datesWithTimeZone;
    @NonFinal
    private boolean laxJSONNullValidation;
    @NonFinal
    private String queryLanguage;
    @NonFinal
    private String xmlVersion;
    @NonFinal
    private String staticBaseUri;

    @Builder(toBuilder = true)
    private SemanticsConfig(
            Boolean datesWithTimeZone,
            Boolean laxJSONNullValidation,
            String queryLanguage,
            String xmlVersion,
            String staticBaseUri
    ) {
        this.datesWithTimeZone = Objects.requireNonNullElse(datesWithTimeZone, DEFAULT_DATES_WITH_TIMEZONE);
        this.laxJSONNullValidation = Objects.requireNonNullElse(
            laxJSONNullValidation,
            DEFAULT_LAX_JSON_NULL_VALIDATION
        );
        this.queryLanguage = Objects.requireNonNullElse(queryLanguage, DEFAULT_QUERY_LANGUAGE);
        this.xmlVersion = Objects.requireNonNullElse(xmlVersion, DEFAULT_XML_VERSION);
        this.staticBaseUri = staticBaseUri;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(this.datesWithTimeZone);
        output.writeBoolean(this.laxJSONNullValidation);
        output.writeString(this.queryLanguage);
        output.writeString(this.xmlVersion);
        output.writeString(this.staticBaseUri);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.datesWithTimeZone = input.readBoolean();
        this.laxJSONNullValidation = input.readBoolean();
        this.queryLanguage = input.readString();
        this.xmlVersion = input.readString();
        this.staticBaseUri = input.readString();
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

    @JsonPOJOBuilder(withPrefix = "")
    public static class SemanticsConfigBuilder {
        public SemanticsConfigBuilder xmlVersion(String xmlVersion) {
            this.xmlVersion = normalizeXmlVersion(xmlVersion);
            return this;
        }
    }
}
