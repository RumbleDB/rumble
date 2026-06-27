package org.rumbledb.cli.options;

import java.time.ZoneId;

import org.rumbledb.config.FormattingOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Formatting {
    @Option(
        names = "--default-formatting-place",
        scope = ScopeType.INHERIT,
        paramLabel = "timezone",
        description = "Sets the default place used for formatting date and time values."
    )
    private ZoneId defaultFormattingPlace;

    @Option(
        names = "--default-formatting-calendar",
        scope = ScopeType.INHERIT,
        paramLabel = "calendar",
        description = "Sets the default calendar used for formatting date and time values."
    )
    private String defaultFormattingCalendar;

    @Option(
        names = "--default-formatting-language",
        scope = ScopeType.INHERIT,
        paramLabel = "language",
        description = "Sets the default language used for formatting date and time values."
    )
    private String defaultFormattingLanguage;

    public FormattingOptions toFormattingOptions() {
        FormattingOptions.FormattingOptionsBuilder builder = FormattingOptions.builder();
        OptionConversion.applyIfPresent(this.defaultFormattingPlace, builder::defaultFormattingPlace);
        OptionConversion.applyIfPresent(this.defaultFormattingCalendar, builder::defaultFormattingCalendar);
        OptionConversion.applyIfPresent(this.defaultFormattingLanguage, builder::defaultFormattingLanguage);
        return builder.build();
    }
}
