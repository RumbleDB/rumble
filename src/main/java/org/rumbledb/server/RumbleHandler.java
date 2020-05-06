package org.rumbledb.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkException;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.items.ItemFactory;

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
            List<Item> items = translator.runQuery(
                configuration.getQueryPath(),
                configuration.getOutputPath()
            );

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

            exchange.sendResponseHeaders(200, output.serialize().getBytes().length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(output.serialize().getBytes());
            stream.close();
        } catch (Exception e) {
            Item output = handleException(e);
            exchange.sendResponseHeaders(500, output.serialize().getBytes().length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(output.serialize().getBytes());
            stream.close();
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
                    return handleException(new SparksoniqRuntimeException(ex.getMessage()));
                } else if (ex instanceof SparksoniqRuntimeException && !(ex instanceof OurBadException)) {
                    Item output = ItemFactory.getInstance().createObjectItem();
                    output.putItemByKey("error-message", ItemFactory.getInstance().createStringItem(ex.getMessage()));
                    output.putItemByKey(
                        "error-code",
                        ItemFactory.getInstance().createStringItem(((SparksoniqRuntimeException) ex).getErrorCode())
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
                        "Unexpected error: "
                            + ex.getMessage()
                            + " We should investigate this. Please contact us or file an issue on GitHub with your query."
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
