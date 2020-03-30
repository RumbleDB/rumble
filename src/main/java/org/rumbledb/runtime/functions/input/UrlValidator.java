package org.rumbledb.runtime.functions.input;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class UrlValidator {

    private static final String[] allowedSchemes = { "file", "hdfs", "s3", "s3a", "s3n", "wasb", "gs", "root" };

    public static boolean exists(String url) {
        URI locator = null;
        try {
            locator = new URI(url);
        } catch (URISyntaxException e) {
            throw new OurBadException("Malformed URL: " + url);
        }
        if (
            !locator.isAbsolute()
            || Arrays.asList(allowedSchemes).contains(locator.getScheme())
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

        throw new OurBadException("Cannot interpret URL: " + url);
    }
}
