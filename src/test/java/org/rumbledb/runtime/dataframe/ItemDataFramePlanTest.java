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
package org.rumbledb.runtime.dataframe;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.sequences.aggregate.CountFunctionIterator;
import org.rumbledb.runtime.plan.ItemDataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.SequenceType;

public class ItemDataFramePlanTest {
    @Test
    public void heterogeneousItemsSurviveAllPlanBoundaries() {
        SparkSessionManager manager = SparkSessionManager.getInstance();
        manager.resetSession();
        manager.initializeConfigurationAndSession(
                new SparkConf()
                        .setMaster("local[2]")
                        .setAppName("ItemDataFramePlanTest")
                        .set("spark.driver.host", "127.0.0.1"),
                true);
        try {
            RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
            RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                    .configuration(configuration)
                    .executionMode(ExecutionMode.RDD)
                    .staticType(new SequenceType(
                            org.rumbledb.types.BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))
                    .metadata(ExceptionMetadata.EMPTY_METADATA)
                    .build();
            DynamicContext context = new DynamicContext(configuration);
            ItemRuntimePlan plan = new MixedItemsPlan(staticContext);
            ItemRuntimePlan local = new org.rumbledb.runtime.ConstantRuntimeIterator(
                    ItemFactory.getInstance().createStringItem("local"),
                    staticContext.toBuilder().executionMode(ExecutionMode.LOCAL).build());
            assertEquals(
                    "local",
                    local.getItemDataFrame(context).first().<Item>getAs("item").getStringValue());
            assertEquals(
                    0,
                    SerializedItemDataFrame.fromRDD(
                                    manager.getJavaSparkContext().emptyRDD())
                            .count());
            List<Item> items = plan.materialize(context);
            assertEquals(3, items.size());
            assertEquals(42, items.get(0).getIntValue());
            assertEquals("hello", items.get(1).getStringValue());
            assertTrue(items.get(2).isArray());
            assertEquals(3, plan.getItemDataFrame(context).count());
            assertEquals(3, plan.getRDD(context).count());
            assertEquals(
                    3,
                    CountFunctionIterator.computeCount(plan, context, ExceptionMetadata.EMPTY_METADATA)
                            .getLongValue());
            try (var cursor = plan.getCursor(context)) {
                assertEquals(42, cursor.next().getIntValue());
            }
        } finally {
            manager.resetSession();
        }
    }

    private static final class MixedItemsPlan extends ItemRuntimePlan implements ItemDataFrameRuntimePlan {
        MixedItemsPlan(RuntimeStaticContext context) {
            super(List.of(), context);
        }

        public Dataset<Row> createNativeItemDataFrame(DynamicContext context) {
            ItemFactory factory = ItemFactory.getInstance();
            List<Item> items = List.of(
                    factory.createIntItem(42),
                    factory.createStringItem("hello"),
                    factory.createArrayItem(new ArrayList<>(List.of(factory.createBooleanItem(true))), false));
            return SerializedItemDataFrame.fromRDD(
                    SparkSessionManager.getInstance().getJavaSparkContext().parallelize(items, 2));
        }
    }
}
