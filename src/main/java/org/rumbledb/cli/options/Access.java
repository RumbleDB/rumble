package org.rumbledb.cli.options;

import java.util.ArrayList;
import java.util.List;

import picocli.CommandLine.Option;

public final class Access {
    @Option(
        names = { "--allowed-uri-prefixes" },
        description = "Allowed URI prefixes for read/write (with I/O functions to read data)"
    )
    private List<String> allowedPrefixes = new ArrayList<>();

    public org.rumbledb.config.Access toAccess() {
        return org.rumbledb.config.Access.builder()
            .allowedPrefixes(this.allowedPrefixes)
            .build();
    }
}
