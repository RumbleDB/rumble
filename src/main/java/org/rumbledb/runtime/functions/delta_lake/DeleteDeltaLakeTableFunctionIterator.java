package org.rumbledb.runtime.functions.delta_lake;

import org.apache.commons.io.FileUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DeleteDeltaLakeTableFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    public DeleteDeltaLakeTableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        urlIterator.open(context);
        String url = urlIterator.next().getStringValue();
        urlIterator.close();
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());
        if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }

//        URI tableURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
//                this.currentAnnotation.getDeltaTablePath(),
//                DeltaLakeConfigurationCatalogue.defaultDeltaLakeConfiguration,
//                ExceptionMetadata.EMPTY_METADATA
//        );

        try {
            File oldTable = new File(uri.getPath());
            FileUtils.deleteDirectory(oldTable);
            return new BooleanItem(true);
        } catch (IOException e) {
            e.printStackTrace();
            return new BooleanItem(false);
        }
    }
}
