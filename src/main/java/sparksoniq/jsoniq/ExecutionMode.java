package sparksoniq.jsoniq;

public enum ExecutionMode {
    UNSET,
    LOCAL,
    RDD,
    DATAFRAME;

    public boolean isRDD() {
        return this == ExecutionMode.RDD || this == ExecutionMode.DATAFRAME;
    }

    public boolean isDataFrame() {
        return this == ExecutionMode.DATAFRAME;
    }
}
