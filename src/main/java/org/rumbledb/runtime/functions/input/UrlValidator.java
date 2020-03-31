package org.rumbledb.runtime.functions.input;

import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class UrlValidator {

    private static final String[] allowedSchemes = { "file", "hdfs", "s3", "s3a", "s3n", "wasb", "gs", "root" };

    public static boolean exists(String url, ExceptionMetadata metadata) {
        URI locator = null;
        try {
            locator = new URI(url);
        } catch (URISyntaxException e) {
            throw new CannotRetrieveResourceException("Malformed URL: " + url, metadata);
        }
        if (locator.isAbsolute() && !Arrays.asList(allowedSchemes).contains(locator.getScheme())) {
            throw new OurBadException("Cannot interpret URL: " + url);
        }
        {
            try {
                FileContext fileContext = FileContext.getFileContext();
                Path path = new Path(url);
                return url.contains("*") || fileContext.util().exists(path);

            } catch (UnsupportedFileSystemException e) {
                throw new CannotRetrieveResourceException(
                        "No file system is configured for scheme " + url + "!",
                        metadata
                );
            } catch (IOException e) {
                e.printStackTrace();
                throw new CannotRetrieveResourceException(
                        "Error while accessing the " + locator.getScheme() + " filesystem.",
                        metadata
                );
            }
        }
    }
}
