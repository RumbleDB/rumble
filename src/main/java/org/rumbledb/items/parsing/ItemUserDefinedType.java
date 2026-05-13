package org.rumbledb.items.parsing;

import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.UserDefinedType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

public class ItemUserDefinedType extends UserDefinedType<Item> {

    DataFrameContext dataFrameContext;
    public void initialize() {
       this.dataFrameContext = new DataFrameContext();
    }

  @Override
  public DataType sqlType() {
    return DataTypes.BinaryType;
  }

  @Override
  public InternalRow serialize(Item item) {
    InternalRow row = new GenericInternalRow(1);
    if(dataFrameContext == null)
    {
        initialize();
    }
    this.dataFrameContext.getOutput().clear();
    this.dataFrameContext.getKryo().writeClassAndObject(this.dataFrameContext.getOutput(), item);
    byte[] s = this.dataFrameContext.getOutput().toBytes();
    row.update(0, s);
    return row;
  }

  @Override
  public Item deserialize(Object object) {
    if(object instanceof byte[] b) {
        this.dataFrameContext.getInput().setBuffer(b);
        Object result = this.dataFrameContext.getKryo().readClassAndObject(this.dataFrameContext.getInput());
        if (result instanceof Item) {
            return (Item) result;
        }
        throw new OurBadException("Expected an item but got " + result.getClass());
    }
    throw new OurBadException("Expected a byte array but got " + object.getClass());
}

  @Override
  public Class<Item> userClass() {
    return Item.class;
  }
}