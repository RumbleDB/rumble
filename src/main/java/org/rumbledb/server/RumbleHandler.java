package org.rumbledb.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.parser.JsoniqLexer;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.vanderbilt.accre.laurelin.array.Array;
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
                queryParameters.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
            }
            String[] args = new String[queryParameters.keySet().size() * 2];
            int i = 0;
            for(String param : queryParameters.keySet())
            {
                args[i++] = "--" + param;
                args[i++] = queryParameters.get(param);
                System.out.println(param);
                System.out.println(queryParameters.get(param));
            }
            System.out.println(args.length);
            System.out.println(args[0]);
            System.out.println(args[1]);
            RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(args);
            SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
            System.out.println(configuration.getQueryPath());
            System.out.println(configuration.getOutputPath());
            
            JsoniqQueryExecutor translator = new JsoniqQueryExecutor(configuration);
            String response = translator.runQuery(configuration.getQueryPath(), configuration.getOutputPath());
            System.out.println(response);
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(response.getBytes());
            stream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, 0);

        }
    }
    
}
