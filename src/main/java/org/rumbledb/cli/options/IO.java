package org.rumbledb.cli.options;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.config.IOOptions;

import picocli.CommandLine.Option;

public class IO {
    @Option(
        names = { "allowed-uri-prefixes" },
        description = "Allowed URI prefixes for read/write (with I/O functions to read data)"
    )
    List<String> allowedPrefixes = new ArrayList<>();


    public IOOptions toIOOptions() {
        return IOOptions.builder()
            .allowedPrefixes(this.allowedPrefixes)
            .build();
    }
}
