package org.rumbledb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import org.apache.spark.SparkException;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sparksoniq.spark.SparkSessionManager;

public class RumbleHttpHandler implements HttpHandler {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    private enum StatusCode {
        SUCCESS(200),
        METHOD_NOT_SUPPORTED(405),
        SERVER_ERROR(500);

        private int code;

        StatusCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public RumbleHttpHandler(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    private void sendResponse(HttpExchange exchange, StatusCode code, String response) throws IOException {
        exchange.sendResponseHeaders(code.getCode(), response.getBytes().length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(response.getBytes());
        stream.close();
    }

    private String[] getCLIArguments(String query) throws UnsupportedEncodingException {
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
            validateRequest(exchange);
            String queryString = uri.getQuery();
            String[] args = getCLIArguments(queryString);

            RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(args);
            configuration.setAllowedURIPrefixes(this.rumbleRuntimeConfiguration.getAllowedURIPrefixes());
            validateConfiguration(exchange, configuration);
            SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();

            JsoniqQueryExecutor translator = new JsoniqQueryExecutor(configuration);
            List<Item> items = null;
            long count = -1;
            if (configuration.getQueryPath() != null) {
                items = translator.runQuery();
            } else {
                InputStreamReader r = new InputStreamReader(exchange.getRequestBody());
                BufferedReader r2 = new BufferedReader(r);
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = r2.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
                String JSONiqQuery = sb.toString();
                items = new ArrayList<Item>();
                count = translator.runInteractive(JSONiqQuery, items);
            }

            Item output = assembleResponse(configuration, items, count);

            this.sendResponse(exchange, StatusCode.SUCCESS, output.serialize());
        } catch (Exception e) {
            Item output = handleException(e);
            this.sendResponse(exchange, StatusCode.SUCCESS, output.serialize());
        }
    }


    private void validateRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET") && !exchange.getRequestMethod().equals("POST")) {
            this.sendResponse(
                exchange,
                StatusCode.METHOD_NOT_SUPPORTED,
                "Only the GET and POST methods are supported."
            );
        }
    }

    private void validateConfiguration(HttpExchange exchange, RumbleRuntimeConfiguration configuration)
            throws IOException {
        if (configuration.getOutputPath() != null && !exchange.getRequestMethod().equals("POST")) {
            this.sendResponse(
                exchange,
                StatusCode.METHOD_NOT_SUPPORTED,
                "The POST method must be used if specifying an output path, as this has side effects."
            );
        }
    }

    private static Item assembleResponse(RumbleRuntimeConfiguration configuration, List<Item> results, long count) {
        Item output = ItemFactory.getInstance().createObjectItem();
        if (configuration.getOutputPath() != null) {
            output.putItemByKey(
                "output-path",
                ItemFactory.getInstance().createStringItem(configuration.getOutputPath())
            );
        } else {
            if (results != null) {
                Item values = ItemFactory.getInstance().createArrayItem(results);
                output.putItemByKey("values", values);
            }
        }
        if (configuration.getLogPath() != null) {
            output.putItemByKey("log-path", ItemFactory.getInstance().createStringItem(configuration.getLogPath()));
        }
        if (count != -1) {
            output.putItemByKey(
                "warning",
                ItemFactory.getInstance()
                    .createStringItem(
                        "Warning! The output sequence contains "
                            + count
                            + " items but its materialization was capped at "
                            + SparkSessionManager.COLLECT_ITEM_LIMIT
                            + " items. This value can be configured with the result-size parameter in the query string of the HTTP request."
                    )
            );
        }
        return output;
    }

    private static Item assembleErrorReponse(String message, String code, StackTraceElement[] stackTraceElements) {
        Item output = ItemFactory.getInstance().createObjectItem();
        output.putItemByKey("error-message", ItemFactory.getInstance().createStringItem(message));
        output.putItemByKey(
            "error-code",
            ItemFactory.getInstance().createStringItem(code)
        );
        Item stackTrace = ItemFactory.getInstance().createArrayItem();
        if (stackTrace == null) {
            return output;
        }
        output.putItemByKey("stack-trace", stackTrace);
        for (StackTraceElement e : stackTraceElements) {
            stackTrace.append(ItemFactory.getInstance().createStringItem(e.toString()));
        }
        return output;
    }


