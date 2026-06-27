package org.rumbledb.cli.options;

import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.ServerOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Server {
    @Option(
        names = { "-h", "--host" },
        scope = ScopeType.INHERIT,
        paramLabel = "host",
        description = "Changes the host of the RumbleDB HTTP server to any of your liking."
    )
    private String host;

    @Option(
        names = { "-p", "--port" },
        scope = ScopeType.INHERIT,
        paramLabel = "port",
        description = "Changes the port of the RumbleDB HTTP server to any of your liking."
    )
    private Integer port;

    public ServerOptions toServerOptions(ExecutionMode mode) {
        return ServerOptions.builder()
            .mode(mode)
            .host(this.host)
            .port(this.port)
            .build();
    }
}
