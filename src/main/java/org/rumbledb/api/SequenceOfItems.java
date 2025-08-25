package org.rumbledb.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
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
     * Returns available output modes, order by decreasing efficiency.
     * 
     * "DataFrame" means getAsDataFrame() can be called.
     * "RDD" means getAsRDD() can be called.
     * "PUL" means applyPUL() can be called.
     * "Local" means getAsList() (if the count does not exceed the materialization cap) or getFirstItemsAsList() can be
     * called, or the streaming methods (open/hasNext/next/close).
     *
     * @return a list of output modes, among "DataFrame", "RDD", "PUL", and "Local".
     */
    public List<String> availableOutputs() {
        if (this.iterator.isDataFrame()) {
            return Arrays.asList("DataFrame", "RDD", "Local");
        } else if (this.iterator.canProduceDataFrame()) {
            return Arrays.asList("RDD", "Local", "DataFrame");
        } else if (this.iterator.isRDD()) {
            return Arrays.asList("RDD", "Local");
        } else if (this.iterator.isUpdating()) {
            return Arrays.asList("PUL");
        } else {
            return Arrays.asList("Local");
        }
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
     * Returns the number of items in the sequence.
     */
    public long count() {
        return getAsRDD().count();
    }

    /**
     * Returns the sequence of strings as an RDD of Items rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     *
     * @return an RDD of strings.
     */
    public JavaRDD<byte[]> getAsPickledStringRDD() {
        if (this.availableAsPUL()) {
            return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
        }
        if (this.isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        return this.iterator.getRDD(this.dynamicContext)
            .map(
                item -> ("\u0080\u0005\u0095"
                    + longToLittleEndianString(item.serializeAsJSON().length() + 7)
                    + "]\u0094\u008c"
                    + Character.toString((char) item.serializeAsJSON().length())
                    + item.serializeAsJSON()
                    + "\u0094a.").getBytes("ISO-8859-1")
            );
    }

    public static String longToLittleEndianString(long value) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (value >> (8 * i));
        }
        // Convert to a hex string representation
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Character.toString((char) b));
        }
        return sb.toString();
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
        Dataset<Row> res = this.iterator.getOrCreateDataFrame(this.dynamicContext).getDataFrame();
        if (res.columns().length == 1 && res.columns()[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            res = res.withColumnRenamed(SparkSessionManager.atomicJSONiqItemColumnName, "value");
        }
        return res;
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
    public List<Item> getAsList() {
        List<Item> result = new ArrayList<Item>();
        long num = populateList(result, this.configuration.getResultSizeCap());
        if (num != -1) {
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
    public List<Item> getFirstItemsAsList(int maxNumberOfItems) {
        List<Item> resultList = new ArrayList<Item>();
        if (this.availableAsPUL()) {
            return resultList;
        }
        if (this.iterator.isRDDOrDataFrame()) {
            JavaRDD<Item> rdd = this.iterator.getRDD(this.dynamicContext);
            List<Item> result = rdd.take(maxNumberOfItems);
            resultList.addAll(result);
            return resultList;
        }
        this.iterator.open(this.dynamicContext);
        Item result = null;
        if (this.iterator.hasNext()) {
            result = this.iterator.next();
        }
        if (result == null) {
            this.iterator.close();
            return resultList;
        }
        Item singleOutput = result;
        if (!this.iterator.hasNext()) {
            resultList.add(singleOutput);
            this.iterator.close();
            return resultList;
        } else {
            int itemCount = 1;
            resultList.add(result);
            while (
                this.iterator.hasNext()
                    &&
                    ((itemCount < maxNumberOfItems && maxNumberOfItems > 0)
                        ||
                        maxNumberOfItems == 0)
            ) {
                resultList.add(this.iterator.next());
                itemCount++;
            }
            this.iterator.close();
            return resultList;
        }
    }

    /*
     * Populates a existing list with the output items.
     *
     * @return -1 if the full sequence could be materialized. If there were more items beyond the materialization cap,
     * then the sequence length. If the sequence length is not known, then Long.MAX_VALUE.
     */
    public long populateList(List<Item> resultList, int maxNumberOfItems) {
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
            this.iterator.close();
            return -1;
        }
        Item singleOutput = result;
        if (!this.iterator.hasNext()) {
            resultList.add(singleOutput);
            this.iterator.close();
            return -1;
        } else {
            int itemCount = 1;
            resultList.add(result);
            while (
                this.iterator.hasNext()
                    &&
                    ((itemCount < maxNumberOfItems && maxNumberOfItems > 0)
                        ||
                        maxNumberOfItems == 0)
            ) {
                resultList.add(this.iterator.next());
                itemCount++;
            }
            if (this.iterator.hasNext() && itemCount == maxNumberOfItems) {
                this.iterator.close();
                return Long.MAX_VALUE;
            }
            this.iterator.close();
            return -1;
        }
    }

    /**
     * Returns a SequenceWriter to save the sequence in various formats.
     */
    public SequenceWriter write() {
        return new SequenceWriter(this, this.configuration);
    }

}
