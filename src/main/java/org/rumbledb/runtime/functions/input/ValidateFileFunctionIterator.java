package org.rumbledb.runtime.functions.input;

import org.api.executors.JSoundExecutor;
import org.api.executors.JSoundSchema;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.io.IOException;
import java.util.List;

public class ValidateFileFunctionIterator extends LocalFunctionCallIterator {

    public ValidateFileFunctionIterator(
            List<RuntimeIterator> children,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(children, executionMode, metadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item schemaPath = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            Item filePath = this.children.get(1)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            Item targetType = this.children.get(2)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            Item compact = this.children.get(3).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

            try {
                JSoundSchema schema = JSoundExecutor.loadSchemaFromPath(
                    schemaPath.getStringValue(),
                    targetType.getStringValue(),
                    compact.getBooleanValue()
                );
                return ItemFactory.getInstance()
                    .createBooleanItem(schema.validateJSONLinesFromPath(filePath.getStringValue()));
            } catch (IOException e) {
                throw new SparksoniqRuntimeException(e.getMessage());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " matches function",
                    getMetadata()
            );
    }
}
