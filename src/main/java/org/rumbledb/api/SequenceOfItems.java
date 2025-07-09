package org.rumbledb.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.exceptions.ExceptionMetadata;

import org.rumbledb.runtime.update.PendingUpdateList;
import sparksoniq.spark.SparkSessionManager;

/**
 * A sequence of items is the value returned by any expression in JSONiq, which is a set-based language.
 *
 * In particular, it is what RumbleDB returns after evaluating a query.
 *
 * Sequences of items are flat and do not nest. A sequence may be empty. A sequence may consist of only one item: it is
 * then canonically identified
 * with that item. Or a sequence may contain more than one item.
 *
 * With an instance of this class, it is possible to iterate on a sequence of items, getting each item in turn.
 * It is also possible to collect the items in a list.
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

    /**
     * The constructor is not meant to be used directly. Sequences of items are obtained through a Rumble object and a
     * query.
     * 
     * @param iterator The top-level iterator of the query.
     * @param dynamicContext An initialized dynamic context.
     * @param configuration A RumbleDB configuration.
     */
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
        if (this.availableAsPUL()) {
            return;
        }
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
        if (this.availableAsPUL()) {
            return;
        }
        if (this.isOpen) {
            this.iterator.close();
        }
        this.isOpen = false;
    }

    /**
     * Checks whether there are more items to get from the iterator.
     *
     * @return true if there are more items, false otherwise.
     */
    public boolean hasNext() {
        if (this.availableAsPUL()) {
            return false;
        }
        return this.iterator.hasNext();
    }

    /**
     * Returns the current item and moves on to the next one. The number of items the iterator can returned is capped by
     * Spark's settings (collect-item-limit).
     *
     * @return the next item.
     */
    public Item next() {
        if (this.availableAsPUL()) {
            return ItemFactory.getInstance().createNullItem();
        }
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
     * Returns whether the iterator is updating
     *
     * @return true if updating; otherwise false.
     */
    public boolean availableAsPUL() {
        return this.iterator.isUpdating();
    }

    /**
     * Returns the sequence of items as an RDD of Items rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     *
     * @return an RDD of Items.
     */
    public JavaRDD<Item> getAsRDD() {
        if (this.availableAsPUL()) {
            return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
        }
        if (this.isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        return this.iterator.getRDD(this.dynamicContext);
    }

    /**
     * Returns the sequence of items as a data frame rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     *
     * @return a data frame.
     */
    public Dataset<Row> getAsDataFrame() {
        if (this.availableAsPUL()) {
            return SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame();
        }
        if (this.isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        return this.iterator.getDataFrame(this.dynamicContext).getDataFrame();
    }

    /**
     * Applies the PUL available when the iterator is updating.
     */
    public void applyPUL() {
        PendingUpdateList pul = this.iterator.getPendingUpdateList(this.dynamicContext);
        pul.applyUpdates(this.iterator.getMetadata());
    }

    /**
     * Outputs the results as a list. Throws an exception if there are more items than the allowed materialization
     * limit.
     * 
     * @return The list of all items in the sequence.
     */
    public List<Item> getList(boolean cap) {
        List<Item> result = new ArrayList<Item>();
        long num = populateList(result);
        if (!cap && num != -1) {
            throw new CannotMaterializeException(
                    "Cannot materialize a sequence of "
                        + num
                        + " items because the limit is set to "
                        + SparkSessionManager.COLLECT_ITEM_LIMIT
                        + ". This value can be configured with the --materialization-cap parameter at startup",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        return result;
    }

    /**
     * Outputs the results as a list. If there are more items than the allowed materialization limit,
     * then the list is incomplete and no error is thrown.
     * 
     * @return The list of items in the sequence, possibly capped.
     */
    public List<Item> getCappedList() {
        List<Item> result = new ArrayList<Item>();
        populateList(result);
        return result;
    }

    /*
     * Populates a existing list with the output items.
     *
     * @return -1 if the full sequence could be materialized. If there were more items beyond the materialization cap,
     * then the sequence length. If the sequence length is not known, then Long.MAX_VALUE.
     */
    public long populateList(List<Item> resultList) {
        resultList.clear();
        if (this.availableAsPUL()) {
            return -1;
        }
        if (this.iterator.isRDDOrDataFrame()) {
            JavaRDD<Item> rdd = this.iterator.getRDD(this.dynamicContext);
            return SparkSessionManager.collectRDDwithLimitWarningOnly(rdd, resultList);
        }
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
}
