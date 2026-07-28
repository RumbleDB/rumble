/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.dataframe;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * A Spark DataFrame whose rows represent runtime values of type {@code T}.
 *
 * <p>
 * Spark stores the physical representation as {@link Row}; implementations define how rows are mapped back to the
 * logical runtime type.
 * </p>
 *
 * @param <T> the logical runtime value represented by each row
 */
public interface RuntimeDataFrame<T> extends Serializable {

    /**
     * Returns the underlying physical Spark DataFrame.
     */
    Dataset<Row> getDataFrame();

    /**
     * Converts this DataFrame to its logical runtime representation.
     *
     * @param metadata query metadata used if a row cannot be decoded
     * @return an RDD of logical runtime values
     */
    JavaRDD<T> toRDD(ExceptionMetadata metadata);
}
