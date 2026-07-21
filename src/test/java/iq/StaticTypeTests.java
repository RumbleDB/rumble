package iq;

import org.rumbledb.config.RumbleRuntimeConfiguration;

import iq.base.SparkAnnotationsTestsBase;

import java.io.File;

public class StaticTypeTests extends SparkAnnotationsTestsBase {

    protected static final RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(
            new String[] { "--static-typing", "yes" }
    );

    public static final File staticTypeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/static-typing"
    );

    @Override
    public RumbleRuntimeConfiguration getConfiguration() {
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
