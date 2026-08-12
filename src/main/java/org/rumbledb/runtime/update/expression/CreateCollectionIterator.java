package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.net.URI;
import java.util.Arrays;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class CreateCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan targetIterator;
    private final ItemRuntimePlan contentIterator;
    private final Mode mode;

    public CreateCollectionIterator(
            ItemRuntimePlan targetIterator,
            ItemRuntimePlan contentIterator,
            Mode mode,
            RuntimeStaticContext staticContext) {
        super(
                Arrays.asList(targetIterator, contentIterator),
                staticContext.toBuilder().isUpdating(true).build());
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.mode = mode;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but more than one item was provided.",
                    this.getRuntimeStaticContext().getMetadata());
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but no item was provided.",
                    this.getRuntimeStaticContext().getMetadata());
        }

        if (!targetItem.isString()) {
            throw new InvalidUpdateTargetException(
                    "Expecting collection name as a String, but it was: "
                            + targetItem.getDynamicType().getIdentifierString(),
                    this.getRuntimeStaticContext().getMetadata());
        }

        String logicalPath = targetItem.getStringValue();
        Mode mode = this.mode;
        // If it is a delta-file() call we need to resolve the path to an absolute path.
        if (mode == Mode.DELTA) {
            URI uri =
                    FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), logicalPath, getMetadata());
            logicalPath = FileSystemUtil.convertURIToStringForSpark(uri);
        }

        Collection collection = new Collection(mode, logicalPath);
        Dataset<Row> contentDF = null;
        try {
            contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            e.setMetadata(getMetadata());
            throw e;
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createCreateCollectionPrimitive(
                collection, contentDF, this.getRuntimeStaticContext().getMetadata());

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
