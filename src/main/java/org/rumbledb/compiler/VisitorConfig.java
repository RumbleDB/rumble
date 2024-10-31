package org.rumbledb.compiler;

public class VisitorConfig {

    // flag to suppress errors when a function declaration collides with an existing function
    private boolean suppressErrorsForFunctionSignatureCollision;
    // flag to suppress errors when an unrecognized function is called
    private boolean suppressErrorsForCallingMissingFunctions;
    // flag to suppress errors when an unset execution mode value is fetched from a node
    private boolean suppressErrorsForAccessingUnsetExecutionModes;
    // flag to set unset expressions (actually, variable references) to local
    private boolean setUnsetExecutionModeOfVariableReferenceExpressionsToLocal;

    public static class Builder {
        private boolean suppressErrorsForFunctionSignatureCollision = false;
        private boolean suppressErrorsForCallingMissingFunctions = false;
        private boolean suppressErrorsForAccessingUnsetExecutionModes = false;
        private boolean setUnsetExecutionModeOfVariableReferenceExpressionsToLocal = false;

        Builder withsuppressErrorsForFunctionSignatureCollision(boolean value) {
            this.suppressErrorsForFunctionSignatureCollision = value;
            return this;
        }

        Builder withSuppressErrorsForCallingMissingFunctions(boolean value) {
            this.suppressErrorsForCallingMissingFunctions = value;
            return this;
        }

        Builder withSuppressErrorsForAccessingUnsetExecutionModes(boolean value) {
            this.suppressErrorsForAccessingUnsetExecutionModes = value;
            return this;
        }

        Builder withSetUnsetExecutionModeOfVariableReferenceExpressionsToLocal(boolean value) {
            this.setUnsetExecutionModeOfVariableReferenceExpressionsToLocal = value;
            return this;
        }

        VisitorConfig build() {
            return new VisitorConfig(
                    this.suppressErrorsForFunctionSignatureCollision,
                    this.suppressErrorsForCallingMissingFunctions,
                    this.suppressErrorsForAccessingUnsetExecutionModes,
                    this.setUnsetExecutionModeOfVariableReferenceExpressionsToLocal
            );
        }
    }

    private VisitorConfig(
            boolean suppressErrorsForFunctionSignatureCollision,
            boolean suppressErrorsForCallingMissingFunctions,
            boolean suppressErrorsForAccessingUnsetExecutionModes,
            boolean setUnsetToLocal
    ) {
        this.suppressErrorsForFunctionSignatureCollision = suppressErrorsForFunctionSignatureCollision;
        this.suppressErrorsForCallingMissingFunctions = suppressErrorsForCallingMissingFunctions;
        this.suppressErrorsForAccessingUnsetExecutionModes = suppressErrorsForAccessingUnsetExecutionModes;
        this.setUnsetExecutionModeOfVariableReferenceExpressionsToLocal = setUnsetToLocal;
    }

    public boolean suppressErrorsForFunctionSignatureCollision() {
        return this.suppressErrorsForFunctionSignatureCollision;
    }

    public boolean suppressErrorsForCallingMissingFunctions() {
        return this.suppressErrorsForCallingMissingFunctions;
    }

    public boolean suppressErrorsForAccessingUnsetExecutionModes() {
        return this.suppressErrorsForAccessingUnsetExecutionModes;
    }

    public boolean setUnsetToLocal() {
        return this.setUnsetExecutionModeOfVariableReferenceExpressionsToLocal;
    }


    /**
     * The initial pass should collect all function declaration information to support hoisting.
     * User defined functions'(UDF) signatures should not collide with each other or built-in functions.
     * As Some functions may not be known yet, missing functions should not raise errors.
     * Since unknown functions have unset execution modes, errors should not be raised for accessing these.
     */
    static final VisitorConfig staticContextVisitorInitialPassConfig = new VisitorConfig.Builder()
        .withsuppressErrorsForFunctionSignatureCollision(false)
        .withSuppressErrorsForCallingMissingFunctions(true)
        .withSuppressErrorsForAccessingUnsetExecutionModes(true)
        .withSetUnsetExecutionModeOfVariableReferenceExpressionsToLocal(false)
        .build();

    /**
     * Intermediate passes should update the execution modes of expressions as more UDFs can be resolved.
     * As all UDFs should be known at this stage, missing functions should raise errors
     * As UDFs may still have unresolved execution modes, errors should not be raised for accessing these.
     */
    static final VisitorConfig staticContextVisitorIntermediatePassConfig = new VisitorConfig.Builder()
        .withsuppressErrorsForFunctionSignatureCollision(true)
        .withSuppressErrorsForCallingMissingFunctions(false)
        .withSuppressErrorsForAccessingUnsetExecutionModes(true)
        .withSetUnsetExecutionModeOfVariableReferenceExpressionsToLocal(false)
        .build();

    /**
     * All expression execution mode and UDF information should be available in the final pass
     */
    static final VisitorConfig staticContextVisitorFinalPassConfig = new VisitorConfig.Builder()
        .withsuppressErrorsForFunctionSignatureCollision(true)
        .withSuppressErrorsForCallingMissingFunctions(false)
        .withSuppressErrorsForAccessingUnsetExecutionModes(false)
        .withSetUnsetExecutionModeOfVariableReferenceExpressionsToLocal(true)
        .build();

    static final VisitorConfig runtimeIteratorVisitorConfig = new VisitorConfig.Builder()
        .withsuppressErrorsForFunctionSignatureCollision(false)
        .withSuppressErrorsForCallingMissingFunctions(false)
        .withSuppressErrorsForAccessingUnsetExecutionModes(false)
        .withSetUnsetExecutionModeOfVariableReferenceExpressionsToLocal(false)
        .build();

}
