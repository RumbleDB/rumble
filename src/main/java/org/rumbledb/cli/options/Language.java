package org.rumbledb.cli.options;

import org.rumbledb.config.LanguageOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Language {
    @Option(
        names = "--default-language",
        scope = ScopeType.INHERIT,
        paramLabel = "language",
        description = "Specifies the query language to be used."
    )
    private String defaultLanguage;

    @Option(
        names = "--xml-version",
        scope = ScopeType.INHERIT,
        paramLabel = "version",
        description = "Sets the XML version to use."
    )
    private String xmlVersion;

    @Option(
        names = "--dates-with-timezone",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates timezone support for the type xs:date (deactivated by default)."
    )
    private Boolean datesWithTimezone;

    @Option(
        names = { "--lax-json-null-validation", "--lax-json-null-valication" },
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Allows conflating JSON nulls with absent values when validating nillable object fields for more flexibility (activated by default)."
    )
    private Boolean laxJSONNullValidation;

    public LanguageOptions toLanguageOptions() {
        LanguageOptions.LanguageOptionsBuilder builder = LanguageOptions.builder();

        OptionConversion.applyBooleanIfPresent(this.datesWithTimezone, builder::datesWithTimeZone);
        OptionConversion.applyBooleanIfPresent(this.laxJSONNullValidation, builder::laxJSONNullValidation);
        OptionConversion.applyIfPresent(this.defaultLanguage, builder::queryLanguage);
        OptionConversion.applyIfPresent(this.xmlVersion, builder::xmlVersion);

        return builder.build();
    }
}
