package org.rumbledb.cli.options;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.config.IOOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class IO {
    @Option(
        names = { "--allowed-uri-prefixes" },
        scope = ScopeType.INHERIT,
        description = "Allowed URI prefixes for read/write (with I/O functions to read data)"
    )
    private List<String> allowedPrefixes = new ArrayList<>();

    public IOOptions toIOOptions() {
        return IOOptions.builder()
            .allowedPrefixes(this.allowedPrefixes)
            .build();
    }
}
