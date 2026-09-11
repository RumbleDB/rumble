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

import org.rumbledb.config.model.SemanticsConfig;

public final class SemanticsArguments {
    @Option(
            names = "--default-language",
            paramLabel = "language",
            description = "Specifies the query language to be used.")
    private String defaultLanguage;

    @Option(names = "--xml-version", paramLabel = "version", description = "Sets the XML version to use.")
    private String xmlVersion;

    @Option(
            names = "--dates-with-timezone",
            negatable = true,
            description = "Activates timezone support for the type xs:date (deactivated by default).")
    private Boolean datesWithTimezone;

    @Option(
            names = {"--lax-json-null-validation", "--lax-json-null-valication"},
            negatable = true,
            description =
                    "Allows conflating JSON nulls with absent values when validating nillable object fields for more flexibility (activated by default).")
    private Boolean laxJSONNullValidation;

    @Option(
            names = "--static-base-uri",
            paramLabel = "uri",
            description =
                    "Sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query.")
    private String staticBaseUri;

    public SemanticsConfig toConfig() {
        SemanticsConfig.SemanticsConfigBuilder builder = SemanticsConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.datesWithTimezone, builder::datesWithTimeZone);
        OptionConversion.applyBooleanIfPresent(this.laxJSONNullValidation, builder::laxJSONNullValidation);
        OptionConversion.applyIfPresent(this.defaultLanguage, builder::queryLanguage);
        OptionConversion.applyIfPresent(this.xmlVersion, builder::xmlVersion);
        OptionConversion.applyIfPresent(this.staticBaseUri, builder::staticBaseUri);

        return builder.build();
    }
}
