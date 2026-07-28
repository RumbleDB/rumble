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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import sparksoniq.spark.SparkSessionManager;

/**
 * Stateless conversions between the local, RDD, and DataFrame representations used by runtime plans.
 */
public final class RuntimePlanConversions {

    private RuntimePlanConversions() {
    }

    public static <T> LocalCursor<T> rddToLocalCursor(JavaRDD<T> rdd, RuntimeStaticContext staticContext) {
        return new IteratorLocalCursor<>(
                () -> collectRDDWithLimit(
                    rdd,
                    staticContext.getConfiguration(),
                    staticContext.getMetadata()
                ).iterator(),
                staticContext.getMetadata()
        );
    }

    public static <T> List<T> collectRDDWithLimit(
            JavaRDD<T> rdd,
            RumbleRuntimeConfiguration configuration,
            ExceptionMetadata metadata
    ) {
        if (configuration.getMaterializationCap() <= 0) {
            return rdd.collect();
        }

        List<T> result = rdd.take(configuration.getMaterializationCap() + 1);
        if (result.size() <= configuration.getMaterializationCap()) {
            return result;
        }

        long count = rdd.count();
        throw new CannotMaterializeException(
                "Cannot materialize a sequence of "
                    + count
                    + " items because the limit is set to "
                    + configuration.getMaterializationCap()
                    + ". This value can be configured with the --materialization-cap parameter at startup",
                metadata
        );
    }

    public static <T> JavaRDD<T> localToRDD(RuntimePlan<T> plan, DynamicContext context) {
        return SparkSessionManager.getInstance()
            .getJavaSparkContext()
            .parallelize(materializeLocal(plan, context));
    }

    public static <T> List<T> materializeLocal(RuntimePlan<T> plan, DynamicContext context) {
        List<T> items = new ArrayList<>();
        try (LocalCursor<T> cursor = plan.createLocalCursor(context)) {
            while (cursor.hasNext()) {
                items.add(cursor.next());
            }
        }
        return items;
    }

}
