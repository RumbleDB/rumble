package org.rumbledb.server;

import java.io.IOException;
import java.io.OutputStream;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sparksoniq.spark.SparkSessionManager;

public class RumbleHandler implements HttpHandler {
    
    private RumbleRuntimeConfiguration configuration;
    
    public RumbleHandler(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "foo";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }
    
}
