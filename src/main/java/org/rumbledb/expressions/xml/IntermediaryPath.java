package org.rumbledb.expressions.xml;

public class IntermediaryPath {
    private Dash preStepExprDash;
    private StepExpr stepExpr;

    public IntermediaryPath(Dash preStepExprDash, StepExpr stepExpr) {
        this.preStepExprDash = preStepExprDash;
        this.stepExpr = stepExpr;
    }


    public StepExpr getStepExpr() {
        return stepExpr;
    }

    public Dash getPreStepExprDash() {
        return preStepExprDash;
    }
}
