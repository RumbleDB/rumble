package org.rumbledb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkException;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sparksoniq.spark.SparkSessionManager;

@SuppressWarnings("restriction")
public class RumbleHandler implements HttpHandler {

    private enum StatusCode {
        SUCCESS(200),
        SERVER_ERROR(500);

        private int code;

        StatusCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public RumbleHandler() {
    }

    private void sendResponse(HttpExchange exchange, StatusCode code, String response) throws IOException {
        exchange.sendResponseHeaders(code.getCode(), response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }

    public String[] getCLIArguments(String query) throws UnsupportedEncodingException {
        Map<String, String> queryParameters = new HashMap<String, String>();
        if (query == null) {
            query = "";
        }
        for (String pair : query.split("&")) {
            int index = pair.indexOf("=");
            if (index != -1) {
                queryParameters.put(
                    URLDecoder.decode(pair.substring(0, index), "UTF-8"),
                    URLDecoder.decode(pair.substring(index + 1), "UTF-8")
                );
            }
        }
        String[] args = new String[queryParameters.keySet().size() * 2];
        int i = 0;
        for (String param : queryParameters.keySet()) {
            args[i++] = "--" + param;
            args[i++] = queryParameters.get(param);
        }
        return args;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            URI uri = exchange.getRequestURI();
            String queryString = uri.getQuery();
            String args[] = getCLIArguments(queryString);

            RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(args);
            SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();

            JsoniqQueryExecutor translator = new JsoniqQueryExecutor(configuration);
            List<Item> items;
            if (configuration.getQueryPath() != null) {
                if(configuration.getOutputPath() != null && !exchange.getRequestMethod().equals("POST"))
                {
                    throw new CliException("The POST method must be used if specifying an output path, as this has side effects.");
                }
                items = translator.runQuery(
                    configuration.getQueryPath(),
                    configuration.getOutputPath()
                );
            } else {
                if(configuration.getOutputPath() != null && !exchange.getRequestMethod().equals("POST"))
                {
                    throw new CliException("The POST method must be used if specifying an output path, as this has side effects.");
                }
                if(!exchange.getRequestMethod().equals("GET") && !exchange.getRequestMethod().equals("POST"))
                {
                    throw new CliException("Only the GET and POST methods are supported.");
                }
                InputStreamReader r = new InputStreamReader(exchange.getRequestBody());
                BufferedReader r2 = new BufferedReader(r);
                StringBuffer sb = new StringBuffer();
                String s;
                while ((s = r2.readLine()) != null) {
                    sb.append(s);
                }
                String JSONiqQuery = sb.toString();
                items = translator.runInteractive(JSONiqQuery);
            }

            Item output = ItemFactory.getInstance().createObjectItem();
            if (items != null) {
                Item values = ItemFactory.getInstance().createArrayItem(items);
                output.putItemByKey("values", values);
            }
            output.putItemByKey("status", ItemFactory.getInstance().createIntegerItem(200));
            if (configuration.getOutputPath() != null) {
                output.putItemByKey(
                    "output-path",
                    ItemFactory.getInstance().createStringItem(configuration.getOutputPath())
                );
            }
            if (configuration.getLogPath() != null) {
                output.putItemByKey("log-path", ItemFactory.getInstance().createStringItem(configuration.getLogPath()));
            }

            sendResponse(exchange, StatusCode.SUCCESS, output.serialize());
        } catch (Exception e) {
            Item output = handleException(e);
            sendResponse(exchange, StatusCode.SUCCESS, output.serialize());
        }
    }


    private static Item handleException(Throwable ex) {
        try {
            if (ex != null) {
                if (ex instanceof SparkException) {
                    Throwable sparkExceptionCause = ex.getCause();
                    if (sparkExceptionCause != null) {
                        return handleException(sparkExceptionCause);
                    }
                    return handleException(new RumbleException(ex.getMessage()));
                } else if (ex instanceof RumbleException && !(ex instanceof OurBadException)) {
                    Item output = ItemFactory.getInstance().createObjectItem();
                    output.putItemByKey("error-message", ItemFactory.getInstance().createStringItem(ex.getMessage()));
                    output.putItemByKey(
                        "error-code",
                        ItemFactory.getInstance().createStringItem(((RumbleException) ex).getErrorCode())
                    );
                    Item stackTrace = ItemFactory.getInstance().createArrayItem();
                    output.putItemByKey("stack-trace", stackTrace);
                    for (StackTraceElement e : ex.getStackTrace()) {
                        stackTrace.append(ItemFactory.getInstance().createStringItem(e.toString()));
                    }
                    return output;
                } else {
                    Item output = ItemFactory.getInstance().createObjectItem();
                    output.putItemByKey(
                        "error-message",
                        ItemFactory.getInstance()
                            .createStringItem(
                                "Unexpected error: "
                                    + ex.getMessage()
                                    + " We should investigate this. Please contact us or file an issue on GitHub with your query."
                            )
                    );
                    output.putItemByKey("error-code", ItemFactory.getInstance().createStringItem("RBST0004"));
                    Item stackTrace = ItemFactory.getInstance().createArrayItem();
                    output.putItemByKey("stack-trace", stackTrace);
                    for (StackTraceElement e : ex.getStackTrace()) {
                        stackTrace.append(ItemFactory.getInstance().createStringItem(e.toString()));
                    }
                    return output;
                }
            }
            Item output = ItemFactory.getInstance().createObjectItem();
            output.putItemByKey(
                "error-message",
                ItemFactory.getInstance()
                    .createStringItem(
                        "Unexpected error. We should investigate this. Please contact us or file an issue on GitHub with your query."
                    )
            );
            output.putItemByKey("error-code", ItemFactory.getInstance().createStringItem("RBST0004"));
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
