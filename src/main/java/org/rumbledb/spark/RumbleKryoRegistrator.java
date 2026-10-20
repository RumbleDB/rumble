/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.spark;

import java.util.ArrayList;

import org.apache.parquet.format.IntType;
import org.apache.spark.serializer.KryoRegistrator;
import org.apache.spark.sql.types.BooleanType;
import org.apache.spark.sql.types.DoubleType;
import org.apache.spark.sql.types.FloatType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.SourcePosition;
import org.rumbledb.exceptions.SourceRange;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.AnyURIItem;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.Base64BinaryItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DateItem;
import org.rumbledb.items.DateTimeItem;
import org.rumbledb.items.DateTimeStampItem;
import org.rumbledb.items.DayTimeDurationItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.FloatItem;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.HexBinaryItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.QNameItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.items.TimeItem;
import org.rumbledb.items.YearMonthDurationItem;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.CommentItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.NamespaceItem;
import org.rumbledb.items.xml.NodeTypedValue;
import org.rumbledb.items.xml.ProcessingInstructionItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.flwor.tuple.FlworKey;
import org.rumbledb.runtime.flwor.tuple.FlworTuple;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;

/**
 * Registers the classes that RumbleDB serializes with Kryo.
 *
 * The same registrator is used by Spark's serializer (through {@code spark.kryo.registrator}) and by the Kryo
 * instances that RumbleDB creates itself (see {@link #newKryo()}), so that both register the same classes in the same
 * order.
 */
public class RumbleKryoRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {
        // Kryo 4's FieldSerializer cannot construct Java records on recent JDKs.
        kryo.register(SourcePosition.class, new JavaSerializer());
        kryo.register(SourceRange.class, new JavaSerializer());

        kryo.register(Item.class);
        kryo.register(AnnotatedItem.class);
        kryo.register(ArrayItem.class);
        kryo.register(ObjectItem.class);

        kryo.register(AnyURIItem.class);
        kryo.register(Base64BinaryItem.class);
        kryo.register(BooleanItem.class);
        kryo.register(DateItem.class);
        kryo.register(DateTimeItem.class);
        kryo.register(DateTimeStampItem.class);
        kryo.register(DayTimeDurationItem.class);
        kryo.register(DecimalItem.class);
        kryo.register(DoubleItem.class);
        kryo.register(DurationItem.class);
        kryo.register(FloatItem.class);
        kryo.register(HexBinaryItem.class);
        kryo.register(IntegerItem.class);
        kryo.register(IntItem.class);
        kryo.register(NullItem.class);
        kryo.register(QNameItem.class);
        kryo.register(StringItem.class);
        kryo.register(TimeItem.class);
        kryo.register(YearMonthDurationItem.class);

        kryo.register(DocumentItem.class);
        kryo.register(ElementItem.class);
        kryo.register(AttributeItem.class);
        kryo.register(NamespaceItem.class);
        kryo.register(CommentItem.class);
        kryo.register(ProcessingInstructionItem.class);
        kryo.register(TextItem.class);
        kryo.register(NodeTypedValue.class);
        kryo.register(NodeTypedValue.State.class);
        kryo.register(XMLDocumentPosition.class);

        kryo.register(FunctionItem.class);
        kryo.register(FunctionIdentifier.class);
        kryo.register(Name.class);
        kryo.register(SequenceType.class);
        kryo.register(SequenceType.Arity.class);
        kryo.register(SequenceCardinality.class);
        kryo.register(ItemType.class);

        kryo.register(ArrayList.class);
        kryo.register(RumbleConfiguration.class);
        kryo.register(DynamicContext.class);
        kryo.register(FlworTuple.class);
        kryo.register(FlworKey.class);
        kryo.register(RuntimePlan.class);
        kryo.register(TupleRuntimePlan.class);

        kryo.register(StructType.class);
        kryo.register(StructType[].class);
        kryo.register(StructField.class);
        kryo.register(StructField[].class);
        kryo.register(BooleanType.class);
        kryo.register(DoubleType.class);
        kryo.register(FloatType.class);
        kryo.register(IntType.class);
        // URI and immutable map internals need their Java serialization contracts.
        kryo.register(ResourceResolver.class, new JavaSerializer());
    }

    /**
     * Creates a Kryo instance configured like Spark's: with RumbleDB's class loader, without requiring constructors
     * without arguments, and with the classes of this registrator.
     *
     * @return a new Kryo instance.
     */
    public static Kryo newKryo() {
        Kryo kryo = new Kryo();
        // Under spark-submit, the RumbleDB jar is not visible to the system class loader that Kryo uses by default,
        // so classes that are not registered could not be loaded by name.
        kryo.setClassLoader(RumbleKryoRegistrator.class.getClassLoader());
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.setReferences(true);
        new RumbleKryoRegistrator().registerClasses(kryo);
        return kryo;
    }
}
