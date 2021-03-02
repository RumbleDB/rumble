package org.rumbledb.types;

public class ItemTypeFactory {
    public static FunctionItemType createFunctionItemType(FunctionSignature signature) {
        return new FunctionItemType(signature);
    }
}
