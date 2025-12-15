package org.rumbledb.runtime.update.primitives;

public enum Mode {
    HIVE,
    DELTA,
    HUDI,
    ICEBERG;

    public static Mode fromString(String modeStr) {
        switch (modeStr) {
            case "table":
                return HIVE;
            case "delta-file":
                return DELTA;
            case "iceberg-table":
                return ICEBERG;
            default:
                throw new IllegalArgumentException("Unknown mode: " + modeStr);
        }
    }
}
