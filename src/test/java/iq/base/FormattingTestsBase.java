package iq.base;

import java.io.File;

import org.rumbledb.config.RumbleConfiguration;

public abstract class FormattingTestsBase extends AnnotationsTestsBase {

    private static final String TEST_FILES_ROOT = System.getProperty("user.dir") + "/src/test/resources/test_files/";

    protected abstract String subdirectory();

    @Override
    protected File testDirectory() {
        return new File(TEST_FILES_ROOT + subdirectory());
    }

    @Override
    public RumbleConfiguration getConfiguration() {
        return TestConfigurations.defaultConfigurationBuilder()
                .configureRuntime(runtime -> runtime.resultsSizeCap(200).materializationCap(100000))
                .build();
    }
}
