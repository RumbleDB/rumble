package utils;

import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

public class ApproximatedDoubleComparator extends DefaultComparator {
    private Double accuracy;

    public ApproximatedDoubleComparator(JSONCompareMode mode, Double accuracy) {
        super(mode);
        this.accuracy = accuracy;
    }

    @Override
    protected boolean areNotSameDoubles(Object expectedValue, Object actualValue) {
        return Math.abs(((Number) expectedValue).doubleValue() - ((Number) actualValue).doubleValue()) >= this.accuracy;
    }
}
