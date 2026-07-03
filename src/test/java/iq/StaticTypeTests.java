package iq;

import org.rumbledb.config.RumbleRuntimeConfiguration;

import iq.base.AnnotationsTestsBase;
import scala.Function0;
import scala.util.Properties;

import org.apache.spark.SparkConf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import sparksoniq.spark.SparkSessionManager;

import java.io.File;
import java.util.List;

@ParameterizedClass
@MethodSource("testFiles")
public class StaticTypeTests extends AnnotationsTestsBase {

    protected static final RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(
            new String[] { "--print-iterator-tree", "yes", "--static-typing", "yes" }
    );

    public static final File staticTypeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/static-typing"
    );
    public static final String javaVersion =
        System.getProperty("java.version");
    public static final String scalaVersion =
        Properties.scalaPropOrElse("version.number", new Function0<String>() {
            @Override
            public String apply() {
                return "unknown";
            }
        });
    @Parameter
    File testFile;

    public static List<File> testFiles() {
        return loadTestFiles(staticTypeTestsDirectory);
    }

    @BeforeAll
    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + javaVersion);
        System.err.println("Scala version: " + scalaVersion);
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product

        // prevents spark from failing to start on MacOS when disconnected from the internet
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");


        // sparkConfiguration.set("spark.driver.memory", "2g");
        // sparkConfiguration.set("spark.executor.memory", "2g");
        // sparkConfiguration.set("spark.speculation", "true");
        // sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    @Test
    @Timeout(1000)
    public void testRuntimeIterators() throws Throwable {
        System.err.println(counter++ + " : " + this.testFile);
        testAnnotations(
            this.testFile.getAbsolutePath(),
            StaticTypeTests.configuration,
            false,
            StaticTypeTests.configuration.applyUpdates(),
            StaticTypeTests.configuration.getResultSizeCap()
        );
    }
}
