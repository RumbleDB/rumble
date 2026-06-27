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
        names = "--static-base-uri",
        scope = ScopeType.INHERIT,
        paramLabel = "uri",
        description = "Sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query."
    )
    private String staticBaseUri;

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
    private boolean datesWithTimezone;

    @Option(
        names = { "--lax-json-null-validation", "--lax-json-null-valication" },
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Allows conflating JSON nulls with absent values when validating nillable object fields for more flexibility (activated by default)."
    )
    private boolean laxJSONNullValidation;

    public LanguageOptions toLanguageOptions() {
        LanguageOptions.LanguageOptionsBuilder builder = LanguageOptions.builder()
            .datesWithTimeZone(this.datesWithTimezone)
            .laxJSONNullValidation(this.laxJSONNullValidation);
        OptionConversion.applyIfPresent(this.defaultLanguage, builder::queryLanguage);
        OptionConversion.applyIfPresent(this.staticBaseUri, builder::staticBaseUri);
        OptionConversion.applyIfPresent(this.xmlVersion, builder::xmlVersion);
        return builder.build();
    }
}
