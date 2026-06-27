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

    @Option(
        names = "--static-base-uri",
        scope = ScopeType.INHERIT,
        paramLabel = "uri",
        description = "Sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query."
    )
    private String staticBaseUri;

    public IOOptions toIOOptions() {
        return IOOptions.builder()
            .allowedPrefixes(this.allowedPrefixes)
            .staticBaseUri(this.staticBaseUri)
            .build();
    }
}
