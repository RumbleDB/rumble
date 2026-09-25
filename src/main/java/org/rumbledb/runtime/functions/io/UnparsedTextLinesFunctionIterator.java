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
package org.rumbledb.runtime.functions.io;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.parsing.StringToStringItemMapper;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;

public class UnparsedTextLinesFunctionIterator extends ItemRuntimePlan implements RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\r\n|\r|\n");

    public UnparsedTextLinesFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        ItemRuntimePlan hrefIterator = this.getChild(0);
        Item hrefItem = hrefIterator.materializeFirstOrNull(context);
        if (hrefItem == null) {
            return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
        }
        String encoding = null;
        if (this.getChildren().size() == 2) {
            Item encodingItem = this.getChild(1).materializeFirstOrNull(context);
            encoding = encodingItem.getStringValue();
        }

        String text = UnparsedTextReader.read(
                this.staticContext.getStaticURI(),
                hrefItem.getStringValue(),
                encoding,
                getConfiguration().semantics().xmlVersion(),
                getMetadata());

        String[] split = LINE_SPLIT_PATTERN.split(text, -1);
        List<String> lines = new ArrayList<>(split.length);
        int last = split.length - 1;
        for (int i = 0; i < split.length; i++) {
            if (i == last && split[i].isEmpty()) {
                continue;
            }
            lines.add(split[i]);
        }

        return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(lines)
                .mapPartitions(new StringToStringItemMapper());
    }
}
