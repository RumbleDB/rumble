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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;

public class FileSystemUtil {

    public static URI resolveURI(URI base, String url, ExceptionMetadata metadata) {
        if (url == null || url.isEmpty()) {
            throw new CannotRetrieveResourceException(
                    "No path provided!",
                    metadata
            );
        }
        if (!base.isAbsolute()) {
            throw new OurBadException(
                    "The base URI is not absolute!",
                    metadata
            );
        }
        try {
            return base.resolve(url);
        } catch (IllegalArgumentException e) {
            throw new CannotRetrieveResourceException("Malformed URI: " + url, metadata);
        }
    }

    public static URI resolveURIAgainstWorkingDirectory(String url, ExceptionMetadata metadata) {
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path workingDirectory = fileContext.getWorkingDirectory();
            if (url == null || url.isEmpty()) {
                return new Path(workingDirectory, ".").toUri();
            }
            return new Path(workingDirectory, url).toUri();
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "The default file system is not supported!",
                    metadata
            );
        } catch (IllegalArgumentException e) {
            throw new CannotRetrieveResourceException("Malformed URI: " + url, metadata);
        }
    }

    public static boolean exists(URI locator, ExceptionMetadata metadata) {
        if (!locator.isAbsolute()) {
            throw new OurBadException("Unresolved uri passed to exists()");
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(locator);
            return locator.toString().contains("*") || fileContext.util().exists(path);

        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
                    metadata
            );
        } catch (IOException e) {
            throw new CannotRetrieveResourceException(
                    "I/O error while accessing the " + locator.getScheme() + " filesystem. File: " + locator,
                    metadata
            );
        }
    }

    public static boolean delete(URI locator, ExceptionMetadata metadata) {
        if (!locator.isAbsolute()) {
            throw new OurBadException("Unresolved uri passed to delete()");
        }
        if (locator.toString().contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(locator);
            if (!fileContext.util().exists(path)) {
                throw new CannotRetrieveResourceException(
                        "Cannot delete file that does not exist: " + locator,
                        metadata
                );
            }
            return fileContext.delete(path, true);
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
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

    public static FSDataInputStream getDataInputStream(URI locator, ExceptionMetadata metadata) {
        if (!locator.isAbsolute()) {
            throw new OurBadException("Unresolved uri passed to getDataInputStream()");
        }
        if (locator.toString().contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(locator);
            if (!fileContext.util().exists(path)) {
                throw new CannotRetrieveResourceException("File does not exist: " + locator, metadata);
            }
            return fileContext.open(path);
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
                    metadata
            );
        } catch (IOException e) {
            throw new CannotRetrieveResourceException(
                    "I/O error while accessing the " + locator.getScheme() + " filesystem. File: " + locator,
                    metadata
            );
        }
    }

    public static String readContent(URI locator, ExceptionMetadata metadata) {
        FSDataInputStream inputStream = getDataInputStream(locator, metadata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
                    metadata
            );
        } catch (IOException e) {
            throw new CannotRetrieveResourceException(
                    "I/O error while accessing the " + locator.getScheme() + " filesystem. File: " + locator,
                    metadata
            );
        }
    }

    public static void write(URI locator, List<String> content, ExceptionMetadata metadata) {
        if (!locator.isAbsolute()) {
            throw new OurBadException("Unresolved uri passed to write()");
        }
        if (locator.toString().contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(locator);
            FSDataOutputStream outputStream = fileContext.create(
                path,
                EnumSet.of(CreateFlag.CREATE, CreateFlag.OVERWRITE)
            );
            for (String s : content) {
                outputStream.writeBytes(s);
                outputStream.writeBytes("\n");
            }
            outputStream.close();
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
                    metadata
            );
        } catch (IOException e) {
            throw new CannotRetrieveResourceException(
                    "I/O error while accessing the " + locator.getScheme() + " filesystem. File: " + locator,
                    metadata
            );
        }
    }

    public static void append(URI locator, List<String> content, ExceptionMetadata metadata) {
        if (!locator.isAbsolute()) {
            throw new OurBadException("Unresolved uri passed to append()");
        }
        if (locator.toString().contains("*")) {
            throw new CannotRetrieveResourceException(
                    "Path cannot contain *!",
                    metadata
            );
        }
        try {
            FileContext fileContext = FileContext.getFileContext();
            Path path = new Path(locator);
            FSDataOutputStream outputStream = fileContext.create(
                path,
                EnumSet.of(CreateFlag.CREATE, CreateFlag.APPEND)
            );
            for (String s : content) {
                outputStream.writeBytes(s);
                outputStream.writeBytes("\n");
            }
            outputStream.close();
        } catch (UnsupportedFileSystemException e) {
            throw new CannotRetrieveResourceException(
                    "No file system is configured for scheme " + locator.getScheme() + "!",
                    metadata
            );
        } catch (IOException e) {
            throw new CannotRetrieveResourceException(
                    "I/O error while accessing the " + locator.getScheme() + " filesystem. File: " + locator,
                    metadata
            );
        }
    }
}
