package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import java.net.URI;
import java.util.List;

public class CollectionFunctionIterator extends DataFrameRuntimeIterator {

	private static final long serialVersionUID = 1L;

	public CollectionFunctionIterator(
            List<RuntimeIterator> children,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(children, executionMode, iteratorMetadata);
    }

    // TODO: implement collection function

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        String url = stringItem.getStringValue();
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());
        if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        //DataFrameReader dfr = SparkSessionManager.getInstance().getOrCreateSession().read();
        return JSoundDataFrame.emptyDataFrame();
    }

}
