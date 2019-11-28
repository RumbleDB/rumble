package org.rumbledb.api;

import org.apache.spark.api.java.JavaRDD;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;

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

    private RuntimeIterator _iterator;
    private boolean _isOpen;

    protected SequenceOfItems(RuntimeIterator iterator) {
        _iterator = iterator;
        _isOpen = false;
    }

    /**
     * Opens the iterator.
     */
    public void open() {
        _iterator.open(new DynamicContext());
        _isOpen = true;
    }

    /**
     * Checks whether the iterator is open.
     * 
     * @return true if it is open, false if it is closed.
     */
    public boolean isOpen() {
        return _isOpen;
    }

    /**
     * Closes the iterator.
     */
    public void close() {
        _iterator.close();
        _isOpen = false;
    }

    /**
     * Checks whether there are more items.
     * 
     * @return true if there are more items, false otherwise.
     */
    public boolean hasNext() {
        return _iterator.hasNext();
    }

    /**
     * Returns the current item and moves on to the next one. The number of items the iterator can returned is capped by
     * Spark's settings (collect-item-limit).
     * 
     * @return the next item.
     */
    public Item next() {
        return _iterator.next();
    }

    /**
     * Checks whether the iterator is available as an RDD of Items for further processing without having to collect.
     * 
     * @return true if it is available as an RDD of Items.
     */
    public boolean availableAsRDD() {
        return _iterator.isRDD(new DynamicContext());
    }

    /**
     * Returns the sequence of items as an RDD of Items rather than iterating over them locally.
     * It is not possible to do so if the iterator is open.
     * 
     * @return an RDD of Items.
     */
    public JavaRDD<Item> getAsRDD() {
        if (_isOpen) {
            throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
        }
        return _iterator.getRDD(new DynamicContext());
    }

}
