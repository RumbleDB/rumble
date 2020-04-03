package org.rumbledb.runtime.functions.input;

import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;

public class UrlValidator {

    public static URI validateURI(String url, ExceptionMetadata metadata) {
        if (url.isEmpty()) {
            throw new CannotRetrieveResourceException(
                    "No path provided!",
                    new ExceptionMetadata(0, 0)
            );
        }
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new CannotRetrieveResourceException("Malformed URL: " + url, metadata);
        }
    }

    public static boolean exists(String url, ExceptionMetadata metadata) {
        URI locator = validateURI(url, metadata);
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(url);
            return url.contains("*") || fileContext.util().exists(path);

        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + url + "!",
                    metadata
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new CannotRetrieveResourceException(
                    "Error while accessing the " + locator.getScheme() + " filesystem.",
                    metadata
            );
        }
    }

    public static boolean delete(String url, ExceptionMetadata metadata) {
        URI locator = validateURI(url, metadata);
        if (url.contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(url);
            if (!fileContext.util().exists(path)) {
                throw new OurBadException("Cannot delete file that does not exist: " + url);
            }
            return fileContext.delete(path, true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CannotRetrieveResourceException(
                    "Error while accessing the " + locator.getScheme() + " filesystem.",
                    metadata
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurBadException(
                    "An unexpected exception happened while deleting.",
                    metadata
            );
        }
    }

    public static FSDataInputStream getDataInputStream(String url, ExceptionMetadata metadata) {
        URI locator = validateURI(url, metadata);
        if (url.contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            locator = new URI(url);
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(url);
            if (!fileContext.util().exists(path)) {
                throw new OurBadException("Cannot read file that does not exist: " + url);
            }
            return fileContext.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CannotRetrieveResourceException(
                    "Error while accessing the " + locator.getScheme() + " filesystem.",
                    metadata
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurBadException(
                    "An unexpected exception happened while reading the file.",
                    metadata
            );
        }
    }

    public static void write(String url, List<String> content, ExceptionMetadata metadata) {
        URI locator = validateURI(url, metadata);
        if (url.contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            locator = new URI(url);
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(url);
            FSDataOutputStream outputStream = fileContext.create(
                path,
                EnumSet.of(CreateFlag.CREATE, CreateFlag.OVERWRITE)
            );
            for (String s : content) {
                outputStream.writeBytes(s);
                outputStream.writeBytes("\n");
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CannotRetrieveResourceException(
                    "Error while accessing the " + locator.getScheme() + " filesystem.",
                    metadata
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurBadException(
                    "An unexpected exception happened while writing the file.",
                    metadata
            );
        }
    }
}
