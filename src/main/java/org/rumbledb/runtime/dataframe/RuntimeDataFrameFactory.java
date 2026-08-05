/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.dataframe;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;

/**
 * Creates a typed runtime DataFrame from the logical values stored in an RDD.
 *
 * @param <T> the logical runtime value represented by each DataFrame row
 */
public interface RuntimeDataFrameFactory<T> extends Serializable {

    RuntimeDataFrame<T> fromList(
            List<T> values, DynamicContext context, RuntimeStaticContext staticContext);

    RuntimeDataFrame<T> fromRDD(
            JavaRDD<T> rdd, DynamicContext context, RuntimeStaticContext staticContext);
}
