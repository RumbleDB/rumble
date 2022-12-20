package org.rumbledb.runtime.flwor;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the context of a native clause and is used when processing FLWOR expressions without UDF
 */
public class NativeClauseContext {
    public static NativeClauseContext NoNativeQuery = new NativeClauseContext();

    private NativeClauseContext parent;
    private FLWOR_CLAUSES clauseType;
    private DataType schema;
    private DynamicContext context;
    private String resultingQuery;
    private List<String> lateralViewPart; // used in array unboxing to generate the correct lateral view
    private SequenceType resultingType;

    private boolean grouped;

    private List<String> conditionalColumns;  // used in where clauses

    private String tempView;

    private int resultColumnId;

    private boolean isExplodedView;  // if the view is exploded, then the result is a sequence; otherwise it's an atomic item


    private NativeClauseContext() {
    }

    public NativeClauseContext(FLWOR_CLAUSES clauseType, StructType schema, DynamicContext context) {
        this.clauseType = clauseType;
        this.schema = schema;
        this.context = context;
        this.resultingQuery = "";
        this.lateralViewPart = new ArrayList<>();
        this.resultingType = null;
        this.grouped = false;
        this.parent = null;
        this.conditionalColumns = new ArrayList<>();
        this.resultColumnId = 0;
        this.isExplodedView = false;
    }

    public NativeClauseContext(NativeClauseContext sibling) {
        this.clauseType = sibling.clauseType;
        this.schema = sibling.schema;
        this.context = sibling.context;
        this.resultingQuery = sibling.resultingQuery;
        this.lateralViewPart = sibling.lateralViewPart;
        this.resultingType = sibling.resultingType;
        this.grouped = sibling.grouped;
        this.parent = sibling.parent;
        this.conditionalColumns = sibling.conditionalColumns;
        this.resultColumnId = sibling.resultColumnId;
        this.isExplodedView = sibling.isExplodedView;
    }

    public NativeClauseContext(NativeClauseContext sibling, String newResultingQuery, SequenceType resultingType) {
        this.clauseType = sibling.clauseType;
        this.schema = sibling.schema;
        this.context = sibling.context;
        this.resultingQuery = newResultingQuery;
        this.lateralViewPart = sibling.lateralViewPart;
        this.resultingType = resultingType;
        this.tempView = sibling.tempView;
        this.grouped = sibling.grouped;
        this.parent = sibling.parent;
        this.isExplodedView = sibling.isExplodedView;
    }

    public NativeClauseContext createChild(){
        StructType schema = new StructType(((StructType)this.schema).fields());
        NativeClauseContext result = new NativeClauseContext(FLWOR_CLAUSES.RETURN, schema, this.context);
        result.parent = this;
        return this;
    }

    public FLWOR_CLAUSES getClauseType() {
        return this.clauseType;
    }

    public void setClauseType(FLWOR_CLAUSES clauseType) {
        this.clauseType = clauseType;
    }

    public void setResultingQuery(String resultingQuery) {
        this.resultingQuery = resultingQuery;
    }

    public String getResultingQuery() {
        return this.resultingQuery;
    }

    public DataType getSchema() {
        return this.schema;
    }

    public void setSchema(DataType schema) {
        this.schema = schema;
    }

    public DynamicContext getContext() {
        return this.context;
    }

    public List<String> getLateralViewPart() {
        return this.lateralViewPart;
    }

    public SequenceType getResultingType() {
        return this.resultingType;
    }

    public void setResultingType(SequenceType resultingType) {
        this.resultingType = resultingType;
    }


    public String getTempView() {
        return this.tempView;
    }

    public void setTempView(String tempView) {
        this.tempView = tempView;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public boolean getGrouped() {
        return this.grouped;
    }

    public void addConditionalColumn(String name) {
        this.conditionalColumns.add(name);
    }

    public List<String> getConditionalColumns(){
        List<String> result = new ArrayList<>(this.conditionalColumns);
        if (this.parent != null){
            result.addAll(parent.getConditionalColumns());
        }
        return result;
    }

    public int getAndIncreaseResultColumnId(){
        return this.resultColumnId++;
    }

    public void setExplodedView(boolean isExplodedView) {
        this.isExplodedView = isExplodedView;
    }

    public boolean isExplodedView() {
        return isExplodedView;
    }
}
