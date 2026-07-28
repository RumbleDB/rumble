/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.ItemType;

/**
 * DataFrame conversions specific to item runtime plans.
 */
public final class ItemRuntimePlanConversions {

    private ItemRuntimePlanConversions() {
    }

    public static JSoundDataFrame rddToDataFrame(
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

    public static JSoundDataFrame localToDataFrame(RuntimePlan<Item> plan, DynamicContext context) {
        RuntimeStaticContext staticContext = plan.getRuntimeStaticContext();
        List<Item> items = materializeLocal(plan, context);
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

        return ValidateTypeIterator.convertLocalItemsToDataFrame(
            items,
            itemType,
            context,
            true,
            staticContext
        );
    }

    private static List<Item> materializeLocal(RuntimePlan<Item> plan, DynamicContext context) {
        List<Item> items = new ArrayList<>();
        try (LocalCursor<Item> cursor = plan.createLocalCursor(context)) {
            while (cursor.hasNext()) {
                items.add(cursor.next());
            }
        }
        return items;
    }
}
