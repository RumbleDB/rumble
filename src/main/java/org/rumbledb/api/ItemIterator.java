package org.rumbledb.api;

import org.apache.spark.api.java.JavaRDD;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;

/**
 * An ItemIterator iterates on a sequence of items, but it also allows obtaining this sequence as an RDD of Items if the sequence is too big
 * to be collected locally.
 * 
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class ItemIterator {
	
	private RuntimeIterator _iterator;
	private boolean _isOpen;
	
	protected ItemIterator(RuntimeIterator iterator)
	{
		_iterator = iterator;
		_isOpen = false;
	}
	
	/**
	 * Opens the iterator.
	 */
	public void open()
	{
		_iterator.open(new DynamicContext());
		_isOpen = true;
	}
	
	/**
	 * Checks whether the iterator is open.
	 * @return true if it is open, false if it is closed.
	 */
	public boolean isOpen() {
		return _isOpen;
	}

	/**
	 * Closes the iterator.
	 */
	public void close()
	{
		_iterator.close();
		_isOpen = false;
	}

	/**
	 * Checks whether there are more items.
	 * @return true if there are more items, false otherwise.
	 */
	public boolean hasNext()
	{
		return _iterator.hasNext();
	}

	/**
	 * Returns the current item and moves on to the next one.
	 * @return the next item.
	 */
	public Item next()
	{
		return _iterator.next();
	}
	
	/**
	 * Says whether the iterator is available as an RDD of Items for further processing without having to collect.
	 * @return true if it is available as an RDD of Items.
	 */
	public boolean availableAsRDD()
	{
		return _iterator.isRDD();
	}

	/**
	 * Returns the sequence of items as an RDD of Items rather than iterating over them locally.
	 * @return an RDD of Items.
	 */
	public JavaRDD<Item> getAsRDD()
	{
		if(_isOpen)
		{
			throw new RuntimeException("Cannot obtain an RDD if the iterator is open.");
		}
		return _iterator.getRDD(new DynamicContext());
	}

}
