package iq;

import iq.base.SparkAnnotationsTestsBase;
import org.rumbledb.config.RumbleConfiguration;

import java.io.File;

public class StaticTypeTests extends SparkAnnotationsTestsBase {

    protected static final RumbleConfiguration configuration = RumbleConfiguration.builder()
        .configureDebug(debug -> debug.printIteratorTree(true))
        .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
        .build();

    public static final File staticTypeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/static-typing"
    );

    @Override
    public RumbleConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    protected File testDirectory() {
        return staticTypeTestsDirectory;
    }

    @Override
    protected boolean checkOutput() {
        return false;
    }
}
