package org.rumbledb.runtime.functions.delta_lake;

import org.rumbledb.config.RumbleRuntimeConfiguration;

public class DeltaLakeConfigurationCatalogue {
    static final RumbleRuntimeConfiguration defaultDeltaLakeConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--output-format",
                    "delta",
                    "--show-error-info",
                    "yes",
                    "--apply-updates",
                    "yes",
            }
    );

    static final RumbleRuntimeConfiguration createDeltaLakeConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--output-format",
                    "delta",
                    "--show-error-info",
                    "yes",
                    "--apply-updates",
                    "yes",
            }
    );

}
