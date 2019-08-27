package sparksoniq.spark.udf;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.util.AccumulatorV2;

import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class OrderByAccumulator extends AccumulatorV2<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	private int _flags;
	private IteratorMetadata _metadata;
		
	private static List<OrderByAccumulator> _accumulators = new ArrayList<OrderByAccumulator>();
	
	public static OrderByAccumulator createNewAccumulator(IteratorMetadata metadata) {
		OrderByAccumulator a = new OrderByAccumulator(metadata);
		_accumulators.add(a);
		return a;
	}
	
	public static void checkForTypeErrors() {
		for(OrderByAccumulator a : _accumulators)
		{
			if(a._flags != 0 && a._flags != 1 && a._flags != 2 && a._flags != 4)
			{
				_accumulators.clear();
				throw new UnexpectedTypeException("Incompatible types in order by clause. Flags: " + a._flags, a._metadata);
			}
		}
		_accumulators.clear();
	}
	
	private OrderByAccumulator(IteratorMetadata metadata)
	{
		_flags = 0;
		_metadata = metadata;
	}

	private OrderByAccumulator(int flags, IteratorMetadata metadata)
	{
		_flags = flags;
		_metadata = metadata;
	}

	@Override
	public void add(Integer v) {
		_flags |= (1 << v.intValue());
	}

	@Override
	public AccumulatorV2<Integer, Integer> copy() {
		return new OrderByAccumulator(_flags, _metadata);
	}

	@Override
	public boolean isZero() {
		return _flags == 0;
	}

	@Override
	public void merge(AccumulatorV2<Integer,Integer> other) {
		if(!(other instanceof OrderByAccumulator))
		{
			throw new RuntimeException ("Unexpected accumulator, expecting OrderByAccumulator");
		}
		_flags |= ((OrderByAccumulator)other)._flags;
	}

	@Override
	public void reset() {
		_flags = 0;
	}

	@Override
	public Integer value() {
		return _flags;
	}
}
