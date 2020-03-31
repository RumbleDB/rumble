package org.rumbledb.runtime.functions.input;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class UrlValidator {

    private static final String[] allowedSchemes = { "file", "hdfs", "s3", "s3a", "s3n", "wasb", "gs", "root" };

    public static boolean check(String url, ExceptionMetadata metadata) {
        URI locator = null;
        try {
            locator = new URI(url);
        } catch (URISyntaxException e) {
            throw new CannotRetrieveResourceException("Malformed URL: " + url, metadata);
        }
        if (locator.isAbsolute() && !Arrays.asList(allowedSchemes).contains(locator.getScheme())) {
            throw new OurBadException("Cannot interpret URL: " + url);
        }
        if (
            !locator.isAbsolute() || (locator.isAbsolute() && locator.getScheme().equals("hdfs"))
        ) {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            try {
                FileSystem fileSystem = FileSystem.get(sparkContext.hadoopConfiguration());
                Path path = new Path(url);
                return url.contains("*") || fileSystem.exists(path);
            } catch (IOException e) {
                throw new CannotRetrieveResourceException("Error while accessing local or HDFS filesystem.", metadata);
            }

        }
        return true;
    }
}
