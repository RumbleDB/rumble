package org.rumbledb.runtime.flwor;

import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;

/**
 * This class describes the context of a native clause and is used when processing FLWOR expressions without UDF
 */
public class NativeClauseContext {
    public static NativeClauseContext NoNativeQuery = new NativeClauseContext();

    private FLWOR_CLAUSES clauseType;
    private StructType schema;
    private DynamicContext context;
    private String selectPart;

    private NativeClauseContext() {}

    public NativeClauseContext(FLWOR_CLAUSES clauseType, StructType schema, DynamicContext context){
        this.clauseType = clauseType;
        this.schema = schema;
        this.context = context;
        this.selectPart = "";
    }

    public NativeClauseContext(NativeClauseContext parent){
        this.clauseType = parent.clauseType;
        this.schema = parent.schema;
        this.context = parent.context;
        this.selectPart = parent.selectPart;
    }

    public NativeClauseContext(NativeClauseContext parent, String newSelectPart){
        this.clauseType = parent.clauseType;
        this.schema = parent.schema;
        this.context = parent.context;
        this.selectPart = newSelectPart;
    }

    public void setSelectPart(String selectPart) {
        this.selectPart = selectPart;
    }

    public String getSelectPart(){
        return this.selectPart;
    }
}
