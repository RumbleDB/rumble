package org.rumbledb.runtime.flwor;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.types.SequenceType;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<String> conditionalColumns; // used in where clauses

    private String tempView;

    private int monotonicallyIncreasingId;

    private boolean isExplodedView; // if the view is exploded, then the result is a sequence; otherwise it's atomic

    private List<Name> positionalVariableNames;

    private Map<String, Boolean> sortingColumns;

    private Map<Name, Name> variables;
    private String rowIdField;


    private NativeClauseContext() {
    }

    public NativeClauseContext(FLWOR_CLAUSES clauseType, StructType schema, DynamicContext context) {
        this.clauseType = clauseType;
        this.schema = schema;
        this.context = context;
        this.resultingQuery = "";
        this.lateralViewPart = new ArrayList<>();
        this.resultingType = null;
        this.parent = null;
        this.conditionalColumns = new ArrayList<>();
        this.monotonicallyIncreasingId = 0;
        this.isExplodedView = false;
        this.positionalVariableNames = new ArrayList<>();
        this.sortingColumns = new HashMap<>();
        this.variables = new HashMap<>();
        this.rowIdField = null;
    }

    public NativeClauseContext(NativeClauseContext sibling) {
        this.clauseType = sibling.clauseType;
        this.schema = sibling.schema;
        this.context = sibling.context;
        this.resultingQuery = sibling.resultingQuery;
        this.lateralViewPart = sibling.lateralViewPart;
        this.resultingType = sibling.resultingType;
        this.parent = sibling.parent;
        this.conditionalColumns = sibling.conditionalColumns;
        this.monotonicallyIncreasingId = sibling.monotonicallyIncreasingId;
        this.isExplodedView = sibling.isExplodedView;
        this.positionalVariableNames = sibling.positionalVariableNames;
        this.sortingColumns = sibling.sortingColumns;
        this.variables = sibling.variables;
        this.rowIdField = sibling.rowIdField;
    }

    public NativeClauseContext(NativeClauseContext sibling, String newResultingQuery, SequenceType resultingType) {
        this.clauseType = sibling.clauseType;
        this.schema = sibling.schema;
        this.context = sibling.context;
        this.resultingQuery = newResultingQuery;
        this.lateralViewPart = sibling.lateralViewPart;
        this.resultingType = resultingType;
        this.tempView = sibling.tempView;
        this.conditionalColumns = sibling.conditionalColumns;
        this.monotonicallyIncreasingId = sibling.monotonicallyIncreasingId;
        this.parent = sibling.parent;
        this.isExplodedView = sibling.isExplodedView;
        this.positionalVariableNames = sibling.positionalVariableNames;
        this.sortingColumns = sibling.sortingColumns;
        this.variables = sibling.variables;
        this.rowIdField = sibling.rowIdField;
    }

    public NativeClauseContext createChild() {
        StructType schema = new StructType(((StructType) this.schema).fields());
        NativeClauseContext result = new NativeClauseContext(FLWOR_CLAUSES.RETURN, schema, this.context);
        result.conditionalColumns = new ArrayList<>();
        result.parent = this;
        result.tempView = this.tempView;
        result.positionalVariableNames = new ArrayList<>();
        result.variables = new HashMap<>();
        return result;
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

    public void addConditionalColumn(String name) {
        this.conditionalColumns.add(name);
    }

    public List<String> getConditionalColumns() {
        List<String> result = new ArrayList<>(this.conditionalColumns);
        if (this.parent != null) {
            result.addAll(this.parent.getConditionalColumns());
        }
        return result;
    }

    private int getAndIncrementMonotonicallyIncreasingId() {
        if (this.parent != null) {
            return this.parent.getAndIncrementMonotonicallyIncreasingId();
        }
        return this.monotonicallyIncreasingId++;
    }

    public Name addVariable() {
        Name variable = Name.createVariableInNoNamespace(
            SparkSessionManager.sparkSqlVariableName + "-" + this.getAndIncrementMonotonicallyIncreasingId()
        );
        this.variables.put(variable, variable);
        return variable;
    }

    public Name addVariable(Name name) {
        Name variable = Name.createVariableInNoNamespace(
            SparkSessionManager.sparkSqlVariableName + "-" + this.getAndIncrementMonotonicallyIncreasingId()
        );
        this.variables.put(name, variable);
        return variable;
    }

    public Name getVariable(Name name) {
        if (this.variables.containsKey(name)) {
            return this.variables.get(name);
        }
        if (this.parent != null) {
            return this.parent.getVariable(name);
        }
        return name;
    }

    public void setExplodedView(boolean isExplodedView) {
        this.isExplodedView = isExplodedView;
    }

    public boolean isExplodedView() {
        return this.isExplodedView;
    }

    public void addPositionalVariableName(Name name) {
        this.positionalVariableNames.add(name);
    }

    public Name getPositionalVariableName() {
        if (this.positionalVariableNames.isEmpty()) {
            return null;
        }
        return this.positionalVariableNames.get(this.positionalVariableNames.size() - 1);
    }

    public Map<String, Boolean> getSortingColumns() {
        return this.sortingColumns;
    }

    public void addSortingColumn(String name, boolean descending) {
        this.sortingColumns.put(name, descending);
    }

    public void setRowId(String rowIdField) {
        this.rowIdField = rowIdField;
    }

    public String getRowIdField() {
        return this.rowIdField;
    }

    public void clearConditionalColumns() {
        this.conditionalColumns.clear();
    }

    public void clearSortingColumns() {
        this.sortingColumns.clear();
    }
}
