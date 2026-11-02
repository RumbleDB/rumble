package org.rumbledb.runtime;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.scripting.mutation.AssignStatementIterator;
import java.util.List;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.runtime.functions.io.UnparsedTextReader;
import org.rumbledb.runtime.scripting.mutation.ApplyStatementIterator;

public class UnparsedTextCacheTest {
    @TempDir Path directory;

    @Test
    public void textIsStableUntilScriptingSideEffect() throws Exception {
        Path file = this.directory.resolve("text.txt");
        Files.writeString(file, "before");
        DynamicContext context = new DynamicContext(RumbleConfiguration.defaultConfiguration());
        ResourceResolver resolver = context.getResourceResolver();
        assertEquals("before", read(file, resolver));
        Files.writeString(file, "after");
        assertEquals("before", read(file, new DynamicContext(context).getResourceResolver()));
        assertEquals("after", read(file, resolver.forExecution()));
        RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                .configuration(RumbleConfiguration.defaultConfiguration())
                .executionMode(ExecutionMode.LOCAL)
                .metadata(ExceptionMetadata.EMPTY_METADATA).build();
        new ApplyStatementIterator(new ConstantRuntimeIterator(
                ItemFactory.getInstance().createIntItem(1), staticContext), staticContext)
                .evaluateAtMostOne(context);
        assertEquals("after", read(file, resolver));
        Files.writeString(file, "assigned");
        Name variable = new Name("", "", "v");
        context.getVariableValues().addVariableValue(variable,
                List.of(ItemFactory.getInstance().createIntItem(0)));
        new AssignStatementIterator(new ConstantRuntimeIterator(
                ItemFactory.getInstance().createIntItem(1), staticContext), variable, staticContext)
                .evaluateAtMostOne(context);
        assertEquals("assigned", read(file, resolver));
    }

    private String read(Path file, ResourceResolver resolver) {
        return UnparsedTextReader.read(file.toUri(), file.toUri().toString(), null, "1.0",
                resolver, ExceptionMetadata.EMPTY_METADATA);
    }
}
