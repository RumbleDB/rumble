package org.rumbledb.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class RumbleServer {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    public RumbleServer(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", this.rumbleRuntimeConfiguration.getPort()),
                0
            );
            HttpContext context = server.createContext("/jsoniq");
            context.setHandler(new RumbleHttpHandler());
            server.start();
        } catch (IOException e) {
            throw new OurBadException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
        }
    }

}
