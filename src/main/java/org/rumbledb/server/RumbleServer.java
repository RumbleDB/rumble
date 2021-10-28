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
            System.err.println(
                "[INFO] Starting Rumble in server mode on port " + this.rumbleRuntimeConfiguration.getPort() + "..."
            );
            HttpServer server = HttpServer.create(
                new InetSocketAddress(
                        this.rumbleRuntimeConfiguration.getHost(),
                        this.rumbleRuntimeConfiguration.getPort()
                ),
                0
            );
            HttpContext context = server.createContext("/jsoniq");
            context.setHandler(new RumbleHttpHandler(this.rumbleRuntimeConfiguration));
            context = server.createContext("/public.html");
            context.setHandler(new MainPageHandler());
            context = server.createContext("/jsound-validator.html");
            context.setHandler(new ValidatorPageHandler());
            server.start();
            System.err.println("[INFO] Server running. Press Control+C to stop.");
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(10000);
            }
        } catch (IOException e) {
            throw new OurBadException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
        } catch (InterruptedException e) {
            System.err.println("[INFO] Interrupted.");
        }
    }

}
