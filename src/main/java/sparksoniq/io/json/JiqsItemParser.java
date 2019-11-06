/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.io.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.javalang.typed;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;

public class JiqsItemParser implements Serializable {


	private static final long serialVersionUID = 1L;

	public static Item getItemFromObject(JsonIterator object, IteratorMetadata metadata) {
        try {
            if (object.whatIsNext().equals(ValueType.STRING))
                return ItemFactory.getInstance().createStringItem(object.readString());
            if (object.whatIsNext().equals(ValueType.NUMBER))
            {
                String number = object.readNumberAsString();
                if(number.contains("E") || number.contains("e"))
                {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if(number.contains("."))
                {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number));
            }
            if (object.whatIsNext().equals(ValueType.BOOLEAN))
                return ItemFactory.getInstance().createBooleanItem(object.readBoolean());
            if (object.whatIsNext().equals(ValueType.ARRAY)) {
                List<Item> values = new ArrayList<Item>();
                while(object.readArray())
                {
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance().createArrayItem(values);
            }
            if (object.whatIsNext().equals(ValueType.OBJECT)) {
                List<String> keys = new ArrayList<String>();
                List<Item> values = new ArrayList<Item>();
                String s = null;
                while((s = object.readObject()) != null)
                {
                    keys.add(s);
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
            }
            if (object.whatIsNext().equals(ValueType.NULL)) {
                object.readNull();
                return ItemFactory.getInstance().createNullItem();
            }
            throw new SparksoniqRuntimeException("Invalid value found while parsing. JSON is not well-formed!");
        } catch (IOException e)
        {
            throw new SparksoniqRuntimeException("IO error while parsing. JSON is not well-formed!");
        }
    }

	public static Item getItemFromRow(Row row, IteratorMetadata metadata) {
		List<String> keys = new ArrayList<String>();
		List<Item> values = new ArrayList<Item>();
		StructType schema = row.schema();
		String[] fieldnames = schema.fieldNames();
		StructField[] fields = schema.fields();
		for(int i = 0; i < fieldnames.length; ++i) {
			StructField field = fields[i];
			DataType fieldType = field.dataType();
			keys.add(field.name());
			addValue(row, i, fieldType, values, metadata);
		}
		return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
	}
	
	public static void addValue(Row row, int i, DataType fieldType, List<Item> values, IteratorMetadata metadata)
	{
		if(row.isNullAt(i)) {
			values.add(ItemFactory.getInstance().createNullItem());
		} else if(fieldType.equals(DataTypes.StringType)) {
			String s = row.getString(i);
			values.add(ItemFactory.getInstance().createStringItem(s));
		} else if(fieldType.equals(DataTypes.BooleanType)) {
			boolean b = row.getBoolean(i);
			values.add(ItemFactory.getInstance().createBooleanItem(b));
		} else if(fieldType.equals(DataTypes.DoubleType)) {
			double value = row.getDouble(i);
			values.add(ItemFactory.getInstance().createDoubleItem(value));
		} else if(fieldType.equals(DataTypes.IntegerType)) {
			int value = row.getInt(i);
			values.add(ItemFactory.getInstance().createIntegerItem(value));
		} else if(fieldType.equals(DataTypes.FloatType)) {
			float value = row.getFloat(i);
			values.add(ItemFactory.getInstance().createDoubleItem(value));
		} else if(fieldType.equals(DataTypes.LongType)) {
			BigDecimal value = new BigDecimal(row.getLong(i));
			values.add(ItemFactory.getInstance().createDecimalItem(value));
		} else if(fieldType.equals(DataTypes.NullType)) {
			values.add(ItemFactory.getInstance().createNullItem());
		} else if(fieldType.equals(DataTypes.ShortType)) {
			short value = row.getShort(i);
			values.add(ItemFactory.getInstance().createIntegerItem(value));
		} else if(fieldType.equals(DataTypes.TimestampType)) {
			Timestamp value = row.getTimestamp(i);
			values.add(ItemFactory.getInstance().createStringItem(value.toString()));
		} else if(fieldType instanceof StructType) {
			Row value = row.getStruct(i);
			values.add(getItemFromRow(value, metadata) );
		} else if(fieldType instanceof ArrayType) {
			ArrayType arrayType = (ArrayType)fieldType;
			DataType dataType = arrayType.elementType();
			List<Item> members = new ArrayList<Item>();
			List<Object> objects = row.getList(i);
			for ( int j = 0; j < objects.size(); ++j)
			{
				addValueInArray(objects.get(j), dataType, members, metadata);
			}
			values.add(ItemFactory.getInstance().createArrayItem(members));
		} else
		{
			throw new RuntimeException("DataFrame type unsupported: " + fieldType.json());
		}
	}
	
	public static void addValueInArray(Object o, DataType fieldType, List<Item> values, IteratorMetadata metadata)
	{
		if(o == null) {
			values.add(ItemFactory.getInstance().createNullItem());
		} else if(fieldType.equals(DataTypes.StringType)) {
			String s = (String) o;
			values.add(ItemFactory.getInstance().createStringItem(s));
		} else if(fieldType.equals(DataTypes.BooleanType)) {
			boolean b = ((Boolean)o).booleanValue();
			values.add(ItemFactory.getInstance().createBooleanItem(b));
		} else if(fieldType.equals(DataTypes.DoubleType)) {
			double value = ((Double)o).doubleValue();
			values.add(ItemFactory.getInstance().createDoubleItem(value));
		} else if(fieldType.equals(DataTypes.IntegerType)) {
			int value = ((Integer)o).intValue();
			values.add(ItemFactory.getInstance().createIntegerItem(value));
		} else if(fieldType.equals(DataTypes.FloatType)) {
			float value = ((Float)o).floatValue();
			values.add(ItemFactory.getInstance().createDoubleItem(value));
		} else if(fieldType.equals(DataTypes.LongType)) {
			BigDecimal value = new BigDecimal(((Long)o).longValue());
			values.add(ItemFactory.getInstance().createDecimalItem(value));
		} else if(fieldType.equals(DataTypes.NullType)) {
			values.add(ItemFactory.getInstance().createNullItem());
		} else if(fieldType.equals(DataTypes.ShortType)) {
			short value = ((Short)o).shortValue();
			values.add(ItemFactory.getInstance().createIntegerItem(value));
		} else if(fieldType.equals(DataTypes.TimestampType)) {
			Timestamp value = (Timestamp)o;
			values.add(ItemFactory.getInstance().createStringItem(value.toString()));
		} else if(fieldType instanceof StructType) {
			Row value = (Row)o;
			values.add(getItemFromRow(value, metadata) );
		} else if(fieldType instanceof ArrayType) {
			ArrayType arrayType = (ArrayType)fieldType;
			DataType dataType = arrayType.elementType();
			List<Item> members = new ArrayList<Item>();
			List<Object> objects = (List<Object>)o;
			addValueInArray(objects, fieldType, members, metadata);
			values.add(ItemFactory.getInstance().createArrayItem(members));
		} else {
			throw new RuntimeException("DataFrame type unsupported: " + fieldType.json());
		}
	}
}
