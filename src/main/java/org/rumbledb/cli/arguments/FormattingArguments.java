package org.rumbledb.cli.arguments;

import java.time.ZoneId;

import picocli.CommandLine.Option;

public final class FormattingArguments {
    @Option(
        names = "--default-formatting-place",
        paramLabel = "timezone",
        description = "Sets the default place used for formatting date and time values."
    )
    private ZoneId defaultFormattingPlace;

    @Option(
        names = "--default-formatting-calendar",
        paramLabel = "calendar",
        description = "Sets the default calendar used for formatting date and time values."
    )
    private String defaultFormattingCalendar;

    @Option(
        names = "--default-formatting-language",
        paramLabel = "language",
        description = "Sets the default language used for formatting date and time values."
    )
    private String defaultFormattingLanguage;
}
