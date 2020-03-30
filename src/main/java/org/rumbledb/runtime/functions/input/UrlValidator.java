package org.rumbledb.runtime.functions.input;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class UrlValidator {

    private static final String[] allowedSchemes = { "file", "hdfs", "s3", "s3a", "s3n", "wasb", "gs", "root" };

    public static boolean exists(String url) {
        URL locator = null;
        try {
            locator = new URL(url);
        } catch (MalformedURLException e) {
            throw new OurBadException("Malformed URL: " + url);
        }
        if (
            url.startsWith("/")
                || url.startsWith("./")
                || Arrays.asList(allowedSchemes).contains(locator.getProtocol())
        ) {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            try {
                FileSystem fileSystem = FileSystem.get(sparkContext.hadoopConfiguration());
                Path path = new Path(url);
                return url.contains("*") || fileSystem.exists(path);
            } catch (IOException e) {
                throw new SparksoniqRuntimeException("Error while accessing hadoop filesystem.");
            }

        }

        throw new OurBadException("Cannot interprete URL: " + url);
    }
}
