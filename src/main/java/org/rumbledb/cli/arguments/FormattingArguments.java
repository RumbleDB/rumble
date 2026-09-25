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
package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;

import org.rumbledb.config.model.FormattingConfig;

public final class FormattingArguments {
    @Option(
            names = "--default-formatting-place",
            paramLabel = "timezone",
            description = "Sets the default place used for formatting date and time values.")
    private String defaultFormattingPlace;

    @Option(
            names = "--default-formatting-calendar",
            paramLabel = "calendar",
            description = "Sets the default calendar used for formatting date and time values.")
    private String defaultFormattingCalendar;

    @Option(
            names = "--default-formatting-language",
            paramLabel = "language",
            description = "Sets the default language used for formatting date and time values.")
    private String defaultFormattingLanguage;

    public FormattingConfig toConfig() {
        FormattingConfig.FormattingConfigBuilder builder = FormattingConfig.builder();
        OptionConversion.applyIfPresent(this.defaultFormattingPlace, builder::defaultFormattingPlace);
        OptionConversion.applyIfPresent(this.defaultFormattingCalendar, builder::defaultFormattingCalendar);
        OptionConversion.applyIfPresent(this.defaultFormattingLanguage, builder::defaultFormattingLanguage);

        return builder.build();
    }
}
