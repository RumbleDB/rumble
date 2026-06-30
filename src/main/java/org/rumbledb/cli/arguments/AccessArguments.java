package org.rumbledb.cli.arguments;

import java.util.ArrayList;
import java.util.List;

import picocli.CommandLine.Option;

public final class AccessArguments {
    @Option(
        names = { "--allowed-uri-prefixes" },
        description = "Allowed URI prefixes for read/write (with I/O functions to read data)"
    )
    private List<String> allowedPrefixes = new ArrayList<>();
}
