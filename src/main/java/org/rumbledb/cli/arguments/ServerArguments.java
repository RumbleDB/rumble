package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;

public final class ServerArguments {
    @Option(
        names = { "-h", "--host" },
        paramLabel = "host",
        description = "Changes the host of the RumbleDB HTTP server to any of your liking."
    )
    private String host;

    @Option(
        names = { "-p", "--port" },
        paramLabel = "port",
        description = "Changes the port of the RumbleDB HTTP server to any of your liking."
    )
    private Integer port;
}
