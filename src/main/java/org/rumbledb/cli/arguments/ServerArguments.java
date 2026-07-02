package org.rumbledb.cli.arguments;

import org.rumbledb.config.model.ServerConfig;

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

    public ServerConfig toConfig() {
        ServerConfig.ServerConfigBuilder builder = ServerConfig.builder();
        OptionConversion.applyIfPresent(this.host, builder::host);
        OptionConversion.applyIntIfPresent(this.port, builder::port);
        return builder.build();
    }
}
