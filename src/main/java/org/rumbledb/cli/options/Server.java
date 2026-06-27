package org.rumbledb.cli.options;

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

    public ServerOptions toServerOptions() {
        ServerOptions.ServerOptionsBuilder builder = ServerOptions.builder();
        OptionConversion.applyIfPresent(this.host, builder::host);
        OptionConversion.applyIfPresent(this.port, builder::port);
        return builder.build();
    }
}
