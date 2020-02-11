package sparksoniq.spark.iterator.function;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;

import sparksoniq.spark.SparkSessionManager;

import java.io.File;
import java.io.IOException;

public class UrlValidator {

    public static boolean isValid(String url) {
        if (url.startsWith("file://") || url.startsWith("/") || url.startsWith("./") || url.startsWith("hdfs://") || url.startsWith("s3://")) {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            try {
                FileSystem fileSystem = FileSystem.get(sparkContext.hadoopConfiguration());
                Path path = new Path(url);
                return url.contains("*") || fileSystem.exists(path);
            } catch (IOException e) {
                throw new SparksoniqRuntimeException("Error while accessing hadoop filesystem.");
            }

        }

        throw new OurBadException("Unhandled url type found in the validator.");
    }
}
