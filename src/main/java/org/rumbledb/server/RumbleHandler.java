package org.rumbledb.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sparksoniq.spark.SparkSessionManager;

public class RumbleHandler implements HttpHandler {

    public RumbleHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            Map<String, String> queryParameters = new HashMap<String, String>();
            for (String pair : query.split("&")) {
                int index = pair.indexOf("=");
                queryParameters.put(
                    URLDecoder.decode(pair.substring(0, index), "UTF-8"),
                    URLDecoder.decode(pair.substring(index + 1), "UTF-8")
                );
            }
            String[] args = new String[queryParameters.keySet().size() * 2];
            int i = 0;
            for (String param : queryParameters.keySet()) {
                args[i++] = "--" + param;
                args[i++] = queryParameters.get(param);
            }
            RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(args);
            SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();

            JsoniqQueryExecutor translator = new JsoniqQueryExecutor(configuration);
            List<String> responseString = translator.runQuery(
                configuration.getQueryPath(),
                configuration.getOutputPath()
            );

            JSONObject result = new JSONObject();
            if (responseString != null) {
                JSONArray results = new JSONArray();
                for (String s : responseString) {
                    try {
                        JSONObject value = new JSONObject(s);
                        results.put(value);
                    } catch (Exception e) {
                        try {
                            JSONArray array = new JSONArray(s);
                            results.put(array);
                        } catch (Exception f) {
                            results.put(s);
                        }
                    }
                }
                result.put("values", results);
            }
            result.put("status", 200);
            if (configuration.getOutputPath() != null) {
                result.put("output-path", configuration.getOutputPath());
            }
            if (configuration.getLogPath() != null) {
                result.put("log-path", configuration.getLogPath());
            }

            exchange.sendResponseHeaders(200, result.toString().getBytes().length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(result.toString().getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            String error = e.getMessage();
            exchange.sendResponseHeaders(500, error.getBytes().length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(error.getBytes());
            stream.close();
        }
    }

}
