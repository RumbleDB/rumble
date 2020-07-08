package org.rumbledb.server;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.rumbledb.cli.Main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class MainPageHandler implements HttpHandler {

    public MainPageHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = IOUtils.toString(Main.class.getResourceAsStream("/assets/public.html"), "UTF-8");;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }
}
