package org.rumbledb.context;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.items.structured.JSoundDataFrame;

import java.util.List;

public class VariableMapAndPrevValue {
    private final Name variableName;
    private List<Item> valueReferenceListItem;
    private JavaRDD<Item> valueReferenceRDD;
    private JSoundDataFrame valueReferenceDF;

    public VariableMapAndPrevValue(Name variableName, List<Item> valueReferenceListItem) {
        this.variableName = variableName;
        this.valueReferenceListItem = valueReferenceListItem;
        this.valueReferenceRDD = null;
        this.valueReferenceDF = null;
    }

    public VariableMapAndPrevValue(Name variableName, JavaRDD<Item> valueReferenceRDD) {
        this.variableName = variableName;
        this.valueReferenceRDD = valueReferenceRDD;
        this.valueReferenceListItem = null;
        this.valueReferenceDF = null;
    }

    public VariableMapAndPrevValue(Name variableName, JSoundDataFrame valueReferenceDF) {
        this.variableName = variableName;
        this.valueReferenceDF = valueReferenceDF;
        this.valueReferenceListItem = null;
        this.valueReferenceRDD = null;
    }


    public Name getVariableName() {
        return variableName;
    }

    public List<Item> getValueReferenceListItem() {
        return valueReferenceListItem;
    }

    public JSoundDataFrame getValueReferenceDF() {
        return valueReferenceDF;
    }

    public JavaRDD<Item> getValueReferenceRDD() {
        return valueReferenceRDD;
    }

    public boolean isListItem() {
        return this.valueReferenceListItem != null;
    }

    public boolean isRDD() {
        return this.valueReferenceRDD != null;
    }

    public boolean isDF() {
        return this.valueReferenceDF != null;
    }
}
