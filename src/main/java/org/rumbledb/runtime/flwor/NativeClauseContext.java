package org.rumbledb.runtime.flwor;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the context of a native clause and is used when processing FLWOR expressions without UDF
 */
public class NativeClauseContext {
    public static NativeClauseContext NoNativeQuery = new NativeClauseContext();

    private FLWOR_CLAUSES clauseType;
    private DataType schema;
    private DynamicContext context;
    private String resultingQuery;
    private List<String> lateralViewPart; // used in array unboxing to generate the correct lateral view

    private NativeClauseContext() {
    }

    public NativeClauseContext(FLWOR_CLAUSES clauseType, StructType schema, DynamicContext context) {
        this.clauseType = clauseType;
        this.schema = schema;
        this.context = context;
        this.resultingQuery = "";
        this.lateralViewPart = new ArrayList<>();
    }

    public NativeClauseContext(NativeClauseContext parent) {
        this.clauseType = parent.clauseType;
        this.schema = parent.schema;
        this.context = parent.context;
        this.resultingQuery = parent.resultingQuery;
        this.lateralViewPart = parent.lateralViewPart;
    }

    public NativeClauseContext(NativeClauseContext parent, String newResultingQuery) {
        this.clauseType = parent.clauseType;
        this.schema = parent.schema;
        this.context = parent.context;
        this.resultingQuery = newResultingQuery;
        this.lateralViewPart = parent.lateralViewPart;
    }

    public FLWOR_CLAUSES getClauseType() {
        return this.clauseType;
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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Native clause context.\n");
        sb.append("Query: " + this.resultingQuery + "\n");
        sb.append("Clause type: " + this.clauseType + "\n");
        sb.append("Schema: " + this.schema.toString() + "\n");
        sb.append("Context: " + this.context.toString() + "\n");
        sb.append("Lateral views:\n");
        for (String s : this.lateralViewPart) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}
