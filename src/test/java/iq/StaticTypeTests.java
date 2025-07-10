package iq;

import org.rumbledb.config.RumbleRuntimeConfiguration;

import iq.base.AnnotationsTestsBase;
import scala.Function0;
import scala.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.api.Item;
import org.rumbledb.api.SequenceOfItems;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
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
    protected static List<File> _testFiles = new ArrayList<>();
    protected final File testFile;

    public StaticTypeTests(File testFile) {
        this.testFile = testFile;
    }

    public static void readFileList(File dir) {
        FileManager.loadJiqFiles(dir).forEach(file -> StaticTypeTests._testFiles.add(file));
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        StaticTypeTests.readFileList(StaticTypeTests.staticTypeTestsDirectory);
        StaticTypeTests._testFiles.forEach(file -> result.add(new Object[] { file }));
        return result;
    }

    @BeforeClass
    public static void setupSparkSession() {
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
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    @Test(timeout = 1000000)
    public void testRuntimeIterators() throws Throwable {
        System.err.println(AnnotationsTestsBase.counter++ + " : " + this.testFile);
        testAnnotations(this.testFile.getAbsolutePath(), StaticTypeTests.configuration);
    }

    @Override
    protected void checkExpectedOutput(
            String expectedOutput,
            SequenceOfItems sequence
    ) {
        @SuppressWarnings("unused")
        String actualOutput;
        if (!sequence.availableAsRDD()) {
            actualOutput = runIterators(sequence);
        } else {
            actualOutput = getRDDResults(sequence);
        }
        // For static typing check we just need to check that the program run, no need to compare the output
        Assert.assertTrue(true);
    }

    protected String runIterators(SequenceOfItems sequence) {
        String actualOutput = getIteratorOutput(sequence);
        return actualOutput;
    }

    protected String getIteratorOutput(SequenceOfItems sequence) {
        sequence.open();
        Item result = null;
        if (sequence.hasNext()) {
            result = sequence.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!sequence.hasNext()) {
            return singleOutput;
        } else {
            int itemCount = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(result.serialize());
            sb.append(", ");
            while (
                sequence.hasNext()
                    &&
                    ((itemCount < getConfiguration().getResultSizeCap()
                        && getConfiguration().getResultSizeCap() > 0)
                        ||
                        getConfiguration().getResultSizeCap() == 0)
            ) {
                sb.append(sequence.next().serialize());
                sb.append(", ");
                itemCount++;
            }
            if (sequence.hasNext() && itemCount == getConfiguration().getResultSizeCap()) {
                System.err.println(
                    "Warning! The output sequence contains a large number of items but its materialization was capped at "
                        + SparkSessionManager.COLLECT_ITEM_LIMIT
                        + " items. This value can be configured with the --result-size parameter at startup"
                );
            }
            // remove last comma
            String output = sb.toString();
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }

    private String getRDDResults(SequenceOfItems sequence) {
        JavaRDD<Item> rdd = sequence.getAsRDD();
        JavaRDD<String> output = rdd.map(o -> o.serialize());
        List<String> collectedOutput = new ArrayList<String>();
        SparkSessionManager.collectRDDwithLimitWarningOnly(output, collectedOutput);

        if (collectedOutput.isEmpty()) {
            return "";
        }

        if (collectedOutput.size() == 1) {
            return collectedOutput.get(0);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String item : collectedOutput) {
            sb.append(item);
            sb.append(", ");
        }

        String result = sb.toString();
        result = result.substring(0, result.length() - 2);
        result += ")";
        return result;
    }
}
