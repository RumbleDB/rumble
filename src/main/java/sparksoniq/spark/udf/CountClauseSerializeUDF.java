package sparksoniq.spark.udf;

import org.apache.spark.sql.api.java.UDF1;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.spark.DataFrameUtils;

import java.util.ArrayList;
import java.util.List;

public class CountClauseSerializeUDF implements UDF1<Long, byte[]> {

    private List<Item> _nextResult;

    public CountClauseSerializeUDF() {
        _nextResult = new ArrayList<>();
    }

    @Override
    public byte[] call(Long countIndex) {
        _nextResult.clear();
        _nextResult.add(new IntegerItem(countIndex.intValue()));

        return DataFrameUtils.serializeItemList(_nextResult);
    }
}
