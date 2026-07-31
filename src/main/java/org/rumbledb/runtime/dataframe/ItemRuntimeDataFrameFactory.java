/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.dataframe;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.ItemType;

/**
 * Encodes item RDDs as {@link HomogeneousItemDataFrame}s.
 */
public final class ItemRuntimeDataFrameFactory implements RuntimeDataFrameFactory<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final ItemRuntimeDataFrameFactory INSTANCE = new ItemRuntimeDataFrameFactory();

    private ItemRuntimeDataFrameFactory() {
    }

    @Override
    public RuntimeDataFrame<Item> fromList(
            List<Item> items,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        ItemType itemType = staticContext.getStaticType().getItemType();
        if (!itemType.isCompatibleWithDataFrames(staticContext.getConfiguration())) {
            itemType = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items,
                staticContext.getMetadata(),
                TypeInferrenceUtils.TypeMergeMode.LAX
            );
            if (staticContext.getConfiguration().printInferredTypes()) {
                System.err.println("Inferred DataFrame type:\n" + itemType);
            }
        }
        return ValidateTypeIterator.convertLocalItemsToDataFrame(items, itemType, context, true, staticContext);
    }

    @Override
    public RuntimeDataFrame<Item> fromRDD(
            JavaRDD<Item> rdd,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        ItemType itemType = staticContext.getStaticType().getItemType();
        if (!itemType.isCompatibleWithDataFrames(staticContext.getConfiguration())) {
            itemType = TypeInferrenceUtils.inferItemTypeOfRDDItems(
                rdd,
                staticContext.getMetadata(),
                TypeInferrenceUtils.TypeMergeMode.LAX
            );
        }
        return ValidateTypeIterator.convertRDDToValidDataFrame(rdd, itemType, context, true, staticContext);
    }
}
