package org.rumbledb.api;

import java.util.List;

import org.apache.spark.SparkRuntimeException;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

/**
 * A sequence of items is the value returned by any expression in JSONiq, which is a set-based language.
 *
 * In particular, it is what Rumble returns after evaluating a query.
 *
 * Sequences of items are flat and do not nest. A sequence may be empty. A sequence may consist of only one item: it is
 * then canonically identified
 * with that item. Or a sequence may contain more than one item.
 *
 * With an instance of this class, it is possible to iterate on a sequence of items, getting each item in turn.
 *
 * The number of items returned by the iterator API is capped by the collect-item-limit parameter of Spark to avoid an
 * overflow.
 * For big sequences, it is preferable to obtain it as an RDD, also via this class, if the sequence is too big to be
 * collected locally.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class SequenceOfItems {

    private RuntimeIterator iterator;
    private DynamicContext dynamicContext;
    private RumbleRuntimeConfiguration configuration;
    private boolean isOpen;

    public SequenceOfItems(
            RuntimeIterator iterator,
            DynamicContext dynamicContext,
            RumbleRuntimeConfiguration configuration
    ) {
        this.iterator = iterator;
        this.isOpen = false;
        this.dynamicContext = dynamicContext;
        this.configuration = configuration;
    }

    /**
     * Opens the iterator.
     */
    public void open() {
        this.iterator.open(this.dynamicContext);
        this.isOpen = true;
    }

    /**
     * Checks whether the iterator is open.
     *
     * @return true if it is open, false if it is closed.
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Closes the iterator.
     */
    public void close() {
        this.iterator.close();
        this.isOpen = false;
    }

    /**
     * Checks whether there are more items.
     *
     * @return true if there are more items, false otherwise.
     */
    public boolean hasNext() {
        try {
            return this.iterator.hasNext();
        } catch (NumberFormatException e) {
            RumbleException ex = new CastException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
            ex.initCause(e);
            throw ex;
        } catch (SparkRuntimeException e) {
            if (e.getMessage().contains("CAST_INVALID_INPUT")) {
                RumbleException ex = new CastException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
                ex.initCause(e);
                throw ex;
            } else {
                throw e;
            }
        } catch (UnsupportedOperationException e) {
            RumbleException ex = new CastException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Returns the current item and moves on to the next one. The number of items the iterator can returned is capped by
     * Spark's settings (collect-item-limit).
     *
     * @return the next item.
     */
    public Item next() {
        return this.iterator.next();
    }

    /**
     * Checks whether the iterator is available as an RDD of Items for further processing without having to collect.
     *
     * @return true if it is available as an RDD of Items.
     */
    public boolean availableAsRDD() {
        return this.iterator.isRDDOrDataFrame();
    }

    /**
     * Checks whether the iterator is available as a data frame for further processing without having to collect.
     *
     * @return true if it is available as a data frame.
     */
    public boolean availableAsDataFrame() {
        return this.iterator.isDataFrame();
    }

    /**
     * Returns the sequence of items as an RDD of Items rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     *
     * @return an RDD of Items.
     */
    public JavaRDD<Item> getAsRDD() {
        if (this.isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        try {
            return this.iterator.getRDD(this.dynamicContext);
        } catch (NumberFormatException e) {
            RumbleException ex = new UnexpectedTypeException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
            ex.initCause(e);
            throw ex;
        } catch (SparkRuntimeException e) {
            if (e.getMessage().contains("CAST_INVALID_INPUT")) {
                RumbleException ex = new CastException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
                ex.initCause(e);
                throw ex;
            } else {
                throw e;
            }
        } catch (UnsupportedOperationException e) {
            RumbleException ex = new UnexpectedTypeException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Returns the sequence of items as a data frame rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     *
     * @return a data frame.
     */
    public Dataset<Row> getAsDataFrame() {
        if (this.isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        return this.iterator.getDataFrame(this.dynamicContext).getDataFrame();
    }

    /*
     * Populates a list of items with the output.
     *
     * @return -1 if successful. Returns Long.MAX_VALUE if there were more items beyond the materialization cap.
     */
    public long populateList(List<Item> resultList) {
        resultList.clear();
        this.iterator.open(this.dynamicContext);
        Item result = null;
        if (this.iterator.hasNext()) {
            result = this.iterator.next();
        }
        if (result == null) {
            return -1;
        }
        Item singleOutput = result;
        if (!this.iterator.hasNext()) {
            resultList.add(singleOutput);
            return -1;
        } else {
            int itemCount = 1;
            resultList.add(result);
            while (
                this.iterator.hasNext()
                    &&
                    ((itemCount < this.configuration.getResultSizeCap() && this.configuration.getResultSizeCap() > 0)
                        ||
                        this.configuration.getResultSizeCap() == 0)
            ) {
                resultList.add(this.iterator.next());
                itemCount++;
            }
            if (this.iterator.hasNext() && itemCount == this.configuration.getResultSizeCap()) {
                return Long.MAX_VALUE;
            }
            return -1;
        }
    }

    public long populateListWithWarningOnlyIfCapReached(List<Item> resultList) {
        if (this.availableAsRDD()) {
            try {
                JavaRDD<Item> rdd = this.iterator.getRDD(this.dynamicContext);
                return SparkSessionManager.collectRDDwithLimitWarningOnly(rdd, resultList);
            } catch (NumberFormatException e) {
                RumbleException ex = new UnexpectedTypeException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
                ex.initCause(e);
                throw ex;
            } catch (SparkRuntimeException e) {
                if (e.getMessage().contains("CAST_INVALID_INPUT")) {
                    RumbleException ex = new CastException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
                    ex.initCause(e);
                    throw ex;
                } else {
                    throw e;
                }
            } catch (UnsupportedOperationException e) {
                RumbleException ex = new UnexpectedTypeException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
                ex.initCause(e);
                throw ex;
            }
        } else {
            return populateList(resultList);
        }
    }


}
