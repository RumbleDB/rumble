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

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.rumbledb.config.FormattingCalendarModeSupport;
import org.rumbledb.config.FormattingLanguageSupport;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarSupport;
import org.rumbledb.runtime.functions.util.formatting.language.LanguageSupport;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Accessors(fluent = true)
@JsonDeserialize(builder = FormattingConfig.FormattingConfigBuilder.class)
public class FormattingConfig {
    /**
     * The default place used for formatting date and time values.
     *
     * <p>
     * The default place is used by date/time formatting functions when no explicit
     * place is supplied. The initial default is {@code UTC}.
     * </p>
     */
    private String defaultFormattingPlace;

    /**
     * The default calendar used for formatting date and time values.
     *
     * <p>
     * The default calendar is used by date/time formatting functions when no explicit
     * calendar is supplied. The initial default is
     * {@link FormattingCalendarModeSupport#DEFAULT}.
     * </p>
     *
     */
    private String defaultFormattingCalendar;

    /**
     * The default language used for formatting date and time values.
     *
     * <p>
     * The default language is used by date/time formatting functions when no explicit
     * language is supplied. The initial default is
     * {@link FormattingLanguageSupport#DEFAULT_FORMATTING_LANGUAGE}.
     * </p>
     *
     */
    private String defaultFormattingLanguage;

    @Builder(toBuilder = true)
    private FormattingConfig(
            String defaultFormattingPlace,
            String defaultFormattingCalendar,
            String defaultFormattingLanguage
    ) {
        this.defaultFormattingPlace = Objects.requireNonNullElse(defaultFormattingPlace, "UTC");
        this.defaultFormattingCalendar = Objects.requireNonNullElse(
            defaultFormattingCalendar,
            FormattingCalendarModeSupport.DEFAULT
        );
        this.defaultFormattingLanguage = Objects.requireNonNullElse(
            defaultFormattingLanguage,
            FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE
        );
    }

    private static String normalizeFormattingCalendar(String calendar) {
        String normalized = CalendarSupport.normalizeKnownCalendarMode(calendar);
        if (FormattingCalendarModeSupport.isValidFormattingCalendar(normalized)) {
            return normalized;
        }

        throw new CliException(
                "Invalid argument supplied for default-formatting-calendar: " + calendar
        );
    }

    private static String normalizeFormattingLanguage(String language) {
        String normalized = LanguageSupport.normalizeLanguage(language);
        String primary = LanguageSupport.getPrimaryLanguageSubtag(normalized);
        if (FormattingLanguageSupport.isValidFormattingLanguage(primary)) {
            return primary;
        }

        throw new CliException(
                "Invalid argument supplied for default-formatting-language: " + language
        );
    }

    private static String normalizeFormattingPlace(String place) {
        if (place == null) {
            return "UTC";
        }
        try {
            java.time.ZoneId.of(place);
            return place;
        } catch (java.time.DateTimeException e) {
            throw new CliException(
                    "Invalid argument supplied for default-formatting-place: " + place
            );
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class FormattingConfigBuilder {
        public FormattingConfigBuilder defaultFormattingPlace(String place) {
            this.defaultFormattingPlace = normalizeFormattingPlace(place);
            return this;
        }

        public FormattingConfigBuilder defaultFormattingCalendar(String calendar) {
            this.defaultFormattingCalendar = normalizeFormattingCalendar(calendar);
            return this;
        }

        public FormattingConfigBuilder defaultFormattingLanguage(String language) {
            this.defaultFormattingLanguage = normalizeFormattingLanguage(language);
            return this;
        }
    }
}