    @SuppressWarnings("null")
    private static Item handleException(Throwable ex) {
        try {
            if (ex != null) {
                if (ex instanceof SparkException) {
                    Throwable sparkExceptionCause = ex.getCause();
                    if (sparkExceptionCause != null) {
                        return handleException(sparkExceptionCause);
                    }
                    return handleException(
                        new OurBadException(
                                "There was a problem with Spark, but Spark did not provide any cause or stracktrace. The message from Spark is:  "
                                    + ex.getMessage()
                        )
                    );
                } else if (ex instanceof RumbleException && !(ex instanceof OurBadException)) {
                    return assembleErrorReponse(
                        ex.getMessage(),
                        ((RumbleException) ex).getErrorCode(),
                        ex.getStackTrace()
                    );
                } else if (ex instanceof OutOfMemoryError) {
                    return assembleErrorReponse(
                        "⚠️  Java went out of memory."
                            + " If running locally, try adding --driver-memory 10G (or any quantity you need) between spark-submit and the RumbleDB jar in the command line to see if it fixes the problem. If running on a cluster, --executor-memory is the way to go.",
                        ErrorCode.OurBadErrorCode.toString(),
                        ex.getStackTrace()
                    );
                } else if (ex instanceof IllegalArgumentException) {
                    return assembleErrorReponse(
                        "It seems that you are not using Java 8. Spark only works with Java 8. If you have several versions of java installed, you need to set your JAVA_HOME accordingly. If you do not have Java 8 installed, we recommend installing AdoptOpenJDK 1.8.",
                        ErrorCode.OurBadErrorCode.toString(),
                        ex.getStackTrace()
                    );
                } else if (ex instanceof CannotCompileException) {
                    return assembleErrorReponse(
                        "⚠️  There was a CannotCompileException."
                            +
                            " There is a known issue with this on Docker and on certain versions of OpenJDK due to the JSONiter library."
                            +
                            " We have a workaround: please try again using --deactivate-jsoniter-streaming yes on your command line. json-doc() will, however, not be available."
                            +
                            " For more debug info, please try again using --show-error-info yes in your command line.",
                        ErrorCode.OurBadErrorCode.toString(),
                        ex.getStackTrace()
                    );
                } else if (ex instanceof ConnectException) {
                    return assembleErrorReponse(
                        "There was a problem with the connection to the cluster.",
                        ErrorCode.ClusterConnectionErrorCode.toString(),
                        ex.getStackTrace()
                    );
                } else if (ex instanceof NullPointerException) {
                    return assembleErrorReponse(
                        "There was a null pointer exception."
                            +
                            " We would like to investigate this and make sure to fix it in a subsequent release. We would be very grateful if you could contact us or file an issue on GitHub with your query."
                            +
                            " Link: https://github.com/RumbleDB/rumble/issues."
                            +
                            " For more debug info (e.g., so you can communicate it to us), please try again using --show-error-info yes in your command line.",
                        ErrorCode.OurBadErrorCode.toString(),
                        ex.getStackTrace()
                    );
                } else {
                    return assembleErrorReponse(
                        "An error has occured: "
                            + ex.getMessage()
                            + " We should investigate this 🙈. Please contact us or file an issue on GitHub with your query. Link: https://github.com/RumbleDB/rumble/issues",
                        ErrorCode.OurBadErrorCode.toString(),
                        ex.getStackTrace()
                    );
                }
            }
            return assembleErrorReponse(
                "Unexpected error: "
                    + ex.getMessage()
                    + " We should investigate this. Please contact us or file an issue on GitHub with your query.",
                ErrorCode.OurBadErrorCode.toString(),
                null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
