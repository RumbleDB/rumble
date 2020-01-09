package sparksoniq.jsoniq;

public enum ExecutionMode {
    UNSET,
    LOCAL,
    RDD,
    DF;

    public boolean isRDD() {
        return this == ExecutionMode.RDD || this == ExecutionMode.DF;
    }

    public boolean isDF() {
        return this == ExecutionMode.DF;
    }
}
