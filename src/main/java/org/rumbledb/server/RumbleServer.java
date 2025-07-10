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
                "Starting Rumble in server mode on port " + this.rumbleRuntimeConfiguration.getPort() + "..."
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
            System.err.println("Server running. Press Control+C to stop.");
            System.err.println("RumbleDB is now running as a server and accepts requests.");
            System.err.println();
            System.err.println("What to do next?");
            System.err.println();
            String host = this.rumbleRuntimeConfiguration.getHost();
            int port = this.rumbleRuntimeConfiguration.getPort();
            System.err.println(
                "- You can go to http://" + host + ":" + port + "/public.html in your browser and type queries there."
            );
            System.err.println();
            System.err.println("- You can use Jupyter notebooks to write queries interactively.");
            System.err.println(
                "You can download a tutorial notebook from https://github.com/RumbleDB/rumble/blob/master/RumbleSandbox.ipynb (use a raw download) that you can point to http://"
                    + host
                    + ":"
                    + port
                    + "/jsoniq"
            );
            System.err.println();

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(10000);
            }
        } catch (IOException e) {
            throw new OurBadException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
        } catch (InterruptedException e) {
            System.err.println("Interrupted.");
        }
    }

}
