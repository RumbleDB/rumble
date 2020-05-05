package org.rumbledb.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import sparksoniq.spark.SparkSessionManager;

public class RumbleServer {
    
    public RumbleServer() {
    }
    
    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            HttpContext context = server.createContext("/jsoniq");
            context.setHandler(new RumbleHandler());
            server.start();
        } catch (IOException e)
        {
            throw new OurBadException(e.getMessage(), new ExceptionMetadata(0,0));
        }
    }

}
