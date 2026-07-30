package org.rumbledb.server;

import lombok.NoArgsConstructor;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.rumbledb.cli.Main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@NoArgsConstructor
public class MainPageHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = IOUtils.toString(Main.class.getResourceAsStream("/assets/public.html"), "UTF-8");;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }
}
