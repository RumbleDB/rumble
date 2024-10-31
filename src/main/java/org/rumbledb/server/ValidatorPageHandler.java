package org.rumbledb.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.rumbledb.cli.Main;

import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("restriction")
public class ValidatorPageHandler implements HttpHandler {

    public ValidatorPageHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = IOUtils.toString(Main.class.getResourceAsStream("/assets/jsound-validator.html"), "UTF-8");;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }
}
