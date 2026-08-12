package org.rumbledb.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.SerializerUtils;
import org.rumbledb.serialization.Serializers;
import org.rumbledb.spark.SparkSessionManager;

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

    private final ItemRuntimePlan plan;
    private final DynamicContext dynamicContext;
    private final RumbleConfiguration configuration;
    private Cursor<Item> cursor;

    /**
     * Checks whether the iterator is open.
     */
    @Getter
    private boolean isOpen;

    private List<Item> cachedItems;

    /**
     * The constructor is not meant to be used directly. Sequences of items are obtained through a Rumble object and a
     * query.
     *
     * @param plan The top-level runtime plan of the query.
     * @param dynamicContext An initialized dynamic context.
     * @param configuration A RumbleDB configuration.
     */
    public SequenceOfItems(ItemRuntimePlan plan, DynamicContext dynamicContext, RumbleConfiguration configuration) {
        this.plan = plan;
        this.isOpen = false;
        this.dynamicContext = dynamicContext;
        this.configuration = configuration;
        this.cachedItems = null;
        this.cursor = null;
    }

    /**
     * Opens the iterator.
     */
    public void open() {
        if (this.availableAsPUL()) {
            return;
        }
        this.cursor = this.plan.getCursor(this.dynamicContext);
        this.isOpen = true;
    }

    /**
     * Closes the iterator.
     */
    public void close() {
        if (this.availableAsPUL()) {
            return;
        }
        if (this.isOpen) {
            this.cursor.close();
            this.cursor = null;
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
        return this.cursor.hasNext();
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
        return this.cursor.next();
    }

    /**
     * Checks whether the iterator is available as an RDD of Items for further processing without having to collect.
     *
     * @return true if it is available as an RDD of Items.
     */
    public boolean availableAsRDD() {
        return this.executionMode().isRDDOrDataFrame();
    }

    /**
     * Checks whether the iterator is available as a data frame for further processing without having to collect.
     *
     * @return true if it is available as a data frame.
     */
    public boolean availableAsDataFrame() {
        return this.executionMode().isDataFrame();
    }

    /**
     * Returns whether the iterator is updating
     *
     * @return true if updating; otherwise false.
     */
    public boolean availableAsPUL() {
        return this.plan.getRuntimeStaticContext().isUpdating();
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
        if (this.executionMode().isDataFrame()) {
            return Arrays.asList("DataFrame", "RDD", "Local");
        } else if (this.plan
                .getRuntimeStaticContext()
                .getStaticType()
                .getItemType()
                .isCompatibleWithDataFrames(this.configuration)) {
            return Arrays.asList("RDD", "Local", "DataFrame");
        } else if (this.executionMode().isRDD()) {
            return Arrays.asList("RDD", "Local");
        } else if (this.availableAsPUL()) {
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
        return this.plan.getRDD(this.dynamicContext);
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
        return this.plan.getRDD(this.dynamicContext).map(item -> ("\u0080\u0005\u0095"
                        + longToLittleEndianString(item.serializeAsJSON().length() + 7)
                        + "]\u0094\u008c"
                        + Character.toString((char) item.serializeAsJSON().length())
                        + item.serializeAsJSON()
                        + "\u0094a.")
                .getBytes("ISO-8859-1"));
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
        Dataset<Row> res = this.plan.getDataFrame(this.dynamicContext).getDataFrame();
        if (res.columns().length == 1 && res.columns()[0].equals(SparkSessionManager.nonObjectJSONiqItemColumnName)) {
            res = res.withColumnRenamed(SparkSessionManager.nonObjectJSONiqItemColumnName, "__value");
        }
        return res;
    }

    /**
     * Returns the runtime static context associated with this sequence.
     *
     * This context provides access to the default serialization parameters
     * that should be used when serializing the results of this sequence.
     */
    public RuntimeStaticContext getRuntimeStaticContext() {
        return this.plan.getRuntimeStaticContext();
    }

    /**
     * Applies the PUL available when the iterator is updating.
     */
    public void applyPUL() {
        PendingUpdateList pul = UpdatingRuntimePlan.get(this.plan, this.dynamicContext);
        pul.applyUpdates(this.plan.getRuntimeStaticContext().getMetadata());
    }

    /**
     * Outputs the results as a list. Throws an exception if there are more items than the allowed materialization
     * limit. This method is governed only by the materialization cap; the result-size cap is not considered here.
     *
     * @return The list of all items in the sequence.
     */
    public List<Item> getAsList() {
        if (this.cachedItems != null) {
            return new ArrayList<Item>(this.cachedItems);
        }
        List<Item> result = new ArrayList<Item>();
        int materializationCap = this.configuration.runtime().materializationCap();
        long num = populateList(result, materializationCap);
        if (num != -1) {
            throw new CannotMaterializeException(
                    "Cannot materialize a sequence of "
                            + num
                            + " items because the limit is set to "
                            + materializationCap
                            + ". This value can be configured with the --materialization-cap parameter at startup",
                    ExceptionMetadata.EMPTY_METADATA);
        }
        this.cachedItems = new ArrayList<Item>(result);
        return new ArrayList<Item>(this.cachedItems);
    }

    /**
     * Serializes the query result to a string using the serialization parameters carried by the
     * runtime static context.
     *
     * This is intended for API and test harness use when the result must be observed exactly as a
     * serialized sequence rather than as raw {@link Item} objects.
     *
     * @return the serialized result.
     */
    public String serialize() {
        if (this.availableAsPUL()) {
            return "";
        }
        if (this.isOpen) {
            throw new RuntimeException("Cannot serialize a sequence if the iterator is open.");
        }

        SerializationParameters params =
                SerializationParameters.copy(this.getRuntimeStaticContext().getSerializationParameters());
        SerializationParameters itemParams = SerializationParameters.copy(params);
        if ("xml".equalsIgnoreCase(params.getMethod())) {
            itemParams.setOmitXmlDeclaration(true);
        }
        Serializer serializer = Serializers.from(itemParams);
        String itemSeparator = params.getItemSeparator();
        if (itemSeparator == null) {
            itemSeparator = "adaptive".equalsIgnoreCase(params.getMethod()) ? "\n" : "";
        }

        StringBuilder sb = new StringBuilder();
        List<Item> items = this.getAsList();
        if ("xml".equalsIgnoreCase(params.getMethod()) && !params.getOmitXmlDeclaration() && !items.isEmpty()) {
            SerializerUtils.appendXmlDeclaration(sb, params);
        }
        if ("json".equalsIgnoreCase(params.getMethod())) {
            if (items.isEmpty()) {
                return "null";
            }
            if (items.size() > 1) {
                throw new RumbleException(
                        "JSON serialization requires the top-level sequence to contain at most one item.",
                        new ErrorCode(new Name(Name.ERROR_NS, "err", "SERE0023")),
                        ExceptionMetadata.EMPTY_METADATA);
            }
        }
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(itemSeparator);
            }
            sb.append(serializer.serialize(items.get(i)));
        }
        return sb.toString();
    }

    /**
     * Outputs the results as a list. If there are more items than the allowed materialization limit,
     * then the list is incomplete and no error is thrown.
     *
     * @return The list of items in the sequence, possibly capped.
     */
    public List<Item> getFirstItemsAsList(int maxNumberOfItems) {
        if (this.availableAsPUL()) {
            return new ArrayList<>();
        }
        return maxNumberOfItems == 0
                ? this.plan.materialize(this.dynamicContext)
                : this.plan.materializeAtMost(this.dynamicContext, maxNumberOfItems);
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
        if (this.executionMode().isRDDOrDataFrame()) {
            long count = -1;
            JavaRDD<Item> rdd = this.plan.getRDD(this.dynamicContext);
            List<Item> result = rdd.take(maxNumberOfItems + 1);
            if (result.size() == maxNumberOfItems + 1) {
                count = rdd.count();
            }
            result.stream().limit(maxNumberOfItems).collect(Collectors.toCollection(() -> resultList));
            return count;
        }
        try (Cursor<Item> localCursor = this.plan.getCursor(this.dynamicContext)) {
            int itemCount = 0;
            while (localCursor.hasNext()
                    && ((itemCount < maxNumberOfItems && maxNumberOfItems > 0) || maxNumberOfItems == 0)) {
                resultList.add(localCursor.next());
                itemCount++;
            }
            return localCursor.hasNext() && itemCount == maxNumberOfItems ? Long.MAX_VALUE : -1;
        }
    }

    private ExecutionMode executionMode() {
        return this.plan.getRuntimeStaticContext().getExecutionMode();
    }

    /**
     * Returns a SequenceWriter to save the sequence in various formats.
     */
    public SequenceWriter write() {
        return new SequenceWriter(this);
    }
}
