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

import java.time.ZoneId;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class FormattingOptions {
    /**
     * The default place used for formatting date and time values.
     *
     * <p>
     * The default place is used by date/time formatting functions when no explicit
     * place is supplied. This implementation represents the place as a Java
     * {@link ZoneId}. The initial default is {@code UTC}.
     * </p>
     */
    @Default
    private ZoneId defaultFormattingPlace = ZoneId.of("UTC");

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
    @Default
    private String defaultFormattingCalendar = FormattingCalendarModeSupport.DEFAULT;

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
    @Default
    private String defaultFormattingLanguage = FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE;
}
